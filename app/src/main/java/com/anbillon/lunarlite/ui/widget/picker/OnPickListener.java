package com.anbillon.lunarlite.ui.widget.picker;

/**
 * Picker item selected listener interface.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface OnPickListener {
  /**
   * Callback method to be invoked when current item selected.
   *
   * @param pickerView the picker view
   * @param position the position of selected item
   */
  void onPick(PickerView pickerView, int position);
}
