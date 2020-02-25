<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- Mev10-s3: aggiunta pagina --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>

<jsp:useBean id="modalitaRicerca"  scope="request" class="java.lang.String"/>

<%
	// Valore di default della funzione
	String lNomeFunzione = "Ricerca Provvedimenti Privi di Foglio Complementare";
%>
<html>
<head>
  <title>[S.I.E.S.] - Statistiche Ricerca Provvedimenti Privi di Foglio Complementare</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ICostantiStatistiche.RICERCA_PROVVEDIMENTO_JS%>"></script>

</head>
  <body class="corpo">
  <form name="f">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lNomeFunzione%></font>

      </td>
    </tr>
  </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="valoreRadio" value="">
  <br>
  </form>

  <div id="comune" style="position: relative; top: 0; left: 0;   visibility:visible; " >     
     <div id="RicercaAvanzataDiv" style="position: absolute; top: 0; left: 0; visibility:visible; ">      
        <jsp:include page="<%=ICostantiStatistiche.DIV_RICERCA_PROVV_PRIVI_DI_FC%>"/>
     </div>
  </div>

  </body>
</html>