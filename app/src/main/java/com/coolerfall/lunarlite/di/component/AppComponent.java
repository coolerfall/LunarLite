package com.coolerfall.lunarlite.di.component;


import com.coolerfall.lunarlite.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A application-level component used in this projct.
 *
 * @author Vincent Cheung
 * @since Jan. 08, 2016
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

}
