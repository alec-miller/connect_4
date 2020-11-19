import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Connect4View extends Application implements Observer {
	
	private Connect4Model model = new Connect4Model();
	private Connect4Controller controller = new Connect4Controller(model);
	private static String[] arguments;
	private static ArrayList<ArrayList<Circle>> circles = new ArrayList<ArrayList<Circle>>();
	
	final static int NUM_ROWS = 6;
	final static int NUM_COLS = 7;
	final static int CIRCLE_SIZE = 20;
	final int PANE_WIDTH = 344;
	final int PANE_HEIGHT = 296;

	public static void main(String[] args) {
		setupInitialCircles();
		launch(args);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		if(((Connect4MoveMessage) arg).getColor() == ((Connect4MoveMessage) arg).RED) {
			circles.get(NUM_ROWS - 1 - ((Connect4MoveMessage) arg).getRow()).get(((Connect4MoveMessage) arg).getColumn()).setFill(javafx.scene.paint.Color.RED);
		} else {
			circles.get(NUM_ROWS - 1 -((Connect4MoveMessage) arg).getRow()).get(((Connect4MoveMessage) arg).getColumn()).setFill(javafx.scene.paint.Color.YELLOW);
		}
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		model.addObserver(this);  // CHANGE THIS
		primaryStage.setTitle("Connect 4");
		GridPane pane = new GridPane();
		StackPane background = new StackPane();
		Canvas canvas = new Canvas(PANE_WIDTH, PANE_HEIGHT);
		Insets insets = new Insets(8, 8, 8, 8);
		background.getChildren().add(canvas);
		//background.setPadding(insets);
		pane.getChildren().add(background);
		//background.setStyle("-fx-background-color: blue;" + "-fx-border-insets: 20;");
		pane.setHgap(8);
		pane.setVgap(8);
		pane.setPadding(insets);
		pane.setStyle("-fx-background-color: blue;" + "-fx-border-insets: 8;");
		pane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				controller.makeHumanMove(me, PANE_WIDTH, NUM_COLS);
			}
		});
		setCircles(pane);
		Scene scene = new Scene(pane, 360, 312);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void setCircles(GridPane pane) {
		for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / NUM_COLS);
            pane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < NUM_ROWS; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / NUM_ROWS);
            pane.getRowConstraints().add(rowConst);         
        }
		for(int y = 0; y < NUM_ROWS; y++) {
			for(int x = 0; x < NUM_COLS; x++) {
				pane.add(circles.get(y).get(x), x, y);
				
				//circle.setCenterX(x * 20);
				//circle.setCenterY(i * 20);
				//pane.addRow(0, circle);
				//pane.getChildren().add(circle);
			}
		}
	}

	public void giveModel(Connect4Model model) {
		this.model = model;
		this.model.addObserver(this);
	}
	
	public void giveController(Connect4Controller controller) {
		System.out.println("this running");
		System.out.println(this.controller);
		this.controller = controller;
		System.out.println(this.controller);
	}
	
	public static void setupInitialCircles() {
		for(int i = 0; i < NUM_ROWS; i++) {
			ArrayList<Circle> circlesRow = new ArrayList<Circle>();
			for(int j = 0; j < NUM_COLS; j++) {
				Circle circle = new Circle(CIRCLE_SIZE);
				circle.setFill(javafx.scene.paint.Color.WHITE);
				circlesRow.add(circle);
			}
			circles.add(circlesRow);
		}
	}

}
