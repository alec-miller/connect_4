import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Connect4Controller {
	
	private Connect4Model model;

	public Connect4Controller(Connect4Model model) {
		this.model = model;
	}
	
	public void makeHumanMove(MouseEvent click, int width, int numColumns) {
		if(click.getSceneX() < width / numColumns) {
			System.out.println("0");
			model.add(0);
		} else if(click.getSceneX() < width / numColumns * 2) {
			System.out.println("1");
			model.add(1);
		} else if(click.getSceneX() < width / numColumns * 3) {
			System.out.println("2");
			model.add(2);
		} else if(click.getSceneX() < width / numColumns * 4) {
			System.out.println("3");
			model.add(3);
		} else if(click.getSceneX() < width / numColumns * 5) {
			System.out.println("4");
			model.add(4);
		} else if(click.getSceneX() < width / numColumns * 6) {
			System.out.println("5");
			model.add(5);
		} else if(click.getSceneX() < width / numColumns * 7) {
			System.out.println("6");
			model.add(6);
		}
	}

}
