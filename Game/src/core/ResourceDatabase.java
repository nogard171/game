package core;

import java.awt.Point;
import java.awt.Polygon;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class ResourceDatabase {
	public static Texture texture;

	public static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	public static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();

	public static void load() {
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("assets/textures/tileset.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sprite unknown = new Sprite();
		Polygon newShape = new Polygon();
		newShape.addPoint(0, 0);
		newShape.addPoint(64, 0);
		newShape.addPoint(64, 64);
		newShape.addPoint(0, 64);
		unknown.shape = newShape;

		Polygon unknownTexture = new Polygon();
		unknownTexture.addPoint(0, 0);
		unknownTexture.addPoint(64, 0);
		unknownTexture.addPoint(64, 64);
		unknownTexture.addPoint(0, 64);
		unknown.texture = unknownTexture;

		sprites.put("unknown", unknown);

		Sprite grass = new Sprite();
		grass.shape = newShape;

		Polygon grassTexture = new Polygon();
		grassTexture.addPoint(64, 0);
		grassTexture.addPoint(128, 0);
		grassTexture.addPoint(128, 64);
		grassTexture.addPoint(64, 64);
		grass.texture = grassTexture;

		sprites.put("grass", grass);

	}
}