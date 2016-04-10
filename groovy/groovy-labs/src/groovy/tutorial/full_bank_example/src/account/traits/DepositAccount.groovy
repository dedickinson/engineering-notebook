package account.traits

import account.interfaces.Deposit

trait DepositAccount implements Deposit {
    Integer deposit(Integer amount) {
        this.setBalance this.balance + amount
    }
}
