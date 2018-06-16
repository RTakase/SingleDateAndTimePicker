package com.github.florent37.sample.singledateandtimepicker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.widget.WheelDayPicker;
import com.github.florent37.singledateandtimepicker.widget.WheelHourPicker;
import com.github.florent37.singledateandtimepicker.widget.WheelMinutePicker;
import com.github.florent37.singledateandtimepicker.widget.WheelPickersLinker;

import java.util.Calendar;
import java.util.Date;

public class LimitedDateAndTimePickerDialog extends DialogFragment {
	
	private WheelPickersLinker linker = new WheelPickersLinker();
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.dialog_limited_date_and_time_picker, container, false);
		
		WheelDayPicker dayPicker = (WheelDayPicker)root.findViewById(R.id.day_picker);
		linker.addWheelPicker(dayPicker);
		
		WheelHourPicker hourPicker = (WheelHourPicker)root.findViewById(R.id.hour_picker);
		linker.addWheelPicker(hourPicker);
		
		WheelMinutePicker minPicker = (WheelMinutePicker)root.findViewById(R.id.min_picker);
		minPicker.setStepMinutes(30);
		linker.addWheelPicker(minPicker);
		
		root.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), new Date(linker.getCurrentTime() * 1000).toString(), Toast.LENGTH_SHORT).show();
			}
		});
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		linker.setDefaultDate(cal.getTime());
		
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
	}
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		return dialog;
	}
	
	public static LimitedDateAndTimePickerDialog newInstance(long earlierLimit, long laterLimit) {
		LimitedDateAndTimePickerDialog dialog = new LimitedDateAndTimePickerDialog();
		dialog.linker.setEarlierLimit(earlierLimit);
		dialog.linker.setLaterLimit(laterLimit);
		return dialog;
	}
}