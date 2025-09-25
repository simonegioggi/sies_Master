<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoDepositoModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"%>

<jsp:useBean id="provvedimenti"     		scope="request" class="java.util.Vector"/>
<jsp:useBean id="provvedimentiDataRicorso"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" 			scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="flag_valida"  				scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile"			scope="request" class="java.lang.String"/>
<%-- MEV_9: aggiunto useBean --%>
<jsp:useBean id="depositoOrdinanzaVector"	scope="request" class="java.util.Vector"/>

<html>
<head>
<title>[S.I.E.S.] - Lista Provvedimenti</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
// funzione per il richiamo alla cancellazione
function conferma(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2) {
	var documentoRegistrato = a_entityname2;
   	if (window.confirm('Confermi la cancellazione ?')) {
      	if (documentoRegistrato == "S") {
       		var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname + "&" + a_parameter2 + "=" +a_entityname2, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
         	window.parent.close();
      	} else {
           	str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname;
			window.location.href=str;
      	}
	}
}

// Funzione di visualizzazione motivo annullamento
function cancella(idEvento) {
	window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento + "&campoSIUS=s","Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
}
</script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
      	<td class="LBG">
	        <font class="label">Funzione :</font>&nbsp;
	        <font class="campo">Ricerca Provvedimenti</font>
      	</td>
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>
<br>
<%
boolean modificabile = true;
if (isModificabile != null && isModificabile.equalsIgnoreCase("NO"))
	modificabile = false;

String lFunAnnullaValidaProvvedimento = "";
String lFunAnnullaValidaAllegato = "";

if (flag_valida.equals(""))
	flag_valida = "SI";
String isDepositato = "NO";

if (fascicoloSiusGP != null) {
%>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<%
}
%>
<br>
<%
if (provvedimenti.size() == 0) {
%>
<table>
	<tr>
		<td class="LBG">
          	<font class="label"> Non ci sono provvedimenti riferiti al procedimento indicato </font>
        </td>
	</tr>
</table>
<%
} else {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<table width="96%">
	<tr>
		<td class="int" nowrap>Data emissione</td>
		<td class="int">Tipo provvedimento</td>
		<td class="int">Motivo provvedimento</td>
		<td class="int">Esito provvedimento</td>
		<td class="int" nowrap>Data Deposito</td>
		<%-- MEV_9: aggiunta data esecutivita e gestita nella pagina solo per C050 e C051 --%>
<%
	if (!Utils.isNullObj(fascicoloSiusGP) && !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel())
			&& !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
			&& (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C050") == 0
			|| fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C051") == 0)) {
%>
		<td class="int" nowrap>Data Esecutivita&#768;</td>
<%
	}
%>
		<td class="int" nowrap>Data Ricorso </td>
<%
	if (flag_valida.equals("SI")) {
		// Estrazione funzioni annulla Validazione
      	Collection<?> lFunzFiglie = (Collection<?>) request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
      	if ((lFunzFiglie != null) && (lFunzFiglie.size() != 0)) {
			Iterator<?> lIterFunz = lFunzFiglie.iterator();
			FunctionModel lFunz = null;
			while (lIterFunz.hasNext()) {
          		lFunz = (FunctionModel)lIterFunz.next();
          		if (lFunz.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_LINK)
          				&& lFunz.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)) {
          			if (lFunz.getVisualizationOrder().intValue() == 1)
                 		lFunAnnullaValidaProvvedimento = lFunz.getNameAction();
             		else if (lFunz.getVisualizationOrder().intValue() == 2)
                 		lFunAnnullaValidaAllegato = lFunz.getNameAction();
          		}
        	}
      	}
%>
		<td class="int">Provv.<br>Validato</td>
      	<td class="int">Deposito<br>Validato</td>
<%
	}
%>
      	<td class="int">Azioni</td>
	</tr>
