package com.chess.engine.player;

import com.chess.debug.ChessLog;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    // the board on which the player is playing
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,
                                                    calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calcaulateAttackOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    public static Collection<Move> calcaulateAttackOnTile(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing(){
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King)piece;
            }
        }
        throw new RuntimeException("The King is Missing! Not a valid board!");
    }

    public boolean isMoveLegeal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return this.isInCheck;
    }

    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move){
        ChessLog.indentUp();
        ChessLog.logPrint("[Player] inside - makeMove()");

        if(!isMoveLegeal(move)){
            ChessLog.logPrint("Move is illegal!...returning illegal move");
            ChessLog.indentDown();
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        ChessLog.logPrint("Move is legal!");

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calcaulateAttackOnTile(
                transitionBoard.currentPlayer().
                getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()){
            ChessLog.indentDown();
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        ChessLog.indentDown();
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public King getPlayerKing(){
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                             Collection<Move> opponentLegals);
}
