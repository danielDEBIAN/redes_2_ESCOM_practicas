import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client
{
    @SuppressWarnings({ "unchecked", "rawtypes", "resource", "unused" })
    //public static void main(String args[]) throws Exception
    public Client()
    {
        Socket socket;
        ArrayList al;
        ArrayList<FileInfo> arrList = new ArrayList<FileInfo>();
        Scanner scanner = new Scanner(System.in);
        ObjectInputStream ois ;
        ObjectOutputStream oos ;
        String string;
        Object o,b;
        String directoryPath=null;
        int peerServerPort=0;

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Bienvenido");
            System.out.println(" ");
            System.out.println("Ingresa el directorio que contiene los archivos -->");
            directoryPath=br.readLine();

            System.out.println("Ingresa el puerto del servidor:");
            peerServerPort=Integer.parseInt(br.readLine());

            ServerDownload objServerDownload = new ServerDownload(peerServerPort,directoryPath);
            objServerDownload.start();

            Socket clientThread = new Socket("localhost",7799);

            ObjectOutputStream objOutStream = new ObjectOutputStream(clientThread.getOutputStream());
            ObjectInputStream objInStream = new ObjectInputStream(clientThread.getInputStream());

            al = new ArrayList();

            socket = new Socket("localhost",7799);
            System.out.println("Conexion establecida con el cliente");

            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Ingresa el ID de este directorio");
            int readpid=Integer.parseInt(br.readLine());

            File folder = new File(directoryPath);
            File[] listofFiles = folder.listFiles();
            FileInfo currentFile;
            File file;

            for (int i = 0; i < listofFiles.length; i++) {
                currentFile= new FileInfo();
                file = listofFiles[i];
                currentFile.fileName=file.getName();
                currentFile.peerid=readpid;
                currentFile.portNumber=peerServerPort;
                arrList.add(currentFile);
            }

            oos.writeObject(arrList);
            System.out.println("The complete ArrayList :::"+arrList);

            Scanner objsc = new Scanner(System.in);
            int seguir = 1;

            System.out.println("");
            System.out.println("Seleccione una opcion");
            System.out.println("1. Buscar archivo");
            System.out.println("2. Salir");

            seguir=objsc.nextInt();

            while(seguir != 2){
                System.out.println("Ingresa el nombre del archivo que deseas descargar de los demas clientes:");
                String fileNameToDownload = br.readLine();
                oos.writeObject(fileNameToDownload);

                System.out.println("Esperando respuesta del servidor...");

                ArrayList<FileInfo> peers= new ArrayList<FileInfo>();
                peers = (ArrayList<FileInfo>)ois.readObject();

                for(int i=0;i<peers.size();i++)
                {
                    int result = peers.get(i).peerid;
                    int port = peers.get(i).portNumber;
                    System.out.println("Este archivo esta almacenado en el cliente con ID " +result+ " en el puerto "+port);
                }

                System.out.println("Ingrese el puerto del ID anterior: ");
                int clientAsServerPortNumber = Integer.parseInt(br.readLine());

                System.out.println("Ingrese el ID del cliente del cual descargara el archivo: ");
                int clientAsServerPeerid = Integer.parseInt(br.readLine());

                clientAsServer(clientAsServerPeerid,clientAsServerPortNumber,fileNameToDownload,directoryPath);

                System.out.println("");
                System.out.println("Seleccione una opcion");
                System.out.println("1. Buscar archivo");
                System.out.println("2. Salir");

                seguir=objsc.nextInt();
            }

            System.exit(0);
        }
        catch(Exception e)
        {
            System.out.println("Error en la conexion cliente servidor");
            System.out.println("Verifique la direccion y el puerto del host");
        }
    }

    public static void clientAsServer(int clientAsServerPeerid, int clientAsServerPortNumber, String fileNamedwld, String directoryPath) throws ClassNotFoundException
    {
        try {
            @SuppressWarnings("resource")
            Socket clientAsServersocket = new Socket("localhost",clientAsServerPortNumber);

            ObjectOutputStream clientAsServerOOS = new ObjectOutputStream(clientAsServersocket.getOutputStream());
            ObjectInputStream clientAsServerOIS = new ObjectInputStream(clientAsServersocket.getInputStream());

            clientAsServerOOS.writeObject(fileNamedwld);
            int readBytes=(int) clientAsServerOIS.readObject();

            //System.out.println("Number of bytes that have been transferred are ::"+readBytes);

            byte[] b=new byte[readBytes];
            clientAsServerOIS.readFully(b);
            OutputStream  fileOPstream = new FileOutputStream(directoryPath+"//"+fileNamedwld);

            @SuppressWarnings("resource")

            BufferedOutputStream BOS = new BufferedOutputStream(fileOPstream);
            BOS.write(b, 0,(int) readBytes);

            System.out.println("El archivo -> "+fileNamedwld+ ", ha sido descargado al directorio "+directoryPath);
            System.out.println(" ");
            System.out.println("Nombre en pantalla: "+fileNamedwld);

            BOS.flush();
        }
        catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
