package com.luna.framework.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.luna.framework.api.PageParam;
import com.luna.framework.api.PageResult;
import com.luna.framework.service.annotation.IgnoreSelection;
import com.luna.framework.utils.reflect.Entity;
import com.luna.framework.utils.reflect.ReflectUtil;
import com.luna.framework.service.mybatis.CommonMapper;
import com.luna.framework.utils.convert.SimpleBeanCopyUtil;
import com.luna.framework.utils.reflect.EntityField;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

/**
 * 基于mybatis-plus的服务辅助类
 *
 * @author TontoZhou
 * @since 2021/3/11
 */
@Slf4j
public class ServiceSupport<Model, Mapper extends CommonMapper<Model>> {

    /**
     * 选择项缓存
     */
    private static Map<Class, String[]> selectionCacheMap = new HashMap<>();

    /**
     * 具体的业务数据模型
     */
    protected Class<Model> modelType;

    public ServiceSupport() {
        // 获取泛型类，该泛型类应该是对应数据库某表的实体类类型
        Class<?> clazz = ReflectUtil.getSuperClassArgument(this.getClass(), ServiceSupport.class, 0);
        if (clazz == null) {
            log.warn("实现类[" + this.getClass().getName() + "]没有明确定义["
                    + ServiceSupport.class.getName() + "]的泛型，无法为其注册commonMapper");
        }
        modelType = (Class<Model>) clazz;
    }

    protected void init() {
    }

    /**
     * 基于mybatis plus的commonMapper
     */
    protected Mapper sqlMapper;

    public Mapper getSqlMapper() {
        return sqlMapper;
    }

    public void setSqlMapper(Mapper sqlMapper) {
        this.sqlMapper = sqlMapper;
    }


    // -------------------------
    // save update delete
    // -------------------------

    public Model get(Serializable id) {
        return sqlMapper.selectById(id);
    }

    public Model getWhole(Serializable id) {
        return sqlMapper.selectWholeById(id);
    }

    public void save(Model model) {
        sqlMapper.insert(model);
    }

    public boolean updateWhole(Model model) {
        return sqlMapper.updateWholeById(model) > 0;
    }

    public boolean updateSelection(Model model) {
        return sqlMapper.updateById(model) > 0;
    }

    public boolean deleteById(Serializable id) {
        return sqlMapper.deleteById(id) > 0;
    }

    // -------------------------
    // select 部分
    // -------------------------

    /**
     * 构建同步查询部分
     */
    protected Wrapper buildCommon(Wrapper queryWrapper) {
        return queryWrapper;
    }

    /**
     * 构建select部分（当返回对象不为当前model）
     */
    protected Wrapper buildSelection(Wrapper queryWrapper, Class<?> clazz) {
        if (queryWrapper == null) {
            queryWrapper = new QueryWrapper();
        }
        String[] selections = selectionCacheMap.get(clazz);
        if (selections == null) {
            List<String> list = new ArrayList<>();
            for (EntityField entityField : Entity.getEntity(clazz).getEntityFields()) {
                if (entityField.getAnnotation(IgnoreSelection.class) == null) {
                    String fieldName = entityField.getName();
                    if (Entity.getEntity(modelType).getEntityField(fieldName) != null) {
                        list.add(fieldName);
                    }
                }
            }
            selections = list.toArray(new String[list.size()]);
            selectionCacheMap.put(clazz, selections);
        }

        if (queryWrapper instanceof Query) {
            ((Query) queryWrapper).select(selections);
        }
        return queryWrapper;
    }

    /**
     * 查询结果集
     * <p>
     * 会构建通用的sql条件和select部分
     *
     * @param clazz        需要返回的对象类
     * @param queryWrapper 查询条件
     * @param isSimple     是否返回简单信息
     * @return 返回查询结果集
     */
    protected <T> List<T> searchAll(Class<T> clazz, Wrapper queryWrapper, boolean isSimple) {
        queryWrapper = buildCommon(queryWrapper);
        if (modelType != clazz) {
            queryWrapper = buildSelection(queryWrapper, clazz);
            List<Model> result = isSimple ? sqlMapper.selectSimpleList(queryWrapper) : sqlMapper.selectList(queryWrapper);
            if (result != null && result.size() > 0) {
                // mybatis plus sqlMapper 没有提供改变返回对象的方法，这里
                // 使用bean copy方法，增加了一些性能损耗。
                return SimpleBeanCopyUtil.simpleCopyList(result, clazz);
            } else {
                return Collections.EMPTY_LIST;
            }
        } else {
            return isSimple ? sqlMapper.selectSimpleList(queryWrapper) : sqlMapper.selectList(queryWrapper);
        }
    }

