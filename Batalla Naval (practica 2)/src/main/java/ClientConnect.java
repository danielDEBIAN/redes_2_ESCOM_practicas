import java.io.*;
import java.net.*;

public class ClientConnect {
    public static void main(String argv[]) throws Exception
    {

        String ip = "127.0.0.1";
        String sentence;
        String modifiedSentence;
        Socket clientSocket;
        Socket hitOrMiss;
        String miss = "fallo";
        String hit = "acierto";

        BufferedReader inFromUser = new BufferedReader(
                new InputStreamReader(System.in));

        //Creamos el tablero

        board gameBoard = new board();

        System.out.println("\tA C O R A Z A D O\nLocalizacion de la cabeza:");
        String line1 = inFromUser.readLine();
        System.out.println("Localizacion de la cola:");
        String line2 = inFromUser.readLine();
        int head = Integer.parseInt(line1);
        int tail = Integer.parseInt(line2);
        //Testeamos la posicion
        gameBoard.testPos(head, tail);
        if(gameBoard.boatBool == true){
            gameBoard.createBoat(head, tail);
            System.out.println("Acorazado generado en "+head+ " y "+tail);
        }
        else
            System.out.println("No se puede posicionar el barco en esa casilla");

        gameBoard.printBoard();

        System.out.println("\n\tC R U C E R O  1\nLocalizacion de la cabeza:");
        line1 = inFromUser.readLine();
        System.out.println("Localizacion de la cola:");
        line2 = inFromUser.readLine();
        head = Integer.parseInt(line1);
        tail = Integer.parseInt(line2);
        //Testeamos la posicion
        gameBoard.testPos(head, tail);
        if(gameBoard.boatBool == true){
            gameBoard.createBoat(head, tail);
            System.out.println("Crucero 1 generado en "+head+ " y "+tail);
        }
        else
            System.out.println("No se puede posicionar el barco en esa casilla");

        gameBoard.printBoard();

        System.out.println("\n\tC R U C E R O  2\nLocalizacion de la cabeza:");
        line1 = inFromUser.readLine();
        System.out.println("Localizacion de la cola:");
        line2 = inFromUser.readLine();
        head = Integer.parseInt(line1);
        tail = Integer.parseInt(line2);
        //Testeamos la posicion
        gameBoard.testPos(head, tail);
        if(gameBoard.boatBool == true){
            gameBoard.createBoat(head, tail);
            System.out.println("Crucero 2 generado en "+head+ " y "+tail);
        }
        else
            System.out.println("No se puede posicionar el barco en esa casilla");

        gameBoard.printBoard();

        System.out.println("\n\tD E S T R U C T O R  1\nLocalizacion de la cabeza:");
        line1 = inFromUser.readLine();
        System.out.println("Localizacion de la cola:");
        line2 = inFromUser.readLine();
        head = Integer.parseInt(line1);
        tail = Integer.parseInt(line2);
        //Testeamos la posicion
        gameBoard.testPos(head, tail);
        if(gameBoard.boatBool == true){
            gameBoard.createBoat(head, tail);
            System.out.println("Destructor 1 generado en "+head+ " y "+tail);
        }
        else
            System.out.println("No se puede posicionar el barco en esa casilla");

        gameBoard.printBoard();

        System.out.println("\n\tD E S T R U C T O R  2\nLocalizacion de la cabeza:");
        line1 = inFromUser.readLine();
        System.out.println("Localizacion de la cola:");
        line2 = inFromUser.readLine();
        head = Integer.parseInt(line1);
        tail = Integer.parseInt(line2);
        //Testeamos la posicion
        gameBoard.testPos(head, tail);
        if(gameBoard.boatBool == true){
            gameBoard.createBoat(head, tail);
            System.out.println("Destructor 2 generado en "+head+ " y "+tail);
        }
        else
            System.out.println("No se puede posicionar el barco en esa casilla");

        gameBoard.printBoard();

        System.out.println("\n\tD E S T R U C T O R  3\nLocalizacion de la cabeza:");
        line1 = inFromUser.readLine();
        System.out.println("Localizacion de la cola:");
        line2 = inFromUser.readLine();
        head = Integer.parseInt(line1);
        tail = Integer.parseInt(line2);
        //Testeamos la posicion
        gameBoard.testPos(head, tail);
        if(gameBoard.boatBool == true){
            gameBoard.createBoat(head, tail);
            System.out.println("Destructor 3 generado en "+head+ " y "+tail);
        }
        else
            System.out.println("No se puede posicionar el barco en esa casilla");

        gameBoard.printBoard();

        System.out.println("\n\tS U B M A R I N O \nLocalizacion de la cabeza:");
        line1 = inFromUser.readLine();
        System.out.println("Localizacion de la cola:");
        line2 = inFromUser.readLine();
        head = Integer.parseInt(line1);
        tail = Integer.parseInt(line2);
        //Testeamos la posicion
        gameBoard.testPos(head, tail);
        if(gameBoard.boatBool == true){
            gameBoard.createBoat(head, tail);
            System.out.println("Submarino generado en "+head+ " y "+tail);
        }
        else
            System.out.println("No se puede posicionar el barco en esa casilla");

        gameBoard.printBoard();

        // Comienza el juego
        clientSocket = new Socket(ip, 6789);
        //Tomamos los datos del servidor
        BufferedReader inFromServer = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        //Mensaje de acierto o error del servidor
        DataOutputStream outToServer = new DataOutputStream(
                clientSocket.getOutputStream());

        while(true){
            int hitRow;
            int hitCol;
            //El cliente envia su tiro
            System.out.println("Disparo:");
            sentence = inFromUser.readLine();
            outToServer.writeBytes(sentence+"\n");
            outToServer.flush();
            //El cliente recibe el tiro del servidor
            modifiedSentence = inFromServer.readLine();
            System.out.println("Resultado del servidor: " + modifiedSentence);

            modifiedSentence = inFromServer.readLine();
            System.out.println("Tiro del servidor: " + modifiedSentence);
            int clientInt = Integer.parseInt(modifiedSentence);
            hitRow = Math.abs(clientInt/10)-1;
            hitCol = clientInt%10-1;

            if(gameBoard.testHit(hitRow, hitCol)){
                gameBoard.testLoss();
                if(gameBoard.testLoss() == false){
                    hit = miss = "GANASTE!";
                    System.out.println("Como pierdes contra una maquina? XD");
                }
                outToServer.writeBytes(hit+"\n");
                outToServer.flush();
            }
            else{
                outToServer.writeBytes(miss+"\n");
                outToServer.flush();
            }
        }

    }

}