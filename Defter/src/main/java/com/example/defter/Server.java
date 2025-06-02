package com.example.defter;

import com.example.defter.Data.Data;
import com.example.defter.Data.LogData;
import com.example.defter.Model.Anbar;
import com.example.defter.Model.Mal;
import com.example.defter.Model.MexaricFaktura;
import com.example.defter.Model.Total;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private  Data data;
    private LogData logData;
    private List<ClientHandler> clients= new ArrayList<>();


    public void start(int port){
        try {
            data =new Data();
            logData= new LogData();
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println(serverSocket.getInetAddress().getHostAddress());
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket,this);
                clients.add(clientHandler);
                clientHandler.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserList(ClientHandler clientHandler){
        logData.open();
        clientHandler.sendListResponse(logData.queryLogin());
        logData.close();
    }
    public void getAnbarList(ClientHandler clientHandler){
        data.open();
        clientHandler.sendListResponse(data.queryAnbar());
        data.close();
    }
    public void getMalList(ClientHandler clientHandler){
        data.open();
        clientHandler.sendListResponse(data.queryMal());
        data.close();
    }
    public void getKreditorList(ClientHandler clientHandler){
        data.open();
        clientHandler.sendListResponse(data.queryKreditor());
        data.close();
    }
    public void getDebitorList(ClientHandler clientHandler){
        data.open();
        clientHandler.sendListResponse(data.queryDebitor());
        data.close();
    }
    public void getMedaxilList(ClientHandler clientHandler){
        data.open();
        clientHandler.sendListResponse(data.queryMedaxil());
        data.close();
    }
    public void getMexaricList(ClientHandler clientHandler){
        data.open();
        clientHandler.sendListResponse(data.queryMexaric());
        data.close();
    }
    public void insertNewMal(Long barcode,String code,String ad,double maya,double satish1,double satish2){
        data.open();
        data.insertNewMal(barcode,code,ad,maya,satish1,satish2);
        data.close();
    }
    public void updateMal(int id, Long barcode,String code,String ad,double maya,double satish1,double satish2){
        data.open();
        data.updateMal(id, barcode, code, ad, maya, satish1, satish2);
        data.close();
    }

    public void insertDebitor(String ad, String nomre,double borc){
        data.open();
        data.insertDebitor(ad,nomre,borc);
        data.close();
    }

    public void insertKreditor(String ad,double borc){
        data.open();
        data.insertKreditor(ad,borc);
        data.close();
    }

    public void insertMedaxil(ClientHandler clientHandler,String tarix,int id){
        data.open();
        int nr = data.insertMedaxil(tarix,id);
        clientHandler.sendMessage(String.valueOf(nr));
        data.close();
    }

    public void insertMedaxilFaktura(int malNr,double miqdar , double satishQiymet, double mebleg, int medaxilNr){
        data.open();
        data.insertMedaxilFaktura(malNr,miqdar,satishQiymet,mebleg,medaxilNr);
        data.close();
    }
    public void insertMexaric(ClientHandler clientHandler,String tarix,int id){
        data.open();
        int nr = data.insertMexaric(tarix,id);
        data.close();
        clientHandler.sendMessage(String.valueOf(nr));
    }
    public void insertMexaricFaktura(int malNr,double miqdar , double satishQiymet, double mebleg, int mexaricNr){
        data.open();
        data.insertMexaricFaktura(malNr,miqdar,satishQiymet,mebleg,mexaricNr);
        data.close();
    }

    public void selectAnbar(ClientHandler clientHandler, int id){
        data.open();
        Anbar anbar = data.selectAnbar(id);
        data.close();
        clientHandler.sendObject(anbar);
    }
    public void getTotal(ClientHandler clientHandler){
        data.open();
        Total total= data.getTotal();
        data.close();
        clientHandler.sendObject(total);
    }


    public void queryMedaxilFaktura(ClientHandler clientHandler, int nr){
        data.open();
        clientHandler.sendListResponse(data.queryMedaxilFaktura(nr));
        data.close();
    }
    public void queryMexaricFaktura(ClientHandler clientHandler , int nr){
        data.open();
        clientHandler.sendListResponse(data.queryMexaricFaktura(nr));
        data.close();
    }
    public void handleExit(ClientHandler clientHandler){
        clients.remove(clientHandler);
        System.out.println("Client left " + clientHandler.getClientSocket().getInetAddress().getHostAddress());
    }

    public void getKreditorMedaxil(ClientHandler clientHandler, int nr){
        data.open();
        clientHandler.sendListResponse(data.getKreditorMedaxils(nr));
        data.close();
    }
    public void getDebitorMexaric(ClientHandler clientHandler, int nr){
        data.open();
        clientHandler.sendListResponse(data.getDebitorMexaric(nr));
        data.close();
    }
    public void getKassaList(ClientHandler clientHandler){
        data.open();
        clientHandler.sendListResponse(data.queryKassa());
        data.close();
    }
    public void insertKassa(String tarix,String nov,String qeyd,int id,double mebleg,String command){
        data.open();
        data.insertKassa(tarix,nov,qeyd,id,mebleg,command);
        data.close();
    }
    public void updateAnbarItem(int id,double miqdar,double mebleg){
        data.open();
        data.updateAnbarItem(id,miqdar,mebleg);
        data.close();
    }
    public void insertAnbar(int id,double miqdar,double mebleg){
        data.open();
        data.insertAnbar(id,miqdar,mebleg);
        data.close();
    }
    public void handleDeleteMedaxil(int medaxilNr,int kreditorId){
        data.open();
        data.deleteMedaxil(medaxilNr,kreditorId);
        data.close();
    }
    public void deleteMexaric(int nr,int debitorId){
        data.open();
        data.deleteMexaric(nr,debitorId);
        data.close();

    }
    public void insertUser(String user,String password){
        logData.open();
        logData.insertUser(user,password);
        logData.close();
    }
    public void updateUser(int nr,String user, String password){
        logData.open();
        logData.updateUser(nr,user,password);
        logData.close();
    }

    public void deleteMexaricInfo(int mexaricNr,int debitorId , ClientHandler clientHandler){
        data.open();
        int result = data.deleteMexaricFaktura(mexaricNr,debitorId);
        data.close();
        clientHandler.sendMessage(String.valueOf(result));
    }
    public void deleteMedaxilInfo(int medaxilNr,int kreditorNr , ClientHandler clientHandler){
        data.open();
        int result = data.deleteMedaxilFaktura(medaxilNr,kreditorNr);
        data.close();
        clientHandler.sendMessage(String.valueOf(result));
    }

    public void getMal(int nr, ClientHandler clientHandler){
        data.open();
        Mal mal = data.getMal(nr);
        data.close();
        clientHandler.sendObject(mal);
    }

    public void updateDebitor(int id,String debitor,String nomre, double borc){
        data.open();
        data.updateDebitor(id,debitor,nomre,borc);
        data.close();
    }
    public void queryAllMexaricInfo(ClientHandler clientHandler){
        data.open();
       List<MexaricFaktura> mexaricFakturaList =   data.queryAllMexaricFaktura();
       data.close();
       clientHandler.sendListResponse(mexaricFakturaList);
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.start(4444);
    }
}
