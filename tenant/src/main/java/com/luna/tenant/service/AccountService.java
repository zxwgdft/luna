package com.luna.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.luna.constant.GlobalConstants;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.service.ServiceSupport;
import com.luna.framework.utils.UUIDUtil;
import com.luna.framework.utils.secure.SecureUtil;
import com.luna.tenant.api.AccountCreate;
import com.luna.tenant.api.AccountDelete;
import com.luna.tenant.api.AccountUpdate;
import com.luna.tenant.api.PasswordUpdate;
import com.luna.tenant.mapper.AccountMapper;
import com.luna.tenant.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户账号是指通过userId关联了用户的一类账号，他们有具体指向用户数据，例如员工等
 * 非用户账号是指没有关联用户的一类账号，如业务需要他们可以扩展为用户账号，例如系统管理员，租户管理员等
 */
@Service
@RequiredArgsConstructor
public class AccountService extends ServiceSupport<Account, AccountMapper> {

    @Value("${auth.default-password:1}")
    private String defaultPassword;

    @Value("${auth.default-password-random:false}")
    private boolean randomPassword;

    private final DynamicHisServlet dynamicHisServlet;
    private final WebSecurityManager webSecurityManager;
    private final TenantService tenantService;

    private Pattern accountPattern = Pattern.compile("^\\w{6,30}$");
    private Pattern passwordPattern = Pattern.compile("^\\w{6,20}$");


    /**
     * 创建用户账号
     */
    public void createUserAccount(AccountCreate accountCreate) {
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
     * 更新用户账号和密码
     */
    public void updateUserAccount(AccountUpdate accountUpdate) {
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
     * 更新用户登录人密码
     */
    public void updateUserPassword(PasswordUpdate passwordUpdate) {
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

        int effect = getSqlMapper().update(null,
                new LambdaUpdateWrapper<Account>()
                        .eq(Account::getId, user.getId())
                        .set(Account::getSalt, salt)
                        .set(Account::getPassword, newPassword)
        );

        if (effect > 0) {
            return;
        }

        throw new BusinessException("密码修改失败");
    }

    /**
     * 根据关联用户类型和ID删除账号
     */
    public boolean deleteUserAccount(AccountDelete accountDelete) {
        return getSqlMapper().delete(new LambdaQueryWrapper<Account>()
                .eq(Account::getUserId, accountDelete.getUserId())
                .eq(Account::getType, accountDelete.getType())) > 0;
    }

    /**
     * 重置员工用户密码
     *
     * @param employeeId
     * @return
     */
    public String resetEmployeePassword(long employeeId) {
        Account user = getAccountByUser(employeeId, GlobalConstants.USER_TYPE_EMPLOYEE);
        String salt = SecureUtil.createSalt();
        String password = getDefaultPassword();
        String hashPassword = SecureUtil.hashByMD5(password, salt);

        int effect = getSqlMapper().update(null,
                new LambdaUpdateWrapper<Account>()
                        .eq(Account::getId, user.getId())
                        .set(Account::getSalt, salt)
                        .set(Account::getPassword, hashPassword)
        );

        if (effect > 0) {
            return password;
        }

        throw new BusinessException("重置密码失败");
    }

    private String getDefaultPassword() {
        if (randomPassword) {
            return UUIDUtil.createUUID().substring(0, 10);
        }
        return defaultPassword;
    }

}
