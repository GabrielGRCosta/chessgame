package com.chessgame.engine.piece;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.move.Move;

import java.util.Collection;

public abstract class Piece {

	private Alliance alliance;
	private Point position;

	protected Piece(Alliance alliance, Point position) {
		this.alliance = alliance;
		this.position = position;
	}

	@Override
	public abstract String toString();

	public abstract Collection<Move> calculatePseudoLegalMoves(Board board);

	public abstract Collection<Move> calculateLegalMoves(Board board);

	public abstract Piece withPosition(Point newPosition);

	public Alliance getAlliance() {
		return this.alliance;
	}

	public Point getPosition() {
		return this.position;
	}

}
