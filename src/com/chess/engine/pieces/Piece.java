package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
    //Every piece has its coordinate
	protected final int piecePosition;
	
	//Piece has a black or white alliance
	protected final Alliance pieceAlliance;

	//check for the first move of the piece
	//TODO more work to do here!!!
	protected final boolean isFirstMove;
	
	Piece(final int piecePosition, final Alliance pieceAlliance){
		this.pieceAlliance = pieceAlliance;
		this.piecePosition = piecePosition;
		this.isFirstMove = false;
	}

	public int getPiecePosition(){
		return this.piecePosition;
	}

	public Alliance getPieceAlliance(){
	    return this.pieceAlliance;
    }

    public boolean isFirstMove(){
		return this.isFirstMove;
	}
	
	//calculating the legal moves of the piece in the form
	public abstract Collection<Move> calculatedLegalMoves(final Board board);

	public enum PieceType{

		PAWN("P"),
		KNIGHT("N"),
		BISHOP("B"),
		ROOK("R"),
		QUEEN("Q"),
		KING("K");

		private String pieceName;

		PieceType(final String pieceName){
			this.pieceName = pieceName;
		}

		@Override
		public String toString(){
			return this.pieceName;
		}
	}
}
