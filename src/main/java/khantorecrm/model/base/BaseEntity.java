package khantorecrm.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import khantorecrm.model.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    @CreatedDate
    private Timestamp createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @LastModifiedDate
    private Timestamp updatedAt;

    @CreatedBy
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User createdBy;

    public BaseEntity(long id) {
        this.id = id;
    }
}
