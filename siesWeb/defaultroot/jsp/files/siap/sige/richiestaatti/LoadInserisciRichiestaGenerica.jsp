<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="autorita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"      scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="datairrevocabilita" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Generica</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
     var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // Lista Uffici per TIPO_UFFICIO
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    
    // Destinatario 3 - Elenco Istituti Penitenziari.
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

    function Verify()
	  {
		  if (document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione non valida');
			 return false;
		  }
    if(CompareDate(data_to_verify,"<%=datairrevocabilita%>"))
    {
        alert("La Data di Emissione del Documento non può essere Inferiore alla Data di Irrevocabilità della Sentenza");
        document.LoadInserisciRichiestaGenerica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
    }
// Controlla che le coppie di campi Destinatario/Sede siano riempiti
      // Destinatario 1
      if (document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0].value == '')
      {
        alert('La Sede del destinatario 1 è obbligatoria');
        return false;
      }
      // Destinatario 2
      if (document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value != '-'
          && document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1].value == '')
      {
        alert('La Sede del destinatario 2 è obbligatoria');
        return false;
      }
      // Controlla che almeno un destinatario sia inserito
      if (document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value == '-'
       && document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value == '-'
       && document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value == ''
       )
      {
        alert('Inserire almeno un Destinatario.');
        return false;
      }

	  }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
  </script>

  </head>
<body onLoad="document.forms[0].elements[0].focus()" class="corpo">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta Generica</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
<br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciRichiestaGenerica" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.richiestaatti.action.ActInserisciRichiestaGenerica">
    
	<table  width="95%" >
   <tr><td class="Titolo" colspan=2>Destinatari  </td></tr></table>

  <table>   <tr>
            <td class="l">Destinatario<font class=ob>(*)</font></td>

            <input type="hidden"  Title="Tipo" name="tipoDest" value="AUT_EXT" size=50>

            <td class="l" >
              <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
               <%= autorita %>
              </select>
            </td>
          </tr>

          <tr>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
            <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaComuni('LoadInserisciRichiestaGenerica','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]');">
                <img src="/images/filefolder.gif" border=0> </a>
          </td>
          </tr></table>
     <table>     
          <!-- Secondo destinatario + luogo -->
		  <tr>
				<td class="L">Ufficio<font class=ob>(*)</font></td>
				<input type="hidden"  Title="Tipo" name="tipoDest" value="AUT_EXT2" size=50>
				
				
		<td class="l" >		
        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          <%= TipiIstituti1 %>
        </select></td>
      </tr>
      <tr>
        <td class="l">Sede <font class=ob>(*)</font></td>
        <td class="l">
           <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35">
              <%--a href="Javascript:ListaUfficiPerTipo('LoadInserisciRichiestaGenerica','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1].value',document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[1].value');"--%>
              <%--a href="Javascript:ListaUfficiPerTipo('LoadInserisciRichiestaGenerica','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>',document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[document.LoadInserisciRichiestaGenerica.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>.selectedIndex].value);"--%>
              <a href="Javascript:ListaUffici('LoadInserisciRichiestaGenerica','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr></table>
          
      <!-- Terzo destinatario + luogo -->
	<table>	  <tr>
             <td class="l">Istituto Penitenziario <font class=ob>(*)</font></td>

             <input type="hidden"  Title="Tipo" name="tipoDest" value="IST_DET" size=50>
             <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >

             <td class="l">
               <input type="hidden"  Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" value="" size="35">

               <input readonly  Title="Istituto" name="Comune" value="" size=50>
               <input type="hidden"  Title="Istituto" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="" size=50>
               <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRichiestaGenerica','<%= ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2]','Comune');">
               <img src="/images/filefolder.gif" border=0></a>
             </td>
          </tr>
</table>

<table  width="95%" >
<tr><td class="Titolo" colspan=2>Oggetto  </td></tr></table>
<table>   <tr>
        <td class="l">Data richiesta : </td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciRichiestaGenerica','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
      </tr>
      
      <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Tipo Richiesta</td>
            <td class="L" colspan=3>
             <input title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>" type="text" maxlength="80" size="80">
            </td>
        </tr>

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=80 rows=4 ></textarea>
            </td>
        </tr>
      

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr></table>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaGenerica");

  
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");

  
 </script>


	</form>

	</body>
</html>