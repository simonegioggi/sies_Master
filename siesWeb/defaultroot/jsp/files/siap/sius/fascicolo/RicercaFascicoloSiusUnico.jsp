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
    <title>[S.I.E.S.] - Ricerca del Soggetto con Procedimento di Sorveglianza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti con Procedimenti di Sorveglianza</font></td>
<%
       FascicoloGPModel fascicoloUno = (FascicoloGPModel) fascicoli.get(0);
%>
      <!-- BOTTONE DI ISCRIZIONE NUOVO SOGGETTO -->
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadInserisciSoggetto" >
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Iscrizione Nuovo Soggetto" width="24" height="24" border="0">
        </a>
      </td>

      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>

   </tr>
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data di nascita</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">N° Fascicoli SIUS</td>
      <td class="int">Azioni</td>
    </tr>
<%
    Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
%>
    <tr>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getCognome()%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
      <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>
      <% if (fascicolo.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-")==0){%>
        <td class="l"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getDescComuneNascitaEstero()%> </td>
      <% }else {%>
        <td class="l"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%> (<%=fascicolo.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()%>)</td>
      <%}%>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getNumFascicoli()%></font></td>

      <td class="c">
          <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
            <jsp:param name="CampoIdEntita" value="IdSoggetto" />
            <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiusModel().getSogIdSoggetto()%>" />
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