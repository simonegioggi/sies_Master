<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<html>

<head>
<title>[S.I.E.S.] - GestioneVerbale </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<%//modifica relativa al tipo istituto%>
<script language="JavaScript">
      var desktop;

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
</script>
 <%//fine modifica relativa al tipo istituto%>

<script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
</script>
<script language="JavaScript">
function Verifica()
{
      if (document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
       document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
      if (document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
       document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value;
        var data_to_verify = document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;
      if (! ControllaData(data_to_verify))
      {
        alert('Data Pervenimento non valida');
        return false;
      }

      if (document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA%>.value.length==1)
       document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA%>.value='0'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA%>.value;
      if (document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA%>.value.length==1)
       document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA%>.value='0'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA%>.value;
        var data_to_verify = document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA%>.value+'/'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA%>.value+'/'+document.LoadInserisciVerbaleArresto.<%=ICostantiVerbale.CAMPO_ANNO_DATA_SCADENZA%>.value;
      if (! ControllaData(data_to_verify))
      {
        alert('Data Scadenza non valida');
        return false;
      }
      return true;
 }

</SCRIPT>



</head>


		<body class="corpo">
			<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			 <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%

			%><font class="campo">Inserimento Notifica Carcere</font>

       </td>
      </tr>
   </table>
    <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciVerbaleArresto">
     <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.verbale.action.ActInserisciNotificaCarcere">
		 <table cellspacing=2 cellpadding=2>


		<tr>
				<td class="l">Data pervenimento notifica</td>
				<td class="l" colspan=3>
        <input title="Giorno Pervenimento" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				-
        <input title="Mese Pervenimento" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
				-
        <input title="Anno Pervenimento" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</tr>

    <tr>
       <td class="l" >Data scadenza altra pena</td>
        <td class="l" colspan=3>

          <input title="Giorno Scadenza" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
         -
          <input title="Mese Scadenza" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Scadenza" size=4 maxlength=4 value="" type="text" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>

      <tr>
        <td class="l">Istituto di Detenzione</td>

        <td class="l" colspan=3>
        <input readonly Title="Istituto" name="Comune" value="" size=50 >
        <input type="hidden" readonly Title="Istituto" name="<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50 >
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciVerbaleArresto','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        <img src="/images/filefolder.gif" border=0></a></td>
        </td>

         </tr>
          <td>&nbsp;</td>

         <tr>
          <td>
          <INPUT  class="bottone" type="submit" name="INSERISCI" value="Conferma">
         </td>
        </tr>

</table>
		</form>
 <script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadInserisciVerbaleArresto");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_SCADENZA%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_SCADENZA%>","lt=12");

   frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_SCADENZA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_SCADENZA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_SCADENZA%>","lt=2099");

   frmvalidator.setAddnlValidationFunction("Verifica");

 </script>
	</body>
</html>