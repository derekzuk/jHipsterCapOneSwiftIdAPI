package com.jhipsterswiftid.web.rest.dto;

public class WebHook {
    private String webhookId;
    private String callbackUrl;
    private String eventType;

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "WebHook{" +
            "webhookId='" + webhookId + '\'' +
            ", callbackUrl='" + callbackUrl + '\'' +
            ", eventType='" + eventType + '\'' +
            '}';
    }
}
