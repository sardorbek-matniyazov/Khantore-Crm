package khantorecrm.service.functionality;

import khantorecrm.payload.dao.OwnResponse;

public interface Updatable<D, I> {
    OwnResponse update(D dto, I id);
}
