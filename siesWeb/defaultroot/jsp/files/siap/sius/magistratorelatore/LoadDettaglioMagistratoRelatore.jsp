<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="magistratoNew" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="magistratoOld" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="espertoNew"    scope="request" class="siap.sius.esperto.model.EspertoModel"/>
<jsp:useBean id="espertoOld"    scope="request" class="siap.sius.esperto.model.EspertoModel"/>
<jsp:useBean id="acdest"        scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Magistrato Relatore</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
</head>

<body class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Magistrato Relatore</font>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </td>
      </tr>
    </table>

    <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
  </FORM>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Titolo" colspan=6> Magistrato Relatore </td>
    </tr>

    <tr>
      <td class="l">Cognome :
      <font class="campo">
      <%=StringUtils.toStringJSP(magistratoNew.getCognome())%>
      <%=StringUtils.toStringJSP(espertoNew.getCognome())%>
      </font></td>
      <td class="l">Nome :
      <font class="campo">
      <%=StringUtils.toStringJSP(magistratoNew.getNome())%>
      <%=StringUtils.toStringJSP(espertoNew.getNome())%>
      </font></td>
    </tr>
  </table>
  <br>

  <table cellspacing=2 cellpadding=2>
<%
    if (magistratoOld.getCodMagistrato() != ""
        || espertoOld.getIdEsperto() != null)
        {%>
       <tr>
        <td class="l">
         Procedimento assegnato dal Magistrato <font class="campo">
         <%=magistratoOld.getCognome()%>&nbsp;<%=magistratoOld.getNome()%>
         <%=espertoOld.getCognome()%>&nbsp;<%=espertoOld.getNome()%>
         </font>
         al Magistrato <font class="campo">
         <%=magistratoNew.getCognome()%>&nbsp;<%=magistratoNew.getNome()%>
         <%=espertoNew.getCognome()%>&nbsp;<%=espertoNew.getNome()%>
         </font>
        </td>
       </tr>
       <%}%>
  </table>

  <%
  String lAzione;
  // Azione da chiamare per l'inserimento dei dati.
  if (acdest.compareTo("")!= 0 )
  {%>
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadDettaglioMagistratoRelatore">
      <table >
        <tr>
          <td>
            <input class="bottone"  type="submit" value="Ritorna">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=acdest%>" >
            <input type="HIDDEN" name="ritorno" value="SI" >
          </td>
        </tr>
      </table>
    </form>
  <%
  }%>

</body>
</html>