package mil.pavelkisliuk.parsingservice.model;

public class GemOption {
	private GemColorType color;
	private double opacity;
	private int boundNumber;

	public enum GemColorType {BLACK, BLUE, CYAN, GRAY, GREEN, PINK, RED, WHITE}

	public static class InnerBuilder {
		private GemOption newGemOption;

		public InnerBuilder() {
			newGemOption = new GemOption();
		}

		public InnerBuilder withColor(GemColorType color) {
			newGemOption.color = color;
			return this;
		}

		public InnerBuilder withOpacity(double opacity) {
			newGemOption.opacity = opacity;
			return this;
		}

		public InnerBuilder withBoundNumber(int boundNumber) {
			newGemOption.boundNumber = boundNumber;
			return this;
		}

		public GemOption build() {
			return newGemOption;
		}
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GemOption gemOption = (GemOption) o;

		if (Double.compare(gemOption.opacity, opacity) != 0) return false;
		if (boundNumber != gemOption.boundNumber) return false;
		return color == gemOption.color;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = color.hashCode();
		temp = Double.doubleToLongBits(opacity);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + boundNumber;
		return result;
	}

	@Override
	public String toString() {
		return "GemOption{" +
				"color=" + color +
				", opacity=" + opacity +
				", boundNumber=" + boundNumber +
				'}';
	}
}