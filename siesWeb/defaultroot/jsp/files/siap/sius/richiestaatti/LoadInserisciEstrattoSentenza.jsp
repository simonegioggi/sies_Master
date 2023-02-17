<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Date"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="dataInsFS"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti2"  scope="request" class="java.lang.String"/>

<%-- MEV_9 (D.lgs. 123/2018) --%>
<jsp:useBean id="ultimoEventoRichAtti"   scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.sius.richiestaatti.action.ActInserisciEstrattoSentenza";

  //MEV_9 (D.lgs. 123/2018)
  Date lUltimaDataRestitAttiIstruttori = ultimoEventoRichAtti.getDataRestituzioneAi();
%>

<script language="JavaScript">
    var desktop;
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    // Lista Uffici per TIPO_UFFICIO
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>

</script>

<html>
<head>
  <title>[S.I.E.S.] - Richiesta Estratto/Copia Provvedimento</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    
    <%-- INIZIO: MEV_9 (D.lgs. 123/2018) --%>
    function abilitaCampiDataRestituzione() {
    	if (document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CHECK_DATA_RESTITUZIONE%>.checked){
	   		document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.disabled = false;
	   		document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.disabled = false;
	   		document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.disabled = false;
    	}
    	else {
	   		document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.disabled = true;
	   		document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.disabled = true;
	   		document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.disabled = true;
    	}    	
   	}
    <%-- FINE: MEV_9 (D.lgs. 123/2018) --%>
    
    function Verify()
    {
      if (document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
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
      var data_restituzione = document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.value
                        +'/'+ document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.value
                        +'/'+ document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.value;
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
      
      if (document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>[0].value == '')
		{
              alert('Estremi provvedimento è un campo obbligatorio');
              return false;
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
        <font class="campo">Richiesta stampa Estratto/Copia Provvedimento</font>
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

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciEstrattoSentenza">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Data Emissione <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        
        <%-- INIZIO: MEV_9 (D.lgs. 123/2018) --%>
        <input value="S" type="checkbox" name="<%=ICostantiRichiestaAtti.CHECK_DATA_RESTITUZIONE%>" 
                             onClick="abilitaCampiDataRestituzione()"
                             >  Atti da restituire entro il </font>
                             

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

      <!-- Primo destinatario + luogo -->
		  <tr>
				<td class="l">Destinatario</td>
				<td class="L">
        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          <%= TipiIstituti1 %>
        </select>
      </tr>
      <tr>
        <td class="l">Sede <font class=ob>(*)</font></td>
        <td class="l">
           <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35">
              <%--a href="Javascript:ListaUffici('LoadInserisciEstrattoSentenza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');"--%>
              <a href="Javascript:ListaUfficiPerTipo('LoadInserisciEstrattoSentenza','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>',document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[document.LoadInserisciEstrattoSentenza.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>.selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>

      <tr>
        <td class="l">Estremi provvedimento <font class=ob>(*)</font></td>
        <td class="l"><input type="text" Title="Estremi provvedimento" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>" size="35"></td>
      </tr>

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=75 rows=4 ></textarea>
            </td>
            <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >
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
    var frmvalidator = new Validator("LoadInserisciEstrattoSentenza");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>", "req","Il campo Sede è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","maxlen=35","La lunghezza massima per la Sede è di 35 caratteri");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","alpha");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>", "req","Il campo Destinatario 01 è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>", "req","Il campo Sede 01 è obbligatorio");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

  </body>
</html>