package io.quarkuscoffeeshop.customermocker.domain;

public enum CustomerVolume {

    DEAD(120), DEV(5), SLOW(20), MODERATE(45), BUSY(30), WEEDS(10);

    private final int delay;

    CustomerVolume(int delayTime) {
        this.delay = delayTime;
    }

    public int getDelay() {
        return this.delay;
    }
}
