<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>

<jsp:useBean id="tenori" scope="session" class="java.util.Vector"/>

<html>
  <table width="100%" >
<%
  if (tenori != null &&  tenori.size() == 0 )
  {
%>
        <td class="int" align="left">Non ci sono oggetti assegnati all'Atto.</td>
<%
  } 
  else
  {
	   Iterator itx = tenori.iterator();
	   String lIdTenore = "";
	   String lCodTenore = "";

	   while ( itx.hasNext())
       {
		   Object tenore=itx.next();
		   TenoreSigeEstesoModel  lTenoreEsteso = null;
		   TenoreSigeModel lTenore =null;
		   try {
		       lTenoreEsteso=(TenoreSigeEstesoModel)tenore;
		       lTenore=lTenoreEsteso.getTenoreSige();
		   } catch (ClassCastException cce) {
		       lTenore=(TenoreSigeModel)tenore;
		   }
		   
		   // Raggruppamento X Codice Oggetto
		//if (lTenore != null &&  !lTenore.getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore)) 
 		if (lTenore != null &&  !lTenore.getCodOggettoSige().equalsIgnoreCase(lCodTenore)) 		
		{
			lIdTenore = lTenore.getIdTenoreSige().toString();
			lCodTenore = lTenore.getCodOggettoSige();
%>
 			<tr>
  				<td class="campo" width=29%><font class="campo"><%=lTenore.getDescrContenutoSige()%> - <%=lTenore.getDescrOggettoSige()%></font></td>
  			</tr>
<%
 		} // endif
       } // endwhile
  }  // endif provvedimenti.size()
%>
  </table>
</html>