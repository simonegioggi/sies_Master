<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="nuovaistanza" scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>

<jsp:useBean id="statonuovaistanza" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca Nuova Istanza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  <!-- 20210524	MEV Scheda-21 -->
  function ListaComuniNascita(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
  }

    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      /* Esempio:
      if (document.LoadInserisciNuovaIstanza.<%="ICostantiNuovaIstanza.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciNuovaIstanza.<%="ICostantiNuovaIstanza.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo corretteza campo 'Data Nascita Soggetto' 
      //=============================================================
      var data_to_verify = document.LoadRicercaNuovaIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+ 
                           document.LoadRicercaNuovaIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+ 
                           document.LoadRicercaNuovaIstanza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value; 

                           if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Istanza non corretta ' + data_to_verify); 
        document.LoadRicercaNuovaIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.focus(); 
        return false; 
      } 
      /*
      if ((progReg_to_verify.length > 0) && ((progReg_to_verify < minProgReg) || (progReg_to_verify > maxProgReg))) {
    	  alert('Il Numero Registro deve essere compreso tra ' + minProgReg + ' e ' +  maxProgReg); 
    	  document.LoadRicercaNuovaIstanza.<%=ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO%>.focus();
    	  return false; 
      } 
     
      if ((annoRegIni_to_verify.length > 0)  && (annoRegFin_to_verify.length > 0) ) {
          if (annoRegIni_to_verify > annoRegFin_to_verify) {
    	  	alert('Errore nell''inserimento di Anno e Numero Registro' ); 
    	  	document.LoadRicercaNuovaIstanza.<%=ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_INIZIALE%>.focus();
    	  	return false; 
          }
          if (annoRegIni_to_verify == annoRegFin_to_verify) {
        	  if ((progRegIni_to_verify.length > 0)  && (progRegFin_to_verify.length > 0) && (progRegIni_to_verify > progRegFin_to_verify) ) {
      	  		alert('Errore nell''inserimento di Anno e Numero Registro' ); 
      	  		document.LoadRicercaNuovaIstanza.<%=ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_INIZIALE%>.focus();
      	  		return false; 
        	  }
            }
      } 
      */
      function cancellaCodComuneReale() {
       	document.LoadRicercaNuovaIstanza.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
      }	 
    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Ricerca Istanza</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaNuovaIstanza">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActRicercaNuovaIstanza">
  <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
     <tr><td class="Titolo" colspan=4>Anno Numero Istanza</td></tr>
     <tr>
      <td class="l">Anno/Numero Registro Istanza</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(nuovaistanza.getAnnoRegistro()) %>"
               name="<%= ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO %>"  
               > 
        <input type="text" maxlength="9" size="11" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(nuovaistanza.getProgrRegistro()) %>"
               name="<%= ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO %>"  
               > 
      </td> 
    </tr>
     <tr><td class="Titolo" colspan=4>Intervallo Numero Registro Istanza</td></tr>
    <tr>
      <td class="L" >
        <font class="label">
          Anno/Numero Iniziale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Iniziale" name="<%= ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_INIZIALE %>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Iniziale" name="<%= ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_INIZIALE %>" maxlength="10" size="10">
      </td>
      <td class="L">
        <font class="label">
          Anno/Numero Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Finale" name="<%= ICostantiNuovaIstanza.CAMPO_CHIAVE_ANNO_FINALE %>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Finale" name="<%= ICostantiNuovaIstanza.CAMPO_CHIAVE_PROGR_FINALE %>" maxlength="10" size="10">
      </td>
     </tr>
    
    <tr><td class="Titolo" colspan=4>Ricerca Soggetto</td></tr>
      <tr>
        <td class="l">Cognome</td>
        <td class="l"><input title="Cognome Soggetto" type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l">Nome</td>
        <td class="l"><input title="Nome Soggetto"  type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l">Data di nascita </td>
          <td class="l"><input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>
       <tr>
        <td class="l">Comune di nascita</td>
        <td class="l">
          <input title="Comune di Nascita" value="" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" maxlength="30" size="30" onChange="cancellaCodComuneReale();">
          <a href="Javascript:ListaComuniNascita('LoadRicercaNuovaIstanza','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
          <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
    
    <tr><td class="Titolo" colspan=4>Ricerca per Attività</td></tr>
     <tr>
         <td class="L">Per Attività sull'istanza</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

         <td class="l"><select name="<%=ICostantiNuovaIstanza.CAMPO_ATTIVITA_ISTANZA%>" >
          <%= statonuovaistanza %>
                     </select> </td>
      </tr>
           <tr>
         <td class="L">Per Tipologia Istanza</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

         <td class="l"><select name="<%=ICostantiNuovaIstanza.CAMPO_TIPOLOGIA_ISTANZA%>" >
          <option value="-" >
                -</option>
          <option value="P">
                Presentata</option>
          <option value="D">
                Depositata</option>
                       </select> </td>
      </tr>
      
    
    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaNuovaIstanza");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");

  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_CONTENUTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_ISTANZA %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_ISTANZA %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_ISTANZA %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_NOTE %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_FLAG_PRESDEP %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_SOGG_PRESENTANTE_IDENTIFICATO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_AUTORITA_MITTENTE %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_SEDE_MITTENTE %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_ESITO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_UFFICIO_DESTINATARIO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_STATO_ISTANZA %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_OPERATORE_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_UFFICIO_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiNuovaIstanza.CAMPO_EVE_ID_EVENTO %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>