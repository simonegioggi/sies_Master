<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius" %>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<jsp:useBean id="modalita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAvvocato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna"       scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoDesignazione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="descrTipo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaDif"       scope="request" class="java.lang.String"/>
<jsp:useBean id="foro" scope="request" class="java.lang.String"/>
<jsp:useBean id="comune" scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"    scope="request" class="siap.sius.avvocato.model.AvvocatoModel"/>

<jsp:useBean id="nazione"  scope="request" class="java.lang.String"/>  <!-- MEV_21 -->
<jsp:useBean id="statoAvv" scope="request" class="java.lang.String"/>  <!-- MEV_21 -->

<%AvvocatoModel lAvv = avvocato;
// if (lAvv==null)
// 	lAvv = new AvvocatoModel();

%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Avvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
  var desktop;
  
  <%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
  function ListaAvvocatiRegInde(a_formname) {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActLoadRicercaAvvocatoRegInde&formname="+a_formname,"Ricerca_Avvocato_RegInde","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600");
  }  
  
  <%-- MEV_21: aggiunta abilitazione combo sulla submit --%>
  function EnableCombo() {
  	document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.disabled = false;
  	document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.disabled = false;
  	document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.disabled = false;
  }  

  <%-- MEV_21: aggiunta selezione comuni --%>
  function ListaComuniNascita(a_formname,a_fieldname) {
    if (document.LoadInserisciAvvocato.lTipoInserimento.value != "reginde")
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
  }  
  function cancellaCodComuneReale() {
    document.LoadInserisciAvvocato.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
  }

  function Inserisci()
  {
  	<%-- MEV_21 - Aggiunti controlli per inserimento avvocato non certificato. --%>
    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value.length==0 ) {
      alert('Il Cognome è obbligatorio');
      document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COGNOME %>.focus;
      return false;
    }
    
    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_NOME%>.value.length==0 ) {
      alert('Il Nome è obbligatorio');
      document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_NOME %>.focus;
      return false;
    }
    
    /*
    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='-') 
    {
      alert('Indicare lo stato di nascita');
      document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COD_STATO_NASCITA %>.focus;
      return false;
    }
    */
    
    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039') 
    {
      document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value="";
      if (document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value.length==0) 
      {
        alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
        document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
        return false;
      }
    } else 	if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value!='-') {
      document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value='';
      cancellaCodComuneReale();
    }

    if (   document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value.length > 0 	
        && document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value.length > 0 
       ) 
    {
      alert('Il Comune di Nascita e il luogo di Nascita Estero sono alternativi');
      document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
      return false;
    }

    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
      document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value;
    
    if (document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
      document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value;

    var data_to_verify=document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value;
    if (! ControllaData(data_to_verify)) {
      alert('Data di nascita non valida');
      return false;
    }   
 
    if (Verify()) {
      document.LoadInserisciAvvocato.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.avvocato.action.ActInserisciAvvocato";   
      document.LoadInserisciAvvocato.submit();
    }
  }
  
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

<%-- MEV_21: Sostituita da ricerca avvocati reginde
  function ListaAvvocati(a_formname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
  }
