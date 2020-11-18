import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.stage.Stage;

public class Connect4View extends Application implements Observer {
	
	private Connect4Model model;
	
	public Connect4View(Connect4Model model) {
		this.model = model;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
