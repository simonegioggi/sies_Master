<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.sezione.model.SezioneModel"%>
<%@ page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>

<jsp:useBean id="evento"  						scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="udienza"         				scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="ProvvedimentoEvento" 			scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="udienzaProcedimentoSige" scope="request" class="siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel"/>
<jsp:useBean id="modalita"        				scope="request" class="java.lang.String"/>
<jsp:useBean id="tenori"          				scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"        				scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="tipoGiudizioVal"        scope="request" class="java.lang.String"/>

<%
  FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
  EventoModel lEve = evento;
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  
  String aula="&nbsp;";
  String ingresso="&nbsp;";
  String piano="&nbsp;";
  String sezione="&nbsp;";
  
  AulaUdienzaModel aulaUdienzaModel=udienza.getAulaUdienzaModel();
  if (aulaUdienzaModel != null) {
      aula=(aulaUdienzaModel.getDescrizioneStanza()==null?"&nbsp;":aulaUdienzaModel.getDescrizioneAula());
  	  ingresso=(aulaUdienzaModel.getDescrizioneIngresso()==null?"&nbsp;":aulaUdienzaModel.getDescrizioneIngresso());
  	  piano=(aulaUdienzaModel.getNumeroPiano()==null?"&nbsp;":aulaUdienzaModel.getNumeroPiano().toString());
  }

  SezioneModel sezioneModel=udienza.getSezioneModel();
  if (sezioneModel != null) {
  	  sezione=(sezioneModel.getDescrizione()==null?"&nbsp;":sezioneModel.getDescrizione());
  }

  String oraInizio = (udienza.getOraInizio()== null || udienza.getOraInizio().equalsIgnoreCase("NULL")?"":udienza.getOraInizio());
  String minInizio = (udienza.getMinInizio()== null || udienza.getMinInizio().equalsIgnoreCase("NULL")?"":udienza.getMinInizio());
  String oraFine = (udienza.getOraFine()== null || udienza.getOraFine().equalsIgnoreCase("NULL")?"":udienza.getOraFine());
  String minFine = (udienza.getMinFine()== null || udienza.getMinFine().equalsIgnoreCase("NULL")?"":udienza.getMinFine());

  String orarioInizio=oraInizio +":"+minInizio;
  String orarioFine=oraFine +":"+minFine;
  
  boolean modificabile=false;
  if (lEve.getFlagDocumentoRegistrato() == null || !lEve.getFlagDocumentoRegistrato().equalsIgnoreCase("S")) {
	  modificabile=true;
  }
  
  RedirectTo lRedir=new RedirectTo(); 
  lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=O&IdEvento="+lEve.getIdEvento()+"&IdUdienzaSige="+udienza.getIdUdienzaSige()+"&IdUdienzaProcedimentoSige="+udienzaProcedimentoSige.getIdUdienzaProcedimentoSige()+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
  lRedir.setParameter("TornaQui", TornaQui);
  String lLinkParteOffesa = lRedir.toString();

  lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=C&IdEvento="+lEve.getIdEvento()+"&IdUdienzaSige="+udienza.getIdUdienzaSige()+"&IdUdienzaProcedimentoSige="+udienzaProcedimentoSige.getIdUdienzaProcedimentoSige()+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
  lRedir.setParameter("TornaQui", TornaQui);
  String lLinkParteCivile = lRedir.toString();
  
%>
<script language="JavaScript" src="/html/conferma.js"> </script>
<script type="text/javascript">
//
// Funzione JS per chiamata azione inserimento parti offese/civili
//   
function InserisciParte( tipoParte ){
	var lLink;
	if( tipoParte == 'O' ){
		lLink = "<%=lLinkParteOffesa%>";
	} else {
		lLink = "<%=lLinkParteCivile%>";
	}		 
	window.location=lLink;	
}
</script>
<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Ordinanza Rinvio Udienza da Verbale - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" >
      function lookUpload() {
        var node;
        node=document.getElementById('upld');
			  node.style.visibility='visible';
      }
    </script>
  </head>