--%>

  function ListaAvvocatiSiep(a_formname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActLoadRicercaAvvocatoSiep&formname="+a_formname, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
  }
  
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }
  
  function Verify()
  {
    //alert("Verify");
    
    if(   document.LoadInserisciAvvocato.<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value==""
       && !document.getElementById('lTipoInserimento').value=="manuale"
      )
    {
      alert('Selezionare un difensore dalla lista');
      return false;
    }
    if(document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.value=="-")
    {
      alert('Il tipo difensore è obbligatorio');
      return false;
    }

    Avvocato();
    return true;
  }

  function cambiaMotivo()
  {
         var note =document.getElementById('note');
         var idxSelMotivo = document.LoadInserisciAvvocato.<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE%>.selectedIndex;

         var valoreMotivo=document.LoadInserisciAvvocato.<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE%>[idxSelMotivo].value;
         if(valoreMotivo=='0008')
         {
          note.style.visibility='visible';
          ufficioSotto.style.top='-55px';
          conferma.style.top='-10px';

         }else
        {
          note.style.visibility='hidden';
          ufficioSotto.style.top='-100px';
          conferma.style.top='-25px';

         }
  }

  function caricamento()
  {
    <%-- MEV_21: Gestione riabilitazione dei campi sul tasto indietro dopo segnalazione di errore --%>
    var tipoInserimento = document.getElementById('lTipoInserimento').value;
    
    if (tipoInserimento=="manuale"){
        // torna indietro devo riabilitare i campi
        //alert("Abilito i campi: ");
        document.getElementById('inserimento').style.visibility = 'visible';
        document.getElementById('confermaBtn').style.visibility = 'hidden';
        document.getElementById('ricReginde').style.visibility = 'hidden';

        document.getElementById('<%=ICostantiAvvocato.CAMPO_COGNOME%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_NOME%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>').disabled = false;
        document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_FORO%>').disabled = false;
        document.getElementById('<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_TELEFONO%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_FAX%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_E_MAIL%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_PEC%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>').readOnly = false; 
        document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>').disabled = false; 

        document.getElementById('IconComuneNascita').style.visibility = 'visible';
        document.getElementById('IconComuneStudio').style.visibility = 'visible';     
    }
    <%-- MEV_21: FINE --%>
    
    var idxSel = document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex;

    var valore=document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>[idxSel].value;
    var fiducia =document.getElementById('fiducia');
    var ufficio =document.getElementById('ufficio');
    var ufficioSotto =document.getElementById('ufficioSotto');
    var motivoDes =document.getElementById('motivoDes');
    ufficioSotto.style.top='-100px';
    note.style.visibility='hidden';
    var conferma =document.getElementById('conferma');

    conferma.style.visibility='visible';
    fiducia.style.visibility='hidden';

    if( valore == '01')
    {
       document.LoadInserisciAvvocato.<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF%>.value=document.LoadInserisciAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value;
    }

    if(valore == '01')
    {
      fiducia.style.visibility='hidden';
      ufficio.style.visibility='visible';
      ufficioSotto.style.visibility='visible';
      motivoDes.style.visibility='visible';
      conferma.style.visibility='visible';
      conferma.style.top='-55px';
    }
        
    if(valore == '02')
    {
      fiducia.style.visibility='visible';
      ufficio.style.visibility='hidden';
      ufficioSotto.style.visibility='hidden';
      motivoDes.style.visibility='hidden';

      conferma.style.visibility='visible';
      conferma.style.top='-325px';
    }
        
    if(valore == '-')
    {
      fiducia.style.visibility='hidden';
      ufficio.style.visibility='hidden';
      ufficioSotto.style.visibility='hidden';
      motivoDes.style.visibility='hidden';
      conferma.style.visibility='visible';
      conferma.style.top='-325px';
    }
        
    if(valore == '03')
    {
      fiducia.style.visibility='hidden';
      ufficio.style.visibility='hidden';
      ufficioSotto.style.visibility='hidden';
      motivoDes.style.visibility='hidden';
      conferma.style.visibility='visible';
      conferma.style.top='-325px';
    }
  }

</script>

<script language="JavaScript">
  function  Avvocato()
    {

        document.LoadInserisciAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sius.avvocato.action.ActInserisciAvvocato";
        document.LoadInserisciAvvocato.IA.disabled=true;
        document.LoadInserisciAvvocato.IN.disabled=true;

    }

  </script>

</head>

<body class="corpo" onLoad="caricamento();">
<table>
   <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
   <td class=LBG><font class="label">Funzione : </font>&nbsp;&nbsp;
   <font class="campo">Inserimento Difensore</font></td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

</tr>
</table>

<br><jsp:include page="/jsp/files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp"/><br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAvvocato">

