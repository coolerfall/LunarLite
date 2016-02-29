package com.coolerfall.lunarlite.data.db.repository;

import android.content.Context;

import com.coolerfall.lunarlite.data.db.DaoHelper;
import com.coolerfall.lunarlite.data.db.model.Almanac;
import com.coolerfall.lunarlite.data.db.model.AlmanacDao;
import com.coolerfall.lunarlite.domain.repository.AlmanacRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Provide almanac data source from database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 20, 2016
 */
public class AlmanacDataSource implements AlmanacRepository {
	private AlmanacDao mAlmanacDao;

	@Inject
	public AlmanacDataSource(Context context) {
		mAlmanacDao = DaoHelper.getDaoSession(context).getAlmanacDao();
	}

	@Override
	public Observable<Almanac> getAlmanac(int wielding, int heavenlyEarthly) {
		return Observable.defer(() -> {
			List<Almanac> dataList = mAlmanacDao.queryBuilder()
				.where(AlmanacDao.Properties.Wielding.eq(wielding))
				.where(AlmanacDao.Properties.Hseb.eq(heavenlyEarthly))
				.list();

			return Observable.just(dataList.isEmpty() ? null : dataList.get(0));
		});
	}
}
