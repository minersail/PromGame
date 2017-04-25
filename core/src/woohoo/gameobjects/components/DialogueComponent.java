package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.ArrayList;

public class DialogueComponent implements Component
{
    private ArrayList<DialogueLine> sequence;
    private int index;
    
	/**
	 * Holds all the dialogue component for an NPC, unless event tag is true.
	 * If event tag is true, holds all the dialogue for an event.
	 * @param id ID to locate the dialogue in XML
	 * @param cutscene Whether this is for an NPC or an event
	 */
	public DialogueComponent(int id, boolean cutscene)
    {
        sequence = new ArrayList<>();
        
        FileHandle handle = Gdx.files.local(cutscene ? "data/cutscenedialogue.xml" : "data/dialogue.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());       
        Element dialogue = root.getChild(id);
        
        for (Element e : dialogue.getChildrenByName("line"))
        {
            sequence.add(new DialogueLine(e.get("name"), e.get("text"), e.getInt("character"), e.getInt("choices", -1)));
        }
    }
    
    public DialogueLine getCurrentLine()
    {
        if (index < sequence.size())
            return sequence.get(index);
        else
            return null; //sequence.get(sequence.size() - 1);
    }
	
	public void advance()
	{
		index++;
	}
	
	/*
	Advances the index to the given choice
	*/
	public void advanceChoice(int choice)
	{
		int i = index;
		while (choice >= 0)
		{
			i++;
			if (sequence.get(i).name().equals("Choice"))
				choice--;
		}
		
		index = i;
	}
	
	public void advanceToChoiceEnd()
	{
		while (!(sequence.get(index).id() == -1 && sequence.get(index).text().equals("ENDCHOICE")))
		{
			index++;
		}
	}
	
	/*
	Re-sets the dialogue to the previous break, or to beginning if none are found
	*/
	public void loop()
	{
		boolean foundLoop = false;
		for (int i = index - 1; i >= 0; i--)
		{
			if (sequence.get(i).id() == -1 && sequence.get(i).text.equals("BREAK"))
			{
				index = i + 1;
				foundLoop = true;
			}
		}
		
		if (!foundLoop) index = 0;
	}
	
	/**
	 * Given the index of a starting choice, return all choices' strings until the end choice
	 * @return array of strings of the choices
	 */
	public Array<String> getChoices()
	{
		if (sequence.get(index).choices == -1)
		{
			Gdx.app.log("ERROR", "Dialogue index is not at a choice start");
			return null;
		}
		
		Array<String> choices = new Array<>();
		
		int i = index;
		// Search until the end choice for all choice options
		while (!(sequence.get(i).id() == -1 && sequence.get(i).text().equals("ENDCHOICE")))
		{
			if (sequence.get(i).id() == -1 && sequence.get(i).name().equals("Choice"))
			{
				choices.add(sequence.get(i).text());
			}
			i++;
		}
		
		return choices;
	}
	
	public class DialogueLine
	{
		private String name;
		private String text;
		private int id;
		private int choices;
		
		public DialogueLine(String name, String text, int id, int choices)
		{
			this.name = name;
			this.text = text;
			this.id = id;
			this.choices = choices;
		}
		
		public String name() { return name; }
		public String text() { return text; }
		public int id() { return id; }
		public int choices() { return choices; }
	}
}
