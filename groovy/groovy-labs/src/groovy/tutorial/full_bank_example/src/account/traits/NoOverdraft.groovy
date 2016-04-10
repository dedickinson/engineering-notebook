package account.traits

import account.exceptions.InsufficientFunds
import account.interfaces.Withdrawal;

trait NoOverdraft implements Withdrawal {
	Integer withdraw(Integer amount) throws InsufficientFunds {
		if (this.getBalance() > amount) {
			this.setBalance this.getBalance() - amount
		} else {
			throw new InsufficientFunds()
		}
	}
}

