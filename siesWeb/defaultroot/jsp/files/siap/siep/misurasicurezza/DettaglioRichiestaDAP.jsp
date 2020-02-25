<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<jsp:useBean id="eventonotifica"      		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizione"					scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="MisuraModel"				scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="tornadavalida"				scope="request" class="java.lang.String"/>

<%

FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

MagistratoModel lMagistrato = eventonotifica.getMagistrato();
if( lMagistrato == null)
  lMagistrato = new MagistratoModel();

// Gestione Autorità Esterne sulle Notifiche - ( Notifica Fissa : DAP))
String lCodTipoAutorita = "-";
NotificaModel lPrimaNotifica = new NotificaModel();
AutoritaEsternaModel lPrimaAutoritaEsterna = new AutoritaEsternaModel();

if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
	    if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null)
	    {
	      	lCodTipoAutorita = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodTipoAutorita();
	    }
	
	    lPrimaNotifica = eventonotifica.getNotifiche()[0];
      	lPrimaAutoritaEsterna = lPrimaNotifica.getAutoritaEsterna();
}

%>
<!--  DettaglioRichiestaDAP -->
<html>
  <head>
    <title>[S.I.E.S.] -Esecuzione Misure sicurezza- Dettaglio Richiesta al DAP di Designazione Istituto</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
 	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
	<script language="JavaScript">
	
	   function stampaSiep(lAzione)
	   {
	      var  hrefStampa = lAzione;
	      var lIndice = hrefStampa.indexOf("?");
	
	      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
	
	      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
	   }	   
	</script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Richista D.A.P. di Designazione Istituto</font>
      </td>
<%
  	if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    {%>
   		<!-- BOTTONE DI STAMPA -->
<%-- 		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>"> --%>
<%-- 			<jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaRichiestaDAP&autorita="+lCodTipoAutorita+"&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo()+"&IdPosizioneGiuridica="+posizione.getCodPosizioneGiuridica()%>"/> --%>
<%-- 		</jsp:include> --%>
		<%-- MEV_39: aggiunti pulsanti di validazione e cancellazione --%>
		<!-- BOTTONE DI STAMPA -->
		<td class="LBG">
      			<a  href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaRichiestaDAP&autorita=<%=lCodTipoAutorita%>&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&CodMotivo=<%=eventonotifica.getEvento().getCodMotivo()%>&CodPosizioneGiuridica=<%=posizione.getCodPosizioneGiuridica()%>')" onclick="javascript:lookUpload();">
       			 <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      			</a>
   		</td>
		<!-- BOTTONE DI VALIDAZIONE -->
   		<td class="LBG">
   			<a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadEsecuzioneMS&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActDettaglioRichiestaDAP&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
    			<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
   			</a>
   		</td>
		<!-- ICONA DI MODIFICA -->
		<td class="LBG">
			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaRichiestaDAP&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10">
        		<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
       		</a>
       	</td>
       	<!-- ICONA DI CANCELLAZIONE -->
		<td class="LBG">
  			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActModificaRichiestaDAP&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&modalita=D&TornaQui=10">
   				<img align="middle" src="/images/delete24.gif" alt="Elimina" width="24" height="24" border="0">
   			</a>
		</td>
<%   }%>

    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<!--  Posizione Giuridica -->  
	<table>
    	<tr>
      		<td class="l" width="20%">Posizione Giuridica </td>
      		<td class="L" colspan=5 width="30%">
        		<font class="campo">
         		<%=posizione.getDescrPosizioneGiuridica()%>
  	    		</font>
      		</td>
        	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
    	</tr>
    </table>
<!--   // Eventuali Misura di Sicurezza   -->
	<table>
<%
	List lMisure =(List) request.getAttribute("listaMisure");
	if(lMisure.size() > 0)
	{
	    Iterator itx = lMisure.iterator();
	    while ( itx.hasNext())
	    {
		      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
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
	}    %>			 		
    </table>
 <!--Richiesta -->  
	<table width="80%" >
  		<tr>
  			<td class="Titolo" align="center" colspan="4" > Richiesta </td>
  		</tr>
  		<br>
   		<tr>
        	<td class="l" width="20%">Motivo  </td>
        	<td class="L" colspan=2 width="60%">
        		<font class="campo">
        			<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
        		</font>
        	</td>
        </tr>
        <tr>	
        	<td class="l" width="20%">Data Emissione</td>
	    	<td class="L" colspan=2  width="60%">
	      		<font class="campo">
	      			<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
	      		</font>
	    	</td>
        </tr>
     	
  <!--Magistrato Firmatario -->   

  <tr>
     <td class="l" width="20%">Magistrato firmatario</td>
     <td class="L" width="60%">
          <font class="campo">
            <%=StringUtils.toStringJSP(lMagistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
          </font>
      </td>
      
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(lMagistrato.getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
  </tr>
<!--Autorita ESTERNA o Altra Autorita 	PRIMA-->

<%	if(lPrimaNotifica.getAutoritaEsterna() != null)
	{ %>
		   <tr>
			    <td class="l" width="20%">Destinatario</td>
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
		    			<td class="l" width="20%">Note</td>
		    			<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>&nbsp;</td>
		   		</tr>
<%			}
		}
%>
<!--  Eventuali ulteriori Notizie -->
<%	if(eventonotifica != null && eventonotifica.getCampoNote().length != 0) 
	{ %>
			<tr>
		    	<td class="l" width="20%">Eventuali Ulteriori Notizie</td>
		    	<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr() )%></font>&nbsp;</td>
		   	</tr>
<%	} %>

</table>
<br>

<%if((eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null) &&
    (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) )
  { %>
  		<form name="comandiperDesIst" method="POST" action="/jsp/Main.jsp">
		  <table>
        	<tr>
          		<td class="L">
            	  <INPUT  class="bottone" type="submit" name="ORDESEC" value="Designazione Istituto">
		    	  <input type="HIDDEN" name="motivo" value="<%=eventonotifica.getEvento().getCodMotivo()%>">
				  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
		    	  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActDettaglioRichiestaDAP">
				  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActLoadInserisciDesignazioneIstituto">
          		</td>
        	</tr>
      	  </table>
    	</form>    	     
<%} %>

  <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class="bottone" type="submit" value="Conferma">
            <input type="HIDDEN" name="motivo" value="<%=eventonotifica.getEvento().getCodMotivo()%>">
            <input type="HIDDEN" name="autorita" value="<%=lPrimaAutoritaEsterna.getCodTipoAutorita()%>">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadEsecuzioneMS">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActDettaglioRichiestaDAP">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
<br>
<br>
</body>
</html>