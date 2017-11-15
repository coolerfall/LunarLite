package com.anbillon.lunarlite.rx

import android.support.annotation.CallSuper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * A [Observer] that will invoke on complete, on error and on end.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class EndObserver<T> : Observer<T> {
  override fun onSubscribe(d: Disposable) {

  }

  @CallSuper override fun onComplete() {
    onEnd()
  }

  @CallSuper override fun onError(e: Throwable) {
    onEnd()
  }

  abstract fun onEnd()
}