<table id="ricReginde">
	<tr>
		<%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde 
 		<td class="label">
      		<a href="Javascript:ListaAvvocati('LoadInserisciAvvocato');">
        	Seleziona dalla lista <img src="/images/filefolder.gif" border=0></a>
    	</td>
    	--%>
 		<td class="label">
      		<a href="Javascript:ListaAvvocatiRegInde('LoadInserisciAvvocato');">
        	Seleziona da RegInde <img src="/images/filefolder.gif" border=0></a>
    	</td>    	
    	
    	<td>&nbsp;&nbsp;&nbsp;</td>
    	<td class="label">
      		<a href="Javascript:ListaAvvocatiSiep('LoadInserisciAvvocato');">
        	Seleziona dalla lista  Siep   <img src="/images/filefolder.gif" border=0></a>
    	</td>
  	</tr>
</table>

<table>
	<tr>
		<td class="l" >Cognome </td>
    	<td class="l">
    		<input type="hidden" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="">
    		<input size=35 maxlength=35 title="Campo Cognome" type="text" readonly value="<%=lAvv.getCognome()%>" name="<%= ICostantiAvvocato.CAMPO_COGNOME %>">
    	</td>
  	</tr>
  	<tr>
		<td class="l">Nome</td>
		<td class="l"><input size=35 maxlength=35  title="Campo Nome" type="text" readonly value="<%=lAvv.getNome()%>" name="<%= ICostantiAvvocato.CAMPO_NOME %>"  ></td>
	</tr>
  
<%-- MEV_21:  --%>
<%
String comuneNascita = "", comuneNascitaEstero = "";
if (Utils.isPresent(lAvv.getDescrStatoNascita())) {
	if ("ITALIA".equalsIgnoreCase(lAvv.getDescrStatoNascita()))
		comuneNascita = lAvv.getDescLuogoNascita();
	else
		comuneNascitaEstero = lAvv.getDescLuogoNascitaReginde();
} else if (Utils.isPresent(lAvv.getDescLuogoNascita())) {
	comuneNascita = lAvv.getDescLuogoNascita();
}
%>  
  <tr>
    <td class="l">Comune di Nascita </td>
    <td class="L">
      <input title="Comune di Nascita" readonly 
             value="<%=StringUtils.toStringJSP(comuneNascita) %>" 
              type="text" maxlength="35" size="35"
             name="<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>"  >
      		
      <a href="Javascript:ListaComuniNascita('LoadInserisciAvvocato','<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>');" id="IconComuneNascita" style="visibility:hidden;" >
				<img src="/images/filefolder.gif" border=0>
			</a>
    </td>
  </tr>
      

<%-- MEV_21: aggiunta nuovi campi --%>
<tr>
  <td class="l">Stato di Nascita</td>
  <td class="L">
    <select disabled="disabled" name="<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>" size="1"><%=nazione%></select>
  </td>
</tr>
<tr>
  <td class="l">Luogo di Nascita Estero</td>
  <td class="l">
    <input title="Luogo di Nascita Estero" readonly value="<%=StringUtils.toStringJSP(comuneNascitaEstero)%>" type="text" name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>" maxlength="35" size="35">
  </td>
</tr>
<%-- MEV_21: FINE aggiunta nuovi campi --%>

<tr>
<td class="l">Data di nascita </td>
          <td class="L">
 	 <%if (lAvv.getDataNascita() == null) {%>
            <input type="text" readonly value ="" title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text"readonly value ="" title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly value ="" title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
                <%  }else{%>         
            <input type="text" readonly value ="<%=StringUtils.toStringJSP(DateUtils.getDayToString(lAvv.getDataNascita()))%>"  title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly value ="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(lAvv.getDataNascita()))%>" title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly value ="<%=StringUtils.toStringJSP(DateUtils.getYearToString(lAvv.getDataNascita()))%>" title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          <%  }%> 
       </td>

