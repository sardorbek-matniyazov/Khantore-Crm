package khantorecrm.service;

import khantorecrm.model.Outcome;
import khantorecrm.payload.dao.projection.ChartOutcome;

import java.util.List;
import java.util.Map;

public interface IOutcomeService {
    Map<String, String> getTypes();
    List<Outcome> getAllInstances(String start, String end);

    List<ChartOutcome> forChart(String start, String end);
}
