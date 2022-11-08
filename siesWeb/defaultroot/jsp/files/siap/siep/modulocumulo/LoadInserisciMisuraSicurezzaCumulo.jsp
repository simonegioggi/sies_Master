<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiProcedimentoCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="misurasicurezzacumulo" scope="request" class="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"/>
<jsp:useBean id="naturaMisuraSicurezza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza" scope="request" class="java.util.Vector"/>

<jsp:useBean id="autoritaCumulo" scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoMisuraSicurezza" scope="request" class="java.lang.String"/>
<% 
//=========================================================================== 
// Form per l'inserimento e la modifica delle Misure di Sicurezza 
//=========================================================================== 

  String lcod = "";
  String lnat = "";
  if(modalita.compareTo("M")==0)
  { 
    if(misurasicurezzacumulo != null)
    { 
      lcod = misurasicurezzacumulo.getCodTipo();
      lnat = misurasicurezzacumulo.getCodNatura();
    } 
  } 
  
  String strOggetto ="";
  int element = 0;
  int eledaModificare = 0;
    Iterator itx = tipoMisuraSicurezza.iterator();
    while(itx.hasNext())
    {
         DecodificheModel lDecMod = (DecodificheModel)itx.next();
  
         strOggetto += lDecMod.getFiltro() +";";
         strOggetto += lDecMod.getCode()+";";
         strOggetto += lDecMod.getDescription()+"#";
         if(modalita.compareTo("M")==0)
         {
           if(lDecMod.getFiltro().equals(lnat))
           {  
             element++;
             if(lDecMod.getCode().equals(lcod))
             {
               eledaModificare = element;
             }
           }       
         }
     }

%> 
<!--    LoadInserisciMisuraSicurezzaCumulo     -->
<html>
<head>
  <title> Gestione MisuraSicurezzaCumulo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >

  var strOggetto = "<%=strOggetto%>";
  var TipodaModificare ="<%= eledaModificare%>";

  function caricaComboMod (valueTextStr, sep1, sep2, filtro, selField)
  {
    caricaCombo(valueTextStr, sep1, sep2, filtro, selField);
    selField.options.selectedIndex = TipodaModificare-1;
  }
  
  function caricaCombo (valueTextStr, sep1, sep2, filtro, selField)
  {
      // valueTextStr = stringa nel formato richiesto
      // sep1 = separatore interno alla coppia di valori
      // sep2 = separatore tra coppie
      // filtro = valore su cui fare il test
      // selField = oggetto combo da caricare
  
      clearDropDown(selField);
  
      var aPairs = valueTextStr.split(sep2);
  
      if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
      {
        aPairs[aPairs.length - 1] = null;
        aPairs.length--;
      }
  
      for (var i=0; i < aPairs.length; i++)
      {
        aValueText = aPairs[i].split(sep1);
        if (filtro=='null' || filtro==aValueText[0])
        {
          oItem = new Option;
          oItem.value = aValueText[1];
          oItem.text = aValueText[2];
          selField.options[selField.options.length] = oItem;
        }
      }
  
      selField.options.selectedIndex = 0;
      //se il valore del filtro è "-" disabilito il campo
      if(filtro=='-'){
        selField.disabled=true;
      }
      else{
        selField.disabled=false;
      }

  }
  
  function clearDropDown (selField)
  {
    while (selField.options.length > 0)
    selField.options[0] = null;
  }
  
    function Verify() 
    { 
      // Tipo Misura Obbligatoria
      if (document.LoadInserisciMisuraSicurezzaCumulo.<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_TIPO%>.value=='-')
      {
        alert('Il campo Tipo Misura è obbligatorio');
        return false;
      }
      
      if(document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value.length > 0)
      { 
         
        if (document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value.length==1)
          document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value='0'+document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value;
        if (document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MESE_DATA_FINE_VALIDITA%>.value.length==1)
          document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MESE_DATA_FINE_VALIDITA%>.value='0'+document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MESE_DATA_FINE_VALIDITA%>.value;
  
        var data_to_verify = document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MESE_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;
  
        if (!ControllaData(data_to_verify) )
        {
          alert('Data Fine Validità Errata');
          document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.focus();
          return false;
        }
        
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        
        retValue = confirm("Attenzione: valorizzando la Data Fine Validità, la misura di sicurezza \n non sarà considerata valida per il procedimento!");

        if(retValue)
        { 
	          // Controllo : data di sistema deve essere >= Data Fine Validità .
	          if( !CompareDate( data_to_verify, data_sistema) )
	          {
	              alert('Data Fine Validità non può essere superiore alla data odierna!');
	              document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.focus();
	              return false;
	          }
        }
        else
        {
             return false;
        } 
      } 

      // Stato della Misura: 
      if(document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>.value == "" &&
   		 document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>.value == "" &&
   		 document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>.value == "-" &&
   		 (   document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO%>.value == "" 
   		  || document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO%>.value == "-")	)	
      {
			// OK 
      }	
      else if(document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>.value != "" &&
   		 	document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>.value != "" &&
   		 	document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>.value != "-" &&
   		 	(   document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO%>.value != "" 
   		 	 && document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO%>.value != "-" ) )
      {
			 // OK 
      }
      else
      {	
	      // Anno e Numero Fascicolo Siep a cui è iscritta la Misura
	      if(document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>.value=="" )
	      {
	      		alert("Inserire Numero Fascicolo SIEP di classe IV")
	      		document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>.focus();
	      		return false;
	      }
	      
	      if(document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>.value=="" )
	      {
	        	alert("Inserire Anno Fascicolo SIEP di classe IV")
	        	document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>.focus();
	        	return false;
	      }
	      
	      // Tipo e Luogo Autorità Emittente Fascicolo Siep a cui è iscritta la Misura
	      if(document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>.value=="-" )
	      {
	      		alert("Inserire Tipo Autorità ")
	      		document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO%>.focus();
	      		return false;
	      }
	      
	      if(document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO%>.value=="" ||
	      	document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO%>.value=="-"	)
	      {
	        	alert("Inserire Luogo Autorità ")
	        	document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO%>.focus();
	        	return false;
	      }
      }    
      
      // Durata Misura Obbligatoria (?)
      return true; 
    } 
    
    //==========================================================================
    // Ritorna alla lista delle Misure di Sicurezza per il Titolo
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.LoadInserisciMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInserisciMisuraSicurezzaCumulo.submit();
    }
    
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&TipoUfficio="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
  </script>
