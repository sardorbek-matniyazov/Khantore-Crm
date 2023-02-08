package khantorecrm.payload.dao.projection;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Feb 2023
 **/
public interface ProductPriceForSellerProjection {
    String getProductName();
    Long getProductId();
    String getDelivererName();
    Long getDelivererId();
    Double getPrice();
}
