package TicTacToe;

import java.lang.Math;;

public class Board {
	static final int BOARD_LENGTH = 15;
	static final int AIM_LENGTH = 5;

	public enum State {
		X, O, Blank
	};

	public State[][] board;
	public int moveNum;

	Board(){
		this.board = new State[BOARD_LENGTH][BOARD_LENGTH];
		this.moveNum = 0;
	}
	
	public void initialization() {
		
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
	
	public void move(int x, int y) {
		State player;
		if (this.moveNum % 2 == 0) {
			player = State.O;
		} else {
			player = State.X;
		}
		
		this.board[x][y] = player;
		this.moveNum++;
	}
	
	public void show() {
		System.out.print(" \t");
		for(int i = 0; i < 10; i++) {
			System.out.print(i + " ");
		}
		for(int i = 10; i < BOARD_LENGTH; i++) {
			System.out.print(i);
		}
		System.out.println();
		for(int i = 0; i < BOARD_LENGTH; i++) {
			System.out.print(i + "\t");
			for(int j = 0; j < BOARD_LENGTH; j++) {
				if(this.board[i][j] == State.Blank) {
					System.out.print("- ");
				}else {
					System.out.print(this.board[i][j] + " ");
				}
			}
			System.out.println();
		}
	}
	

	public static void main(String[] args) {
		System.out.println("hello world");
		Board board = new Board();
		board.initialization();
		board.show();
	}

}
