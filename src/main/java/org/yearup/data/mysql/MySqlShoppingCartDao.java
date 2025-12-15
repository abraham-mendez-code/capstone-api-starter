package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource){
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        ShoppingCart cart = new ShoppingCart();

        String sql = """
                SELECT
                    *
                FROM
                    shopping_cart
                    NATURAL JOIN products
                WHERE
                    user_id = ?;
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            )
        {
            statement.setInt(1, userId);
            try (ResultSet results = statement.executeQuery())
            {
                while (results.next())
                {
                    cart.add(mapRow(results));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return cart;
    }


    protected static ShoppingCartItem mapRow(ResultSet row) throws SQLException
    {
        int userId = row.getInt("user_id");
        int productId = row.getInt("product_id");
        int quantity = row.getInt("quantity");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String subCategory = row.getString("subcategory");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        Product product = new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);

        shoppingCartItem.setProduct(product);
        shoppingCartItem.setQuantity(quantity);

        return shoppingCartItem;
    }
}
