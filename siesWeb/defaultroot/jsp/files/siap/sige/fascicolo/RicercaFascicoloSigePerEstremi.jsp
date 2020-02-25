<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.LoadPaginazione" %>
<%@ page import="siap.web.PaginazioneModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String" />
<jsp:useBean id="codOggetto" scope="request" class="java.lang.String" />
<jsp:useBean id="codMagistrato" scope="request" class="java.lang.String" />
<jsp:useBean id="descrSezione" scope="request" class="java.lang.String" />
<jsp:useBean id="dataIscrizioneInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataIscrizioneFine" scope="request" class="java.lang.String" />
<jsp:useBean id="dataFinePendenza" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDefinizioneInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDefinizioneFine" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca  Procedimento SIGE per estremi </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento SIGE per Estremi Atto&nbsp;</font></td>

		  <!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
 	</table>
<%
    if(!(dataIscrizioneInizio.equals("") && dataIscrizioneFine.equals("") &&
    		 dataDefinizioneInizio.equals("") && dataDefinizioneFine.equals("") &&
    		 tipoAtto.equals("") && codOggetto.equals("")  &&
    		 codMagistrato.equals("") && descrSezione.equals("") &&
    		 tipoRito.equals("") && tipoRito.equals("-") && dataFinePendenza.equals("")   )  )
    {
%>
  	<table>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
      if(tipoAtto.length()>1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Tipo Atto : <%=tipoAtto%></td>
        </tr>
<%    }
			if(codOggetto.length()>1 )
			{%>
  			<tr>
    			<td class="lVerdeNB">Oggetto : <%=codOggetto%></td>
  			</tr>
<%    }
      if(codMagistrato.length()>1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Magistrato : <%=codMagistrato%></td>
        </tr>
<%    }
      if(descrSezione.length()>1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Sezione : <%=descrSezione%></td>
        </tr>
<%    }
      if(!(dataIscrizioneInizio.equals(""))||!(dataIscrizioneFine.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Iscrizione :&nbsp;&nbsp;
<%
          if(!(dataIscrizioneInizio.equals("")))
          {
%>
            Dal <%=dataIscrizioneInizio%>&nbsp;&nbsp;
<%        }
          if(!(dataIscrizioneFine.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataIscrizioneFine%>
            </td>
<%        }
       %></tr><%
      }
      if(!(dataDefinizioneInizio.equals(""))||!(dataDefinizioneFine.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Definizione :&nbsp;&nbsp;
<%
          if(!(dataDefinizioneInizio.equals("")))
          {
%>
            Dal <%=dataDefinizioneInizio%>&nbsp;&nbsp;
<%        }
          if(!(dataDefinizioneFine.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataDefinizioneFine%>
            </td>
<%        }
       %></tr><%
      }
      if(!(dataFinePendenza.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti Pendenti al :&nbsp;&nbsp;<%=dataFinePendenza%></td>
				</tr>
		<%}
      if(tipoRito.length()>0 && tipoRito != "-" )
      {
%>
        <tr>
          <td class="lVerdeNB">Tipo Rito : <%=tipoRito%></td>
        </tr>
<%    }
      
	}
%>

  </table>

  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>


  <br>

  <table cellspacing=2 cellpadding=2>
    <tr>

      <td class="int">Numero SIGE</td>
      <td class="int">TipoAtto</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data di nascita</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Data Udienza</td>
      <td class="int">Data di Definizione</td>
      <td class="int">Azioni</td>
    </tr>
<%

    //Iterator itx = PagModel.getRisultatiRicerca().iterator();

    Iterator itx = fascicoli.iterator();
    String aDataIscrizione = "";
    String aDataNascita = "";
    String aDataDefinizione = "";
    String aDataUdienza = "";
    while ( itx.hasNext())
    {
      FascicoloSigeEstesoModel fascicolo = (FascicoloSigeEstesoModel)itx.next();
      aDataIscrizione = (DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy") : "-";
      aDataNascita = (DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy") : "-";
      aDataDefinizione = (DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataDefinizione(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataDefinizione(),"dd-MM-yyyy") : "-";
			if (fascicolo.getUdienzaProcedimento()==null)
				aDataUdienza="-";
			else
      	aDataUdienza = (DateUtils.getDateToString(fascicolo.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy") != null) ? DateUtils.getDateToString(fascicolo.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy") : "-";
%>
      <tr>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSige().getChiaveAnno()%>/<%=fascicolo.getFascicoloSige().getChiaveProgr()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getRichiestaSige().getDescrTipoAtto()%> </font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognome()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getNome()%></font></td>
        <td class="c"><font class="label"><%=aDataNascita%></font></td>
        <td class="c"><font class="label"><%=aDataIscrizione%></font></td>
        <td class="c"><font class="label"><%=aDataUdienza%></font></td>
        <td class="c"><font class="label"><%=aDataDefinizione%></font></td>
        <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>" />
           <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSige().getIdFascicoloSige()%>" />
        </jsp:include>
        </td>
      </tr>
<%
  }
%>
    </table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  </FORM>
  <br>

  </body>
</html>