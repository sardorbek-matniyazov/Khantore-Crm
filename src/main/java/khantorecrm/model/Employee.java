package khantorecrm.model;

import khantorecrm.model.base.BaseEntity;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "employee")
@Table()
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity {
    @Column(name = "employee_name", length = NamingConstants.MODEL_NAME_LENGTH)
    private String name;

    @Column(name = "employee_phone_number", unique = true, length = NamingConstants.MODEL_NUMBER_LENGTH)
    private String phoneNumber;

    @Column(name = "employee_comment", length = NamingConstants.MODEL_COMMENT_LENGTH)
    private String comment;

    @OneToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST})
    private Balance balance;
}
