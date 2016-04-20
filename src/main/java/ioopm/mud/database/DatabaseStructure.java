package ioopm.mud.database;

public enum DatabaseStructure {

	TABLE_ITEM(
		"CREATE TABLE IF NOT EXISTS Item (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"name VARCHAR NOT NULL," +
			"description VARCHAR," +
			"size INTEGER" +
		");"
	),

	TABLE_LOCATION(
		"CREATE TABLE IF NOT EXISTS Location (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"name VARCHAR NOT NULL," +
			"description VARCHAR," +
			"pvp BOOLEAN" +
		");"
	),

	TABLE_CHARACTER(
		"CREATE TABLE IF NOT EXISTS Character (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT" +
			"has_equiped INTEGER," +

			"FOREIGN KEY(has_equiped) REFERENCES Item(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	),

	TABLE_WEAPON(
		"CREATE TABLE IF NOT EXISTS Weapon (" +
			"id INTEGER PRIMARY KEY," +
			"damage INTEGER," +

			"FOREIGN KEY(id) REFERENCES Item(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	),

	TABLE_KEY(
		"CREATE TABLE IF NOT EXISTS Key (" +
			"id INTEGER PRIMARY KEY," +
			"target INTEGER," +

			"FOREIGN KEY(id) REFERENCES Item(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT," +
			"FOREIGN KEY(target) REFERENCES Location(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	),

	TABLE_PLAYER(
		"CREATE TABLE IF NOT EXISTS Player (" +
			"id INTEGER PRIMARY KEY," +
			"username VARCHAR NOT NULL UNIQUE," +
			"password VARCHAR," +
			"is_admin BOOLEAN," +

			"FOREIGN KEY(id) REFERENCES Character(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	),

	TABLE_CHARACTERSHEET(
		"CREATE TABLE IF NOT EXISTS CharacterSheet (" +
			"id INTEGER PRIMARY KEY," +
			"hp INTEGER," +
			"level INTEGER," +
			"health INTEGER," +
			"maximum_health INTEGER," +

			"FOREIGN KEY(id) REFERENCES Character(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	),

	TABLE_INVENTORY(
		"CREATE TABLE IF NOT EXISTS Inventory (" +
			"id INTEGER PRIMARY KEY," +
			"volume INTEGER NOT NULL," +

			"FOREIGN KEY(id) REFERENCES Character(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	),

	RELATION_CONTAINS_ITEM(
		"CREATE TABLE IF NOT EXISTS ContainsItem (" +
			"inv_id INTEGER," +
			"item_id INTEGER," +
			"amount INTEGER NOT NULL," +

			"PRIMARY KEY(inv_id, item_id)," +

			"FOREIGN KEY(inv_id) REFERENCES Inventory(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT," +
			"FOREIGN KEY(item_id) REFERENCES Item(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	),

	RELATION_EXITS_TO(
		"CREATE TABLE IF NOT EXISTS ExitsTo (" +
			"loc_1 INTEGER," +
			"loc_2 INTEGER," +

			"PRIMARY KEY(loc_1, loc_2)," +

			"FOREIGN KEY(loc_1) REFERENCES Location(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT," +
			"FOREIGN KEY(loc_2) REFERENCES Location(id)" +
				"ON DELETE CASCADE" +
				"ON UPDATE RESTRICT" +
		");"
	);

	private final String SQL;

	DatabaseStructure(String sql) {
		SQL = sql;
	}

	public String toString() {
		return SQL;
	}
}