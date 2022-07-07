/**
 * Interface Service Credit
 *
 * @author Renato Ponce
 * @version 1.0
 * @since 2022-06-24
 */

package com.nttdata.apirestcredit.service;

import com.nttdata.apirestcredit.dto.FilterDto;
import com.nttdata.apirestcredit.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

    Mono<Credit> create(Credit credit);

    Mono<Credit> update(Credit credit);

    Flux<Credit> listAll();

    Mono<Credit> getById(String id);

    Mono<Credit> getByContractNumber(String contractNumber);

    Flux<Credit> getByCustomer_Id(String customerId);

    Flux<Credit> findByCreationDateBetween(FilterDto filter);
}
