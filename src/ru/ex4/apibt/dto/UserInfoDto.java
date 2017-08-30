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
    private ArrayList<PairSum> balances;
    //      баланс в ордерах
    private ArrayList<PairSum> reserved;

    @JsonProperty("server_date")
    private void setServerData(Long serverData) {
        this.serverData = new Date(serverData * 1000L);
    }

    @JsonProperty("balances")
    private void setBalances(LinkedHashMap<Object, Object> balances) {
        this.balances = new ArrayList<>();
        convertObjectToPairSum(this.balances, balances);
    }

    @JsonProperty("reserved")
    private void setReserved(LinkedHashMap<Object, Object> reserved) {
        this.reserved = new ArrayList<>();
        convertObjectToPairSum(this.reserved, reserved);
    }

    private void convertObjectToPairSum(ArrayList<PairSum> thisPairSum, LinkedHashMap<Object, Object> hashMap) {
        if (hashMap != null) {
            Set<Object> objects = hashMap.keySet();
            for (Object key : objects) {
                Object o = hashMap.get(key);
                PairSum pairSum = new PairSum(key.toString(), Float.valueOf(o.toString()));
                thisPairSum.add(pairSum);
            }
        }
    }


    public class PairSum {
        //  валюта
        private String pair;
        //  сумма
        private float amount;


        public PairSum() {
        }

        public PairSum(String pair, float amount) {
            this.amount = amount;
            this.pair = pair;
        }

        public float getAmount() {
            return amount;
        }

        public String getPair() {
            return pair;
        }
    }


    public String getUid() {
        return uid;
    }

    public Date getServerData() {
        return serverData;
    }

    public ArrayList<PairSum> getReserved() {
        return reserved;
    }

    public ArrayList<PairSum> getBalances() {
        return balances;
    }
}
