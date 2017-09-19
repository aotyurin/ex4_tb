package ru.ex4.apibt.dao;

import ru.ex4.apibt.bd.JdbcTemplate;
import ru.ex4.apibt.bd.PreparedParamsSetter;
import ru.ex4.apibt.dto.PairSettingDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PairSettingDao {
    private JdbcTemplate jdbcTemplate;

    public PairSettingDao() {
        try {
            this.jdbcTemplate = JdbcTemplate.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PairSettingDto getPairSettingByPair(String pair) {
        try {
            String sql = "SELECT\n" +
                    "  pair,\n" +
                    "  minQuantity,\n" +
                    "  maxQuantity,\n" +
                    "  minPrice,\n" +
                    "  maxPrice,\n" +
                    "  minAmount,\n" +
                    "  maxAmount \n" +
                    "FROM Pair_Setting WHERE pair = :pair";

            PreparedParamsSetter prs = new PreparedParamsSetter();
            prs.setValues("pair", pair);

            ResultSet resultSet = jdbcTemplate.executeQuery(sql, prs);
            if (resultSet != null) {
                List<PairSettingDto> pairSettingDtos = new ArrayList<>();
                while (resultSet.next()) {
                    pairSettingDtos.add(new PairSettingDto(resultSet.getString("pair"),
                            resultSet.getFloat("minQuantity"),
                            resultSet.getFloat("maxQuantity"),
                            resultSet.getFloat("minPrice"),
                            resultSet.getFloat("maxPrice"),
                            resultSet.getFloat("minAmount"),
                            resultSet.getFloat("maxAmount")));
                }
                if (pairSettingDtos.size() > 1 || pairSettingDtos.isEmpty()) {
                    throw new RuntimeException("pairSetting sql: записей не найдено или их более одной");
                }
                return pairSettingDtos.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
