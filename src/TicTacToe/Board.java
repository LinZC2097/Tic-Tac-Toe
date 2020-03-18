package TicTacToe;

import java.lang.Math;;

public class Board {
	static final int BOARD_LENGTH = 15;
	static final int AIM_LENGTH = 6;

	public enum State {
		X, O, Blank
	};

	public State[][] board;
	public int moveNum = 0;

	public void initilizaiton() {
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				this.board[i][j] = State.Blank;
			}
		}
	}

	public boolean isUseless(int x, int y) {
		// check row
		int i = Math.min(0, x - AIM_LENGTH / 2);
		int edgeX = Math.min(BOARD_LENGTH, x + AIM_LENGTH / 2);
		for (; i < edgeX; i++) {
			if (this.board[i][y] != State.Blank) {
				return true;
			}
		}

		// check col
		int j = Math.min(0, y - AIM_LENGTH / 2);
		int edgeY = Math.min(BOARD_LENGTH, y + AIM_LENGTH / 2);
		for (; j < edgeY; j++) {
			if (this.board[x][j] != State.Blank) {
				return true;
			}
		}

		// check diagonal \
		i = Math.min(0, x - AIM_LENGTH / 2);
		j = Math.min(0, y - AIM_LENGTH / 2);
		edgeX = Math.min(BOARD_LENGTH, x + AIM_LENGTH / 2);
		edgeY = Math.min(BOARD_LENGTH, y + AIM_LENGTH / 2);
		for (; i < edgeX && j < edgeY; i++, j++) {
			if (this.board[i][j] != State.Blank) {
				return true;
			}
		}

		// check diagonal \
		i = Math.min(BOARD_LENGTH, x + AIM_LENGTH / 2);
		j = Math.min(0, y - AIM_LENGTH / 2);
		edgeX = Math.min(0, x - AIM_LENGTH / 2);
		edgeY = Math.min(BOARD_LENGTH, y + AIM_LENGTH / 2);
		for (; i < edgeX && j < edgeY; i++, j++) {
			if (this.board[i][j] != State.Blank) {
				return true;
			}
		}

		return false;
	}

	public void play(int x, int y) {
		State player;
		if (this.moveNum / 2 == 0) {
			player = State.O;
		} else {
			player = State.X;
		}

		this.board[x][y] = player;
	}
	
	public void show() {
		
	}
	

	public static void main(String[] args) {

	}

}
