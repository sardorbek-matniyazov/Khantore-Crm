package khantorecrm.controller;

import khantorecrm.service.impl.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/statistics")
@PreAuthorize(value = "hasAnyRole('ADMIN')")
public class StatisticsController {
    private final StatisticsService service;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.service = statisticsService;
    }

    @GetMapping(value = "main-page-all")
    public HttpEntity<?> getMainPageStatistics() {
        return service.getMainPageStatistics().handleResponse();
    }

    @GetMapping(value = "client-list-by-bought-products")
    public HttpEntity<?> getAllClientByBoughtProducts() {
        return service.getAllClientByBoughtProducts().handleResponse();
    }
}
