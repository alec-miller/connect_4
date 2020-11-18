
public class Connect4 {

	public static void main(String args[]) {
		Connect4Model model = new Connect4Model();
		Connect4Controller controller = new Connect4Controller(model);
		Connect4View view = new Connect4View();
		view.giveModel(model);
		view.giveController(controller);
		view.main(null);
	}
	
}
