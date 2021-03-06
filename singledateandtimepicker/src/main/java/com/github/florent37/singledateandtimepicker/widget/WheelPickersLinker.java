package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WheelPickersLinker {
	private ArrayList<WheelPicker> pickers = new ArrayList<>();
	
	private long earlierLimit;
	
	private long laterLimit;
	
	private long currentTime;
	
	public void setEarlierLimit(long earlierLimit) {
		this.earlierLimit = earlierLimit;
	}
	
	public void setLaterLimit(long laterLimit) {
		this.laterLimit = laterLimit;
	}
	
	public void setDefaultDate(Date date) {
		for (WheelPicker picker: pickers) {
			picker.setDefaultDate(date);
		}
		updateCurrentTime(-1, date.getTime()/1000);
	}
	
	public void addWheelPicker(WheelPicker picker) {
		picker.setLimit(earlierLimit, laterLimit);
		
		if (picker instanceof WheelDayPicker) {
			WheelDayPicker dayPicker = (WheelDayPicker)picker;
			
			dayPicker.setOnDaySelectedListener(new WheelDayPicker.OnDaySelectedListener() {
				@Override
				public void onDaySelected(WheelDayPicker picker, int position, String name, Date date) {
					updateCurrentTime();
				}
			});
			dayPicker.setDayFormatter(new SimpleDateFormat("MM/dd(E)"));
		} else if (picker instanceof WheelHourPicker) {
			WheelHourPicker hourPicker = (WheelHourPicker)picker;
			
			hourPicker.setHourChangedListener(new WheelHourPicker.OnHourChangedListener() {
				@Override
				public void onHourChanged(WheelHourPicker picker, int hour) {
					updateCurrentTime();
				}
			});
		} else if (picker instanceof WheelMinutePicker) {
			WheelMinutePicker minPicker = (WheelMinutePicker)picker;
			
			minPicker.setOnMinuteChangedListener(new WheelMinutePicker.OnMinuteChangedListener() {
				@Override
				public void onMinuteChanged(WheelMinutePicker picker, int minutes) {
					updateCurrentTime();
				}
			});
		}
		
		pickers.add(picker);
	}
	
	private void updateCurrentTime() {
		long newTime = getCurrentTime();
		
		updateCurrentTime(currentTime, newTime);
	}

	public void updateCurrentTime(long oldTime, long newTime) {
		for (WheelPicker picker: pickers) {
			picker.setGlobalValue(newTime);
		}
		currentTime = newTime;
	}
	
	public Long getCurrentTime() {
		long dayInSec = -1, hourInSec = -1, minInSec = -1;
		
		for (LinkableWheelPicker picker: pickers) {
			if (picker instanceof WheelDayPicker) {
				WheelDayPicker dayPicker = (WheelDayPicker)picker;
				
				dayInSec = dayPicker.getCurrentDate().getTime() / 1000;
			} else if (picker instanceof WheelHourPicker) {
				WheelHourPicker hourPicker = (WheelHourPicker)picker;
				
				hourInSec = TimeUnit.SECONDS.convert(hourPicker.getCurrentHour(), TimeUnit.HOURS);
			} else if (picker instanceof WheelMinutePicker) {
				WheelMinutePicker minPicker = (WheelMinutePicker)picker;
				
				minInSec = TimeUnit.SECONDS.convert(minPicker.getCurrentMinute(), TimeUnit.MINUTES);
			}
		}
		
		return dayInSec + hourInSec + minInSec;
	}
}