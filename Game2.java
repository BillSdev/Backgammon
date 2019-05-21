import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Game2 {
	private Main2 main;
	public static final int WHITE_CHECKER = 0;
	public static final int BLACK_CHECKER = 1;
	public static final int BOARD_WIDTH = (int)((double)Main2.WIDTH * ((double)16/18));
	public static final int BOARD_HEIGHT = (int)((double)Main2.HEIGHT * 0.94);
	private boolean midAnimation;
	private int xOffSet;
	private int yOffSet;
	private ArrayList<Integer> moveList;
	private int[] checkersToInsert;   //first element is whites to insert second element is black to insert. access through the constants.
	private Lane[] lanes;
	private Lane selectedLane;
	private Lane tempSelectedLane;     //lazy fix to some problems.
	private int playingChecker;
	private BackButton backButton;
	private ConfirmButton confirmButton;
	private BearOffButton boffButton;
	private NewGameButton newGameButton;
	private ExitButton exitButton;
	private int checkerMoveTime;
	private int[] checkersToBearOff;
	private boolean playing;
	private boolean couldntMove;
	//private int[] numbersRolled;
	//private boolean rollingDice;
	private Stack<int[]> backMoves;    //first number is number of from-lane , second num to-lane, third if a piece was captured in from lane (0 - no , 1 - yes, -1 = bear off info). 
	private final int BACK_FROM = 0;   //number of the lane we go back from.
	private final int BACK_TO = 1;     //number of lane we come back to
	private final int BACK_INFO = 2;  //additional info.  0 - nothing special , 1 - a piece was captures on from-lane. 2 - a bear off was done.
	private int[][] setup = {{2,0}, {0,0}, {0,0}, {0,0}, {0,0}, {5,1}, {0,0}, {3,1}, {0,0}, {0,0}, {0,0}, {5,0}, 
			{5,1}, {0,0}, {0,0}, {0,0}, {3,0}, {0,0}, {5,0}, {0,0}, {0,0}, {0,0}, {0,0}, {2,1}};

	private int[][] setup2 = {{3,1}, {2,1}, {5,1}, {2,1}, {0,0}, {3,1}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0},    //all blacks are home ->
			{0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {3,0}, {1,0}, {4,0}, {5,0}, {2,0}};            //bearing off testing.

	private int[][] setup1 =  {{2,0}, {0,0}, {1,0}, {0,0}, {0,0}, {5,1}, {0,0}, {3,1}, {0,0}, {0,0}, {0,0}, {5,0}, 
			{5,1}, {0,0}, {0,0}, {0,0}, {3,0}, {0,0}, {5,0}, {0,0}, {0,0}, {0,0}, {0,0}, {2,1}};

	private ArrayList<Animation> animationList;

	public Game2(Main2 main) {
		this.main = main;
		lanes = new Lane[24];
		selectedLane = null;
		checkerMoveTime = 300;
		xOffSet = yOffSet = (Main2.HEIGHT - BOARD_HEIGHT) / 2;
		moveList = new ArrayList<Integer>();
		backMoves = new Stack<int[]>();
		animationList = new ArrayList<Animation>();
		//checkersToInsert = new int[2];
		checkersToInsert = new int[] {0, 0};
		checkersToBearOff = new int[]{15, 15};    
		playing = true;
		backButton = new BackButton(Main2.WIDTH-(int)(xOffSet*0.5)-30, yOffSet + BOARD_HEIGHT/2-15, 30, 30, this);
		confirmButton = new ConfirmButton((int)(xOffSet*1.5) + BOARD_WIDTH, yOffSet + BOARD_HEIGHT/2-16, 30, 30, this);
		boffButton = new BearOffButton((int)(xOffSet + BOARD_WIDTH + Main2.WIDTH)/2-20, yOffSet + (int)(BOARD_HEIGHT*0.4) + 6, 40, 40, this);
		newGameButton = new NewGameButton(xOffSet + 200, yOffSet + 200, 80, 80, this, main);
		exitButton = new ExitButton(xOffSet + 500, yOffSet + 200, 80, 80, this);
		midAnimation = false;
		couldntMove = false;
		int laneWidth = (BOARD_WIDTH - 2*xOffSet - 4)/12;
		int laneHeight = BOARD_HEIGHT/2;
		Color laneColor;
		for(int i = 0 ; i < lanes.length ; i++) {
			laneColor = Color.red;
			if(i % 2 == 1)
				laneColor = new Color(78, 51, 36);
			if(i < 12)
				lanes[i] = new Lane(xOffSet + BOARD_WIDTH - laneWidth*(i+1) - (2*xOffSet+4)*(i / 6), yOffSet, laneWidth, laneHeight, Lane.TOP_LANE, laneColor, i);
			else
				lanes[i] = new Lane(xOffSet + laneWidth*(i-12) + (2*xOffSet+4)*((i-12) / 6), yOffSet + BOARD_HEIGHT, laneWidth, laneHeight, Lane.BOTTOM_LANE, laneColor, i);
			for(int j = 0 ; j < setup[i][0] ; j++) {
				lanes[i].addChecker(setup[i][1]);
			}
		}
		//choosing who starts;
		playingChecker = (int)(Math.random()*2);
		//playingChecker = 1; //force white to start for debugging purposes.
		String chosenChecker = "White";
		if(playingChecker == BLACK_CHECKER)
			chosenChecker = "Black";
		nextRound();
		//		System.out.println(chosenChecker + " starts!");
		//		animationList.add(new PopMessageAnimation(chosenChecker + "'s turn", 550, 330, 550, 330, 2000, new Color(147, 28, 31)));
		//		rollDice();
	}

	public void laneChosen(Lane lane) {
		if(checkersToInsert[playingChecker] != 0) {
			moveChecker(null, lane, true);
		}
		else if(selectedLane == null) {     //selectedLane is null so here we just select the lane from which we want to move the checker.               
			if(!moveList.isEmpty() && !lane.isEmpty() && lane.getSelected() == false && lane.getCheckerType() == playingChecker) {
				selectedLane = lane;
				lane.setSelected(true);
			}
		}
		else if(selectedLane == lane) {
			lane.setSelected(false);
			selectedLane = null;
		}
		else {                          //in this case selectedLane is not null meaning that here we already make the move.
			moveChecker(selectedLane, lane, true);
		}
	}

	public boolean moveChecker(Lane from, Lane to, boolean notTesting) {
		int sign = (-2 * playingChecker) + 1;   // 1 -> -1 , 0 -> 1 . used in next if. blacks can only go backward and whites forward
		int fromGetNumber = playingChecker*25 - 1;  //you bring the whites from -1 and the blacks from 24.
		if(from != null)
			fromGetNumber = from.getNumber();
		if(to.isClosed() && to.getCheckerType() != playingChecker)            
			return false;

		else if(sign * to.getNumber() - sign * fromGetNumber < 0) 
			return false;

		else if(!moveList.contains(Math.abs(to.getNumber() - fromGetNumber))) 
			return false;

		else {
			Point removedC;
			int captured = 0;  
			if(to.isSingleChecker() && to.getCheckerType() != playingChecker) {
				captured = 1;
				removedC = to.getCurrentChecker();
				to.removeChecker();
				if(!notTesting)         
					checkersToInsert[-playingChecker + 1]++;
				else {    
					int xDest = xOffSet+BOARD_WIDTH/2-lanes[0].getCheckerRadius();
					int yDest = yOffSet-lanes[0].getCheckerRadius() + (int)(BOARD_HEIGHT*0.25) + (int)(BOARD_HEIGHT*0.5)*(playingChecker);
					animationList.add(new CheckerAnimation(removedC.x, removedC.y, xDest, yDest, to.getCheckerRadius(), -playingChecker + 1, to, null, checkerMoveTime));
				}
			}
			if(from != null) {
				removedC = from.getCurrentChecker();
				from.removeChecker();
				if(selectedLane != null)
					selectedLane.setSelected(false);
			}
			else {
				int xStart = xOffSet+BOARD_WIDTH/2-lanes[0].getCheckerRadius();
				int yStart = yOffSet-lanes[0].getCheckerRadius() + (int)(BOARD_HEIGHT*0.25) + (int)(BOARD_HEIGHT*0.5)*(-playingChecker+1);
				removedC = new Point(xStart, yStart);
				checkersToInsert[playingChecker]--;
			}
			if(!notTesting)                    //if animation problems remove 3 first above else lines, this if *line*, and whole else below
				to.addChecker(playingChecker);
			else {
				to.addChecker(playingChecker);    //Temporarily just for new checker cords.
				animationList.add(new CheckerAnimation(removedC.x, removedC.y, to.getCurrentChecker().x, to.getCurrentChecker().y, to.getCheckerRadius(), playingChecker, from, to, checkerMoveTime));
				to.removeChecker();
			}
			moveList.remove(new Integer(Math.abs(to.getNumber() - fromGetNumber)));
			selectedLane = null;
			backMoves.add(new int[] {to.getNumber(), fromGetNumber, captured});
			return true;
		}
	}

	public void nextRound() {
		if(!moveList.isEmpty()) { 
			moveList.clear();
		}
		backMoves.clear();
		playingChecker = (-1 * playingChecker) + 1;   // 0 -> 1 ,  1 -> 0 thus switching players.
		if(selectedLane != null) {                    //deselect lanes for new round.
			selectedLane.setSelected(false);
			selectedLane = null;
		}
		String chosenChecker = "white";               //print whos turn it is.
		if(playingChecker == BLACK_CHECKER)
			chosenChecker = "black";
		System.out.println(chosenChecker + " to play");
		animationList.add(new DiceAnimation(xOffSet + (int)(BOARD_WIDTH * 0.65), yOffSet + BOARD_HEIGHT/2 - 15,
				xOffSet + (int)(BOARD_WIDTH * 0.65), yOffSet + BOARD_HEIGHT/2 - 15, 1000, 30));
		postRollAnimation();
	}

	public void postRollAnimation() {                
		rollDice();
		String chosenChecker = "white";               
		if(playingChecker == BLACK_CHECKER)
			chosenChecker = "black";
		if(cantMove(playingChecker)) {
			System.out.println("You cant move, skip turn.");
			couldntMove = true;
		}
		else 
			animationList.add(new PopMessageAnimation(chosenChecker + "'s turn", xOffSet + BOARD_WIDTH/2, Main2.HEIGHT/2-25, 
					xOffSet + BOARD_WIDTH/2, Main2.HEIGHT/2-25, 1500, new Color(147, 28, 31)));
	}

	public void rollDice() {
		int dice1 = (int)(Math.random()*6) + 1;
		int dice2 = (int)(Math.random()*6) + 1;
		//dice1 = 1;
	    //dice2 = 3;
		moveList.add(dice1);
		moveList.add(dice2);
		if(dice1 == dice2) {
			moveList.add(dice1);
			moveList.add(dice1);
		}
		Collections.sort(moveList);
		System.out.println("Rolled : " + dice1 + " and " + dice2);
	}

	public void moveBack() {
		int[] lastMove = backMoves.pop();
		moveList.add(Math.abs(lastMove[BACK_FROM]-lastMove[BACK_TO]));
		if(lastMove[BACK_FROM] >= 0 && lastMove[BACK_FROM] <= 23)
			lanes[lastMove[BACK_FROM]].removeChecker();
		else 
			checkersToBearOff[playingChecker]++;
		if(lastMove[BACK_TO] == -1 || lastMove[BACK_TO] == 24)
			checkersToInsert[playingChecker]++;
		else
			lanes[lastMove[BACK_TO]].addChecker(playingChecker);
		if(lastMove[BACK_INFO] == 1) {
			lanes[lastMove[BACK_FROM]].addChecker(-playingChecker + 1);
			checkersToInsert[-playingChecker + 1]--;
		}
		if(lastMove[BACK_INFO] == 2) {
			//dont think i need to put anything here right now.
		}
		Collections.sort(moveList);
		if(selectedLane != null) 
			selectedLane.setSelected(false);
		selectedLane = null;
	}

	public boolean canBearOff(Lane lane) {
		if(allAtHome(playingChecker)) {
			int flip = -playingChecker + 1;
			int sign = -2*playingChecker + 1;
			int laneHomeNumber = Math.abs(lane.getNumber() % 6 - 5*flip) + 1;
			if(moveList.contains(new Integer(laneHomeNumber)))
				return true;
			for(int i = lane.getNumber() - sign ; -sign*i < 6-23*flip ; i=i-sign) {
				if(!lanes[i].isEmpty() && lanes[i].getCheckerType() == playingChecker)
					return false;
			}
			for(int i = 0 ; i < moveList.size(); i++) {
				if(moveList.get(i) > laneHomeNumber)
					return true;
			}
			return false;
		}
		return false;
	}

	public boolean allAtHome(int pChecker) {
		int sign = 2 * pChecker - 1;
		boolean allAtHome = true;
		for(int i = 6 + 11*(-pChecker+1) ; sign*i < pChecker*23+1 ; i=i+sign) {
			if(lanes[i].getCheckerType() == pChecker && !lanes[i].isEmpty())
				allAtHome = false;
		}
		return allAtHome;
	}

	public boolean cantMove(int pChecker) {
		int sign = -2*pChecker + 1;
		boolean result = true;
		Collections.sort(moveList);
		if(checkersToInsert[pChecker] > 0) {
			for(int i = 0 ; i < moveList.size() ; i++) {
				if(canMoveFromTo(null, lanes[25*pChecker - 1 + moveList.get(i)*sign])) {
					result = false;
					lanes[25*pChecker - 1 + moveList.get(i)*sign].setHighlight(true);
				}
			}
			return result;
		}
		for(int i = 0 ; i < lanes.length ; i++) {
			if(!lanes[i].isEmpty() && lanes[i].getCheckerType() == pChecker) {
				if(canBearOff(lanes[i]))
					result = false;
				for(int j = 0 ; j < moveList.size() ; j++) {   
					if(i + moveList.get(j)*sign < 24 && i + moveList.get(j)*sign > -1) {
						if(canMoveFromTo(lanes[i], lanes[i + moveList.get(j)*sign])) {
							result = false;
							if(tempSelectedLane == lanes[i]) {
								//System.out.println("i + moveList.get(j)*sign = " + (i + moveList.get(j)*sign) + " i = " + i + " moveList.get(j)*sign = " + (moveList.get(j)*sign));
								lanes[i + moveList.get(j)*sign].setHighlight(true);

							}
						}
					}
				}
			}
		}
		return result;
	}
	//returns if the *playing* checker can move from one lane to another.
	//right now the function is only called when the from lane for sure is of the playing checker type.
	public boolean canMoveFromTo(Lane from, Lane to) {    
		boolean b = moveChecker(from, to, false);         
		if(b) {
			moveBack();
			return true;
		}
		Collections.sort(moveList);
		return false;
	}

	public void bearOff() {
		if(selectedLane == null)    //just in case even though it cant happen.
			return;
		int flip = -playingChecker + 1;
		int sign = -2*playingChecker + 1;
		int laneHomeNumber = Math.abs(selectedLane.getNumber() % 6 - 5*flip) + 1;
		int move;
		//checkersToBearOff[playingChecker]--;
		/////////////////////////////**animation section. delete and replace with the above //ed line to reverse back to pre-animation state.
		int xStart = selectedLane.getCurrentChecker().x;
		int yStart = selectedLane.getCurrentChecker().y;
		int xDest = 2*xOffSet + BOARD_WIDTH;
		int yDest = (int)(yOffSet + (BOARD_HEIGHT * (0.4 + 0.2*(-playingChecker+1))) + sign*(((15 - checkersToBearOff[playingChecker])+1)*((BOARD_HEIGHT*0.4) / 15)));
		CheckerAnimation cAnimation = new CheckerAnimation(xStart, yStart, xDest, yDest, selectedLane.getCheckerRadius(), playingChecker, null, null, checkerMoveTime);
		cAnimation.setDrawMethod(lanes[0]);
		animationList.add(cAnimation);
		///////////////////////////////////////////////////////////////////////////////////////////
		if(moveList.contains(new Integer(laneHomeNumber))) 
			move = laneHomeNumber;
		else 
			move = HelperMethods.maxIntArrayList(moveList);
		moveList.remove(new Integer(move));
		backMoves.add(new int[]{selectedLane.getNumber() + sign*move, selectedLane.getNumber(), 2});
		selectedLane.removeChecker();
		selectedLane.setSelected(false);
		selectedLane = null;

	}

	public Lane[] getLanes() {
		return lanes;
	}
	public int getPlayingChecker() {
		return playingChecker;
	}

	public void draw(Graphics2D g) {
		//Drawing main parts of the board , the lanes and the checkers.
		g.setColor(new Color(132, 65, 20));
		g.fillRect(0, 0, Main2.WIDTH, Main2.HEIGHT);                               //back board.
		g.setColor(new Color(255, 221, 183));
		g.fillRect(xOffSet, yOffSet, BOARD_WIDTH, BOARD_HEIGHT);                   //playing board.
		g.setColor(new Color(132, 65, 20));		            
		g.fillRect(BOARD_WIDTH/2-2, yOffSet, 2*xOffSet+4, BOARD_HEIGHT);
		g.setColor(new Color(92, 25, 10));
		g.fillRect(xOffSet + BOARD_WIDTH/2-2, 0, 4, Main2.HEIGHT);
		for(int i = 0 ; i < lanes.length ; i++) {
			lanes[i].draw(g);
		}

		//drawing dice
		Collections.sort(moveList);
		for(int i = 0 ; i < moveList.size() ; i++) {
			if(!isRolling())
				DiceAnimation.drawDice(xOffSet + (int)(BOARD_WIDTH * 0.65) + i*(30+30), yOffSet + BOARD_HEIGHT/2 - 15, 30, moveList.get(i), g);
		}

		//draw checkers awaiting their entrance. draw 1 each + string indicating how many need to enter.
		int xTemp = xOffSet+BOARD_WIDTH/2-lanes[0].getCheckerRadius();
		int yTemp = yOffSet-lanes[0].getCheckerRadius();
		if(checkersToInsert[BLACK_CHECKER] > 0) {
			lanes[0].drawChecker(xTemp, yTemp + (int)(BOARD_HEIGHT*0.25), lanes[0].getCheckerRadius(), BLACK_CHECKER, g);   //just need the drawchecker() method doesnt matter which lane is used.
			if(checkersToInsert[BLACK_CHECKER] > 1) {
				g.setColor(Color.white);
				g.drawString(""+checkersToInsert[BLACK_CHECKER], xTemp + lanes[0].getCheckerRadius() - 5, yTemp + lanes[0].getCheckerRadius() + (int)(BOARD_HEIGHT*0.25) + 5);
			}
		}
		if(checkersToInsert[WHITE_CHECKER] > 0) {
			lanes[0].drawChecker(xTemp , yTemp + (int)(BOARD_HEIGHT*0.75), lanes[0].getCheckerRadius(), WHITE_CHECKER, g);
			if(checkersToInsert[WHITE_CHECKER] > 1) {
				g.setColor(Color.black);
				g.drawString(""+checkersToInsert[WHITE_CHECKER], xTemp + lanes[0].getCheckerRadius() - 5, yTemp + lanes[0].getCheckerRadius() + (int)(BOARD_HEIGHT*0.75) + 5);
			}
		}

		//here we draw the bear-off section.
		g.setColor(new Color(255, 221, 183));
		g.fillRect(2*xOffSet + BOARD_WIDTH, yOffSet, Main2.WIDTH - BOARD_WIDTH - 3*xOffSet, (int)(BOARD_HEIGHT*0.4));
		g.fillRect(2*xOffSet + BOARD_WIDTH, yOffSet + (int)(BOARD_HEIGHT*0.6), Main2.WIDTH - BOARD_WIDTH - 3*xOffSet, (int)(BOARD_HEIGHT*0.4));
		int bocHeight = (int)(BOARD_HEIGHT*0.4) / 15;
		int bocWidth = Main2.WIDTH - BOARD_WIDTH - 3*xOffSet;
		for(int i = 0 ; i < 15 - checkersToBearOff[BLACK_CHECKER] ; i++) {
			drawBearOffChecker(2*xOffSet + BOARD_WIDTH, yOffSet + (int)(BOARD_HEIGHT * 0.4) - (i+1)*bocHeight, bocWidth, bocHeight, g, BLACK_CHECKER);
		}
		for(int i = 0 ; i < 15 - checkersToBearOff[WHITE_CHECKER] ; i++) {
			drawBearOffChecker(2*xOffSet + BOARD_WIDTH, yOffSet + (int)(BOARD_HEIGHT * 0.6) + i*bocHeight, bocWidth, bocHeight, g, WHITE_CHECKER);
		}

		for(int i = 0 ; i < animationList.size() ; i++) {
			animationList.get(i).draw(g);
		}

		//drawing the buttons (drawing when active is handled in the button class).
		confirmButton.draw(g);
		backButton.draw(g);
		boffButton.draw(g);
		exitButton.draw(g);
		newGameButton.draw(g);
	}

	public void drawBearOffChecker(int x, int y, int width, int height, Graphics2D g, int checkerType) {
		Color color = new Color(150 * (-checkerType + 1), 150 * (-checkerType + 1), 150 * (-checkerType + 1));
		g.setColor(color);
		//g.drawLine(x, y + height*(-checkerType+1)-1, x+width, y + height*(-checkerType+1)-1);
		g.fillRect(x, y, width, height);
		color = new Color(215 * (-checkerType + 1), 215 * (-checkerType + 1), 215 * (-checkerType + 1));
		g.setColor(color);
		for(int i = 0 ; i < 10 ; i++) {
			g.fillRect(x + i*(int)(0.025*width), y, (int)(width - width * 0.05*i), height-1);
			g.setColor(new Color(color.getRed() + 4, color.getGreen() + 4, color.getBlue() + 4));
			color = g.getColor();
		}
	}


	public void update() {
		if(!playing) {
			newGameButton.activate();
			exitButton.activate();
			return;
		}
		//if we are here then the game is on.

		confirmButton.deactivate();
		backButton.deactivate();
		boffButton.deactivate();
		//animation stuff
		midAnimation = false;
		if(!animationList.isEmpty())
			midAnimation = true;
		for(int i = 0 ; i < animationList.size() ; i++) {                          //new addition
			animationList.get(i).update();
			if(animationList.get(i).isFinished()) {
				if(animationList.get(i) instanceof CheckerAnimation) {
					CheckerAnimation currAn = (CheckerAnimation)animationList.get(i);
					if(currAn.getTo() == null && currAn.getFrom() == null) {
						checkersToBearOff[playingChecker]--;
						System.out.println("player : " + playingChecker + " finished bearing off his checker");
					}
					else if(currAn.getTo() == null) { 
						checkersToInsert[-playingChecker + 1]++;
						System.out.println("player :  " + (-playingChecker + 1) + " got eaten");
					}
					else if(currAn.getFrom() == null) { 
						currAn.getTo().addChecker(playingChecker);
						System.out.println("player : " + playingChecker + " entered his checker");
					}
					else { 
						currAn.getTo().addChecker(playingChecker);
						System.out.println("player : " + playingChecker + " added his checker to a lane");
					}
				}
				else if(animationList.get(i) instanceof DiceAnimation && couldntMove) { 
					animationList.add(new PopMessageAnimation("Cant move", xOffSet + BOARD_WIDTH/2, Main2.HEIGHT/2-25, 
							xOffSet + BOARD_WIDTH/2, Main2.HEIGHT/2-25, 1500, Color.red));
				}

				animationList.remove(animationList.get(i));
				i--;      //maybe use an iterator to avoid unexpected behaviors after deleting elements mid loop.
			}
		}

		if(animationList.isEmpty() && couldntMove) {
			couldntMove = false;
			nextRound();
			return;
		}

		Collections.sort(moveList);
		deHighlightLanes();
		tempSelectedLane = selectedLane;        				    //fix cantMove-test bug nulling the selected lane every run.
		if(!midAnimation && (moveList.size() == 0 || cantMove(playingChecker))) {   //ending the round by using cantMove() here is extremely inefficient 
			confirmButton.activate();					    //but easy and doesnt mess this already messed up code as much.
		}									    
		selectedLane = tempSelectedLane;
		if(selectedLane != null)
			selectedLane.setSelected(true);
		if(!backMoves.isEmpty()) {
			backButton.activate();
		}
		if(selectedLane != null && canBearOff(selectedLane)) {
			boffButton.activate();
		}
		if(checkersToBearOff[0]*checkersToBearOff[1] == 0) {
			System.out.println("Game is OVER");
			String winner = "White";
			if(playingChecker == BLACK_CHECKER)
				winner = "Black";
			System.out.println("The winner is : " + winner);
			playing = false;
		}

	}

	public void deHighlightLanes() {
		for(int i = 0 ; i < lanes.length ; i++) {
			lanes[i].setHighlight(false);
		}
	}

	public boolean isRolling() {
		for(int i = 0 ; i < animationList.size() ; i++) {
			if(animationList.get(i) instanceof DiceAnimation)
				return true;
		}
		return false;
	}

	public void mousePressed(MouseEvent e) {
		if(playing && !midAnimation) {
			if(confirmButton.isInBounds(e) && !confirmButton.getIsPressed())
				confirmButton.setIsPressed(true);
			else if(backButton.isInBounds(e) && !backButton.getIsPressed()) {
				backButton.setIsPressed(true);
				System.out.println("pressed on the back button");
			}
			else if(boffButton.isInBounds(e) && !boffButton.getIsPressed())
				boffButton.setIsPressed(true);
		}
		if(newGameButton.isInBounds(e) && !newGameButton.getIsPressed()) 
			newGameButton.setIsPressed(true);
		else if(exitButton.isInBounds(e) && !exitButton.getIsPressed()) 
			exitButton.setIsPressed(true);
	}

	public void mouseReleased(MouseEvent e) {     //setting isPressed to false in action(){}
		if(playing && !midAnimation) {
			if(confirmButton.isInBounds(e) && confirmButton.getIsPressed())
				confirmButton.action();
			else if(backButton.isInBounds(e) && backButton.getIsPressed()) {
				System.out.println("Released back");
				backButton.action();
			}
			else if(boffButton.isInBounds(e) && boffButton.getIsPressed())
				boffButton.action();
			confirmButton.setIsPressed(false);
			backButton.setIsPressed(false);
			boffButton.setIsPressed(false);
		}
		if(newGameButton.isInBounds(e) && newGameButton.getIsPressed()) 
			newGameButton.action();
		else if(exitButton.isInBounds(e) && exitButton.getIsPressed()) 
			exitButton.action();
		newGameButton.setIsPressed(false);
		exitButton.setIsPressed(false);
	}


	public void mouseClicked(MouseEvent e) {
		if(playing && !midAnimation) {
			for(int i = 0 ; i < lanes.length ; i++) {
				int sign = ((-i/(lanes.length/2)) * 2) + 1;
				if((e.getX() >= lanes[i].getX() && sign * e.getY() >= sign * lanes[i].getY()) &&
						(e.getX() <= lanes[i].getX() + lanes[i].getWidth() && sign * e.getY() <= sign * lanes[i].getY() + lanes[i].getHeight())) {
					laneChosen(lanes[i]);
				}
			}
		}
	}
}
