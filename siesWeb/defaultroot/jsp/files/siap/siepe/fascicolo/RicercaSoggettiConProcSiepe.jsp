<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siepe.fascicolo.model.FascicoloSoggAttModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeArchiviati" scope="request" class="java.lang.String" />
<jsp:useBean id="codIncarico" scope="request" class="java.lang.String" />
<jsp:useBean id="descrIncarico" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDal" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAl" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Soggetti con Procedimenti UEPE</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti con Procedimenti SIEPE</font></td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<%
       FascicoloSoggAttModel fascicoloUno = (FascicoloSoggAttModel) fascicoli.get(0);
%>
   </tr>
  </table>


  <table cellspacing="2" cellpadding="2">
<%
  if(!(lIncludeArchiviati.equals("") && codIncarico.equals("-") &&
     (dataDal.equals("")) && (dataAl.equals("")) ))
  {
%>
   <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
   </tr>
<%
    if(lIncludeArchiviati.equals("A"))
    {
%>
     <tr>
        <td class="lVerdeNB">Solo i procedimenti definiti</td>
     </tr>
<%  }
    else if(!lIncludeArchiviati.equals(""))
    {
%>
     <tr>
        <td class="lVerdeNB">Anche i procedimenti definiti</td>
     </tr>
<%  }
    if(!codIncarico.equals("-"))
    {
%>
     <tr>
        <td class="lVerdeNB">Solo i procedimenti di : <%=descrIncarico%></td>
     </tr>
<%  }
    if(!(dataDal.equals(""))||!(dataAl.equals("")))
    {
%>
      <tr>
        <td class="lVerdeNB">Procedimenti con Data Invio :&nbsp;&nbsp;
<%
        if(!(dataDal.equals("")))
        {
%>
          Dal <%=dataDal%>&nbsp;&nbsp;
<%      }
        if(!(dataAl.equals("")))
        {
%>
          &nbsp;Al&nbsp;&nbsp;<%=dataAl%>
        </td>
<%      }

    }
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
      <td class="int">N° Fascicoli SIEPE</td>
      <td class="int">Azioni</td>
    </tr>
<%
    Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      FascicoloSoggAttModel fascicolo = (FascicoloSoggAttModel)itx.next();
%>
    <tr>
      <td class="c"><font class="label"><%=fascicolo.getSoggettoModel().getCognome()%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getSoggettoModel().getNome()%></font></td>
      <td class="c"><font class="label">
<%      if ((fascicolo.getSoggettoModel().getDataNascita())==null)
        {%>-<%}
        else
        {%>
          <%=DateUtils.getDateToString(fascicolo.getSoggettoModel().getDataNascita(),"dd-MM-yyyy")%>
        <%}%>
        </font></td>
      <% if (fascicolo.getSoggettoModel().getDescrComuneNascita().compareTo("-")==0){%>
        <td class="l"><%=fascicolo.getSoggettoModel().getDescComuneNascitaEstero()%> </td>
      <% }else {%>
        <td class="l"><%=fascicolo.getSoggettoModel().getDescrComuneNascita()%> (<%=fascicolo.getSoggettoModel().getCodProvinciaNascita()%>)</td>
      <%}%>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiepeRicercaModel().getNumFascicoli()%></font></td>

      <td class="c">
          <%-- Settaggio dei criteri di ricerca. --%>
          <jsp:include page="<%=ICostantiFascicoloSiepe.PG_BUTTONS_PROCEDIMENTI%>">
            <jsp:param name="CampoIdEntita" value="IdSoggetto" />
            <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getSoggettoModel().getIdSoggetto()%>" />
            <jsp:param name="hufUtConnesso" value="<%=ufUtConnesso%>" />
            <jsp:param name="hlIncludeArchiviati" value="<%=lIncludeArchiviati%>" />
            <jsp:param name="hcodIncarico" value="<%=codIncarico%>" />
            <jsp:param name="hdescrIncarico" value="<%=descrIncarico%>" />
            <jsp:param name="htipoUfficio" value="<%=tipoUfficio%>" />
            <jsp:param name="hdataDal" value="<%=dataDal%>" />
            <jsp:param name="hdataAl" value="<%=dataDal%>" />
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