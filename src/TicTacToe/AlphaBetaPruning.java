package TicTacToe;


public class AlphaBetaPruning {

	private int depth = 0;

	public AlphaBetaPruning(int depth) {

		this.depth = depth;
	}

	public void run(Board board) {
		

		int width = Board.BOARD_LENGTH;

		if (board.getAvailableMoves().size() == width * width) {
			if (width % 2 == 1) {
				board.move(width * width / 2);
				board.setPreMove(width * width / 2);
			} else {
				board.move(width * width / 2 - width / 2 - 1);
				board.setPreMove(width * width / 2 - width / 2 - 1);
			}
		} else {
			alphaBetaPruning(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, board, 0);
		}
	}

	public int alphaBetaPruning(int alpha, int beta, Board board, int curDepth) {
		if (curDepth == this.depth || board.isGameOver()) {
			return this.value(board);
		}
		if (board.getPlayer() == Board.State.O) {
			return getMax(alpha, beta, board, curDepth);
		} else {
			return getMin(alpha, beta, board, curDepth);
		}
	}

	public int getMax(int alpha, int beta, Board board, int curDepth) {
		for (int moveIndex : board.getAvailableMove()) {

			Board copyedBoard = board.deepcopy();
			copyedBoard.move(moveIndex);

			int v = this.alphaBetaPruning(alpha, beta, copyedBoard, curDepth + 1);
			if (v >= beta) {
				board.move(moveIndex);
				return v;
			}
			alpha = Math.max(v, alpha);
		}
		return alpha;
	}

	public int getMin(int alpha, int beta, Board board, int curDepth) {
		for (int moveIndex : board.getAvailableMove()) {

			Board copyedBoard = board.deepcopy();
			copyedBoard.move(moveIndex);

			int v = this.alphaBetaPruning(alpha, beta, copyedBoard, curDepth);
			if (v <= alpha) {
				board.move(moveIndex);
				return v;
			}
			beta = Math.min(v, beta);
		}
		return beta;
	}

	public int value(Board board) {
		if (board.getPlayer() == Board.State.Blank) {
			throw new IllegalArgumentException("Player must be X or O.");
		}

		Board.State opponent = (board.getPlayer() == Board.State.X) ? Board.State.O : Board.State.X;

		if (board.isGameOver() && board.getWinner() == Board.State.O) {
			return Integer.MAX_VALUE;
		} else if (board.isGameOver() && board.getWinner() == Board.State.X) {
			return Integer.MIN_VALUE;
		} else {
			return board.getScore();
		}
	}

}
