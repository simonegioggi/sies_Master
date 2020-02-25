<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Sentenza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
        FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		lFascicolo = fascicolo;
		
        String lAction = new String();
		
        if (lFascicolo.getNote() == null || lFascicolo.getNote().trim().equals("")) {
        	
          lAction = "siap.siep.fascicolo.action.ActInserisciNoteProcedimento";
%>
          <font class="campo">Inserimento Note Procedimento</font>
<%
        }
        else {
        	
          lAction = "siap.siep.fascicolo.action.ActModificaNoteProcedimento";

%>
          <font class="campo">Modifica Note Procedimento</font>
<%
    }
%>
      </td>
    </tr>
  </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="form1">
  <table cellspacing=2 cellpadding=2>
  <tr>
      <td class="l">Note Procedimento</td>
      <td class="l">
        <textarea title="Note Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>" cols=40 rows=5><%=StringUtils.toStringJSP(lFascicolo.getNote()).trim()%></textarea>
		</tr>
    <tr height=50>
      <td> </td>
    </tr>		
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(lFascicolo.getIdFascicoloSiep())%>">

  </form>
  </body>
</html>