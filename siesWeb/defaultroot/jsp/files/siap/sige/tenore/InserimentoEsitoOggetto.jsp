<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="f3b.util.StringUtils"%>
 
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="dati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="id_tenore" scope="request" class="java.lang.String"/>
<jsp:useBean id="tenori" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipo_esito" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />
<jsp:useBean id="dati_selezionati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="FlagIndulto" scope="request" class="java.lang.String"/>
<jsp:useBean id="isTitoliEsecutivi" scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggettoSige" scope="request" class="java.lang.String"/>
<jsp:useBean id="idProvvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="idSenSentenza" scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiesteAlGE1" scope="request" class="java.util.Vector"/>
<jsp:useBean id="Modificabile"     scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnotazioneManuale"     scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>

<% 
boolean lEsitoUnico = true;

// Passaggio dei codici dei Dati Provvedimento SIGE già selezionati (modifica)
boolean lDatoSelezionato = false;
String[] lDatiSelezionati = null;

if (tipo_esito.compareTo("unico")!= 0) 
	lEsitoUnico = false;

	String lNote = "";
	TenoreSigeEstesoModel  lTenore = null;
	
	if (tenori != null && tenori.size() > 0)
	{
		lTenore =(TenoreSigeEstesoModel) tenori.get(0) ;
		if (lTenore.getTenoreSige().getNote() != null)
			lNote = lTenore.getTenoreSige().getNote();
		
		if (dati_selezionati != null && dati_selezionati.size() >  0)
		{
			lDatoSelezionato = true;
			lDatiSelezionati = (String[]) dati_selezionati.toArray(new String[0]);
		}
	}
	
	String lCodOggetto = "";
	String lIdTenore = "";
	String lIdSentenza = "";
	String lDescOggetto = "";

	// Passaggio del titolo
	String lTitolo = "Gestione Oggetti Relativi alla Richiesta/Atto";
	if (titolo != null && titolo.length() > 0)
		lTitolo = titolo;
	
	if (isTitoliEsecutivi.equals(""))
		isTitoliEsecutivi="false";
	
	 boolean abilitaModifica = true;

	  if( !"SI".equalsIgnoreCase(Modificabile))
	  	abilitaModifica = false;
%>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Esiti Tenori Sige </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="/html/verifyCombo.js"></script>

