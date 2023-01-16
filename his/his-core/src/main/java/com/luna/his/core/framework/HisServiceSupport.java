package com.luna.his.core.framework;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luna.framework.api.SystemException;
import com.luna.framework.security.WebSecurityManager;
import com.luna.framework.service.ServiceSupport;
import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.his.core.BaseModel;
import com.luna.his.core.HisConstants;
import com.luna.his.core.HisUserSession;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 基于HIS的辅助类
 *
 * @author TontoZhou
 */
@Slf4j
public class HisServiceSupport<Model, Mapper extends CommonMapper<Model>> extends ServiceSupport<Model, Mapper> {

    protected boolean isBaseModel;


    @Override
    protected void init() {
        // 获取是否基于BaseModel
        isBaseModel = BaseModel.class.isAssignableFrom(modelType);
    }

    protected HisUserSession getHisUserSession() {
        return (HisUserSession) WebSecurityManager.getCurrentUserSession();
    }

    @Override
    protected Wrapper buildCommon(Wrapper queryWrapper) {
        if (isBaseModel) {
            HisUserSession userSession = getHisUserSession();
            if (userSession != null) {
                if (queryWrapper == null) {
                    queryWrapper = new QueryWrapper();
                }
                if (queryWrapper instanceof LambdaQueryWrapper) {
                    ((LambdaQueryWrapper<BaseModel>) queryWrapper).eq(BaseModel::getTenantId, userSession.getTenantId());
                } else if (queryWrapper instanceof QueryWrapper) {
                    ((QueryWrapper) queryWrapper).eq(true, HisConstants.COLUMN_TENANT_ID, userSession.getTenantId());
                } else {
                    log.error("不支持的Wrapper类：{}", queryWrapper.getClass());
                    throw new SystemException(SystemException.CODE_ERROR_CODE);
                }
            }
        }
        return queryWrapper;
    }

    @Override
    public void save(Model model) {
        if (isBaseModel) {
            BaseModel baseModel = (BaseModel) model;
            if (baseModel.getTenantId() == null) {
                baseModel.setTenantId(getHisUserSession().getTenantId());
            }
        }
        super.save(model);
    }

    @Override
    public boolean updateWhole(Model model) {
        return sqlMapper.updateWholeById(model) > 0;
    }

    @Override
    public boolean updateSelection(Model model) {
        if (isBaseModel) {
            // 创建时间、更新时间、租户ID不需要更新
            BaseModel baseModel = (BaseModel) model;
            baseModel.setTenantId(getHisUserSession().getTenantId());
            baseModel.setUpdateTime(null);
            baseModel.setCreateTime(null);
            return sqlMapper.updateSelectionById(model) > 0;
        }
        return sqlMapper.updateById(model) > 0;
    }

    @Override
    public boolean deleteById(Serializable id) {
        if (isBaseModel) {
            return sqlMapper.deleteSafeById(id, getHisUserSession().getTenantId()) > 0;
        } else {
            return sqlMapper.deleteById(id) > 0;
        }
    }
}
