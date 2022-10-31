<%@page import="service.WifiService" %>
<%@page import="java.util.List"%>
<%@page import="dto.SearchHistory" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>와이파이 정보 구하기</title>

  <style>

    #historyTable{
      margin-top: 21px;
      font-family: Arial, Helvetica, sans-serif;
      border-collapse: collapse;
      width: 100%;
    }

    #historyTable td, #historyTable th{
      border: 1px solid #ddd;
      padding: 8px;
    }

    #historyTable tr:nth-child(even){background-color: #f2f2f2;}

    #historyTable tr:hover {background-color: #ddd;}

    #historyTable th {
      padding-top: 12px;
      padding-bottom: 12px;
      text-align: left;
      background-color: #04AA6D;
      color: white;
    }
  </style>

  <script>
    function del(mgrNo) {
      location.href="deleteHistory.jsp?mgrNo="+mgrNo;
    }
  </script>
</head>
<body>

<%
  WifiService wifiService = new WifiService();
  List<SearchHistory> historyList = wifiService.getSearchHistory();
%>

<h1>위치 히스토리 목록</h1>
<div>
  <a href="index.jsp">홈</a> |
  <a href="history.jsp">위치 히스토리 목록</a> |
  <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
</div>

<div>
  <table id="historyTable">
    <tr>
      <th>ID</th>
      <th>X좌표</th>
      <th>Y좌표</th>
      <th>조회일자</th>
      <th>비고</th>
    </tr>

    <tr>
      <%
        int idx = 10;
        if(historyList != null)
          for(SearchHistory history : historyList){
      %>
      <td><%= history.getHISTORY_MGR_NO() %></td>
      <td><%= history.getLAT() %></td>
      <td><%= history.getLNT() %></td>
      <td><%= history.getRegisterDate() %></td>
      <td id="del"><input type="button" value="삭제" onclick="del('<%=history.getHISTORY_MGR_NO()%>');"></td>
    </tr>
    <%
        }
    %>

  </table>
</div>
</body>
</html>