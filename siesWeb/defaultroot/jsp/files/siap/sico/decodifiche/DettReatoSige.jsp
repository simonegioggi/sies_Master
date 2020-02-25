<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>
<%@ page import="siap.sico.decodifiche.model.OggettiModel" %>

<%
String lDesc = "";
ReatoModel lReato = null;
	  
// Lettura dalla request di ReatoCircostanzaModel
ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel) request.getAttribute("lReatoCircostanza");
     
if (lReatoCircostanza != null) {
	lReato = lReatoCircostanza.getReato();
	String[] xx = lReatoCircostanza.getDescPopUp();
	lDesc = xx[0];
	out.print(xx[1]);
} // endif lReatoCircostanza != null
      
// Scrittura nella request del  ReatoModel
request.setAttribute("lReato", lReato);

// Scrittura nella request del lDesc, stringa con la descrizione del reato
request.setAttribute("lDesc", lDesc);
%>