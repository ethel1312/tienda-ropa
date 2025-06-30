package com.example.tienda_ropa.model;

public class WebhookRequest {
    private String custom;
    private String payment_status;

    public WebhookRequest(String custom, String payment_status) {
        this.custom = custom;
        this.payment_status = payment_status;
    }

    // Getters y setters si los necesitas
}
