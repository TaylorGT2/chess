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



    private static void drawHeaders(PrintStream out) {

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




    private static void drawChessBoard(PrintStream out) {

        for(int second = 0; second<BOARD_SIZE_IN_SQUARES;++second) {

            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

                if(second%2==0) {

                    if (boardRow % 2 == 0) {

                        setRed(out);
                        out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                    } else {
                        setWhite(out);
                        out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                    }

                }
                else{
                    if (boardRow % 2 == 0) {

                        setWhite(out);
                        out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                    } else {
                        setRed(out);
                        out.print(EMPTY.repeat((int) (SQUARE_SIZE_IN_PADDED_CHARS)));
                    }

                }




            }
            setBlack(out);
            out.println();

        }

    }




    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }










}




