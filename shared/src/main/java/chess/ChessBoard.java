package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //throw new RuntimeException("Not implemented");
        squares[position.getRow()][position.getColumn()] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        //throw new RuntimeException("Not implemented");
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        //throw new RuntimeException("Not implemented");

        //Be sure to clear the board first

//        addPiece((1,1),new ChessPiece(WHITE,ROOK));
//        addPiece(([1][2]), new ChessPiece(WHITE,KNIGHT));
//        addPiece([1][3], new ChessPiece(WHITE,BISHOP));
//        addPiece([1][4], new ChessPiece(WHITE,QUEEN));
//        addPiece([1][5], new ChessPiece(WHITE,KING));
//        addPiece([1][6], new ChessPiece(WHITE,BISHOP));
//        addPiece([1][7], new ChessPiece(WHITE,KNIGHT));
//        addPiece(([1][8]),new ChessPiece(WHITE,ROOK));
//
//
//        addPiece((2,1),new ChessPiece(WHITE,PAWN));
//        addPiece(([2][2]), new ChessPiece(WHITE,PAWN));
//        addPiece([2][3], new ChessPiece(WHITE,PAWN));
//        addPiece([2][4], new ChessPiece(WHITE,PAWN));
//        addPiece([2][5], new ChessPiece(WHITE,PAWN));
//        addPiece([2][6], new ChessPiece(WHITE,PAWN));
//        addPiece([2][7], new ChessPiece(WHITE,PAWN));
//        addPiece(([2][8]),new ChessPiece(WHITE,PAWN));
//
//        addPiece((7,1),new ChessPiece(BLACK,PAWN));
//        addPiece(([7][2]), new ChessPiece(BLACK,PAWN));
//        addPiece([7][3], new ChessPiece(ChessGame.TeamColor.BLACK,PAWN));
//        addPiece([7][4], new ChessPiece(BLACK,PAWN));
//        addPiece([7][5], new ChessPiece(BLACK,PAWN));
//        addPiece([7][6], new ChessPiece(BLACK,PAWN));
//        addPiece([7][7], new ChessPiece(BLACK,PAWN));
//        addPiece(([7][8]),new ChessPiece(BLACK,PAWN));
//
//        addPiece((8,1),new ChessPiece(ChessGame.TeamColor.BLACK,ROOK));
//        addPiece(([8][2]), new ChessPiece(BLACK,KNIGHT));
//        addPiece([8][3], new ChessPiece(BLACK,BISHOP));
//        addPiece([8][4], new ChessPiece(BLACK,QUEEN));
//        addPiece([8][5], new ChessPiece(BLACK,KING));
//        addPiece([8][6], new ChessPiece(BLACK,BISHOP));
//        addPiece([8][7], new ChessPiece(BLACK,KNIGHT));
//        addPiece(([8][8]),new ChessPiece(BLACK,ROOK));
//
//



    }
}
