package it.eng.giustizia.avvocatura.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;

public class ValidationHandler extends ValidationEventCollector {
	
	private List<ValidationEvent> events=new ArrayList<ValidationEvent>();
	
	@Override
	public boolean handleEvent(ValidationEvent event) {
		events.add(event);
		return true;
	}
	
	@Override
	public boolean 	hasEvents() {
		if (events.size()>0)
			return true;
		else
			return false;
	}
	
	@Override
	public ValidationEvent[] getEvents() {
		return (ValidationEvent[])events.toArray();
	}
	
	public String formatEvents() {
		StringBuffer buffer=new StringBuffer();
		for(ValidationEvent event:events) {
			buffer.append(event.getMessage());
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
