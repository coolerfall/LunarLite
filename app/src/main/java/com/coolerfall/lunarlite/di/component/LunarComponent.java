package com.coolerfall.lunarlite.di.component;

import com.coolerfall.lunarlite.di.module.ActivityModule;
import com.coolerfall.lunarlite.di.scope.PerActivity;
import com.coolerfall.lunarlite.ui.lunar.LunarLiteActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component. This is a common component for this project,
 * that means several activities will use this component.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 29, 2016
 */
@PerActivity @Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface LunarComponent extends ActivityComponent {
	/**
	 * Inject this component into {@link LunarLiteActivity}.
	 *
	 * @param lunar {@link LunarLiteActivity}
	 */
	void inject(LunarLiteActivity lunar);
}
