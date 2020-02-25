<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %> 
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel" %>
<%@ page import="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.calcolopena.model.CalcoloPenaModel"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil" %>

<jsp:useBean id="benefici" 				scope="request" class="java.util.Vector"/>


<html>
<%

if(benefici != null && benefici.size() != 0)
{
%>
	  <table cellspacing="0" cellpadding="0" width="95%">
	    <tr><td class="Titolo" colspan="4">Benefici</td></tr>
	    <tr>
	      <td class="l">
	        <center><font class="label">Tipo</font></center>
	      </td>
	      <td class="l">
	        <center><font class="label">Subordinata</font></center>
	      </td>
	      <td class="l">
	        <center><font class="label">DPR</font></center>
	      </td>
	       <td class="l">
	        <center><font class="label">Pena</font></center>
	      </td>
	    </tr>
<%

Iterator lIterBenefici = benefici.iterator();
while (lIterBenefici.hasNext())
   {
     	BeneficioModel lBene = (BeneficioModel)lIterBenefici.next();

     	String Natur = lBene.getCodNaturaBeneficio();
     	if(Natur.compareTo("R") != 0)
     	{	  
   %>
   		<tr>
     			<td class="l">
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrNaturaBeneficio(), "-")%></font>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoBeneficio(), "-")%></font>
     			</td>
     			<td class="l">
       		<%if(lBene.getDescrTipoSospSubordinata()!= null && !lBene.getDescrTipoSospSubordinata().equals(""))
       		{%>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoSospSubordinata(),"-")%></font>
       		<%}%>
     			</td>
     
     			<td class="l">
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrDpr(), "-")%></font>
     			</td>
     
     			<td class="l">
       	<%
       		if((lBene.getNumAnniReclusione()!=null && lBene.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiReclusione()!=null && lBene.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniReclusione()!=null && lBene.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
       		{
       	%>
       			<font class="campo">Reclusione</font>
       			<font class="label">Anni</font>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniReclusione(), "0")%></font>
       			<font class="label">Mesi</font>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiReclusione(), "0")%></font>
       			<font class="label">Giorni</font>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniReclusione(), "0")%></font>
       	<%
       		}
             
       		if(lBene.getImportoMulta()!=null && lBene.getImportoMulta().compareTo(new BigDecimal(0))!=0)
       		{
       	%>
       			<font class="label">Multa </font>
       			<font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoMulta())%></font>&nbsp;€&nbsp;
       	<%
       		}
       
       		if((lBene.getNumAnniArresto()!=null && lBene.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiArresto()!=null && lBene.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniArresto()!=null && lBene.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
       		{
       	%>
       			<font class="campo">Arresto</font>
       			<font class="label">Anni</font>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniArresto(), "0")%></font>
       			<font class="label">Mesi</font>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiArresto(), "0")%></font>
       			<font class="label">Giorni</font>
       			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniArresto(), "0")%></font>
       	<%
       		}
       
       		if(lBene.getImportoAmmenda()!=null && lBene.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
       		{
       	%>
       			<font class="label">Ammenda </font>
       			<font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoAmmenda())%></font>&nbsp;€&nbsp;
       	<%
       		}
       	%>
           	&nbsp;
     			</td>
   		</tr>
		<%
     	 }  // end if Natur
     	 
	}  // end while
%>
 </table>
   <br>
<%
 } // Chiude le if
%>      
</html>