package hideoumada;

import java.awt.Color;

/*

****

**
**

 *
***

*
***

  *
***

 **
**

**
 **

*/

public class BlockSelecter {

	private static final int[][][][] blockForm =
		{
			{
				//iBlock
				{ {0,0},{1,0},{2,0},{3,0} },
				{ {2,0},{2,1},{2,2},{2,3} },
			},
			{
				//oBlock
				{ {0,0},{0,1},{1,1},{1,0} }
			},
			{
				//tBlock
				{ {0,1},{1,0},{1,1},{1,2} },
				{ {0,1},{1,0},{1,1},{2,1} },
				{ {0,0},{0,1},{0,2},{1,1} },
				{ {0,0},{1,0},{1,1},{2,0} },
			},
			{
				//jBlock
				{ {0,0},{1,0},{1,1},{1,2} },
				{ {0,1},{1,1},{2,0},{2,1} },
				{ {0,0},{0,1},{0,2},{1,2} },
				{ {0,0},{0,1},{1,0},{2,0} },
			},
			{
				//lBlock
				{ {0,2},{1,0},{1,1},{1,2} },
				{ {0,0},{0,1},{1,1},{2,1} },
				{ {0,0},{0,1},{0,2},{1,0} },
				{ {0,0},{1,0},{2,0},{2,1} },
			},
			{
				//sBlock
				{ {0,1},{0,2},{1,0},{1,1} },
				{ {0,0},{1,0},{1,1},{2,1} },
			},
			{
				//zBlock
				{ {0,0},{0,1},{1,1},{1,2} },
				{ {0,1},{1,0},{1,1},{2,0} },
			},
		};

	//各ブロックを定義
	private static final Block iBlock = new Block(2, Color.cyan,	blockForm[0]);
	private static final Block oBlock = new Block(1, Color.yellow,	blockForm[1]);
	private static final Block tBlock = new Block(4, Color.pink,	blockForm[2]);
	private static final Block jBlock = new Block(4, Color.blue,	blockForm[3]);
	private static final Block lBlock = new Block(4, Color.orange,	blockForm[4]);
	private static final Block sBlock = new Block(2, Color.green,	blockForm[5]);
	private static final Block zBlock = new Block(2, Color.red,		blockForm[6]);

	//現在のブロックを返す
	public static Block getNowBlock(int nowBlock){
		Block block = null;
		switch(nowBlock){
		case 1: block = iBlock; break;
		case 2: block = oBlock; break;
		case 3: block = tBlock; break;
		case 4: block = jBlock; break;
		case 5: block = lBlock; break;
		case 6: block = sBlock; break;
		case 7: block = zBlock; break;
		}
		return block;
	}
}
