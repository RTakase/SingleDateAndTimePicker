package com.github.florent37.singledateandtimepicker.widget;

import android.support.annotation.Nullable;

public interface LinkableWheelPicker<T> {
	@Nullable public T getGlobalValue();
	public void setGlobalValue(T value);
}