<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraCautelareCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="MisuraCautelareCumulo" scope="request" class="siap.siep.modulocumulo.model.MisuraCautelareCumuloModel"/>
<jsp:useBean id="IstitutoDetenzione" scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<html>
<head>
  <title> Dettaglio Misura Cautelare</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Inserisci'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciMisuraCautelareCumulo";
        document.DettaglioMisuraCautelareCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaglioMisuraCautelareCumulo.modalita.value = "I";
        document.DettaglioMisuraCautelareCumulo.submit();
      }      
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciMisuraCautelareCumulo";
        document.DettaglioMisuraCautelareCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaglioMisuraCautelareCumulo.modalita.value = "M";
        document.DettaglioMisuraCautelareCumulo.submit();
      }
      else if (aTipoAzione=='Cancella'){
        var aStato = document.DettaglioMisuraCautelareCumulo.<%= ICostantiMisuraCautelareCumulo.CAMPO_FLAG_STATO %>.value;
        
        if (aStato=='E' || aStato=='M'){
          // Cancellazione Logica Richiedo Motivazione
          lAzione = "siap.siep.modulocumulo.action.ActInserisciMisuraCautelareCumulo";
          document.DettaglioMisuraCautelareCumulo.modalita.value = "C";
          document.DettaglioMisuraCautelareCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          aMotivoModifica = document.DettaglioMisuraCautelareCumulo.<%=ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA%>.value
          var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettaglioMisuraCautelareCumulo"
                                     + "&" + "<%=ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
          window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I'){
          // Cancellazione fisica richiedo conferma
          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciMisuraCautelareCumulo";
            document.DettaglioMisuraCautelareCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.DettaglioMisuraCautelareCumulo.modalita.value = "C";

            document.DettaglioMisuraCautelareCumulo.submit();
          }
        }
      }
      else if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaMisureCautelariCumulo";
        document.DettaglioMisuraCautelareCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaglioMisuraCautelareCumulo.submit();
      }
    }
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Misura Cautelare&nbsp;</font>
      </td>
      <% if (IstruttoriaCumulo.getFlagStato().equals("A")){ %>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Inserisci')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
        <% if (!"C".equals(MisuraCautelareCumulo.getFlagStato())) { %>
        <a href="javascript:eseguiFunzione('Modifica')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <a href="javascript:eseguiFunzione('Cancella')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>      
        <% } %>
      </td>
      <% } %>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioMisuraCautelareCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_ID_MISURA_CAUTELARE_CUMULO %>" value="<%=StringUtils.toStringJSP(MisuraCautelareCumulo.getIdMisuraCautelareCumulo()) %>">
  <input type="hidden" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(MisuraCautelareCumulo.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiMisuraCautelareCumulo.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(MisuraCautelareCumulo.getMotivoModifica()), "") %>">

  <input type="hidden" name="modalita" value="">


<div id="divPosizionamento" align="left" style="padding-left: 25px;">
  <table cellspacing="2" cellpadding="2">


    <tr>
      <td class="l">Natura Misura</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getDescrTipoMisura()) %></font>&nbsp;
      </td>
    </tr>
    
    <tr>
      <td class="l">Periodo sofferto </td>
      <td class="l">
        dal&nbsp; 
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(MisuraCautelareCumulo.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
        <% if (MisuraCautelareCumulo.getDataFine()!=null) {%>
        al&nbsp; 
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(MisuraCautelareCumulo.getDataFine(),"dd-MM-yyyy"))%></font>&nbsp;
        Pari a:
        anni
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getNumAnni()) %></font>&nbsp;
        mesi
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getNumMesi()) %></font>&nbsp;
        giorni
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getNumGiorni()) %></font>&nbsp;
        <% } %>
      </td>
    </tr>
    
    <%-- if (MisuraCautelareCumulo.getGiorni()!=null && MisuraCautelareCumulo.getGiorni().intValue()>0  ) { --%>
    
    <% if ("CL".equals(MisuraCautelareCumulo.getCodTipoMisura())) { %>
    <tr>
      <td class="l">Totale Giorni </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getGiorni()) %></font>&nbsp;
      </td>
    </tr>
    <% } %>
    
    
    <%
    if (   MisuraCautelareCumulo.getIstDetIdIstitutoDetenzione()!=null 
        && !"".equals(MisuraCautelareCumulo.getIstDetIdIstitutoDetenzione())  
       )
    {
    %>
    <tr>
      <td class="l">Luogo di Espiazione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP (IstitutoDetenzione.getDescrizioneIstitutoPerVisualizzazione()) %></font>&nbsp;
      </td>
    </tr>
    <% } else { %>
    <tr>
      <td class="l">Luogo di Espiazione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getAltroLuogoDetenzione()) %></font>&nbsp;
      </td>
    </tr>
    <% } %>

    <tr>
      <td class="l">Autorita' competente per territorio</td>
      <td class="l">
        <% if (MisuraCautelareCumulo.getDescrAutoritaCompetente()!=null && !MisuraCautelareCumulo.getDescrAutoritaCompetente().equals("")) { %>
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getDescrAutoritaCompetente()) %></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getDescrAutoritaCompetenteSede()) %></font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP (MisuraCautelareCumulo.getAutoritaCompetenteIndirizzo()) %></font>&nbsp;
        <% } else {%>
        &nbsp;
        <% } %>
      </td>
    </tr>
    
    <tr><td>&nbsp;</td></tr>
    <%
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
    //==========================================================================
      String lDescStato = "";
      if      ( MisuraCautelareCumulo.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
      else if ( MisuraCautelareCumulo.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
      else if ( MisuraCautelareCumulo.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
      else if ( MisuraCautelareCumulo.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
    %>
    <tr>
      <td class="l" colspan="2"> <%=lDescStato %></td> 
    </tr>
  
    <%
    //========================================================================== 
    // Campo note visualizzato sia in inserimento sia in modifica dove l'utente
    // può motivare l'intervento sui dati su cui sta intervenendo
    //========================================================================== 
    %>
    <% if (!MisuraCautelareCumulo.getFlagStato().equals("E")){ %>
    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(MisuraCautelareCumulo.getMotivoModifica()) %>&nbsp;</font></td>
    </tr>
    <% } %>
</table>
</div>
<br><br>
</form>
</body>
</html>