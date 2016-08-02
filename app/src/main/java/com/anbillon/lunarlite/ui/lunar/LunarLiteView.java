package com.anbillon.lunarlite.ui.lunar;

import com.anbillon.lunarlite.data.db.model.Almanac;
import com.anbillon.lunarlite.ui.BaseView;

/**
 * Used as a view representing a lunar view.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public interface LunarLiteView extends BaseView {
	/**
	 * Bind almanac information to this view.
	 *
	 * @param almanac {@link Almanac}
	 */
	void bindAlmanac(Almanac almanac);
}
