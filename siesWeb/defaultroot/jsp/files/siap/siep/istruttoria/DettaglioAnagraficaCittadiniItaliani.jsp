<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<jsp:useBean id="fascicolo"     scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<% EventoNotificaModel lEve = eventonotifica;%>

<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Richiesta Anagrafica Cittadini Italiani- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>


  </head>

<%if(lEve.getEvento().getFlagDocumentoRegistrato()!= null)
  {if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0){%>
   <BODY class="corpo" onload="javascript:lookUpload();">
<%}else{%>
   <BODY class="corpo">
<%}}else{%>
   <BODY class="corpo">
<%}%>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Richiesta Accertamenti Anagrafici Cittadini Italiani</font>
        </td>
 <%if (lEve.getEvento().getFlagDocumentoRegistrato()!=null)
 if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
   <!-- BOTTONE DI STAMPA -->
       <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.istruttoria.action.ActStampaAnagraficaCittadiniItaliani&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
       </jsp:include>

        <%}%>

<%if (lEve.getEvento().getFlagDocumentoRegistrato()==null)
 {%>
   <!-- BOTTONE DI STAMPA -->
       <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.istruttoria.action.ActStampaAnagraficaCittadiniItaliani&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
       </jsp:include>
       <%}%>
      </tr>
    </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <table cellspacing=4 cellpadding=4>



   <tr>
      <td class="l" colspan=2>SINDACO DI <%=fascicolo.getSoggetto().getDescrComuneNascita()%></td>
    </tr>




   <tr>
        <td class="l">Data richiesta :

          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
        </td>
      </tr>


  </table>


 <div align=left style="visibility:hidden" id="upld">
         <FORM name="comandi" enctype="multipart/form-data" method="post">
             <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
             <tr><td class="L">
                 <input  class=bottone  type="submit" value="Conferma">
                 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= lEve.getEvento().getIdEvento() %>">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.istruttoria.action.ActDettaglioAnagraficaCittadiniItaliani">
              </td> </tr>
            </table>

          </FORM>
      </div>
    <br>
</body>

</html>