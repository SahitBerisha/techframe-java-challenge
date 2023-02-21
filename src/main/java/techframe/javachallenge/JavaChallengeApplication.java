package techframe.javachallenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Comparator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import techframe.javachallenge.invoices.Invoice;
import techframe.javachallenge.invoices.InvoiceGenerator;
import techframe.javachallenge.products.ProductService;

@SpringBootApplication
public class JavaChallengeApplication {

  public static final String INVOICES_JSON_FILE_PATH = "src/main/resources/invoices.json";

  public static void main(String[] args) {
    SpringApplication.run(JavaChallengeApplication.class, args);
  }

  @Bean
  CommandLineRunner invoiceGenerator(ProductService service) {
    return args -> {
      var products = service.readProducts();
      var invoiceGenerator = new InvoiceGenerator(products);
      var invoices = invoiceGenerator.invoicesGenerator();
      invoices.sort(Comparator.comparing(Invoice::getOrderId));
      var jsonObjectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
      jsonObjectWriter.writeValue(new File(INVOICES_JSON_FILE_PATH), invoices);
    };
  }
}