</head>

<body class="corpo" onload="javascript:caricaComboMod(strOggetto,';','#',document.LoadInserisciMisuraSicurezzaCumulo.<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_NATURA %>.value, document.LoadInserisciMisuraSicurezzaCumulo.<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_TIPO %>);">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        MisuraSicurezzaCumuloModel lMisuraSicurezzaCumulo = new MisuraSicurezzaCumuloModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciMisuraSicurezzaCumulo"; 
        %>
        <font class="campo">Inserimento Misura Sicurezza</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.siep.modulocumulo.action.ActModificaMisuraSicurezzaCumulo";
          lMisuraSicurezzaCumulo = misurasicurezzacumulo;
        %>
        <font class="campo">Modifica Misura Sicurezza</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaMisuraSicurezzaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraSicurezzaCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"        value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO %>" value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo()) %>">
  <input type="hidden" name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_STATO %>"                 value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getFlagStato()) %>">
  <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA %>" value="<%=modalita%>">               

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="titolo" colspan="2">Misura di Sicurezza</td>
    </tr>
    <tr>
      <td class="l">Natura Misura</td>
      <td class="l">
        <select title="Natura Misura" name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_NATURA %>" onChange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_COD_NATURA %>.value, document.LoadInserisciMisuraSicurezzaCumulo.<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_TIPO %>);">
        <%=naturaMisuraSicurezza%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Tipo Misura  <font class="ob">(*)</font></td>
      <td class="l">
        <select title="Tipo Misura" name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_TIPO  %>">

        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Durata Misura</td>
      <td class="l">
        Anni
        <input title="Anni" size="2" maxlength="2" type="text" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumAnni()) %>"  
               name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_NUM_ANNI %>"  >
        Mesi
        <input title="Mesi" size="2" maxlength="2" type="text" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumMesi()) %>"  
               name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_NUM_MESI%>"  >
        Giorni 
        <input title="Giorni" size="2" maxlength="2" type="text" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumGiorni()) %>" 
               name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_NUM_GIORNI %>"  >
      </td>
    </tr>

    <tr>
      <td class="l">Stato</td>
      <td class="L">
        <select title="Stato Misura" name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_STATO_MISURA%>" >
          <%=StatoMisuraSicurezza %>
        </select>
      </td>
    </tr>    

    <tr>
      <td class="l">Data Fine Validità </td>
      <td class="L">
          <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMisuraSicurezzaCumulo.getDataFineValidita(),"dd")) %>"  
            name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_GIORNO_DATA_FINE_VALIDITA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMisuraSicurezzaCumulo.getDataFineValidita(),"MM")) %>" 
            name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_MESE_DATA_FINE_VALIDITA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMisuraSicurezzaCumulo.getDataFineValidita(),"yyyy")) %>"
            name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
        
    <tr><td><br></td></tr>
    <tr>
      <td class="titolo" colspan="2">Iscritta al Procedimento</td>
    </tr>
    <tr>
      <td class="l">Anno/Numero SIEP</td>    
      <td class="L">
        <input type="text" title="Anno"  maxlength="4" size="4" 
               name="<%=ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO %>"
               value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getAnnoFascicoloSiepIV()) %>"
               onkeypress="return TicTabNumField(this,event)"
               onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero SIEP"  maxlength="14" size="14"
               value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumeroFascicoloSiepIV()) %>"
               name="<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>    
    </tr>
    <tr>
      <td class="l">Autorità</td>
      <td class="l">
        <select name="<%= ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO %>" >
          <option value="-">-</option>
          <%=autoritaCumulo%>
        </select>    
      </td>
    </tr>
  
    <tr>
      <td class="l">Luogo</td>
      <td class="L">
        <input type="text" title="Sede Ufficio"  maxlength="35" size="35" name="<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO %>" 
        					value="<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrLuogoEmittenteIV(), "" ) %>">
         <a href="Javascript:ListaUfficiPerTipo('LoadInserisciMisuraSicurezzaCumulo','<%=ICostantiProcedimentoCumulato.CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO %>',document.LoadInserisciMisuraSicurezzaCumulo.<%= ICostantiProcedimentoCumulato.CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO %>.value);">
          <img src="/images/filefolder.gif" border=0></a>
        </a>
      </td>
    </tr>
    
    <tr><td>&nbsp;</td></tr>
    <%
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
    //==========================================================================
    if (lMisuraSicurezzaCumulo!=null && lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo()!=null)
    {
      String lStato = "";
      String lDescStato = "";
        if      ( lMisuraSicurezzaCumulo.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto dal fascicolo originale";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("I")){lStato = "Iscritto"; lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
    %>
    <tr>
    <td class="l" width="25%">Stato</td>
        <td class="l">
          <font class="campo">&nbsp;<%=lStato%>&nbsp;&nbsp;(<%=lDescStato%>)</font>
        </td>
    </tr>
    <% } %>
  
    <%
    //========================================================================== 
    // Campo note visualizzato sia in inserimento sia in modifica dove l'utente
    // può motivare l'intervento sui dati su cui sta intervenendo
    //========================================================================== 
    %>
    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l">
        <textarea cols="100" rows="6" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MOTIVO_MODIFICA%>">
        	<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getMotivoModifica()) %>
        </textarea>
      </td>
    </tr>
    
<%  if(modalita.equals("I") )
  { %> 
    <tr>
      <td align="left">
        <input class="bottone" type="submit" title="Inserisci Misura" name="conferma" value="Conferma">
      </td>
    </tr>
<%  }
  else if(modalita.equals("M") )
  { %>
  <tr>
      <td align="left">
        <input class="bottone" type="submit" title="Modifica Misura" name="conferma" value="Conferma">
      </td>
    </tr> 
<%  }  %> 
  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraSicurezzaCumulo");

  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_ANNO_FAS_CUMULATO%>","lt=2099");
  
  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>","gt=39999");
  frmvalidator.addValidation("<%= ICostantiProcedimentoCumulato.CAMPO_CHIAVE_PROGR_FAS_CUMULATO%>","lt=49999");
  
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>