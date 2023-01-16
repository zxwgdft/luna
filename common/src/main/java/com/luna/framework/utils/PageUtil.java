package com.luna.framework.utils;

import com.github.pagehelper.Page;
import com.luna.framework.api.PageResult;

public class PageUtil {

    public static <T> PageResult<T> toPageResult(Page<T> page) {
        if (page == null) {
            return new PageResult<>();
        }
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), page.getResult());
    }

}
