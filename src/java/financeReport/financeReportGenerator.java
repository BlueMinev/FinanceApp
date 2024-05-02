package financeReport;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.HashMap;

import transactionHandling.*;

public class financeReportGenerator {
    private transactionTracker txnTracker;
    private List<transactionRecord> txtRecords;

    public static void main(String[] args){
        transactionTracker txTrckr = new transactionTracker();
        transactionTracker.dummyPopulate(txTrckr);
        financeReportGenerator frg = new financeReportGenerator(txTrckr);
        
        for (transactionRecord txnRcd : frg.getExpenses(12)) {
            System.out.println(txnRcd);
        }
    }

    /**
     * Constructor
     * @param transactionTracker
     */
    @SuppressWarnings("unchecked") // also i promise this breaks nothing
    public financeReportGenerator(transactionTracker transactionTracker){
        txnTracker = transactionTracker;
        txtRecords = txnTracker.filterTransactions();
    }
    
    /**
     * Retrieves a list of income transaction records based on the specified timeframe.
     *
     * @param  timeframeInMonths  the number of months to go back in time for the transaction records
     * @return                   a list of income transaction records
     */
    @SuppressWarnings("unchecked")
    public List<transactionRecord> getIncomes(int timeframeInMonths){
        List<transactionRecord> incomes = txnTracker.filterTransactions(
            transactionFilters.hasTransactionType(transactionTypes.INCOME), 
            transactionFilters.hasDateAfter(LocalDate.now().minusMonths(timeframeInMonths)));

        return incomes;
    }

    /**
     * returns a map which stores put each transaction to its type
     * 
     * @param timeframeInMonths How many months back you want it to search
     * @return Map of transactionType to List<transactionRecord>
     */
    @SuppressWarnings("unchecked") // i swear this doenst break anything :)
    private Map<transactionTypes, List<transactionRecord>> expenseBreakdown(int timeframeInMonths){
        List<transactionRecord> expenses = txnTracker.filterTransactions(
            transactionFilters.doesNotHaveTransactionType(transactionTypes.INCOME), 
            transactionFilters.hasDateAfter(LocalDate.now().minusMonths(timeframeInMonths)));

        // Map which maps each transactionType to the relevant transactions
        Map<transactionTypes, List<transactionRecord>> expenseMap = new HashMap<transactionTypes, List<transactionRecord>>();

        expenseMap.put(transactionTypes.BILL, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.BILL)));
        expenseMap.put(transactionTypes.EATOUT, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.EATOUT)));
        expenseMap.put(transactionTypes.ENTERTAINMENT, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.ENTERTAINMENT)));
        expenseMap.put(transactionTypes.GENERAL, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.GENERAL)));
        expenseMap.put(transactionTypes.GROCERIES, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.GROCERIES)));
        expenseMap.put(transactionTypes.HEALTH, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.HEALTH)));
        expenseMap.put(transactionTypes.HOME, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.HOME)));
        expenseMap.put(transactionTypes.SHOPPING, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.SHOPPING)));
        expenseMap.put(transactionTypes.TRANSPORT, transactionTracker.filterTransactions(expenses, transactionFilters.hasTransactionType(transactionTypes.TRANSPORT)));

        return expenseMap;
    }

    /**
     * Retrieves a list of expense transaction records based on the specified timeframe.
     *
     * @param  timeframeInMonths  the number of months to go back in time for the transaction records
     * @return                   a list of expense transaction records
     */
    public List<transactionRecord> getExpenses(int timeframeInMonths){
        Collection<List<transactionRecord>> placeholder = expenseBreakdown(timeframeInMonths).values();
        return placeholder.stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of expense transaction records of a specific type based on the specified timeframe.
     *
     * @param  timeframeInMonths  the number of months to go back in time for the transaction records
     * @param  type               the specific type of expense transactions to retrieve
     * @return                   a list of expense transaction records of the specified type
     */
    public List<transactionRecord> getExpensesByType(int timeframeInMonths, transactionTypes type){
        return expenseBreakdown(timeframeInMonths).get(type);
    }


    /**
     * Sets the transaction tracker to null, effectively deleting customer information.
     *
     * @param  None  This function does not take any parameters.
     * @return void  This function does not return any value.
     */
    public void onClose(){
        txnTracker = null; // just deleting customer info to make it safe? idrk
    }
}
