package Generalized_Tic_tac_Toe;

import java.util.HashSet;




/**
 * Represents the Tic Tac Toe board.
 */
public class Board {

    static final int BOARD_WIDTH = 15;
    static final int M = 5;
    static final int[][] SCORE = new int[][] {{10, 100, 1000, 100000, 10000000}, {1, 10, 100, 10000, 10000000}};

    public enum State {Blank, X, O}
    private State[][] board;
    private State playersTurn;
    private State winner;
    private HashSet<Integer> movesAvailable;
    private int[][] winningWindowsX;
    private int[][] winningWindowsO;
    private int scoreX;
    private int scoreO;
    private int preMoveRow;
    private int preMoveCol;
    
    private int moveCount;
    private boolean gameOver;

    /**
     * Construct the Tic Tac Toe board.
     */
    Board() {
        board = new State[BOARD_WIDTH][BOARD_WIDTH];
        winningWindowsX = new int[2][M + 1];
        winningWindowsO = new int[2][M + 1];
        movesAvailable = new HashSet<>();
        reset();
    }

    /**
     * Set the cells to be blank and load the available moves (all the moves are
     * available at the start of the game).
     */
    private void initialize () {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = State.Blank;
            }
        }
        
        scoreX = 0;
        scoreO = 0;
        
        for(int i = 0; i < 2; i++) {
        	for(int j = 0; j < M; j++ ) {
	        	winningWindowsX[i][j] = 0;
	        	winningWindowsO[i][j] = 0;
        	}
        }
        
        movesAvailable.clear();

//        for (int i = 0; i < BOARD_WIDTH*BOARD_WIDTH; i++) {
//            movesAvailable.add(i);
//        }
    }

    /**
     * Restart the game with a new blank board.
     */
    void reset () {
        moveCount = 0;
        gameOver = false;
        playersTurn = State.X;
        winner = State.Blank;
        initialize();
    }
    
    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index     the position on the board (example: index 4 is location (0, 1))
     * @return          true if the move has not already been played
     */
    public boolean isUseless (int index) {
        int col = index % BOARD_WIDTH;
        int row = index / BOARD_WIDTH;
        
        int radius = 3;
        
     // Check Manhattan distance with max limit 2*M - 1
        int distance = Math.abs(row - preMoveRow) + Math.abs(col - preMoveCol);
        if(moveCount < 5 && distance > 3) return true;
        if(moveCount < 10 && distance > 5) return true;
        if(moveCount < BOARD_WIDTH * BOARD_WIDTH / 2 && distance > 2 * M) return true;
        
        
        
        // Search for radius M, if all empty, it's a useless move.
        int start_row = Math.max(0, row - radius);
        int start_col = Math.max(0, col - radius);
        int end_row = Math.min(row + radius, BOARD_WIDTH - 1);
        int end_col = Math.min(col + radius, BOARD_WIDTH - 1);
        
        int count = 0;
        for(int i = start_row; i <= end_row; i++) {
        	for(int j = start_col; j <= end_col; j++) {
        		if(board[i][j] != State.Blank) count++;
        		if(count / moveCount > 1 / 3 || count > 3) return false;
        	}
        }
        
        
        
        
        return true;
    }
    

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index     the position on the board (example: index 4 is location (0, 1))
     * @return          true if the move has not already been played
     */
    public boolean move (int index) {
        return move(index % BOARD_WIDTH, index / BOARD_WIDTH);
    }
    

    public void updateAvailable(int x, int y) {
		int radius = 2;
		int startX = Math.max(0, x - radius);
		int startY = Math.max(0, y - radius);

		int endX = Math.min(x + radius, BOARD_WIDTH - 1);
		int endY = Math.min(y + radius, BOARD_WIDTH - 1);

		for (int j = startY; j <= endY; j++) {
			for (int i = startX; i <= endX; i++) {
				if (this.board[j][i] == State.Blank) {
					this.movesAvailable.add(j * BOARD_WIDTH + i);
				}
			}
		}
	}
    
    /**
     * Places an X or an O on the specified location depending on who turn it is.
     * @param x         the x coordinate of the location
     * @param y         the y coordinate of the location
     * @return          true if the move has not already been played
     */
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
//        movesAvailable.remove(y * BOARD_WIDTH + x);
        this.updateAvailable(x, y);

        // The game is a draw.
        if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
            winner = State.Blank;
            gameOver = true;
        }

        // Check for a winner.
        checkWin(x, y, playersTurn);
        updateScoreWindow(x, y, playersTurn);

        playersTurn = (playersTurn == State.X) ? State.O : State.X;
        return true;
    }

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index     the position on the board (example: index 4 is location (0, 1))
     * @return          true if the move has not already been played
     */
    public void setPreMove (int index) {
        preMoveCol = index % BOARD_WIDTH;
        preMoveRow = index / BOARD_WIDTH;
    }
    
    
    /**
     * Check to see if the game is over (if there is a winner or a draw).
     * @return          true if the game is over
     */
    public boolean isGameOver () {
        return gameOver;
    }

    /**
     * Get a copy of the array that represents the board.
     * @return          the board array
     */
    State[][] toArray () {
        return board.clone();
    }

    /**
     * Check to see who's turn it is.
     * @return          the player who's turn it is
     */
    public State getTurn () {
        return playersTurn;
    }

    /**
     * Check to see who won.
     * @return          the player who won (or Blank if the game is a draw)
     */
    public State getWinner () {
        if (!gameOver) {
            throw new IllegalStateException("TicTacToe is not over yet.");
        }
        return winner;
    }

    /**
     * Get the indexes of all the positions on the board that are empty.
     * @return          the empty cells
     */
    public HashSet<Integer> getAvailableMoves () {
        return movesAvailable;
    }

    
