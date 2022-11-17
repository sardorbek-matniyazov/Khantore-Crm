package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.ProductType;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "warehouse")
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse extends BaseEntity {
    @Column(name = "wr_name", length = NamingConstants.MODEL_NAME_LENGTH, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_type", length = NamingConstants.MODEL_ENUM_LENGTH)
    private ProductType type;
}
