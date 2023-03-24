package khantorecrm.service;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Mar 2023
 **/
public interface IClientSmsService {
    void sendSmsAfterEachSale(String clientName, String phone, String wholePrice, String paidPrice, String debtPrice);
}
