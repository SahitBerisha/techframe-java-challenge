package techframe.javachallenge.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductService {

  public static final String PRODUCT_JSON_FILE = "src/main/resources/products.json";

  public List<Product> readProducts() {
    try {
      var mapper = new ObjectMapper();
      return Arrays.asList(mapper.readValue(Paths.get(PRODUCT_JSON_FILE).toFile(), Product[].class));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }
}
