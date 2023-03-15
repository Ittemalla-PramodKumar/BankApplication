package com.example.BankApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {
	
	
	@javax.persistence.Id
	private String id;
	private String event;
	
	public EventLog(String event) {
		this.event = event;
	}

}
