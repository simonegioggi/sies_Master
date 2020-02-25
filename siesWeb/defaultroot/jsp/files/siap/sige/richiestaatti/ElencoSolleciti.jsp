<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.notifica.model.NotificaModel"%>
<%@page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>
<%@ page import="siap.sius.documentoallegato.model.DocumentoAllegatoModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="eventoNotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="actRet"  scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />

<%

Vector <DocumentoAllegatoModel> allegati=(Vector <DocumentoAllegatoModel>) request.getAttribute("allegati");
String titoloPagina="Elenco Solleciti agli Atti Istruttori";
if (allegati.size() == 0) {
	titoloPagina="Inserimento Sollecito";	
}
%>


<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>


<html>
<head>
  <title>[S.I.E.S.] - <%=titoloPagina %></title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=titoloPagina %></font>
      </td>
        <td class="LBG">
                <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.richiestaatti.action.ActInserisciSollecito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventoNotifica.getEvento().getIdEvento()%>')" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Stampa Sollecito" width="24" height="24" border="0">
          </a>
        </td>
    </tr>
   </table>
<br />
<%
  if (FascicoloSigeEsteso != null)
  {
%>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
   </table>
<%
  } // endif fascicoloSige
%>

<br />
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Richiesta</td>
      <td class="L" colspan="5">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

  <%
    int count=0;
    for(NotificaModel notifica : eventoNotifica.getNotifiche())
    {
          if (notifica.getUfficio() != null )
            {
            %>
              <tr>
                <td class="l">Destinatario n° <%=count+1%></td>
                <td class="L" colspan="5">
                  <font class="campo"><%=notifica.getUfficio().getDescrTipoUfficio()%></font>&nbsp;
                </td>

                <td class="l">Luogo </td>
                <td class="L" colspan="5">
                  <font class="campo"><%=notifica.getUfficio().getDescrComune()%></font>&nbsp;
                </td>
              </tr>

            <%

            }else if (notifica.getAutoritaEsterna() != null )
                    {
                    %>
                      <tr>
                        <td class="l">Destinatario n° <%=count+1%></td>
                        <td class="L" colspan="5">
                          <font class="campo"><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp;
                        </td>

                        <td class="l">Luogo </td>
                        <td class="L" colspan="5">
                          <font class="campo"><%=notifica.getAutoritaEsterna().getDescrSede()%></font>&nbsp;
                        </td>

                      </tr>
                    <%
                    }else if (notifica.getCSSA() != null )
                            {
                            %>
                              <tr>
                                <td class="l">Destinatario n° <%=count+1%></td>
                                <td class="L" colspan="5">
                                  <font class="campo"><%=notifica.getCSSA().getTipo()%></font>&nbsp;
                                </td>
                                <td class="l">Luogo </td>
                                <td class="L" colspan="5">
                                  <font class="campo"><%=notifica.getCSSA().getComune()%></font>&nbsp;
                                </td>

                              </tr>
                            <%
                            }
%>
<% }%>

   </table>
<%
  if ( allegati.size() == 0 )
  {
%>
    <br />
    <table>
        <td class="LBG">
          <font class="label"> Non ci sono solleciti per questo atto. </font>
        </td>
    </table >
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaDocumentiAllegati">
  <table width="90%">
  <div align=center>
    <tr>
      <td class="int" width=30%>Data emissione</td>
      <td class="int" width=50%>Tipo documento</td>
      <td class="int" width=20%>Azioni</td>
    </tr>
  </div>
<%
    for (DocumentoAllegatoModel lDoc : allegati)
    {
%>
    <tr>
      <td class="l">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDoc.getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="l" ><%=StringUtils.toStringJSP(lDoc.getDescrTipoDocumento(),"-")%></td>
<%    String isBlob = "SI";   // flag BLOB pieno
	  
      if (lDoc.getFlagDocumentoRegistrato()==null)
      {
        isBlob="NO";
      }
%>
    <td class="l">
 
                <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sius.documentoallegato.action.ActLoadDocumentoAllegato&<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>=<%=lDoc.getIdDocumentoAllegato()%>')">
                  <img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
                </a>

                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.richiestaatti.action.ActEliminaSollecito&<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>=<%=lDoc.getIdDocumentoAllegato()%>">
                  <img src="/images/delete.gif" alt="Elimina Sollecito" width="12" height="12" border="0">
                </a>
     
     </td>
    </tr>
  <%
   } // endwhile
  %>
  </table>
  </FORM>
<%
  }  
%>
  <%
    String lAzione;
    // Azione da chiamare per l'inserimento dei dati.
    
    if (!actRet.equals(""))
    {
      lAzione = actRet;
    }
    else{
      lAzione = "siap.sige.richiestaatti.action.ActRicercaStatoAtti";
    }
  %>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ListaDocumentiAllegati">
    <table >
      <tr>
        <td>
          <input class="bottone"  type="submit" value="Ritorna">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
          <input type="HIDDEN" name="noQuery" value="ok" >
        </td>
      </tr>
    </table>
  </form>

  </body>
</html>