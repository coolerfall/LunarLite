package com.coolerfall.lunarlite.domain.repository

import com.anbillon.lunarlite.data.db.model.Almanac
import io.reactivex.Observable

/**
 * Interface that represents a repository for getting related data from database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
interface AlmanacRepository {
  /**
   * Load an almanac from database with given wielding and heavenly earthly.
   *
   * @param wielding wielding
   * @param heavenlyEarthly heavenly and earthly
   * @return [Almanac]
   */
  fun loadAlmanac(wielding: Int, heavenlyEarthly: Int): Observable<Almanac>
}
