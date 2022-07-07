/**
 * Controller that receives the requests
 *
 * @author Renato Ponce
 * @version 1.0
 * @since 2022-06-24
 */

package com.nttdata.apirestcredit.controller;

import com.nttdata.apirestcredit.dto.FilterDto;
import com.nttdata.apirestcredit.model.Credit;
import com.nttdata.apirestcredit.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/credit")
public class CreditController {

    @Autowired
    private CreditService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Credit>>> list() {
        Flux<Credit> fxCredits = service.listAll();

        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxCredits));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Credit>> getById(@PathVariable("id") String id) {
        return service.getById(id)
                .map(c -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)
                ); //Mono<ResponseEntity<Credit>>
    }

    @GetMapping("/{contractNumber}")
    public Mono<ResponseEntity<Credit>> getByPan(@PathVariable("contractNumber") String contractNumber) {
        return service.getByContractNumber(contractNumber)
                .map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p)
                ); //Mono<ResponseEntity<CreditCard>>
    }

    @PostMapping
    public Mono<ResponseEntity<Credit>> register(@RequestBody Credit credit, final ServerHttpRequest req) {
        return service.create(credit)
                .map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Credit>> update(@PathVariable("id") String id, @RequestBody Credit credit) {

        Mono<Credit> monoBody = Mono.just(credit);
        Mono<Credit> monoBD = service.getById(id);

        return monoBD
                .zipWith(monoBody, (bd, c) -> {
                    bd.setId(id);
                    bd.setAmount(c.getAmount());
                    bd.setCapital(c.getCapital());
                    bd.setAmountInitial(c.getAmountInitial());
                    bd.setChargeDay(c.getChargeDay());
                    bd.setCommission(c.getCommission());
                    bd.setContractNumber(c.getContractNumber());
                    bd.setCustomer(c.getCustomer());
                    bd.setDebitor(c.isDebitor());
                    bd.setInterestRate(c.getInterestRate());
                    return bd;
                })
                .flatMap(service::update) //bd->service.modificar(bd)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(new ResponseEntity<Credit>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/customer/{customerId}")
    public Mono<ResponseEntity<Flux<Credit>>> getByCustomer_Id(@PathVariable("customerId") String customerId) {
        Flux<Credit> fxCredits = service.getByCustomer_Id(customerId);

        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxCredits));
    }

    @GetMapping("/expiredDebt/customer/{customerId}")
    public Mono<ResponseEntity<Flux<Credit>>> expiredDebt(@PathVariable("customerId") String customerId) {
        Flux<Credit> fxCredits = service.getByCustomer_Id(customerId);

        return Mono.just(ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxCredits.filter(c -> {
                    long diff = 0;
                    Date nowDate = new Date();
                    if (c.getPaymentDate() != null) {
                        diff = c.getExpiredDate().getTime() - c.getPaymentDate().getTime();
                    } else {
                        diff = c.getExpiredDate().getTime() - nowDate.getTime();
                    }
                    TimeUnit time = TimeUnit.DAYS;
                    long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);

                    return diffrence < 0;
                })));
    }

    @PostMapping("/reporting")
    public Mono<ResponseEntity<Flux<Credit>>> reporting(@RequestBody FilterDto filter) {
        Flux<Credit> fxCredits = service.findByCreationDateBetween(filter);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fxCredits)
        );

    }

}
