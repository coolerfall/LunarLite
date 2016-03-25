package com.coolerfall.lunarlite.ui.widget.picker;

/**
 * Picker scrolled listener interface.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface OnPickerScrollListener {
	/**
	 * Callback method to be invoked when scrolling started.
	 *
	 * @param picker the picker view whose state has changed.
	 */
	void onScrollingStarted(PickerView picker);

	/**
	 * Callback method to be invoked when scrolling ended.
	 *
	 * @param picker the picker view whose state has changed.
	 */
	void onScrollingFinished(PickerView picker);
}
