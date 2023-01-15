package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ClientDto;
import khantorecrm.service.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "client")
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER')")
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
}
