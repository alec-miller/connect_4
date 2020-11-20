import java.util.Observable;

public class Connect4Model extends Observable{
	
	private Connect4MoveMessage[][] board;
	private int turn = 1; // 1 = yellow, 2 = red

	public Connect4Model() {
		board = new Connect4MoveMessage[7][6];
		// Initialize board to have null in every position
		for(int x = 0; x < 7; x++) {
			for(int i = 0; i < 6; i++) {
				board[x][i] = null;
			}
		}
	}
	
	/**
	 * 0 - valid move
	 * 1 - yellow win
	 * 2 - red win
	 * 3 - tie
	 * 4 - invalid move
	 * 
	 * @param column
	 */
	public int add(int column) {
		if(board[column][5] != null) {
			return 4;
		}else {
			int row = 0;
			// find next open row in the given column
			while(board[column][row] != null) {
				row++;
			}
			Connect4MoveMessage piece = new Connect4MoveMessage(row, column, turn);
			this.setChanged();
			this.notifyObservers(piece);
			board[column][row] = piece;
			if(winCheck(column, row)) {
				if(turn == 1) {
					return 1;
				}else {
					return 2;
				}
			}else {
				if(catCheck()) {
					return 3;
				}else {
					if(turn == 1) {
						turn = 2;
					}else {
						turn = 1;
					}
				}
			}
		}
		return 0;
	}

	/**
	 * Checks to see if the game ended in a draw
	 */
	private boolean catCheck() {
		// Checks top row of each column, if at least one is empty, game isn't over
		for(int col = 0; col < 7; col++) {
			if(board[col][5] == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks the current piece added to see if the user has now won
	 * 
	 * @param column
	 * @param row
	 */
	private boolean winCheck(int column, int row) {
		if(downwardsCheck(column, row, board[column][row].getColor()) || sidewaysCheck(column, row, board[column][row].getColor()) || diagonalCheck(column, row, board[column][row].getColor())) {
			return true;
		}
		return false;
	}
	
	private boolean downwardsCheck(int column, int row, int color) {
		// if row < 3, then height can't reach 4, so skip check
		if(row > 2) {
			int count = 1;
			while(board[column][row] != null && board[column][row].getColor() == color) {
				if(count == 4) {
					return true;
				}
				count++;
				row--;
			}
		}
		return false;
	}

	private boolean sidewaysCheck(int column, int row, int color) {
		int count = 1;
		int col = column;
		// Check the pieces to the left of the given position
		while(col >= 0 && board[col][row] != null && board[col][row].getColor() == color) {
			if(count == 4) {
				return true;
			}
			col--;
			count++;
		}
		// Move col to the right one since we already recorded a count for column's original pos
		col = column + 1;
		// Check the pieces to the right of the given position
		while(col < 7 && board[col][row] != null && board[col][row].getColor() == color) {
			if(count == 4) {
				return true;
			}
			col++;
			count++;
		}
		return false;
	}

	private boolean diagonalCheck(int column2, int row2, int color) {
		int count = 1;
		int col = column2;
		int row = row2;
		// Bottom left to Top right diagonal check
		// Down-left diagonal check
		while((col < 7 && row < 6) && (col >= 0 && row >= 0) && board[col][row] != null && board[col][row].getColor() == color) {
			if(count == 4) {
				return true;
			}
			col--;
			row--;
			count++;
		}
		// Reset col and row to their original position + 1 since we already recorded the original pos
		col = column2 + 1;
		row = row2 + 1;
		// Top-right diagonal check
		while((col < 7 && row < 6) && (col >= 0 && row >= 0) && board[col][row] != null && board[col][row].getColor() == color) {
			if(count == 4) {
				return true;
			}
			col--;
			row--;
			count++;
		}
		// End of bottom left to top right diagonal check
		//
		// Top left to bottom right diagonal check, have to reset count since other diagonal check failed.
		count = 1;
		col = column2;
		row = row2;
		// Top left to bottom right diagonal check
		// Down-left diagonal check
		while((col < 7 && row < 6) && (col >= 0 && row >= 0) && board[col][row] != null && board[col][row].getColor() == color) {
			if(count == 4) {
				return true;
			}
			col++;
			row--;
			count++;
		}
		// Reset col and row to their original position (+/-) 1 since we already recorded the original pos
		col = column2 - 1;
		row = row2 + 1;
		// Top-right diagonal check
		while((col < 7 && row < 6) && (col >= 0 && row >= 0) && board[col][row] != null && board[col][row].getColor() == color) {
			if(count == 4) {
				return true;
			}
			col--;
			row++;
			count++;
		}
		return false;
	}
}
