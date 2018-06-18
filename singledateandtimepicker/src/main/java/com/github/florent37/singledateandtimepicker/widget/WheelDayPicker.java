package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.github.florent37.singledateandtimepicker.DateHelper;
import com.github.florent37.singledateandtimepicker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.github.florent37.singledateandtimepicker.widget.SingleDateAndTimeConstants.*;

public class WheelDayPicker extends WheelPicker<String, Date> {

    private SimpleDateFormat simpleDateFormat;

    private OnDaySelectedListener onDaySelectedListener;

    public WheelDayPicker(Context context) {
        super(context);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        simpleDateFormat = new SimpleDateFormat("EEE d MMM", getCurrentLocale());
    }

    @Override
    protected String initDefault() {
        return getTodayText();
    }

    @NonNull
    private String getTodayText() {
        return getResources().getString(R.string.picker_today);
    }

    public WheelDayPicker setDayFormatter(SimpleDateFormat simpleDateFormat){
        this.simpleDateFormat = simpleDateFormat;
        adapter.setData(generateAdapterValues());
        notifyDatasetChanged();
        return this;
    }

    @Override
    protected void onItemSelected(int position, String item) {
        if (onDaySelectedListener != null) {
            final Date date = convertItemToDate(position);
            onDaySelectedListener.onDaySelected(this, position, item, date);
        }
    }
    
    @Override
    protected List<String> generateAdapterValues() {
        final List<String> days = new ArrayList<>();
        
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -1 * DAYS_PADDING - 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        
        for (int i = (-1) * DAYS_PADDING; i < 0; ++i) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            
            if (!isOutOfLimit(instance.getTime())) {
                days.add(getFormattedValue(instance.getTime()));
            }
        }
        
        //today
        instance.add(Calendar.DAY_OF_MONTH, 1);
        if (!isOutOfLimit(instance.getTime())) {
            days.add(getFormattedValue(instance.getTime()));
        }
        
        for (int i = 0; i < DAYS_PADDING; ++i) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            if (!isOutOfLimit(instance.getTime())) {
                days.add(getFormattedValue(instance.getTime()));
            }
        }
        return days;
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    public Date getCurrentDate() {
        return convertItemToDate(super.getCurrentItemPosition());
    }

    private Date convertItemToDate(int itemPosition) {
        Date date = null;
        final String itemText = adapter.getItemText(itemPosition);
        final Calendar todayCalendar = Calendar.getInstance();

        final int todayPosition = adapter.getData().indexOf(getTodayText());

        if (getTodayText().equals(itemText)) {
            date = todayCalendar.getTime();
        } else {
            try {
                date = simpleDateFormat.parse(itemText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (date != null) {
            //try to know the year
            final Calendar dateCalendar = DateHelper.getCalendarOfDate(date);

            todayCalendar.add(Calendar.DATE, (itemPosition - todayPosition));

            dateCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
            date = dateCalendar.getTime();
        }

        return date;
    }

    public void setTodayText(String todayText) {
        int index = adapter.getData().indexOf(getTodayText());
        if (index != -1) {
            adapter.getData().set(index, todayText);
            notifyDatasetChanged();
        }
    }
    
    @Override
    public void setLimit(Long earlier, Long later) {
        Calendar cal = Calendar.getInstance();
        
        cal.setTimeInMillis(earlier * 1000);
        
        //to display today
        cal.add(Calendar.DATE, -1);
        earlierLimit = cal.getTimeInMillis() / 1000;

        laterLimit = later;
        
        updateAdapter();
    }
    
    @Override
    public boolean isOutOfLimit(Date value) {
        if (getEarlierLimit() == 0 ||
            getLaterLimit() == 0 ||
            getEarlierLimit() > getLaterLimit()) {
            return true;
        }
        
        long time = value.getTime() / 1000;
        return getEarlierLimit() > time || time > getLaterLimit();
    }
    
    public interface OnDaySelectedListener {
        void onDaySelected(WheelDayPicker picker, int position, String name, Date date);
    }
}