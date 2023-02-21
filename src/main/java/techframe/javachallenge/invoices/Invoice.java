package techframe.javachallenge.invoices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import techframe.javachallenge.products.Product;

@Data
@NoArgsConstructor
public class Invoice {

  private int orderId;
  private BigDecimal subTotal = BigDecimal.ZERO;
  private BigDecimal vat = BigDecimal.ZERO;
  private BigDecimal total = BigDecimal.ZERO;
  private List<Product> products = new ArrayList<>();

  public void addProduct(Product product) {
    products.add(product);
  }

  @JsonIgnore
  public boolean isEmpty() {
    return products.isEmpty();
  }

  public BigDecimal addToSubTotal(BigDecimal value) {
    return this.subTotal = subTotal.add(value);
  }

  public BigDecimal addToTotal(BigDecimal value) {
    return this.total = total.add(value);
  }

  public BigDecimal addToVat(BigDecimal value) {
    return this.vat = vat.add(value);
  }

  public Boolean containsProduct(String name) {
    return products.stream().anyMatch(p -> p.getName().equals(name));
  }
}
