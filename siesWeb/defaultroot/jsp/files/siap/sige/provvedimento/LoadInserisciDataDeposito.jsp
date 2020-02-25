<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>

<jsp:useBean id="Aggiungi"          scope="request" class="java.lang.String"/>
<jsp:useBean id="lProvvedimento"    scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="Modificabile" 	    scope="request" class="java.lang.String"/>
<jsp:useBean id="domicilioSoggetto" scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="avvocato"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoAutorita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"    scope="request" class="java.lang.String"/>
<jsp:useBean id="vectNotAvv" 			  scope="request" class="java.util.Vector"/>


<%
    String flagDomicilioPressoDifensore = "N";
    if(domicilioSoggetto != null && domicilioSoggetto.getFlgDomicilioDifensore() != null
       && domicilioSoggetto.getFlgDomicilioDifensore().equals("S")){
    	flagDomicilioPressoDifensore = "S";
    }
%>

<%@page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<html>
  <head>
    <title>[S.I.E.S.] - Deposito Ordinanza/Decreto SIGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_CONFIRM%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>listeDestSIUS.js" ></script>

    <script language="JavaScript">

    function Verify()
    {
      var ritorno = true;
      var data_deposito=document.LoadInserisciDataDeposito.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>.value+'/'+document.LoadInserisciDataDeposito.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>.value+'/'+document.LoadInserisciDataDeposito.<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>.value;
      var data_emissione='<%=DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataEmissione(), "dd/MM/yyyy")%>'
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

      // MEV 15 Step 2
      // se il provvedimento non è validato non è possibile
      // procedere con il deposito
      if( document.LoadInserisciDataDeposito.FlagDocRegistrato.value == null ||
    	  (document.LoadInserisciDataDeposito.FlagDocRegistrato.value != null && 
           document.LoadInserisciDataDeposito.FlagDocRegistrato.value != 'S')){
          alert('Non è possibile effettuare il Deposito, se il Provvedimento non è validato!');
          return false;
      }
	  
      if (! ControllaData(data_deposito))
      {
        alert('Data deposito non valida!');
        return false;
      }
      // Controllo data di sistema >= Data Emissione .
      if( !CompareDate( data_deposito, data_sistema) )
      {
        alert('Data Deposito maggiore della Data di sistema!');
        return false;
      }
      // Controllo della data deposito <= data emissione
      else if ( !CompareDate( data_emissione, data_deposito) )
      {
        alert('Data Deposito minore della Data di Emissione!');
        return false;
      }

      // Controllo data di trasmissione
      if (document.LoadInserisciDataDeposito.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%> != undefined )
      {
	       var data_trasmissione=document.LoadInserisciDataDeposito.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDataDeposito.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDataDeposito.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       	 if (! ControllaData(data_trasmissione))
       	 {
         		alert('Data Trasmissione atti non valida!');
         		return false;
       	 }
       	 else if( !CompareDate( data_trasmissione, data_sistema) )
       	 {
        	 alert('Data Trasmissione Atti maggiore della Data di sistema!');
         		return false;
       	 }
       	 else if( !CompareDate( data_deposito,data_trasmissione ) )
       	 {
         	 alert('Data Trasmissione atti minore della Data Deposito!');
        	 return false;
       	 }
       }
     	 return ritorno;
    }
    </script>

    <script language="JavaScript">
     function CambiaTipo()
     {
       var node;
       node=document.getElementById('elenco');
       if (document.LoadInserisciDataDeposito.tipo[1].checked)
        {
          node.style.visibility='visible';
          node.style.display = ''
        }
       if (document.LoadInserisciDataDeposito.tipo[0].checked)
        {
          node.style.visibility='hidden';
          node.style.display = 'none'
        }
     }

     function calendario(a_formname,a_field_year,a_field_month,a_field_day)
     {
       desktop = 
           window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
     }
     
     function checkSNT(idEle) {
    	var flag = document.getElementById("flagSNT_"+idEle);
 	  	var select = document.getElementById("<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>_"+idEle);
 	  	var sede = document.getElementById("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>_"+idEle);
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
     
    </script>


  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :  </font>&nbsp;
        <font class="campo">Deposito <%=lProvvedimento.getProvvedimento().getDescrTipoProvvedimento()%>&nbsp;del&nbsp; <%=DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataEmissione(), "dd/MM/yyyy") %> </font>
      </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <tr>
	  	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>

    <tr>
