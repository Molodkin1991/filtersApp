package com.wisercat.filtersApp.model;

import com.wisercat.filtersApp.repo.SearchFilterRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.wisercat.filtersApp.SearchFilterTestObject.getTestSearchFilter;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FilterServiceTest {

    FilterService service;
    SearchFilterRepo repo;

    @BeforeEach
    public void setup() {
        repo = mock(SearchFilterRepo.class);
        service = new FilterService(repo);
    }

    @Test
    public void itReturnsCorrectValuesForStaticListStoredInTheService() {
        service.getCriteriaForType().forEach((key, value) -> {
            assertEquals(FilterService.CRITERIA_FOR_TYPE.get(key), value);
        });
        assertEquals(FilterService.CRITERIA_FOR_TYPE.entrySet().size(), service.getCriteriaForType().entrySet().size());

        service.getFieldsWithType().forEach((key, value) -> {
            assertEquals(FilterService.FIELDS_WITH_TYPE.get(key), value);
        });

        assertEquals(FilterService.FIELDS_WITH_TYPE.entrySet().size(), service.getFieldsWithType().entrySet().size());
    }

    @Test
    public void itSavesToRepository() {
        SearchFilter searchFilter = getTestSearchFilter();
        service.saveToRepo(List.of(searchFilter));
        verify(repo, times(1)).saveAll(List.of(searchFilter));
    }

    @Test
    public void itReturnSearchFilterFromRepo() {
        SearchFilter searchFilter = getTestSearchFilter();
        when(repo.findAll()).thenReturn(List.of(searchFilter));
        List<SearchFilter> resultList = service.getExistingFilters();
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(searchFilter, resultList.get(0));
    }
}