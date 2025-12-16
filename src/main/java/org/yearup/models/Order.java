package org.yearup.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Order
{

    private Map<Integer, OrderLineItem> items = new HashMap<>();

    public Map<Integer, OrderLineItem> getItems()
    {
        return items;
    }

    public void setItems(Map<Integer, OrderLineItem> items)
    {
        this.items = items;
    }

    public boolean contains(int productId)
    {
        return items.containsKey(productId);
    }

    public void add(OrderLineItem item)
    {
        items.put(item.getProductId(), item);
    }

    public OrderLineItem get(int productId)
    {
        return items.get(productId);
    }

    public BigDecimal getTotal()
    {

        return items.values()
                .stream()
                .map(OrderLineItem::getLineTotal)
                .reduce( BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));
    }
}
