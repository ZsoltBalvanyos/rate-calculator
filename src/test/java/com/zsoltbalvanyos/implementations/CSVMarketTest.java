package com.zsoltbalvanyos.implementations;

import com.zsoltbalvanyos.OfferEvaluator;
import com.zsoltbalvanyos.PaymentCalculator;
import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CSVMarketTest {

    OfferEvaluator evaluator = new LowestRateEvaluator();
    PaymentCalculator calculator = new CompoundingInterestCalculator();
    CSVMarket market = new CSVMarket(evaluator, calculator);

    @Test
    public void parseCorrectLine() {
        assertEquals(
            Option.some(new Offer(
                new Lender("Jon"),
                new Rate(0.02),
                new Available(500.00)
            )),
            market.buildOffer("Jon,0.02,500")
        );
    }

    @Test
    public void noneWhenParsingError() {
        assertEquals(
            Option.none(),
            market.buildOffer("Jon,x,500")
        );
    }

    @Test
    public void noneWhenOutOfBound() {
        assertEquals(
            Option.none(),
            market.buildOffer("Jon")
        );
    }

    @Test
    public void trimOffers() {
        RequestedAmount amount = new RequestedAmount(15);

        Offer offer1 = new Offer(new Lender(""), new Rate(2), new Available(5));
        Offer offer2 = new Offer(new Lender(""), new Rate(3), new Available(7));
        Offer offer3 = new Offer(new Lender(""), new Rate(5), new Available(9));

        Offer expected1 = new Offer(new Lender(""), new Rate(2), new Available(5));
        Offer expected2 = new Offer(new Lender(""), new Rate(3), new Available(7));
        Offer expected3 = new Offer(new Lender(""), new Rate(5), new Available(3));

        List<Offer> expected = List.of(expected1, expected2, expected3);
        List<Offer> result = market.trimOffers(List.of(offer1, offer2, offer3), amount);

        assertTrue(expected.containsAll(result));
    }

}
