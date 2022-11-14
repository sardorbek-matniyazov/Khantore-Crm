package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "warehouse")
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse extends BaseEntity {
    @Column(name = "wr_name", length = NamingConstants.MODEL_NAME_LENGTH)
    private String name;
}
