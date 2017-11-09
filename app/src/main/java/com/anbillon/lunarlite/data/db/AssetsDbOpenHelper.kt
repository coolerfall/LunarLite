package com.anbillon.lunarlite.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException

/**
 * A sqlite database open helper for external database in assets folder.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
class AssetsDbOpenHelper(private val context: Context, private val dbName: String, version: Int) :
    SQLiteOpenHelper(context, dbName, null, version) {
  private var shouldUpgrade: Boolean = false
  private var oldVersion: Int = 0

  override fun getReadableDatabase(): SQLiteDatabase {
    if (!isDbExisted(context, dbName)) {
      try {
        copyAssetsDb(context, dbName)
        Timber.i("First open and copy")
      } catch (e: IOException) {
        deleteDb(context, dbName)
        e.printStackTrace()
        Timber.e(e, "First open and fail to copy db, %s", e.message)
      }
      return super.getReadableDatabase()
    } else {
      var database = super.getReadableDatabase()
      var shouldDowngrade = false
      if (shouldUpgrade) {
        database.close()
        try {
          copyAssetsDb(context, dbName)
          shouldUpgrade = false
          Timber.i("External database upgrade, new version: %d", database.version)
        } catch (e: IOException) {
          shouldDowngrade = true
          deleteDb(context, dbName)
          Timber.e(e, "Upgrade and fail to copy db, %s", e.message)
        }

        database = super.getReadableDatabase()
        if (shouldDowngrade) {
          database.version = oldVersion
        }
      }
      return database
    }
  }

  override fun onCreate(db: SQLiteDatabase) {}

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    this.oldVersion = oldVersion
    shouldUpgrade = newVersion > oldVersion
  }

  companion object {
    /**
     * Copy database in raw resource folder into app database folder.
     *
     * @param context the context to use
     * @param dbName database name
     */
    @Throws(IOException::class) internal fun copyAssetsDb(context: Context, dbName: String) {
      val dbFile = context.getDatabasePath(dbName)
      val parentFile = dbFile.parentFile
      if (!parentFile.exists()) {
        parentFile.mkdirs()
      }

      val fos = FileOutputStream(dbFile)
      val ins = context.assets.open(dbName)
      val buffer = ByteArray(2048)
      var count = 0

      while ({ count = ins.read(buffer); count }() > 0) {
        fos.write(buffer, 0, count)
      }
    }

    /**
     * Delete db file by name.
     */
    internal fun deleteDb(context: Context, dbName: String) {
      val file = context.getDatabasePath(dbName)
      file.delete()
    }

    /**
     * To check if database existed.
     *
     * @param context the context to use
     * @param dbName database name
     * @return true if existed, otherwise return false
     */
    internal fun isDbExisted(context: Context, dbName: String): Boolean {
      return context.getDatabasePath(dbName).exists()
    }
  }
}
