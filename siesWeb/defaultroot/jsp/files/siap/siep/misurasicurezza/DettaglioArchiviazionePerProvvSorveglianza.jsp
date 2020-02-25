<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel" %>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.archiviazione.model.ArchiviazioneModel" %>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="f3b.log.LogF3B"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventonotifica"			scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"				scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="StrdataInizioPena"			scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataFinePenaA"			scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"				scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="archiviazione"				scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>
<%
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
  
/* 
* ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
* Numero MEV : SIES v10
* Autore    : gioggi
* Data      : 28/gen/2016
* Branch    : MEV_SIES v10
*/
// List listaMisure =(List) request.getAttribute("listaMisureSic");
// int nMis = 0;
// if (listaMisure != null)
// 	nMis = listaMisure.size();
//***** FINE INTERVENTO MEV_SIES v10 *****//
	
String CodMisura="";

/* 
* ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
* Numero MEV : SIES v10
* Autore    : gioggi
* Data      : 28/gen/2016
* Branch    : MEV_SIES v10
*/
// MisuraSicurezzaModel nuovaMis = new MisuraSicurezzaModel();
// if(nMis != 0) {	  
// 	nuovaMis = (MisuraSicurezzaModel)listaMisure.get(nMis-1);
// 	CodMisura=nuovaMis.getCodTipo();
// }
//***** FINE INTERVENTO MEV_SIES v10 *****//
  
//Gestione Autorità Esterne sulle Notifiche - 
String lCodTipoAutorita = "-";
NotificaModel lPrimaNotifica = new NotificaModel();
AutoritaEsternaModel lAutoritaEsterna = new AutoritaEsternaModel();

if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
	    if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null && 
	    	eventonotifica.getNotifiche()[0].getCodTipoNotifica().equals("AA")	)
	    {
	   	 	lPrimaNotifica = eventonotifica.getNotifiche()[0];
	      	lCodTipoAutorita = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodTipoAutorita();
	    }
	
	    lAutoritaEsterna = lPrimaNotifica.getAutoritaEsterna();
}

%>
<!-- DettaglioArchiviazionePerProvvSorveglianza -->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- Archiviazione per Provvedimento della Sorveglianza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
   
 	</script>
 
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Definizione Procedimento - Archiviazione per Provvedimento della Sorveglianza</font>
      </td>

<% 	if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    { %>
			<!-- BOTTONE DI VALIDAZIONE -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadAnnotazioneDecisioneDellaSorveglianza&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActDettaglioArchiviazionePerProvvSorveglianza&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
          <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        </a>
      </td>
      
<%   } %>
 
<% 	if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    {%>
    		<!-- BOTTONE DI STAMPA -->
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaArchiviazione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo()+"&CodPosizioneGiuridica="+lPosizione.getCodPosizioneGiuridica()+"&CodTipo="+CodMisura %>"/>
			</jsp:include>
	<!-- 	ICONA DI MODIFICA	 -->
		<td class="LBG">
			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaArchiviazionePerProvvSorveglianzaMS&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10">
       		<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
       		</a>
       	</td>      			
<%   }%> 
    
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
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        </font>
        </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      	<input type="HIDDEN" title="id Evento" value="" type="text" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" >
      	<input type="HIDDEN" title="Cod Motivo" value="" type="text" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" >
      	
      </tr>
  
