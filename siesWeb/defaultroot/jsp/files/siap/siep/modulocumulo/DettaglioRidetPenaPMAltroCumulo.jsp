<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils "%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel" />
<jsp:useBean id="TitoloInCumulo" scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel" />

<jsp:useBean id="Provvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel" />

<jsp:useBean id="UfficioEmittenteProvv" scope="request" class="siap.sico.ufficio.model.UfficioModel" />
<jsp:useBean id="lReato" scope="request" class="siap.siep.modulocumulo.model.ReatoCumuloModel" />

<%
    Vector <ComputiCumuloModel> lListaComputi = Provvedimento.getListaComputi();
%>

<html>
<head>
<title>Dettaglio Annotazione Rideterminazione Pena PM altro</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Inserisci'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciRidetPenaPMAltroCumulo";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.modalita.value = "I";
        
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = "";
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value = "";
        document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>.value = "";
        
        document.formName.submit();
      }      
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciRidetPenaPMAltroCumulo";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.modalita.value = "M";
        document.formName.submit();
      }
      else if (aTipoAzione=='Cancella'){
        var aStato = document.formName.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value;
        
        if (aStato=='E' || aStato=='M'){
          // Cancellazione Logica Richiedo Motivazione
          lAzione = "siap.siep.modulocumulo.action.ActInserisciRidetPenaPMAltroCumulo";
          document.formName.modalita.value = "C";
          document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          aMotivoModifica = document.formName.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>.value
          var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"formName"
                                     + "&" + "<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
          window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I'){
          // Cancellazione fisica richiedo conferma
          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciRidetPenaPMAltroCumulo";
            document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.formName.modalita.value = "C";

            document.formName.submit();
          }
        }
      }
      else if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaRidetPenaPMAltroCumulo";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
      }
    }
    
    
    function nuovoPeriodo(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciRidetPenaPMAltroCumulo";
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.formName.modalita.value = "NP";  
      document.formName.submit();   
    }
  </script>
</head>

<body class="corpo">

	<FORM name="comandi">
		<table>
			<tr>
				<td class="LBG"><a href="Javascript:window.print();">
				<img align="middle"
						src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
						alt="Stampa questa videata" border=0>
				</a></td>
				<td class="LBG"><font class="label">Funzione :</font>&nbsp; <font
					class="campo">Dettaglio Annotazione Rideterminazione Pena Altro&nbsp;</font></td>
				<%-- if (IstruttoriaCumulo.getFlagStato().equals("A")){ %>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Inserisci')">
          <img align="absmiddle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
        <% if (!"C".equals(Provvedimento.getFlagStato())) { %>
        <a href="javascript:eseguiFunzione('Modifica')">
          <img align="absmiddle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <a href="javascript:eseguiFunzione('Cancella')">
          <img align="absmiddle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>      
        <% } %>
      </td>
      <% } --%>
				<td class="LBG"><a href="javascript:eseguiFunzione('Indietro')">
						<img align="middle"
						src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif"
						alt="ritorna su" width="24" height="24" border="0">
				</a></td>
			</tr>
		</table>
	</FORM>

	<table align="center" width="95%" border="0" cellspacing="1"
		cellpadding="1">
		<tr>
			<td><jsp:include
					page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>" />
			</td>
		</tr>
		<tr>
			<td><jsp:include
					page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>" />
			</td>
		</tr>
	</table>

	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
		<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">

		<input type="hidden"
			name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>"
			value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>"> <input
			type="hidden"
			name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"
			value="<%=TitoloInCumulo.getIdTitoloCumulato()%>"> <input
			type="hidden"
			name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>"
			value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
		<input type="hidden"
			name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"
			value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
		<input type="hidden"
			name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>"
			value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">

		<input type="hidden" name="modalita" value="">


		<div id="divPosizionamento" align="left" style="padding-left: 25px;">

			<table align="center" width="95%" border="0" cellspacing="1" cellpadding="1">

				<%-- if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroDufficio(Provvedimento.getCodMotivo() )) { --%>

				<tr>
					<td class="l">Provvedimento</td>
					<td class="l" colspan="3">
						<font class="campo"> <%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%> </font> del 
						<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%>
					</font></td>
				</tr>
				<tr>
					<td class="l">Note</td>
					<td class="l"><font class="campo"><%=StringUtils.toStringJSP( Provvedimento.getNote() ) %></font>&nbsp;
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>

				<%-- } else {--%>
					<!-- Sezione con i dati del provvedimento di Altra Autorità -->
				<% if (!StatoEsecuzioneCumuloUtils.isRidPenaPMAltroDufficio(Provvedimento.getCodMotivo() )) { %>
					<tr>
						<td colspan="100%">
							<table width="100%">
								<tr>
									<td colspan=4 class="titolo">Dati Provvedimento Altra Autorità</td>
								</tr>
			
								<tr>
									<td class="l" width="20%">Provvedimento emesso da</td>
									<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lListaComputi.get(0).getDescUfficioEmittenteProvv(),"")%></font>
										di <font class="campo"><%=StringUtils.toStringJSP(lListaComputi.get(0).getDescluogoUfficioEmittenteProvv(),"")%></font>
									</td>
								</tr>
								<tr>
									<td class="l">Data ricezione provvedimento</td>
									<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lListaComputi.get(0).getDataRicezioneProvv(), "dd-MM-yyyy") )%></font></td>
								</tr>
								<tr>
									<td class="l">Data emissione provvedimento</td>
									<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lListaComputi.get(0).getDataEmissioneProvv(), "dd-MM-yyyy") )%></font></td>
									<td class="l">Anno / Numero Provvedimento</td>
									<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lListaComputi.get(0).getAnnoProvv(),"")%></font>/
										<font class="campo"><%=StringUtils.toStringJSP(lListaComputi.get(0).getProgrProvv(),"")%></font>
									</td>
								</tr>
			
								<tr>
									<td class="l">Tipo Provvedimento</td>
									<td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(), lListaComputi.get(0).getCodTipoProvv() ),"")%></font>
									</td>
								</tr>
			
								<tr>
									<td class="l">Oggetto Provvedimento</td>
									<td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(Provvedimento.getDescrMotivo(),"")%></font>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<% } %>
				
