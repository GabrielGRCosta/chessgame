package com.chessgame.engine.move;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.piece.Piece;

public class CaptureMove extends Move {

	private Point start;
        private Point destination;

        public CaptureMove(Point start, Point destination, Board board) {
                super(board);
                this.start = start;
                this.destination = destination;
        }

        @Override
        public Point getStart() {
                return this.start;
        }

        @Override
        public Point getDestination() {
                return this.destination;
        }


	@Override
	public Board execute(Board board) {
		this.validateBoard(board);
		Piece oldPiece = this.board.getPiece(this.start).orElseThrow();
		Piece newPiece = oldPiece.withPosition(this.destination);
		Piece enemyPiece = this.board.getPiece(this.destination).orElseThrow();
		return Board.builder()
			.copyBoard(this.board)
			.removePiece(enemyPiece)
			.setPiece(newPiece)
			.removePiece(oldPiece)
			.setAllianceTurn(this.board.getAllianceTurn().getOpposite())
			.setWhiteCanKingSideCastle(MoveUtils.calculateWhiteCanKingSideCastle(oldPiece, this.board))
			.setWhiteCanQueenSideCastle(MoveUtils.calculateWhiteCanQueenSideCastle(oldPiece, this.board))
			.setBlackCanKingSideCastle(MoveUtils.calculateBlackCanKingSideCastle(oldPiece, this.board))
			.setBlackCanQueenSideCastle(MoveUtils.calculateBlackCanQueenSideCastle(oldPiece, this.board))
			.build();
	}

}
