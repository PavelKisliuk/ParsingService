package mil.pavelkisliuk.parsingservice.parser;

import mil.pavelkisliuk.parsingservice.model.GemConstantType;
import mil.pavelkisliuk.parsingservice.model.GemOption;
import mil.pavelkisliuk.parsingservice.model.Gemstone;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


public class SimpleDOMParser {
	public static final Logger LOGGER = LogManager.getLogger();

	private Set<Gemstone> gemstones = new HashSet<>();
	private DocumentBuilder documentBuilder;

	public SimpleDOMParser() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			//logger.error("Wrong parser configuration", e);
		}
	}

	public Set<Gemstone> getGemstones() {
		return gemstones;
	}

	public void buildSetDevices(String filename) {
		Document document;
		try {
			document = documentBuilder.parse(filename);
			Element root = document.getDocumentElement();
			NodeList gemstoneList = root.getElementsByTagName(GemConstantType.GEM.getValue());
			for (int i = 0; i < gemstoneList.getLength(); i++) {
				Element gemstoneElement = (Element) gemstoneList.item(i);
				Gemstone gemstone = buildGemstone(gemstoneElement);
				gemstones.add(gemstone);
			}
		} catch (IOException | SAXException e) {
			LOGGER.error("Could't parse " + filename, e);
		}
	}

	private Gemstone buildGemstone(Element gemstoneElement) {
		return new Gemstone.InnerBuilder()
				.withSerialNumber(gemstoneElement.getAttribute(GemConstantType.SERIAL_NUMBER.getValue()))
				.withCuriosity(Gemstone.CuriosityLevelType.valueOf(
						gemstoneElement.getAttribute(GemConstantType.CURIOSITY_LEVEL.getValue()).toUpperCase()))
				.withName(getElementTextContent(gemstoneElement, GemConstantType.NAME.getValue()))
				.withSemiprecious(Boolean.parseBoolean(
						getElementTextContent(gemstoneElement, GemConstantType.SEMIPRECIOUS.getValue())))
				.withOrigin(Gemstone.OriginType.valueOf(
						getElementTextContent(gemstoneElement, GemConstantType.ORIGIN.getValue()).toUpperCase()))
				.withPrice(new BigDecimal(getElementTextContent(gemstoneElement, GemConstantType.PRICE.getValue())))
				.withOption(buildOption((Element) gemstoneElement.getElementsByTagName(
						GemConstantType.OPTION.getValue()).item(0)))
				.build();
	}

	private GemOption buildOption(Element optionElement) {
		GemOption.InnerBuilder builder = new GemOption.InnerBuilder();

		if (optionElement.hasChildNodes()) {
			NodeList nodeList = optionElement.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeName().equals(GemConstantType.COLOR.getValue())) {
					builder.withColor(GemOption.GemColorType.valueOf(
							getElementTextContent(optionElement, GemConstantType.COLOR.getValue()).toUpperCase()));
				}
				if (nodeList.item(i).getNodeName().equals(GemConstantType.OPACITY.getValue())) {
					builder.withOpacity(Double.parseDouble(
							getElementTextContent(optionElement, GemConstantType.OPACITY.getValue())));
				}
				if (nodeList.item(i).getNodeName().equals(GemConstantType.BOUND_NUMBER.getValue())) {
					builder.withBoundNumber(Integer.parseInt(getElementTextContent(
							optionElement, GemConstantType.BOUND_NUMBER.getValue())));
				}
			}
		}

		return builder.build();
	}

	private String getElementTextContent(Element element, String elementName) {
		NodeList nList = element.getElementsByTagName(elementName);
		Node node = nList.item(0);
		return node.getTextContent();
	}
}
