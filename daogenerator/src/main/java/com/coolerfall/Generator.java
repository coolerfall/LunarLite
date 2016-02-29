package com.coolerfall;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generator {
	public static void main(String args[]) throws Exception {
		Schema schema = new Schema(1, "com.coolerfall.lunarlite.data.db.model");
		Entity almanac = schema.addEntity("Almanac");
		almanac.setTableName("almanac");

		almanac.addIntProperty("wielding").columnName("wielding");
		almanac.addIntProperty("hseb").columnName("hseb");
		almanac.addStringProperty("dread").columnName("dread");
		almanac.addStringProperty("suit").columnName("suit");

		/* generate all dao */
		new DaoGenerator().generateAll(schema, args[0]);
	}
}
