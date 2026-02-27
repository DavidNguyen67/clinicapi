package com.clinicsystem.clinicapi.util;

import com.clinicsystem.clinicapi.dto.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {

    private final List<SearchCriteria> criteriaList;

    public SpecificationBuilder() {
        this.criteriaList = new ArrayList<>();
    }

    public SpecificationBuilder<T> with(String key, String operation, Object value) {
        criteriaList.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public SpecificationBuilder<T> with(SearchCriteria criteria) {
        criteriaList.add(criteria);
        return this;
    }

    public Specification<T> build() {
        if (criteriaList.isEmpty()) {
            return null;
        }

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteria criteria : criteriaList) {
                Predicate predicate = createPredicate(criteria, root, criteriaBuilder);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Predicate createPredicate(SearchCriteria criteria, Root<T> root, CriteriaBuilder builder) {
        String key = criteria.getKey();
        Object value = criteria.getValue();
        SearchCriteria.Operation operation = SearchCriteria.Operation.fromValue(criteria.getOperation());

        switch (operation) {
            case EQUALS:
                return builder.equal(root.get(key), value);

            case NOT_EQUALS:
                return builder.notEqual(root.get(key), value);

            case GREATER_THAN:
                if (value instanceof Comparable) {
                    return builder.greaterThan(root.get(key), (Comparable) parseValue(value));
                }
                break;

            case LESS_THAN:
                if (value instanceof Comparable) {
                    return builder.lessThan(root.get(key), (Comparable) parseValue(value));
                }
                break;

            case GREATER_THAN_OR_EQUALS:
                if (value instanceof Comparable) {
                    return builder.greaterThanOrEqualTo(root.get(key), (Comparable) parseValue(value));
                }
                break;

            case LESS_THAN_OR_EQUALS:
                if (value instanceof Comparable) {
                    return builder.lessThanOrEqualTo(root.get(key), (Comparable) parseValue(value));
                }
                break;

            case LIKE:
                if (value instanceof String) {
                    return builder.like(builder.lower(root.get(key)), "%" + value.toString().toLowerCase() + "%");
                }
                break;

            case IN:
                if (value instanceof List) {
                    return root.get(key).in((List<?>) value);
                }
                break;

            case BETWEEN:
                if (value instanceof List && ((List<?>) value).size() == 2) {
                    List<?> range = (List<?>) value;
                    return builder.between(root.get(key), (Comparable) parseValue(range.get(0)),
                            (Comparable) parseValue(range.get(1)));
                }
                break;
        }

        return null;
    }

    private Object parseValue(Object value) {
        if (value instanceof String) {
            String strValue = (String) value;

            // Try parsing as LocalDateTime
            try {
                return LocalDateTime.parse(strValue, DateTimeFormatter.ISO_DATE_TIME);
            } catch (Exception ignored) {
            }

            // Try parsing as LocalDate
            try {
                return LocalDate.parse(strValue, DateTimeFormatter.ISO_DATE);
            } catch (Exception ignored) {
            }

            // Try parsing as number
            try {
                if (strValue.contains(".")) {
                    return Double.parseDouble(strValue);
                } else {
                    return Long.parseLong(strValue);
                }
            } catch (Exception ignored) {
            }
        }

        return value;
    }
}
