package com.luna.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luna.constant.GlobalConstants;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.service.ServiceSupport;
import com.luna.framework.utils.StringUtil;
import com.luna.framework.utils.WebUtil;
import com.luna.framework.utils.secure.SecureUtil;
import com.luna.his.api.AuthenticatedUser;
import com.luna.his.api.InternalRequestPath;
import com.luna.tenant.api.*;
import com.luna.tenant.mapper.AccountMapper;
import com.luna.tenant.model.Account;
import com.his.tenant.api.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccountService extends ServiceSupport<Account, AccountMapper> {

    private final DynamicHisServlet dynamicHisServlet;

    private Pattern accountPattern = Pattern.compile("^\\w{6,30}$");
    private Pattern passwordPattern = Pattern.compile("^\\w{6,20}$");

    /**
     * 通过账号密码认证并返回登录结果
     *
     * @param passwordToken 密码凭证
     */
    public LoginResult loginByPassword(PasswordToken passwordToken, HttpServletRequest request) {
        Account account = authAccountByPassword(passwordToken);
        // 认证成功
        int type = account.getType();
        if (GlobalConstants.USER_TYPE_EMPLOYEE == type) {
            AuthenticatedUser authenticatedUser = new AuthenticatedUser();
            authenticatedUser.setEmployeeId(account.getUserId());
            authenticatedUser.setRememberMe(passwordToken.isRememberMe());
            // TODO ip是否还需要验证
            authenticatedUser.setIp(WebUtil.getIpAddress(request));
            authenticatedUser.setDeviceId("");
            authenticatedUser.setDeviceCode("");
            authenticatedUser.setRememberMe(passwordToken.isRememberMe());

            // 获取员工账号的token
            String token = dynamicHisServlet.postJsonRequest(account.getServer(),
                    InternalRequestPath.GET_USER_TOKEN
                    , authenticatedUser, String.class);

            return new LoginResult(token, account.getServer(), type);
        } else {
            // 其他账号用户还未实现
            throw new BusinessException("暂时不支持的账号用户");
        }
    }

    /**
     * 通过账号密码认证
     *
     * @param passwordToken
     * @return
     */
    public Account authAccountByPassword(PasswordToken passwordToken) {
        String username = passwordToken.getUsername();
        if (StringUtil.isNotEmpty(username)) {
            Account account = getAccountByAccount(username);
            if (account != null) {
                String password = passwordToken.getPassword();
                if (StringUtil.isNotEmpty(password) &&
                        StringUtil.equals(
                                SecureUtil.hashByMD5(password, account.getSalt()),
                                account.getPassword()
                        )
                ) {
                    if (account.getState() != Account.STATE_ENABLED) {
                        throw new BusinessException("账号已被锁定，请联系管理员解决！");
                    }
                    // 认证成功
                    return account;
                }
            }
        }
        throw new BusinessException("账号或密码错误！");
    }

    /**
     * 创建用户账号
     */
    public void createAccount(AccountCreate accountCreate) {
        String password = accountCreate.getPassword();
        if (password == null || !passwordPattern.matcher(password).matches()) {
            throw new BusinessException("密码不符合规则");
        }

        String salt = SecureUtil.createSalt();
        String hashPassword = SecureUtil.hashByMD5(password, salt);

        String account = accountCreate.getAccount();
        if (!accountPattern.matcher(account).matches()) {
            throw new BusinessException("账号不符合规则");
        }
        if (searchCount(
                new LambdaQueryWrapper<Account>().eq(Account::getAccount, account)) > 0
        ) {
            throw new BusinessException("账号已被占用");
        }

        // TODO 手机号码验证

        Account user = new Account();
        user.setAccount(account);
        user.setPassword(hashPassword);
        user.setSalt(salt);
        user.setUserId(accountCreate.getUserId());
        user.setCellphone(accountCreate.getCellphone());
        user.setType(accountCreate.getType());
        user.setTenantId(accountCreate.getTenantId());
        user.setState(Account.STATE_ENABLED);
        user.setServer(accountCreate.getServer());
        save(user);
    }

    /**
     * 更新账号和密码
     */
    public void updateAccount(AccountUpdate accountUpdate) {
        String account = accountUpdate.getAccount();
        long userId = accountUpdate.getUserId();
        int userType = accountUpdate.getType();
        String cellphone = accountUpdate.getCellphone();

        if (!accountPattern.matcher(account).matches()) {
            throw new BusinessException("账号不符合规则");
        }
        if (getSqlMapper().countOtherUserByAccount(account, userType, userId) > 0) {
            throw new BusinessException("账号已被占用");
        }

        //TODO 手机号码验证

        getSqlMapper().update(null,
                new LambdaUpdateWrapper<Account>()
                        .eq(Account::getType, userType)
                        .eq(Account::getUserId, userId)
                        .set(Account::getAccount, account)
                        .set(Account::getCellphone, cellphone)
        );
    }


    /**
     * 通过账号名查找账号
     */
    public Account getAccountByAccount(String account) {
        List<Account> users = findList(new LambdaQueryWrapper<Account>().eq(Account::getAccount, account));
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }

    /**
     * 通过用户查找账号
     */
    public Account getAccountByUser(long userId, int userType) {
        List<Account> users = findList(
                new LambdaQueryWrapper<Account>()
                        .eq(Account::getUserId, userId)
                        .eq(Account::getType, userType)
        );
        return (users != null && users.size() > 0) ? users.get(0) : null;
    }


    /**
     * 更新登录人密码
     */
    public void updatePassword(PasswordUpdate passwordUpdate) {
        String newPassword = passwordUpdate.getNewPassword();
        String oldPassword = passwordUpdate.getOldPassword();

        if (newPassword == null || !passwordPattern.matcher(newPassword).matches()) {
            throw new BusinessException("密码不符合规则");
        }

        Account user = getAccountByUser(passwordUpdate.getUserId(), passwordUpdate.getType());
        oldPassword = SecureUtil.hashByMD5(oldPassword, user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }

        String salt = SecureUtil.createSalt();
        newPassword = SecureUtil.hashByMD5(newPassword, salt);

        Account updateUser = new Account();
        updateUser.setId(user.getId());
        updateUser.setSalt(salt);
        updateUser.setPassword(newPassword);

        if (updateSelection(updateUser)) {
            //是否重新登录，jwt无法直接实现注销
            return;
        }

        throw new BusinessException("密码修改失败");
    }

    /**
     * 更新最近登录时间
     */
    public void updateLastLoginTime(String id) {
        getSqlMapper().updateLastLoginTime(id);
    }

    /**
     * 根据关联用户类型和ID删除账号
     */
    public boolean deleteAccount(AccountDelete accountDelete) {
        return getSqlMapper().delete(new LambdaQueryWrapper<Account>()
                .eq(Account::getUserId, accountDelete.getUserId())
                .eq(Account::getType, accountDelete.getType())) > 0;
    }

}