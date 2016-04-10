package account

import customer.Customer
import groovy.transform.*

/**
 * @author Duncan Dickinson
 *
 */
@EqualsAndHashCode
@ToString(includeNames=true)
abstract class BankAccount {

    /** The unique account identifier */
    final id
    
    /** The name of the account holder */
    Customer accountHolder
    
    /** The current balance in the account */
    Integer balance = 0
    
    static BankAccount openAccount(Customer customer) {}
	
    static BankAccount closeAccount() {}
}