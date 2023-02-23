package khantorecrm.service.impl;

import khantorecrm.model.Outcome;
import khantorecrm.model.User;
import khantorecrm.model.enums.OutcomeType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dao.projection.ChartOutcomeProjection;
import khantorecrm.payload.dto.OutcomeDto;
import khantorecrm.repository.OutcomeRepository;
import khantorecrm.repository.UserRepository;
import khantorecrm.service.IOutcomeService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.Deletable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static khantorecrm.utils.constants.Constants.A_MONTH;
import static khantorecrm.utils.constants.Statics.getTime;
import static khantorecrm.utils.constants.Statics.isNonDeletable;

@Service
public class OutcomeService
        implements IOutcomeService,
        Creatable<OutcomeDto>,
        Deletable<Long> {
    private final OutcomeRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public OutcomeService(OutcomeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public OwnResponse create(OutcomeDto dto) {
        OutcomeType outcomeType = OutcomeType.valueOf(dto.getType());
        Outcome outcome = new Outcome(dto.getMoneyAmount(), outcomeType, dto.getComment());
        if (outcomeType.equals(OutcomeType.ЗАРПЛАТА)) {
            Optional<User> byId = userRepository.findById(dto.getUserId());
            if (byId.isPresent()) {
                User user = byId.get();
                user.getBalance().setAmount(user.getBalance().getAmount() - dto.getMoneyAmount());
                outcome.setUser(userRepository.save(user));
            } else {
                return OwnResponse.NOT_FOUND.setMessage("User not found");
            }
        }
        repository.save(outcome);
        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    @Override
    public List<Outcome> getAllInstances(String start, String end) {
        if (start == null && end == null) {
            return repository.findAllByCreatedAtBetweenOrderByIdDesc(
                    new Timestamp(System.currentTimeMillis() - A_MONTH * 60 * 1000 * 60 * 60),
                    new Timestamp(System.currentTimeMillis())
            );
        }
        return repository.findAllByCreatedAtBetweenOrderByIdDesc(getTime(start), getTime(end));
    }

    @Override
    public List<ChartOutcomeProjection> forChart(String start, String end) {
        if (start == null && end == null) {
            return repository.sumMoneyByType(
                    new Timestamp(System.currentTimeMillis() - A_MONTH * 60 * 1000 * 60 * 60),
                    new Timestamp(System.currentTimeMillis())
            );
        }
        return repository.sumMoneyByType(getTime(start), getTime(end));
    }

    @Override
    public Map<String, String> getTypes() {
        Map<String, String> types = new HashMap<>();
        for (OutcomeType value : OutcomeType.values()) {
            types.put("name", value.name());
        }
        return types;
    }

    @Override
    public OwnResponse delete(Long id) {
        Optional<Outcome> byId = repository.findById(id);
        if (byId.isPresent()) {
            User creator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Outcome outcome = byId.get();
            if (isNonDeletable(outcome.getCreatedAt().getTime())) {
                return OwnResponse.CANT_DELETE;
            }
            if (outcome.getCreatedBy().getId().equals(creator.getId())) {
                if (outcome.getType().equals(OutcomeType.ЗАРПЛАТА)) {
                    User user = outcome.getUser();
                    user.getBalance().setAmount(user.getBalance().getAmount() + outcome.getAmount());
                    userRepository.save(user);
                }
                repository.delete(outcome);
                return OwnResponse.DELETED_SUCCESSFULLY;
            }
            return OwnResponse.CANT_DELETE;
        }
        return OwnResponse.NOT_FOUND.setMessage("Outcome not found");
    }
}
