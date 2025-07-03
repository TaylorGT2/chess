package chess;

import java.util.*;
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {return false;}
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
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }
    public PieceType getPieceType() {
        return type;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece pieceMoving = board.getPiece(myPosition);
        if(pieceMoving.getPieceType() == PieceType.BISHOP){return bishopMovesCalculator(myPosition, board);}
        if(pieceMoving.getPieceType() == PieceType.KING){return kingMovesCalculator(myPosition, board);}
        if(pieceMoving.getPieceType() == PieceType.KNIGHT){return knightMovesCalculator(myPosition, board);}
        if(pieceMoving.getPieceType() == PieceType.ROOK){return rookMovesCalculator(myPosition, board);}
        if(pieceMoving.getPieceType()== PieceType.QUEEN){return queenMovesCalculator(myPosition, board);}
        if(pieceMoving.getPieceType()==PieceType.PAWN){return pawnMovesCalculator(myPosition,board);}
        return bishopMovesCalculator(myPosition, board);
    }
    public Collection<ChessMove> bishopMovesCalculator(ChessPosition myPosition, ChessBoard board) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int currentColumn = myPosition.getColumn();
        int currentRow = myPosition.getRow();
        ChessPosition curpos = new ChessPosition(currentRow,currentColumn);
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn + moveDiagonal;
            int nextRow = currentRow + moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle!=null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    }
                    else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn - moveDiagonal;
            int nextRow = currentRow - moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle!=null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    }
                    else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn + moveDiagonal;
            int nextRow = currentRow - moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle!=null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    }
                    else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        for(int moveDiagonal = 1; moveDiagonal < 8; moveDiagonal++) {
            int nextColumn = currentColumn - moveDiagonal;
            int nextRow = currentRow + moveDiagonal;
            if ((nextColumn < 9) && (nextRow < 9) && (nextColumn>0) && (nextRow>0)) {
                ChessPosition addPos = new ChessPosition(nextRow,nextColumn);
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle!=null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    }
                    else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        return possibleMoves;
    }
    public Collection<ChessMove> kingMovesCalculator(ChessPosition myPosition, ChessBoard board) {
        int a = myPosition.getColumn();
        int b = myPosition.getRow();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPosition curpos = new ChessPosition(b,a);
        for(int i = 0; i<3; i++) {
            if (i==1) {a = a - 1;}
            if (i==2) {a = a+2;}
            for(int j = 0; j<3; j++) {
                if (j==0) {b = myPosition.getRow();}
                if (j==1) {b = myPosition.getRow()-1;}
                if (j==2) {b = myPosition.getRow()+1;}
                if ((!(a==myPosition.getColumn() && b==myPosition.getRow())) && ((a < 9) && (b<9)) && ((a>0 && b>0))) {
                    ChessPosition addPos = new ChessPosition(b,a);
                    ChessPiece obstacle = board.getPiece(addPos);
                    ChessPiece myColor = board.getPiece(curpos);
                    if (obstacle!=null) {
                        if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                            ChessMove goodMove = new ChessMove(curpos, addPos, null);
                            possibleMoves.add(goodMove);
                        }
                    }
                    else {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                    }
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
        if (pawnType.getTeamColor()== ChessGame.TeamColor.BLACK){
            pawnmoves = pawnBlackMovesCalculator(myPosition, board);
        }
        return pawnmoves;
    }
    public Collection<ChessMove> pawnWhiteMovesCalculator(ChessPosition myPosition, ChessBoard board) {
        int rowcheck = myPosition.getRow();
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition curpos = new ChessPosition(myPosition.getRow(),myPosition.getColumn());
        ChessPiece myColor = board.getPiece(curpos);
        if(rowcheck==2){
            ChessPosition addPos = new ChessPosition(rowcheck+2,myPosition.getColumn());
            ChessPosition addPos2 = new ChessPosition(rowcheck+1,myPosition.getColumn());
            ChessPiece obstacle = board.getPiece(addPos);
            ChessPiece obstacle2 = board.getPiece(addPos2);
            if (obstacle == null && obstacle2 == null) {
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        ChessPosition attackLeft = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1);
        ChessPiece obstacleEnemy = board.getPiece(attackLeft);
        if (obstacleEnemy != null && (myPosition.getRow() < 8) && (myPosition.getColumn() > 1)) {
            if (obstacleEnemy.getTeamColor() != myColor.getTeamColor()) {
                if (myPosition.getRow()+1!=8) {
                    ChessMove goodMove = new ChessMove(curpos, attackLeft, null);
                    possibleMoves.add(goodMove);
                }
                else {
                    ChessMove goodMove = new ChessMove(curpos, attackLeft, PieceType.QUEEN);
                    possibleMoves.add(goodMove);
                    ChessMove badMove = new ChessMove(curpos, attackLeft, PieceType.ROOK);
                    possibleMoves.add(badMove);
                    ChessMove poorMove = new ChessMove(curpos, attackLeft, PieceType.BISHOP);
                    possibleMoves.add(poorMove);
                    ChessMove bestMove = new ChessMove(curpos, attackLeft, PieceType.KNIGHT);
                    possibleMoves.add(bestMove);
                }
            }
        }
        ChessPosition attackRight = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
        ChessPiece obstacleEnemy2 = board.getPiece(attackRight);
        if ((obstacleEnemy2 != null) && (myPosition.getRow() < 8) && (myPosition.getColumn() < 8)) {
            if (obstacleEnemy2.getTeamColor() != myColor.getTeamColor()) {
                if (myPosition.getRow()+1!=8) {
                    ChessMove goodMove = new ChessMove(curpos, attackRight, null);
                    possibleMoves.add(goodMove);
                }
                else {
                    ChessMove goodMove = new ChessMove(curpos, attackRight, PieceType.QUEEN);
                    possibleMoves.add(goodMove);
                    ChessMove badMove = new ChessMove(curpos, attackRight, PieceType.ROOK);
                    possibleMoves.add(badMove);
                    ChessMove poorMove = new ChessMove(curpos, attackRight, PieceType.BISHOP);
                    possibleMoves.add(poorMove);
                    ChessMove bestMove = new ChessMove(curpos, attackRight, PieceType.KNIGHT);
                    possibleMoves.add(bestMove);
                }
            }
        }
        ChessPosition marchOn = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn());
        ChessPiece obstacleEnemy3 = board.getPiece(marchOn);
        if (obstacleEnemy3 == null) {
            if (myPosition.getRow()+1!=8) {
                ChessMove goodMove = new ChessMove(curpos, marchOn, null);
                possibleMoves.add(goodMove);
            }
            else {
                ChessMove goodMove = new ChessMove(curpos, marchOn, PieceType.QUEEN);
                possibleMoves.add(goodMove);
                ChessMove badMove = new ChessMove(curpos, marchOn, PieceType.ROOK);
                possibleMoves.add(badMove);
                ChessMove poorMove = new ChessMove(curpos, marchOn, PieceType.BISHOP);
                possibleMoves.add(poorMove);
                ChessMove bestMove = new ChessMove(curpos, marchOn, PieceType.KNIGHT);
                possibleMoves.add(bestMove);
            }
        }
        return possibleMoves;
    }
    public Collection<ChessMove> pawnBlackMovesCalculator(ChessPosition myPosition, ChessBoard board) {
        int rowcheck = myPosition.getRow();
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition curpos = new ChessPosition(myPosition.getRow(),myPosition.getColumn());
        ChessPiece myColor = board.getPiece(curpos);
        if(rowcheck==7){
            ChessPosition addPos = new ChessPosition(rowcheck-2,myPosition.getColumn());
            ChessPosition addPos2 = new ChessPosition(rowcheck-1,myPosition.getColumn());
            ChessPiece obstacle = board.getPiece(addPos);
            ChessPiece obstacle2 = board.getPiece(addPos2);
            if (obstacle == null && obstacle2==null) {
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        ChessPosition attackLeft = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-1);
        ChessPiece obstacleEnemy = board.getPiece(attackLeft);
        if (obstacleEnemy != null && (myPosition.getRow() > 1) && (myPosition.getColumn() > 1)) {
            if (obstacleEnemy.getTeamColor() != myColor.getTeamColor()) {
                if (myPosition.getRow()-1!=1) {
                    ChessMove goodMove = new ChessMove(curpos, attackLeft, null);
                    possibleMoves.add(goodMove);
                }
                else {
                    ChessMove goodMove = new ChessMove(curpos, attackLeft, PieceType.QUEEN);
                    possibleMoves.add(goodMove);
                    ChessMove badMove = new ChessMove(curpos, attackLeft, PieceType.ROOK);
                    possibleMoves.add(badMove);
                    ChessMove poorMove = new ChessMove(curpos, attackLeft, PieceType.BISHOP);
                    possibleMoves.add(poorMove);
                    ChessMove bestMove = new ChessMove(curpos, attackLeft, PieceType.KNIGHT);
                    possibleMoves.add(bestMove);
                }
            }
        }
        ChessPosition attackRight = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1);
        ChessPiece obstacleEnemy2 = board.getPiece(attackRight);
        if ((obstacleEnemy2 != null) && (myPosition.getRow() > 1) && (myPosition.getColumn() < 8)) {
            if (obstacleEnemy2.getTeamColor() != myColor.getTeamColor()) {
                if (myPosition.getRow()-1!=1) {
                    ChessMove goodMove = new ChessMove(curpos, attackRight, null);
                    possibleMoves.add(goodMove);
                }
                else {
                    ChessMove goodMove = new ChessMove(curpos, attackRight, PieceType.QUEEN);
                    possibleMoves.add(goodMove);
                    ChessMove badMove = new ChessMove(curpos, attackRight, PieceType.ROOK);
                    possibleMoves.add(badMove);
                    ChessMove poorMove = new ChessMove(curpos, attackRight, PieceType.BISHOP);
                    possibleMoves.add(poorMove);
                    ChessMove bestMove = new ChessMove(curpos, attackRight, PieceType.KNIGHT);
                    possibleMoves.add(bestMove);
                }
            }
        }
        ChessPosition marchOn = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn());
        ChessPiece obstacleEnemy3 = board.getPiece(marchOn);
        if (obstacleEnemy3 == null) {
            if (myPosition.getRow()-1!=1) {
                ChessMove goodMove = new ChessMove(curpos, marchOn, null);
                possibleMoves.add(goodMove);
            }
            else {
                ChessMove goodMove = new ChessMove(curpos, marchOn, PieceType.QUEEN);
                possibleMoves.add(goodMove);
                ChessMove badMove = new ChessMove(curpos, marchOn, PieceType.ROOK);
                possibleMoves.add(badMove);
                ChessMove poorMove = new ChessMove(curpos, marchOn, PieceType.BISHOP);
                possibleMoves.add(poorMove);
                ChessMove bestMove = new ChessMove(curpos, marchOn, PieceType.KNIGHT);
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
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int a = myPosition.getColumn();
        int b = myPosition.getRow();
        int a2 = myPosition.getColumn();
        int b2 = myPosition.getRow();
        ChessPosition curpos = new ChessPosition(b,a);
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
                    ChessPosition addPos = new ChessPosition(b,a);
                    ChessPiece obstacle = board.getPiece(addPos);
                    ChessPiece myColor = board.getPiece(curpos);
                    if (obstacle != null) {
                        if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                            ChessMove goodMove = new ChessMove(curpos, addPos, null);
                            possibleMoves.add(goodMove);
                        }
                    }
                    else {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                    }
                }
            }
        }
        return possibleMoves;
    }
    public Collection<ChessMove> rookMovesCalculator(ChessPosition myPosition, ChessBoard board) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        int a = myPosition.getColumn();
        int b = myPosition.getRow();
        ChessPosition curpos = new ChessPosition(b,a);
        for(int i=1;i<9;i++) {
            a = myPosition.getColumn() + i;
            int a2 = myPosition.getColumn() - i;
            if ((a > 0 && a < 9)) {
                ChessPosition addPos = new ChessPosition(myPosition.getRow(), a);
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle != null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    } else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        for(int i=1;i<9;i++) {
            a = myPosition.getColumn() + i;
            int a2 = myPosition.getColumn() - i;
            if(a2>0 && a2<9) {
                ChessPosition addPos = new ChessPosition(myPosition.getRow(),a2);
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle!=null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    }
                    else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }
        for(int j=1;j<9;j++) {
            b = myPosition.getRow() + j;
            int b2 = myPosition.getRow() - j;
            if (b > 0 && b < 9) {
                ChessPosition addPos = new ChessPosition(b, myPosition.getColumn());
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle != null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    } else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);
                possibleMoves.add(goodMove);
            }
        }

        for(int j=1;j<9;j++) {

            b = myPosition.getRow() + j;
            int b2 = myPosition.getRow() - j;
            if(b2>0 && b2<9) {
                ChessPosition addPos = new ChessPosition(b2,myPosition.getColumn());
                ChessPiece obstacle = board.getPiece(addPos);
                ChessPiece myColor = board.getPiece(curpos);
                if (obstacle!=null) {
                    if (obstacle.getTeamColor() != myColor.getTeamColor()) {
                        ChessMove goodMove = new ChessMove(curpos, addPos, null);
                        possibleMoves.add(goodMove);
                        break;
                    }
                    else {
                        break;
                    }
                }
                ChessMove goodMove = new ChessMove(curpos, addPos, null);

                possibleMoves.add(goodMove);
            }
        }
        return possibleMoves;
    }
}
