import groovy.transform.Immutable
import groovy.transform.Canonical

@Immutable class BankWithdrawal {
    String customerName
    String customerID
    int amount
}

BankWithdrawal transaction1 = new BankWithdrawal(customerName: 'Hank James', customerID: 'ABANK-12345', amount: 120)
println transaction1

/**
 * The next 3 lines will fail as @Immutable will have configured the fields as final
 */
//transaction1.customerName = 'Fred Smith'
//transaction1.customerID = 'OTHERBANK-54321'
//transaction1.amount = 150

/**
 * The next instantiation uses the same values but calls the tuple-style constructor
 */
BankWithdrawal transaction2 = new BankWithdrawal('Hank James', 'ABANK-12345', 120)

/**
 * As @Immutable implements the equals method this next assertion is fine and that
 * the two transactions are equal (the same) - this is true given the fields defined.
 * However, this demonstrates that the class hasn't been properly thought out and probably
 * needed a field to differentiate between the transactions - most likely a date/time stamp
 * or a unique transaction ID.
 */
assert transaction1 == transaction2


@Canonical
class BankDeposit {
    final String customerName
    final String customerID
    final int amount
}

BankDeposit transaction3 = new BankDeposit('Jane Smith', 'XBANK-98765', 250)
println transaction3

/**
 * The next 3 lines will fail as you cannot change a final field
 */
//transaction3.customerName = 'Fred Smith'
//transaction3.customerID = 'OTHERBANK-54321'
//transaction3.amount = 150

//The use of named constructor parameters won't work:
//BankDeposit transaction4 = new BankDeposit(customerName: 'Jane Smith', customerID: 'XBANK-98765', amount: 250)

