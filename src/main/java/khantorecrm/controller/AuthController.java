package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.LoginDto;
import khantorecrm.payload.dto.RegisterDto;
import khantorecrm.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
