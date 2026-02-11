<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.competenza.action.ICostantiCompetenza"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<%@ page import="java.util.Iterator" %>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="competenza"          scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>
<jsp:useBean id="fascCompetenza"      scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="noteautoritaEsterna" scope="request" class="java.lang.String"/>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission --%> 
<jsp:useBean id="MessaggiEsiti" scope="request" class="java.util.Vector"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();

  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
  
  String lUffComp="";
  if(competenza!=null && competenza.getIdCompetenza()!=null)
  {
	  if(competenza.getChiaveUfficio()!=null)
	  {
		  lUffComp = competenza.getChiaveUfficio();
	  }
  }
  
  // Seguito Atti
  String lTitolo="Dettaglio Trasmissione Competenza";
  if(eventonotifica!=null && eventonotifica.getEvento()!=null && eventonotifica.getEvento().getCodMotivo()!=null )
  {
	  if(eventonotifica.getEvento().getCodMotivo().compareTo("0740")==0 )
	  {
		  lTitolo +=" - Seguito Atti";
	  }
  }

  // Ticket#20231010019:€” SIEP - Fascicolo trasmesso per errore - se fascicolo non di competenza vanno bloccati i tasti funzione
  //                             sulla form di dettaglio
  boolean isFascicoloDiCompetenza = false;
  String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();

  if (lUffUtente.equals(lFascicoloAssociato.getChiaveUfficio()))
    isFascicoloDiCompetenza = true;  
  // Ticket#20231010019 - FINE 
  
  
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Richiesta </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  
  <script language="JavaScript">
	function Seguito()
	{
		//document.formName.< %=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciTrasmissioneCompetenza";
		document.formName.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciTrasmissioneSeguitoAtti";
		document.formName.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value="0740";
	}
	
	<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission --%>
	function marcaElaborato(idMessaggio) {
		strMsg = "Attenzione procedendo il messaggio selezionato verrà marcato come 'Elaborato' senza registrare l'annotazione esito.";
        strMsg += " Si vuole procedere comunque?";
        if (window.confirm(strMsg)){
            //
        	document.marcaEsito.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value=idMessaggio;
        	document.marcaEsito.submit();
        	$('#formSubmit').find(':submit').prop('disabled',true);
        }		
	}
	
	
	function Verify(){
		   
		<% if (MessaggiEsiti.size() > 0) { %>
		var count = $('input[type="checkbox"][name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>_ESITO"]:checked').length;

		if (count>1) {
			alert("Selezionare un solo messaggio per l'annotazione esito");
	        return false;
		}
		else if (count==0){
			strMsg = "Attenzione non è stato selezionato nessuno dei messaggi di esito in attesa di elaborazione.";
			strMsg += " Proseguendo il messaggio restera' in attesa elaborazione. ";
			strMsg += " Si vuole procedere comunque?";
			if (!window.confirm(strMsg)){
				return false;
			}
		}
		else {
			<%-- Selezionato un solo messaggio setto il valore --%>
			var selected = $('input[type="checkbox"][name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>_ESITO"]:checked');
			var idMsgSel = selected.val();
			document.formName.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value= idMsgSel;
		}
		<% }%>
		
		return true;
	}
	<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission - FINE --%>
  </script>
		
</head>
<body class="corpo">

  <table>
  <tr>
  <td class="LBG">
  	<a href="Javascript:window.print();">
  	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
  </td>
  <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
  	<font class="campo"><%=lTitolo%></font>
  </td>
<%  if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
       (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
	{
	    // BOTTONE DI VALIDAZIONE DIRETTA 
	  	if(!lUffComp.equals(""))
   	  	{	
 			if(lUfficioUtenteConnesso.getCodUfficio().compareTo(lUffComp) == 0 )
 			{	%>	
   				<td class="LBG">
        			<a onclick="javascript:lookUpload(); document.comandi.CampoBlob.focus();">
          				<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        			</a>
      			</td>
   				
  <%		}
   	  	}
   	  	
  		//BOTTONE DI STAMPA 	%>
      	<input type="hidden" name="tipo" value="D">
      	<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        	<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaTrasmissioneCompetenza&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      	</jsp:include>
    				
<% 	} %>

  <!-- TOOLBAR HEADER (per il tasto di TRASMISSIONE -->  
  <%
if (isFascicoloDiCompetenza) { // Ticket#20231010019:€” SIEP solo se di competenza  
  //if(eventonotifica.getEvento().getDataTrasmissioneAtti().equals(eventonotifica.getEvento().getDataEmissione())){
    if(eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null && eventonotifica.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S")){
	  String lModificabile = "NO";
	  if(eventonotifica.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("N"))lModificabile = "SI";%>
		<td class="LBG">	
	        <jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
	          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
	          <jsp:param name="ValoreIdEntita" value="<%=eventonotifica.getEvento().getIdEvento()%>" />
	          <jsp:param name="FlagDocumentoRegistrato" value="<%=eventonotifica.getEvento().getFlagDocumentoRegistrato()%>" />
			  <jsp:param name="Modificabile" value="<%=lModificabile %>" />
			</jsp:include>
		</td>
	<%} %>
<%} %>	
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<table width=90%>
	<br>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
      <font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {%>
              DETENUTO PER ALTRA CAUSA
       <%
        }
        else
        {
%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
         }%>
         </font>
   </td>
</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
<%
          }
        }
