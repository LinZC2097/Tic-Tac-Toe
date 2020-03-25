package TicTacToe;

import java.lang.Math;
import java.util.HashSet;
import java.util.Hashtable;

import javafx.scene.layout.Border;;

public class Board {
	static final int BOARD_LENGTH = 15;
	static final int AIM_LENGTH = 3;
	static final int ONE = 10;
	static final int TWO = 100;
	static final int THREE = 1000;
	static final int FOUR = 100000;
	static final int FIVE = 10000000;
	static final int BLOCKED_ONE = 1;
	static final int BLOCKED_TWO = 10;
	static final int BLOCKED_THREE = 100;
	static final int BLOCKED_FOUR = 10000;
	static final int[][] SCORE = new int[][] {{10, 100, 1000, 100000, 10000000}, {1, 10, 100, 10000, 10000000}};

	public enum State {
		X, O, Blank
	};

	private State[][] board;
	private HashSet<Integer> availableMove;
	private int moveNum;
	private State curPlayer;
	private int[][] preScore;
	private int[][] score;
	private boolean gameOver;
	private State winner;

	Board() {
		this.board = new State[BOARD_LENGTH][BOARD_LENGTH];
		this.moveNum = 0;
		this.curPlayer = State.O;
		this.availableMove = new HashSet<>();
		this.gameOver = false;
		this.score = new int[2][AIM_LENGTH];
		this.preScore = new int[2][AIM_LENGTH];
		this.winner = null;
		this.initialization();
	}

	public void initialization() {
		// initial board
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				this.board[i][j] = State.Blank;
			}
		}
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < AIM_LENGTH; j++) {
				this.score[i][j] = 0;
				this.preScore[i][j] = 0;
			}
		}
		// initialize availableMove
