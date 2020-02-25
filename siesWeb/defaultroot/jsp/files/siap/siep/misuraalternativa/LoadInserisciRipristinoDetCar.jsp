<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<jsp:useBean id="tipoAutorita" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Evento </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
    var desktop;
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
</script>
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
      if (document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
       document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
      if (document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
       document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value;
        var data_to_verify = document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;
      if (! ControllaData(data_to_verify))
      {
        alert('Data Pervenimento non valida');
        return false;
      }

      if (document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
       document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
       document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value;
        var data_to_verify = document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciMaRipristinoDetCarc.<%=ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (! ControllaData(data_to_verify))
      {
        alert('Data Ingresso in carcere non valida');
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
        <font class="campo">Ripristino Detenzione In Carcere</font>
       </td>
      </tr>
   </table>
    <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMaRipristinoDetCarc">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMARipristinoDetCarc">
		<table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l" width="30%">Data pervenimento del verbale</td>
				<td class="l" colspan=3>
        <input title="Giorno Pervenimento" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				-
        <input title="Mese Pervenimento" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
				-
        <input title="Anno Pervenimento" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</tr>

    <tr>
       <td class="l" width="30%">Data ingresso in carcere</td>
        <td class="l" colspan=3>

          <input title="Giorno Arresto" size=2 maxlength=2 value="<%=DateUtils.getSysDate("dd")%>" type="text" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
         -
          <input title="Mese Arresto" size=2 maxlength=2 value="<%=DateUtils.getSysDate("MM")%>" type="text" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Arresto" size=4 maxlength=4 value="<%=DateUtils.getSysDate("yyyy")%>" type="text" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>

      <tr>
        <td class="l" width="30%">Autorità che ha proceduto all'accompagnamento in carcere</td>
        <td class="l" colspan=3>
          <select title="TipoAutorita" name="<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>">
            <%=tipoAutorita%>
          </select>
        </td>
      </tr>

      </tr>
      <tr>
         <td class="l" width="30%">Luogo</td>
         <td class="L">
           <input title="Luogo" value="" type="text" name="<%=  ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciMaRipristinoDetCarc','<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO %>');">
             <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
	       <td class="l">Indirizzo</td>
         <td class="L">
           <TEXTAREA title="Note" name="<%= ICostantiVerbale.CAMPO_NOTE %>"  cols=30></textarea>
         </td>
      </tr>
      <tr>
        <td class="l" width="30%">Istituto di Detenzione</td>
        <td class="l" colspan=3>
         <input readonly Title="Istituto" name="Comune" value="" size=50 >
         <input type="hidden" readonly Title="Istituto" name="<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50 >
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMaRipristinoDetCarc','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
           <img src="/images/filefolder.gif" border=0>
          </a>
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
  var frmvalidator  = new Validator("LoadInserisciMaRipristinoDetCarc");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO %>","alphabetic");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.setAddnlValidationFunction("Verifica");

 </script>
	</body>
</html>