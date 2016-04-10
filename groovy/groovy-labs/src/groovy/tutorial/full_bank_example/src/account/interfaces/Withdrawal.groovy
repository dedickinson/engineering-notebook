package account.interfaces

import account.exceptions.InsufficientFunds

interface Withdrawal {
    Integer withdraw(Integer amount) throws InsufficientFunds
}
