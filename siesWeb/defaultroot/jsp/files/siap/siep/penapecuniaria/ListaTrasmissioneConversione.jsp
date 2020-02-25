<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.xml.TreeModel" %>
<%@ page import="siap.sico.evento.model.XModel" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<jsp:useBean id="dataRicercaInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataRicercaFine" scope="request" class="java.lang.String" />
<jsp:useBean id="codUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoEsito" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoEsito" scope="request" class="java.lang.String" />
<jsp:useBean id="codTipoOperazione" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoOperazione" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
<html>
  <head>
    <title>[S.I.E.S.] - Lista Messaggi Trasmessi (Conversione Pene Pecuniarie)</title>
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
      <font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Atti di Conversione Pene Pecuniarie Trasmessi</font>&nbsp;&nbsp;
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
          <td class="lVerdeNB">Procedimenti con Data di Trasmissione :&nbsp;&nbsp;
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
      }%>
      <tr>
        <td class="lVerdeNB">Tipo Atto : <%=descTipoOperazione%></td>
      </tr>
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
      <td class="int">Tipo operazione</td>
<%		if(UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("UEPE")==0 )
			{%>
      	<td class="int">Anno/Numero SIEPE</td>
    <%}else{%>
      	<td class="int">Anno/Numero SIEP</td>
    <%}%>
      <td class="int">Anno/Numero SIUS</td>
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
      <td class="c"><%= lMess.getDescrTipoOperazione()%></td>
<%		if(UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("UEPE")==0 )
			{%>
      	<td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiepe())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiepe())%></td>
    <%}else{%>
      	<td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
    <%}%>
      <td class="c"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSius())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSius())%></td>
      <td class="c"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
      <td class="c">-</td>
      <%--td class="c"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td--%>
      <td class="c"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
      <td class="c">In Attesa di Risposta...</td>
<%
    }
    else
    {
			// 16/08/2006 Aggiunti ESITI 01001 (Atto Preso in Carico) e 01002 (Atto Preso in Visione)
      if (lMess.getMessaggioCorrelato().getCodEsito().compareTo("00000") == 0 ||
          lMess.getMessaggioCorrelato().getCodEsito().compareTo("01001") == 0 ||
          lMess.getMessaggioCorrelato().getCodEsito().compareTo("01002") == 0    )
      {
%>
        <td class="cVerde"><%= lMess.getDescrTipoOperazione()%></td>

<%			if(UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("UEPE")==0 )
				{%>
      		<td class="cVerde"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiepe())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiepe())%></td>
    	<%}else{%>
      		<td class="cVerde"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
    	<%}%>
        <td class="cVerde"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSius())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSius())%></td>
        <td class="cVerde"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cVerde"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getMessaggioCorrelato().getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <%--td class="cVerde"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td--%>
        <td class="cVerde"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
        <td class="cVerde"><%= lMess.getMessaggioCorrelato().getDescrEsito()%></td>
<%
      }
      else if (lMess.getMessaggioCorrelato().getCodEsito().compareTo("01003") == 0) // Atto Restituito
      {
				String lTitle = "";
				if (lMess.getMessaggioCorrelato().getTreeModel()!=null)
				{
        	XModel lXMod = (XModel)lMess.getMessaggioCorrelato().getTreeModel().getModel();
					if (lXMod != null && lXMod.getMessage()!=null)
        		lTitle = "Motivo : "+lXMod.getMessage();
				}
%>
        <td class="cGrigio"><%= lMess.getDescrTipoOperazione()%></td>
<%			if(UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("UEPE")==0 )
				{%>
      		<td class="cGrigio"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiepe())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiepe())%></td>
    	<%}else{%>
      		<td class="cGrigio"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
    	<%}%>
        <td class="cGrigio"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSius())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSius())%></td>
        <td class="cGrigio"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cGrigio"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getMessaggioCorrelato().getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cGrigio"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
        <td class="cGrigio" title="<%=lTitle%>"><%= lMess.getMessaggioCorrelato().getDescrEsito()%></td>
<%
      }
      else
      {
%>
        <td class="cRosso"><%= lMess.getDescrTipoOperazione()%></td>
<%			if(UtenteConnesso.getUfficioUtente().getCodTipoUfficio().compareTo("UEPE")==0 )
				{%>
      		<td class="cRosso"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiepe())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiepe())%></td>
    	<%}else{%>
      		<td class="cRosso"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
    	<%}%>
        <td class="cRosso"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSius())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSius())%></td>
        <td class="cRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="cRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getMessaggioCorrelato().getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <%--td class="cRosso"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td--%>
        <td class="cRosso"><%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%></td>
        <td class="cRosso"><%= lMess.getMessaggioCorrelato().getDescrEsito()%></td>
<%    }
    }%>
    <td class="c">
        <%--jsp:include page="<%=IWebConstants.PG_BUTTONS%>"--%>
        <jsp:include page="<%=ICostantiMessaggio.PG_BUTTONS_MESSAGGIO%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
           <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
        </jsp:include>
       </td>
     </tr>
<%}%>
    </table>
  </div>
<%
}
%>
    <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
    </body>
</html>