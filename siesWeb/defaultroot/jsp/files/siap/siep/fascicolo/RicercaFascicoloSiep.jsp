<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="flagDettaglio" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="LabelFunzioneNonValidati" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimento</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class=label>Funzione :</font>&nbsp;
      	<font class="campo">Elenco Procedimenti <%=LabelFunzioneNonValidati%></font>
      </td>
    </tr>
  </table>

  <br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

<br>
<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
<%if(flagDettaglio.equals("S")){%>
     <!--è state effettuata una ricerca per data-->
      <!--td class="int">Data Titolo Esecutivo</td-->
      <!--td class="int">Tipo Titolo Esecutivo</td-->
      <td class="int" width ="15%">Data di Iscrizione</td>
      <td class="int">Numero SIEP</td>

<%}else{%>
			<td class="int">Numero SIEP</td>
      <!--td class="int">Data Titolo Esecutivo</td-->
      <!--td class="int">Tipo Titolo Esecutivo</td-->
      <td class="int" width ="15%">Data di Iscrizione</td>

<%}%>
      <td class="int">Soggetto</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <!--td class="int">Luogo Autorità</td-->
      <td class="int">Data Irrevocabilità</td>
     <td class="int">Stato del Procedimento</td>
      <td class="int">Azioni</td>
    </tr>

<%
    Iterator itx = fascicoli.iterator();
    while ( itx.hasNext())
    {
      FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
%>
    <tr>

<%if(flagDettaglio.equals("S")){%>
     <!--è state effettuata una ricerca per data-->
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>

<%}else{%>
      <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>

<%}%>
<%    if (fascicolo.getSoggetto() != null)
      { %>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognome() +" " +fascicolo.getSoggetto().getNome()%></font></td>
<%    }
      else
      { %>
        <td class="c">&nbsp;</td>
<%    } %>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%--td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
      	<%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
      <td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoAutoritaEmittente()%></font> di <font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td>
      	<%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoProcedimento())%>&nbsp;</font></td>

      <td class="c">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
           <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
           <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
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