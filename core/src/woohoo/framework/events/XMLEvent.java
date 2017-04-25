package woohoo.framework.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public abstract class XMLEvent implements Event
{
	protected String filename;
	protected String attributeName;
	protected String attributeValue;
	
	protected XMLEvent(String file, String name, String value)
	{
		filename = file;
		attributeName = name;
		attributeValue = value;
	}	
	
	@Override
	public void activate()
	{
		FileHandle handle = Gdx.files.local("data/" + filename);
		
		XmlReader reader = new XmlReader();
		Element root = reader.parse(handle.readString());
		Element targetElement = getElement(root);
		
		targetElement.setAttribute(attributeName, attributeValue);
		handle.writeString(root.toString(), false);
	}
	
	public abstract Element getElement(Element root);
}
