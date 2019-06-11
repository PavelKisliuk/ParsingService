package mil.pavelkisliuk.parsingservice.parser;

import mil.pavelkisliuk.parsingservice.model.GemConstantType;
import mil.pavelkisliuk.parsingservice.model.GemOption;
import mil.pavelkisliuk.parsingservice.model.Gemstone;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class SimpleStAXParser {
	public static final Logger LOGGER = LogManager.getLogger();

	private Set<Gemstone> gemstones = new HashSet<>();
	private XMLInputFactory inputFactory;

	public SimpleStAXParser() {
		inputFactory = XMLInputFactory.newInstance();
	}

	public Set<Gemstone> getGemstones() {
		return gemstones;
	}

	public void buildGemstones(String fileName) {

		XMLStreamReader reader;
		String name;
		try (InputStream inputStream = Files.newInputStream(Paths.get(fileName))) {
			reader = inputFactory.createXMLStreamReader(inputStream);
			while (reader.hasNext()) {
				int type = reader.next();
				if (type == XMLStreamConstants.START_ELEMENT) {
					name = reader.getLocalName();
					if (GemConstantType.valueOf(name.toUpperCase()) == GemConstantType.GEM) {
						Gemstone gemstone = buildGemstone(reader);
						gemstones.add(gemstone);
					}
				}
			}
		} catch (XMLStreamException | IOException e) {
			LOGGER.error("can't read " + fileName, e);
		}
	}

	private Gemstone buildGemstone(XMLStreamReader reader) throws XMLStreamException {
		Gemstone.InnerBuilder gemstoneBuilder = new Gemstone.InnerBuilder();
		gemstoneBuilder.withSerialNumber(reader.getAttributeValue(null, GemConstantType.SERIAL_NUMBER.getValue()))
				.withCuriosity(Gemstone.CuriosityLevelType.valueOf(
						reader.getAttributeValue(null, GemConstantType.CURIOSITY_LEVEL.getValue())));

		String name;
		while (reader.hasNext()) {
			int type = reader.next();
			if (type == XMLStreamConstants.START_ELEMENT) {
				name = reader.getLocalName();
				switch (GemConstantType.valueOf(name.toUpperCase())) {
					case NAME:
						gemstoneBuilder.withName(getXMLText(reader));
						break;
					case SEMIPRECIOUS:
						gemstoneBuilder.withSemiprecious(Boolean.parseBoolean(getXMLText(reader)));
						break;
					case ORIGIN:
						gemstoneBuilder.withOrigin(Gemstone.OriginType.valueOf(getXMLText(reader)));
						break;
					case PRICE:
						gemstoneBuilder.withPrice(new BigDecimal(getXMLText(reader)));
						break;
					case OPTION:
						gemstoneBuilder.withOption(buildOption(reader));
						break;
					default:
						throw new EnumConstantNotPresentException(GemConstantType.class, name);
				}
			} else if (type == XMLStreamConstants.END_ELEMENT) {
				name = reader.getLocalName();
				if (GemConstantType.valueOf(name.toUpperCase()) == GemConstantType.GEM) {
					LOGGER.log(Level.DEBUG, gemstoneBuilder.build() + " has been built");
					return gemstoneBuilder.build();
				}
			}
		}
		throw new XMLStreamException("Wrong element tag");
	}

	private GemOption buildOption(XMLStreamReader reader) throws XMLStreamException {
		GemOption.InnerBuilder optionBuilder = new GemOption.InnerBuilder();

		int type;
		String name;
		while (reader.hasNext()) {
			type = reader.next();
			if (type == XMLStreamConstants.START_ELEMENT) {
				name = reader.getLocalName().toUpperCase();
				switch (GemConstantType.valueOf(name)) {
					case COLOR:
						optionBuilder.withColor(GemOption.GemColorType.valueOf(getXMLText(reader)));
						break;
					case OPACITY:
						optionBuilder.withOpacity(Double.parseDouble(getXMLText(reader)));
						break;
					case BOUND_NUMBER:
						optionBuilder.withBoundNumber(Integer.parseInt(getXMLText(reader)));
						break;
					default:
						throw new EnumConstantNotPresentException(GemConstantType.class, name);
				}
			} else if (type == XMLStreamConstants.END_ELEMENT) {
				name = reader.getLocalName();
				if (GemConstantType.valueOf(name.toUpperCase()) == GemConstantType.OPTION) {
					return optionBuilder.build();
				}
			}
		}
		throw new XMLStreamException("Unknown element in tag Address");
	}

	private String getXMLText(XMLStreamReader reader) throws XMLStreamException {
		String text = null;
		if (reader.hasNext()) {
			reader.next();
			if (reader.isCharacters()) {
				text = reader.getText();
			}
		}
		return text;
	}
}