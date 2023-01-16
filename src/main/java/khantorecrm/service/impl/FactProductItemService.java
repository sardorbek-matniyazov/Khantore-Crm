package khantorecrm.service.impl;

import khantorecrm.model.FactProductItemDaily;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;
import khantorecrm.repository.FactProductItemRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.service.IFactProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactProductItemService implements IFactProductItemService {
    private final FactProductItemRepository repository;
    private final ProductItemRepository itemRepository;

    @Autowired
    public FactProductItemService(FactProductItemRepository repository, ProductItemRepository itemRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
    }

    @Override
    public OwnResponse createDailyFactProductItem(FactProductItemDailyDto dto) {
        if (dto.getDate().matches("\\d{2}-\\d{2}-\\d{4}")) {
            dto.getProductItems().forEach(
                    item ->
                            itemRepository.findById(item.getProductItemId()).ifPresent(
                                    productItem ->
                                            repository.save(
                                                    new FactProductItemDaily(
                                                            item.getProductItemId(),
                                                            item.getStartAmount()  != null ? item.getStartAmount() : 0.0,
                                                            item.getEndAmount()  != null ? item.getEndAmount() : 0.0,
                                                            dto.getDate()
                                                    )
                                            )
                            )
            );
        } else {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage("Date must be in format dd-MM-yyyy");
        }
        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    @Override
    public OwnResponse updateDailyFactProductItemWithId(FactProductItemDailyDto dto) {
        if (dto.getDate().matches("\\d{2}-\\d{2}-\\d{4}")) {
            dto.getProductItems().forEach(
                    item ->
                            itemRepository.findById(
                                    item.getProductItemId()).flatMap(
                                            productItem -> repository.findByIdAndDate(item.getProductItemId(), dto.getDate())).ifPresent(factProductItemDaily -> {
                                                factProductItemDaily.setDayStartAmount(item.getStartAmount() != null ? item.getStartAmount() : 0.0);
                                                factProductItemDaily.setDayEndAmount(item.getEndAmount() != null ? item.getEndAmount() : 0.0);
                                                repository.save(factProductItemDaily);
                                            })
            );
        } else {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage("Date must be in format dd-MM-yyyy");
        }
        return OwnResponse.UPDATED_SUCCESSFULLY;
    }

    @Override
    public List<FactProductItemDaily> getFactDailyWarehouseProducts(Long warehouseId) {
        return repository.findAll();
    }
}
