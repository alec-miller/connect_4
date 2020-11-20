import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Connect4Controller {
	
	private Connect4Model model;

	public Connect4Controller(Connect4Model model) {
		this.model = model;
	}
	
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

}
