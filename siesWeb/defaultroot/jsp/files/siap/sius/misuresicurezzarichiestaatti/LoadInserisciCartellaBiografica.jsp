<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione" %>

<jsp:useBean id="dataInsFS"          							scope="request" class="java.lang.String"/>

<%
	//Azione da chiamare per l'inserimento dei dati.
	String lFormName = "LoadInserisciCartellaBiografica";
  	String lAzione = "siap.sius.misuresicurezzarichiestaatti.action.ActInserisciCartellaBiografica";
%>

<script language="JavaScript">
  var desktop;

  // Destinatario  - Elenco Istituti Penitenziari.
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

</script>
	<html>
	<head>
  	<title>[S.I.E.S.] - Cartella biografica e Relazione di sintesi</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript">
    function Verify()
    {
      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      
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
      // Destinatario
      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>.value != '-'
          && document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>.value == '')
      {
        alert('La Sede del destinatario  è obbligatoria');
        return false;
      }

      // Controlla che almeno un destinatario sia inserito
      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>.value == '-')
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
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione : </font>&nbsp;
        <font class="campo">Cartella biografica e Relazione di sintesi</font>
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

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="<%=lFormName%>">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Data Emissione <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Mese" 	value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Anno" 	value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>
      <!-- Start new impl -->
			
       	<tr>
        	<td class="l">Istituto Detenzione</td>
        	<td class="l">
               	<input readonly  Title="Istituto" name="Comune" value="" size=50>
               	<input type="hidden" Title="Tipo" name="tipoDest" value="IST_DET" size="50">
               	<input type="hidden"  Title="Istituto" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="" size=50>
              	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciCartellaBiografica','<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO %>','Comune');">
              	<img src="/images/filefolder.gif" border=0></a>
        </tr>
          
        <!-- End new impl  -->

        <!-- Campo Note + campo hidden -->
        <tr>
        	<td class="l">Note 1</td>
          <td class="L" colspan=3>
             <input title="Periodo Detenzione" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>" type="text" maxlength="80" size="80">
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
    var frmvalidator = new Validator("<%=lFormName%>");

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