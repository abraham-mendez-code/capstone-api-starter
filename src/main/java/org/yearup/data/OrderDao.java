package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

import java.sql.SQLException;

public interface OrderDao
{

    Order checkOut(int userId, Profile profile);


    void addOrder(Order order) throws SQLException;

    void addOrderItems(Order order);
}
