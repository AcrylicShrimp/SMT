package smt;

import java.awt.Color;
import java.awt.Font;

import game.GameText;

public class Word extends GameText {
	
	public float speed;
	
	public Word(Font new_font, float font_size, int new_width, int new_height, int new_alignh, int new_alignv,
			Color new_color) {
		super(new_font, font_size, new_width, new_height, new_alignh, new_alignv, new_color);
		// TODO Auto-generated constructor stub
		
		this.speed = (float)(Math.random() * (250.0) + 50.0);
	}
}