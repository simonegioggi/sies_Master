<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.permesso.action.ICostantiPermesso" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="ufOTribunale" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeRigettati" scope="request" class="java.lang.String" />
<jsp:useBean id="codPermesso" scope="request" class="java.lang.String" />
<jsp:useBean id="descrPermesso" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDalInCancelleria" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAlInCancelleria" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Soggetti con Permessi </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti con Procedimenti di Sorveglianza relativi a Permessi</font></td>

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
<%
       FascicoloGPModel fascicoloUno = (FascicoloGPModel) fascicoli.get(0);
%>
   </tr>
  </table>

  <br>

  <table cellspacing="2" cellpadding="2">
<%
  if(!(codDistretto.equals("") && ufOTribunale.equals("") &&
     lIncludeRigettati.equals("") && codPermesso.equals("-") &&
     (dataDalInCancelleria.equals("")) && (dataAlInCancelleria.equals("")) ))
  {
%>
   <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
   </tr>
<%
    if(codDistretto.length()==1 )
    {
%>
     <tr>
        <td class="lVerdeNB">Visualizzazione di tutti i permessi, compresi quelli pervenuti da altri Distretti</td>
     </tr>
<%  }

    if(!codDistretto.equals("") && (codDistretto.length()>1) )
    {
%>
     <tr>
        <td class="lVerdeNB">Visualizzazione dei permessi dell'intero Distretto</td>
     </tr>
<%  }
    else if(!ufOTribunale.equals(""))
    {
      if (tipoUfficio.equals("UDS"))
      {
%>
       <tr>
          <td class="lVerdeNB">Anche i permessi del Tribunale</td>
       </tr>
<%
      }
      else if (tipoUfficio.equals("TDS"))
      {
%>
       <tr>
          <td class="lVerdeNB">Anche i permessi dell' Ufficio (Sede)</td>
       </tr>
<%    }
    }
    if(!lIncludeRigettati.equals(""))
    {
%>
     <tr>
        <td class="lVerdeNB">Anche i permessi rigettati</td>
     </tr>
<%  }
    if(!codPermesso.equals("-"))
    {
%>
     <tr>
        <td class="lVerdeNB">Tipo : <%=descrPermesso%></td>
     </tr>
<%  }
    if(!(dataDalInCancelleria.equals(""))||!(dataAlInCancelleria.equals("")))
    {
%>
      <tr>
        <td class="lVerdeNB">Permessi con Data emissione :&nbsp;&nbsp;
<%
        if(!(dataDalInCancelleria.equals("")))
        {
%>
          Dal <%=dataDalInCancelleria%>&nbsp;&nbsp;
<%      }
        if(!(dataAlInCancelleria.equals("")))
        {
%>
          &nbsp;Al&nbsp;&nbsp;<%=dataAlInCancelleria%>
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
      <td class="int">N° Provvedimenti</td>
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
      <td class="c"><font class="label">
<%      if ((fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita())==null)
        {%>-<%}
        else
        {%>
          <%=DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy")%>
        <%}%>
        </font></td>
      <% if (fascicolo.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-")==0){%>
        <td class="l"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getDescComuneNascitaEstero()%> </td>
      <% }else {%>
        <td class="l"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%> (<%=fascicolo.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()%>)</td>
      <%}%>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getNumFascicoli()%></font></td>

      <td class="c">
          <%--jsp:include page="<%=IWebConstants.PG_BUTTONS%>"--%>
          <%-- Settaggio dei criteri di ricerca. --%>
          <jsp:include page="<%=ICostantiPermesso.PG_BUTTONS_PERMESSI%>">
            <jsp:param name="CampoIdEntita" value="IdSoggetto" />
            <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiusModel().getSogIdSoggetto()%>" />
            <jsp:param name="hufUtConnesso" value="<%=ufUtConnesso%>" />
            <jsp:param name="hufOTribunale" value="<%=ufOTribunale%>" />
            <jsp:param name="hcodDistretto" value="<%=codDistretto%>" />
            <jsp:param name="hlIncludeRigettati" value="<%=lIncludeRigettati%>" />
            <jsp:param name="hcodPermesso" value="<%=codPermesso%>" />
            <jsp:param name="hdescrPermesso" value="<%=descrPermesso%>" />
            <jsp:param name="htipoUfficio" value="<%=tipoUfficio%>" />
            <jsp:param name="hdataDalInCancelleria" value="<%=dataDalInCancelleria%>" />
            <jsp:param name="hdataAlInCancelleria" value="<%=dataDalInCancelleria%>" />
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