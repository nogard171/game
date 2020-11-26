package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.Building;
import classes.Chunk;
import classes.Object;
import classes.ObjectType;
import classes.ResourceData;
import data.Settings;
import data.UIData;
import data.WorldData;
import utils.FPS;
import utils.KeySystem;
import utils.Renderer;
import utils.View;
import utils.Window;
import utils.WorldGenerator;

public class UserInterface {
	public Rectangle menuBounds;
	public ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
	int mouseDownCount = 0;

	ObjectMenu objectMenu;

	EventManager eventManager;
	public static InventorySystem inventory;
	public static CharacterSystem character;
	public static SkillSystem skills;
	public static OptionSystem options;
	public static CraftingSystem crafting;
	public static SmeltingSystem smelting;
	public static BuildingSystem building;
	public static ChatSystem chat;
	public static HudSystem hud;

	public static MouseIndex hover;
	public static boolean inventoryHovered = false;
	public static boolean menuHovered = false;
	public static boolean characterHovered = false;
	public static boolean optionsHovered = false;

	public static boolean craftingHovered = false;
	public static boolean craftingDragging = false;

	public static boolean smeltingHovered = false;
	public static boolean smeltingDragging = false;

	public static boolean buildingHovered = false;
	public static boolean buildingDragging = false;

	public void setup() {

		MenuItem bag = new MenuItem(new AFunction() {
			public void click() {
				inventory.showSystem = !inventory.showSystem;
				character.showSystem = false;
				skills.showSystem = false;
				options.showSystem = false;
				chat.showSystem = false;
				building.showSystem = false;
			}
		});
		bag.bounds = new Rectangle(0, 0, 32, 32);
		bag.text = "Inventory";
		bag.material = "BAG";
		menu.add(bag);

		MenuItem chara = new MenuItem(new AFunction() {
			public void click() {
				character.showSystem = !character.showSystem;
				inventory.showSystem = false;
				skills.showSystem = false;
				options.showSystem = false;
				chat.showSystem = false;
				building.showSystem = false;
			}
		});
		chara.bounds = new Rectangle(32, 0, 32, 32);
		chara.text = "Character";
		chara.material = "CHARACTER_ICON";
		menu.add(chara);

		MenuItem skill = new MenuItem(new AFunction() {
			public void click() {
				skills.showSystem = !skills.showSystem;
				inventory.showSystem = false;
				character.showSystem = false;
				options.showSystem = false;
				chat.showSystem = false;
				building.showSystem = false;
			}
		});
		skill.bounds = new Rectangle(64, 0, 32, 32);
		skill.text = "Skills";
		skill.material = "SKILL_ICON";
		menu.add(skill);

		MenuItem chatIcon = new MenuItem(new AFunction() {
			public void click() {
				chat.showSystem = !chat.showSystem;
				inventory.showSystem = false;
				character.showSystem = false;
				skills.showSystem = false;
				options.showSystem = false;
				building.showSystem = false;
			}
		});
		chatIcon.bounds = new Rectangle(0, 0, 32, 32);
		chatIcon.text = "Chat";
		chatIcon.material = "CHAT_ICON";
		menu.add(chatIcon);

		MenuItem buildButton = new MenuItem(new AFunction() {
			public void click() {
				building.showSystem = !building.showSystem;
				options.showSystem = false;
				inventory.showSystem = false;
				character.showSystem = false;
				skills.showSystem = false;
				chat.showSystem = false;
			}
		});
		buildButton.bounds = new Rectangle(96, 0, 32, 32);
		buildButton.text = "Build";
		buildButton.material = "BUILD_ICON";
		menu.add(buildButton);

		MenuItem optionsButton = new MenuItem(new AFunction() {
			public void click() {
				options.showSystem = !options.showSystem;
				inventory.showSystem = false;
				character.showSystem = false;
				skills.showSystem = false;
				chat.showSystem = false;
				building.showSystem = false;
			}
		});
		optionsButton.bounds = new Rectangle(96, 0, 32, 32);
		optionsButton.text = "Options";
		optionsButton.material = "OPTIONS_ICON";
		menu.add(optionsButton);

		menuBounds = new Rectangle(0, Window.height - 32, menu.size() * 33, 32);

		inventory = new InventorySystem();
		inventory.setup();

		character = new CharacterSystem();
		character.setup();

		skills = new SkillSystem();
		skills.setup();

		options = new OptionSystem();
		options.setup();

		crafting = new CraftingSystem();
		crafting.setup();

		smelting = new SmeltingSystem();
		smelting.setup();

		building = new BuildingSystem();
		building.setup();

		eventManager = new EventManager();
		eventManager.setup();

		objectMenu = new ObjectMenu();
		objectMenu.setup();

		chat = new ChatSystem();
		chat.setup();

		hud = new HudSystem();
		hud.setup();
	}

