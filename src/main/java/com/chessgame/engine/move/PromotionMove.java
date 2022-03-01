package com.chessgame.engine.move;

import com.chessgame.engine.Point;
import com.chessgame.engine.board.Board;
import com.chessgame.engine.piece.*;

import java.util.Optional;

public class PromotionMove extends Move {

	private Point start;
	private Point destination;
	private static PromotionPrompt promotionPrompt = () -> "Q";
	private static boolean canPrompt = false;

	public PromotionMove(Point start, Point destination, Board board) {
		super(board);
		this.start = start;
		this.destination = destination;
	}

	public static void setPromotionPrompt(PromotionPrompt promotionPrompt) {
		PromotionMove.promotionPrompt = promotionPrompt;
	}

	public static void setCanPrompt(boolean canPrompt) {
		PromotionMove.canPrompt = canPrompt;
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
		Piece oldPawn = this.board.getPiece(this.start).orElseThrow();
		Piece chosenPiece = null;
		do {
			System.out.println("I'M ON A LOOP");
			String pieceName = PromotionMove.canPrompt ? promotionPrompt.prompt() : "Q"; //may return null
			char letter = pieceName.toUpperCase().charAt(0);
			switch (letter) {
				case 'Q':
					chosenPiece = new Queen(oldPawn.getAlliance(), this.destination); break;
				case 'R':
					chosenPiece = new Rook(oldPawn.getAlliance(), this.destination); break;
				case 'B':
					chosenPiece = new Bishop(oldPawn.getAlliance(), this.destination); break;
				case 'K':
					chosenPiece = new Knight(oldPawn.getAlliance(), this.destination); break;
			}
		} while (chosenPiece == null);
		Optional<Piece> enemyPiece = this.board.getPiece(this.destination);
		if (enemyPiece.isPresent()) {
			return Board.builder()
				.copyBoard(this.board)
				.removePiece(oldPawn)
				.removePiece(enemyPiece.get())
				.setPiece(chosenPiece)
				.setAllianceTurn(this.board.getAllianceTurn().getOpposite())
				.build();
		} else {
			return Board.builder()
				.copyBoard(this.board)
				.removePiece(oldPawn)
				.setPiece(chosenPiece)
				.setAllianceTurn(this.board.getAllianceTurn().getOpposite())
				.build();
		}
	}

}
