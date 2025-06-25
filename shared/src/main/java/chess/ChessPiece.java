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
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
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
            return kingMovesCalculator(myPosition, board);

        }
        if(pieceMoving.getPieceType() == PieceType.KNIGHT){
            return knightMovesCalculator(myPosition, board);

        }
        if(pieceMoving.getPieceType() == PieceType.ROOK){
            return rookMovesCalculator(myPosition, board);

        }
        if(pieceMoving.getPieceType()== PieceType.QUEEN){

            return queenMovesCalculator(myPosition, board);
        }
        if(pieceMoving.getPieceType()==PieceType.PAWN){

            return pawnMovesCalculator(myPosition,board);
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
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
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
            int nextColumn = currentColumn + moveDiagonal;
            int nextRow = currentRow - moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                // possibleMoves.append([nextColumn][nextRow]
                //possibleMoves[nextColumn][nextRow] = 1;
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
            int nextRow = currentRow + moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                // possibleMoves.append([nextColumn][nextRow]
                //possibleMoves[nextColumn][nextRow] = 1;
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
        //Collection<ChessMove> okayCollect = Arrays.asList(possibleMoves);
        //List<ChessMove> okayCollect = Arrays.asList(possibleMoves);
        return possibleMoves;
        }

        public Collection<ChessMove> kingMovesCalculator(ChessPosition myPosition, ChessBoard board) {
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
                        //ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                        //possibleMoves.add(goodMove);
                        //ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                        ChessPiece obstacle = board.getPiece(addPos);
                        ChessPiece myColor = board.getPiece(Curpos);
                        if (obstacle!=null) {
                            if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                                ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                                possibleMoves.add(goodMove);
                                //break;
                            }

                        }
                        else {
                            ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                            possibleMoves.add(goodMove);

                        }


                        //possibleMoves.append([a][b])
                    }




                }

            }


            return possibleMoves;
        }

        public Collection<ChessMove> pawnMovesCalculator(ChessPosition myPosition, ChessBoard board) {
            myPosition.getRow();
            ChessPiece pawnType = board.getPiece(myPosition);
            Collection<ChessMove> pawnmoves = pawnWhiteMovesCalculator(myPosition,board);
            if (pawnType.getTeamColor()== ChessGame.TeamColor.WHITE){

                pawnmoves = pawnWhiteMovesCalculator(myPosition, board);
            }


            return pawnmoves;

        }

        public Collection<ChessMove> pawnWhiteMovesCalculator(ChessPosition myPosition, ChessBoard board) {

            int rowcheck = myPosition.getRow();
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            ChessPosition Curpos = new ChessPosition(myPosition.getRow(),myPosition.getColumn());
            ChessPiece myColor = board.getPiece(Curpos);
            if(rowcheck==2){
                ChessPosition addPos = new ChessPosition(rowcheck+2,myPosition.getColumn());

                ChessPiece obstacle = board.getPiece(addPos);
                //ChessPiece myColor = board.getPiece(Curpos);
                if (obstacle == null) {
                    ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    possibleMoves.add(goodMove);

                }
            }
            ChessPosition attackLeft = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1);
            ChessPiece obstacleEnemy = board.getPiece(attackLeft);
            if (obstacleEnemy != null && (myPosition.getRow() < 8) && (myPosition.getColumn() > 1)) {
                if (obstacleEnemy.getTeamColor() != myColor.getTeamColor()) {
                    if (myPosition.getRow()+1!=8) {

                        ChessMove goodMove = new ChessMove(Curpos, attackLeft, null);

                        possibleMoves.add(goodMove);
                    }
                    else {

                        ChessMove goodMove = new ChessMove(Curpos, attackLeft, PieceType.QUEEN);
                        possibleMoves.add(goodMove);
                        ChessMove badMove = new ChessMove(Curpos, attackLeft, PieceType.ROOK);
                        possibleMoves.add(badMove);
                        ChessMove poorMove = new ChessMove(Curpos, attackLeft, PieceType.BISHOP);
                        possibleMoves.add(poorMove);
                        ChessMove bestMove = new ChessMove(Curpos, attackLeft, PieceType.KNIGHT);


                        possibleMoves.add(bestMove);
                    }
                }
            }
            ChessPosition attackRight = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
            ChessPiece obstacleEnemy2 = board.getPiece(attackRight);
            if ((obstacleEnemy2 != null) && (myPosition.getRow() < 8) && (myPosition.getColumn() < 8)) {
                if (obstacleEnemy2.getTeamColor() != myColor.getTeamColor()) {
                    if (myPosition.getRow()+1!=8) {

                        ChessMove goodMove = new ChessMove(Curpos, attackRight, null);

                        possibleMoves.add(goodMove);
                    }
                    else {

                        //ChessMove goodMove = new ChessMove(Curpos, attackRight, PieceType.QUEEN);

                        //possibleMoves.add(goodMove);
                        ChessMove goodMove = new ChessMove(Curpos, attackRight, PieceType.QUEEN);
                        possibleMoves.add(goodMove);
                        ChessMove badMove = new ChessMove(Curpos, attackRight, PieceType.ROOK);
                        possibleMoves.add(badMove);
                        ChessMove poorMove = new ChessMove(Curpos, attackRight, PieceType.BISHOP);
                        possibleMoves.add(poorMove);
                        ChessMove bestMove = new ChessMove(Curpos, attackRight, PieceType.KNIGHT);


                        possibleMoves.add(bestMove);
                    }
                }
            }
            ChessPosition marchOn = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn());
            ChessPiece obstacleEnemy3 = board.getPiece(marchOn);
            if (obstacleEnemy3 == null) {
                if (myPosition.getRow()+1!=8) {

                    ChessMove goodMove = new ChessMove(Curpos, marchOn, null);

                    possibleMoves.add(goodMove);
                }

                else {

                    //ChessMove goodMove = new ChessMove(Curpos, marchOn, PieceType.QUEEN);

                    //possibleMoves.add(goodMove);

                    ChessMove goodMove = new ChessMove(Curpos, marchOn, PieceType.QUEEN);
                    possibleMoves.add(goodMove);
                    ChessMove badMove = new ChessMove(Curpos, marchOn, PieceType.ROOK);
                    possibleMoves.add(badMove);
                    ChessMove poorMove = new ChessMove(Curpos, marchOn, PieceType.BISHOP);
                    possibleMoves.add(poorMove);
                    ChessMove bestMove = new ChessMove(Curpos, marchOn, PieceType.KNIGHT);


                    possibleMoves.add(bestMove);
                }

            }
            return possibleMoves;

        }


    public Collection<ChessMove> pawnBlackMovesCalculator(ChessPosition myPosition, ChessBoard board) {

        int rowcheck = myPosition.getRow();
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition Curpos = new ChessPosition(myPosition.getRow(),myPosition.getColumn());
        ChessPiece myColor = board.getPiece(Curpos);
        if(rowcheck==7){
            ChessPosition addPos = new ChessPosition(rowcheck+2,myPosition.getColumn());

            ChessPiece obstacle = board.getPiece(addPos);
            //ChessPiece myColor = board.getPiece(Curpos);
            if (obstacle == null) {
                ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                possibleMoves.add(goodMove);

            }
        }
        ChessPosition attackLeft = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1);
        ChessPiece obstacleEnemy = board.getPiece(attackLeft);
        if (obstacleEnemy != null && (myPosition.getRow() < 8) && (myPosition.getColumn() > 1)) {
            if (obstacleEnemy.getTeamColor() != myColor.getTeamColor()) {
                if (myPosition.getRow()+1!=8) {

                    ChessMove goodMove = new ChessMove(Curpos, attackLeft, null);

                    possibleMoves.add(goodMove);
                }
                else {

                    ChessMove goodMove = new ChessMove(Curpos, attackLeft, PieceType.QUEEN);
                    possibleMoves.add(goodMove);
                    ChessMove badMove = new ChessMove(Curpos, attackLeft, PieceType.ROOK);
                    possibleMoves.add(badMove);
                    ChessMove poorMove = new ChessMove(Curpos, attackLeft, PieceType.BISHOP);
                    possibleMoves.add(poorMove);
                    ChessMove bestMove = new ChessMove(Curpos, attackLeft, PieceType.KNIGHT);


                    possibleMoves.add(bestMove);
                }
            }
        }
        ChessPosition attackRight = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
        ChessPiece obstacleEnemy2 = board.getPiece(attackRight);
        if ((obstacleEnemy2 != null) && (myPosition.getRow() < 8) && (myPosition.getColumn() < 8)) {
            if (obstacleEnemy2.getTeamColor() != myColor.getTeamColor()) {
                if (myPosition.getRow()+1!=8) {

                    ChessMove goodMove = new ChessMove(Curpos, attackRight, null);

                    possibleMoves.add(goodMove);
                }
                else {

                    //ChessMove goodMove = new ChessMove(Curpos, attackRight, PieceType.QUEEN);

                    //possibleMoves.add(goodMove);
                    ChessMove goodMove = new ChessMove(Curpos, attackRight, PieceType.QUEEN);
                    possibleMoves.add(goodMove);
                    ChessMove badMove = new ChessMove(Curpos, attackRight, PieceType.ROOK);
                    possibleMoves.add(badMove);
                    ChessMove poorMove = new ChessMove(Curpos, attackRight, PieceType.BISHOP);
                    possibleMoves.add(poorMove);
                    ChessMove bestMove = new ChessMove(Curpos, attackRight, PieceType.KNIGHT);


                    possibleMoves.add(bestMove);
                }
            }
        }
        ChessPosition marchOn = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn());
        ChessPiece obstacleEnemy3 = board.getPiece(marchOn);
        if (obstacleEnemy3 == null) {
            if (myPosition.getRow()+1!=8) {

                ChessMove goodMove = new ChessMove(Curpos, marchOn, null);

                possibleMoves.add(goodMove);
            }

            else {

                //ChessMove goodMove = new ChessMove(Curpos, marchOn, PieceType.QUEEN);

                //possibleMoves.add(goodMove);

                ChessMove goodMove = new ChessMove(Curpos, marchOn, PieceType.QUEEN);
                possibleMoves.add(goodMove);
                ChessMove badMove = new ChessMove(Curpos, marchOn, PieceType.ROOK);
                possibleMoves.add(badMove);
                ChessMove poorMove = new ChessMove(Curpos, marchOn, PieceType.BISHOP);
                possibleMoves.add(poorMove);
                ChessMove bestMove = new ChessMove(Curpos, marchOn, PieceType.KNIGHT);


                possibleMoves.add(bestMove);
            }

        }
        return possibleMoves;

    }



        public Collection<ChessMove> queenMovesCalculator(ChessPosition myPosition, ChessBoard board) {
            ArrayList<ChessMove> queenmoves = new ArrayList<>();
            queenmoves.addAll(bishopMovesCalculator(myPosition,board));
            queenmoves.addAll(rookMovesCalculator(myPosition,board));
            return queenmoves;

        }


        public Collection<ChessMove> knightMovesCalculator(ChessPosition myPosition, ChessBoard board) {
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
                    a2 = 1;

                }
                if(i==1) {
                    a=myPosition.getColumn()+2;
                    a2 = 2;
                }
                if(i==2) {
                    a= myPosition.getColumn()-1;
                    a2 = 1;
                }
                if(i==3) {
                    a= myPosition.getColumn()-2;
                    a2 = 2;
                }

                for(int j=0;j<4;j++) {
                    if(j==0) {
                        b = myPosition.getRow()+1;
                        b2 = 1;
                    }
                    if(j==1) {
                        b = myPosition.getRow()+2;
                        b2 = 2;
                    }
                    if(j==2) {
                        b= myPosition.getRow()-1;
                        b2=1;
                    }
                    if(j==3) {
                        b = myPosition.getRow()-2;
                        b2 = 2;
                    }

                    if(((a>0) && (a<9)) && ((b>0) && (b<9)) && ((a2!=b2))) {
                        //append [a][b]
                        ChessPosition addPos = new ChessPosition(b,a);
                        //ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                        //possibleMoves.add(goodMove);


                        //ChessPosition addPos = new ChessPosition(myPosition.getRow(), a);

                        ChessPiece obstacle = board.getPiece(addPos);
                        ChessPiece myColor = board.getPiece(Curpos);
                        if (obstacle != null) {
                            if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                                ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                                possibleMoves.add(goodMove);
                                //break;
                            }


                        }
                        else {
                            ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                            possibleMoves.add(goodMove);
                        }







                    }



                }

            }
            return possibleMoves;


        }
        public Collection<ChessMove> rookMovesCalculator(ChessPosition myPosition, ChessBoard board) {
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
                if ((a > 0 && a < 9)) {

                    //append[a][myPosition.getRow();
                    ChessPosition addPos = new ChessPosition(myPosition.getRow(), a);

                    ChessPiece obstacle = board.getPiece(addPos);
                    ChessPiece myColor = board.getPiece(Curpos);
                    if (obstacle != null) {
                        if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                            ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                            possibleMoves.add(goodMove);
                            break;
                        } else {
                            break;
                        }
                    }
                    ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    possibleMoves.add(goodMove);


                }
            }


            for(int i=1;i<9;i++) {

                a = myPosition.getColumn() + i;
                int a2 = myPosition.getColumn() - i;
                if(a2>0 && a2<9) {
                    ChessPosition addPos = new ChessPosition(myPosition.getRow(),a2);
                    //ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    //possibleMoves.add(goodMove);

                    //ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
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

                    //append[a2][myPosition.getRow();
                }


            }
            for(int j=1;j<9;j++) {

                b = myPosition.getRow() + j;
                int b2 = myPosition.getRow() - j;
                if (b > 0 && b < 9) {

                    //append[a][myPosition.getRow();
                    ChessPosition addPos = new ChessPosition(b, myPosition.getColumn());
                    //ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    //possibleMoves.add(goodMove);

                    //ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                    ChessPiece obstacle = board.getPiece(addPos);
                    ChessPiece myColor = board.getPiece(Curpos);
                    if (obstacle != null) {
                        if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                            ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                            possibleMoves.add(goodMove);
                            break;
                        } else {
                            break;
                        }
                    }
                    ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    possibleMoves.add(goodMove);
                }
            }

            for(int j=1;j<9;j++) {

                b = myPosition.getRow() + j;
                int b2 = myPosition.getRow() - j;
                if(b2>0 && b2<9) {
                    ChessPosition addPos = new ChessPosition(b2,myPosition.getColumn());
                    //ChessMove goodMove = new ChessMove(Curpos, addPos, null);

                    //possibleMoves.add(goodMove);

                    //ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
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

                    //append[a2][myPosition.getRow();
                }

            }
            return possibleMoves;



        }

    }

//}
