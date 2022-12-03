package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.RoleName;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "role")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseEntity implements GrantedAuthority {
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, length = NamingConstants.MODEL_ENUM_LENGTH)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName.name();
    }
}