//    /**
//     * Check to see who's turn it is.
//     * @return          the player who's turn it is
//     */
//    public int getScoreX () {
//    	scoreX = 0;
//    	for(int i = 0; i < M; i++){
//    		scoreX += winningWindowsX[i] * i * i;
//    	}
//        return scoreX;
//    }
//    
//    /**
//     * Check to see who's turn it is.
//     * @return          the player who's turn it is
//     */
//    public int getScoreO () {
//    	scoreO = 0;
//    	for(int i = 0; i < M; i++){
//    		scoreO += winningWindowsO[i] * i;
//    	}
//        return scoreO;
//    }
    
    /**
     * Check to see who's turn it is.
     * @return          the player who's turn it is
     */
    public void printScoreO () {
    	for(int i = 0; i < M; i++){
    		System.out.print(winningWindowsO[i] + " ");
    	}
    	System.out.println();
    }
    
    /**
     * Check to see who's turn it is.
     * @return          the player who's turn it is
     */
    public void printScoreX () {
    	for(int i = 0; i < M; i++){
    		System.out.print(winningWindowsX[i] + " ");
    	}
    	System.out.println();
    }
    
    
    /**
     * Check to see who's turn it is.
     * @return          the player who's turn it is
     */
    public int getBoardWidth () {
        return BOARD_WIDTH;
    }
    
    /**
     * Check to see who's turn it is.
     * @return          the player who's turn it is
     */
