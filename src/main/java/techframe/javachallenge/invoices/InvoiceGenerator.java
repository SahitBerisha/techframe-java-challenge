package techframe.javachallenge.invoices;

import static java.util.Objects.isNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import techframe.javachallenge.products.Product;

@RequiredArgsConstructor
public class InvoiceGenerator {

  private static final BigDecimal MAX_INVOICE_VALUE = new BigDecimal(500);
  private static final int MAX_PRODUCT_QUANTITY = 50;

  private Map<Integer, Product> products = new HashMap<>();

  public InvoiceGenerator(List<Product> productsList) {
    var productId = 1;
    for (Product product : productsList) {
      products.put(productId, product);
      productId++;
    }
  }

  public List<Invoice> invoicesGenerator() {
    var invoices = new ArrayList<Invoice>();
    var currentInvoice = new Invoice();
    var currentInvoiceId = 1;
    currentInvoice.setOrderId(currentInvoiceId);
    var currentInvoiceValue = currentInvoice.getTotal();
    var currentProductQuantity = 0;
    var productsSize = products.size();
    for (int productId = 1; productId <= productsSize; productId++) {
      if (isNull(products.get(productId))) {
        continue;
      }
      var product = products.get(productId);
      if (currentInvoice.containsProduct(product.getName())) {
        invoices.add(currentInvoice);
        currentInvoice = new Invoice();
        currentInvoiceValue = currentInvoice.getTotal();
        currentInvoiceId++;
      }
      currentProductQuantity = product.getQuantity();

      // Handle product that exceeds 500$
      if (product.getPrice().compareTo(MAX_INVOICE_VALUE) > 0) {
        if (currentInvoice.isEmpty()) {
          addToCurrentInvoice(currentInvoice, product, currentInvoiceId);
          invoices.add(currentInvoice);
          products.remove(productId);
          productId = 0;
          currentInvoice = new Invoice();
          currentInvoiceValue = currentInvoice.getTotal();
          currentInvoiceId++;
          continue;
        }
        var newInvoice = createNewInvoice(product, currentInvoiceId + 1);
        invoices.add(newInvoice);
        products.remove(productId);
        continue;
      }
      //  Handle if Invoice count exceeds 50
      if (currentProductQuantity > MAX_PRODUCT_QUANTITY) {
        product.setQuantity(MAX_PRODUCT_QUANTITY);
        var remainingQuantity = currentProductQuantity - MAX_PRODUCT_QUANTITY;
        if (currentInvoiceValue.add(product.totalPrice()).compareTo(MAX_INVOICE_VALUE) > 0) {
          for (int quantity = 1; quantity <= MAX_PRODUCT_QUANTITY; quantity++) {
            product.setQuantity(quantity);
            if (currentInvoiceValue.add(product.totalPriceForQty(quantity)).compareTo(MAX_INVOICE_VALUE) < 0) {
              product.setQuantity(quantity);
              break;
            }
          }
          addToCurrentInvoice(currentInvoice, product, currentInvoiceId);
          updateProductQty(productId, product, MAX_PRODUCT_QUANTITY - product.getQuantity());
          currentInvoice = new Invoice();
          currentInvoiceValue = currentInvoice.getTotal();
          currentInvoiceId++;
          productId = 0;
        } else {
          addToCurrentInvoice(currentInvoice, product, currentInvoiceId);
          updateProductQty(productId, product, remainingQuantity);
          currentInvoiceValue = currentInvoice.getTotal();
        }
      } else {
        if (currentInvoiceValue.add(product.totalPrice()).compareTo(MAX_INVOICE_VALUE) > 0) {
          for (int quantity = 1; quantity <= MAX_PRODUCT_QUANTITY; quantity++) {
            if (currentInvoiceValue.add(product.totalPriceForQty(quantity)).compareTo(MAX_INVOICE_VALUE) > 0) {
              currentProductQuantity -= product.getQuantity();
              break;
            }
            product.setQuantity(quantity);
          }
          addToCurrentInvoice(currentInvoice, product, currentInvoiceId);
          invoices.add(currentInvoice);
          products.remove(productId);
          currentInvoice = new Invoice();
          currentInvoiceValue = currentInvoice.getTotal();
          currentInvoiceId++;
          productId = 0;
          continue;
        }
        // If everything passes continue normal flow
        addToCurrentInvoice(currentInvoice, product, currentInvoiceId);
        currentInvoiceValue = currentInvoice.getTotal();
        products.remove(productId);
      }
    }
    return invoices;
  }

  private Invoice createNewInvoice(Product product, int invoiceId) {
    var invoice = new Invoice();
    invoice.setOrderId(invoiceId);
    invoice.setSubTotal(product.subtotalPrice());
    invoice.setVat(product.getVATAmount());
    invoice.setTotal(product.totalPrice());
    invoice.addProduct(product);
    return invoice;
  }

  private void addToCurrentInvoice(Invoice invoice, Product product, int invoiceId) {
    invoice.setOrderId(invoiceId);
    invoice.addToSubTotal(product.subtotalPrice());
    invoice.addToVat(product.getVATAmount());
    invoice.addToTotal(product.totalPrice());
    invoice.addProduct(product);
  }

  private void updateProductQty(int productId, Product product, int quantity) {
    var newProduct = new Product();
    newProduct.setName(product.getName());
    newProduct.setProductType(product.getProductType());
    newProduct.setQuantity(quantity);
    newProduct.setPrice(product.getPrice());
    newProduct.setDiscount(product.getDiscount());
    newProduct.setVat(product.getVat());
    products.put(productId, newProduct);
  }
}
