package khantorecrm.service.functionality;

import khantorecrm.payload.dao.OwnResponse;

public interface Creatable<D> {
    OwnResponse create(D dto);
}
