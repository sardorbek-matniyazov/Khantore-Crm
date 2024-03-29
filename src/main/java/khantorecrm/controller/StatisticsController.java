package khantorecrm.controller;

import khantorecrm.service.impl.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public HttpEntity<?> mainPageStatistics() {
        return service.mainPageStatistics().handleResponse();
    }

    @GetMapping(value = "client-list-by-bought-products")
    public HttpEntity<?> allClientByBoughtProducts() {
        return service.allClientByBoughtProducts().handleResponse();
    }

    @GetMapping(value = "benefit-by-sold-products")
    public HttpEntity<?> benefitBySoldProducts() {
        return service.benefitBySoldProducts().handleResponse();
    }

    @GetMapping(value = "client-list-by-payments")
    public HttpEntity<?> clientListByPayments() {
        return service.clientListByPayments().handleResponse();
    }

    @GetMapping(value = "product-list-by-amount")
    public HttpEntity<?> productListByAmount() {
        return service.productListByAmount().handleResponse();
    }

    @GetMapping(value = "product-list-about-input")
    public HttpEntity<?> productListAboutInput() {
        return service.productListAboutInput().handleResponse();
    }

    @GetMapping(value = "benefit-by-payment-and-outcome")
    public HttpEntity<?> benefitByPaymentAndOutcome() {
        return service.benefitByPaymentAndOutcome().handleResponse();
    }

    @GetMapping(value = "sellers-expenses/{id}")
    public HttpEntity<?> sellerListByPayments(
            @PathVariable Long id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return service.sellerListByPayments(id, from, to).handleResponse();
    }
}
