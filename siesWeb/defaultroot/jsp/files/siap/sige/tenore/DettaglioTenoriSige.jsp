<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.util.SIGELookupRemote"%>
<%@ page import="siap.sige.tenore.controller.ITenoreSige"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
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

<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="java.math.BigDecimal"%>
 
<jsp:useBean id="idRichiesta"  scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"    scope="request" class="java.lang.String"/>
<%-- 20190519 [SG]: aggiunto useBean --%>
<jsp:useBean id="idUdiSig"  			scope="request" class="java.lang.String"/>

<% 
// Passaggio del titolo
String lTitolo = "Gestione Oggetti Relativi alla Richiesta/Atto";
if (titolo != null && titolo.length() > 0){
	lTitolo = titolo;
}

String lActionInserimento = "siap.sige.tenore.action.ActInserisciOggettoRichiesta";

// Funzioni abilitate
Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
if( (lFunFiglie != null) && (lFunFiglie.size() != 0) ) {
	Iterator lIterBottoni = lFunFiglie.iterator();
	FunctionModel lFun = null;
	while(lIterBottoni.hasNext())    {
		lFun = (FunctionModel)lIterBottoni.next();
		if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {
			if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO))        {
				lActionInserimento = lFun.getNameAction();
			}
		}
	} // endWhile
} // endif

