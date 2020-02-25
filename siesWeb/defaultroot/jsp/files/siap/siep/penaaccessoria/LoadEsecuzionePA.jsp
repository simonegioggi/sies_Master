<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>

<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>

<jsp:useBean id="dataInsFS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario1"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario2"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario3"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario4"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario5"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario6"  scope="request" class="java.lang.String"/>
<jsp:useBean id="CodMotivo"      scope="request" class="java.lang.String"/>
<jsp:useBean id="penaaccessoria" scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.siep.penaaccessoria.action.ActEsecuzionePA";

  String idPenaAccessoria = request.getParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);
  String CodTipoPenaAccessoria = request.getParameter(ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA);
  String DescrTipoPenaAccessoria = request.getParameter(ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA);
	// Parametri per i controlli della form.
  int LungDest1 = Destinatario1.length() ;
  int LungDest2 = Destinatario2.length() ;
  int LungDest3 = Destinatario3.length() ;
  int LungDest4 = Destinatario4.length() ;
  int LungDest5 = Destinatario5.length() ;
  int LungDest6 = Destinatario6.length() ;
%>
	<script language="JavaScript">
    var desktop;
  	function ListaComuni(a_formname,a_fieldname)
  	{
    	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
    function ListaUfficiPerTipo(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

	</script>

<html>
<head>
  <title>[S.I.E.S.] - Esecuzione Pena Accessoria </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function Init()
    {
			var data_fine_validità='<%=DateUtils.getDateToString(penaaccessoria.getDataFineValidita(), "dd/MM/yyyy")%>';
      if ( ControllaData(data_fine_validità) && data_fine_validità.length>2)
        alert('Attenzione! Pena Accessoria non più valida.');
			document.forms[0].elements[0].focus();
    }

    function Verify()
    {
      var descrTipoPA='<%=DescrTipoPenaAccessoria%>'
      if (descrTipoPA == 'Altre Pene Accessorie')
      {
        alert('Tipo Pena Accessoria non Eseguibile');
        return false;
      }

      var data_fine_validità='<%=DateUtils.getDateToString(penaaccessoria.getDataFineValidita(), "dd/MM/yyyy")%>';
      if ( ControllaData(data_fine_validità) && data_fine_validità.length>2)
      {
        if( window.confirm('Attenzione! Pena Accessoria non più valida. Vuoi continuare?') );
        else
        	return false;
      }
      if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_EMISSIONE%>.value;
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
			// 31/01/2008 Controllo abolito
      // Data inserimento Fascicolo Siep <= data Emissione
      //if ( !CompareDate( '<%=dataInsFS%>', data_emissione) )
      //{
      //  alert('Data Emissione minore della data di inserimento del fascicolo SIEP!');
      //  return false;
      //}

      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[0].value == '')
      {
        alert('La Sede del destinatario 1 è obbligatoria');
        return false;
      }

  		if ('<%=LungDest2%>'!='0')
			{
      	if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[1].value != '-'
        	  && document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[1].value == '')
      	{
        	alert('La Sede del destinatario 2 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest3%>'!=0)
			{
      	if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[2].value != '-'
        	  && document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[2].value == '')
      	{
        	alert('La Sede del destinatario 3 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest4%>'!=0)
			{
      	if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[3].value != '-'
        	  && document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[3].value == '')
      	{
        	alert('La Sede del destinatario 4 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest5%>'!=0)
			{
      	if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[4].value != '-'
        	  && document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[4].value == '')
      	{
        	alert('La Sede del destinatario 5 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest6%>'!=0)
			{
      	if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[5].value != '-'
        	  && document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[5].value == '')
      	{
        	alert('La Sede del destinatario 6 è obbligatoria');
        	return false;
      	}
			}

      // Controlla che almeno un destinatario sia inserito
      if (document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].value == '-' &&
				 (('<%=LungDest2%>'!=0 &&
							document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[1].value == '-') ||
				  ('<%=LungDest2%>'==0)) &&
				 (('<%=LungDest3%>'!=0 &&
							document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[2].value == '-') ||
				  ('<%=LungDest3%>'==0)) &&
				 (('<%=LungDest4%>'!=0 &&
							document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[3].value == '-') ||
				  ('<%=LungDest4%>'==0)) &&
				 (('<%=LungDest5%>'!=0 &&
							document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[4].value == '-') ||
				  ('<%=LungDest5%>'==0)) &&
				 (('<%=LungDest6%>'!=0 &&
							document.LoadEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[5].value == '-') ||
				  ('<%=LungDest6%>'==0 )) )
      {
        alert('Inserire almeno un Destinatario.');
        return false;
      }

      return true;
    }
  </script>
</head>

<body onLoad="Javascript:Init();" class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Comunicazione <%=DescrTipoPenaAccessoria%></font>
      </td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="Torna Indietro" width="24" height="24" border="0">
        </a>
      </td>
    </tr>

    <tr>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    </tr>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadEsecuzionePA">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Tipo di Pena Accessoria</td>
        <td class="campo"><%=DescrTipoPenaAccessoria%></td>
      </tr>
      <tr>
        <td class="l">Data Emissione <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaAccessoria.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaAccessoria.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

      <!-- Primo destinatario + luogo -->
      <tr>
        <td class="l">Destinatario n°1</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario1 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[0]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>

      <!-- Secondo destinatario + luogo -->
<%  	if (Destinatario2.length()>2)
			{%>
       	<tr>
        	<td class="l">Destinatario n°2</td>
        	<td class="L" colspan=3>
         		<table>
           		<tr >
           			<td class="l"> Autorità </td>
           			<td class="l" >
             			<select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               			<%= Destinatario2 %>
             			</select>
           			</td>
           		</tr>
           		<tr>
           			<td class="l">Sede <font class=ob>(*)</font></td>
           			<td class="l">
             			<input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              			value="" type="text" maxlength="35" size="35" >
                 	<a href="Javascript:ListaComuni('LoadEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[1]');">
              			<img src="/images/filefolder.gif" border=0> </a>
        				</td>
        			</tr>
        			<tr>
          			<td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          			<td class="L" colspan=3>
           				<input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          			</td>
        			</tr>
        		</table>
     		</tr>
		<%}%>

    	<!-- Terzo destinatario + luogo -->
<%  	if (Destinatario3.length()>2)
			{%>
      	<tr>
        	<td class="l">Destinatario n°3</td>
        	<td class="L" colspan=3>
         	<table>
           	<tr >
           		<td class="l"> Autorità </td>
           		<td class="l" >
             		<select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               		<%= Destinatario3 %>
             		</select>
           		</td>
           	</tr>
           	<tr>
           		<td class="l">Sede <font class=ob>(*)</font></td>
           		<td class="l">
             		<input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              		value="" type="text" maxlength="35" size="35" >
              		<a href="Javascript:ListaComuni('LoadEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[2]');">
              		<img src="/images/filefolder.gif" border=0> </a>
        			</td>
        		</tr>
        		<tr>
          		<td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          		<td class="L" colspan=3>
           			<input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          		</td>
        		</tr>
        </table>
      </tr>
		<%}%>

    <!-- Quarto destinatario + luogo -->
<%  if (Destinatario4.length()>2)
		{%>
      <tr>
        <td class="l">Destinatario n°4</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario4 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[3]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>
		<%}%>

    <!-- Quinto destinatario + luogo -->
<%  if (Destinatario5.length()>2)
		{%>
      <tr>
        <td class="l">Destinatario n°5</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario5 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[4]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>
		<%}%>

    <!-- Sesto destinatario + luogo -->
<%  if (Destinatario6.length()>2)
		{%>
      <tr>
        <td class="l">Destinatario n°6</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario6 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[5]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>
		<%}%>

      <!-- Campo Note + campo hidden -->
      <tr>
          <td class="l">Nota 1</td>
          <td class="L" colspan=3>
           <TEXTAREA title="Note" name="<%= ICostantiPenaAccessoria.CAMPO_AGGIUNTIVO %>"  cols=80 rows=1 ></textarea>
          </td>
          <input name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="hidden" >
      </tr>
      <tr>
          <td class="l">Nota 2</td>
          <td class="L" colspan=3>
           <TEXTAREA title="Note" name="<%= ICostantiPenaAccessoria.CAMPO_AGGIUNTIVO %>"  cols=80 rows=1 ></textarea>
          </td>
          <input name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="hidden" >
      </tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" value="<%=idPenaAccessoria%>" >
    <input type="HIDDEN" name="CodTipoPenaAccessoria" value="<%=CodTipoPenaAccessoria%>" >
    <input type="HIDDEN" name="DescrTipoPenaAccessoria" value="<%=DescrTipoPenaAccessoria%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadEsecuzionePA");

    frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

  </body>
</html>