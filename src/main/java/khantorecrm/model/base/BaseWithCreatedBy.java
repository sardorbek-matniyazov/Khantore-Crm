package khantorecrm.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import khantorecrm.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseWithCreatedBy extends BaseEntity{
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

    @JsonIgnore
    @UpdateTimestamp
    @LastModifiedDate
    private Timestamp updatedAt;

    @CreatedBy
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User createdBy;
}
