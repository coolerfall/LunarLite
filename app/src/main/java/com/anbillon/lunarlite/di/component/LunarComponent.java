package com.anbillon.lunarlite.di.component;

import com.anbillon.lunarlite.ui.lunar.LunarLiteActivity;
import com.anbillon.lunarlite.di.scope.PerActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component. This is a common component for this project,
 * that means several activities will use this component.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@PerActivity @Component(dependencies = AppComponent.class)
public interface LunarComponent extends ActivityComponent {
	/**
	 * Inject this component into {@link LunarLiteActivity}.
	 *
	 * @param lunar {@link LunarLiteActivity}
	 */
	void inject(LunarLiteActivity lunar);
}
