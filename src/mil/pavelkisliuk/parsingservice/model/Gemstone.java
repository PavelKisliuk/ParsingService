package mil.pavelkisliuk.parsingservice.model;

import java.math.BigDecimal;

public class Gemstone {
	private String serialNumber;
	private String name;
	private boolean semiprecious;
	private OriginType origin;
	private BigDecimal price;
	private CuriosityLevelType curiosityLevel;
	private GemOption option;


	public enum OriginType {MINE_WORKINGS, SURFACE_MINING, STREAM_GRAVEL}

	public enum CuriosityLevelType {VERY_RARE, RARE, MEDIUM, FREQUENT, VERY_FREQUENT}

	public static class InnerBuilder {
		private Gemstone newGemstone;

		public InnerBuilder() {
			newGemstone = new Gemstone();
		}

		public InnerBuilder withSerialNumber(String serialNumber){
			newGemstone.serialNumber = serialNumber;
			return this;
		}

		public InnerBuilder withName(String name){
			newGemstone.name = name;
			return this;
		}

		public InnerBuilder withSemiprecious(boolean semiprecious){
			newGemstone.semiprecious = semiprecious;
			return this;
		}

		public InnerBuilder withOrigin(OriginType origin){
			newGemstone.origin = origin;
			return this;
		}

		public InnerBuilder withPrice(BigDecimal price){
			newGemstone.price = price;
			return this;
		}

		public InnerBuilder withCuriosity(CuriosityLevelType curiosityLevel) {
			newGemstone.curiosityLevel = curiosityLevel;
			return this;
		}

		public InnerBuilder withOption(GemOption option) {
			newGemstone.option = option;
			return this;
		}

		public Gemstone build(){
			return newGemstone;
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Gemstone gemstone = (Gemstone) o;

		if (semiprecious != gemstone.semiprecious) return false;
		if (!serialNumber.equals(gemstone.serialNumber)) return false;
		if (!name.equals(gemstone.name)) return false;
		if (origin != gemstone.origin) return false;
		if (!price.equals(gemstone.price)) return false;
		if (curiosityLevel != gemstone.curiosityLevel) return false;
		return option.equals(gemstone.option);
	}

	@Override
	public int hashCode() {
		int result = serialNumber.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (semiprecious ? 1 : 0);
		result = 31 * result + origin.hashCode();
		result = 31 * result + price.hashCode();
		result = 31 * result + curiosityLevel.hashCode();
		result = 31 * result + option.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Gemstone{" +
				"serialNumber='" + serialNumber + '\'' +
				", name='" + name + '\'' +
				", semiprecious=" + semiprecious +
				", origin=" + origin +
				", price=" + price +
				", curiosityLevel=" + curiosityLevel +
				", option=" + option +
				'}';
	}
}