<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>

<jsp:useBean id="anagraficaParteUdienza" scope="request" class="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel" />
<jsp:useBean id="notificaSoggetto"       scope="request" class="siap.siep.notifica.model.NotificaModel" />
<jsp:useBean id="modalita"               scope="request" class="java.lang.String" />
<jsp:useBean id="sesso"                  scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni"                scope="request" class="java.lang.String"/>
<jsp:useBean id="convocazioneUdienza"    scope="request" class="java.lang.String"/>
<jsp:useBean id="difensori"		         scope="request" class="java.util.Vector" />
<jsp:useBean id="tipiIstituto"           scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutorita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioniResidenza"       scope="request" class="java.lang.String"/>
<jsp:useBean id="tipiIstitutoSNT"        scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"               scope="request" class="java.lang.String" />

<jsp:useBean id="idEventoUdienza"  			scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaSige"    			scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaProcedimentoSige" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoParte"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="idSoggetto"       			scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Difensore</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<% 
	String lAction = "";	
	String lTitolo = "";
	String azioneChiamante = "";
	
	lAction = "siap.sige.udienzaparti.action.ActModificaDifensore";
	azioneChiamante = "siap.sige.udienzaparti.action.ActLoadModificaDifensore";
	
	if(modalita.equals("M") ){
		if (anagraficaParteUdienza.getCodParte().equals("F")) //FISICA
		{
	    	lTitolo = "Modifica Difensore e Convocazione Parte Fisica ";
	   	} else { //GIURIDICA
	    	lTitolo = "Modifica Difensore e Convocazione Parte Giuridica ";
		}
	} else {
		//if (anagraficaParteUdienza.getCodParte().equals("F")) //FISICA
		//{
	    	lTitolo = "Inserimento Difensore Parte";
	   	//} else { //GIURIDICA
	    //	lTitolo = "Inserimento Difensore Parte Giuridica ";
		//}
	}
%>

<script language="JavaScript">
	var desktop;
	function ListaComuni(a_formname,a_fieldname)
	{
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
	
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function cancellaCodComuneReale() 
    {
      	document.LoadInserisciPersonaFisica.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
    }

	function checkSNT(idEle) {
	  	var flag = document.getElementById("flagSNT_"+idEle);
	  	var select = document.getElementById("<%=ICostantiPartiUdienza.CAMPO_COD_DESTINATARIO%>_"+idEle);
	  	var sede = document.getElementById("<%=ICostantiPartiUdienza.CAMPO_SEDE%>_"+idEle);
	  	var autRow = document.getElementById("AutDestRow_"+idEle);
	  	var sedeRow = document.getElementById("SedeDestRow_"+idEle);
	  	if (flag.checked){
  			select.options[0].setAttribute("selected", "selected");
	  		sede.value = "";
	  		autRow.style.display="none";
	  		sedeRow.style.display="none";
	  	}else{
	  		// SNT off
			autRow.style.display="block";
			sedeRow.style.display="block";
	  	}
	}
	
    function Verify()
    {

      if (typeof(document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>) != "undefined"){
	      if (document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>.checked == false
	    	  && document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_CONVOCAZIONE_UDIENZA%>.value == "S" ){      
	    	  // se la parte non è domiciliata presso il difensore
	    	  // bisogna compilare la sezione "Notifica al Soggetto"
	   	      if ( document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_COD_IST_DETENZIONE%>.value == "-"
	    	       || document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_COD_IST_DETENZIONE%>.value == ""
	    	       || document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>.value == "" )
	    	  {
	    	      alert('Scegliere Autorità di Destinazione e Sede per il destinario Soggetto!');
	    	      return false;
	    	  }
	      }
      }
      
      return true;
    }
</script>

<script language="JavaScript">
	function VisualizzaNotificaSoggetto(param)
	{
		//alert("STEP A param: " + param);
		if(param == 'DD'){
			if (document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE %>.checked ) {
				//alert("1");
				DisabilitaDiv("notificaSoggetto");
		    } else {
		    	AbilitaDiv("notificaSoggetto");
		    }
		} else if (param == 'CU'){
			if (document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_CONVOCAZIONE_UDIENZA %>.value == 'N'	) {
				//alert("1");
				DisabilitaDiv("notificaSoggetto");
		    } else {
		    	//alert("2");
		    	AbilitaDiv("notificaSoggetto");
		    }
		} else {
			//alert("SONO IN ELSE ");
			if (document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_CONVOCAZIONE_UDIENZA %>.value == 'N' ||
				document.LoadInserisciPersonaFisica.<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE %>.checked
				){
				DisabilitaDiv("notificaSoggetto");
			} else {
				AbilitaDiv("notificaSoggetto");
			}
		}
	}

	function DisabilitaDiv(nomeDiv)
	{
		//alert("DISABILITA");
		node = document.getElementById(nomeDiv);
	   	node.style.visibility = 'hidden';
	    node.disabled = true;
	}

	function AbilitaDiv(nomeDiv)
	{
	    //alert("ABILITA");
		node = document.getElementById(nomeDiv);
	    node.style.visibility = 'visible';
	    node.disabled = false;   
	}
</script>

</head>
<body class="corpo" onload="VisualizzaNotificaSoggetto('ON');">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"	name="LoadInserisciPersonaFisica">

      <table>
        <tr>
        	<td class="LBG">
        		<a href="Javascript:window.print();">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>	
        		</a>
        	</td>
          	<td class="LBG">
          		<font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lTitolo%></font>
      		</td>

            <!-- BOTTONE DI RITORNO -->
            <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
        </tr>
      </table>

	  <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>

<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="Titolo" colspan="4">Parte</td>
	</tr>
<%
	// Persona Fisica
	if(anagraficaParteUdienza.getCodParte().equals("F")){
%>
	    <tr>
	        <td class="l">Cognome</td>
	        <td class="l">
	           <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCognome())%></font>&nbsp;
	        </td>
	        <td class="l">Nome</td>
	        <td class="l">
	           <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getNome())%></font>&nbsp;
	        </td>
	    </tr>
	    <tr>
	        <td class="l">Sesso</td>
	        <td class="L">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getSesso())%></font>&nbsp;
	        </td>
	        <td class="l" colspan="2">&nbsp;</td>
	    </tr>
	    <tr>
	        <td class="l">Data di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(anagraficaParteUdienza.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
	        </td>
	        <td class="l">Comune di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascita())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Stato di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescrStatoNascita())%></font>&nbsp;
	        </td>
	        <td class="l">Comune di Nascita Estero</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascitaEstero())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Codice Fiscale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCodFiscale())%></font>&nbsp;
	        </td>
	        <td class="l" colspan="2">&nbsp;</td>
		</tr>
