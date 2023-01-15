package khantorecrm.utils.aop;

import khantorecrm.payload.dto.FactProductItemDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class CheckFactProductItemValidator implements ConstraintValidator<CheckFactProductItemList, List<FactProductItemDto>> {
    @Override
    public boolean isValid(List<FactProductItemDto> factProductItemDTOs, ConstraintValidatorContext constraintValidatorContext) {
        return factProductItemDTOs != null
                && factProductItemDTOs.size() > 0
                && factProductItemDTOs.stream().allMatch(
                        factProductItemDTO -> factProductItemDTO.getProductItemId() != null
                                && (factProductItemDTO.getStartAmount() != null || factProductItemDTO.getEndAmount() != null)
        );
    }
}
