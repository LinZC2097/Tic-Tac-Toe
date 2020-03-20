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
	
	public boolean checkWin (int x, int y) {
		State player;
		player = this.board[x][y];
		int posX = 0;
		int posY = 0;
		
		//水平向左
		int count_horizontal_left = 1;
		for(posX = x - 1; posX > 0 ; posX--) {
			if (this.board[posX][y] == player) {
				count_horizontal_left++;
				if (count_horizontal_left >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//水平向右
		int count_horizontal_right = 1;
		for(posX = x + 1; posX <= BOARD_LENGTH; posX++) {
			if (this.board[posX][y] == player) {
				count_horizontal_right++;
				if (count_horizontal_right >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//水平
		int count_horizontal = 0;
		count_horizontal = count_horizontal_left + count_horizontal_right - 1;
		if(count_horizontal >= AIM_LENGTH ) {
			return true;
		}
		
		//垂直向上
		int count_vertical_up = 1;
		for(posY = y - 1; posY > 0; posY--) {
			if (this.board[x][posY] == player) {
				count_vertical_up++;
				if (count_vertical_up >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//垂直向下
		int count_vertical_down = 1;
		for(posY = y + 1; posY <= BOARD_LENGTH; posY++) {
			if (this.board[x][posY] == player) {
				count_vertical_down++;
				if (count_vertical_down >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//垂直
		int count_vertical = 0;
		count_vertical = count_vertical_up + count_vertical_down - 1;
		if(count_vertical >= AIM_LENGTH ) {
			return true;
		}
    	
		//左上右下 左上
		int count_left_up = 1;
		for(posX = x - 1, posY = y - 1; posX > 0 && posY > 0; posX--, posY--) {
			if (this.board[posX][posY] == player) {
				count_left_up++;
				if (count_left_up >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//左上右下 右下
		int count_right_down = 1;
		for(posX = x + 1, posY = y + 1; posX <= BOARD_LENGTH && posY <= BOARD_LENGTH; posX++, posY++) {
			if (this.board[posX][posY] == player) {
				count_right_down++;
				if (count_right_down >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//左上右下
		int count_left_up_right_down = 0;
		count_left_up_right_down = count_left_up + count_right_down - 1;
		if(count_left_up_right_down >= AIM_LENGTH ) {
			return true;
		}
    	    	
		//右上左下 右上
		int count_right_up = 1;
		for(posX = x + 1, posY = y - 1; posX <= BOARD_LENGTH && posY > 0; posX++, posY--) {
			if (this.board[posX][posY] == player) {
				count_right_up++;
				if (count_right_up >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//右上左下 左下
		int count_left_down = 1;
		for(posX = x - 1, posY = y + 1; posX > 0 && posY <= BOARD_LENGTH; posX--, posY++) {
			if (this.board[posX][posY] == player) {
				count_left_down++;
				if (count_left_down >= AIM_LENGTH) {
					return true;
				}
			}else {
				break;
			}
		}
		//右上左下
		int count_right_up_left_down = 0;
		count_right_up_left_down = count_right_up + count_left_down - 1;
		if(count_right_up_left_down >= AIM_LENGTH ) {
			return true;
		}		
		return false;
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
