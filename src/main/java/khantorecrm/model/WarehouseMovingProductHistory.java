package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity(name = "warehouse_moving_pr")
@Table(name = "warehouse_moving_pr")
public class WarehouseMovingProductHistory extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private Product movingProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    private Warehouse fromWarehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    private Warehouse toWarehouse;

    @Column(name = "moving_amount", nullable = false)
    private Double amount;

    public WarehouseMovingProductHistory(Product itemProduct, Warehouse fromWh, Warehouse toWh, Double amount) {
        this.movingProduct = itemProduct;
        this.fromWarehouse = fromWh;
        this.toWarehouse = toWh;
        this.amount = amount;
    }
}
