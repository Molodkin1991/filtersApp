package com.wisercat.filtersApp.controller;

import com.wisercat.filtersApp.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(value = "filter")
public class FilterController {

    private final FilterService service;

    public FilterController(FilterService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SearchFilter>> all() {
        return ResponseEntity.ok(service.getExistingFilters());
    }

    @GetMapping(path = "/fields-types")
    public ResponseEntity<Map<DatabaseFields, List<SearchCriteria>>> getFieldTypes() {
        Map<FieldType, List<SearchCriteria>> typeCriteriaMap = service.getCriteriaForType();

        return ResponseEntity.ok(service.getFieldsWithType().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, es -> typeCriteriaMap.get(es.getValue()))));
    }

    @PostMapping("/saveFilter")
    public void save(@RequestBody List<SearchFilter> searchFilter) {
        service.saveToRepo(searchFilter);
    }
}
