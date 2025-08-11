package online.store.criteria.specification;

import lombok.extern.slf4j.Slf4j;
import online.store.criteria.NumericSearchCriterion;
import online.store.criteria.SearchCriterion;
import online.store.criteria.StringSearchCriterion;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static online.store.util.LogMessageUtil.*;

@Slf4j
@Component
public class MySpecificationBuilder<T> {

    public Specification<T> build(Class<T> entityClass, List<SearchCriterion> criteria) {

        if (criteria == null || criteria.isEmpty()) {
            log.warn(EMPTY_CRITERIA);
            return (root, query, cb) -> cb.conjunction();
        }

        return criteria.stream()
                .map(criterion -> createSpecification(entityClass, criterion))
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
    }

    private Specification<T> createSpecification(Class<T> entityClass, SearchCriterion criteria) {

        return switch (criteria) {
            case StringSearchCriterion str -> str.toSpecification(entityClass);
            case NumericSearchCriterion num -> num.toSpecification(entityClass);
            default -> {
                log.error(ILLEGAL_CRITERIA_TYPE);
                throw new IllegalArgumentException(ILLEGAL_CRITERIA_TYPE);
            }
        };
    }
}
