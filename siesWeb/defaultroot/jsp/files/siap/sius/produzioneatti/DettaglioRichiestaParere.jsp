<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.motivazionedecreto.model.MotivazioneDecretoModel"%>

<jsp:useBean id="eventoNotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="actRet"  scope="request" class="java.lang.String"/>
<jsp:useBean id="codMotivo"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="motivazioniDecreto" scope="request" class="java.util.Vector" />

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Richiesta Parere </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    function lookUpload()
    {
      var node;
      node = document.getElementById('upld');
			node.style.visibility='visible';
    }
    </script>
  </head>

  <%
  if(eventoNotifica.getEvento().getFlagDocumentoRegistrato()!= null)
  {
    if (eventoNotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)
    {%>
      <BODY class="corpo" onload="javascript:lookUpload();">
  <%}
    else
    {%>
      <BODY class="corpo">
  <%}
  }
  else
  {%>
      <BODY class="corpo">
  <%}%>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">DETTAGLIO <%=eventoNotifica.getEvento().getDescrMotivo()%></font>
        </td>
<%
  if ( (eventoNotifica.getEvento().getFlagDocumentoRegistrato()==null ) || (eventoNotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0))
  {%>
    <td class="LBG">
      <a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sius.produzioneatti.action.ActStampaProduzioneAtti&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventoNotifica.getEvento().getIdEvento()%>')" onclick="javascript:lookUpload();">
        <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      </a>
    </td>

    <%
    if (Modificabile.compareTo("SI") == 0)
    {%>
      <!-- BOTTONE DI CANCELLAZIONE -->
      <td class="LBG">
        <a href="Javascript:conferma('siap.sius.richiestaatti.action.ActCancellaRichiestaAtti','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=eventoNotifica.getEvento().getIdEvento()%>','TornaQui','<%=TornaQui%>');">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
        </a>
      </td>
	<%}  /* endif Modificabile = SI */
  }%>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
  </table>

  <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Richiesta Parere</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

  <%
    int lSizeNotifiche = eventoNotifica.getNotifiche().length;
    for(int count = 0;count < lSizeNotifiche; count++)
    {
      if (eventoNotifica.getNotifiche()[count].getUfficio() != null )
        {%>
          <tr>
            <td class="l">Destinatario</td>
            <td class="L" >
              <font class="campo"><%=eventoNotifica.getNotifiche()[count].getUfficio().getDescrTipoUfficio()%></font>&nbsp;
            </td>

            <td class="l">Luogo </td>
            <td class="L" >
              <font class="campo"><%=eventoNotifica.getNotifiche()[count].getUfficio().getDescrComune()%></font>&nbsp;
            </td>
          </tr>
        <%
        }else if (eventoNotifica.getNotifiche()[count].getAutoritaEsterna() != null )
        {%>
	        <tr>
  	        <td class="l">Destinatario</td>
    	      <td class="L" >
      	      <font class="campo"><%=eventoNotifica.getNotifiche()[count].getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp;
        	  </td>
          	<td class="l">Luogo </td>
	          <td class="L" >
  	          <font class="campo"><%=eventoNotifica.getNotifiche()[count].getAutoritaEsterna().getDescrSede()%></font>&nbsp;
    	      </td>
      	  </tr>
      <%}else if (eventoNotifica.getNotifiche()[count].getCSSA() != null )
        {%>
          <tr>
            <td class="l">Destinatario</td>
            <td class="L" colspan="5">
              <font class="campo"><%=eventoNotifica.getNotifiche()[count].getCSSA().getTipo()%></font>&nbsp;
            </td>
            <td class="l">Luogo </td>
            <td class="L" colspan="5">
              <font class="campo"><%=eventoNotifica.getNotifiche()[count].getCSSA().getComune()%></font>&nbsp;
            </td>
          </tr>
      <%}%>

        <tr>
          <td class="l">Tipo Parere</td>
          <td class="L" colspan="5">
            <font class="campo"><%=codMotivo%></font>&nbsp;
          </td>
        </tr>

        <tr>
          <td class="l">Data Emissione Parere</td>
          <td class="L" colspan="5">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataRicezioneAtti(),"dd-MM-yyyy"))%></font>&nbsp;
          </td>
        </tr>

        <tr>
          <td class="l">Esito Parere</td>
          <td class="L" colspan="5">
            <font class="campo"><%=eventoNotifica.getEvento().getDescrEsito()%></font>&nbsp;
          </td>
        </tr>
    <%}%>

   <%
   // Preleva le note
    if (eventoNotifica.getCampoNote()!= null && eventoNotifica.getCampoNote().length > 0 )
    {
    %>
      <tr>
       <td class="l" >Motivazioni: </td>
      </tr>
     <%
      int lung = eventoNotifica.getCampoNote().length;
      for (int i=0; i<lung; i++)
      {
       %>
      <tr>
        <td class="L" colspan="8">
          <font class="campo"><%=StringUtils.toStringJSP( eventoNotifica.getCampoNote()[i].getDescr())%></font>&nbsp;
        </td>
      </tr>
    <%
      }
    }
    %>

   <%
   // Motivazioni decreto inammissibilità
    if (motivazioniDecreto != null && motivazioniDecreto.size() > 0)
    {
    %>
      <tr>
       <td class="l" >Motivazioni di inammissibilità: </td>
      </tr>
     <%

      Iterator itx = motivazioniDecreto.iterator();
      while ( itx.hasNext())
      {
          MotivazioneDecretoModel motivazione = (MotivazioneDecretoModel)itx.next();

        // Stampa la descrizione motivazione se è != null
        if( motivazione.getDescrMotivazione() != null )
        {
%>
          <tr>
            <td class="l" colspan=2><%=motivazione.getDescrMotivazione()%></td>
          </tr>
<%
        }
        // Stampa la descrizione  Altra motivazione se != null
        else if( motivazione.getAltraMotivazione() != null )
        {
%>
          <tr>
            <td class="l" colspan=2><%=motivazione.getAltraMotivazione()%></td>
          </tr>
<%
        }
      }
    }
%>
   </table>

  <%
    String lAzione;
    // Azione da chiamare per l'inserimento dei dati.
    if (actRet.compareTo("")!= 0 )
    {
      lAzione = actRet;
    }
    else{
      lAzione = "siap.sius.produzioneatti.action.ActLoadFSPRichiestaParere";
    }
  %>


  <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input class="bottone"  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.produzioneatti.action.ActLoadDettaglioRichiestaParere">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=eventoNotifica.getEvento().getIdEvento()%>">
        </td>
      </tr>
    </table>

    </FORM>
    </div>
    <br>
  </body>

</html>