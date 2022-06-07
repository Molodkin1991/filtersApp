package com.wisercat.filtersApp.repo;

import com.wisercat.filtersApp.model.SearchFilter;

import java.util.List;

public interface SearchFilterRepo {
    SearchFilter save(SearchFilter filter);

    List<SearchFilter> saveAll(List<SearchFilter> filter);


    List<SearchFilter> findAll();
}
