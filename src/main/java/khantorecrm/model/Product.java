package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.ProductType;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    @Column(name = "product_name", length = NamingConstants.MODEL_NAME_LENGTH)
    private String name;

    @Column(name = "product_price")
    private Double price = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", length = NamingConstants.MODEL_ENUM_LENGTH)
    private ProductType type;
}
