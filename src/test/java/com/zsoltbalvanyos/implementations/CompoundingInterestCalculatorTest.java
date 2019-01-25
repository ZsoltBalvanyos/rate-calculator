package com.zsoltbalvanyos.implementations;

import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CompoundingInterestCalculatorTest {
    CompoundingInterestCalculator calculator = new CompoundingInterestCalculator();

    @Test
    public void test() {
        RepaymentDetails result = calculator.monthlyPayments(
            List.of(
                new Offer(new Lender("Jane"), new Rate(0.069), new Available(480)),
                new Offer(new Lender("Fred"), new Rate(0.071), new Available(520))
            ),
            36
        );

        assertEquals(30.87, result.monthlyRepayment, 0.01);
    }
}
