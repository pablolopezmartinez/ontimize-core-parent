package com.ontimize.util.rule;

import java.util.List;

public interface IRules {

	public List getEvents();

	public void addEvent(IEvent event);

	@Override
	public String toString();

}
