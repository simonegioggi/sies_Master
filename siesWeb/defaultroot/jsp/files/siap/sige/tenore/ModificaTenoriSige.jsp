<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeModel"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.richiesta.model.RichiestaSigeModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiDecodifiche"%>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel"%>
<%@ page import="siap.sico.decodifiche.model.OggettiModel" %>

<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="f3b.security.model.FunctionModel"%>
 
<jsp:useBean id="tenore"       scope="request" class="siap.sige.tenore.model.TenoreSigeEstesoModel"/>
<jsp:useBean id="sentenze"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="idRichiesta"  scope="request" class="java.lang.String"/>
<jsp:useBean id="idTenore"     scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="listaOggetti" scope="request" class="java.util.Vector" />
<jsp:useBean id="contenuto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetto"   scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetto"  scope="request" class="java.lang.String"/>
<jsp:useBean id="codSentenza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="descSentenza" scope="request" class="java.lang.String"/>
<jsp:useBean id="idReato"      scope="request" class="java.lang.String"/>
<jsp:useBean id="descReato"    scope="request" class="java.lang.String"/>
<!-- EC 18032019 intervento post COLLAUDO 11.2 adeguamento all'AF -->
<jsp:useBean id="codContenuto"   scope="request" class="java.lang.String"/>
<!-- 20190508 [SG]: aggiunta variabile -->
<%-- <jsp:useBean id="oggettiSessione"	scope="request" class="java.lang.String"/> --%>

<% 
// Passaggio del titolo
String lTitolo = "Modifica Oggetti Relativi alla Richiesta/Atto";
if (titolo != null && titolo.length() > 0)
	lTitolo = titolo;

// MERGE v10 COLLAUDO: modificata action di riferimento >>> ActModificaOggetto
String lActionModifica = "siap.sige.tenore.action.ActModificaOggettoRichiesta";
// 20190508 [SG]: aggiunta variabile
// if (Utils.isPresent(oggettiSessione) && "SI".equals(oggettiSessione))
// 	lActionModifica = "siap.sige.tenore.action.ActModificaOggetto";
String codContenutoOld = codContenuto;

 // Funzioni abilitate
Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
if( (lFunFiglie != null) && (lFunFiglie.size() != 0) ) {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext()) {
      lFun = (FunctionModel)lIterBottoni.next();
      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)) {
        	lActionModifica = lFun.getNameAction();
        }
      }
	} // endWhile
} // endif
%>

<html>
<head>
<title>[S.I.A.P.] - Modifica Tenori Sige </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<script language="JavaScript">
	var desktop;

	// Chiamata funzione lista Oggetti.
