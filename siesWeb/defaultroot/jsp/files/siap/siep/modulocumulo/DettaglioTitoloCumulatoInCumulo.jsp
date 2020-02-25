<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo" %>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiProcedimentoCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSoggettoCumulato" %>


<jsp:useBean id="IstruttoriaCumulo"  scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="titolocumulato"     scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>


<%
//==============================================================================
// Form di visualizzazione del dettaglio del singolo titolo cumulato.
// n.b. il titolo cumulato può essere:
//      - una Sentenza (01)
//      - un Decreto Penale (02bis n.b. il codice reale è 02 ma viene passato alla jsp come 02bis per distinguerlo dal decreto della Sorv)
//      - una Sentena Straniera (05)
//      - un Decreto dela Sorv (02) o una Ordinanza (03)
// In funzione del tipo di titolo, la visualizzazione del dettaglio può differire
//==============================================================================
%>
<html>
<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>

  <script language="JavaScript">
  
  </script>
</head>

<BODY class="corpo">

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formComandi">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=StringUtils.toStringJSP(titolocumulato.getIdTitoloCumulato()) %>">

  <% if (titolocumulato.getProcedimentoCumulato()!=null) { %>
  <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_ID_PROCEDIMENTO_CUMULATO%>" value="<%=StringUtils.toStringJSP(titolocumulato.getProcedimentoCumulato().getIdProcedimentoCumulato()) %>">
  <% } %>

  <% if (titolocumulato.getSoggettoCumulato()!=null) { %>
  <input type="hidden" name="<%=ICostantiSoggettoCumulato.CAMPO_ID_SOGGETTO_CUMULATO%>" value="<%=StringUtils.toStringJSP(titolocumulato.getSoggettoCumulato().getIdSoggettoCumulato()) %>">
  <% } %>

<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();">
        <img align="middle" src="/images/quickprint24.gif" alt="Stampa questa videata" border=0>
      </a>
    </td>
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp; 
      <font class="campo">Dettaglio Titolo Cumulato</font>
    </td>
    
    
    <td class="LBG">
      <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>

      <% if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(IstruttoriaCumulo.getFlagStato())) {%>
      <a href="javascript:submit('siap.siep.modulocumulo.action.ActLoadInserisciTitoloCumulato')">
        <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
      <% } %>
      
      <SCRIPT LANGUAGE="JavaScript">
        function submit( aAction )
        {
          document.formComandi.<%=IWebConstants.ACTION_FIELD%>.value=aAction;
          document.formComandi.submit();
        }
      </SCRIPT>
    
     <select name="vai">
       <% if (titolocumulato.getProcedimentoCumulato()==null) { %>
       <option value="siap.siep.modulocumulo.action.ActLoadInserisciProcedimentoCumulato">Inserimento Procedimento di Esecuzione</option>
       <% } else { %>
       <option value="siap.siep.modulocumulo.action.ActLoadDettaglioProcedimentoCumulato">Dettaglio Procedimento di Esecuzione</option>
       <% } %>
       
       <% if (titolocumulato.getSoggettoCumulato()==null) { %>
       <option value="siap.siep.modulocumulo.action.ActLoadInserisciSoggettoCumulato">Inserimento Soggetto</option>
       <% } else { %>
       <option value="siap.siep.modulocumulo.action.ActDettaglioSoggettoCumulato">Dettaglio Soggetto</option>
       <% } %>
     </select>
       
     <a href="javascript:submit(document.forms[0].vai.value);">
       <img align="middle" src="/images/vedi24.gif" alt="Vai" width="24" height="24" border="0">
     </a>
    </td>    
  </tr>
</table>
</FORM>

  <br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>

