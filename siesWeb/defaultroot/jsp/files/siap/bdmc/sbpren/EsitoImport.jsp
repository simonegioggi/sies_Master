<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"%>
<%@ page import="siap.bdmc.sbpren.model.ProvvedimentoSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@page import="siap.sico.ufficio.controller.UfficioUtils"%>
<%@page import="siap.sico.decodifiche.controller.IComune"%>
<%@page import="siap.sico.decodifiche.model.ComuneModel"%>
<%@page import="siap.sico.util.SICOLookupRemote"%>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="esito" scope="request" class="siap.bdmc.sbpren.model.EsitoImportModel"/>

<%
BigDecimal KeyFascicolo = (BigDecimal) request.getAttribute("KeyFascicolo");
%>

<html>
  <head>
    <title> [S.I.E.S.] -Esito Importazione Dati da Bdmc in SIEP- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    </script>
  </head>

<BODY class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Esito Importazione Dati da Bdmc in SIEP</font>
        </td>
       
      </tr>
    </table>
  </FORM>

<br>
<%
String ESITO_POSITIVO = siap.regesies.regesentenza.action.ICostantiRegeSentenza.ESITO_POSITIVO;
  ProvvedimentoModelBDMC lProvvedimento = esito.getProvvedimento();
  ProvvedimentoSiepModel lProvv = esito.getProvvedimentoSiep();

  SentenzaModel sentenza =  new SentenzaModel();
  if ( esito.getProvvedimentoSiep()!= null ) {
sentenza = esito.getProvvedimentoSiep().getSentenza();}
else {
  sentenza = ((SbViewProcpenaModel)esito.getProvvedimento().getSbViewProcpena().get(0)).toSentenza();
  if (esito.getProvvedimento().getSentenza() != null)
  sentenza.setIdSentenza(esito.getProvvedimento().getSentenza().getIdSentenza());
}
  String finale;%>

   <table width="100%">
  	<tr>
      <td class="LBGISI" width="8%">Sentenza</td>

      <%if(sentenza.getCodTipoProvvedimento().equals("01"))
      {
        finale="a";
      }
      else
      {
        finale="o";
      }
      if(esito.getEsitoSentenza().equals(ICostantiSbPren.ESITO_POSITIVO) ||
           esito.getEsitoSentenza().equals(ICostantiSbPren.ESITO_DATO_PRESENTE))
           {
             %><td class="L"><%
             if(esito.getEsitoSentenza().equals(ICostantiSbPren.ESITO_POSITIVO))
               {%><font class="label">Inserit<%=finale%> Correttamente con </font>&nbsp;
             <%}
              else
               {%><font class="label">già presente nel sistema con </font>&nbsp;
             <%}%>
           <font class="label"> N.&nbsp;</font>
           <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> </a>&nbsp;
         emess<%=finale%> da
         
         <% if (sentenza.getDescrLuogoEmittente() != null )  {%>
         <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente() %> di <%=sentenza.getDescrLuogoEmittente() %></font>&nbsp;
      <%  } else { %>
      	 <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente() %></font>&nbsp;
     
       <% 
     	 }}
       else
       {//l'inseirmento della sentenza è andato in errore
       %> <td class="lRosso">ERRORE <%=esito.getEsitoSentenza()%>
      <% } %>
      </td>
    </tr>
    <tr>
      <td class="LBGISI">Soggetto</td>
      <%if(!esito.getEsitoSoggetto().equals(ICostantiSbPren.ESITO_ERRORE))
      {
        SoggettoModel lSoggetto = new SoggettoModel();
        if(lProvv!= null)
        {
          if(lProvv.getSoggetto()!=null)
             lSoggetto = lProvv.getSoggetto();
          else
            if(esito.getProvvedimento().getIdSoggettoOmonimo()!=null)
              {
            	lSoggetto = esito.getProvvedimento().getSoggetto();
                 lSoggetto.setIdSoggetto(esito.getProvvedimento().getIdSoggettoOmonimo());
              }
        }
        else
        {//Non è stato inserito il provvedimento
           if(esito.getProvvedimento().getIdSoggettoOmonimo()!=null)
           {
             lSoggetto = esito.getProvvedimento().getSoggetto();
             lSoggetto.setIdSoggetto(esito.getProvvedimento().getIdSoggettoOmonimo());
           }
         }
     %>
     <td class="L">
      <%if(esito.getEsitoSoggetto().equals(ESITO_POSITIVO))
      {%>
        <font class="label">Inserito Correttamente</font>&nbsp;
        <%}
       else if(esito.getEsitoSoggetto().equals(ICostantiSbPren.ESITO_DATO_PRESENTE))
       {%>
        <font class="label">Già presente </font>&nbsp;
     <%}%>
        <font class="campo">
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <a class="cliccabile" title="Dettaglio Soggetto" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=siap.sico.soggetto.action.ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lSoggetto.getIdSoggetto()%>">
          <%=lSoggetto.getCognome()%>&nbsp;<%=lSoggetto.getNome()%>
        </a>
      </font>
<%    if (lSoggetto.getSesso().compareTo("F")==0)
       {%>  <font class="label">&nbsp; nata il </font><%}else{
%>          <font class="label">&nbsp; nato il </font><%}%>
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSoggetto.getDataNascita(),"dd-MM-yyyy"),"-")%></font>
       <font class="label">&nbsp; in  </font>
<%   if (lSoggetto.getDescrComuneNascita().compareTo("-")==0)
       {%> <font class="campo">
           <%=lProvv.getSoggetto().getDescrStatoNascita()%> </font>
<%     }else{%><font class="campo">
           <%=lSoggetto.getDescrComuneNascita() + "  ("+lSoggetto.getCodProvinciaNascita()+")" %></font>
<%     }%>
<%     }else
      {//Nel caso in cui l'inserimento del soggetto è andato in errore%>
         <td class="lRosso">ERRORE <%=esito.getEsitoSoggetto()%>
      <%} %>
         </td>
     	</tr>
  	<tr>
      <td class="LBGISI">Fascicolo SIEP</td>
    <%
    FascicoloSiepModel fascicolo = new FascicoloSiepModel();
    if(lProvv!= null)
      fascicolo = lProvv.getFascicoloSiep();
    if(esito.getEsitoFascicolo() != null && esito.getEsitoFascicolo().equals(ESITO_POSITIVO))
    {%>
    <td class="l">
       <font class="label">Inserito correttamente con N.</font>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
            <%=fascicolo.getChiaveAnno()%>
            /
            <%=fascicolo.getChiaveProgr()%>
          </a>
<%     }else
      {//Nel caso in cui l'inserimento del soggetto è andato in errore%>
         <td class="lRosso">ERRORE <%=StringUtils.toStringJSP(esito.getEsitoFascicolo())%>
      <%} %>
     </td>
   </tr>
   </table>
  


  </body>

</html>