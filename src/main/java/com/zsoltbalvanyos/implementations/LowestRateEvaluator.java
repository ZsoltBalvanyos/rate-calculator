package com.zsoltbalvanyos.implementations;

import com.zsoltbalvanyos.OfferEvaluator;
import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class LowestRateEvaluator implements OfferEvaluator {

    @Override
    public List<Offer> evaluate(RequestedAmount requestedAmount, Offer offer, List<Offer> offers) {
        return
            totalAmount(offers) < requestedAmount.value ?
                offers.prepend(offer) :
                reselectOffers(requestedAmount, offers.prepend(offer));
    }

    private List<Offer> reselectOffers(RequestedAmount requestedAmount, List<Offer> offers) {
        return offers
            .sorted()
            .foldLeft(List.empty(), (list, o) ->
                totalAmount(list) > requestedAmount.value ?
                    list :
                    list.prepend(o));
    }

    private double totalAmount(List<Offer> offers) {
        return Stream.ofAll(offers)
            .map(Offer::getAvailable)
            .map(Available::getValue)
            .fold(0.0, (a, b) -> a + b);
    }
}
