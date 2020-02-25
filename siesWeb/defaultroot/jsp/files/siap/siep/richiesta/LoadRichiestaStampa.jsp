<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta" %>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="soggettoSiep"   scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="sentenzaSiep"   scope="request" class="siap.siep.sentenza.model.SentenzaModel" />

<% // Probabilmente mai utilizzata  %>


<head>
  <title> [S.I.E.S.] - Ricerca Sentenza - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  function Verify()
	{
		if (document.f.<%=ICostantiRichiesta.CAMPO_DATA_GG_EMISSIONE%>.value.length==1)
			document.f.<%=ICostantiRichiesta.CAMPO_DATA_GG_EMISSIONE%>.value='0'+document.f.<%=ICostantiRichiesta.CAMPO_DATA_GG_EMISSIONE%>.value;
		if (document.f.<%=ICostantiRichiesta.CAMPO_DATA_MM_EMISSIONE%>.value.length==1)
			document.f.<%=ICostantiRichiesta.CAMPO_DATA_MM_EMISSIONE%>.value='0'+document.f.<%=ICostantiRichiesta.CAMPO_DATA_MM_EMISSIONE%>.value;

		var data_to_verify = document.f.<%=ICostantiRichiesta.CAMPO_DATA_GG_EMISSIONE%>.value+'/'+document.f.<%=ICostantiRichiesta.CAMPO_DATA_MM_EMISSIONE%>.value+'/'+document.f.<%=ICostantiRichiesta.CAMPO_DATA_AAAA_EMISSIONE%>.value;
		if (!ControllaData(data_to_verify) )
		{
       alert('Data di emissione non valida');
			 return false;
		}
	 }
  </script>

</head>

<body class="corpo">
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActRichiestaStampa">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Richiesta Stampa</font>
      </td>
    </tr>
  </table>

  <br>

  <table>

    <tr>
		<td class="l">Destinatario</td>
        <td>
        <input readonly type="text" value="<%=sentenzaSiep.getDescrTipoAutoritaEmittente()%>" name="<%=ICostantiRichiesta.AUTORITA_DESTINATARIO%>" size = "35">
        </td>
    </tr>
		<tr>
				<td class="l">Sede Destinatario</td>
				<td class="l"><input readonly type="text" value="<%=sentenzaSiep.getDescrLuogoEmittente()%>" name="<%= ICostantiRichiesta.AUTORITA_SEDE %>" size = "35" ></td>
		</tr>
		<tr>
				<td class="l">Testo Libero</td>
				<td class="l"><input type="text" name="<%= ICostantiRichiesta.CAMPO_LIBERO %>" size = "35" ></td>
		</tr>

      <tr>
        <td class="l">Data Emissione <font class=ob>(*)</font></td>
          <td class="L">

            <input value="<%=DateUtils.getSysDate("dd")%>" type="text" name="<%=ICostantiRichiesta.CAMPO_DATA_GG_EMISSIONE%>" maxlength="2" size="2">
            /
            <input value="<%=DateUtils.getSysDate("MM")%>" type="text" name="<%= ICostantiRichiesta.CAMPO_DATA_MM_EMISSIONE %>" maxlength="2" size="2">
            /
            <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" name="<%= ICostantiRichiesta.CAMPO_DATA_AAAA_EMISSIONE %>" maxlength="4" size="4">

          </td>
      </tr>

    <tr>
      <td class="lNoBord" colspan="2">
        <br><br>
        <INPUT class="bottone" type="submit" name="STAMPA" value="Stampa" onClick="javascript:return Verify();">
      </td>
    </tr>

 </table>

</form>

</body>
</html>