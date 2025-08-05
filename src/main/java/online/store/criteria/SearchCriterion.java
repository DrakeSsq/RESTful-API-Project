package online.store.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.store.criteria.enums.SearchOperation;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = StringSearchCriterion.class, name = "STRING"),
        @JsonSubTypes.Type(value = NumericSearchCriterion.class, name = "NUMERIC")
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class SearchCriterion {

    @JsonProperty("field")
    private String field;

    @JsonProperty("operation")
    private SearchOperation operation;

    @JsonProperty("type")
    private String type;
}
