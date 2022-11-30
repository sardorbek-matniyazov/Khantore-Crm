package khantorecrm.utils.aop;

import khantorecrm.payload.dto.ProductItemList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CheckItemsValidator implements ConstraintValidator<CheckProductItemList, List<ProductItemList>> {
    @Override
    public boolean isValid(List<ProductItemList> productItems, ConstraintValidatorContext constraintValidatorContext) {
        return productItems != null && productItems.stream().anyMatch(item -> item.getProductItemId() != null && item.getAmount() != null && item.getAmount() >= 0.0);
    }
}
