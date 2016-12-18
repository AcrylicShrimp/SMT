package game.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.Game;
import game.GameText;

public class KORInput extends GameText implements KeyListener {
	
	public KORInput(Font new_font, float font_size, int new_width, int new_height, int new_alignh, int new_alignv, Color new_color) {
		super(new_font, font_size, new_width, new_height, new_alignh, new_alignv, new_color);
		// TODO Auto-generated constructor stub
	}
	
	public int max_char = Integer.MAX_VALUE;
	public boolean is_kor = false;
	
	private String input_text = "";
	private boolean is_editing = false;
	private boolean is_first = false;
	private boolean is_second = false;
	private boolean is_last = false;
	private char first_char = '?';
	private char second_char = '?';
	private char last_char = '?';
	
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
			
			graphics.drawString(this.text + '|', render_x, render_y);
		}
	}

	public void bind_key() {
		Game.game_frame.addKeyListener(this);
	}
	
	public void unbind_key() {
		Game.game_frame.removeKeyListener(this);
	}
	
	public void process_input(int key, boolean is_shifted) {
		if(key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE) {
			if(this.is_editing) {
				if(this.is_last) {
					if(KORInput.is_can_uncombine_last(this.last_char))
						this.last_char = KORInput.uncombine_last(this.last_char);
					else
						this.is_last = false;
					
				} else if(this.is_second) {
					if(KORInput.is_can_uncombine_second(this.second_char))
						this.second_char = KORInput.uncombine_second(this.second_char);
					else {
						this.is_second = false;
						this.is_editing = this.is_first;
					}
					
				} else {
					this.is_first = false;
					this.is_editing = false;
				}
				
			} else if(!this.input_text.isEmpty())
				this.input_text = this.input_text.substring(0, this.input_text.length() - 1);
			
		} else {
			if(this.input_text.length() >= this.max_char)
				return;
			
			char new_char = this.is_kor ? KORInput.get_kor(key, is_shifted) : '?';
			
			if(new_char == '?') {
				new_char = (char)key;
				
				if(!Character.isWhitespace((char)key) || (char)key == ' ') {
					this.submit_text();
					
					if(this.input_text.length() >= this.max_char)
						return;
					
					this.input_text += (char)key;
				}
				
				return;
			}
			
			if(!this.is_editing) {
				this.is_editing = true;
				
				if(KORInput.is_g(key)) {
					this.is_first = true;
					this.first_char = new_char;
					
				} else {
					this.is_second = true;
					this.second_char = new_char;
				}
				
			} else if(this.is_first && !this.is_second) {
				if(KORInput.is_g(key)){
					if(this.input_text.length() + 1 >= this.max_char)
						return;
					
					this.submit_text();
					
					this.is_editing = true;
					this.is_first = true;
					this.first_char = new_char;
				} else {
					this.is_second = true;
					this.second_char = new_char;
				}
				
			} else if(this.is_second && !this.is_last) {
				if(KORInput.is_g(key)) {
					if(this.is_first) {
						if(KORInput.get_last_index(new_char) != 0) {
							this.is_last = true;
							this.last_char = new_char;
						} else {
							if(this.input_text.length() + 1 >= this.max_char)
								return;
							
							this.submit_text();
							
							this.is_editing = true;
							this.is_first = true;
							this.first_char = new_char;
						}
						
					} else {
						if(this.input_text.length() + 1 >= this.max_char)
							return;
						
						this.submit_text();
						
						this.is_editing = true;
						this.is_first = true;
						this.first_char = new_char;
					}
					
				} else if(KORInput.is_can_combine_second(this.second_char, new_char))
					this.second_char = KORInput.combine_second(this.second_char, new_char);
				
				else {
					if(this.input_text.length() + 1 >= this.max_char)
						return;
					
					this.submit_text();
					
					this.is_editing = true;
					this.is_second = true;
					this.second_char = new_char;
				}
				
			} else {
				if(KORInput.is_g(key) && KORInput.is_can_combine_last(this.last_char, new_char))
					this.last_char = KORInput.combine_last(this.last_char, new_char);
				
				else if(KORInput.is_m(key)) {
					if(this.input_text.length() + 1 >= this.max_char)
						return;
					
					char new_first;
					
					if(KORInput.is_can_uncombine_last(this.last_char)) {
						new_first = KORInput.uncombined_last_second(this.last_char);
						this.last_char = KORInput.uncombine_last(this.last_char);
					} else {
						new_first = this.last_char;
						this.is_last = false;
					}
					
					this.submit_text();
					
					this.is_editing = true;
					this.is_first = true;
					this.is_second = true;
					this.first_char = new_first;
					this.second_char = new_char;
					
				} else {
					if(this.input_text.length() + 1 >= this.max_char)
						return;
					
					this.submit_text();
					
					this.is_editing = true;
					this.is_first = true;
					this.first_char = new_char;
				}
			}
		}
	}
	
	public String get_editing_text() {
		if(this.is_editing) {
			if(this.is_first) {
				if(this.is_second)
					if(this.is_last)
						return KORInput.make_kor(this.first_char, this.second_char, this.last_char);
					else
						return KORInput.make_kor(this.first_char, this.second_char, ' ');
				else
					return String.valueOf(this.first_char);
			} else if(this.is_second)
				return String.valueOf(this.second_char);
			else
				return String.valueOf(this.last_char);
		}
		
		return "";
	}
	
	public void update_text() {
		this.text = this.input_text + this.get_editing_text();
	}
	
	public void submit_text() {
		this.input_text += this.get_editing_text();
		
		this.is_editing = false;
		this.is_first = false;
		this.is_second = false;
		this.is_last = false;
	}
	
	public void clear_text() {
		this.text = this.input_text = "";
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//Ignore.
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//Ignore.
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		this.process_input(e.getKeyChar(), e.isShiftDown());
		this.update_text();
	}
	
	public static boolean is_g(int keycode) {
		switch(Character.toLowerCase(keycode)) {
		case 'q':
		case 'w':
		case 'e':
		case 'r':
		case 't':
		case 'a':
		case 's':
		case 'd':
		case 'f':
		case 'g':
		case 'z':
		case 'x':
		case 'c':
		case 'v':
			return true;
			
			default:
				return false;
		}
	}
	
	public static boolean is_m(int keycode) {
		switch(Character.toLowerCase(keycode)) {
		case 'y':
		case 'u':
		case 'i':
		case 'o':
		case 'p':
		case 'h':
		case 'j':
		case 'k':
		case 'l':
		case 'b':
		case 'n':
		case 'm':
			return true;
			
			default:
				return false;
		}
	}
	
	public static char get_kor(int keycode, boolean is_shifted) {
		switch(Character.toLowerCase(keycode)) {
		case 'q':
			return is_shifted ? 'こ' : 'げ';
			
		case 'w':
			return is_shifted ? 'す' : 'じ';
			
		case 'e':
			return is_shifted ? 'え' : 'ぇ';
			
		case 'r':
			return is_shifted ? 'あ' : 'ぁ';
			
		case 't':
			return is_shifted ? 'ざ' : 'さ';
			
		case 'a':
			return 'け';
			
		case 's':
			return 'い';
			
		case 'd':
			return 'し';
			
		case 'f':
			return 'ぉ';
			
		case 'g':
			return 'ぞ';
			
		case 'z':
			return 'せ';
			
		case 'x':
			return 'ぜ';
			
		case 'c':
			return 'ず';
			
		case 'v':
			return 'そ';
			
		case 'y':
			return 'に';
			
		case 'u':
			return 'づ';
			
		case 'i':
			return 'ち';
			
		case 'o':
			return is_shifted ? 'ぢ' : 'だ';
			
		case 'p':
			return is_shifted ? 'て' : 'つ';
			
		case 'h':
			return 'で';
			
		case 'j':
			return 'っ';
			
		case 'k':
			return 'た';
			
		case 'l':
			return 'び';
			
		case 'b':
			return 'ば';
			
		case 'n':
			return 'ぬ';
			
		case 'm':
			return 'ぱ';
			
			default:
				return '?';
		}
	}
	
	public static int get_first_index(char first_char) {
		switch(first_char) {
		case 'ぁ':
			return 0;
			
		case 'あ':
			return 1;
			
		case 'い':
			return 2;
			
		case 'ぇ':
			return 3;
			
		case 'え':
			return 4;
			
		case 'ぉ':
			return 5;
			
		case 'け':
			return 6;
			
		case 'げ':
			return 7;
			
		case 'こ':
			return 8;
			
		case 'さ':
			return 9;
			
		case 'ざ':
			return 10;
			
		case 'し':
			return 11;
			
		case 'じ':
			return 12;
			
		case 'す':
			return 13;
			
		case 'ず':
			return 14;
			
		case 'せ':
			return 15;
			
		case 'ぜ':
			return 16;
			
		case 'そ':
			return 17;
			
		case 'ぞ':
			return 18;
			
			default:
				return -1;
		}
	}
	
	public static int get_second_index(char second_char) {
		switch(second_char) {
		case 'た':
			return 0;
			
		case 'だ':
			return 1;
			
		case 'ち':
			return 2;
			
		case 'ぢ':
			return 3;
			
		case 'っ':
			return 4;
			
		case 'つ':
			return 5;
			
		case 'づ':
			return 6;
			
		case 'て':
			return 7;
			
		case 'で':
			return 8;
			
		case 'と':
			return 9;
			
		case 'ど':
			return 10;
			
		case 'な':
			return 11;
			
		case 'に':
			return 12;
			
		case 'ぬ':
			return 13;
			
		case 'ね':
			return 14;
			
		case 'の':
			return 15;
			
		case 'は':
			return 16;
			
		case 'ば':
			return 17;
			
		case 'ぱ':
			return 18;
			
		case 'ひ':
			return 19;
			
		case 'び':
			return 20;
			
			default:
				return -1;
		}
	}
	
	public static int get_last_index(char last_char) {
		switch(last_char) {
		case ' ':
			return 0;
			
		case 'ぁ':
			return 1;
			
		case 'あ':
			return 2;
			
		case 'ぃ':
			return 3;
			
		case 'い':
			return 4;
			
		case 'ぅ':
			return 5;
			
		case 'う':
			return 6;
			
		case 'ぇ':
			return 7;
			
		case 'ぉ':
			return 8;
			
		case 'お':
			return 9;
			
		case 'か':
			return 10;
			
		case 'が':
			return 11;
			
		case 'き':
			return 12;
			
		case 'ぎ':
			return 13;
			
		case 'く':
			return 14;
			
		case 'ぐ':
			return 15;
			
		case 'け':
			return 16;
			
		case 'げ':
			return 17;
			
		case 'ご':
			return 18;
			
		case 'さ':
			return 19;
			
		case 'ざ':
			return 20;
			
		case 'し':
			return 21;
			
		case 'じ':
			return 22;
			
		case 'ず':
			return 23;
			
		case 'せ':
			return 24;
			
		case 'ぜ':
			return 25;
			
		case 'そ':
			return 26;
			
		case 'ぞ':
			return 27;
			
			default:
				return 0;
		}
	}
	
	public static boolean is_can_combine_second(char first, char second) {
		switch(first) {
		case 'で':
			switch(second) {
			case 'た':
			case 'だ':
			case 'び':
				return true;
				
				default:
					return false;
			}
			
		case 'ぬ':
			switch(second) {
			case 'っ':
			case 'つ':
			case 'び':
				return true;
				
				default:
					return false;
			}
			
		case 'ぱ':
			return second == 'び';
			
			default:
				return false;
		}
	}
	
	public static boolean is_can_combine_last(char first, char second) {
		switch(first) {
		case 'ぁ':
			return second == 'さ';
			
		case 'い':
			switch(second) {
			case 'じ':
			case 'ぞ':
				return true;
				
				default:
					return false;
			}
			
		case 'ぉ':
			switch(second) {
			case 'ぁ':
			case 'け':
			case 'げ':
			case 'さ':
			case 'ぜ':
			case 'そ':
			case 'ぞ':
				return true;
				
				default:
					return false;
			}
			
		case 'げ':
			return second == 'さ';
			
			default:
				return false;
		}
	}
	
	public static char combine_second(char first, char second) {
		switch(first) {
		case 'で':
			switch(second) {
			case 'た':
				return 'と';
				
			case 'だ':
				return 'ど';
				
			case 'び':
				return 'な';
				
				default:
					return '?';
			}
			
		case 'ぬ':
			switch(second) {
			case 'っ':
				return 'ね';
				
			case 'つ':
				return 'の';
				
			case 'び':
				return 'は';
				
				default:
					return '?';
			}
			
		case 'ぱ':
			return second == 'び' ? 'ひ' : '?';
			
			default:
				return '?';
		}
	}
	
	public static char combine_last(char first, char second) {
		switch(first) {
		case 'ぁ':
			return second == 'さ' ? 'ぃ' : '?';
			
		case 'い':
			switch(second) {
			case 'じ':
				return 'ぅ';
				
			case 'ぞ':
				return 'う';
				
				default:
					return '?';
			}
			
		case 'ぉ':
			switch(second) {
			case 'ぁ':
				return 'お';
				
			case 'け':
				return 'か';
				
			case 'げ':
				return 'が';
				
			case 'さ':
				return 'き';
				
			case 'ぜ':
				return 'ぎ';
				
			case 'そ':
				return 'く';
				
			case 'ぞ':
				return 'ぐ';
				
				default:
					return '?';
			}
			
		case 'げ':
			return second == 'さ' ? 'ご' : '?';
			
			default:
				return '?';
		}
	}
	
	public static boolean is_can_uncombine_second(char second_char) {
		switch(second_char) {
		case 'と':
		case 'ど':
		case 'な':
			
		case 'ね':
		case 'の':
		case 'は':
			
		case 'ひ':
			return true;
			
			default:
				return false;
		}
	}
	
	public static boolean is_can_uncombine_last(char last_char) {
		switch(last_char) {
		case 'ぃ':
			
		case 'ぅ':
		case 'う':
			
		case 'お':
		case 'か':
		case 'が':
		case 'き':
		case 'ぎ':
		case 'く':
		case 'ぐ':
			
		case 'ご':
			return true;
			
			default:
				return false;
		}
	}
	
	public static char uncombine_second(char second_char) {
		switch(second_char) {
		case 'と':
		case 'ど':
		case 'な':
			return 'で';
			
		case 'ね':
		case 'の':
		case 'は':
			return 'ぬ';
			
		case 'ひ':
			return 'ぱ';
			
			default:
				return '?';
		}
	}
	
	public static char uncombine_last(char last_char) {
		switch(last_char) {
		case 'ぃ':
			return 'ぁ';
			
		case 'ぅ':
		case 'う':
			return 'い';
			
		case 'お':
		case 'か':
		case 'が':
		case 'き':
		case 'ぎ':
		case 'く':
		case 'ぐ':
			return 'ぉ';
			
		case 'ご':
			return 'げ';
			
			default:
				return '?';
		}
	}
	
	public static char uncombined_last_second(char last_char) {
		switch(last_char) {
		case 'ぃ':
			return 'さ';
			
		case 'ぅ':
			return 'じ';
			
		case 'う':
			return 'ぞ';
			
		case 'お':
			return 'ぁ';
			
		case 'か':
			return 'け';
			
		case 'が':
			return 'げ';
			
		case 'き':
			return 'さ';
			
		case 'ぎ':
			return 'ぜ';
			
		case 'く':
			return 'そ';
			
		case 'ぐ':
			return 'ぞ';
			
		case 'ご':
			return 'さ';
			
			default:
				return '?';
		}
	}
	
	public static String make_kor(char first_char, char second_char, char last_char) {
		return new String(Character.toChars(0xAC00 + (KORInput.get_first_index(first_char) * 21 + KORInput.get_second_index(second_char)) * 28 + KORInput.get_last_index(last_char)));
	}
}