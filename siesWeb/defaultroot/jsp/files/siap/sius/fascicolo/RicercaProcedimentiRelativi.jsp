<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>


<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>


<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco dei Procedimenti di Esecuzione della Misura Alternativa</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco dei Procedimenti di Esecuzione della Misura Alternativa </font></td>

<%
       FascicoloGPModel fascicoloUno = (FascicoloGPModel) fascicoli.get(0);
%>

    </tr>
     <tr> </tr>
     <tr> </tr>
  </table>

  <br>

  <table cellspacing=2 cellpadding=2>

    <tr>
      <td class="int">Numero Registro</td>
      <td class="int">Numero SIUS</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Misura da eseguire</td>
      <td class="int">Data sottoscrizione verbale/programma</td>
      <td class="int">Soggetto</td>
      <td class="int">Azioni</td>

    </tr>

<%
    Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
%>

<%

    while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
%>
      <tr>

       <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getAnnoS1()%>/<%=fascicolo.getGeneraleProcedimentoModel().getProgrS1()%>
       </font></td>

        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%>" Title="Dettaglio Procedimento SIUS">
            <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>
            /
            <%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>
          </a>

        </font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
<!-- genny 12/01/2004  utilizzo getDescrDefinizione per trasportare il contenuto di Misura da Eseguire-->
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
<!-- genny 12/01/2004 utilizzo setDataDefinizione come vettore per DATA_INIZIO_MISURA -->
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataDefinizione(),"dd-MM-yyyy"),"-")%></font></td>
		<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
     <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" />
           <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%>" />
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