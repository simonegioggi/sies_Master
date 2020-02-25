<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<jsp:useBean id="fc" scope="request" class="siap.sige.documentoallegato.model.DocumentoAllegatoModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Motivazioni dell'Annullamento  Foglio Complementare</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      function verify(){
        window.close();
      }
    </script>
</head>
  
<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">Motivazioni dell' Annullamento </font>
      </td>
    </tr>
  </table>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <!--input type="HIDDEN" name="<-%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActCancellaProvvedimento"-->
    <table width="80%">
      <tr>
        <td class="l" width="20%">Motivazioni </td>         
		<td class="l">
			<TEXTAREA title="Note" name="motivazioni" readonly cols=50 rows=3><%=StringUtils.toStringJSP(fc.getMotivoAnnullamento())%></textarea>
		</td>
   </tr>

   <tr>
     <td class="lNoBord" colspan="2">
       <br><br>
       <INPUT class="bottone" type="submit" name="I" value="Chiudi" onClick="self.close();">
    </td>
   </tr>
  </table>
	</form>
	</body>
</html>