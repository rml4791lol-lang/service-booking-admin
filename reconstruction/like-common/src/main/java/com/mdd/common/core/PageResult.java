/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.github.pagehelper.PageInfo
 */
package com.mdd.common.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

public class PageResult<T> {
    private Long count;
    private Integer page_no;
    private Integer page_size;
    private Map<String, Object> extend;
    private List<T> lists;

    public static <T> PageResult<T> pageHelper(List<T> list) {
        PageResult<T> pageResult = new PageResult<T>();
        PageInfo pageInfo = new PageInfo(list);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setPage_no(pageInfo.getPageNum());
        pageResult.setPage_size(pageInfo.getPageSize());
        pageResult.setLists(pageInfo.getList());
        return pageResult;
    }

    public static <T> PageResult<T> pageHelper(List<T> list, List<T> data) {
        PageResult<T> pageResult = new PageResult<T>();
        PageInfo pageInfo = new PageInfo(list);
        pageResult.setCount(pageInfo.getTotal());
        pageResult.setPage_no(pageInfo.getPageSize());
        pageResult.setPage_size(pageInfo.getPageNum());
        pageResult.setLists(data);
        return pageResult;
    }

    public static <T> PageResult<T> iPageHandle(IPage<T> iPage) {
        PageResult<T> pageResult = new PageResult<T>();
        pageResult.setCount(iPage.getTotal());
        pageResult.setPage_no((int)iPage.getCurrent());
        pageResult.setPage_size((int)iPage.getSize());
        pageResult.setLists(iPage.getRecords());
        return pageResult;
    }

    public static <T> PageResult<T> iPageHandle(Long total, Long pageNo, Long size, List<T> list) {
        PageResult<T> pageResult = new PageResult<T>();
        pageResult.setCount(total);
        pageResult.setPage_no(Math.toIntExact(pageNo));
        pageResult.setPage_size(Math.toIntExact(size));
        pageResult.setLists(list);
        return pageResult;
    }

    public static <T> PageResult<T> iPageHandle(Long total, Long pageNo, Long size, List<T> list, Map<String, Object> extend) {
        PageResult<T> pageResult = new PageResult<T>();
        pageResult.setCount(total);
        pageResult.setPage_no(Math.toIntExact(pageNo));
        pageResult.setPage_size(Math.toIntExact(size));
        pageResult.setLists(list);
        pageResult.setExtend(extend);
        return pageResult;
    }

    public Long getCount() {
        return this.count;
    }

    public Integer getPage_no() {
        return this.page_no;
    }

    public Integer getPage_size() {
        return this.page_size;
    }

    public Map<String, Object> getExtend() {
        return this.extend;
    }

    public List<T> getLists() {
        return this.lists;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    public void setLists(List<T> lists) {
        this.lists = lists;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PageResult)) {
            return false;
        }
        PageResult other = (PageResult)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$count = this.getCount();
        Long other$count = other.getCount();
        if (this$count == null ? other$count != null : !((Object)this$count).equals(other$count)) {
            return false;
        }
        Integer this$page_no = this.getPage_no();
        Integer other$page_no = other.getPage_no();
        if (this$page_no == null ? other$page_no != null : !((Object)this$page_no).equals(other$page_no)) {
            return false;
        }
        Integer this$page_size = this.getPage_size();
        Integer other$page_size = other.getPage_size();
        if (this$page_size == null ? other$page_size != null : !((Object)this$page_size).equals(other$page_size)) {
            return false;
        }
        Map<String, Object> this$extend = this.getExtend();
        Map<String, Object> other$extend = other.getExtend();
        if (this$extend == null ? other$extend != null : !((Object)this$extend).equals(other$extend)) {
            return false;
        }
        List<T> this$lists = this.getLists();
        List<T> other$lists = other.getLists();
        return !(this$lists == null ? other$lists != null : !((Object)this$lists).equals(other$lists));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $count = this.getCount();
        result = result * 59 + ($count == null ? 43 : ((Object)$count).hashCode());
        Integer $page_no = this.getPage_no();
        result = result * 59 + ($page_no == null ? 43 : ((Object)$page_no).hashCode());
        Integer $page_size = this.getPage_size();
        result = result * 59 + ($page_size == null ? 43 : ((Object)$page_size).hashCode());
        Map<String, Object> $extend = this.getExtend();
        result = result * 59 + ($extend == null ? 43 : ((Object)$extend).hashCode());
        List<T> $lists = this.getLists();
        result = result * 59 + ($lists == null ? 43 : ((Object)$lists).hashCode());
        return result;
    }

    public String toString() {
        return "PageResult(count=" + this.getCount() + ", page_no=" + this.getPage_no() + ", page_size=" + this.getPage_size() + ", extend=" + this.getExtend() + ", lists=" + this.getLists() + ")";
    }
}

