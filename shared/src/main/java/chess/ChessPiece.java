package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    //This variable might have to be a boolean
    //Also it might have to be public???
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        ChessPiece pieceMoving = board.getPiece(myPosition);
        if(pieceMoving.getPieceType() == PieceType.BISHOP){
            return bishopMovesCalculator(myPosition, board);
        }
        if(pieceMoving.getPieceType() == PieceType.KING){
            return kingMovesCalculator(myPosition);

        }
        if(pieceMoving.getPieceType() == PieceType.KNIGHT){
            return knightMovesCalculator(myPosition);

        }
        if(pieceMoving.getPieceType() == PieceType.ROOK){
            return rookMovesCalculator(myPosition);

        }
        //return new ArrayList<>();
        return bishopMovesCalculator(myPosition, board);
    }
    //This maybe should not be static
    public Collection<ChessMove> bishopMovesCalculator(ChessPosition myPosition, ChessBoard board) {

        // current array theory, make a board and place a one if it is possible to move there

        //int moveDiagonal = 1;
        //ChessMove [] possibleMoves = {};
        //possibleMoves = new ChessMove[100];

        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //int currentspot = possibleMoves.length-1;
        int currentColumn = myPosition.getColumn();
        int currentRow = myPosition.getRow();
        ChessPosition Curpos = new ChessPosition(currentRow,currentColumn);
      // Yes, this is a bad function, will change later maybe
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn + moveDiagonal;
            int nextRow = currentRow + moveDiagonal;
            if ((nextColumn < 8) && (nextRow < 8) && (nextColumn>0) && (nextRow>0)) {
                // possibleMoves.append([nextColumn][nextRow]
                //possibleMoves[nextColumn][nextRow] = 1;
                //board.getPiece(addPos);

                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(Curpos);
                if (obstacle!=null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                        possibleMoves.add(goodMove);
                        break;
                    }
                    else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                possibleMoves.add(goodMove);
            }


        }
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn - moveDiagonal;
            int nextRow = currentRow - moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                // possibleMoves.append([nextColumn][nextRow]
                //possibleMoves[nextColumn][nextRow] = 1;
                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessMove goodMove = new ChessMove(Curpos, addPos, null);
