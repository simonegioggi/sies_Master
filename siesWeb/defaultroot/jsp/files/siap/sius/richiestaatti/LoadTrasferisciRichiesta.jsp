<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDEvento" scope="request" class="java.lang.String"/>
<jsp:useBean id="UEPE" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Richiesta Relazione all'UEPE </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  <script language="JavaScript">
    var desktop;
    function ListaComuniTds(formname,fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // Lista Uffici per TIPO_UFFICIO ( UEPE / UEPESS )
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>
</head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        lAction = "siap.sius.richiestaatti.action.ActTrasferisciRichiesta";
%>
        <font class="campo">Trasferimento Richiesta</font>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp"/>
  <br>
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciRichiesta" onsubmit="document.forms[0].go.disabled=true">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">UEPE Destinatario </td>
        <td class="L">
         <select Title="UEPE Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           <%=UEPE%>
         </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede UEPE Destinatario </td>
        <td class="L">
          <input title="Sede UEPE Destinatario"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaUfficiPerTipo('LoadTrasferisciRichiesta','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>',document.LoadTrasferisciRichiesta.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[document.LoadTrasferisciRichiesta.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0>
            </a>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>
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