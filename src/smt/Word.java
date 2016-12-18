package smt;

import java.awt.Color;
import java.awt.Font;

import game.GameSprite;
import game.GameText;

public class Word extends GameSprite {
	
	public float speed;
	public GameText text;
	
	public Word(Font new_font, float font_size, int new_width, int new_height, int new_alignh, int new_alignv,
			Color new_color) {
		// TODO Auto-generated constructor stub
		super("res/cat.png");
		this.text = new GameText(new_font, font_size, new_width, new_height, new_alignh, new_alignv, new_color);
		this.speed = (float)(Math.random() * (250.0) + 50.0);
		
		this.width *= 0.3f;
		this.height *= 0.3f;
	}
	
	public void sync_position() {
		this.text.x = this.x;
		this.text.y = this.y;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		this.text.dispose();
	}
}