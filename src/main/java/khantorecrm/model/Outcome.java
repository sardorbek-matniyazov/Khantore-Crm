package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
import khantorecrm.model.enums.OutcomeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Outcome extends BaseWithCreatedBy {
    @Column(name = "outcome_amount", nullable = false)
    private double amount;

    @ManyToOne()
    private User user;

    @Column(name = "outcome_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OutcomeType type;

    @Column(name = "outcome_comment")
    private String comment;

    public Outcome(double amount, OutcomeType type, String comment) {
        this.amount = amount;
        this.type = type;
        this.comment = comment;
    }

    @JsonValue
    public Map<String, Object> toJson(){
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.getId());
        response.put("type", this.getType().name());
        response.put("user", this.getUser() == null ? "none" : getUser().getName());
        response.put("amount", this.getAmount());
        response.put("createdBy", this.getCreatedBy());
        response.put("createdAt", this.getCreatedAt());
        response.put("comment", this.getComment());
        return response;
    }
}
