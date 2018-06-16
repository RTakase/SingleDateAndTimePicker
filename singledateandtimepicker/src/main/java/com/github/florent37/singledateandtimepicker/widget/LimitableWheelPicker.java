package com.github.florent37.singledateandtimepicker.widget;

import android.support.annotation.NonNull;

public interface LimitableWheelPicker<LimitT, ViewT> {
	@NonNull public LimitT getEarlierLimit();
	@NonNull public LimitT getLaterLimit();
	public void setLimit(LimitT earlier, LimitT later);
	public boolean isOutOfLimit(ViewT value);
}
