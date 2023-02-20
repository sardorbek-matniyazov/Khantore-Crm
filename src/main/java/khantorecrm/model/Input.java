package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.utils.exceptions.TypesInError;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.EnumType.STRING;
import static khantorecrm.utils.constants.Constants.MODEL_ENUM_LENGTH;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "input")
@NoArgsConstructor
public class Input extends BaseWithCreatedBy {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = false, cascade = MERGE)
    private ProductItem productItem;

    @Column(name = "input_amount")
    private Double amount = 0.0;

    @Enumerated(STRING)
    @Column(name = "input_product_type", length = MODEL_ENUM_LENGTH)
    private ProductType type;

    @Column(name = "input_cr_pr_price")
    private Double currentProductPrice = 0.0;

    @Column(name = "input_cr_pr_ingr_price")
    private Double currentProductIngPrice = 0.0;

    @ManyToOne(cascade = MERGE)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Employee employee;

    @Column(name = "input_status")
    private ActionType status = ActionType.ACCEPTED;

    @Column(name = "input_date_string", length = MODEL_ENUM_LENGTH)
    private String inputDateString;

    public Input(
            ProductItem productItem,
            Double amount,
            ProductType type,
            Double currentProductPrice,
            Employee employee,
            Double currentProductIngPrice,
            ActionType actionType
    ) {
        this.productItem = productItem;
        this.amount = amount;
        this.type = type;
        this.status = actionType;
        this.employee = employee;
        this.currentProductIngPrice = currentProductIngPrice;
        this.currentProductPrice = currentProductPrice;
        setInputDateString();
    }

    public Input setCreatedDate(String time) {
        this.setCreatedAt(getTime(time));
        return this;
    }

    private void setInputDateString() {
        this.inputDateString = convertTimestampToReadableInSqlDaily();
    }

    public static String convertTimestampToReadableInSqlDaily() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(new Date());
    }

    public static Timestamp getTime(String time) {
        if (time == null) return null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            throw new TypesInError("Date time is not parseable, format should be yyyy-MM-dd HH:mm");
        }
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("inputId", super.getId());
        result.put("createdAt", super.getCreatedAt());
        result.put("createdBy", super.getCreatedBy().getName());

        result.put("amount", this.getAmount());
        result.put("product", this.getProductItem().getItemProduct().getName());
        result.put("warehouse", this.getProductItem().getWarehouse().getName());
        result.put("productPrice", this.getCurrentProductPrice());

        if (this.getEmployee() != null) {
            result.put("employee", this.getEmployee().getName());
        }
        return result;
    }
}
