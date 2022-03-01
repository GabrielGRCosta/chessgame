package com.chessgame.engine.move;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;

public abstract class Move {

	protected Board board;

	protected Move(Board board) {
		this.board = board;
	}

	public abstract Point getStart();

	public abstract Point getDestination();

	public abstract Board execute(Board board);

	protected void validateBoard(Board board) {
		if (this.board != board)
			throw new RuntimeException("board is different from the which the move was calculated for");
	}

}
