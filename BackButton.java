import java.awt.Color;
import java.awt.Graphics2D;

public class BackButton extends Button {

	public BackButton(int x, int y, int width, int height, Game2 game) {
		super(x, y, width, height, game);
	}

	public void action() {
		if(active) {
			game.moveBack();
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
			g.setColor(Color.yellow);
			g.fillRect(x, y, width, height);
			g.setColor(Color.green);
			g.fillRect(x + (int)(0.3*width), y + (int)(0.45*height), width/2, (int)(0.1*height));
			int[] xPoints = {x + (int)(0.15*width), x + (int)(0.3*width), x + (int)(0.3*width)};
			int[] yPoints = {y + height/2, y + (int)(height * 0.25) , y + (int)(height * 0.75)};
			g.fillPolygon(xPoints, yPoints, 3);
			g.setColor(color);
		}
	}

}