    /**
     * 查询分页结果集
     *
     * @param clazz        需要返回的对象类
     * @param pageParam    分页参数
     * @param queryWrapper 查询条件
     * @param isSimple     是否返回简单信息
     * @return 返回查询的分页结果集
     */
    protected <T> PageResult<T> searchPage(Class<T> clazz, PageParam pageParam, Wrapper queryWrapper, boolean isSimple) {
        Page<T> page = PageHelper.offsetPage(pageParam.getOffset(), pageParam.getLimit());
        List<T> result = searchAll(clazz, queryWrapper, isSimple);
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), result);
    }

    /**
     * 查找所有结果集合
     */
    public List<Model> findList() {
        return searchAll(modelType, null, false);
    }

    /**
     * 查找所有结果集合
     */
    public List<Model> findList(Wrapper queryWrapper) {
        return searchAll(modelType, queryWrapper, false);
    }

    /**
     * 查找所有结果集合
     */
    public List<Model> findList(Object queryParam) {
        return searchAll(modelType, queryParam != null ? QueryWrapperHelper.buildQuery(queryParam) : null, false);
    }

    /**
     * 查找唯一结果（如果有多个会返回第一个结果）
     */
    public Model findOne(Wrapper queryWrapper) {
        List<Model> result = searchAll(modelType, queryWrapper, false);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * 查找唯一结果（如果有多个会返回第一个结果）
     */
    public Model findOne(Object queryParam) {
        List<Model> result = searchAll(modelType, queryParam != null ? QueryWrapperHelper.buildQuery(queryParam) : null, false);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * 查找所有结果集合（简单信息的）
     */
    public List<Model> findSimpleList() {
        return searchAll(modelType, null, true);
    }

    /**
     * 查找所有结果集合（简单信息的）
     */
    public List<Model> findSimpleList(Wrapper queryWrapper) {
        return searchAll(modelType, queryWrapper, true);
    }

    /**
     * 查找所有结果集合（简单信息的）
     */
    public List<Model> findSimpleList(Object queryParam) {
        return searchAll(modelType, queryParam != null ? QueryWrapperHelper.buildQuery(queryParam) : null, true);
    }

    /**
     * 查找分页结果集合
     */
    public PageResult<Model> findPage(PageParam pageParam) {
        return searchPage(modelType, pageParam, pageParam != null ? QueryWrapperHelper.buildQuery(pageParam) : null, false);
    }

    /**
     * 查找分页结果集合
     */
    public PageResult<Model> findPage(PageParam pageParam, Object queryParam) {
        return searchPage(modelType, pageParam, queryParam != null ? QueryWrapperHelper.buildQuery(queryParam) : null, false);
    }

    /**
     * 查找分页结果集合
     */
    public PageResult<Model> findPage(PageParam pageParam, Wrapper queryWrapper) {
        return searchPage(modelType, pageParam, queryWrapper, false);
    }

    /**
     * 查找分页结果集合（简单信息的）
     */
    public PageResult<Model> findSimplePage(PageParam pageParam) {
        return searchPage(modelType, pageParam, pageParam != null ? QueryWrapperHelper.buildQuery(pageParam) : null, true);
    }

    /**
     * 查找分页结果集合（简单信息的）
     */
    public PageResult<Model> findSimplePage(PageParam pageParam, Object queryParam) {
        return searchPage(modelType, pageParam, queryParam != null ? QueryWrapperHelper.buildQuery(queryParam) : null, true);
    }

    /**
     * 查找分页结果集合（简单信息的）
     */
    public PageResult<Model> findSimplePage(PageParam pageParam, Wrapper queryWrapper) {
        return searchPage(modelType, pageParam, queryWrapper, true);
    }

    /**
     * 查找记录数
     */
    public long findCount(Object queryParam) {
        if (queryParam != null) {
            if (queryParam instanceof Wrapper) {
                return searchCount((Wrapper) queryParam);
            } else {
                return searchCount(QueryWrapperHelper.buildQuery(queryParam));
            }
        } else {
            return searchCount(null);
        }
    }

    /**
     * 查询记录数
     *
     * @param queryWrapper 查询条件
     * @return 记录数
     */
    public long searchCount(Wrapper queryWrapper) {
        queryWrapper = buildCommon(queryWrapper);
        return sqlMapper.selectCount(queryWrapper);
    }

}
