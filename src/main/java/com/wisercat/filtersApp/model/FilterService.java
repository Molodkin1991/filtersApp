package com.wisercat.filtersApp.model;

import com.wisercat.filtersApp.repo.SearchFilterRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wisercat.filtersApp.model.DatabaseFields.AMOUNT;
import static com.wisercat.filtersApp.model.DatabaseFields.TITLE;
import static com.wisercat.filtersApp.model.FieldType.*;
import static com.wisercat.filtersApp.model.SearchCriteria.*;

@Service
public class FilterService {
    public static final Map<FieldType, List<SearchCriteria>> CRITERIA_FOR_TYPE = Map.of(
            STRING, List.of(EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH),
            NUMBER, List.of(EQUALS, MORE, LESS),
            DATE, List.of(EQUALS, BEFORE, FROM));
    public static final Map<DatabaseFields, FieldType> FIELDS_WITH_TYPE = Map.of(
            TITLE, STRING, AMOUNT, NUMBER, DatabaseFields.DATE, DATE);

    private final SearchFilterRepo searchFilterRepo;

    public FilterService(SearchFilterRepo searchFilterRepo) {
        this.searchFilterRepo = searchFilterRepo;
    }

    public void saveToRepo(List<SearchFilter> searchFilter) {
        searchFilterRepo.saveAll(searchFilter);
    }

    public Map<FieldType, List<SearchCriteria>> getCriteriaForType() {
        return CRITERIA_FOR_TYPE;
    }

    public Map<DatabaseFields, FieldType> getFieldsWithType() {
        return FIELDS_WITH_TYPE;
    }

    public List<SearchFilter> getExistingFilters() {
        return searchFilterRepo.findAll();
    }
}
