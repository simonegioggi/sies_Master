<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils "%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.NotificaCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="Provvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<html>
<head>
  <title> Dettaglio Presofferto</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Inserisci'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciDecretiSospPM";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.modalita.value = "I";
        
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = "";
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value = "";
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>.value = "";
        
        document.formName.submit();
      }      
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciDecretiSospPM";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.modalita.value = "M";
        document.formName.submit();
      }
      else if (aTipoAzione=='Cancella'){
        var aStato = document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value;
        
        if (aStato=='E' || aStato=='M'){
          // Cancellazione Logica Richiedo Motivazione
          lAzione = "siap.siep.modulocumulo.action.ActInserisciDecretiSospPM";
          document.formName.modalita.value = "C";
          document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          aMotivoModifica = document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>.value
          var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"formName"
                                     + "&" + "<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
          window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I'){
          // Cancellazione fisica richiedo conferma
          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciDecretiSospPM";
            document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.formName.modalita.value = "C";

            document.formName.submit();
          }
        }
      }
      else if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaDecretiSospPM";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
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
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Decreti di Sospensione&nbsp;</font>
      </td>
      <% //if (IstruttoriaCumulo.getFlagStato().equals("A")){ %>
      <%--td class="LBG">
        <a href="javascript:eseguiFunzione('Inserisci')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
        <% //if (!"C".equals(Provvedimento.getFlagStato())) { %>
        <a href="javascript:eseguiFunzione('Modifica')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <a href="javascript:eseguiFunzione('Cancella')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>      
        <% //} %>
      </td --%>
      <%// } %>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
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

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">

  <input type="hidden" name="modalita" value="">


