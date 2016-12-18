package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GameText implements GameRenderable, GameDisposable {
	
	public static final int ALIGN_LEFT = -1;
	public static final int ALIGN_UP = -1;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_DOWN = 1;
	public static final int ALIGN_CENTER = 0;
	
	public boolean visible = true;
	public float x = 0f;
	public float y = 0f;
	public float opaque = 1f;
	public int width;
	public int height;
	public int alignh;
	public int alignv;
	public Font font;
	public String text;
	public Color color;
	
	public GameText(Font new_font, float font_size, int new_width, int new_height, int new_alignh, int new_alignv, Color new_color) {
		this.font = new_font.deriveFont(font_size);
		this.width = new_width;
		this.height = new_height;
		this.alignh = new_alignh;
		this.alignv = new_alignv;
		this.text = "";
		this.color = new_color;
		
		Game.game_scene.add_renderable(this);
	}

	@Override
	public void render(Graphics2D graphics) {
		// TODO Auto-generated method stub
		if(this.visible) {
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.opaque));
			graphics.setFont(this.font);
			graphics.setColor(this.color);
			
			FontMetrics sMetrics = graphics.getFontMetrics();
			float render_x = (Game.GAME_FRAME_WIDTH - this.width) * .5f + this.x;
			float render_y = (Game.GAME_FRAME_HEIGHT - this.height) * .5f + this.y + sMetrics.getAscent();
			
			if(this.alignh > 0)
				render_x += this.width - sMetrics.stringWidth(this.text);
			else if(this.alignh == 0)
				render_x += (this.width - sMetrics.stringWidth(this.text)) * .5f;
			
			if(this.alignv > 0)
				render_y += this.height - sMetrics.getHeight();
			else if(this.alignv == 0)
				render_y += (this.height - sMetrics.getHeight()) * .5f;
			
			graphics.drawString(this.text, render_x, render_y);
		}
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Game.game_scene.remove_sprite(this);
	}
}