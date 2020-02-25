<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="siap.jms.jmscode.action.ICostantiJmsCode" %>
<%@ page import="siap.jms.jmscode.model.JmsCodeModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="NumerazioneManuale"    scope="request" class="java.lang.String"/>

<%
String assegnazione_manuale = "N";
if ("S".equals(NumerazioneManuale))
  assegnazione_manuale = "S";
else 
  assegnazione_manuale = "N";

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//siesLogger.debug(" --XX-- LoadRicerca - assegnazione_manuale = "+assegnazione_manuale);

%>

<!-- 		LoadRicercaProvvedimentiIscrizioneMisuraFuoriSentenza 		-->
<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <title> [S.I.E.S.] Load Ricerca Provvedimenti - Misura Sicurezza Fuori Sentenza </title>

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  
	function Verify()
    {

	    if (document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value.length==1)
			document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value='0'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value;
		if (document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value.length==1)
			document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value='0'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value;
		var data_to_verify=document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value+'/'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value+'/'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>.value;

		if (! ControllaData(data_to_verify) && data_to_verify.length>2)
		{
               alert('Data INIZIALE non valida');
               document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.focus();
			   return false;
		}
		
	    if (document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value.length==1)
			document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value='0'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value;
		if (document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value.length==1)
			document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value='0'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value;
		var data_to_verify=document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value+'/'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value+'/'+document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>.value;

		if (! ControllaData(data_to_verify) && data_to_verify.length>2)
		{
               alert('Data FINALE non valida');
               document.LoadRicercaProvvMisuraFuori.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.focus();
			   return false;
		}

    	return true;
  	}

</script>
</head>

<body class="corpo">

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaProvvMisuraFuori">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActRicercaProvvedimentiIscrizioneMisuraFuoriSentenza">

    <input type="HIDDEN" name="<%=ICostantiJmsCode.CAMPO_DESCRIZIONE%>">
	<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
	<input type="HIDDEN" name="<%=ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS%>" value="<%=assegnazione_manuale %>">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> 
        				<font class="campo">Iscrizione Procedimento Misura Sicurezza disposta fuori sentenza - Ricerca Provvedimento Sorveglianza</font></td>
      </tr>
    </table>
<br>
<table width="90%" >
 	<tr>
    	<td class="l" width="30%" >Indicare intervallo di Date Deposito</td>
        <td class="l">Data Iniziale </td>
        <td class="l"><input title="Data inizio GG" type="text" name="<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%> >
            /
            <input title="Data inizio MM" type="text" name="<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
            /
            <input title="Data inizio AAAA" type="text" name="<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>"maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>
        <td class="l">Data Finale </td>
        <td class="l"><input title="Data fine GG" type="text" name="<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%> >
            /
            <input title="Data fine MM" type="text" name="<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
            /
            <input title="Data fine AAAA" type="text" name="<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>"maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>        
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr> 
   		<td class="l" colspan=2>
   			<input type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ELABORATO %>" value="ELABORATI" >Visualizza anche Provvedimenti già Elaborati &nbsp;&nbsp;
   		</td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
    <tr>
        <td colspan="2">
          <INPUT class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
    </tr>
</table>
 </form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
         var frmvalidator  = new Validator("LoadRicercaProvvMisuraFuori");

          frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno DATA INIZIO è di 4 caratteri");
          frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","numeric");
          frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","gt=1900");

          frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno DATA FINE è di 4 caratteri");
          frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","numeric");
          frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","gt=1900");
          
          frmvalidator.setAddnlValidationFunction("Verify"); 

          </script>