//    public void updateScoreWindow (int col, int row, State player) {
//    	int[] scoreWindow = new int[M + 1];
//    	scoreWindow = (player == State.X) ? this.winningWindowsX : this.winningWindowsO;
//    	State opponent = (player == State.X) ? State.O : State.X;
//    	// Update row
//    	int col_begin = Math.max(0, col - (M - 1));
//    	
//    	for(int i = col_begin; i <= col; i++) {
//    		// winningWindow of (row, i) to (row, i + M - 1)
//    		
//    		int count = 0;   		
//    		
//    		for(int j = 0; j < M && count != -1; j++) {
//    			if(i + j == BOARD_WIDTH || board[row][i + j] == opponent) {
//    				count = -1;
//    			}
//    			else if(board[row][i + j] == player) {
//    				count++;
//    			}
//    			
//    		}
//    		
//    		
//    		if(count > 0) {
//    			scoreWindow[count - 1]--;
//    			scoreWindow[count]++;
//    		}
//    	}
//    	
//    	
//    	
//    	// Update col
//    	int row_begin = Math.max(0, row - (M - 1) );
//    	
//    	for(int i = row_begin; i <= row; i++) {
//    		// winningWindow of (i, col) to (i + M - 1, col)
//    		int count = 0;
//    		for(int j = 0; j < M && count != -1; j++) {
//    			if(i + j == BOARD_WIDTH || board[i + j][col] == opponent) {
//    				count = -1;
//    			}
//    			else if(board[i + j][col] == player) {
//    				count++;
//    			}
//    		}
//    		
//    		if(count > 0) {
//    			scoreWindow[count - 1]--;
//    			scoreWindow[count]++;
//    		}
//    	}
//    	
//    	
//    	// Update diagonal '\'
//    	int d1_row_end = Math.min(row + M - 1, BOARD_WIDTH - 1);
//    	int d1_col_end = Math.min(col + M - 1, BOARD_WIDTH - 1);
//    	
//    	for(int i = row, j = col; i <= d1_row_end && j <= d1_col_end; i++, j++) {
//    		int r = i - (M - 1);
//    		int c = j - (M - 1);
//    		if(r < 0 || c < 0) continue;
//    		
//    		// winningWindow of (r, c) to (i, j)
//    		int count = 0;
//    		for(; r <= i && c <= j && count != -1; r++, c++) {	
//    			if(board[r][c] == opponent) {
//    				count = -1;
//    			}
//    			else if(board[r][c] == player) {
//    				count++;
//    			}
//    		}
//    		
//    		if(count > 0) {
//    			scoreWindow[count - 1]--;
//    			scoreWindow[count]++;
//    		}
//    	}
//    	
//    	
//    	// Update diagonal '/'
//    	int d2_row_end = Math.max(row - (M - 1), 0);
//    	int d2_col_end = Math.min(col + M - 1, BOARD_WIDTH - 1);
//    	
//    	for(int i = row, j = col; i >= d2_row_end && j <= d1_col_end; i--, j++) {
//    		int r = i + M - 1;
//    		int c = j - (M - 1);
//    		if(r >= BOARD_WIDTH || c < 0) continue;
//    		
//    		// winningWindow of (r, c) to (i, j)
//    		int count = 0;
//    		for(; r >= i && c <= j && count != -1; r--, c++) {	
//    			if(board[r][c] == opponent) {
//    				count = -1;
//    			}
//    			else if(board[r][c] == player) {
//    				count++;
//    			}
//    		}
//    		
//    		if(count > 0) {
//    			scoreWindow[count - 1]--;
//    			scoreWindow[count]++;
//    		}
//    	}
//    	   	
//    	
//    	if(player == State.X) {
//    		this.winningWindowsX = scoreWindow;
//    	}
//    	else {
//    		this.winningWindowsO = scoreWindow;
//    	}
//   	
//        
//    }

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
	        if (count >= 5) {
	        	score[0][5 - 1]++;
	        	return;// FIVE
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
	            }
	        }

	    } else if (empty == 1 || empty == count - 1) {
	        //第1个是空位
	        if (count >= 6) {
	        	score[0][5 - 1]++;
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
	                	score[1][2 - 1]--;
	                    return;
	            }
	        }
	    } else if (empty == 2 || empty == count - 2) {
	        //第二个是空位
	        if (count >= 7) {
	        	score[0][5 - 1]++;
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
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
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
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	            }
	        }
	    } else if (empty == 3 || empty == count - 3) {
	        if (count >= 8) {
	        	score[0][5 - 1]++;
	            return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 4:
	                case 5:
	                	score[0][3 - 1]++;
	                	score[0][2 - 1]--;
	                    return;
	                case 6:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 7:
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 7:
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                case 7:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	            }
	        }
	    } else if (empty == 4 || empty == count - 4) {
	        if (count >= 9) {
	        	score[0][5 - 1]++;
	            return;
	        }
	        if (block == 0) {
	            switch (count) {
	                case 5:
	                case 6:
	                case 7:
	                case 8:
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	            }
	        }

	        if (block == 1) {
	            switch (count) {
	                case 4:
	                case 5:
	                case 6:
	                case 7:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	                case 8:
	                	score[0][4 - 1]++;
	                	score[0][3 - 1]--;
	                    return;
	            }
	        }

	        if (block == 2) {
	            switch (count) {
	                case 5:
	                case 6:
	                case 7:
	                case 8:
	                	score[1][4 - 1]++;
	                	score[1][3 - 1]--;
	                    return;
	            }
	        }
	    } else if (empty == 5 || empty == count - 5) {
	    	score[0][5 - 1]++;
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
    
    /**
     * Check the right diagonal to see if there is a winner.
     * @param x     the x coordinate of the most recently played move
     * @param y     the y coordinate of the most recently played move
     */
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
    	
    	if(count_vertical >= M ) {
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
    	
    	if(count_horizontal >= M) {
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
    	
    	if(count_diagonal_left >= M || count_diagonal_right >= M) {
    		winner = playersTurn;
            gameOver = true;
            return;
    	}
    }

    /**
     * Get a deep copy of the Tic Tac Toe board.
     * @return      an identical copy of the board
     */
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
