<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%
String idMessaggio ="";
if( request.getParameter("IdMessaggio") != null)
	idMessaggio=request.getParameter("IdMessaggio");

String codTipoOperazione ="";
if( request.getParameter("CodTipoOperazione") != null)
	codTipoOperazione=request.getParameter("CodTipoOperazione");

	// Il parametro nextAction consente di prestabilire l'azione da eseguire
	// alla fine dell'attività di revoca/cancellazione provvedimento.
	String nAction ="";
	if( request.getParameter("nextAction") != null)
    nAction="&nextAction="+request.getParameter("nextAction").toString();
%>

<html>
  <head>
    <title>[S.I.E.S.] - Restituzione Atto Ricevito </title>

    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

		function verify()
		{
    	var lmotivazioni = document.f.motivazioni.value;
			var lCodTipoOperazione = '<%=codTipoOperazione%>'
			var lHref ="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.ricezioneatti.action.ActRestituzioneAttoRicevuto&IdMessaggio="+<%=idMessaggio%>+"&CodTipoOperazione="+lCodTipoOperazione+"&Motivazioni="+lmotivazioni+"<%=nAction%>&TornaQui=10";
    	window.opener.top.frames['centrale'].frames['body'].location.href=lHref;
 			window.close();
		}

		</script>

  </head>
  <body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
		    <font class="campo">Motivazioni della Restituzione </font>
      </td>
    </tr>
  </table>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
    <table width="80%">
      <tr>
				<td class="l" width="20%">Motivazioni </td>
				<td  class="l">
         <TEXTAREA title="Note" name="motivazioni" cols=50 rows=3></textarea>
        </td>
			</tr>
			<tr>
       <td class="lNoBord" colspan="2">
       <br><br>
      <INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="verify()">
       </td>
   </tr>

  </table>
	</form>
	</body>
</html>