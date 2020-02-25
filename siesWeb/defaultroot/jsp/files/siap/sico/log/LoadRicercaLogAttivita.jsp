<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.log.action.ICostantiLogAttivita" %>
<%@ page import="siap.sico.utente.action.ICostantiUtente" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title> [S.I.E.S.] - Ricerca Soggetto - </title>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
    function Verify()
    {
      if (document.RicLog.ggIn.value.length==1)
          document.RicLog.ggIn.value='0'+document.RicLog.ggIn.value;
      if (document.RicLog.mmIn.value.length==1)
          document.RicLog.mmIn.value='0'+document.RicLog.mmIn.value;
      var data_to_verify=document.RicLog.ggIn.value+'/'+document.RicLog.mmIn.value+'/'+document.RicLog.aaIn.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {	alert('Data di inizio non valida');
               return false;
      }
      if (document.RicLog.ggFi.value.length==1)
          document.RicLog.ggFi.value='0'+document.RicLog.ggFi.value;
      if (document.RicLog.mmFi.value.length==1)
          document.RicLog.mmFi.value='0'+document.RicLog.mmFi.value;
      var data_to_verify=document.RicLog.ggFi.value+'/'+document.RicLog.mmFi.value+'/'+document.RicLog.aaFi.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {	alert('Data di fine non valida');
               return false;
      }
    }
    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
	desktop = window.open("/jsp/files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");

    }
    </script>
</head>

<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="RicLog">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.log.action.ActRicercaLogAttivita">

    <table>



      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Log Attività</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
<tr>
        <td class="l">Dalla data</td>
<td class="l"  valign=middle>
        <input title="Giorno Inizio" type="text" name="ggIn" value="" size="2" maxlength="2">
        /
        <input title="Mese Inizio" type="text" name="mmIn" value="" size="2" maxlength="2">
        /
        <input title="Anno Inizio" type="text" name="aaIn" value="" size="4" maxlength="4">
        <a href="Javascript:calendario('RicLog','aaIn','mmIn','ggIn');"><img align=middle alt="Seleziona Data da Calendario" border=0 src="/images/calendario.gif"></a>
</td>

<td class="l">Alla data</td>
<td class="l" valign=middle>
        <input title="Giorno Fine" type="text" name="ggFi" value="" size="2" maxlength="2">
        /
        <input title="Mese Fine" type="text" name="mmFi" value="" size="2" maxlength="2">
        /
        <input title="Anno Fine" type="text" name="aaFi" value="" size="4" maxlength="4">
        <a href="Javascript:calendario('RicLog','aaFi','mmFi','ggFi');"><img align=middle alt="Seleziona Data da Calendario" border=0 src="/images/calendario.gif"></a>
</td>
</tr>
<tr>
        <td class="l">Utente</td>
        <td class="l" valign=middle colspan=3><input name="<%=ICostantiLogAttivita.CAMPO_COD_OPERATORE%>" type=text size=20 maxlength=20 value=""></td>
     </tr>
<tr>
        <td class="l">Cognome</td>
        <td class="l" valign=middle><input name="<%=ICostantiUtente.CAMPO_COGNOME%>" type=text size=20 maxlength=20 value=""></td>
        <td class="l">Nome</td>
        <td class="l" valign=middle><input name="<%=ICostantiUtente.CAMPO_NOME%>" type=text size=20 maxlength=20 value=""></td>
     </tr>
<tr>
        <td class="l">Ip Utente</td>
        <td class="l" valign=middle colspan=3><input name="<%=ICostantiLogAttivita.CAMPO_IP_UTENTE%>" type=text size=16 maxlength=15 value=""></td>
     </tr>
<tr>
        <td class="l">Testo libero</td>
        <td class="l" valign=middle colspan=3><input name="<%=ICostantiLogAttivita.CAMPO_RECORD%>" type=text size=40 maxlength=40 value=""></td>
     </tr>

 <tr>
<tr>
        <td class="l">Escludi attività utente <%=UtenteConnesso.getUserId()%></td>
        <td class="l" valign=middle colspan=3><input name="FlagAttivita" type=checkbox value="1"></td>
     </tr>

 <tr>
        <td colspan="2">
        <br><br>
          <INPUT class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>

    </table>
  </form>

 <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("RicLog");
  
  frmvalidator.addValidation("<%=ICostantiLogAttivita.CAMPO_COD_OPERATORE %>","alphanum","Il campo Codice Operatore può contenere solo caratteri alfanumerici");
  
  //frmvalidator.addValidation("<!%=ICostantiLogAttivita.CAMPO_IP_UTENTE %>","num");
  
  frmvalidator.addValidation("<%=ICostantiUtente.CAMPO_NOME %>","alpha","Il campo Nome Utente non può contenere caratteri non alfabetici");
  
  frmvalidator.addValidation("<%=ICostantiUtente.CAMPO_COGNOME %>","alpha","Il campo Cognome Utente non può contenere caratteri non alfabetici");
  
  frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>

</html>