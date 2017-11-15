package com.anbillon.lunarlite.data.db.repository

import com.anbillon.lunarlite.data.db.model.Almanac
import com.anbillon.lunarlite.data.db.model.AlmanacModel
import com.coolerfall.lunarlite.domain.repository.AlmanacRepository
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Provide almanac data source from database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
class AlmanacDataSource @Inject constructor(private val briteDatabase: BriteDatabase) :
    AlmanacRepository {

  override fun loadAlmanac(wielding: Int, heavenlyEarthly: Int): Observable<Almanac> {
    return briteDatabase.createQuery(AlmanacModel.TABLE_NAME,
        Almanac.selectByWieldingAndHseb(wielding,
            heavenlyEarthly)).mapToOne { Almanac.MAPPER.map(it) }
  }
}
