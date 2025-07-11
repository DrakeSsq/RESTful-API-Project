package online.store;

import online.store.models.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class ResTfulApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResTfulApiApplication.class, args);
		Product product = new Product();
		product.setName("testspring");
		product.setArticle("article");
		product.setDescription("desx");
		product.setCategory("category");
		product.setPrice(BigDecimal.valueOf(12.54));
		product.setQuantity(1);



	}

}
