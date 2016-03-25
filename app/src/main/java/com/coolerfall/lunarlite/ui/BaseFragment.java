package com.coolerfall.lunarlite.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Base fragment for all fragments, all fragments must inherit from this fragment.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public abstract class BaseFragment extends Fragment {
	private OnBackHandler mOnBackHandler;
	protected AppCompatActivity mActivity;

	/**
	 * A wrap for back pressed action for fragment.
	 */
	public interface OnBackHandler {
		/**
		 * Set current fragment attached to activity.
		 *
		 * @param fragment the fragment
		 */
		void setCurrentFragment(BaseFragment fragment);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mActivity = (AppCompatActivity) context;
	}

	@Override
	public void onStart() {
		super.onStart();
		/* to check if the host activity has implemented OnBackHandler */
		if (mActivity instanceof OnBackHandler) {
			mOnBackHandler = (OnBackHandler) mActivity;
			mOnBackHandler.setCurrentFragment(this);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (getLayoutResID() != 0) {
			View view = inflater.inflate(getLayoutResID(), container, false);
			ButterKnife.bind(this, view);

			return view;
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initView();
	}

	@Override
	public void onStop() {
		super.onStop();
		mOnBackHandler.setCurrentFragment(null);
	}

	/**
	 * Get layout resource id of inherited fragment.
	 */
	protected abstract @LayoutRes int getLayoutResID();

	/**
	 * Initialize view in inherited fragment.
	 */
	protected abstract void initView();

	/**
	 * Fragment can do something here when back pressed.
	 *
	 * @return true if fragment handled back action, otherwise return false.
	 */
	public boolean onBackPressed() {
		return false;
	}

	/**
	 * Set a {@link android.widget.Toolbar Toolbar} to act as the
	 * {@link ActionBar} for this Activity window.
	 *
	 * @param toolbar toolbar to set as the host Activity's action bar
	 */
	protected void setSupportActionBar(@Nullable Toolbar toolbar) {
		mActivity.setSupportActionBar(toolbar);
	}

	/**
	 * Retrieve a reference to this activity's ActionBar.
	 *
	 * @return the host Activity's ActionBar, or null if it does not have one
	 */
	@Nullable
	protected ActionBar getSupportActionBar() {
		return mActivity.getSupportActionBar();
	}
}
