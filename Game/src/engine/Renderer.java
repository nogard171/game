package engine;

import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import game.ResourceDatabase;

public class Renderer {

	public static void renderSprite(String name, int x, int y) {
		Sprite sprite = ResourceDatabase.getSprite(name);
		if (sprite != null) {
			Texture texture = ResourceDatabase.getTexture();
			for (int i = 0; i < sprite.shape.npoints; i++) {
				GL11.glTexCoord2f((float) sprite.texture.xpoints[i] / (float) texture.getImageWidth(),
						(float) sprite.texture.ypoints[i] / (float) texture.getImageHeight());
				GL11.glVertex2i(x + sprite.shape.xpoints[i] + sprite.offset.x,
						y + sprite.shape.ypoints[i] + sprite.offset.y);
			}
		}
	}

	public static void renderQuad(Rectangle bound, Color color) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor4f(color.r, color.g, color.b, color.a);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2i(bound.x, bound.y);
		GL11.glVertex2i(bound.x + bound.width, bound.y);
		GL11.glVertex2i(bound.x + bound.width, bound.y + bound.height);
		GL11.glVertex2i(bound.x, bound.y + bound.height);

		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderText(Point position, String text, int fontSize, Color color) {

		renderText(position.x, position.y, text, fontSize, color);
	}

	public static void renderText(int x, int y, String text, int fontSize, Color color) {

		TrueTypeFont font = ResourceDatabase.getFont(fontSize);

		if (font == null) {
			Font awtFont = new Font("Courier", Font.PLAIN, fontSize);
			ResourceDatabase.addFont(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			unbindTexture();
			font.drawString(x, y, text, color);
		}
	}

	public static void bindTexture() {

		bindTexture("tileset");
	}

	public static void bindTexture(String name) {

		boolean isBound = ResourceDatabase.isBoundTexture(name);
		if (!isBound) {
			Texture texture = ResourceDatabase.getTexture();
			if (texture != null) {
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
				ResourceDatabase.addBoundTexture(name);
			}
		}
	}

	public static void unbindTexture() {
		TextureImpl.bindNone();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		ResourceDatabase.clearBoundTextures();
	}
}
