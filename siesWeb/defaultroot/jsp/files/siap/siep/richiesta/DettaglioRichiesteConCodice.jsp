<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="annotazioneManuale" scope="request"  class="java.util.Vector"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistrato"   scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="lEveOE" scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="eventonotifica"   scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="modifica"   scope="request" class="java.lang.String" />

<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
  <%if(eventonotifica.getEvento().getCodMotivo().equals("0287")){%>
      <font class="campo">Determinazione pena - ex art. 671 c.p.p. art. 174 c.p.</font>
  <%}%>
  <%if(eventonotifica.getEvento().getCodMotivo().equals("0288")){%>
      <font class="campo">Determinazione pena - art. 671 c.p.p. 151 c.p.</font>
  <%}%>
  <%if(eventonotifica.getEvento().getCodMotivo().equals("0290")){%>
      <font class="campo">Applic. benefici - ex.art.174 c.p. e 672 c.p.p.</font>
  <%}%>
  <%if(eventonotifica.getEvento().getCodMotivo().equals("0291")){%>
      <font class="campo">Revoca benefici - ex art. 174 c.p. e 674 c.p.p.</font>
  <%}%>
  <%if(eventonotifica.getEvento().getCodMotivo().equals("0292")){%>
      <font class="campo">Revoca benefici - ex art.151 c.p. e 674 c.p.p.</font>
  <%}%>
  <%if(eventonotifica.getEvento().getCodMotivo().equals("0294")){%>
      <font class="campo">Estinzione Reato ex art. 151 c.p. e  672 c.p.p.</font>
  <%}%>
  <%if(eventonotifica.getEvento().getCodMotivo().equals("0296")){%>
    <font class="campo">Restituzione Ordine Esecuzione - ex art. 672 c.p.p.</font>
  <%}%>
  </td>

<%
  if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
    if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
    {
%>
      <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichiesteConCodice&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
<%
    }

    if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
    {
%>
      <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichiesteConCodice&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
<%    }%>
    <td class="LBG">
    <% 
  
if((modifica!=null) && (modifica.equals("D")))
	{
	%>
      <a href="javascript:history.back()">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
     <%    }else{%> 
       <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste&<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>=<%=StringUtils.toStringJSP(eventonotifica.getEvento().getAnnIdAnnotazioneManuale())%>" >
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
     <%    }%>
    </td>
    
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
         
        </td>
      </tr>
<%
    }
  }

  if(eventonotifica.getEvento().getCodMotivo().equals("0296") && (lEveOE != null &&lEveOE.getIdEvento()!= null ))
  {
%>
    <tr>
      <td class="l">Data Emissione OE </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveOE.getDataEmissione(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l" >Anno / Numero Protocollo </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(lEveOE.getAnnoProtocollo())%> /
        </font>
        <font class="campo">
          <%=StringUtils.toStringJSP(lEveOE.getProgrProtocollo())%>
        </font>
      </td>
    </tr>
<%
  }
%>
  </table>
  <table width="70%">
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
  <tr><td>&nbsp;</td></tr>
  <tr>
   <td class="l">Magistrato
   <td class="L">
     <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
     <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<%
  if(eventonotifica.getEvento().getCodMotivo().equals("0296"))
  {
%>
    <tr>
      <td class="l">Autorità Competente</td >
      <td class="l">
      	<font class="campo">
      		<%if(eventonotifica.getNotifiche() !=null && eventonotifica.getNotifiche().length >0)
        	  {%>
      			<%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrTipoAutorita()%>
      		<%} %>
      	</font>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="l">
        <font class="campo">
        	<%if(eventonotifica.getNotifiche() !=null && eventonotifica.getNotifiche().length >0)
        	  {%>
          		<%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede()%>
          	<%} %>
        </font>
      </td>
    </tr>
<%
    if(eventonotifica.getNotifiche() !=null && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0].getNote() != null)
    {
%>
      <tr>
        <td class="l">Note</td>
        <td class="l">
          <font class="campo">
            <%=eventonotifica.getNotifiche()[0].getNote()%>
          </font>
        </td>
      </tr>
<%
    }
  }
  else
  {
%>
    <tr>
      <td class="l">Giudice dell'esecuzione</td >
      <td class="l">
      	<font class="campo">
      		<%if(eventonotifica.getNotifiche() !=null && eventonotifica.getNotifiche().length >0)
        	  {%>
      			<%=eventonotifica.getNotifiche()[0].getUfficio().getDescrTipoUfficio()%>
      		<%} %>
      	</font>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="l">
        <font class="campo">
        	<%if(eventonotifica.getNotifiche() !=null && eventonotifica.getNotifiche().length >0)
        	  {%>
        		<%=eventonotifica.getNotifiche()[0].getUfficio().getDescrComune()%>
        	<%} %>
        </font>
      </td>
    </tr>
<%
  if(eventonotifica.getNotifiche() !=null && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0].getNote() != null)
  {
%>
    <tr>
      <td class="l">Note</td>
      <td class="l">
        <font class="campo">
          <%=eventonotifica.getNotifiche()[0].getNote()%>
        </font>
      </td>
    </tr>
<%
  }
}
%>
</table>
  <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadRichiesteConCodice">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.richiesta.action.ActDettaglioRichiesteConCodice">
          </td>
        </tr>
      </table>
    </form>
  </div>
  <br>
  <br>
</body>
</html>