<%  if (Modificabile.compareTo("SI")==0) { %> 
	    <jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
      		<jsp:param name="MagAssRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza"/>
    	</jsp:include>
    </tr>
    <tr>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      	<jsp:param name="AvvRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanza"/>
      </jsp:include>
<%	}else{ %>
    	<jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>"/>
    </tr>
    <tr>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>"/>
    </tr>
	<%}%>
  </table>
		<br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciDataDeposito">
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" value="<%=DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataEmissione(),"yyyy") %>" />
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" value="<%=DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataEmissione(),"MM") %>" />
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" value="<%=DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataEmissione(),"dd") %>" />
  
  <table cellspacing="2" cellpadding="2" width="95%">
<%
      if(lProvvedimento.getProvvedimento().getDataDeposito() != null)
      {
%>     <tr>
         <td class="l"> Anno / Numero del Provvedimento</td>
         <td class="l"> <%=StringUtils.toStringJSP(lProvvedimento.getProvvedimento().getChiaveAnno())%> / <%=StringUtils.toStringJSP(lProvvedimento.getProvvedimento().getChiaveProgr())%></td>
      </tr>
<%    }%>

    <tr>
      <td class="l">Data Emissione</td>
      <td class="L">
      <%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataEmissione(),"dd/MM/yyyy"))%>
      </td>
    </tr>

    <tr>
      <td class="l">Data Deposito in Cancelleria
<%
      if(lProvvedimento.getProvvedimento()!=null && lProvvedimento.getProvvedimento().getDataDeposito() != null)
      {
%>      </td>
        <td class="L">
        <%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy"))%>
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataDeposito(),"dd"))%>" type="hidden" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>"  >
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataDeposito(),"MM"))%>" type="hidden" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>"  >
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataDeposito(),"yyyy"))%>" type="hidden" size="4" maxlength="4" name="<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>"  >
<%    }else{%>
        <font class="ob">(*)</font></td>
        <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
		
		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('LoadInserisciDataDeposito','<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>','<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>','<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>');">
      	    <img src="/images/calendario.gif" border=0>
       	</a>
<%    }%>

      </td>
    </tr>
  </table>
  
  <br>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo">Oggetti</td>
      <td class="Titolo">Esito</td>
      
        <%-- [EC] - 20171031 : intervento per risoluzione anomalia segnalata da SIGE - TRIBUNALE TERAMO 
		    emissione ordinzanza di incompetenza(email Maffucci del 27102017).	Commento la colonna azioni
       --%>
     <!--  <td class="Titolo" colspan="2">Azioni</td> -->
    </tr>

    <tr>
  			<%-- <div id="elenco1" style="width: 100%; display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoGestioneTenori.jsp"/>
  			</div> --%>
  			
  			<%-- [EC] - 20171031 : intervento per risoluzione anomalia segnalata da SIGE - TRIBUNALE TERAMO 
		    emissione ordinzanza di incompetenza(email Maffucci del 27102017).	Includo la jsp ElencoDettaglioTenori.jsp (prima includeva erroneamente ElencoGestioneTenori)
       	    --%>
       	    
  			<div id="elenco1" style="display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoDettaglioTenori.jsp"/>
  			</div>
	</tr>
  </table>
  <br>
  
<%
if (Aggiungi.equals("no") ){
%>
  <table cellspacing="0" cellpadding="0" width="95%">
   <tr>
     <td class="l"><input type="radio" name="tipo" value="Dep" CHECKED onClick="javascript:CambiaTipo();">Deposito <%=lProvvedimento.getProvvedimento().getDescrTipoProvvedimento()%></td>
     <td class="l"><input type="radio" name="tipo" value="DepTrasm" onClick="javascript:CambiaTipo();">Deposito <%=lProvvedimento.getProvvedimento().getDescrTipoProvvedimento()%> e Trasmissione</td>
   </tr>
  </table>
  <div id="elenco" style="visibility:hidden;display:none">
<%
}else{
%>
  <div id="elenco" style="visibility:visible;">
<%
}
%>

 	<jsp:include page="<%=ICostantiUdienzaSige.PG_LOAD_DESTINATARI%>"/>

	<jsp:include page="<%=ICostantiProvvedimentoSige.PG_INSERIMENTO_DESTINATARI%>">
 		<jsp:param name="NomeForm" value="LoadInserisciDataDeposito"/>
 	</jsp:include>

