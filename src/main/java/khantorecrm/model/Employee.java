package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.base.BaseWithCreatedBy;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "employee")
//caching
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseWithCreatedBy {
    @Column(name = "employee_name", length = NamingConstants.MODEL_NAME_LENGTH)
    private String name;

    @Column(name = "employee_phone_number", unique = true, length = NamingConstants.MODEL_NUMBER_LENGTH)
    private String phoneNumber;

    @Column(name = "employee_comment", length = NamingConstants.MODEL_COMMENT_LENGTH)
    private String comment;

    @OneToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST})
    private Balance balance;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("employeeId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("name", this.getName());
        result.put("phoneNumber", this.getPhoneNumber());
        result.put("comment", this.getComment());
        result.put("balance", this.getBalance().getAmount());
        result.put("createdBy", this.getCreatedBy().getName());
        return result;
    }
}
