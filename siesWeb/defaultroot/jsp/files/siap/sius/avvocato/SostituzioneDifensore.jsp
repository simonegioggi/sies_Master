<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius" %>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione" %>


<jsp:useBean id="foro" scope="request" class="java.lang.String"/>

<jsp:useBean id="avvocatoVecchio"       scope="request" class="siap.sius.avvocato.model.AvvocatoSiusModel"/>
<jsp:useBean id="tipoAvvocato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna"       scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoDesignazione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaDif"       scope="request" class="java.lang.String"/>



<jsp:useBean id="idAvvVecchio"       scope="request" class="java.lang.String"/>
<jsp:useBean id="nazione"  scope="request" class="java.lang.String"/>  <!-- MEV_21 -->
<jsp:useBean id="statoAvv" scope="request" class="java.lang.String"/>  <!-- MEV_21 -->

<%
// MEV_21
AvvocatoSiusModel avvocato = new AvvocatoSiusModel();

%>
<html>
<head>
<title>[S.I.E.S.] - Gestione Avvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
  var desktop;

  function Inserisci()
  {
  <%-- MEV_21: modificata funzione di inserimento del difensore --%>
  <%--     document.LoadModificaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.avvocato.action.ActLoadInserisciDifensore"; --%>
  	// MEV_21 - Aggiunti controlli per inserimento avvocato non certificato.
    if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value.length==0 ) {
      alert('Il Cognome è obbligatorio');
      document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COGNOME %>.focus;
      return false;
    }
    
    if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_NOME%>.value.length==0 ) {
      alert('Il Nome è obbligatorio');
      document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_NOME %>.focus;
      return false;
    }
    
    /*
    if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='-') 
    {
      alert('Indicare lo stato di nascita');
      document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_STATO_NASCITA %>.focus;
      return false;
    }
    */
    
    if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039') 
    {
      document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value="";
      if (document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value.length==0) 
      {
        alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
        document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
        return false;
      }
    } else 	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value!='-') {
      document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value='';
      cancellaCodComuneReale();
    }

    if (   document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value.length > 0 	
        && document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value.length > 0 
       ) 
    {
      alert('Il Comune di Nascita e il luogo di Nascita Estero sono alternativi');
      document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
      return false;
    }

    if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
      document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value;
    
    if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
      document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value;

    var data_to_verify=document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value;
    if (! ControllaData(data_to_verify)) {
      alert('Data di nascita non valida');
      return false;
    }   
 
    if (Verify()) {
      document.LoadModificaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.avvocato.action.ActSostituzioneDifensore";
      document.LoadModificaAvvocato.submit();    
    }
  }
  
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  <%-- MEV_21: aggiunta abilitazione combo sulla submit --%>
  function EnableCombo() {
  	document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.disabled = false;
  	document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.disabled = false;
  	document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.disabled = false;
  }  

  <%-- MEV_21: aggiunta selezione comuni --%>
  function ListaComuniNascita(a_formname,a_fieldname) {
    if (document.LoadModificaAvvocato.lTipoInserimento.value != "reginde")
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
  }    
  
  <%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
  function ListaAvvocatiRegInde(a_formname) {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActLoadRicercaAvvocatoRegInde&formname="+a_formname,"Ricerca_Avvocato_RegInde","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600");
  } 
  
  <%-- MEV_21: sostituita dalla ListaAvvocatiRegInde
  function ListaAvvocati(a_formname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
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
    var tipo=document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_TIPO%>[document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex].value;
    
    // MEV 29 - 07/2015 Aggiunto controllo su presenza Foro
    if(document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value=="")
    {
      alert('Selezionare il Foro');
      return false;
    }
    
    if(tipo =="-")
    {
       alert("Campo Tipo Avvocato è obbligatorio");
       return false;
    }

<%-- MEV_21: eliminato 
    if(document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=="")
    {
      document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=document.LoadModificaAvvocato.idAvvVecchio.value;
    }
MEV_21: --%>

    EnableCombo();
    document.LoadModificaAvvocato.IA.disabled=true;
    document.LoadModificaAvvocato.IN.disabled=true;
    return true;
  }

  function cambiaMotivo()
  {
    var note =document.getElementById('note');
    var idxSelMotivo = document.LoadModificaAvvocato.<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE%>.selectedIndex;

    var valoreMotivo=document.LoadModificaAvvocato.<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE%>[idxSelMotivo].value;
    if(valoreMotivo=='0008')
    {
     note.style.visibility='visible';
     ufficioSotto.style.top='-90px';
     conferma.style.top='-35px';

    }else{
      note.style.visibility='hidden';
      ufficioSotto.style.top='-135px';
      conferma.style.top='-50px';
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

    var idxSel = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex;
  
    var valore=document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>[idxSel].value;
    var fiducia =document.getElementById('fiducia');
    var ufficio =document.getElementById('ufficio');
    var ufficioSotto =document.getElementById('ufficioSotto');
    var conferma =document.getElementById('conferma');
    var motivoDes =document.getElementById('motivoDes');
    
    note.style.visibility='hidden';
    ufficioSotto.style.top='-135px';

    if( valore == '01')
    {
      document.LoadModificaAvvocato.<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF%>.value=document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value;
    }

    conferma.style.visibility='visible';
    fiducia.style.visibility='hidden';
    if(valore == '01')
    {
      fiducia.style.visibility='hidden';
      ufficio.style.visibility='visible';
      ufficioSotto.style.visibility='visible';
      conferma.style.visibility='visible';
      motivoDes.style.visibility='visible';

      conferma.style.top='-100px';
    }
     if(valore == '02')
    {
      fiducia.style.visibility='visible';
      ufficio.style.visibility='hidden';
      ufficioSotto.style.visibility='hidden';
      motivoDes.style.visibility='hidden';

      conferma.style.visibility='visible';
      conferma.style.top='-350px';
    }
    if(valore == '-')
    {
      fiducia.style.visibility='hidden';
      ufficio.style.visibility='hidden';
      ufficioSotto.style.visibility='hidden';
      motivoDes.style.visibility='hidden';

      conferma.style.visibility='visible';
      conferma.style.top='-350px';
    }
    if(valore == '03')
    {
      fiducia.style.visibility='hidden';
      ufficio.style.visibility='hidden';
      ufficioSotto.style.visibility='hidden';
      motivoDes.style.visibility='hidden';

      conferma.style.visibility='visible';
      conferma.style.top='-350px';
    }
  }

</script>

</head>


<body class="corpo" onLoad="caricamento();">
<table>
   <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
   <td class=LBG><font class="label">Funzione : </font>&nbsp;&nbsp;
   <font class="campo">Sostituzione Difensore</font>
  </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

</tr>
</table>

<br><jsp:include page="/jsp/files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp"/><br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaAvvocato">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.avvocato.action.ActSostituzioneDifensore">

<table id="ricReginde">
  <tr>
    <%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde
    <td class="l">
      <a href="Javascript:ListaAvvocati('LoadModificaAvvocato');">
        Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    --%>
    
    <td class="label">
      <a href="Javascript:ListaAvvocatiRegInde('LoadModificaAvvocato');">
      Seleziona da RegInde <img src="/images/filefolder.gif" border=0></a>
    </td> 
      
    <td>&nbsp;&nbsp;&nbsp;</td>
    <td class="label">
      <a href="Javascript:ListaAvvocatiSiep('LoadModificaAvvocato');">
        seleziona dalla lista  Siep <img src="/images/filefolder.gif" border=0></a>
    </td>
  </tr>
</table>

<table >
<input type="hidden" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="">

<%-- MEV_21: aggiunti dati del difensore da sostituire non più caricati in form --%>
<tr>
  <td class="l" colspan=2>
    <input type="hidden" name="idAvvVecchio" value="<%=avvocatoVecchio.getAvvocato().getIdAvvocato()%>">
    <font class="label">Difensore da sostituire :  </font>&nbsp; 
    <font class="campo"> <%=StringUtils.toStringJSP(avvocatoVecchio.getAvvocato().getCognome())%> &nbsp; <%=StringUtils.toStringJSP(avvocatoVecchio.getAvvocato().getNome())%></font> &nbsp;&nbsp;
    <font class="label">  foro di  </font> &nbsp;
    <font class="campo"> <%=StringUtils.toStringJSP(avvocatoVecchio.getAvvocato().getForo())%></font>
  </td>
</tr>
<%-- MEV_21: FINE --%>
<tr>
     <td class="l" >Cognome</td>
    <td class="l"  ><input  size=35 maxlength=35 readonly title="Campo Cognome" type="text"  value="<%=avvocato.getAvvocato().getCognome()%>" name="<%= ICostantiAvvocato.CAMPO_COGNOME %>" ></td>
  </tr>
  <tr>
    <td class="l">Nome </td>
    <td class="l"><input size=35 maxlength=35 readonly title="Campo Nome" type="text"  value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getNome())%>" name="<%= ICostantiAvvocato.CAMPO_NOME %>"  ></td>
  </tr>
  
