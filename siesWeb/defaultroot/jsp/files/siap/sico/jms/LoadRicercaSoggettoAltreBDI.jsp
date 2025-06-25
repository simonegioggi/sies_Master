<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>


<jsp:useBean id="nazioni"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String"/>


<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title> [S.I.E.S.] - Ricerca Soggetto - </title>
  <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
	  <!-- 20210524	MEV Scheda-21 -->
      function ListaComuniNascita(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
      }
      function cancellaCodComuneReale() {
		document.LoadRicercaSoggetto.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
	  }	
      
  </script>
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
    function Verify()
    {
		if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
			document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
		if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
			document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;

		var data_to_verify=document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
		if (! ControllaData(data_to_verify) && data_to_verify.length>2)
		{	alert('Data di nascita non valida');
			 return false;
		}
	 }
    </script>
</head>

<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaSoggetto">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.jms.action.ActRicercaSoggettoAltreBDI">
    <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Soggetto in Altre BDI</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Cognome <font class=ob>(*)</font></td>
        <td class="l"><input title="Cognome Soggetto" type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30" maxlength="30"></td>
      </tr>

      <tr>
        <td class="l">Nome <font class=ob>(*)</font></td>
        <td class="l"><input title="Nome Soggetto"  type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30" maxlength="30"></td>
      </tr>

       <tr>
        <td class="l">Comune di nascita <font class=ob>(*)</font></td>
        <td class="l">
          <input title="Comune di Nascita" value="" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" maxlength="30" size="30" onChange="cancellaCodComuneReale();">
          <a href="Javascript:ListaComuniNascita('LoadRicercaSoggetto','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
          <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
     <tr>
        <td class="l">Stato di Nascita <font class=ob>(*)</font></td>
        <td class="L">
          <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
            <%= nazioni %>
          </select>
         </td>
      </tr>
      <tr>
        <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="l"><input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2">
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2"  >
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"maxlength="4" size="4" >
          </td>
      </tr>
      <tr>
        <td class="l">Atto Nascita</td>
        <td class="L">
          <input title="Atto di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>" maxlength="10" size="10">
        </td>
      </tr>
      <tr>
        <td class="l">Codice CUI</td>
        <td class="L">
          <input title="Codice CUI" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_CS %>" maxlength="6" size="6">
        </td>
      </tr>
      <tr>
        <td colspan="2">
        <br><br>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>

    </table>
  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaSoggetto");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","req");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","req");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");
    <%-- Ticket#202506130166 - SIES: Anomalia inserimento provvedimento - schermata sede dell'autorità emittente--%>
    <%-- ELIMINATO CONTROLLO per consentire inserimento comuni tipo MERANO/MERAN) --%>
<%--     frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>","alpha"); --%>
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>","req");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric");

  </script>
</body>

</html>