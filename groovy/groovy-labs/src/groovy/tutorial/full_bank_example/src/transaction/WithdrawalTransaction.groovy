package transaction

class WithdrawalTransaction extends TransactionItem {
	public static final String TYPE = "WITHDRAWAL"
	
	@Override
	public String getType() {
		return TYPE
	}

}
