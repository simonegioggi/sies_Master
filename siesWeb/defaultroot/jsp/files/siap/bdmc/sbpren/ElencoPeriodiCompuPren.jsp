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
 		<td class="LBGISIV" ><font class="campoLow">Inizio periodo</font>  </td>
		<td class="LBGISIV" ><font class="campoLow">Fine periodo</font></td>
		<td class="LBGISIV" ><font class="campoLow">Anno/Num. Fasc. BDMC</font></td>
		<td class="LBGISIV"><font class="campoLow">Sede Inst.</font></td>

		
	  </tr>
	<%
		Iterator lIterPeriodi = lPeriodi.iterator();
		int lsel=0;

		while(lIterPeriodi.hasNext())
		{
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
	        if (!lPeriodo.getCodStatPrenPeri().equals("0")) { }
	        else {
		%>
	         <tr>
				
	             
	             <td class="L">     <font class="campo">
	             
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataInizPeri(),"dd-MM-yyyy"))%>
	             </font>
	             </td>
	             
	             <td class="L">     <font class="campo">
	            
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPeriodo.getDataFinePeri(),"dd-MM-yyyy"))%>
	            </font></td>

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
	             <td class="L" >     <font class="campo">
	             
	           
	             
					<%=fascBdmc%> 
			     </font>  </td>

	           <% String descUff="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-----";
	                 if(lPeriodo.getCodiSedeInst() != null)
	                	 descUff=UfficioUtils.getDescTipoUffByCodUfficio(lPeriodo.getCodiSedeInst()); 
	            	   %>
	             <td class="L">     <font class="campo">
	             
	               
					<%=descUff%> 
			      </font> </td>

		       
	            </tr>
		<% lsel++;}  }%>
		
   <% }else {%>
        
		<table cellspacing=1 cellpadding=1 width="50%" >
       		<tr>
        		<td class="l">
       			<font class="cGrigio">Nessun Periodo computabile prenotato</font>
      			</td>
    		</tr>
 <%}%>  </table>
 
</html>



