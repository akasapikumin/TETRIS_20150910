package hideoumada;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;


public class MyApplet extends Applet
		implements Runnable, ActionListener, KeyListener{

	private boolean resetFlag;

	//keyboard flag
	private boolean shift,up,down,right,left;

	//座標
	public int x,y;

	private static int counter;

	private int[] deleteLine = new int[5];	//同時に消えるラインの数 + 番兵

	private Block nowBlock;	//今動かせるブロックを記録

	//java.awt.*
	private Image offImage;	//テトリス板の画像
	private Thread thread;
	private Button resetBt;

	//dimension
	public static final Dimension blockD = new Dimension(20, 20);		//1マスのピクセル数
	public static final Dimension defD = new Dimension(50, 50);			//余白のピクセル数
	private static final Dimension appletD = new Dimension(400, 600);	//アップレットのピクセル数
	public static final Dimension boardD = new Dimension(12, 22);		//マス目 12*22

	//現在のゲームシーンを記録
	private GameScene gameScene;

	public void init() {
		setLayout(null);
		setSize(appletD);

		this.addKeyListener(this);

		resetBt = new Button("reset");
		add(resetBt);
		resetBt.setBounds(180, 560, 40, 15);
		resetBt.addActionListener(this);

		offImage = createImage(appletD.width, appletD.height);

		gameScene = GameScene.main;		//開始時からテトリスがスタートする（menuをつくってもよい）
		nowBlock = BlockSelecter.getNowBlock(getNumber());			//次のブロックを取得
		positionReset();				//座標リセット
		TetrisBoard.reset();
	}

	//1-7(ブロックの種類）までの乱数にする
	private int getNumber(){
		Random rnd = new Random();
		return rnd.nextInt(7) + 1;
	}
	private void positionReset(){
		x = 4; y = 1;
	}

	public void start(){
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	public void stop() {
		thread = null;
	}
	public void run(){
		while (thread == Thread.currentThread()) {
			try {
				Thread.sleep(100);//適正100前後
			} catch (InterruptedException e) {}
			requestFocusInWindow();

			//基本はgameMainでループ
			switch(gameScene){
			case main: gameMain(); break;
			case gameOver: gameOver(); break;
			}
		}
	}

	//100msに1回gameMainが呼ばれる
	public void gameMain(){
		//リセットタイミングをsleep通過後にしたがエラーが消えない
		if(resetFlag){
			resetAction();
			try {
				Thread.sleep(50);	//チャタリング軽減
			} catch (InterruptedException e) {}
			resetFlag = false;
		}

		//keyflagに対する処理
		if(shift && nowBlock.rotAble(x, y)){		//回転
			nowBlock.rotBlock();
		}else
		if(up){										//瞬間落下
			//y座標を着地点に更新
			y = nowBlock.fallBlock(x, y);
			landing2next(x, y);
			counter = -1;
		}else
		if(down && nowBlock.moveAbleDown(x, y)){	//下移動
			y++;
		}else
		if(right && nowBlock.moveAbleRight(x, y)){	//右移動
			x++;
		}else
		if(left && nowBlock.moveAbleLeft(x, y)){	//左移動
			x--;
		}
		//100*5msに1回落下判定がなされる
		if(count()){
			//着地時の処理
			if(!nowBlock.moveAbleDown(x, y)){
				nowBlock.landingBlock(x, y);	//着地処理
				landing2next(x, y);
			}else{
				y++;//落下
			}
		}

		Graphics g = offImage.getGraphics();
		Animation.allClear(g);
		Animation.mapPaint(g);
		nowBlock.paintBlock(g, x, y);
		Animation.staticBlockPaint(g, Color.DARK_GRAY);

		repaint();
	}

	private void resetAction(){
		nowBlock.resetRotMode();							//回転状況の初期化
		nowBlock = BlockSelecter.getNowBlock(getNumber());	//次のブロックを取得
		positionReset();									//座標リセット
	}

	//着地から次のブロックに移行する際のメソッド
	private void landing2next(int x, int y){

		//消去列があるかどうかの判定
		if(jedgeLineDelete()){
			lineDeleteProcess();
		}
		//ゲームオーバーかどうかの判定
		if(jedgeGameOver()){
			gameScene = GameScene.gameOver;
		}
		resetFlag = true;
	}

	public boolean jedgeLineDelete(){
		boolean flag = false;
		int n = 0;
		for(int jy = 0; jy < 4; jy++){
			int stack = 0;
			for(int ix = 1; ix < boardD.width - 1; ix++){
				if(y + jy < boardD.height - 1){
					if(TetrisBoard.getBoardState(ix, y + jy) == BoardState.blank){
						break;
					}
					if(stack++ == boardD.width - 3){
						deleteLine[n++] = y + jy;
						flag = true;
					}
				}
			}
		}
		return flag;
	}
	private void lineDeleteProcess() {
		int n = 0;
		while(deleteLine[n] != 0){
			for(int jy = deleteLine[n]; jy >= 2; jy--){
				for(int ix = 1;ix < boardD.width - 1;ix++){
					//一個上の行を下にずらす
					TetrisBoard.setBoardState(ix, jy, TetrisBoard.getBoardState(ix, jy - 1));
				}
			}
			//一番上の行は状態をblankにする
			for(int ix = 1; ix < boardD.width - 1; ix++){
				TetrisBoard.setBoardState(ix, 1,BoardState.blank);
			}
			deleteLine[n] = 0;
			n++;
		}
	}

	public boolean jedgeGameOver(){
		for(int i = 3; i < 8; i++){
			if(TetrisBoard.getBoardState(i, 1) == BoardState.blank){
			}else{
				return true;
			}
		}
		return false;
	}
	public void gameOver(){

		Graphics g = offImage.getGraphics();
		g.drawString("Game Over", 180, 520);

		repaint();
	}

	//落下処理用にタイミングを遅らせる
	private boolean count(){
		if(counter++ == 4){
			counter = 0;
			return true;
		}
		return false;
	}
	//オーバーライド
	public void update(Graphics g){
		paint(g);
	}
	public void paint(Graphics g){
		g.drawImage(offImage, defD.width, defD.width, this);	//余白を開けて描画
	}

	//resetBtの動き
	public void actionPerformed(ActionEvent e) {
		resetAction();
		gameScene = GameScene.main;
		TetrisBoard.reset();
	}

	//入力キーに関するオペレーション
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_SHIFT:shift = true;break;
		case KeyEvent.VK_UP:up = true;break;
		case KeyEvent.VK_DOWN:down = true;break;
		case KeyEvent.VK_RIGHT:right = true;break;
		case KeyEvent.VK_LEFT:left = true;break;
		}
	}
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_SHIFT:
			shift = false;
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		}
	}
	//実装しない抽象メソッド
	public void keyTyped(KeyEvent e) {}


}
