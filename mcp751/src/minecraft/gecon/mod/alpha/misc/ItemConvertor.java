package gecon.mod.alpha.misc;

public class ItemConvertor {
	public static String transactionToDate(String transaction) {
		String out = "";

		out += transaction.substring(5, 7)+ "/";
		out += transaction.substring(8, 10)+ "/";
		out += transaction.substring(2, 4);
		
		return out;
	}
	
	public static String transactionToTime(String transaction) {
		String out = transaction.substring(11);
		return out;
	}
}
