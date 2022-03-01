package com.chessgame.engine.piece;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.board.BoardUtilities;
import com.chessgame.engine.move.CaptureMove;
import com.chessgame.engine.move.Move;
import com.chessgame.engine.move.NormalMove;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.function.Consumer;

public class Knight extends Piece {

	private static final Collection<Point> CANDIDATE_DESTINATIONS_OFFSETS = List.of(
		Point.of(-1, 2), Point.of(1, 2), Point.of(2, 1), Point.of(2, -1),
		Point.of(1, -2), Point.of(-1, -2), Point.of(-2, -1), Point.of(-2, 1)
	);

	public Knight(Alliance alliance, Point position) {
		super(alliance, position);
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
		return pseudoLegalMoves.stream()
                        .filter(m -> ! BoardUtilities.kingIsInCheck(this.getAlliance(), m.execute(board)))
                        .collect(Collectors.toList());
	}

	@Override
	public Piece withPosition(Point newPosition) {
		return new Knight(this.getAlliance(), newPosition);
	}

	@Override
	public String toString() {
		return this.getAlliance().toString() + "Knight";
	}

}
