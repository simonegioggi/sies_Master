<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>


<jsp:useBean id="provvedimento"  scope="request" class="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"/>


<%@page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@page import="siap.sico.ufficio.controller.UfficioUtils"%>
<html>
	<%
	String visuPeriodi = " ";
	boolean flagVisuNonComputabili = false;
	Vector lPeriodi = provvedimento.getSbPeriPren();
	if(lPeriodi != null && lPeriodi.size() != 0)
	{
		Iterator lIterPeriodi = lPeriodi.iterator();
		int lsel=0;
		while(lIterPeriodi.hasNext())
		{
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
	        if (lPeriodo.getCodStatPrenPeri().equals("3")) { 
	        	flagVisuNonComputabili = true;
	         visuPeriodi +=    "<tr>";
				
	          visuPeriodi +=    "<td class=\"l\">     <font class=\"campo\">";
	        
			visuPeriodi +=   StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"));
	        visuPeriodi +=    "</font></td>";
	             
	       visuPeriodi +=    "<td class=\"l\">     <font class=\"campo\">";
	   
			visuPeriodi +=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"));
	       visuPeriodi +=    "</font></td>";

	     visuPeriodi +=    " <td class=\"l\">     <font class=\"campo\">";
	   
		visuPeriodi +=    lPeriodo.getAnnoFascBdmc()+"/"+lPeriodo.getNumeFascBdmc();
		visuPeriodi +=    "</font></td>";

	   visuPeriodi +=    " <td class=\"l\">     <font class=\"campo\">";
	
		visuPeriodi += UfficioUtils.getDescTipoUffByCodUfficio(lPeriodo.getCodiSedeInst()); 
		visuPeriodi +=    "</font></td>";

		
	     visuPeriodi +=    " </tr>";
		 } lsel++; }
    }
 if (flagVisuNonComputabili) {%>  
 			<table cellspacing=1 cellpadding=1 width="100%">
	  <tr>
 		
 		<td class="LBGISIV" ><font class="campoLow">Inizio periodo</font>  </td>
		<td class="LBGISIV" ><font class="campoLow">Fine periodo</font></td>
		<td class="LBGISIV" ><font class="campoLow">Anno/Num. Fasc. BDMC</font></td>
		<td class="LBGISIV" ><font class="campoLow">Distretto B.D.M.C</font></td>
		
	  </tr>
	  <%=visuPeriodi%>
 		<%} else { %>
 			<table cellspacing=1 cellpadding=1 width="50%" >
       		<tr>
        		<td class="l">
       			<font class="cGrigio">Nessun Periodo non computabile prenotato</font>
      			</td>
    		</tr>
 <%}%>
 </table>
</html>