%>

<%
       if (penaresidua.getDataInizio() != null)
       {
%>
 <tr>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
 </tr>
<%
       }

       if (penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
        <tr>
          <td class="l">Pena Detentiva</td>
          <td class="L" colspan=5><font class="campo">ERGASTOLO&nbsp;</font></td>
        </tr>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
        <tr>
          <td class="l">Pena Detentiva</td>
          <td class="L"colspan=5><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
        </tr>
<%
        }
       }
%>
      <tr>
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())){
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=5>
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
         </td>
<%
        }else
        {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" colspan=5>
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
         </td>


<%     }
      }
%>
</tr>
<tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
            <%if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0){ %>

          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
            } }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
            <%if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0){ %>

      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
            }}
    }
%>

       </tr>
<tr>
       <%if(eventonotifica.getEvento().getDataEmissione()!= null){%>
        <td class="l">Data Emissione</td>
        <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>

         </td>
<%}%>
<%if(eventonotifica != null && eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0] != null && eventonotifica.getNotifiche()[0].getDataInvio()!= null)
  {%>
        <td class="l">Data Trasmissione</td>
        <td class="L" >
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy") )%></font>

         </td>
        <%}%>
      </tr>
	</table>
    <br>
    <table width=90%>
	   <tr>
	      <td class="Titolo" colspan='8'>Titolo Che Determina La Competenza</td>
	   </tr>
<%if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getCodTipoProvvedimento()!= null){%>
		<tr>
			<td class="l">Tipo Provvedimento</td>
			<td class="l" colspan='7'>
				<font class="campo">
					<%=competenza.getDescrTipoProvvedimento()%>
				</font>
			</td>
		</tr>
		<tr>
			<td class="l">Data Provvedimento</td>
			<td class="l"><font class="campo"><%=DateUtils.getDateToString(competenza.getDataProvvedimento(), "dd-MM-yyyy")%></font></td>
			<td class="l">Definitivo in Data</td>
			<td class="l"><font class="campo"><%=DateUtils.getDateToString(competenza.getDataIrrevocabilita(), "dd-MM-yyyy")%></font></td>
		</tr>		
		<tr>
			<td class="l">Pronunciata da</td>
			<td class="l"  colspan='7'><font class="campo"><%=competenza.getDescrTipoAutoritaEmittente()%></font></td>
		</tr>
		<tr>
			<td class="l">Luogo</td>
			<td class="l"><font class="campo"><%=competenza.getDescrLuogoEmittente()%></font></td>
			<td class="l">Sezione</td>
			<td class="l"><font class="campo">
			<%if(competenza.getNumSezioneAutoritaEmittente()!=null){ %>
				<%=competenza.getNumSezioneAutoritaEmittente()%>
				<%} %>&nbsp;
			</font></td>
		</tr>
		
		<tr>
	      <td class="Titolo" colspan='8'>Ufficio Competente all'Emissione del Provvedimento</td>
	   	</tr>
	   	
	   	<tr>
	      <td class="l">Ufficio Pubblico Ministero</td>
	      <td class="l" colspan='7'>
	         <font class="campo"><%=competenza.getDescrTipoAutoritaComp()%></font>&nbsp;
	       </td>
	    </tr>
		<tr>
			<td class="l">Luogo</td>
			<td class="l"  colspan='7'><font class="campo"><%=competenza.getDescrLuogoAutoritaComp()%></font>&nbsp;</td>
		</tr>

				<tr>
					<td class="l">Relativa al Procedimento Numero</td>
					<td class="l"  colspan='7'>
						<font class="campo">
							<%=StringUtils.toStringJSP(competenza.getChiaveAnno())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(competenza.getChiaveProgr())%>
						</font>
					</td>
				</tr>
					
		
		<tr>
	      <td class="Titolo" colspan='7'>Dati Atto</td>
	   	</tr>
	   	
	   	<tr>
			<td class="l">Tipologia Atto</td>
			<td class="l"  colspan='7'><font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%></font></td>
		</tr>
	   	<tr>
			<td class="l">Oggetto Atto</td>
			<td class="l"  colspan='7'><font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%></font></td>
		</tr>
	   	
		
	<%}%>
	<%if(eventonotifica.getCampoNote()!= null && eventonotifica.getCampoNote().length >0 && eventonotifica.getCampoNote()[0] != null && eventonotifica.getCampoNote()[0].getDescr() != null){%>
		<tr>
		    <td class="l">Contenuto </td>
		    <td class="l" colspan='7'>
		    	<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr())%></font>
		   </td>
		</tr>
