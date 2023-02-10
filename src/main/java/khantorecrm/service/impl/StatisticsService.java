package khantorecrm.service.impl;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.repository.StatisticsRepository;
import khantorecrm.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService
        implements IStatisticsService {
    private final StatisticsRepository repository;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.repository = statisticsRepository;
    }

    @Override
    public OwnResponse mainPageStatistics() {
        final Map<String, Double> mainPageStatistics = new HashMap<>();
        final Long countOfClients           = repository.countClients();
        final Long countOfDebtClients       = repository.countOfDebtClients();
        final Double sumOfAllTheDebtSums    = repository.sumOfAllDebtSumOfClients();
        final Double sumOfAllEmployeeDebts  = repository.sumOfAllDebtSumEmployers();


        mainPageStatistics.put("countOfClients",            countOfClients         == null ? 0.0 : countOfClients.doubleValue());
        mainPageStatistics.put("countOfDebtClients",        countOfDebtClients     == null ? 0.0 : countOfDebtClients.doubleValue());
        mainPageStatistics.put("sumOfAllTheDebtSums",       sumOfAllTheDebtSums    == null ? 0.0 : sumOfAllTheDebtSums);
        mainPageStatistics.put("sumOfAllThPaidSaleSums",    sumOfAllEmployeeDebts  == null ? 0.0 : sumOfAllEmployeeDebts);

        return OwnResponse.ALL_DATA.setMessage("Main page statistics").setData(mainPageStatistics);
    }

    @Override
    public OwnResponse allClientByBoughtProducts() {
        return OwnResponse.ALL_DATA.setMessage("Clients by bought products").setData(repository.allClientsByBoughtProducts());
    }

    @Override
    public OwnResponse benefitBySoldProducts() {
        final Double benefitBySoldProducts = repository.benefitBySoldProducts();
        return OwnResponse.ALL_DATA.setMessage("Benefit by sold products").setData(benefitBySoldProducts == null ? 0.0 : benefitBySoldProducts);
    }
}