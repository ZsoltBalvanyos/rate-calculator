package com.zsoltbalvanyos;

import com.zsoltbalvanyos.domain.Model.*;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

public interface Market {

    Option<RepaymentDetails> offerStream(String source, RequestedAmount amount, int duration);

}
