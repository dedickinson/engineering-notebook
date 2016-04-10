package transaction;

abstract public class TransactionItem {
	
	def date
	def amount
	
	abstract String getType()
}
