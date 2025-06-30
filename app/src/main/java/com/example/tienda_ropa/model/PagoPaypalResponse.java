package com.example.tienda_ropa.model;

public class PagoPaypalResponse {
    private int code;
    private Data data;
    private String message;

    public int getCode() {
        return code;
    }

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class Data {
        private String id_pago;
        private double igv;
        private String paypal_link;
        private String subtotal;
        private double total;

        public String getId_pago() {
            return id_pago;
        }

        public double getIgv() {
            return igv;
        }

        public String getPaypal_link() {
            return paypal_link;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public double getTotal() {
            return total;
        }
    }
}
