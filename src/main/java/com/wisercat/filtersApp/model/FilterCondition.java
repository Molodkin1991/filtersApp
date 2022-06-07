package com.wisercat.filtersApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class FilterCondition {

    private Integer id;
    private DatabaseFields name;
    private SearchCriteria searchCriteria;
    private String value;

    private SearchFilter searchFilter;

    public FilterCondition() {
    }

    public FilterCondition(DatabaseFields name, SearchCriteria searchCriteria, String value, SearchFilter searchFilter) {
        this.name = name;
        this.searchCriteria = searchCriteria;
        this.value = value;
        this.searchFilter = searchFilter;
    }

    public FilterCondition(DatabaseFields name, SearchCriteria searchCriteria, String value, SearchFilter searchFilter, Integer id) {
        this.name = name;
        this.searchCriteria = searchCriteria;
        this.value = value;
        this.searchFilter = searchFilter;
        this.id = id;
    }

    @JsonIgnore
    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(SearchFilter filter) {
        this.searchFilter = filter;
    }

    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DatabaseFields getName() {
        return name;
    }

    public void setName(DatabaseFields name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterCondition that = (FilterCondition) o;
        return Objects.equals(name, that.name) && searchCriteria == that.searchCriteria && Objects.equals(value, that.value) &&
                (Objects.equals(id, that.id) || that.id == null || id == null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, searchCriteria, value, id);
    }


    @Override
    public String toString() {
        return "FilterCondition{" +
                "name=" + name +
                ", searchCriteria=" + searchCriteria +
                ", value='" + value + '\'' +
                ", id=" + id +
                '}';
    }
}
