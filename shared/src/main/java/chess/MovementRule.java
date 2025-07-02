package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovementRule {

    public MovementRule(){
        // Do the new classes need a constructor??
    }


//    public Collection<ChessMove> SingleUnit(ChessPosition myPosition, ChessBoard board, ChessGame game){
//        ChessGame fakegame = game;
//        Collection<ChessMove> allmoves = new ArrayList<>();
//        ArrayList<ChessMove> validmoves = new ArrayList<>();
//        //dfakegame.getBoard();
//
//        ChessPiece thisIsNotByPiece = board.getPiece(myPosition);
//        //why does this not convert??
//        allmoves = thisIsNotByPiece.pieceMoves(board,myPosition);
//
//
////        for(ChessMove checkmove:allmoves){
////            ChessBoard checkboard = fakegame.makeFakeMove(checkmove);
////            ChessGame checkGame = new ChessGame(checkboard);
////            if(checkGame.isInCheck(thisIsNotByPiece.getTeamColor())==false){
////                validmoves.add(checkmove);
////            }
////        }
//
//        return validmoves;
//    }
//    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition myPosition, ChessGame game) {
//        ChessPiece pieceMoving = board.getPiece(myPosition);
//        Boolean initialCheckTest = game.isInCheck(game.getTeamTurn());
//        ChessGame fakegame = game;
//
//        // if king is in check, kill or defend
//        // if king is not in check, but would be, cannot move
//
//
//
//        //make a new game, set the board with the new board after a move was made, and check if in check is false
//        if(initialCheckTest==true) {
//
//            return SingleUnit(myPosition, board, fakegame);
//
//
//        }
//        return SingleUnit(myPosition, board,fakegame);
//
//    }







}
