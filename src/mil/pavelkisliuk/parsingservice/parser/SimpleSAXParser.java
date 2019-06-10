package mil.pavelkisliuk.parsingservice.parser;

import mil.pavelkisliuk.parsingservice.model.GemConstantType;
import mil.pavelkisliuk.parsingservice.model.GemOption;
import mil.pavelkisliuk.parsingservice.model.Gemstone;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class SimpleSAXParser extends DefaultHandler {
	public static final Logger LOGGER = LogManager.getLogger();

	private Set<Gemstone> gemstones = new HashSet<>();
	private Gemstone.InnerBuilder gemstoneBuilder;
	private GemOption.InnerBuilder optionBuilder;
	private GemConstantType currentGem;

	public Set<Gemstone> getGemstones() {
		return gemstones;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (GemConstantType.GEM.getValue().equals(localName)) {
			gemstoneBuilder = new Gemstone.InnerBuilder();
			gemstoneBuilder.withCuriosity(Gemstone.CuriosityLevelType.valueOf(attributes.getValue(0)));
			gemstoneBuilder.withSerialNumber(attributes.getValue(1));
		} else if (GemConstantType.OPTION.getValue().equals(localName)) {
			optionBuilder = new GemOption.InnerBuilder();
		} else if(!localName.equals(GemConstantType.GEMS.getValue())){
			currentGem = GemConstantType.valueOf(localName.toUpperCase());
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		if (GemConstantType.OPTION.getValue().equals(localName)) {
			gemstoneBuilder.withOption(optionBuilder.build());
		} else if (GemConstantType.GEM.getValue().equals(localName)) {
			LOGGER.log(Level.DEBUG, gemstoneBuilder.build() + " successfully added");
			gemstones.add(gemstoneBuilder.build());
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		String s = new String(ch, start, length).trim();
		if (currentGem != null) {
			switch (currentGem) {
				case NAME:
					gemstoneBuilder.withName(s);
					break;
				case SEMIPRECIOUS:
					gemstoneBuilder.withSemiprecious(Boolean.parseBoolean(s));
					break;
				case ORIGIN:
					gemstoneBuilder.withOrigin(Gemstone.OriginType.valueOf(s));
					break;
				case PRICE:
					gemstoneBuilder.withPrice(new BigDecimal(s));
					break;
				case COLOR:
					optionBuilder.withColor(GemOption.GemColorType.valueOf(s));
					break;
				case OPACITY:
					optionBuilder.withOpacity(Double.parseDouble(s));
					break;
				case BOUND_NUMBER:
					optionBuilder.withBoundNumber(Integer.parseInt(s));
					break;
				default:
//					throw new EnumConstantNotPresentException(
//							currentDeviceConstant.getDeclaringClass(), currentDeviceConstant.name());
			}
			currentGem = null;
		}
	}

}