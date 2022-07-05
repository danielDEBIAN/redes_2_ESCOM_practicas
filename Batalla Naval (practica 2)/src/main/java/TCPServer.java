import java.io.*;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;

public class TCPServer {
    public static void main(String[] args) throws Exception{
        String clientSentence;
        String serverSentence;
        ServerSocket welcomeSocket;
        Socket serverSocket;

        String miss = "fallo";
        String hit = "acierto";
        String won = "victoria";
        boolean turn = true;

        //De la linea de comandos
        BufferedReader inFromUser = new BufferedReader(
                new InputStreamReader(System.in));
        //Creacion de tablero
        board gameBoard = new board();

        int head = 11;
        int tail = 14;
        gameBoard.createBoat(head, tail);
        System.out.println("Acorazado generado en "+head+ " y "+tail);


        head = 17;
        tail = 37;
        gameBoard.createBoat(head, tail);
        System.out.println("Crucero 1 generado en "+head+ " y "+tail);


        head = 44;
        tail = 64;
        gameBoard.createBoat(head, tail);
        System.out.println("Crucero 2 generado en "+head+ " y "+tail);

        head = 94;
        tail = 95;
        gameBoard.createBoat(head, tail);
        System.out.println("Destructor 1 generado en "+head+ " y "+tail);


        head = 48;
        tail = 58;
        gameBoard.createBoat(head, tail);
        System.out.println("Destructor 2 generado en "+head+ " y "+tail);


        head = 31;
        tail = 32;
        gameBoard.createBoat(head, tail);
        System.out.println("Destructor 3 generado en "+head+ " y "+tail);


        head = 71;
        tail = 75;
        gameBoard.createBoat(head, tail);
        System.out.println("Submarino generado en "+head+ " y "+tail);


        gameBoard.printBoard();
        //Inicia el juego
        welcomeSocket = new ServerSocket(6789);
        serverSocket  = welcomeSocket.accept();
        //Datos de cliente
        BufferedReader inFromClient = new BufferedReader(
                new InputStreamReader(serverSocket.getInputStream()));

        //Mensaje de acierto o error al cliente
        DataOutputStream outToClient1 =
                new DataOutputStream(serverSocket.getOutputStream());


        while(true){
            int hitRow;
            int hitCol;
            //El servidor recibe el golpe del cliente
            //y responde si es acierto o fallo
            clientSentence = inFromClient.readLine();
            System.out.println("Golpe del cliente: " + clientSentence);
            int clientInt = Integer.parseInt(clientSentence);
            hitRow = Math.abs(clientInt/10)-1;
            hitCol = clientInt%10-1;

            if(gameBoard.testHit(hitRow, hitCol)){
                gameBoard.testLoss();
                if(gameBoard.testLoss() == false){
                    hit = miss = "GANASTE!";
                    System.out.println("Gano el humano");
                }
                outToClient1.writeBytes(hit+"\n");
                outToClient1.flush();
            }
            else{
                outToClient1.writeBytes(miss+"\n");
                outToClient1.flush();
            }

            //El servidor manda su tiro
            System.out.println("Disparo: ");
            int min = 11;
            int max = 99;
            ThreadLocalRandom tlr = ThreadLocalRandom.current();
            int randomNum = tlr.nextInt(min, max + 1);
            if(randomNum == 10){
                randomNum += randomNum + 1;
            }
            if(randomNum == 20){
                randomNum = randomNum + 1;
            }
            if(randomNum == 30){
                randomNum = randomNum + 1;
            }
            if(randomNum == 40){
                randomNum = randomNum + 1;
            }
            if(randomNum == 50){
                randomNum = randomNum + 1;
            }
            if(randomNum == 60){
                randomNum = randomNum + 1;
            }
            if(randomNum == 70){
                randomNum = randomNum + 1;
            }
            if(randomNum == 80){
                randomNum = randomNum + 1;
            }
            if(randomNum == 90){
                randomNum = randomNum + 1;
            }
            String newS = randomNum+"";
            outToClient1.writeBytes(newS+"\n");
            outToClient1.flush();

            clientSentence = inFromClient.readLine();
            System.out.println("Resultado del cliente: " + clientSentence);
        }
    }

}