//                possibleMoves[currentspot] = goodMove;
//                currentspot -=1;
                possibleMoves.add(goodMove);
            }


        }
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn + moveDiagonal;
            int nextRow = currentRow - moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                // possibleMoves.append([nextColumn][nextRow]
                //possibleMoves[nextColumn][nextRow] = 1;
                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessMove goodMove = new ChessMove(Curpos, addPos, null);
                //possibleMoves[currentspot] = goodMove;
                //currentspot -= 1;
                possibleMoves.add(goodMove);
            }


        }
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn - moveDiagonal;
            int nextRow = currentRow + moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                // possibleMoves.append([nextColumn][nextRow]
                //possibleMoves[nextColumn][nextRow] = 1;
                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessMove goodMove = new ChessMove(Curpos, addPos, null);
                //possibleMoves[currentspot] = goodMove;
                //currentspot -= 1;
                possibleMoves.add(goodMove);
            }


        }
        //Collection<ChessMove> okayCollect = Arrays.asList(possibleMoves);
        //List<ChessMove> okayCollect = Arrays.asList(possibleMoves);
        return possibleMoves;
        }

        public Collection<ChessMove> kingMovesCalculator(ChessPosition myPosition) {
            //int [][] possibleMoves = {};
            // This move set is also probably wrong, I still can't think in matrixes
            //possibleMoves = new int[8][8];
            int a = myPosition.getColumn();
            int b = myPosition.getRow();
            ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
            ChessPosition Curpos = new ChessPosition(b,a);

            for(int i = 0; i<3; i++) {
                if (i==1) {
                    a = a - 1;
                }
                if (i==2) {
                    a = a+2;
                }

                for(int j = 0; j<3; j++) {
                    if (j==0) {
                        b = myPosition.getRow();
                    }
                    if (j==1) {
                        b = myPosition.getRow()-1;
                    }
                    if (j==2) {
                        b = myPosition.getRow()+1;
                    }
                    if ((!(a==myPosition.getColumn() && b==myPosition.getRow())) && ((a < 9) && (b<9)) && ((a>0 && b>0))) {
                        ChessPosition addPos = new ChessPosition(b,a);
                        ChessMove goodMove = new ChessMove(Curpos, addPos, PieceType.KING);

                        possibleMoves.add(goodMove);
                        //possibleMoves.append([a][b])
                    }




                }

            }


            return possibleMoves;
        }

        public Collection<ChessMove> knightMovesCalculator(ChessPosition myPosition) {
            //int[][] possibleMoves = {};

           // possibleMoves = new int[8][8];
            ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
            //ChessPosition Curpos = new ChessPosition(b,a);
            int a = myPosition.getColumn();
            int b = myPosition.getRow();
            int a2 = myPosition.getColumn();
            int b2 = myPosition.getRow();
            ChessPosition Curpos = new ChessPosition(b,a);
;
            for(int i=0;i<4;i++) {

                if(i==0) {
                    a = myPosition.getColumn()+1;
                    a2 = myPosition.getColumn()+1;

                }
                if(i==1) {
                    a=myPosition.getColumn()+2;
                    a2 = myPosition.getColumn()+2;
                }
                if(i==2) {
                    a= myPosition.getColumn()-1;
                    a2 = myPosition.getColumn()+1;
                }
                if(i==3) {
                    a= myPosition.getColumn()-2;
                    a2 = myPosition.getColumn()+2;
                }

                for(int j=0;j<4;j++) {
                    if(j==0) {
                        b = myPosition.getRow()+1;
                        b2 = myPosition.getRow()+1;
                    }
                    if(j==1) {
                        b = myPosition.getRow()+2;
                        b2 = myPosition.getRow()+2;
                    }
                    if(j==2) {
                        b= myPosition.getRow()-1;
                        b2=myPosition.getRow()+1;
                    }
                    if(j==3) {
                        b = myPosition.getRow()-2;
                        b2 = myPosition.getRow()+2;
                    }

                    if(((a>0) && (a<9)) && ((b>0) && (b<9)) && ((a2!=b2))) {
                        //append [a][b]
                        ChessPosition addPos = new ChessPosition(b,a);
                        ChessMove goodMove = new ChessMove(Curpos, addPos, PieceType.KNIGHT);

                        possibleMoves.add(goodMove);

                    }



                }

            }
            return possibleMoves;


        }
        public Collection<ChessMove> rookMovesCalculator(ChessPosition myPosition) {
            ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
            //int[][] possibleMoves = {};
            // This move set is also probably wrong, I still can't think in matrixes
            //possibleMoves = new int[8][8];
            int a = myPosition.getColumn();
            int b = myPosition.getRow();
            ChessPosition Curpos = new ChessPosition(b,a);
            for(int i=1;i<9;i++) {

                a = myPosition.getColumn() + i;
                int a2 = myPosition.getColumn() - i;
                if((a>0 && a<9)) {

                    //append[a][myPosition.getRow();
                    ChessPosition addPos = new ChessPosition(myPosition.getRow(),a);
                    ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    possibleMoves.add(goodMove);
                }
                if(a2>0 && a2<9) {
                    ChessPosition addPos = new ChessPosition(myPosition.getRow(),a2);
                    ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    possibleMoves.add(goodMove);

                    //append[a2][myPosition.getRow();
                }


            }
            for(int j=1;j<9;j++) {

                b = myPosition.getRow() + j;
                int b2 = myPosition.getRow() - j;
                if(b>0 && b<9) {

                    //append[a][myPosition.getRow();
                    ChessPosition addPos = new ChessPosition(b,myPosition.getColumn());
                    ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    possibleMoves.add(goodMove);
                }
                if(b2>0 && b2<9) {
                    ChessPosition addPos = new ChessPosition(b2,myPosition.getColumn());
                    ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    possibleMoves.add(goodMove);

                    //append[a2][myPosition.getRow();
                }

            }
            return possibleMoves;



        }

    }

//}
