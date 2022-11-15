package khantorecrm.service.functionality;

import java.util.List;

public interface InstanceReturnable<M, I> {
    List<M> getAllInstances();
    M getInstanceWithId(I id);
}
