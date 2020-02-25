<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<jsp:useBean id="strFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>
<%
//==============================================================================
// Form utilizzata GRIGLIA BOTTONI per 
// le Misure di Sicurezza
//==============================================================================

%>
<html>
<head>
  <title>[S.I.E.S.] - <%=strFunzione%></title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

  <script language="JavaScript1.2">
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
  </script>
  

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=strFunzione%></font>
      </td>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActGestioneAltreSanzioni">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
     </tr>
  </table>
  <br>
<%
    // Se il Fascicolo NON è in Sessione, la funzione viene fermata nella ActLoad precedente
    if( !fascicoloNotInSession.equals("S") )
    {
%>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <% } %>
  <br>
	<table cellpadding="5" cellspacing="5" width="96%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">

<!-- Iscrizione Procedimenti -->	

	<tr><td class="Titolonocap" colspan=3>Iscrizione Procedimenti</td></tr>	
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciFascicoloClasseIVdaClasseI">Iscrizione Procedimento da Titolo Esecutivo
			</a>
		</td>      
		<td width="32%" class="menulines" >
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciFascicoloClasseIVdaClasseI&NumerazioneManuale=S">
				Iscrizione Procedimento da Titolo Esecutivo (Assegnazione Manuale)
			</a>
		</td>
		<%-- MEV_39: modificati i nomi delle due seguenti funzionalita' --%>
		<td width="32%" class="menulines"> <!-- nowrap -->
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadRicercaProvvedimentiIscrizioneMisuraFuoriSentenza">
				Iscrizione Procedimento per Misura di Sicurezza applicata con ordinanza dal Magistrato di Sorveglianza e/o in sede di impugnazione dal Tribunale di Sorveglianza
			</a>
		</td>
	</tr>
	<tr>
		<td width="32%" class="menulines">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadRicercaProvvedimentiIscrizioneMisuraFuoriSentenza&NumerazioneManuale=S">
				Iscrizione Procedimento per Misura di Sicurezza applicata con ordinanza dal Magistrato di Sorveglianza e/o in sede di impugnazione dal Tribunale di Sorveglianza (Assegnazione Manuale)
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadRicercaSoggettoIscrizioneMisuraProvvisoria">Iscrizione Procedimento Misura Provvisoria
			</a>
		</td>
		<td width="32%" class="menulines" >
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadRicercaSoggettoIscrizioneMisuraProvvisoria&NumerazioneManuale=S">
				Iscrizione Procedimento Misura Provvisoria <br> (Assegnazione Manuale)
			</a>
		</td>	      
  	</tr>
	
	<tr><td>&nbsp;</td></tr>

<!--Trasmissione Atti -->
	<tr><td class="Titolonocap" colspan=3>Gestione Trasmissione Atti per competenza </td></tr>	
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciTrasmissioneCompetenza">Trasmissione Atti per Competenza 
			</a>
		</td>      
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadRicercaAttiTrasmessi">Verifica Esito Trasmissione Atti Per Competenza
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActRicercaAttiRicevuti">Ricerca Atti Ricevuti per Competenza 
			</a>
		</td>
	</tr>
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadRicercaAttiPresiInCarico">Visualizza Atti presi in Carico 
			</a>
		</td> 
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActRicercaAttiPresiInCaricoDaIscrivere">Iscrizione Procedimenti Classe IV da Atti Presi in Carico
			</a>
		</td>
	  <td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciAnnotaEsitoTrasmissione">Annotazione Riscontro Trasmissioni
			</a>
		</td>
  </tr>
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  <%--tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.sico.web.ActionUnderConstruction">Restituzione Atti presi in Carico 
			</a>
		</td> 
  </tr--%>
	<tr><td>&nbsp;</td></tr>
		
<!--Fase Istruttoria -->	

	<tr><td class="Titolonocap" colspan=3>Fase Istruttoria</td></tr>	
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadRichRiesamePericoloSociale">
				Richiesta Accertamento pericolosità sociale 
			</a>
		</td>      
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciAnnotazioneDecisioneDellaSorveglianza">Annotazione decisione della Sorveglianza
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciRichiestaDAP">Richiesta al DAP 
			</a>
		</td>
	</tr>
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciDesignazioneIstituto">Registrazione Designazione Istituto 
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciAnnotazioneDecisioneGiudiceCassazione">Annotazione decisione del Giudice / Cassazione
			</a>
		</td>
	</tr>
	
	<tr><td>&nbsp;</td></tr>
<!--Esecuzione Misure di Sicurezza-->

	<tr><td class="Titolonocap" colspan=3>Esecuzione Misure di Sicurezza</td></tr>
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
			=siap.siep.misurasicurezza.action.ActLoadInserisciComunicazionePolizia">Comunicazione
		</a>
		</td> 
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOrdinediConsegna">Ordine di Consegna
			</a>
		</td>      
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOEInternamento">Ordine Esecuzione per Internamento
			</a>
		</td>
	</tr>
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOrdineLiberazione">Ordine di Liberazione
			</a>
		</td>
		<%-- MEV_39: aggiunte funzionalità --%>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciRestituzioneOrdineConsegna">Restituzione Ordine di Consegna
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciOLDifferimento">Ordine di Liberazione per Differimento
			</a>
		</td>
	</tr>
	
	<tr><td>&nbsp;</td></tr>
	<!--Definizione Procedimento-->

	<tr><td class="Titolonocap" colspan=3>Definizione Procedimento</td></tr>	
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazioneManuale">Archiviazione Manuale
			</a>
		</td>      
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazionePerProvvSorveglianza">Archiviazione per Provvedimento Sorveglianza
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazionePerProvvGEsecuzione">Archiviazione per Provvedimento Giudice Esecuzione
			</a>
		</td>
	</tr>
	<tr>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazionePerProvvAltroUfficio">Archiviazione per Provvedimento Altro Ufficio
			</a>
		</td>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazionePerProvvGiudiceCassazione">Archiviazione per Provvedimento Giudice/Cassazione
			</a>
		</td>
		<%-- MEV_39: aggiunta funzionalità --%>
		<td width="32%" class="menulines" nowrap>
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>
				=siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazionePerProvvCumulo">Archiviazione per Provvedimento di Cumulo
			</a>
		</td>
	</tr>	
	
	<tr><td>&nbsp;</td></tr>
	</table>
</body>
</html>