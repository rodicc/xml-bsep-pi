package dto;

import java.sql.Date;

public class ReportDto {
	
	private Date beginDate;
	private Date endDate;
	private String br_racuna;
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getBr_racuna() {
		return br_racuna;
	}
	public void setBr_racuna(String br_racuna) {
		this.br_racuna = br_racuna;
	}
	
	
}
