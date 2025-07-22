package online.store.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductServiceUtil {

    public final String PRODUCT_NOT_FOUND = "Product with %s UUID not found";

    public final String UPDATE_PRODUCT = "Error when changing the product. Check the correctness of the entered data";

    public final String ARTICLE_ALREADY_EXISTS = "A product with article %s already exists";
}
