<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.io.ByteArrayOutputStream"%><%
%><%@ page import="java.io.OutputStream"%><%

/*
 * !!!!!!!!!!!!!!!!ATTENZIONE!!!!! 
 * Nelle jsp che richiamano il metodo response.getOutputStream();
 * occorre eliminare qualsiasi carattere, compresi gli spazi e gli a capo, 
 * al di fuori dei tag jsp. 
 * In questo modo si evita l'eccezione lanciata 
 * da Tomcat java.lang.IllegalStateException.
 * Questo perchè secondo le specifiche java non è possibile scrivere 2 volte sullo stream
 * che costituirà la pagina di ritorno, e Tomcat chiama in ogni jsp il metodo in questione...
 * Se non viene scritto alcun carattere al di fuori dei tag jsp, non si accorge che lo stream
 * è già stato chiamato e non si genera l'errore.
 */

 
	ByteArrayOutputStream report = (ByteArrayOutputStream)request.getAttribute("report");
	
	response.reset();
	response.setContentType("application/msword");
	//response.setContentLength(report.length());
	response.setHeader("Content-Disposition", "inline; filename=\"Documento.rtf\"");
	
	OutputStream OutStrm = response.getOutputStream();
	OutStrm.write(report.toByteArray());
	OutStrm.flush();
	OutStrm.close();	
%>