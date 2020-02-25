<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>

<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Atti Visti per Soggetto - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
    function init()
    {
      document.LoadRicercaAttiVistiPerSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
    }
    function Verify()
    {
      var data_to_verify=document.LoadRicercaAttiVistiPerSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadRicercaAttiVistiPerSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadRicercaAttiVistiPerSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
        alert('Data di nascita non valida');
        return false;
      }
      return true;
    }
  </script>
  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
  </script>

</head>

<body class="corpo"  onLoad="Javascript:init();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Atti pervenuti per Soggetto</font></td>
      </tr>
    </table>
  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaAttiVistiPerSoggetto">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActRicercaAttiVistiPerSoggetto">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Cognome</td>
        <td class="l"><input title="Cognome Soggetto" type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30" maxlength="30"></td>
      </tr>

      <tr>
        <td class="l">Nome</td>
        <td class="l"><input title="Nome Soggetto"  type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30" maxlength="30"></td>
      </tr>

      <tr>
        <td class="l">Comune di nascita</td>
        <td class="l">
          <input Title="Comune di nascita" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" value="" type="text" maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadRicercaAttiVistiPerSoggetto','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
          <img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
        </td>
      </tr>

      <tr>
        <td class="l">Stato di Nascita</td>
        <td class="L">
          <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
            <%= nazioni %>
         </select>
         </td>
      </tr>

      <tr>
        <td class="l">Data di nascita </td>
        <td class="l">
          <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>

    <br>
 	  <table cellspacing=2 cellpadding=2>
   	  <tr>
     	  <td class="lVerdeNB" >
       	  N.B.: La ricerca è limitata agli atti ricevuti già presi in visione.
        </td>
 	    </tr>
   	</table>
   	<br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>

  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaAttiVistiPerSoggetto");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");


    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=2010");

  </script>
</body>

</html>