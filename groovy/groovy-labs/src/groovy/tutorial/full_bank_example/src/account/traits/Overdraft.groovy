package account.traits

import account.exceptions.InsufficientFunds
import account.interfaces.Withdrawal;

trait Overdraft implements Withdrawal {
    Integer overdraftAmount = 0
    
    def setOverdraftAmount(Integer amount) {
        this.overdraftAmount = amount
    }
    
    Integer withdraw(Integer amount) throws InsufficientFunds {
        if ((this.getBalance() +  this.getOverdraftAmount()) > amount) {
            this.setBalance this.getBalance() - amount
        } else {
            throw new InsufficientFunds()
        }
    }
}
