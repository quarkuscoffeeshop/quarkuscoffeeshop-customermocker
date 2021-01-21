package io.quarkuscoffeeshop.customermocker.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.*;

@RegisterForReflection
public class PlaceOrderCommand {

    private final CommandType commandType = CommandType.PLACE_ORDER;
    List<OrderLineItem> baristaItems;
    List<OrderLineItem> kitchenItems;
    private String id;
    private String storeId;
    private OrderSource orderSource;
    private String rewardsId;
    private BigDecimal total;

    public PlaceOrderCommand() {
    }

    public PlaceOrderCommand(String id, String storeId, OrderSource orderSource, String rewardsId, List<OrderLineItem> baristaItems, List<OrderLineItem> kitchenItems, BigDecimal total) {
        this.orderSource = orderSource;
        this.storeId = storeId;
        this.rewardsId = rewardsId;
        this.baristaItems = baristaItems;
        this.kitchenItems = kitchenItems;
        this.total = total;
    }

    public Optional<String> getRewardsId() {
        return Optional.ofNullable(rewardsId);
    }

    public Optional<List<OrderLineItem>> getBaristaItems() {
        return Optional.ofNullable(baristaItems);
    }

    public Optional<List<OrderLineItem>> getKitchenItems() {
        return Optional.ofNullable(kitchenItems);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PlaceOrderCommand.class.getSimpleName() + "[", "]")
                .add("commandType=" + commandType)
                .add("baristaItems=" + baristaItems)
                .add("kitchenItems=" + kitchenItems)
                .add("id='" + id + "'")
                .add("storeId='" + storeId + "'")
                .add("orderSource=" + orderSource)
                .add("rewardsId='" + rewardsId + "'")
                .add("total=" + total)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOrderCommand that = (PlaceOrderCommand) o;
        return commandType == that.commandType &&
                Objects.equals(baristaItems, that.baristaItems) &&
                Objects.equals(kitchenItems, that.kitchenItems) &&
                Objects.equals(id, that.id) &&
                Objects.equals(storeId, that.storeId) &&
                orderSource == that.orderSource &&
                Objects.equals(rewardsId, that.rewardsId) &&
                Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, baristaItems, kitchenItems, id, storeId, orderSource, rewardsId, total);
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public OrderSource getOrderSource() {
        return orderSource;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setBaristaItems(List<OrderLineItem> baristaItems) {
        this.baristaItems = baristaItems;
    }

    public void setKitchenItems(List<OrderLineItem> kitchenItems) {
        this.kitchenItems = kitchenItems;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setOrderSource(OrderSource orderSource) {
        this.orderSource = orderSource;
    }

    public void setRewardsId(String rewardsId) {
        this.rewardsId = rewardsId;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
