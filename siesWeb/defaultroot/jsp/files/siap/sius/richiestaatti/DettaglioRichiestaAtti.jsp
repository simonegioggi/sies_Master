<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="eventoNotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="actRet"          scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"  scope="request" class="java.lang.String"/>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Richiesta Atti</title>
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
   if (Modificabile == null || Modificabile.trim().length() < 1)
    Modificabile = "SI";

  if(eventoNotifica.getEvento().getFlagDocumentoRegistrato()!= null)
  {
    if (eventoNotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)
    {
  %>
      <BODY class="corpo" onload="javascript:lookUpload();">
  <%
    }
    else
    {
  %>
      <BODY class="corpo">
  <%
    }
  }
  else
  {
  %>
      <BODY class="corpo">
  <%
  }
  %>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">DETTAGLIO <%=eventoNotifica.getEvento().getDescrMotivo()%></font>
        </td>

  <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%=ICostantiRichiestaAtti.PG_BUTTONS_DETTAGLIORICHIESTAATTI%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
          <jsp:param name="ValoreIdEntita" value="<%=eventoNotifica.getEvento().getIdEvento()%>"/>
          <jsp:param name="FlagDocumentoRegistrato" value="<%=eventoNotifica.getEvento().getFlagDocumentoRegistrato()%>"/>
          <jsp:param name="CodMotivo" value="<%=eventoNotifica.getEvento().getCodMotivo()%>"/>
    </jsp:include>
<%
       if (Modificabile.compareTo("SI") == 0)
       {
%>
         <!-- BOTTONE DI CANCELLAZIONE -->
         <td class="LBG">
           <a href="Javascript:conferma('siap.sius.richiestaatti.action.ActCancellaRichiestaAtti','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=eventoNotifica.getEvento().getIdEvento()%>','TornaQui','<%=TornaQui%>');">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
           </a>
         </td>
<%
       }  /* endif Modificabile = SI */
%>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
  </table>

  <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Richiesta</td>
      <td class="L" colspan="5">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
  <%
    int lSizeNotifiche = eventoNotifica.getNotifiche().length;
    for(int count = 0;count < lSizeNotifiche; count++)
    {
            if (eventoNotifica.getNotifiche()[count].getUfficio() != null )
            {
            %>
              <tr>
                <td class="l">Destinatario n° <%=count+1%></td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getUfficio().getDescrTipoUfficio()%></font>&nbsp;
                </td>

                <td class="l">Luogo </td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getUfficio().getDescrComune()%></font>&nbsp;
                </td>
              </tr>

            <%
            } // Gestione Autorità Esterna
            else if (eventoNotifica.getNotifiche()[count].getAutoritaEsterna() != null )
            {
            %>
              <tr>
                <td class="l">Destinatario n° <%=count+1%></td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp;
                </td>

                <td class="l">Luogo </td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getAutoritaEsterna().getDescrSede()%></font>&nbsp;
                </td>

              </tr>
            <%
            } // Gestione CSSA
            else if (eventoNotifica.getNotifiche()[count].getCSSA() != null )
            {
            %>
              <tr>
                <td class="l">Destinatario n° <%=count+1%></td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getCSSA().getTipo()%></font>&nbsp;
                </td>
                <td class="l">Luogo </td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getCSSA().getComune()%></font>&nbsp;
                </td>

              </tr>
            <%
            } // Gestione istituto di detenzione.
            else if (eventoNotifica.getNotifiche()[count].getIstitutoDetenzione() != null )
            {
            %>
              <tr>
                <td class="l">Destinatario n° <%=count+1%></td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getIstitutoDetenzione().getDescrTipoIstituto()%></font>&nbsp;
                </td>
                <td class="l">Luogo </td>
                <td class="L" colspan="5">
                  <font class="campo"><%=eventoNotifica.getNotifiche()[count].getIstitutoDetenzione().getDescrizione()%></font>&nbsp;
                </td>

              </tr>
            <%
            }
            // Gestione Note
            if (eventoNotifica.getNotifiche()[count].getNote() != null )
            {
                %>
                  <tr>
                    <td class="l"> </td>
                    <td class="L" colspan="5">
                      <font class="campo"><%=eventoNotifica.getNotifiche()[count].getNote()%></font>&nbsp;
                    </td>
                  </tr>
                <%
             }
    }
    %>

   <%
   // Preleva le note
    if (eventoNotifica.getCampoNote()!= null && eventoNotifica.getCampoNote().length > 0 )
    {
    %>
      <tr>
        <td class="L" colspan="6">
          Ulteriori Informazioni
        </td>
      </tr>
    <%

    int lSizeCampoNote = eventoNotifica.getCampoNote().length;
    for(int count = 0;count < lSizeCampoNote; count++)
    {
      %>
      <tr>
        <td class="L" colspan="6">
          <font class="campo"><%=StringUtils.toStringJSP( eventoNotifica.getCampoNote()[count].getDescr())%></font>&nbsp;
        </td>
      </tr>
      <%
     }
    }
    %>
   </table>

 <%
  if( eventoNotifica.getEvento().getFlagDocumentoRegistrato() == null ||
      eventoNotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0 )
  {
    if( eventoNotifica.getEvento().getTemIdTemplate() == null )
    {
    %>
      <form name="dettaglio">
        <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
      </form>
    <%
    }
  }
  %>


  <%
    String lAzione;
    // Azione da chiamare per l'inserimento dei dati.
    if (actRet.compareTo("")!= 0 )
    {
      lAzione = actRet;
    }
    else
    {
      lAzione = "siap.sius.richiestaatti.action.ActRicercaFSPRichiestaAtti";
    }
  %>


  <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input class="bottone"  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.richiestaatti.action.ActLoadDettaglioRichiestaAtti">
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