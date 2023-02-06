package khantorecrm.service.impl;

import khantorecrm.model.Balance;
import khantorecrm.model.Client;
import khantorecrm.model.enums.ClientType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ClientDto;
import khantorecrm.repository.ClientRepository;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements
        InstanceReturnable<Client, Long>,
        Creatable<ClientDto> {
    private final ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.repository = clientRepository;
    }

    @Override
    public List<Client> getAllInstances() {
        return repository.findAll();
    }

    @Override
    public Client getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public OwnResponse create(ClientDto dto) {
        if (repository.existsByNameAndPhone(dto.getName(), dto.getPhone())) return OwnResponse.CLIENT_ALREADY_EXISTS;
        repository.save(
                new Client(
                        dto.getName(),
                        dto.getPhone(),
                        dto.getComment(),
                        ClientType.BASIC,
                        new Balance()
                )
        );
        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    public List<Client> debtClients() {
        return repository.findAllByBalance_AmountSmallerThan0();
    }
}
