@Grab(group='com.google.guava', module='guava', version='17.0')

import groovy.transform.Immutable
import groovy.transform.Canonical

@Immutable
class BankTransaction {
    String customerName
    String customerID
    ArrayList<Integer> notes = []
}

BankTransaction transaction5 = new BankTransaction(customerName: 'Graham Finch', customerID: 'XBANK-32554', notes: [10, 10, 20, 50, 20])
println transaction5

/**
 * The next line will fail as you cannot change a final field
 */
//transaction5.notes = [100, 20]

//The next line appears to work (no exception thrown)
transaction5.notes.add(100)

//But the 100 doesn't appear in notes so it wasn't added
println transaction5.notes


@Canonical
class BrokenBankTransaction {
    final String customerName
    final String customerID
    final ArrayList<Integer> notes = []
}
BrokenBankTransaction transaction6 = new BrokenBankTransaction('Clive Jenkins', 'YBANK-339854', [10, 10, 10])
println transaction6

//This next call will work - proving that the object pointer is final but the array's contents are not!
def result = transaction6.notes.add(100)
println transaction6
println result

// But trying to assign a new array will fail
//transaction6.notes = [20, 20]


@Canonical
class StillBrokenBankTransaction {
    final String customerName
    final String customerID
    final ArrayList<Integer> notes
}
StillBrokenBankTransaction transaction7 = new StillBrokenBankTransaction('Clive Jenkins', 'YBANK-339854', [10, 10, 10])
println transaction7

transaction7.notes.add(100)
println transaction7

import com.google.common.collect.ImmutableList
@Canonical
class FixedBankTransaction {
    final String customerName
    final String customerID
    final ImmutableList<Integer> notes
}
FixedBankTransaction transaction8 = new FixedBankTransaction('Clive Jenkins', 'YBANK-339854', ImmutableList.of(10, 10, 10))
println transaction8
transaction8.notes.add(100)
println transaction8