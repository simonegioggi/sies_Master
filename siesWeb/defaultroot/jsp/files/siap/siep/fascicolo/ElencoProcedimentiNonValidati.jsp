<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimenti Non Validati</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti Non Validati</font></td>
    </tr>
  </table>
  <br>
		<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
	<br>
  <table width=100% >
    <tr>
      <td class="int">Numero SIEP</td>
      <td class="int">Cognome e Nome</td>
      <td class="int">Luogo e Data Nasicta</td>
      <td class="int">Azioni</td>
    </tr>

<%
    Iterator itx = fascicoli.iterator();
    while ( itx.hasNext())
    {
      FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
%>
    <tr>

      <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
<%    if (fascicolo.getSoggetto() != null)
      { %>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognome() +" " +fascicolo.getSoggetto().getNome()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getDescrComuneNascita() +" " + DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>

<%    }
      else
      { %>
        <td class="c">&nbsp;</td>
        <td class="c">&nbsp;</td>

<%    } %>
      <td class="c">
      <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
           <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getIdFascicoloSiep()%>" />
           <jsp:param name="FlagValidato" value="<%=fascicolo.getFlagValidato()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>