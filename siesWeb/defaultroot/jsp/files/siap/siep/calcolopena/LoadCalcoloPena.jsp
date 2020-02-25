<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>


<head>
  <title> [S.I.E.S.] - Calcolo Pena - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  function Verify()
	{

		if (document.f.GiornoInizio.value.length==1)
			document.f.GiornoInizio.value='0'+document.f.GiornoInizio.value;
		if (document.f.MeseInizio.value.length==1)
			document.f.MeseInizio.value='0'+document.f.MeseInizio.value;

		var data_to_verify = document.f.GiornoInizio.value+'/'+document.f.MeseInizio.value+'/'+document.f.AnnoInizio.value;
                if (data_to_verify.length>4)
                {
		  if (!ControllaData(data_to_verify) )
		  {
                    alert('Data di Decorrenza non valida');
                    return false;
		  }
                }
	 }

  </script>

</head>

<body class="corpo">
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActCalcoloPena">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Calcolo della Pena</font>
      </td>
    </tr>
  </table>

  <br>

  <table>



     <tr>
        <td class="l">Data Decorrenza Pena</td>
          <td class="L">

            <input value="" type="text" name="GiornoInizio" maxlength="2" size="2">
            /
            <input value="" type="text" name="MeseInizio" maxlength="2" size="2">
            /
            <input value="" type="text" name="AnnoInizio" maxlength="4" size="4">

          </td>
      </tr>

    <tr>
      <td class="lNoBord" colspan="2">
        <br><br>
        <INPUT class="bottone" type="submit" name="STAMPA" value="Conferma" onClick="javascript:return Verify();">
      </td>
    </tr>

 </table>

</form>

</body>
</html>