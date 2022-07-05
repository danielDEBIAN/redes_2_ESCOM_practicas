package deb.practica1_1;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;	
import java.io.*;

public class interfaz extends JFrame implements ActionListener {
        JButton BtnSubir, BtnActualizar, BtnDescargar, BtnDelete, BtnOtroPanel;
	static JList<String> archivos;
	static DefaultListModel<String> modelo;
        static DefaultListModel<String> modelo2;
	MouseListener mouseListener;
        MouseListener mouseListener2;
	JPanel panelBotones;
	JScrollPane scroll;
        JScrollPane scroll2; 
	//File list[];

	public interfaz() {
            Container c = getContentPane();
            c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

            archivos = new JList<String>();
            archivos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = archivos.locationToIndex(e.getPoint());
                    String nombreSeleccion = modelo.getElementAt(index);

                    if (Cliente.tipoFile[index] == 1) {
                        modelo.clear();
                        Cliente.AbrirCarpeta(index);
                    }
                }
            }
        };
        
        archivos.addMouseListener(mouseListener);
        modelo = new DefaultListModel<>();
	Cliente.Actualizar();
	archivos.setModel(modelo);
		
        scroll = new JScrollPane(archivos);
        scroll.setMinimumSize(new Dimension(100, 200));
       
        c.add(scroll);
  
	panelBotones = new JPanel();
	panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

	BtnSubir = new JButton("Subir Archivo");
	BtnActualizar = new JButton("Actualizar");
	BtnDescargar = new JButton("Descargar Archivos");
        BtnDelete = new JButton("Borrar");
        BtnOtroPanel = new JButton("Cliente");

	panelBotones.add(BtnSubir);
	panelBotones.add(BtnActualizar);
	panelBotones.add(BtnDescargar);
        panelBotones.add(BtnDelete);
        panelBotones.add(BtnOtroPanel);

	c.add(panelBotones);

	BtnSubir.addActionListener(this);
	BtnActualizar.addActionListener(this);
	BtnDescargar.addActionListener(this);
        BtnDelete.addActionListener(this);
        BtnOtroPanel.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
	JButton b = (JButton) e.getSource();
		    
	if(b == BtnSubir) {
            Cliente.SeleccionarArchivos();
	}
	else if(b == BtnActualizar) {
            modelo.clear();
            Cliente.Actualizar();
        }
	else if(b == BtnDescargar) {
            File zp = new File("D:\\Documentos\\ESCOM\\6toSemestre\\AplicacionesRd\\practica1_1\\servidor\\Download.zip");
            zp.delete();
                        
            if(!archivos.isSelectionEmpty()) {
		int[] indices = archivos.getSelectedIndices();	
                String[] nombreSeleccion = new String[indices.length];

		for(int i = 0; i < indices.length; i++) {
                    nombreSeleccion[i] = modelo.getElementAt(indices[i]);
		}
                Cliente.RecibirArchivos(nombreSeleccion, indices.length);
            }
        else if(b == BtnDelete){
            if(!archivos.isSelectionEmpty()){
                int [] indices = archivos.getSelectedIndices();
                String[] nombreSeleccion = new String[indices.length];
                
                for(int i = 0 ; i < indices.length ;i++){
                    nombreSeleccion[i] = modelo.getElementAt(indices[i]);
                }
            }
        }
        else if(b == BtnOtroPanel){
             
        }
        else {
            JOptionPane.showMessageDialog(null, "Seleccione archivos para descargarlos.");
        }
            }
	}

    public static void main(String s[]) {
	interfaz f = new interfaz();
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.setSize(700,500);
	f.setVisible(true);
	f.setLocationRelativeTo(null);
    }
}