<%
	if(flagDomicilioPressoDifensore.equals("S") && avvocato.size()>0 ){
%>
		<jsp:include page="<%=ICostantiProvvedimentoSige.PG_INSERIMENTO_NOTIFICA_SOGGETTO_PRESSO_DIFENSORE%>">
	 		<jsp:param name="NomeForm" value="LoadInserisciDataDeposito"/>
	    </jsp:include>
<%
	} else {
%>
		<jsp:include page="<%=ICostantiProvvedimentoSige.PG_INSERIMENTO_NOTIFICA_SOGGETTO%>">
	 		<jsp:param name="NomeForm" value="LoadInserisciDataDeposito"/>
	    </jsp:include>
<%
	}
%>

	  <table cellspacing="2" cellpadding="2" width="95%">
<%
        String NomeForm = "LoadInserisciDataDeposito";
        int lIdxAvv = 0;
        Iterator itxAvv = avvocato.iterator();
        int num_sede = 0;
        while ( itxAvv.hasNext())
        {
         AvvocatoSigeModel lAvv = (AvvocatoSigeModel)itxAvv.next();
%>
    <tr>
       <td class='LBG' colspan=6>Per la notifica all' avvocato <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>  Foro di <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%> Difensore <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%></td>
    </tr>
    
    <tr>
		<td class="l" colspan=6>
			<input type="checkbox" name="flagSNT"  id="flagSNT_<%=lIdxAvv%>"  value="<%=lIdxAvv%>" onclick="javascript:checkSNT('<%=lIdxAvv%>');" >
			S.N.T. (Sistema Notifiche Telematiche)
		</td>
	</tr>
    
    
    <tr id="AutDestRow_<%=lIdxAvv%>">
      <td class="l">Autorità Destinazione</td>
      <td class="l">
        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" id="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>_<%=lIdxAvv%>">
          <%= TipiIstituti1 %>
        </select>
      </td>
    </tr>

    <tr id="SedeDestRow_<%=lIdxAvv%>">
      <td class="l">Sede</td>
      <td class="l">
           <input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" id="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>_<%=lIdxAvv%>"
              value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUNEP('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
              <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
    <tr>
	    <td class="l">Indirizzo</td>
	    <td class="l">
		    <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="300" size="35">
	    </td>
    </tr>

    <input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="<%=lAvv.getAvvocatoFascicoloSigeModel().getIdAvvocatoFascicoloSige()%>" type="hidden" >
    <input name="<%=ICostantiRichiestaAtti.CODTIPONOTIFICA%>" value="C" type="hidden" >
<%
      num_sede++;
      lIdxAvv++;
     }
%>
    <tr><td colspan=6>&nbsp;</td></tr>

    <tr>
      <td class=l colspan=6>Per la  <input type='radio' name="<%=ICostantiRichiestaAtti.CODTIPONOTIFICA%>" value='N' >Notifica /<input type='radio' name="<%=ICostantiRichiestaAtti.CODTIPONOTIFICA%>" value='C' checked>Comunicazione ad altro destinatario</td>
    </tr>

    <tr>
      <td class="l">Destinatario</td>
      <td class="l">
        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          <%= tipoAutorita %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Sede </td>
      <td class="l">
         <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
            value="" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaComuniUNEP('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%><% if (num_sede > 0) { %> [<%=num_sede%>]<%}%>',document.<%=NomeForm%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%><% if (num_sede > 0) { %> [<%=num_sede%>]<%}%>.value );">
            <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
    
    
    <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
    <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="300" size="65">
    </td>
    </tr>

    <input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="" type="hidden" >

    </table>

	</div>
    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.provvedimento.action.ActInserisciDataDeposito" >
    	<input type="HIDDEN" name="FlagDocRegistrato" value="<%=lProvvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() %>" >
    </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciDataDeposito");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>