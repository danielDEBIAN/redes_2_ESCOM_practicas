import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyMain {
    public static void main(String args[]) throws Exception{

        System.out.println("Seleccione una opcion");
        System.out.println("1. Correr servidor");
        System.out.println("2. Correr cliente");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice = Integer.parseInt(br.readLine());

        if(choice == 1){
            Server s = new Server();
        }
        else if(choice == 2){
            Client c = new Client();
        }
        else{
            System.out.println("Eleccion incorrecta");
        }
    }
}