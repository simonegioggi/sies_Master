<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.sico.util.CalendarUtil" %>
<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@page import="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"%>
<%@page import="siap.bdmc.sbpren.model.SbPrenModel"%>
<jsp:useBean id="lProvv"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="flagConnessioneBdmc"  scope="request" class="java.lang.String"/>


<%@page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@page import="siap.sico.ufficio.controller.UfficioUtils"%>
<html>
<input type="hidden" name="appoCheck" value="999999999">
<table width="100%">
  		<tr>
    		<td class="titolo" colspan="100%">Periodi non computabili (Dati Estratti da Banca Dati Misure Cutelari)</td>
  		</tr>
	<%
	//Vector lPeriodi = provvedimento.getSbPeriPren();
	//DATAPROCCOLL.ANNO_REGI_PMPM,
    //DATAPROCCOLL.NUME_REGI_PMPM,
	int lsel=0;
	int contaPeriodi=0;
	if (lProvv != null && lProvv.size() != 0) {%>
		 <tr>
    	<td class="l" rowspan="2" style="text-align:center"><font class="label">Seleziona</font></td>
    	<td class="l" colspan="2" style="text-align:center"><font class="label">Periodo sofferto</font></td>
    	<td class="l" rowspan="2" width= "50%" style="text-align:center"><font class="label">Tipo Misura sofferta</font></td>
    	<td class="l" rowspan="2" style="text-align:center" width="120px"><font class="label">Pari a</font></td>
 	   </tr>
 	   <tr>
		    <td class="l" style="text-align:center"><font class="label">Inizio</font></td>
		    <td class="l" style="text-align:center"><font class="label">Fine</font></td>
		</tr>
	<% 
	
	   for (int i=0; i<lProvv.size();i++) {
	 	ProvvedimentoModelBDMC lProvvBdmcMod= (ProvvedimentoModelBDMC) lProvv.get(i);
	   	Vector lPeriodi = lProvvBdmcMod.getSbPeriPren();
	   	SbPrenModel lSbPren = lProvvBdmcMod.getSbPren();
	   	SbViewProcpenaModel lSbProcPena = (SbViewProcpenaModel)lProvvBdmcMod.getSbViewProcpena().get(0);
	if(lPeriodi != null && lPeriodi.size() != 0)
	{
		Iterator lIterPeriodi = lPeriodi.iterator();
		

		while(lIterPeriodi.hasNext())
		{
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
	      //  if (lPeriodo.getCodStatPrenPeri().equals("1") || lPeriodo.getCodStatPrenPeri().equals("2") || lPeriodo.getCodStatPrenPeri().equals("3") || lPeriodo.getCodStatPrenPeri().equals("4")) { 
	          if (lPeriodo.getCodStatPrenPeri().equals("3") ) { 
	    	  %>
	              <tr>
					 <td  class="c" >
	 				 <input type="checkbox" name="<%=ICostantiSbPren.CAMPO_CHECK_PERIODI_NC%>"  value="<%=lsel%>" /> 
					 </td>
	           
	              
	               
	               
	                    <td class="l">     <font class="campo">
		             
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>
		             </font>
		             </td>
		             
		             <td class="l">     <font class="campo">
		            
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>
		             </font>
		             </td>
 					<td class="l"><font class=campo><%=lPeriodo.getDescPeri() %></font></td>
		            
		            <% // Calcolo numero giorni mesi anni della custodia
		     	   
		     	   CalendarModel lCalPenaEspiata = new CalendarModel();
		     	    CalendarUtil  lCalUtil = new CalendarUtil();
		     	    lCalPenaEspiata.setDataInizio(lPeriodo.getDataInizPeri());
		     	    lCalPenaEspiata.setDataFine(lPeriodo.getDataFinePeri());
		     	    lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata,false);
		     	    lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);

		               %>
					
					    <td class="L" id="Quantum_MC_<%=lsel %>" nowrap>Anni&nbsp;<font class="campo"><%=lCalPenaEspiata.getNumAnni()%></font>&nbsp;Mesi&nbsp;<font class="campo"><%=lCalPenaEspiata.getNumMesi()%></font>&nbsp;Giorni&nbsp;<font class="campo"><%=lCalPenaEspiata.getNumGiorni()%></font></td>
					  </tr>
					   <% String fascBdmc = new String();
		            
		                if (lPeriodo.getAnnoFascBdmc() != null ){ %>
	                <%	fascBdmc = lPeriodo.getAnnoFascBdmc()+"/";
		                }
		                else
		                	fascBdmc = "-/";
		                if (lPeriodo.getNumeFascBdmc() != null ){ %>
		                	
		               <%	fascBdmc += lPeriodo.getNumeFascBdmc();
	      				 }else
		                	fascBdmc += "-"; 
		                	  if (lSbProcPena.getAnnoRegiPmpm() != null ){ %>
		                	  
		                   <%}  if (lSbProcPena.getNumeRegiPmpm() != null ){ %>
		                	  
		                
		               		
						      <%} %>       					 
						       <tr>
					    <td>&nbsp;</td>
					    <td class="l" colspan="100%">
					          <font class="campoSmall">Prenotazione n.</font><font class="campoSmall"  style="color: Purple;" ><a   class="cliccabile" href="Main.jsp?Action=siap.bdmc.sbpren.action.ActLoadDettaglioPrenotazione&IdPren= <%=lPeriodo.getIdPren()%>" title="Dettaglio Prenotazione"> <%=lPeriodo.getIdPren()%> </a></font><font class="campoSmall"> per </font><font class="campoSmall" style="color: Purple;" ><a  class="cliccabile"  href="Main.jsp?Action=siap.bdmc.sbpren.action.ActLoadDettaglioPrenotazione&IdPren= <%=lPeriodo.getIdPren()%>" title="Dettaglio Prenotazione"><%= lSbPren.getCognSogg() +" "+ lSbPren.getNomeSogg()  %> </a></font> 
					      <font class="campoSmall">- Anno/Numero B.D.M.C. </font><font class="campoSmall" style="color: Purple;" ><%=fascBdmc %></font>
					      <font class="campoSmall">- Anno/Numero R.G.N.R. </font><font class="campoSmall" style="color: Purple;" ><%=lSbProcPena.getAnnoRegiPmpm() %>/<%=lSbProcPena.getNumeRegiPmpm() %></font>
					    </td>
					  </tr>
		            

		            
		           
		           
		<% } 
	          if (!lPeriodo.getCodStatPrenPeri().equals("0") )
	         		 lsel++; 
	         }%>
	 <% }} %>
		<input type="hidden" name="contaPeriodiNC" value="<%=contaPeriodi%>">
		     
	<% } if (lsel == 0) { 
			if (flagConnessioneBdmc.compareTo("S") == 0) {%>
	 			<table cellspacing=1 cellpadding=1 width="50%" >
	       		<tr>
	        		<td class="l">
	       			<font class="cGrigio">Nessun Periodo non Computabile prenotato</font>
	      			</td>
	    		</tr>
 			<%}	else { %>
 				<table cellspacing=1 cellpadding=1 width="50%" >
	       		<tr>
	        		<td class="l">
	       			<font class="cGrigio">Problemi di connessione verso la Banca Dati Misure Cautelari</font>
	      			</td>
	    		</tr>
 		<%  	}
	}%>  
 
 </table>
 
</html>