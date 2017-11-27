package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.model.TrendType;
import ru.ex4.apibt.model.custom.Trailing;
import ru.ex4.apibt.util.DateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
                        DateUtil.parse(resultSet.getString("dateCreated")),
                        DateUtil.parse(resultSet.getString("dateNotify")) ));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return trailingList;
    }


    public void save(Trailing trailing) {
        String sql = "INSERT OR REPLACE INTO Stop_Trailing VALUES (:pair, :trendType, :price, :dateCreated, :dateNotify);";

        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("pair", trailing.getPair());
        prs.setValues("trendType", trailing.getTrendType().name());
        prs.setValues("price", trailing.getPrice());
        prs.setValues("dateCreated", DateUtil.format(trailing.getDateCreated()));
        prs.setValues("dateNotify", DateUtil.format(trailing.getDateNotify()));

        jdbcTemplate.executeUpdate(sql, prs);
    }
}
