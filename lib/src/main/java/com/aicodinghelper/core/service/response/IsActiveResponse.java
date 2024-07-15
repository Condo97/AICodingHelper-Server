package com.aicodinghelper.core.service.response;

import com.aicodinghelper.database.model.Subscriptions;

public class IsActiveResponse {

    boolean isActive;
    Subscriptions subscription;

    public IsActiveResponse() {

    }

    public IsActiveResponse(boolean isActive, Subscriptions subscription) {
        this.isActive = isActive;
        this.subscription = subscription;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Subscriptions getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscriptions subscription) {
        this.subscription = subscription;
    }

}
