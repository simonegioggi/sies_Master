<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.RedirectTo" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>
<%@ page import="siap.sius.statistiche.model.EveFasGepSogProvModel" %>
<%@ page import="siap.sius.statistiche.model.RicercaProcedimentoModel" %>

<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>


<jsp:useBean id="ricercaAttiIstruttori" scope="session" class="siap.sius.statistiche.model.RicercaProcedimentoModel" />
<jsp:useBean id="elencoProcedimenti"    scope="request" class="java.util.ArrayList" />
<jsp:useBean id="TornaQui"              scope="request" class="java.lang.String"/>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti con atti istruttori </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

  <BODY class="corpo">
    <table>
      <tr>
         <td class="LBG">
           <a href="Javascript:window.print();">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
         </td>
         <td class="LBG">
           <font class="label">Funzione :</font>&nbsp;
           <font class="campo">Elenco Procedimenti con atti istruttori con data restituzione</font>
         </td>
         <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
         <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
          <td class="LBG">
            <a class="cliccabile" 
               href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActRicercaAttiIstruttoriDataRestExcel">
              <img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
            </a>
          </td>
      </table>
  <br>
  
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table cellspacing=2 cellpadding=2>
<%  if( ricercaAttiIstruttori != null ) { %>

        <tr>
          <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
        </tr>
        
<%
        if( (ricercaAttiIstruttori.getAnnoInizio() != null || ricercaAttiIstruttori.getNumeroInizio() != null) || 
            (ricercaAttiIstruttori.getAnnoFine() != null || ricercaAttiIstruttori.getNumeroFine() != null) ) {
%>
          <tr>
            <td class="lVerdeNB">Procedimenti con Anno Numero :&nbsp;&nbsp;</td>
            <td class="lVerdeNB">
              Dal&nbsp;<%=StringUtils.toStringJSP(ricercaAttiIstruttori.getAnnoInizio(),"____")%> 
            /
              <%=StringUtils.toStringJSP(ricercaAttiIstruttori.getNumeroInizio(),"_")%>
            </td>                
            <td class="lVerdeNB">            
              Al&nbsp;<%=StringUtils.toStringJSP(ricercaAttiIstruttori.getAnnoFine(),"____")%> 
            /
              <%=StringUtils.toStringJSP(ricercaAttiIstruttori.getNumeroFine(),"_")%>
            </td>
          </tr>
<%      } %>        
        
        
<%
        if( ricercaAttiIstruttori.getDataDepositoInizio() != null || ricercaAttiIstruttori.getDataDepositoFine() != null ) {
%>
          <tr>
            <td class="lVerdeNB">Procedimenti con Data Iscrizione :&nbsp;&nbsp;</td>
            <td class="lVerdeNB">
              Dal&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(ricercaAttiIstruttori.getDataDepositoInizio(), "dd-MM-yyyy") ,"__/__/____")%>&nbsp;&nbsp;
            </td>
            <td class="lVerdeNB">
              Al&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString( ricercaAttiIstruttori.getDataDepositoFine(),"dd-MM-yyyy"),"__/__/____") %>
            </td>
        </tr>
<%      } %>


<%
        if(ricercaAttiIstruttori.getDataRestituzioneInizio() != null || ricercaAttiIstruttori.getDataRestituzioneFine() != null) 
        {
%>
          <tr>
            <td class="lVerdeNB">Atti con Data Restituzione :&nbsp;&nbsp;</td>
            <td class="lVerdeNB">
              Dal&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(ricercaAttiIstruttori.getDataRestituzioneInizio(),"dd-MM-yyyy"),"__/__/____")%>&nbsp;&nbsp;
            </td>
            <td class="lVerdeNB">
              Al&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(ricercaAttiIstruttori.getDataRestituzioneFine(),"dd-MM-yyyy"),"__/__/____")%>
            </td>
          </tr>
<%     } %>

<% } %> 
  </table>
  <br>
<%
//  Iterator itx = elencoProcedimenti.iterator();
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Data Emissione</td>
      <td class="int">Data Restituzione</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Contenuto</td>
      <td class="int">Atto Istruttorio</td>
      <td class="int">Stato Procedimento</td>
      <td class="int">Azioni</td>
    </tr>

<%
   RedirectTo lRedirect = null;
   Iterator itx = elencoProcedimenti.iterator();
   while ( itx.hasNext() ) {
     EveFasGepSogProvModel procedimento = (EveFasGepSogProvModel)itx.next();
%>
      <tr>
        <td class="c">
<% 
          lRedirect = new RedirectTo();
          lRedirect.setPage(IWebConstants.PG_MAIN);
          lRedirect.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
          lRedirect.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,""+procedimento.getFascicoloSius().getIdFascicoloSius());             
          if((TornaQui != null) && TornaQui.trim().length() > 1) 
            lRedirect.setParameter("TornaQui", TornaQui);
%>            
      
          <font class="label">
            <a class="cliccabile"
              href="<%=lRedirect.toString()%>" 
              Title="<%=procedimento.getFascicoloSius().getDescrTipoUfficio()%>&nbsp;<%=procedimento.getFascicoloSius().getDescrComuneUfficio()%> - Dettaglio Procedimento" >
              <%=procedimento.getFascicoloSius().getChiaveAnno()%>/<%=procedimento.getFascicoloSius().getChiaveProgr()%>
            </a>
          </font>
        </td>
        <td class="c">
          <font class="label">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(procedimento.getEvento().getDataEmissione(),"dd-MM-yyyy"),"-")%>
          </font>
        </td>
        <td class="c">
          <font class="label">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(procedimento.getEvento().getDataRestituzioneAi() ,"dd-MM-yyyy"),"-")%>
          </font>
        </td>
        <td class="c">
          <font class="label"><%=procedimento.getFascicoloSius().getSoggetto().getCognome()%></font>
        </td>
        <td class="c">
          <font class="label"><%=procedimento.getFascicoloSius().getSoggetto().getNome()%></font>
        </td>
<%
        if( ricercaAttiIstruttori.getStatoProcedimento() == 999 ) {
%>
          <td class="c">-</td>
          <td class="c">-</td>
          <td class="c">-</td>
          <td class="c">-</td>
<%
        } else {
%>          
        <td class="c">
          <font class="label"><%=StringUtils.toStringJSP(procedimento.getGeneraleProcedimento().getDescrOggettoProcedimento(),"")%></font>
        </td>
        <td class="c">
            <font class="label"><%=StringUtils.toStringJSP(procedimento.getEvento().getDescrMotivo(),"")%></font>
        </td>        
        <td class="c">
          <font class="label"><%=StringUtils.toStringJSP(procedimento.getFascicoloSius().getDescrStatoFascicolo(),"")%></font>
        </td>
        
        <td class="c">
        <% 
          if ( procedimento.getEvento() != null && procedimento.getEvento().getIdEvento() != null ) {
            lRedirect = new RedirectTo();
            lRedirect.setPage(IWebConstants.PG_MAIN);
            lRedirect.setAction("siap.sius.richiestaatti.action.ActLoadDettaglioRichiestaAtti");
            lRedirect.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, ""+procedimento.getEvento().getIdEvento());
            
            if ((TornaQui != null) && TornaQui.trim().length() > 1) 
              lRedirect.setParameter("TornaQui", TornaQui);
        %>
          <a href="<%=lRedirect.toString()%>">
            <img src="/images/dettagli.gif" alt="Dettaglio Provvedimento" width="12" height="12" border="0">
          </a>

        <% }else { %>
            -
        <% }%>
        </td>

<%
        }
%>
      </tr>
<%
  } // end while
%>
    </table>

  </body>
</html>