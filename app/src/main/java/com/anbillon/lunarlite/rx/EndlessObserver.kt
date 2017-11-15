package com.anbillon.lunarlite.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * A [Observer] that will invoke on complete and on error
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class EndlessObserver<T> : Observer<T> {
  override fun onSubscribe(d: Disposable) {}

  override fun onComplete() {}

  override fun onError(e: Throwable) {}
}
