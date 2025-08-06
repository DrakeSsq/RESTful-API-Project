package online.store.util;

import online.store.criteria.NumericSearchCriterion;
import online.store.criteria.SearchCriterion;
import online.store.criteria.StringSearchCriterion;
import online.store.criteria.enums.SearchOperation;

import java.math.BigDecimal;

import static online.store.entity.enums.Category.ELECTRONICS;

public class ProductServiceData {

    public static final String PRICE_SEARCH = """
            [{
                "field": "price",
                "operation": "GREATER_THAN",
                "type": "NUMERIC",
                "value": 10000
            }]
            """;

    public static NumericSearchCriterion getNumericSearchCriterion() {
        NumericSearchCriterion criterion = new NumericSearchCriterion();
        criterion.setField("price");
        criterion.setOperation(SearchOperation.GREATER_THAN);
        criterion.setType("NUMERIC");
        criterion.setValue(BigDecimal.valueOf(1500));
        return criterion;
    }
    public static NumericSearchCriterion getSecondNumericSearchCriterion() {
        NumericSearchCriterion criterion = new NumericSearchCriterion();
        criterion.setField("price");
        criterion.setOperation(SearchOperation.EQUAL);
        criterion.setType("NUMERIC");
        criterion.setValue(BigDecimal.valueOf(1500));
        return criterion;
    }



    public static StringSearchCriterion getStringSearchCriterion() {
        StringSearchCriterion criterion = new StringSearchCriterion();
        criterion.setField("category");
        criterion.setOperation(SearchOperation.EQUAL);
        criterion.setType("STRING");
        criterion.setValue(String.valueOf(ELECTRONICS));
        return criterion;
    }

}
