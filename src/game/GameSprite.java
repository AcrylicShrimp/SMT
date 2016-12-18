package game;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class GameSprite implements GameRenderable, GameDisposable {
	
	public boolean visible = true;
	public float x = 0f;
	public float y = 0f;
	public float opaque = 1f;
	public int width;
	public int height;
	public Image sprite;
	
	public GameSprite(String sprite_path) {
		ImageIcon image_icon = new ImageIcon(sprite_path);
		
		this.width = image_icon.getIconWidth();
		this.height = image_icon.getIconHeight();
		this.sprite = image_icon.getImage();
		
		Game.game_scene.add_renderable(this);
	}
	
	public void reset_sprite(String sprite_path) {
		ImageIcon image_icon = new ImageIcon(sprite_path);
		
		this.sprite = image_icon.getImage();
		this.width = image_icon.getIconWidth();
		this.height = image_icon.getIconHeight();
	}

	@Override
	public void render(Graphics2D graphics) {
		// TODO Auto-generated method stub
		if(this.visible) {
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.opaque));
			graphics.drawImage(
					this.sprite,
					(int)((Game.GAME_FRAME_WIDTH - this.width) * .5f + this.x + .5f), 
					(int)((Game.GAME_FRAME_HEIGHT - this.height) * .5f + this.y + .5f),
					this.width,
					this.height,
					null);
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Game.game_scene.remove_sprite(this);
	}
	
	public boolean is_collide(float x, float y) {
		return Math.abs(this.x - x) <= this.width * .5f &&
				Math.abs(this.y - y) <= this.height * .5f;
	}
	
	public boolean is_collide(GameSprite other_sprite) {
		return Math.abs(this.x - other_sprite.x) <= (this.width + other_sprite.width) * .5f &&
				Math.abs(this.y - other_sprite.y) <= (this.height + other_sprite.height) * .5f;
	}
}