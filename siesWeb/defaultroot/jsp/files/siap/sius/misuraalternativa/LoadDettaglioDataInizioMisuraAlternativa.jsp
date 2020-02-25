<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.action.ICostantiEsecuzioneMA"%>
<%@ page import="siap.sius.scadenzario.action.ICostantiScadenzarioSius"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="verbale"                      scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="cssa"                         scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="istitutodetenzione"           scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>


<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio concessione misure alternative alla detenzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :  </font>&nbsp;
<%
        FascicoloGPModel lFascicolo = new FascicoloGPModel();
        Date lDataIscrizione = new Date();
%>
        <font class="campo">Dettaglio concessione misure alternative alla detenzione</font>
      </td>
      <!-- BOTTONE DI CANCELLAZIONE -->
      <td class="LBG">
        <a href="Javascript:conferma('siap.sius.misuraalternativa.action.ActCancellaDataInizioMisuraAlternativa','idVerbale','<%=verbale.getIdVerbale()%>','','');">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
        </a>
      </td>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>

  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadDettaglioDataInizioMisuraAlternativa">

  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="l" colspan=2>Concessione Affidamento in Prova</td>
    </tr>

 <tr>
   <td class="l">Data pervenimento del verbale</td>
   <td class="l"><font class="campo">
   <%= StringUtils.toStringJSP( DateUtils.getDateToString(verbale.getDataPervenimento(),"dd/MM/yyyy"))%>
   </font></td>
 </tr>

 <tr>
   <td class="l">Data sottoscrizione obblighi</td>
   <td class="l"><font class="campo">
   <%= StringUtils.toStringJSP( DateUtils.getDateToString(verbale.getDataEmissione() ,"dd/MM/yyyy"))%>
   </font></td>
 </tr>

 <tr> <td>&nbsp;</td></tr>

    <tr>
      <td class="l" colspan=2>Autorità competente che ha inviato il verbale:</td>
    </tr>

<% if(cssa.getIdCSSA() != null) {%>
    <tr>
      <td class="l">Ufficio di Esecuzione Penale Esterna</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(cssa.getComune())+ " " +StringUtils.toStringJSP(cssa.getIndirizzo())%></font></td>

    </tr>
<% }%>
<% if( istitutodetenzione!=null){
    if( istitutodetenzione.getIdIstitutoDetenzione().trim().length() > 0 ) {%>
     <tr>
      <td class="l">Istituto Detenzione</td>
      <td class="l">
      <font class="campo">
      <%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutodetenzione.getDescrComune() )%>
      </font></td>
     </tr>
<%  }
   }%>

<% if(!verbale.getCodTipoUfficioFirmatario().equals("-")) {%>
    <tr>
        <td class="l">Autorità</td>
        <td class="l"><font class="campo">
        <%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario()) %> di
        <%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario()) %>
        </font></td>
    </tr>
<% }%>

    <tr><td colspan=2>&nbsp;</td></tr>

      <tr>
        <td class="l">Note</td>
        <td class="L" >
          <font class="campo">
          <%=StringUtils.toStringJSP(verbale.getNote() ) %>
          </font>
       </td>
      </tr>

    </table>

     </form>
  </body>
</html>