<%-- MEV_21:  --%>
<%
String comuneNascita = "", comuneNascitaEstero = "";
if (Utils.isPresent(avvocato.getAvvocato().getDescrStatoNascita())) {
	if ("ITALIA".equalsIgnoreCase(avvocato.getAvvocato().getDescrStatoNascita()))
		comuneNascita = avvocato.getAvvocato().getDescLuogoNascita();
	else
		comuneNascitaEstero = avvocato.getAvvocato().getDescLuogoNascitaReginde();
} else if (Utils.isPresent(avvocato.getAvvocato().getDescLuogoNascita())) {
	comuneNascita = avvocato.getAvvocato().getDescLuogoNascita();
}
%>  
  <tr>
    <td class="l">Comune di Nascita </td>
    <td class="L">
      <input title="Comune di Nascita"  readonly type="text" maxlength="35" size="35"
             value="<%=StringUtils.toStringJSP(comuneNascita)%>"  
             name="<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>"  >
      <a href="Javascript:ListaComuniNascita('LoadModificaAvvocato','<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>');" id="IconComuneNascita" style="visibility:hidden;">
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
<%if(avvocato.getAvvocato().getDataNascita() != null){%>
            <input type="text" readonly title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(avvocato.getAvvocato().getDataNascita()))%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(avvocato.getAvvocato().getDataNascita()))%>"maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly  title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(avvocato.getAvvocato().getDataNascita()))%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%}else{%>

            <input type="text" readonly title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly  title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%}%>
