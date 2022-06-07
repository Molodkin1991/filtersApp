package com.wisercat.filtersApp;

import com.wisercat.filtersApp.model.DatabaseFields;
import com.wisercat.filtersApp.model.FilterCondition;
import com.wisercat.filtersApp.model.SearchFilter;

import java.util.List;

import static com.wisercat.filtersApp.model.SearchCriteria.*;
import static java.util.Collections.emptyList;

public class SearchFilterTestObject {

    public static SearchFilter getTestSearchFilter() {
        SearchFilter searchFilter = new SearchFilter(null, "My First Filter", emptyList());
        List<FilterCondition> conditions = List.of(
                new FilterCondition(DatabaseFields.AMOUNT, MORE, "4", searchFilter),
                new FilterCondition(DatabaseFields.TITLE, ENDS_WITH, "MEOW", searchFilter),
                new FilterCondition(DatabaseFields.TITLE, STARTS_WITH, "MEOW", searchFilter));
        searchFilter.setListOfCondition(conditions);
        return searchFilter;
    }
}
