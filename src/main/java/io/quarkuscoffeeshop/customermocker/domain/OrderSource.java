package io.quarkuscoffeeshop.customermocker.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum OrderSource {

    COUNTER, WEB, DELIVERY, REMAKE;
}
