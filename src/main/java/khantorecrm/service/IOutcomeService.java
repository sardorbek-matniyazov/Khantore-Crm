package khantorecrm.service;

import khantorecrm.model.Outcome;

import java.util.List;
import java.util.Map;

public interface IOutcomeService {
    Map<String, String> getTypes();
    List<Outcome> getAllInstances(String start, String end);
}
