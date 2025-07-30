package online.store.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMessageUtil {

    public static final String SAVING_IN_DB_LOG = "Saving {} in database";
    public static final String UPDATING_PRICES_BASIC = "Updated prices for {} products";
    public static final String TIMEABLE_ASPECT_LOG = "The transaction for the {} method was completed with the status {} in {} ms";
    public static final String SUCCESSFUL_ERRORS_COUNTER = "Successful: {}, Errors: {}";
    public static final String ERROR_CLOSING_CSV = "Error closing CSV writer: {}";
}
