<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>

<jsp:useBean id="annotazioneManuale"   scope="request" class="java.util.Vector"/>
<jsp:useBean id="posizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistrato"           scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="eventonotifica"       scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="cssa"                 scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="ufficiouds"           scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiotds"           scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="istituto"             scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="autoritaEsterna"      scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="noteautoritaEsterna"  scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
  BigDecimal lIdAnnotazione = null;
  if (annotazioneManuale!=null && annotazioneManuale.size()>0)
  {
    AnnotazioneManualeModel lAnnPrima = (AnnotazioneManualeModel)annotazioneManuale.get(0);
    lIdAnnotazione = lAnnPrima.getIdAnnotazioneManuale();
  }
%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Ordine Scarcerazione Provvisorio per Indulto</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<!-- DettaglioOrdineScarcerazioneProvv -->
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione : </font>&nbsp;&nbsp;
        <%if("0367".equals(eventonotifica.getEvento().getCodMotivo())){%>
        <font class="campo">Dettaglio Ordine Provvisorio di Scarcerazione per Indulto</font>
        <%}else if("0369".equals(eventonotifica.getEvento().getCodMotivo())){%>
        <font class="campo">Dettaglio Ordine Provvisorio di Scarcerazione per Ex artt. 673 c.p. e 672 comma 3° c.p.p.</font>
        <%}%>
      </td>
<%
      //========================================================================
      // Se evento non ancora validato visualizzo tasto di stampa e indietro di 2 pagine
      // Se evento Validato solo tasto indietro di 4
      //========================================================================
      if(   eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
         || (   eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
             && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
            )
        )
      {
      %>
      <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaOrdineScarcerazioneProvv&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
      <td class="LBG">
        <a href="Javascript:history.go(-2);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      <%
      } else {
      %>
      <td class="LBG">
        <!--a href="Javascript:history.go(-4);"-->
        <%
        // Ricarico la pagine delle stampe per aggiornare la pena residua, se modificata per azzeramento.
        %>
        <a href="/jsp/Main.jsp?Action=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste&IdAnnotazioneManuale=<%=lIdAnnotazione%>">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      <%
      }
      %>
  </tr>
</table>

  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<table>
  <tr>
    <td class="l">Posizione Giuridica</td>
    <td class="L">
      <font class="campo"><%=posizioneGiuridica.getDescrPosizioneGiuridica()%></font>
    </td>
  </tr>

  <tr>
    <td colspan=8 class="titolo">Richiesta</td>
  </tr>
<%
  for(int i = 0;i<annotazioneManuale.size();i++)
  {
    AnnotazioneManualeModel lAnnPrima = (AnnotazioneManualeModel)annotazioneManuale.get(i);
%>
  <tr>
    <td class="l" width="20%">Data Richiesta</td>
    <td class="L">
    <font class="campo">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnPrima.getDataRichiesta(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>

  <tr>
    <td class="l" >DPR </td>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(lAnnPrima.getDescrDpr())%>
      </font>
      &nbsp;Per  &nbsp;
      <font class="campo">
        <%=StringUtils.toStringJSP(lAnnPrima.getDescrTipoAnnotazione())%>
      </font>
    </td>
  </tr>
<%
  if( lAnnPrima.getMotivazioni()!= null && !eventonotifica.getEvento().getCodMotivo().equals("0296") )
  {
%>
    <tr>
      <td class="l">Motivazioni </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getMotivazioni())%></font>
        </font>
      </td>
    </tr>
<%
  }
%>
<%}%>

    <tr>
      <td class="l">Data Emissione</td>
<%
      if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getDataEmissione()!= null)
      {
%>
        <td class="L" >
          <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
        </td>
<%
      }
%>
  </tr>
  <tr>
    <td class="l">Data Trasmissione</td>
<%
  if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getDataTrasmissioneAtti()!= null)
  {
%>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%>&nbsp;
      </font>
    </td>
<%
  }
%>
  </tr>
  <tr>
   <td class="l">Magistrato
   <td class="L">
     <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
     <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>

<%
  if(istituto != null && istituto.getDescrTipoIstituto() != null
     && !istituto.getDescrTipoIstituto().equals(""))
  {
%>
    <tr>
      <td class="l">Istituto di detenzione</td>
      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(istituto.getDescrTipoIstituto())%></font>&nbsp;di
         <font class="campo"><%=StringUtils.toStringJSP(istituto.getDescrComune())%></font>
       </td>
    </tr>
<%
  }


  if(cssa != null && cssa.getIdCSSA() != null && cssa.getComune() != null && cssa.getIndirizzo() != null)
  {
%>
    <tr>
      <td class="l">UEPE</td>
      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(cssa.getComune())%></font>&nbsp;
         <font class="campo"><%=StringUtils.toStringJSP(cssa.getIndirizzo())%></font>
       </td>
    </tr>
<%
  }
  if(ufficiouds != null && ufficiouds.getDescrComune() != null
    && !ufficiouds.getDescrComune().equals(""))
  {
%>
    <tr>
      <td class="l">Magistrato di Sorveglianza</td>
      <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(ufficiouds.getDescrComune())%></font>
       </td>
    </tr>
<%
  }
  if(ufficiotds != null && ufficiotds.getDescrComune() != null
    && !ufficiotds.getDescrComune().equals(""))
  {
%>
    <tr>
      <td class="l">Tribunale di Sorveglianza</td>
      <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(ufficiotds.getDescrComune())%></font>
       </td>
    </tr>
<%
  }

 if(autoritaEsterna!= null && autoritaEsterna.getCodTipoAutorita() != null && autoritaEsterna.getCodSede()!= null)
{%>

   <tr>
    <td class="l">Autorità Competente</td>
    <td class="L">
    <font class="campo">
         <%=StringUtils.toStringJSP(autoritaEsterna.getDescrTipoAutorita())%></font>
 <%if(autoritaEsterna !=null && !autoritaEsterna.getDescrSede().equals("-"))
{%>
di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsterna.getDescrSede()) %></font>
<%}%>

     </td>
   </tr>

<%if(noteautoritaEsterna != null && !noteautoritaEsterna.equals("")){%>
<tr>
<td class="l">Indirizzo</td>
    <td class="l">
         <font class="campo"><%=noteautoritaEsterna%>&nbsp;</font>
    <td>
</tr>

<%}
 }


%>


  <tr><td>&nbsp;</td></tr>

</table>
  <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadOrdineScarcerazioneProvv">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.richiesta.action.ActDettaglioOrdineScarcerazioneProvv">
          </td>
        </tr>
      </table>
    </form>
  </div>
  <br>
  <br>
</body>
</html>