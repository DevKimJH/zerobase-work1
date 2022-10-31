package service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dto.WifiInfo;
import dto.SearchHistory;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class WifiService {


    public WifiService() {};



    /**
     * idx가 0일 경우 0001부터 1000까지 공공와이파이 데이터를 가져온다.
     * 		1일 경우 1001부터 2000까지 공공와이파이 데이터를 가져온다.
     * @param idx
     * @return
     * @throws Exception
     */
    public static String getWifiListFromAPIServer(int idx) throws Exception{
        int startIdx = 1 + (1000 * idx);
        int endIdx = 1000 + (1000 * idx);

        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" +  URLEncoder.encode("6a57547841616c6f3131336a67596d6f","UTF-8") ); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
        urlBuilder.append("/" +  URLEncoder.encode("json","UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(startIdx),"UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(endIdx),"UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
        // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;

        while((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();


        return sb.toString();
    }

    public int getWifiList() throws Exception{

        System.out.println("--- Start getWifiList --- ");


        deleteWifiList();

        JSONParser jsonParser = new JSONParser();
        List<WifiInfo> wifiList = new ArrayList();

        // 1 ~ 1000 요청해서 총 데이터 건수를 확인
        JSONObject json = (JSONObject) jsonParser.parse(getWifiListFromAPIServer(0));
        JSONObject wifiJSONList = (JSONObject) json.get("TbPublicWifiInfo");
        JSONArray row = (JSONArray) wifiJSONList.get("row");

        int count = 1;

        for(int i = 0 ; i < row.size(); i++) {
            JSONObject array = (JSONObject) row.get(i);
            WifiInfo wifiInfo = new WifiInfo();
            wifiInfo.setMgrNo(String.valueOf(array.get("X_SWIFI_MGR_NO")));
            wifiInfo.setWrdofc(String.valueOf(array.get("X_SWIFI_WRDOFC")));
            wifiInfo.setMainNm(String.valueOf(array.get("X_SWIFI_MAIN_NM")));
            wifiInfo.setAdres1(String.valueOf(array.get("X_SWIFI_ADRES1")));
            wifiInfo.setAdres2(String.valueOf(array.get("X_SWIFI_ADRES2")));
            wifiInfo.setInstlFloor(String.valueOf(array.get("X_SWIFI_INSTL_FLOOR")));
            wifiInfo.setInstlTy(String.valueOf(array.get("X_SWIFI_INSTL_TY")));
            wifiInfo.setInstlMby(String.valueOf(array.get("X_SWIFI_INSTL_MBY")));
            wifiInfo.setSvcSe(String.valueOf(array.get("X_SWIFI_SVC_SE")));
            wifiInfo.setCmcwr(String.valueOf(array.get("X_SWIFI_CMCWR")));
            wifiInfo.setCnstcYear(String.valueOf(array.get("X_SWIFI_CNSTC_YEAR")));
            wifiInfo.setInoutDoor(String.valueOf(array.get("X_SWIFI_INOUT_DOOR")));
            wifiInfo.setRemars3(String.valueOf(array.get("X_SWIFI_REMARS3")));
            wifiInfo.setLat(Double.parseDouble(String.valueOf(array.get("LNT"))));
            wifiInfo.setLnt(Double.parseDouble(String.valueOf(array.get("LAT"))));
            wifiList.add(wifiInfo);
            System.out.println(count++ + " " + wifiInfo);
        }


        // 총 데이터 건수 확인
        int listTotalCount = Integer.parseInt(wifiJSONList.get("list_total_count").toString());
        int numberOfGetWifiList = listTotalCount/1000;

        for(int i = 1 ; i <= numberOfGetWifiList ; i++) {
            json = (JSONObject) jsonParser.parse(getWifiListFromAPIServer(i));
            wifiJSONList = (JSONObject) json.get("TbPublicWifiInfo");
            row = (JSONArray) wifiJSONList.get("row");

            for(int j = 0 ; j < row.size(); j++) {
                JSONObject array = (JSONObject) row.get(j);
                WifiInfo wifiInfo = new WifiInfo();
                wifiInfo.setMgrNo(String.valueOf(array.get("X_SWIFI_MGR_NO")));
                wifiInfo.setWrdofc(String.valueOf(array.get("X_SWIFI_WRDOFC")));
                wifiInfo.setMainNm(String.valueOf(array.get("X_SWIFI_MAIN_NM")));
                wifiInfo.setAdres1(String.valueOf(array.get("X_SWIFI_ADRES1")));
                wifiInfo.setAdres2(String.valueOf(array.get("X_SWIFI_ADRES2")));
                wifiInfo.setInstlFloor(String.valueOf(array.get("X_SWIFI_INSTL_FLOOR")));
                wifiInfo.setInstlTy(String.valueOf(array.get("X_SWIFI_INSTL_TY")));
                wifiInfo.setInstlMby(String.valueOf(array.get("X_SWIFI_INSTL_MBY")));
                wifiInfo.setSvcSe(String.valueOf(array.get("X_SWIFI_SVC_SE")));
                wifiInfo.setCmcwr(String.valueOf(array.get("X_SWIFI_CMCWR")));
                wifiInfo.setCnstcYear(String.valueOf(array.get("X_SWIFI_CNSTC_YEAR")));
                wifiInfo.setInoutDoor(String.valueOf(array.get("X_SWIFI_INOUT_DOOR")));
                wifiInfo.setRemars3(String.valueOf(array.get("X_SWIFI_REMARS3")));
                wifiInfo.setLat(Double.parseDouble(String.valueOf(array.get("LNT"))));
                wifiInfo.setLnt(Double.parseDouble(String.valueOf(array.get("LAT"))));
                wifiList.add(wifiInfo);
            }
        }

        insertWifiList(wifiList);

        System.out.println("--- End getWifiList ---");

        return listTotalCount;
    }

    public static boolean insertWifiList(List<WifiInfo> wifiList) {


        System.out.println("--- Start insertWifiList ---");

        String url = "jdbc:mariadb://localhost:3306/wifi_db";
        String dbUserId = "wifi_admin";
        String dbPassword = "zerobase";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "insert into wifi"
                    + 	"	  ( DIST,"
                    + 	"		MGR_NO,"
                    + 	"		WRDOFC,"
                    + 	"		MAIN_NM,"
                    + 	"		ADRES1,"
                    + 	"		ADRES2,"
                    + 	"		INSTL_FLOOR,"
                    + 	"		INSTL_TY	,"
                    + 	"		INSTL_MBY	,"
                    + 	"		SVC_SE		,"
                    + 	"		CMCWR		,"
                    + 	"		CNSTC_YEAR	,"
                    + 	"		INOUT_DOOR	,"
                    + 	"		REMARS3		,"
                    + 	"		LAT			,"
                    +	"		LNT			,"
                    +	"		WORK_DTTM"
                    +   " 	  )"
                    +   "VALUES"
                    + "	(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now());";

            preparedStatement = connection.prepareStatement(sql);

            // parameterIndex는 1부터 시작한다.
            for(WifiInfo wifiInfo : wifiList) {
                preparedStatement.setString(1, "");
                preparedStatement.setString(2, wifiInfo.getMgrNo());
                preparedStatement.setString(3, wifiInfo.getWrdofc());
                preparedStatement.setString(4, wifiInfo.getMainNm());
                preparedStatement.setString(5, wifiInfo.getAdres1());
                preparedStatement.setString(6, wifiInfo.getAdres2());
                preparedStatement.setString(7, wifiInfo.getInstlFloor());
                preparedStatement.setString(8, wifiInfo.getInstlTy());
                preparedStatement.setString(9, wifiInfo.getInstlMby());
                preparedStatement.setString(10, wifiInfo.getSvcSe());
                preparedStatement.setString(11, wifiInfo.getCmcwr());
                preparedStatement.setString(12, wifiInfo.getCnstcYear());
                preparedStatement.setString(13, wifiInfo.getInoutDoor());
                preparedStatement.setString(14, wifiInfo.getRemars3());
                preparedStatement.setDouble(15, wifiInfo.getLat());
                preparedStatement.setDouble(16, wifiInfo.getLnt());

                int affected = preparedStatement.executeUpdate();

                preparedStatement.clearParameters();

                if(affected > 0) {

                }
                else {
                    System.out.println(" 저장 실패 ");
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(connection != null && !connection.isClosed()){
                    connection.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("--- End insertWifiList ---");

        return true;
    }

    public static boolean deleteWifiList() {
        System.out.println("--- Start deleteWifiList ---");

        String url = "jdbc:mariadb://localhost:3306/wifi_db";
        String dbUserId = "wifi_admin";
        String dbPassword = "zerobase";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "delete from wifi;";

            preparedStatement = connection.prepareStatement(sql);

            int affected = preparedStatement.executeUpdate();

            if(affected > 0){
                System.out.println(" 리스트 삭제 성공 ");
            }
            else{
                System.out.println(" 리스트 삭제 실패 ");
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(connection != null && !connection.isClosed()){
                    connection.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }


    public List<SearchHistory> getSearchHistory(){
        List<SearchHistory> historyList = new ArrayList();

        String url = "jdbc:mariadb://localhost:3306/wifi_db";
        String dbUserId = "wifi_admin";
        String dbPassword = "zerobase";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "SELECT HISTORY_MGR_NO\n"
                    + "			, LAT"
                    + "			, LNT"
                    + "			, REGISTER_DATE"
                    + "		FROM SEARCH_HISTORY"
                    + "	ORDER BY HISTORY_MGR_NO DESC";

            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();

            int idx = 1;

            while(rs.next()) {
                SearchHistory historyInfo = new SearchHistory();
                historyInfo.setHISTORY_MGR_NO(rs.getInt("HISTORY_MGR_NO"));
                historyInfo.setLAT(rs.getDouble("LAT"));
                historyInfo.setLNT(rs.getDouble("LNT"));
                historyInfo.setRegisterDate(rs.getDate("REGISTER_DATE"));

                historyList.add(historyInfo);
            }

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(connection != null && !connection.isClosed()){
                    connection.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return historyList;

    }


    public List<WifiInfo> searchNearWifi(String inputLat, String inputLnt){

        if(inputLat.equals("0.0") || inputLnt.equals("0.0")) {
            return null;
        }

        List<WifiInfo> wifiList = new ArrayList();

        String url = "jdbc:mariadb://localhost:3306/wifi_db";
        String dbUserId = "wifi_admin";
        String dbPassword = "zerobase";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "INSERT INTO SEARCH_HISTORY(LAT, LNT, REGISTER_DATE)"
                    +   "VALUES"
                    + "	(?,?, now());";


            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, inputLat);
            preparedStatement.setString(2, inputLnt);

            preparedStatement.executeUpdate();



            sql = "SELECT MGR_NO\n"
                    + "			, WRDOFC"
                    + "			, MAIN_NM"
                    + "			, ADRES1"
                    + "			, ADRES2"
                    + "			, INSTL_FLOOR"
                    + "			, INSTL_TY"
                    + "			, INSTL_MBY"
                    + "			, SVC_SE"
                    + "			, CMCWR"
                    + "			, CNSTC_YEAR"
                    + "			, INOUT_DOOR"
                    + "			, REMARS3"
                    + "			, LAT"
                    + "			, LNT"
                    + "			, WORK_DTTM"
                    + "			, ACOS(SIN(? * PI() / 180) * SIN(LNT * PI() / 180) "
                    + "			+ COS(? * PI() / 180) * COS(LNT * PI() / 180) "
                    + "			* (SIN(? * PI() / 180) * SIN(LAT * PI() / 180) "
                    + "			+ COS(? * PI() / 180) * COS(LAT * PI() / 180))"
                    + "			) * 6371000 AS DISTANCE"
                    + "		FROM WIFI"
                    + "	ORDER BY DISTANCE ASC"
                    + "	   LIMIT 20;";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, inputLat);
            preparedStatement.setString(2, inputLat);
            preparedStatement.setString(3, inputLnt);
            preparedStatement.setString(4, inputLnt);

            rs = preparedStatement.executeQuery();

            while(rs.next()) {

                WifiInfo wifiInfo = new WifiInfo();

                wifiInfo.setMgrNo(rs.getString("MGR_NO"));
                wifiInfo.setWrdofc(rs.getString("WRDOFC"));
                wifiInfo.setMainNm(rs.getString("MAIN_NM"));
                wifiInfo.setAdres1(rs.getString("ADRES1"));
                wifiInfo.setAdres2(rs.getString("ADRES2"));
                wifiInfo.setInstlFloor(rs.getString("INSTL_FLOOR"));
                wifiInfo.setInstlTy(rs.getString("INSTL_TY"));
                wifiInfo.setInstlMby(rs.getString("INSTL_MBY"));
                wifiInfo.setSvcSe(rs.getString("SVC_SE"));
                wifiInfo.setCmcwr(rs.getString("CMCWR"));
                wifiInfo.setCnstcYear(rs.getString("CNSTC_YEAR"));
                wifiInfo.setInoutDoor(rs.getString("INOUT_DOOR"));
                wifiInfo.setRemars3(rs.getString("REMARS3"));
                wifiInfo.setLat(rs.getDouble("LAT"));
                wifiInfo.setLnt(rs.getDouble("LNT"));
                wifiInfo.setWorkDttm(rs.getDate("WORK_DTTM"));
                wifiInfo.setDist(rs.getDouble("DISTANCE"));
                wifiList.add(wifiInfo);
            }

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(connection != null && !connection.isClosed()){
                    connection.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return wifiList;
    }


    public static boolean deleteSearchHistory(String mgrNo) {
        System.out.println("--- Start deleteSearchHistory ---");

        String url = "jdbc:mariadb://localhost:3306/wifi_db";
        String dbUserId = "wifi_admin";
        String dbPassword = "zerobase";

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = "DELETE FROM SEARCH_HISTORY WHERE HISTORY_MGR_NO = ?;";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,  mgrNo);

            int affected = preparedStatement.executeUpdate();

            if(affected > 0){
                System.out.println(" 검색 기록 삭제 성공 ");
            }
            else{
                System.out.println(" 검색 기록 삭제 실패 ");
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if(connection != null && !connection.isClosed()){
                    connection.isClosed();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
