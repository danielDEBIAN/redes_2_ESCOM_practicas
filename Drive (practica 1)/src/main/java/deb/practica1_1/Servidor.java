package deb.practica1_1;
import static deb.practica1_1.Servidor.zipFile;
import java.net.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.lingala.zip4j.*;

public class Servidor {
    public static String sep = System.getProperty("file.separator");
    private static String rutaServer = "." + sep + "servidor" + sep;
    private static File[] list;
    private static String rutaActual = "";

    public static void RecibirArchivos(DataInputStream dis, String nombre) throws IOException {
	long tam = dis.readLong();
	String pathDestino = dis.readUTF();
	nombre = rutaServer + pathDestino;
		
	DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre)); // OutputStream
		
	long recibidos = 0;
	int n = 0, porciento = 0;
	byte[] b = new byte[2000];

	while(recibidos < tam) {
            n = dis.read(b);
            dos.write(b, 0, n);
            dos.flush();
            recibidos += n;
            porciento = (int)((recibidos * 100) / tam);
	} // while
                
	dos.close();
	dis.close();
    } // RecibirArchivos

    public static void ActualizarCliente(Socket cl, DataInputStream dis, String path, int bandera) throws IOException {
	File archivosRuta = new File(path);
		
	if(!archivosRuta.exists()) {
            archivosRuta.mkdir();
	}//if

	if(bandera == 1) {
            rutaActual = rutaActual + sep + archivosRuta.getName();
        }

        list = archivosRuta.listFiles();
	DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); // OutputStream
        dos.writeInt(list.length);
	dos.flush();

	String info = "";
	int tipo = 0;

        for (File f : list) {
            if (f.isDirectory()) { 
                tipo = 1;
                if(bandera == 0) {
                    info = "." + sep + f.getName();
                }
                else {
                    info = "." + rutaActual + sep + f.getName();
                }
            }//if
            else { 
            	tipo = 2;
            	if(bandera == 0){
                    info = f.getName();            		
            	}
            	else {
                    info = "." + rutaActual + sep + f.getName();
            	}
            }//else
            dos.writeUTF(info);
            dos.flush();   
            dos.writeInt(tipo);
            dos.flush();
            tipo = 0;
        }//for
        dos.close();
	}//Actualizar
	
    public static void crearZIP(DataInputStream dis, int tam) throws IOException {
	try {
            String[] nombreArchivos = new String[tam];
            String aux = "";
            int i, j;
            for(i = 0; i < tam; i++) {
		nombreArchivos[i] = dis.readUTF();
            }

            char aux1 = ' ', aux2 = ' ';
            String nombre = ""; 
            for(i = 0; i < tam; i++) {
		aux1 = nombreArchivos[i].charAt(0);
		if( aux1 == '.') {
                    for(j = 2; j < nombreArchivos[i].length(); j++)
			nombre = nombre + Character.toString(nombreArchivos[i].charAt(j));
                        nombreArchivos[i] = nombre;
			nombre = "";
                    }
            }
            String destino = rutaServer + "Download" + ".zip";
            FileOutputStream fos = new FileOutputStream(destino);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            String sourceFile = "";
            for(i = 0; i < tam; i++) {
		sourceFile = rutaServer + nombreArchivos[i];
		File fileToZip = new File(sourceFile);
		zipFile(fileToZip, fileToZip.getName(), zipOut);
		sourceFile = " ";
            }
            zipOut.close();
            fos.close();
               
        }catch(Exception e) {
            e.printStackTrace();
            }
    }

    public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            if (fileName.endsWith(sep)) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } 
            else {
                zipOut.putNextEntry(new ZipEntry(fileName + sep));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + sep + childFile.getName(), zipOut);
            }
            return;
        }
        
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;

        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }

        fis.close();
    }

    public static void EnviarArchivo(DataOutputStream dos, File f) {
        try {
            String nombre = f.getName();
            long tam = f.length();
            String path = f.getAbsolutePath();
            DataInputStream disArchivo = new DataInputStream(new FileInputStream(path)); // InputStream
            dos.writeUTF(nombre); 
            dos.flush();
            dos.writeLong(tam);	
            dos.flush();

            long enviados = 0;
            int n = 0, porciento = 0;
            byte[] b = new byte[2000];

            while(enviados < tam) {
                n = disArchivo.read(b);
                dos.write(b, 0, n);
                dos.flush();
                enviados += n;
                porciento = (int)((enviados * 100) / tam);
            } //while
            disArchivo.close();
            dos.close();
		} // try
		catch(Exception e) {
			e.printStackTrace();
		}
	} // Enviar archivo

    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(4444);
            s.setReuseAddress(true);
            System.out.println("Servidor de archivos iniciado, esperando cliente...");
            for( ; ; ) {
		Socket cl = s.accept();
		System.out.println("\n\nCliente conectado desde " + cl.getInetAddress() + " " + cl.getPort());
		DataOutputStream dos = new DataOutputStream(cl.getOutputStream()); //OutputStream
		DataInputStream dis = new DataInputStream(cl.getInputStream()); // InputStream

		int bandera = dis.readInt();

		if(bandera == 0) {
                    String nombre = dis.readUTF();
                    RecibirArchivos(dis, nombre);
		}
		else if (bandera == 1) {
                    rutaActual = "";
                    ActualizarCliente(cl, dis, rutaServer, 0);
                }
		else if (bandera == 2) {
                    int tam = dis.readInt();
                    String path = "Download" + ".zip";
                    path = rutaServer + path;
                    File archivoZip = new File(path);
                    
                    crearZIP(dis, tam);

                    if(archivoZip.exists()) {
                        EnviarArchivo(dos, archivoZip);
                    }
                                        
                    String source = "D:/Documentos/ESCOM/6toSemestre/AplicacionesRd/practica1_1/servidor/Download.zip";
                    String destination = "D:/Documentos/ESCOM/6toSemestre/AplicacionesRd/Cliente/";   
                    ZipFile archivo = new ZipFile(source);
                    archivo.extractAll(destination);
                }
		else if (bandera == 3) {
                    int ubicacionRuta = dis.readInt();			
                    String nuevaRuta = "" + list[ubicacionRuta].getAbsoluteFile();
                    ActualizarCliente(cl, dis, nuevaRuta, 1);
		}
		else if(bandera == 4) {
                    String rutaDirectorio = dis.readUTF();
                    String path = rutaServer + rutaDirectorio;
                    File archivosRuta = new File(path);
                    if(!archivosRuta.exists()) {
			archivosRuta.mkdir();
                    }
		}
		else {
                    System.out.println("Error al atender la solicitud del cliente.");
		}
		dis.close(); 
		cl.close();
            }//for
	}catch(Exception e) {
            e.printStackTrace();
	}//catch
    }//main
}