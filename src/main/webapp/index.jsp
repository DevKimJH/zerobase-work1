<%@page import="service.WifiService"%>
<%@page import="java.util.List"%>
<%@page import="dto.WifiInfo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>와이파이 정보 구하기</title>

    <style>

        #wifiTable{
            margin-top: 21px;
            font-family: Arial, Helvetica, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        #wifiTable td, #wifiTable th{
            border: 1px solid #ddd;
            padding: 8px;
        }

        #wifiTable tr:nth-child(even){background-color: #f2f2f2;}

        #wifiTable tr:hover {background-color: #ddd;}

        #wifiTable th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #04AA6D;
            color: white;
        }
    </style>

    <script>

        var options = {
            enableHighAccuracy: true,
            timeout: 20000,
            maximumAge: 0
        };

        function log(data) {
            const tag = document.createElement('p');
            tag.textContent = data;
            document.body.appendChild(tag);
        }

        function success(pos) {
            var crd = pos.coords;

            document.getElementById("inputLAT").value = crd.latitude;
            document.getElementById("inputLNT").value = crd.longitude;
        }

        function error(err) {
            console.warn(`ERROR(${err.code}): ${err.message}`);
        }


        function getUserLocation(){
            navigator.geolocation.getCurrentPosition(success, error, options);
        }

    </script>
</head>
<body>

<%
    String lat = request.getParameter("inputLAT");
    String lnt = request.getParameter("inputLNT");
    boolean chkFlag = true;

    if(lat == null){
        lat = "0.0";
    }

    if(lnt == null){
        lnt = "0.0";
    }

    WifiService wifiService = new WifiService();
    List<WifiInfo> nearWifiList = wifiService.searchNearWifi(lat, lnt);
%>
<p id="myLocation"></p>
<h1>와이파이 정보 구하기</h1>
<div>
    <a href="index.jsp">홈</a> |
    <a href="history.jsp">위치 히스토리 목록</a> |
    <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
</div>

<div style="margin-top:21px;">
    <form action="index.jsp" method="get">
        <label for="inputLAT">LAT:</label>
        <input type="text" id="inputLAT" name="inputLAT" value="<%=lat%>"/>

        <span>, </span>

        <label for="inputLNT">LNT:</label>
        <input type="text" id="inputLNT" name="inputLNT" value="<%=lnt%>"/>


        <input type="button" onclick="getUserLocation()" value="내 위치 가져오기">
        <!-- <button onclick="location.href='load-wifi'"> 근처 WIPI 정보 보기 </button>  -->
        <input type="submit" value="근처 WIFI 정보 보기">
    </form>
</div>

<div>
    <table id="wifiTable">
        <tr>
            <th>거리(Km)</th>
            <th>관리번호</th>
            <th>자치구</th>
            <th>와이파이명</th>
            <th>도로명주소</th>
            <th>상세주소</th>
            <th>설치위치(층)</th>
            <th>설치유형</th>
            <th>설치기관</th>
            <th>서비스구분</th>
            <th>망종류</th>
            <th>설치년도</th>
            <th>실내외구분</th>
            <th>WIFI접속환경</th>
            <th>X좌표</th>
            <th>Y좌표</th>
            <th>작업일자</th>
        </tr>
        <!--
        <tr>
            <td>0.1849</td>
            <td>WF120043</td>
            <td>마포구</td>
            <td>원한강안내센터</td>
            <td>마포나루길 467 망원안내센터</td>
            <td>사무실3층</td>
            <td></td>
            <td>7-1. 커뮤니티 - 행정</td>
            <td>서울시(AP)</td>
            <td>공공WIFI</td>
            <td>임대망</td>
            <td>2020</td>
            <td>실내</td>
            <td></td>
            <td>37</td>
            <td>126</td>
            <td>2022-05-07</td>
        </tr>
         -->
        <tr>
            <%
                if(nearWifiList != null)
                    for(WifiInfo wifi : nearWifiList){
            %>
            <td><%= wifi.getDist() / 1000.0 %></td>
            <td><%= wifi.getMgrNo() %></td>
            <td><%= wifi.getWrdofc() %></td>
            <td><%= wifi.getMainNm() %></td>
            <td><%= wifi.getAdres1() %></td>
            <td><%= wifi.getAdres2() %></td>
            <td><%= wifi.getInstlFloor() %></td>
            <td><%= wifi.getInstlTy() %></td>
            <td><%= wifi.getInstlMby() %></td>
            <td><%= wifi.getSvcSe() %></td>
            <td><%= wifi.getCmcwr() %></td>
            <td><%= wifi.getCnstcYear() %></td>
            <td><%= wifi.getInoutDoor() %></td>
            <td><%= wifi.getRemars3() %></td>
            <td><%= wifi.getLat() %></td>
            <td><%= wifi.getLnt() %></td>
            <td><%= wifi.getWorkDttm() %></td>
        </tr>
        <%
                }
        %>
    </table>
</div>
</body>
</html>