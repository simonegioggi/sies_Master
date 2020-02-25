<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

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

<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         	scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"        	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="eventonotifica" 		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="competenza" 			scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>

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
  
//Costruzione della stringa con i dati del Titolo Richiesto
String stringaTitolo="";
stringaTitolo += competenza.getDescrTipoProvvedimento_Rich()+" N. "+competenza.getAnnoSentenza_Rich()+"/"+competenza.getNumeroSentenza_Rich();
stringaTitolo += "<font class='label'> del </font>&nbsp;";
stringaTitolo += DateUtils.getDateToString(competenza.getDataProvvedimento_Rich(),"dd-MM-yyyy");

if(competenza.getCodTipoProvvedimento_Rich().compareTo("01")==0 ||
	competenza.getCodTipoProvvedimento_Rich().compareTo("03")==0 || 
	competenza.getCodTipoProvvedimento_Rich().compareTo("05")==0 )
{
	 stringaTitolo += "<font class='label'> emessa da </font>";
}
else
{
	 stringaTitolo += "<font class='label'> emesso da </font>";
}
	 
stringaTitolo += competenza.getDescrTipoAutoritaEmittente_Rich(); 
stringaTitolo += "<font class='label'> di </font>";
stringaTitolo += competenza.getDescrLuogoEmittente_Rich();

//Costruzione della stringa con i dati del Soggetto 
String stringaSoggetto = "";

stringaSoggetto += "<font class='campo'>"
                  +StringUtils.toStringJSP(competenza.getCognome_Soggetto_Rich())+" "
                  +StringUtils.toStringJSP(competenza.getNome_Soggetto_Rich())+"</font>&nbsp;";

 stringaSoggetto += "<font class='label'> nato/a il :</font>&nbsp;";

//Soggetto: Data Nascita
if (competenza.getDataNascita_Soggetto_Rich()!=null)
 stringaSoggetto += "<font class='campo'>"+StringUtils.toStringJSP (DateUtils.getDateToString (competenza.getDataNascita_Soggetto_Rich(),"dd-MM-yyyy"))+"</font>";
else 
 stringaSoggetto += "<font class='campo'>***</font>";


//Soggetto: Comune nascita
stringaSoggetto += "<font class='label'>&nbsp; in &nbsp;</font>";
stringaSoggetto += "<font class='campo'>";

if (competenza.getDescrComuneNascita_Soggetto_Rich()!=null && 
	competenza.getDescrComuneNascita_Soggetto_Rich().compareTo("")!=0 && 
	competenza.getDescrComuneNascita_Soggetto_Rich().compareTo("-")!=0 )
{
	 stringaSoggetto += "&nbsp;"+competenza.getDescrComuneNascita_Soggetto_Rich();
}

if (competenza.getSigla_Provincia_Soggetto_Rich()!=null && 
	competenza.getSigla_Provincia_Soggetto_Rich().compareTo("")!=0 && 
	competenza.getSigla_Provincia_Soggetto_Rich().compareTo("-")!=0 )
{
	stringaSoggetto += "&nbsp;("+competenza.getSigla_Provincia_Soggetto_Rich()+")";
}

stringaSoggetto += "</font>";

if(competenza.getCodiceCui_Soggetto_Rich()!=null)
{	
	stringaSoggetto += "&nbsp;<font class='label'>Codice CUI : </font>"
                  	+"<font class='campo'>"+StringUtils.toStringJSP(competenza.getCodiceCui_Soggetto_Rich())+"</font>";
}                  	
  
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Richiesta Atti per Competenza</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

</head>
<body class="corpo">

  <table>
  <tr>
  <td class="LBG">
  	<a href="Javascript:window.print();">
  	<img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
  </td>
  <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
  	<font class="campo">Dettaglio Rigetto Richiesta Atti per Competenza</font>
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
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <br>  
<table>
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
        }	%>
        
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

       if (penaresidua.getDataInizio() != null)
       {
%>
 		<tr>
         	<td class="l">Data Decorrenza Pena</td>
         	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
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
	          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
	        </tr>
	<%
	        }
	        else
	        if(penaresidua.getFlagErgastolo().equals("D"))
	        {
	%>
	        <tr>
	          <td class="l">Pena Detentiva</td>
	          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
	        </tr>
	<%
	        }
       }
       
       if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
       {
          	if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
          	{	%>
			<tr>
	         <td class="l">Data Fine Pena</td>
	         <td class="L" >
	           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
	           -
	           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
	           -
	           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
	         </td>
	        </tr> 
<%
        	}
        	else
        	{	%>
        	<tr>
	         <td class="l">Data Fine Pena</td>
	         <td class="lRosso" >
	           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
	           -
	           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
	           -
	           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
	         </td>
			</tr>
<%     		}
      }

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
				<tr>
          			<td class="l">Reclusione</td>
          			<td class="l" >
            		  <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            		  <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            		  <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          			</td>
     <%			if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          		{ %>
          			<td class="l">Multa</td>
          			<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%        		} %>
				</tr> 
<%     		} %>

<%
    		if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        		(penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        		(penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0) )
    		{}
    		else
    		{	%>
    			<tr>
		    		<td class="l" >Arresto</td>
		      		<td class="l" >
		         	  <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
		         	  <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
		         	  <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
		      		</td>
     <%			if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
     			{ %>
     				<td class="l">Ammenda</td>
      				<td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%      		} %>
				</tr>
<%      	}
    }
%>

	<tr>
<%	if(eventonotifica.getEvento().getDataEmissione()!= null)
	{	%>
        <td class="l">Data Emissione</td>
        <td class="L" >
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
		</td>
		<td>&nbsp;&nbsp;&nbsp;</td>
<%	}

	if(eventonotifica != null && eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0] != null && eventonotifica.getNotifiche()[0].getDataInvio()!= null)
  	{	%>

        <td class="l">Data Trasmissione</td>
        <td class="L" >
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy") )%></font>

         </td>
<%	}%>
    </tr>
</table>
    
    <table width=90%>
		<tr>
    		<td class="Titolo" colspan="8">  Titolo Richiesto  </td>
  		</tr>
  		<tr>
    		<td class="l">Titolo</td>
    		<td class="L" colspan="7"><font class="campo"><%=stringaTitolo%></font></td>
  		</tr>
  		<tr><td>&nbsp;</td></tr>
	    <tr>
	      <td class="Titolo" colspan='8'>Titolo Che Determina La Competenza</td>
	    </tr>
<%if(eventonotifica.getEvento()!= null && eventonotifica.getEvento().getCodTipoProvvedimento()!= null){%>
		<tr>
			<td class="l">Tipo Provvedimento</td>
			<td class="l"><font class="campo"><%=competenza.getDescrTipoProvvedimento()%></font></td>
			<td class="l">Anno e Numero </td>
			<td class="l"><font class="campo"><%=competenza.getAnnoSentenza()%> / <%=competenza.getNumeroSentenza()%></font></td>
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
    		<td class="l">A carico di:</td>
    		<td class="L" colspan="7"><font class="campo"><%=stringaSoggetto%></font></td>
  		</tr>
		<tr><td>&nbsp;</td></tr>
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
			<td class="l" nowrap>Relativa al Procedimento Numero</td>
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

<br>
  <div align=left style="visibility:hidden" id="upld"><%-- onSubmit="return controllaUpload();" --%>
    <FORM name="comandi" enctype="multipart/form-data" method="post">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
         	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD %>" value="siap.siep.richiesta.action.ActUploadRigettoRichiestaAtti">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.richiesta.action.ActDettaglioRigettoRichiestaAtti">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  <br>
  <br>
</body>
</html>