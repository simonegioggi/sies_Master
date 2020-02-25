<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />

<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Soggetti con Procedimenti SIGE</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti con Procedimenti SIGE</font></td>

<%	if(tipoUfficio.substring(1,3).compareTo("DS")==0)
	{%>
      <!-- BOTTONE DI ISCRIZIONE NUOVO SOGGETTO -->
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadInserisciSoggetto" >
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Iscrizione Nuovo Soggetto" width="24" height="24" border="0">
        </a>
      </td>
<%	}%>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<%
       FascicoloSigeEstesoModel fascicoloUno = (FascicoloSigeEstesoModel) fascicoli.get(0);
%>
   </tr>
  </table>

  <br>

  <table cellspacing="2" cellpadding="2">
<%
  if(!(codDistretto.equals("") ))
  {
%>
   <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
   </tr>
<%
    if(codDistretto.compareTo("1") == 0 )
    {
%>
     <tr>
        <td class="lVerdeNB">Visualizzazione dei procedimenti dell'intero Distretto</td>
     </tr>
<%   }
  }
%>
  </table>
  <br>

  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data di nascita</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">N° Fascicoli SIGE</td>
      <td class="int">Azioni</td>
    </tr>
<%
    Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      FascicoloSigeEstesoModel fascicolo = (FascicoloSigeEstesoModel)itx.next();
%>
    <tr>
      <td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognome()%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getSoggetto().getNome()%></font></td>
      <td class="c"><font class="label">

<%      if ((fascicolo.getSoggetto().getDataNascita())==null)
        {%>-<%}
        else
        {%>
          <%=DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy")%>
        <%}%>
        </font></td>
      <% if (fascicolo.getSoggetto().getDescrComuneNascita().compareTo("-")==0){%>
        <td class="l"><%=fascicolo.getSoggetto().getDescComuneNascitaEstero()%> </td>
      <% }else {%>
        <td class="l"><%=fascicolo.getSoggetto().getDescrComuneNascita()%> (<%=fascicolo.getSoggetto().getCodProvinciaNascita()%>)</td>
      <%}%>
	  <!-- Nel campo Note del Soggetto ho caricato il Numero totale dei fascicoli del Soggetto -->	
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSige().getNote()%></font></td>

      <td class="c">
          <%--jsp:include page="<%=IWebConstants.PG_BUTTONS%>"--%>
          <%-- Settaggio dei criteri di ricerca. --%>
          <jsp:include page="<%=ICostantiFascicoloSige.PG_BUTTONS_FASCICOLI%>">
            <jsp:param name="CampoIdEntita" value="IdSoggetto" />
            <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getSoggetto().getIdSoggetto()%>" />
            <jsp:param name="hufUtConnesso" value="<%=ufUtConnesso%>" />
            <jsp:param name="hcodDistretto" value="<%=codDistretto%>" />
          </jsp:include>
        </td>
      </tr>
<%  }%>
    </table>
  </FORM>
  <br>
  </body>
</html>