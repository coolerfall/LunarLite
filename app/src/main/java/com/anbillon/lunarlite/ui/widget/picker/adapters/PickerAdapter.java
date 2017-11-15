package com.anbillon.lunarlite.ui.widget.picker.adapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Picker items adapter interface.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface PickerAdapter {
  /**
   * Gets items count
   *
   * @return the count of wheel items
   */
  int getItemCount();

  /**
   * Get a View that displays the data at the specified position in the data set
   *
   * @param position the item position
   * @param convertView the old view to reuse if possible
   * @param parent the parent that this view will eventually be attached to
   * @return the picker item view
   */
  View getView(int position, View convertView, ViewGroup parent);

  /**
   * Get a View that displays an empty wheel item placed before the first or after
   * the last wheel item.
   *
   * @param convertView the old view to reuse if possible
   * @param parent the parent that this view will eventually be attached to
   * @return the empty item View
   */
  View getEmptyItem(View convertView, ViewGroup parent);

  /**
   * Update the selected item.
   *
   * @param position the selected position
   */
  void updateSelectedItem(int position);

  /**
   * Register an observer that is called when changes happen to the data used by this adapter.
   *
   * @param observer the observer to be registered
   */
  void registerDataSetObserver(DataSetObserver observer);

  /**
   * Unregister an observer that has previously been registered
   *
   * @param observer the observer to be unregistered
   */
  void unregisterDataSetObserver(DataSetObserver observer);
}
