package com.zsoltbalvanyos.implementations;

import com.zsoltbalvanyos.OfferOps;
import com.zsoltbalvanyos.Market;
import com.zsoltbalvanyos.OfferEvaluator;
import com.zsoltbalvanyos.PaymentCalculator;
import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVMarket implements Market, OfferOps {

    final private static Logger logger = LogManager.getLogger(CSVMarket.class);

    final private OfferEvaluator evaluator;
    final private PaymentCalculator calculator;

    public CSVMarket(OfferEvaluator evaluator, PaymentCalculator calculator) {
        this.evaluator = evaluator;
        this.calculator = calculator;
    }

    @Override
    public Option<RepaymentDetails> offerStream(String source, RequestedAmount amount, int duration) {
        try(BufferedReader br = new BufferedReader(new FileReader(source))) {
            List<Offer> emptyList = List.empty();
            return
                Option.of(
                    Stream
                        .ofAll(br.lines())
                        .map(this::buildOffer)
                        .filter(Option::isDefined)
                        .map(Option::get)
                        .foldLeft(emptyList, (list, offer) -> evaluator.evaluate(amount, offer, list)))
                    .map(offers -> trimOffers(offers, amount))
                    .map(offers -> calculator.monthlyPayments(offers, duration));
        } catch (IOException e) {
            logger.error("File " + source + " was not found.");
            return Option.none();
        }
    }

    protected Option<Offer> buildOffer(String line) {
        String[] fields = line.split(",");
        return
            Try.of(() ->
                new Offer(
                    new Lender(fields[0]),
                    new Rate(Double.valueOf(fields[1])),
                    new Available(Double.valueOf(fields[2]))))
                .onFailure(e -> logger.warn("Failed to parse " + line + ". " + e.getMessage()))
                .toOption();
    }

    protected List<Offer> trimOffers(List<Offer> offers, RequestedAmount amount) {
        List<Offer> sortedOffers = offers.sorted();
        Offer worstOffer = sortedOffers.last();

        double overspill = totalAmount(sortedOffers) - amount.value;

        Offer trimmedWorstOffer =
            new Offer(
                worstOffer.lender,
                worstOffer.rate,
                new Available(worstOffer.available.value - overspill)
            );

        return
            sortedOffers
                .dropRight(1)
                .prepend(trimmedWorstOffer);
    }
}