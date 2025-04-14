<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.html.Option"%>
<%@ page import="java.util.Collection" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>

<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="impugnazione" scope="request" class="siap.sige.impugnazione.model.ImpugnazioneSigeModel"/>
<jsp:useBean id="scadenzario" scope="request" class="siap.sige.scadenzario.model.ScadenzarioSigeModel"/>
<jsp:useBean id="provvedimento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="soggettoImpugnante" scope="request" class="java.lang.String"/>
<jsp:useBean id="tenoreDecisioneRicorso" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoImpugnazione" scope="session" class="java.lang.String"/>
<jsp:useBean id="ufficiRecuperoCrediti" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiPubbliciMinisteri" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeRecuperoCrediti" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedePubblicoMinistero" scope="request" class="java.lang.String"/>

<jsp:useBean id="TipoDest" 			  	scope="request" class="java.lang.String" />
<jsp:useBean id="notificaComunicazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="vectNotAvv" 			scope="request" class="java.util.Vector"/>
<jsp:useBean id="notSogg"        		scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="notAltro"        		scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="luogodet"            	scope="request" class="siap.sige.detenzione.model.FasSigeDetenzioneModel"/>
<jsp:useBean id="tipoAutoritaSogg"      scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"    	        scope="request" class="java.util.Vector" />
<jsp:useBean id="tipoAutoritaAltro"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstitutiColl"      scope="request" class="java.lang.Object"/>

<%
String labelTipoImpugnazione="Opposizione";
if (codTipoImpugnazione.equals("01"))
	labelTipoImpugnazione="Ricorso";
%>