<%	//fine modifica relativa al tipo istituto
    if(penaresidua.getIdPenaResidua() != null && 
    ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
    	&& !penaresidua.getFlagErgastolo().equals("S") 
    	&& !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            	(penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            	(penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0) )
        	{
        		
        	}
        	else
        	{
%>
			<tr>
		          <td class="l">Reclusione</td>
		          <td class="l" colspan=2>
		            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
		            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
		            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		          </td>
		          <td class="l">Multa</td>
		          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
			</tr>
<%
        	}
%>

<% 
			if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         		(penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             	(penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
     	 	{
				
     	 	}
			else
     	 	{
%>
			   <tr>
			      <td class="l" >Arresto</td>
			      <td class="l" colspan=2>
			         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
			      </td>
			      <td class="l">Ammenda</td>
			      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
			    	</td>
			    </tr>	
<%
      		}

     }  // CHIUDO if(penaresidua...)
%>
    <tr>
<%
      if (penaresidua.getDataInizio() != null)
      {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
      }
 
       if (penaresidua.getFlagErgastolo() != null)
       {
	         if(penaresidua.getFlagErgastolo().equals("S"))
	         {
	%>
	           	<td class="l">Pena Detentiva</td>
	           	<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
	<%
	         }
	         else if(penaresidua.getFlagErgastolo().equals("D"))
	         {
	%>
	           	<td class="l">Pena Detentiva</td>
	           	<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
	<%
	         }
       }

        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {
           	if (penaresidua.getDataFine() != null)
           	{
%>

	             <td class="l">Data Fine Pena</td>
	             <td class="L" colspan=2>
	               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
	            </td>

<%       	}

      }
%>
	</tr>
</table>

<!-- Misure di Sicurezza -->
<table> 
<%	List lMisure =(List) request.getAttribute("listaMisureSic");
	if(lMisure.size() > 0) {
    	Iterator itx = lMisure.iterator();
    	while (itx.hasNext()) {
	    	MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
	      	CodMisura = lMis.getCodTipo();
%>
		    <tr>
		    	<td class=C>Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		      	<td class=C> Anni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
		      	<td class=C> Mesi</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
		      	<td class=C> Giorni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
		    </tr>  		
<%		}
	}	%>
</table>

<%--
/* 
* ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
* Numero MEV : SIES v10
* Autore    : gioggi
* Data      : 28/gen/2016
* Branch    : MEV_SIES v10
*/
<%	if(nMis > 0)
	{
%>
	 <table>
	    <tr>
	    	<td class=C>Misura di Sicurezza da espiare</td>
	      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getDescrTipo())%>&nbsp;</font></td>
	      	<td class=C> Anni</td>
	      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumAnni(),"0")%>&nbsp;</font></td>
	      	<td class=C> Mesi</td>
	      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumMesi(),"0")%>&nbsp;</font></td>
	      	<td class=C> Giorni</td>
	      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
	    </tr>  		
    </table>
<%	}
	else
	{ %>  
			<tr><td class="l" > Fascicolo privo di Misure di Sicurezza </td></tr>	
<%	} %>
//***** FINE INTERVENTO MEV_SIES v10 *****//
--%>

	<input type="HIDDEN" title="Codice Misura" value="<%=CodMisura%>" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>" >	
	<br>	
					 <!--Dati sull Ordinanze Sorveglianza -->  
	<table>
		<tr>
			<td class="l">Data Emissione Provvedimento</td>
			<td class="L" colspan=1>
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataEmissione(),"dd-MM-yyyy"))%></font>
			</td>
 		</tr>	
		<tr>
			<td class="l">Data Ricezione</td>
			<td class="L" colspan=1>
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataRicezione(),"dd-MM-yyyy"))%></font>
			</td>
 		</tr>
 
  		<tr>	<!--  Ordinanza/decreto -->
        	<td class="l" width="30%">Tipo Provvedimento Sorveglianza</td>
        	<td class="L" colspan=1 width="60%">
        		<font class="campo">
        			<%=archiviazione.getDescrTipoProvvedimentoArc() %>
        		</font>
        	</td>
        </tr>
        
 	<%	if(archiviazione!=null && archiviazione.getChiaveAnno()!=null && archiviazione.getChiaveProgr()!=null)
 	  	{	%>         
        <tr>
        	<td class="l" width="20%">Anno / Numero SIUS </td>
        	<td class="L" colspan=1 width="60%">
        		<font class="campo">
        			<%=StringUtils.toStringJSP(archiviazione.getChiaveAnno())%>
        			/
        			<%=StringUtils.toStringJSP(archiviazione.getChiaveProgr())%>
        		</font>
        	</td>
        </tr>
<%		}
 	
 		if(eventonotifica.getEvento().getProgrProtocollo() != null)
 		{	%>
        <tr>
        	<td class="l" width="20%">Anno / Numero Provvedimento </td>
        	<td class="L" colspan=1 width="60%">
        		<font class="campo">
        			<%=StringUtils.toStringJSP(eventonotifica.getEvento().getAnnoProtocollo()) %>
        			/
        			<%=StringUtils.toStringJSP(eventonotifica.getEvento().getProgrProtocollo())%>
        		</font>
        	</td>
        </tr>        
<%		} %>

        <tr>	
        	<td class="l" width="30%">Autorità Emittente</td>
        	<td class="L" colspan=1 width="60%">
        		<font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrTipoAutoritaEmittente())%></font>&nbsp;
        		di&nbsp;
        		<font class="campo"><%= StringUtils.toStringJSP(archiviazione.getDescrLuogoEmittente()) %></font>
        	</td>
        </tr>

