import java.util.Optional;

public class Square {
	public final int x;
	public final int y;
	private Optional<Disc> mayBeDisc;
	
	/**
	 * square を x y discを持った状態に
	 */
	public Square(int x, int y) {
		this(x, y, Optional.empty());
	}
	
	public Square(int x, int y, Disc disc) {
		this(x, y, Optional.of(disc));
	}
	
	public Square(int x, int y, Optional<Disc> mayBeDisc) {
		this.x = x;
		this.y = y;
		this.mayBeDisc = mayBeDisc;
	}
	
	public Pos getPos() {
		return new Pos(x, y);
	}
	
	/**
	 * 石が設定されていたらtrueを返す
	 */
	public boolean hasDisc() {
		return mayBeDisc.isPresent();
	}
	
	/**
	 * 設定されている石が引数と一致したらtrueを返す
	 */
	public boolean hasSameDisc(Disc disc) {
		if (!mayBeDisc.isPresent()) return false;
		Disc currentDisc = mayBeDisc.get();
		return currentDisc.hasSameColor(disc);
	}
	/**
	 * disc の色を変える
	 */
	public void setDisc(Disc disc){
		this.mayBeDisc = Optional.of(disc);
	}
	
	@Override
	public String toString() {
		return mayBeDisc
				.map(it -> it.toString())
				.orElse("＋");
	}
}
