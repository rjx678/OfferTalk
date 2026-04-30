package com.offertalk.common;

public class PageResult<T> {
    private long total;
    private long page;
    private long size;
    private long totalPages;
    private T records;

    public PageResult() {
    }

    public PageResult(long total, long page, long size, T records) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = (total + size - 1) / size;
        this.records = records;
    }

    public static <T> PageResult<T> of(long total, long page, long size, T records) {
        return new PageResult<>(total, page, size, records);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public T getRecords() {
        return records;
    }

    public void setRecords(T records) {
        this.records = records;
    }
}