<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- <% --%>
<!-- if(archiviazione.getCodProvvedimento()!= null) -->
<!-- { -->
<%-- %> --%>
<%--   UFFICIO EMITTENTE  
<tr>
 <td class="l">Provvedimento emesso da</td>
 <td class="L">
   <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrProvvedimento())%></font>
 </td>
</tr>
--%>
<%-- <%    //  } %> --%>
<%--  		AUTORITA' EMITTENTE (LOCALE DELL'EVENTO)
    <tr>	
    	<td class="l" width="30%">Autorità Emittente</td>
    	<td class="L" colspan=1 width="60%">
    		<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrUfficioEmittente())%></font>&nbsp;
    		di&nbsp;
    		<font class="campo"><%= StringUtils.toStringJSP(eventonotifica.getEvento().getDescrLuogoEmittente()) %></font>	
    	</td>
    </tr>
--%>
 		<tr><td>&nbsp;</td></tr>	    
		<tr>
			<td class="l">Data Definizione Procedimento</td>
			<td class="L" colspan=1>
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataDefinizione(),"dd-MM-yyyy"))%></font>
			</td>
 		</tr>
 		
<% 	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{
		NotificaModel lNotMod = eventonotifica.getNotifiche()[0];
%>
		<tr>
          <td class="l">Data Trasmissione</td>
          <td class="L" colspan='3'>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(), "dd-MM-yyyy") )%></font>
          </td>
        </tr>
<%	} %> 

   		<tr>
        	<td class="l" width="30%">Oggetto Definizione Procedimento</td>
        	<td class="L" colspan=1 width="60%">
        		<font class="campo">
        			<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
        		</font>
        	</td>
        </tr>
		<tr>
			<td class="l">Data Definizione Procedimento</td>
			<td class="L" colspan=1>
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataDefinizione(),"dd-MM-yyyy"))%></font>
			</td>
 		</tr>
<%	if(archiviazione.getNote() != null)
	{	%>
       <tr>
        	<td class="l" width="30%">Note </td>
        	<td class="L" colspan=1 width="60%">
        		<font class="campo">
        			<%=archiviazione.getNote() %>
        		</font>
        	</td>
        </tr>	
<%	}

	if(magistrato != null)
	{
%>
      <tr>
       <td class="l">Magistrato firmatario</td>
        <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
        </td>
      </tr>
<% } %>

<%	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{ %>
	<tr>
      <td colspan=2 class="titolo">	Destinatari	</td>
    </tr>
<%	} %>    

<!--Autorita ESTERNA o Altra Autorita 	PRIMA-->
<%	if(lPrimaNotifica.getAutoritaEsterna() != null)
	{ %>
		   <tr>
			    <td class="l" width="30%"> Autorita'</td>
			    <td class="L" colspan=2 width="60%">
			      <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
			      di
			      <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
			    </td>
		   </tr>
<%  
			if(lPrimaNotifica != null &&
			   lPrimaNotifica.getNote()!= null)
			{
%>
		   		<tr>
		    			<td class="l" width="30%">Note</td>
		    			<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>&nbsp;</td>
		   		</tr>
<%			}
	}
