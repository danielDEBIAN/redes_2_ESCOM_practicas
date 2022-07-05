public class board {
    public int[][] board;
    //this will hold info if the boat can be created
    public boolean boatBool;
    public boolean loss = true;
    public String  str;
    public board(){
        board = new int[10][10];
    }
    public void testPos(int a, int b){
        int row1 = Math.abs(a/10);
        int col1 = a%10;
        int row2 = Math.abs(b/10);
        int col2 = b%10;
        if(row1 == row2 || col1 == col2){
            boatBool = true;
        }
        else
            boatBool = false;
    }
    //Creamos el bote
    public void createBoat(int a, int b){
        //Creamos la cabeza y cola del bote
        int row1 = Math.abs(a/10);
        int col1 = a%10;
        int row2 = Math.abs(b/10);
        int col2 = b%10;
        //Checamos que este en la misma fila
        if(row1 == row2){
            //Si esta llenamos lo demas
            for(int i = col1; i<=col2; i++){
                board[row1-1][i-1] = 1;
            }
        }
        else if(col1 == col2){
            for(int i = row1; i<=row2; i++){
                board[i-1][col1-1] = 1;
            }
        }
        else{
            boatBool = false;
        }
    }
    //Impresion del tablero
    public void printBoard(){
        System.out.println("\n");
        for(int i = 0; i<board.length; i++){
            for(int c = 0; c<board[i].length; c++){
                System.out.print(board[i][c]+" ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }
    public boolean testHit(int row, int col){
        if(board[row][col] == 1){
            board[row][col] = 5;
            printBoard();
            return true;
        }
        else
            return false;
    }
    //Checamos si el usuario o servidor ha perdido
    public boolean testLoss(){
        int count = 0;
        boolean bool1 = true;
        for(int i = 0; i<board.length; i++){
            for(int c = 0; c<board[i].length; c++){
                if(board[i][c]==0 || board[i][c]==5){
                    count++;
                }
            }
        }
        if(count == 100)
            bool1 = false;
        return bool1;
    }
}