<%
	} else {
	// Persona Giuridica
%>
	    <tr>
	        <td class="l">Società</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDenominazione())%></font>&nbsp;
	        </td>
	        <td class="l">Ragione Sociale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getRagSociale())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Provincia</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescrProvincia())%></font>&nbsp;
	        </td>
	        <td class="l">Partita IVA/Codice Fiscale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCodFiscale())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Sede Legale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getIndSedeLegale())%></font>&nbsp;
	        </td>
	        <td class="l">Sede Operativa/Indirizzo Attività</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getIndSedeOperativa())%></font>&nbsp;
	        </td>
		</tr>
<%
	}
%>
</table>
<br>

  <table>
	<tr>
		<td class="l" colspan="4" >Convocazione Udienza
			&nbsp;<select title="ConvocazioneUdienza" name="<%=ICostantiPartiUdienza.CAMPO_CONVOCAZIONE_UDIENZA%>" onchange="VisualizzaNotificaSoggetto('CU');">
          	<%= convocazioneUdienza %></select>&nbsp;</td>
		<!-- <td class="lVerdeNB">
        	Se è selezionata 'N' il sistema non deve riportare la notifica al Soggetto.
       </td> -->           	
	</tr>

	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
<%
    Iterator itxDif = difensori.iterator();
    int num_sede = 0;
    while ( itxDif.hasNext()) {
    	String checkSNT = "";
    	PartiUdienzaDifensoreModel lDif = (PartiUdienzaDifensoreModel)itxDif.next();

        if (lDif.isFlagSNT()){
    		checkSNT = "checked";
    	}
%>
      <tr>
        	<td class=l colspan="4">Per la notifica all' avvocato <%=StringUtils.toStringJSP(lDif.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lDif.getAvvocato().getNome(),"-")%>  Foro di <%=StringUtils.toStringJSP(lDif.getAvvocato().getForo(),"-")%> Difensore <%=StringUtils.toStringJSP(lDif.getAvvocato().getDescrTipo(),"-")%>
				<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_COD_AVVOCATO %>" value="<%=lDif.getAvvocato().getIdAvvocato() %>">
        	</td>
      </tr>

      <tr>
       	    <td class="l" colspan="2">
				<input type="checkbox" name="<%=ICostantiPartiUdienza.CAMPO_FLAG_SNT%>" <%=checkSNT%> id="<%=ICostantiPartiUdienza.CAMPO_FLAG_SNT%>_<%=num_sede%>"  onclick="javascript:checkSNT('<%=num_sede%>');">
       				S.N.T. (Sistema Notifiche Telematiche)
       		</td>
      </tr>

      <tr id="AutDestRow_<%=num_sede%>" >
      		<td class="l">Autorità Destinazione</td>
      		<td class="l">
        		<select title="Autorita Destinazione" name="<%=ICostantiPartiUdienza.CAMPO_COD_DESTINATARIO%>" id="<%=ICostantiPartiUdienza.CAMPO_COD_DESTINATARIO%>_<%=num_sede%>">
				<% if (lDif.getNotifica().getAutoritaEsterna() == null){%>
          			<%= tipiIstitutoSNT %>
          		<% } else { %>
          			<%= tipiIstituto %>
          		<% } %>
        		</select>
      		</td>
      </tr>

      <tr id="SedeDestRow_<%=num_sede%>" >
      		<td class="l">Sede</td>
      		<td class="l">
           		<input Title="Sede Procura" name="<%=ICostantiPartiUdienza.CAMPO_SEDE%>" id="<%=ICostantiPartiUdienza.CAMPO_SEDE%>_<%=num_sede%>"
              			value="<%if(lDif.getNotifica().getAutoritaEsterna() != null)%><%=StringUtils.toStringJSP(lDif.getNotifica().getAutoritaEsterna().getDescrSede(),"-")%>" type="text" maxlength="35" size="35">
              		<a href="Javascript:ListaUffici('LoadInserisciPersonaFisica','<%=ICostantiPartiUdienza.CAMPO_SEDE%>_<%=num_sede%>');">
              			<img src="/images/filefolder.gif" border=0> </a>
      		</td>
      </tr>

<%
      num_sede++;
    } 
