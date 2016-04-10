import groovy.transform.*

/**
 * A very basic counter that distributes identifiers.
 * Be mindful that this is not likely to be thread-safe.
 */
class IdGenerator {
    static private id = 0
    
    static private setId(value) {}
    
    static getId(){
        ++id
    }
}

/**
 * Provides a more useful Bank Account bean that implements the following
 * business rules:
 * <ul>
 *   <li>A unique account ID is allocated once at contruction and can't be edited</li>
 *   <li>All accounts start with a zero balance and can only be changed through deposits and withdrawals</li>
 *   <li>All new accounts must be opened with an account holder's name
 *   <li>Accounts are not allowed to go below zero balance</li>
 * </ul>
 *
 * @author Duncan Dickinson
 */
@EqualsAndHashCode
@ToString(includeNames=true)
class BankAccountExtended {

    /** The unique account identifier */
    final def id = IdGenerator.getId()
    
    /** The name of the account holder */
    def accountName
    
    /** The current balance in the account */
    def balance = 0
    
    /** 
     * All new accounts must be created with an accountName
     * @param accountName     the account holder's name
     */
    BankAccountExtended(accountName){
        this.accountName = accountName
    }
    
    /** 
     * This is an invalid constructor
     * @throws Exception    always thrown
     */
    private BankAccountExtended(){
        throw new Exception("You must call BankAccountExtended.openNewAccount")
    }
    
    /** 
     * Make a withdrawal
     * @param amount        the amount to withdraw
     * @throws Exception    if amount > account balance
     * @return   the balance after the deposit
     */
    def withdraw(amount) throws Exception {
        if (amount < balance) {
            balance -= amount
        } else {
            throw new Exception("Account balance cannot be less than 0")
        }
        return balance
    }
    
    /** 
     * Make a deposit
     * @param    amount    the amount to deposit
     * @return   the balance after the deposit
     */
    def deposit(amount) {
        balance += amount
        return balance
    }
}

def account1 = new BankAccountExtended("Fred Nurk")
account1.deposit 100
account1.withdraw 20
account1.deposit 50
println account1

try {
    account1.withdraw 800
} catch (e) {
    println "The withdrawal failed with error: $e.message"
}


def account2 = new BankAccountExtended()
println account2

