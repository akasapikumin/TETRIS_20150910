package hideoumada;

//テトリス板オブジェクトを管理する

public class TetrisBoard {

	public static final int w  = MyApplet.boardD.width;
	public static final int h  = MyApplet.boardD.height;

	//横に走査したいので(y,x)の順に設定
	static BoardState[][] board = new BoardState[h][w];

	public static void setBoardState(int ix, int jy, BoardState state){
		board[jy][ix] = state;
	}

	public static BoardState getBoardState(int ix, int jy){
		return board[jy][ix];
	}
	//開始時の状態
	public static void reset(){
		for(int ix = 0; ix < w; ix++){
			board[0][ix] =
			board[h - 1][ix] = BoardState.wall;
		}
		for(int jy = 0; jy < h; jy++){
			board[jy][0] =
			board[jy][w - 1] = BoardState.wall;
		}

		for(int jy = 1; jy < h - 1; jy++){
		for(int ix = 1; ix < w - 1; ix++){
					board[jy][ix] = BoardState.blank;
		}
		}
	}

	//横に走査しないとデバッグがしづらくなる
	public static void debug(){
		for(int jy = 0; jy < h; jy++){
			for(int ix = 0; ix < w; ix++){
				switch(board[jy][ix]){
					case blank: System.out.print("0"); break;
					case block: System.out.print("1"); break;
					case wall : System.out.print("2"); break;
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}
}
