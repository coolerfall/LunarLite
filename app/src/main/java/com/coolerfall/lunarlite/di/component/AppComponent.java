package com.coolerfall.lunarlite.di.component;


import com.coolerfall.lunarlite.di.module.AppModule;
import com.coolerfall.lunarlite.domain.repository.AlmanacRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A application-level component used in this projct.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@Singleton @Component(modules = AppModule.class)
public interface AppComponent {
	/**
	 * Provides {@link AlmanacRepository} for other component to use.
	 *
	 * @return {@link AlmanacRepository}
	 */
	AlmanacRepository almanacRepository();
}
