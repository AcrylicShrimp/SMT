package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

@SuppressWarnings("serial")
public class GameFrame extends JFrame implements KeyListener, MouseInputListener {
	
	public GamePanel game_panel;
	
	public GameFrame(int width, int height, String title) {
		super.setTitle(title);
		super.setResizable(false);
		
		this.game_panel = new GamePanel(width, height);
		
		super.getContentPane().add(this.game_panel);
		super.pack();
		
		//화면 정중앙에 오도록 설정
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		super.setLocation((screen_size.width - width) / 2, (screen_size.height - height) / 2);
		
		//이벤트 등록
		this.addKeyListener(this);
		this.game_panel.addMouseListener(this);
		this.game_panel.addMouseMotionListener(this);
		
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setVisible(true);
	}

	@Override
	public void update(Graphics g) {
		// TODO Auto-generated method stub
		this.paint(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int nKeyCode = e.getKeyCode();
		
		if(nKeyCode < 256)
			Game.game_input.key_state[nKeyCode] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int nKeyCode = e.getKeyCode();
		
		if(nKeyCode < 256)
			Game.game_input.key_state[nKeyCode] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//Ignore.
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//Ignore.
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		Game.game_input.mouse_inbound = true;
		Game.game_input.mouse_x = e.getX() - Game.GAME_FRAME_WIDTH / 2;
		Game.game_input.mouse_y = e.getY() - Game.GAME_FRAME_HEIGHT / 2;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		Game.game_input.mouse_inbound = false;
		Game.game_input.mouse_x = e.getX() - Game.GAME_FRAME_WIDTH / 2;
		Game.game_input.mouse_y = e.getY() - Game.GAME_FRAME_HEIGHT / 2;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		Game.game_input.mouse_state[e.getButton() - 1] = true;
		Game.game_input.mouse_x = e.getX() - Game.GAME_FRAME_WIDTH / 2;
		Game.game_input.mouse_y = e.getY() - Game.GAME_FRAME_HEIGHT / 2;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		Game.game_input.mouse_state[e.getButton() - 1] = false;
		Game.game_input.mouse_x = e.getX() - Game.GAME_FRAME_WIDTH / 2;
		Game.game_input.mouse_y = e.getY() - Game.GAME_FRAME_HEIGHT / 2;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//Ignored.
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		Game.game_input.mouse_x = e.getX() - Game.GAME_FRAME_WIDTH / 2;
		Game.game_input.mouse_y = e.getY() - Game.GAME_FRAME_HEIGHT / 2;
	}
}