<%
	if((lEve.getFlagDocumentoRegistrato()!= null) &&
		 (lEve.getFlagDocumentoRegistrato().compareTo("N") == 0) ) 
	{%>
   	<BODY class="corpo" onload="javascript:lookUpload();">
<%}else{%>
		<BODY class="corpo">
<%}
%>
    <table>
      <tr>
				<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
					</a>
				</td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Ordinanza Rinvio Udienza da Verbale</font>
        </td>
<%
	//Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO
	//if (modificabile) {
%>
<%--
<td class="LBG">
	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienzaprocedimento.action.ActLoadModificaOrdinanzaRinvioUdienza&IdEvento=<%=lEve.getIdEvento()%>&ritorno=si&TornaQui=<%=TornaQui%>">
		<img align="absmiddle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Udienza" width="24" height="24" border="0">
	</a>
</td>
--%>
<%    	
    //}
    //Modifica del 24/02/2016 Nuova Infrastruttura - FINE

	if (request.getAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE) != null ){
%>
		<%-- Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO --%>
		<jsp:include page="<%=ICostantiUdienzaSige.PG_BUTTONS%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" />
			<jsp:param name="ValoreIdEntita" value="<%=request.getAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)%>" />
			<jsp:param name="CampoIdEntitaEve" value="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" />
			<jsp:param name="ValoreIdEntitaEve" value="<%=lEve.getIdEvento()%>" />
		</jsp:include>
		<%-- Modifica del 24/02/2016 Nuova Infrastruttura - FINE --%>
<%
	}

	if ((lEve.getFlagDocumentoRegistrato()==null) || 
	    (lEve.getFlagDocumentoRegistrato()!=null) && 
	    (lEve.getFlagDocumentoRegistrato().compareTo("N")==0)){
%>
		<!-- BOTTONE DI STAMPA -->
		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIGE%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
			<jsp:param name="ValoreIdEntita" value="<%=lEve.getIdEvento()%>" />
		</jsp:include>
<%
	}
%>
  	<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
  	</tr>

  	<tr>
    	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
  	</tr>

  	<tr>
    	<jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>"/>
  	</tr>

  	<tr>
			<jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>"/>
  	</tr>        
 </table>
 <br>
	<table cellspacing=2 cellpadding=2 width="95%">
		 <tr><td class="Titolo" colspan=6 >PARTI CIVILI</td></tr>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
			<jsp:param name="tipoParte" value="C"/>
		</jsp:include>
<%	if( modificabile ) {%>
		<tr>
			<td class="label">
				<a class="cliccabile" href="Javascript:InserisciParte('C');">Gestione Parti Civili</a>
			</td>
		</tr>
<%}%>
	</table>
	<br>
	<table cellspacing=2 cellpadding=2 width="95%">
		 <tr><td class="Titolo" colspan=6 >PARTI OFFESE</td></tr>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
			<jsp:param name="tipoParte" value="O"/>
		</jsp:include>
<%	if( modificabile ) {%>
		<tr>
			<td class="label">
				<a class="cliccabile" href="Javascript:InserisciParte('O');">Gestione Parti Offese</a>
			</td>
		</tr>
<%}%>
	</table>
 
 
 <table cellspacing=2 cellpadding=2  width="95%">
    <tr>
      <td class="Titolo" colspan="5">Dati Provvedimento</td>
    </tr>
    
    <tr>
            <td class="l" width="20%">Data Emissione</td>
                <td class="L" width="80%">
                    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
                </td>
     </tr>
 <%    
     if (ProvvedimentoEvento.getProvvedimento().getDataDeposito() != null)
		{%>
  		<tr>
    		<td class="l">
    		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO%>=<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%><%=retParam%>">
         		Dettaglio Deposito Decreto
      		</a>
    		</td>
    		<td class="l">
      		
    		</td>
  		</tr>
  		<tr>
    		<td class="l"> Data Deposito in Cancelleria</td>
    		<td class="l"><font class="campo"> <%=DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy")%></font></td>
  		</tr>
	<%}%>
	
	<tr>
    	<td class="l"> Stato del provvedimento</td>
