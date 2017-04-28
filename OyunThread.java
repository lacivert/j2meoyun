public class OyunThread extends Thread {

  private boolean duraklasinMi;
  private boolean dursunMu;
  private ZiplaCanvas ziplaCanvas;
  private long sonRefreshZamani;

  OyunThread(ZiplaCanvas canvas) {
    ziplaCanvas = canvas;
  }

  private long getBeklemeZamani() {
    long retVal = 1;
    long difference = System.currentTimeMillis() - sonRefreshZamani;
    if(difference < 50) {
      retVal = 50 - difference;
    }
    return(retVal);
  }

  void oyunuDuraklat() {
    duraklasinMi = true;
  }

  void oyunuDevamEttir() {
    duraklasinMi = false;
    synchronized(this) {
      notify();
    }
  }

  void durIstegi() {
    dursunMu = true;
    synchronized(this) {
      notify();
    }
  }

  public void run() {
    ziplaCanvas.flushKeys();
    dursunMu = false;
    duraklasinMi = false;
    while(true) {
      sonRefreshZamani = System.currentTimeMillis();
      if(dursunMu) {
	break;
      }
      while(duraklasinMi) {
	synchronized(this) {
	  try {
	    wait();
	  } catch(Exception e) {}
	}
      }
      ziplaCanvas.tusKontrol();
      ziplaCanvas.hareketEttir();
      synchronized(this) {
	try {
	  wait(getBeklemeZamani());
	} catch(Exception e) {}
      }
    }
  }

}
