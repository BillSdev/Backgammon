import java.awt.Color;
import java.awt.Graphics2D;

public class BearOffButton extends Button {

	public BearOffButton(int x, int y, int width, int height, Game2 game) {
		super(x, y, width, height, game);
	}
	
	public void action() {
		if(active) {
			game.bearOff();
		}
	}
	
	public void draw(Graphics2D g) {
		if(active) {
//			Color color = g.getColor();
//			g.setColor(Color.gray);
//			g.fillRect(x, y, width, height);
			int pC = game.getPlayingChecker();
			y = yFixed + (-game.getPlayingChecker() + 1) * ((int)(Game2.BOARD_HEIGHT*0.2) - 12 - height);
			int recY = (y + (int)(height*0.46))*pC + (y + height - ((int)(height*0.79)))*(-pC+1);
			game.getLanes()[0].drawChecker(x, y, width/2, pC, g);
			g.setColor(new Color(50 + pC*170, 50 + pC*170, 50 + pC*170));
			if(isPressed)
				g.setColor(Color.green);
			g.fillRect(x + (int)(width*0.4), recY, (int)(width*0.16), (int)(height*0.34));
			int[] xPoints = {x + (int)(width*0.32), x + width/2, x + (int)(width*0.66)};
			int[] yPoints = {y + height*(-pC+1) + (2*pC-1)*(int)(height*0.46), y + height*(-pC+1) + (2*pC-1)*(int)(height*0.12), y + height*(-pC+1) + (2*pC-1)*(int)(height*0.46)};
			g.fillPolygon(xPoints, yPoints, 3);
		}
	}
	
}
