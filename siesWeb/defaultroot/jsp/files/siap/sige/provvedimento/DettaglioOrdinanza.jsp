<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.web.RedirectTo"%>

<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="magistratoassegnatario" 	scope="request" class="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel" />
<jsp:useBean id="avvocato"		scope="request" class="java.util.Vector" />
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="Modificabile"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="Cancellabile"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="UdienzaSige"	      scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="IdUdienzaEvento"        scope="request" class="java.lang.Object"/>
<jsp:useBean id="IdUdienzaSige"        scope="request" class="java.lang.Object"/>
<jsp:useBean id="IdUdienzaProcedimentoSige"        scope="request" class="java.lang.Object"/>
<jsp:useBean id="tipoGiudizioVal"     		scope="request" class="java.lang.String"/>

<%
BigDecimal IdEvento = (BigDecimal) request.getAttribute("IdEvento");

// Estrazione del provvedimento dal model strutturato
ProvvedimentoSigeModel provvedimento = ProvvedimentoEvento.getProvvedimento();
EventoNotificaModel lEve=ProvvedimentoEvento.getEventoNotifica();

String FlagDocReg = lEve.getEvento().getFlagDocumentoRegistrato();

if (FlagDocReg == null)
	FlagDocReg="N";

BigDecimal idProvvedimento=provvedimento.getIdProvvedimentoSige();
request.setAttribute("idProvvedimentoTitoliEsecutivi", idProvvedimento);

String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";

// Fascicolo SIGE
// FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
// Richiesta SIGE
// RichiestaSigeModel lRichiesta = FascicoloSigeEsteso.getRichiestaSige();
//Magistrato Assegnatario
//MagistratoAssegnatarioModel magistratoassegnatario = FascicoloSigeEsteso.getMagAssegnatario();

String dataUdienza=null;
if( UdienzaSige!=null && UdienzaSige.getIdUdienzaSige() != null  ) {
	dataUdienza = DateUtils.getDateToString(UdienzaSige.getDataUdienza(),"dd/MM/yyyy");
}
else{	
	if (ProvvedimentoEvento.getProvvedimento()!=null && ProvvedimentoEvento.getProvvedimento().getUdienzaSige()!=null
			&& ProvvedimentoEvento.getProvvedimento().getUdienzaSige().getDataUdienza()!=null){
		dataUdienza=DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getUdienzaSige().getDataUdienza(),"dd/MM/yyyy");
	}
}
if (dataUdienza==null)
	dataUdienza="-";

// Link alla Gestione Oggetti 
RedirectTo lRedir = new RedirectTo();
lRedir.setPage(IWebConstants.PG_MAIN);
lRedir.setAction("siap.sige.tenore.action.ActLoadDettaglioOggettiProv");
lRedir.setParameter("TornaQui", TornaQui );
String lLinkOggettiSessione = lRedir.toString();

boolean retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
boolean ulterioreDescrizione = false;
  
// Flag per indicare la modalità di Modifica Ordinanza
boolean modificaOrdinanza = false;
if (modalita != null && modalita.trim().equalsIgnoreCase("M"))
	modificaOrdinanza = true;

String titolo = "Dettaglio Ordinanza";
if (modificaOrdinanza)
	titolo = "Modifica Ordinanza";

//Fascicolo SIGE in sessione
FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();

String sIdUdienzaEvento = "";
if (IdUdienzaEvento!=null){
	sIdUdienzaEvento = IdUdienzaEvento.toString();
}

String sIdUdienzaSige = "";
if (IdUdienzaSige!=null){
	sIdUdienzaSige = IdUdienzaSige.toString();
}

String sIdUdienzaProcedimentoSige = "";
if (IdUdienzaProcedimentoSige!=null){
	sIdUdienzaProcedimentoSige = IdUdienzaProcedimentoSige.toString();
}

lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=O&IdEvento="+sIdUdienzaEvento+"&IdUdienzaSige="+sIdUdienzaSige+"&IdUdienzaProcedimentoSige="+sIdUdienzaProcedimentoSige+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
lRedir.setParameter("TornaQui", TornaQui);
String lLinkParteOffesa = lRedir.toString();

lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=C&IdEvento="+sIdUdienzaEvento+"&IdUdienzaSige="+sIdUdienzaSige+"&IdUdienzaProcedimentoSige="+sIdUdienzaProcedimentoSige+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
lRedir.setParameter("TornaQui", TornaQui);
String lLinkParteCivile = lRedir.toString();

boolean isValidable=true;
if (provvedimento.getDataEmissione() == null)
	isValidable=false;
%>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Ordinanza SIGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
        var desktop;

        function  Verifica() {
          var ritorno = true;

<%--           <% if (magistratoassegnatario == null )	{ %> --%>
//               ritorno = false;
<%--           <% } %> --%>

          if (! ritorno)
          alert (" Magistrato non assegnato!");

          return ritorno;
        }
    </script>

    <script language="JavaScript">
    function Verify() {
    	var ritorno = true;
    	
    	return ritorno;
    }

    //
    // Funzione JS per chiamta azione inserimento parti offese/civili
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
    
    function verificaValidazione() {
    	if (<%=isValidable%>) {
    	    document.forms["comandi"].submit();
    	    return;
    	}
    	
    	alert ("Impostare la Data di Emissione dell'Ordinanza");
    }
    
    function InserisciDepositoDecreto (){
		lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadInserisciDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=provvedimento.getIdEventoGenerato()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvedimento.getIdProvvedimentoSige()%>&TornaQui=<%=TornaQui%>&isUdienza=false";
		window.location=lLink;
	}
    </script>

  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :  </font>&nbsp;
          <font class="campo">Dettaglio Ordinanza</font>
      </td>

<%
			if (Modificabile == null || Modificabile.trim().length() < 1)
    			Modificabile = "SI";

			if (Stampabile == null || Stampabile.trim().length() < 1)
    			Stampabile = "SI";
	
			// Nel caso di Modifica Ordinanza si elimina stampa e cancellazione
			if (modificaOrdinanza) {
				Modificabile = "NO";
				Stampabile = "NO";
			}

  		if (Stampabile.compareTo("SI") == 0) {
    		// Deve esistere il template : da list o predefinito.
    		if ( ((ElencoTemplate != null) && (ElencoTemplate.trim().length() > 0)) || (ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate() != null && ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate().trim().length() > 1 )) {
%>
  				<!-- BOTTONE DI STAMPA -->
    			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIGE%>">
  					<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
    				<jsp:param name="ValoreIdEntita" value="<%=ProvvedimentoEvento.getEventoNotifica().getEvento().getIdEvento()%>"/>
  				</jsp:include>
<%
     		} /* endif esistenza ElencoTemplate */
  		}  /* endif Stampabile = SI */
  		
			// Nel caso di Evento già validato con stampa, si consente di riprodurre il documento.  		
		
			if (Stampabile.compareTo("NO") == 0 &&
					ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null &&
				  ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0	) {
%>
				<!--- BOTTONE DI DOWNLOAD DOCUMENTO -->
      <td class="LBG">
       <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=ProvvedimentoEvento.getEventoNotifica().getEvento().getIdEvento()%>')">
        <img src="/images/print24.gif" alt="Visualizza Ordinanza" width="24" height="24" border="0">
        </a>
      </td> 
<%
		}
  		
  		if (Modificabile.compareTo("SI") == 0) {
%>
  		  <!-- BOTTONE DI MODIFICA -->
  		        <td class="LBG">
  		                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadModificaProvvedimento&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvedimento.getIdProvvedimentoSige()%>&ritorno=si&TornaQui=<%=TornaQui%>">
  		                <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
  		                </a>
  		              </td>
<%      } if (Cancellabile.compareTo("SI") == 0) { %>
			<!--- BOTTONE DI CANCELLAZIONE -->
    		<td class="LBG">
      		<a href="Javascript:conferma('siap.sige.provvedimento.action.ActCancellaProvvedimento','<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>','<%=provvedimento.getIdProvvedimentoSige()%>','TornaQui','<%=TornaQui%>');">
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      		</a>
    		</td>
<%
  }
