package com.zsoltbalvanyos.implementations;

import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class LowestRateEvaluatorTest {

    LowestRateEvaluator evaluator = new LowestRateEvaluator();

    @Test
    public void emptyList() {
        Offer offer = new Offer(new Lender(""), new Rate(1), new Available(12));
        List<Offer> result = evaluator.evaluate(new RequestedAmount(1), offer, List.empty());

        assertEquals(List.of(offer), result);
    }

    @Test
    public void totalIsLessThanAmount() {
        Offer offer = new Offer(new Lender(""), new Rate(1), new Available(4));
        List<Offer> offers = List.of(
            new Offer(new Lender(""), new Rate(1), new Available(2)),
            new Offer(new Lender(""), new Rate(2), new Available(3))
        );
        List<Offer> result = evaluator.evaluate(new RequestedAmount(10), offer, offers);

        assertEquals(offers.prepend(offer), result);
    }

    @Test
    public void totalOverspillsAmount() {
        Offer offer = new Offer(new Lender(""), new Rate(1), new Available(4));
        List<Offer> offers = List.of(
            new Offer(new Lender(""), new Rate(1), new Available(2)),
            new Offer(new Lender(""), new Rate(2), new Available(7))
        );
        List<Offer> result = evaluator.evaluate(new RequestedAmount(10), offer, offers);

        assertEquals(offers.prepend(offer), result);
    }

    @Test
    public void offerIsDiscarded() {
        Offer offer = new Offer(new Lender(""), new Rate(4), new Available(4));
        List<Offer> offers = List.of(
            new Offer(new Lender(""), new Rate(1), new Available(5)),
            new Offer(new Lender(""), new Rate(3), new Available(7))
        );
        List<Offer> result = evaluator.evaluate(new RequestedAmount(10), offer, offers);

        assertTrue(offers.containsAll(result));
    }

    @Test
    public void newOfferIncluded_NothingDropsOut() {
        Offer offer = new Offer(new Lender(""), new Rate(2), new Available(4));
        List<Offer> offers = List.of(
            new Offer(new Lender(""), new Rate(1), new Available(5)),
            new Offer(new Lender(""), new Rate(3), new Available(7))
        );
        List<Offer> result = evaluator.evaluate(new RequestedAmount(10), offer, offers);

        assertTrue(offers.prepend(offer).containsAll(result));
    }

    @Test
    public void newOfferIncluded_MaxDropsOut() {
        Offer newOffer = new Offer(new Lender(""), new Rate(2), new Available(6));
        Offer offer1 = new Offer(new Lender(""), new Rate(1), new Available(5));
        Offer offer2 = new Offer(new Lender(""), new Rate(3), new Available(7));

        List<Offer> offers = List.of(offer1, offer2);
        List<Offer> result = evaluator.evaluate(new RequestedAmount(10), newOffer, offers);

        assertTrue(List.of(offer1, newOffer).containsAll(result));
    }

    @Test
    public void newOfferIncluded_MultipleDropsOut() {
        Offer newOffer = new Offer(new Lender(""), new Rate(2), new Available(12));
        Offer offer1 = new Offer(new Lender(""), new Rate(5), new Available(5));
        Offer offer2 = new Offer(new Lender(""), new Rate(3), new Available(7));

        List<Offer> offers = List.of(offer1, offer2);
        List<Offer> result = evaluator.evaluate(new RequestedAmount(10), newOffer, offers);

        assertEquals(List.of(newOffer), result);
    }
}
