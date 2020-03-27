package TicTacToe;

import java.util.HashSet;


public class Board {

    static final int BOARD_WIDTH = 16;
    static final int AIM_LENGTH = 8;
    static final long[][] SCORE = new long[][] {{10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000}, {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000}};

    public enum State {Blank, X, O}
    private State[][] board;
    private State playersTurn;
    private State oppnent;
    private State winner;
    private HashSet<Integer> movesAvailable;
    private int[][] winningWindowsX;
    private int[][] winningWindowsO;
    private long scoreX;
    private long scoreO;
    private int preMoveRow;
    private int preMoveCol;
    
    private int moveCount;
    private boolean gameOver;


    Board() {
        board = new State[BOARD_WIDTH][BOARD_WIDTH];
        winningWindowsX = new int[2][8];
        winningWindowsO = new int[2][8];
        movesAvailable = new HashSet<>();
        reset();
    }

    private void initialize () {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = State.Blank;
            }
        }
        
        scoreX = 0;
        scoreO = 0;
        
        for(int i = 0; i < 2; i++) {
        	for(int j = 0; j < 8; j++ ) {
	        	winningWindowsX[i][j] = 0;
	        	winningWindowsO[i][j] = 0;
        	}
        }
        
        movesAvailable.clear();

        for (int i = 0; i < BOARD_WIDTH*BOARD_WIDTH; i++) {
            movesAvailable.add(i);
        }
    }


    void reset () {
        moveCount = 0;
        gameOver = false;
        playersTurn = State.X;
        oppnent = State.O;
        winner = State.Blank;
        initialize();
    }
    
    public int getMoveCount() {
    	return this.moveCount;
    }

    public boolean isUseless (int index) {
        int col = index % BOARD_WIDTH;
        int row = index / BOARD_WIDTH;
        int radius = 2;
//        if(this.moveCount < 10) {
//        	radius = 2;
//        }else {
//        	radius = 3;
//        }

        int start_row = Math.max(0, row - radius);
        int start_col = Math.max(0, col - radius);
        int end_row = Math.min(row + radius, BOARD_WIDTH - 1);
        int end_col = Math.min(col + radius, BOARD_WIDTH - 1);
        
        int count = 0;
        int playerCount = 0;
        int oppnentCount = 0;
        outLoop:
        for(int i = start_row; i <= end_row; i++) {
        	for(int j = start_col; j <= end_col; j++) {
        		if(board[i][j] != State.Blank) count++;
        		if(this.moveCount > 1) {
        			if(count > 1)	return false;
        		}else {
        			if(count >= 1)	return false;
        		}
        	}
        }
        
        return true;
    }
    
    
    
    
    public boolean isMoreTwo(int y, int x) {
    	int startX = Math.max(0, x - 2);
    	int startY = Math.max(0, y - 2);
    	int endX = Math.min(BOARD_WIDTH - 1, x + 2);
    	int endY = Math.min(BOARD_WIDTH - 1, y + 2);
    	
    	for(int i = startX; i < endX; i++) {
    		if(i == x) {
    			continue;
    		}
    		if(i == x - 1 && i + 2 <= endX) {
    			if(this.board[y][i] == this.board[y][i + 2] && this.board[y][i] != State.Blank ) {
    				return true;
    			}
    		}
    		if(this.board[y][i] == this.board[y][i + 1] && this.board[y][i] != State.Blank ) {
    			return true;
    		}
    	}
    	
    	for(int i = startY; i < endY; i++) {
    		if(i == y) {
    			continue;
    		}
    		if(i == y - 1 && i + 2 <= endY) {
    			if(this.board[i][x] == this.board[i + 2][x] && this.board[i][x] != State.Blank ) {
    				return true;
    			}
    		}
    		if(this.board[i][x] == this.board[i + 1][x] && this.board[i][x] != State.Blank) {
    			return true;
    		}
    	}
    	
    	for(int i = startX, j = startY; i < endX && j < endY; i++, j++) {
    		if(i == x) {
    			continue;
    		}
    		if(i == x - 1 && i + 2 <= endX && j + 2 <= endY) {
    			if(this.board[j][i] == this.board[j + 2][i + 2] && this.board[j][i] != State.Blank ) {
    				return true;
    			}
    		}
    		if(this.board[j][i] == this.board[j + 1][i + 1] && this.board[j][i] != State.Blank) {
    			return true;
    		}
    	}
    	
    	for(int i = startX, j = endY; i < endX && j > startY; i++, j--) {
    		if(i == x) {
    			continue;
    		}
    		if(i == x - 1 && i + 2 <= endX && j - 2 >= startY) {
    			if(this.board[j][i] == this.board[j - 2][i + 2] && this.board[j][i] != State.Blank ) {
    				return true;
    			}
    		}
    		if(this.board[j][i] == this.board[j - 1][i + 1] && this.board[j][i] != State.Blank) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    
    

    public boolean move (int index) {
    	this.setPreMove(index);
        return move(index % BOARD_WIDTH, index / BOARD_WIDTH);
    }
    

    private boolean move (int x, int y) {

        if (gameOver) {
            throw new IllegalStateException("TicTacToe is over. No moves can be played.");
        }

        if (board[y][x] == State.Blank) {
            board[y][x] = playersTurn;
        } else {
            return false;
        }

        moveCount++;
        movesAvailable.remove(y * BOARD_WIDTH + x);

        // The game is a draw.
        if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
            winner = State.Blank;
            gameOver = true;
        }

        // Check for a winner.
        checkWin(x, y, playersTurn);
        updateScoreWindow(x, y, playersTurn);
        oppnent = playersTurn;
        playersTurn = (playersTurn == State.X) ? State.O : State.X;
        return true;
    }

    public void setPreMove (int index) {
        preMoveCol = index % BOARD_WIDTH;
        preMoveRow = index / BOARD_WIDTH;
    }
    
    public int[] getPreMove () {
    	return new int[] {preMoveRow, preMoveCol};
    }
    
    public boolean isGameOver () {
        return gameOver;
    }

    State[][] toArray () {
        return board.clone();
    }

    public State getTurn () {
        return playersTurn;
    }

    public State getWinner () {
        if (!gameOver) {
            throw new IllegalStateException("TicTacToe is not over yet.");
        }
        return winner;
    }

    public HashSet<Integer> getAvailableMoves () {
        return movesAvailable;
    }

    
  
    
    public void printScoreO () {
    	for(int i = 0; i < 8; i++){
    		System.out.print(winningWindowsO[i] + " ");
    	}
    	System.out.println();
    }

    public void printScoreX () {
    	for(int i = 0; i < 8; i++){
    		System.out.print(winningWindowsX[i] + " ");
    	}
    	System.out.println();
    }
    
    
    public int getBoardWidth () {
        return BOARD_WIDTH;
    }

    
    public void updateScoreWindow (int x, int y, State player) {
		
		int count = 1;
		int block = 0;
		int empty = -1;
		int secondCount = 0;
		
		for(int i = y + 1; true; i++) {
			if(i >= BOARD_WIDTH) {
				block++;
				break;
			}
			if(this.board[i][x] == State.Blank) {
				if(empty == -1 && i < BOARD_WIDTH - 1 && board[i + 1][x] == player) {
					empty = count;
					continue;
				}else {
					break;
				}
			}
			
			if(this.board[i][x] == player) {
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
				if(empty == -1 && i > 0 && board[i + 1][x] == player) {
					empty = 0;
					continue;
				}else {
					break;
				}
			}
			if(this.board[i][x] == player) {
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
		updateScoreArray(count, block, empty, player);
		
		
		count = 1;
		block = 0;
		empty = -1;
		secondCount = 0;
		
		for (int i = x + 1; true; i++) {
            if (i >= BOARD_WIDTH) {
                block++;
                break;
            }
            if (this.board[y][i] == State.Blank) {
                if (empty == -1 && i < BOARD_WIDTH - 1 && board[y][i + 1] == player) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if (this.board[y][i] == player) {
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
                if (empty == -1 && i > 0 && this.board[i - 1][y] == player) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if (this.board[i][y]  == player) {
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
		updateScoreArray(count, block, empty, player);
		
		
		count = 1;
		block = 0;
		empty = -1;
		secondCount = 0;
		
		for (int i = 1; true; i++) {
            int tempX = x + i;
            int tempY = y + i;
            if (tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
                block++;
                break;
            }
            State t = this.board[tempY][tempX];
            if (t == State.Blank) {
                if (empty == -1 && (tempX < BOARD_WIDTH- 1 && tempY < BOARD_WIDTH- 1) && this.board[tempY + 1][tempX + 1] == player) {
                    empty = count;
                    continue;
                } else {
                    break;
                }
            }
            if (t == player) {
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
                if (empty == -1 && (tempX > 0 && tempY > 0) && this.board[tempY - 1][tempX - 1] == player) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if (t == player) {
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
		updateScoreArray(count, block, empty, player);
		
		count = 1;
		block = 0;
		empty = -1;
		secondCount = 0;

		for (int i = 1; true; i++) {
            int tempX = x + i;
            int tempY = y - i;
            if (tempX < 0 || tempY < 0 || tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
                block++;
                break;
            }
            State t = this.board[tempY][tempX];
            if (t == State.Blank) {
            	try {
	                if (empty == -1 && (tempX < BOARD_WIDTH - 1 && tempY > 0) && this.board[tempY - 1][tempX + 1] == player) {
	                    empty = count;
	                    continue;
	                } else {
	                    break;
	                }
            	} catch(Exception e) {
            		System.out.println("\n" + this.toString() + "\n");
            		System.out.print("y:");
            		System.out.println(tempY - 1);
            		System.out.print("x:");
            		System.out.println(tempX - 1);
            	}
            }
            if (t == player) {
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
            if (tempX < 0 || tempY < 0 || tempX >= BOARD_WIDTH || tempY >= BOARD_WIDTH) {
                block++;
                break;
            }
            State t = this.board[tempX][tempY];
            if (t == State.Blank) {
                if (empty == -1 && (tempX > 0 && tempY > BOARD_WIDTH - 1) && this.board[tempY + 1][tempX - 1] == player) {
                    empty = 0;
                    continue;
                } else {
                    break;
                }
            }
            if (t == player) {
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
		updateScoreArray(count, block, empty, player);
        
    }
    
    public void updateScoreArray(int count, int block, int empty, State player) {
		int[][] score = new int[2][BOARD_WIDTH];
		
		
		if(player == State.O) {
			score = this.winningWindowsO;
		}else {
			score = this.winningWindowsX;
		}
		
		
    	if (empty <= 0) {
	        if (count >= 8) {
	        	score[0][8 - 1]++;
	        	return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 1:
	                    score[0][1 - 1]++;
	                    return;
	                case 2:
	                	score[0][2 - 1]++;
	                	score[0][1 - 1]--;
	                    return;
	                case 3:
	                	score[0][3 - 1]++;
	                	score[0][2 - 1]--;
	                    return;
	                case 4:
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	                case 5:
	                	score[0][5 - 1]++;
	                	score[0][4 - 1]--;
	                    return;   
	                case 6:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                    return;
	                case 7:
	                	score[0][7 - 1]++;
	                	score[0][8 - 1]--;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 1:
	                	score[1][1 - 1]++;
	                    return;
	                case 2:
	                	score[1][2 - 1]++;
	                	score[1][1 - 1]--;
	                    return;
	                case 3:
	                	score[1][3 - 1]++;
	                	score[1][2 - 1]--;
	                    return;
	                case 4:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 5:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                    return;
	                case 6:
	                	score[1][6 - 1]++;
	                	score[1][5 - 1]--;
	                    return;
	                case 7:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                    return;
	            }
	        }

	    } else if (empty == 1 || empty == count - 1) {
	        if (count >= 9) {
	        	score[0][8 - 1]++;
	        	return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 2:
	                    score[0][1 - 1]++;
	                    return;
	                case 3:
	                	score[0][2 - 1]++;
	                	score[0][1 - 1]--;
	                    return;
	                case 4:
	                	score[0][3 - 1]++;
	                	score[0][2 - 1]--;
	                    return;
	                case 5:
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	                case 6:
	                	score[0][5 - 1]++;
	                	score[0][4 - 1]--;
	                    return;
	                case 7:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                    return;
	                case 8:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
		            case 2:
	                	score[1][1 - 1]++;
	                    return;
	                case 3:
	                	score[1][2 - 1]++;
	                	score[1][1 - 1]--;
	                    return;
	                case 4:
	                	score[1][3 - 1]++;
	                	score[1][2 - 1]--;
	                    return;
	                case 5:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 6:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                    return;
	                case 7:
	                	score[1][6 - 1]++;
	                	score[1][5 - 1]--;
	                    return;
	                case 8:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                    return;
	            }
	        }
	    } else if (empty == 2 || empty == count - 2) {
	        if (count >= 10) {
	        	score[0][8 - 1]++;
	        	return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 3:
	                	score[0][3 - 1]++;
	                	score[0][2 - 1]--;
	                    return;
	                case 4:
	                case 5:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                	return;
	                	
	                case 6:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                	return;
	                case 7:
	                	score[1][6 - 1]++;
	                	score[1][5 - 1]--;
	                	return;
	                case 8:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                	return;
	                case 9:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                	return;

	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 3:
	                	score[1][3 - 1]++;
	                	score[1][2 - 1]--;
	                    return;
	                case 4:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 5:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 6:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                    return;
	                case 7:
	                	score[1][6 - 1]++;
	                	score[1][5 - 1]--;
	                    return;
	                case 8:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                    return;
	                case 9:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 5:
	                case 6:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                    return;
	                case 7:
	                case 8:
	                case 9:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                    return;
	            }
	        }
	    } else if (empty == 3 || empty == count - 3) {
	        if (count >= 11) {
	        	score[0][8 - 1]++;
	            return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                	score[0][3 - 1]++;
	                	score[0][2 - 1]--;
	                	return;
	                case 7:
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	                case 8:
	                	score[0][5 - 1]++;
	                	score[0][4 - 1]--;
	                    return;
	                case 9:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                    return;
	                case 10:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 4:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 5:
	                case 6:
	                case 7:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                	return;
	                case 8:
	                	score[0][5 - 1]++;
	                	score[0][4 - 1]--;
	                	return;
	                case 9:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                	return;
	                case 10:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	            	case 5:
	            		score[1][5 - 1]++;
	            		score[1][4 - 1]--;
	            		return;
	            	case 6:
	                case 7:
	                	score[1][6 - 1]++;
	                	score[1][5 - 1]--;
	                    return;
	                case 8:
	                case 9:
	                case 10:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                    return;
	            }
	        }
	    } else if (empty == 4 || empty == count - 4) {
	        if (count >= 12) {
	        	score[0][8 - 1]++;
	            return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 5:
	                case 6:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                	return;
	                case 7:
	                case 8:
	                	score[0][5 - 1]++;
	                	score[0][4 - 1]--;
	                	return;
	                case 9:
	                case 10:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                	return;
	                case 11:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 5:
	                case 6:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                	return;
	                case 7:
	                case 8:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                	return;
	                case 9:
	                	score[0][5 - 1]++;
	                	score[0][4 - 1]--;
	                	return;
	                case 10:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                	return;
	                case 11:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 5:
	                case 6:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                    return;
	                case 7:
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                	score[1][7 - 1]++;
	                	score[1][7 - 1]--;
	                	return;
	            }
	        }
	    } else if (empty == 5 || empty == count - 5) {
	    	if (count >= 13) {
	        	score[0][8 - 1]++;
	            return;
	        }
	    	if (block == 0) {
	    		switch (count) {
		    		case 6:
	                case 7:
	                case 8:
	                	score[0][5 - 1]++;
	                	score[0][4 - 1]--;
	                	return;
	                case 9:
	                case 10:
	                case 11:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                	return;
	                case 12:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                	return;
	    		}
	    	}
	    	if (block == 1) {
	    		switch (count) {
		    		case 6:
		    			score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                	return;
	                case 7:
	                case 8:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                	return;
	                case 9:
	                case 10:
	                case 11:
	                case 12:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                	return;
	    		}
	    	}
	    	if( block == 2) {
	    		switch (count) {
	                case 6:
	                	score[1][5 - 1]++;
	                	score[1][4 - 1]--;
	                	return;
	                case 7:
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                case 12:
	                	score[1][7 - 1]++;
	                	score[1][7 - 1]--;
	                	return;
	    		}
	    	}
	    } else if (empty == 6 || empty == count - 6) {
	    	if (count >= 14) {
	        	score[0][8 - 1]++;
	            return;
	        }
	    	if (block == 0) {
	    		switch (count) {
	                case 7:
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                	return;
	                case 12:
	                case 13:
	                case 14:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                	return;
	    		}
	    	}
	    	if (block == 1) {
	    		switch (count) {
		    		case 7:
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                	score[1][6 - 1]++;
	                	score[1][5 - 1]--;
	                	return;
	                case 12:
	                case 13:
	                case 14:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                	return;
	    		}
	    	}
	    	if( block == 2) {
	    		switch (count) {
	                case 7:
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                case 12:
	                case 13:
	                	score[1][7 - 1]++;
	                	score[1][7 - 1]--;
	                	return;
	    		}
	    	}
	    } else if (empty == 7 || empty == count - 7) {
	    	if (count >= 15) {
	        	score[0][8 - 1]++;
	            return;
	        }
	    	if (block == 0) {
	    		switch (count) {
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                	score[0][6 - 1]++;
	                	score[0][5 - 1]--;
	                	return;
	                case 12:
	                case 13:
	                case 14:
	                	score[0][7 - 1]++;
	                	score[0][6 - 1]--;
	                	return;
	    		}
	    	}
	    	if (block == 1) {
	    		switch (count) {
		    		case 7:
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                	score[1][6 - 1]++;
	                	score[1][5 - 1]--;
	                	return;
	                case 12:
	                case 13:
	                case 14:
	                	score[1][7 - 1]++;
	                	score[1][6 - 1]--;
	                	return;
	    		}
	    	}
	    	if( block == 2) {
	    		switch (count) {
	                case 8:
	                case 9:
	                case 10:
	                case 11:
	                case 12:
	                case 13:
	                case 14:
	                	score[1][7 - 1]++;
	                	score[1][7 - 1]--;
	                	return;
	    		}
	    	}
	    } else if (empty == 8 || empty == count - 8) {
	    	score[0][8 - 1]++;
            return;
	    }
    	

	    return;
	}
    
    public int getScoreX() {
		int result = 0;
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 5; j++) {
				result += this.winningWindowsX[i][j] * SCORE[i][j];
			}
		}
		return result;
	}
    
	public int getScoreO() {
		int result = 0;
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 5; j++) {
				result += this.winningWindowsO[i][j] * SCORE[i][j];
			}
		}
		return result;
	}
    
    private void checkWin (int col, int row, State player) {
    	int N = BOARD_WIDTH;
    	// Check vertical
    	int count_vertical = 1;
    	int row_up = row - 1;
    	while (row_up >= 0 && board[row_up][col] == player) {
    		count_vertical++;
    		row_up--;
    	}
    	int row_down = row + 1;
    	while (row_down < N && board[row_down][col] == player) {
    		count_vertical++;
    		row_down++;
    	}
    	
    	if(count_vertical >= AIM_LENGTH ) {
    		winner = playersTurn;
            gameOver = true;
            return;
    	}
    	
    	
    	// Check horizontal
    	int count_horizontal = 1;
    	int col_left = col - 1;
    	while (col_left >= 0 && board[row][col_left] == player) {
    		count_horizontal++;
    		col_left--;
    	}
    	int col_right = col + 1;
    	while (col_right < N && board[row][col_right] == player) {
    		count_horizontal++;
    		col_right++;
    	}
    	
    	if(count_horizontal >= AIM_LENGTH) {
    		winner = playersTurn;
            gameOver = true;
            return;
    	}
    	
    	// Check diagonal
    	int count_diagonal_left = 1;
    	int row_left_up = row - 1, col_left_up = col - 1;
    	while (row_left_up >= 0 && col_left_up >= 0 && board[row_left_up][col_left_up] == player) {
    		count_diagonal_left++;
    		row_left_up--;
    		col_left_up--;
    	}
    	int row_right_down = row + 1, col_right_down = col + 1;
    	while (row_right_down < N && col_right_down < N && board[row_right_down][col_right_down] == player) {
    		count_diagonal_left++;
    		row_right_down++;
    		col_right_down++;
    	}

    	int count_diagonal_right = 1;
    	int row_left_down = row + 1, col_left_down = col - 1;
    	while (row_left_down < N && col_left_down >= 0 && board[row_left_down][col_left_down] == player) {
    		count_diagonal_right++;
    		row_left_down++;
    		col_left_down--;
    	}
    	int row_right_up = row - 1, col_right_up = col + 1;
    	while (row_right_up >= 0 && col_right_up < N && board[row_right_up][col_right_up] == player) {
    		count_diagonal_right++;
    		row_right_up--;
    		col_right_up++;
    	}
    	
    	if(count_diagonal_left >= AIM_LENGTH || count_diagonal_right >= AIM_LENGTH) {
    		winner = playersTurn;
            gameOver = true;
            return;
    	}
    }


    public Board getDeepCopy () {
        Board board             = new Board();

        for (int i = 0; i < board.board.length; i++) {
            board.board[i] = this.board[i].clone();
        }
        
        for(int i = 0; i < 2; i++) {
        	board.winningWindowsX[i] = this.winningWindowsX[i].clone();
        	board.winningWindowsO[i] = this.winningWindowsO[i].clone();
        }

        board.playersTurn       = this.playersTurn;
        board.oppnent	        = this.oppnent;
        board.winner            = this.winner;
        board.movesAvailable    = new HashSet<>();
        board.movesAvailable.addAll(this.movesAvailable);
        board.moveCount         = this.moveCount;
        board.gameOver          = this.gameOver;
        board.preMoveRow        = this.preMoveRow;
        board.preMoveCol        = this.preMoveCol;
        
        
        return board;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < BOARD_WIDTH; y++) {
        	if(y == 0) {
        		sb.append("  ");
        		for(int i = 0; i < BOARD_WIDTH; i++) {
        			sb.append(i < 10 ? " " + i : i);
        		}
        		sb.append("\n");
        	}
            for (int x = 0; x < BOARD_WIDTH; x++) {
            	if(x == 0) {
            		sb.append(y < 10 ? " " + y : y);
            		sb.append(" ");
            	}
                if (board[y][x] == State.Blank) {
                    sb.append("-");
                } else {
                    sb.append(board[y][x].name());
                }
                sb.append(" ");

            }
            if (y != BOARD_WIDTH -1) {
                sb.append("\n");
            }
        }

        return new String(sb);
    }

}
