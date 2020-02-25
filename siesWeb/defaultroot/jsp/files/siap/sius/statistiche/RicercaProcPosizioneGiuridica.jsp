<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.RedirectTo" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel" %>
<%@ page import="siap.sius.statistiche.model.EveFasGepSogCancModel" %>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="ricercaModel" scope="session" class="siap.sius.statistiche.model.RicercaProcedimentoModel" />
<jsp:useBean id="elencoProcedimenti" scope="request" class="java.util.ArrayList" />

<jsp:useBean id="cancelleria_assegnataria" scope="request" class="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel" />
<jsp:useBean id="TornaQui"  scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca  Procedimento per Posizione Giuridica</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti per Posizione Giuridica</font></td>
      
      <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
			<td class=l>
				<a class="cliccabile" 
					 href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActRicercaProcPosizioneGiuridicaExcel">
					<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
				</a>
			</td>
      
    </tr>
    
  </table>

  <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<% 
      if(ricercaModel.getDescrMagistrato() != null ) {
%>
        <tr>
          <td class="lVerdeNB">Magistrato : <%=ricercaModel.getDescrMagistrato()%></td>
        </tr>
<%    } else { %>
	
				<tr>
					<td class="lVerdeNB">Magistrato : Tutti </td>
				</tr>
<% 			
			}

      if(ricercaModel.getCodPosizioneGiuridica() != null ){
%>
        <tr>
          <td class="lVerdeNB">Posizione Giuridica : <%=ricercaModel.getDescrPosizioneGiuridica()%></td>
        </tr>
<%    }
      
      if(ricercaModel.getCodOggettoProcedimento() != null ){
%>
        <tr>
          <td class="lVerdeNB">Tipo Atto : <%=ricercaModel.getDescrOggettoProcedimento()%></td>
        </tr>
<%    }
      
      if(ricercaModel.getDataFinePendenza() != null){
%>
        <tr>
          <td class="lVerdeNB">Procedimenti Pendenti al :&nbsp;<%=DateUtils.getDateToString(ricercaModel.getDataFinePendenza(),"dd-MM-yyyy")%>&nbsp;&nbsp;
        </tr>
<%    }
      
     if(ricercaModel.getDataIscrizioneInizio() != null || ricercaModel.getDataIscrizioneFine() != null ){
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Iscrizione :&nbsp;&nbsp;
<%        
				if(ricercaModel.getDataIscrizioneInizio() != null) { 
%>
            Dal <%=DateUtils.getDateToString(ricercaModel.getDataIscrizioneInizio(), "dd-MM-yyyy")%>&nbsp;&nbsp;
<%        
					if(ricercaModel.getDataIscrizioneFine() != null) {
%>
           &nbsp;Al&nbsp;&nbsp;<%=DateUtils.getDateToString(ricercaModel.getDataIscrizioneFine(), "dd-MM-yyyy")%>
           </td>
<%        
		  		}
      	}
     }
%>
		</tr>
<%
      if ( ricercaModel.getCodCancelleria() != null &&  ricercaModel.getCodCancelleria().compareTo("-") != 0 ) {
%>
        <tr>
        	<td class="lVerdeNB">Cancelleria Assegnataria :&nbsp;&nbsp; <%=ricercaModel.getDescrCancelleria()%></td>
        </tr>
<%
      } 
%>
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Data Udienza</td>
      <td class="int">Data Arr.Canc.</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Data Definizione</td>
      <td class="int">Contenuto</td>
      <td class="int">Cognome Nome</td>
      <td class="int">Azioni</td>
    </tr>
<%
    Iterator itx = elencoProcedimenti.iterator();
    while ( itx.hasNext()) {
        EveFasGepSogCancModel procedimento = (EveFasGepSogCancModel)itx.next();
%>
      <tr>
        <td class="l">
        	<font class="label">
        		<%=procedimento.getFascicoloSius().getChiaveAnno()%>/<%=procedimento.getFascicoloSius().getChiaveProgr()%>
        	</font>
        </td>
        
        <td class="c">
	        <font class="label">
<%      		if (procedimento.getGeneraleProcedimento().getDataCameraConsiglio() == null )
	        	{%>-<%}
	        	else{%>
	        		<%=DateUtils.getDateToString(procedimento.getGeneraleProcedimento().getDataCameraConsiglio(),"dd-MM-yyyy")%>
	        	<%}%>
	        </font>
        </td>
        
        <td class="c">
        	<font class="label">
	<%      	if (Utils.isNullObj(procedimento.getGeneraleProcedimento().getDataArrivoCancelleria() ) )
	        	{%>-<%}
 	       		else{%>
 		       		<%=DateUtils.getDateToString(procedimento.getGeneraleProcedimento().getDataArrivoCancelleria(),"dd-MM-yyyy")%>
 		        <%}%>
        	</font>
        </td>
        
        <td class="c">
        	<font class="label">
        		<%=DateUtils.getDateToString(procedimento.getFascicoloSius().getDataIscrizione(),"dd-MM-yyyy")%>
        	</font>
        </td>
        
        <td class="c">
        	<font class="label">
						<% if (procedimento.getFascicoloSius().getDataDefinizione()!= null){ %> 
							<%=DateUtils.getDateToString(procedimento.getFascicoloSius().getDataDefinizione(),"dd-MM-yyyy")%>
						<%} else {%>
							<%="-"%>
						<%} %>	
        	</font>
        </td>

        <td class="l">
        	<font class="label">
        		<%=procedimento.getGeneraleProcedimento().getDescrOggettoProcedimento()%>
        	</font>
        </td>
        
        <td class="c">
        	<font class="label">
        		<%=procedimento.getFascicoloSius().getSoggetto().getCognome()%>&nbsp;<%=procedimento.getFascicoloSius().getSoggetto().getNome()%> 
        	</font>
        </td>

        <td class="c">
	      <% 
	      	RedirectTo lRedirect = new RedirectTo();
					lRedirect.setPage(IWebConstants.PG_MAIN);
					lRedirect.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
					lRedirect.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,""+procedimento.getFascicoloSius().getIdFascicoloSius());							
					if((TornaQui != null) && TornaQui.trim().length() > 1) 
						lRedirect.setParameter("TornaQui", TornaQui);	    	  	
	      %>
					<a href="<%=lRedirect.toString()%>">
       			<img src="/images/dettagli.gif" alt="Dettaglio Provvedimento" width="12" height="12" border="0">
        	</a>
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