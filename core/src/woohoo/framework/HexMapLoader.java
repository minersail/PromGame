package woohoo.framework;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.contactcommands.ContactData;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.screens.PlayingScreen;

public class HexMapLoader
{
	PlayingScreen screen;
	public HexMapLoader(PlayingScreen scr)
	{
		screen = scr;
	}
	
	public TiledMap load(int area)
	{		
        FileHandle handle = Gdx.files.local("data/areas.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());       
        Element areaData = root.getChild(area);  
        int tilesetNum = areaData.getInt("tileset", 0);
        int d_tilesetNum = areaData.getInt("d_tileset", 0);
        
        Texture tileset = screen.getAssets().get("images/tilesets/tileset" + tilesetNum + ".png", Texture.class);
        Texture decorationTileset = screen.getAssets().get("images/tilesets/d_tileset" + d_tilesetNum + ".png", Texture.class);
        
		FileHandle mapHandle = Gdx.files.internal("maps/" + area + ".txt");
		String map = mapHandle.readString().replace("\r", "");		
															// NOTES AFTER A FRUSTRATING DEBUG SESSION
		String[] rows = map.split("\n");					// For some reason, libgdx will turn \n into some random combination of \r\n.
															// To prevent this, use Gdx.files.internal, rather than Gdx.files.local.
		int mapWidth = (rows[0].length() + 1) / 9;			// All maps used by this game may have \r\n, or \n. \rs will be filtered out.
		int mapHeight = rows.length;						// The final line's newline is optional (works either way).
															
		TiledMapTileLayer layer1 = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);
		TiledMapTileLayer layer2 = new TiledMapTileLayer(mapWidth, mapHeight, 16, 16);
        
		int i = 0;
		int j = 0;
		for (String row : rows)
		{
			String[] tiles = row.split(" ");
			for (String tile : tiles)
			{	
                int decorRot = Integer.parseInt(tile.substring(1, 2), 16);
                int decorID = Integer.parseInt(tile.substring(2, 4), 16);
				int funcID = Integer.parseInt(tile.substring(4, 6), 16);
				int tileID = Integer.parseInt(tile.substring(6, 8), 16);
				int tileWidth = ((PlayingScreen)screen).T_TILE_WIDTH;
				int tileHeight = ((PlayingScreen)screen).T_TILE_HEIGHT;

				int columns = tileset.getWidth() / tileWidth;
				int tileX = (tileID % columns) * tileWidth;
				int tileY = (tileID / columns) * tileHeight;                
                
				int columns2 = decorationTileset.getWidth() / tileWidth;
				int tileX2 = (decorID % columns2) * tileWidth;
				int tileY2 = (decorID / columns2) * tileHeight;

				TextureRegion texture = new TextureRegion(tileset, tileX, tileY, 
														  tileWidth, tileHeight);
				texture.flip(false, true);
				
				StaticTiledMapTile t = new StaticTiledMapTile(texture);
				t.setId(Integer.parseInt(tile.substring(4, 8), 16));
				t.getProperties().put("isWall", funcID >= 4 && funcID <= 7); // funcIDs between 4 and 7 represent walls
                				
				if (t.getProperties().get("isWall", Boolean.class))
				{					
					BodyDef bodyDef = new BodyDef();
					bodyDef.type = BodyDef.BodyType.StaticBody;
					bodyDef.position.set(i + 0.5f, j + 0.5f);

					Body body = screen.getWorld().createBody(bodyDef);

					PolygonShape shape = new PolygonShape();
					shape.setAsBox(0.5f, 0.5f);

					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = shape;
					fixtureDef.density = 1f;
					fixtureDef.friction = 0f;

					body.createFixture(fixtureDef);
					
					Entity wall = new Entity();
					screen.getEngine().addEntity(wall);
					body.setUserData(new ContactData(ContactType.Wall, wall));
				}
					
				Cell cell = new Cell();
				cell.setTile(t);
				cell.setRotation(funcID % 4);
				layer1.setCell(i, j, cell);                
                
                if (decorID != 0)
                {
                    TextureRegion decoration = new TextureRegion(decorationTileset, tileX2, tileY2, 
                                                                 tileWidth, tileHeight);
                    decoration.flip(false, true);
                    
                    StaticTiledMapTile t2 = new StaticTiledMapTile(decoration);
                    t2.setId(Integer.parseInt(tile.substring(0, 4), 16));
                    
                    Cell cell2 = new Cell();
                    cell2.setTile(t2);
                    cell2.setRotation(decorRot % 4);
                    layer2.setCell(i, j, cell2);
                }
				
				i++;
			}
			j++;
			i = 0;
		}
		
		screen.mapWidth = mapWidth;
		screen.mapHeight = mapHeight;
		
		MapLayer items = new MapLayer();
		items.setName("Items");
		MapLayer entities = new MapLayer();
		entities.setName("Entities");
                        
        TiledMap tiledMap = new TiledMap();
		layer1.setName("Base");
		layer2.setName("Decorations");
        tiledMap.getLayers().add(layer1);
		tiledMap.getLayers().add(items);
		tiledMap.getLayers().add(entities);
        tiledMap.getLayers().add(layer2);
		
		return tiledMap;
	}
}