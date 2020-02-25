<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Stack"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="depositoordinanzapc" scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="documentoAllegato" 	scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel"/>
<jsp:useBean id="StackDiRitorno" 			scope="session" class="java.util.Stack"/>
<jsp:useBean id="notifiche" 					scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"     					scope="request" class="siap.sico.evento.model.EventoModel"/>

<jsp:useBean id="Trasferibile" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile" 				scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Deposito Ordinanza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="/html/conferma.js"></script>
  <script language="JavaScript" >
    function lookUpload()
    {
      var node;
      node=document.getElementById('upld');
      node.style.visibility='visible';
    }
  </script>
</head>

  <body class="corpo">
    <table>
      <tr>
      	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione : </font><font class="campo">Dettaglio Deposito Ordinanza</font>&nbsp;

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  
  // Abilitazione pulsante di stampa
  if( Stampabile.compareTo("SI") == 0 )
  {
%>
  			<!-- BOTTONE DI STAMPA -->
    		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
    			<jsp:param name="CampoIdEntita" value="IdDocumentoAllegato"/>
      		<jsp:param name="ValoreIdEntita" value="<%=documentoAllegato.getIdDocumentoAllegato()%>"/>
    		</jsp:include>
<% 
  }
	// Abilitazione pulsanti alla modifica
 	if( Modificabile.compareTo("SI") == 0 )
	{
%>    
  	<td class="LBG">
   		<a href="/jsp/Main.jsp?Action=siap.sius.depositoordinanzapc.action.ActLoadInserisciDataDeposito&Aggiungi=yes&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoordinanzapc.getIdEventoGenerato()%><%=retParam%>">
      	<img align="middle" src="/images/new24.gif" alt="Iscrizione Altri Destinatari" width="24" height="24" border="0">
      </a>
    </td>
   	
   	<td class="LBG">
   		<a href="/jsp/Main.jsp?Action=siap.sius.depositoordinanzapc.action.ActLoadModificaDataDepositoOrdinanza&Aggiungi=yes&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoordinanzapc.getIdEventoGenerato()%><%=retParam%>">
      	<img  align="middle" src="/images/modifica24.gif" alt="Modifica Deposito Ordinanza" width="24" height="24" border="0">
      </a>
    </td>    	
<%
	}
  // Abilitazione al trasferimento.
	if( Trasferibile.compareTo("SI") == 0 )
	{
%>
		<!-- BOTTONE DI TRASFERIMENTO -->
    <td class="LBG">
    	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositoordinanzapc.action.ActLoadTrasferisciOrdinanza&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoordinanzapc.getIdEventoGenerato()%>">
      	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasferisci" width="24" height="24" border="0">
      </a>
    </td>
<%  
	}
%>
<!--  BOTTONE INSERITO DA MAX GRIFANTINI per Trasferire PROVVEDIMENTO DI ESECUZIONE a NSC -->
<!-- La variabile lVisualizzaBottone è stata inserita in quanto tale bottone non deve essere visibile nel
     Rilascio della Release 5.0 -->
<% String lVisualizzaBottone= "NO";
	 if (lVisualizzaBottone.equals("SI"))
	 {    
%>
			<td class="LBG">
					<a href="/jsp/Main.jsp?Action=siap.sico.webservice.action.ActUDSTrasferimentoEsecuzione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoordinanzapc.getIdEventoGenerato()%>&IdDocumentoAllegato=<%=documentoAllegato.getIdDocumentoAllegato()%>">
      			<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>wsSiesToNscUDS.gif" alt="Trasferimento Provvedimento Esecuzione da SIES a NSC" width="24" height="24" border="0">
      		</a>
 			</td>    	    
		  
<% } %>
<!-- ---------------------------------------------------------------------------------------> 
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

  <tr>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  </tr>
</table>

<table cellspacing=4 cellpadding=4>
  <tr>
    <td>&nbsp;</td>
    <input Title="Id Evento" type="hidden" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoordinanzapc.getIdEventoGenerato()%>" >
  </tr>

  <tr>
    <td class="l"> Anno / Numero Ordinanza</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(depositoordinanzapc.getAnnoS3())%> / <%=StringUtils.toStringJSP(depositoordinanzapc.getNumS3())%></font></td>
  </tr>

  <tr>
    <td class="l"> Data Emissione</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(depositoordinanzapc.getDataCameraConsiglio(),"dd/MM/yyyy")%></font></td>
  </tr>
  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(depositoordinanzapc.getDataDeposito(),"dd/MM/yyyy")%></font></td>
  </tr>
  <tr>
  	<td class="l"> Stato del deposito</td>
<% 
		if (evento.getFlagDocumentoRegistrato() != null && evento.getFlagDocumentoRegistrato().equalsIgnoreCase("A"))
		{	 
%>
    <td class="l"><font class="cRosso">ANNULLATO</font></td>
<% 
	 	} 
		else if (documentoAllegato.getFlagDocumentoRegistrato() != null && documentoAllegato.getFlagDocumentoRegistrato().compareTo("S") == 0)
   	{
%>
    <td class="l"><font class="campo">Validato</font></td>
<% 	} 
		else
   	{
%>
    <td class="l"><font class="campo">Da Validare </font></td>
<% 	
		} 
%>
</tr>

<%
  Iterator itx2 = notifiche.iterator();
  if ( itx2.hasNext())
  {
    NotificaModel notifica = (NotificaModel)itx2.next();
%>  <tr>
    <td class="l"> Data Trasferimento Atti</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(notifica.getDataInvio() ,"dd/MM/yyyy")%></font></td>
    </tr>
<%}%>

  <tr>
      <td  colspan=2> &nbsp;</td>
  </tr>

</table>

  <jsp:include page="<%=ICostantiUdienza.PG_LOAD_DESTINATARI%>"/>


 <div align=left style="visibility:hidden" id="upld">
 	<FORM name="comandi" enctype="multipart/form-data" method="post">
  <table>
  	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
    <tr>
    	<td class="L">
      	<input  class=bottone  type="submit" value="Conferma">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.documentoallegato.action.ActUploadDocumentoAllegato">
        <input type="HIDDEN" name="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>"  value="<%=documentoAllegato.getIdDocumentoAllegato()%>">
        <input type="HIDDEN" name="<%=ICostantiDocumentoAllegato.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza">
        <input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.DEPOSITO_ORDINANZA%>"> 
        <input type="HIDDEN" name="<%= ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoordinanzapc.getIdEventoGenerato()%>" >
        <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>">
      </td> 
    </tr>
  </table>

  </FORM>
</div>

</body>
</html>