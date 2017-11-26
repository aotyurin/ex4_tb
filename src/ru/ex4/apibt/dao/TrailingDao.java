package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.model.TrendType;
import ru.ex4.apibt.model.custom.Trailing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrailingDao {
    private JdbcTemplate jdbcTemplate;

    public TrailingDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Trailing> getTrailingList() {
        String sql = " SELECT \n" +
                "  pair, \n" +
                "  trendType, \n" +
                "  price, \n" +
                "  dateCreated, \n" +
                "  dateNotify \n" +
                " FROM Stop_Trailing";

        List<Trailing> trailingList = new ArrayList<>();

        ResultSet resultSet = jdbcTemplate.executeQuery(sql);
        try {
            while (resultSet.next()) {
                trailingList.add(new Trailing(
                        resultSet.getString("pair"),
                        TrendType.valueOf(resultSet.getString("trendType")),
                        resultSet.getBigDecimal("price"),
                        resultSet.getDate("dateCreated"),
                        resultSet.getDate("dateNotify")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trailingList;
    }


}
