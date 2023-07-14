package com.writesmith.core.database.ws.managers;

import com.writesmith.core.database.DBManager;
import com.writesmith.model.database.DBRegistry;
import com.writesmith.model.database.objects.Receipt;
import com.writesmith.common.exceptions.DBObjectNotFoundFromQueryException;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.preparedstatement.component.OrderByComponent;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReceiptDBManager extends DBManager {

    public static Receipt getMostRecentReceiptFromDB(Integer userID) throws DBSerializerException, SQLException, IllegalAccessException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException {
//        Receipt receipt = new Receipt(userID, null);

//        receipt.fillMostRecentByColumnNameAndObject("user_id", receipt.getUserID()); // TODO: - Fix the redundancy of supplying the object something it already contains.. user_id can be used to locate the value of the field

        List<Receipt> receipts = selectAllWhereOrderByLimit(
                Receipt.class,
                Map.of(
                        DBRegistry.Table.Receipt.user_id, userID
                ),
                SQLOperators.EQUAL,
                List.of(
                        DBRegistry.Table.Receipt.record_date
                ),
                OrderByComponent.Direction.DESC,
                1
        );

        // If there are no receipts, throw an exception
        if (receipts.size() == 0)
            throw new DBObjectNotFoundFromQueryException(("No most recent receipt found!"));

        // If there is more than one receipt, it shouldn't be a functionality issue at this moment but print to console to see how widespread this is
        if (receipts.size() > 1)
            System.out.println("More than one transaction found when getting most recent receipt, even though there is a limit of one receipt.. This should never be seen!");

        // Return first receipt
        return receipts.get(0);
    }

}