</tr>

 <tr>
		  <td class="l">Foro </td>		   
			<td class="l">
				<select disabled="disabled" name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1">
        	 <%=foro%>
        </select>
     	</td>	
    </tr>
  <tr>
      <td class="l">Indirizzo</td>
      <td class="l"><input size="80" readonly maxlength="200" title="Indirizzo" value="<%=StringUtils.toStringJSP(lAvv.getIndirizzo()) %>" type="text" name="<%= ICostantiAvvocato.CAMPO_INDIRIZZO %>"  ></td>
  </tr>
  
  
  	<%-- MEV_21: modificato campo Comune Residenza in comune Studio --%>
    <%--
    <tr>
      <td class="l">Comune di residenza </td>
        <td class="L">
          <input title="Comune di Residenza"  value="<%=StringUtils.toStringJSP(lAvv.getDescComuneResidenza()) %>" type="text" name="<%= ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA %>"  maxlength="35" size="35">
        </td>
    </tr>
    --%>
    <tr>
        <td class="l">Con Studio in </td>
        <td class="L">
          <input type="text" title="Comune Sede dello Studio" maxlength="35" size="35" readonly
                 value="<%=StringUtils.toStringJSP(lAvv.getDescrComuneStudio())%>" 
                 name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>" >
          <a id="IconComuneStudio" href="Javascript:ListaComuni('LoadInserisciAvvocato','<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO %>');" style="visibility:hidden;">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
    </tr> 
    <%-- MEV_21: FINE--%>   
    
    <tr>
      <td class="l">Telefono</td>
      <td class="l"><input readonly size=12 maxlength=12   title="Telefono" value="<%=StringUtils.toStringJSP(lAvv.getTelefono())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_TELEFONO %>"  ></td>
    </tr>
    <tr>
      <td class="l">Fax</td>
      <td class="l"><input readonly size=12 maxlength=12  title="Fax" value="<%=StringUtils.toStringJSP(lAvv.getFax())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_FAX %>"  ></td>
    </tr>
    <tr>
      <td class="l">e-mail</td>
      <td class="l"><input readonly size=50 maxlength=50  value="<%=StringUtils.toStringJSP(lAvv.getEMail()) %>" title="e-mail" type="text" name="<%= ICostantiAvvocato.CAMPO_E_MAIL %>"  ></td>
    </tr>
    <tr>
      <td class="l">PEC</td>
      <td class="l"><input readonly size=50 maxlength=50  value="<%=StringUtils.toStringJSP(lAvv.getPec()) %>" title="pec" type="text" name="<%= ICostantiAvvocato.CAMPO_PEC %>"  ></td>
    </tr>      
    <tr>
      <td class="l">Codice Fiscale</td>
      <td class="l"><input size=25 maxlength=16 readonly value="<%=StringUtils.toStringJSP(lAvv.getCodiceFiscale()) %>" title="codice fiscale" type="text" name="<%= ICostantiAvvocato.CAMPO_CODICE_FISCALE %>"  ></td>
    </tr>
      
      <%-- MEV_21: Aggiunto "stato" --%>
      <tr>
        <td class="l" >Stato Difensore</td>
        <td class="L">
          <select disabled="disabled" title="Stato Difensore" name="<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>"><%=statoAvv%></select>
        </td>
      </tr>  
      <%-- MEV_21: FINE--%>
      
      <tr>
         <td class="l" >Tipo Difensore (*)</td>
           <td class="L">
           <select title="attività" name="<%=ICostantiAvvocato.CAMPO_COD_TIPO%>"  onChange="caricamento();" >
            <%=tipoAvvocato%>
          </select>
         </td>
    </tr>
   </table>

   <div id="fiducia"  style="visibility:hidden; position:relative;  top:-30px; left:330px  ">

   <table cellspacing=2 cellpadding=2>

   <tr>
           <td class="label">Nomina in Data</td>
           <td>
            <input type="text"  title="Giorno Data di Nomina" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_NOMINA%>" value="<%=DateUtils.getSysDate("dd")%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Mese Data di Nomina" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_NOMINA %>" value="<%=DateUtils.getSysDate("MM")%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text"  title="Anno Data di Nomina" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_NOMINA %>" value="<%=DateUtils.getSysDate("yyyy")%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
            </td>
   </tr>
