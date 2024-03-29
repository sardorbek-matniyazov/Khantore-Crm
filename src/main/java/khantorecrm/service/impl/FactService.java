package khantorecrm.service.impl;

import khantorecrm.model.FactProductItemDaily;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;
import khantorecrm.repository.FactProductItemRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.service.IFactService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 28 Mar 2023
 **/
@Component
public class FactService implements IFactService {
    private final FactProductItemRepository factProductItemRepository;
    private final ProductItemRepository productItemRepository;

    public FactService(FactProductItemRepository factProductItemRepository, ProductItemRepository productItemRepository) {
        this.factProductItemRepository = factProductItemRepository;
        this.productItemRepository = productItemRepository;
    }

    @Override
    public OwnResponse createOrUpdateDailyFactProductItem(FactProductItemDailyDto dto) {
        if (dto.getDate().matches("\\d{2}-\\d{2}-\\d{4}")) {
            dto.getProductItems().forEach(
                    item -> {
                        final FactProductItemDaily factProductItemDaily = factProductItemRepository.findByProductItemIdAndDate(item.getProductItemId(), dto.getDate()).orElse(
                                new FactProductItemDaily(
                                        productItemRepository.findById(item.getProductItemId()).orElse(null),
                                        item.getStartAmount(),
                                        item.getEndAmount(),
                                        dto.getDate()
                                )
                        );
                        factProductItemDaily.setDayStartAmount(item.getStartAmount());
                        factProductItemDaily.setDayEndAmount(item.getEndAmount());
                        factProductItemRepository.save(factProductItemDaily);
                    }
            );
        } else {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage("Date must be in format dd-MM-yyyy");
        }

        return OwnResponse.UPDATED_SUCCESSFULLY;
    }

    @Override
    public List<FactProductItemDaily> getFactDailyWarehouseProducts(Long warehouseId) {
        return factProductItemRepository.findAll();
    }
}
