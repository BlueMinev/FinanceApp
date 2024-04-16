import java.util.ArrayList;


public class expenseTracker {
    // creating data structers needed
    public enum expGroups {HOME,BILL,GROCERIES,EATOUT,TRANSPORT,ENTERTAINMENT,HEALTH,SHOPPING,GENERAL}
    public enum expBillTypes{WEEKLY,BIWEEKLY,MONTHLY, QUARTERLY,ANNUAL,NA}
    record expenseRecord (
            double expAmnt,
            expGroups expGroup,
            expBillTypes expBillType,
            String expDate
    ){}
// added and example expense record.
    public static void main(String[] args) {
    ArrayList<expenseRecord> exampleExpense = new ArrayList<>();
        exampleExpense.add(new expenseRecord(
                1234.69,
                expGroups.BILL,
                expBillTypes.MONTHLY,
                "2014-04-27"
        ));

        exampleExpense.add(new expenseRecord(
                500.00,
                expGroups.BILL,
                expBillTypes.MONTHLY,
                "2014-05-15"
        ));

        exampleExpense.add(new expenseRecord(
                200.00,
                expGroups.GROCERIES,
                expBillTypes.NA,
                "2014-05-01"
        ));

        exampleExpense.add(new expenseRecord(
                50.00,
                expGroups.ENTERTAINMENT,
                expBillTypes.NA,
                "2014-05-10"
        ));

        exampleExpense.add(new expenseRecord(
                300.00,
                expGroups.TRANSPORT,
                expBillTypes.NA,
                "2014-05-03"
        ));
        exampleExpense.add(new expenseRecord(
                750.00,
                expGroups.HOME,
                expBillTypes.MONTHLY,
                "2014-05-20"
        ));

        exampleExpense.add(new expenseRecord(
                35.00,
                expGroups.GROCERIES,
                expBillTypes.NA,
                "2014-05-05"
        ));

        exampleExpense.add(new expenseRecord(
                80.00,
                expGroups.EATOUT,
                expBillTypes.NA,
                "2014-05-08"
        ));

        exampleExpense.add(new expenseRecord(
                150.00,
                expGroups.TRANSPORT,
                expBillTypes.WEEKLY,
                "2014-05-10"
        ));

        exampleExpense.add(new expenseRecord(
                100.00,
                expGroups.ENTERTAINMENT,
                expBillTypes.NA,
                "2014-05-15"
        ));

        exampleExpense.add(new expenseRecord(
                50.00,
                expGroups.HEALTH,
                expBillTypes.NA,
                "2014-05-12"
        ));

        exampleExpense.add(new expenseRecord(
                200.00,
                expGroups.SHOPPING,
                expBillTypes.NA,
                "2014-05-22"
        ));

        exampleExpense.add(new expenseRecord(
                50.00,
                expGroups.GENERAL,
                expBillTypes.NA,
                "2014-05-25"
        ));
    // filter with streams
        //TODO work out how to implement dates correctly so i can filter by dates.
        //TODO create filters that take an input e.g. custom selection of groups
    }
}