<%		}
  		if(magistrato != null){  			
			if(!eventonotifica.getEvento().getCodMagistrato().equalsIgnoreCase("-")){ %>
			<tr>
				<td class="l">Magistrato Firmatario
				<td class="L" colspan='7'>
				    <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
					<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
				</td>
			</tr>
		<%}
	}%>
	<% if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() !=null) {%>
		<tr>
			<td class="l">Altro Destinatario
			<td class="L" colspan='7'>
				<font class="campo">
				<%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;
				</font>
			</td>
		</tr>
		<tr>
			<td class="l">Luogo
			<td class="L" colspan='7'>
				<font class="campo">
				<%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede()%>&nbsp;
				</font>
			</td>		
		</tr> 
	<%}%>
</table>

<%
//==============================================================================
// MEV26 - Cumulo
// Solo se trasmissione x Competenza (0340) e provvedimento validato, aggancio
// la funzone di annotazione esito trasmissione agganciabile anche dal riscontro
// esito trasmissioni.
// 
//==============================================================================
%>
<% 
if (isFascicoloDiCompetenza) { // Ticket#20231010019:€” SIEP solo se di competenza  
	EventoModel lEvento = eventonotifica.getEvento();
	if( (   "0340".equals(lEvento.getCodMotivo())
	     || "5403".equals(lEvento.getCodMotivo())     
	     || "0740".equals(lEvento.getCodMotivo())     
	    )
	   && "S".equals(lEvento.getFlagDocumentoRegistrato())
	  )
	{ %>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName" id="formSubmit"> 
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.presaincarico.action.ActLoadInsAnnotaEsitoTrasmComp">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= lEvento.getIdEvento() %>">
  <input type="HIDDEN" name="<%=ICostantiCompetenza.CAMPO_ID_COMPETENZA%>"  value="<%=competenza.getIdCompetenza()%>">
  <input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>"  value="<%=StringUtils.toStringJSP(Messaggio.getIdMessaggio(),"")%>" >
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>"  value="" >

  <table cellspacing=2 cellpadding=2 >    
    <tr>
      <td>
        <input type="submit" name="AnnotazioneEsito" value="Annotazione Esito" >
      </td>
<% if( !"0740".equals(lEvento.getCodMotivo()) ) { %>
      <td>
        <input type="submit" name="SeguitoAtti" value="Seguito Atti" onClick=javascript:Seguito(); >
      </td>
<% } %>

    </tr>
  </table>  

<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission --%>   
<% if (MessaggiEsiti.size() > 0) 
{ 
    String alert = "";
    int contaDaElaborare = 0;
    for (int i=0; i<MessaggiEsiti.size(); i++) {
        MessaggioModel lMsg = (MessaggioModel) MessaggiEsiti.elementAt(i);
        if (!"S".equals(lMsg.getFlagVisto()))
            contaDaElaborare = contaDaElaborare+1; 
    }
    if (contaDaElaborare>0)
        alert = "&nbsp;<font style='color:red;'>(Attenzione ci sono esiti in attesa di essere annotati)</font>";

%>
<br>
  <table width=90%>
     <tr>
        <td class="Titolo">Esiti Ricevuti <%=alert%> </td>
     </tr>
   </table>  
     
  <table cellspacing=2 cellpadding=2 width="90%">
    <tr>
      <td class="int">Ufficio Mittente Esito&nbsp;</td>
      <td class="int">Anno/Numero Fascicolo Cumulante&nbsp;</td>
      <td class="int">Tipo operazione&nbsp;</td>
      <td class="int">Data Trasmissione&nbsp;<br>Esito</td>
      <td class="int">Esito&nbsp;</td>
      <td class="int">Motivazioni&nbsp;</td>
      <td class="int">Azioni&nbsp;</td>
    </tr>

<%
  Iterator itx = MessaggiEsiti.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    String strCumulante = "";
    if (lMess.getChiaveAnnoFasCumulante()!=null)
        strCumulante = StringUtils.toStringJSP(lMess.getChiaveAnnoFasCumulante())+"/"+StringUtils.toStringJSP(lMess.getChiaveProgrFasCumulante());
    else
        strCumulante = "<font style='color:red;'>n.d./n.d.</font>";
%>
  <tr>
    <td class="c"><%= lMess.getDescrUfficioMittente() + " " + lMess.getDescrSedeUfficioMittente()%></td>    
    <td class="c"><%= StringUtils.toStringJSP(strCumulante)%></td>
    <td class="c"><%= lMess.getDescrTipoOperazione()%></td>     
    <td class="c"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm"))%></td>
   
<% if(lMess.getCodEsito()!=null && lMess.getCodEsito().compareTo("01001")==0 )  {%>    
    <%-- Atti Presi in carico --%>
    <td class="cVerde"><%= lMess.getDescrEsito()%></td>
<% } else if(lMess.getCodEsito()!=null && lMess.getCodEsito().compareTo("01007")==0 ){%>
    <%-- Atti Rigettati --%>
    <td class="cRosso"><%= lMess.getDescrEsito()%></td>
<% } else   {%>
    <%-- Atti Trasmessi o Restituiti --%>
    <td class="c"><%= lMess.getDescrEsito()%></td>
<% } %>
      
    <td class="c"><%if(lMess.getNote()!=null){%><%=lMess.getNote()%><%} %>&nbsp;</td>
         
    <td class="c">
    <% if (!"S".equals(lMess.getFlagVisto())) { %>
        <input type="checkbox"  value="<%=lMess.getIdMessaggio() %>" 
               name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>_ESITO" 
               >
        <a href="Javascript:marcaElaborato('<%=lMess.getIdMessaggio() %>');">
            <img src="/images/delete.gif" width="12" height="12" alt="Marca Elaborato" border="0">
        </a>               
    <% } else { %>
        elaborato
    <% } %>
    </td>
  </tr>
<% } %>
</table>
<% } %>  
<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni - FINE --%> 
</form>  
<% } %>
<% } %>