<html>
  <head>
    <script language="JavaScript1.2">
    </script>
    <title>[S.I.E.S.] - <%=labelTipoImpugnazione %></title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>listeDestSIUS.js" ></script>

    <script language="JavaScript">
    function Verify() {
        // Controllo della data atto.
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        var data_canc='<%=DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria(),"dd/MM/yyyy")%>';
        var data_atto ='<%=DateUtils.getDateToString(impugnazione.getDataRicorso() ,"dd/MM/yyyy")%>';
        // Si Controlla la data Decisione (se valorizzata).
        var data_decisione=document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_DECISIONE%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_DECISIONE%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_DECISIONE%>.value;

        if (!ControllaData(data_decisione)) {
        	alert ('Data Decisione non Valida.');
        	return false;
        }
        
        
        // Si Controlla che: data decisione <= data sistema.
        if (! CompareDate(data_decisione, data_sistema)) {
            alert('Data decisione > della data odierna');
            return false;
        }

        if (! CompareDate(data_canc, data_decisione)) {
            alert('Data arrivo in cancelleria > della Data Decisione ');
            return false;
        }
        
        if (! CompareDate(data_atto, data_decisione)) {
          alert('Data Atto > della Data Decisione ');
          return false;
        }
        
        <%
        if (codTipoImpugnazione.equals("01")) {
        %>
        var data_restituzione_atti=document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>.value+'/'+document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>.value;
        if (data_restituzione_atti != '//' && !ControllaData (data_restituzione_atti)) {
        	alert ("Data Restituzione Atti non valida.");
        	return false;
        }
        
        <%
        }
        %>

        var codDecisione = document.getElementById('<%=ICostantiImpugnazioneSige.CAMPO_COD_TENORE_DECISIONE%>').value;
        if (codDecisione == '-') {
        	alert ('Campo Tenore Decisione obbligatorio.');
        	return false;
        }
        return true;
    }

	function prova() {
    	var codDecisione = document.getElementById('<%=ICostantiImpugnazioneSige.CAMPO_COD_TENORE_DECISIONE%>').value;
        <%
       	// Ricorso
       	if (codTipoImpugnazione.equals("01")) {
        %>
        	<%-- MAC 2017/04/11: modificato layout pagina --%>
			conf.style.visibility = 'visible';
			if ( codDecisione == '01' || codDecisione == '03' || codDecisione == '04' || codDecisione == '05'
	    		  || codDecisione == '06' || codDecisione == '07' || codDecisione == '08' ) {
				dest.style.visibility = 'visible';
				confRic.style.visibility = 'hidden';
			} else {
	    		dest.style.visibility = 'hidden';
	    		confRic.style.visibility = 'hidden';
			}
	    <%
	    }

       	// Opposizione
       	if (codTipoImpugnazione.equals("04")) {
	    %>
			if( codDecisione == '05' || codDecisione == '13') {
			  	destOpposizione.style.visibility='visible';
			  	confOpposizione.style.visibility='hidden';
		  	} else {
			  	destOpposizione.style.visibility='hidden';
			  	confOpposizione.style.visibility='visible';
		  	}
	    <%
		}
	    %>
	}

	function effettoTree() {
    	node=document.getElementById("frameDestina");
     	node.style.display = (node.style.display == "none")? "block" : "none";
      	if(node.style.display == "none"){
   			conf.style.visibility='visible';
  			confRic.style.visibility='hidden';
       	} else {
      		conf.style.visibility='hidden';
      		confRic.style.visibility='visible';
      	}
    	document.images["imageDestina"].src = (node.style.display == "none")? "/images/expand.gif" : "/images/collapse.gif";
    	return false;
    }
    
    function ListaComuni(a_formname,a_fieldname)    {
    	window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function SelListProcura() {
          ListaProcure('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO%>');  
    }

    // Lista Uffici per TIPO_UFFICIO    
    //@emma 09072018 intervento post COLLAUDO 11.2 
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {       
       window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    // Lista comuni per TIPO_UFFICIO    
    //@emma 09072018 intervento post COLLAUDO 11.2 
    //@emma 18032019 intervento post COLLAUDO 11.2 adeguamento all'AF
   function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
 	{
    	 
 		if('98' == codTipoUfficio ) 
 			window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
 		else {
 			if('54' == codTipoUfficio)
 				codTipoUfficio = 'DIB';
 			else if ('57' == codTipoUfficio)
 				codTipoUfficio = 'DIBM';
 			else if ('37' == codTipoUfficio)
 				codTipoUfficio = 'CAP';
 			 
 		 window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
 	}
 	}
    
    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }

    function effettoTreeOpposizione(){
    	nodeOpp=document.getElementById("frameDestinaOpposizione");
    	nodeOpp.style.display = (nodeOpp.style.display == "none")? "block" : "none";
      	if(nodeOpp.style.display == "none"){
   			confOpposizione.style.visibility='hidden';
  			confRic.style.visibility='hidden';
       	} else {
      		confOpposizione.style.visibility='hidden';
      		confRic.style.visibility='visible';
      	}
    	document.images["imageDestinaOpposizione"].src = (nodeOpp.style.display == "none")? "/images/expand.gif" : "/images/collapse.gif";
    	return false;
    }

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)    {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

	function checkSNT(idEle) {
		var flag = document.getElementById("flagSNT_"+idEle);
	  	var select = document.getElementById("<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>_"+idEle);
	  	var sede = document.getElementById("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>_"+idEle);
	  	var autRow = document.getElementById("AutDestRow_"+idEle);
	  	var sedeRow = document.getElementById("SedeDestRow_"+idEle);
	  	// 20170703: aggiunto ulteriore controllo
	  	if (flag) {
		  	if (flag.checked) {
		  		// SNT on
	  			select.options[0].setAttribute("selected", "selected");
		  		sede.value = "";
		  		autRow.style.display="none";
		  		sedeRow.style.display="none";
		  	} else {
		  		// SNT off
				autRow.style.display="block";
				sedeRow.style.display="block";
		  	}
	  	}
	}

    function ListaUffici(a_formname,a_fieldname)    {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    <%-- 20170703: aggiunta funzione --%>
	function controllaSNT() {
		for (i = 0; i < <%=avvocato.size()%>; i++) {
			checkSNT(i);
		}
	}
    </script>
 	</head>
	<%-- 20170703: aggiunta chiamata a funzione --%>
  	<body class="corpo" onload="prova(); controllaSNT();">
    <table>
      	<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        	<td class=LBG><font class="label">Funzione :</font>&nbsp;<font class="campo">Esito <%=labelTipoImpugnazione%></font></td>
  			<!-- BOTTONE DI RITORNO -->
  			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      	</tr>
	</table>
  	<br />

  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadInserisciImpugnazioneSige'>
    	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    	<br />
    	<table cellspacing=2 cellpadding=2>
      		<tr>
        		<td class="label">Avverso il Provvedimento : </td>
      		</tr>
      		<tr>
        		<td> <font class="campo"><%=provvedimento.getProvvedimento().getDescrTipoProvvedimento()%> N. <%=(provvedimento.getProvvedimento().getChiaveAnno()==null?"-": provvedimento.getProvvedimento().getChiaveAnno())%>/<%=(provvedimento.getProvvedimento().getChiaveProgr()==null?"-": provvedimento.getProvvedimento().getChiaveProgr())%> </font> <font class="Label"> del </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(),"dd/MM/yyyy"))%> </font> <font class="Label"> depositato il </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy"),"-")%> </font> </td>
      		</tr>
		</table>
   		<br />
    	<table cellspacing=2 cellpadding=2>
      	<tr>
	        <td class="l">Anno/Numero&nbsp; - <%=impugnazione.getDescrTipoImpugnazione()%>&nbsp;</td>
	        <td class="L"><%=impugnazione.getAnnoS7()%>/<%=impugnazione.getProgrS7()%></td>
      	</tr>
      	
      <!-- @emma 18072018 intervento post COLLAUDO 11.2 -->
