package dl.news.portal.domain.service.impl;

import dl.news.portal.aspect.annotation.CascadeDelete;
import dl.news.portal.domain.entity.mongo.Client;
import dl.news.portal.domain.repository.mongo.ClientRepository;
import dl.news.portal.domain.service.ClientService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void create(Client client) {
        clientRepository.save(client);
    }


    @Override
    public void delete(Client client) {
        clientRepository.delete(client);
    }

    @Override
    public Optional<Client> findById(ObjectId id) {
        return clientRepository.findById(id);
    }

    @Override
    public Long count() {
        return clientRepository.count();
    }
}
