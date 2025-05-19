<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.catalogo" %>
<%@ page import="modelo.coche" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="modelo.User" %>
<%@ page session="true" %>
<%
User usuario = (User) session.getAttribute("usuario");
if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>
<!DOCTYPE html>