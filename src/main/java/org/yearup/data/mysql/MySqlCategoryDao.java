package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // Create an empty list for Category objects
        List<Category> categories = new ArrayList<>();

        // Create SQL string
        String sql = "SELECT * FROM categories";

        // Try to get a connection
        try (Connection connection = getConnection())
        {
            // Create the PreparedStatement with the SQL query String
            PreparedStatement statement = connection.prepareStatement(sql);

            // Execute the query and try to get the query results from the DB
            // Use a try with resources to autoclose the ResultSet
            try (ResultSet row = statement.executeQuery())
            {
                // Loop through each result row and add the Category to the list
                while (row.next()){
                    // Use the mapRow method to add results to the list
                    categories.add(mapRow(row));
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        // Return the list
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // Create a SQL query String
        String sql = "SELECT * FROM categories " +
                "WHERE category_id = ?";

        // Try to get a connection
        try(Connection connection = getConnection())
        {

            // Create the PreparedStatement with the SQL query String
            PreparedStatement statement = connection.prepareStatement(sql);

            // Replace placeholder value
            statement.setInt(1, categoryId);

            // Execute the query and try to get the ResultSet
            try(ResultSet row = statement.executeQuery())
            {
                // If there are results get the Category values from the results row
                if (row.next())
                {
                    // return the Category
                    return mapRow(row);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // Create the SQL query String
        String sql = "INSERT INTO categories(name, description) " +
                "VALUES(? , ?);";

        // Get a connection
        try (Connection connection = getConnection())
        {
            // Create the prepared statement and specify return of generated keys
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Replace placeholder values
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            // Execute the INSERT statement and store the rows affected
            int rowsAffected = statement.executeUpdate();

            // If the INSERT was successful
            if (rowsAffected > 0) {
                // Get the generated keys
                try (ResultSet generatedKeys = statement.getGeneratedKeys())
                {
                    if (generatedKeys.next())
                    {
                        int id = generatedKeys.getInt(1);
                        // Return the new Category
                        return getById(id);
                    }
                }
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category update(int categoryId, Category category)
    {
        // Create SQL query String
        String sql = "UPDATE categories " +
                "SET name = COALESCE(?, name) " +
                "   ,description =  COALESCE(?, description) " +
                "WHERE category_id = ?";

        // Get a connection
        try(Connection connection = getConnection())
        {
            // Create the PreparedStatement with the SQL query String
            PreparedStatement statement = connection.prepareStatement(sql);

            // Replace placeholder values
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            // Execute the query
            statement.executeUpdate();

            // Return the category (Check if multiple queries are fine)
            return getById(categoryId);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // Create the SQL query String
        String sql = "DELETE FROM categories " +
                "WHERE category_id = ?";

        // Get a connection
        try (Connection connection = getConnection())
        {
            // Create the PreparedStatement
            PreparedStatement statement = connection.prepareStatement(sql);
            // Replace placeholder value
            statement.setInt(1, categoryId);
            // Execute the query
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
