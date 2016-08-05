package com.anbillon.lunarlite.data.db.model;

import com.google.auto.value.AutoValue;

/**
 * This class represents a model for almanac information in database.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@AutoValue public abstract class Almanac implements AlmanacModel {
	static final Factory<Almanac> FACTORY =
		new Factory<>((Creator<Almanac>) AutoValue_Almanac::new);

	public static final Mapper<Almanac> MAPPER = FACTORY.select_by_wielding_and_hsebMapper();
}
