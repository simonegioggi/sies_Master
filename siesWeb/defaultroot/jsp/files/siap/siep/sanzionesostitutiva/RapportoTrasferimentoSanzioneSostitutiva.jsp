<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>


<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Rapporto Trasferimento Sanzione Sostitutiva</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
 <body class="corpo">
  <FORM name="comandi" method="post" onsubmit="document.comandi.I.disabled=true;">
    <table>
        <tr><td class="LBG"><a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione: </font>&nbsp;
            <font class="campo">Rapporto Trasferimento Sanzione Sostitutiva</font>
          </td>
         </tr>
      </table>
	<br>

   <table width="300"  cellspacing="0" align="center" class="tab" border="1">
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab">
          <p>&nbsp;<p>
          <B>Ricezione Atto Completata e Esito rispedito al Mittente.</B>
          <B> Iscrivere il procedimento Sius!</B>
          <p>&nbsp;<p>

        </td>
      </tr>

      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab2">
             <input class=bottone name="I" type="submit" value="Prosegui">
        </td>
      </tr>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.iscrizioneprocedimento.action.ActLoadIscrProcedimentoDaSiep">
      <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
      <tr align="left" >
        <td colspan="2"  class="tabhead"></td>
      </tr>

    </table>

		<p>
     <table width="700"  cellspacing="0" align="center" class="tab" border="1">

        <%if(fascicoli != null &&fascicoli.size()>0)
        {
          for(int i=0;i<fascicoli.size();i++)
          {
          FascicoloSiepModel fascicolo = (FascicoloSiepModel)fascicoli.get(i);%>
        <tr>
        <td colspan="2">
        <font class="label">Procedimento : N.</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
          <%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>
          /
          <%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>
        </a>&nbsp;</td></tr>
        <%}
        }%>


<%			if (Messaggio.getRapportoEsito()!=null && Messaggio.getRapportoEsito().length()>2)
				{%>
        <tr>
          <td class="LBG" colspan="2">
            <font class="label">Esito del Trasferimento</font>&nbsp;
          </td>
        </tr>
        <%=Messaggio.getRapportoEsito()%>
      <%}%>

      </table>
    </FORM>
  </body>
 </html>