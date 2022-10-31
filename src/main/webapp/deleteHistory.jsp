<%@page import="service.WifiService" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>와이파이 정보 구하기</title>
</head>
<body>
<%
  WifiService wifiService = new WifiService();
  wifiService.deleteSearchHistory(request.getParameter("mgrNo"));
%>

<script>
  location.href="history.jsp";
</script>
</body>
</html>