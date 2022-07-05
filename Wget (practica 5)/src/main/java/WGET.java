import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class WGET {
    static String urlAbsoluto;
    public static ArrayList<String> urlsAbsolutas = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.print("URL: ");
        urlAbsoluto = scan.nextLine();
        urlsAbsolutas.add(urlAbsoluto);
        URL url = new URL(urlsAbsolutas.get(urlsAbsolutas.size()-1));
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    getFile(url);
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        t1.start();
        t1.join();
        urlsAbsolutas.remove(urlsAbsolutas.size()-1);
    }

    private static void getFile(URL url) throws IOException, DataFormatException, InterruptedException{

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String contentType = conn.getHeaderField("Content-Type");
        if (contentType == null) contentType = "";
        if (contentType.indexOf("ISO-8859-1") >= 0 && conn.getResponseCode() == 200) {            // no es archivo
            Document html = Jsoup.connect(url.toString()).get();
            Elements hipervinculos = html.getElementsByTag("a");
            int i = 0;
            for (Element vinculo : hipervinculos) {
                if (i > 4) {
                    String liga = vinculo.attr("href");
                    urlsAbsolutas.add(urlsAbsolutas.get(urlsAbsolutas.size()-1)+liga);
                    URL urlAndidado = new URL(urlsAbsolutas.get(urlsAbsolutas.size()-1));
                    Thread t2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                getFile(urlAndidado);
                                urlsAbsolutas.remove(urlsAbsolutas.size()-1);
                            }catch(Exception e){
                                System.out.println(e);
                            }
                        }
                    });
                    t2.start();
                    t2.join();
                }
                i++;
            }
        } else {                                                // es archivo
            String[] arrOfStr = url.getFile().split("/");
            String nombreArchivo = arrOfStr[arrOfStr.length-1];
            String[] arrRuta = Arrays.copyOf(arrOfStr, arrOfStr.length-1);
            String ruta = "./";
            for (String direc : arrRuta) {
                ruta = ruta.concat("/"+direc);
            }
            crearCarpeta(ruta);
            try ( BufferedInputStream in = new BufferedInputStream(url.openStream());
                  FileOutputStream fileOutputStream = new FileOutputStream(ruta+"/"+nombreArchivo) ) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println(nombreArchivo +" descargado");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private static void crearCarpeta(String directorio) throws DataFormatException {
        if (directorio != null) {
            if (directorio.matches("[-_. A-Za-z0-9áéíóúÁÉÍÓÚ/]+")) {
                File file = new File(directorio);
                if (file.mkdirs()) {
                    //System.out.println("Directorio creado: " + file.getPath());
                } else {
                    //System.out.println("Error al crear el directorio o ya existe uno con el mismo nombre.");
                }
            } else {
                throw new DataFormatException("Error: El nombre del directorio contiene caracteres invalidos");
            }
        } else {
            throw new DataFormatException("Error: El nombre del directorio esta vacio");
        }
    }
}
