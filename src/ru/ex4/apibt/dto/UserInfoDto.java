package ru.ex4.apibt.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;

public class UserInfoDto {
    //      идентификатор пользователя
    @JsonProperty("uid")
    private String uid;
    //      дата и время сервера
    private Date serverData;
    //      доступный баланс
    private ArrayList<Balance> balances;
    //      баланс в ордерах
    private ArrayList<Balance> reserved;

    @JsonProperty("server_date")
    private void setServerData(Long serverData) {
        this.serverData = new Date(serverData * 1000L);
    }

    @JsonProperty("balances")
    private void setBalances(LinkedHashMap<Object, Object> balances) {
        this.balances = new ArrayList<>();
        convertObjectToBalance(this.balances, balances);
    }

    @JsonProperty("reserved")
    private void setReserved(LinkedHashMap<Object, Object> reserved) {
        this.reserved = new ArrayList<>();
        convertObjectToBalance(this.reserved, reserved);
    }

    private void convertObjectToBalance(ArrayList<Balance> thisBalance, LinkedHashMap<Object, Object> hashMap) {
        if (hashMap != null) {
            Set<Object> objects = hashMap.keySet();
            for (Object key : objects) {
                Object o = hashMap.get(key);
                Balance pairSum = new Balance(key.toString(), Float.valueOf(o.toString()));
                thisBalance.add(pairSum);
            }
        }
    }


    public class Balance {
        //  валюта
        private String currency;
        //  сумма
        private float amount;


        public Balance() {
        }

        public Balance(String currency, float amount) {
            this.amount = amount;
            this.currency = currency;
        }

        public float getAmount() {
            return amount;
        }

        public String getCurrency() {
            return currency;
        }
    }


    public String getUid() {
        return uid;
    }

    public Date getServerData() {
        return serverData;
    }

    public ArrayList<Balance> getReserved() {
        return reserved;
    }

    public ArrayList<Balance> getBalances() {
        return balances;
    }
}
