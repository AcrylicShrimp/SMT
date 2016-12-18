package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	public Image front_buffer;
	
	public GamePanel(int width, int height) {
		super();
		super.setSize(width, height);
		super.setPreferredSize(new Dimension(width, height));
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(this.front_buffer, 0, 0, null);
	}

	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		this.paint(g);
	}
}