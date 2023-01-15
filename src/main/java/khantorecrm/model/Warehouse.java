package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
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
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "warehouse")
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse extends BaseWithCreatedBy {
    @Column(name = "wr_name", length = NamingConstants.MODEL_NAME_LENGTH, unique = true)
    private String name;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_type", length = NamingConstants.MODEL_ENUM_LENGTH)
    private ProductType type;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("warehouseId", super.getId());

        result.put("name", this.getName());
        result.put("type", this.getType());
        return result;
    }
}
