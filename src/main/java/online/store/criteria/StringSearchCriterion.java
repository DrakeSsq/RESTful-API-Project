package online.store.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringSearchCriterion extends SearchCriterion {
    private String value;
}
