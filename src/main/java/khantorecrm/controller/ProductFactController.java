package khantorecrm.controller;

import khantorecrm.model.FactProductItemDaily;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;
import khantorecrm.repository.FactProductItemRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.service.IFactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "fact-product")
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER', 'LOADER', 'SUPER_LOADER')")
public class ProductFactController {
    private final IFactService service;
    private final FactProductItemRepository factProductItemRepository;
    private final ProductItemRepository productItemRepository;


    @Autowired
    public ProductFactController(IFactService service,  FactProductItemRepository factProductItemRepository, ProductItemRepository productItemRepository) {
        this.service = service;
        this.factProductItemRepository = factProductItemRepository;
        this.productItemRepository = productItemRepository;
    }

    @RequestMapping(value = "daily-fact", method = {RequestMethod.POST, RequestMethod.PUT})
    private HttpEntity<?> dailyFactProductItem(@RequestBody @Valid FactProductItemDailyDto dto) {
        return createOrUpdateDailyFactProductItem(dto).handleResponse();
    }

    @GetMapping(value = "daily-fact-warehouse/{warehouseId}")
    private HttpEntity<?> getFactDailyByWarehouse(@PathVariable Long warehouseId) {
        return OwnResponse.ALL_DATA.setData(getFactDailyWarehouseProducts(warehouseId)).handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }

    // timeless logic
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

    public List<FactProductItemDaily> getFactDailyWarehouseProducts(Long warehouseId) {
        return factProductItemRepository.findAll();
    }

}
