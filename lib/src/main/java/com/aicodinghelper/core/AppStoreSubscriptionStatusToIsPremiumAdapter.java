package com.aicodinghelper.core;

import com.aicodinghelper.database.model.AppStoreSubscriptionStatus;

import java.util.List;

public class AppStoreSubscriptionStatusToIsPremiumAdapter {

    private static List<AppStoreSubscriptionStatus> premiumStatuses = List.of(
            AppStoreSubscriptionStatus.ACTIVE,
            AppStoreSubscriptionStatus.BILLING_GRACE
    );

    public static Boolean getIsPremium(AppStoreSubscriptionStatus subscriptionStatus) {
        // If premiumStatuses contains subscriptionStatus, return true because user is premium otherwise return false
        if (premiumStatuses.contains(subscriptionStatus))
            return true;

        return false;
    }

}