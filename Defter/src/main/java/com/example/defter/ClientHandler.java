package com.example.defter;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler  extends Thread{
    private Socket clientSocket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;



    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }
    @Override
    public void run() {
        try {

            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            int i =1;
            while (true) {
                String message = reader.readLine();
                if (message.equals("GETUSERLIST")){
                    server.getUserList(this);
                }
                if (message.equals("GETANBARLIST")){
                    server.getAnbarList(this);
                }
                if (message.equals("GETMALLIST")){
                    server.getMalList(this);
                }
                if (message.equals("GETKREDITORLIST")){
                    server.getKreditorList(this);
                }if (message.equals("GETDEBITORLIST")){
                    server.getDebitorList(this);
                }if (message.equals("INSERTNEWMAL")){
                    while (true){
                        String command= reader.readLine();
                        if (command.equals("DONE")){
                            break;
                        }
                        String [] splitValues = command.split("/");
                        if (splitValues.length==6){
                            Long barcode= Long.parseLong(splitValues[0]);
                            String code= splitValues[1];
                            String name= splitValues[2];
                            double maya = Double.parseDouble(splitValues[3]);
                            double satish1 = Double.parseDouble(splitValues[4]);
                            double satish2 = Double.parseDouble(splitValues[5]);
                            server.insertNewMal(barcode,code,name,maya,satish1,satish2);
                        }
                    }
                }if (message.equals("UPDATEMALSIYAHI")){
                    int id = Integer.parseInt(reader.readLine());
                    Long barcode = Long.parseLong(reader.readLine());
                    String code= reader.readLine();
                    String ad = reader.readLine();
                    double maya= Double.parseDouble(reader.readLine());
                    double satish1 = Double.parseDouble(reader.readLine());
                    double satish2 = Double.parseDouble(reader.readLine());
                    server.updateMal(id,barcode,code,ad,maya,satish1,satish2);
                }if (message.equals("INSERTDEBITOR")){
                    String ad= reader.readLine();
                    String nomre =reader.readLine();
                    double borc = Double.parseDouble(reader.readLine());
                    server.insertDebitor(ad,nomre,borc);
                }if (message.equals("INSERTKREDITOR")){
                    String ad = reader.readLine();
                    double borc = Double.parseDouble(reader.readLine());
                    server.insertKreditor(ad,borc);
                }if (message.equals("GETMEDAXILLIST")){
                    server.getMedaxilList(this);
                }if (message.equals("INSERTMEDAXIL")){
                    String tarix = reader.readLine();
                    int id = Integer.parseInt(reader.readLine());
                    server.insertMedaxil(this,tarix,id);
                }if (message.equals("INSERTMEDAXILFAKTURA")){
                    while (true){
                        String command = reader.readLine();

                        if (command.equals("DONE")){
                            break;
                        }
                        String [] splitValues = command.split("/");
                        if (splitValues.length==5){
                            System.out.println("inserting");
                            int malNr = Integer.parseInt(splitValues[0]);
                            double miqdar= Double.parseDouble(splitValues[1]);
                            double satish = Double.parseDouble(splitValues[2]);
                            double mebleg = Double.parseDouble(splitValues[3]);
                            int medaxilNr = Integer.parseInt(splitValues[4]);
                            server.insertMedaxilFaktura(malNr,miqdar,satish,mebleg,medaxilNr);
                        }

                    }
                }if (message.equals("GETMEDAXILFAKTURAINFO")){
                    int nr = Integer.parseInt(reader.readLine());
                    server.queryMedaxilFaktura(this,nr);
                }if (message.equals("GETMEXARICINFO")){
                    int nr = Integer.parseInt(reader.readLine());
                    server.queryMexaricFaktura(this,nr);
                }
                if (message.equals("GETMEXARICLIST")){
                    server.getMexaricList(this);
                }if (message.equals("INSERTMEXARIC")){
                    String tarix= reader.readLine();
                    int id= Integer.parseInt(reader.readLine());
                    server.insertMexaric(this,tarix,id);
                }if (message.equals("INSERTMEXARICFAKTURA")) {
                    while (true) {
                        String command = reader.readLine();

                        if (command.equals("DONE")) {
                            break;
                        }
                        String[] splitValues = command.split("/");
                        if (splitValues.length == 5) {
                            int malNr = Integer.parseInt(splitValues[0]);
                            double miqdar = Double.parseDouble(splitValues[1]);
                            double satish = Double.parseDouble(splitValues[2]);
                            double mebleg = Double.parseDouble(splitValues[3]);
                            int mexaricNr = Integer.parseInt(splitValues[4]);
                            server.insertMexaricFaktura(malNr, miqdar, satish, mebleg, mexaricNr);
                        }

                    }
                }if (message.equals("GETANBARMOVCUD")){
                    int nr = Integer.parseInt(reader.readLine());
                    server.selectAnbar(this,nr);
                }if (message.equals("GETKREDITORINFO")){
                    int nr = Integer.parseInt(reader.readLine());
                    server.getKreditorMedaxil(this,nr);
                }
                if (message.equals("EXIT")) {
                    server.handleExit(this);
                    clientSocket.close();
                    break;
                }if (message.equals("GETDEBITORINFOMEXARIC")){
                    int nr = Integer.parseInt(reader.readLine());
                    server.getDebitorMexaric(this,nr);
                }if (message.equals("GETKASSALIST")){
                    server.getKassaList(this);
                }if (message.equals("INSERTKASSA")){
                    String command = reader.readLine();
                    String [] splitvalues =command.split("/");
                    if (splitvalues.length ==6){
                        String tarix= splitvalues[0];
                        int id = Integer.parseInt(splitvalues[1]);
                        double mebleg= Double.parseDouble(splitvalues[2]);
                        String qeyd = splitvalues[3];
                        String cmm= splitvalues[4];
                        String nov= splitvalues[5];
                        System.out.println(cmm);
                        server.insertKassa(tarix,nov,qeyd,id,mebleg,cmm);

                    }
                }if (message.equals("GETKASSATOTAL")){
                    server.getTotal(this);
                }if (message.equals("UPDATEANBARITEM")){
                    int nr = Integer.parseInt(reader.readLine());
                    double miqdar = Double.parseDouble(reader.readLine());
                    double mebleg = Double.parseDouble(reader.readLine());
                    server.updateAnbarItem(nr,miqdar,mebleg);


                }if (message.equals("INSERTANBAR")){
                    while (true){
                        String command = reader.readLine();
                        if (command.equals("DONE")){
                            break;
                        }
                        String [] splitValues = command.split("/");
                        int nr= Integer.parseInt(splitValues[0]);
                        double miqdar= Double.parseDouble(splitValues[1]);
                        double mebleg= Double.parseDouble(splitValues[2]);
                        server.insertAnbar(nr,miqdar,mebleg);
                    }
                }if (message.equals("DELETEMEDAXIL")){
                    int nr= Integer.parseInt(reader.readLine());
                    int kreditorId= Integer.parseInt(reader.readLine());
                    server.handleDeleteMedaxil(nr,kreditorId);
                }if (message.equals("DELETEMEXARIC")){
                    int nr= Integer.parseInt(reader.readLine());
                    int debitorId=Integer.parseInt(reader.readLine());
                    server.deleteMexaric(nr,debitorId);
                }if (message.equals("INSERTUSER")){
                    String user= reader.readLine();
                    String password= reader.readLine();
                    server.insertUser(user,password);
                }if (message.equals("UPDATEUSERINFO")){
                    int nr = Integer.parseInt(reader.readLine());
                    String name = reader.readLine();
                    String password = reader.readLine();
                    server.updateUser(nr,name,password);
                }if (message.equals("DELETEMEXARICINFO")){
                    int nr = Integer.parseInt(reader.readLine());
                    int debitorId= Integer.parseInt(reader.readLine());
                    server.deleteMexaricInfo(nr,debitorId,this);

                }if (message.equals("DELETEMEDAXILINFO")){
                    int nr = Integer.parseInt(reader.readLine());
                    int kreditorId= Integer.parseInt(reader.readLine());
                    server.deleteMedaxilInfo(nr,kreditorId,this);
                }if (message.equals("GETMAL")){
                    int nr = Integer.parseInt(reader.readLine());
                    server.getMal(nr,this);
                }if (message.equals("EDITDEBITOR")){
                    int nr = Integer.parseInt(reader.readLine());
                    String name = reader.readLine();
                    String nomre = reader.readLine();
                    Double borc = Double.parseDouble(reader.readLine());
                    server.updateDebitor(nr,name,nomre,borc);
                }if (message.equals("GETALLMEXARICINFO")){
                    server.queryAllMexaricInfo(this);
                }
                System.out.println("recieved Message " + message);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try {
                reader.close();
                writer.close();
                objectOutputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Socket getClientSocket() {
        return clientSocket;
    }

    public synchronized void sendListResponse(List<?> List) {
        try {
            objectOutputStream.writeObject(List);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendObject(Object object){
        try {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public synchronized List<?> objectReader(){
        try {
            return (List<?>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void sendMessage(String message){
        try {
            writer.println(message);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
