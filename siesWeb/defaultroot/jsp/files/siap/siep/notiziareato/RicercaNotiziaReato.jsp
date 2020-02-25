<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel"%>
<%@ page import="siap.siep.notiziareato.action.ICostantiNotiziaReato"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Iterator" %>

<jsp:useBean id="elenconotiziareato" scope="request" class="java.util.Vector" />
<html>
	<head>
		<title>[S.I.A.P.] - GestioneNotiziaReato </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>

	<body class="corpo">
		<!-- INTESTAZIONE -->
		<FORM name="comandi" >
			<table>
    			<tr>
              <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			        <td class="LBG">
				        <font class="label">Funzione :</font>&nbsp;
				        <font class="campo">Elenco Notizie di Reato</font>
			      	</td>

   				</tr>
 			</table>
		</FORM>
		<!-- Visualizzazione Dettagli Procedimento -->
		<br />
      	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    	<br />
		<div align=center>

			<table>
				<tr>
					<td class=titolo colspan=12>Notizie di Reato</td>
				</tr>
				<tr>
				    <td class="int">Data Pervenimento</td>
				    <td class="int">Acquisione Diretta</td>
				    <td class="int">Data Fatto</td>
				    <td class="int">Fonte</td>
				    <td class="int">Numero Reg Autorità</td>
				    <td class="int">Luogo Provenienza</td>
				    <td class="int">Data Acquisizione</td>
				    <td class="int">Numero Ricevuta</td>				    			    
				    <td class="int">Data Arresto</td>				    				    
				    <td class="int">Data Fermo</td>
					<td class="int">Fotosegnalato</td>	
				    <td class="int">Azioni</td>				   
				</tr>

				<%
   				Iterator itx = elenconotiziareato.iterator();
  				while ( itx.hasNext())
  				{
					NotiziaReatoModel nreato = (NotiziaReatoModel)itx.next();
				%>
				<tr>

					<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nreato.getDataPervenimento(), "dd-MM-yyyy") )%>&nbsp;</td>
					<td class="l"><%=StringUtils.toStringJSP(nreato.getAcquisizioneDiretta())%>&nbsp;</td>
					<td class="l" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(nreato.getDataFatto(), "dd-MM-yyyy") )%>&nbsp;</td>

					<!-- Controllo valori dei campi Descrizione Fonte e Comune Fonte  -->
					<%
					String fonte = "";
					if(!StringUtils.toStringJSP(nreato.getDescrizioneFonte()).equals("") && !nreato.getDescrComuneFonte().equals("-"))
					{
						fonte = StringUtils.toStringJSP(nreato.getDescrizioneFonte())+"&nbsp"+StringUtils.toStringJSP(nreato.getDescrComuneFonte());
					}
					else if (StringUtils.toStringJSP(nreato.getDescrizioneFonte()).equals("") && !nreato.getDescrComuneFonte().equals("-"))
					{
						fonte = StringUtils.toStringJSP(nreato.getDescrComuneFonte());
					}
					else
					{
						fonte = StringUtils.toStringJSP(nreato.getDescrizioneFonte())+"&nbsp";
					}
					%>
					<td class="l" nowrap><%=fonte%></td>
					<td class="l"><%=StringUtils.toStringJSP(nreato.getNumRegAutorita())%>&nbsp;</td>
					<td class="l"><%=StringUtils.toStringJSP(nreato.getLuogoProvenienza())%>&nbsp;</td>
					<td class="l" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(nreato.getDataAcquisizione(), "dd-MM-yyyy") )%>&nbsp;</td>
					<td class="l"><%=StringUtils.toStringJSP(nreato.getNumeroRicevuta()) %>&nbsp;</td>
					<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nreato.getDataArresto(), "dd-MM-yyyy")) %>&nbsp;</td>
					<td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nreato.getDataFermo(), "dd-MM-yyyy")) %>&nbsp;</td>
					<td class="l"><%=StringUtils.toStringJSP(nreato.getFlagFotosegnalato()) %>&nbsp;</td>
					
					<td class="l">
						<!-- Inserimento bottoni per la visualizzione, la modifica e la cancellazione -->
        				<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           				<jsp:param name="CampoIdEntita" value="<%=ICostantiNotiziaReato.CAMPO_ID_NOTIZIA_REATO%>" />
						<jsp:param name="ValoreIdEntita" value="<%=nreato.getIdNotiziaReato()%>" />
        				</jsp:include>

      				</td>
				</tr>
				<%
				}
				%>
			</table>

		</div>

	</body>
</html>