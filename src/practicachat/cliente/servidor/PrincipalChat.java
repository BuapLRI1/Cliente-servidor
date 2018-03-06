package practicachat.cliente.servidor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class PrincipalChat extends JFrame{
    public JTextField campoTexto; 
    public JTextArea areaTexto; 
    private static ServerSocket servidor; 
    private static Socket conexion;
    private static String ip = "127.0.0.1"; 
    
    public static PrincipalChat main; 
    
    public PrincipalChat(){
         super("Servidor");
        
        campoTexto = new JTextField();
        campoTexto.setEditable(false);
        add(campoTexto, BorderLayout.NORTH);
        
        areaTexto = new JTextArea(); 
        areaTexto.setEditable(false);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        
        setSize(680, 600); 
        setVisible(true); 
    }
    public void mostrarMensaje(String mensaje) {
        areaTexto.append(mensaje + "\n");
    } 
    public void habilitarTexto(boolean editable) {
        campoTexto.setEditable(editable);
    }
 
    public static void main(String[] args){
        PrincipalChat main = new PrincipalChat();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ExecutorService executor = Executors.newCachedThreadPool(); 
        
        try {
            //main.mostrarMensaje("No se encuentra Servidor");
            servidor = new ServerSocket(11111, 100); 
            main.mostrarMensaje("Esperando Cliente ...");

            //Bucle infinito para esperar conexiones de los clientes
            while (true){
                try {
                    conexion = servidor.accept(); //Permite al servidor aceptar conexiones        

                    //main.mostrarMensaje("Conexion Establecida");
                    main.mostrarMensaje("Conectado a : " + conexion.getInetAddress().getHostName());

                    main.habilitarTexto(true); //permite escribir texto para enviar

                    //Ejecucion de los threads
                    executor.execute(new ThreadRecibe(conexion, main)); //client
                    executor.execute(new ThreadEnvia(conexion, main));
                } catch (IOException ex) {
                    Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE, null, ex);
        } //Fin del catch
        finally {
        }
        executor.shutdown();
    }
        
}
