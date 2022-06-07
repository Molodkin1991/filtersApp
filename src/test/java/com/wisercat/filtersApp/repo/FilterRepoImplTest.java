package com.wisercat.filtersApp.repo;

import com.wisercat.filtersApp.model.FilterCondition;
import com.wisercat.filtersApp.model.SearchFilter;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static com.wisercat.filtersApp.SearchFilterTestObject.getTestSearchFilter;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilterRepoImplTest {

    @Autowired
    FilterRepoImpl repo;

    @Autowired
    Flyway flyway;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    public void setup() {
        flyway.clean();
        flyway.migrate();
        deleteAllDataInDb();
    }

    @AfterEach
    public void cleanup() {
        flyway.clean();
    }


    private void deleteAllDataInDb(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM filter_condition");
        jdbcTemplate.update("DELETE FROM search_filter");
    }

    @Test
    void ableToSaveAndLoadAndUpdateFilter() {
        SearchFilter searchFilter = getTestSearchFilter();
        List<SearchFilter> savedFilters = repo.saveAll(List.of(searchFilter));

        assertNotNull(savedFilters);
        assertEquals(1, savedFilters.size());

        SearchFilter savedSingleFilter = savedFilters.get(0);
        assertNotNull(savedSingleFilter.getId());
        assertEquals(searchFilter.getName(), savedSingleFilter.getName());
        assertNotNull(savedSingleFilter.getListOfCondition());
        assertNotNull(savedSingleFilter.getListOfCondition());
        assertEquals(3, savedSingleFilter.getListOfCondition().size());

        FilterCondition savedCondition = savedSingleFilter.getListOfCondition().get(0);
        assertNotNull(savedCondition.getId());
        assertTrue(searchFilter.getListOfCondition().contains(savedCondition));

        assertEquals(List.of(searchFilter), repo.findAll());
    }

    @Test
    void itShouldDeleteConditionsIfNotPresentInFilter() {
        SearchFilter searchFilter = getTestSearchFilter();
        List<SearchFilter> savedFiltersOne = repo.saveAll(List.of(searchFilter));

        assertNotNull(savedFiltersOne);
        assertEquals(1, savedFiltersOne.size());
        assertEquals(3, savedFiltersOne.get(0).getListOfCondition().size());

        List<SearchFilter> savedFiltersTwo = repo.saveAll(List.of(searchFilter));

        assertNotNull(savedFiltersTwo);
        assertEquals(1, savedFiltersTwo.size());
        assertEquals(3, savedFiltersTwo.get(0).getListOfCondition().size());

        searchFilter.setListOfCondition(null);
        List<SearchFilter> savedFiltersThree = repo.saveAll(List.of(searchFilter));
        assertNotNull(savedFiltersThree);
        assertEquals(1, savedFiltersThree.size());
        assertEquals(0, savedFiltersThree.get(0).getListOfCondition().size());

        List<SearchFilter> savedFiltersFour = repo.findAll();
        assertNotNull(savedFiltersFour);
        assertEquals(1, savedFiltersFour.size());
        assertEquals(0, savedFiltersFour.get(0).getListOfCondition().size());
    }
}