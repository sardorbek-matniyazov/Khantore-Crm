package khantorecrm.payload.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
public class OwnResponse {
    public static final OwnResponse ALL_DATA = new OwnResponse("There are all the Data");
    public static final OwnResponse PRODUCT_ALREADY_EXISTS = new OwnResponse("The product already exists in the database", HttpStatus.BAD_REQUEST.value(), false);
    public static final OwnResponse WAREHOUSE_ALREADY_EXISTS = new OwnResponse("The warehouse already exists in the database", HttpStatus.BAD_REQUEST.value(), false);
    public static final OwnResponse CREATED_SUCCESSFULLY = new OwnResponse("Created Successfully", HttpStatus.CREATED.value(), true);
    public static final OwnResponse UPDATED_SUCCESSFULLY = new OwnResponse("Updated Successfully");
    public static final OwnResponse PRODUCT_NOT_FOUND = new OwnResponse("The product not found", HttpStatus.NOT_FOUND.value(), false);
    public static final OwnResponse ERROR = new OwnResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR.value(), false);
    public static final OwnResponse INGREDIENTS_NOT_FOUND = new OwnResponse("The ingredients not found", HttpStatus.NOT_FOUND.value(), false);
    public static final OwnResponse WAREHOUSE_ITEMS_TYPE_NOT_EQUAL = new OwnResponse("The warehouse items type not equal", HttpStatus.BAD_REQUEST.value(), false);
    public static final OwnResponse WAREHOUSE_NOT_FOUND = new OwnResponse("Warehouse not found", HttpStatus.NOT_FOUND.value(), false);
    public static final OwnResponse PRODUCT_ALREADY_EXISTS_IN_THE_WAREHOUSE = new OwnResponse("The item product already in the warehouse", HttpStatus.BAD_REQUEST.value(), false);

    private String message;
    @JsonIgnore
    private Integer status = HttpStatus.OK.value();
    private boolean success = true;
    private Object data = null;

    public OwnResponse(String message, Integer status, boolean success) {
        this.message = message;
        this.status = status;
        this.success = success;
    }

    public OwnResponse(String message) {
        this.message = message;
    }

    public OwnResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public OwnResponse setMessage(String data) {
        this.message = data;
        return this;
    }

    public HttpEntity<OwnResponse> handleResponse() {
        return ResponseEntity.status(this.status).body(this);
    }
}