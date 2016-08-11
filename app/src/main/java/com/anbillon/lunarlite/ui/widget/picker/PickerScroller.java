package com.anbillon.lunarlite.ui.widget.picker;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Scroller class handles scrolling events and updates the picker view.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class PickerScroller {
	private final static int MESSAGE_SCROLL = 0;
	private final static int MESSAGE_JUSTIFY = 1;
	/* scrolling duration */
	private static final int SCROLLING_DURATION = 100;
	/* minimum delta for scrolling */
	public static final int MIN_DELTA_FOR_SCROLLING = 1;

	private ScrollingListener mListener;
	private Context mContext;

	private GestureDetector mGestureDetector;
	private Scroller mScroller;
	private int mLastScrollY;
	private float mLastTouchedY;
	private boolean mIsScrollingPerformed;

	/**
	 * Scrolling listener interface
	 */
	public interface ScrollingListener {
		/**
		 * Scrolling callback called when scrolling is performed.
		 *
		 * @param distance the distance to scroll
		 */
		void onScroll(int distance);

		/**
		 * Starting callback called when scrolling is started
		 */
		void onStarted();

		/**
		 * Finishing callback called after justifying
		 */
		void onFinished();

		/**
		 * Justifying callback called to justify a view when scrolling is ended
		 */
		void onJustify();
	}

	/**
	 * Constructor.
	 *
	 * @param context  the current context
	 * @param listener the scrolling listener
	 */
	public PickerScroller(Context context, ScrollingListener listener) {
		mGestureDetector = new GestureDetector(context, mGestureListener);
		mGestureDetector.setIsLongpressEnabled(false);

		mScroller = new Scroller(context);

		mListener = listener;
		mContext = context;
	}

	/**
	 * Set the the specified scrolling interpolator.
	 *
	 * @param interpolator the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		mScroller.forceFinished(true);
		mScroller = new Scroller(mContext, interpolator);
	}

	/**
	 * Scroll the picker.
	 *
	 * @param distance the scrolling distance
	 * @param time     the scrolling duration
	 */
	public void scroll(int distance, int time) {
		mScroller.forceFinished(true);
		mLastScrollY = 0;
		mScroller.startScroll(0, 0, 0, distance, time != 0 ? time : SCROLLING_DURATION);
		setNextMessage(MESSAGE_SCROLL);

		startScrolling();
	}

	/**
	 * Stops scrolling
	 */
	public void stopScrolling() {
		mScroller.forceFinished(true);
	}

	/**
	 * Handle Touch event.
	 *
	 * @param event the motion event
	 * @return true if event has been cusumed, otherwise return false
	 */
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastTouchedY = event.getY();
			mScroller.forceFinished(true);
			clearMessages();
			break;

		case MotionEvent.ACTION_MOVE:
			/* perform scrolling */
			int distanceY = (int) (event.getY() - mLastTouchedY);
			if (distanceY != 0) {
				startScrolling();
				mListener.onScroll(distanceY);
				mLastTouchedY = event.getY();
			}
			break;
		}

		if (!mGestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
			justify();
		}

		return true;
	}

	/* gesture listener */
	@SuppressWarnings("FieldCanBeLocal")
	private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			/*
			 * Do scrolling in onTouchEvent() since onScroll() are not call immediately
			 * when user touch and move the wheel
			 */
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			mLastScrollY = 0;
			final int maxY = 0x8FF;
			final int minY = -maxY;
			mScroller.fling(0, mLastScrollY, 0, (int) -velocityY, 0, 0, minY, maxY);
			setNextMessage(MESSAGE_SCROLL);
			return true;
		}
	};

	/**
	 * Set next message to queue. Clears queue before.
	 *
	 * @param message the message to set
	 */
	private void setNextMessage(int message) {
		clearMessages();
		mAnimationHandler.sendEmptyMessage(message);
	}

	/**
	 * Clears messages from queue
	 */
	private void clearMessages() {
		mAnimationHandler.removeMessages(MESSAGE_SCROLL);
		mAnimationHandler.removeMessages(MESSAGE_JUSTIFY);
	}

	/* animation handler */
	private Handler mAnimationHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			mScroller.computeScrollOffset();
			int currY = mScroller.getCurrY();
			int delta = mLastScrollY - currY;
			mLastScrollY = currY;
			if (delta != 0) {
				mListener.onScroll(delta);
			}

			/* scrolling is not finished when it comes to final Y. so, finish it manually */
			if (Math.abs(currY - mScroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
//				currY = mScroller.getFinalY();
				mScroller.forceFinished(true);
			}

			if (!mScroller.isFinished()) {
				mAnimationHandler.sendEmptyMessage(msg.what);
			} else if (msg.what == MESSAGE_SCROLL) {
				justify();
			} else {
				finishScrolling();
			}

			return true;
		}
	});

	/**
	 * Justifies wheel
	 */
	private void justify() {
		mListener.onJustify();
		setNextMessage(MESSAGE_JUSTIFY);
	}

	/**
	 * Starts scrolling
	 */
	private void startScrolling() {
		if (!mIsScrollingPerformed) {
			mIsScrollingPerformed = true;
			mListener.onStarted();
		}
	}

	/**
	 * Finishes scrolling
	 */
	void finishScrolling() {
		if (mIsScrollingPerformed) {
			mListener.onFinished();
			mIsScrollingPerformed = false;
		}
	}
}
