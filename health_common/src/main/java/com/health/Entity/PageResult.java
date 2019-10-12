package com.health.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装对象
 */
public class PageResult<T> implements Serializable{
    private Long total;//总记录数
    private List<T>  pageList;//当前页结果

    public PageResult() {
    }

    public PageResult(Long total, List<T> pageList) {
        this.total = total;
        this.pageList = pageList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", pageList=" + pageList +
                '}';
    }
}
