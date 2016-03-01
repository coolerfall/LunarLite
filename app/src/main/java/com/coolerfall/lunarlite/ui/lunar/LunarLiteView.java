package com.coolerfall.lunarlite.ui.lunar;

import com.coolerfall.lunarlite.data.db.model.Almanac;
import com.coolerfall.lunarlite.ui.View;

/**
 * Used as a view representing a lunar view.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 * @since Feb. 29, 2016
 */
public interface LunarLiteView extends View {
	/**
	 * Bind almanac information to this view.
	 *
	 * @param almanac {@link Almanac}
	 */
	void bindAlmanac(Almanac almanac);
}