</table>
</div>

 <div id="ufficio"  style="visibility:hidden; position:relative;  top:-60px; left:320px  ">

   <table cellspacing=2 cellpadding=2>
   <tr>
           <td class="label"> Designato in Data   </td>
           <td>
            <input type="text"  title="Giorno Data di Designazione" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_DESIGNAZIONE%>" value="<%=DateUtils.getSysDate("dd")%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Mese Data di Designazione" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_DESIGNAZIONE %>" value="<%=DateUtils.getSysDate("MM")%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text"  title="Anno Data di Designazione" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_DESIGNAZIONE %>" value="<%=DateUtils.getSysDate("yyyy")%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
            </td>
   </tr>
  </table>
 </div>
<div id="motivoDes"  style="visibility:hidden; position:relative;  top:-52px ">
   <table >
   <tr>
           <td class="l" >Motivo della Designazione</td>
           <td class="L" width="70%">
           <select title="designazione" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE%>" onChange="cambiaMotivo();" >
           <%=motivoDesignazione%>
          </select>
         </td>
   </tr>
</table>
</div>

<div id="note"  style="visibility:hidden; position:relative;  top:-55px ">
<table width="68%"  >
   <tr>
           <td class="l"  width="30%" >Note</td>
           <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_NOTE%>"  cols=40 ></textarea>
            </td>

   </tr>
</table>
</div>
<div id="ufficioSotto"  style="visibility:hidden; position:relative;  top:-55px ">

<table>
<tr><td>&nbsp;</td></tr>

      <tr><td class="Titolo" colspan='8'>Autorità per la notifica al Condannato  </td></tr>
 <tr>
<!--autorità di polizia-->
          <td class="l" width=30%>Autorità  </td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small"  name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA%>">
              <%=autoritaEsterna%>
            </select>
           </td>
  </tr>
     <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciAvvocato','<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
       </td>
    </tr>
     <tr>
           <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_INDIRIZZO_TIPO_AUTORITA%>"  cols=40 ></textarea>
            </td>
    </tr>
 <tr>
     <td class="l" width=30%>Istituto di Detenzione </td>
     <td class="l">
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto"  name ="<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>"size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciAvvocato','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
     </td>
 </tr>
<tr><td>&nbsp;</td></tr>
      <tr><td class="Titolo" colspan='8'>Autorità per la notifica al Difensore  </td></tr>

 <tr>
<!--autorità di polizia-->
          <td class="l" width=30%>Autorità </td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small"  name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF%>">
              <%=autoritaEsternaDif%>
            </select>
           </td>
  </tr>
     <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciAvvocato','<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
       </td>
    </tr>
</table>

</div>
<div id="conferma"  style="visibility:hidden; position:relative;  top:-85px ">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td colspan=2>
        <input class="bottone"  id="confermaBtn" type="submit" value="Conferma" name="IA" onClick="Javascript:return EnableCombo();"> 
      </td>
<%-- MEV_21: tasto inserimento hidden --%>
    	<td colspan=2 id="inserimento" style="visibility:hidden;">
        <input class="bottone" type="button" value="Inserimento" name="IN" onClick="Javascript:Inserisci();">
    	</td>
<%-- MEV_21: FINE --%> 
    </tr>
  </table>
</div>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">

  <%-- MEV_21: --%>
  <input type="HIDDEN" name="lTipoInserimento" id="lTipoInserimento" value="reginde">
  <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
</form>
<script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadInserisciAvvocato");

  frmvalidator.setAddnlValidationFunction("Verify");


</script>
</body>
</html>