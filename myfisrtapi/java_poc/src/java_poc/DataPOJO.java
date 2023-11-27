package java_poc;

public class DataPOJO {
	private String Record_Type;
	@Override
	public String toString() {
		return "" + Record_Type + "," + lineNumber + "," + refDocNum
				+ "," + city + "," + amount + "";
	}
	private int lineNumber;
	private String refDocNum;
	private String city;
	private double amount;
	public Object getRefDocNum;
	
	public DataPOJO(String record_Type, int lineNumber, String refDocNum, String city, double amount) {
		super();
		Record_Type = record_Type;
		this.lineNumber = lineNumber;
		this.refDocNum = refDocNum;
		this.city = city;
		this.amount = amount;
	}
	
	public String getRecord_Type() {
		return Record_Type;
	}
	public void setRecord_Type(String record_Type) {
		Record_Type = record_Type;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getRefDocNum() {
		return refDocNum;
	}
	public void setRefDocNum(String refDocNum) {
		this.refDocNum = refDocNum;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}