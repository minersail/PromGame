package woohoo.framework.events;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * XMLElement implementation for XML files split into areas (e.g. entities, events, etc.)
 * @author jordan
 */
public class AttributeXMLEvent extends XMLEvent
{
	private int searchArea;
	private String selectorName;
	private String selectorValue;
	private String elementName;
	
	/** Takes three additional parameters, the area to search and a selector pair for the element to modify
	 * @param file The file to modify (does not need path prefix, but does need .xml suffix)
	 * @param attributeName The modified attribute's name
	 * @param attributeValue The modified attribute's value
	 * @param area The area in which to search (in order to use this class the xml file must be separated by area
	 * @param element The element owning the attribute
	 * @param selector An attribute to select the child by (the attribute's name)
	 * @param selectorVal An attribute to select the child by (the attribute's value) */
	public AttributeXMLEvent(String file, String attributeName, String attributeValue, int area, String element, String selector, String selectorVal)
	{
		super(file, attributeName, attributeValue);
		searchArea = area;
		elementName = element;
		selectorName = selector;
		selectorValue = selectorVal;
	}

	@Override
	public Element getElement(XmlReader.Element root)
	{
		for (XmlReader.Element element : root.getChild(searchArea).getChildrenByNameRecursively(elementName))
		{
			if (element.get(selectorName).equals(selectorValue))
				return element;
		}

		return null;
	}
}
