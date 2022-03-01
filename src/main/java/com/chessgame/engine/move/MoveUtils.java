package com.chessgame.engine.move;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.piece.Alliance;
import com.chessgame.engine.piece.King;
import com.chessgame.engine.piece.Piece;
import com.chessgame.engine.piece.Rook;

public class MoveUtils {

	public static boolean calculateWhiteCanKingSideCastle(Piece piece, Board board) {
		Point kingsRookInitialPosition = Point.of(7, 0);
		if (piece.getAlliance() != Alliance.WHITE) {
			return board.getWhiteCanKingSideCastle();
		} else if (piece.getClass() == King.class) {
			return false;
		} else if (piece.getClass() == Rook.class && piece.getPosition().equals(kingsRookInitialPosition)) {
			return false;
		} else {
			return board.getWhiteCanKingSideCastle();
		}
	}

	public static boolean calculateWhiteCanQueenSideCastle(Piece piece, Board board) {
		Point queensRookInitialPosition = Point.of(0, 0);
		if (piece.getAlliance() != Alliance.WHITE) {
			return board.getWhiteCanQueenSideCastle();
		} else if (piece.getClass() == King.class) {
			return false;
		} else if (piece.getClass() == Rook.class && piece.getPosition().equals(queensRookInitialPosition)) {
			return false;
		} else {
			return board.getWhiteCanQueenSideCastle();
		}
	}

	public static boolean calculateBlackCanKingSideCastle(Piece piece, Board board) {
		Point kingsRookInitialPosition = Point.of(7, 7);
		if (piece.getAlliance() != Alliance.BLACK) {
			return board.getBlackCanKingSideCastle();
		} else if (piece.getClass() == King.class) {
			return false;
		} else if (piece.getClass() == Rook.class && piece.getPosition().equals(kingsRookInitialPosition)) {
			return false;
		} else {
			return board.getBlackCanKingSideCastle();
		}
	}

	public static boolean calculateBlackCanQueenSideCastle(Piece piece, Board board) {
		Point queensRookInitialPosition = Point.of(0, 7);
		if (piece.getAlliance() != Alliance.BLACK) {
			return board.getBlackCanQueenSideCastle();
		} else if (piece.getClass() == King.class) {
			return false;
		} else if (piece.getClass() == Rook.class && piece.getPosition().equals(queensRookInitialPosition)) {
			return false;
		} else {
			return board.getBlackCanQueenSideCastle();
		}
	}

}
