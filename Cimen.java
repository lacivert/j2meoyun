import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Cimen extends TiledLayer {

  static final int TILE_WIDTH = 20;
  static final int[] FRAME_SEQUENCE = { 2, 3, 2, 4 };
  static int COLUMNS;
  static final int CYCLE = 3;
  static int TOP_Y;
  private int mySequenceIndex = 0;
  private int myAnimatedTileIndex;
  static int setColumns(int screenWidth) {
    COLUMNS = ((screenWidth / 20) + 1)*3;
    return(COLUMNS);
  }
  public Cimen() throws Exception {
    super(setColumns(ZiplaCanvas.DISP_WIDTH), 1, 
	  Image.createImage("/resimler/grass.png"), 
	  TILE_WIDTH, TILE_WIDTH);
    TOP_Y = ZiplamaYoneticisi.DISP_HEIGHT - TILE_WIDTH;
    setPosition(0, TOP_Y);
    myAnimatedTileIndex = createAnimatedTile(2);
    for(int i = 0; i < COLUMNS; i++) {
      if((i % CYCLE == 0) || (i % CYCLE == 2)) {
	setCell(i, 0, myAnimatedTileIndex);
      } else {
	setCell(i, 0, 1);
      }
    }
  }
  void reset() {
    setPosition(-(TILE_WIDTH*CYCLE), TOP_Y);
    mySequenceIndex = 0;
    setAnimatedTile(myAnimatedTileIndex, FRAME_SEQUENCE[mySequenceIndex]);
  }
  void hareketEttir(int tickCount) {
    if(tickCount % 2 == 0) { 
      mySequenceIndex++;
      mySequenceIndex %= 4;
      setAnimatedTile(myAnimatedTileIndex, FRAME_SEQUENCE[mySequenceIndex]);
    }
  }
}
