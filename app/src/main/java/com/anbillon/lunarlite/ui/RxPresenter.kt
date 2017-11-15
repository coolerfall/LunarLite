package com.anbillon.lunarlite.ui

import android.os.Bundle
import android.support.annotation.CallSuper
import com.anbillon.lunarlite.rx.EndlessObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Abstract class representing a Presenter with rxJava in a model view presenter (MVP) pattern.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
abstract class RxPresenter<T : BaseView> : Presenter<T> {
  protected val disposables = CompositeDisposable()
  protected var view: T? = null

  @CallSuper override fun attach(view: T) {
    this.view = view
  }

  override fun restoreInstanceState(savedInstanceState: Bundle?) {}

  override fun saveInstanceState(outState: Bundle) {}

  @CallSuper override fun detach() {
    disposables.clear()
    view = null
  }

  protected fun <T> observeOnView(observable: Observable<T>): Observable<T> {
    return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
  }

  protected fun <T> observeSilently(observable: Observable<T>) {
    return observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
        object : EndlessObserver<T>() {
          /* ignore */
          override fun onNext(t: T) {
          }
        })
  }
}