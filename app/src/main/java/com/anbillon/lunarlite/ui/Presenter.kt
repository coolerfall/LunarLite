/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anbillon.lunarlite.ui

import android.os.Bundle

/**
 * Interface representing a Presenter in a model view presenter (MVP) pattern.
 */
interface Presenter<in T : BaseView> {
  /**
   * Method that attach to [View]. Each view should invoke this method, so that
   * the presenter can control view.
   *
   * @param view view which extends [View]
   */
  fun attach(view: T)

  /**
   * Method that control the lifecycle of the view. Save instance state.
   *
   * @param outState [Bundle]
   */
  fun saveInstanceState(outState: Bundle)

  /**
   * Method that control the lifecycle of the view. Restore instance state.
   *
   * @param savedInstanceState [Bundle]
   */
  fun restoreInstanceState(savedInstanceState: Bundle?)

  /**
   * Method that control the lifecycle of the view. It should be called in the view's
   * (Activity or Fragment) onDestroy() method.
   */
  fun detach()
}