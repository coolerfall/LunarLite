package com.anbillon.lunarlite.di.module

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.anbillon.lunarlite.data.db.AssetsDbOpenHelper
import com.anbillon.lunarlite.data.db.repository.AlmanacDataSource
import com.coolerfall.lunarlite.domain.repository.AlmanacRepository
import com.squareup.sqlbrite2.BriteDatabase
import com.squareup.sqlbrite2.SqlBrite
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Singleton

/**
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Module
class DbModule {
  companion object {
    private val DB_NAME = "almanac.db"
    private val DB_VERSION = 1
  }

  @Provides
  @Singleton internal fun provdeBriteDatabase(context: Context): BriteDatabase {
    val helper: SQLiteOpenHelper = AssetsDbOpenHelper(context,
        DB_NAME,
        DB_VERSION)
    val sqlBrite = SqlBrite.Builder().logger { message -> Timber.d(message) }.build()
    return sqlBrite.wrapDatabaseHelper(helper, Schedulers.io())
  }

  @Provides
  @Singleton internal fun provideAlmanacRepository(
      dataSource: AlmanacDataSource): AlmanacRepository {
    return dataSource
  }
}