package khantorecrm.service;

import khantorecrm.model.Outcome;
import khantorecrm.payload.dao.projection.ChartOutcomeProjection;

import java.util.List;
import java.util.Map;

public interface IOutcomeService {
    Map<String, String> getTypes();
    List<Outcome> getAllInstances(String start, String end);

    List<ChartOutcomeProjection> forChart(String start, String end);
}
