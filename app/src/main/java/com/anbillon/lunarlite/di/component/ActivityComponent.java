package com.anbillon.lunarlite.di.component;

import com.anbillon.lunarlite.di.module.ActivityModule;
import com.anbillon.lunarlite.di.scope.PerActivity;

import dagger.Component;

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this componet.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@PerActivity @Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

}
