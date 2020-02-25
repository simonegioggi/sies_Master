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
<input type="hidden" name="appoCheck" value="999999999">

	<%
	Vector lPeriodi = provvedimento.getSbPeriPren();
	int totPerComputabili = 0;
	for (int conta=0;conta < lPeriodi.size();conta++){
		SbPeriprenModel lPeriodo = (SbPeriprenModel) lPeriodi.get(conta);
		if (lPeriodo.getCodStatPrenPeri().equals("0")) {
			totPerComputabili++;
			if (totPerComputabili > 1)
				break;
			
		}
	}
	if(lPeriodi != null && lPeriodi.size() != 0 && totPerComputabili != 0)
	{%>
	  <table cellspacing=1 cellpadding=1 width="100%">
	  <tr>
 		<td class="LBGISIV" width="5%"  align="center"><font class="campoLow">Seleziona</font>  </td>
 		<td class="LBGISIV" width="14%"><font class="campoLow">Inizio periodo</font>  </td>
		<td class="LBGISIV" width="14%"><font class="campoLow">Fine periodo</font></td>
		<td class="LBGISIV" width="8%"><font class="campoLow">Anno/Num. Fasc. BDMC</font></td>
		<td class="LBGISIV" width="31%"><font class="campoLow">Sede Inst.</font></td>

		<td class="LBGISIV" width="28%"><font class="campoLow">Perido di interesse</font></td>
	  
	  </tr>
	<%
		Iterator lIterPeriodi = lPeriodi.iterator();
		int lsel=0;
		if (totPerComputabili == 1) {
			while(lIterPeriodi.hasNext())
			{
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
			 if (lPeriodo.getCodStatPrenPeri().equals("0")) { 
	        %>
	        	 <input type="hidden" name="dtIni<%=lsel %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>">
					<input type="hidden" name="dtFine<%=lsel %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>">
		            <tr>
					 <td  class="c" >
	 				 <input type="checkbox" name="<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>"  value="<%=lsel%>" onmouseover="DisattivaVincoli(<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>)" onmouseout="AttivaVincoli(<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>)" onClick="ControllaCheckBox(<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>)"/> 
					 </td>
		             
		             <td class="l">     <font class="campo">
		             </font>
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>
		             </td>
		             
		             <td class="l">     <font class="campo">
		             </font>
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>
		             </td>

		             <td class="l">     <font class="campo">
		             <% String fascBdmc = new String();
		            
		                if (lPeriodo.getAnnoFascBdmc() != null )
		                	fascBdmc = lPeriodo.getAnnoFascBdmc()+"/";
		                else
		                	fascBdmc = "-/";
		                if (lPeriodo.getNumeFascBdmc() != null )
		                	fascBdmc += lPeriodo.getNumeFascBdmc();
		                else
		                	fascBdmc += "-";
		            	 	
		            	 %>
		             </font>
						<%=fascBdmc%> 
				     </td>

		             <td class="l">     <font class="campo">
		             </font>
						<%=UfficioUtils.getDescTipoUffByCodUfficio(lPeriodo.getCodiSedeInst())%> 
				     </td>

			          <td class="L" >
			        
		            <table>
		           
		            
		            <tr>
				 	<td class="l">Da</td>
	        	  	 
	        	  	<td class="L" >
	            		<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value);">
		           	   -<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value);">
	    	           -<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value);">
	          		</td>
	          		</tr>
	          		<tr>
	            	<td class="l">A</td>
			        <td class="L" >
						<input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value);">
			            -<input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value);">
			            -<input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value);">
			           	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
						<%-- ControllaPeriodo(< %= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,< %= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,< %= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,< %= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,< %= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,< %=ICostantiSbPren.CAMPO_CHECK_PERIODI %>,dtIni< %=lsel %>,dtFine< %=lsel %>) --%>
			        </td>
			        </tr>
			        </table>
			     
			        </td>


		            </tr>

<%	       
			 }lsel++;	} // chiusura ciclo lettura periodi e chiusura if tipo_per = 0
		}
		else {
		while(lIterPeriodi.hasNext())
		{
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
	        if (!lPeriodo.getCodStatPrenPeri().equals("0")) { }
	        else {
		%>
	         <% String campoCheckBox = ICostantiSbPren.CAMPO_CHECK_PERIODI+"["+lsel+"]";  
	               String campoGiornoDa = ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel;
	            
	            %>
	            <input type="hidden" name="dtIni<%=lsel %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>">
				<input type="hidden" name="dtFine<%=lsel %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>">
	            <tr>
				 <td  class="c" >
 				 <input type="checkbox" name="<%=ICostantiSbPren.CAMPO_CHECK_PERIODI%>"  value="<%=lsel%>" onmouseover="DisattivaVincoli(<%=campoCheckBox %>)" onmouseout="AttivaVincoli(<%=campoCheckBox %>)" onClick="ControllaCheckBox(<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,<%=campoCheckBox%>)"/> 
				 </td>
	             
	             <td class="l">     <font class="campo">
	             </font>
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>
	             </td>
	             
	             <td class="l">     <font class="campo">
	             </font>
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>
	             </td>

	           <% String fascBdmc = new String();
		            
		                if (lPeriodo.getAnnoFascBdmc() != null )
		                	fascBdmc = lPeriodo.getAnnoFascBdmc()+"/";
		                else
		                	fascBdmc = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-/";
		                if (lPeriodo.getNumeFascBdmc() != null )
		                	fascBdmc += lPeriodo.getNumeFascBdmc();
		                else
		                	fascBdmc += "-";
		            	 	
		            	 %>
	             <td class="l" >     <font class="campo">
	             
	             </font>
	             
					<%=fascBdmc%> 
			     </td>

	           <% String descUff="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-----";
	                 if(lPeriodo.getCodiSedeInst() != null)
	                	 descUff=UfficioUtils.getDescTipoUffByCodUfficio(lPeriodo.getCodiSedeInst()); 
	            	   %>
	             <td class="l">     <font class="campo">
	             </font>
	               
					<%=descUff%> 
			     </td>

		          <td class="L" >
		        
	            <table>
	           
	            
	            <tr>
			 	<td class="l">Da</td>
        	  	 
        	  	<td class="L" >
            		<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value);">
	           	   -<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
    	           -<input Title="Dalla data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataDaPeri(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          		</td>
          		</tr>
          		<tr>
            	<td class="l">A</td>
		        <td class="L" >
		            <input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value);">
		           -<input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		           -<input Title="Alla Data" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPeriodo.getDataAPeri(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		        </td>
		        </tr>
		        </table>
		     
		        </td>


	            </tr>
		<% }lsel++;  }}%>
		<input type="hidden" name="contaPeriodi" value="<%=lsel%>">
   <% }else {%>
        
		<table cellspacing=1 cellpadding=1 width="50%" >
       		<tr>
        		<td class="l">
       			<font class="cGrigio">Nessun Periodo computabile prenotato</font>
      			</td>
    		</tr>
 <%}%>  </table>
 
</html>



