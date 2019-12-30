package com.ontimize.gui.alarm;

/**
 * @version 1.0
 */
public interface AlarmListener {

	public void alarmFired(Alarm a);

	public void alarmAcknowledge(Alarm a);
}
