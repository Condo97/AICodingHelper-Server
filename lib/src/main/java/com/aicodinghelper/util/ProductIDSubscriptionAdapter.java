package com.aicodinghelper.util;

import com.aicodinghelper.Constants;
import com.aicodinghelper.database.model.Subscriptions;

public class ProductIDSubscriptionAdapter {

    private static final String weeklyLowProductID = Constants.Subscription.ID.WEEKLY_LOW;
    private static final String monthlyLowProductID = Constants.Subscription.ID.MONTHLY_LOW;
    private static final String weeklyMediumProductID = Constants.Subscription.ID.WEEKLY_MEDIUM;
    private static final String monthlyMediumProductID = Constants.Subscription.ID.MONTHLY_MEDIUM;
    private static final String weeklyHighProductID = Constants.Subscription.ID.WEEKLY_HIGH;
    private static final String monthlyHighProductID = Constants.Subscription.ID.MONTHLY_HIGH;

    public static Subscriptions getSubscription(String productID) {
        // Start from high to low and return free if none found
        switch (productID) {
            case monthlyHighProductID:
                return Subscriptions.HIGH_MONTHLY;
            case weeklyHighProductID:
                return Subscriptions.HIGH_WEEKLY;
            case monthlyMediumProductID:
                return Subscriptions.MEDIUM_MONTHLY;
            case weeklyMediumProductID:
                return Subscriptions.MEDIUM_WEEKLY;
            case monthlyLowProductID:
                return Subscriptions.LOW_MONTHLY;
            case weeklyLowProductID:
                return Subscriptions.LOW_WEEKLY;
            default:
                return Subscriptions.FREE;
        }
    }

}
