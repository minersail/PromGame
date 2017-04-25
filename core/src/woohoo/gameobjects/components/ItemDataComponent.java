package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class ItemDataComponent implements Component
{    
    public enum ItemType // Used in ItemMetaData
    {
        Item("item"),
		Weapon("weapon");
		
		private String text;
		
		ItemType(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static ItemType fromString(String str) 
		{
			if (str == null) return Item;
			
			for (ItemType b : ItemType.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No ItemType with text " + str + " found.");
		}
    }
    
    public ItemType type;
	public int id;			   // Contains id used to get texture/name/description from idmanager
    public ObjectMap metaData; // Contains extra data
	public ObjectMap baseData; // Contains texture, name, and description
    
    public ItemDataComponent(int ID, ObjectMap meta, ObjectMap base)
    {
		id = ID;
		metaData = meta;
		baseData = base;
		type = ItemType.fromString((String)meta.get("type"));
		
		// Default keys will ensure no crashes
		metaData.put("count", meta.get("count", "1"));
		metaData.put("sell", meta.get("sell", "0"));
		metaData.put("buy", meta.get("buy", "0"));
    }          
}
