package org.anuran.model;

import java.util.ArrayList;
import java.util.List;

public class PaginatedResponse<T> {
    private final List<T> content;
    private final int totalElements;
    private final int totalPages;
    private final int size;
    private final int numberOfElements;
    private final int number;
    private final boolean first;
    private final boolean last;
    private final boolean empty;


    public PaginatedResponse(List<T> content, int totalElements, int totalPages,
                             int pageSize, int numberOfElements, int number,
                             boolean first, boolean last, boolean empty) {
        this.content = new ArrayList<>(content);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = pageSize;
        this.numberOfElements = numberOfElements;
        this.number = number;
        this.first = first;
        this.last = last;
        this.empty = empty;
    }


    // Getters for the properties

    public List<T> getContent() {
        return content;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getSize() {
        return size;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getNumber() {
        return number;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isEmpty() {
        return empty;
    }

}
