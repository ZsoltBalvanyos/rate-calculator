package com.zsoltbalvanyos.domain;

import lombok.Data;

public class Model {

    @Data
    static public class RequestedAmount {
        final public double value;
    }

    @Data
    static public class Lender {
        final public String name;
    }

    @Data
    static public class Rate {
        final public double value;
    }

    @Data
    static public class Available {
        final public double value;
    }

    @Data
    static public class Offer implements Comparable<Offer> {
        final public Lender lender;
        final public Rate rate;
        final public Available available;

        @Override
        public int compareTo(Offer o) {
            return Double.compare(this.rate.value, o.rate.value);
        }
    }

    @Data
    static public class RepaymentDetails {
        final public Rate rate;
        final public double monthlyRepayment;
        final public double totalRepayment;
    }
}

