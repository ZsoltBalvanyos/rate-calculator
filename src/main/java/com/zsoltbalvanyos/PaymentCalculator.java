package com.zsoltbalvanyos;

import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;

public interface PaymentCalculator {

    RepaymentDetails monthlyPayments(List<Offer> offers, int duration);

}
