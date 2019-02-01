package com.zsoltbalvanyos;

import com.zsoltbalvanyos.domain.Model;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

public interface OfferOps {
    default double totalAmount(List<Model.Offer> offers) {
        return Stream.ofAll(offers)
            .map(Model.Offer::getAvailable)
            .map(Model.Available::getValue)
            .fold(0.0, (a, b) -> a + b);
    }
}
