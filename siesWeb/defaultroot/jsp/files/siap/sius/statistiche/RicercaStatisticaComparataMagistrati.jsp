<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.statistiche.model.IspConteggioRelatoriMagistratiModel"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
 
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.RedirectTo" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>

<jsp:useBean id="ricercaProcedimento"   		scope="session" class="siap.sius.statistiche.model.RicercaProcedimentoModel" />
<jsp:useBean id="elencoOggettiSelezionatiCbx"  scope="request" class="java.lang.String" />
<jsp:useBean id="elencoConteggioRelatori"   scope="request" class="java.util.ArrayList" />
<jsp:useBean id="TornaQui"              		scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Statistica Comparata Magistrati per Specifici Oggetti</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    	function waiting() {
      	var node=document.getElementById('waiting');
      	node.style.visibility='visible';
    	}
  	</script>
  </head>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

  <BODY class="corpo">
	  <table>
	    <tr>
	       <td class="LBG">
	         <a href="Javascript:window.print();">
	         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
	       </td>
	       <td class="LBG">
	       	 <font class=label>Funzione :</font>&nbsp;
	       	 <font class="campo"> Statistica Comparata Magistrati per Specifici Oggetti </font>
	       </td>
	       <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	       <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
					<td class="LBG">
						<a class="cliccabile" 
								href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActRicercaStatisticaComparataMagistratiExcel" 
								onClick="javascript:waiting();" >
							<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
						</a>
					</td>
					<td>
      			<div align=center id="waiting" style="visibility:hidden;position:relative;">
      				<img src="/images/rotelle3.gif" height="25" width="25" border="0"> <font class="cRosso">Attendere...</font>
      			</div>
      		</td>
	  	</table>

  <br>
  
  <table cellspacing=2 cellpadding=2>
  
<%
	if( ricercaProcedimento != null ) { 
      	if( ricercaProcedimento.getDataIscrizioneInizio() != null || ricercaProcedimento.getDataIscrizioneFine() != null ) {
%>
        	<tr>
          		<td class="lVerdeNB">Periodo&nbsp;&nbsp;
								dal <%=DateUtils.getDateToString( ricercaProcedimento.getDataIscrizioneInizio(), "dd-MM-yyyy" )%>
								&nbsp;&nbsp;
            		al <%=DateUtils.getDateToString( ricercaProcedimento.getDataIscrizioneFine(), "dd-MM-yyyy" )%>
            	</td>
        	</tr>
<%
		} 
	} 
%>

  </table>
  <br>
<%
  Iterator itx = elencoConteggioRelatori.iterator();

  BigDecimal totalePendentiInizio		= new BigDecimal("0");
  BigDecimal totaleSopravvenuti			= new BigDecimal("0");
  BigDecimal totaleAccolti					= new BigDecimal("0");
  BigDecimal totaleAccoltiProvvisoriamente  = new BigDecimal("0"); // MEV_9
  BigDecimal totaleRigettati				= new BigDecimal("0");
  BigDecimal totaleInammissibilita	= new BigDecimal("0");
  BigDecimal totaleNLPNDP						= new BigDecimal("0");
  BigDecimal totaleIncompetenza			= new BigDecimal("0");
  BigDecimal totaleAltro						= new BigDecimal("0");
  BigDecimal totalePendentiFine			= new BigDecimal("0");
  BigDecimal totalePerErrore				= new BigDecimal("0");
  BigDecimal totaleCancellati				= new BigDecimal("0");  
  BigDecimal totaleUnificati				= new BigDecimal("0");
%>

<style>
	td.int {
		padding-left:5px;
		padding-right:5px
	}
</style>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Magistrato</td>
      <td class="int">Pendenti Inizio Periodo</td>
      <td class="int">Sopravvenuti</td>
      <td class="int">Accolti</td>
      <td class="int">Accolti Provvisoriamente</td>  <%-- MEV_9 --%>    
      <td class="int">Rigettati</td>
      <td class="int">Inammissibilità</td>
      <td class="int">NLP/NDP</td>
      <td class="int">Incompetenza</td>
      <td class="int">Iscritti per Errore</td>
      <td class="int">Unificati</td>
      <td class="int">Cancellati</td>
      <td class="int">Altro</td>
      <td class="int">Pendenti Fine Perido</td>
    </tr>
