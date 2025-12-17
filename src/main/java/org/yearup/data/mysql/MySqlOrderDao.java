package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.*;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public Order checkOut(int userId, Profile profile)
    {
        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(profile.getAddress());
        order.setCity(profile.getCity());
        order.setState(profile.getState());
        order.setZip(profile.getZip());

        try
        {
            addOrder(order);
            addOrderItems(order);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public void addOrder(Order order) throws SQLException
    {
        String sqlInsertOrder = """
               INSERT INTO
                   orders(user_id, date, address, city, state, zip, shipping_amount)
                   VALUES(?, NOW(), ?, ?, ?, ?, ?);
               """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlInsertOrder, Statement.RETURN_GENERATED_KEYS)
        )
        {
            statement.setInt(1, order.getUserId());
            statement.setString(2, order.getAddress());
            statement.setString(3, order.getCity());
            statement.setString(4, order.getState());
            statement.setString(5, order.getZip());
            statement.setDouble(6, order.getShippingFee());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys())
            {
                if (keys.next())
                {
                    int newId = keys.getInt(1);
                    order.setOrderId(newId);
                }
            }
        }

    }

    @Override
    public void addOrderItems(Order order)
    {
        String sqlInsertOrderItems = """
                INSERT INTO
                	order_line_items(order_id, product_id, sales_price, quantity, discount)
                SELECT
                	orders.order_id
                    ,shopping_cart.product_id
                    ,(products.price * (1 - ?))
                    ,shopping_cart.quantity
                    ,(?) as discount
                FROM
                	shopping_cart
                    JOIN orders ON orders.user_id = shopping_cart.user_id
                    JOIN products ON products.product_id = shopping_cart.product_id
                WHERE
                	shopping_cart.user_id = ? AND orders.order_id = ?;
               """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlInsertOrderItems)
            )
        {
            // First 2 statements apply the discount rate currently set to 0
            statement.setDouble(1, 0);
            statement.setDouble(2, 0);
            statement.setInt(3, order.getUserId());
            statement.setInt(4, order.getOrderId());

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

}
