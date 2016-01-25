package data;

import java.sql.Date;

public class Query {
	private int queryID;
	private String accountID;
	private String category;
	private String text;
	private Date date;
	private String answer;
	public Query(int queryID, String accountID, String category,
			String text, Date date, String answer) {
		super();
		this.queryID = queryID;
		this.accountID = accountID;
		this.category = category;
		this.text = text;
		this.date = date;
		this.answer = answer;
	}
	
	public int getQueryID() {
		return queryID;
	}
	public String getAccountID() {
		return accountID;
	}
	public String getCategory() {
		return category;
	}
	public String getText() {
		return text;
	}
	public Date getDate() {
		return date;
	}
	public String getAnswer() {
		return answer;
	}
	
}
