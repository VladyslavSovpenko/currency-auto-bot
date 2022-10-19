package com.telegram.facade;




import java.util.Optional;
import java.util.TreeMap;

//класс нормализации ответа MarketApi
public class ECurrencyRate {

//    private final Map<ECurrencyEnum, ERate> mapECurrency;
//
//    public ECurrencyRate() {
//        this.mapECurrency = new TreeMap<>();
//    }

    //класс структуры курса e-валюты
    public static class ERate {
        private float last; //last price
        private float last_change; //last price change
        private float open; //open price
        private float high; //24 hours max
        private float min; //24 hours min

        public ERate(float last, float last_change, float open, float high, float min) {
            this.last = last;
            this.last_change = last_change;
            this.open = open;
            this.high = high;
            this.min = min;
        }

        public float getLast() {
            return last;
        }

        public void setLast(float last) {
            this.last = last;
        }

        public float getLast_change() {
            return last_change;
        }

        public void setLast_change(float last_change) {
            this.last_change = last_change;
        }

        public float getOpen() {
            return open;
        }

        public void setOpen(float open) {
            this.open = open;
        }

        public float getHigh() {
            return high;
        }

        public void setHigh(float high) {
            this.high = high;
        }

        public float getMin() {
            return min;
        }

        public void setMin(float min) {
            this.min = min;
        }
    }

//        public void setERate(ECurrencyEnum eCurrency, ERate rate) {
//            mapECurrency.put(eCurrency, rate);
//        }
//
//        public ERate getERate(ECurrencyEnum eCurrency) {
//            return Optional
//                    .ofNullable(mapECurrency.get(eCurrency))
//                    .orElse(new ERate(0f, 0f, 0f, 0f, 0f));
//        }

}
