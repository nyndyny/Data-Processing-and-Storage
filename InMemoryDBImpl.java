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
            throw new IllegalStateException("Transaction not in progress");
        }
        transactionData.put(key, val);
    }

    @Override
    public void begin_transaction() {
        if (inTransaction) {
            throw new IllegalStateException("Another transaction is already in progress");
        }
        inTransaction = true;
        transactionData.clear();
    }

    @Override
    public void commit() {
        if (!inTransaction) {
            throw new IllegalStateException("No open transaction to commit");
        }
        data.putAll(transactionData);
        clearTransaction();
    }

    @Override
    public void rollback() {
        if (!inTransaction) {
            throw new IllegalStateException("No ongoing transaction to rollback");
        }
        clearTransaction();
    }

    private void clearTransaction() {
        transactionData.clear();
        inTransaction = false;
    }

    public static void main(String[] args) {
        InMemoryDB inmemoryDB = new InMemoryDBImpl();

        System.out.println(inmemoryDB.get("A")); // should return null

        try {
            inmemoryDB.put("A", 5); // should throw an error
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        inmemoryDB.begin_transaction();

        inmemoryDB.put("A", 5);
        System.out.println(inmemoryDB.get("A")); // should return null

        inmemoryDB.put("A", 6);

        inmemoryDB.commit();

        System.out.println(inmemoryDB.get("A")); // should return 6

        try {
            inmemoryDB.commit(); // throws an error
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            inmemoryDB.rollback(); // throws an error
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(inmemoryDB.get("B")); // should return null

        inmemoryDB.begin_transaction();
        inmemoryDB.put("B", 10);
        inmemoryDB.rollback();

        System.out.println(inmemoryDB.get("B")); // should return null
    }
}
