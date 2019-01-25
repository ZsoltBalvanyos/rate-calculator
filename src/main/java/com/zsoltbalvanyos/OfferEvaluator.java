package com.zsoltbalvanyos;

import com.zsoltbalvanyos.domain.Model.Offer;
import com.zsoltbalvanyos.domain.Model.RequestedAmount;
import io.vavr.collection.List;

public interface OfferEvaluator {

    List<Offer> evaluate(RequestedAmount requestedAmount, Offer offer, List<Offer> offers);

}
