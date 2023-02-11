package khantorecrm.payload.dao.projection;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 11 Feb 2023
 **/
public interface ProductListAboutInput {
    Double getAmount();
    Long getProductId();
    String getProductName();
    Double getSumWholePrice();
    Double getSumRealPrice();
}
