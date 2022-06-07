package com.wisercat.filtersApp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFilter {
    private Integer id;
    private String name;
    private List<FilterCondition> listOfCondition;

    public SearchFilter(Integer id, String name, List<FilterCondition> listOfCondition) {
        this.id = id;
        this.name = name;
        this.listOfCondition = listOfCondition == null ? new ArrayList<>() : listOfCondition;
    }

    public SearchFilter() {
        this.listOfCondition = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "SearchFilter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", listOfCondition=" + listOfCondition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchFilter that = (SearchFilter) o;
        return id.equals(that.id) && name.equals(that.name) && listOfCondition.equals(that.listOfCondition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, listOfCondition);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilterCondition> getListOfCondition() {
        return listOfCondition;
    }

    public void setListOfCondition(List<FilterCondition> listOfCondition) {
        this.listOfCondition = listOfCondition == null ? new ArrayList<>() : listOfCondition;
    }
}
