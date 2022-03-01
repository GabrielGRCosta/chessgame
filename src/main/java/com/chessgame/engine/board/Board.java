package com.chessgame.engine.board;

import com.chessgame.engine.Point;
import com.chessgame.engine.piece.Alliance;
import com.chessgame.engine.piece.King;
import com.chessgame.engine.piece.Piece;

import java.util.Optional;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Board {

	private Map<Point,Piece> livePieces;
	private Alliance allianceTurn;
	private boolean whiteCanKingSideCastle;
	private boolean whiteCanQueenSideCastle;
	private boolean blackCanKingSideCastle;
	private boolean blackCanQueenSideCastle;
	//private Optional<Point> enPassantSquare;
	//private int fullMoveNumber;

	private Board(Builder builder) {
		this.livePieces = builder.livePieces;
		this.allianceTurn = builder.allianceTurn;
		this.whiteCanKingSideCastle = builder.whiteCanKingSideCastle;
		this.whiteCanQueenSideCastle = builder.whiteCanQueenSideCastle;
		this.blackCanKingSideCastle = builder.blackCanKingSideCastle;
		this.blackCanQueenSideCastle = builder.blackCanQueenSideCastle;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Optional<Piece> getPiece(Point coordinate) {
		if (BoardUtilities.isValidCoordinate(coordinate)) {
			return Optional.ofNullable(livePieces.get(coordinate));
		} else {
			throw new RuntimeException("point out of board bounds");
		}
	}

	public Piece getKing(Alliance alliance) {
		return this.livePieces.values().stream()
			.filter(p -> p.getClass() == King.class)
			.filter(k -> k.getAlliance() == alliance)
			.findAny()
			.orElseThrow(() -> new RuntimeException("board is missing a king"));
	}

	public Collection<Piece> getPiecesOfAlliance(Alliance alliance) {
		return this.livePieces.values()
			.stream()
			.filter(p -> p.getAlliance() == alliance)
			.collect(Collectors.toList());
	}

	public Alliance getAllianceTurn() {
		return this.allianceTurn;
	}

	public boolean getWhiteCanKingSideCastle() {
		return this.whiteCanKingSideCastle;
	}

	public boolean getWhiteCanQueenSideCastle() {
		return this.whiteCanQueenSideCastle;
	}

	public boolean getBlackCanKingSideCastle() {
		return this.blackCanKingSideCastle;
	}

	public boolean getBlackCanQueenSideCastle() {
		return this.blackCanQueenSideCastle;
	}

	public static class Builder {

		private Map<Point,Piece> livePieces;
		private Alliance allianceTurn;
		private boolean whiteCanKingSideCastle;
		private boolean whiteCanQueenSideCastle;
		private boolean blackCanKingSideCastle;
		private boolean blackCanQueenSideCastle;

		private Builder() {
			this.livePieces = new HashMap<>();
			this.allianceTurn = Alliance.WHITE;
			this.whiteCanKingSideCastle = true;
			this.whiteCanQueenSideCastle = true;
			this.blackCanKingSideCastle = true;
			this.blackCanQueenSideCastle = true;
		}

		public Builder copyBoard(Board board) {
			this.livePieces.clear();
			board.livePieces.forEach((k, v) -> this.livePieces.put(k, v));
			this.allianceTurn = board.allianceTurn;
			this.whiteCanKingSideCastle = board.whiteCanKingSideCastle;
			this.whiteCanQueenSideCastle = board.whiteCanQueenSideCastle;
			this.blackCanKingSideCastle = board.blackCanKingSideCastle;
			this.blackCanQueenSideCastle = board.blackCanQueenSideCastle;
			return this;
		}

		public Builder setPiece(Piece piece) {
			this.livePieces.put(piece.getPosition(), piece);
			return this;
		}

		public Builder removePiece(Piece piece) {
			this.livePieces.remove(piece.getPosition());
			return this;
		}

		public Builder setAllianceTurn(Alliance alliance) {
			this.allianceTurn = alliance;
			return this;
		}

		public Builder setWhiteCanKingSideCastle(boolean whiteCanKingSideCastle) {
			this.whiteCanKingSideCastle = whiteCanKingSideCastle;
			return this;
		}

		public Builder setWhiteCanQueenSideCastle(boolean whiteCanQueenSideCastle) {
			this.whiteCanQueenSideCastle = whiteCanQueenSideCastle;
			return this;
		}

		public Builder setBlackCanKingSideCastle(boolean blackCanKingSideCastle) {
			this.blackCanKingSideCastle = blackCanKingSideCastle;
			return this;
		}

		public Builder setBlackCanQueenSideCastle(boolean blackCanQueenSideCastle) {
			this.blackCanQueenSideCastle = blackCanQueenSideCastle;
			return this;
		}

		public Board build() {
			return new Board(this);
		}

	}

}
