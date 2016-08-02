package com.anbillon.lunarlite.ui.widget.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.anbillon.lunarlite.ui.widget.picker.adapters.PickerAdapter;
import com.coolerfall.lunarlite.R;

/**
 * A picker view can use to pick one data from a data list, such as
 * date picker and time picker.
 *
 * @author Vincent Cheung
 * @since sept. 02, 2015
 */
public class PickerView extends View {
	/**
	 * Top and bottom items offset (to hide that)
	 */
	private static final int ITEM_OFFSET_PERCENT = 0;

	/**
	 * Left and right padding value
	 */
	private static final int PADDING = 10;

	/**
	 * Default count of visible items
	 */
	private static final int DEF_VISIBLE_ITEMS = 3;

	/* current item */
	private int mCurrentItem = 0;

	/* count of visible items */
	private int mVisibleItems = DEF_VISIBLE_ITEMS;

	/* item height */
	private int mItemHeight = 0;

	private Drawable mSelectedDrawable;
	private int mForeground = R.drawable.picker_selected_bg;
	private int mBackground = Color.WHITE;

	private PickerScroller mScroller;
	private boolean mIsScrollingPerformed;
	private int mScrollingOffset;

	private boolean mIsCyclic = false;

	/* items layout */
	private LinearLayout mItemsLayout;

	/* the number of first item in layout */
	private int mFirstItem;

	/* picker view adapter */
	private PickerAdapter mAdapter;

	private PickerRecycler mRecycler = new PickerRecycler(this);

	private OnPickerScrollListener mOnPickerScrollListener;
	private OnItemClickListener mOnItemClickListener;
	private OnPickListener mOnPickListener;

	public PickerView(Context context) {
		this(context, null);
	}

	public PickerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		rebuildItems();
		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(mItemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mAdapter != null && mAdapter.getItemCount() > 0) {
			updateView();

			drawItems(canvas);
			drawCenterRect(canvas);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layout(r - l, b - t);
	}


	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		if (!isEnabled() || getAdapter() == null) {
			return true;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (getParent() != null) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;

		case MotionEvent.ACTION_UP:
			if (!mIsScrollingPerformed) {
				int distance = (int) event.getY() - getHeight() / 2;
				if (distance > 0) {
					distance += getItemHeight() / 2;
				} else {
					distance -= getItemHeight() / 2;
				}
				int items = distance / getItemHeight();
				if (items != 0 && isValidItemIndex(mCurrentItem + items)) {
					notifyClickListener(mCurrentItem + items);
				}
			}
			break;
		}

