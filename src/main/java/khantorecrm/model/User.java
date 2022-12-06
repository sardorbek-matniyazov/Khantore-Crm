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
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseEntity implements UserDetails {
    @Column(name = "user_name", nullable = false, length = NamingConstants.MODEL_NAME_LENGTH)
    private String name;

    @Column(name = "user_phone_number", nullable = false, unique = true, length = NamingConstants.MODEL_NAME_LENGTH)
    private String phoneNumber;

    @Column(name = "user_password", nullable = false, length = NamingConstants.PASSWORD_LENGTH)
    private String password;

    @Column(name = "user_current_token", length = NamingConstants.TOKEN_LENGTH)
    private String currentToken;

    @ManyToOne(optional = false, targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Role role;

    public User(String name, String phoneNumber, String password, Role role) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("name", this.name);
        result.put("phoneNumber", this.phoneNumber);
        result.put("role", this.role.getRoleName());
        result.put("createdBy", this.getCreatedBy().getName());
        result.put("currentToken", this.currentToken);
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
