package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ClientDto;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 10 Feb 2023
 **/
public interface IClientService {
    OwnResponse paymentToBalance(ClientDto dto);
}
