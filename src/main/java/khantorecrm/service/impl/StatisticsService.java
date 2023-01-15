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
    public OwnResponse getMainPageStatistics() {
        final Map<String, Double> mainPageStatistics = new HashMap<>();
        final Long countOfClients           = repository.countClients();
        final Long countOfDebtClients       = repository.countOfDebtClients();
        final Double sumOfAllTheDebtSums    = repository.sumOfAllDebtSumOfClients();
        final Double sumOfAllThPaidSaleSums = repository.sumOfAllPaidSumsOfSale();


        mainPageStatistics.put("countOfClients",            countOfClients         == null ? 0.0 : countOfClients.doubleValue());
        mainPageStatistics.put("countOfDebtClients",        countOfDebtClients     == null ? 0.0 : countOfDebtClients.doubleValue());
        mainPageStatistics.put("sumOfAllTheDebtSums",       sumOfAllTheDebtSums    == null ? 0.0 : sumOfAllTheDebtSums);
        mainPageStatistics.put("sumOfAllThPaidSaleSums",    sumOfAllThPaidSaleSums == null ? 0.0 : sumOfAllThPaidSaleSums);

        return OwnResponse.ALL_DATA.setMessage("Main page statistics").setData(mainPageStatistics);
    }

    @Override
    public OwnResponse getAllClientByBoughtProducts() {
        return OwnResponse.ALL_DATA.setMessage("Clients by bought products").setData(repository.allClientsByBoughtProducts());
    }
}