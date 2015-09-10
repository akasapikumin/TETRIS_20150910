package hideoumada;

import java.awt.Color;
import java.awt.Graphics;

//テトリスのブロックオブジェクト
public class Block {
	private int rotPattern;
	private Color blockColor;
	private int[][][]blockForm;
	/*
	 int[x][y][rotMode]blockForm
	 4*4でブロックが存在する場所を表す
	 */

	//回転回数を表すパラメータ
	private int rotMode;
	//ブロックの存在する座標
	private int[]blockPointX = new int[4];
	private int[]blockPointY = new int[4];

	public Block(int rotPattern, Color blockColor, int blockForm[][][]){
		this.rotPattern = rotPattern;
		this.blockColor = blockColor;
		this.blockForm = blockForm;

	}
	//ブロックの存在するx座標を更新
	public void updateBlockPointX(int x){
		for(int i = 0; i < blockPointX.length; i++){
			blockPointX[i] = x + blockForm[rotMode][i][0];
		}
	}
	//ブロックの存在するy座標を更新
	public void updateBlockPointY(int y){
		for(int i = 0; i < blockPointY.length; i++){
			blockPointY[i] = y + blockForm[rotMode][i][1];
		}
	}

	//ブロックの移動・回転が可能かどうか判断するメソッド群
	public boolean moveAbleRight(int x, int y){
		updateBlockPointX(x);
		updateBlockPointY(y);
		for(int i = 0; i < 4; i++){
			if(TetrisBoard.getBoardState
					(blockPointX[i] + 1, blockPointY[i]) == BoardState.blank){
			}else{
				return false;
			}
		}
		return true;
	}
	public boolean moveAbleLeft(int x, int y){
		for(int i = 0; i < 4; i++){
			updateBlockPointX(x);
			updateBlockPointY(y);
			if(TetrisBoard.getBoardState
					(blockPointX[i] - 1, blockPointY[i]) == BoardState.blank){
			}else{
				return false;
			}
		}
		return true;
	}
	public boolean moveAbleDown(int x, int y){
		updateBlockPointX(x);
		updateBlockPointY(y);
		for(int i = 0; i < 4; i++){
			if(TetrisBoard.getBoardState
					(blockPointX[i], blockPointY[i] + 1) == BoardState.blank){
			}else{
				return false;
			}
		}
		return true;
	}
	public boolean rotAble(int x, int y){
		int nextRotMode = (rotMode + 1) % rotPattern;
		for(int i = 0; i < 4; i++){
			try{
				if(TetrisBoard.getBoardState
                        (x + blockForm[nextRotMode][i][0], y + blockForm[nextRotMode][i][1]) == BoardState.blank){
				}else{
					return false;
				}
				//iBlockのはみ出しを無理やり解決
			}catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
		}
		return true;
	}
	//上キーのときの落下 着地点を返す
	public int fallBlock(int x, int y){
		while(moveAbleDown(x, y)){
			y++;
		}
		//着地メソッド
		landingBlock(x, y);
		return y;
	}

	public void resetRotMode() {
		 rotMode = 0;
	}
	public void rotBlock() {
		if(++rotMode == rotPattern){
			rotMode = 0;
		}
	}
	//着地メソッド 着地点のテトリス板を更新
	public void landingBlock(int x, int y){
		updateBlockPointX(x);
		updateBlockPointY(y);
		for(int i = 0; i < 4; i++){
			TetrisBoard.setBoardState
				(blockPointX[i], blockPointY[i], BoardState.block);
		}
	}
	//描画メソッド
	public void paintBlock(Graphics g, int x, int y){
		updateBlockPointX(x);
		updateBlockPointY(y);
		for(int i = 0; i < 4; i++){
			Animation.blockPaint
				(g, blockColor, blockPointX[i], blockPointY[i]);
		}
	}
}
