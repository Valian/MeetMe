package sample.web.app.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleStuff
{
	
	private List<String> listField = new ArrayList<>();
	private Integer intField;
	private String stringField;
	private Date dateField;
	
	public Integer getIntField()
	{
		
		return intField;
		
	}
	
	public void setIntField(Integer intField)
	{
		
		this.intField = intField;
		
	}
	
	public String getStringField()
	{
		
		return stringField;
		
	}
	
	public void setStringField(String stringField)
	{
		
		this.stringField = stringField;
		
	}
	
	public Date getDateField()
	{
		
		return dateField;
		
	}
	
	public void setDateField(Date dateField)
	{
		
		this.dateField = dateField;
		
	}

	public List<String> getListField()
	{
		
		return listField;
		
	}

	public void setListField(List<String> listField)
	{
		
		this.listField = listField;
		
	}

	@Override
	public String toString()
	{
		
		return "SimpleStuff [listField=" + listField + ", intField=" + intField
				+ ", stringField=" + stringField + ", dateField=" + dateField
				+ "]";
		
	}
	
}
