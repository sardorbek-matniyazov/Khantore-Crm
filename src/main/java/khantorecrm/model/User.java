package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
import khantorecrm.utils.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseWithCreatedBy implements UserDetails {
    @Column(name = "user_name", nullable = false, length = Constants.MODEL_NAME_LENGTH)
    private String name;

    @Column(name = "user_phone_number", nullable = false, unique = true, length = Constants.MODEL_NAME_LENGTH)
    private String phoneNumber;

    @Column(name = "user_password", nullable = false, length = Constants.PASSWORD_LENGTH)
    private String password;

    @Column(name = "user_current_token", length = Constants.TOKEN_LENGTH)
    private String currentToken;

    @ManyToOne(optional = false, targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Role role;

    @Column(name = "user_kpi_percent")
    private Double kpiPercent;

    @OneToOne(
            targetEntity = Balance.class,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private Balance balance;

    public User(String name, String phoneNumber, String password, Role role, Double kpiPercent, Balance balance) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.kpiPercent = kpiPercent;
        this.balance = balance;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("name", this.name);
        result.put("phoneNumber", this.phoneNumber);
        result.put("role", this.role.getRoleName());
        if (this.getCreatedBy() != null) {
            result.put("createdBy", this.getCreatedBy().getName());
        }
        result.put("kpiPercent", kpiPercent);
        return result;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
