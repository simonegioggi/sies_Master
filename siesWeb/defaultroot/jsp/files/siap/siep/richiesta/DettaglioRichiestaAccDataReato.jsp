<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="eventonotifica"   scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistrato"   scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="annotazioneManuale" scope="request"  class="java.util.Vector"/>
<jsp:useBean id="reati" scope="request"  class="java.util.Vector"/>
<jsp:useBean id="ufficio"   scope="request" class="siap.sico.ufficio.model.UfficioModel" />

<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

</head>
<body class="corpo">
<table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Richiesta Accertamento della data di Commesso reato</font>
</td>

     <%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
    
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichAccDataReato&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%}%>

<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 {%>
    
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichAccDataReato&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%}%>

  <td class="LBG">
    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste&<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>=<%=StringUtils.toStringJSP(eventonotifica.getEvento().getAnnIdAnnotazioneManuale())%>" >
      <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
    </a>
  </td>
</tr>
</table>
<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table width="100%">
  <tr>
    <td class="l">Posizione Giuridica</td>
    <td class="L" colspan=5>
      <font class="campo"><%=posizioneGiuridica.getDescrPosizioneGiuridica()%></font>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
<%
 AnnotazioneManualeModel lAnnPrima = (AnnotazioneManualeModel)annotazioneManuale.get(0);
%>
 </table>

  <table width="100%">

     <tr>
      <td colspan=8 class="titolo">Richiesta</td>
    </tr>

<tr>
              <td class="l" width="10%">DPR </td>
              <td class="L">
            <font class="campo">
                <%=StringUtils.toStringJSP(lAnnPrima.getDescrDpr())%></font>
              </td>


       </tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = reati.iterator();
      while(lItxAvv.hasNext())
      {
        ReatoModel lreato =  (ReatoModel)lItxAvv.next();
%>
        </table>
  <table width="100%">
         <td class="l" width="10%">Reato :</td>
              <td class="L">
            <font class="campo">
                <%=StringUtils.toStringJSP(lreato.getCodFonte())%></font>

              <font class="campo">
                <%=StringUtils.toStringJSP(lreato.getAnnoFonte())%>/</font>
             <font class="campo">
                <%=StringUtils.toStringJSP(lreato.getNumeroFonte())%></font>
            Art.
            <font class="campo">
                <%=StringUtils.toStringJSP(lreato.getArticolo())%></font>
            &nbsp;
            C.

            <font class="campo">
                <%=StringUtils.toStringJSP(lreato.getComma())%></font>
             L.

               <font class="campo">
                <%=StringUtils.toStringJSP(lreato.getLettera())%></font>
            N.

               <font class="campo">
                <%=StringUtils.toStringJSP(lreato.getNumero())%></font>
              </td>


           </tr>
        </table>

<%
    lIdxAvv++;
  }
%>
<tr><td>&nbsp;</td></tr>
</table>
  <table width="60%">
  <tr>
   <td class="l">Magistrato
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<tr>
        <td class="l">Data Emissione</td>
<%if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getDataEmissione()!= null){%>

 <td class="L" >
       <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
             </td>

<%}%>
</tr>
<tr>
        <td class="l">Data Trasmissione</td>

<%if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getDataTrasmissioneAtti()!= null){%>
 <td class="L">
      <font class="campo">    <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%>&nbsp;</font>
                 </td>
<%}%>
      </tr>

   <tr>
         <td class="l">Giudice dell'esecuzione</td >
           <td class="l"><font class="campo"><%=ufficio.getDescrTipoUfficio()%></font></td>
   </tr>
 <tr>
          <td class="l">Sede</td>
           <td class="l">
              <font class="campo">
<%=ufficio.getDescrComune()%>
</font>

            </td>
   </tr>
<%if(eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0].getNote() != null){%>
 <tr>
          <td class="l">Note</td>
           <td class="l">
              <font class="campo">
<%=eventonotifica.getNotifiche()[0].getNote()%>
</font>

            </td>
   </tr>
<%}%>
</table>

 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadRichAccDataReato">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.richiesta.action.ActDettaglioRichAccDataReato">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>