package io.github.mitarashi87.othello;

public abstract class Player {
	private final String icon;
	private String discIcon;



	public Player(String discIcon) {
		this.icon = discIcon;
	}

	public Disc playDisc() {
		return new Disc(discIcon);
	}

	public String getIcon() {
		return this.icon;
	}

	public abstract Pos playPos();

	public abstract void receiveMessage(String message);

}
