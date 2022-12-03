package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "client")
@Table(name = "client", uniqueConstraints = @UniqueConstraint(
        columnNames = {"client_name", "client_phone"}
))
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client extends BaseEntity {

    @Column(name = "client_name", nullable = false)
    private String name;

    @Column(name = "client_phone", nullable = false)
    private String phone;

    @Column(name = "client_address", nullable = false)
    private String comment = "this is a basic comment ).";

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", length = 10, nullable = false)
    private ClientType type;

    @OneToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private Balance balance;
}
