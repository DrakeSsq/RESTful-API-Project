package online.store.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.store.criteria.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "field",
        visible = true
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = StringSearchCriterion.class, name = "name"),
        @JsonSubTypes.Type(value = StringSearchCriterion.class, name = "description"),
        @JsonSubTypes.Type(value = StringSearchCriterion.class, name = "category"),
        @JsonSubTypes.Type(value = NumericSearchCriterion.class, name = "price"),
        @JsonSubTypes.Type(value = NumericSearchCriterion.class, name = "quantity")
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class SearchCriterion {

    @NotBlank(message = "Field cannot be blank")
    @Size(min = 3, max = 15, message = "Field must be between 3 and 15 characters")
    @JsonProperty("field")
    private String field;

    @NotNull(message = "Operation cannot be null")
    @JsonProperty("operation")
    private SearchOperation operation;

    public abstract <T> Specification<T> toSpecification(Class<T> entityClass);

}
