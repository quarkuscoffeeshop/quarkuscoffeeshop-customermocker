package io.quarkuscoffeeshop.customermocker.domain;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerNamesTest {

    Logger logger = LoggerFactory.getLogger(CustomerNamesTest.class);

    @Test
    public void test5Names() {
        List<String> names = CustomerNames.randomNames(5);
        assertNotNull(names);
        assertEquals(5, names.size());
        names.forEach(name -> logger.info("{}", name));
    }
}
