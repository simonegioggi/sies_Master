<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Date"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="dataInsFS"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti2"  scope="request" class="java.lang.String"/>

<%-- MEV_9 (D.lgs. 123/2018) --%>
<jsp:useBean id="ultimoEventoRichAtti"   scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.sius.richiestaatti.action.ActInserisciRelazioneSanitaria";

  //MEV_9 (D.lgs. 123/2018)
  Date lUltimaDataRestitAttiIstruttori = ultimoEventoRichAtti.getDataRestituzioneAi();
%>

<script language="JavaScript">
    var desktop;
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
</script>
<script language="JavaScript">

	<%-- INIZIO: MEV_9 (D.lgs. 123/2018) --%>
	function abilitaCampiDataRestituzione() {
		if (document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CHECK_DATA_RESTITUZIONE%>.checked){
	   		document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.disabled = false;
	   		document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.disabled = false;
	   		document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.disabled = false;
		}
		else {
	   		document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.disabled = true;
	   		document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.disabled = true;
	   		document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.disabled = true;
		}    	
	}
	<%-- FINE: MEV_9 (D.lgs. 123/2018) --%>

    function Verify()
    {
      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      if (document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0].value == '')
          {
              alert('La Sede del destinatario n°1 è obbligatoria');
              return false;
          }
      if (document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value != '-'
          && document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1].value == '')
          {
              alert('La Sede del destinatario n°2 è obbligatoria');
              return false;
          }

      // Controlla che almeno un destinatario sia inserito
      if (document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value == '-'
          && document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value == '-')
          {
              alert('Inserire almeno un Destinatario con la relativa Sede.');
              return false;
          }

      if (document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
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
      if (document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CHECK_DATA_RESTITUZIONE%>.checked){
	      var data_restituzione = document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_RESTITUZIONE%>.value
	                        +'/'+ document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_RESTITUZIONE%>.value
	                        +'/'+ document.LoadInserisciRelazioneSanitaria.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_RESTITUZIONE%>.value;
	      if (! ControllaData(data_restituzione))
	      {
	        alert('Data Restituzione atti non valida');
	        return false;
	      }  
	      
	      if ( ! CompareDate( data_emissione, data_restituzione) )
	      {
	        alert('Data Emissione maggiore della Data Restituzione atti!');
	        return false;
	      }
      }         
      <%-- FINE: MEV_9 (D.lgs. 123/2018) --%>
      
      return true;
    }
</script>

<html>
  <head>
    <title>[S.I.E.S.] - Richiesta Relazione Sanitaria</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>

  <body onLoad="document.forms[0].elements[0].focus()" class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label"> Funzione :</font>&nbsp;
          <font class="campo">Richiesta Relazione Sanitaria</font>
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

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRelazioneSanitaria">
      <table cellspacing=2 cellpadding=2>

        <tr>
          <td class="l">Data Emissione <font class=ob>(*)</font></td>
          <td class="L">
            <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
            <input Title="Mese"   value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
            <input Title="Anno"   value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
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

        <!-- Primo destinatario + luogo -->
        <tr>
          <td class="l">Destinatario n°1</td>
          <td class="L" colspan=3>
          <table>
          <tr >
          <td class="l"> Tipo</td>
          <td class="l" >
          <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= TipiIstituti1 %>
          </select>
          </td>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaUffici('LoadInserisciRelazioneSanitaria','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]');">
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

        <!-- secondo destinatario + luogo -->
        <tr>
          <td class="l">Destinatario n°2</td>
          <td class="L" colspan=3>
          <table>
          <tr >
          <td class="l"> Tipo</td>
          <td class="l" >
          <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= TipiIstituti2 %>
          </select>
          </td>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
            <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaUffici('LoadInserisciRelazioneSanitaria','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1]');">
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

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=40 rows=4 ></textarea>
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
      var frmvalidator = new Validator("LoadInserisciRelazioneSanitaria");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

      frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","maxlen=35","La lunghezza massima per la Sede è di 35 caratteri");
<%--       frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","alpha"); --%>

      //Chiama la funzione di Verify().
      frmvalidator.setAddnlValidationFunction("Verify");

    </script>
  </body>
</html>