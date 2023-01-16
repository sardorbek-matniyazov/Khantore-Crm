package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;

public interface IStatisticsService {
    OwnResponse mainPageStatistics();

    OwnResponse allClientByBoughtProducts();

    OwnResponse benefitBySoldProducts();


}
