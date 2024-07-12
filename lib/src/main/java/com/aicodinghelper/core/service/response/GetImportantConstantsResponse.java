package com.aicodinghelper.core.service.response;

import com.aicodinghelper.Constants;
import com.aicodinghelper.keys.Keys;

public class GetImportantConstantsResponse {

    private final String sharedSecret = Keys.sharedAppSecret;

    private final String weeklyLowProductID = Constants.Subscription.ID.WEEKLY_LOW;
    private final String monthlyLowProductID = Constants.Subscription.ID.MONTHLY_LOW;
    private final String weeklyMediumProductID = Constants.Subscription.ID.WEEKLY_MEDIUM;
    private final String monthlyMediumProductID = Constants.Subscription.ID.MONTHLY_MEDIUM;
    private final String weeklyHighProductID = Constants.Subscription.ID.WEEKLY_HIGH;
    private final String monthlyHighProductID = Constants.Subscription.ID.MONTHLY_HIGH;

    private final Integer weeklyLowTokens = Constants.Subscription.Cap.WEEKLY_LOW;
    private final Integer weeklyMediumTokens = Constants.Subscription.Cap.WEEKLY_MEDIUM;
    private final Integer weeklyHighTokens = Constants.Subscription.Cap.WEEKLY_HIGH;
    private final Integer monthlyLowTokens = Constants.Subscription.Cap.MONTHLY_LOW;
    private final Integer monthlyMediumTokens = Constants.Subscription.Cap.MONTHLY_MEDIUM;
    private final Integer monthlyHighTokens = Constants.Subscription.Cap.MONTHLY_HIGH;

    private final String shareURL = Constants.SHARE_URL;
    private final int freeEssayCap = Constants.Cap_Free_Total_Essays;

    public GetImportantConstantsResponse() {
        
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    public String getWeeklyLowProductID() {
        return weeklyLowProductID;
    }

    public String getMonthlyLowProductID() {
        return monthlyLowProductID;
    }

    public String getWeeklyMediumProductID() {
        return weeklyMediumProductID;
    }

    public String getMonthlyMediumProductID() {
        return monthlyMediumProductID;
    }

    public String getWeeklyHighProductID() {
        return weeklyHighProductID;
    }

    public String getMonthlyHighProductID() {
        return monthlyHighProductID;
    }

    public Integer getWeeklyLowTokens() {
        return weeklyLowTokens;
    }

    public Integer getWeeklyMediumTokens() {
        return weeklyMediumTokens;
    }

    public Integer getWeeklyHighTokens() {
        return weeklyHighTokens;
    }

    public Integer getMonthlyLowTokens() {
        return monthlyLowTokens;
    }

    public Integer getMonthlyMediumTokens() {
        return monthlyMediumTokens;
    }

    public Integer getMonthlyHighTokens() {
        return monthlyHighTokens;
    }

    public String getShareURL() {
        return shareURL;
    }

    public int getFreeEssayCap() {
        return freeEssayCap;
    }

}