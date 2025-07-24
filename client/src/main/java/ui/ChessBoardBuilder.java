package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessBoardBuilder {


    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;


    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";

    private static final String P = " P ";
    private static final String P2 = " p ";

    private static final String Q = " Q ";
    private static final String Q2 = " q ";

    private static final String K = " K ";
    private static final String K2 = " k ";

    private static final String R = " R ";
    private static final String R2 = " r ";

    private static final String B = " B ";
    private static final String B2 = " b ";

    private static final String N = " N ";
    private static final String N2 = " n ";

    private static Random rand = new Random();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);

        drawChessBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


//    PrintStream out;
//    public ChessBoardBuilder(){
//        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//        out.print(ERASE_SCREEN);
//    }



    public static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = { "1", "2", "3", "4", "5", "6", "7", "8" };
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }


    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }




    public static void drawChessBoard(PrintStream out) {

        //printPlayer(out,K,true);

        for(int second = 0; second<BOARD_SIZE_IN_SQUARES;++second) {

            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {


                if(second%2==0) {

                    if (boardRow % 2 == 0) {

                        if(second==6){
                            printPlayer(out,P2,true);
                        }
                        else {
                            if(second==0){
                                if(boardRow==0){
                                    printPlayer(out,R,true);
                                }
                                if(boardRow==1){
                                    printPlayer(out,N,false);
                                }
                                if(boardRow==2){
                                    printPlayer(out,B,true);
                                }
                                if(boardRow==3){
                                    printPlayer(out,Q,false);
                                }
                                if(boardRow==4){
                                    printPlayer(out,K,true);
                                }
                                if(boardRow==5){
                                    printPlayer(out,B,false);
                                }
                                if(boardRow==6){
                                    printPlayer(out,N,true);
                                }
                                if(boardRow==7){
                                    printPlayer(out,R,false);
                                }
                            }

                            else {


                                setRed(out);
                                out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                            }
                        }
                    } else {
                        if(second==6){
                            printPlayer(out,P2,false);
                        }
                        else {
                            if(second==0){

                                if(boardRow==0){
                                    printPlayer(out,R,true);
                                }
                                if(boardRow==1){
                                    printPlayer(out,N,false);
                                }
                                if(boardRow==2){
                                    printPlayer(out,B,true);
                                }
                                if(boardRow==3){
                                    printPlayer(out,Q,false);
                                }
                                if(boardRow==4){
                                    printPlayer(out,K,true);
                                }
                                if(boardRow==5){
                                    printPlayer(out,B,false);
                                }
                                if(boardRow==6){
                                    printPlayer(out,N,true);
                                }
                                if(boardRow==7){
                                    printPlayer(out,R,false);
                                }
                            }
                            else {
                                setWhite(out);
                                out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                            }
                        }
                    }

                }
                else{
                    if (boardRow % 2 == 0) {
                        if(second==1){
                            printPlayer(out,P,false);
                        }
                        else {
                            if(second==7){
                                if(boardRow==0){
                                    printPlayer(out,R2,false);
                                }
                                if(boardRow==1){
                                    printPlayer(out,N2,true);
                                }
                                if(boardRow==2){
                                    printPlayer(out,B2,false);
                                }
                                if(boardRow==3){
                                    printPlayer(out,Q2,true);
                                }
                                if(boardRow==4){
                                    printPlayer(out,K2,false);
                                }
                                if(boardRow==5){
                                    printPlayer(out,B2,true);
                                }
                                if(boardRow==6){
                                    printPlayer(out,N2,false);
                                }
                                if(boardRow==7){
                                    printPlayer(out,R2,true);
                                }
                            }
                            else {

                                setWhite(out);
                                out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                            }
                        }
                    } else {

                        if(second==1){
                            printPlayer(out,P,true);
                        }
                        else {
                            if(second==7){
                                if(boardRow==0){
                                    printPlayer(out,R2,false);
                                }
                                if(boardRow==1){
                                    printPlayer(out,N2,true);
                                }
                                if(boardRow==2){
                                    printPlayer(out,B2,false);
                                }
                                if(boardRow==3){
                                    printPlayer(out,Q2,true);
                                }
                                if(boardRow==4){
                                    printPlayer(out,K2,false);
                                }
                                if(boardRow==5){
                                    printPlayer(out,B2,true);
                                }
                                if(boardRow==6){
                                    printPlayer(out,N2,false);
                                }
                                if(boardRow==7){
                                    printPlayer(out,R2,true);
                                }
                            }
                            else {
                                setRed(out);
                                out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                            }
                        }
                    }

                }




            }
            setBlack(out);
            out.println();

        }

    }




    private static void printPlayer(PrintStream out, String player, Boolean bac) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        if(bac==true){
            out.print(SET_BG_COLOR_RED);
        }

        out.print(player);

        setWhite(out);
    }










}