<%--       <tr>
        <td class="l">Tipo </td>
        <td class="L"><%=impugnazione.getDescrTipoImpugnazione()%>
        </td>
      </tr> --%>

<!-- Siamo nel caso di un Opposizione convertita in Ricorso, visualizzo il campo 
     Soggetto Impugnante che contiene la descrizione "Opposizione N. annoOpp/numOpp", 
     mentre non dovranno essere visualizzati i campi Data Atto, Data Arrivo 
     in Cancelleria e Presentato Da -->
<% if(impugnazione.getSoggettoImpugnante() != null && impugnazione.getSoggettoImpugnante().startsWith("Opposizione")){ %>
	  <tr>
        <td class="l">Proveniente da </td>
        <td class="L"><%=impugnazione.getSoggettoImpugnante()%>
        </td>
      </tr>
<% } else { %>
      <tr>
        <td class="l">Presentato da </td>
        <td class="L">
          <%=impugnazione.getDescrSoggettoImpugnante()%>
          <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_SOGGETTO_IMPUGNANTE%>" value="<%=impugnazione.getSoggettoImpugnante()%>" >
        </td>
      </tr>

      <tr>
        <td class="l">Data atto</td>
        <td class="L">
          <%=DateUtils.getDateToString(impugnazione.getDataRicorso() ,"dd-MM-yyyy") %>
       </td>
      </tr>
      
      <tr>
        <td class="l">Data arrivo in cancelleria</font></td>
        <td class="L">

          <%=DateUtils.getDateToString(impugnazione.getDataArrivoCancelleria(),"dd-MM-yyyy")%>
        </td>
      </tr>
      <!-- INIZIO - @emma 18032019 intervento post COLLAUDO 11.2 per adeguare il sistema a quanto riportato sull'AF (figura 25 di pag. 43)-->
       <%
		  if (codTipoImpugnazione.equals("01")) { // ricorso
		%>
      <tr>
        <td class="l">Data Trasmissione Atti</font></td>
          <td class="L"><%=DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti(),"dd-MM-yyyy") == null ? "-" : DateUtils.getDateToString(impugnazione.getDataTrasmissioneAtti(),"dd-MM-yyyy")%>
      </tr>
      
       <tr>
        <td class="l">Autorità Destinataria</font></td>
        <td class="L">
         <%=impugnazione.getDescrAutoritaDestinataria()%>
        </td>
      </tr>
       <!-- FINE - @emma 18032019 intervento post COLLAUDO 11.2 per adeguare il sistema a quanto riportato sull'AF (figura 25 di pag. 43)-->
       <%
		  }// ricorso
		%>
     
