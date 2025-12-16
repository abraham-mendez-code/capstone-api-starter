package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

public interface OrderDao
{

    Order checkOut(int userId, ShoppingCart cart, Profile profile);

}
