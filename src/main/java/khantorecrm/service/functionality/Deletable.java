package khantorecrm.service.functionality;

import khantorecrm.payload.dao.OwnResponse;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 10 Feb 2023
 **/
public interface Deletable <I> {
    OwnResponse delete(I id);
}
