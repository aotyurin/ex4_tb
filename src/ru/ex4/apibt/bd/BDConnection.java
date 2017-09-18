package ru.ex4.apibt.bd;

import ru.ex4.apibt.log.Logs;

import java.sql.*;

public class BDConnection {

    public static void init() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try
        {

            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:ex4base.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");

            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
                // read the result set
                Logs.info("name = " + rs.getString("name"));
                Logs.info("id = " + rs.getInt("id"));
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            Logs.error(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                Logs.error(e.getMessage());
            }
        }
    }

}
