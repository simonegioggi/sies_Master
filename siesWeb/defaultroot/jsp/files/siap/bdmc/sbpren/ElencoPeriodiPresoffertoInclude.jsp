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
    		<td class="titolo" colspan="100%">Periodi da computare (Dati Estratti da Banca Dati Misure Cutelari)</td>
  		</tr>
	<%
	//Vector lPeriodi = provvedimento.getSbPeriPren();
	//DATAPROCCOLL.ANNO_REGI_PMPM,
    //DATAPROCCOLL.NUME_REGI_PMPM,
	int lsel=0;
	if (lProvv != null && lProvv.size() != 0) {
	int contaPeriodi = 0;
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
	   			if (lPeriodo.getCodStatPrenPeri().equals("0")) { 
	   				contaPeriodi++;
	   			}
	   			if (contaPeriodi > 1)
	   				break;
			}
		}
	   	if (contaPeriodi > 1)
				break;
	}
	if (contaPeriodi >0) { %>
		 <tr>
	    	<td class="l" rowspan="2" style="text-align:center"><font class="label">Seleziona</font></td>
	    	<td class="l" colspan="2" style="text-align:center"><font class="label">Periodo sofferto</font></td>
	    	<td class="l" rowspan="2"  width="9%" style="text-align:center"><font class="label">Tipo Misura sofferta</font></td>
	   	 	<td class="l" colspan="2" style="text-align:center"><font class="label">Periodo Selezionato</font></td>
	    	<td class="l" rowspan="2" style="text-align:center"><font class="label">&nbsp;</font></td>
	    	<td class="l" rowspan="2" style="text-align:center" width="120px"><font class="label">Pari a</font></td>
	 	   </tr>
	 	   <tr>
			    <td class="l" style="text-align:center"><font class="label">Inizio</font></td>
			    <td class="l" style="text-align:center"><font class="label">Fine</font></td>
			    <td class="l" width="15%" style="text-align:center"><font class="label">Dalla Data</font></td>
			    <td class="l" width="15%" style="text-align:center"><font class="label">Alla Data</font></td>
	   		 </tr>
		
	<% }
	int numPeriodi=0;
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
	        if (!lPeriodo.getCodStatPrenPeri().equals("0")) { }
	        else {
		%>
	         <% String campoCheckBox = ICostantiSbPren.CAMPO_CHECK_PERIODI+"["+lsel+"]";  
	               String campoGiornoDa = ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel;
	            
	            %>
	            <input type="hidden" name="appoProg" value="0">
	            <input type="hidden" name="dtIni<%=lsel %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>">
				<input type="hidden" name="dtFine<%=lsel %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>">
	            <input type="hidden" name="idPren<%=lsel %>" value="<%=lPeriodo.getIdPren() %>">
	            <input type="hidden" name="idProg<%=lsel %>" value="<%=lPeriodo.getProgPeriPres() %>">
	            <%if (contaPeriodi  ==1) {%>
	           			 <tr>
						 <td  class="c" >
		 				 <input type="checkbox" name="<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>"  value="<%=lsel%>" onmouseover="DisattivaVincoli(<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>)" onmouseout="AttivaVincoli(<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>)" onClick="ControllaCheckBox(<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>,dtIni<%=lsel %>,dtFine<%=lsel %>,'MC_<%=lsel %>')"/> 
						 </td>
	           <% } else {%>
	           
		            <tr>
					 <td  class="c" >
	 				 <input type="checkbox" name="<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>"  value="<%=lsel%>" onmouseover="DisattivaVincoli(<%=campoCheckBox %>)" onmouseout="AttivaVincoli(<%=campoCheckBox %>)" onClick="ControllaCheckBox(<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,<%=campoCheckBox%>,dtIni<%=lsel %>,dtFine<%=lsel %>,'MC_<%=lsel %>')"/> 
					 </td>
	             <% } %>
	              
	               
	               
	                    <td class="l">     <font class="campo">
		             
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>
		             </font>
		             </td>
		             
		             <td class="l">     <font class="campo">
		            
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>
		             </font>
		             </td>
 					<td class="l"><font class=campo><%=lPeriodo.getDescPeri() %></font></td>
		            
		            <td class="l" nowrap>
		                	<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value);" >
		           	        -<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value);" >
	    	                -<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value);" >	                
				   </td>
					<td class="l" nowrap>
					   <input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value);" >
			           -<input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value);" >
			           -<input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value);" >
    				</td>
		             <td class="L" nowrap>
					      <a href="Javascript:callCalcolaQuantum('MC_<%=lsel %>');"><img src="<%=IWebConstants.IMAGES_DIR%>freccia_verde.gif" alt="Calcola Quantum" border=0></a>
					  </td>
					    <td class="L" id="Quantum_MC_<%=lsel %>" nowrap>&nbsp;</td>
					  </tr>
					   <% String fascBdmc = new String();
		            
		                if (lPeriodo.getAnnoFascBdmc() != null ){ %>
		                
		                <input type="hidden" name="annoBdmc<%=lsel %>" value="<%=lPeriodo.getAnnoFascBdmc()%>">
		              <input type="hidden" name="sedeBdmc<%=lsel %>" value="<%=lPeriodo.getCodiSedeInst()%>">
		                	
		                
		                <%	fascBdmc = lPeriodo.getAnnoFascBdmc()+"/";
		                }
		                else
		                	fascBdmc = "-/";
		                if (lPeriodo.getNumeFascBdmc() != null ){ %>
		                	
		              	  <input type="hidden" name="numeroBdmc<%=lsel %>" value="<%=lPeriodo.getNumeFascBdmc()%>">

		                <%	fascBdmc += lPeriodo.getNumeFascBdmc();
	      				 }else
		                	fascBdmc += "-"; 
		                	  if (lSbProcPena.getAnnoRegiPmpm() != null ){ %>
		                	  
		                
		               			 <input type="hidden" name="annoRgnr<%=lsel %>" value="<%=lSbProcPena.getAnnoRegiPmpm()%>">
		                
						      <%}  if (lSbProcPena.getNumeRegiPmpm() != null ){ %>
		                	  
		                
		               			 <input type="hidden" name="numeroRgnr<%=lsel %>" value="<%=lSbProcPena.getNumeRegiPmpm()%>">
		                
						      <%} %>       					 
						       <tr>
					    <td>&nbsp;</td>
					    <td class="l" colspan="100%">
					          <font class="campoSmall">Prenotazione n.</font><font class="campoSmall" style="color: Red" ><a  class="cliccabile"  href="Main.jsp?Action=siap.bdmc.sbpren.action.ActLoadDettaglioPrenotazione&IdPren= <%=lPeriodo.getIdPren()%>" title="Dettaglio Prenotazione"> <%=lPeriodo.getIdPren()%> </a></font><font class="campoSmall"> per </font><font class="campoSmall" style="color: Purple;" ><a  class="cliccabile"  href="Main.jsp?Action=siap.bdmc.sbpren.action.ActLoadDettaglioPrenotazione&IdPren= <%=lPeriodo.getIdPren()%>" title="Dettaglio Prenotazione"><%= lSbPren.getCognSogg() +" "+ lSbPren.getNomeSogg()  %> </a></font> 
					      <font class="campoSmall">- Anno/Numero B.D.M.C. </font><font class="campoSmall" style="color: Purple;" ><%=fascBdmc %></font>
					      <font class="campoSmall">- Anno/Numero R.G.N.R. </font><font class="campoSmall" style="color: Purple;" ><%=lSbProcPena.getAnnoRegiPmpm() %>/<%=lSbProcPena.getNumeRegiPmpm() %></font>
					    </td>
					  </tr>
		            

		            
		           
		           
		<%lsel++; }numPeriodi++; }%>
	 <% }} %>
		<input type="hidden" name="contaPeriodi" value="<%=numPeriodi%>">
		     
	<% }if (lsel == 0) { 
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
}%>   </table>
 
</html>