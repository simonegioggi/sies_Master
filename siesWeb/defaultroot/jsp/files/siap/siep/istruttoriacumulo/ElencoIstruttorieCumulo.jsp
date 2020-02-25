<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>


<%@ page import="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<jsp:useBean id="ListaIstruttorieCumulo" scope="request" class="java.util.Vector"/>

<%
//==============================================================================
//          FORM di visualizzazione dell'elenco delle istruttorie
//==============================================================================
%>

<%
BigDecimal lIdIstruttoriaCorrente = (BigDecimal) request.getAttribute("lIdIstruttoriaCorrente");
FascicoloSiepModel lFascicoloInSessione =  (FascicoloSiepModel) session.getAttribute("fascicolo");
UtenteModel lUtenteConnesso = (UtenteModel) session.getAttribute("UtenteConnesso");
boolean isDiCompetenza = false;
if(   lFascicoloInSessione!=null 
   && lFascicoloInSessione.getIdFascicoloSiep()!=null
   && lUtenteConnesso.getUfficioUtente().isUfficioDiCompetenza (lFascicoloInSessione.getChiaveUfficio() )
  )  
{
  isDiCompetenza = true;
}
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function dettaglioIstrutoria(idRecord)
    {
      var riga = document.getElementById(idRecord); 
      if (riga.style.display =="none" ){
        riga.style.display = "block";
      }
      else {
        riga.style.display = "none";
      }
    }
    
    //==========================================================================
    // Ritorna alla Griglia Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.RicercaIstruttoriaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.RicercaIstruttoriaCumulo.submit();
    }
    
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Elenco Istruttorie</font>
        </td>
        <td class="LBG"><!-- Tasto indietro alla Lista dei fascicoli coinvolti -->
          <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaIstruttoriaCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">

  <% if (lIdIstruttoriaCorrente.compareTo(new BigDecimal(0))!=0) { %>
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=lIdIstruttoriaCorrente%>">
  <% } %>
  
  <table cellspacing="2" cellpadding="2" align="center">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">N°</td>
      <td class="int">Data Apertura</td>
      <td class="int">Data Chiusura</td>
      <td class="int">Provvedimento Cumulo</td>
      <td class="int">Stato</td>
      <td class="int">Note</td>
      <td class="int">Dettaglio</td>
    </tr>
    <%
int conta_rec_visualizzati = 0;
      int id_record = 0;
      Iterator itx = ListaIstruttorieCumulo.iterator();
      while ( itx.hasNext()) {
        IstruttoriaCumuloModel lIstruttoriaCumulo = (IstruttoriaCumuloModel)itx.next();
        id_record = id_record+1;

if (!isDiCompetenza) continue;
conta_rec_visualizzati++;
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class="c" nowrap>
          <%=lIstruttoriaCumulo.getAnnoProtocollo()%>/<%=lIstruttoriaCumulo.getNumProtocollo()%>
      </td>
      <td class="c" nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstruttoriaCumulo.getDataApertura(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class="c" nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstruttoriaCumulo.getDataChiusura(),"dd-MM-yyyy"),"&nbsp;")%></td>
      <td class="c">&nbsp;
      <% if (lIstruttoriaCumulo.getFlagStato().equals("C")) { %>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo&IdEvento=<%=lIstruttoriaCumulo.getProvvedimentoCumulo().getIdEvento()%>">
          <%=StringUtils.toStringJSP(lIstruttoriaCumulo.getProvvedimentoCumulo().getDescrMotivo()) %>
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
        </a>
          <% if ("A".equals(lIstruttoriaCumulo.getProvvedimentoCumulo().getFlagDocumentoRegistrato())) { %>
          <br><font class="cRosso">PROVVEDIMENTO ANNULLATO</font>
          <% } %>      
      <% } %>      
      </td>
      <% if (lIstruttoriaCumulo.getFlagStato().equals("A")) { %>
            <td class=c>&nbsp;<font color="red">Aperta</font></td>
      <% } else if (lIstruttoriaCumulo.getFlagStato().equals("C")) {%>
            <td class=c>&nbsp;Chiusa</td>
      <% } else if (lIstruttoriaCumulo.getFlagStato().equals("N")) {%>
            <td class=c>&nbsp;Annullata</td>
      <% } %>
      <td class=c nowrap>&nbsp;
        <a href="javascript:dettaglioIstrutoria('rec_<%=id_record%>')">note</a>&nbsp;&nbsp;
      </td>
      <td class=c style="text-align:left" nowrap>
<% if (isDiCompetenza) { %>
        <a href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=lIstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
          <img src="/images/vedi24.gif" title="Seleziona Istruttoria" border="0"></a>
      <% if (lIstruttoriaCumulo.getFlagStato().equals("A")) 
         { %>
            <a href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadCancellaIstruttoriaCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=lIstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
                <img src="/images/delete24.gif" title="Annulla Istruttoria" border="0"></a>
      <% } %>
<% } else { %>
<font color="red">Non Consultabile</font>
      <% } %>
      </td>
    </tr>
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lIstruttoriaCumulo.getNote(),"&nbsp;")%>
        </font>
      </td>
    </tr>
    <% } // end while su iterator %>

    <% if (ListaIstruttorieCumulo.size()==0 || conta_rec_visualizzati==0){ %>
    <tr>
      <td>Nessun dato presente</td>
    </tr>
    <% } %>
  </table>
</FORM>
</body>
</html>