<%
	Iterator<?> itx = provvedimenti.iterator();
    int i = 0;
    while (itx.hasNext()) {
		EventoDepositoModel lProv = (EventoDepositoModel) itx.next();
%>
	<tr>
		<td class="c">
		 	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(),"dd-MM-yyyy"), "-")%>
		</td>
		<td class="c"><%=StringUtils.toStringJSP(lProv.getDescrTipoProvvedimento(),"-")%></td>
		<td class="c"><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></td>
		<td class="c"><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></td>
		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataDeposito(),"dd-MM-yyyy"),"-")%></td>
<%
		if (!Utils.isNullObj(fascicoloSiusGP) && !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel())
				&& !Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
				&& (fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C050") == 0
				|| fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C051") == 0)) {
			if ("0270".equals(lProv.getCodEsito()) && "03".equals(lProv.getCodTipoProvvedimento())) {
				Date dataEsecutivita = null;
				Iterator iter = depositoOrdinanzaVector.iterator();
				while (iter.hasNext()) {
					DepositoOrdinanzaPcModel dopcm = (DepositoOrdinanzaPcModel) iter.next();
					if (lProv.getIdEvento().compareTo(dopcm.getIdEventoGenerato()) == 0
							&& !Utils.isNullObj(dopcm.getDataEsecutivita())) {
						dataEsecutivita = dopcm.getDataEsecutivita();
						break;
					}
				}
%>
		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEsecutivita, "dd-MM-yyyy"), "-")%></td>
<%
			} else {
%>
		<td class="c">-</td>
<%
			}
		}
%>
		<td class="c">
			<font class="campo">
<%
		if (provvedimentiDataRicorso != null && provvedimentiDataRicorso.size() > i) {
			if (!StringUtils.toStringJSP(provvedimentiDataRicorso.get(i), "-").equals("-")) {
%>
      			&nbsp;<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione&IdEvento=<%=lProv.getIdEvento()%>&CodTipoProvvedimento=<%=StringUtils.toStringJSP(lProv.getCodTipoProvvedimento())%>&TornaQui=<%=0%>"><%=StringUtils.toStringJSP(provvedimentiDataRicorso.get(i), "-")%></a>
<%
			} else {
%>
      			<%=StringUtils.toStringJSP(provvedimentiDataRicorso.get(i), "-")%>
<%
			}
		} else {
%>
				-
<%
		}
%>
     		</font>
		</td>
<%
		if (flag_valida.equals("SI")) {
%>
        <td class="c">
<%
			if (lProv.getFlagDocumentoRegistrato() != null && lProv.getFlagDocumentoRegistrato().compareTo("S") == 0) {
           		// SVALIDAZIONE. Il decreto di Unificazione è sempre svalidabile !
           		if (lProv.getNumAllValidati() < 1 && lFunAnnullaValidaProvvedimento.length() > 1
           				&& (modificabile || lProv.getCodEsito().compareToIgnoreCase("0600") == 0)) {
%>
			<a href="Javascript:annulla('Vuoi annullare la validazione del provvedimento?', '<%=lFunAnnullaValidaProvvedimento%>', '<%=ICostantiEvento.CAMPO_ID_EVENTO%>', '<%=lProv.getIdEvento()%>');">
          		<img src="/images/TickRed.gif" alt="Annulla validazione provvedimento" border="0">
          	</a>
<%
				} else {
%>
          	<img src="/images/TickRed.gif">
<%
				}
        	// ANNULLATO
        	} else if (lProv.getFlagDocumentoRegistrato() != null && lProv.getFlagDocumentoRegistrato().compareTo("A") == 0) {
%>
			<a class="cliccabile" href="javascript:cancella('<%=lProv.getIdEvento()%>');" title="ANNULLAMENTO">
				<font class="cRosso">ANNULLATO</font>
			</a>
<%

        	} else {
%>
          	-
<%
			}
%>
		</td>
        <td class="c">
<%
        	isDepositato = "NO";
        	if (lProv.getNumAllValidati() > 0) {
           		if (lProv.getFlagDocumentoRegistrato() != null && lProv.getFlagDocumentoRegistrato().compareTo("S") == 0) {
           			isDepositato = "SI";
           			if (lFunAnnullaValidaAllegato.length() > 1 && modificabile) {
%>
			<a href="Javascript:annulla('Vuoi annullare la validazione del deposito?', '<%=lFunAnnullaValidaAllegato%>', '<%=ICostantiEvento.CAMPO_ID_EVENTO%>', '<%=lProv.getIdEvento()%>');">
          		<img src="/images/TickRed.gif" alt="Annulla validazione deposito" border="0">
          	</a>
<%
					} else {
%>
          	<img src="/images/TickRed.gif">
<%
					}
           		} else {
%>
			-
<%
				}
			} else {
%>
          	-
<%
			}
%>
		</td>
<%
 		}
