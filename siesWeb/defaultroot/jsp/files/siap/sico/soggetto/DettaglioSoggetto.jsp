<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggettodattilo.action.ICostantiSoggettoDattilo" %>
<%@ page import="siap.sico.soggettodattilo.model.SoggettoDattiloModel" %>
<%@ page import="siap.jms.jmscode.action.ICostantiJmsCode"%>
<%@ page import="siap.jms.jmscode.model.JmsCodeModel"%>
<%@ page import="f3b.security.model.ProfileModel" %>
<%@ page import="java.lang.String" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="UtenteConnesso" 	  scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" 			  scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="TornaQui" 			  scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="modificabile" 		  scope="request" class="java.lang.String" />
<jsp:useBean id="riferimentiDattilo"  scope="request" class="java.util.Vector" />
<jsp:useBean id="ListaBDI" 			  scope="request" class="java.util.Vector" />
<jsp:useBean id="TipoOperazione" 	  scope="request" class="java.lang.String" />
<jsp:useBean id="insertAllegato" 	  scope="request" class="java.lang.String" />

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Gestione Soggetto - Dettaglio</title>
<script language="JavaScript" src="/html/conferma.js"></script>

<script language="JavaScript">
function VisualizzaCertificato(lAzione) {
	var  hrefStampa = lAzione;
  	var lIndice = hrefStampa.indexOf("?");
  	var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
  	stampa2("/jsp/files/Stampa.jsp",  parametri);
}
</script>

<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if (!"".equals(lTipoFunzione) && !"ritornodettaglio".equals(lTipoFunzione)) {
%>
<script language="JavaScript">
var aForm = null;
function Verify() {
	alert("La funzione di Iscrizione Guidata è stata Interrotta");
	aForm=document.getElementById("Abbandona");
	Disabilita();
}

function Disabilita() {
	if (aForm == null)
		aForm = document.getElementById("Residenza");
    document.Abbandona.A.disabled = true;
    document.Residenza.R.disabled = true;
   	aForm.submit();
}
</script>
<%
}
%>
</head>

<BODY class="corpo" onload="radioBase();">
<FORM name="comandi" >
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
			<font class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Soggetto</font>
        </td>
<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if ("".equals(lTipoFunzione)) {
%>
		<td class="LBG">
			<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" />
				<jsp:param name="ValoreIdEntita" value="<%=soggetto.getIdSoggetto()%>" />
				<jsp:param name="Modificabile" value="<%=modificabile%>" />
				<jsp:param name="InsertAllegato" value="<%=insertAllegato%>" />
			</jsp:include>
		</td>
<%
	if (request.getParameter("ricerca") == null) {
%>
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>      
<%
	}
}
if (request.getParameter("ricerca") != null) {
%>
		<td class="LBG">         	
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActRicercaSoggettoCodCui&<%=ICostantiSoggetto.CAMPO_COD_AFIS%>=<%=soggetto.getCodAfis()%>">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>
		</td>
<%
}
%>
	</tr>
</table>
</FORM>
<table cellspacing=2 cellpadding=2>
	<tr>
      	<td class="l"><font class="label">Cognome e Nome</font></td>
      	<td class="l"><font class="campo"><%=soggetto.getCognome() %>&nbsp;&nbsp;<%=soggetto.getNome() %></font></td>
      	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%-- <td class="1">
	      	<a href="Javascript:VisualizzaCertificato('/jsp/Main.jsp?Action=siap.sico.webservice.action.ActLoadCertificatoSoggetto&IDSoggetto=<%//=soggetto.getIdSoggetto()%>')">
	        	<img src="/images/certificato.gif" width="103" height="23" alt="Certificato Penale" border="0">
	        </a>
		</td> --%>
	</tr>
    <tr>
      	<td class="l"><font class="label">Sesso</font></td>
      	<td class="l"><font class="campo"><%=soggetto.getSesso()%>&nbsp;</font></td>
    </tr>
	<tr>
      	<td class="l" width="25%"><font  class="label">Data di nascita</font></td>
      	<td class="l" width="25%">
      		<font class="campo">
<%
if (soggetto.getDataNascita() != null) {
%>
			<%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
} else {
%>
          	<%="**-"+StringUtils.toStringJSP(soggetto.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggetto.getAnnoNascita(), "****")%>&nbsp;
<%
}
%>
			</font>
		</td>
      	<td class="l" width="25%"><font class="label">Data Presunta</font></td>
      	<td class="l" width="25%"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%></font></td>
	</tr>

