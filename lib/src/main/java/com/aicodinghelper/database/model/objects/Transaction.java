package com.aicodinghelper.database.model.objects;

import com.aicodinghelper.database.model.AppStoreSubscriptionStatus;
import com.aicodinghelper.database.model.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

import java.time.LocalDateTime;

@DBSerializable(tableName = DBRegistry.Table.Transaction.TABLE_NAME)
public class Transaction {

    @DBColumn(name = DBRegistry.Table.Transaction.transaction_id)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.Transaction.user_id)
    private Integer userID;

    @DBColumn(name = DBRegistry.Table.Transaction.appstore_transaction_id)
    private Long appstoreTransactionID;

    @DBColumn(name = DBRegistry.Table.Transaction.product_id)
    private String productID;

    @DBColumn(name = DBRegistry.Table.Transaction.recent_subscription_start_date_epoch)
    private Long recentSubscriptionStartDateEpoch;

    @DBColumn(name = DBRegistry.Table.Transaction.transaction_date)
    private LocalDateTime transactionDate;

    @DBColumn(name = DBRegistry.Table.Transaction.record_date)
    private LocalDateTime recordDate;

    @DBColumn(name = DBRegistry.Table.Transaction.check_date)
    private LocalDateTime checkDate;

    @DBColumn(name = DBRegistry.Table.Transaction.status)
    private AppStoreSubscriptionStatus subscriptionStatus;


    public Transaction() {

    }

    public Transaction(Integer userID, Long appstoreTransactionID, String productID, Long recentSubscriptionStartDateEpoch, LocalDateTime recordDate, AppStoreSubscriptionStatus subscriptionStatus) {
        this.userID = userID;
        this.appstoreTransactionID = appstoreTransactionID;
        this.productID = productID;
        this.recentSubscriptionStartDateEpoch = recentSubscriptionStartDateEpoch;
        this.recordDate = recordDate;
        this.subscriptionStatus = subscriptionStatus;
    }

    public Transaction(Integer id, Integer userID, Long appstoreTransactionID, String productID, Long recentSubscriptionStartDateEpoch, LocalDateTime transactionDate, LocalDateTime recordDate, AppStoreSubscriptionStatus subscriptionStatus) {
        this.id = id;
        this.userID = userID;
        this.appstoreTransactionID = appstoreTransactionID;
        this.productID = productID;
        this.recentSubscriptionStartDateEpoch = recentSubscriptionStartDateEpoch;
        this.transactionDate = transactionDate;
        this.recordDate = recordDate;
        this.subscriptionStatus = subscriptionStatus;
    }

//    public static Transaction withNowRecordDate(Integer userID, Long appstoreTransactionID, String productID, Long recentSubscriptionStartDateEpoch) {
//        return new Transaction(userID, appstoreTransactionID, productID, recentSubscriptionStartDateEpoch, LocalDateTime.now());
//    }

    public Integer getId() {
        return id;
    }

    public Integer getUserID() {
        return userID;
    }

    public Long getAppstoreTransactionID() {
        return appstoreTransactionID;
    }

    public String getProductID() {
        return productID;
    }

    public Long getRecentSubscriptionStartDateEpoch() {
        return recentSubscriptionStartDateEpoch;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public LocalDateTime getRecordDate() {
        return recordDate;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public AppStoreSubscriptionStatus getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setRecentSubscriptionStartDateEpoch(Long recentSubscriptionStartDateEpoch) {
        this.recentSubscriptionStartDateEpoch = recentSubscriptionStartDateEpoch;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public void setSubscriptionStatus(AppStoreSubscriptionStatus subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

//    /* Functionality */
//
//    public static Transaction getMostRecent(Integer userID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        try {
//            Transaction transaction = new Transaction();
//
//            transaction.fillWhereColumnObjectMapOrderByLimit(Map.of(
//                    DBRegistry.Table.Transaction.user_id, userID
//            ), List.of(
//                    DBRegistry.Table.Transaction.record_date
//            ), OrderByComponent.Direction.DESC, 1);
//
//            return transaction;
//        } catch (DBObjectNotFoundFromQueryException e) {
//            // No most recent transaction was found, so return null
//            return null;
//        }
//    }
//
//    public void updateCheckedStatus() throws DBSerializerException, SQLException, InterruptedException {
//        updateWhere( // TODO: Should this be all that is updated? I think so..
//                Map.of(
//                        DBRegistry.Table.Transaction.status, getStatus().getValue(),
//                        DBRegistry.Table.Transaction.check_date, getCheckDate()
//                ),
//                Map.of(
//                        DBRegistry.Table.Transaction.transaction_id, getId()
//                ));
//    }

}
