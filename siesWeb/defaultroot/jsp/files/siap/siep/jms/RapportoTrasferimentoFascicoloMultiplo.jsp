<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>

<jsp:useBean id="Messaggio"     scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="fascicoli"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="dettFascicoli" scope="request" class="java.util.Vector"/>


<%
  String[] Selection = (String[])request.getAttribute("Selection");
%>


<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Istanza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
 <body class="corpo">
  <FORM name="comandi" >
    <table>
        <tr><td class="LBG"><a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione: </font>&nbsp;
            <font class="campo">Dettaglio Trasferimento Procedimenti da Altra BDI</font>
          </td>
         </tr>
          <tr>
          <td colspan="2">
           &nbsp;
          </td>
        </tr>
     </table>

<%
        if(fascicoli != null && fascicoli.size()>0)
        {
          // 11/02/2008 Modifica controllo di Selezione del Fascicolo.
          for(int i=0;i<fascicoli.size();i++)
          {
            FascicoloSiepModel fascicolo = (FascicoloSiepModel)fascicoli.get(i);
            if ( Selection[i].compareTo("on")==0 )
            {
%>
              <table align="center">
              <tr>
                <td colspan="2">
                  <font class="label">Procedimento  N.ro </font>&nbsp;
                  <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
                    <%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>
                    /
                    <%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>
                  </a>
                </td>
              </tr>
              <tr>
                <td class="LBG" colspan="2">
                  <font class="label">Esito del Trasferimento Procedimento</font>&nbsp;
                </td>
              </tr>

              <%=Messaggio.getRapportoEsito()%>
              </table>
          <%}%>
          <br>
        <%}
        }%>

    </FORM>
  </body>
 </html>