%>
  		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>
</table>

<%  if (Modificabile.compareTo("SI")==0) {%> 
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
    	<jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
        	<jsp:param name="MagAssRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza"/>
	    </jsp:include>
    </tr>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      		<jsp:param name="AvvRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza"/>
      </jsp:include>
</table>

	<table cellspacing=2 cellpadding=2 width="95%">
		 <tr><td class="Titolo" colspan=6 >PARTI CIVILI</td></tr>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
			<jsp:param name="tipoParte" value="C"/>
		</jsp:include>
		<tr>
			<td class="label">
				<a class="cliccabile" href="Javascript:InserisciParte('C');">Gestione Parti Civili</a>
			</td>
		</tr>
	</table>

	<table cellspacing=2 cellpadding=2 width="95%">
		 <tr><td class="Titolo" colspan=6 >PARTI OFFESE</td></tr>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
			<jsp:param name="tipoParte" value="O"/>
		</jsp:include>
		<tr>
			<td class="label">
				<a class="cliccabile" href="Javascript:InserisciParte('O');">Gestione Parti Offese</a>
			</td>
		</tr>
	</table>
<%} else {%>
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
    	<jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>"/>
    </tr>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>"/>
	</table>
<%}%>

<br>
  <table cellspacing=2 cellpadding=2 width="95%">
      <jsp:include page="<%=ICostantiProvvedimentoSige.PG_INCLUDE_DATI_PROVVEDIMENTO%>"/>
  </table>

  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo">Oggetti Titoli Esecutivi</td>
      <td class="Titolo">Esito</td>
      <td class="Titolo" colspan="2">Scarico Esito</td>
    </tr>
     <tr>
      <td class="Titolo">&nbsp;</td>
      <td class="Titolo">&nbsp;</td>
      <td class="Titolo">Differenziato per Sentenza-Reato</td>
      <td class="Titolo">Unico per Oggetto</td>
    </tr>
    <tr>
  		<div id="elenco1" style="width: 100%; display:block" >
 			<jsp:include page="/jsp/files/siap/sige/tenore/ElencoGestioneTenori.jsp"/>
  		</div>
	</tr>
  </table>
  <br>

  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo" colspan="2"> Dati Udienza </td>
    </tr>
    <tr>
      <td class="L"><font class="label"> Tipo Giudizio </font></td>
      <td class="L"><font class="campo"> <%=("C".equals(tipoGiudizioVal))?"Collegiale":(("M".equals(tipoGiudizioVal))?"Monocratica":"-")%></font></td>
    </tr>
    <tr>
      <td class="L"><font class="label"> Data Udienza </font></td>
      <td class="L"><font class="campo"> <%=dataUdienza%></font></td>
    </tr>
  </table>

<%
  if (FlagDocReg.equalsIgnoreCase("S") && provvedimento.getDataDeposito()==null) {
%>	  
  <br />
  <table>
    <tr>
      <td>
        <input class="bottone" type="button" value="Deposito Ordinanza" onclick="InserisciDepositoDecreto()" >
      </td>
    </tr>
    </table>
<%
  }
%>    
  
  

 <form name="dettaglio">
<%  
  if(provvedimento.getCodTipoProvvedimento().trim().length() > 1 )  {
	  // Combo template di stampa solo sulle Emissioni di Ordinanza Nuove
	  // E se non esiste il template predefinito.
	  if(ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate() == null || ProvvedimentoEvento.getEventoNotifica().getEvento().getTemIdTemplate().trim().length() < 2 ) {
%>
      <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
<%
     }
   } // endif cod tipo ordinanza != null
%>
</form>

  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="button" value="Conferma" onclick="javascript:verificaValidazione();">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
            <input type="HIDDEN" name="IdEvento"  value="<%=IdEvento%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sige.provvedimento.action.ActLoadDettaglioOrdinanza">
          </td>
        </tr>
        </table>
        </FORM>
      </div>
  
   </body>
</html>