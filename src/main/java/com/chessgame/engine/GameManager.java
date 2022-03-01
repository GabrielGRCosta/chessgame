package com.chessgame.engine;

import com.chessgame.engine.board.Board;
import com.chessgame.engine.board.BoardUtilities;
import com.chessgame.engine.move.Move;
import com.chessgame.engine.move.PromotionMove;
import com.chessgame.engine.move.PromotionPrompt;
import com.chessgame.gui.Table;
import com.chessgame.engine.*;

import java.util.*;

public class GameManager {

	private List<Board> boards;
	private List<Move> moves;

	public GameManager(PromotionPrompt promotionPrompt) {
		this.boards = new LinkedList<>();
		this.boards.add(BoardUtilities.INITIAL_POSITION);
		this.moves = new LinkedList<>();
		PromotionMove.setPromotionPrompt(promotionPrompt);
		PromotionMove.setCanPrompt(false);
	}

	public Board getCurrentBoard() {
		Board currentBoard = null;
		try {
			currentBoard = this.boards.get(this.boards.size()-1);
		} catch (IndexOutOfBoundsException e) {
			this.boards.add(BoardUtilities.INITIAL_POSITION);
		}
		return currentBoard == null ? this.boards.get(this.boards.size()-1) : currentBoard;
	}

	public Collection<Move> getMovesFromPiece(Point position) {
		Board currentBoard = this.getCurrentBoard();
		return currentBoard.getPiece(position)
			.filter(p -> p.getAlliance() == currentBoard.getAllianceTurn())
			.map(p -> p.calculateLegalMoves(currentBoard))
			.orElse(Collections.emptyList());
	}

	public void applyMove(Move move) {
		PromotionMove.setCanPrompt(true);
		Board currentBoard = this.getCurrentBoard();
		Board newBoard = move.execute(currentBoard);
		this.boards.add(newBoard);
		this.moves.add(move);
		PromotionMove.setCanPrompt(false);
	}

	public Board restartGame() {
		boards.clear();
		moves.clear();
		this.boards.add(BoardUtilities.INITIAL_POSITION);
		return this.boards.get(this.boards.size()-1);
	}

	public void runRandomTurn() {
		Random x = new Random();
		Random y = new Random();

		Point randomPointTable = new Point(x.nextInt(8), y.nextInt(8));

		while (getCurrentBoard().getPiece(randomPointTable).isEmpty() ||
				getCurrentBoard().getPiece(randomPointTable).map(p -> p.getAlliance()).filter(a -> a.equals(getCurrentBoard().getAllianceTurn())).isEmpty()) {

			randomPointTable = new Point(x.nextInt(8), y.nextInt(8));
		}

		Collection<Move> randomMoveCandidates = getCurrentBoard().getPiece(randomPointTable).
				map(p -> p.calculateLegalMoves(getCurrentBoard())).orElse(Collections.emptyList());

		Optional<Move> randomMove = randomMoveCandidates.stream()
				.skip((int) (randomMoveCandidates.size() * Math.random()))
				.findFirst();

		if(randomMove.isEmpty()) {
			runRandomTurn();
		} else {
			applyMove(randomMove.get());
		}

	}

}
