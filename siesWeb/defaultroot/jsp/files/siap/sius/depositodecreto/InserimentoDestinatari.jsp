<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="fascicolo" scope="session"
	class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="UtenteConnesso" scope="session"
	class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="destDeposito" scope="request" class="java.util.Vector" />
<jsp:useBean id="Aggiungi" scope="request" class="java.lang.String" />
<jsp:useBean id="NotifichePresenti" scope="request"
	class="java.lang.String" />
<jsp:useBean id="altreAutoritaGiudiziarie" scope="request"
	class="java.lang.String" />

<script language="JavaScript">
  var desktop;
  function ListaAutorita(a_formname,a_fieldname,codTipoUfficio) {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>

<script language="JavaScript">
        function Lista(tipo, codice) {
           var nomeForm = "<%=request.getParameter("NomeForm")%>";
		if (tipo == 'C')
			ListaComuni(nomeForm, codice);
		else if (tipo == 'U')
			ListaUffici(nomeForm, codice);
		else if (tipo == 'PGCAP')
			ListaProcure(nomeForm, codice);
		else if (tipo == 'UDS' || tipo == 'UDSM')
			ListaUDS(nomeForm, codice);
		else if (tipo == 'UEPE')
			ListaCSSA(nomeForm, codice);
		else if (tipo == 'TDS' || tipo == 'TDSM')
			ListaTDS(nomeForm, codice);
		else if (tipo == 'USSM') {
			ListaUSSM(nomeForm, codice);
		} else if (tipo == 'PMM') {
			ListaPMM(nomeForm, codice);
		}

	}
</script>

<table cellspacing="2" cellpadding="2" width="95%">

	<tr>
		<td colspan=3 class="Titolo" colspan=2>Nuovi Destinatari</td>
	</tr>

	<tr>
		<td class="l">Data Trasmissione atti</td>
		<td class="L"><input value="<%=DateUtils.getSysDate("dd")%>"
			type="text" size="2" maxlength="2"
			name="<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>"
			onBlur="javascript:value=FillDM(value)"> / <input
			value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2"
			maxlength="2"
			name="<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>"
			onBlur="javascript:value=FillDM(value)"> / <input
			value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4"
			maxlength="4"
			name="<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>">
		</td>
	</tr>

	<tr>
		<td colspan=3 class="Titolo" colspan=2>Per la comunicazione</td>
	</tr>

	<%
		// Contatore destinatari
		int ind = 0;
		String sedeCom = "";
		String sedeEsec = "";
		String tipoUffEsec = "";
		String TipoUfficioConnesso = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
		if (UtenteConnesso != null && UtenteConnesso.getUfficioUtente() != null)
			sedeCom = UtenteConnesso.getUfficioUtente().getDescrComune();

		if (fascicolo.getDescrComuneUfficio() != null && fascicolo.getDescrComuneUfficio().length() > 0) {
			// Viene ricavato il Comune dell'ufficio di esecuzione ed il tipo
			sedeEsec = fascicolo.getDescrComuneUfficio();
			if (fascicolo.getCodTipoUfficio() != null)
				tipoUffEsec = fascicolo.getCodTipoUfficio();
		}

		if (NotifichePresenti.equalsIgnoreCase("NO")) {
			// Comunicazione alla Procura di Ufficio per cui non viene emessa notifica ma sempre una comunicazione
			String lDescrProcura = "Procura Generale di";
			String lCodProc = "PGCAP";
			if (TipoUfficioConnesso.equalsIgnoreCase("UDS")) {
				lDescrProcura = "Procura Presso il Tribunale di ";
				lCodProc = "PM";
				// MEV10-s3: aggiunta or condition per gestire "Ufficio di Sorveglianza presso il Tribunale per minorenni"
			} else if ("TDSM".equalsIgnoreCase(TipoUfficioConnesso)
					|| "UDSM".equalsIgnoreCase(TipoUfficioConnesso)) {
				lDescrProcura = "Procura della Repubblica presso il tribunale dei Minorenni ";
				lCodProc = "PMM";
			}
	%>
	<tr>
		<td class="l"><%=lDescrProcura%> <input type="HIDDEN"
			name="cod_destinatari" value="<%=lCodProc%>" maxlength="6" size="6">
		</td>
		<td class="l"><input
			Title="Sede della Procura presso il Tribunale"
			name="sede_destinatari" value="<%=sedeCom%>" type="text"
			maxlength="35" size="35"></td>
		<td><input name="nota_destinatari" value="per comunicazione"
			type="text" maxlength="300" size="35" readonly></td>
	</tr>

	<%
		ind++;
		}
		Iterator itDeposito = destDeposito.iterator();
		DecodificheModel lDec = null;
		while (itDeposito.hasNext()) {
			lDec = (DecodificheModel) itDeposito.next();

			if (lDec.getCode().equalsIgnoreCase("UGI") || lDec.getCode().equalsIgnoreCase("UGD")) {
			} else {
	%>
	<tr>
		<td class="l"><%=lDec.getDescription()%> <input type="HIDDEN"
			name="cod_destinatari" value="<%=lDec.getCode()%>" maxlength="6"
			size="6"></td>

		<td class="l"><input Title="Sede <%=lDec.getDescription()%>"
			name="sede_destinatari"
<%if (lDec.getCode().equalsIgnoreCase("PGCAP")) {%>
			value="<%=((tipoUffEsec.equalsIgnoreCase("PGCAP")) ? sedeEsec : "")%>"
			type="text" maxlength="35" size="35"> <%
 	} else if (lDec.getCode().equalsIgnoreCase("PM")) {
 %> value= "<%=((tipoUffEsec.equalsIgnoreCase("PM")) ? sedeEsec : "")%>"
			type="text" maxlength="35" size="35"> <%
 	} 
    // MEV63
 	else if (lDec.getCode().equalsIgnoreCase("PMM")) {
 		 %> value= "<%=((tipoUffEsec.equalsIgnoreCase("PMM")) ? sedeEsec : "")%>"
 					type="text" maxlength="35" size="35"> <%
	}else {
 %> value="" type="text" maxlength="35" size="35"> <%
 	}
 %> <a
			href="Javascript:Lista('<%=lDec.getFiltro()%>','sede_destinatari[<%=ind%>]');">
				<img src="/images/filefolder.gif" border=0>
		</a></td>
		<%
			if (lDec.getCode().equals("CPS") || lDec.getCode().equals("SERT")
							|| lDec.getCode().equals("CC")) {
		%>
		<td><input name="nota_destinatari" value="" type="text"
			maxlength="300" size="35"></td>
		<%
			} else if (lDec.getCode().equals("PGCAP") || lDec.getCode().equals("PM")
							|| lDec.getCode().equals("PMM")) {
		%>
		<td><input name="nota_destinatari" value="per l'esecuzione"
			type="text" maxlength="300" size="35" readonly></td>
		<%
			} else {
		%>
		<td><input name="nota_destinatari" value="" type="HIDDEN"
			maxlength="300" size="35" readonly></td>
		<%
			}
		%>
	</tr>
	<%
		}
			ind++;
		}
	%>
</table>

<!-- 23/03/2007 Aggiunto ulteriore destinatario Autorità Giudiziaria -->
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" colspan=6>Altre autorità Giudiziarie</td>
	</tr>

	<tr>
		<td class="l">Autorità Destinazione</td>
		<td class="l"><select title="Destinatario" name="cod_destinatari">
				<%=altreAutoritaGiudiziarie%>
		</select></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="l"><input Title="Sede " name="sede_destinatari"
			value="" type="text" maxlength="35" size="35"> <a
			href="Javascript:ListaAutorita('<%=request.getParameter("NomeForm")%>','sede_destinatari[<%=ind%>]', document.<%=request.getParameter("NomeForm")%>.cod_destinatari[<%=ind%>][document.<%=request.getParameter("NomeForm")%>.cod_destinatari[<%=ind%>].selectedIndex].value);">
				<img src="/images/filefolder.gif" border=0>
		</a></td>
	</tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="l"><input name="nota_destinatari" value="" type="text"
			maxlength="300" size="65"></td>
	</tr>

</table>