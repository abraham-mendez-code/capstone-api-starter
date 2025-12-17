package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrderController
{
    // CLASS ATTRIBUTES
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ProfileDao profileDao;
    private final ShoppingCartDao shoppingCartDao;

    @Autowired
    public OrderController(OrderDao orderDao, UserDao userDao, ProfileDao profileDao, ShoppingCartDao shoppingCartDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.shoppingCartDao = shoppingCartDao;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Order checkOut(Principal principal) {
        try {
            // get the currently logged-in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            Profile profile = profileDao.getByUserId(userId);

            Order order = orderDao.checkOut(userId, profile);
            shoppingCartDao.clearCart(userId);

            return order;
        }
        catch (Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

}