&nbsp;</td>
</tr>

  <tr>
    <td class="l">Foro </td>
    <td class="l">
      <select name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1" disabled="disabled"> 
        <%=foro%>
      </select>
    </td> 
  </tr>
  <tr>
      <td class="l">Indirizzo</td>
      <td class="l"><input readonly size=80 maxlength=200  title="Indirizzo" value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getIndirizzo())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_INDIRIZZO %>"  ></td>
  </tr>
    <%-- MEV_21: modificato campo Comune Residenza in comune Studio --%>
    <%--
    <tr>
      <td class="l">Comune di residenza </td>
      <td class="L">
        <input title="Comune di Residenza"   value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getDescComuneResidenza())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA %>"  maxlength="35" size="35">
      </td>
    </tr>
    --%>
    <tr>
        <td class="l">Con Studio in </td>
        <td class="L">
          <input type="text" title="Comune Sede dello Studio" maxlength="35" size="35" readonly
                 value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getDescrComuneStudio())%>" 
                 name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>" >
          <a id="IconComuneStudio" href="Javascript:ListaComuni('LoadModificaAvvocato','<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO %>');" style="visibility:hidden;" >
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
    </tr>     
    <%-- MEV_21: FINE--%> 
    
    
    <tr>
           <td class="l">Telefono</td>
           <td class="l"><input readonly size=12 maxlength=12   title="Telefono" value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getTelefono())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_TELEFONO %>"  ></td>
    </tr>
    <tr>
           <td class="l">Fax</td>
           <td class="l"><input readonly size=12 maxlength=12  title="Fax" value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getFax())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_FAX %>"  ></td>
    </tr>
    <tr>
           <td class="l">e-mail</td>
           <td class="l"><input readonly size=50 maxlength=50  value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getEMail()) %>" title="e-mail" type="text" name="<%= ICostantiAvvocato.CAMPO_E_MAIL %>"  ></td>
      </tr>
    <%-- MEV_21: Aggiunto campo PEC--%> 
    <tr>
      <td class="l">PEC</td>
      <td class="l"><input readonly size=50 maxlength=50  value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getPec()) %>" title="pec" type="text" name="<%= ICostantiAvvocato.CAMPO_PEC %>"  ></td>
    </tr>      
    <%-- MEV_21: FINE--%>  
    
      <tr>
           <td class="l">Codice Fiscale</td>
           <td class="l"><input size=20 maxlength=16 readonly value="<%=StringUtils.toStringJSP(avvocato.getAvvocato().getCodiceFiscale()) %>" title="codice fiscale" type="text" name="<%= ICostantiAvvocato.CAMPO_CODICE_FISCALE %>"  ></td>
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
    <tr><td>&nbsp;</td></tr>


  </table>

   <div id="fiducia"  style="visibility:hidden; position:relative;  top:-50px; left:330px  ">

   <table cellspacing=2 cellpadding=2>

   <tr>
           <td class="label">Nomina in Data</td>
           <td>
            <input type="text"  title="Giorno Data di Nomina" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_NOMINA%>" value="<%=DateUtils.getSysDate("dd")%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            -
            <input type="text" title="Mese Data di Nomina" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_NOMINA %>" value="<%=DateUtils.getSysDate("MM")%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            -
            <input type="text"  title="Anno Data di Nomina" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_NOMINA %>" value="<%=DateUtils.getSysDate("yyyy")%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
            </td>
   </tr>