		return mScroller.onTouchEvent(event);
	}

	/**
	 * Initializes class data.
	 */
	private void initData(Context context, AttributeSet attrs) {
		mScroller = new PickerScroller(getContext(), scrollingListener);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PickerView);
		mBackground = a.getColor(R.styleable.PickerView_android_background, mBackground);

		a.recycle();

		setBackgroundColor(mBackground);
	}

	/**
	 * Calculates desired height for layout
	 *
	 * @param layout the source layout
	 * @return the desired layout height
	 */
	private int getDesiredHeight(LinearLayout layout) {
		if (layout != null && layout.getChildAt(0) != null) {
			mItemHeight = layout.getChildAt(0).getMeasuredHeight();
		}

		int desired = mItemHeight * mVisibleItems - mItemHeight * ITEM_OFFSET_PERCENT / 50;

		return Math.max(desired, getSuggestedMinimumHeight());
	}

	/**
	 * Returns height of wheel item
	 *
	 * @return the item height
	 */
	private int getItemHeight() {
		if (mItemHeight != 0) {
			return mItemHeight;
		}

		if (mItemsLayout != null && mItemsLayout.getChildAt(0) != null) {
			mItemHeight = mItemsLayout.getChildAt(0).getHeight();
			return mItemHeight;
		}

		return getHeight() / mVisibleItems;
	}

	/**
	 * Calculates control width and creates text layouts.
	 *
	 * @param widthSize the input layout width
	 * @param mode      the layout mode
	 * @return the calculated control width
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mItemsLayout.setLayoutParams(params);
		mItemsLayout.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
			MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		int width = mItemsLayout.getMeasuredWidth();

		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width += 2 * PADDING;
			/* check against our minimum width */
			width = Math.max(width, getSuggestedMinimumWidth());
			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
			}
		}

		mItemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING, MeasureSpec.EXACTLY),
			MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		return width;
	}

	/**
	 * Sets layouts width and height.
	 *
	 * @param width  the layout width
	 * @param height the layout height
	 */
	private void layout(int width, int height) {
		int itemsWidth = width - 2 * PADDING;
		mItemsLayout.layout(0, 0, itemsWidth, height);
	}

	/**
	 * Draws items.
	 *
	 * @param canvas the canvas for drawing
	 */
	private void drawItems(Canvas canvas) {
		canvas.save();

		int top = (mCurrentItem - mFirstItem) * getItemHeight() +
			(getItemHeight() - getHeight()) / 2;
		canvas.translate(PADDING, -top + mScrollingOffset);
		mItemsLayout.draw(canvas);
		canvas.restore();
	}

	/**
	 * Draws rect for current value.
	 *
	 * @param canvas the canvas for drawing
	 */
	private void drawCenterRect(Canvas canvas) {
		int center = getHeight() / 2;
		int offset = (int) (getItemHeight() / 2 * 1.2);
		mSelectedDrawable.setBounds(0, center - offset, getWidth(), center + offset);
		mSelectedDrawable.draw(canvas);
	}

	/* picker scroll listener */
	PickerScroller.ScrollingListener scrollingListener = new PickerScroller.ScrollingListener() {
		@Override
		public void onStarted() {
			mIsScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}

		@Override
		public void onScroll(int distance) {
			doScroll(distance);

			int height = getHeight();
			if (mScrollingOffset > height) {
				mScrollingOffset = height;
				mScroller.stopScrolling();
			} else if (mScrollingOffset < -height) {
				mScrollingOffset = -height;
				mScroller.stopScrolling();
			}
		}

		@Override
		public void onFinished() {
			if (mIsScrollingPerformed) {
				mIsScrollingPerformed = false;
				notifyScrollingListenersAboutEnd();
				notifyPickListener(mCurrentItem);
			}

			mScrollingOffset = 0;
			invalidate();
		}

		@Override
		public void onJustify() {
			if (Math.abs(mScrollingOffset) > PickerScroller.MIN_DELTA_FOR_SCROLLING) {
				mScroller.scroll(mScrollingOffset, 0);
			}
		}
	};

	/**
	 * Set the the specified scrolling interpolator
	 *
	 * @param interpolator the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		mScroller.setInterpolator(interpolator);
	}

	/**
	 * Gets count of visible items
	 *
	 * @return the count of visible items
	 */
	public int getVisibleItems() {
		return mVisibleItems;
	}

	/**
	 * Sets the desired count of visible items.
	 * Actual amount of visible items depends on wheel layout parameters.
	 * To apply changes and rebuild view call measure().
	 *
	 * @param count the desired count for visible items
	 */
	public void setVisibleItems(int count) {
		mVisibleItems = count;
	}

	/**
	 * Gets view adapter.
	 *
	 * @return the view adapter
	 */
	public PickerAdapter getAdapter() {
		return mAdapter;
	}

	/* data observer */
	private DataSetObserver mDataObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			invalidatePicker(false);
		}

		@Override
		public void onInvalidated() {
			invalidatePicker(true);
		}
	};

	/**
	 * Sets view adapter. Usually new adapters contain different views, so
	 * it needs to rebuild view by calling measure().
	 *
	 * @param adapter the view adapter
	 */
	public void setAdapter(PickerAdapter adapter) {
		if (this.mAdapter != null) {
			this.mAdapter.unregisterDataSetObserver(mDataObserver);
		}

		mAdapter = adapter;
		if (this.mAdapter != null) {
			this.mAdapter.registerDataSetObserver(mDataObserver);
		}

		invalidatePicker(true);
	}

	/**
	 * Notifies listeners about starting scrolling
	 */
	protected void notifyScrollingListenersAboutStart() {
		if (mOnPickerScrollListener != null) {
			mOnPickerScrollListener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling
	 */
	protected void notifyScrollingListenersAboutEnd() {
		if (mOnPickerScrollListener != null) {
			mOnPickerScrollListener.onScrollingFinished(this);
		}
	}

	/**
	 * Set on picker scroll Listener.
	 *
	 * @param l the listener to set
	 */
	public void setOnPickerScrollListener(OnPickerScrollListener l) {
		mOnPickerScrollListener = l;
	}

	/**
	 * Set on picker item selected listner.
	 *
	 * @param l the listener to set
	 */
	public void setOnPickListener(OnPickListener l) {
		mOnPickListener = l;
	}

	/**
	 * Set on item click listener.
	 *
	 * @param l the listener
	 */
	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	/**
	 * Notifies listener about clicking.
	 */
	protected void notifyClickListener(int item) {
		if (mOnItemClickListener != null) {
			mOnItemClickListener.onItemClicked(this, item);
		}
	}

	/**
	 * Notifies listener about selecting.
	 *
	 * @param item the item selected
	 */
	protected void notifyPickListener(int item) {
		/* notify item selected listener */
		if (mOnPickListener != null) {
			mOnPickListener.onPick(this, item);
		}

		mAdapter.updateSelectedItem(item);
	}

	/**
	 * Gets current value.
	 *
	 * @return the current value
	 */
	public int getCurrentItem() {
		return mCurrentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 *
	 * @param index    the item index
	 * @param animated the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (mAdapter == null || mAdapter.getItemCount() == 0) {
			return;
		}

		int itemCount = mAdapter.getItemCount();
		if (index < 0 || index >= itemCount) {
			if (mIsCyclic) {
				while (index < 0) {
					index += itemCount;
				}
				index %= itemCount;
			} else {
				return;
			}
		}

		if (index != mCurrentItem) {
			if (animated) {
				int itemsToScroll = index - mCurrentItem;
				if (mIsCyclic) {
					int scroll = itemCount + Math.min(index, mCurrentItem) -
						Math.max(index, mCurrentItem);
					if (scroll < Math.abs(itemsToScroll)) {
						itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
					}
				}
				scroll(itemsToScroll, 0);
			} else {
				mScrollingOffset = 0;
				mCurrentItem = index;

				invalidate();
			}

			if (!mIsScrollingPerformed) {
				notifyPickListener(mCurrentItem);
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 *
	 * @param index the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	/**
	 * Tests if picker is cyclic. That means before the 1st item there is shown the last one.
	 *
	 * @return true if picker is cyclic
	 */
	public boolean isCyclic() {
		return mIsCyclic;
	}

	/**
	 * Set picker cyclic flag.
	 *
	 * @param isCyclic the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		mIsCyclic = isCyclic;
		invalidatePicker(false);
	}

	/**
	 * Sets the drawable for the selected item background
	 *
	 * @param resId drawable resource id
	 */
	public void setSelectedBackground(@DrawableRes int resId) {
		mForeground = resId;
		mSelectedDrawable = getContext().getResources().getDrawable(mForeground);
	}

	/**
	 * Sets the drawable for the selected item background color
	 *
	 * @param resId drawable resource id
	 */
	public void setSelectedBackgroundColor(@ColorRes int resId) {
		mSelectedDrawable = new ColorDrawable(getContext().getResources().getColor(resId));
	}

	/**
	 * Invalidates picker.
	 *
	 * @param clearCaches if true then cached views will be clear
	 */
	public void invalidatePicker(boolean clearCaches) {
		if (clearCaches) {
			mRecycler.clearAll();
			if (mItemsLayout != null) {
				mItemsLayout.removeAllViews();
			}
			mScrollingOffset = 0;
		} else if (mItemsLayout != null) {
			/* cache all items */
			mRecycler.recycleItems(mItemsLayout, mFirstItem, new ItemsRange());
		}

		/* reset current item if out of index */
		if (mAdapter != null && mCurrentItem >= mAdapter.getItemCount()) {
			setCurrentItem(mAdapter.getItemCount() - 1);
		}

		invalidate();
	}

	/**
	 * Initializes resources.
	 */
	private void initResourcesIfNecessary() {
		if (mSelectedDrawable == null) {
			mSelectedDrawable = getContext().getResources().getDrawable(mForeground);
		}
	}

	/**
	 * Scrolls the picker.
	 *
	 * @param delta the scrolling value
	 */
	private void doScroll(int delta) {
		mScrollingOffset += delta;

		int itemHeight = getItemHeight();
		int count = mScrollingOffset / itemHeight;

		int pos = mCurrentItem - count;
		int itemCount = mAdapter.getItemCount();

		int fixPos = mScrollingOffset % itemHeight;
		if (Math.abs(fixPos) <= itemHeight / 2) {
			fixPos = 0;
		}

		if (mIsCyclic && itemCount > 0) {
			if (fixPos > 0) {
				pos--;
				count++;
			} else if (fixPos < 0) {
				pos++;
				count--;
			}

			/* fix position by rotating */
			while (pos < 0) {
				pos += itemCount;
			}

			pos %= itemCount;
		} else {
			if (pos < 0) {
				count = mCurrentItem;
				pos = 0;
			} else if (pos >= itemCount) {
				count = mCurrentItem - itemCount + 1;
				pos = itemCount - 1;
			} else if (pos > 0 && fixPos > 0) {
				pos--;
				count++;
			} else if (pos < itemCount - 1 && fixPos < 0) {
				pos++;
				count--;
			}
		}

		int offset = mScrollingOffset;
		if (pos != mCurrentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		/* update offset */
		mScrollingOffset = offset - count * itemHeight;
		if (mScrollingOffset > getHeight()) {
			if (getHeight() <= 0) {
				mScrollingOffset = 0;
			} else {
				mScrollingOffset = mScrollingOffset % getHeight() + getHeight();
			}
		}
	}

	/**
	 * Scroll the wheel
	 *
	 * @param itemsToScroll items to scroll
	 * @param time          scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		int distance = itemsToScroll * getItemHeight() - mScrollingOffset;
		mScroller.scroll(distance, time);
	}

	/**
	 * Calculates range for wheel items
	 *
	 * @return the items range
	 */
	private ItemsRange getItemsRange() {
		int start = mCurrentItem - mVisibleItems / 2;
		int end = start + mVisibleItems - (mVisibleItems % 2 == 0 ? 0 : 1);
		if (mScrollingOffset != 0) {
			if (mScrollingOffset > 0) {
				start--;
			} else {
				end++;
			}
		}

		if (!isCyclic()) {
			if (start < 0) {
				start = 0;
			}

			if (mAdapter == null) {
				end = 0;
			} else if (end > mAdapter.getItemCount()) {
				end = mAdapter.getItemCount();
			}
		}

		return new ItemsRange(start, end - start + 1);
	}

	/**
	 * Rebuilds wheel items if necessary. Caches all unused items.
	 *
	 * @return true if items are rebuilt
	 */
	private boolean rebuildItems() {
		boolean updated;
		ItemsRange range = getItemsRange();
		if (range == null) {
			return false;
		}

		if (mItemsLayout != null) {
			int first = mRecycler.recycleItems(mItemsLayout, mFirstItem, range);
			updated = mFirstItem != first;
			mFirstItem = first;
		} else {
			createItemsLayout();
			updated = true;
		}

		if (!updated) {
			updated = mFirstItem != range.getFirst() ||
				mItemsLayout.getChildCount() != range.getCount();
		}

		if (mFirstItem > range.getFirst() && mFirstItem <= range.getLast()) {
			for (int i = mFirstItem - 1; i >= range.getFirst(); i--) {
				if (!addViewItem(i, true)) {
					break;
				}
				mFirstItem = i;
			}
		} else {
			mFirstItem = range.getFirst();
		}

		int first = mFirstItem;
		for (int i = mItemsLayout.getChildCount(); i < range.getCount(); i++) {
			if (!addViewItem(mFirstItem + i, false) && mItemsLayout.getChildCount() == 0) {
				first++;
			}
		}
		mFirstItem = first;

		return updated;
	}

	/**
	 * Updates view. Rebuilds items and label if necessary, recalculate items sizes.
	 */
	private void updateView() {
		if (rebuildItems()) {
			calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			layout(getWidth(), getHeight());
		}
	}

	/**
	 * Creates item layouts if necessary
	 */
	private void createItemsLayout() {
		if (mItemsLayout == null) {
			mItemsLayout = new LinearLayout(getContext());
			mItemsLayout.setOrientation(LinearLayout.VERTICAL);
		}
	}

	/**
	 * Adds view for item to items layout.
	 *
	 * @param index the item index
	 * @param first the flag indicates if view should be first
	 * @return true if corresponding item exists and is added
	 */
	private boolean addViewItem(int index, boolean first) {
		View view = getItemView(index);
		if (view != null) {
			if (first) {
				mItemsLayout.addView(view, 0);
			} else {
				mItemsLayout.addView(view);
			}

			return true;
		}

		return false;
	}

	/**
	 * Checks whether intem index is valid.
	 *
	 * @param index the item index
	 * @return true if item index is not out of bounds or the wheel is cyclic
	 */
	private boolean isValidItemIndex(int index) {
		return mAdapter != null && mAdapter.getItemCount() > 0 &&
			(mIsCyclic || index >= 0 && index < mAdapter.getItemCount());
	}

	/**
	 * Returns view for specified item.
	 *
	 * @param index the item index
	 * @return item view or empty view if index is out of bounds
	 */
	private View getItemView(int index) {
		if (mAdapter == null || mAdapter.getItemCount() == 0) {
			return null;
		}
		int count = mAdapter.getItemCount();
		if (!isValidItemIndex(index)) {
			return mAdapter.getEmptyItem(mRecycler.getEmptyItem(), mItemsLayout);
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return mAdapter.getView(index, mRecycler.getItem(), mItemsLayout);
	}

	/**
	 * Stops scrolling.
	 */
	public void stopScrolling() {
		mScroller.stopScrolling();
	}
}
