package com.anbillon.lunarlite.ui.widget.picker.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;

import java.util.Arrays;
import java.util.List;

/**
 * The simple Array picker adapter.
 *
 * @param <T> the element type
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class PickerArrayAdapter<T> extends PickerTextAdapter {
	private List<T> mItemList;

	/**
	 * Constructor.
	 *
	 * @param context the current context
	 * @param items   the items
	 */
	public PickerArrayAdapter(Context context, T items[]) {
		super(context);
		mItemList = Arrays.asList(items);
	}

	/**
	 * Constructor with layout resource
	 *
	 * @param context  the context to use
	 * @param resource the resource ID for a layout file containing a TextView to
	 *                 use when instantiating views
	 * @param items    the items to represent in picker view
	 */
	public PickerArrayAdapter(Context context, @LayoutRes int resource, T[] items) {
		super(context, resource);
		mItemList = Arrays.asList(items);
	}

	/**
	 * Constructor with layout resource
	 *
	 * @param context  the context to use
	 * @param resource the resource ID for a layout file containing a TextView to
	 *                 use when instantiating views
	 * @param list    the items to represent in picker view
	 */
	public PickerArrayAdapter(Context context, @LayoutRes int resource, List<T> list) {
		super(context, resource);
		mItemList = list;
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < mItemList.size()) {
			T item = mItemList.get(index);
			if (item instanceof CharSequence) {
				return (CharSequence) item;
			}
			return item.toString();
		}

		return null;
	}

	@Override
	public int getItemCount() {
		return mItemList.size();
	}
}
