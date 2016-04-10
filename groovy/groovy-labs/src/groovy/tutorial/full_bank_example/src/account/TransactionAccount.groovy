package account

import groovy.transform.EqualsAndHashCode
import account.traits.DepositAccount

@EqualsAndHashCode
class TransactionAccount extends BankAccount implements DepositAccount {
}
