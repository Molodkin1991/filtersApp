package com.wisercat.filtersApp.repo;

import com.wisercat.filtersApp.model.DatabaseFields;
import com.wisercat.filtersApp.model.FilterCondition;
import com.wisercat.filtersApp.model.SearchCriteria;
import com.wisercat.filtersApp.model.SearchFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Transactional
public class FilterRepoImpl implements SearchFilterRepo {

    private static final String WHERE_ID_IS = " WHERE id = ? ";
    private static final String UPDATE_FILTER =
            "UPDATE search_filter "
                    + " SET "
                    + " name = ? "
                    + WHERE_ID_IS;
    private static final String INSERT_FILTER = "INSERT INTO search_filter (name) VALUES ( ? )";
    private static final String INSERT_FILTER_CONDITION =
            " INSERT INTO filter_condition "
                    + " (name, search_criteria, condition_value, search_filter_id) "
                    + " VALUES ( ?, ?, ?, ? )";
    private static final String UPDATE_FILTER_CONDITION =
            "UPDATE filter_condition "
                    + " SET "
                    + " name = ?,  "
                    + " search_criteria = ?, "
                    + " condition_value = ?, "
                    + " search_filter_id = ? "
                    + WHERE_ID_IS;

    private static final String FIND_ALL_FILTERS =
            "SELECT * FROM search_filter ";
    private static final String DELETE_FILTER_CONDITION_BY_FILTER_ID_AND_NOT_ID_NOT_IN_LIST =
            "DELETE FROM filter_condition "
                    + " WHERE search_filter_id = ? "
                    + " AND id NOT IN ( ? ) ";
    private static final String DELETE_FILTER_CONDITION_BY_FILTER_ID =
            "DELETE FROM filter_condition "
                    + " WHERE search_filter_id = ? ";
    private static final String FIND_FILTER_CONDITIONS_BY_ID =
            "SELECT * FROM filter_condition WHERE search_filter_id = ? ";
    private final JdbcTemplate jdbcTemplate;

    public FilterRepoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public SearchFilter save(SearchFilter filter) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (filter.getId() != null) {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(UPDATE_FILTER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, filter.getName());
                ps.setLong(2, filter.getId());
                return ps;
            }, keyHolder);
            deleteAllOldFilterCondition(filter);
        } else {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(INSERT_FILTER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, filter.getName());
                return ps;
            }, keyHolder);
        }
        filter.setId((Integer) keyHolder.getKeyList().get(0).get("id"));
        saveAllFilterConditions(filter);
        return filter;
    }

    private void saveAllFilterConditions(SearchFilter filter) {
        for (FilterCondition filterCondition : filter.getListOfCondition()) {
            filterCondition.setSearchFilter(filter);
            save(filterCondition);
        }
    }

    private FilterCondition save(FilterCondition filterCondition) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (filterCondition.getId() != null) {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(UPDATE_FILTER_CONDITION, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, filterCondition.getName().name());
                ps.setString(2, filterCondition.getSearchCriteria().name());
                ps.setString(3, filterCondition.getValue());
                ps.setLong(4, filterCondition.getSearchFilter().getId());
                ps.setLong(5, filterCondition.getId());
                return ps;
            }, keyHolder);
        } else {

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(INSERT_FILTER_CONDITION, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, filterCondition.getName().name());
                ps.setString(2, filterCondition.getSearchCriteria().name());
                ps.setString(3, filterCondition.getValue());
                ps.setLong(4, filterCondition.getSearchFilter().getId());
                return ps;
            }, keyHolder);
        }
        filterCondition.setId((Integer) keyHolder.getKeyList().get(0).get("id"));

        return filterCondition;
    }

    @Override
    public List<SearchFilter> saveAll(List<SearchFilter> filters) {
        List<SearchFilter> savedFilters = new ArrayList<>();
        for (SearchFilter filter : filters) {
            savedFilters.add(this.save(filter));
        }
        return savedFilters;
    }

    public void deleteAllOldFilterCondition(SearchFilter filter) {
        String query = getDeleteStringWithConditionIdParams(filter);
        jdbcTemplate.update(query,
                filter.getId());
    }

    private String getDeleteStringWithConditionIdParams(SearchFilter filter) {
        String commaSeparatedIds = filter.getListOfCondition()
                .stream()
                .map(FilterCondition::getId)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(","));

        if (commaSeparatedIds.isBlank()) {
            return DELETE_FILTER_CONDITION_BY_FILTER_ID;
        }
        return DELETE_FILTER_CONDITION_BY_FILTER_ID_AND_NOT_ID_NOT_IN_LIST
                .replace("AND id NOT IN ( ? )",
                        "AND id NOT IN ( " + commaSeparatedIds + " ) ");
    }

    @Override
    public List<SearchFilter> findAll() {
        return jdbcTemplate.queryForList(FIND_ALL_FILTERS)
                .stream()
                .map(this::getSearchFilterForDbRow)
                .collect(Collectors.toList());
    }

    private List<FilterCondition> getListOfConditionsByFilter(SearchFilter filter) {
        return jdbcTemplate.queryForList(FIND_FILTER_CONDITIONS_BY_ID, filter.getId())
                .stream()
                .map(this::getFilterConditionsForDbRow)
                .peek(x -> x.setSearchFilter(filter))
                .collect(Collectors.toList());
    }

    private FilterCondition getFilterConditionsForDbRow(Map<String, Object> row) {
        Integer filterId = (Integer) row.get("id");
        FilterCondition condition = new FilterCondition();
        condition.setId(filterId);
        condition.setValue((String) row.get("condition_value"));
        condition.setName(DatabaseFields.valueOf((String) row.get("name")));
        condition.setSearchCriteria(SearchCriteria.valueOf((String) row.get("search_criteria")));
        return condition;
    }

    private SearchFilter getSearchFilterForDbRow(Map<String, Object> row) {
        Integer filterId = (Integer) row.get("id");
        SearchFilter filter = new SearchFilter();
        filter.setId(filterId);
        filter.setName((String) row.get("name"));
        filter.setListOfCondition(getListOfConditionsByFilter(filter));
        return filter;
    }

}
