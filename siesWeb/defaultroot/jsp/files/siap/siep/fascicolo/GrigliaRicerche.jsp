<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

<script language="JavaScript">

  function submProcedimento()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadRicercaFascicolo";

    //document.f.submit();
  }
	
	function submProcedimentoReato()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadElencoProcReato";
  }

	function submTitoloEsec()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadRicercaSentenza";
  }

	function submTitoloEsecSoggetto()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadRicercaSentenzaSoggetto";
  }

	function submSoggetto()
  {
   document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadRicercaSoggetto";
  }

	function submIstanza()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istanza.action.ActLoadRicercaIstanza";
  }
  
	// per link vuoto
	function submvuoto()
	{
	}

	function submProcedimentoSoggetto()
 	{
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerSoggetto";
  }

	function submEsitiRichiesteSoggetto()
	{
		document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.jms.action.ActLoadListaEsitiRicercaSoggAltreBDI";
	}

	function submEsitiAltriProcBDI()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.jms.action.ActLoadListaEsitiRicercaFascAltreBDI";
  }

	// per link a messaggio "funzione da implementare"
	function submDaImplementare()
  {
	  document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction";
	}

	function submProcedimentiNonValidati()
  {
	  document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadRicercaFascicoliNonValidati";
	}

	function submProvvedimentiNonValidati()
  {
	  document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActRicercaProvvedimentiNonValidati";
	}
	
  function submOmesseNotifiche()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActRicercaOmesseNotificheOE";
  }

  function submSospesiInterrotti()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadRicercaSospesiInterrotti";
  }
 
  function submAttiTrasmessi()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.jms.action.ActLoadListaMessaggiTrasmessi";
  }
  
  function submProcedimentoMisuraSicurezza()
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActRicercaFascicoliMisuraSicurezza";
  }
  
  function submProcedimentoRGNR()
  {
	document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadRicercaProcedimentoRGNR";  
  }
</script>

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

  <style>
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
  </style>
  <title>[S.I.E.S.] - Ricerche </title>
</head>

<body class="corpo">
    <table>
    <tr>
        <td class="lbg">
          <font class="label">Funzione :&nbsp;</font><font class="campo">Ricerche</font>
        </td>
    </tr>
    </table>
    
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
	    <tr><td class="Titolo" colspan=4>Procedimento</td></tr>
			<tr>
				<td width="32%" class="menulines" nowrap><a href="javascript:submProcedimento();">Procedimento</a></td>
	      <td width="32%" class="menulines" nowrap><a href="javascript:submProcedimentoSoggetto();">Procedimento Per Soggetto</a></td>
				<td width="32%" class="menulines" nowrap><a href="javascript:submProcedimentoReato();">Procedimento Per Reato</a></td>
	    </tr>
	   <tr>
				<td width="32%" class="menulines" nowrap><a href="javascript:submEsitiAltriProcBDI();">Esiti Ricerche Procedimenti</a></td>
				<td width="32%" class="menulines" nowrap><a href="javascript:submSospesiInterrotti();">Elenco Procedimenti Sospesi/Interrotti</a></td>
				<td width="32%" class="menulines" nowrap><a href="javascript:submProcedimentoMisuraSicurezza();">Elenco Procedimenti con Misura di Sicurezza</a></td>				
			</tr>
			<tr><td>&nbsp;</td></tr>
	    <tr><td class="Titolo" colspan=4>Titolo Esecutivo</td></tr>
			<tr>
				<td width="32%" class="menulines" nowrap><a href="javascript:submTitoloEsec();">Titolo Esecutivo</a></td>
				<!--td width="32%" class="menulines" nowrap><a href="javascript:submDaImplementare();">Esiti Ricerche Titolo Esecutivo</a></td-->
				<td width="32%" class="menulines" nowrap><a href="javascript:submTitoloEsecSoggetto();">Titolo Esecutivo Per Soggetto</a></td>
				<td width="32%" class="menulines" nowrap><a href="javascript:submProcedimentoRGNR();">Procedimento per RGNR</a></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
	    <tr><td class="Titolo" colspan=4>Soggetto</td></tr>
			<tr>
				<td width="32%" class="menulines" nowrap><a href="javascript:submSoggetto();">Soggetto</a></td>
				<td width="32%" class="menulines" nowrap><a href="javascript:submEsitiRichiesteSoggetto();">Esiti Ricerche Soggetto</a></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
	    <tr><td class="Titolo" colspan=4>Istanza</td></tr>
			<tr>
				<td width="32%" class="menulines" nowrap><a href="javascript:submIstanza();">Istanza ante Rel.5.0</a></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
	    <tr><td class="Titolo" colspan=4>Elenchi</td></tr>
			<tr>
				<td width="32%" class="menulines" nowrap><a href="javascript:submProvvedimentiNonValidati();">Provvedimenti non Validati</a></td>
				<td width="32%" class="menulines" nowrap><a href="javascript:submProcedimentiNonValidati();">Procedimenti non Validati</a></td>
				<td width="32%" class="menulines" nowrap><a href="javascript:submOmesseNotifiche();">Omesse Notifiche OE con Sospensione</a></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
	    <tr><td class="Titolo" colspan=4>Riscontro Trasmissioni</td></tr>
			<tr>
				<td width="32%" class="menulines" nowrap><a href="javascript:submAttiTrasmessi();">Atti Trasmessi</a></td>
			</tr>
  	</table>
</body>
</html>