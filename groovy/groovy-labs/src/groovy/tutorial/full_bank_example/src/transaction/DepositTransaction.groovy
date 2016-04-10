package transaction

class DepositTransaction extends TransactionItem {
	public static final String TYPE = "DEPOSIT"
	
	@Override
	public String getType() {
		return TYPE
	}

}