<%
    
				if (lListaComputi!=null) {
					Iterator itxComputi = lListaComputi.iterator();
					int ind = 0;
    				while ( itxComputi.hasNext()) 
    				{
						ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
						ind++;
%>
					<table align="center" width="95%" border="0" cellspacing="1" cellpadding="1">

					<%	// Dati Pena pecuniaria
					if (ind==1) {
%>
						<tr>
							<td class="titolo" colspan="4">Quantum Pena</td>
						</tr>
					<% } %>

					<%
						String lClassComputi = "campo";
						String lFlagPiu = "";
						if ("+".equals(lComputo.getFlagPiuMeno())) {
						  //isRevoche = true;
						  lClassComputi = "cRosso";
						  lFlagPiu = "<font class='cRosso'>+</font>";
						}

					%>

					<tr>
						<td class="l" colspan="1">Reclusione : <% if (!lComputo.isQuantumReclusioneZero()) {%>&nbsp;<%=lFlagPiu%>
							Anni <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione(),"0")%></font>
							Mesi <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione(),"0")%></font>
							Giorni <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione(),"0")%></font>
							<%}%>
						</td>
						<td class="l" colspan="3">Multa : <% if (!lComputo.isMultaZero()) {%><%=lFlagPiu%>
							<%}%> <font class="<%=lClassComputi%>"> &nbsp;<%=StringUtils.toStringJSP(lComputo.getImportoMulta(),"-")%>&nbsp;
						</font>
						</td>

					</tr>

					<tr>
						<td class="l" colspan="1">Arresto : <% if (!lComputo.isQuantumArrestoZero()) {%>&nbsp;<%=lFlagPiu%>
							Anni <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumAnniArresto(),"-")%></font>
							Mesi <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumMesiArresto(),"-")%></font>
							Giorni <font class="<%=lClassComputi%>"><%=StringUtils.toStringJSP(lComputo.getNumGiorniArresto(),"-")%></font>
							<%}%>
						</td>
						<td class="l" colspan="3">Ammenda : <% if (!lComputo.isAmmendaZero()) {%><%=lFlagPiu%>
							<%}%> <font class="<%=lClassComputi%>"> <%=StringUtils.toStringJSP(lComputo.getImportoAmmenda(),"-")%>&nbsp;
						</font>
						</td>
					</tr>

					<tr>
						<td class="l">Motivazioni</td>
						<td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lComputo.getNote(),"")%></font> </td>
					</tr>

					<tr>
						<td>&nbsp;</td>
					</tr>

				<% 	} 	// end while%>
			<% 	} 		// end if (lListaComputi!=null) %>

			</table>
		</div>
		<br>
		<br>
	</form>
</body>
</html>