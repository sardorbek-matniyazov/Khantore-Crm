package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "product_item")
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem extends BaseEntity {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = false)
    private Product itemProduct;

    @Column(name = "item_amount")
    private Double itemAmount = 0.0;

    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = false)
    private Warehouse warehouse;

    @OneToMany(orphanRemoval = true, mappedBy = "productItem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    private Set<Ingredient> ingredients;
}
