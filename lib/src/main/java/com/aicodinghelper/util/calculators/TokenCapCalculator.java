package com.aicodinghelper.util.calculators;

import com.aicodinghelper.Constants;
import com.aicodinghelper.database.model.Subscriptions;
import com.aicodinghelper.util.ProductIDSubscriptionAdapter;

public class TokenCapCalculator {

    private static Integer freeWeeklyCap = Constants.Subscription.Cap.WEEKLY_FREE;
    private static Integer lowWeeklyCap = Constants.Subscription.Cap.WEEKLY_LOW;
    private static Integer lowMonthlyCap = Constants.Subscription.Cap.MONTHLY_LOW;
    private static Integer mediumWeeklyCap = Constants.Subscription.Cap.WEEKLY_MEDIUM;
    private static Integer mediumMonthlyCap = Constants.Subscription.Cap.MONTHLY_MEDIUM;
    private static Integer highWeeklyCap = Constants.Subscription.Cap.WEEKLY_HIGH;
    private static Integer highMonthlyCap = Constants.Subscription.Cap.MONTHLY_HIGH;


    public static Integer getChatCapFromAppStoreProductID(String appStoreProductID) {
        // Get subscription
        Subscriptions subscription = ProductIDSubscriptionAdapter.getSubscription(appStoreProductID);

        // Return cap for subscription
        switch (subscription) {
            case HIGH_MONTHLY:
                return highMonthlyCap;
            case HIGH_WEEKLY:
                return highWeeklyCap;
            case MEDIUM_MONTHLY:
                return mediumMonthlyCap;
            case MEDIUM_WEEKLY:
                return mediumWeeklyCap;
            case LOW_MONTHLY:
                return lowMonthlyCap;
            case LOW_WEEKLY:
                return lowWeeklyCap;
        }

        return null;
    }

    public static Integer getFreeChatCap() {
        return freeWeeklyCap;
    }

}
