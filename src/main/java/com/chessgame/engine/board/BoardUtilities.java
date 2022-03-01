package com.chessgame.engine.board;

import com.chessgame.engine.Point;
import com.chessgame.engine.piece.*;
import com.chessgame.engine.move.Move;


import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class BoardUtilities {

	private BoardUtilities() {
		//NO!
	}

	public static final Board INITIAL_POSITION =
		Board.builder()
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(0, 1)))
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(1, 1)))
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(2, 1)))
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(3, 1)))
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(4, 1)))
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(5, 1)))
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(6, 1)))
		     .setPiece(new Pawn(Alliance.WHITE, Point.of(7, 1)))
		     .setPiece(new Rook(Alliance.WHITE, Point.of(0, 0)))
		     .setPiece(new Knight(Alliance.WHITE, Point.of(1, 0)))
		     .setPiece(new Bishop(Alliance.WHITE, Point.of(2, 0)))
		     .setPiece(new Queen(Alliance.WHITE, Point.of(3, 0)))
		     .setPiece(new King(Alliance.WHITE, Point.of(4, 0)))
		     .setPiece(new Bishop(Alliance.WHITE, Point.of(5, 0)))
		     .setPiece(new Knight(Alliance.WHITE, Point.of(6, 0)))
		     .setPiece(new Rook(Alliance.WHITE, Point.of(7, 0)))
		     .setAllianceTurn(Alliance.WHITE)
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(0, 6)))
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(1, 6)))
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(2, 6)))
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(3, 6)))
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(4, 6)))
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(5, 6)))
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(6, 6)))
		     .setPiece(new Pawn(Alliance.BLACK, Point.of(7, 6)))
		     .setPiece(new Rook(Alliance.BLACK, Point.of(0, 7)))
		     .setPiece(new Knight(Alliance.BLACK, Point.of(1, 7)))
		     .setPiece(new Bishop(Alliance.BLACK, Point.of(2, 7)))
		     .setPiece(new Queen(Alliance.BLACK, Point.of(3, 7)))
		     .setPiece(new King(Alliance.BLACK, Point.of(4, 7)))
		     .setPiece(new Bishop(Alliance.BLACK, Point.of(5, 7)))
		     .setPiece(new Knight(Alliance.BLACK, Point.of(6, 7)))
		     .setPiece(new Rook(Alliance.BLACK, Point.of(7, 7)))
		     .setWhiteCanKingSideCastle(true)
		     .setWhiteCanQueenSideCastle(true)
		     .setBlackCanKingSideCastle(true)
		     .setBlackCanQueenSideCastle(true)
		     .build();

	public static boolean isValidCoordinate(Point coordinate) {
		return coordinate.x >= 0 && coordinate.x < 8 &&
		       coordinate.y >= 0 && coordinate.y < 8;
	}

	public static boolean squareIsAttackedByAlliance(Point position, Alliance alliance, Board board) {
		return board.getPiecesOfAlliance(alliance)
			.stream()
			.flatMap(p -> p.calculatePseudoLegalMoves(board).stream())
			.filter(m -> m.getDestination().equals(position))
			.findAny()
			.isPresent();
	}

	public static boolean kingIsInCheck(Alliance kingAlliance, Board board) {
		Piece king = board.getKing(kingAlliance);
		return board.getPiecesOfAlliance(kingAlliance.getOpposite())
			.stream()
			.flatMap(p -> p.calculatePseudoLegalMoves(board).stream()) //MUST BE CALCULATEPSEUDOLEGALMOVES!!
			.filter(m -> m.getDestination().equals(king.getPosition()))
			.findAny()
			.isPresent();
	}

}
