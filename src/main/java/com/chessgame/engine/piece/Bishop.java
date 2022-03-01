package com.chessgame.engine.piece;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.board.BoardUtilities;
import com.chessgame.engine.move.CaptureMove;
import com.chessgame.engine.move.Move;
import com.chessgame.engine.move.NormalMove;

import java.util.Optional;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Bishop extends Piece {

	private static final List<Point> DIRECTION_VECTORS = List.of(
		Point.of(1, 1), Point.of(-1, -1), Point.of(1, -1), Point.of(-1, 1)
	);

	public Bishop(Alliance alliance, Point position) {
		super(alliance, position);
	}

	private void addMovesUntilPieceOrBorderHit(Point start, Point adder, Board board, Collection<Move> moves) {
		Point dest = start.add(adder);
		if (BoardUtilities.isValidCoordinate(dest)) {
			Optional<Piece> pieceOptional = board.getPiece(dest);
			if (pieceOptional.isPresent()) {
                                if (pieceOptional.get().getAlliance() != this.getAlliance())
                                        moves.add(new CaptureMove(this.getPosition(), dest, board));
				return;
			} else {
				moves.add(new NormalMove(this.getPosition(), dest, board));
				addMovesUntilPieceOrBorderHit(dest, adder, board, moves);
			}
		} else {
			return;
		}
	}


	@Override
        public Collection<Move> calculatePseudoLegalMoves(Board board) {
                List<Move> pseudoMoves = new LinkedList<>();
		DIRECTION_VECTORS.forEach(a -> addMovesUntilPieceOrBorderHit(this.getPosition(), a, board, pseudoMoves));
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
		return new Bishop(this.getAlliance(), newPosition);
	}

	@Override
	public String toString() {
		return getAlliance().toString() + "Bishop";
	}

}
