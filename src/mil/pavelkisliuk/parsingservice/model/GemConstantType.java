package mil.pavelkisliuk.parsingservice.model;

public enum GemConstantType {
	GEMS("gems"),
	GEM("gem"),
	SERIAL_NUMBER("serialNumber"),
	NAME("name"),
	SEMIPRECIOUS("semiprecious"),
	ORIGIN("origin"),
	PRICE("price"),
	CURIOSITY_LEVEL("curiosityLevel"),
	OPTION("option"),
	COLOR("color"),
	OPACITY("opacity"),
	BOUND_NUMBER("boundNumber");


	private String value;

	GemConstantType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
