<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="dataInsFS"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiAutoritaPolizia"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiAutorita"  				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"  				scope="request" class="java.lang.String"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.sius.richiestaatti.action.ActInserisciVisitaFiscaleFamiliare";
%>

<script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function CambiaDestinatario(a_indice)
    {
			document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[a_indice].value=''
    }
</script>

<html>
<head>
  <title>[S.I.E.S.] - Richiesta Vista Fiscale per Familiare </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function Verify()
    {
      if (document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
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

      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      if (document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0].value == '')
      {
        alert('La Sede del destinatario ASL è obbligatoria');
        return false;
      }

      if (document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value != '-'
          && document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1].value == '')
      {
        alert('La Sede del destinatario Autorità Polizia è obbligatoria');
        return false;
      }

      if (document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value != '-'
          && document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2].value == '')
      {
        alert('La Sede del Ulteriore Destinatario è obbligatoria');
        return false;
      }

      // Controlla che almeno un destinatario sia inserito
      if (document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value == '-'
       && document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value == '-'
       && document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value == '-')
      {
        alert('Inserire almeno un Destinatario.');
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
        <font class="campo">Richiesta Visita Fiscale per Familiare</font>
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

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaVisitaFiscaleFamiliare">
    <table cellspacing="2" cellpadding="2">

    	<tr>
        <td class="l">Data Emissione <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

			<!-- Primo destinatario + luogo -->
      <tr>
        <td class="l">ASL</td>
        <td class="L" colspan=3>
         <table>
           <tr>
           	<td class="l"> Tipo </td>
           	<td class="l" >
            	<select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" onchange="Javascript:CambiaDestinatario('0')"  >
              	<%= TipiIstituti1 %>
             	</select>
           	</td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadInserisciRichiestaVisitaFiscaleFamiliare','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]' , document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0][document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0> </a>
        	 </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>

      <!-- Secondo destinatario + luogo -->
      <tr>
        <td class="l">Autorità di Polizia</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Tipo </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" onchange="Javascript:CambiaDestinatario('1')"  >
               <%= TipiAutoritaPolizia %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadInserisciRichiestaVisitaFiscaleFamiliare','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]' , document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0][document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>

      <!-- Terzo destinatario + luogo -->
      <tr>
        <td class="l">Ulteriore Destinatario</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Tipo </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" onchange="Javascript:CambiaDestinatario('2')"  >
               <%= TipiAutorita %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadInserisciRichiestaVisitaFiscaleFamiliare','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1]' , document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1][document.LoadInserisciRichiestaVisitaFiscaleFamiliare.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>

      <!-- Campo Note 1 + campo hidden -->
      <tr>
          <td class="l">Nome Congiunto</td>
          <td class="L" colspan=3>
           <input title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  value="" type="text" maxlength="80" size="80">
          </td>
          <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >
      </tr>

      <!-- Campo Note + campo hidden -->
      <tr>
          <td class="l">Luogo Ricovero</td>
          <td class="L" colspan=3>
           <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=80 rows=4 ></textarea>
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
    var frmvalidator = new Validator("LoadInserisciRichiestaVisitaFiscaleFamiliare");

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