package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;

public interface IStatisticsService {
    OwnResponse getMainPageStatistics();

    OwnResponse getAllClientByBoughtProducts();
}
