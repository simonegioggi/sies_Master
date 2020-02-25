<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza" %>

<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDEvento" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Istanza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  <script language="JavaScript">
    var desktop;

    function ListaComuniTds(formname,fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>
</head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        lAction = "siap.siep.istanza.action.ActTrasferisciIstanza";
%>
        <font class="campo">Trasferimento Istanza</font>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="TrasferisciIstanza" onsubmit="document.forms[0].go.disabled=true">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Destinatario </td >
        <td class="L">
         <select Title="Destinatario" name="<%=ICostantiIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           <%=uffici%>
         </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Destinatario </td><td class="L">
          <input title="Sede Destinatario"  type="text" name="<%= ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuniTds('TrasferisciIstanza','<%= ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
        </td>
      </tr>
<!----------------------------------------->
      <tr>
        <td>
          <input name=go class=bottone  type="submit" value="Conferma">
        </td>
      </tr>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IDEvento%>">
    </table>
  </form>

</body>
</html>