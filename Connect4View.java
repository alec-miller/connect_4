import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * 
 * @author Taylor McLaughlin and Alec Miller
 * This class is the view of this MVC connect 4 game.
 * This creates a board that the user can interact with by clicking on one of the seven columns.  The 
 * next move will then be made by another person running another instance of the program, or a computer.
 * The user can create and play as many games as they want.
 */

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
	
	/**
	 * setup initial white tokens for board and call launch
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		setupInitialCircles();
		launch(args);
	}
	
	/**
	 * Update the view when the model has changed
	 * 
	 * @param o observable, not used in this case
	 * @param arg - a Connect4MoveMessage that contains the color and location of token that has changed
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		if(((Connect4MoveMessage) arg).getColor() == ((Connect4MoveMessage) arg).RED) {
			circles.get(NUM_ROWS - 1 - ((Connect4MoveMessage) arg).getRow()).get(((Connect4MoveMessage) arg).getColumn()).setFill(javafx.scene.paint.Color.RED);
		} else {
			circles.get(NUM_ROWS - 1 -((Connect4MoveMessage) arg).getRow()).get(((Connect4MoveMessage) arg).getColumn()).setFill(javafx.scene.paint.Color.YELLOW);
		}
		
	}
	
	/**
	 * Builds view that the user interacts with.
	 * Creates a menu bar on top, with the board in the center of the screen
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		model.addObserver(this);  // CHANGE THIS
		primaryStage.setTitle("Connect 4");
		Menu file = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(e -> {
			openMenu();
		});
		file.getItems().add(newGame);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(file);
		VBox vb = new VBox(menuBar);
		GridPane pane = new GridPane(); // hold connect 4 board
//		StackPane background = new StackPane();
//		Canvas canvas = new Canvas(PANE_WIDTH, PANE_HEIGHT);
		Insets insets = new Insets(8, 8, 8, 8);
//		background.getChildren().add(canvas);
//		
//		pane.getChildren().add(background);
		pane.setHgap(8);
		pane.setVgap(8);
		pane.setPadding(insets);
		pane.setStyle("-fx-background-color: blue;" + "-fx-border-insets: 8;");
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
		root.setPrefSize(360,312);
		root.setTop(vb);
		root.setCenter(pane);
		Scene scene = new Scene(root, 344, 328);
//		root.setCenter(pane);
//		root.setTop(addMenu());
//		Scene scene = new Scene(pane, 360, 312);
//		Scene scene = new Scene(root, PANE_WIDTH, PANE_HEIGHT);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private MenuBar addMenu() {
		Menu menu = new Menu("File");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        //VBox root = new VBox(menuBar);

        return menuBar;
	}
	
	/**
	 * Comment this whenever you get a chance
	 * 
	 * @param pane
	 * @param string
	 */
	protected void invalidPopup(GridPane pane, String string) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(string);
		alert.show();
	}
	
	/**
	 * Comment this whenever you get a chance
	 * 
	 * @param pane
	 * @param string
	 */
	protected void tiePopup(GridPane pane, String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(string);
		alert.setHeaderText("Game Over!");
		alert.show();
		pane.setDisable(true);
	}
	
	/**
	 * Comment this whenever you get a chance
	 * 
	 * @param pane
	 * @param string
	 */
	private void winPopup(GridPane pane, String team) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(team + " has won!");
		alert.setHeaderText("Game Over!");
		alert.show();
		pane.setDisable(true);
	}
	
	/**
	 * This creates the board based on the local 2d array of circles which is updated by update
	 * 
	 * @param pane - a GridPane that stores the tokens of the board
	 */
	private void setCircles(GridPane pane) {
		for(int y = 0; y < NUM_ROWS; y++) {
			for(int x = 0; x < NUM_COLS; x++) {
				pane.add(circles.get(y).get(x), x, y);
			}
		}
	}
	
	/**
	 * Comment this whenever you get a chance
	 * 
	 * @param model
	 */
	public void giveModel(Connect4Model model) {
		this.model = model;
		this.model.addObserver(this);
	}
	
	/**
	 * Comment this whenever you get a chance
	 * 
	 * @param controller
	 */
	public void giveController(Connect4Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * Builds a 2d array of white circles that represent the board.
	 * Mdifies local variable circles
	 */
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
	
	/**
	 * Create a new Connect4Menu();
	 */
	public void openMenu() {
		Connect4Menu menu = new Connect4Menu();
	}
	
	/**
	 * 
	 * @author Taylor McLaughlin
	 * This class is the creates the screen that the user uses to create a new game.
	 * The screen created allows the user to create another game either with a human or a computer.  The
	 * user then decide if they are the server or the client and create the new game
	 */
	private class Connect4Menu extends Stage {
		public Connect4Menu() {
			//this.initModality(APPLICATION_MODAL);
			Label create = new Label("Create:");
			Label play = new Label("Play as:");
			
			final ToggleGroup createGroup = new ToggleGroup();
			RadioButton rbServer = new RadioButton("Server");
			rbServer.setToggleGroup(createGroup);
			RadioButton rbClient = new RadioButton("Client");
			rbClient.setToggleGroup(createGroup);
			HBox createRow = new HBox(10); 
			createRow.getChildren().addAll(create, rbServer, rbClient);
			
			final ToggleGroup playGroup = new ToggleGroup();
			RadioButton rbHuman = new RadioButton("Human");
			rbHuman.setToggleGroup(playGroup);
			RadioButton rbComputer = new RadioButton("Computer");
			rbComputer.setToggleGroup(playGroup);
			HBox playRow = new HBox(10); 
			playRow.getChildren().addAll(play, rbHuman, rbComputer);
			
			Label server = new Label("Server");
			Label port = new Label("Port");
			TextField serverTextField = new TextField();
			TextField portTextField = new TextField();
			HBox serverRow = new HBox(10); 
			serverRow.getChildren().addAll(server, serverTextField, port, portTextField);
			
			Button ok = new Button("OK");
			Button cancel = new Button("Cancel");
			HBox buttonRow = new HBox(10); 
			buttonRow.getChildren().addAll(ok, cancel);
			
			VBox holder = new VBox(10);
			holder.getChildren().addAll(createRow, playRow, serverRow, buttonRow);
			
			
            
			Scene scene = new Scene(holder, 500, 200);
			this.setScene(scene);
			this.show();
			
		}
	}
	
	/**
	 * Comment this whenever you get a chance
	 * 
	 * @param pane
	 * @param string
	 */
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
