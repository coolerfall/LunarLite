package com.anbillon.lunarlite.data.db.model

import com.anbillon.lunarlite.data.db.model.AlmanacModel.Factory
import com.anbillon.lunarlite.data.db.model.AlmanacModel.Mapper

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
class Almanac(private val wielding: Int, private val hseb: Int, private val dread: String,
    private val suit: String) : AlmanacModel {

  override fun wielding(): Int {
    return wielding
  }

  override fun hseb(): Int {
    return hseb
  }

  override fun dread(): String {
    return dread
  }

  override fun suit(): String {
    return suit
  }

  companion object {
    private val FACTORY: Factory<Almanac> = Factory<Almanac>(
        AlmanacModel.Creator<Almanac> { wielding: Int, hseb: Int, dread: String, suit: String ->
          Almanac(wielding, hseb, dread, suit)
        })
    val MAPPER: Mapper<Almanac> = FACTORY.select_by_wielding_and_hsebMapper()

    fun selectByWieldingAndHseb(wielding: Int, hseb: Int): String {
      return FACTORY.select_by_wielding_and_hseb(wielding, hseb).statement
    }
  }
}
