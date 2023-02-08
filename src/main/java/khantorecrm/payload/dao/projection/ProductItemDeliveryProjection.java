package khantorecrm.payload.dao.projection;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Feb 2023
 **/
public interface ProductItemDeliveryProjection {
    Long getProductItemId();
    String getProduct();
    Double getProductPrice();
    Double getProductAmount();
    Long getWarehouseId();
    String getWarehouseName();
}
