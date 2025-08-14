package online.store.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMessageUtil {

    public static final String SAVING_IN_DB_LOG = "Saving {} in database";
    public static final String UPDATING_PRICES_BASIC = "Updated prices for {} products";
    public static final String TIMEABLE_ASPECT_LOG = "The transaction was completed with the status {} in {} ms";
    public static final String SUCCESSFUL_UPDATE = "Successful update";
    public static final String ERROR_CLOSING_CSV = "Error closing CSV writer: {}";
    public static final String ERROR_WRITING_CSV = "Error writing CSV: {}";
    public static final String ERROR_UPDATE_PRICE = "Price update error: {}";
    public static final String ERROR_IN_PROCESS_METHOD = "Error in the method: {}";
    public static final String ERROR_CLEAR_DATA = "Data clearing error: {}";
    public static final String ILLEGAL_ARGUMENT = "Illegal argument into json";
    public static final String EMPTY_CRITERIA = "An empty list of criteria";
    public static final String ILLEGAL_CRITERIA_TYPE = "Unknown criteria type";
    public static final String GETTING_PRODUCT = "Getting a product by UUID {}";
    public static final String CHECK_ARTICLE = "Checking product article availability";
    public static final String GETTING_PAGES = "Getting {} pages of {} products";
    public static final String DELETE_PRODUCT = "Product with id {} has been deleted";
    public static final String UPDATE_PRODUCT = "Product {} has been updated";
    public static final String PRODUCT_NOT_FOUND = "Product with %s UUID not found";
    public static final String CUSTOMER_NOT_FOUND = "Customer with id %s not found";
    public static final String NOT_ENOUGH_QUANTITY = "The quantity of goods you want is more than in stock or not at all. In the warehouse %s. Your wish %s";
}