<div id="divPosizionamento" align="left" style="padding-left: 25px;">

  <%
  //==========================================================================
  // Sospensione Simeone
  //==========================================================================
  %>  
  <% if (   StatoEsecuzioneCumuloUtils.isSospensioneC5Provv  (Provvedimento.getCodMotivo())
         || StatoEsecuzioneCumuloUtils.isSospensioneC5VVR    (Provvedimento.getCodMotivo())
         || StatoEsecuzioneCumuloUtils.isSospensioneC5DecIrr (Provvedimento.getCodMotivo())
         || StatoEsecuzioneCumuloUtils.isSospC5Revoca        (Provvedimento.getCodMotivo())
        ) 
  { %>
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="Titolo" colspan="8">Sospensione ex art. 656 comma 5 c.p.p.</td>
    </tr>
    <tr>
      <td class="l" width="250px">Provvedimento</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%>
        </font> 
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr> 
    
    
    <%
    if ( StatoEsecuzioneCumuloUtils.isSospensioneC5Provv  (Provvedimento.getCodMotivo()) )
    {
      NotificaCumuloModel lNotificaC5Avv = new NotificaCumuloModel();
      NotificaCumuloModel lNotificaC5Int = new NotificaCumuloModel();
      Vector <NotificaCumuloModel> lListaNotificaC5 = Provvedimento.getListaNotifiche();
      
      if (lListaNotificaC5!=null){
        Iterator lItxNotC5 = lListaNotificaC5.iterator();
        while (lItxNotC5.hasNext()) {
          NotificaCumuloModel lNot = (NotificaCumuloModel)lItxNotC5.next();
          if (lNot.getCodTipoNotifica().equals("N"))
            lNotificaC5Avv = lNot;
          else if (lNot.getCodTipoNotifica().equals("E"))
            lNotificaC5Int = lNot;
        }
      }
    %>
    <tr>
      <td class="l">Notifica Avvocato</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaC5Avv.getDataAvvenutaNotifica(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr> 
    
    <tr>
      <td class="l">Notifica al Condannato</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaC5Int.getDataAvvenutaNotifica(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <% } %>
    
    <% if ( StatoEsecuzioneCumuloUtils.isSospensioneC5VVR  (Provvedimento.getCodMotivo()) )  { %>
    <tr>
      <td class="l">Autorità che ha redatto il verbale</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrAutoritaEmittente())%></font> 
        di 
        <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrLuogoEmittente())%></font>
      </td>
    </tr> 
    <% } %>
    
    <% if ( StatoEsecuzioneCumuloUtils.isSospC5Revoca  (Provvedimento.getCodMotivo()) )  { %>
    <tr>
      <td class="l">Motivo Revoca</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrMotivoRevoca())%></font> 
      </td>
    </tr>
    
      <% if ("0001".equals (Provvedimento.getCodMotivoRevoca()) ) { %>
      <tr>
        <td class="l">Motivo Revoca PM</td>
        <td class="l" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrMotivoRevocaPm())%></font> 
        </td>
      </tr>
      <tr>
        <td class="l">Motivazioni</td>
        <td class="l" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getNote())%></font>&nbsp;
        </td>
      </tr>      
      <% } %>    
         
    <% } %>    
  </table>
  <% } %>
  
  <%
  //==========================================================================
  // Sospensione Simeone - Istanza
  //==========================================================================
  %>  
  <% if (   StatoEsecuzioneCumuloUtils.isSospensioneC5Istanza  (Provvedimento.getCodMotivo())) { %>
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="Titolo" colspan="8">Sospensione ex art. 656 comma 5 c.p.p.</td>
    </tr>    
    <tr>
      <td class="l" width="100px">Istanza</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(Provvedimento.getDescrContenutoIstanza())%>
        </font> 
      </td>
    </tr>
    <tr>
      <% if ("D".equals(Provvedimento.getFlagIstanzaPresdep())) { %>
      <td class="l">Depositata</td>
      <% } else if ("P".equals(Provvedimento.getFlagIstanzaPresdep())) { %>
      <td class="l">Pervenuta</td>
      <% } %>
      <td class="l" colspan="3">&nbsp;in data
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataIstanza(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>     
    <tr>
      <td class="l">Trasmessa a</td>
      <% if (Provvedimento.getDescrTipoUfficioDestinatario()!=null && !Provvedimento.getDescrTipoUfficioDestinatario().equals("")) { %>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrTipoUfficioDestinatario())%></font> 
        di
        <font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrLuogoDestinatario())%></font> 
        in data        
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataTrasmissione(),"dd-MM-yyyy"))%></font>
      </td>
      <% } else { %>
      <td class="l" colspan="3">&nbsp;</td>
      <% } %>
    </tr> 
  </table>   
  <% }%> 


  <%
  //==========================================================================
  // Sospensione Simeone - Ordinanza di Revoca
  //==========================================================================
  %>  
  <% if (   StatoEsecuzioneCumuloUtils.isSospC5RevocaSorv  (Provvedimento.getCodMotivo(),Provvedimento.getFlagTipoSosp())) { %>
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="Titolo" colspan="8">Dati Ordinanza della Sorveglianza</td>
    </tr>
    <tr>
      <td class="l" width="250px">Provvedimento</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%>
        </font> 
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr> 
    <tr>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getAnnoProcedimento()) %></font>
        /<font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getProgrProcedimento()) %></font>          
        &nbsp; <font class="label">Anno / Numero Provvedimento</font>
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getAnnoProvvedimento()) %></font>
        /<font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getProgrProvvedimento()) %></font>
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
      <td class="l" colspan="3">          
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getDescrUfficioEmittente()) %></font>
         di <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getDescrLuogoEmittente()) %></font>          
        &nbsp;
      </td>
    </tr>   
    <tr>
      <td class="l">Note</td>
      <td class="l" colspan="3">          
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getNote()) %></font>&nbsp;
      </td>
    </tr>   
  </table>
  <% } %>



  
  <%
  //==========================================================================
  // DL 78/2013  
  //==========================================================================
  %>  
  <% if (StatoEsecuzioneCumuloUtils.isSospensionePM78 (Provvedimento.getCodMotivo())) { %>
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="Titolo" colspan="8">Sospensione D.L. 78/2013</td>
    </tr>    
    <tr>
      <td class="l">Provvedimento</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%>
        </font> 
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr> 

    <tr>
      <td class="Titolo" colspan="6"> Notifica Sorveglianza</td>
    </tr> 
    <%
    NotificaCumuloModel lNotifica78 = new NotificaCumuloModel();
    Vector <NotificaCumuloModel> lListaNotifica78 = Provvedimento.getListaNotifiche();
    UfficioModel lUfficio = null;
    if (lListaNotifica78!=null && lListaNotifica78.size()>0){
      lNotifica78 = lListaNotifica78.elementAt(0);
      lUfficio = lNotifica78.getUfficio();
    }
    %>
    <tr>
      <td class="l">Destinatario</td>
      <td class="l" colspan="1">
        <% if (lUfficio!=null) { %>  
        <font class="campo">
          <%=StringUtils.toStringJSP(lUfficio.getDescrTipoUfficio())+" "+StringUtils.toStringJSP(lUfficio.getDescrComune())%>
        </font>
        <% } %> &nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Data Invio</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotifica78.getDataInvio(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP (lNotifica78.getNote()) %></font>&nbsp;
      </td>
    </tr>
  </table>
  <% } %>
  
  
  <%
  //============================================================================
  //   Legge 199/2013
  //============================================================================
  %>  
  <% if (StatoEsecuzioneCumuloUtils.isSospensionePM199 (Provvedimento.getCodMotivo(), Provvedimento.getFlagTipoSosp())) { %>
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td class="Titolo" colspan="8"><%=ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199_DESC%></td>
    </tr>
    
    <tr>
      <td class="l">Provvedimento</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%>
        </font> 
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>
    <% if (StatoEsecuzioneCumuloUtils.isSospensionePM199_Rev(Provvedimento.getCodMotivo())) { %>
    <tr>
      <td class="l">Motivo Revoca</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP (Provvedimento.getDescrMotivoRevoca()) %>
        </font>         
      </td>
    </tr>
    <% } %>
    
    <% if (StatoEsecuzioneCumuloUtils.isSospensionePM199_Sorv(Provvedimento.getCodMotivo(),Provvedimento.getFlagTipoSosp())) { %>
    <tr>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getAnnoProcedimento()) %></font>
        /<font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getProgrProcedimento()) %></font>          
        &nbsp; <font class="label">Anno / Numero Provvedimento</font>
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getAnnoProvvedimento()) %></font>
        /<font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getProgrProvvedimento()) %></font>
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
      <td class="l" colspan="3">          
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getDescrUfficioEmittente()) %></font>
         di <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getDescrLuogoEmittente()) %></font>          
        &nbsp;
      </td>
    </tr>   
    <tr>
      <td class="l">Note</td>
      <td class="l" colspan="3">          
        <font class="campo"><%=StringUtils.toStringJSP (Provvedimento.getNote()) %></font>&nbsp;
      </td>
    </tr>      
    <% } %>    
    
  </table>
  <% } %>

  
    
</div>

<br><br>
</form>
</body>
</html>