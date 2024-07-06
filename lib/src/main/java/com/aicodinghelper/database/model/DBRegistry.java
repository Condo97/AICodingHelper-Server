package com.aicodinghelper.database.model;

public class DBRegistry {

    public class Table {

        public class Chat {

            public static final String TABLE_NAME = "Chat";
            public static final String chat_id = "chat_id";
            public static final String user_id = "user_id";
            public static final String completion_tokens = "completion_tokens";
            public static final String prompt_tokens = "prompt_tokens";
            public static final String date = "date";

        }

        public class Transaction {

            public static final String TABLE_NAME = "Transaction";
            public static final String transaction_id = "transaction_id";
            public static final String user_id = "user_id";
            public static final String product_id = "product_id";
            public static final String recent_subscription_start_date_epoch = "recent_subscription_start_date_epoch";
            public static final String appstore_transaction_id = "appstore_transaction_id";
            public static final String transaction_date = "transaction_date";
            public static final String record_date = "record_date";
            public static final String check_date = "check_date";
            public static final String status = "status";

        }

        public class User_AuthToken {
            public static final String TABLE_NAME = "User_AuthToken";
            public static final String user_id = "user_id";
            public static final String auth_token = "auth_token";
            public static final String creation_date = "creation_date";

        }

    }

}
