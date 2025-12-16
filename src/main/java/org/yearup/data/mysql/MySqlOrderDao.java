package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    private static final double SHIPPING_FEE = 10.99;

    public MySqlOrderDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public Order checkOut(int userId, ShoppingCart cart, Profile profile)
    {

        return null;
    }

    private boolean addOrder(int userId, Profile profile) throws SQLException {
        String sqlInsertOrder = """
               INSERT INTO
                   orders(user_id, date, address, city, state, zip, shipping_amount)
                   VALUES(?, NOW(), ?, ?, ?, ?, ?);
               """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlInsertOrder);
        )
        {
            statement.setInt(1, userId);
            statement.setString(2, profile.getAddress());
            statement.setString(3, profile.getCity());
            statement.setString(4, profile.getState());
            statement.setString(5, profile.getZip());
            statement.setDouble(6, SHIPPING_FEE);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }

    }

    private boolean addOrderItems()
    {
        String sqlInsertOrderItems = """
               INSERT INTO
               	    order_line_items(order_id, product_id, sales_price, quantity, discount)
               	    VALUES(?, ?, ?, ?, ?);
               """;



    }
}
