package com.example.udemywebbackend.categories;

public class PageCategoryInfo {
    
    private int totalPages;
    private long totalElements;

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public void setTotalElements(long l) {
        this.totalElements = l;
    }
}
