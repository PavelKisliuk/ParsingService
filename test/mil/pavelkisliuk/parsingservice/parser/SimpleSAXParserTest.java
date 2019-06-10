package mil.pavelkisliuk.parsingservice.parser;

import mil.pavelkisliuk.parsingservice.model.GemOption;
import mil.pavelkisliuk.parsingservice.model.Gemstone;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;

import static org.testng.Assert.*;

public class SimpleSAXParserTest {
	@Test
	public void test() throws ParserConfigurationException, SAXException, IOException {
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

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		SAXParser parser = factory.newSAXParser();

		SimpleSAXParser handler = new SimpleSAXParser();
		parser.parse(new File("testfile/test.xml"), handler);


		Gemstone gemstone = builder.build();
		HashSet<Gemstone> expected = new HashSet<>();
		expected.add(gemstone);
		assertEquals(handler.getGemstones(), expected);
	}
}