function ListaOggetti(a_formname, a_fieldname, a_fieldcode) {
	// 20190513 [SG]: aggiunto controllo
	// se è stato selezionato un contenuto nella combo questo link non deve esere usato    	
	var codC = document.ModificaTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO_SEL%>.value;    	
	if (codC.length > 0 && codC != '-') {
		alert('E stato già selezionato un contentuto. Questo pulsante è non utilizzabile!');
		return;
	}
	  // Compone il link URL per passare i parametri alla pagina jsp
	  var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggettiSige";
	      aLink += "&formname=" + a_formname;
	      aLink += "&fieldname=" + a_fieldname;
	      aLink += "&fieldcode=" + a_fieldcode;
	  desktop = window.open(aLink, "Lista_Oggetti_Sige","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
	}

	// Chiamata funzione lista Oggetti per Contenuto.
function ListaOggettiPerContenuto(a_formname, a_fieldname, a_fieldcode, codContenuto) {
	    var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggettiSigePerContenuto";
	        aLink += "&formname=" + a_formname;
	        aLink += "&fieldname=" + a_fieldname;
	        aLink += "&fieldcode=" + a_fieldcode;
	        aLink += "&CodContenuto=" + codContenuto;
		  desktop = window.open(aLink, "Lista_Oggetti_Sige_Per_Contenuto","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

	// Chiamata funzione lista Titoli Esecutivi.
function ListaTitoliEsecutivi(a_formname, a_fieldname, a_fieldcode) {
	  // Compone il link URL per passare i parametri alla pagina jsp
	  var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaTitoliEsecutiviSige";
	      aLink += "&formname=" + a_formname;
	      aLink += "&fieldname=" + a_fieldname;
	      aLink += "&fieldcode=" + a_fieldcode;
	  desktop = window.open(aLink, "Lista_Titoli_Esecutivi","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
	}

	// Chiamata funzione Lista Reati.
function ListaReati(a_formname, a_fieldnameDesc, a_fieldnameCod) {
		var idSentenza = document.ModificaTenoreSige.<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>.value;
		
		var idSentSplit;
		idSentSplit = idSentenza.split("|");
		// se sono stati selezionati più Titoli Esecutivi
		// non è possibile selezionare i Reati.
		if(idSentSplit.length > 2){
			alert("Non è possibile selezionare i Reati, per la presenza di più Titoli Esecutivi.");      		
		} else {
			if(idSentenza.indexOf("|") != -1){
				idSentenza = idSentenza.substring(0, idSentenza.length -1);
			}
	
			// Compone il link URL per passare i parametri alla ElencoReati.JSP
	      var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.tenore.action.ActRicercaReati";
	          aLink += "&formname="+a_formname;
	          aLink += "&fieldname="+a_fieldnameDesc;
	          aLink += "&fieldcodes="+a_fieldnameCod;
	          aLink += "&" +"<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>" + "=" +idSentenza;
	          
	      desktop = window.open(aLink, "Lista_Reati_Sentenza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
		}
	}

function ClearReati() {
	  document.ModificaTenoreSige.<%=ICostantiTenoreSige.CAMPO_DESC_REATO%>.value = "";
	  document.ModificaTenoreSige.<%=ICostantiTenoreSige.CAMPO_REA_ID_REATO%>.value = "";
	  return true;
	}

	function Verify(){
	  	return true;
	}

function ClearOggetti() {
	document.ModificaTenoreSige.<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>.value = "";
	document.ModificaTenoreSige.<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>.value = "";
	// 20190508 [SG]: risolto errore
  	document.ModificaTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO_SEL%>.value =  document.ModificaTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO%>.value;
	return true;
}
</script>

</head>

<body class="corpo">

<FORM name="comandi" >
<table>
   	<tr>
   		<td class="LBG">
   			<a href="Javascript:window.print();">
   				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
 			<font class="label">Funzione:</font>&nbsp;&nbsp;
	        <font class="campo"><%=lTitolo %></font>
        </td>     
   		<td class="LBG">
	        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </td>     
      </tr>
    </table>

<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
<br>

</FORM>

<FORM name="ModificaTenoreSige" >         
<table style="width: 85%;">
    <tr>   
     	<td class="Titolo" colspan="2">Modifica Oggetto</td>
    </tr>
    
    <tr>
    	<td class="l">Contenuto</td>
      	<td class="l">
      		<select Title="Contenuto" name="<%=ICostantiTenoreSige.COD_CONTENUTO%>" onchange = ClearOggetti(); >
        		<%=contenuto%>
     		</select>
      	</td>
  	</tr>
   
    <tr>
    	<td class="l" >Oggetto <font class=ob>(*)</font></td>
     	<td class="L">  
     		<Textarea Title="Oggetti" name="<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>" cols=90 rows=3 readonly><%=descOggetto%></Textarea>
        	<%-- 20171004: [SG] invertiti le icone di selezione contenuti --%>
        	<a href="Javascript:ListaOggettiPerContenuto('ModificaTenoreSige','<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>','<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>',document.ModificaTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO%>[document.ModificaTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO%>.options.selectedIndex].value);">
            	<img src="/images/fileselected.gif" title="Seleziona Oggetto per Contenuto" border=0>
          	</a>&nbsp;&nbsp;&nbsp;
        	<a href="Javascript:ListaOggetti('ModificaTenoreSige', '<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>', '<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>');">
        		<img src="/images/filefolder.gif" title="Seleziona Oggetto" border=0>
        	</a>
     	</td>
    </tr>
     
    <tr>
        <td class="l">Titolo Esecutivo <font class=ob>(*)</font></td>
        <td class="L" >
        	<Textarea Title="Sentenze" name="<%=ICostantiTenoreSige.CAMPO_DESC_SENTENZA%>" cols=90 rows=3 readonly><%=descSentenza%></Textarea>
        	<a href="Javascript:ListaTitoliEsecutivi('ModificaTenoreSige', '<%=ICostantiTenoreSige.CAMPO_DESC_SENTENZA%>', '<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>');">
        		<img src="/images/fileselected.gif" title="Seleziona Titolo Esecutivo" border=0>
        	</a>
    	</td>
    </tr>
     
    <tr>
     	<td class="l">Reati </td>
      	<td class="l">
        	<Textarea Title="Reati" name="<%=ICostantiTenoreSige.CAMPO_DESC_REATO%>" cols=90 rows=3 readonly><%=descReato%></Textarea>
         	<a href="Javascript:ListaReati('ModificaTenoreSige', '<%=ICostantiTenoreSige.CAMPO_DESC_REATO%>', '<%=ICostantiTenoreSige.CAMPO_REA_ID_REATO%>');">
        	<img src="/images/fileselected.gif" title="Seleziona Reati per Sentenza" border=0></a>
       	</td>   
    </tr>
 </table>
 <br>
 <table>
    <tr>
    	<td>
      		<input class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>
 </table>

   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lActionModifica%>" >
   <input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE%>" value="<%=idRichiesta%>">
   <input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_ID_TENORE_SIGE%>" value="<%=idTenore%>">
   <input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>"  value="<%=codOggetto%>"> <!-- tenore.getTenoreSige().getCodOggettoSige()%> -->
   <input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_REA_ID_REATO%>" value="<%=idReato%>">
   <input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>"  value="<%=codSentenza%>">
<input type="HIDDEN" name="<%=ICostantiTenoreSige.COD_CONTENUTO_SEL%>" value="<%=codContenuto%>">
<%-- 20190508 [SG]: aggiunto campo nascosto --%>
<input type="HIDDEN" name="codContenutoOld" value="<%=codContenutoOld%>">
</FORM>
 
<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("ModificaTenoreSige");
    frmvalidator.addValidation("<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>","req", "Oggetto non selezionato");
    frmvalidator.addValidation("<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>","req", "Titolo Esecutivo non selezionato");

    frmvalidator.setAddnlValidationFunction("Verify");
</script>

 </body>
</html>