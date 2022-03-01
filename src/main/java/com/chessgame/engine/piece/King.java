package com.chessgame.engine.piece;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.board.BoardUtilities;
import com.chessgame.engine.move.*;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class King extends Piece {

        private static final List<Point> CANDIDATE_DESTINATIONS_OFFSETS = List.of(
                Point.of(0, 1), Point.of(0, -1), Point.of(1, 0), Point.of(-1, 0),
                Point.of(1, 1), Point.of(-1, -1), Point.of(1, -1), Point.of(-1, 1)
        );

        private static final List<Point> WHITES_KING_SIDE_CASTLE_PATH = List.of(
                Point.of(4, 0), Point.of(5, 0), Point.of(6, 0)
        );

        private static final List<Point> WHITES_QUEEN_SIDE_CASTLE_PATH = List.of(
                Point.of(4, 0), Point.of(3, 0), Point.of(2, 0), Point.of(1, 0)
        );

        private static final List<Point> BLACKS_KING_SIDE_CASTLE_PATH = List.of(
                Point.of(4, 7), Point.of(5, 7), Point.of(6, 7)
        );

        private static final List<Point> BLACKS_QUEEN_SIDE_CASTLE_PATH = List.of(
                Point.of(4, 7), Point.of(3, 7), Point.of(2, 7), Point.of(1, 7)
        );

	public King(Alliance alliance, Point position) {
		super(alliance, position);
	}

	private Collection<Move> calculateCastlingMoves(Board board) { //what a beast!
		List<Move> castleMoves = new LinkedList<>();
		if (BoardUtilities.kingIsInCheck(this.getAlliance(), board))
			return castleMoves;

		Runnable addKingSideCastleMove = () -> castleMoves.add(new KingSideCastleMove(this, board));
		Runnable addQueenSideCastleMove = () -> castleMoves.add(new QueenSideCastleMove(this, board));
		Alliance enemyAlliance = this.getAlliance().getOpposite();
		Predicate<List<Point>> pathIsObstructed = path ->
			path.stream()
				.skip(1)
				.flatMap(s -> board.getPiece(s).stream())
				.findAny()
				.isEmpty();
		BiConsumer<List<Point>, Runnable> ifPathIsSafeThenRun = (path, runnable) ->
			path.stream()
				.filter(s -> BoardUtilities.squareIsAttackedByAlliance(s, enemyAlliance, board))
				.findAny()
				.ifPresentOrElse(s -> {}, runnable);

		if (this.getAlliance() == Alliance.WHITE) {
			if (board.getWhiteCanKingSideCastle() && pathIsObstructed.test(WHITES_KING_SIDE_CASTLE_PATH)) {
				ifPathIsSafeThenRun.accept(WHITES_KING_SIDE_CASTLE_PATH, addKingSideCastleMove);
			}
			if (board.getWhiteCanQueenSideCastle() && pathIsObstructed.test(WHITES_QUEEN_SIDE_CASTLE_PATH)) {
				ifPathIsSafeThenRun.accept(WHITES_QUEEN_SIDE_CASTLE_PATH.subList(0, 2), addQueenSideCastleMove);
			}
		} else {
			if (board.getBlackCanKingSideCastle() && pathIsObstructed.test(BLACKS_KING_SIDE_CASTLE_PATH)) {
				ifPathIsSafeThenRun.accept(BLACKS_KING_SIDE_CASTLE_PATH, addKingSideCastleMove);
			}
			if (board.getBlackCanQueenSideCastle() && pathIsObstructed.test(BLACKS_QUEEN_SIDE_CASTLE_PATH)) {
				ifPathIsSafeThenRun.accept(BLACKS_QUEEN_SIDE_CASTLE_PATH.subList(0, 2), addQueenSideCastleMove);
			}
		}
		return castleMoves;
	}

	@Override
	public Collection<Move> calculatePseudoLegalMoves(Board board) {
		List<Point> destinations = CANDIDATE_DESTINATIONS_OFFSETS.stream()
			.map(o -> this.getPosition().add(o))
			.filter(d -> BoardUtilities.isValidCoordinate(d))
			.collect(Collectors.toList());
		List<Move> pseudoMoves = new LinkedList<>();
		for (final Point dest : destinations) {
			Consumer<Piece> ifEnemyPieceThenAddCaptureMove = p -> {
				if (p.getAlliance() != this.getAlliance())
					pseudoMoves.add(new CaptureMove(this.getPosition(), dest, board));
			};
			Runnable addNormalMove = () -> pseudoMoves.add(new NormalMove(this.getPosition(), dest, board));
			board.getPiece(dest).ifPresentOrElse(ifEnemyPieceThenAddCaptureMove, addNormalMove);

		}
		return pseudoMoves;
	}

	@Override
        public Collection<Move> calculateLegalMoves(Board board) {
                Collection<Move> pseudoLegalMoves = calculatePseudoLegalMoves(board);
                Collection<Move> castleMoves = calculateCastlingMoves(board);
		return Stream.concat(pseudoLegalMoves.stream(), castleMoves.stream())
			.filter(m -> ! BoardUtilities.kingIsInCheck(this.getAlliance(), m.execute(board)))
			.collect(Collectors.toList());
        }

	@Override
	public Piece withPosition(Point newPosition) {
		return new King(this.getAlliance(), newPosition);
	}

	@Override
	public String toString() {
		return this.getAlliance().toString() + "King";
	}

}
