<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel"%>

<jsp:useBean id="udienzaPartiC" scope="request" class="java.util.Vector"/>
<jsp:useBean id="udienzaPartiO" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoParte" scope="request" class="java.lang.String"/>

<% 
java.util.Vector udienzaParti = null; 

if ("C".equals(request.getParameter("tipoParte"))){
	udienzaParti = udienzaPartiC; 
} else if ("O".equals(request.getParameter("tipoParte"))){
	udienzaParti = udienzaPartiO; 
}
 
if (udienzaParti != null && udienzaParti.size() > 0){
%>

<%
	Iterator itx = udienzaParti.iterator();
	while ( itx.hasNext())  {
		AnagraficaPartiUdienzaModel lParti = (AnagraficaPartiUdienzaModel)itx.next();
%>
    <tr>
      <td class="L">
<%
		if(lParti.getCodParte() != null && lParti.getCodParte().equals("F")){	
%> 
			<font class="campo"><%=lParti.getCognome()%>&nbsp;&nbsp;<%=lParti.getNome()%></font>
<%
		}
%>     

<%
		if(lParti.getCodParte() != null && lParti.getCodParte().equals("G")){	
%>      
      		<font class="campo"><%=lParti.getDenominazione()%>&nbsp;<%=StringUtils.toStringJSP(lParti.getRagSociale())%></font>
<%
		}
%>
      &nbsp;</td>


      <td class="L">
<%
		if(lParti.getCodParte() != null && lParti.getCodParte().equals("F")){
			if(lParti.getCodComuneNascita() != null && !lParti.getCodComuneNascita().equals("-")){
%>     
      			nato: <font class="campo"><%=StringUtils.toStringJSP(lParti.getDescComuneNascita())%></font>
<%
			} else {
%>
				nato: <font class="campo"><%=StringUtils.toStringJSP(lParti.getDescComuneNascitaEstero())%></font>
<%		  
			}
%>
			&nbsp;
<%
			if(lParti.getDataNascita() != null) {
%>
          		il: <font class="campo"><%=DateUtils.getDateToString(lParti.getDataNascita(),"dd-MM-yyyy")%></font>
<%
			} else {
%>
          		il: <font class="campo"><%="**-" +StringUtils.toStringJSP(lParti.getDataNascita(), "**")%></font>
<%
			}
		}
%>

<%
		if(lParti.getCodParte() != null && lParti.getCodParte().equals("G")){
			if(lParti.getDenominazione() != null && !lParti.getDenominazione().equals("")){
%>
				sede legale: <font class="campo"><%=StringUtils.toStringJSP(lParti.getIndSedeLegale())%></font>
<%
			}
		}
%>
      </td>

	  <td class="L"> Convocazione Udienza: 
<%
		if(lParti.getFlagConvUdienza() != null && !lParti.getFlagConvUdienza().equals("") && lParti.getFlagConvUdienza().equals("S") ){
%>		
			<font class="campo">SI</font>
<%
		} else {
%>		
			<font class="campo">NO</font>
<%
		}
%>
		
	  </td>

    </tr>

<%
	
	} // end while ( itx.hasNext())  { 
		
} // end if (udienzaParti != null && udienzaParti.size() > 0){
%>