%>
      	<td class="c">
<%
		String isBlob = "SI";
		if (lProv.getFlagDocumentoRegistrato() == null) {
			isBlob = "NO";
		}
        String isAllegato = "NO";
        if (lProv.getNumAllegati() > 0) {
        	isAllegato = "SI";
        }
/*
Le condizioni per abilitare la modifica di un provvedimento sono:
il Procedimento SIUS è modificabile; (modificabile)
il provv. è validato; (lProv.getFlagDocumentoRegistrato() != null && lProv.getFlagDocumentoRegistrato().compareTo("S") == 0)
il provv. è depositato ma con deposito non validato; (isDepositato.equalsIgnoreCase("NO") && lProv.getDataDeposito() != null)
il provv. non appartiene ad uno dei seguenti tipi:
	Unificazione (cod. Esito = 0600),
	Fissazione Udienza (cod. Esito = 0601),
	Irreperibilità (cod. Esito = 0602),
	Rinvio Udienza (cod. Esito = 0603),
	// MEV_9: aggiungo 3 nuovi esiti per il decreto di designazione magistrato relatore
	ESITO_PROVVEDIMENTO 0270 Applica ex art. 678 comma 1 ter cpp,			NON PIU'
	ESITO_PROVVEDIMENTO 0271 Conferma Decisione del Magistrato Relatore,	NON PIU'
	ESITO_PROVVEDIMENTO 0610 Magistrato Designato art. 678 1-ter;
*/
		String lModificaProvvedimento = "NO";
		if (modificabile && lProv.getFlagDocumentoRegistrato() != null && lProv.getFlagDocumentoRegistrato().equalsIgnoreCase("S")
				&& isDepositato.equalsIgnoreCase("NO") && lProv.getDataDeposito() != null) {
    		if (lProv.getCodEsito().compareTo("0600") != 0
    				&& lProv.getCodEsito().compareTo("0601") != 0
    				&& lProv.getCodEsito().compareTo("0602") != 0
    				&& lProv.getCodEsito().compareTo("0603") != 0
					/*&& lProv.getCodEsito().compareTo("0270") != 0
    				&& lProv.getCodEsito().compareTo("0271") != 0*/
    				&& lProv.getCodEsito().compareTo("0610") != 0)
        		lModificaProvvedimento = "SI";
		}
	  	if (flag_valida.equals("SI")) {
%>
			<jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_RICERCA%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=lProv.getIdEvento()%>"/>
				<jsp:param name="CampoIdEntitaProvv" value="campoSIUS"/>
				<jsp:param name="ValoreIdEntitaProvv" value="<%=lProv.getFlagDocumentoRegistrato()%>"/>
				<jsp:param name="CodTipoProvvedimento" value="<%=lProv.getCodTipoProvvedimento()%>"/>
				<jsp:param name="Stampa" value="<%=isBlob%>"/>
				<jsp:param name="CodEsito" value="<%=lProv.getCodEsito()%>"/>
				<jsp:param name="Allegato" value="<%=isAllegato%>"/>
				<jsp:param name="Depositato" value="<%=isDepositato%>"/>
				<jsp:param name="Modifica" value="<%=lModificaProvvedimento%>"/>
			</jsp:include>
<%
		} else {
%>
			<jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_NOTIFICHE%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=lProv.getIdEvento()%>"/>
				<jsp:param name="FLAG_PIU_MENO" value="<%=lProv.getFlagPiuMeno()%>"/>
			</jsp:include>
<%
}
%>
		</td>
	</tr>
<%
		i++;
   	} // end while
%>
</table>
<%
} // end if provvedimenti.size()
%>
</body>
</html>