<% 		if (ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null && ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A"))
			{%>
    		<td class="l"><font class="cRosso">ANNULLATO</font></td>
<% 		} else if (ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null && ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
   		{%>
    		<td class="l"><font class="campo">Validato</font></td>
		<%} else
   		{%>
    		<td class="l"><font class="campo">Da Validare </font></td>
		<%}%>
		</tr>
     
     
    
   </table> 
		
  <table cellspacing=2 cellpadding=2  width="95%">
    <tr>
      <td class="Titolo" colspan="5">Udienza</td>
    </tr>

<%
  	//  if (DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"yyyyMMdd").trim().compareTo(DateUtils.getDateToString(udienza.getDataUdienza(),"yyyyMMdd").trim() ) != 0 )
  	if (!udienzaProcedimentoSige.getFlagRinviata().equalsIgnoreCase("N"))
  	{
%>
      <tr>
        <td class="l"><font class="Label">Nuova Data Udienza </font></td>
      	<td class="L">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy"))%>
					</font>&nbsp;
      	</td>
      </tr>
      
      <%

if(udienza != null && udienza.getCodGiudice()!= null){
%>        
	      <td class="l">Giudice</td>
	      <td class="L" >
	        <font class="campo">
	        		<%=StringUtils.toStringJSP(udienza.getDescrGiudice()) %>
	      	</font>
	      </td>
<%
	  } else { 
%>        <td width="25%">&nbsp;</td> 
		  <td width="100%">&nbsp;</td> 
<%
	  }	  
%>      
    </tr>     
 

      <tr>
      <td class="l">Sezione</td>
      <td class="l">
        <font class="campo">
        <%=sezione %>
        </font>
      </td>
      <td class="l">
      Aula
      <font class="campo">
        <%=aula %>
      </font>
      </td>
      <td class="l">
      Ingresso
      <font class="campo">
        <%=ingresso %>
      </font> 
      </td> 
      <td class="l">
      Piano
        <font class="campo">
        <%=piano %>
      </font>  
      </td>
    </tr>

    <tr>
      <td class="l">Orario Inizio (ora:min)</td>
      <td class="l">
      <font class="campo">
        <%=orarioInizio %>
      </font>
      </td>
      <td class="l">Orario Fine (ora:min)</td>
      <td class="l">
        <font class="campo">
        <%=orarioFine %>
         </font>      
      </td>
    </tr>
      

    	<tr>
      	<td class="l" nowrap><font class="Label">Luogo Svolgimento Udienza</font></td>
      	<td class="L">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(ProvvedimentoEvento.getProvvedimento().getLuogoSvolgimento())%>
					</font>&nbsp;
      	</td>
    	</tr>
<%
    }
  	else
  	{
%>
      <tr>
        <td class="label">Il Procedimento è stato rinviato a nuovo ruolo</td>
      </tr>
      <tr>
        <td class="label">&nbsp;</td>
      </tr>
<%
    }
%>
 </table>
	<br>
  <table cellspacing=2 cellpadding=2  width="95%">
    <tr>
      <td class="Titolo">Oggetti</td>
    </tr>
					<tr>
		<jsp:include page="/jsp/files/siap/sige/tenore/ElencoDettaglioOggetti.jsp"/>
					</tr>
<%-- <% --%>
<!--  			Iterator itx = tenori.iterator(); -->
<!--  			String lIdTenore = ""; -->
<!--  			while ( itx.hasNext()){ -->
<!--  				TenoreSigeModel lTenore = (TenoreSigeModel)itx.next(); -->
<!--  				if (lTenore != null &&   -->
<!--  				    !lTenore.getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore)) { -->
<%-- %>   		 --%>
<!-- 					<tr> -->
<!-- 						<td class="L"><font class="label"> -->
<%-- 							<%=lTenore.getDescrOggettoSige()%></font> --%>
<!-- 						</td> -->
<!-- 					</tr> -->
<%-- <%			 --%>
<!--  				} -->
<!--  			} -->
<%-- %> --%>
		<tr>
  </table>
	<br>
	<form name="dettaglio">
 		<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
 	</form>
   <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
     <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"   value="<%= lEve.getIdEvento()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sige.udienza.action.ActUploadFissazioneUdienza">
          </td>
        </tr>
     </table>
    </FORM>
   </div>
  </body>
</html>