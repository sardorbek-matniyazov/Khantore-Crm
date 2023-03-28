package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;

public interface IStatisticsService {
    OwnResponse mainPageStatistics();

    OwnResponse allClientByBoughtProducts();

    OwnResponse benefitBySoldProducts();

    OwnResponse clientListByPayments();

    OwnResponse productListByAmount();

    OwnResponse productListAboutInput();

    OwnResponse benefitByPaymentAndOutcome();

    OwnResponse sellerListByPayments(Long id, String from, String to);
}
