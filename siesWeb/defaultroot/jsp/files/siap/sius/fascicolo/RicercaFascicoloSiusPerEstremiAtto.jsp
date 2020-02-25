<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>


<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="dataIscrizioneInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataIscrizioneFine" scope="request" class="java.lang.String" />
<jsp:useBean id="dataArrivoInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataArrivoFine" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAttoInizio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAttoFine" scope="request" class="java.lang.String" />
<jsp:useBean id="codAtto" scope="request" class="java.lang.String" />
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="statoProcedimento" scope="request" class="java.lang.String" />
<jsp:useBean id="codMagistrato" scope="request" class="java.lang.String" />

<jsp:useBean id="cancelleria_assegnataria" scope="request" class="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel" />
<jsp:useBean id="filtroCollaboratore" scope="request" class="java.lang.String" />

<jsp:useBean id="dataDefinizioneIniziale" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDefinizioneFinale" scope="request" class="java.lang.String" />
<jsp:useBean id="dataFinePendenza" scope="request" class="java.lang.String" />

<%@page import="f3b.util.Utils"%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca  Procedimento per Estremi Atto</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti per Estremi Atto</font></td>
      
      <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
			<td class=l>
				<a class="cliccabile" 
					 href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActRicercaProcedimentoPerEstremiExcel">
					<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
				</a>
			</td>
      
    </tr>
    
  </table>

  <table cellspacing=2 cellpadding=2>
