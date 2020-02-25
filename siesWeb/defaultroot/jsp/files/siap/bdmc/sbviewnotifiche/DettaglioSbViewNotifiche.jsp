<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel"%>
<%@ page import="siap.bdmc.sbviewnotifiche.action.ICostantiSbViewNotifiche"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewnotifiche" scope="request" class="siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel"/>
<jsp:useBean id="lFascicoloSiep" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio SbViewNotifiche </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione SbViewNotifiche </title>
  <%}%> 
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    function Verify() { 
      var msgConfirm = "Si vuole procedere con la cancellazione dei dati?";
 
      if (window.confirm(msgConfirm)) 
        return true; 
      else 
        return false; 
    } 

    function ConfermaPresaInCarico() { 
       var msgConfirm = "";

	   	if (<%=sbviewnotifiche.getCodiNoti().equals("005")%>)
	   			msgConfirm = "La notifica corrente sarà eliminata dalla lista e " + "la trasmissione dei provvedimenti del SIES verso BDMC sarà attivata/disattivata automaticamente." + "\r" + "Si vuole procedere ?";
	   	else
				msgConfirm = "La notifica corrente sarà eliminata dalla lista." + "\r" + "Si vuole procedere ?";

       	if (window.confirm(msgConfirm)){
       		// alert("Ho premuto il bottone OK");
	   		// if (<//%=StringUtils.toStringJSP(sbviewnotifiche.getCodiNoti().equals("003"))%>){
	   			// alert ("Chiamata alla SP per aggiornare il flag di trasmissione nella tabella");
			// submit
			strUrl= "Main.jsp";
			//alert (strUrl);
 			document.DettaglioSbViewNotifiche.action=strUrl;
 			//document.all.< %=ISIAPCostantiWeb.ACTION_FIELD%>.value ="siap.bdmc.sbviewnotifiche.action.ActRicercaSbViewNotifiche";
 			document.all.<%=ISIAPCostantiWeb.ACTION_FIELD%>.value ="siap.bdmc.sbviewnotifiche.action.ActVariazioneStatoSbViewNotifiche";
 			// alert ("Ho premuto il bottone OK ==> Chiamata alla SP per aggiornare il flag di trasmissione nella tabella");
 			document.DettaglioSbViewNotifiche.submit();
	   		//}
  			return true;
        } 
       	else{
       		// alert("Ho premuto il bottone CANCEL"); 
			// submit
			strUrl= "Main.jsp";
			// alert (strUrl);
 			document.DettaglioSbViewNotifiche.action=strUrl;
 			document.all.<%=ISIAPCostantiWeb.ACTION_FIELD%>.value ="siap.bdmc.sbviewnotifiche.action.ActLoadDettaglioSbViewNotifiche";
 			// alert ("Ho premuto il bottone CANCEL ==> selezionare Chiudi Dettaglio per ritornare ad elenco notifiche ");
 			document.DettaglioSbViewNotifiche.submit();

        	 return false;
        } 
    } 

    function ChiudiDettaglio() { 
			// submit
			strUrl= "Main.jsp";
			// alert (strUrl);
 			document.DettaglioSbViewNotifiche.action=strUrl;
 			document.all.<%=ISIAPCostantiWeb.ACTION_FIELD%>.value ="siap.bdmc.sbviewnotifiche.action.ActRicercaSbViewNotifiche";
 			alert ("Ho selezionato Chiudi Dettaglio ==> visualizzo elenco notifiche");
 			document.DettaglioSbViewNotifiche.submit();
  			return true;
    } 
 
  </script>
  
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
      <%  if( modalita.equals("D") )  {%> 
        <font class="campo">Dettaglio Notifiche</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione Notifiche</font>
      <%}%> 
      </td>
       <td class="LBG">
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- 
      <td class="LBG">
         <a href="Main.jsp?Action=siap.bdmc.sbviewnotifiche.action.ActLoadInserisciSbViewNotifiche&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.bdmc.sbviewnotifiche.action.ActLoadModificaSbViewNotifiche&ProgNoti=<\\%=sbviewnotifiche.getProgNoti()%>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.bdmc.sbviewnotifiche.action.ActLoadCancellaSbViewNotifiche','ProgNoti','<\\%=sbviewnotifiche.getProgNoti()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
--%>
    </tr>
  </table>
</FORM>

<FORM method="POST" action="" name="DettaglioSbViewNotifiche">
<!-- FORM method="POST" action="Main.jsp" name="CancellaSbViewNotifiche" -->
<!-- FORM method="POST" action="" name="CancellaSbViewNotifiche" -->

  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbviewnotifiche.action.ActRicercaSbViewNotifiche">
  <input type="HIDDEN" name="<%=ICostantiSbViewNotifiche.CAMPO_PROG_NOTI%>" value="<%=StringUtils.toStringJSP(sbviewnotifiche.getProgNoti()) %>">
  
  
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<table width="100%">
	<tr>
    	<td class="l" width="15%">Progressivo</td>
    	<td class="l" width="35%"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getProgNoti()) %></font>&nbsp;</td>
    	<td class="l" width="15%">Descrizione</td>
    	<td class="l" width="35%"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getDescrizione()) %></font>&nbsp;</td>
	</tr>
	<tr>
    	<td class="l">Data Registrazione</td>
    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataRegiNoti(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    	<td class="l">Data Validità</td>
    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataValiNoti(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
	</tr>
  	<tr>
    	<td class="l">Ufficio BDMC</td>
	    <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getDescriUffi()) %></font>&nbsp;</td>
  	</tr>
  	<tr>
    	<td class="l">Note</td>
    	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getNote()) %></font>&nbsp;</td>
  	</tr>
