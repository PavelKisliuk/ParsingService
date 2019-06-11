package mil.pavelkisliuk.parsingservice.parser;

import mil.pavelkisliuk.parsingservice.model.GemOption;
import mil.pavelkisliuk.parsingservice.model.Gemstone;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.testng.Assert.*;

public class SimpleDOMParserTest {
	@Test
	public void test(){
		Gemstone.InnerBuilder builder = new Gemstone.InnerBuilder();
		builder.withSerialNumber("Gem0001");
		builder.withCuriosity(Gemstone.CuriosityLevelType.valueOf("VERY_RARE"));
		builder.withName("Grandidierite");
		builder.withSemiprecious(false);
		builder.withOrigin(Gemstone.OriginType.valueOf("MINE_WORKINGS"));
		builder.withPrice(new BigDecimal(1_000_000));
		GemOption.InnerBuilder optionBuilder = new GemOption.InnerBuilder();
		optionBuilder.withColor(GemOption.GemColorType.valueOf("BLACK"));
		optionBuilder.withOpacity(0.7);
		builder.withOption(optionBuilder.build());

		SimpleDOMParser parser = new SimpleDOMParser();
		parser.buildSetDevices("testfile/test.xml");


		Gemstone gemstone = builder.build();
		HashSet<Gemstone> expected = new HashSet<>();
		expected.add(gemstone);

		assertEquals(parser.getGemstones(), expected);
	}
}