<%
    if( !(dataIscrizioneInizio.equals("") && dataIscrizioneFine.equals("") &&
        	dataDefinizioneIniziale.equals("") && dataDefinizioneFinale.equals("") && dataFinePendenza.equals("") &&
       		dataArrivoInizio.equals("") && dataArrivoFine.equals("") &&
       		dataAttoInizio.equals("") && dataAttoFine.equals("") &&
       		codAtto.equals("") && codMagistrato.equals("") &&
       		descrUfficio.equals("") && statoProcedimento.equals("")    )  || (cancelleria_assegnataria != null &&   cancelleria_assegnataria.getDescCancelleriaAssegnataria().trim().length()>0) || (filtroCollaboratore != null && filtroCollaboratore.trim().length() >0) )
    {
%>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
      if(descrUfficio.length()>1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Ufficio : <%=descrUfficio%></td>
        </tr>
<%    }
      if(codMagistrato.length()>0 )
      {
%>
        <tr>
          <td class="lVerdeNB">Magistrato : <%=codMagistrato%></td>
        </tr>
<%    }
      if(codAtto.length()>1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Tipo Atto : <%=codAtto%></td>
        </tr>
<%    }
      if(!(dataFinePendenza.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti Pendenti al :&nbsp;<%=dataFinePendenza%>&nbsp;&nbsp;
        </tr>
<%    }
      if(!(dataDefinizioneIniziale.equals(""))||!(dataDefinizioneFinale.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti Definiti :&nbsp;&nbsp;
<%
          if(!(dataDefinizioneIniziale.equals("")))
          {
%>
            Dal <%=dataDefinizioneIniziale%>&nbsp;&nbsp;
<%        }
          if(!(dataDefinizioneFinale.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataDefinizioneFinale%>
            </td>
<%        }
       %></tr><%
      }
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
      if(!(dataAttoInizio.equals(""))||!(dataAttoFine.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Atto :&nbsp;&nbsp;
<%
          if(!(dataAttoInizio.equals("")))
          {
%>
            Dal <%=dataAttoInizio%>&nbsp;&nbsp;
<%        }
          if(!(dataAttoFine.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataAttoFine%>
            </td>
<%        }
       %></tr><%
      }
      if(!(dataArrivoInizio.equals(""))||!(dataArrivoFine.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Arrivo in cancelleria :&nbsp;&nbsp;
<%
          if(!(dataArrivoInizio.equals("")))
          {
%>
            Dal <%=dataArrivoInizio%>&nbsp;&nbsp;
<%        }
          if(!(dataAttoFine.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataArrivoFine%>
            </td>
<%        }
       %></tr><%
      }
      if ((cancelleria_assegnataria != null &&   cancelleria_assegnataria.getDescCancelleriaAssegnataria().trim().length() > 0) )
      {
%>
        			<tr>
          				<td class="lVerdeNB">Cancelleria Assegnataria :&nbsp;&nbsp; <%=cancelleria_assegnataria.getDescCancelleriaAssegnataria()%>&nbsp;<%=cancelleria_assegnataria.getDescrUfficio()%> </td>
        			</tr>
<%
       }
    	  else if ((cancelleria_assegnataria != null && cancelleria_assegnataria.getCodCancelleriaAssegnataria() != null
    			     && cancelleria_assegnataria.getCodCancelleriaAssegnataria().compareTo("nocanc") == 0 ))
       {
    		  
    		  %>
    		          <tr>
    		            <td class="lVerdeNB">Privi di Cancelleria Assegnataria </td>
    		          </tr>
    		  <%
    	}
      
      if (filtroCollaboratore != null && filtroCollaboratore.trim().length() > 0) 
      {
    	if (filtroCollaboratore.equalsIgnoreCase("SI"))
    	{ 
%>
        <tr>
          <td class="lVerdeNB">Collaboratore di Giustizia  :&nbsp;&nbsp; Procedimento collegato a Collaboratore</td>
        </tr>
<%
        } 
    	else if (filtroCollaboratore.equalsIgnoreCase("NO") )
      {
%>
        <tr>
         <td class="lVerdeNB">Collaboratore di Giustizia  :&nbsp;&nbsp; Procedimento non collegato a Collaboratore</td>
        </tr>
<%
      }
    }
  }
%>
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table cellspacing=2 cellpadding=2>
    <tr>

      <td class="int">Numero SIUS</td>
      <%-- %>td class="int">Descr. Ufficio</td--%>
      <td class="int">Data Udienza</td>
      <%--td class="int">Data Atto</td--%>
      <td class="int">Data Arr.Canc.</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Data Definizione</td>
      <td class="int">Contenuto</td>
      <%--td class="int">Cognome</td>
      <td class="int">Nome</td--%>
      <td class="int">Cognome Nome</td>
      <%-->td class="int">Data di nascita</td--%>
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
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%></font></td>
        <%--td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%> / <%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%></font></td--%>
        <td class="c"><font class="label">
<%      if (Utils.isNullObj(fascicolo.getGeneraleProcedimentoModel().getDataCameraConsiglio() ) )
        {%>-<%}
        else{%>
        	<%=DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd-MM-yyyy")%>
        <%}%>
        </font></td>
        <td class="c"><font class="label">
<%--      if (fascicolo.getGeneraleProcedimentoModel().getDataRichiesta() == null )
        {%>-<%}
        else{%>
          <%=DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy")%>
        <%}--%>
<%      if (Utils.isNullObj(fascicolo.getGeneraleProcedimentoModel().getDataArrivoCancelleria() ) )
        {%>-<%}
        else{%>
        	<%=DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd-MM-yyyy")%>
        <%}%>
        </font></td>
        <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getDataInserimento(),"dd-MM-yyyy")%></font></td>
        <td class="c"><font class="label">
<%      if (!(Utils.isNullObj(fascicolo.getFascicoloSiusModel().getDataDefinizione() ) ) )
				{%><%=DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getDataDefinizione(),"dd-MM-yyyy")%>
        <%}else if (!(Utils.isNullObj(fascicolo.getFascicoloSiusModel().getDataDefinizioneFinale() ) ) ){%>
        	<%=DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getDataDefinizioneFinale(),"dd-MM-yyyy")%>
        <%}else{%>-<%} %>
        </font></td>

        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
        <%--td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getCognome()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getNome()%></font></td--%>
<%
				String lDataNascita =" -";
        if (fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita() != null )
        	lDataNascita = DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy");
%>
        <td class="c"><font class="label" title='Data Nascita : <%=lDataNascita%>' ><%=fascicolo.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getSoggetto().getNome()%> </font></td>
        <%--td class="c"><font class="label">
<%      if (fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita() == null )
        {
%>        &nbsp;-
<%      }
        else
        {
%>
          <%=DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy")%>
<%
        }%>
        </font></td --%>

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