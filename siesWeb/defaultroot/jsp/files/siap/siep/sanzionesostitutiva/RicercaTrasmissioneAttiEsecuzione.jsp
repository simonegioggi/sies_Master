<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<jsp:useBean id="dataRicercaInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataRicercaFine" scope="request" class="java.lang.String" />
<jsp:useBean id="codUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoEsito" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoEsito" scope="request" class="java.lang.String" />
<jsp:useBean id="codUfficioDestinatario" scope="request" class="java.lang.String" />
<jsp:useBean id="descUfficioDestinatario" scope="request" class="java.lang.String" />
<jsp:useBean id="annoSiep" scope="request" class="java.lang.String" />
<jsp:useBean id="progrSiep" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
<html>
  <head>
    <title>[S.I.E.S.] - Messaggi Trasmessi</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript" src="/html/conferma.js"></script>
    <SCRIPT LANGUAGE="JavaScript">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      // preload images:
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
      <font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Atti Trasmessi</font>&nbsp;&nbsp;
     </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
<br>
<%
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
%>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
    </tr>
<%
      if(!(dataRicercaInizio.equals(""))||!(dataRicercaFine.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Data di Trasmissione :&nbsp;&nbsp;
<%
          if(!(dataRicercaInizio.equals("")))
          {
%>
            Dal <%=dataRicercaInizio%>&nbsp;&nbsp;
<%        }
          if(!(dataRicercaFine.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataRicercaFine%>
            </td>
<%        }
       %></tr><%
      }
      if(!(descUfficioDestinatario.equals("")))
      {
%>
      <tr>
        <td class="lVerdeNB">Ufficio destinatario : <%=descUfficioDestinatario%></td>
      </tr>
     <%}
      if((annoSiep.length()>1) )
      {
%>
      <tr>
        <td class="lVerdeNB">Numero SIEP : <%=annoSiep%>/<%=progrSiep%></td>
      </tr>
     <%}%>

      <tr>
        <td class="lVerdeNB">Tipo Esito : <%=descTipoEsito%></td>
      </tr>
      <tr>
        <td class="lVerdeNB">Utente che ha effettuato la trasmissione :
<%        if(codUtente.length()>1 )
	    {%> <%=codUtente%><%}
	  else
	    {%> <%=descTipoUtente%><%}
%>
	</td>
      </tr>
  </table>

<div id="elenco" style="width: 100%;">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Tipo Operazione</td>
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
      <td class="c"><%= StringUtils.toStringJSP(lMess.getDescrTipoOperazione())%></td>
      <td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
      <td class="c"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
      <td class="c">-</td>
      <%--td class="c"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td--%>
      <td class="c"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
      <td class="c">In Attesa di Risposta...</td>
<%
    }
    else
    {
      if (lMess.getMessaggioCorrelato().getCodEsito().compareTo("00000") == 0)
      {
%>
        <td class="c"><%= StringUtils.toStringJSP(lMess.getDescrTipoOperazione())%></td>
        <td class="cVerde"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
        <td class="cVerde"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cVerde"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getMessaggioCorrelato().getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <%--td class="cVerde"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td--%>
        <td class="cVerde"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
        <td class="cVerde"><%= lMess.getMessaggioCorrelato().getDescrEsito()%></td>
<%
      }
      else
      {
%>
        <td class="c"><%= StringUtils.toStringJSP(lMess.getDescrTipoOperazione())%></td> 
        <td class="cRosso"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
        <td class="cRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getMessaggioCorrelato().getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <%--td class="cRosso"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td--%>
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
    <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
  </body>
</html>