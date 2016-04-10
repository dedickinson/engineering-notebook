/**
 * Provides a basic Bank Account bean
 * @author Duncan Dickinson
 */
class BankAccountBasic {
    /** The unique account identifier */
    def id
    
    /** The name of the account holder */
    def accountName
    
    /** The current balance in the account */
    def balance = 0
}

BankAccountBasic account1 = new BankAccountBasic(id: 1, accountName: "Fred Nurk")
account1.balance = 200
println account1.balance

BankAccountBasic account2 = new BankAccountBasic(id: 1, accountName: "Sally Smith")
account2.balance = 2000
account2.balance = 20000
println account2.balance

BankAccountBasic account3 = new BankAccountBasic()
account3.id = 123
account3.id = 99