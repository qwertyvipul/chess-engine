package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {7, 8, 9, 16};

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    // calculating the legal move of the pawn
    @Override
    public Collection<Move> calculatedLegalMoves(Board board) {

        // a list of legal moves
        final List<Move> legalMoves = new ArrayList<>(); // The list is an array list

        // loop through all the offsets for the pawn
        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            // The mathematics for the candidate destination coordinate
            /*
            * piece position + direction * offset
            * */
            final int candidadateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
            // If not a valid tile coordinate then continue
            if(!BoardUtils.isvalidTileCoordinate(candidadateDestinationCoordinate)){
                continue;
            }

            // if offset is 8 and the destination tile is not occupied then add a moajor move
            if(currentCandidateOffset == 8 && !board.getTile(candidadateDestinationCoordinate).isTileOccupied()){
                //TODO more work to do here (deal with promotions)!!!
                legalMoves.add(new Move.MajorMove(board, this, candidadateDestinationCoordinate));

            // if offset is 16 and the very first move and black on the second row
            // and the white on the seventh row add the destination is not occupied then add the major move
            }else if(currentCandidateOffset == 16 && this.isFirstMove() && // 16 and first move
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) || // the black
                    (BoardUtils.SEVENTH_ROW[this.piecePosition]) && this.getPieceAlliance().isWhite()){ // the white
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                // tile is not occupied and neither the previous one then add the major move
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidadateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidadateDestinationCoordinate));
                }

            // When attacking move
            }else if(currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
                if(board.getTile(candidadateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidadateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //TODO attcking on pawn promotion
                        legalMoves.add(new MajorMove(board, this, candidadateDestinationCoordinate));
                    }
                }
            }else if(currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
                if(board.getTile(candidadateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidadateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //TODO attcking on pawn promotion
                        legalMoves.add(new Move.MajorMove(board, this, candidadateDestinationCoordinate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
}
