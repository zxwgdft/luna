package com.luna.framework.service.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**
 * @author TontoZhou
 * @since 2021/4/9
 */
public class CommonSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
        list.add(new UpdateWholeById());
        list.add(new UpdateSelectionById());
        list.add(new SelectWholeById());
        list.add(new SelectSimpleList());
        list.add(new DeleteSafeById());
        return list;
    }
}
