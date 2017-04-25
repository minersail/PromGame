package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.Iterator;

/*
Class managing data for character/items

Does NOT represent the in-game physics, position, etc.

Think of it like an imdb style list of all characters
 */
public class IDManager
{
	private IntMap<CharacterData> characters;
	private IntMap<ItemData> items;
	
	public IDManager(AssetManager manager)
	{
		characters = new IntMap<>();
		items = new IntMap<>();
		
		// Load items
		{
			FileHandle handle = Gdx.files.local("data/items.xml");

			XmlReader xml = new XmlReader();
			Element root = xml.parse(handle.readString());

			for (int i = 0; i < root.getChildCount(); i++)
			{
				Element e = root.getChild(i);

				ItemData itemdata = new ItemData(manager.get("images/items/" + e.get("texture"), Texture.class), e.get("name"), e.get("description"));

				items.put(e.getInt("id"), itemdata);
			}
		}
		
		// Load characters
		{
			FileHandle handle = Gdx.files.local("data/characters.xml");

			XmlReader xml = new XmlReader();
			Element root = xml.parse(handle.readString());

			for (int i = 0; i < root.getChildCount(); i++)
			{
				Element e = root.getChild(i);
				
				CharacterData characterData = new CharacterData(manager.get("images/faces/" + e.get("texture"), Texture.class), e.get("name"));

				characters.put(e.getInt("id"), characterData);
			}
		}
	}
	
	public CharacterData getCharacter(int ID)
	{
		if (ID >= characters.size)
			return null;
		
		return characters.get(ID);
	}
	
	public CharacterData getCharacter(String name)
	{
		Iterator<Entry<CharacterData>> iter = characters.iterator();
		
		while (iter.hasNext())
		{
			Entry<CharacterData> entry = iter.next();
			
			if (entry.value.getName().equals(name))
			{
				return entry.value;
			}
		}
		
        return null;
	}
    
    public ItemData getItem(int ID)
	{
		if (ID >= items.size)
			return null;
		
		return items.get(ID);
	}
	
	public ItemData getItem(String name)
	{
		Iterator<Entry<ItemData>> iter = items.iterator();
		
		while (iter.hasNext())
		{
			Entry<ItemData> entry = iter.next();
			
			if (entry.value.getName().equals(name))
			{
				return entry.value;
			}
		}
		
        return null;
	}
    
    public class CharacterData
    {
        private TextureRegion face;
        private String name;

        public CharacterData(Texture texture, String str)
        {
            face = new TextureRegion(texture);
            name = str;
        }

        public TextureRegion getFace()
        {
            return face;
        }

        public String getName() 
        {
            return name;
        }
    }
    
    public class ItemData
    {
        private TextureRegion item;
        private String name;
		private String description;

        public ItemData(Texture texture, String itemName, String itemDescription)
        {
            item = new TextureRegion(texture);
            name = itemName;
			description = itemDescription;
        }

        public TextureRegion getItemTexture()
        {
            return item;
        }

        public String getName() 
        {
            return name;
        }
		
		public String getDescription()
		{
			return description;
		}
		
		public ObjectMap toObjectMap()
		{
			ObjectMap map = new ObjectMap();
			
			map.put("texture", item);
			map.put("name", name);
			map.put("description", description);
			
			return map;
		}
    }
}
