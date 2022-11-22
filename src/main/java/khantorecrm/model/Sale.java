package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "sale")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sale extends BaseEntity {
    @OneToOne(optional = false)
    private Output output;

    @ManyToOne(optional = false)
    private Client client;

    private Double price;
    private Double debtPrice;
    private Double paidPrice;
    private Double productPrice;
}
