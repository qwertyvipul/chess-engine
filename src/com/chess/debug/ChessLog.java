package com.chess.debug;

public class ChessLog {
    public static boolean CHESS_LOG = false;
    public static int tab = 0;

    private static String getTabs(){
        for(int i=0; i<tab; i++){
            return "\t";
        }
        return "";
    }

    public static void indentUp(){
        tab++;
    }

    public static void indentDown(){
        if(tab > 0) tab--;
    }

    public static void logPrint(String message){
        if(CHESS_LOG)System.out.println(getTabs() + message);
    }

    public static void indentPrint(String message){
        if(CHESS_LOG)System.out.println(getTabs() + "\t" + message);
    }
}