<%-- MERGE v10 COLLAUDO: modifica alla gestione del codice --%>
<%
// Si ricava il profilo dell'utente connesso
ProfileModel lProfilo = (ProfileModel)UtenteConnesso.getUserProfile();
UfficioModel lUfficio = UtenteConnesso.getUfficioUtente();
String codTipoUfficio = lUfficio.getCodTipoUfficio();
// MEV_57: esclusi anche sige minorenni
boolean isSigeMinorenni = lProfilo.isSige()
		&& ("CAPSM".equals(codTipoUfficio)
			|| "DIBM".equals(codTipoUfficio)
			|| "GIPM".equals(codTipoUfficio)
			|| "GUPM".equals(codTipoUfficio)
			|| "PMM".equals(codTipoUfficio));
if (!lProfilo.isSius() && !isSigeMinorenni) {
%>
	<tr>
		<td class="l"><font class="label">Età Presunta</font></td>
		<td class="l">
			<font class="campo">
<%
	if (soggetto.getEtaPresuntaAnni() != null) {
%> 
				<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;
			</font> anni
<%
		if (soggetto.getEtaPresuntaMesi() != null) {
%> 
	      	e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
<%
		}
	}
%>
			&nbsp;
		</td>
	</tr> 
<%
}

// Data commesso reato deve essere visibile solo per 
// gli utenti SIUS (Minorenni), no per gli utenti SIEP
// MEV_57: aggiunti anche 3 uffici sige minorenni
if ((!lProfilo.isSiep() && ("TDSM".equals(codTipoUfficio) || "UDSM".equals(codTipoUfficio))) || isSigeMinorenni) {
%> 
	<tr>
		<td class="l"><font class="label">Età Presunta</font></td>
		<td class="l">
			<font class="campo">
<%
	if (soggetto.getEtaPresuntaAnni() != null) {
%> 
				<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;
			</font> anni
<%
		if (soggetto.getEtaPresuntaMesi() != null){
%> 
	      	e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
<%
		}
	}
%>
			&nbsp;
		</td>
		<td class="l" width="25%"><font  class="label">Data commesso reato</font></td>
		<td class="l" width="25%">
			<font class="campo">
<%
	if (soggetto.getDataReatoSius() != null) {
%>
				<%=DateUtils.getDateToString(soggetto.getDataReatoSius(),"dd-MM-yyyy")%>&nbsp;
<%
	} else {
%>
				<%="**-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "**")+"-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "****")%>&nbsp;
<%
	}
%>
			</font>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td class="l"><font class="label">Comune Nascita</font></td>
		<td class="l">
			<font class="campo">
          		<%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita())%>
<%
if ((soggetto.getDescrComuneNascita() != null)
		&& (!(soggetto.getDescrComuneNascita().equals("")))
		&& (!(soggetto.getDescrComuneNascita().equals("-")))) {
%>
				(<%=soggetto.getCodProvinciaNascita()%>)
<%
}
%>
          		&nbsp;
			</font>
		</td>
    </tr>
	<tr>
		<td class="l"><font class="label">Stato Cittadinanza</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font></td>
		<td class="l"><font  class="label">Stato Nascita</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
	<tr>
		<td class="l"><font class="label">Comune Di Nascita Estero</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
    </tr>
	<tr>
      	<td class="l"><font class="label">Paternità</font></td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%>&nbsp;</font></td>
    </tr>
    <%-- Ticket#20210514018 - Iscrizione soggetto sige: splittati nome e cognome --%>
    <tr>
      	<td class="l"><font class="label">Cognome Madre</font></td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%></font>
      	</td>
    </tr>
    <tr>
      	<td class="l"><font class="label">Nome Madre</font></td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNomeMadre())%></font></td>
    </tr>
<!-- 	<tr> -->
<!--       	<td class="l"><font class="label">Nome Madre</font></td> -->
<!--       	<td class="l"> -->
<!--       		<font class="campo"> -->
<%--       			<%=StringUtils.toStringJSP(soggetto.getNomeMadre())%> --%>
<%--       			<%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%>&nbsp; --%>
<!--       		</font> -->
<!--       	</td> -->
<!--     </tr> -->
	<tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
		<td class="l"><font class="label">Codice Fiscale</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%>&nbsp;</font></td>
		<td class="l"><font class="label">Atto Nascita</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getAttoNascita()) %>&nbsp;</font></td>
    </tr>
	<tr>
       	<td class="l"><font class="label">Codice CUI</font></td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>&nbsp;</font></td>
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    	<%--
    	<td class="l"><font class="label">Codice Fascicolo Rosso</font></td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodCs())%>&nbsp;</font></td>
     	--%>
     	<td>&nbsp;</td><td>&nbsp;</td>
	</tr>
    <tr>
      	<td class="l"><font class="label">Note</font></td>
      	<td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNote())%>&nbsp;</font></td>
    </tr>