</table><br><br>
  <%//  if( StringUtils.toStringJSP(sbviewnotifiche.getFlagModi()).equals("M") )  {%> 
   	  
  
	<% if( StringUtils.toStringJSP(sbviewnotifiche.getCodiNoti()).equals("0003") )  {%> 
	<!-- font class="campo">Notifica di modifica periodo di misura cautelare</font -->
	<font class="campo">Variazione periodo di misura ISCRITTO</font>
	<table width="100%">
		<tr>
    		<td class="l" width="35%">Id Prenotazione</td>
    		<td class="l" width="15%"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getIdPren()) %></font>&nbsp;</td>
    		<td class="l" width="35%">Progressivo</td>
    		<td class="l" width="15%"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getProgPeri()) %></font>&nbsp;</td>
		</tr>
  		<tr>
    		<td class="l">Inizio periodo precedente</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataInizPrec(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    		<td class="l">Fine periodo precedente</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataFinePrec(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  		</tr>
  		<tr>
    		<td class="l">Inizio periodo modificato</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataIniz(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    		<td class="l">Fine periodo modificato</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataFine(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  		</tr>
	</table>
	<table cellspacing=1 cellpadding=4>  		
  		<tr>
    		<td class="l" >Anno/Numero Fascicolo BDMC</td>
    		<td class="l" align="right"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getModiAnnoFascBdmc()) %></font>&nbsp;</td>
    		<td class="l" >/</td>
    		<td class="l" align="left"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getModiNumeFascBdmc()) %></font>&nbsp;</td>
  		</tr>
	</table>	  
 <%} else if( StringUtils.toStringJSP(sbviewnotifiche.getCodiNoti()).equals("0004") ) { %> 
  	 <font class="campo">Variazione periodo di misura COMPUTATO</font> 
	<table width="100%">
		<tr>
    		<td class="l">Inizio periodo</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataIniz(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    		<td class="l">Fine periodo</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewnotifiche.getDataFine(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  		</tr>
	</table>
<% 	if (lFascicoloSiep != null) {%>
	<table cellspacing=1 cellpadding=4>  		
  		<tr>
    		<td class="l" >Anno/Numero Fascicolo Siep</td>
    		<td class="l" align="right"><font class="campo"><%=StringUtils.toStringJSP(lFascicoloSiep.getChiaveAnno()) %></font>&nbsp;</td>
    		<td class="l" >/</td>
    		<td class="l" align="left"><font class="campo"><%=StringUtils.toStringJSP(lFascicoloSiep.getChiaveProgr()) %></font>&nbsp;</td>
  		</tr>
	</table>	  
<% } %>
	

  <%//} else {%> 
  <%} else if( StringUtils.toStringJSP(sbviewnotifiche.getCodiNoti()).equals("0005") ) { %> 

  	 <!-- font class="campo">Notifica di stop trasmissione provvedimento del SIES</font> -->
  	 <font class="campo">Comunicazione CESSAZIONE/RIPRISTINO trasmissioniS</font>
	<table cellspacing=4 cellpadding=4>
		<tr>
    		<td class="l">Anno Fascicolo BDMC</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getStopAnnoFascBdmc()) %></font>&nbsp;</td>
		</tr>
  		<tr>
    		<td class="l">Numero Fascicolo BDMC</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getStopNumeFascBdmc()) %></font>&nbsp;</td>
  		</tr>
  		<tr>
    		<td class="l">Attiva/Disattiva</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewnotifiche.getFlagTras()) %></font>&nbsp;</td>
  		</tr>
  		  <input type="HIDDEN" name="annoBdmc" value="<%=sbviewnotifiche.getStopAnnoFascBdmc() %>"/>
		  <input type="HIDDEN" name="numeroBdmc" value="<%=sbviewnotifiche.getStopNumeFascBdmc() %>"/>
		  <input type="HIDDEN" name="ufficioBdmc" value="<%=sbviewnotifiche.getCodiUffi() %>"/>
		  <input type="HIDDEN" name="flagAssociazione" value="<%=sbviewnotifiche.getFlagTras() %>"/>
	</table>
 	<%} else {%> 
 	 <font class="campo">Codice Notifica non previsto</font>
  <%} %> 
<br><br>
<table cellspacing=4 cellpadding=4>
 <tr>
    <td align="center">
      <input class="bottone" type="submit" name="conferma" value="Presa in carico"  onclick="Javascript:ConfermaPresaInCarico();">
    </td>
    <td align="center">
    </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<!-- <td align="center">
      <input class="bottone" type="submit" name="conferma" value="    Chiudi    "  onclick="Javascript:ChiudiDettaglio();">
      <input class="bottone" type="submit" name="conferma" value="    Chiudi    "  onclick="location.href='/jsp/Main.jsp?Action=siap.bdmc.sbviewnotifiche.action.ActRicercaSbViewNotifiche'" 
    </td>
-->
  </tr>
</table>

</FORM>
</body>
</html>