<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni --%> 
<% if (isFascicoloDiCompetenza) { %>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="marcaEsito"> 
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"            value="siap.jms.messaggio.action.ActMarcaMessaggio">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"       value="<%=eventonotifica.getEvento().getIdEvento() %>">
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="">
  <input type="HIDDEN" name="Return" value="siap.siep.richiesta.action.ActDettaglioTrasmissioneCompetenza&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento() %>">
</form>
<% } %>
<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni - FINE --%> 

<br>
  <div align=left style="visibility:hidden" id="upld"><%-- onSubmit="return controllaUpload();" --%>
    <FORM name="comandi" enctype="multipart/form-data" method="post">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
         <%  //  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD % >" value="siap.siep.richiesta.action.ActUploadRichiestaGenerica">	-- %>
         	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD %>" value="siap.siep.richiesta.action.ActUploadTrasmissioneAttiPerCompetenza">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.richiesta.action.ActDettaglioTrasmissioneCompetenza">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  <br>
  <br>
</body>
  <%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission--%>
<% if (isFascicoloDiCompetenza) { 
    EventoModel lEvento = eventonotifica.getEvento();
    if( (   "0340".equals(lEvento.getCodMotivo()) // atti per competenza (per emissione provvedimento cumulo)
         || "5403".equals(lEvento.getCodMotivo()) // Emissione provvedimento di cumulo a seguito revoca beneficio    
         || "0740".equals(lEvento.getCodMotivo()) // atti per competenza (per emissione provvedimento cumulo) - Seguito Atti
        )
       && "S".equals(lEvento.getFlagDocumentoRegistrato())
      )
    { %>  
  <script language="JavaScript" type="text/javascript">
      var frmvalidator = new Validator("formName");
      frmvalidator.setAddnlValidationFunction("Verify"); 
  </script>
  <%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission --%>  
<% } } %>   
</html>