/**
 * Repository that stores Credit information
 *
 * @author Renato Ponce
 * @version 1.0
 * @since 2022-06-24
 */

package com.nttdata.apirestcredit.repository;

import com.nttdata.apirestcredit.model.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {

    Mono<Credit> findByContractNumber(String contractNumber);

    Flux<Credit> findByCustomer_Id(String customerId);
    Flux<Credit> findByCreationDateBetween(LocalDate startDate, LocalDate endDate);
}