<%
boolean searchForm = false;
// La sezione sottostante deve essere visibile solo per
// gli utenti SIEP
if (lProfilo.isSiep()) {
// Viene visualizzato l'elenco dei Documenti allegati
// e la sezione per allegare i documenti
//if(TipoOperazione.equals("A")) {
%>	
	<tr><td colspan=4 class=l>&nbsp;</td></tr>
<%
	if (riferimentiDattilo != null && riferimentiDattilo.size() > 0) {
		Iterator itx = riferimentiDattilo.iterator();
%>
	<tr>
      	<td colspan="4">
			<table width="100%">
        		<tr>
        			<td colspan="4" class="Titolo" width="100%">Elenco Allegati</td>
        		</tr>
				<tr>
					<td class="int">Data</td>
					<td class="int">Operatore</td>
					<td class="int">Descrizione</td>
					<td class="int">Azioni</td>
				</tr>
<%
		while (itx.hasNext()) {
			SoggettoDattiloModel sdm = (SoggettoDattiloModel) itx.next();
%>
				<tr>
					<td class="c"><%=DateUtils.getDateToString(sdm.getDataInserimento(),"dd-MM-yyyy")%>&nbsp;</td>
					<td class="c"><%=StringUtils.toStringJSP(sdm.getCodOperatoreInserimento())%>&nbsp;</td>
					<td class="c"><%=StringUtils.toStringJSP(sdm.getDocTipoDesc())%>&nbsp;</td>
<%
   			String cancellabileSogDatt = "";
      		if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(sdm.getCodUfficioInserimento())) {
    	  		cancellabileSogDatt = "SI";
      		} else {
    	  		cancellabileSogDatt = "NO";
      		}
%>
					<td class=c>
						<jsp:include page="<%=ICostantiSoggettoDattilo.PG_BUTTONS_MORE_PARAMETERS%>">
							<jsp:param name="CampoIdSoggetto" value="<%=ICostantiSoggettoDattilo.CAMPO_COD_SOGGETTO%>" />
							<jsp:param name="ValoreIdSoggetto" value="<%=sdm.getCodSoggetto()%>" />
							<jsp:param name="CampoIdDattilo" value="<%=ICostantiSoggettoDattilo.CAMPO_ID_DATTILO%>" />
							<jsp:param name="ValoreIdDattilo" value="<%=sdm.getIdDattilo()%>" />
							<jsp:param name="Cancellabile" value="<%=cancellabileSogDatt%>" />
						</jsp:include>
					</td>
				</tr>
<%
		} // end while
%>
			</table>
      	</td>
	</tr>
<%
	} // end if (riferimentiDattilo!=null && riferimentiDattilo.size()>0)
	// L'elenco dei Documenti allegati viene sempre visualizzato
	// mentre la sezione per allegare i documenti viene visualizzata
	// solo quando si seleziona l'icona 'Allegare Documento' 
	if ("A".equals(TipoOperazione)) {
%>
    <tr>
		<td colspan="4">
			<FORM name="comandi" enctype="multipart/form-data" method="post">
			<table width="100%">
        		<tr>
        			<td colspan="2">&nbsp;</td>
        		</tr>
		        <tr>
		        	<td colspan="2" class="Titolo" width="100%">Allegare Documento</td>
		        </tr>
				<tr>
					<td class="l">Codice CUI <input type="radio" name="<%=ICostantiSoggettoDattilo.CAMPO_DOC_TIPO%>" checked value="A"></td>
					<td class="l">Cartellino Fotosegnaletico <input type="radio" name="<%=ICostantiSoggettoDattilo.CAMPO_DOC_TIPO%>" value="B"></td>
				</tr>
				<tr>
					<td class="l">Indica il percorso locale del documento<br>riferimenti dattiloscopici da salvare</td>
					<td class="l">
						<font class="campo"><input type=file size="35" name="<%=ICostantiSoggettoDattilo.CAMPO_BLOB%>"></font>
					</td>
				</tr>
				<tr>
					<td class="l" colspan="2">
			            <input  class=bottone  type="submit" value="Conferma">
			            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.soggettodattilo.action.ActUploadSoggettoDattilo">
			            <input type="HIDDEN" name="<%=ICostantiSoggettoDattilo.CAMPO_COD_SOGGETTO%>" value="<%=soggetto.getIdSoggetto()%>">
					</td>
				</tr>
			</table>
			</FORM>
		</td>
	</tr>
<%
	} // enf if(TipoOperazione.equals("A"))
	// lTipoFunzione per capire che si proviene da iscrizione guidata
	if (!"".equals(lTipoFunzione) && !"ritornodettaglio".equals(lTipoFunzione)) {
%>
	<tr>
		<td class="lNoBord" colspan="2">
			<FORM method="POST" name="Residenza" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.residenza.action.ActLoadInserisciResidenza&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>">
      			<br><INPUT class="bottone" type="button" name="R" value="Prosegui" onclick="Javascript:Disabilita();">
 	 		</FORM>
		</td>
		<td class="lNoBord" colspan="2">
			<FORM  method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&lTipoFunzione=ritornodettaglio">
      			<br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:Verify();">
 			</FORM>
		</td>
	</tr>
<%
	} else {
		searchForm = true;
	 	// Viene visualizzata la sezione "Ricerca Soggetto"
     	// if(TipoOperazione.equals("R")) { 
     	if (!"A".equals(TipoOperazione)) {
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="lNoBord" colspan="4">
			<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaSoggetto">
			<table width=100%>
				<tr>
					<td class="Titolo" colspan="4">Tipo Ricerca</td>
				</tr>
				<tr>
					<td class="c" width="25%">
						Nell'Ufficio &nbsp;<input type="radio" name="tipoRicerche" value="ufficio" onClick="radioBase();">
					</td>
					<td class="c" width="25%">
						Nel distretto &nbsp;<input type="radio" name="tipoRicerche" value="distretto" onClick="radioBase();" checked>
					</td>
					<td class="c" width="25%">
						In altri distretti &nbsp; <input type="radio" name="tipoRicerche" value="altriDistretti" onClick="radioBase();">
					</td>
					<td class="c" width="25%">
						<div id="divAltreBDISpace">
						&nbsp;
						</div>
						<div id="divAltreBDI" style="display: none;">
						<select name="<%=ICostantiJmsCode.CAMPO_CODICE%>" 
							onChange="javascript:document.LoadRicercaSoggetto.<%=ICostantiJmsCode.CAMPO_DESCRIZIONE%>.value = document.LoadRicercaSoggetto.<%=ICostantiJmsCode.CAMPO_CODICE%>.options[document.LoadRicercaSoggetto.<%=ICostantiJmsCode.CAMPO_CODICE%>.options.selectedIndex].text">
							<option value="-">Tutte</option>
							<%
								Iterator itx = ListaBDI.iterator();
								while (itx.hasNext()) {
									JmsCodeModel lCodBDI = (JmsCodeModel) itx.next();
							%>
							<%-- Ticket#20210208018 - Ricerca soggetto altre bdi: aggiunta descrizione --%>
							<option value="<%=lCodBDI.getCodice()%>"><%=lCodBDI.getDescrizione()%></option>
							<%
								}
							%>
						</select>
						</div>
					</td>
				</tr>
				<tr>
					<!-- onclick="Javascript:return Verify();" -->
					<td colspan="4"><INPUT class="bottone" type="submit" name="RICERCA" value="Ricerca"></td>
				</tr>
			</table>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value=""> 
			<input type="HIDDEN" name="<%=ICostantiJmsCode.CAMPO_DESCRIZIONE%>" value=""> 
			<input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=soggetto.getIdSoggetto()%>">
			<input type="HIDDEN" name="DalDettaglio" value="S">
			</form>
		</td>
	</tr>
<%  //} // end if(TipoOperazione.equals("R"))
		} // end if(!TipoOperazione.equals("A"))
	} // end else 
} // end if( lProfilo.isSiep()){
%>
</table>