<% } %>
      <tr>
        <td class="l">Data decisione <font class="ob">(*)</font> </td>
        <td class="L">
          <input type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(impugnazione.getDataDecisione(),"dd"))%>" name="<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_DECISIONE %>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(impugnazione.getDataDecisione(),"MM"))%>" name="<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_DECISIONE %>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(impugnazione.getDataDecisione(),"yyyy"))%>" name="<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_DECISIONE %>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_DECISIONE%>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_DECISIONE%>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_DECISIONE%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>

        </td>
      </tr>
      
      <tr>
        <td class="l">Tenore decisione <font class="ob">(*)</font></td>
        <td class="L">
          <select title="Decisione Ricorso" class=small name="<%=ICostantiImpugnazioneSige.CAMPO_COD_TENORE_DECISIONE%>" id="<%=ICostantiImpugnazioneSige.CAMPO_COD_TENORE_DECISIONE%>" onchange="prova();">
            <%= tenoreDecisioneRicorso %>
          </select>
        </td>
      </tr>
      	
  <%
  if (codTipoImpugnazione.equals("01")) { // ricorso
  %>
  <tr>
        <td class="l">Data restiruzione atti <font class="ob">(*)</font> </td>
        <td class="L">
          <input type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"dd"))%>" name="<%= ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI %>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"MM"))%>" name="<%= ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RESTITUZIONE_ATTI %>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(impugnazione.getDataRestituzioneAtti() ,"yyyy"))%>" name="<%= ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI %>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI %>','<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RESTITUZIONE_ATTI %>','<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI %>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>

        </td>
   </tr>
  
  <%
  }
  %>

      	<tr>
       		<td class="l">Note</td>
        	<td class="l">
          		<Textarea Title="Note" name="<%= ICostantiImpugnazioneSige.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(impugnazione.getAnnotazione()) %></textarea>
        	</td>
      	</tr>
	</table>
<%
if (codTipoImpugnazione.equals("01")) { // ricorso
%>
 	<div style="visibility:hidden" id="dest">
		<table style="width: 100%;" cellpadding="2" cellspacing="2">
	    	<tr>
	      		<td class="Titolo">
	        		<div align="left"><a><img name="imageDestina" src="/images/expand.gif"  onClick="return effettoTree();" alt="" border=0></a>Destinatari </div>
	      		</td>
	    	</tr>
	    	<tr id="frameDestina" style="display:none"> 
	      		<td> 
	        		<table>
				 		<tr>
			        		<td class="l">Ufficio Pubblico Ministero</td>
			        		<td class="l">
			          			<select title="Destinatario" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO %>">
			            			<%=ufficiPubbliciMinisteri %>
			          			</select>
			        		</td>
			        		<td class="l">Sede</td>
			        		<td class="l">
			           			<input value="<%=sedePubblicoMinistero %>" type="text" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO %>" maxlength="35" size="35" title="Sede">
			           			<a href="Javascript:ListaUfficiPerTipo('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_PUBBLICO_MINISTERO%>',document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO%>[document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_PUBBLICO_MINISTERO%>.selectedIndex].value);">
                					<img src="/images/filefolder.gif" border=0>
                 				</a>
			           			
		        			</td>
			     		</tr>
			     		<tr>
			        		<td class="l">Ufficio Recupero Crediti</td>
			        		<td class="l">
			          			<select title="Destinatario" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>">
			            			<%= ufficiRecuperoCrediti %>
			          			</select>
			        		</td>
			        		<td class="l">Sede</td>
			        		<td class="l">
			           			<input type="text" value="<%=sedeRecuperoCrediti %>" name="<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>" maxlength="35" size="35" title="Sede">
			           			<%--<a href="Javascript:ListaComuni('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>');"><img src="/images/filefolder.gif" border=0></a>--%>  			           			
			           			<a href="Javascript:ListaUfficiComuni('LoadInserisciImpugnazioneSige','<%=ICostantiImpugnazioneSige.CAMPO_COD_SEDE_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>',document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>[document.LoadInserisciImpugnazioneSige.<%=ICostantiImpugnazioneSige.CAMPO_COD_DESTINATARIO_UFFICIO_RECUPERO_CREDITI%>.options.selectedIndex].value);"> 
            					<img src="/images/filefolder.gif" border=0></a>
			        		</td>
			     		</tr>
					</table>
		  		</td>
			</tr>
		</table>
 	</div>
	<div style="visibility:hidden" id="conf">
		<table cellspacing=2 cellpadding=2>
			<tr>
	        	<td>
	          		<input class="bottone" type="submit" value="Conferma">
	        	</td>
	      	</tr>
		</table>
	</div>
<%
}
%>