%>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Tenori Sige </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<script language="JavaScript">
	  function myConfirm(a_action, a_entityname, a_entityvalue, a_entityname1, a_entityvalue1 ) {
		str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" + a_entityvalue +  "&" + a_entityname1 + "=" + a_entityvalue1;
		if (window.confirm('Confermi la cancellazione?')) {
			window.location.href = str;
		}
	  }
 
	  function VisualizzaInserimento()
	  {
	    node=document.getElementById("InserimentoDiv");
	    node.style.visibility='visible';
	    node.disabled = false;   
	  }

	  function Verify()
	  {
	  	return true;
	  }

  	  var desktop;

      // Chiamata funzione lista Oggetti.
      function ListaOggetti(a_formname, a_fieldname, a_fieldcode )
      {
    	// se è stato selezionato un contenuto nella combo questo link non deve esere usato    	
    	var codC = document.InserisciTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO_SEL%>.value;    	
    	if(codC.length > 0 && codC!='-'){
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
      function ListaOggettiPerContenuto(a_formname, a_fieldname, a_fieldcode, codContenuto)
      {
          var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggettiSigePerContenuto";
              aLink += "&formname=" + a_formname;
              aLink += "&fieldname=" + a_fieldname;
              aLink += "&fieldcode=" + a_fieldcode;
              aLink += "&CodContenuto=" + codContenuto;
    	  desktop = window.open(aLink, "Lista_Oggetti_Sige_Per_Contenuto","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      // Chiamata funzione lista Titoli Esecutivi.
      function ListaTitoliEsecutivi(a_formname, a_fieldname, a_fieldcode)
      {
        // Compone il link URL per passare i parametri alla pagina jsp
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaTitoliEsecutiviSige";
            aLink += "&formname=" + a_formname;
            aLink += "&fieldname=" + a_fieldname;
            aLink += "&fieldcode=" + a_fieldcode;
        desktop = window.open(aLink, "Lista_Titoli_Esecutivi","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }

      // Chiamata funzione Lista Reati.
      function ListaReati(a_formname, a_fieldnameDesc, a_fieldnameCod)
      {
      	var idSentenza = document.InserisciTenoreSige.<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>.value;
      	
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

  function ClearReati()
  {
  	  document.InserisciTenoreSige.<%=ICostantiTenoreSige.CAMPO_DESC_REATO%>.value = "";
  	  document.InserisciTenoreSige.<%=ICostantiTenoreSige.CAMPO_REA_ID_REATO%>.value = "";
  	  return true;
  }

  function ClearOggetti()
  {
  	  document.InserisciTenoreSige.<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>.value = "";
  	  document.InserisciTenoreSige.<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>.value = "";
  	  document.InserisciTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO_SEL%>.value =  document.InserisciTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO%>.value;
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
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        	</a>
        </td>
        <td class="LBG">
        	<font class="label">Funzione :</font>&nbsp;
        	<font class="campo"><%=lTitolo %></font>
 	 	</td>
 	 	<%-- MERGE v10 COLLAUDO: modificato layout della pagina --%>
<!--    		<td class="LBG"> -->
		<jsp:include page="<%=ICostantiTenoreSige.BOTTONE_INSERISCI_OGGETTO%>"/>
<!--       	</td> -->
<!--    		<td class="LBG"> -->
        <!-- intervento per richieste sulla versione 11.2.1: commento il pulsante di modifica a livello di pagina. Invece deve essere aggiungo 
        a livello di riga come la cancellazione -->
<%-- 		<jsp:include page="<%=ICostantiTenoreSige.BOTTONE_MODIFICA_OGGETTO%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE%>"/>
			<jsp:param name="ValoreIdEntita" value="<%=idRichiesta%>"/>
		</jsp:include> --%>
<!--      	</td> -->
   		<!-- BOTTONE DI CANCELLAZIONE GENERALE - TUTTI I TITOLI ESECUTIVI/OGGETTI -->
<!--    		<td class="LBG"> -->
		<jsp:include page="<%=ICostantiTenoreSige.BOTTONE_CANCELLA_OGGETTO_LBG%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE%>"/>
			<jsp:param name="ValoreIdEntita" value="<%=idRichiesta%>"/>
			<jsp:param name="TipoCanc" value="G"/>
		</jsp:include>
<!--      	</td> -->
		<%-- 20190519 [SG]: aggiunta gestione idUdienzaSige --%>
     	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>">
			<jsp:param name="idUdiSig" value="<%=idUdiSig%>"/>
		</jsp:include>
      </tr>
    </table>

 <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>

 <br>

<%
	// Elenco dei TenoriEstesoModel
	Vector tenori = null;
	// Possono essere passati attraverso la request
	if (request.getAttribute("tenori") != null)
		tenori = (Vector) request.getAttribute("tenori");
	// Oppure attraverso la session
	else if (session.getAttribute("tenori") != null)
		tenori = (Vector) session.getAttribute("tenori");

  if (tenori != null &&  tenori.size() == 0 )
  {
%>
<font class="campo"> Nessun oggetto associato all'Atto </font>
<%
  } 
  else
  {
%>
 <table  width="85%">
	<tr>
		<td class="int">Oggetto</td>
		<td class="int">Titolo Esecutivo</td>
		<td class="int">Azioni</td>
	</tr>

<%
String lCodOggetto = "";
String lIdTenore = "";
String lIdSentenza = "";
String lIdSentenzaApp = "";
String lCodContenuto = "";

Iterator itx = tenori.iterator();
// 20190516 [SG]: aggiunto codice per gestire passaggio da TenoreSigeModel a TenoreSigeEstesoModel
if (tenori != null && tenori.size() > 0) {
	Object o = tenori.firstElement();
    if (!(o instanceof TenoreSigeEstesoModel)) {
    	ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
		TenoreSigeModel aTenore = new TenoreSigeModel();
		FascicoloSigeEstesoModel mFasEsteso = (FascicoloSigeEstesoModel) session.getAttribute("FascicoloSigeEsteso");
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = new Vector<TenoreSigeEstesoModel>();
		if (mFasEsteso != null && mFasEsteso.getFascicoloSige() != null
				&& mFasEsteso.getFascicoloSige().getIdFascicoloSige() != null) {
			aTenore.setFasIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
			lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);
		}
		if (lTenoriEstesi.size() == 0 && Utils.isPresent(idRichiesta))
			lTenoriEstesi = lCtrl.ExRicercaTenoreEstesoByRichiesta(new BigDecimal(idRichiesta));
		tenori.clear();
		tenori.addAll(lTenoriEstesi);
		itx = tenori.iterator();
    }
}
while ( itx.hasNext()) {
	TenoreSigeEstesoModel  lTenore = (TenoreSigeEstesoModel)itx.next();
	
	  // intervento per richieste sulla versione 11.2.1 
	  // al cambio del contenuto devo prevedere un tasto per la modifica
  if (!lTenore.getTenoreSige().getCodContenutoSige().equalsIgnoreCase(lCodContenuto)) {
	     lCodContenuto= lTenore.getTenoreSige().getCodContenutoSige();
	    
	     %>
			<tr>
				<td class="l" colspan="2" align="center">
					<font class="campo">&nbsp;<%=lTenore.getTenoreSige().getDescrContenutoSige()%></font>
				</td>
				 <td class="l">
					<jsp:include page="<%=ICostantiTenoreSige.PG_BUTTONS_PROVVEDIMENTO_TENORI_SINGOLI%>">
						<jsp:param name="CampoIdEntita" value="<%=ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE%>"/>
						<jsp:param name="ValoreIdEntita" value="<%=idRichiesta%>"/>						
						<jsp:param name="CodContenuto" value="<%=lTenore.getTenoreSige().getCodContenutoSige()%>"/>
					</jsp:include> 	
				 </td>
			</tr>
		<%}
	

	
	if (!lTenore.getTenoreSige().getCodOggettoSige().equalsIgnoreCase(lCodOggetto)) {
		lIdTenore = lTenore.getTenoreSige().getIdTenoreSige().toString();
		lCodOggetto = lTenore.getTenoreSige().getCodOggettoSige();
		lIdSentenza = "";
		lIdSentenzaApp = "";
%>      

	<tr>
		<td class="l">
			<font class="campo">&nbsp;<%=lTenore.getTenoreSige().getDescrOggettoSige()%></font>
		</td>
		<td class="l">

<%
		// Sentenza
		lIdSentenza = lTenore.getSentenza().getIdSentenza().toString();
		lIdSentenzaApp = lTenore.getSentenza().getIdSentenza().toString();
		out.print(lTenore.getSentenza().getCellSentenza());
%>
		</td>
	
		<td class="l">
			<jsp:include page="<%=ICostantiTenoreSige.BOTTONE_CANCELLA_OGGETTO%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiTenoreSige.CAMPO_ID_TENORE_SIGE%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=lIdTenore%>"/>
				<jsp:param name="CampoIdSentenza" value="<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>"/>
				<jsp:param name="ValoreIdSentenza" value="<%=lIdSentenza%>"/>
				<jsp:param name="TipoCanc" value="S"/>
			</jsp:include> 					
		</td>
	</tr>

<%
		} else {// Endif sul cambio del tenore
			
			if (!lTenore.getSentenza().getIdSentenza().toString().equalsIgnoreCase(lIdSentenzaApp)) {
%>
	<tr>
		<td class="l">
			<font class="campo">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
		</td>
		<td class="l">

<%
			lIdTenore = lTenore.getTenoreSige().getIdTenoreSige().toString();
			// Sentenza
			lIdSentenza = lTenore.getSentenza().getIdSentenza().toString();
			lIdSentenzaApp = lTenore.getSentenza().getIdSentenza().toString();		
			out.print(lTenore.getSentenza().getCellSentenza());
%>
		</td>
	
		<td class="l">
			<!-- BOTTONE DI CANCELLAZIONE PER SINGOLO TITOLO ESECUTIVO -->
			<jsp:include page="<%=ICostantiTenoreSige.BOTTONE_CANCELLA_OGGETTO%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiTenoreSige.CAMPO_ID_TENORE_SIGE%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=lIdTenore%>"/>
				<jsp:param name="CampoIdSentenza" value="<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>"/>
				<jsp:param name="ValoreIdSentenza" value="<%=lIdSentenza%>"/>
				<jsp:param name="TipoCanc" value="S"/>
			</jsp:include> 					
		</td>
	</tr>
	
<%	
			} // end if (!lTenore.getSentenza().getIdSentenza().toString().equalsIgnoreCase(lIdSentenzaApp)) {
		} // end else 
	} // end while
 %>
       </table>
 <%
}  // endif provvedimenti.size()
%>
<br>
</FORM>

<FORM name="InserisciTenoreSige" >         
<div id="InserimentoDiv" style="position: relative; top: 0; left: 0; visibility:hidden; " >     
<table  width="85%">
	<tr>   
    	<td class="Titolo">Inserimento Nuovo Oggetto</td>
    </tr>
    <tr> 
	 	<table width="85%">
	    	<tr>
		    	<%-- 20171013: [SG] invertite le scritte contenuto - oggetto --%>
		    	<td class="l">Contenuto <font class=ob>(*)</font></td>
		      	<td class="l">
		      		<select Title="Oggetto" name="<%=ICostantiTenoreSige.COD_CONTENUTO%>" onchange = ClearOggetti(); >
		        		<%=contenuto%>
		     		</select>
		      	</td>
		  	</tr>
	    	<tr>
		    	<td class="l" >Oggetto <font class=ob>(*)</font></td>
		     	<td class="L">  
		     		<Textarea Title="Contenuti" name="<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>" cols=90 rows=3 readonly></Textarea>
		     		<%-- 20171004: [SG] invertiti le icone di selezione contenuti --%>
		     		<a href="Javascript:ListaOggettiPerContenuto('InserisciTenoreSige','<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>','<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>',document.InserisciTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO%>[document.InserisciTenoreSige.<%=ICostantiTenoreSige.COD_CONTENUTO%>.options.selectedIndex].value);">
		            	<img src="/images/fileselected.gif" title="Seleziona Oggetto per Contenuto" border=0>
		          	</a>&nbsp;&nbsp;&nbsp;
		        	<a href="Javascript:ListaOggetti('InserisciTenoreSige', '<%=ICostantiTenoreSige.CAMPO_DESC_OGGETTO_SIGE%>', '<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>');">
		        		<img src="/images/filefolder.gif" title="Seleziona Oggetto" border=0>
		        	</a>
		     	</td>
	    	</tr>
	    	<tr>
		        <td class="l">Titolo Esecutivo <font class=ob>(*)</font></td>
		        <td class="L" >
		        	<Textarea Title="Sentenze" name="<%=ICostantiTenoreSige.CAMPO_DESC_SENTENZA%>" cols=90 rows=3 readonly></Textarea>
		        	<a href="Javascript:ListaTitoliEsecutivi('InserisciTenoreSige', '<%=ICostantiTenoreSige.CAMPO_DESC_SENTENZA%>', '<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>');">
		        		<img src="/images/fileselected.gif" title="Seleziona Titolo Esecutivo" border=0>
		        	</a>
		    	</td>
	    	</tr>
			<tr>
	     		<td class="l">Reati </td>
	      		<td class="l">
	        		<Textarea Title="Reati" name="<%=ICostantiTenoreSige.CAMPO_DESC_REATO%>" cols=90 rows=3 readonly></Textarea>
	         		<a href="Javascript:ListaReati('InserisciTenoreSige', '<%=ICostantiTenoreSige.CAMPO_DESC_REATO%>', '<%=ICostantiTenoreSige.CAMPO_REA_ID_REATO%>');">
	        			<img src="/images/fileselected.gif" title="Seleziona Reati per Sentenza" border=0>
	        		</a>
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
 	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lActionInserimento%>" >
<input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE%>" value="<%=idRichiesta%>">
<input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>"  value="">
<input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_REA_ID_REATO%>" value="">
<input type="HIDDEN" name="<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>"  value="">
</div>
<input type="HIDDEN" name="<%=ICostantiTenoreSige.COD_CONTENUTO_SEL%>" value="">
</FORM>
  <script language="JavaScript" type="text/javascript">
  		var frmvalidator  = new Validator("InserisciTenoreSige");
    	frmvalidator.addValidation("<%=ICostantiTenoreSige.CAMPO_COD_OGGETTO_SIGE%>","req", "Oggetto non selezionato");
    	frmvalidator.addValidation("<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>","req", "Titolo Esecutivo non selezionato");

    	frmvalidator.setAddnlValidationFunction("Verify");
  </script>
 </body>
</html>