package com.chessgame.engine.piece;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.board.BoardUtilities;
import com.chessgame.engine.move.CaptureMove;
import com.chessgame.engine.move.Move;
import com.chessgame.engine.move.NormalMove;
import com.chessgame.engine.move.PromotionMove;

import java.util.Optional;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Consumer;

public class Pawn extends Piece {

	private static final Map<Alliance,Collection<Point>> CAPTUREMOVES_OFFSETS = Map.of(
		Alliance.WHITE, List.of(Point.of(1, 1), Point.of(-1, 1)),
		Alliance.BLACK, List.of(Point.of(1, -1), Point.of(-1, -1))
	);

	private static final Map<Alliance,Point> DIRECTION_VECTORS = Map.of(
		Alliance.WHITE, Point.of(0, 1), Alliance.BLACK, Point.of(0, -1)
	);

	private static final Map<Alliance,Integer> STARTING_RANK = Map.of(
		Alliance.WHITE, 1, Alliance.BLACK, 6
	);

	private static final Map<Alliance,Integer> PROMOTION_RANK = Map.of(
		Alliance.WHITE, 7, Alliance.BLACK, 0
	);

	public Pawn(Alliance alliance, Point position) {
		super(alliance, position);
	}

	private void addMovesUntilPieceOrBorderHitOr2Steps(Point start, Point adder, Board board, Collection<Move> moves) {
                Point dest = start.add(adder);
                if (BoardUtilities.isValidCoordinate(dest)) {
                        Optional<Piece> pieceOptional = board.getPiece(dest);
                        if (pieceOptional.isPresent()) {
                                return;
                        } else {
				if (dest.y == PROMOTION_RANK.get(this.getAlliance())) {
					moves.add(new PromotionMove(this.getPosition(), dest, board));
				} else {
                                	moves.add(new NormalMove(this.getPosition(), dest, board));
				}
				if (start.y == STARTING_RANK.get(this.getAlliance()))
                                	addMovesUntilPieceOrBorderHitOr2Steps(dest, adder, board, moves);
                        }
                } else {
                        return;
                }
        }

	@Override
	public Collection<Move> calculatePseudoLegalMoves(Board board) {
		List<Point> captureDestinations = CAPTUREMOVES_OFFSETS.get(this.getAlliance()).stream()
			.map(o -> this.getPosition().add(o))
			.filter(d -> BoardUtilities.isValidCoordinate(d))
			.collect(Collectors.toList());
		List<Move> pseudoMoves = new LinkedList<>();
		for (final Point dest : captureDestinations) {
			Consumer<Piece> ifEnemyPieceThenAddCaptureMove = p -> {
				if (p.getAlliance() != this.getAlliance())
					if (dest.y == PROMOTION_RANK.get(this.getAlliance())) {
						pseudoMoves.add(new PromotionMove(this.getPosition(), dest, board));
					} else {
						pseudoMoves.add(new CaptureMove(this.getPosition(), dest, board));
					}
			};
			board.getPiece(dest).ifPresent(ifEnemyPieceThenAddCaptureMove);
		}
		addMovesUntilPieceOrBorderHitOr2Steps(this.getPosition(), DIRECTION_VECTORS.get(this.getAlliance()), board, pseudoMoves);
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
		return new Pawn(this.getAlliance(), newPosition);
	}

	@Override
	public String toString() {
		return this.getAlliance().toString() + "Pawn";
	}

}
