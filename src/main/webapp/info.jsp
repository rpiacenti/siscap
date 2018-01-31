<%@ page import="javax.servlet.ServletContext" %>
<%@ page import="javax.servlet.jsp.JspFactory" %>
<%@ page import= "java.io.*"%>
<%@ page import= "javax.servlet.*"%>
<%@ page import= "javax.servlet.http.*"%>
<%@ page import= "java.util.*"%>
<html>
<body background="#CDCDB4">
 
<center>${initParam.siglaSistema}</center>
<table border=1 cellpadding="0" cellspacing="0" >
<td height="1" valign="top">
<%
	TreeSet propKeys = new TreeSet(System.getProperties().keySet());
	for (Iterator it = propKeys.iterator(); it.hasNext(); ) {
	String key = (String)it.next();
	String value = System.getProperty(key);
%>
<tr>
<td>
 
<i><%= key %>:</i><td><b><%= value %></b><br>
<%
}
%>
</td></tr>
</table>
</body>
</html>
