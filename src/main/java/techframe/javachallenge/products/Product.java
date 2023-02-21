package techframe.javachallenge.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import lombok.NoArgsConstructor;
import techframe.javachallenge.core.ProductType;

@Data
@NoArgsConstructor
public class Product {

  private String name;
  private ProductType productType;
  private int quantity;
  private BigDecimal price;
  private BigDecimal discount;
  private int vat;

  @JsonIgnore
  public BigDecimal subtotalPrice() {
    return price.subtract(discount)
        .multiply(BigDecimal.valueOf(quantity));
  }

  @JsonIgnore
  public BigDecimal getVATAmount() {
    return subtotalPrice().multiply(BigDecimal.valueOf(vat))
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
  }

  @JsonIgnore
  public BigDecimal totalPrice() {
    return subtotalPrice().add(getVATAmount());
  }

  @JsonIgnore
  public BigDecimal totalPriceForQty(int qty) {
    return subtotalPriceForQty(qty).add(subtotalPriceForQty(qty).multiply(BigDecimal.valueOf(vat))
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
  }

  private BigDecimal subtotalPriceForQty(int qty) {
    return price.subtract(discount)
        .multiply(BigDecimal.valueOf(qty));
  }
}
