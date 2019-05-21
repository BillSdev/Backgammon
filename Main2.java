import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Main2 extends JPanel implements Runnable, MouseListener {
    
	
	public static final int FPS = 30;
	public static final int WIDTH = 18 * 60;
	public static final int HEIGHT = 11 * 60;
	public static int frameCounter;
	Graphics2D g2;
	BufferedImage background;
	Thread thread;
	Game2 game;
	private boolean inMenu;
	private NewGameButton newGameButton ;
	private ExitButton exitButton;


	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Backgammon");
		frame.setSize(new Dimension(WIDTH + 14, HEIGHT + 37));
		frame.setVisible(true);
		frame.setResizable(false);
		frame.requestFocus();
		frame.setContentPane(new Main2());
	}

	public Main2() {
		this.setSize(new Dimension(WIDTH, HEIGHT));
		background = new BufferedImage(WIDTH, HEIGHT, 1);
		background.setRGB(255, 221, 183);
		g2 = (Graphics2D)background.getGraphics();
		if(thread == null) {
			this.addMouseListener(this);
			thread = new Thread(this);
			thread.start();
		}
		//skip the unfinished menu and start the game directly.
//		inMenu = true;
//		newGameButton = new NewGameButton(WIDTH/2 - 100, (int)(HEIGHT*0.23), 200, 50, null, this);
//		exitButton = new ExitButton(WIDTH/2-100, (int)(HEIGHT*0.57), 200, 50, null);
//		newGameButton.activate();
//		exitButton.activate();
		game = new Game2(this);
	}

	public void run() {
		long lastTime = System.nanoTime();
		frameCounter = 0;
		while(true) {
			if((System.nanoTime() - lastTime) >= 1000000000/FPS) {
				update();
				repaint();
				lastTime = System.nanoTime();
				frameCounter++;
			}
		}
	}

	public void newGame() {
		this.game = new Game2(this);
	}

	public void setInMenu(boolean b) {
		inMenu = b;
	}
	public boolean getInMenu() {
		return inMenu;
	}
	
	public void update() {
		if(inMenu) {
			
		}
		if(game != null)
			game.update();
	}

	public void draw(Graphics2D g) {                
		if(inMenu) {
			drawMenu(g);
		}
		if(game != null)
			game.draw(g);
	}
	
	public void drawMenu(Graphics2D g) {
		newGameButton.draw(g);
		exitButton.draw(g);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
		draw((Graphics2D)g);
		g.setColor(Color.WHITE);
	}


	public void mouseClicked(MouseEvent e) {
		if(game != null)
			game.mouseClicked(e);
	}

	public void mousePressed(MouseEvent e) {
		if(inMenu) {
			if(newGameButton.isInBounds(e) && !newGameButton.getIsPressed())
				newGameButton.setIsPressed(true);
			if(exitButton.isInBounds(e) && !exitButton.getIsPressed())
				exitButton.setIsPressed(true);
		}
		if(game != null)
			game.mousePressed(e);
	}
	public void mouseReleased(MouseEvent e) {
		if(inMenu) {
			if(newGameButton.isInBounds(e) && newGameButton.getIsPressed())
				newGameButton.action();
			if(exitButton.isInBounds(e) && exitButton.getIsPressed()) 
				exitButton.action();
			newGameButton.setIsPressed(false);
			exitButton.setIsPressed(false);
		}
		if(game != null)
			game.mouseReleased(e);
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
