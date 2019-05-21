import java.awt.Color;
import java.awt.Graphics2D;

public class ConfirmButton extends Button{

	public ConfirmButton(int x, int y, int width, int height, Game2 game) {
		super(x, y, width, height, game);
	}

	public void action() {
		if(active) {
			game.nextRound();
		}
	}

	public void draw(Graphics2D g) {
		if(active) {
			Color color = g.getColor();
			x = xFixed + 2;
			y = yFixed + 2;
			if(!isPressed) {
				x = xFixed;
				y = yFixed;
				g.setColor(Color.black);
				g.fillRect(x, y, width+2, height+2);
			}
			g.setColor(Color.white);
			g.fillRect(x, y, width, height);
			g.setColor(Color.green);
			int[] xPoints = {x + (int)(0.23 * width), x + (int)(0.15 * width), x + (int)(0.45 * width),  
					x + (int)(0.75 * width), x + (int)(0.6 * width), x + (int)(0.45 * width)};
			int[] yPoints = {y + (int)(0.5 * height), y + (int)(0.65 * height), y + (int)(0.8 * height),  
					y + (int)(0.22 * height), y + (int)(0.17 * height), y + (int)(0.65 * height)};
			g.fillPolygon(xPoints, yPoints, 6);
			g.setColor(color);
		}
	}

}
