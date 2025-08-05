package online.store.criteria.specification;

import jakarta.persistence.criteria.Path;
import lombok.extern.slf4j.Slf4j;
import online.store.criteria.NumericSearchCriterion;
import online.store.criteria.SearchCriterion;
import online.store.criteria.StringSearchCriterion;
import online.store.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static online.store.util.LogMessageUtil.*;

@Slf4j
public class ProductSpecificationBuilder {

    public static Specification<Product> build(List<SearchCriterion> criterions) {

        if (criterions.isEmpty()) {
            log.error(EMPTY_CRITERIA);
        }

        Specification<Product> result = null;
        for (SearchCriterion criteria : criterions) {
            if (result == null) {
                result = createSpecification(criteria);
            } else {
                result = result.and(createSpecification(criteria));
            }
        }
        return result;
    }

    private static Specification<Product> createSpecification(SearchCriterion criteria) {

        return switch (criteria.getType()) {
            case "STRING" -> getStringSpecificationType(criteria);

            case "NUMERIC" -> getNumericSpecificationType(criteria);

            default -> {
                log.error(ILLEGAL_CRITERIA_TYPE);
                throw new IllegalArgumentException(ILLEGAL_CRITERIA_TYPE);
            }
        };
    }

    private static Specification<Product> getNumericSpecificationType(SearchCriterion criteria) {
        return ((root, query, criteriaBuilder) -> {

            Path<BigDecimal> field = root.get(criteria.getField());
            BigDecimal value = ((NumericSearchCriterion) criteria).getValue();

            switch (criteria.getOperation()) {
                case EQUAL -> {
                    return criteriaBuilder.equal(field, value);
                }
                case NOT_EQUAL -> {
                    return criteriaBuilder.notEqual(field, value);
                }
                case GREATER_THAN -> {
                    return criteriaBuilder.greaterThan(field, value);
                }
                case GREATER_THAN_OR_EQUAL -> {
                    return criteriaBuilder.greaterThanOrEqualTo(field, value);
                }
                case LESS_THAN -> {
                    return criteriaBuilder.lessThan(field, value);
                }
                case LESS_THAN_OR_EQUAL -> {
                    return criteriaBuilder.lessThanOrEqualTo(field, value);
                }
                default -> {
                    log.error(ILLEGAL_ARGUMENT);
                    throw new IllegalArgumentException(ILLEGAL_ARGUMENT);
                }
            }
        });
    }

    private static Specification<Product> getStringSpecificationType(SearchCriterion criteria) {
        return ((root, query, criteriaBuilder) -> {

            Path<String> field = root.get(criteria.getField());
            String value = ((StringSearchCriterion) criteria).getValue();

            switch (criteria.getOperation()) {
                case EQUAL -> {
                    return criteriaBuilder.equal(field, value);
                }
                case NOT_EQUAL -> {
                    return criteriaBuilder.notEqual(field, value);
                }
                case GREATER_THAN -> {
                    return criteriaBuilder.greaterThan(field, value);
                }
                case GREATER_THAN_OR_EQUAL -> {
                    return criteriaBuilder.greaterThanOrEqualTo(field, value);
                }
                case LESS_THAN -> {
                    return criteriaBuilder.lessThan(field, value);
                }
                case LESS_THAN_OR_EQUAL -> {
                    return criteriaBuilder.lessThanOrEqualTo(field, value);
                }
                case CONTAINS -> {
                    return criteriaBuilder.like(field, "%" + value + "%");
                }
                default -> throw new IllegalArgumentException(ILLEGAL_ARGUMENT);
            }
        });
    }
}
