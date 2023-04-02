package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ClientDto;
import khantorecrm.payload.dto.PaymentDto;
import khantorecrm.service.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "client")
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER', 'LOADER', 'SUPER_LOADER')")
public class ClientController {
    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAllClients() {
        return OwnResponse.ALL_DATA.setData(service.getAllInstances()).handleResponse();
    }

    @GetMapping(value = "{id}")
    public HttpEntity<?> getClientById(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(
                service.getInstanceWithId(id)
        ).handleResponse();
    }

    @PostMapping(value = "create")
    public HttpEntity<?> createClient(@RequestBody @Valid ClientDto dto) {
        return service.create(dto).handleResponse();
    }

    @PutMapping(value = "{id}")
    public HttpEntity<?> updateClient(@RequestBody @Valid ClientDto dto, @PathVariable Long id) {
        return service.update(dto, id).handleResponse();
    }

    @DeleteMapping(value = "{id}")
    public HttpEntity<?> deleteClient(@PathVariable Long id) {
        return service.delete(id).handleResponse();
    }

    @PostMapping(value = "{id}/pay")
    public HttpEntity<?> paymentToBalance(@RequestBody @Valid PaymentDto dto, @PathVariable Long id) {
        return service.paymentToBalance(id, dto).handleResponse();
    }

    @GetMapping(value = "debt-clients")
    public HttpEntity<?> debtClients() {
        return OwnResponse.ALL_DATA
                .setMessage("All the debt clients")
                .setData(service.debtClients())
                .handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }
}
