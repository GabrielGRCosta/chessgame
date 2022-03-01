package com.chessgame.engine.move;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.piece.*;

public class QueenSideCastleMove extends Move {

	private King oldKing;

	public QueenSideCastleMove(King oldKing, Board board) {
		super(board);
		this.oldKing = oldKing;
	}

        @Override
        public Point getStart() {
                return this.oldKing.getPosition();
        }

        @Override
        public Point getDestination() {
		return oldKing.getAlliance() == Alliance.WHITE ? Point.of(2, 0) : Point.of(2, 7);
        }


	@Override
	public Board execute(Board board) {
		this.validateBoard(board);
		Piece newKing;
		Piece oldKingsRook;
		Piece newKingsRook;
		if (oldKing.getAlliance() == Alliance.WHITE) {
			newKing = oldKing.withPosition(this.getDestination());
			oldKingsRook = board.getPiece(Point.of(0, 0)).orElseThrow();
			newKingsRook = oldKingsRook.withPosition(Point.of(3, 0));
		} else {
			newKing = oldKing.withPosition(this.getDestination());
			oldKingsRook = board.getPiece(Point.of(0, 7)).orElseThrow();
			newKingsRook = oldKingsRook.withPosition(Point.of(3, 7));
		}
		return Board.builder()
			.copyBoard(board)
			.setPiece(newKing)
			.removePiece(oldKing)
			.setPiece(newKingsRook)
			.removePiece(oldKingsRook)
			.setAllianceTurn(board.getAllianceTurn().getOpposite())
			.setWhiteCanKingSideCastle(MoveUtils.calculateWhiteCanKingSideCastle(oldKing, this.board))
                        .setWhiteCanQueenSideCastle(MoveUtils.calculateWhiteCanQueenSideCastle(oldKing, this.board))
                        .setBlackCanKingSideCastle(MoveUtils.calculateBlackCanKingSideCastle(oldKing, this.board))
                        .setBlackCanQueenSideCastle(MoveUtils.calculateBlackCanQueenSideCastle(oldKing, this.board))
			.build();
	}

}
