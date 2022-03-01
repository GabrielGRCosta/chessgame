package com.chessgame.engine.piece;

public enum Alliance {

	WHITE("white"), BLACK("black");

	private String string;

	private Alliance(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return this.string;
	}

	public Alliance getOpposite() {
		return this == WHITE ? BLACK : WHITE;
	}

}
