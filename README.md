# Data-Processing-and-Storage

## Instructions
1. Clone the respository:
   ```java
   git clone https://github.com/nyndyny/Data-Processing-and-Storage.git
   ```
3. Navigate to the project directory:
    ``` java
   cd Data-Processing-and-Storage
    ```
5. Compile and run the java program
    ```java
    javac InMemoryDBImpl.java
    java InMemoryDBImpl
    ```
6. Observe the output in the console, which demonstrates the functionality of the in-memory database with transactions

## Usage Example
```java
InMemoryDB inmemoryDB = new InMemoryDBImpl();

// Example transactions and queries
inmemoryDB.begin_transaction();
inmemoryDB.put("A", 5);
inmemoryDB.put("A", 6);
inmemoryDB.commit();
System.out.println(inmemoryDB.get("A")); // Should return 6
```

## Code Overview
The implementation uses a HashMap for the main state (data) and a separate HashMap (transactionData) to store changes made within a transaction. The begin_transaction(), commit(), and rollback() methods manage transactions, while get() and put() handle data retrieval and modification.

## Potential Assignment Modifications
To refine this assignment for official use, consider adding detailed documentation for the interface methods, explicitly outlining expected behaviors, exception handling, and practical examples. Expand the scope to cover transaction isolation levels and the implementation of persistent storage mechanisms, providing students with a more comprehensive understanding of database functionality. Additionally, develop a robust testing suite that encompasses various scenarios, encourage support for diverse data types, and introduce challenges related to performance optimization. 
