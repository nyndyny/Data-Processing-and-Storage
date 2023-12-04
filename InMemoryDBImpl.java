import java.util.HashMap;
import java.util.Map;

interface InMemoryDB {
    int get(String key);

    void put(String key, int val);

    void begin_transaction();

    void commit();

    void rollback();
}

public class InMemoryDBImpl implements InMemoryDB {

    private Map<String, Integer> data;
    private Map<String, Integer> transactionData;
    private boolean inTransaction;

    public InMemoryDBImpl() {
        this.data = new HashMap<>();
        this.transactionData = new HashMap<>();
        this.inTransaction = false;
    }

    @Override
    public int get(String key) {
        if (inTransaction && transactionData.containsKey(key)) {
            return transactionData.get(key);
        }
        return data.getOrDefault(key, 0);
    }

    @Override
    public void put(String key, int val) {
        if (!inTransaction) {
            throw new IllegalStateException("Error: Transaction not in progress");
        }
        transactionData.put(key, val);
    }

    @Override
    public void begin_transaction() {
        if (inTransaction) {
            throw new IllegalStateException("Error: Another transaction is already in progress");
        }
        inTransaction = true;
        transactionData.clear();
    }

    @Override
    public void commit() {
        if (!inTransaction) {
            throw new IllegalStateException("Error: No open transaction to commit");
        }
        data.putAll(transactionData);
        clearTransaction();
    }

    @Override
    public void rollback() {
        if (!inTransaction) {
            throw new IllegalStateException("Error: No ongoing transaction to rollback");
        }
        clearTransaction();
    }

    private void clearTransaction() {
        transactionData.clear();
        inTransaction = false;
    }

    public static void main(String[] args) {
        InMemoryDB inmemoryDB = new InMemoryDBImpl();

        System.out.println(inmemoryDB.get("A")); // should return null, because A doesn’t exist in the DB yet

        try {
            inmemoryDB.put("A", 5); // should throw an error because a transaction is not in progress
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        inmemoryDB.begin_transaction(); //starts a new transaction

        inmemoryDB.put("A", 5); // value not committed yet
        System.out.println(inmemoryDB.get("A")); // should return null, because updates to A are not committed yet

        inmemoryDB.put("A", 6); // updating value to 6

        inmemoryDB.commit(); // commits the open transaction

        System.out.println(inmemoryDB.get("A")); // should return 6, that was the last value of A to be committed

        try {
            inmemoryDB.commit(); // throws an error, because there is no open transaction
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            inmemoryDB.rollback(); // throws an error because there is no ongoing transaction
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(inmemoryDB.get("B")); // should return null because B does not exist in the database

        inmemoryDB.begin_transaction(); // starts a new transaction
        inmemoryDB.put("B", 10); // set key B’s value to 10 within the transaction
        inmemoryDB.rollback(); // rollback the transaction - revert any changes made to B

        System.out.println(inmemoryDB.get("B")); // should return null because changes to B were rolled back
    }
}
