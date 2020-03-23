package TicTacToe;

public class AlphaBetaPruning {
	
	private int depth = 0;
	
	public AlphaBetaPruning(int depth) {
	
		this.depth = depth;
	}
	
	
	
	public void run() {
		
	}
	
	public int getMax(int alpha, int beta, Board board, int curDepth) {
		if(curDepth == this.depth) {
			return this.value(board);
		}
		
		for(int moveIndex : board.getAvailableMove()) {

			Board copyedBoard = board.deepcopy();
			copyedBoard.move(moveIndex);
			
			int v = this.getMin(alpha, beta, copyedBoard, curDepth + 1);
			if(v >= beta) {
				return v;
			}
			alpha = Math.max(v, alpha);
			
		}
		return alpha;
	}
	
	public int getMin(int alpha, int beta, Board board, int curDepth) {
		if(curDepth == this.depth) {
			return this.value(board);
		}
		
		for(int moveIndex : board.getAvailableMove()) {
			
			Board copyedBoard = board.deepcopy();
			copyedBoard.move(moveIndex);

			int v = this.getMax(alpha, beta, copyedBoard, curDepth + 1);
			if(v <= alpha) {
				return v;
			}
			beta = Math.min(v, beta);
		}
		
		
		return beta;
	}
	
	public int value(Board board) {
		
		
		
		return 0;
	}
	
	
	
}
