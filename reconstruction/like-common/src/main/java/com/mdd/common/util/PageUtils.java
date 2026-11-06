package com.mdd.common.util;

/**
 * 列表分页参数收口。
 *
 * <p>小程序与后台自写分页接口统一使用：pageNo 最小 1；pageSize 缺省 20、上限 60，
 * 避免客户端传超大值导致全表返回或内存分页放大。默认值/上限与后台
 * {@code PageValidate} 的 {@code @DecimalMax(60)} 保持一致。</p>
 */
public final class PageUtils {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 60;

    private PageUtils() {
    }

    public static int pageNo(Integer pageNo) {
        return pageNo == null || pageNo < 1 ? 1 : pageNo;
    }

    public static int pageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    public static int offset(int pageNo, int pageSize) {
        return (pageNo(pageNo) - 1) * pageSize(pageSize);
    }
}
