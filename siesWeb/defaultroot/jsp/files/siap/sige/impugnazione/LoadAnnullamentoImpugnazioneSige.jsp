<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>

<jsp:useBean id="IdEvento"      	  scope="request" class="java.lang.String"/>
<jsp:useBean id="IdImpugnazione"      scope="request" class="java.lang.String"/>
<jsp:useBean id="IdFascicoloSige"     scope="request" class="java.lang.String"/>

<jsp:useBean id="nextAction"      scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Motivazioni dell'Annullamento  </title>

    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
   <script language="JavaScript">

function verify()
{
    var lmotivazioni = document.f.motivazioni.value;
    window.opener.top.frames['centrale'].frames['body'].location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=nextAction%>&IdEvento=" + "<%=IdEvento%>" + "&IdImpugnazione=" + "<%=IdImpugnazione%>" + "&IdFascicoloSige=" + "<%=IdFascicoloSige%>" + "&<%=ICostantiImpugnazioneSige.CAMPO_MOTIVO_ANNULLAMENTO%>=" + lmotivazioni + "&TornaQui=10";
 window.close();
}


</script>

  </head>
  <body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
		    <font class="campo">Motivazioni dell' Annullamento </font>
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