package khantorecrm.service.impl;

import khantorecrm.model.enums.ClientType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dao.projection.SellerIncomePayment;
import khantorecrm.repository.StatisticsRepository;
import khantorecrm.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        mainPageStatistics.put("sumOfAllEmployeeDebts",    sumOfAllEmployeeDebts   == null ? 0.0 : sumOfAllEmployeeDebts);

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

    @Override
    public OwnResponse clientListByPayments() {
        return OwnResponse.ALL_DATA.setMessage("Client list by payments").setData(repository.clientListByPayments());
    }

    @Override
    public OwnResponse productListByAmount() {
        return OwnResponse.ALL_DATA.setMessage("Product list by amount").setData(repository.productListByAmount());
    }

    @Override
    public OwnResponse productListAboutInput() {
        return OwnResponse.ALL_DATA.setMessage("Product list about input").setData(repository.productListAboutInput());
    }

    @Override
    public OwnResponse benefitByPaymentAndOutcome() {
        Map<String, Object> mp = new HashMap<>();

        final Double incomePayments     = repository.incomePayments();
        final Double outcomePayments    = repository.outcomePayments();
        final Double sumOfOutcomeAmount = repository.sumOfOutcomeAmount();

        mp.put("incomePayments",     incomePayments     == null ? 0.0 : incomePayments);
        mp.put("outcomePayments",    outcomePayments    == null ? 0.0 : outcomePayments);
        mp.put("sumOfOutcomeAmount", sumOfOutcomeAmount == null ? 0.0 : sumOfOutcomeAmount);


        return OwnResponse.ALL_DATA.setMessage("Benefit by payment and outcome").setData(mp);
    }

    @Override
    public OwnResponse sellerListByPayments(Long id, String from, String to) {
        final Timestamp fromTime;
        final Timestamp toTime;

        try {
            fromTime = getTimeStamp(from);
            toTime = getTimeStamp(to);
        } catch (RuntimeException e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        }

        // payment of seller by time
        final List<SellerIncomePayment> sellerIncomePayment         = repository.sellerListByPayments(id, fromTime, toTime).stream()
                .filter(s -> s.getSumPayment() != null)
                .collect(Collectors.toList());
        // debt of seller by time for basic clients
        final Double debtOfSellerBasics                             = repository.debtOfSeller(id, toTime, ClientType.BASIC.name());
        // debt of seller by time for basic clients
        final Double debtOfSellerPClients                           = repository.debtOfSeller(id, toTime, ClientType.P_CLIENT.name());
        // outcome amount of seller by time
        final Double outcomeAmountOfSeller                          = repository.outcomeAmountOfSeller(id, fromTime, toTime);

        final Map<String, Object> mp = new HashMap<>();
        mp.put("sellerIncomePayment", sellerIncomePayment);
        mp.put("debtOfSellerBasics", debtOfSellerBasics == null ? 0.0 : debtOfSellerBasics);
        mp.put("debtOfSellerPClients", debtOfSellerPClients == null ? 0.0 : debtOfSellerPClients);
        mp.put("outcomeAmountOfSeller", outcomeAmountOfSeller == null ? 0.0 : outcomeAmountOfSeller);

        return OwnResponse
                .ALL_DATA.setMessage("Seller list by payments")
                .setData(mp);
    }

    // time format: yyyy-MM-dd
    private Timestamp getTimeStamp(String date) {
        if (validateTime(date)) {
            return Timestamp.valueOf(String.format("%s 00:00:00", date));
        }
        throw new RuntimeException("Invalid time format");
    }

    private boolean validateTime(String date) {
        if (date == null) {
            return false;
        }
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}