<script language="JavaScript">
   var selBenefici = new Array ();
   var notSelBenefici = new Array ();
    function Verify()
    {
    	var campoEsito = "";
  		var ritorno = true; 
  		
  		
  		try {
     	    //if (document.inserisciEsitiTenore.<//%=ICostantiTenoreSige.CHECK_TIPO_ESITO%>[0].checked)
 			if (document.inserisciEsitiTenore.<%=ICostantiTenoreSige.CHECK_TIPO_ESITO%>.value == 'U')
     	    	campoEsito = document.inserisciEsitiTenore.<%=ICostantiTenoreSige.CAMPO_COD_ESITO_TENORE_SIGE%>;
  		    else
  			    campoEsito =  document.inserisciEsitiTenore.<%=ICostantiTenoreSige.CAMPO_COD_ESITO_TEN_SEN_REA%>;
  		} catch (e) {
  			campoEsito=document.getElementById ("CodEsitoSige");
  		}
 		
  		// 04/11/2009 ritorno =  VerifyCombo(campoEsito,"Esito");
		if (!VerifyCombo(campoEsito,"Esito"))
			return false;   		  		
 		
 <% if (FlagIndulto != null && FlagIndulto.length() > 0) {%>
 		//ritorno = VerificaAnnMan();
 <% } %>
	    var selBeneficiStr=selBenefici.concat();
	    var notSelBeneficiStr=notSelBenefici.concat();
	    document.getElementById("selBenifici").value=selBeneficiStr;
	    document.getElementById("notSelBenifici").value=notSelBeneficiStr;
 		return ritorno;		
   }


  // La funzione attiva e rende visibile una DIV all'interno del documento
  function AbilitaDiv(nomeDiv)
  {
      node=document.getElementById(nomeDiv);
      node.style.visibility='visible';
      node.disabled = false;   
  }
 
   // La funzione disattiva e rende invisibile una DIV all'interno del documento 
  function DisabilitaDiv(nomeDiv)
  {
    node=document.getElementById(nomeDiv);
   	node.style.visibility='hidden';
    node.disabled = true;
  }
 
  function VisualizzaEsitoUnico()
  {
  	// alert ("VisualizzaEsitoUnico()");
  	DisabilitaDiv("EsitoDifferenziatoDiv");
	AbilitaDiv("EsitoUnicoDiv");
	// ValidazioneEsitoUnico();	
  }
 
  function VisualizzaEsitoDifferenziato()
  {
    // alert ("VisualizzaEsitoDifferenziato()");
   	DisabilitaDiv("EsitoUnicoDiv");
	AbilitaDiv("EsitoDifferenziatoDiv");
	// ValidazioneEsitoDifferenziato();	
   }

 </script>

 <script language="JavaScript">
	function Init() {
  		try {
  			if (<%=isTitoliEsecutivi %>) {
  				document.inserisciEsitiTenore.<%=ICostantiTenoreSige.CHECK_TIPO_ESITO%>.value = 'U';
        	   	VisualizzaEsitoUnico();
           	} else {
        	   	document.inserisciEsitiTenore.<%=ICostantiTenoreSige.CHECK_TIPO_ESITO%>.value = 'D';
        	   	VisualizzaEsitoDifferenziato();
           	}
        } catch (e) {}
        <%
		int i=1; 
		for (Object annotazione : RichiesteAlGE1) {
			AnnotazioneManualeModel annotazioneModel = (AnnotazioneManualeModel) annotazione;
        	if (annotazioneModel.getFlagSelQuantum() == null || annotazioneModel.getFlagSelQuantum().equalsIgnoreCase("S")
        			|| !abilitaModifica) {
        		out.println ("insertIT('record_"+i+"', '');");
			} else {
				out.println ("rimuovi ('record_"+i+"')");
            }	  
        	i++;
		}
        %>
        aggiornaDatiAnnotazioneManuale ();
  	}

	function aggiornaDatiAnnotazioneManuale () {
		if (<%=abilitaModifica%>) {
			if (document.inserisciEsitiTenore.ARec)
		 		document.inserisciEsitiTenore.ARec.value  = '<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumAnniReclusione(),"0") %>';
	 	 	if (document.inserisciEsitiTenore.MRec)
		 		document.inserisciEsitiTenore.MRec.value  = '<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumMesiReclusione(),"0") %>';
	 		if (document.inserisciEsitiTenore.GRec)
		 		document.inserisciEsitiTenore.GRec.value  = '<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumGiorniReclusione(),"0") %>';
	 		if (document.inserisciEsitiTenore.Multa)
	 			document.inserisciEsitiTenore.Multa.value = parseInt('<%=StringUtils.toStringJSP(AnnotazioneManuale.getImportoMulta(),"0") %>');
	 		if (document.inserisciEsitiTenore.Mul_dec)
	 			document.inserisciEsitiTenore.Mul_dec.value = getParteDecimale('<%=AnnotazioneManuale.getImportoMulta() %>');

	 		if (document.inserisciEsitiTenore.AArr)
				document.inserisciEsitiTenore.AArr.value='<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumAnniArresto(),"0") %>';
			if (document.inserisciEsitiTenore.MArr)
				document.inserisciEsitiTenore.MArr.value='<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumMesiArresto(),"0") %>';
			if (document.inserisciEsitiTenore.GArr)
				document.inserisciEsitiTenore.GArr.value='<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumGiorniArresto(),"0") %>';
			if (document.inserisciEsitiTenore.Ammenda)
				document.inserisciEsitiTenore.Ammenda.value=parseInt('<%=StringUtils.toStringJSP(AnnotazioneManuale.getImportoAmmenda(),"0") %>'); // recupera la parte intera
			if (document.inserisciEsitiTenore.Amm_dec)
				document.inserisciEsitiTenore.Amm_dec.value=getParteDecimale('<%=AnnotazioneManuale.getImportoAmmenda() %>');
   		} 
	} 
</script>

</head>

<body class="corpo" onLoad="Init();">
 <FORM name="inserisciEsitiTenore">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        	<font class="label">Funzione :</font>&nbsp;
        	<font class="campo"><%=lTitolo %></font>
 	 	</td>     
      	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
 <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>

<br>
<% if (FlagIndulto != null && FlagIndulto.length() > 0) {%>
 	<jsp:include page="<%=ICostantiTenoreSige.PG_INCLUDE_INC_ANNOTAZIONE_MAN%>">
     	<jsp:param name="nome_frame" value="inserisciEsitiTenore"/>
     	<jsp:param name="IdFascicoloSiepSentenza" value="<%=request.getAttribute(ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA) %>"/>
     	<jsp:param name="isTitoliEsecutivi" value="<%=isTitoliEsecutivi %>"/>
     </jsp:include>
<%} 

if (!isTitoliEsecutivi.equalsIgnoreCase("true")) {
%>

 	<table  width="95%">
      <%--
      <tr>
          <td class="titolo" width="30%" > Specificare esito: </td>
          <td class="Titolo" width="70%">
          unico per oggetto <input type="radio"; name = "<%=ICostantiTenoreSige.CHECK_TIPO_ESITO%>"; value="U"  onClick="VisualizzaEsitoUnico();"  <%if(lEsitoUnico) { %>  checked <%} %> >&nbsp;&nbsp;&nbsp;&nbsp; 
		differenziato per sentenza-reato<input type="radio"; name = "<%=ICostantiTenoreSige.CHECK_TIPO_ESITO%>"; value="D"  onClick="VisualizzaEsitoDifferenziato();" <%if(!lEsitoUnico) { %>  checked <%} %>  >&nbsp;</td>
      </tr>--%> 
      <tr>
          <td class="titolo" colspan="2"> Esito Differenziato per sentenza-reato</td>
      </tr>
    </table>
 <%
} else {
%>
	<table  width="95%">
      <tr>
          <td class="titolo" colspan="2"> Esito Unico per Oggetto</td>
      </tr>
    </table>
<%	
}
%>   