</table>
</div>


<div id="ufficio"  style="visibility:hidden; position:relative;  top:-80px; left:320px  ">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="label"> Designato in Data   </td>
        <td>
          <input type="text"  title="Giorno Data di Designazione" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_DESIGNAZIONE%>" value="<%=DateUtils.getSysDate("dd")%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" title="Mese Data di Designazione" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_DESIGNAZIONE %>" value="<%=DateUtils.getSysDate("MM")%>" maxlength="2" size="2"onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          -
          <input type="text"  title="Anno Data di Designazione" name="<%= ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_DESIGNAZIONE %>" value="<%=DateUtils.getSysDate("yyyy")%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
    </tr>
  </table>
</div>
 
<div id="motivoDes"  style="visibility:hidden; position:relative;  top:-77px ">
  <table>
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

<div id="note"  style="visibility:hidden; position:relative;  top:-80px ">
  <table width="68%"  >
     <tr>
       <td class="l"  width="30%" >Note</td>
       <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiAvvocatoFascicoloSius.CAMPO_NOTE%>"  cols=40 ></textarea>
        </td>  
     </tr>
  </table>
</div>


<div id="ufficioSotto"  style="visibility:hidden; position:relative;  top:-90px ">
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
        <a href="Javascript:ListaComuni('LoadModificaAvvocato','<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA%>');">
        <img src="/images/filefolder.gif" border=0></a>
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
      <a href="Javascript:ListaIstitutoDetenzione('LoadModificaAvvocato','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
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
          <a href="Javascript:ListaComuni('LoadModificaAvvocato','<%=ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  </table>

</div>

<div id="conferma"  style="visibility:hidden; position:relative;  top:-110px ">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td colspan=2>
        <input class="bottone"  type="submit" value="Conferma" name="IA" id="confermaBtn" > 
      </td>  
        <%-- MEV_21: campo hidden di default --%>
        <td colspan=2 id="inserimento" style="visibility:hidden;">
          <input class="bottone"  type="button" value="Inserimento" name="IN" onClick="Javascript:Inserisci();">      
        </td>    	
        <%-- MEV_21: FINE --%>         
    </tr>
  </table>
</div>
  <%-- MEV_21: --%>
  <input type="HIDDEN" name="lTipoInserimento" id="lTipoInserimento" value="reginde">
  <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
  <%-- MEV_21: FINE --%>
</form>
<script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadModificaAvvocato");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>