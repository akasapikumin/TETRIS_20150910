package hideoumada;

import java.awt.Color;
import java.awt.Graphics;

public class Animation {

	public static final int unit = MyApplet.blockD.width;
	public static final int w  = MyApplet.boardD.width;
	public static final int h  = MyApplet.boardD.height;


	public static void allClear(Graphics g){
		g.clearRect(0, 0, w * unit, h * unit);
	}

	//1*1のブロックの描画
	public static void blockPaint(Graphics g,Color cl, int x,int y){
		g.setColor(cl);
		g.fillRect(x * unit, y * unit , unit, unit);
		g.setColor(Color.black);
		g.drawRect(x * unit, y * unit , unit, unit);
	}

	//落ちた後のブロックの描画
	public static void staticBlockPaint(Graphics g,Color cl){
		for(int ix = 1; ix < w; ix++){
		for(int jy = 1; jy < h; jy++){
			if(TetrisBoard.getBoardState(ix,jy) == BoardState.block){
				blockPaint(g, cl, ix, jy);
			}
		}
		}
	}

	//初めのマップを描画する
	public static void mapPaint(Graphics g){
		//枠を作成
		g.setColor(Color.gray);

		g.fillRect(0, 0, unit, h * unit);
		g.fillRect((w - 1) * unit , 0, unit, h * unit);

		g.fillRect(0, 0, w * unit, unit);
		g.fillRect(0, (h - 1) * unit, w * unit, unit);

		//マス目を作成
		for(int i=1; i< w - 1; i++){
			g.drawLine(i * unit, 0, i * unit, (h - 1) * unit);
		}
		for(int j=1; j< h - 1; j++){
			g.drawLine(0, j * unit, (w - 1) * unit,  j * unit);
		}

		//ブロックが落ちてくる場所を明確化
		g.setColor(Color.black);
		g.fillRect(4 * unit, 0, 4 * unit, unit);
	}
}

