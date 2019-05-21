import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class DiceAnimation extends Animation {
	private int dice1;
	private int dice2;
	int diceSize;
	int circleSize;
	public static Point[] circleCords;
	public static int[][] numberToCircles;

	public DiceAnimation(int x, int y, int xDest, int yDest, int ms, int size) {
		super(x, y, xDest, yDest, ms);
		dice1 = (int)(Math.random()*6) + 1;
		dice2 = (int)(Math.random()*6) + 1;
		circleCords = new Point[9];
		diceSize = size;
		circleSize = (int)((double)diceSize*0.2);
		for(int i = 0 ; i < circleCords.length; i++) {
			circleCords[i] = new Point((int)((double)diceSize*0.167 + (i%3)*0.25*diceSize), (int)((double)diceSize*0.167 + (i/3)*0.25*diceSize));
		}
		numberToCircles = new int[][]{{2, 3, 4, 5, 6}, {}, {4, 5, 6}, {6}, {1, 3, 5}, {6}, {4, 5, 6}, {}, {2, 3, 4, 5, 6}};
	}

	public void update() {
		super.update();
		dice1 = (dice1 + (int)(Math.random()*5) + 1)%6;
		if(dice1 == 0)
			dice1 = 6;
		dice2 = (dice2 + (int)(Math.random()*5) + 1)%6;
		if(dice2 == 0)
			dice2 = 6;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect((int)x-1, (int)y-1, diceSize+2, diceSize+2);
		g.fillRect((int)x + (diceSize + 30) - 1, (int)y - 1, diceSize+2, diceSize+2);
		g.setColor(Color.white);
		g.fillRect((int)x, (int)y, diceSize, diceSize);
		g.fillRect((int)x + (diceSize + 30), (int)y, diceSize, diceSize);
		g.setColor(Color.black);
		for(int i = 0 ; i < numberToCircles.length ; i++) {
			for(int j = 0 ; j < numberToCircles[i].length ; j++) {
				if(numberToCircles[i][j] == dice1)
					g.fillOval((int)x + circleCords[i].x, (int)y + circleCords[i].y, circleSize, circleSize);
				if(numberToCircles[i][j] == dice2)
					g.fillOval((int)x + circleCords[i].x + (diceSize + 30), (int)y + circleCords[i].y, circleSize, circleSize);
			}
		}
	}
	
	public static void drawDice(int x, int y, int diceSize, int num, Graphics2D g) {
		if(num < 1 || num > 6)
			return;
		g.setColor(Color.black);
		g.fillRect((int)x-1, (int)y-1, diceSize+2, diceSize+2);
		g.setColor(Color.white);
		g.fillRect((int)x, (int)y, diceSize, diceSize);
		g.setColor(Color.black);
		for(int i = 0 ; i < numberToCircles.length ; i++) {
			for(int j = 0 ; j < numberToCircles[i].length ; j++) {
				if(numberToCircles[i][j] == num)
					g.fillOval((int)x + circleCords[i].x, (int)y + circleCords[i].y, (int)(diceSize*0.2), (int)(diceSize*0.2));
			}
		}
		
	}

}
