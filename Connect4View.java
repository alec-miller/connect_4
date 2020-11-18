import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Connect4View extends Application implements Observer {
	
	private Connect4Model model;
	private Connect4Controller controller;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Connect 4");
		GridPane pane = new GridPane();
		StackPane background = new StackPane();
		Canvas canvas = new Canvas(344, 296);
		background.getChildren().add(canvas);
		pane.getChildren().add(background);
		background.setStyle("-fx-background-color: blue");
		pane.setHgap(8);
		pane.setVgap(8);
		setCircles(pane);
		Scene scene = new Scene(pane, 344, 296);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void setCircles(GridPane pane) {
		for(int i = 1; i < 7; i++) {
			for(int x = 1; x < 8; x++) {
				Circle circle = new Circle(20);
				circle.setFill(javafx.scene.paint.Color.WHITE);
				circle.setCenterX(x * 100);
				circle.setCenterY(i * 100);
				//pane.addRow(0, circle);
				pane.getChildren().add(circle);
			}
		}
	}

	public void giveModel(Connect4Model model) {
		this.model = model;
	}
	
	public void giveController(Connect4Controller controller) {
		this.controller = controller;
	}
}
