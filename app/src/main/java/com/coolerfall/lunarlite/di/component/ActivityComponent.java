package com.coolerfall.lunarlite.di.component;

import com.coolerfall.lunarlite.di.module.ActivityModule;
import com.coolerfall.lunarlite.di.scope.PerActivity;

import dagger.Component;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this componet.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since jan. 08, 2016
 */
@PerActivity @Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

}
