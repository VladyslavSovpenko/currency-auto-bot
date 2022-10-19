package com.telegram.eCurrency;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.telegram.facade.ECurrencyRate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BtcApi {

//    private static final String link = "https://api.bitaps.com/market/v1//ticker/btcusd";
//
//    public static ECurrencyRate getListOfCurrenciesERate() throws IOException, InterruptedException {
//        Gson gson = new Gson();
//
//        HttpClient client = HttpClient.newHttpClient();
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(link))
//                .GET()
//                .build();
//
//        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        List<MarketApiCurrency> currencies = gson.fromJson((JsonElement) response.body(), new TypeToken<List<MarketApiCurrency>>() {
//
//        }.getType());
//
//        ECurrencyRate eCurrencyRate = new ECurrencyRate();
//        for (MarketApiCurrency marketApiCurrency : currencies) {
//            eCurrencyRate.setERate(ECurrencyEnum.btc,
//                    new ECurrencyRate.ERate(marketApiCurrency.getLast(),
//                            marketApiCurrency.getLast_change(),
//                    marketApiCurrency.getOpen(),
//                    marketApiCurrency.getHigh(),
//                    marketApiCurrency.getMin()));
//        }
//
//        System.out.println(eCurrencyRate);
//        return eCurrencyRate;
//    }

    public class MarketApiCurrency {
        private float last; //last price
        private float last_change; //last price change
        private float open; //open price
        private float high; //24 hours max
        private float min; //24 hours min

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

        @Override
        public String toString() {
            return "MarketApiCurrency{" +
                    "last=" + last +
                    ", last_change=" + last_change +
                    ", open=" + open +
                    ", high=" + high +
                    ", min=" + min +
                    '}';
        }
    }
}
