package online.store.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import online.store.criteria.enums.SearchOperation;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = StringSearchCriterion.class, name = "STRING"),
        @JsonSubTypes.Type(value = NumericSearchCriterion.class, name = "NUMERIC")
})

@Getter
@Setter
public abstract class SearchCriterion {
    private String field;
    private SearchOperation operation;
    private String type;
}
