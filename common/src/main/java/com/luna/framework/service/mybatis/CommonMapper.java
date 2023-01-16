package com.luna.framework.service.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 基于mybatis plus扩展出自己的通用Mapper
 *
 * @author TontoZhou
 * @since 2021/4/9
 */
public interface CommonMapper<Model> extends BaseMapper<Model> {

    String METHOD_SELECT_WHOLE_BY_ID = "selectWholeById";
    String METHOD_UPDATE_WHOLE_BY_ID = "updateWholeById";
    String METHOD_SELECT_SIMPLE_LIST = "selectSimpleList";
    String METHOD_UPDATE_SELECTION_BY_ID = "updateSelectionById";
    String METHOD_DELETE_SAFE_BY_ID = "deleteSafeById";

    String PARAM_ID = "id";
    String PARAM_TENANT_ID = "tenantId";



    /**
     * 更新对象所有字段（updateById默认情况下只更新非null字段，该方法主要弥补某些场景需要更新null的字段）
     *
     * @param model 更新对象
     * @return 影响条数
     */
    int updateWholeById(@Param(Constants.ENTITY) Model model);

    /**
     * 更新对象字段（不为null）
     *
     * @param model 更新对象
     * @return 影响条数
     */
    int updateSelectionById(@Param(Constants.ENTITY) Model model);

    /**
     * 查询对象所有字段（某些场景下我需要获取标明了不查询的字段，该方法辅助selectById方法）
     *
     * @param id 主键ID
     * @return 数据对象
     */
    Model selectWholeById(Serializable id);

    /**
     * 根据 entity 条件，查询全部记录（部分字段，标注了简单展示的字段）
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @return 部分字段的数据对象集合
     */
    List<Model> selectSimpleList(@Param(Constants.WRAPPER) Wrapper<Model> queryWrapper);

    /**
     * 根据ID和租户更安全的删除
     * <p>
     * 该方法应该放在his模块下，但是由于许多Mapper都基于了CommonMapper，改动量大，所以直接定义在这里，非his模块可忽略该方法
     *
     * @param id       主键ID
     * @param tenantId 租户ID
     */
    int deleteSafeById(@Param(PARAM_ID) Serializable id, @Param(PARAM_TENANT_ID) Long tenantId);

}
