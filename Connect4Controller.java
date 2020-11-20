import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Connect4Controller {
	
	private Connect4Model model;

	public Connect4Controller(Connect4Model model) {
		this.model = model;
	}
	
	/**
	 * Add a token of the correct color to the column the user clicked on.
	 * 
	 * @param click - a MouseEvent of the click that the user just made
	 * @param width - the width of the view of the board
	 * @param numColumns - the number of column (7 in connect 4)
	 * 
	 * @return an int that the view uses to determine next step
	 */
	public int makeHumanMove(MouseEvent click, int width, int numColumns) {
		if(click.getSceneX() < width / numColumns) {
			return model.add(0);
		} else if(click.getSceneX() < width / numColumns * 2) {
			return model.add(1);
		} else if(click.getSceneX() < width / numColumns * 3) {
			return model.add(2);
		} else if(click.getSceneX() < width / numColumns * 4) {
			return model.add(3);
		} else if(click.getSceneX() < width / numColumns * 5) {
			return model.add(4);
		} else if(click.getSceneX() < width / numColumns * 6) {
			return model.add(5);
		} else if(click.getSceneX() < width / numColumns * 7) {
			return model.add(6);
		}
		return -1;
	}
	
	/**
	 * This function makes a random legal move
	 * 
	 */
	public void makeComputerMove() {
		int column = ThreadLocalRandom.current().nextInt(0, 7);
		while(model.add(column) == 4) {
			column = ThreadLocalRandom.current().nextInt(0, 7);
		}
	}

}
