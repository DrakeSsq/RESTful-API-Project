package online.store.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.Path;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static online.store.util.LogMessageUtil.ILLEGAL_ARGUMENT;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class NumericSearchCriterion extends SearchCriterion {

    @NotNull(message = "Value cannot be null")
    @Positive(message = "Value must be positive")
    @JsonProperty("value")
    private BigDecimal value;

    @Override
    public <T> Specification<T> toSpecification(Class<T> entityClass) {
        return ((root, query, criteriaBuilder) -> {

            Path<BigDecimal> field = root.get(getField());
            BigDecimal value = getValue();

            switch (getOperation()) {
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
}
