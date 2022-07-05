package deb.practica1_1;
import javax.swing.JFileChooser;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/*Funciones del cliente que haran las peticiones que se requieran al servidor*/
public class Cliente {
    private static int pto = 4444;
    private static String host = "127.0.0.1";
    private static String rutaDirectorios = "";
    public static String sep = System.getProperty("file.separator");
    public static int[] tipoFile;

    public static void AbrirCarpeta(int indice){
        try {
            Socket cl = new Socket(host, pto);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); //OutputStream
            
            dos.writeInt(3);
            dos.flush();

            dos.writeInt(indice);
            dos.flush();

            DataInputStream dis = new DataInputStream(cl.getInputStream()); // InputStream

            int numArchivos = dis.readInt();
            tipoFile = new int[numArchivos];

            for(int i = 0; i < numArchivos; i++) {
		String archivoRecibido = dis.readUTF();
		interfaz.modelo.addElement(archivoRecibido);
		tipoFile[i] = dis.readInt();
            }//for

            dis.close();
            dos.close();
            cl.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}//catch
    }
    
    public static void EnviarArchivo(File f, String pathOrigen, String pathDestino) {
        try {
            if(f.isFile()) {
		Socket cl = new Socket(host, pto);
		DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); //OutputStream
                String nombre = f.getName();
	        long tam = f.length();
	        DataInputStream dis = new DataInputStream(new FileInputStream(pathOrigen)); // InputStream
                
                dos.writeInt(0); 
                dos.flush();
	        dos.writeUTF(nombre); 
                dos.flush();
	        dos.writeLong(tam);	
                dos.flush();
	        dos.writeUTF(pathDestino); 
                dos.flush();

	        long enviados = 0;
	        int pb = 0;
	        int n = 0, porciento = 0;
                byte[] b = new byte[2000];

	        while(enviados < tam) {
                    n = dis.read(b);
                    dos.write(b, 0, n);
                    dos.flush();
                    enviados += n;
                    porciento = (int)((enviados * 100) / tam);	                
	        } //while

	        JOptionPane.showMessageDialog(null, "Se ha subido el archivo " + nombre);
	        dis.close(); 
	        dos.close(); 
	        cl.close();
            } // If
            else {
		Socket cl = new Socket(host, pto);
		DataOutputStream dos = new DataOutputStream(cl.getOutputStream());

		String nombre = f.getName();
		String ruta = f.getAbsolutePath();

		String aux = rutaDirectorios;
		rutaDirectorios = rutaDirectorios + sep + nombre;
	
	        dos.writeInt(4);
	        dos.flush();

	        dos.writeUTF(rutaDirectorios);
	        dos.flush();

                File folder = new File(ruta);
		File[] files = folder.listFiles();

		for(File file : files)	{
	            String path = rutaDirectorios + sep + file.getName();
	            EnviarArchivo(file, file.getAbsolutePath(), path);
                }// for

                rutaDirectorios = aux;
	        dos.close();
                cl.close();
            } // Else		
        } // try
        catch(Exception e) {
                e.printStackTrace();
            }
	} // Enviar archivo

	// Envia muchos archivos al servidor
    public static void SeleccionarArchivos() {
        try {
	    JFileChooser jf = new JFileChooser();
	    jf.setMultiSelectionEnabled(true);
	    jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    int r = jf.showOpenDialog(null);

	    if(r == JFileChooser.APPROVE_OPTION) {	
                rutaDirectorios = "";        	
	        File[] files = jf.getSelectedFiles();
	        for(File file : files)	{
                    String rutaOrigen = file.getAbsolutePath();
                    EnviarArchivo(file, rutaOrigen, file.getName());
                }//for
                interfaz.modelo.clear();
                Actualizar();
	    }//if   
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void Actualizar(){
    	try {
            Socket cl = new Socket(host, pto);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); //OutputStream
            
            dos.writeInt(1);
            dos.flush();

            DataInputStream dis = new DataInputStream(cl.getInputStream()); // InputStream

            int numArchivos = dis.readInt();
            tipoFile = new int[numArchivos];

            for(int i = 0; i < numArchivos; i++) {
                String archivoRecibido = dis.readUTF();
                interfaz.modelo.addElement(archivoRecibido);
                tipoFile[i] = dis.readInt();
            }//for

            dis.close();
            dos.close();
            cl.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}//catch
    }//Actualizar

    public static void RecibirArchivos(String[] nombresArchivos, int tama) {
	try {
            Socket cl = new Socket(host, pto);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); //OutputStream
            DataInputStream dis = new DataInputStream(cl.getInputStream()); // InputStream

            dos.writeInt(2); 
            dos.flush();

            dos.writeInt(tama); 
            dos.flush();
		
            String aux = "";

            for(int i = 0; i < tama; i++) {
                aux = nombresArchivos[i];
                dos.writeUTF(aux);
		dos.flush();
            }
                        
            String nombre = "D:\\Documentos\\ESCOM\\6toSemestre" + sep;
            nombre = nombre + dis.readUTF();

            long tam = dis.readLong();
            DataOutputStream dosArchivo = new DataOutputStream(new FileOutputStream(nombre)); // OutputStream
			
            long recibidos = 0;
            int n = 0, porciento = 0;
            byte[] b = new byte[2000];

            while(recibidos < tam) {
                n = dis.read(b);
		dosArchivo.write(b, 0, n);
		dosArchivo.flush();
		recibidos += n;
		porciento = (int)((recibidos * 100) / tam);
            } // while

            JOptionPane.showMessageDialog(null, "Se han descargado los archivos");
            dos.close();
            dis.close();
            dosArchivo.close();
            cl.close();

    	}catch(Exception e) {
    		e.printStackTrace();
    	}//catch
	}
    
    
}

