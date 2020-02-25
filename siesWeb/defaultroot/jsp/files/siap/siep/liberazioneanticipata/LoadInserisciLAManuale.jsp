<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<html>

<jsp:useBean id="lTotGiorniConcessi" scope="request" class="java.lang.String"/>

<head>
  <title>[S.I.E.S.] - Inserimento Liberazione Anticipata Manuale </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    function Verifica()
    {
      if(document.f.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value==0)
      {
        alert("Impossibile forzare il numero dei giorni a 0");
        document.f.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.focus;

        return false;
      }

      document.f.INSERISCI.disabled=true;
    }
  </script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Inserimento Liberazione Anticipata Manuale</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActInserisciLAManuale">
		 <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Liberazione Anticipata Concessa in giorni <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Totale Giorni" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>" value="<%=lTotGiorniConcessi%>" size=5>
        </td>
     </tr>
     <tr>
       <td>
       <br> <INPUT  class="bottone" type="submit" name="INSERISCI" value="Conferma">
       </td>
     </tr>
    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("f");

    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>","req","il totale dei giorni è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verifica");
  </script>
</body>
</html>