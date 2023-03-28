package khantorecrm.payload.dao.projection;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 28 Mar 2023
 **/
public interface SellerIncomePayment {
    Double getSumAmount();
    Long getUserId();
    String getPaymentType();
}
