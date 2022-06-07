package com.wisercat.filtersApp.controller;

import com.wisercat.filtersApp.model.DatabaseFields;
import com.wisercat.filtersApp.model.FilterService;
import com.wisercat.filtersApp.model.SearchCriteria;
import com.wisercat.filtersApp.model.SearchFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wisercat.filtersApp.SearchFilterTestObject.getTestSearchFilter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class FilterControllerTest {

    FilterService filterService;
    FilterController controller;

    @BeforeEach
    public void setup() {
        filterService = mock(FilterService.class);
        controller = new FilterController(filterService);
    }

    @Test
    void itShouldCallServiceToGetAllFilters() {
        when(filterService.getExistingFilters()).thenReturn(Collections.emptyList());
        ResponseEntity<List<SearchFilter>> response = controller.all();
        verify(filterService, times(1)).getExistingFilters();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void itShouldCallServiceToSaveAllFilters() {
        SearchFilter searchFilter = getTestSearchFilter();
        controller.save(List.of(searchFilter));
        verify(filterService, times(1)).saveToRepo(List.of(searchFilter));
    }

    @Test
    void itShouldCallServiceToGetAllTypes() {
        when(filterService.getCriteriaForType()).thenReturn(FilterService.CRITERIA_FOR_TYPE);
        when(filterService.getFieldsWithType()).thenReturn(FilterService.FIELDS_WITH_TYPE);
        ResponseEntity<Map<DatabaseFields, List<SearchCriteria>>> response = controller.getFieldTypes();
        verify(filterService, times(1)).getCriteriaForType();
        verify(filterService, times(1)).getFieldsWithType();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getCorrectTypeMapping(), response.getBody());
    }

    private Map<DatabaseFields, List<SearchCriteria>> getCorrectTypeMapping() {
        return FilterService.FIELDS_WITH_TYPE.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, es -> FilterService.CRITERIA_FOR_TYPE.get(es.getValue())));
    }
}