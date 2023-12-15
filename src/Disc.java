
public class Disc {
	private String icon;
	public Disc(String icon) {
		this.icon = icon;
	}
	
	/**
	 * 同じ色と見做せるならtrueを返す
	 */
	public boolean hasSameColor(Disc disc) {
		return this.icon.equals(disc.icon);
	}
	
	@Override
	public String toString() {
		return icon;
	}

}
