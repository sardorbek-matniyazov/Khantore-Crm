package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.OutcomeDto;
import khantorecrm.service.impl.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER', 'LOADER', 'SUPER_LOADER')")
@RequestMapping(value = "outcome")
public class OutcomeController {
    private final OutcomeService service;

    @Autowired
    public OutcomeController(OutcomeService outcomeService) {
        this.service = outcomeService;
    }

    @PostMapping(value = "create")
    public HttpEntity<?> create(@RequestBody @Valid OutcomeDto dto) {
        return service.create(dto).handleResponse();
    }

    // k
    @GetMapping(value = "all")
    public HttpEntity<?> all(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end){
        return OwnResponse.ALL_DATA.setData(service.getAllInstances(start, end)).handleResponse();
    }

    @DeleteMapping(value = "{id}")
    public HttpEntity<?> delete(@PathVariable long id) {
        return  service.delete(id).handleResponse();
    }

    @GetMapping(value = "for-chart")
    public HttpEntity<?> forChart(@RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end){
        return OwnResponse.ALL_DATA.setData(service.forChart(start, end)).handleResponse();
    }

}
