package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessBoardBuilder {


    private static final int BOARD_SIZE_IN_SQUARES = 8;
    public static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;


    public static final String EMPTY = "   ";
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


    private static final String NUM1 = " 1 ";
    private static final String NUM2 = " 2 ";
    private static final String NUM3 = " 3 ";
    private static final String NUM4 = " 4 ";
    private static final String NUM5 = " 5 ";
    private static final String NUM6 = " 6 ";
    private static final String NUM7 = " 7 ";
    private static final String NUM8 = " 8 ";

    private static final String L1 = " a ";
    private static final String L2 = " b ";
    private static final String L3 = " c ";
    private static final String L4 = " d ";
    private static final String L5 = " e ";
    private static final String L6 = " f ";
    private static final String L7 = " g ";
    private static final String L8 = " h ";

    private static Random rand = new Random();





    public static void totalWhiteBoard(PrintStream out){
        out.print(ERASE_SCREEN);



        realHeaders(out);



        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    public static void totalBlackBoard(PrintStream out){
        out.print(ERASE_SCREEN);



        realHeadersBlack(out);



        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }



    public static void realHeaders(PrintStream out){
        printHeaderText(out,L1);
        printHeaderText(out,L2);
        printHeaderText(out,L3);
        printHeaderText(out,L4);
        printHeaderText(out,L5);
        printHeaderText(out,L6);
        printHeaderText(out,L7);
        printHeaderText(out,L8);
        out.println();

    }
    public static void realHeadersBlack(PrintStream out){
        printHeaderText(out,L8);
        printHeaderText(out,L7);
        printHeaderText(out,L6);
        printHeaderText(out,L5);
        printHeaderText(out,L4);
        printHeaderText(out,L3);
        printHeaderText(out,L2);
        printHeaderText(out,L1);
        out.println();

    }


    PrintStream out;
    public ChessBoardBuilder(){
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
    }



    public static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }


    public static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }


    public static void setWhite(PrintStream out) {

        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    public static void setRed(PrintStream out) {


        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }







    public static void printPlayer(PrintStream out, String player, Boolean bac) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_BLACK);
        if(bac==true){

            out.print(SET_BG_COLOR_WHITE);
        }

        out.print(player);

        setWhite(out);
    }










}




