<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="foro"        scope="request" class="java.lang.String"/>
<jsp:useBean id="comune"      scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="codFunzione" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Avvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
  var desktop;

 function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

  function Verify()
  {
      if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
          document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value;
        if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
          document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value;

        var data_to_verify=document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value+'-'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value+'-'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value;
       if (data_to_verify >2 && ! ControllaData(data_to_verify))
          {
            alert('Data di nascita non valida');
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



		<body class="corpo">
		<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			<td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
			 AvvocatoModel lAvvocato = null;
			 String lAction = new String();
			 if( modalita.equals("C") )
			 {
			   lAction = "siap.siep.avvocato.action.ActVisualizzaDifensoreDaCancellare";
			   lAvvocato=new AvvocatoModel();
%>

			<font class="campo">Cancella Difensore</font>
			 <%

			   }

			   %>
</td>
</tr>
</table>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaAvvocato">

<table cellspacing=2 cellpadding=2>

		<tr>
				<td class="l">Cognome <font class=ob>(*)</font></td>
				<td class="l"><input  size=35 maxlength=35 title="Campo Cognome" type="text" name="<%= ICostantiAvvocato.CAMPO_COGNOME %>"  ></td>
		</tr>
		<tr>
				<td class="l">Nome <font class=ob>(*)</font></td>
				<td class="l"><input size=35 maxlength=35  title="Campo Nome" type="text" name="<%= ICostantiAvvocato.CAMPO_NOME %>"  ></td>
		</tr>
<tr>
		    <td class="l">Data di nascita </td>
          <td class="L">

            <input type="text" title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" value="" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" value="" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Cancella Difensore da Funzioni Amministrative
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90110000) ){
%>
			<a href="javascript:calendario('LoadModificaAvvocato','<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>','<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>','<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
       </td>
</tr>
 <tr>
		  <td class="l">Foro : </td>
			<td class="l">
				<select name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1">
        	 <%=foro%>
        </select>
     	</td>
    </tr>
		
<tr><td>&nbsp;</td></tr>
    <tr>
        <td colspan=2>
		    <input type="Hidden" name="Action" value="<%=lAction%>">
        <input class=bottone  type="submit" value="Conferma" onclick="return Verify();">
        </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadModificaAvvocato");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_COGNOME %>","req","Il campo Cognome Avvocato è obbligatorio");
  //frmvalidator.addValidation("<-%= ICostantiAvvocato.CAMPO_COGNOME %->","alpha");

  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_NOME %>","req","Il campo Nome Avvocato è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_NOME %>","alpha");

  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","req","Il campo Foro Avvocato è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_FORO %>","alpha");


  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");



  //frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>