<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--@ page import="java.util.Date" --%>
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
<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="ufOTribunale" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeArchiviati" scope="request" class="java.lang.String" />
<jsp:useBean id="codContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="descrContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDalInCancelleria" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAlInCancelleria" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Soggetti con Procedimenti di Sorveglianza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti con Procedimenti di Sorveglianza</font></td>

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
       FascicoloGPModel fascicoloUno = (FascicoloGPModel) fascicoli.get(0);
%>
   </tr>
  </table>

  <br>

  <table cellspacing="2" cellpadding="2">
<%
  if(!(codDistretto.equals("") && ufOTribunale.equals("") &&
     lIncludeArchiviati.equals("") && codContenuto.equals("-") &&
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
        <td class="lVerdeNB">Visualizzazione di tutti i procedimenti, compresi quelli pervenuti da altri Distretti</td>
     </tr>
<%  }

    if(!codDistretto.equals("") && (codDistretto.length()>1) )
    {
%>
     <tr>
        <td class="lVerdeNB">Visualizzazione dei procedimenti dell'intero Distretto</td>
     </tr>
<%  }
    else if(!ufOTribunale.equals(""))
    {
      if (tipoUfficio.equals("UDS"))
      {
%>
       <tr>
          <td class="lVerdeNB">Anche i procedimenti del Tribunale</td>
       </tr>
<%
      }
      else if (tipoUfficio.equals("TDS"))
      {
%>
       <tr>
          <td class="lVerdeNB">Anche i procedimenti dell' Ufficio (Sede)</td>
       </tr>
<%    }
    }
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
    if(!codContenuto.equals("-"))
    {
%>
     <tr>
        <td class="lVerdeNB">Solo i procedimenti di : <%=descrContenuto%></td>
     </tr>
<%  }
    if(!(dataDalInCancelleria.equals(""))||!(dataAlInCancelleria.equals("")))
    {
%>
      <tr>
        <td class="lVerdeNB">Procedimenti con Data arrivo in cancelleria :&nbsp;&nbsp;
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
      <td class="int">Paternità</td>
      <td class="int">Codice CUI</td>
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
        <td class="c"><font class="label">
<%      if ((fascicolo.getFascicoloSiusModel().getSoggetto().getPaternita()==null) || 
			(fascicolo.getFascicoloSiusModel().getSoggetto().getPaternita().compareTo("")==0))
        {%>-<%}
        else
        {%>
          <%=fascicolo.getFascicoloSiusModel().getSoggetto().getPaternita()%>
        <%}%>
        </font></td>
        <td class="c"><font class="label">
<%      if ((fascicolo.getFascicoloSiusModel().getSoggetto().getCodAfis()==null) || 
			(fascicolo.getFascicoloSiusModel().getSoggetto().getCodAfis().compareTo("")==0))
        {%>-<%}
        else
        {%>
          <%=fascicolo.getFascicoloSiusModel().getSoggetto().getCodAfis()%>
        <%}%>
        </font></td>
      
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getNumFascicoli()%></font></td>

      <td class="c">
          <%--jsp:include page="<%=IWebConstants.PG_BUTTONS%>"--%>
          <%-- Settaggio dei criteri di ricerca. --%>
          <jsp:include page="<%=ICostantiFascicoloSius.PG_BUTTONS_PROVVEDIMENTI%>">
            <jsp:param name="CampoIdEntita" value="IdSoggetto" />
            <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiusModel().getSogIdSoggetto()%>" />
            <jsp:param name="dCognome" value="<%=fascicolo.getFascicoloSiusModel().getSoggetto().getCognome()%>" />
            <jsp:param name="dNome" value="<%=fascicolo.getFascicoloSiusModel().getSoggetto().getNome()%>" />
            <jsp:param name="dDataNascita" value="<%=DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy")%>" />
            <jsp:param name="hufUtConnesso" value="<%=ufUtConnesso%>" />
            <jsp:param name="hufOTribunale" value="<%=ufOTribunale%>" />
            <jsp:param name="hcodDistretto" value="<%=codDistretto%>" />
            <jsp:param name="hlIncludeArchiviati" value="<%=lIncludeArchiviati%>" />
            <jsp:param name="hcodContenuto" value="<%=codContenuto%>" />
            <jsp:param name="hdescrContenuto" value="<%=descrContenuto%>" />
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