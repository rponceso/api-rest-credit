/**
 * Implementation Interface Service Credit
 *
 * @author Renato Ponce
 * @version 1.0
 * @since 2022-06-24
 */

package com.nttdata.apirestcredit.service;

import com.nttdata.apirestcredit.dto.FilterDto;
import com.nttdata.apirestcredit.model.Credit;
import com.nttdata.apirestcredit.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements CreditService{

    @Autowired
    private CreditRepository repository;

    @Override
    public Mono<Credit> create(Credit credit) {
        return repository.save(credit);
    }

    @Override
    public Mono<Credit> update(Credit credit) {
        return repository.save(credit);
    }

    @Override
    public Flux<Credit> listAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Credit> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Credit> getByContractNumber(String contractNumber) {
        return repository.findByContractNumber(contractNumber);
    }

    @Override
    public Flux<Credit> getByCustomer_Id(String customerId) {
        return repository.findByCustomer_Id(customerId);
    }

    @Override
    public Flux<Credit> findByCreationDateBetween(FilterDto filter) {
        return repository.findByCreationDateBetween(filter.getStartDate(),filter.getEndDate());
    }
}