%>

<!-- 				Destinatario MDS		 -->
<%	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{ 	
		for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
		{
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//siesLogger.debug("  ---- XXXXXX -- DettaglioOEInterna - Not = "+eventonotifica.getNotifiche()[i]);
			if(eventonotifica.getNotifiche()[i] != null 
			   && ("MS").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica() ) )
			{	%>
					<tr>
				     	<td class="l" width="30%">Destinatario Sorveglianza </td>
			         	<td class="L" colspan=2 width="60%">
			            	<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio()) %>
			            	</font>&nbsp;
							di 
			          		<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrComune() ) %>
			          		</font>&nbsp;
			            </td>
			        </tr>
<%			}
		}
	}	%>
	
	<!-- 		Istituto Detenzione		 -->
<% 	for (int i=0;i< eventonotifica.getNotifiche().length; i++)
	{
	  NotificaModel lNotMod = eventonotifica.getNotifiche()[i];
	
	  if(lNotMod != null && lNotMod.getIstitutoDetenzione() != null )
	  { 
	  %>
	    <tr>
	     <td class="l">Struttura Designata </td>
	     <td class="l" colspan='3'>
	      <font class="campo"> <%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
	      <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrizione())%></font>
	     </td>
	    </tr>
	
	<%} %>
	
	<!-- 		Difensori	 -->  
<% 	  if(   lNotMod.getCodTipoNotifica().equals("ND") 
	     && lNotMod.getAutoritaEsterna()!=null 
	     && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null
	    )
	  {
	     AvvocatoSiepModel   lAvvMod = lNotMod.getAvvSiep();
	     AutoritaEsternaModel lAuMod = lNotMod.getAutoritaEsterna();
	  %>
	  <tr>
	    <td class="l">Notifica al Difensore</td>
	    <td class="L" colspan="3">
	      <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
	      &nbsp;Foro di&nbsp;
	      <font class="campo">
	        <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
	      </font>
	      &nbsp;Difensore di&nbsp;
	      <font class="campo">
	        <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
	      </font>
	    </td>
	  </tr>
	  <tr>
	    <%  if (StringUtils.toStringJSP( lAuMod.getCodTipoAutorita().trim()).compareTo("C0") == 0){ %>
	        <td class="l" width="30%"> tramite </td>
	        <td class="L" colspan=3 width="60%">
	          <font class="campo"><%=lAuMod.getDescrTipoAutorita().trim()%></font>&nbsp;
	    <%  } else { %>
	        <td class="l" width="30%"> presso </td>
	        <td class="L" colspan=3 width="60%">
	          <font class="campo"><%=lAuMod.getDescrTipoAutorita().trim()%></font>&nbsp;
	          di
	          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
	        </td>
	    <%  }  %>
	  </tr>
	  
	<% } // end if
	} // end for
	%>
	</table>
	<br>
<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->
<br>
 <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
 		<table>
 			<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
 			<tr>
          		<td class="L">
            		<input class="bottone" type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.misurasicurezza.action.ActUploadEsecuzioneMS">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= eventonotifica.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActDettaglioArchiviazionePerProvvSorveglianza">
    			</td>
  			</tr>
		</table>
	</FORM>	
</div>
<%-- MEV_39: aggiunto tasto di reindirizzamento --%>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& "S".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) {
%>
	<div id="restituzione" align="left" style="position:relative; top: -111px;">
	<form method="POST" name="restituzione" action="<%=IWebConstants.PG_MAIN%>">
	<table>
        <tr>
          	<td class="L">
            	<input class="bottone" type="submit" name="restituzione" value="Restituzione Comunicazione Ordine di Consegna">
            	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActLoadInserisciRestituzioneOrdineConsegna">
          	</td>
		</tr>
	</table>
	</form>
	</div>
<%
}
%>
</body>
</html>