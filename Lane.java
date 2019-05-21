import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class Lane {

	public static final int TOP_LANE = 1;
	public static final int BOTTOM_LANE = -1;
	private int number;
	private int checkerRadius;
	private int width;
	private int height;
	private int x;                        //cords of top left corner.
	private int y;
	private int[] checkers = new int[5];  //up to 5 checkers in each lane in order to avoid overlap. 
	//a number bigger than 1 indicates that there is a checker on top of another.
	private int checkerType;
	private int partOfBoard;
	private Color laneColor;                //color of lane
	private boolean selected;
	private int currentChecker;             //follow which checker to select\move
	private boolean isHighlighted;          //if can move to this lane;

	public Lane(int x, int y, int width, int height, int partOfBoard, Color laneColor, int number) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.partOfBoard = partOfBoard;
		this.laneColor = laneColor;
		checkerRadius = Math.min(width/2, ((height-20)/2)/5);
		currentChecker = -1;
		selected = false;
		this.number = number;
	}

	public void setCheckerType(int checkerType) {
		this.checkerType = checkerType;
	}
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void setDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public void setSelected(boolean s) {
		selected = s;
	}
	public void setHighlight(boolean hl) {
		isHighlighted = hl;
	}
	public boolean getSelected() {
		return selected;
	}
	int[] getCheckers() {
		return checkers;
	}
	int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getCheckerType() {
		return checkerType;
	}
	public int getNumber() {
		return number;
	}
	public int getCheckerRadius() {
		return checkerRadius;
	}
	public boolean isEmpty() {
		return (checkers[0] == 0);
	}
	public boolean isSingleChecker() {
		return (!isEmpty() && checkers[1] == 0);
	}
	public boolean isClosed() {
		return checkers[1] != 0;
	}

	public void removeChecker() {
		if(checkers[0] == 0) {
			System.out.println("no checkers to remove");
			return;
		}
		checkers[currentChecker]--;
		currentChecker--;
		if(currentChecker < 0 && checkers[0] != 0)
			currentChecker = checkers.length-1;
	}
	public void addChecker(int checkerType) {
		currentChecker++;
		if(currentChecker > checkers.length-1) {
			currentChecker = 0;
		}
		if(checkers[0] == 0)
			this.checkerType = checkerType;
		checkers[currentChecker]++;

	}

	public void draw(Graphics2D g) {
		//draw the lane
		g.setColor(new Color(56, 235, 54));
		int[] xPoint = {x + 5 - 3, x + width/2, x + width - 5 + 4};
		int[] yPoint = {y - 1*partOfBoard, y + (partOfBoard)*height - (partOfBoard)*45 + 12*partOfBoard, y - 1*partOfBoard};
		if(isHighlighted) 
			g.fillPolygon(xPoint, yPoint, 3);
		g.setColor(laneColor);
		xPoint = new int[]{xPoint[0]+3, xPoint[1], xPoint[2]-4};
		yPoint = new int[]{yPoint[0] + 1*partOfBoard, yPoint[1] - 12*partOfBoard, yPoint[2] + 1*partOfBoard};
		g.fillPolygon(xPoint, yPoint, 3);

		//draw checkers
		Color checkerColor = Color.white;
		boolean drawSelected = false;
		int selectedDrawX = 0;
		int selectedDrawY = 0;
		if(checkerType == Game2.BLACK_CHECKER)
			checkerColor = Color.black;
		g.setColor(checkerColor);
		g.setFont(new Font("Ariel", 20, 30));
		for(int i = 0 ; i < checkers.length ; i++) {
			int drawX = x + width/2 - checkerRadius;
			int drawY = y + (partOfBoard)*checkerRadius*2*i + 2*checkerRadius*((partOfBoard-1)/2);
			if(checkers[i] > 0) {
				if(selected && currentChecker == i) {
					drawSelected = true;
					selectedDrawX = drawX;
					selectedDrawY = drawY;
				}
				else 
					drawChecker(drawX, drawY, checkerRadius, this.checkerType, g);
			}
			if(checkers[i] > 1) {
				g.setColor(Color.gray);
				g.drawString("" + checkers[i], drawX + checkerRadius-10, drawY - partOfBoard*checkerRadius + checkerRadius * 2 * ((partOfBoard + 1)/2) + 10); 
				g.setColor(checkerColor);
			}
		}
		if(drawSelected) 
			drawChecker((int)(selectedDrawX-checkerRadius*0.25),(int)(selectedDrawY-checkerRadius*0.25),(int)(checkerRadius*1.25), this.checkerType,g);
	}

	public Point getCurrentChecker() {
		return new Point(x + width/2 - checkerRadius, y + (partOfBoard)*checkerRadius*2*currentChecker + 2*checkerRadius*((partOfBoard-1)/2));
	}

	public void drawChecker(int x, int y, int radius, int checkerType, Graphics2D g) {
		int increment = (2*checkerType-1) * 15;
		Color color = new Color(255*(-checkerType+1), 255*(-checkerType+1), 255*(-checkerType+1));
		g.setColor(color);
		for(int i = 0 ; i < 15 ; i++ ) {
			g.fillOval((int)(x + radius*i*0.065), (int)(y + radius*i*0.065), (int)(radius*(1-i*0.065))*2, (int)(radius*(1-i*0.065))*2);
			color = new Color(color.getRed() + increment, color.getGreen() + increment, color.getBlue() + increment);
			g.setColor(color);
			if((i+1) % 4 == 0)
				increment = -increment;
		}
	}

}
