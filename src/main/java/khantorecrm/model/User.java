package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseEntity {
    @Column(name = "user_name", nullable = false, length = NamingConstants.MODEL_NAME_LENGTH)
    private String name;
    @Column(name = "user_phone_number", nullable = false, unique = true, length = NamingConstants.MODEL_NAME_LENGTH)
    private String phoneNumber;
    @Column(name = "user_password", nullable = false, length = NamingConstants.PASSWORD_LENGTH)
    private String password;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("name", this.name);
        result.put("phoneNumber", this.phoneNumber);

        // todo: role settings should be here
        return result;
    }
}
