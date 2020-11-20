import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Connect4View extends Application implements Observer {
	
	private Connect4Model model = new Connect4Model();
	private Connect4Controller controller = new Connect4Controller(model);
	private static String[] arguments;
	private static ArrayList<ArrayList<Circle>> circles = new ArrayList<ArrayList<Circle>>();
	private Button yellowWin = new Button("Yellow Wins!");
	private Button redWin = new Button("Red Wins!");
	private Button tie = new Button("It's a tie!");
	private Alert a = new Alert(AlertType.NONE);
	
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
		BorderPane root = new BorderPane();
		model.addObserver(this);  // CHANGE THIS
		primaryStage.setTitle("Connect 4");
		GridPane pane = new GridPane();
		StackPane background = new StackPane();
		Canvas canvas = new Canvas(PANE_WIDTH, PANE_HEIGHT);
		Insets insets = new Insets(8, 8, 8, 8);
		background.getChildren().add(canvas);
		pane.getChildren().add(background);
		pane.setHgap(8);
		pane.setVgap(8);
		pane.setPadding(insets);
		pane.setStyle("-fx-background-color: blue;" + "-fx-border-insets: 8;");
		pane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				int val = controller.makeHumanMove(me, PANE_WIDTH, NUM_COLS);
				if(val == 1) {
					winPopup(pane, "Yellow");
				}else if(val == 2) {
					winPopup(pane, "Red");
				}else if(val == 3) {
					tiePopup(pane, "It's a tie!");
				}else if(val == 4) {
					invalidPopup(pane, "Column full, pick somewhere else!");
				}
			}			
		});
		setCircles(pane);
//		root.setCenter(pane);
		root.setTop(addMenu());
		Scene scene = new Scene(pane, 360, 312);
//		Scene scene = new Scene(root, PANE_WIDTH, PANE_HEIGHT);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private MenuBar addMenu() {
		Menu menu = new Menu("File");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        VBox root = new VBox(menuBar);

        return menuBar;
	}

	protected void invalidPopup(GridPane pane, String string) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(string);
		alert.show();
	}

	protected void tiePopup(GridPane pane, String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(string);
		alert.setHeaderText("Game Over!");
		alert.show();
		pane.setDisable(true);
	}

	private void winPopup(GridPane pane, String team) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(team + " has won!");
		alert.setHeaderText("Game Over!");
		alert.show();
		pane.setDisable(true);
	}
	
	private void setCircles(GridPane pane) {
		for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            //colConst.setPercentWidth(100.0 / NUM_COLS);
            colConst.setPercentWidth(PANE_WIDTH / NUM_COLS);
            pane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < NUM_ROWS; i++) {
            RowConstraints rowConst = new RowConstraints();
//            rowConst.setPercentHeight(100.0 / NUM_ROWS);
            rowConst.setPercentHeight(PANE_HEIGHT / NUM_ROWS);
            pane.getRowConstraints().add(rowConst);         
        }
		for(int y = 0; y < NUM_ROWS; y++) {
			for(int x = 0; x < NUM_COLS; x++) {
				pane.add(circles.get(y).get(x), x, y);
			}
		}
	}

	public void giveModel(Connect4Model model) {
		this.model = model;
		this.model.addObserver(this);
	}
	
	public void giveController(Connect4Controller controller) {
		this.controller = controller;
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
	
	EventHandler<WindowEvent> message = new EventHandler<WindowEvent>() { 
	public void handle(WindowEvent e) 
	{ 
	   // set alert type 
	   a.setAlertType(AlertType.INFORMATION); 
	
	   // show the dialog 
	   a.show(); 
	} 
	};

}
