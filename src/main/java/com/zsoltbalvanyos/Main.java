package com.zsoltbalvanyos;

import com.zsoltbalvanyos.domain.Model.*;
import com.zsoltbalvanyos.implementations.CSVMarket;
import com.zsoltbalvanyos.implementations.CompoundingInterestCalculator;
import com.zsoltbalvanyos.implementations.LowestRateEvaluator;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class Main {

    final int DURATION = 36;
    final int INCREMENT = 100;
    final int MIN_AMOUNT = 1000;
    final int MAX_AMOUNT = 15000;

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println(
                "Expected two arguments: \n" +
                    "   1. File location \n" +
                    "   2. Requested amount"
            );
        } else {
            Option<Double> optAmount = Try.of(() -> Double.valueOf(args[1])).toOption();
            if(optAmount.isEmpty()) {
                System.out.println("The requested amount is of invalid format.");
            } else {
                Main main = new Main();
                double amount = optAmount.get();
                if(main.validAmount(amount)) {
                    main.launch(args[0], new RequestedAmount(optAmount.get()));
                }
            }
        }
    }

    private boolean validAmount(double amount) {
        if(amount < MIN_AMOUNT) {
            System.out.println("The requested amount has to be over £" + MIN_AMOUNT);
            return false;
        }
        if(amount > MAX_AMOUNT) {
            System.out.println("The requested amount has to be under £" + MAX_AMOUNT);
            return false;
        }
        if(amount % INCREMENT != 0) {
            System.out.println("The requested amount has to be a multiple of £" + INCREMENT);
            return false;
        }
        return true;
    }

    private void launch(String location, RequestedAmount amount) {
        OfferEvaluator evaluator = new LowestRateEvaluator();
        PaymentCalculator calculator = new CompoundingInterestCalculator();
        Market market = new CSVMarket(evaluator, calculator);
        Option<RepaymentDetails> details = market.offerStream(location, amount, DURATION);
        details.forEach(d -> {
            StringBuilder sb = new StringBuilder();
            sb.append("Requested amount: £" + amount.value + "\n");
            sb.append("Rate: " + String.format("%.1f", d.rate.value * 100) + "%\n");
            sb.append("Monthly repayment: £" + String.format("%.2f", d.monthlyRepayment) + "\n");
            sb.append("Total repayment: £" + String.format("%.2f", d.totalRepayment));
            System.out.println(sb);
        });
    }
}
