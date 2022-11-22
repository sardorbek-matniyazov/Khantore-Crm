package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.OutputType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "output")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Output extends BaseEntity {
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    private ProductItem productItem;

    @Column(name = "output_amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)

    @Column(name = "output_type", length = 10, nullable = false)
    private OutputType type;
}