	private void pollHover() {
		int cartX = Window.getMouseX() - View.x;
		int cartY = Window.getMouseY() - View.y;
		int isoX = cartX / 2 + (cartY);
		int isoY = cartY - cartX / 2;
		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);

		int chunkX = (int) Math.floor(indexX / 16);
		int chunkY = (int) Math.floor(indexY / 16);
		if (indexX < 0) {
			chunkX--;
		}
		if (indexY < 0) {
			chunkY--;
		}

		hover = new MouseIndex(indexX, indexY, chunkX, chunkY);
	}

	public void update() {
		if ((!inventoryHovered && !inventory.dragging) && !menuHovered && !characterHovered
				&& (!craftingHovered && !craftingDragging) && (!smeltingHovered && !smeltingDragging)
				&& (!buildingHovered && !buildingDragging) && !optionsHovered) {
			pollHover();
			objectMenu.update();
			if (Window.isMainAction() && hover != null) {
				if (UserInterface.crafting.showSystem) {
					UserInterface.crafting.showSystem = false;
				}
				if (!objectMenu.showObjectMenu && mouseDownCount == 0) {
					int hoverX = hover.getX();
					int hoverY = hover.getY();
					int chunkX = hoverX / 16;
					int chunkY = hoverY / 16;

					Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
					if (chunk != null) {
						if (BuildingSystem.building) {
							System.out.println("building");

							building.handleBuild(hover.getX(), hover.getY());
						

						} else {
							boolean ifMove = true;
							String action = "";
							int objX = hover.getX() % 16;
							int objY = hover.getY() % 16;
							if (objX >= 0 && objY >= 0) {

								Object ground = chunk.groundObjects[objX][objY];
								Object mask = chunk.maskObjects[objX][objY];
								Object item = chunk.groundItems[objX][objY];
								if (ground != null) {
									if (mask != null) {
										if (item != null) {
											ifMove = false;
											action = objectToAction(item);
											/*
											 * Event move = new Event(); move.eventName = "MOVE"; move.end = new
											 * Point(hover.getX(), hover.getY()); if (action != "MOVE") { Event
											 * secondary = new Event(); secondary.eventName = action; secondary.end =
											 * new Point(hover.getX(), hover.getY()); move.followUpEvent = secondary;
											 * 
											 * } EventManager.addEvent(move);
											 */
										} else if (mask.getMaterial() != "AIR") {
											ifMove = false;
											action = objectToAction(mask);

										}

									}
								}

							}
							if (!ifMove) {
								Event move = new Event();
								move.eventName = "MOVE";
								move.end = new Point(hover.getX(), hover.getY());
								if (action != "MOVE") {
									Event secondary = new Event();
									secondary.eventName = action;
									secondary.end = new Point(hover.getX(), hover.getY());
									move.followUpEvent = secondary;

								}
								EventManager.addEvent(move);
							}
							if (ifMove) {
								Event move = new Event();
								move.eventName = "MOVE";
								move.end = new Point(hover.getX(), hover.getY());
								EventManager.addEvent(move);
							}
						}
					}
					mouseDownCount++;
				}
			}
			if (!Window.isMainAction() && mouseDownCount > 0) {
				mouseDownCount = 0;
			}
		}
		if (Window.wasResized())

		{
			menuBounds = new Rectangle(0, Window.height - 32, menu.size() * 33, 32);
		}
		eventManager.update();
		inventory.update();
		character.update();
		skills.update();
		options.update();
		crafting.update();
		smelting.update();
		building.update();
		chat.update();
		hud.update();

		if (KeySystem.keyPressed(Keyboard.KEY_I)) {
			inventory.showSystem = !inventory.showSystem;

			character.showSystem = false;
			keyDownCount++;
		}

		if (KeySystem.keyPressed(Keyboard.KEY_C)) {
			character.showSystem = !character.showSystem;

			inventory.showSystem = false;
			keyDownCount++;
		}

		KeySystem.poll();
		if (KeySystem.keyPressed(Keyboard.KEY_A)) {

		}

		if (menuBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
			menuHovered = true;
		} else {
			menuHovered = false;
		}
		if (menuHovered) {
			for (MenuItem item : menu) {
				if (item.bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
					item.hovered = true;
					if (Mouse.isButtonDown(0)) {
						item.click();
					} else {
						item.unclick();
					}
				} else {
					item.hovered = false;
				}
			}
		}
	}

	private int keyDownCount = 0;

	public void renderOnMap() {
		if (hover != null) {
			GL11.glColor4f(0f, 0, 0, 0.5f);
			GL11.glBegin(GL11.GL_QUADS);
			Renderer.renderGrid(hover.getX(), hover.getY());
			GL11.glEnd();
		}

		building.renderOnMap();
		objectMenu.render();

	}

	public void render() {
		inventory.render();
		character.render();
		skills.render();
		options.render();
		crafting.render();
		smelting.render();
		building.render();
		chat.render();
		hud.render();

		Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
				new Color(0, 0, 0, 0.5f));
		int i = 0;
		for (MenuItem item : menu) {
			if (item.hovered) {
				Renderer.renderRectangle(item.bounds.x, item.bounds.y, 32, 32, new Color(1, 1, 1, 0.25f));

			}
		}
		GL11.glBegin(GL11.GL_TRIANGLES);
		for (MenuItem item : menu) {
			item.bounds.x = menuBounds.x + (i * 33);
			item.bounds.y = menuBounds.y;

			Renderer.renderModel(item.bounds.x, item.bounds.y, "SQUARE", item.material, new Color(1, 1, 1, 1f));

			i++;
		}
		GL11.glEnd();

		Point debugPosition = new Point(Window.width - 200, 0);
		Renderer.renderRectangle(debugPosition.x, debugPosition.y, 200, 100, new Color(0, 0, 0, 0.5f));
		if (hover != null) {
			Renderer.renderText(new Vector2f(debugPosition.x, debugPosition.y), "FPS:" + FPS.getFPS(), 12, Color.white);

			Renderer.renderText(new Vector2f(debugPosition.x, debugPosition.y + 12),
					"Hover:" + hover.getX() + "," + hover.getY(), 12, Color.white);
			int hoverX = hover.getX();
			int hoverY = hover.getY();
			int chunkX = hoverX / 16;
			int chunkY = hoverY / 16;

			if (hoverX < 0) {
				chunkX -= 1;

			}
			if (hoverY < 0) {
				chunkY -= 1;
			}

			Renderer.renderText(new Vector2f(debugPosition.x, debugPosition.y + 24), "chunk:" + chunkX + "," + chunkY,
					12, Color.white);
			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = hover.getX() % 16;
				int objY = hover.getY() % 16;

				if (hoverX < 0) {
					objX *= -1;

				}
				if (hoverY < 0) {
					objY *= -1;
				}

				Object ground = chunk.groundObjects[objX][objY];
				Object mask = chunk.maskObjects[objX][objY];
				Object item = chunk.groundItems[objX][objY];
				if (ground != null) {
					String maskString = "/";
					if (mask != null) {
						if (item != null) {
							maskString += item.name + "(I)";
						} else {
							maskString += mask.name + "(M)";
						}
					}

					Renderer.renderText(new Vector2f(debugPosition.x, debugPosition.y + 36),
							"Object:" + ground.getMaterial() + "(G)" + maskString, 12, Color.white);
				}

			}

		}
		if (WorldGenerator.centerIndex != null) {
			Renderer.renderText(new Vector2f(debugPosition.x, debugPosition.y + 48),
					"Center Index:" + WorldGenerator.chunkIndex.getX() + "," + WorldGenerator.chunkIndex.getY(), 12,
					Color.white);
		}
		int size = 0;
		if (eventManager.events != null) {
			size = eventManager.events.size();
		}
		for (Event ev : eventManager.events) {
			// System.out.println("event: " + ev.eventName);
		}
		Renderer.renderText(new Vector2f(debugPosition.x, debugPosition.y + 60), "Event Count:" + size, 12,
				Color.white);
		Renderer.renderText(new Vector2f(debugPosition.x, debugPosition.y + 72),
				"Chunk Render Count:" + WorldData.chunks.size(), 12, Color.white);

	}

	public static MouseIndex getHover() {
		return hover;
	}

	public void clean() {

	}

	public static String objectToAction(Object obj) {
		String action = "MOVE";
		if (obj != null) {
			MenuItem menuItem;
			/*
			 * if (obj.getMaterial() == "TREE") { action = "CHOP"; } else if
			 * (obj.getMaterial() == "WHEAT") { action = "HARVEST"; } else if
			 * (obj.getMaterial() == "ORE") { action = "MINE"; } else if
			 * (obj.getMaterial().contains("CRAFTING")) { action = "CRAFT"; }
			 */

			ResourceData data = UIData.resourceData.get(obj.getMaterial());
			if (data != null) {
				action = data.action.toUpperCase();
				System.out.println("action: " + obj.getMaterial() + "=" + data.action);
			}
			System.out.println("action: " + obj.type);

			if (obj.type.equals(ObjectType.ITEM)) {
				action = "PICKUP";
			}

		}
		return action;
	}
}