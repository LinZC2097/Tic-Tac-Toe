package TicTacToe;


import TicTacToe.Board.State;;

class AlphaBetaAdvanced {
	private static int maxPlay = 10;
    private static int play = 2;
    private static int defaultPlay = 2;
    private static int deepening = 2;

    private AlphaBetaAdvanced() {}

    static void run (Board.State player, Board board) {

        if (play < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }

        //deepening++;
        /*
        
        if(maxPly < 4) {
        	if(deepening == 6) {
        		maxPly++;
        		System.out.println("Depth changes to " + maxPly);
        	}
        	if(deepening == 10) {
        		maxPly++;
        		System.out.println("Depth changes to " + maxPly);
        	}
        }
        */
        
        int width = board.getBoardWidth();
        
        if(board.getAvailableMoves().size() == width * width) {
        	if(width % 2 == 1) {
        		board.move(width * width / 2);
//        		board.setPreMove(width * width / 2);
        	}
        	else {
        		board.move(width * width / 2 - width / 2 - 1);
//        		board.setPreMove(width * width / 2 - width / 2 - 1);
        	}
        }
        else {        
        	alphaBetaPruning(player, board, Double.NEGATIVE_INFINITY + 100, Double.POSITIVE_INFINITY - 100, 0);
        }
    }

    private static int alphaBetaPruning (Board.State player, Board board, double alpha, double beta, int currentPly) {
    	if(board.getMoveCount() > 10) {
	    	if(play < maxPlay && (board.getFour(player) > 2 || (board.getFive(player) > 1 && board.getFive(player) > 1)) 
	    			|| board.getBlockFour(player) > 2 || board.getThree(player) > 2) {
	    		play += deepening;
	    	}else {
	    		play = defaultPlay;
	    	}
    	}
    	
        if (currentPly++ >= play || board.isGameOver()) {
        	return evaluate(player, board, currentPly);
        }
        
        if (board.getTurn() == player) {
            return getMax(player, board, alpha, beta, currentPly);
        } else {
            return getMin(player, board, alpha, beta, currentPly);
        }
    }

    private static int getMax (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;
        
        for (Integer theMove : board.getAvailableMoves()) {
            Board modifiedBoard = board.getDeepCopy();
            
            if(modifiedBoard.isUseless(theMove)) continue;
            
            modifiedBoard.move(theMove);
            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);
            //System.out.println("Player O moves at (" + theMove / board.getBoardWidth() + "," + theMove % board.getBoardWidth() + "), score = " + score);
            
            
            if (score > alpha) {
                alpha = score;
                indexOfBestMove = theMove;
            }

            if (alpha >= beta) {
                break;
            }
        }
        
        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
//             !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//            board.clearWinningWindows();
            
            if(currentPly == 1) {
            	System.out.println("Player O moves at (" + indexOfBestMove % board.getBoardWidth() + "," + indexOfBestMove / board.getBoardWidth() + "), alpha = " + alpha + ",beta = " + beta);
//            	board.setPreMove(indexOfBestMove);
            }
        }
        
        return (int)alpha;
    }

    private static int getMin (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;
        
        for (Integer theMove : board.getAvailableMoves()) {

            Board modifiedBoard = board.getDeepCopy();
            
            if(modifiedBoard.isUseless(theMove)) continue;
            
            modifiedBoard.move(theMove);
            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);
            
            //System.out.println("Player X moves at (" + theMove / board.getBoardWidth() + "," + theMove % board.getBoardWidth() + "), score = " + score);
            
            if (score < beta) {
                beta = score;
                indexOfBestMove = theMove;
            }

            if (alpha >= beta) {
                break;
            }
        }
        

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
//             !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//            board.clearWinningWindows();
            
            if(currentPly == 1) {
            	System.out.println("Player X moves at (" + indexOfBestMove % board.getBoardWidth() + "," + indexOfBestMove / board.getBoardWidth() + "), alpha = " + alpha + ",beta = " + beta);
//            	board.setPreMove(indexOfBestMove);
            }
        }
        return (int)beta;
    }


    private static int evaluate (Board.State player, Board board, int currentPly) {

        if (player == Board.State.Blank) {
            throw new IllegalArgumentException("Player must be X or O.");
        }

        Board.State opponent = (player == Board.State.X) ?  Board.State.O : Board.State.X;
        
        if (board.isGameOver() && board.getWinner() != player) {
//        	System.out.println(board.toString());
//        	System.out.println(currentPly);
            return Integer.MIN_VALUE;
        } else if (board.isGameOver() && board.getWinner() == player) {
//        	System.out.println(board.toString());
//        	System.out.println(currentPly);
        	return Integer.MAX_VALUE ;
        } else if(player == State.O) {
        	return board.getScoreO() - board.getScoreX();
        }else {
        	return board.getScoreX() - board.getScoreO();
        }
    }

}
