package com.aicodinghelper;


import com.oaigptconnector.model.request.chat.completion.CompletionRole;

import java.net.URI;

public final class Constants {

    private Constants() {
    }

    public static final class Additional {

        public static final int functionCallGenerationTokenLimit = 16384;
        public static final int functionCallDefaultTemperature = 1;
        public static final int tokenEstimationTolerance = 250;

    }

    public static final class Subscription {

        public final static class Cap {

            public static final Integer WEEKLY_FREE = 5000000;
            public static final Integer MONTHLY_LOW = 5000;
            public static final Integer WEEKLY_LOW = MONTHLY_LOW / 4;
            public static final Integer MONTHLY_MEDIUM = 50000;
            public static final Integer WEEKLY_MEDIUM = MONTHLY_MEDIUM / 4;
            public static final Integer MONTHLY_HIGH = 500000;
            public static final Integer WEEKLY_HIGH = MONTHLY_HIGH / 4;

        }

        public static final class ID {

            public static final String WEEKLY_LOW = "aicodinghelperweeklylow";
            public static final String MONTHLY_LOW = "aicodinghelpermonthlylow";
            public static final String WEEKLY_MEDIUM = "aicodinghelperweeklymedium";
            public static final String MONTHLY_MEDIUM = "aicodinghelpermonthlymedium";
            public static final String WEEKLY_HIGH = "aicodinghelperweeklyhigh";
            public static final String MONTHLY_HIGH = "aicodinghelpermonthlyhigh";

        }

    }

    public static final class PY {

        public static final String tokenCounterFilepath = "token_counter.py";

    }

    /* In-App Purchases Pricing */
    public static final int DEFAULT_PRICE_INDEX = 0;
//    public static final String WEEKLY_PRICE = "6.95";
//    public static final String WEEKLY_NAME = "chitchatultra";
//    public static final String MONTHLY_PRICE = "19.99";
//    public static final String MONTHLY_NAME = "ultramonthly";
//    public static final String YEARLY_PRICE = "49.99";
//    public static final String YEARLY_NAME = "chitchatultrayearly";

    /* Tiered Limits */
    public static final int Response_Token_Limit_GPT_3_Turbo_Free = 240;
    public static final int Response_Token_Limit_GPT_3_Turbo_Paid = 1400;
    public static final int Response_Token_Limit_GPT_4_Free = 400;
    public static final int Response_Token_Limit_GPT_4_Paid = 1000;
    public static final int Response_Token_Limit_GPT_4_Vision_Free = 400;
    public static final int Response_Token_Limit_GPT_4_Vision_Paid = 400;

    public static final int Character_Limit_GPT_3_Turbo_Free = 1000;
    public static final int Character_Limit_GPT_3_Turbo_Paid = 2800;
    public static final int Character_Limit_GPT_4_Free = 400;
    public static final int Character_Limit_GPT_4_Paid = 1400;
    public static final int Character_Limit_GPT_4_Vision_Free = 1400;
    public static final int Character_Limit_GPT_4_Vision_Paid = 1400;

    public static final int Image_Token_Count = 400;

    public static final int Chat_Context_Select_Query_Limit = 80;

    /* Delays and Cooldowns */
    public static final int Transaction_Status_Apple_Update_Cooldown = 1700;

    /* Caps */
    public static final int Cap_Free_Total_Essays = 3; // This is just a constant sent to the device, which handles everything
    public static final Integer Cap_Chat_Daily_Free = 20;
    public static final Integer Cap_Chat_Daily_Paid = null;
    public static final int Cap_Chat_Daily_Paid_Legacy = -1; //-1 is unlimited

    /* URIs for HTTPSServer */
    class URIs {

        public static final String CALCULATE_TOKENS_URI = "/calculateTokens";
        public static final String GENERATE_CODE_URI = "/generateCode";
        public static final String GET_CHAT_STREAM_URI = "/streamChat";
        public static final String GET_IMPORTANT_CONSTANTS_URI = "/getImportantConstants";
        public static final String GET_IS_ACTIVE_URI = "/getIsActive";
        public static final String GET_REMAINING_TOKENS_URI = "/getRemainingTokens";
        public static final String PLAN_CODE_GENERATION = "/planCodeGeneration";
        public static final String REGISTER_USER_URI = "/registerUser";
        public static final String REGISTER_TRANSACTION_URI = "/registerTransaction";
        public static final String SUBMIT_FEEDBACK = "/submitFeedback";
        public static final String VALIDATE_OPEN_AI_KEY = "/validateOpenAIKey";

    }

    /* Legacy URIs for HTTPServer */
    public static final String GET_DISPLAY_PRICE_URI = "/getDisplayPrice";
    public static final String GET_SHARE_URL_URI = "/getShareURL";
    public static final String VALIDATE_AND_UPDATE_RECEIPT_URI_LEGACY = "/validateAndUpdateReceipt";

    /* Share URL */
    public static final String SHARE_URL = "https://apps.apple.com/us/app/chit-chat-ai-writing-author/id1664039953";

    /* Policy Retrieval Constants */

    /* MySQL Constants */
    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/aicodinghelper_schema";

    /* Apple Server Constants */
    public static final String Apple_Bundle_ID = "com.acapplications.AICodingHelper";
    public static final String Apple_Sandbox_Storekit_Base_URL = "https://api.storekit-sandbox.itunes.apple.com";
    public static final String Apple_Storekit_Base_URL = "https://api.storekit.itunes.apple.com";
    public static final String Apple_In_Apps_URL_Path = "/inApps";
    public static final String Apple_V1_URL_Path = "/v1";
    public static final String Apple_Subscriptions_URL_Path = "/subscriptions";
    public static final String Apple_Get_Subscription_Status_V1_Full_URL_Path = Apple_In_Apps_URL_Path + Apple_V1_URL_Path + Apple_Subscriptions_URL_Path;
    public static final String Apple_SubscriptionKey_JWS_Path = "keys/SubscriptionKey_QPW9RRX3KS.p8";

    public static final String Sandbox_Apple_Verify_Receipt_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    public static final String Apple_Verify_Receipt_URL = "https://buy.itunes.apple.com/verifyReceipt";
    public static long APPLE_TIMEOUT_MINUTES = 4;

    /* ChatSonic Server Constants */
    public static URI CHATSONIC_URI = URI.create("https://api.writesonic.com/v2/business/content/chatsonic?engine=premium");

    /* OpenAI Constants */
    public static URI OPENAI_URI = URI.create("https://api.openai.com/v1/chat/completions");
    public static long AI_TIMEOUT_MINUTES = 4;
    public static String DEFAULT_MODEL_NAME = "gpt-3.5-turbo";//"gpt-4";
    public static String PAID_MODEL_NAME = "gpt-4-1106-preview";
    public static String DEFAULT_BEHAVIOR = null;
    public static CompletionRole LEGACY_DEFAULT_ROLE = CompletionRole.USER;
    public static int DEFAULT_TEMPERATURE = 0;

}