%>

<%
//presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

    <tr>
    	<td class="Label" colspan="4">
			<input type="hidden" value="<%=azioneChiamante%>" name="<%=ICostantiPartiUdienza.CAMPO_AZIONE_CHIAMANTE%>" >
    	   		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienzaparti.action.ActLoadInserisciAvvocato&<%=ICostantiPartiUdienza.CAMPO_ID_SOGGETTO%>=<%=anagraficaParteUdienza.getIdSoggetto()%>&<%=ICostantiSecurity.CAMPO_ID_ENTITA_PROVV%>=<%=idEventoUdienza%><%=retParam%>">
          			Gestione Difensore&nbsp;
        		</a>
      	</td>
    </tr>

<%
		String checkDPD = "";
		if ( anagraficaParteUdienza != null && anagraficaParteUdienza.getResidenza() != null 
			 && anagraficaParteUdienza.getResidenza().getFlgDomicilioDifensore() != null
			 && anagraficaParteUdienza.getResidenza().getFlgDomicilioDifensore().equals("S"))
		{
			checkDPD = "checked";
		}
%>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	
	<tr>	
		<td class="l" colspan="4">Domicilio Presso Difensore &nbsp;
			<input type=checkbox <%=checkDPD%> name="<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE %>" value="S" onclick="VisualizzaNotificaSoggetto('DD');">
		</td>
		<!-- <td class="lVerdeNB">
        	Se si selezionata 'Domicilio Presso Difensore' il sistema non deve riportare la notifica al Soggetto.
       </td> --> 
	</tr>

	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>

</table>

<div id="notificaSoggetto" style="position: relative; top: 0; left: 0;   visibility:visible; " >
	<table>
		<tr>
			<td class="Titolo" colspan="4">Per la notifica al Soggetto</td>
		</tr>
	
	    <tr>
	        <td class="l">Autorità Destinazione</td>
	        <td class="l">
	          <select title="Destinatario" name="<%=ICostantiPartiUdienza.CAMPO_COD_IST_DETENZIONE%>">
	            <%= tipoAutorita %>
	          </select>
	        </td>
	    </tr>
	    <tr>
	        <td class="l">Sede</td>
	        <td class="l">
	           <input Title="Sede " name="<%=ICostantiPartiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>"
	              value="<%if(notificaSoggetto.getAutoritaEsterna() != null)%><%=notificaSoggetto.getAutoritaEsterna().getDescrSede()%>" type="text" maxlength="35" size="35">
	              <a href="Javascript:ListaComuni('LoadInserisciPersonaFisica','<%=ICostantiPartiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>');">
	              <img src="/images/filefolder.gif" border=0> </a>
	        </td>
	    </tr>
	    <tr>
	        <td class="l">Indirizzo</td>
	        <td class="L">
	             <input title="Indirizzo" name="<%=ICostantiPartiUdienza.CAMPO_INDIRIZZO_DETENZIONE%>" type="text" maxlength="100" size="50"
	              value="<%if(notificaSoggetto != null)%><%=StringUtils.toStringJSP(notificaSoggetto.getNote())%>" >
	        </td>
	    </tr>
	</table>
</div>
		
<table cellspacing=2 cellpadding=2>	
	<tr>
		<td colspan=2><br>
		<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>

	<input type="HIDDEN" name="Action" value="<%=lAction%>"> 
    <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">

	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_COD_TIPO_PART %>" value="<%=codTipoParte%>">
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA%>" value="<%=idEventoUdienza%>">  
	<input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" value="<%=idUdienzaSige%>">
	<input type="HIDDEN" name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" value="<%=idUdienzaProcedimentoSige%>">
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.RADIO_COD_PARTE %>" value="F">
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_ID_SOGGETTO%>" value="<%=anagraficaParteUdienza.getIdSoggetto()%>" >
</FORM>

</body>
</html>

<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciPersonaFisica");
    frmvalidator.setAddnlValidationFunction("Verify");
</script>
