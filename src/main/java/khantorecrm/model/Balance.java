package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "balance")
@NoArgsConstructor
@AllArgsConstructor
public class Balance extends BaseEntity {
    @Column(name = "balance_amount")
    private Double amount = 0.0;
}
