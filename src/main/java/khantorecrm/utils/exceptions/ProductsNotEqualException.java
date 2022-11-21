package khantorecrm.utils.exceptions;

public class ProductsNotEqualException extends RuntimeException {
    public ProductsNotEqualException(String product_items_should_be_equal) {
        super(product_items_should_be_equal);
    }
}
