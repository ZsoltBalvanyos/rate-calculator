package com.zsoltbalvanyos.implementations;

import com.zsoltbalvanyos.OfferOps;
import com.zsoltbalvanyos.OfferEvaluator;
import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;

public class LowestRateEvaluator implements OfferEvaluator, OfferOps {

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
}