<div id="comune" style="position:relative; top: 0; left: 0; visibility:visible;">     
 <div id="EsitoDifferenziatoDiv" style="position:relative; top: 0; left: 0; visibility:hidden; ">      
 		<jsp:include page="/jsp/files/siap/sige/tenore/IncEsitoSigeUnico.jsp">
     	<jsp:param name="tipo_esito" value="molti" />
     	<jsp:param name="isTitoliEsecutivi" value="<%=isTitoliEsecutivi %>" />
     	</jsp:include>
</div>
 
<div id="EsitoUnicoDiv" style="position:absolute; top: 0; left: 0; visibility:visible;" >     
 	<jsp:include page="/jsp/files/siap/sige/tenore/IncEsitoSigeUnico.jsp">
     <jsp:param name="tipo_esito" value="unico" />
     <jsp:param name="isTitoliEsecutivi" value="<%=isTitoliEsecutivi %>" />
     </jsp:include>
</div>
</div>
  <br>
<table style="width: 95%;">
   	<tr>
		<td class="l" width="60%" >Ulteriore descrizione della decisione  </td>
      	<td class="l">
        <td class="l"><Textarea title="Note" name="<%=ICostantiTenoreSige.CAMPO_NOTE%>"   cols=88 rows=3><%=lNote%></Textarea></td>
	</tr>
</table>
<% if (dati != null && dati.size() > 0) {%>
  <br>
   <table  width="95%">
    <tr>   
    	<td class="Titolo" > Dati particolari  </td> 
    </tr>
	</table>
	<table  width="95%">
<%
	   Iterator itxDati = dati.iterator();
       while ( itxDati.hasNext())
       {
     	  DecodificheModel  lDato = (DecodificheModel)itxDati.next();
%>
<tr>
		<td class="l"><font class="campo"><%=lDato.getDescription()%> </font></td>
        <td class="l">
		<input type="checkbox" name="<%=ICostantiTenoreSige.CAMPO_COD_TIPO_DATI_PROV%>"  value="<%=lDato.getCode()%>"  <% if (lDatoSelezionato && (Arrays.binarySearch(lDatiSelezionati, lDato.getCode() ) >= 0 )){ %> checked <%}%> >
		</td>
</tr>
<%
       } // endwhile
 %>
 </table>
 <%} // endif dati %>
  <br>
  <table>
  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>
  </table>
    <input value=<%=id_tenore%> type="hidden" name="<%=ICostantiTenoreSige.CAMPO_ID_TENORE_SIGE%>" > 
   	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.tenore.action.ActInserisciEsiti" />
 	<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" />
 	<input type="HIDDEN" name="isTitoliEsecutivi" value="<%=isTitoliEsecutivi %>" />
 	<input type="HIDDEN" name="codOggettoSige" value="<%=codOggettoSige %>" />
 	<input type="HIDDEN" name="idProvvedimento" value="<%=idProvvedimento %>" />
 	<input type="HIDDEN" name="idSenSentenza" value="<%=idSenSentenza%>" />
 	<input type="HIDDEN" name="notSelBenifici" value="" />
 	<input type="HIDDEN" name="selBenifici" value="" />
 	<input type="HIDDEN" name="<%=ICostantiTenoreSige.CHECK_TIPO_ESITO%>" value="" />
 
   </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("inserisciEsitiTenore");
    
    <%
    if (FlagIndulto != null && FlagIndulto.length() > 0) {
    %>
    frmvalidator.addValidation("ARec","numeric","Il Campo Anni Reclusione è un campo Numerico.");
    frmvalidator.addValidation("MRec","numeric","Il Campo Mesi REclusione è un campo Numerico.");
    frmvalidator.addValidation("GRec","numeric","Il Campo Giorni REclusione è un campo Numerico.");
    frmvalidator.addValidation("Multa","numeric","Il Campo Multa è un campo Numerico.");
    frmvalidator.addValidation("Mul_dec","numeric","Il Campo Multa è un campo Numerico.");
    
    frmvalidator.addValidation("AArr","numeric","Il Campo Anni Arresto è un campo Numerico.");
    frmvalidator.addValidation("MArr","numeric","Il Campo Mesi Arresto è un campo Numerico.");
    frmvalidator.addValidation("GArr","numeric","Il Campo Giorni Arresto è un campo Numerico.");
    frmvalidator.addValidation("Ammenda","numeric","Il Campo Ammenda è un campo Numerico.");
    frmvalidator.addValidation("Amm_dec","numeric","Il Campo Ammenda è un campo Numerico.");
    <%
    }
    %>
    frmvalidator.setAddnlValidationFunction("Verify");
   </script>
  
 </body>
</html>