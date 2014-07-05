import java.util.Date;

public class ToshlRecord {

	private String date;
	private String tag;
	private String amount;
	private String amount2;
	private String currency;
	private String description;

	public ToshlRecord() {
	}

	public ToshlRecord(String date, String tag, String amount, String amount2, String currency,
			String description) {
		this.date = date;
		this.tag = tag;
		this.amount = amount;
		this.amount2 = amount2;
		this.currency = currency;
		this.description = description;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setAmount2(String amount2) {
		this.amount2 = amount2;
	}
	
	public String getAmount2() {
		return this.amount2;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.date + " | " + this.tag + " | " + this.amount + " | "
				+ this.currency + " | " + this.description;
	}
}