//		for (int i = 0; i < BOARD_LENGTH * BOARD_LENGTH; i++) {
//			this.availableMove.add(i);
//		}
	}

	public Board deepcopy() {
		Board newBoard = new Board();

		newBoard.initialization();

		for (int i = 0; i < Board.BOARD_LENGTH; i++) {
			newBoard.board[i] = this.board[i].clone();
		}

		newBoard.availableMove = (HashSet) this.availableMove.clone();

		newBoard.moveNum = this.moveNum;
		newBoard.curPlayer = this.curPlayer;
		newBoard.gameOver = this.gameOver;
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < AIM_LENGTH; j++) {
				newBoard.score[i][j] = this.score[i][j];
			}
		}
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < AIM_LENGTH; j++) {
				newBoard.preScore[i][j] = this.preScore[i][j];
			}
		}
		newBoard.winner = this.winner;

		return newBoard;
	}
	
	
		


	public static int[] getMove(int index) {
		return new int[] { (index % BOARD_LENGTH), (index / BOARD_LENGTH) };
	}

	public State getPlayer() {
		return this.curPlayer;
	}
	
	public State getWinner() {
		return this.winner;
	}

	public int getAvaliableMoveLen() {
		return this.availableMove.size();
	}

	public void updateAvailable(int x, int y) {
		int radius = 2;
		int startX = Math.max(0, x - radius);
		int startY = Math.max(0, y - radius);

		int endX = Math.min(x + radius, BOARD_LENGTH - 1);
		int endY = Math.min(y + radius, BOARD_LENGTH - 1);

		for (int j = startY; j <= endY; j++) {
			for (int i = startX; i <= endX; i++) {
				if (this.board[j][i] == State.Blank) {
					this.availableMove.add(j * BOARD_LENGTH + i);
				}
			}
		}
	}

	public HashSet<Integer> getAvailableMove() {
		return this.availableMove;
	}

	public boolean isUseless(int x, int y) {

		// check 2M * 2M matrix whose center is (x, y) is empty or not

		int radius = 2;

		int startX = Math.max(0, x - radius);
		int startY = Math.max(0, y - radius);

		int endX = Math.min(BOARD_LENGTH - 1, x + radius);
		int endY = Math.min(BOARD_LENGTH - 1, y + radius);

		for (int i = startX; i < endX; i++) {
			for (int j = startY; j < endY; j++) {
				if (!this.availableMove.contains(i * BOARD_LENGTH + j)) {
					return true;
				}
			}
		}
		return false;
	}

	public int[] getMoveIndex(int moveIndex) {
		return new int[] { (moveIndex / BOARD_LENGTH), (moveIndex % BOARD_LENGTH) };
	}

	public void move(int moveIndex) {
		this.move(moveIndex % BOARD_LENGTH, moveIndex / BOARD_LENGTH);
	}

	private void move(int x, int y) {

		this.board[y][x] = (this.curPlayer == State.O) ? State.O : State.X;
		this.availableMove.remove(x * BOARD_LENGTH + y);
		this.moveNum++;
		this.curPlayer = (this.curPlayer == State.O) ? State.X : State.O;
		this.updateAvailable(x, y);
	}
	

	public boolean checkWin(int x, int y) {
		State player = (this.curPlayer == State.O) ? State.X : State.O;
		int count = 1;
		// check row
		for (int i = x + 1; i <= BOARD_LENGTH - 1 && this.board[y][i] == player; i++) {
			count++;
		}
		for (int i = x - 1; i >= 0 && this.board[y][i] == player; i--) {
			count++;
		}
		if (count == AIM_LENGTH) {
			this.gameOver = true;
			return true;
		}

		// check column
		count = 1;
		for (int j = y + 1; j <= BOARD_LENGTH - 1 && this.board[j][x] == player; j++) {
			count++;
		}
		for (int j = y - 1; j >= 0 && this.board[j][x] == player; j--) {
			count++;
		}
		if (count == AIM_LENGTH) {
			this.gameOver = true;
			return true;
		}

		// check diagonal \
		count = 1;
		for (int i = x + 1, j = y + 1; i <= BOARD_LENGTH - 1 && j <= BOARD_LENGTH - 1
				&& this.board[j][i] == player; i++, j++) {
			count++;
		}
		for (int i = x - 1, j = y - 1; i >= 0 && j >= 0 && this.board[j][i] == player; i--, j--) {
			count++;
		}
		if (count == AIM_LENGTH) {
			this.gameOver = true;
			return true;
		}

		// check diagonal /
		count = 1;
		for (int i = x + 1, j = y - 1; i <= BOARD_LENGTH - 1 && j >= 0
				&& this.board[j][i] == player; i++, j--) {
			count++;
		}
		for (int i = x - 1, j = y + 1; i >= 0 && j <= BOARD_LENGTH && this.board[j][i] == player; i--, j++) {
			count++;
		}
		if (count == AIM_LENGTH) {
			this.gameOver = true;
			return true;
		}

		return false;
	}
	
	public boolean isGameOver() {
		return this.gameOver;
	}

	public void show() {
		System.out.print("y    x \t");

		for (int i = 0; i < 10; i++) {
			System.out.print(i + " ");
		}
		for (int i = 10; i < BOARD_LENGTH; i++) {
			System.out.print(i);
		}
		System.out.println();
		for (int i = 0; i < BOARD_LENGTH; i++) {
			System.out.print(i + "\t");
			for (int j = 0; j < BOARD_LENGTH; j++) {
				if (this.board[i][j] == State.Blank) {
					System.out.print("- ");
				} else {
					System.out.print(this.board[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	// !!!!!!!!!!!!!!!!!!!!
	public void updateScore(int x, int y) {
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < AIM_LENGTH; j++) {
				this.preScore[i][j] = score[i][j];
			}
		}
		
		int count = 1;
		int block = 0;
		int empty = -1;
		int secondCount = 0;
		
		for(int i = y + 1; true; i++) {
			if(i >= BOARD_LENGTH) {
				block++;
				break;
			}
			if(this.board[i][x] == State.Blank) {
				if(empty == -1 && i < BOARD_LENGTH - 1 && board[i + 1][x] == this.curPlayer) {
					empty = count;
					continue;
				}else {
					break;
				}
			}
			
			if(this.board[i][x] == this.curPlayer) {
				count++;
				continue;
			}else {
				block++;
				break;
			}
		}
	
		for(int i = y - 1; true; i--) {
			if(i < 0) {
				block++;
				break;
			}
			if(this.board[i][x] == State.Blank) {
				if(empty == -1 && i > 0 && board[i + 1][x] == this.curPlayer) {
					empty = 0;
					continue;
				}else {
					break;
				}
			}
			if(this.board[i][x] == this.curPlayer) {
				secondCount ++;
				if(empty != -1) {
					empty++;
				}else {
					block++;
					break;
				}
			}
		}
		count += secondCount;
		updateScoreArray(count, block, empty);
		
		
		count = 1;
		block = 0;
		empty = -1;
		secondCount = 0;
		
		for (int i = x + 1; true; i++) {
            if (i >= BOARD_LENGTH) {
                block++;
                break;
            }
            if (this.board[y][i] == State.Blank) {
                if (empty == -1 && i < BOARD_LENGTH - 1 && board[y][i + 1] == this.curPlayer) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if (this.board[y][i] == this.curPlayer) {
                count++;
                continue;
            } else {
                block++;
                break;
            }
        }
		
		for (int i = x - 1; true; i--) {
            if (i < 0) {
                block++;
                break;
            }
            
            if (this.board[i][y] == State.Blank) {
                if (empty == -1 && i > 0 && this.board[i - 1][y] == this.curPlayer) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if (this.board[i][y]  == this.curPlayer) {
            	secondCount++;
                if(empty != -1) { 
                	empty++;
                }
                continue;
            } else {
                block++;
                break;
            }
        }
		count += secondCount;
		updateScoreArray(count, block, empty);
		
		
		count = 1;
		block = 0;
		empty = -1;
		secondCount = 0;
		
		for (int i = 1; true; i++) {
            int tempX = x + i;
            int tempY = y + i;
            if (tempX >= BOARD_LENGTH || tempY >= BOARD_LENGTH) {
                block++;
                break;
            }
            State t = this.board[tempY][tempX];
            if (t == State.Blank) {
                if (empty == -1 && (tempX < BOARD_LENGTH- 1 && tempY < BOARD_LENGTH- 1) && this.board[tempY + 1][tempX + 1] == this.curPlayer) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if (t == this.curPlayer) {
                count++;
                continue;
            } else {
                block++;
                break;
            }
        }
		for (int i = 1; true; i++) {
            int tempX = x - i;
            int tempY = y - i;
            if (tempX < 0 || tempY < 0) {
                block++;
                break;
            }
            State t = this.board[tempY][tempX];
            if (t == State.Blank) {
                if (empty == -1 && (tempX > 0 && tempY > 0) && this.board[tempY - 1][tempX - 1] == this.curPlayer) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if (t == this.curPlayer) {
                secondCount++;
                if(empty != -1) { 
                	empty++;
                }
                continue;
            } else {
                block++;
                break;
            }
        }
		count += secondCount;
		updateScoreArray(count, block, empty);
		
		count = 1;
		block = 0;
		empty = -1;
		secondCount = 0;

		for (int i = 1; true; i++) {
            int tempX = x + i;
            int tempY = y - i;
            if (tempX < 0 || tempY < 0 || tempX >= BOARD_LENGTH || tempY >= BOARD_LENGTH) {
                block++;
                break;
            }
            State t = this.board[tempY][tempX];
            if (t == State.Blank) {
                if (empty == -1 && (tempX < BOARD_LENGTH - 1 && tempY < BOARD_LENGTH - 1) && this.board[tempY - 1][tempX + 1] == this.curPlayer) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if (t == this.curPlayer) {
                count++;
                continue;
            } else {
                block++;
                break;
            }
        }

        for (int i = 1; true; i++) {
            int tempX = x - i;
            int tempY = y + i;
            if (tempX < 0 || tempY < 0 || tempX >= BOARD_LENGTH || tempY >= BOARD_LENGTH) {
                block++;
                break;
            }
            State t = this.board[tempX][tempY];
            if (t == State.Blank) {
                if (empty == -1 && (tempX > 0 && tempY > 0) && this.board[tempY + 1][tempX - 1] == this.curPlayer) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if (t == this.curPlayer) {
                secondCount++;
                if(empty != -1) { 
                	empty++;
                }
                continue;
            } else {
                block++;
                break;
            }
        }
        count += secondCount;
		updateScoreArray(count, block, empty);
	}
	

	
	// !!!!!!!!!!!!!!!!!!!
	public void updateScoreArray(int count, int block, int empty) {
		if (empty <= 0) {
	        if (count >= 5) {
	        	this.score[0][5 - 1]++;
	        	return;// FIVE
	        }
	        if (block == 0) {
	            switch (count) {
	                case 1:
	                    this.score[0][1 - 1]++;
	                    return;
	                case 2:
	                	this.score[0][2 - 1]++;
	                    return;
	                case 3:
	                	this.score[0][3 - 1]++;
	                    return;
	                case 4:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 1:
	                	this.score[1][1 - 1]++;
	                    return;
	                case 2:
	                	this.score[1][2 - 1]++;
	                    return;
	                case 3:
	                	this.score[1][3 - 1]++;
	                    return;
	                case 4:
	                	this.score[1][4 - 1]++;
	                    return;
	            }
	        }

	    } else if (empty == 1 || empty == count - 1) {
	        //第1个是空位
	        if (count >= 6) {
	        	this.score[0][5 - 1]++;
	        	return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 2:
	                    this.score[0][1 - 1]++;
	                    return;
	                case 3:
	                	this.score[0][2 - 1]++;
	                    return;
	                case 4:
	                	this.score[0][3 - 1]++;
	                    return;
	                case 5:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
		            case 2:
	                	this.score[1][1 - 1]++;
	                    return;
	                case 3:
	                	this.score[1][2 - 1]++;
	                    return;
	                case 4:
	                	this.score[1][3 - 1]++;
	                    return;
	                case 5:
	                	this.score[1][4 - 1]++;
	                    return;
	            }
	        }
	    } else if (empty == 2 || empty == count - 2) {
	        //第二个是空位
	        if (count >= 7) {
	        	this.score[0][5 - 1]++;
	        	return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 3:
	                	this.score[0][3 - 1]++;
	                    return;
	                case 4:
	                case 5:
	                	this.score[1][4 - 1]++;
	                    return;
	                case 6:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 3:
	                	this.score[1][3 - 1]++;
	                    return;
	                case 4:
	                	this.score[1][4 - 1]++;
	                    return;
	                case 5:
	                	this.score[1][4 - 1]++;
	                    return;
	                case 6:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                	this.score[1][4 - 1]++;
	                    return;
	            }
	        }
	    } else if (empty == 3 || empty == count - 3) {
	        if (count >= 8) {
	        	this.score[0][5 - 1]++;
	            return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 4:
	                case 5:
	                	this.score[0][3 - 1]++;
	                    return;
	                case 6:
	                	this.score[1][4 - 1]++;
	                    return;
	                case 7:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                	this.score[1][4 - 1]++;
	                    return;
	                case 7:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                case 7:
	                	this.score[1][4 - 1]++;
	                    return;
	            }
	        }
	    } else if (empty == 4 || empty == count - 4) {
	        if (count >= 9) {
	        	this.score[0][5 - 1]++;
	            return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 5:
	                case 6:
	                case 7:
	                case 8:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                case 7:
	                	this.score[1][4 - 1]++;
	                    return;
	                case 8:
	                	this.score[0][4 - 1]++;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 5:
	                case 6:
	                case 7:
	                case 8:
	                	this.score[1][4 - 1]++;
	                    return;
	            }
	        }
	    } else if (empty == 5 || empty == count - 5) {
	    	this.score[0][5 - 1]++;
            return;
	    }

	    return;
	}
	
	public int getScore() {
		int result = 0;
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 5; j++) {
				result += this.score[i][j] * SCORE[i][j];
			}
		}
		return result;
	}
	

	public static void main(String[] args) {
		System.out.println("hello world");
		Board board = new Board();
//		board.initialization();
		board.show();
		int[] a = board.getMoveIndex(14);
		for (int val : a) {
			System.out.println(val);

		}

	}

}
