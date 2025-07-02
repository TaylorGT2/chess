package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    int turnNumber;
    TeamColor turn;
    ChessBoard board;

    Collection<ChessMove> validMoves;

    public ChessGame(ChessBoard board,Collection<ChessMove> validMoves) {
        //turnNumber = 0;
        this.board = board;
        this.validMoves = validMoves;


    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        //throw new RuntimeException("Not implemented");
//        if(turnNumber%2==0) {
//            turnNumber++;
//            return TeamColor.WHITE;
//        }
//        turnNumber++;
//        return TeamColor.BLACK;
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        //TeamColor turn = team;
        //throw new RuntimeException("Not implemented");
        turn = team;

    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //throw new RuntimeException("Not implemented");
        return validMoves2(board,startPosition,this);
        //return pieceMoves(this,startPosition);
        //ChessPiece checkingPiece = getPiece(startPosition);



    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {




        //throw new RuntimeException("Not implemented");

        // I still need to set the end position as the new start position
        //ChessPosition newStart = move.getEndPosition();
        //new ChessPosition(move.getRow(),move.getColumn());

        validMoves = validMoves2(board, move.getStartPosition(), this);
        boolean okaymove =false;
        for(ChessMove finderror:validMoves){
            if(finderror==move){
                okaymove=true;
                break;
            }
        }
//        if(okaymove==false){
//            //throw InvalidMoveException;
//            //return null;
//        }

        if(move.getPromotionPiece()==null){
            ChessPiece movementPiece = board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()];
            board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()]=null;
            board.squares[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = movementPiece;

        }
        if(move.getPromotionPiece()!=null){
            ChessPiece movementPiece = board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()];
            board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()]=null;
            ChessPiece promo = new ChessPiece(movementPiece.getTeamColor(),move.getPromotionPiece());
            board.squares[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = promo;

        }


        if(turn==TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else {
            setTeamTurn(TeamColor.WHITE);
        }


    }


    public ChessBoard makeFakeMove(ChessMove move) {

        ChessBoard fakeboard = board;
        ChessPiece movingPiece = board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()];
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promote = move.getPromotionPiece();


        fakeboard.addPiece(endPosition,movingPiece);
        fakeboard.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()]=null;


        //throw new RuntimeException("Not implemented");

        // I still need to set the end position as the new start position
        //ChessPosition newStart = move.getEndPosition();
        //new ChessPosition(move.getRow(),move.getColumn());


        if(turn==TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }
        else {
            setTeamTurn(TeamColor.WHITE);
        }

        return fakeboard;


    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "turnNumber=" + turnNumber +
                ", turn=" + turn +
                '}';
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        //throw new RuntimeException("Not implemented");
        ArrayList<ChessMove> allValidMoves = new ArrayList<>();
        ChessPiece CurrentKing;
        ChessPosition kingPos = new ChessPosition(30,30);
        //board.squares;
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                if(board.squares[i][j].getPieceType()== ChessPiece.PieceType.KING && board.squares[i][j].getTeamColor()== teamColor){
                    CurrentKing = board.squares[i][j];
                    kingPos = new ChessPosition(i,j);
                }
                if(board.squares[i][j].getTeamColor()!=teamColor) {
                    ChessPosition CurPos = new ChessPosition(i,j);
                    allValidMoves.addAll(board.squares[i][j].pieceMoves(board,CurPos));

                }


            }
        }
        for(ChessMove aim:allValidMoves){
            if(aim.getEndPosition()==kingPos){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        //Movement rule is so isolated, how can I get the list from it?
        if(isInCheck(teamColor)==true && validMoves==null){
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        if(isInCheck(teamColor)==false&&validMoves==null){
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        //throw new RuntimeException("Not implemented");
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        //throw new RuntimeException("Not implemented");
        return board;
    }


    public Collection<ChessMove> SingleUnit(ChessPosition myPosition, ChessBoard board, ChessGame game){
        ChessGame fakegame = game;
        Collection<ChessMove> allmoves = new ArrayList<>();
        ArrayList<ChessMove> validmoves = new ArrayList<>();
        //dfakegame.getBoard();

        ChessPiece thisIsNotByPiece = board.getPiece(myPosition);
        //why does this not convert??
        allmoves = thisIsNotByPiece.pieceMoves(board,myPosition);


        for(ChessMove checkmove:allmoves){
            ChessBoard checkboard = fakegame.makeFakeMove(checkmove);
            ChessGame checkGame = new ChessGame(checkboard, validmoves);
            if(checkGame.isInCheck(thisIsNotByPiece.getTeamColor())==false){
                validmoves.add(checkmove);
            }
        }

        return validmoves;
    }
    public Collection<ChessMove> validMoves2(ChessBoard board, ChessPosition myPosition, ChessGame game) {
        ChessPiece pieceMoving = board.getPiece(myPosition);
        Boolean initialCheckTest = game.isInCheck(game.getTeamTurn());
        ChessGame fakegame = game;

        // if king is in check, kill or defend
        // if king is not in check, but would be, cannot move



        //make a new game, set the board with the new board after a move was made, and check if in check is false
        if(initialCheckTest==true) {

            return SingleUnit(myPosition, board, fakegame);


        }
        return SingleUnit(myPosition, board,fakegame);

    }




}
