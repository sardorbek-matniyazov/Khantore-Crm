package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.LoginDto;
import khantorecrm.payload.dto.RegisterDto;
import khantorecrm.payload.dto.UpdateUserDto;
import khantorecrm.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
@RequestMapping(value = "auth")
public class AuthController {
    private final AuthService service;

    @Autowired
    public AuthController(AuthService authService) {
        this.service = authService;
    }

    @PostMapping(value = "log-in")
    public HttpEntity<?> signUp(@RequestBody @Valid LoginDto dto) {
        return service.login(dto).handleResponse();
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping(value = "register-user")
    public HttpEntity<?> register(@RequestBody @Valid RegisterDto dto) {
        return service.register(dto).handleResponse();
    }

    @GetMapping(value = "all-users")
    public HttpEntity<?> getAllUsers() {
        return OwnResponse.ALL_DATA.setData(service.getAllUsers()).handleResponse();
    }

    @GetMapping(value = "me")
    public HttpEntity<?> getCurrentUser() {
        return OwnResponse.ALL_DATA.setData(service.getCurrentUser()).handleResponse();
    }

    @PutMapping(value = "{id}")
    public HttpEntity<?> editUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto dto) {
        return service.update(dto, id).handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }
}
