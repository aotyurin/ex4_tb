package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.model.custom.LossOrder;
import ru.ex4.apibt.util.DateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LossOrderDao {
    private JdbcTemplate jdbcTemplate;

    public LossOrderDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LossOrder> getLossOrderList() {
        String sql = " SELECT\n" +
                "  pair,\n" +
                "  priceSell,\n" +
                "  quantity,\n" +
                "  type,\n" +
                "  priceStep,\n" +
                "  priceLoss,\n" +
                "  created \n" +
                " FROM Loss_Order ";

        List<LossOrder> lossOrderList = new ArrayList<>();
        ResultSet resultSet = jdbcTemplate.executeQuery(sql);
        try {
            while (resultSet.next()) {
                lossOrderList.add(new LossOrder(
                        resultSet.getString("pair"),
                        resultSet.getBigDecimal("priceSell"),
                        resultSet.getBigDecimal("quantity"),
                        TypeOrder.valueOf(resultSet.getString("type")),
                        resultSet.getBigDecimal("priceStep"),
                        resultSet.getBigDecimal("priceLoss"),
                        DateUtil.parse(resultSet.getString("created")) ));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return lossOrderList;
    }

    public void save(LossOrder lossOrder) {
        String sql = "INSERT OR REPLACE INTO Loss_Order VALUES (:pair, :priceSell, :quantity, :type, :priceStep, :priceLoss, :created);";

        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("pair", lossOrder.getPair());
        prs.setValues("priceSell", lossOrder.getPrice());
        prs.setValues("quantity", lossOrder.getQuantity());
        prs.setValues("type", lossOrder.getType().name());
        prs.setValues("priceStep", lossOrder.getPriceStep());
        prs.setValues("priceLoss", lossOrder.getPriceLoss());
        prs.setValues("created", DateUtil.format(lossOrder.getCreated()));

        jdbcTemplate.executeUpdate(sql, prs);
    }

    public void delete(LossOrder lossOrder) {
        String sql = "DELETE FROM Loss_Order WHERE pair=:pair AND created=:created ;";

        PreparedParamsSetter prs = new PreparedParamsSetter();
        prs.setValues("pair", lossOrder.getPair());
        prs.setValues("created", DateUtil.format(lossOrder.getCreated()));

        jdbcTemplate.executeUpdate(sql, prs);
    }
}
