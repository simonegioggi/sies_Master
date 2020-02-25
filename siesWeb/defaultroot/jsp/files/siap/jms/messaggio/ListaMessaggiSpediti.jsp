<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>


<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoOperazione" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Messaggi Spediti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <SCRIPT LANGUAGE="JavaScript">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <!-- preload images: -->
      if (document.images)
      {
        clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
        clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
      }

      function hiLite(imgName,imgObjName)
      {
        if (document.images)
        {
            document.images[imgName].src = eval(imgObjName + ".src");
        }
      }
    </SCRIPT>
  </head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : Messaggi Spediti</font>&nbsp;&nbsp;
     </td>
    </tr>
  </table>
<br>
<%
String lTipoOperazione="";
if(Messaggi.size()==0)
{
%>
   <p>&nbsp;<p>&nbsp;<p>&nbsp;
   <table width="300"  cellspacing="0" align="center" class="tab" border="1">
     <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab">
          <p>&nbsp;<p>
          <B>Nessun Messaggio Spedito da verificare</B>
          <p>&nbsp;<p>
        </td>
      </tr>
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab2">
          <a href="javascript:history.go(-1);" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
            <IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif" BORDER="0" ALT="" NAME="img01">
          </a>
        </td>
      </tr>
      <tr align="left" >
        <td colspan="2"  class="tabhead"></td>
      </tr>
    </table>
<%
}
else
{
  if (tipoOperazione.length()>1 )
  {
    MessaggioModel lMess0 = (MessaggioModel) Messaggi.get(0);
%>
    <table cellspacing=2 cellpadding=2 >
      <tr>
        <td class="Titolo" colspan=6>Tipo Operazione : <%=lMess0.getDescrTipoOperazione()%></td>
      </tr>
    </table>
<%}%>

 <div id="elenco" style="width: 100%;">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Data Invio Richiesta</td>
                        <td class="int">Data Arrivo Richiesta</td>
                        <td class="int">Ufficio Destinatario</td>
      <td class="int">Esito</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
%>
    <tr>
<%
    if (lMess.getMessaggioCorrelato() == null)
    {
%>
                <td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
                <td class="c"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
                        <td class="c">-</td>
                        <td class="c"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
      <td class="c">In Attesa di Risposta...</td>
<%
    }
    else
    {
      if (lMess.getMessaggioCorrelato().getCodEsito().compareTo("00000") == 0)
      {
%>
                  <td class="cVerde"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
        <td class="cVerde"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cVerde"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getMessaggioCorrelato().getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cVerde"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
        <td class="cVerde"><%= lMess.getMessaggioCorrelato().getDescrEsito()%></td>
<%
      }
      else
      {
%>
                  <td class="cRosso"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
        <td class="cRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getMessaggioCorrelato().getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cRosso"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
        <td class="cRosso"><%= lMess.getMessaggioCorrelato().getDescrEsito()%></td>
<%
      }
    }
%>
                  <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
        </jsp:include>
       </td>
     </tr>
<%
  }
%>
    </table>
  </div>
<%
}
%>
        </body>
</html>