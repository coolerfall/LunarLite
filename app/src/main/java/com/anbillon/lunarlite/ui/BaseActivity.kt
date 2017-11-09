package com.anbillon.lunarlite.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.anbillon.lunarlite.ui.BaseFragment.OnBackHandler
import dagger.android.AndroidInjection

/**
 * Base activity of this project, all activity must inherit from this activity.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class BaseActivity : AppCompatActivity(), OnBackHandler {
  private var currentFtagment: BaseFragment? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    initView()
  }

  override fun onBackPressed() {
    if (currentFtagment == null || !currentFtagment!!.onBackPressed()) {
      if (supportFragmentManager.backStackEntryCount == 0) {
        super.onBackPressed()
      } else {
        supportFragmentManager.popBackStack()
      }
    }
  }

  override fun setCurrentFragment(fragment: BaseFragment) {
    currentFtagment = fragment
  }

  /**
   * Initialize view in inherited activity.
   */
  protected abstract fun initView()
}
