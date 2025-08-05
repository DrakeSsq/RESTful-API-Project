package online.store.criteria;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NumericSearchCriterion extends SearchCriterion {
    private BigDecimal value;
}