<%
   while ( itx.hasNext()) {
       IspConteggioRelatoriMagistratiModel elenco = (IspConteggioRelatoriMagistratiModel)itx.next();
       
       totalePendentiInizio = totalePendentiInizio.add(elenco.getNumPendentiInizio());
       totaleSopravvenuti = totaleSopravvenuti.add(elenco.getNumSopravvenuti());
       totaleAccolti = totaleAccolti.add(elenco.getNumDefEsito1());
       // MEV_9
       totaleAccoltiProvvisoriamente = totaleAccoltiProvvisoriamente.add(elenco.getNumAppProvv());
       
       totaleRigettati = totaleRigettati.add(elenco.getNumDefEsito2());
       totaleInammissibilita = totaleInammissibilita.add(elenco.getNumDefEsito3());
       totaleNLPNDP = totaleNLPNDP.add(elenco.getNumDefEsito4());
       totaleIncompetenza = totaleIncompetenza.add(elenco.getNumDefEsito5());
       totaleAltro = totaleAltro.add(elenco.getNumDefEsito6());
       totalePendentiFine = totalePendentiFine.add(elenco.getNumPendentiFine());
       totalePerErrore = totalePerErrore.add(elenco.getNumDefIscErr());
       totaleCancellati = totaleCancellati.add(elenco.getNumCancellati());
       totaleUnificati = totaleUnificati.add(elenco.getNumUnificati());
%>
      <tr>
        <td class="c">
        	<font class="label">
	 					<% if( elenco.getMagistrato().getCodMagistrato() != null ) { %>
	        			<%=StringUtils.toStringJSP(elenco.getMagistrato().getCognome()) + " " + 
	        				 StringUtils.toStringJSP(elenco.getMagistrato().getNome())%>
	        	<% } else { %>
	        			Privi di Magistrato 
	        	<% } %>
        	</font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumPendentiInizio())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumSopravvenuti())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumDefEsito1())%></font>
        </td>
        
        <%-- MEV_9 Accolti provvisoriamente --%>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumAppProvv())%></font>
        </td>
        <%-- MEV_9 - FINE --%>
                
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumDefEsito2())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumDefEsito3())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumDefEsito4())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumDefEsito5())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumDefIscErr())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumUnificati())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumCancellati())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumDefEsito6())%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + elenco.getNumPendentiFine())%></font>
        </td>
      </tr>
<%
  }
%>
      <tr>
      	<td>&nbsp;</td>
      </tr>
      <tr>
        <td class="c">
        	<font class="label">Totale complessivo</font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totalePendentiInizio)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleSopravvenuti)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleAccolti)%></font>
        </td>
        
        <%-- MEV_9 Accolti provvisoriamente --%>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleAccoltiProvvisoriamente)%></font>
        </td>        
        <%-- MEV_9 - FINE --%>
        
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleRigettati)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleInammissibilita)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleNLPNDP)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleIncompetenza)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totalePerErrore)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleUnificati)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleCancellati)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totaleAltro)%></font>
        </td>
        <td class="c">
        	<font class="label"><%=StringUtils.toStringJSP("" + totalePendentiFine)%></font>
        </td>
      </tr>
    </table>

    <br>

    <table width="100%" cellspacing=2 cellpadding=2>
			<tr>
				<td class="Titolo" >Oggetti Selezionati</td>
			</tr>
			<tr>
				<td class="c">
					<select title="elencoOggettiSelezionatiCbx"  name="<%=ICostantiStatistiche.CAMPO_COD_OGGETTO%>" size="10">
						<%= elencoOggettiSelezionatiCbx %>
					</select>
				</td>
			</tr>
		</table>

  </body>
</html>