<%
// Su Opposizione in caso di "Rigetto" e "Dichiara Inammissibile"
if (codTipoImpugnazione.equals("04")) {
%>
	<div style="visibility:hidden" id="confOpposizione">
	    <table cellspacing=2 cellpadding=2>
	      <tr>
	        <td>
	          <input class="bottone" type="submit" value="Conferma">
	        </td>
	      </tr>
	    </table>
	</div>
	  
 <div style="visibility:hidden" id="destOpposizione">
	<table style="width: 100%;" cellpadding="2" cellspacing="2">
	    <tr>
	      <td class="Titolo">
	        <div align="left"><a><img name="imageDestinaOpposizione" src="/images/expand.gif"  onClick="return effettoTreeOpposizione();" alt="" border=0></a>Destinatari</div>
	      </td>
	    </tr>
	
	    <tr id="frameDestinaOpposizione" style="display:none"> 
	      <td> 
		  <table style="width: 100%;" cellpadding="2" cellspacing="2">

		    <tr>
		      <td class="l" colspan=6>
		        Per la comunicazione al <%=TipoDest%> <input type="checkbox" name="<%= ICostantiUdienzaSige.CAMPO_PROCURA_GENERALE %>" <%= notificaComunicazione %>>
		      </td>
		    </tr>
				<tr>
					<td colspan=6>&nbsp;</td>
				</tr>
		    <tr>
		      <td class="l" colspan=6>
		        Per la notifica al Soggetto
		      </td>
		    </tr>
		
		<%if (luogodet.getLuogoDetenzione() == null || luogodet.getLuogoDetenzione().getIdLuogoDetenzione() == null  || luogodet.getLuogoDetenzione().getDataFineDetenzione() != null || luogodet.getLuogoDetenzione().getIstitutoDetenzione() == null ) {%>
		       <tr>
		        <td class="l">Autorità Destinazione</td>
		        <td class="l">
		          <select title="Destinatario" name="<%=ICostantiUdienzaSige.CAMPO_COD_IST_DETENZIONE%>">
		            <%= tipoAutoritaSogg %>
		          </select>
		        </td>
		       </tr>
		       <tr>
		        <td class="l">Sede</td>
		        <td class="l">
		           <input type="text" name="<%=ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE%>" value="<%=StringUtils.toStringJSP((notSogg.getAutoritaEsterna()!=null)?notSogg.getAutoritaEsterna().getDescrSede():null)%>" maxlength="35" size="35" title="Sede">
		           <a href="Javascript:ListaComuni('LoadInserisciImpugnazioneSige','<%=ICostantiUdienzaSige.CAMPO_COD_LUOGO_DETENZIONE%>');"><img src="/images/filefolder.gif" border=0></a>
		        </td>
		       </tr>
		          <tr>
		            <td class="l">Indirizzo</td>
		            <td class="L" colspan=3>
		             <input type="text" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="<%=StringUtils.toStringJSP(notSogg.getNote())%>" maxlength="80" size="80" title="Indirizzo">
		            </td>
		          </tr>
		<%}else{%>
		      <tr>
		        <td class="l">Tipo Istituto</td>
		        <td class="l">
		        <input type="text" name="Comune" value="<%=StringUtils.toStringJSP(luogodet.getLuogoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(luogodet.getLuogoDetenzione().getDescrLuogo())%>" size="50" title="Istituto" readonly>
		        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciImpugnazioneSige','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		        	<img src="/images/filefolder.gif" border=0>
		        </a>
		        <input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=luogodet.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>">
		        <input type="hidden" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" >
		        </td>
		      </tr>
		<%}%>
				<tr><td colspan=6>&nbsp;</td></tr>
		
		<%
		//Collection<Object> collTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		int lIdxAvv = 0;
		Iterator itxAvv = avvocato.iterator();
		while ( itxAvv.hasNext()) {
			AvvocatoSigeModel lAvv = (AvvocatoSigeModel)itxAvv.next();
		%>
			<tr>
				<td class=l colspan=6>
					Per la notifica all' avvocato <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>  Foro di <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%> Difensore <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%>
		         	<input type="hidden" name="<%=ICostantiUdienzaSige.CAMPO_COD_AVVOCATO%>" value="<%=lAvv.getAvvocato().getIdAvvocato() %>" >
				</td>
			</tr>
		
		<%
		String checkSNT = "";
    // MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo()
		// String descSede =lAvv.getAvvocato().getForo(); 
    String descSede =lAvv.getAvvocato().getDescComuneSedeForo(); 
    // MEV_21: FINE 
		NotificaModel modNotAvv = null;
		Iterator itxNotAvv = vectNotAvv.iterator();
		while ( itxNotAvv.hasNext()) {
			NotificaModel nm = (NotificaModel)itxNotAvv.next();
			//if (lAvv.getAvvocatoFascicoloSigeModel().getIdAvvocatoFascicoloSige().equals(nm.getAvvIdAvvocatoFascicoloSige())){
			if (lAvv.getAvvocato().getIdAvvocato().equals(nm.getAvvIdAvvocatoFascicoloSige())){
				modNotAvv = nm;
				// se non c'è autorità esterna allora è una notifica telematica
				if (nm.getAutoritaEsterna()==null){
					checkSNT = "checked";
				}
			}
		}
		
		String comboDefVal = ""; 
		if ( modNotAvv != null && modNotAvv.getAutoritaEsterna()!=null && modNotAvv.getAutoritaEsterna().getDescrSede()!=null && !"".equals(modNotAvv.getAutoritaEsterna().getDescrSede()) ) {
			descSede = modNotAvv.getAutoritaEsterna().getDescrSede();
			comboDefVal = modNotAvv.getAutoritaEsterna().getCodTipoAutorita();
		}
		
		String[] lStringFilter = { "-", "22" };
		Option lOptionAvv = new Option((Collection<Object>)TipiIstitutiColl, comboDefVal, 75);
		lOptionAvv.setFilter(lStringFilter);
		String comboTipiIstituti = lOptionAvv.toString();
		%>
		
			<tr>
				<td class="l" colspan=6>
					<input type="checkbox" name="flagSNT"  id="flagSNT_<%=lIdxAvv%>"  value="<%=lIdxAvv%>" onclick="javascript:checkSNT('<%=lIdxAvv%>');" <%=checkSNT%>>
					S.N.T. (Sistema Notifiche Telematiche)
				</td>
			</tr>
		
		    <tr id="AutDestRow_<%=lIdxAvv%>">
		      <td class="l">Autorità Destinazione</td>
		      <td class="l">
		        <select name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" id="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>_<%=lIdxAvv%>" title="Destinatario">
		        	<%= comboTipiIstituti %>
		        </select>
		      </td>
		    </tr>
		
			<tr id="SedeDestRow_<%=lIdxAvv%>">
		      <td class="l">Sede</td>
		      <td class="l">
		           <input type="text" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" id="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>_<%=lIdxAvv%>" value="<%=StringUtils.toStringJSP(descSede)%>" maxlength="35" size="35" title="Sede Procura">
<%-- MODIFICA 20170703: non scriveva dentro il campo poichè esiste un altro campo di nome 'sede' --> sede0, sede1, ... --%>
				    <%-- if (lIdxAvv > 0) { %>[<%=lIdxAvv%>]<%}%> --%>
		           <a href="Javascript:ListaUffici('LoadInserisciImpugnazioneSige','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=lIdxAvv%>]');"><img src="/images/filefolder.gif" border="0"> </a>
		      </td>
			</tr>
		<%
			lIdxAvv++;
		}
		%>
				<tr><td colspan=6>&nbsp;</td></tr>
		
		<%
		String comboNotifica = "";
		String comboComunica = "checked";
		if ("N".equals(notAltro.getCodTipoNotifica()) ){
			comboNotifica = "checked";
			comboComunica = "";
		}
		%>
		    <tr>
		      <td class=l colspan=6>Per la  
		      	<input type='radio' name="<%=ICostantiUdienzaSige.CAMPO_TIPONOTIFICA%>" value='N' <%=comboNotifica%>>Notifica /
		      	<input type='radio' name="<%=ICostantiUdienzaSige.CAMPO_TIPONOTIFICA%>" value='C' <%=comboComunica%>>Comunicazione ad altro destinatario</td>
		    </tr>
		
		    <tr>
		      <td class="l">Destinatario</td>
		      <td class="l">
		        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>"><%= tipoAutoritaAltro %></select>
		        <input type="hidden" name="<%=ICostantiUdienzaSige.CAMPO_COD_AVVOCATO%>" value="" >
		      </td>
		    </tr>
		
		    <tr>
		      <td class="l">Sede</td>
		      <td class="l">
		         <input type="text" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" value="<%=StringUtils.toStringJSP((notAltro.getAutoritaEsterna()!=null)?notAltro.getAutoritaEsterna().getDescrSede():null)%>" maxlength="35" size="35" title="Sede">
		         <a href="Javascript:ListaComuni('LoadInserisciImpugnazioneSige','<%=ICostantiRichiestaAtti.CAMPO_SEDE%><% if (lIdxAvv > 0) { %>[<%=lIdxAvv%>]<%}%>');"><img src="/images/filefolder.gif" border=0> </a>
		      </td>
		    </tr>
		    <tr>
		      <td class="l">Indirizzo</td>
		      <td class="L" colspan=3>
		       <input type="text" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="<%=StringUtils.toStringJSP(notAltro.getNote())%>" maxlength="80" size="80" title="Indirizzo">
		      </td>
		    </tr>
		    <tr><td colspan=6>&nbsp;</td></tr>
		
		        </table>
		      </td>
		    </tr>
		
 </div>
 <%
    } 
 %> 
 
    <div style="visibility:hidden" id="confRic">
	    <table cellspacing=2 cellpadding=2>
	      <tr>
	        <td>
	          <input class="bottone" type="submit" value="Conferma">
	        </td>
	      </tr>
	    </table>
	</div>
    
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.impugnazione.action.ActInserisciEsitoImpugnazioneSige" >
    <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" value="<%=provvedimento.getProvvedimento().getIdProvvedimentoSige()%>" >
    <input type="HIDDEN" name="<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>" value="<%=provvedimento.getProvvedimento().getCodTipoProvvedimento()%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>" value="<%=FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>" >
    <input type="HIDDEN" name="<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>" value="<%=impugnazione.getIdImpugnazioneSige().toString()%>" >
  	</form>
  	<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciImpugnazioneSige");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_DECISIONE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_DECISIONE%>","lt=31");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_DECISIONE%>","required",  "Il campo Giorno Data Decisione è obbligatorio");
    
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_DECISIONE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_DECISIONE%>","lt=12");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_DECISIONE%>","required", "Il campo Mese Data Decisione è obbligatorio");
    
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_DECISIONE%>","lt=2999");
    frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_DECISIONE%>","required", "Il campo Anno Data Decisione è obbligatorio");
    
    <%
    if (codTipoImpugnazione.equals("01")) {
    %>
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>","numeric");
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>","gt=1");
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI%>","lt=31");
    
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>","numeric");
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>","gt=1");
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_MESE_DATA_RESTITUZIONE_ATTI%>","lt=12");
   
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>","numeric");
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>","gt=1900");
        frmvalidator.addValidation("<%=ICostantiImpugnazioneSige.CAMPO_ANNO_DATA_RESTITUZIONE_ATTI%>","lt=2999");
    <%
    }
    %>
    frmvalidator.setAddnlValidationFunction("Verify");
  	</script>

  	</body>
</html>