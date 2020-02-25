<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>

<%@ page import="siap.util.SIESSwitch" %>

<jsp:useBean id="strFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - <%=strFunzione%></title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

  <script language="JavaScript1.2">
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

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=strFunzione%></font>
      </td>
      <td class="LBG">
      </td>
     </tr>
  </table>
  <br>
<%
    // Se il Fascicolo è in Sessione fa l'include del DettaglioSoggettoSentenza
    if( !fascicoloNotInSession.equals("S") )
    {
%>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<%
    }
%>
  <br>
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
      <tr>
        <td colspan=3 class="Titolonocap">Richieste Istruttorie</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadCopertinaFasc">Stampa Copertina</a>
        </td>
        <td width="32%" class="menulines" nowrap>        
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciInizioEsecuzione">Comunicazione Inizio Esecuzione </a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciEstrattoSentenze">Richiesta Sentenza Integrale</a>
        </td>
      </tr>
    <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciCertificatoEsecuzione">Certificato Stato Esecuzione</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciCertificatoPenale">Richiesta Certificato Penale</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciCertificatoDap">Richiesta Certificato DAP</a>
        </td>
      </tr>
    <tr>
       <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciPosizioneGiuridica">Richiesta Posizione Giuridica da Istituto Penitenziario</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciInfoArrestoDenuncia">Informazioni Arresto e/o Denuncia</a>
        </td>
        
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciAnagraficaCittadini">Accertamento Anagrafica</a>
        </td>
    </tr>
    <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciArrestiDomiciliariPrecedenti">Arresti Domiciliari Precedenti</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciArrestiDomiciliariAttuali">Arresti Domiciliari Attuali</a>
        </td>
      <td width="32%" class="menulines" nowrap>
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciRichiestaCodiceCui">Richiesta Codice CUI e Cartellino Dattiloscopico</a>
          </td><td>&nbsp;</td><td>&nbsp;</td>   
   </tr>
    <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciNotEspSanSost">Notizie - Espulsione Sanzione Sostitutiva</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadInserisciIstruttoriaRichiestaPagamentoPP">Notizie - Pagamento Pena Pecuniaria</a>
        </td>
   </tr>
  
  <tr><td>&nbsp;</td></tr>
  
   <tr>
        <td colspan=3 class="Titolonocap">Richieste Istruttorie Stampe Multiple</td>
      </tr>
      <tr>
      <td width="32%" class="menulines" nowrap>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadRicercaStampaMultiCopertine">Stampa Copertine Multiple</a>
        </td>
      <td width="32%" class="menulines" nowrap>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActLoadRicercaStampaMultiInizioEsecuzione">Prenota Stampa Inizio Esecuzione Multiple</a>
        </td>
        <td width="32%" class="menulines" nowrap>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.stampadocumenti.action.ActRicercaStampaDocumenti">Verifica Stampa Inizio Esecuzione Multiple</a>
        </td>
    </tr>
  
  <tr><td>&nbsp;</td></tr>
    
    <tr>
      <td colspan=3 class="Titolonocap">Richieste ad altre Autorità Giudiziarie ed altri atti del PM</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadInserisciRichiesteComunicazione">Richiesta/Comunicazione</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadInserisciParereVistoRicorso">Parere/Visto/Ricorso</a>
      </td>
      <td width="32%" class="menulines" nowrap>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadInserisciTrasmissione">Trasmissione Atti / Richieste ex art. 51 bis</a>
      </td>
    </tr>
    <tr>
      <!--td width="32%" class="menulines" nowrap style="background-color: rgb(255,255,153);"-->
      <%
      Date lDataTrasmissioneAl = DateUtils.getSysDate();
      Date lDataTrasmissioneDal = DateUtils.moveDateTo (lDataTrasmissioneAl,Calendar.MONTH,-2);
      String lStringParametri = "";
      lStringParametri += "&"+ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO+"="+DateUtils.getDateToString(lDataTrasmissioneDal,"yyyy");
      lStringParametri += "&"+ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO+"="+DateUtils.getDateToString(lDataTrasmissioneDal,"MM");
      lStringParametri += "&"+ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO+"="+DateUtils.getDateToString(lDataTrasmissioneDal,"dd");

      lStringParametri += "&"+ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE+"="+DateUtils.getDateToString(lDataTrasmissioneAl,"yyyy");
      lStringParametri += "&"+ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE+"="+DateUtils.getDateToString(lDataTrasmissioneAl,"MM");
      lStringParametri += "&"+ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE+"="+DateUtils.getDateToString(lDataTrasmissioneAl,"dd");

      
      %>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadRichiesteAttiRicevute<%=lStringParametri%>">Richieste Atti per Competenza Ricevute</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadGrigliaTrasmissioneCompetenza">Trasmissione per Competenza/Seguito Atti</a>
      </td>
      <td width="32%" class="menulines" nowrap>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadRicercaTrasmissioniSolleciti">Riscontro Trasmissioni/Solleciti</a>
      </td>
      <td width="32%">
        &nbsp;
      </td>
    </tr>    
  </table>
</body>
</html>