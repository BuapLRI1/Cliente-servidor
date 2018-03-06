package practicachat.cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
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
    JTextField campoTexto;
    JTextArea areaTexto;
    private static ServerSocket servidor;
    private static Socket cliente;
    private static String ip = "127.0.0.1";
    
    public static PrincipalChat main;
    
    public PrincipalChat(){
        super("Cliente");
        
        campoTexto = new JTextField();
        campoTexto.setEditable(false);
        add(campoTexto, BorderLayout.NORTH);
        
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        
        setSize(680,600);
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
            main.mostrarMensaje("Buscando Servidor ...");
            cliente = new Socket(InetAddress.getByName(ip), 11111); //comunicarme con el servidor
            main.mostrarMensaje("Conectado a :" + cliente.getInetAddress().getHostName());
    
            main.habilitarTexto(true); //habilita el texto
            
            //Ejecucion de los Threads
            executor.execute(new ThreadRecibe(cliente, main));
            executor.execute(new ThreadEnvia(cliente, main)); 
            
        } catch (IOException ex) {
            Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE, null, ex);
        } //Fin del catch
        finally {
        }
        executor.shutdown();
    }
    }

