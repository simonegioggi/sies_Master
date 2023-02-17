<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Date"%>

<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="dataInsFS"          scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTipiAutorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoUffGiudiz"    scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto recupero di info dalla sessione ed aggiunto riferimento all'oggetto "ElencoDest3" --%>
<jsp:useBean id="fascicoloSiusGP" 	 scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="ElencoDest3"    	 scope="request" class="java.lang.String"/>

<%-- MEV_9 (D.lgs. 123/2018) --%>
<jsp:useBean id="ultimoEventoRichAtti"   scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.sius.richiestaatti.action.ActInserisciRichiestaAltreIstruttorie";

  //MEV_9 (D.lgs. 123/2018)
  Date lUltimaDataRestitAttiIstruttori = ultimoEventoRichAtti.getDataRestituzioneAi();
%>

<script language="JavaScript">

  var desktop;

  // Chiamata lista Comuni con filtro sulla Provincia dell'ufficio connesso.
  function ListaComuniRicercaUfficio(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

  // Destinatario 2 - Elenco Istituti Penitenziari.
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  // Destinatario 3 - Elenco dei CSSA.
  function ListaCSSA(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

  // Chiamata lista Comuni filtrata per codice tipo ufficio.
  function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
  {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  // Chiamata lista comuni completa. ( Da Elimnare ? ).
  function ListaComuniCompleta(a_formname,a_fieldname)
  {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
	//MEV10-s3: aggiunta funzione per scegliere la selezione alla lista di riferimento a seconda del tipo selezionato
  	function scegliTipo() {
  		var tipo = document.LoadInserisciRichiestaAltreIstruttorie.tipoDest[2].value;
  		if ("B5" == tipo) {
  			ListaUSSM('LoadInserisciRichiestaAltreIstruttorie','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2]');
  		}
  		else {
  			ListaCSSA('LoadInserisciRichiestaAltreIstruttorie','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2]');
		}
  	}
  	function ListaUSSM(a_formname,a_fieldname) {
 		desktop = window.open("/jsp/Main.jsp?Action=siap.sico.cssa.action.ActLoadListaUSSM&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  	}
</script>
<html>
<head>
  <title>[S.I.E.S.] - Altre Richieste Istruttorie </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  
	  <%-- INIZIO: MEV_9 (D.lgs. 123/2018) --%>
	  function abilitaCampiDataRestituzione() {
	  	if (document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CHECK_DATA_RESTITUZIONE%>.checked){
	   		document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.disabled = false;
	   		document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.disabled = false;
	   		document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.disabled = false;
	  	}
	  	else {
	   		document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.disabled = true;
	   		document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.disabled = true;
	   		document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.disabled = true;
	  	}    	
	 }
	 <%-- FINE: MEV_9 (D.lgs. 123/2018) --%>

	function Verify() {
		var tipo = document.LoadInserisciRichiestaAltreIstruttorie.tipoDest[2].value;
		if ("B5" == tipo) {
			document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value = "USSM";
		} else {
			document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value = "CSSA";
		}
    	
      if (document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (! ControllaData(data_emissione))
      {
        alert('Data di emissione non valida');
        return false;
      }

      // Data emissione minore <= data sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if ( ! CompareDate( data_emissione,data_sistema) )
      {
        alert('Data Emissione maggiore della data attuale!');
        return false;
      }

      // Data inserimento Fascicolo Sius <= data Emissione
      if ( !CompareDate( '<%=dataInsFS%>', data_emissione) )
      {
        alert('Data Emissione minore della data di inserimento del fascicolo SIUS!');
        return false;
      }

      <%-- INIZIO: MEV_9 (D.lgs. 123/2018) --%>
      var data_restituzione = document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.value
                        +'/'+ document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.value
                        +'/'+ document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.value;
      if (! ControllaDataPassaVuota(data_restituzione))
      {
        alert('Data Restituzione atti non valida');
        return false;
      }
      
      if ( ! CompareDate( data_emissione, data_restituzione) )
      {
        alert('Data Emissione maggiore della Data Restituzione atti!');
        return false;
      }      
      <%-- FINE: MEV_9 (D.lgs. 123/2018) --%>      
      
      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      // Destinatario 1
      if (document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0].value == '')
      {
        alert('La Sede del destinatario 1 è obbligatoria');
        return false;
      }
      // Destinatario 4
      if (document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[3].value != '-'
          && document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[3].value == '')
      {
        alert('La Sede del destinatario 4 è obbligatoria');
        return false;
      }
      // Destinatario 5
      if (document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[4].value != '-'
          && document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[4].value == '')
      {
        alert('La Sede del destinatario 5 è obbligatoria');
        return false;
      }

      // Controlla che almeno un destinatario sia inserito
      if (document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value == '-'
       && document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value == ''
       && document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[3].value == '-'
       && document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[4].value == '-'
       && document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2].value == '') {
    	  	// MEV10-s3: aggiunto controllo ulteriore
    	  	var test = true;
    	  	<% if ("TDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio()) ||
				"UDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio())) {
			%>
    	  	if (document.LoadInserisciRichiestaAltreIstruttorie.tipoDest[2].value == '-' ||
    	  			document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2].value == '')
    	  		test = true;
    	  	else
    	  		test = false;
    	  	<% } %>
	  		if (test) {
		        alert('Inserire almeno un Destinatario.');
		        return false;
	  		}
     	}
		return true;
}

  </script>
</head>

<body onLoad="document.forms[0].elements[0].focus()" class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Altre Richieste Istruttorie</font>
      </td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="<%=ICostantiRichiestaAtti.MSG_BUTTON_HISTORY%>" width="24" height="24" border="0">
        </a>
      </td>
    </tr>

    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaAltreIstruttorie">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Data Emissione <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
        
        <%-- INIZIO: MEV_9 (D.lgs. 123/2018) --%>
        <td class="l"><input value="S" type="checkbox" name="<%=ICostantiRichiestaAtti.CHECK_DATA_RESTITUZIONE%>" 
                             onClick="abilitaCampiDataRestituzione()"
                             >  Atti da restituire entro il </font></td>
        <td class="L">
          <input Title="Giorno"  type="text" size="2" maxlength="2" 
                 name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaDataRestitAttiIstruttori,"dd"),"")%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
                 onBlur="javascript:value=FillDM(value)" disabled> /
          <input Title="Mese" type="text" size="2" maxlength="2" 
                 name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaDataRestitAttiIstruttori,"MM"),"")%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
                 onBlur="javascript:value=FillDM(value)" disabled> /
          <input Title="Anno" type="text" size="4" maxlength="4" 
                 name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE %>"  
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaDataRestitAttiIstruttori,"yyyy"),"")%>" 
          		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          		 onBlur="javascript:value=FillYear(value)" disabled>
        </td>     
        <%-- FINE: MEV_9 (D.lgs. 123/2018) --%>        
      </tr>
        <!-- Start new impl -->

        <!-- Destinatario 1 -->
        <tr>
          <td class="l">Destinatario n°1</td>
          <td class="L" colspan=3>
           <table>
             <tr >
             <td class="l"> Ufficio Giudiziario </td>

             <input type="hidden"  Title="Tipo" name="tipoDest" value="UFF_GIUD" size=50>
             <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >

             <td class="l" >
               <select title="UfficioGiudiziario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
                 <%= ElencoUffGiudiz %>
               </select>
             </td>
             </tr>
             <tr>
             <td class="l">Sede</td>
             <td class="l">
               <input Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaComuni('LoadInserisciRichiestaAltreIstruttorie','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]', document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0][document.LoadInserisciRichiestaAltreIstruttorie.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].selectedIndex].value);">
                <img src="/images/filefolder.gif" border=0> </a>
          </td>
          </tr>
          </table>
        </tr>

        <!-- End Destinatario 1 -->

        <!-- Start Destinatario 2 -->
        <tr>
          <td class="l">Destinatario n°2</td>
          <td class="L" colspan=3>
           <table>
           <tr>
             <td class="l">Istituto Penitenziario </td>

             <input type="hidden"  Title="Tipo" name="tipoDest" value="IST_DET" size=50>
             <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >

             <td class="l">
               <input type="hidden"  Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" value="" size="35">

               <input readonly  Title="Istituto" name="Comune" value="" size=50>
               <input type="hidden"  Title="Istituto" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="" size=50>
               <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRichiestaAltreIstruttorie','<%= ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1]','Comune');">
               <img src="/images/filefolder.gif" border=0></a>
             </td>
          </tr>
          </table>
        </tr>

        <!-- End Destinatario 2 -->

        <!-- Start Destinatario 3 -->
		<%-- MEV10-s3: discrimino a seconda dell'ufficio collegato --%>
		<% if ("TDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio()) ||
				"UDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio())) {
		%>
			<tr>
          		<td class="l">Destinatario n°3</td>
          		<td class="L" colspan=3>
          			<table>
          				<tr >
          					<td class="l">UEPE/USSM</td>
          					<td class="l" >
          						<select title="Destinatario n°3" name="tipoDest">
            						<%= ElencoDest3 %>
          						</select>
          					</td>
          					<td class="l">Sede <font class=ob>(*)</font></td>
          					<td class="l">
             					<input Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                					value="" type="text" maxlength="35" size="35">
                				<input type="hidden" Title="Tipo" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="" size=50>
                				<a href="Javascript:scegliTipo();">
                				<img src="/images/filefolder.gif" border=0> </a>
          					</td>
          				</tr>
          				<tr>
            				<td class="l">Indirizzo</td>
            				<td class="L" colspan=3>
             					<input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            				</td>
          				</tr>
          			</table>
          		</td>
        	</tr>
		<%
		} else {
		%>
	    	<tr>
	        	<td class="l">Destinatario n°3</td>
	          	<td class="L" colspan=3>
	           		<table>
	           			<tr>
	             			<td class="l">UEPE</td>
             				<input type="hidden" Title="Tipo" name="tipoDest" value="CSSA" size=50>
             				<input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >
             				<td class="l">
	               				<input Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" value="" type="text" maxlength="35" size="35">
	               				<input type="hidden" Title="Tipo" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="CSSA" size=50>
	                			<a href="Javascript:ListaCSSA('LoadInserisciRichiestaAltreIstruttorie','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2]');">
	                				<img src="/images/filefolder.gif" border=0>
	                			</a>
	             			</td>
	           			</tr>
	          	</table>
	        </tr>
		<% } %>
        <!-- End Destinatario 3 -->

        <!-- Start Destinatario 4 -->
        <tr>
          <td class="l">Destinatario n°4</td>
          <td class="L" colspan=3>
          <table>
          <tr>
            <td class="l">Tipo</td>

            <input type="hidden"  Title="Tipo" name="tipoDest" value="AUT_EXT" size=50>

            <td class="l" >
             <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
              <%= ElencoTipiAutorita %>
             </select>
            </td>
          </tr>

          <tr>
            <td class="l">Sede <font class=ob>(*)</font></td>
            <td class="l">
            <input Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaComuniRicercaUfficio('LoadInserisciRichiestaAltreIstruttorie','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[3]');">
                <img src="/images/filefolder.gif" border=0> </a>
            </td>
          </tr>

          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          </table>
        </tr>
        <!-- End Destinatario  4 -->

        <!-- Start Destinatario 5 -->
        <tr>
          <td class="l">Destinatario n°5</td>
          <td class="L" colspan=3>
          <table>

          <tr>
            <td class="l">Tipo</td>

            <input type="hidden"  Title="Tipo" name="tipoDest" value="AUT_EXT" size=50>

            <td class="l" >
              <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
               <%= ElencoTipiAutorita %>
              </select>
            </td>
          </tr>

          <tr>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
            <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaComuniRicercaUfficio('LoadInserisciRichiestaAltreIstruttorie','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[4]');">
                <img src="/images/filefolder.gif" border=0> </a>
          </td>
          </tr>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          </table>
        </tr>
        <!-- End Destinatario 5 -->

        <!-- End new impl  -->

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note 1</td>
            <td class="L" colspan=3>
             <input title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>" type="text" maxlength="80" size="80">
            </td>
        </tr>

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note 2</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=80 rows=4 ></textarea>
            </td>
        </tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
  </form>

		<script language="JavaScript" type="text/javascript">
		    var frmvalidator = new Validator("LoadInserisciRichiestaAltreIstruttorie");
		
		    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
		    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
		
		    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
		    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
		
		    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
		    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
		
		    //Chiama la funzione di Verify().
		    frmvalidator.setAddnlValidationFunction("Verify");
	  	</script>
 	</body>
</html>