package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static khantorecrm.utils.constants.NamingConstants.MODEL_NUMBER_LENGTH;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "fact_warehouse_daily")
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "product_item_id", "fact_day" }) })
public class FactProductItemDaily extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private ProductItem productItem;
    @Column(name = "fact_start_amount")
    private Double dayStartAmount;
    @Column(name = "fact_end_amount")
    private Double dayEndAmount;
    @Column(name = "fact_day", length = MODEL_NUMBER_LENGTH)
    private String date; // dd-MM-yyyy
}
