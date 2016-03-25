package com.coolerfall.lunarlite.ui.widget.picker.adapters;

import android.content.Context;

/**
 * Numeric picker adapter.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class PickerNumericAdapter extends PickerTextAdapter {

	/**
	 * The default min value
	 */
	public static final int DEFAULT_MAX_VALUE = 9;

	/**
	 * The default max value
	 */
	private static final int DEFAULT_MIN_VALUE = 0;

	// Values
	private int minValue;
	private int maxValue;

	// format
	private String format;

	/**
	 * Constructor
	 *
	 * @param context the current context
	 */
	public PickerNumericAdapter(Context context) {
		this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 *
	 * @param context  the current context
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public PickerNumericAdapter(Context context, int minValue, int maxValue) {
		this(context, minValue, maxValue, null);
	}

	/**
	 * Constructor
	 *
	 * @param context  the current context
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 * @param format   the format string
	 */
	public PickerNumericAdapter(Context context, int minValue, int maxValue, String format) {
		super(context);

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < getItemCount()) {
			int value = minValue + index;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	@Override
	public int getItemCount() {
		return maxValue - minValue + 1;
	}
}