<script language="JavaScript">
function radioBase() {
<%
if(searchForm) {
%>
   	if (document.getElementById("tipoRicerche") != null) {
		if (document.LoadRicercaSoggetto.tipoRicerche[1].checked
				|| document.LoadRicercaSoggetto.tipoRicerche[0].checked ) {
		   radioEnable();
		   document.LoadRicercaSoggetto.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.fascicolo.action.ActRicercaFascicoloPerSoggetto";
	   } else if (document.LoadRicercaSoggetto.tipoRicerche[2].checked ) {
		   radioEnable();
		   document.LoadRicercaSoggetto.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.jms.action.ActRicercaSoggettoAltreBDI";
	   }
   	}
<%
}
%>
}

function radioEnable() {
	var nodeAltreBDI=document.getElementById('divAltreBDI');
	var divAltreBDISpace=document.getElementById('divAltreBDISpace');
	if (document.LoadRicercaSoggetto.tipoRicerche[1].checked || document.LoadRicercaSoggetto.tipoRicerche[0].checked) {
		nodeAltreBDI.style.display='none';
		divAltreBDISpace.style.display='block';
	} else if (document.LoadRicercaSoggetto.tipoRicerche[2].checked ) {
		nodeAltreBDI.style.display='block';
		divAltreBDISpace.style.display='none';
	}
}
</script>

</body>
</html>