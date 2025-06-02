package com.example.defter.Data;

import com.example.defter.Admin.KassaController;
import com.example.defter.Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Data {
    private Connection connection;
    private static final String DB_NAME="Data.db";

    private static final String DB_PATH="jdbc:sqlite:/Users/muhammadfeyzullayev/Documents/Huseynxan/";

    private static final String CONNECTION_STRING=DB_PATH+DB_NAME;
    private PreparedStatement insertMedaxil;
    private PreparedStatement insertMexaric;
    private PreparedStatement queryMedaxilFaktura;
    private PreparedStatement queryMexaricFaktura;
    private PreparedStatement insertMedaxilFaktura;
    private PreparedStatement insertMexaricFaktura;
    private PreparedStatement existsInAnbar;
    private PreparedStatement increaseAnbar;
    private PreparedStatement decreaseAnbar;
    private PreparedStatement insertAnbar;
    private PreparedStatement queryMal;
    private PreparedStatement insertNewMal;
    private PreparedStatement updateMal;
    private PreparedStatement updateDebitor;
    private PreparedStatement insertDebitor;
    private PreparedStatement insertKreditor;
    private PreparedStatement findKreditor;
    private PreparedStatement findDebitor;
    private PreparedStatement findMedaxil;
    private PreparedStatement findMexaric;
    private PreparedStatement increaseKreditorBorc;
    private PreparedStatement decreaseKreditorBorc;
    private PreparedStatement decreaseDebitorBorc;
    private PreparedStatement increaseDebitorBorc;
    private PreparedStatement selectAnbar;
    private PreparedStatement findKreditorMedaxil;
    private PreparedStatement findDebitorMexaric;
    private PreparedStatement insertKassa;
    private PreparedStatement updateAnbarItem;
    private PreparedStatement deleteMedaxil;
    private PreparedStatement deleteMedaxilFaktura;
    private PreparedStatement deleteMexaric;
    private PreparedStatement deleteMexaricFaktura;
    private PreparedStatement setMexaricPrice;
    private PreparedStatement setMedaxilPrice;
    private PreparedStatement setMalMaya;

    private static final String TABLE_TOTAL="Total";
    private static final String COLUMN_TOTAL_KASSA="kassa";
    private final static String TABLE_DEBITOR="Debitor";
    private static final String COLUMN_DEBITOR_ID="id";
    private static final String COLUMN_DEBITOR_AD="ad";
    private static final String COLUMN_DEBITOR_BORC="borc";
    private static final String COLUMN_DEBITOR_NOMRE="nomre";
    private static final String TABLE_KASSA="Kassa";
    private static final String COLUMN_KASSA_ID="id";
    private static final String COLUMN_KASSA_TARIX="tarix";
    private static final String COLUMN_KASSA_NOV="nov";
    private static final String COLUMN_KASSA_QEYD="qeyd";
    private static final String COLUMN_KASSA_MEBLEG="mebleg";
    private static final String TABLE_KREDITOR="Kreditor";
    private static final String COLUMN_KREDITOR_ID="id";
    private static final String COLUMN_KREDITOR_AD="ad";
    private static final String COLUMN_KREDITOR_BORC="borc";
    private static final String TABLE_MAL="Mal";
    private static final String COLUMN_MAL_ID="id";
    private static final String COLUMN_MAL_BARCODE="barcode";
    private static final String COLUMN_MAL_KOD="kod";
    private static final String COLUMN_MAL_AD="ad";
    private static final String COLUMN_MAL_MAYA="maya";
    private static final String COLUMN_MAL_SATISH1="satish1";
    private static final String COLUMN_MAL_SATISH2="satish2";
    private static final String COLUMN_MAL_MIQDAR="miqdar";
    private static final String TABLE_MEDAXIL="medaxil";
    private static final String COLUMN_MEDAXIL_ID="id";
    private static final String COLUMN_MEDAXIL_TARIX="tarix";
    private static final String COLUMN_MEDAXIL_KREDITORID="kreditorId";
    private static final String COLUMN_MEDAXIL_MEBLEG="mebleg";
    private static final String TABLE_MEDAXILFAKTURA="MedaxilFaktura";
    private static final String COLUMN_MEDAXILFAKTUTA_ID="id";
    private static final String COLUMN_MEDAXILFAKTURA_MALNR ="malNr";
    private static final String COLUMN_MEDAXILFAKTURA_MIQDAR ="miqdar";
    private static final String COLUMN_MEDAXILFAKTURA_SATISHQIYMET ="satishQiymet";
    private static final String COLUMN_MEDAXILFAKTURA_MEBLEG ="mebleg";
    private static final String COLUMN_MEDAXILFAKTURA_MEDAXILNR ="medaxilNr";
    private static final String TABLE_MEXARIC="Mexaric";
    private static final String COLUMN_MEXARIC_ID="id";
    private static final String COLUMN_MEXARIC_TARIX="tarix";
    private static final String COLUMN_MEXARIC_DEBITORID="debitorId";
    private static final String COLUMN_MEXARIC_MEBLEG="mebleg";
    private static final String TABLE_MEXARICFAKUTA="mexaricFaktura";
    private static final String COLUMN_MEXARICFAKTURA_ID="id";
    private static final String COLUMN_MEXARICFAKTURA_MALNR="malNr";
    private static final String COLUMN_MEXARICFAKTURA_MIQDAR="miqdar";
    private static final String COLUMN_MEXARICFAKTURA_SATISHQIYMET="satishQiymet";
    private static final String COLUMN_MEXARICFAKTURA_MEBLEG="mebleg";
    private static final String COLUMN_MEXARICFAKTURA_MEXARICID="mexaricID";
    private static final String TABLE_ANBAR="Anbar";
    private static final String COLUMN_ANBAR_ID="id";
    private static final String COLUMN_ANBAR_MALNR="malID";
    private static final String COLUMN_ANBAR_MALADI="malAdi";
    private static final String COLUMN_ANBAR_MIQDAR="miqdar";
    private static final String COLUMN_ANBAR_MEBLEG="mebleg";

    private static final String QUERY_MEDAXIL="SELECT * FROM " + TABLE_MEDAXIL;
    private static final String QUERY_MEXARC="SELECT * FROM " + TABLE_MEXARIC;
    private static final String INSERT_MEDAXIL="INSERT INTO " + TABLE_MEDAXIL + " ( " + COLUMN_MEDAXIL_TARIX + " , " +
            COLUMN_MEDAXIL_KREDITORID + ", " + COLUMN_MEDAXIL_MEBLEG + " ) VALUES (?,?,?)" ;
    private static final String INSERT_MEXARIC="INSERT INTO " + TABLE_MEXARIC + " ( "  +COLUMN_MEXARIC_TARIX + " , " +
            COLUMN_MEXARIC_DEBITORID +  " , " + COLUMN_MEXARIC_MEBLEG + " ) VALUES(?,?,?)";

    private static final String SELECT_ANBAR="SELECT * FROM " + TABLE_ANBAR + " WHERE " + COLUMN_ANBAR_MALNR + " = ?";
    private static final String QUERY_MEDAXILFAKTURA="SELECT * FROM " + TABLE_MEDAXILFAKTURA + " WHERE " + COLUMN_MEDAXILFAKTURA_MEDAXILNR + " =  ? ";
    private static final String QUERY_MEXARICFAKTURA="SELECT * FROM " + TABLE_MEXARICFAKUTA + " WHERE " + COLUMN_MEXARICFAKTURA_MEXARICID + " = ?";
    private static final String INSERT_MEDAXIL_FAKTURA="INSERT INTO " + TABLE_MEDAXILFAKTURA +
            " ( " + COLUMN_MEDAXILFAKTURA_MALNR + ", " + COLUMN_MEDAXILFAKTURA_MIQDAR + " , "+
            COLUMN_MEDAXILFAKTURA_SATISHQIYMET + ", " + COLUMN_MEDAXILFAKTURA_MEBLEG + " , "+
            COLUMN_MEDAXILFAKTURA_MEDAXILNR + ") VALUES (?,?,?,?,?)";
    private static final String INSERT_MEXARIC_FAKTURA="INSERT INTO " + TABLE_MEXARICFAKUTA + " ( "+
            COLUMN_MEXARICFAKTURA_MALNR + " , " + COLUMN_MEXARICFAKTURA_MIQDAR + " , " + COLUMN_MEXARICFAKTURA_SATISHQIYMET + " , " +
            COLUMN_MEXARICFAKTURA_MEBLEG + " , " + COLUMN_MEXARICFAKTURA_MEXARICID + " ) VALUES(?,?,?,?,?)";
    private static final String EXIST_IN_ANBAR="SELECT COUNT(*) FROM " + TABLE_ANBAR + " WHERE " + COLUMN_ANBAR_MALNR + " = ?";

    private static final String INCREASE_ANBAR="UPDATE "+ TABLE_ANBAR + " SET " + COLUMN_ANBAR_MIQDAR + " = " + COLUMN_ANBAR_MIQDAR + " + ? , " + COLUMN_ANBAR_MEBLEG + " = " + COLUMN_ANBAR_MEBLEG +
            " + ? WHERE " + COLUMN_ANBAR_MALNR + " = ? ";
    private static final String DECREASE_ANBAR="UPDATE "  + TABLE_ANBAR + " SET " + COLUMN_ANBAR_MIQDAR + " = " + COLUMN_ANBAR_MIQDAR + " - ? , " + COLUMN_ANBAR_MEBLEG + " = " + COLUMN_ANBAR_MEBLEG +
            " - ? WHERE " + COLUMN_ANBAR_MALNR + " = ? ";

    private static final String INCREASE_KREDITOR_BORC= "UPDATE " + TABLE_KREDITOR + " SET " + COLUMN_KREDITOR_BORC + " = " +
    COLUMN_KREDITOR_BORC + " + ? WHERE " +  COLUMN_KREDITOR_ID + " = ?";
    private static final String DECREASE_KREDITOR_BORC= "UPDATE " + TABLE_KREDITOR + " SET " + COLUMN_KREDITOR_BORC + " = " +
            COLUMN_KREDITOR_BORC + " - ? WHERE " +  COLUMN_KREDITOR_ID + " = ?";
    private static final String INCREASE_DEBITOR_BORC= "UPDATE " + TABLE_DEBITOR + " SET " + COLUMN_KREDITOR_BORC + " = " +
            COLUMN_DEBITOR_BORC + " + ? WHERE " +  COLUMN_DEBITOR_ID + " = ?";
    private static final String DECREASE_DEBITOR_BORC= "UPDATE " + TABLE_DEBITOR + " SET " + COLUMN_DEBITOR_BORC + " = " +
            COLUMN_DEBITOR_BORC + " - ? WHERE " +  COLUMN_DEBITOR_ID + " = ?";

    private static final String INSERT_ANBAR="INSERT INTO " + TABLE_ANBAR + " ( "  + COLUMN_ANBAR_MALNR + " , " + COLUMN_ANBAR_MALADI + " , " + COLUMN_ANBAR_MIQDAR + ", " + COLUMN_ANBAR_MEBLEG +
            ") VALUES(?,?,?,?)";
    private static final String INSERT_NEW_MAL="INSERT INTO " + TABLE_MAL  + " ( " + COLUMN_MAL_BARCODE + " , " +
            COLUMN_MAL_KOD + " , " + COLUMN_MAL_AD + " , " + COLUMN_MAL_MAYA + " , " + COLUMN_MAL_SATISH1 + " , " + COLUMN_MAL_SATISH2 +
            " ) VALUES(?,?,?,?,?,?)";
    private static final String UPDATE_MAL="UPDATE " + TABLE_MAL + " SET " + COLUMN_MAL_BARCODE  + " = ?, " + COLUMN_MAL_KOD  + " = ?, "
            + COLUMN_MAL_AD  + " = ?, "
            + COLUMN_MAL_MAYA  + " = ?, "
            + COLUMN_MAL_SATISH1  + " = ?, "
            + COLUMN_MAL_SATISH2  + " = ?"  + " WHERE " + COLUMN_MAL_ID + " = ?";
    private static final String UPDATE_DEBITOR= "UPDATE " + TABLE_DEBITOR + " SET " + COLUMN_DEBITOR_AD +  " = ?, " +
            COLUMN_DEBITOR_NOMRE + " = ?, " + COLUMN_DEBITOR_BORC + " = ?" + " WHERE " + COLUMN_DEBITOR_ID + " = ?";

    private static final String UPDATE_ANBAR_ITEM="UPDATE " + TABLE_ANBAR + " SET " + COLUMN_ANBAR_MIQDAR + " = ?, " + COLUMN_ANBAR_MEBLEG +
            " = ?"  + " WHERE " + COLUMN_ANBAR_ID + " = ?";
    private static final String INSERT_DEBITOR="INSERT INTO " + TABLE_DEBITOR + " ( " + COLUMN_DEBITOR_AD + " , " + COLUMN_DEBITOR_NOMRE +" , " + COLUMN_DEBITOR_BORC
            + " ) VALUES(?,?,?)";

    private static final String INSERT_KREDITOR="INSERT INTO " + TABLE_KREDITOR + " ( " + COLUMN_KREDITOR_AD + " , " +
            COLUMN_KREDITOR_BORC + " ) VALUES(?,?)";

    private static final String INSERT_KASSA="INSERT INTO " + TABLE_KASSA + " ( " + COLUMN_KASSA_TARIX + " , " + COLUMN_KASSA_NOV +
            " , " + COLUMN_KASSA_QEYD  + " , " + COLUMN_KASSA_MEBLEG + " ) VALUES(?,?,?,?)";
    private static final String DELETE_MEDAXIL="DELETE FROM " + TABLE_MEDAXIL + " WHERE " +COLUMN_MEDAXIL_ID + " = ?";
    private static final String DELETE_MEXARIC="DELETE FROM " + TABLE_MEXARIC + " WHERE " + COLUMN_MEXARIC_ID + " = ?";
    private static final String DELETE_MEDAXIL_FAKTURA="DELETE FROM " + TABLE_MEDAXILFAKTURA + " WHERE " + COLUMN_MEDAXILFAKTURA_MEDAXILNR + " = ?";
    private static final String DELETE_MEXARIC_FAKTURA="DELETE FROM " + TABLE_MEXARICFAKUTA + " WHERE " + COLUMN_MEXARICFAKTURA_MEXARICID + " = ?";
    private static final String INCREASE_KASSA="UPDATE " + TABLE_TOTAL + " SET " + COLUMN_TOTAL_KASSA +  " = " + COLUMN_TOTAL_KASSA + "  +  ?";
    private static final String DECREASE_KASSA="UPDATE " + TABLE_TOTAL + " SET " + COLUMN_TOTAL_KASSA +  " = " + COLUMN_TOTAL_KASSA + "  -  ?";
    private static final String QUERY_MAL ="SELECT * FROM " + TABLE_MAL + " WHERE " + COLUMN_MAL_ID + " = ?";
    private static final String FIND_KREDITOR="SELECT * FROM " + TABLE_KREDITOR + " WHERE " + COLUMN_KREDITOR_ID + " = ?";
    private static final String FIND_DEBITOR="SELECT * FROM " + TABLE_DEBITOR + " WHERE " + COLUMN_DEBITOR_ID + " = ?";
    private static final String FIND_MEXADIL="SELECT * FROM " + TABLE_MEDAXIL +  " WHERE " + COLUMN_MEDAXIL_ID + " = ?";
    private static final String FIND_MEXARIC="SELECT * FROM " + TABLE_MEXARIC + " WHERE " + COLUMN_MEXARIC_ID + " = ?";
    private static final String FIND_KREDITORMEDAXIL="SELECT * FROM " + TABLE_MEDAXIL + " WHERE " + COLUMN_MEDAXIL_KREDITORID + " = ?";
    private static final String FIND_DEBITORMEXARIC="SELECT * FROM " + TABLE_MEXARIC + " WHERE "  + COLUMN_MEXARIC_DEBITORID + " = ?";
    private static final String QUERY_ANBAR="SELECT * FROM " + TABLE_ANBAR;
    private static final String QUERY_ALL_MAL="SELECT * FROM " + TABLE_MAL;
    private static final String QUERY_KREDITOR="SELECT * FROM " + TABLE_KREDITOR;
    private static final String QUERY_DEBITOR="SELECT * FROM " + TABLE_DEBITOR;
    private static final String QUERY_KASSA="SELECT * FROM " + TABLE_KASSA;

    private static final String QUERY_TOTAL="SELECT * FROM " + TABLE_TOTAL;
    private static final String QUERY_ALL_MEXARIC_INFO= "SELECT * FROM " + TABLE_MEXARICFAKUTA ;

    private static final String SET_MEXARIC_PRICE = "UPDATE " + TABLE_MEXARIC + " SET  " + COLUMN_MEXARIC_MEBLEG + " = ?" + " WHERE " + COLUMN_MEXARIC_ID + " = ?";
    private static final String SET_MEDAXIL_PRICE = "UPDATE " + TABLE_MEDAXIL + " SET  " + COLUMN_MEDAXIL_MEBLEG + " = ?" + " WHERE " + COLUMN_MEDAXIL_ID + " = ?";
    private static final String SET_MAL_MAYA= "UPDATE " + TABLE_MAL + " SET " + COLUMN_MAL_MAYA + " = ? WHERE " + COLUMN_MAL_ID + " = ?";









    public void open(){
        try {
            connection= DriverManager.getConnection(CONNECTION_STRING);
            insertMedaxil= connection.prepareStatement(INSERT_MEDAXIL);
            queryMedaxilFaktura = connection.prepareStatement(QUERY_MEDAXILFAKTURA);
            queryMexaricFaktura=connection.prepareStatement(QUERY_MEXARICFAKTURA);
            insertMedaxilFaktura = connection.prepareStatement(INSERT_MEDAXIL_FAKTURA);
            existsInAnbar = connection.prepareStatement(EXIST_IN_ANBAR);
            increaseAnbar = connection.prepareStatement(INCREASE_ANBAR);
            insertAnbar= connection.prepareStatement(INSERT_ANBAR);
            queryMal = connection.prepareStatement(QUERY_MAL);
            insertNewMal = connection.prepareStatement(INSERT_NEW_MAL);
            updateMal = connection.prepareStatement(UPDATE_MAL);
            insertDebitor = connection.prepareStatement(INSERT_DEBITOR);
            insertKreditor = connection.prepareStatement(INSERT_KREDITOR);
            findKreditor= connection.prepareStatement(FIND_KREDITOR);
            findMedaxil = connection.prepareStatement(FIND_MEXADIL);
            increaseKreditorBorc = connection.prepareStatement(INCREASE_KREDITOR_BORC);
            findDebitor= connection.prepareStatement(FIND_DEBITOR);
            insertMexaric= connection.prepareStatement(INSERT_MEXARIC);
            insertMexaricFaktura = connection.prepareStatement(INSERT_MEXARIC_FAKTURA);
            decreaseAnbar=connection.prepareStatement(DECREASE_ANBAR);
            findMexaric=connection.prepareStatement(FIND_MEXARIC);
            increaseDebitorBorc=connection.prepareStatement(INCREASE_DEBITOR_BORC);
            selectAnbar = connection.prepareStatement(SELECT_ANBAR);
            findKreditorMedaxil= connection.prepareStatement(FIND_KREDITORMEDAXIL);
            findDebitorMexaric = connection.prepareStatement(FIND_DEBITORMEXARIC);
            insertKassa = connection.prepareStatement(INSERT_KASSA);
            decreaseDebitorBorc = connection.prepareStatement(DECREASE_DEBITOR_BORC);
            decreaseKreditorBorc= connection.prepareStatement(DECREASE_KREDITOR_BORC);
            updateAnbarItem=connection.prepareStatement(UPDATE_ANBAR_ITEM);
            deleteMedaxil =connection.prepareStatement(DELETE_MEDAXIL);
            deleteMedaxilFaktura=connection.prepareStatement(DELETE_MEDAXIL_FAKTURA);
            deleteMexaric = connection.prepareStatement(DELETE_MEXARIC);
            deleteMexaricFaktura =connection.prepareStatement(DELETE_MEXARIC_FAKTURA);
            setMedaxilPrice = connection.prepareStatement(SET_MEDAXIL_PRICE);
            setMexaricPrice = connection.prepareStatement(SET_MEXARIC_PRICE);
            setMalMaya= connection.prepareStatement(SET_MAL_MAYA);
            updateDebitor = connection.prepareStatement(UPDATE_DEBITOR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        try {
            if (connection!=null){
                connection.close();
            }if (insertMedaxil!=null){
                insertMedaxil.close();
            }if (insertMedaxilFaktura !=null){
                insertMedaxilFaktura.close();
            }if (existsInAnbar!=null){
                existsInAnbar.close();
            }if (increaseAnbar!=null){
                increaseAnbar.close();
            }if (insertAnbar!=null){
                insertAnbar.close();
            }if(queryMal!=null){
                queryMal.close();
            }if (insertNewMal!=null){
                insertNewMal.close();
            }if (updateMal!=null){
                updateMal.close();
            }if (insertDebitor != null){
                insertDebitor.close();
            }if (insertKreditor!=null){
                insertKreditor.close();
            }if (findKreditor!=null){
                findKreditor.close();
            }if (findMedaxil != null){
                findMedaxil.close();
            }if (increaseKreditorBorc != null){
                increaseKreditorBorc.close();
            }if (findDebitor!=null){
                findDebitor.close();
            }if (insertMexaric!=null){
                insertMexaric.close();
            }if (insertMexaricFaktura!=null){
                insertMexaricFaktura.close();
            }if (decreaseAnbar!=null){
                decreaseAnbar.close();
            }if (findMexaric!=null){
                findMexaric.close();
            }if (selectAnbar!= null){
                selectAnbar.close();
            }if (findKreditorMedaxil!=null){
                findKreditorMedaxil.close();
            }if (findDebitorMexaric!=null){
                findDebitorMexaric.close();
            }if (insertKassa!=null){
                insertKassa.close();
            }if (decreaseDebitorBorc!=null){
                decreaseDebitorBorc.close();
            }if (decreaseKreditorBorc!=null){
                decreaseKreditorBorc.close();
            }if (updateAnbarItem!=null){
                updateAnbarItem.close();
            }if (deleteMedaxil!=null){
                deleteMedaxil.close();
            }if (deleteMedaxilFaktura!=null){
                deleteMedaxilFaktura.close();
            }if (deleteMexaric!=null){
                deleteMexaric.close();
            }if (deleteMexaricFaktura!=null){
                deleteMexaricFaktura.close();
            }if (setMedaxilPrice!=null){
                setMedaxilPrice.close();
            }if (setMexaricPrice!=null){
                setMexaricPrice.close();
            }if (setMalMaya != null){
                setMalMaya.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public int insertMedaxil(String tarix,int kreditorId){
        int generatedKey=-1;
        try {
            connection.setAutoCommit(false);
            insertMedaxil.setString(1,tarix);
            insertMedaxil.setInt(2,kreditorId);
            insertMedaxil.setDouble(3,0);
            int nrAffectedRows= insertMedaxil.executeUpdate();
            if (nrAffectedRows == 1){
                ResultSet generatedKeys = insertMedaxil.getGeneratedKeys();
                if (generatedKeys.next()){
                    generatedKey=generatedKeys.getInt(1);
                }
            }
        }catch (Exception e){
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("Connection rolled back");
            }catch (SQLException f){
                System.out.println("Failed to rollback ");
            }
        }finally {
            try {
                System.out.println("committing the changes and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException exception){
                System.out.println("Couldn't set auto commit to true " + exception.getMessage());
            }
        }
        return generatedKey;
    }

    public int insertMexaric(String tarix,int debitorId){
        int generatedKey=-1;
        try {
            connection.setAutoCommit(false);
            insertMexaric.setString(1,tarix);
            insertMexaric.setInt(2,debitorId);
            insertMexaric.setDouble(3,0);
            int nrAffectedRows= insertMexaric.executeUpdate();
            if (nrAffectedRows == 1){
                ResultSet generatedKeys = insertMexaric.getGeneratedKeys();
                if (generatedKeys.next()){
                    generatedKey=generatedKeys.getInt(1);
                }
            }
        }catch (Exception e){
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("Connection rolled back");
            }catch (SQLException f){
                System.out.println("Failed to rollback ");
            }
        }finally {
            try {
                System.out.println("committing the changes and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException exception){
                System.out.println("Couldn't set auto commit to true " + exception.getMessage());
            }
        }
        return generatedKey;
    }
    public List<Medaxil> queryMedaxil(){
        try (Statement statement = connection.createStatement();
             ResultSet resultSet= statement.executeQuery(QUERY_MEDAXIL)){
            List<Medaxil> medaxilList= new ArrayList<>();
            while (resultSet.next()){
                Kreditor kreditor = getKreditor(resultSet.getInt(3));
                Medaxil medaxil = new Medaxil(resultSet.getInt(1),resultSet.getString(2),
                        resultSet.getInt(3),kreditor.getAd(),
                        resultSet.getDouble(4));
                medaxilList.add(medaxil);
            }
            return medaxilList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Kassa> queryKassa(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_KASSA)){
            List<Kassa>  kassaList = new ArrayList<>();
            while (resultSet.next()){
                Kassa kassa = new Kassa(resultSet.getInt(1),resultSet.getString(3),resultSet.getString(2)
                ,resultSet.getString(4),resultSet.getDouble(5));
                kassaList.add(kassa);
            }
            return kassaList;
        }catch (SQLException  e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Mexaric> queryMexaric(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_MEXARC)){
            List<Mexaric> mexaricList = new ArrayList<>();
            while (resultSet.next()){
                Debitor debitor = getDebitor(resultSet.getInt(3));
                Mexaric mexaric = new Mexaric(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3),
                        debitor.getAd(),resultSet.getDouble(4));
                mexaricList.add(mexaric);
            }
            return mexaricList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    public List<MedaxilFaktura> queryMedaxilFaktura(int medaxilNr){
        try {
            queryMedaxilFaktura.setInt(1,medaxilNr);
            ResultSet resultSet= queryMedaxilFaktura.executeQuery();
            List<MedaxilFaktura> medaxilFakturaList = new ArrayList<>();
            while (resultSet.next()){
                Mal mal = getMal(resultSet.getInt(2));
                MedaxilFaktura medaxilFaktura = new MedaxilFaktura(resultSet.getInt(1),
                        resultSet.getInt(2),mal.getBarcode(),mal.getKod(),mal.getAd(),resultSet.getDouble(3),
                        resultSet.getDouble(4),resultSet.getDouble(5),resultSet.getInt(6));
                medaxilFakturaList.add(medaxilFaktura);
            }
            return medaxilFakturaList;
        }catch (SQLException e){
            System.out.println("Error "  + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<MexaricFaktura> queryMexaricFaktura(int mexaricNr){
        try {
            queryMexaricFaktura.setInt(1,mexaricNr);
            ResultSet resultSet= queryMexaricFaktura.executeQuery();
            List<MexaricFaktura> mexaricFakturaList = new ArrayList<>();
            while (resultSet.next()){
                Mal mal = getMal(resultSet.getInt(2));
                MexaricFaktura mexaricFaktura= new MexaricFaktura(resultSet.getInt(1),
                        resultSet.getInt(2),mal.getBarcode(),mal.getKod(),mal.getAd(),resultSet.getDouble(3),
                        resultSet.getDouble(4),resultSet.getDouble(5),resultSet.getInt(6));
                mexaricFakturaList.add(mexaricFaktura);
            }
            return mexaricFakturaList;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void insertMedaxilFaktura(int malNr,double miqdar,double alishQiymet,double mebleg,int medaxilNr){
        try {
            connection.setAutoCommit(false);
            insertMedaxilFaktura.setInt(1,malNr );
            insertMedaxilFaktura.setDouble(2,miqdar);
            insertMedaxilFaktura.setDouble(3,alishQiymet );
            insertMedaxilFaktura.setDouble(4,mebleg);
            insertMedaxilFaktura.setInt(5,medaxilNr);
            int nrAffectedRows= insertMedaxilFaktura.executeUpdate();
            setMalMaya.setDouble(1,alishQiymet);
            setMalMaya.setInt(2,malNr);
            setMalMaya.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                updateMedaxilMebleg(medaxilNr);
                insertAnbar(malNr,miqdar,mebleg);
                updateKreditorBorc(medaxilNr,mebleg);
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }

    public void insertMexaricFaktura(int malNr,double miqdar,double satishQiymet,double mebleg,int mexaricNr){
        try {
            connection.setAutoCommit(false);
            insertMexaricFaktura.setInt(1,malNr );
            insertMexaricFaktura.setDouble(2,miqdar);
            insertMexaricFaktura.setDouble(3,satishQiymet );
            insertMexaricFaktura.setDouble(4,mebleg);
            insertMexaricFaktura.setInt(5,mexaricNr);
            int nrAffectedRows= insertMexaricFaktura.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                updateMexaricMebleg(mexaricNr);
                decreaseAnbar(malNr,miqdar,mebleg);
                updateDebitorBorc(mexaricNr,mebleg);
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }

    public void decreaseAnbar(int malNr,double miqdar,double mebleg){
        try {
            decreaseAnbar.setDouble(1, miqdar);
            decreaseAnbar.setDouble(2, mebleg);
            decreaseAnbar.setInt(3, malNr);
            decreaseAnbar.executeUpdate();
        }catch (SQLException g){
                g.printStackTrace();
            }

    }


    public void insertAnbar(int  malNr,double miqdar, double mebleg){
        try {
            connection.setAutoCommit(false);
            if (doesExistAnbar(malNr)){
                increaseAnbar.setDouble(1,miqdar);
                increaseAnbar.setDouble(2,mebleg);
                increaseAnbar.setInt(3,malNr);
                int nrAffectedRows= increaseAnbar.executeUpdate();
                if (nrAffectedRows==1){
                    connection.commit();
                }
            }else{
                Mal mal = getMal(malNr);
                insertAnbar.setInt(1, malNr);
                insertAnbar.setString(2, mal.getAd());
                insertAnbar.setDouble(3,miqdar);
                insertAnbar.setDouble(4,mebleg);
                int nrAffectedRows= insertAnbar.executeUpdate();
                if (nrAffectedRows==1){
                    connection.commit();
                }
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }

    }

    public boolean doesExistAnbar(int  malNr){
        boolean exists=false;
        try {
            existsInAnbar.setInt(1,malNr);
            ResultSet resultSet = existsInAnbar.executeQuery();
            if (resultSet.next()){
                int count= resultSet.getInt(1);
                exists=count>0;
            }
        }catch (SQLException e){
            e.getMessage();

        }
        return exists;
    }
    public Mal getMal(int malId){
        try {
            queryMal.setInt(1,malId);
            ResultSet resultSet= queryMal.executeQuery();
            if (resultSet.next()){
                Mal mal = new Mal(resultSet.getInt(1),resultSet.getLong(2),resultSet.getString(3),
                       resultSet.getString(4),resultSet.getDouble(5),resultSet.getDouble(6),resultSet.getDouble(7));
                return mal;
            }
            return null;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public Kreditor getKreditor(int id){
        try {
            findKreditor.setInt(1,id);
            ResultSet resultSet= findKreditor.executeQuery();
            if (resultSet.next()){
                Kreditor kreditor = new Kreditor(resultSet.getInt(1),resultSet.getString(2), resultSet.getDouble(3));
                return kreditor;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }


    public Debitor getDebitor(int id){
        try {
            findDebitor.setInt(1,id);
            ResultSet resultSet= findDebitor.executeQuery();
            if (resultSet.next()){
                Debitor debitor = new Debitor(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(3));
                return debitor;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private Medaxil getMedaxil(int id){
        try {
            findMedaxil.setInt(1,id);
            ResultSet resultSet = findMedaxil.executeQuery();
            if (resultSet.next()){
                Medaxil medaxil = new Medaxil(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getDouble(4));
                return medaxil;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    private Mexaric getMexaric(int id){
        try {
            findMexaric.setInt(1,id);
            ResultSet resultSet = findMexaric.executeQuery();
            if (resultSet.next()){
                Mexaric mexaric = new Mexaric(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getDouble(4));
                return mexaric;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Anbar selectAnbar(int id){
        try {
            selectAnbar.setInt(1,id);
            ResultSet resultSet = selectAnbar.executeQuery();
            if (resultSet.next()){
                Mal mal = getMal(resultSet.getInt(2));
                Anbar anbar = new Anbar(resultSet.getInt(1),resultSet.getInt(2),
                        mal.getKod(),mal.getBarcode(),resultSet.getString(3),resultSet.getDouble(4),
                        resultSet.getDouble(5));
                return anbar;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Total getTotal(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_TOTAL)){
          if (resultSet.next()){
              Total total = new Total(resultSet.getDouble(1));
              return total;
          }
          return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }
    private void updateKreditorBorc(int medaxilId,double mebleg){
        try {
            Medaxil medaxil = getMedaxil(medaxilId);
            increaseKreditorBorc.setDouble(1,mebleg);
            increaseKreditorBorc.setInt(2,medaxil.getKreditorId());
            increaseKreditorBorc.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void decreaseDebitorBorc(int id,double mebleg){
        try {
            decreaseDebitorBorc.setDouble(1,mebleg);
            decreaseDebitorBorc.setInt(2,id);
            decreaseDebitorBorc.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void decreaseKreditorBorc(int id,double mebleg){
        try {
            decreaseKreditorBorc.setDouble(1,mebleg);
            decreaseKreditorBorc.setInt(2,id);
            decreaseKreditorBorc.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void updateDebitorBorc(int mexaricId,double mebleg){
        try {
            Mexaric mexaric = getMexaric(mexaricId);
            increaseDebitorBorc.setDouble(1,mebleg);
            increaseDebitorBorc.setInt(2,mexaric.getDebitorId());
            increaseDebitorBorc.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private void updateMedaxilMebleg(int medaxilNr) throws SQLException {
        String sql = "UPDATE Medaxil SET Mebleg = (SELECT SUM(mebleg) FROM MedaxilFaktura WHERE medaxilNr = ?) WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, medaxilNr);
            statement.setInt(2, medaxilNr);
            statement.executeUpdate();
        }
    }

    private void increaseKassa(double mebleg)  {
        try (PreparedStatement statement = connection.prepareStatement(INCREASE_KASSA)) {
            statement.setDouble(1, mebleg);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void decreaseKassa(double mebleg)  {
        try (PreparedStatement statement = connection.prepareStatement(DECREASE_KASSA)) {
            statement.setDouble(1, mebleg);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void updateMexaricMebleg(int mexaricNr) throws SQLException {
        String sql = "UPDATE Mexaric SET mebleg = (SELECT SUM(mebleg) FROM MexaricFaktura WHERE mexaricId = ?) WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, mexaricNr);
            statement.setInt(2, mexaricNr);
            statement.executeUpdate();
        }
    }




    public List<Anbar> queryAnbar(){
        try (Statement statement = connection.createStatement();
             ResultSet resultSet= statement.executeQuery(QUERY_ANBAR)){
            List<Anbar> anbarList = new ArrayList<>();
            while (resultSet.next()){
                Mal mal = getMal(resultSet.getInt(2));
                Anbar anbar = new Anbar(resultSet.getInt(1),resultSet.getInt(2),mal.getKod(),mal.getBarcode(),resultSet.getString(3),
                        resultSet.getDouble(4),resultSet.getDouble(5));
                anbarList.add(anbar);
            }
            return anbarList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Mal> queryMal(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet= statement.executeQuery(QUERY_ALL_MAL)) {
            List<Mal> malList = new ArrayList<>();
            while (resultSet.next()){
                Mal mal = new Mal(resultSet.getInt(1),resultSet.getLong(2),resultSet.getString(3),
                        resultSet.getString(4),resultSet.getDouble(5),resultSet.getDouble(6),resultSet.getDouble(7));
                malList.add(mal);
            }
            return malList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }



    public List<Kreditor> queryKreditor(){
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_KREDITOR)){
            List<Kreditor> kreditorList = new ArrayList<>();
            while (resultSet.next()){
                Kreditor kreditor = new Kreditor(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getDouble(3));
                kreditorList.add(kreditor);
            }
            return kreditorList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public List<Debitor> queryDebitor(){
        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_DEBITOR)) {
            List<Debitor> debitorList = new ArrayList<>();
            while (resultSet.next()){
                Debitor debitor = new Debitor(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getDouble(4));
                debitorList.add(debitor);

            }
            return debitorList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<MexaricFaktura> queryAllMexaricFaktura(){
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(QUERY_ALL_MEXARIC_INFO)){
            List<MexaricFaktura> mexaricFakturas = new ArrayList<>();
            while (resultSet.next()){
                Mal mal = getMal(resultSet.getInt(2));
                MexaricFaktura mexaricFaktura= new MexaricFaktura(resultSet.getInt(1),
                        resultSet.getInt(2),mal.getBarcode(),mal.getKod(),mal.getAd(),resultSet.getDouble(3),
                        resultSet.getDouble(4),resultSet.getDouble(5),resultSet.getInt(6));
                mexaricFakturas.add(mexaricFaktura);
            }
            return mexaricFakturas;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void insertNewMal(Long barcode,String code,String ad,double maya,double satish1,double satish2){
        try {
            connection.setAutoCommit(false);
            insertNewMal.setLong(1,barcode);
            insertNewMal.setString(2,code);
            insertNewMal.setString(3,ad);
            insertNewMal.setDouble(4,maya);
            insertNewMal.setDouble(5,satish1);
            insertNewMal.setDouble(6,satish2);
            int nrAffectedRows= insertNewMal.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }

    public void updateMal(int id,Long barcode,String code,String ad, double maya,double satish1,double satish2){
        try {
            connection.setAutoCommit(false);
            updateMal.setLong(1,barcode);
            updateMal.setString(2,code);
            updateMal.setString(3,ad);
            updateMal.setDouble(4,maya);
            updateMal.setDouble(5,satish1);
            updateMal.setDouble(6,satish2);
            updateMal.setInt(7,id);
            int nrAffectedRows = updateMal.executeUpdate();
            if (nrAffectedRows == 1) {
                connection.commit();
            }
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");

            } catch (SQLException f) {
                System.out.println("failed to rollback");
            }
        } finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);

            } catch (SQLException g) {
                System.out.println("couldn't set auto commit to true " + g.getMessage());


            }
        }
    }
    public void insertDebitor(String ad,String nomre, double borc){
        try {
            connection.setAutoCommit(false);
            insertDebitor.setString(1,ad);
            insertDebitor.setString(2,nomre);
            insertDebitor.setDouble(3,borc);

            int nrAffectedRows= insertDebitor.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }
    public void insertKreditor(String ad, double borc){
        try {
            connection.setAutoCommit(false);
            insertKreditor.setString(1,ad);
            insertKreditor.setDouble(2,borc);
            int nrAffectedRows= insertKreditor.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }

    public List<MedaxilFaktura> getKreditorMedaxils(int number){
        try{
           findKreditorMedaxil.setInt(1,number);
           ResultSet resultSet = findKreditorMedaxil.executeQuery();
           List<MedaxilFaktura> medaxilFakturaList = new ArrayList<>();
           while (resultSet.next()){
               Medaxil medaxil = new Medaxil(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getDouble(4));
               medaxilFakturaList.addAll(queryMedaxilFaktura(medaxil.getId()));
               for (MedaxilFaktura medaxilFaktura : medaxilFakturaList) {
                   medaxilFaktura.setTarix(medaxil.getTarix());
               }

           }
           return medaxilFakturaList;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public List<MexaricFaktura> getDebitorMexaric(int number){
        try{
            findDebitorMexaric.setInt(1,number);
            ResultSet resultSet = findDebitorMexaric.executeQuery();
            List<MexaricFaktura> mexaricFakturaList = new ArrayList<>();
            while (resultSet.next()){
                Mexaric medaxil = new Mexaric(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getDouble(4));
                mexaricFakturaList.addAll(queryMexaricFaktura(medaxil.getId()));
                for (MexaricFaktura mexaricFaktura : mexaricFakturaList) {
                    mexaricFaktura.setTarix(medaxil.getTarix());
                }

            }
            return mexaricFakturaList;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void insertKassa(String tarix,String nov,String qeyd,int id,double mebleg,String command){
        try {
            connection.setAutoCommit(false);
            insertKassa.setString(1,tarix);
            insertKassa.setString(2,nov);
            insertKassa.setString(3,qeyd);
            insertKassa.setDouble(4,mebleg);
            int nrAffectedRows= insertKassa.executeUpdate();
            if (nrAffectedRows==1){
                connection.commit();
                if (nov.equals("Mədaxil") && command.equals("debitor")){
                    decreaseDebitorBorc(id,mebleg);
                    increaseKassa(mebleg);
                }if (nov.equals("Məxaric") && command.equals("kreditor")){
                    decreaseKreditorBorc(id,mebleg);
                    decreaseKassa(mebleg);
                }if (command.equals("diger")){
                    if (nov.equals("Mədaxil")){
                        increaseKassa(mebleg);
                    } else if (nov.equals("Məxaric")) {
                        decreaseKassa(mebleg);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");
            }catch (SQLException f){
                System.out.println("failed to rollback");
            }
        }finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);
            }catch (SQLException g){
                System.out.println("couldn't set auto commit to true " +g.getMessage());
            }
        }
    }
    public void updateAnbarItem(int id,double miqdar,double mebleg){
        try {

            updateAnbarItem.setDouble(1, miqdar);
            updateAnbarItem.setDouble(2, mebleg);
            updateAnbarItem.setInt(3, id);
            updateAnbarItem.executeUpdate();

        }
        catch (SQLException g) {
                g.printStackTrace();
            }

    }
    public void deleteMedaxil(int medaxilNr,int kreditorId){
        try {
            deleteMedaxil.setInt(1,medaxilNr);
            int nrAffectedRows = deleteMedaxil.executeUpdate();
            if (nrAffectedRows == 1) {
                List<MedaxilFaktura> medaxilFakturaList= queryMedaxilFaktura(medaxilNr);
                for (MedaxilFaktura medaxilFaktura:medaxilFakturaList){
                    decreaseAnbar(medaxilFaktura.getMalNr(),medaxilFaktura.getMiqdar(),medaxilFaktura.getMebleg());
                    decreaseKreditorBorc(kreditorId,medaxilFaktura.getMebleg());
                }
                deleteMedaxilFaktura.setInt(1,medaxilNr);
                deleteMedaxilFaktura.executeUpdate();
            } else {
                System.out.println("Failed to delete sale.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteMexaric(int mexaricNr,int debitorId){
        try {
            deleteMexaric.setInt(1,mexaricNr);
            int nrAffectedRows = deleteMexaric.executeUpdate();
            if (nrAffectedRows == 1) {
                List<MexaricFaktura> mexaricFakturaList= queryMexaricFaktura(mexaricNr);
                for (MexaricFaktura mexaricFaktura:mexaricFakturaList){
                    insertAnbar(mexaricFaktura.getMalNr(),mexaricFaktura.getMiqdar(),mexaricFaktura.getMebleg());
                    decreaseDebitorBorc(debitorId,mexaricFaktura.getMebleg());
                }
                deleteMexaricFaktura.setInt(1,mexaricNr);
                deleteMexaricFaktura.executeUpdate();
            } else {
                System.out.println("Failed to delete sale.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int deleteMexaricFaktura(int mexaricNr,int debitorId){
        int result =0;
        try {
            setMexaricPrice.setInt(1,0);
            setMexaricPrice.setInt(2,mexaricNr);
            setMexaricPrice.executeUpdate();
            List<MexaricFaktura> mexaricFakturaList = queryMexaricFaktura(mexaricNr);
            for (MexaricFaktura m: mexaricFakturaList){
                insertAnbar(m.getMalNr(),m.getMiqdar(),m.getMebleg());
                decreaseDebitorBorc(debitorId,m.getMebleg());
            }
            deleteMexaricFaktura.setInt(1,mexaricNr);
            int rowsAffected = deleteMexaricFaktura.executeUpdate();

            if (rowsAffected > 0) {
                result = 1;
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
    }


    public int deleteMedaxilFaktura(int medaxilNr,int kreditorNr){
        int result =0;
        try {
            setMedaxilPrice.setInt(1,0);
            setMedaxilPrice.setInt(2,medaxilNr);
            setMedaxilPrice.executeUpdate();
            List<MedaxilFaktura> medaxilFakturas = queryMedaxilFaktura(medaxilNr);
            for (MedaxilFaktura m: medaxilFakturas){
                decreaseAnbar(m.getMalNr(),m.getMiqdar(),m.getMebleg());
                decreaseKreditorBorc(kreditorNr,m.getMebleg());
            }
            deleteMedaxilFaktura.setInt(1,medaxilNr);
            int rowsAffected = deleteMedaxilFaktura.executeUpdate();

            if (rowsAffected > 0) {
                result = 1;
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
    }


    public void updateDebitor(int id,String debitor,String nomre, double borc){
        try {
            connection.setAutoCommit(false);
            updateDebitor.setString(1,debitor);
            updateDebitor.setString(2,nomre);
            updateDebitor.setDouble(3,borc);
            updateDebitor.setInt(4,id);
            int nrAffectedRows = updateDebitor.executeUpdate();
            if (nrAffectedRows == 1) {
                connection.commit();
            }
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            e.printStackTrace();
            try {
                connection.rollback();
                System.out.println("connection rolled back");

            } catch (SQLException f) {
                System.out.println("failed to rollback");
            }
        } finally {
            try {
                System.out.println("committing the changes  and setting to true");
                connection.setAutoCommit(true);

            } catch (SQLException g) {
                System.out.println("couldn't set auto commit to true " + g.getMessage());


            }
        }
    }

}