<table cellspacing=2 cellpadding=2>
  <tr>
    <td class="l">Data Irrevocabilità</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(titolocumulato.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Tipo Provvedimento</td>
    <%
      String lDescrtipoTitolo = titolocumulato.getDescrTipoProvvedimento();
      
      if ("02bis".equals(titolocumulato.getCodTipoProvvedimento())) {
          lDescrtipoTitolo = "Decreto Penale";
      }
    %>
    <td class="L" colspan=5>
      <font class="campo"><%=StringUtils.toStringJSP(lDescrtipoTitolo)%></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td></td><td></td>
  </tr>

  <% if (   "01".equals(titolocumulato.getCodTipoProvvedimento())
         || "02bis".equals(titolocumulato.getCodTipoProvvedimento())
        ) {
  %>
  <tr>
    <td class="l">Numero R.G.N.R.</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getAnnoRegePm()) %></font> / <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumeroRegePm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Sede PM</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrSedeNotiziaReato()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Reg.Gen.</td>
    <td class="L" colspan=5>
      <font class="campo"> <%=StringUtils.toStringJSP(titolocumulato.getAnnoRegGen())%></font> / <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumeroRegGen()) %></font>
      &nbsp;&nbsp;&nbsp; <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getTipoRegGen()) %></font>
    </td>
  </tr>
  <% } %>
  
  
  <%
  String lTitoloSezione = "";
  String lEtichettaTitolo = "";
  if ("01".equals(titolocumulato.getCodTipoProvvedimento())) {
    lTitoloSezione   = "Sentenza da Eseguire";
    lEtichettaTitolo = "Sentenza";
  } else if ("02bis".equals(titolocumulato.getCodTipoProvvedimento())) {
    lTitoloSezione = "Decreto Penale da Eseguire";
    lEtichettaTitolo = "Decreto";
  } else if ("05".equals(titolocumulato.getCodTipoProvvedimento())) {
    lTitoloSezione = "Sentenza Di Appello da Eseguire";
    lEtichettaTitolo = "Sentenza";
  } else if (   "02".equals(titolocumulato.getCodTipoProvvedimento())
             || "03".equals(titolocumulato.getCodTipoProvvedimento())
            ) 
  {
    lTitoloSezione = "Provvedimento della Sorveglianza";
    if ("02".equals(titolocumulato.getCodTipoProvvedimento()))
      lEtichettaTitolo = "Decreto";
    else 
      lEtichettaTitolo = "Ordinanza";
  }    
  %>
  <tr>
    <td class="Titolo" colspan=6><%=lTitoloSezione%></td>
  </tr> 
  <tr>
    <td class="l">Data <%=lEtichettaTitolo%></td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(titolocumulato.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero <%=lEtichettaTitolo%></td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getAnnoSentenza()) %></font>&nbsp; / <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumeroSentenza()) %></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">Autorità Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoAutoritaEmittente()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Luogo Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrLuogoEmittente()) %></font>&nbsp;</td>
  </tr>
  
  <% if ("01".equals(titolocumulato.getCodTipoProvvedimento())) { %>
  <tr>
    <td class="l">Tipo Rito</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoRito()) %></font>&nbsp;</td>
  </tr>
  <% } %>  

  <% if (   "01".equals(titolocumulato.getCodTipoProvvedimento())
         || "02bis".equals(titolocumulato.getCodTipoProvvedimento())
         || "05".equals(titolocumulato.getCodTipoProvvedimento())
        ) 
  { %>
  <tr>
    <td class="l">Sezione Autorità Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumSezioneAutoritaEmittente()) %></font>&nbsp;</td>
  </tr>
  <% } %>  
  
  <%
  //============================================================================
  // Altro Grado di Giudizio (solo 10 = Sentenza)
  //============================================================================
  %>
  <% if ( "01".equals(titolocumulato.getCodTipoProvvedimento())) { %>
  <tr>
    <td class="Titolo" colspan=6>Altro Grado di Giudizio</td>
  </tr>
  <tr>
    <td class="l">Tipo Provvedimento</td>
    <td class="L" colspan=5>
      <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoProvvedimentoRif()) %></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">Tipo Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoProvvRif()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(titolocumulato.getDataProvvRif(),"dd-MM-yyyy"))%></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getAnnoProvvRif()) %></font> / <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumeroProvvRif()) %></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">Autorità Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoAutoritaProvvRif()) %></font>&nbsp;</td>
  </tr>  
  <tr>
    <td class="l">Luogo Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrLuogoProvvRif()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Tipo Rito</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoRitoRif()) %></font>&nbsp;</td>
  </tr>  
  <tr>
    <td class="l">Sezione Autorità Emittente</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumSezioneAutoritaProvvRif()) %></font>&nbsp;</td>
  </tr>
  <% } %>
  
  <%
  //============================================================================
  // Sentenza della Cassazione (solo sentenza 10 e Decreto Penale 02bis
  //============================================================================
  %>
  <% if (   "01".equals(titolocumulato.getCodTipoProvvedimento())
         || "02bis".equals(titolocumulato.getCodTipoProvvedimento())
        ) 
  { %>
  <tr>
    <td class="Titolo" colspan=6>Sentenza della Cassazione</td>
  </tr>
  <% if ( "01".equals(titolocumulato.getCodTipoProvvedimento())) { %>
  <tr>
    <td class="l">Tipo Provvedimento</td>
    <td class="L" colspan=5>
      <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoProvvedimentoAltro()) %></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Reg.Gen. Cassazione</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNote1DecisioneCassazione()) %></font> / <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNote2DecisioneCassazione()) %></font>&nbsp;</td>
  </tr>
  <% } %>
  <tr>
    <td class="l">Anno/Numero Sentenza</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getAnnoSentenzaCassazione()) %></font> / <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumeroSentenzaCassazione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno/Numero Raccolta Generale</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getAnnoRaccoltaGenerale()) %></font> / <font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNumeroRaccoltaGenerale()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Dispositivo Cassazione</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getDescrTipoDecisioneCassazione()) %></font>&nbsp;</td>
  </tr> 
  <tr>
    <td class="l">Note</td>
    <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(titolocumulato.getNote()) %></font>&nbsp;</td>
  </tr>
  <% } %>

  <br>

  
</table>
</body>
</html>

  