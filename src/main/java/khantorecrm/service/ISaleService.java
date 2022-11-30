package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.SaleDto;

public interface ISaleService {
    OwnResponse sell(SaleDto dto);
}
