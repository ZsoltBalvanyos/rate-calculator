package com.zsoltbalvanyos.implementations;

import com.zsoltbalvanyos.OfferOps;
import com.zsoltbalvanyos.PaymentCalculator;
import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;


public class CompoundingInterestCalculator implements PaymentCalculator, OfferOps {
    @Override
    public RepaymentDetails monthlyPayments(List<Offer> offers, int duration) {
        double monthlyPayment =
            offers
                .map(o -> singleOfferPayment(o, duration))
                .fold(0.0, (a, b) -> a + b);
        double totalPayment = monthlyPayment * duration;
        return new RepaymentDetails(getRate(offers), monthlyPayment, totalPayment);
    }

    private double singleOfferPayment(Offer offer, int duration) {
        double rate = offer.rate.value / 12;
        double pow = Math.pow((1 + rate), duration);
        return (offer.available.value * pow * rate) / (pow - 1);
    }

    private Rate getRate(List<Offer> offers) {
        return new Rate(offers
            .map(offer -> offer.available.value / totalAmount(offers) * offer.rate.value)
            .fold(0.0, (a, b) -> a + b)
        );
    }
}
