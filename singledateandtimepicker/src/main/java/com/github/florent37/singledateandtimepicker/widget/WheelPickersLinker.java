package com.github.florent37.singledateandtimepicker.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WheelPickersLinker<T extends LinkableWheelPicker<Long> & LimitableWheelPicker<Long, ?>> {
	private ArrayList<T> pickers = new ArrayList<>();
	
	private long earlierLimit;
	
	private long laterLimit;
	
	private long currentTime;
	
	public void setEarlierLimit(long earlierLimit) {
		this.earlierLimit = earlierLimit;
	}
	
	public void setLaterLimit(long laterLimit) {
		this.laterLimit = laterLimit;
	}
	
	public void addWheelPicker(T picker) {
		picker.setLimit(earlierLimit, laterLimit);
		
		if (picker instanceof WheelDayPicker) {
			WheelDayPicker dayPicker = (WheelDayPicker)picker;
			
			dayPicker.setOnDaySelectedListener(new WheelDayPicker.OnDaySelectedListener() {
				@Override
				public void onDaySelected(WheelDayPicker picker, int position, String name, Date date) {
					long newTime = getCurrentTime();
					updateCurrentTime(currentTime, newTime);
				}
			});
			dayPicker.setDayFormatter(new SimpleDateFormat("MM/dd(E)"));
		} else if (picker instanceof WheelHourPicker) {
			WheelHourPicker hourPicker = (WheelHourPicker)picker;
			
			hourPicker.setHourChangedListener(new WheelHourPicker.OnHourChangedListener() {
				@Override
				public void onHourChanged(WheelHourPicker picker, int hour) {
					long newTime = getCurrentTime();
					updateCurrentTime(currentTime, newTime);
				}
			});
		} else if (picker instanceof WheelMinutePicker) {
			WheelMinutePicker minPicker = (WheelMinutePicker)picker;
			
			minPicker.setOnMinuteChangedListener(new WheelMinutePicker.OnMinuteChangedListener() {
				@Override
				public void onMinuteChanged(WheelMinutePicker picker, int minutes) {
					long newTime = getCurrentTime();
					updateCurrentTime(currentTime, newTime);
				}
			});
		}
		
		pickers.add(picker);
	}
	
	private void updateCurrentTime(long oldTime, long newTime) {
		for (T picker: pickers) {
			picker.setGlobalValue(newTime);
		}
		currentTime = newTime;
	}
	
	private Long getCurrentTime() {
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