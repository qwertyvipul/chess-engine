package com.chess;

import com.chess.debug.ChessLog;
import com.chess.gui.Table;

public class JChess {
    public static void main(String[] args){
        //Board board = Board.createStandardBoard();
        //System.out.println(board);

        ChessLog.CHESS_LOG = true;
        ChessLog.logPrint("Inside JChess - creating new table");
        Table table = new Table();
    }
}
