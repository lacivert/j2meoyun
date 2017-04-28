import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Adam extends Sprite {

  static final int EN = 32;
  static final int BOY = 48;

  static final int[] FRAME_SEQUENCE = { 3, 2, 1, 2 };

  private int myInitialX;
  private int myInitialY;

  private int myNoJumpInt = -6;

  private int myIsJumping = myNoJumpInt;

  private int myScoreThisJump = 0;

  public Adam(int initialX, int initialY) throws Exception {
    super(Image.createImage("/resimler/cowboy.png"), EN, BOY);
    myInitialX = initialX;
    myInitialY = initialY;
    defineReferencePixel(EN/2, 0);
    setRefPixelPosition(myInitialX, myInitialY);
    setFrameSequence(FRAME_SEQUENCE);
  }

  int checkCollision(Engel engel) {
    int retVal = 0;
    if(collidesWith(engel, true)) {
      retVal = 1;
      engel.reset();
    }
    return(retVal);
  }

  void reset() {
    myIsJumping = myNoJumpInt;
    setRefPixelPosition(myInitialX, myInitialY);
    setFrameSequence(FRAME_SEQUENCE);
    myScoreThisJump = 0;
    setTransform(TRANS_NONE);
  }

  void hareketEttir(int tickCount, boolean left) {
    if(left) {
      setTransform(TRANS_MIRROR);
      move(-1, 0);
    } else {
      setTransform(TRANS_NONE);
      move(1, 0);
    }
    if(tickCount % 3 == 0) { 
      if(myIsJumping == myNoJumpInt) {
	nextFrame();
      } else {
	myIsJumping++;
	if(myIsJumping < 0) {
	  setRefPixelPosition(getRefPixelX(), getRefPixelY() - (2<<(-myIsJumping)));
	} else {
	  if(myIsJumping != -myNoJumpInt - 1) {
	    setRefPixelPosition(getRefPixelX(), getRefPixelY() + (2<<myIsJumping));
	  } else {
	    myIsJumping = myNoJumpInt;
	    setRefPixelPosition(getRefPixelX(), myInitialY);
	    setFrameSequence(FRAME_SEQUENCE);
	    myScoreThisJump = 0;
	  }
	}
      }
    }
  }

  void zipla() {
    if(myIsJumping == myNoJumpInt) {
      myIsJumping++;
      setFrameSequence(null);
      setFrame(0);
    }
  }

  int puaniArttir() {
    if(myScoreThisJump == 0) {
      myScoreThisJump++;
    } else {
      myScoreThisJump *= 2;
    }
    return(myScoreThisJump);
  }
}
