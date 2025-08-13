package online.store.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.Path;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import static online.store.util.LogMessageUtil.ILLEGAL_ARGUMENT;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StringSearchCriterion extends SearchCriterion {

    @NotNull(message = "Value cannot be null")
    @JsonProperty("value")
    private String value;

    @Override
    public <T> Specification<T> toSpecification(Class<T> entityClass) {
        return ((root, query, criteriaBuilder) -> {

            Path<String> field = root.get(getField());
            String value = getValue();

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
                case CONTAINS -> {
                    return criteriaBuilder.like(field, "%" + value + "%");
                }
                default -> {
                    log.error(ILLEGAL_ARGUMENT);
                    throw new IllegalArgumentException(ILLEGAL_ARGUMENT);
                }
            }
        });
    }
}
