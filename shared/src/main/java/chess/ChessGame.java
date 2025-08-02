package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    int turnNumber=0;
    TeamColor turn;
    ChessBoard board;
    ChessGame game;

    Collection<ChessMove> validMoves;

    public ChessGame(ChessBoard board,Collection<ChessMove> validMoves) {
        //turnNumber = 0;
        this.board = board;
        this.validMoves = validMoves;


    }
    public ChessGame() {
        //turnNumber = 0;
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;



    }
    public ChessGame(ChessGame copy){
       // game = Arrays.copyOf(copy.board,copy.turn);
        this.setTeamTurn(copy.turn);
        this.setBoard(copy.board);

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

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

    public int getTurnNumber(){return turnNumber;}

    public void setTurnNumber(int num){turnNumber=num;}

    /**
     * Makes a move in a chess game
     *
     * @param //move chess move to perform
     * @throws //InvalidMoveException if move is invalid
     */


    public void makeMove(ChessMove move) throws InvalidMoveException {




        if(board==null){
            board = new ChessBoard();
            board.resetBoard();
        }


        int rowTest = move.getStartPosition().getRow();
        int colTest = move.getStartPosition().getColumn();

        ChessPiece testPiece = board.squares[rowTest][colTest];


            if (testPiece == null || testPiece.getTeamColor() != turn) {
                throw new InvalidMoveException();
            }

            validMoves = validMoves2(board, move.getStartPosition(), this);
            boolean okaymove = false;
            for (ChessMove finderror : validMoves) {
                int moveEndRow = move.getEndPosition().getRow();
                int moveEndCol = move.getEndPosition().getColumn();

                int errorRow = finderror.getEndPosition().getRow();
                int errorCol = finderror.getEndPosition().getColumn();

                if (moveEndRow == errorRow && moveEndCol == errorCol) {
                    okaymove = true;
                    break;
                }
            }
            if (okaymove == false) {
                throw new InvalidMoveException();

            }

            if (move.getPromotionPiece() == null) {
                ChessPiece movementPiece = board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()];
                board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()] = null;
                board.squares[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = movementPiece;

            }
            if (move.getPromotionPiece() != null) {
                ChessPiece movementPiece = board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()];
                board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()] = null;
                ChessPiece promo = new ChessPiece(movementPiece.getTeamColor(), move.getPromotionPiece());
                board.squares[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = promo;

            }


            if (turn == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
      //  }


    }


    public ChessBoard makeFakeMove(ChessMove move) {

        ChessBoard fakeboard = new ChessBoard(board);
        ChessPiece movingPiece = board.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()];
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promote = move.getPromotionPiece();


        fakeboard.addPiece(endPosition,movingPiece);
        fakeboard.squares[move.getStartPosition().getRow()][move.getStartPosition().getColumn()]=null;




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


        ArrayList<ChessMove> allValidMoves = new ArrayList<>();

        ChessPosition kingPos = new ChessPosition(30,30);

        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                if(board.squares[i][j]!=null) {
                    if (board.squares[i][j].getPieceType() == ChessPiece.PieceType.KING && board.squares[i][j].getTeamColor() == teamColor) {

                        kingPos = new ChessPosition(i, j);
                    }
                    if (board.squares[i][j].getTeamColor() != teamColor) {
                        ChessPosition curPos = new ChessPosition(i, j);
                        allValidMoves.addAll(board.squares[i][j].pieceMoves(board, curPos));

                    }
                }


            }
        }
        for(ChessMove aim:allValidMoves){
            ChessPosition end = aim.getEndPosition();
            if(end.getRow()==kingPos.getRow()&&end.getColumn()==kingPos.getColumn()){
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
        Collection<ChessMove> valMove = new ArrayList<>();
        for(int i = 0;i<20;i++){
            for(int j = 0;j<20;j++){
                if(board.squares[i][j]!=null){
                    if(board.squares[i][j].getTeamColor()==teamColor){
                        valMove.addAll(validMoves2(board,new ChessPosition(i,j), this));
                    }
                }

            }
        }

        if(isInCheck(teamColor)==true&&valMove.size()==0){
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

        Collection<ChessMove> valMove = new ArrayList<>();
        for(int i = 0;i<20;i++){
            for(int j = 0;j<20;j++){
                if(board.squares[i][j]!=null){
                    if(board.squares[i][j].getTeamColor()==teamColor){
                        valMove.addAll(validMoves2(board,new ChessPosition(i,j), this));
                    }
                }

            }
        }

        if(isInCheck(teamColor)==false&&valMove.size()==0){
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

        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return board;
    }





    public Collection<ChessMove> singleUnit(ChessPosition myPosition, ChessBoard board, ChessGame game){

        Collection<ChessMove> allmoves = new ArrayList<>();
        ArrayList<ChessMove> validmoves = new ArrayList<>();


        ChessPiece thisIsNotByPiece = board.getPiece(myPosition);

        allmoves = thisIsNotByPiece.pieceMoves(board,myPosition);

        TeamColor testcolor;

        if(thisIsNotByPiece.getTeamColor()==TeamColor.WHITE){
            testcolor=TeamColor.WHITE;
        }
        else{
            testcolor = TeamColor.BLACK;
        }


        for(ChessMove checkmove:allmoves){
            ChessGame fakegame = new ChessGame(this);
            ChessBoard checkboard = fakegame.makeFakeMove(checkmove);
            ChessGame checkGame = new ChessGame(checkboard, validmoves);
            if(checkGame.isInCheck(testcolor)==false){
                validmoves.add(checkmove);
            }

        }

        return validmoves;
    }
    public Collection<ChessMove> validMoves2(ChessBoard board, ChessPosition myPosition, ChessGame game) {
        if(board==null){
            board = new ChessBoard();
            board.resetBoard();
        }
        ChessPiece pieceMoving = board.getPiece(myPosition);
        Boolean initialCheckTest = game.isInCheck(game.getTeamTurn());
        ChessGame fakegame = game;


        if(initialCheckTest==true) {

            return singleUnit(myPosition, board, fakegame);


        }
        return singleUnit(myPosition, board,fakegame);

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turnNumber == chessGame.turnNumber && turn == chessGame.turn
                && Objects.equals(board, chessGame.board) && Objects.equals(validMoves, chessGame.validMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnNumber, turn, board, validMoves);
    }
}
