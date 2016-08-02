package com.anbillon.lunarlite.ui.widget.picker.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Abstract wheel adapter provides common functionality for adapters.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public abstract class PickerTextAdapter extends PickerBaseAdapter {
	/**
	 * Text view resource. Used as a default view for adapter.
	 */
	public static final int TEXT_VIEW_ITEM_RESOURCE = -1;

	/**
	 * No resource constant.
	 */
	protected static final int NO_RESOURCE = 0;

	private int mTextColor = 0xffd0d0d0;
	private int mTextSize = 16;
	private int mSelectedColor = 0xff00c0c0;

	protected Context mContext;
	protected LayoutInflater mInflater;

	/* tems resources */
	protected int itemResourceId;
	protected int itemTextResourceId;

	/* empty items resources */
	protected int emptyItemResourceId;

	private int mSelectedPosition;

	/**
	 * Constructor
	 *
	 * @param context the current context
	 */
	protected PickerTextAdapter(Context context) {
		this(context, TEXT_VIEW_ITEM_RESOURCE);
	}

	/**
	 * Constructor
	 *
	 * @param context      the current context
	 * @param itemResource the resource ID for a layout file containing a TextView to use when instantiating items views
	 */
	protected PickerTextAdapter(Context context, int itemResource) {
		this(context, itemResource, NO_RESOURCE);
	}

	/**
	 * Constructor
	 *
	 * @param context          the current context
	 * @param itemResource     the resource ID for a layout file containing a TextView to use when instantiating items views
	 * @param itemTextResource the resource ID for a text view in the item layout
	 */
	protected PickerTextAdapter(Context context, int itemResource, int itemTextResource) {
		mContext = context;
		itemResourceId = itemResource;
		itemTextResourceId = itemTextResource;

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Gets text color
	 *
	 * @return the text color
	 */
	public int getTextColor() {
		return mTextColor;
	}

	/**
	 * Sets text color
	 *
	 * @param textColor the text color to set
	 */
	public void setTextColor(@ColorInt int textColor) {
		mTextColor = textColor;
	}

	/**
	 * Gets text size
	 *
	 * @return the text size
	 */
	public int getTextSize() {
		return mTextSize;
	}

	/**
	 * Sets text size
	 *
	 * @param textSize the text size to set
	 */
	public void setTextSize(int textSize) {
		mTextSize = textSize;
	}

	/**
	 * Set the text color in selected item.
	 *
	 * @param color color to set
	 */
	public void setSelectedColor(@ColorInt int color) {
		mSelectedColor = color;
	}

	/**
	 * Sets resource Id for items views
	 *
	 * @param itemResourceId the resource Id to set
	 */
	public void setItemResource(int itemResourceId) {
		this.itemResourceId = itemResourceId;
	}

	/**
	 * Gets resource Id for text view in item layout
	 *
	 * @return the item text resource Id
	 */
	public int getItemTextResource() {
		return itemTextResourceId;
	}

	/**
	 * Sets resource Id for text view in item layout
	 *
	 * @param itemTextResourceId the item text resource Id to set
	 */
	public void setItemTextResource(int itemTextResourceId) {
		this.itemTextResourceId = itemTextResourceId;
	}

	/**
	 * Gets resource Id for empty items views
	 *
	 * @return the empty item resource Id
	 */
	public int getEmptyItemResource() {
		return emptyItemResourceId;
	}

	/**
	 * Sets resource Id for empty items views
	 *
	 * @param emptyItemResourceId the empty item resource Id to set
	 */
	public void setEmptyItemResource(int emptyItemResourceId) {
		this.emptyItemResourceId = emptyItemResourceId;
	}

	/**
	 * Returns text for specified item.
	 *
	 * @param position the item position
	 * @return the text of specified items
	 */
	protected abstract CharSequence getItemText(int position);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position >= 0 && position < getItemCount()) {
			if (convertView == null) {
				convertView = getView(itemResourceId, parent);
			}

			TextView textView = getTextView(convertView, itemTextResourceId);
			if (textView != null) {
				CharSequence text = getItemText(position);
				textView.setText(text);

				if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
					configureTextView(textView);
				}

				if (mSelectedPosition == position) {
					textView.setTextColor(mSelectedColor);
				} else {
					textView.setTextColor(mTextColor);
				}
			}

			return convertView;
		}

		return null;
	}

	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = getView(emptyItemResourceId, parent);
		}

		if (emptyItemResourceId == TEXT_VIEW_ITEM_RESOURCE && convertView instanceof TextView) {
			configureTextView((TextView) convertView);
		}

		return convertView;
	}

	@Override
	public void updateSelectedItem(int position) {
		mSelectedPosition = position;
		notifyDataChanged();
	}

	/**
	 * Configures text view. Is called for the TEXT_VIEW_ITEM_RESOURCE views.
	 *
	 * @param view the text view to be configured
	 */
	protected void configureTextView(TextView view) {
		view.setTextColor(mTextColor);
		view.setGravity(Gravity.CENTER);
		view.setTextSize(mTextSize);
		view.setLines(1);
		view.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
	}

	/**
	 * Loads a text view from view
	 *
	 * @param view         the text view or layout containing it
	 * @param textResource the text resource Id in layout
	 * @return the loaded text view
	 */
	private TextView getTextView(View view, int textResource) {
		TextView text = null;
		try {
			if (textResource == NO_RESOURCE && view instanceof TextView) {
				text = (TextView) view;
			} else if (textResource != NO_RESOURCE) {
				text = (TextView) view.findViewById(textResource);
			}
		} catch (ClassCastException e) {
			Log.e("AbstractPickerAdapter", "You must supply a resource ID for a TextView");
			throw new IllegalStateException(
				"AbstractPickerAdapter requires the resource ID to be a TextView", e);
		}

		return text;
	}

	/**
	 * Loads view from resources
	 *
	 * @param resource the resource Id
	 * @return the loaded view or null if resource is not set
	 */
	private View getView(int resource, ViewGroup parent) {
		switch (resource) {
		case NO_RESOURCE:
			return null;

		case TEXT_VIEW_ITEM_RESOURCE:
			return new TextView(mContext);

		default:
			return mInflater.inflate(resource, parent, false);
		}
	}
}
