<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.jms.jmscode.action.ICostantiJmsCode" %>
<%@ page import="siap.jms.jmscode.model.JmsCodeModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="nazioni"     scope="request" class="java.lang.String"/>

<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <title> [S.I.E.S.] - Iscrizione Istanza - Ricerca Soggetto </title>
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
       	document.LoadRicercaSoggettoIstanza.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
      }	
      
  </script>

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    function Verify()
    {
    if (document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value=="" && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_NOME%>.value==""
         && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==0 && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==0 && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value.length==0
         && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>.value=="" && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value =="-" && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.value ==""
         && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.value=="" && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.value=="" && document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>.value=="")

      {
        alert("Inserire almeno il Cognome del Soggetto ");
        document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();

        return false;
      }

     if (document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
			document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
		if (document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
			document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
		  var data_to_verify=document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettoIstanza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;

	     if (! ControllaData(data_to_verify) && data_to_verify.length>2)
		  {
               alert('Data di nascita non valida');
			   return false;
		  }


    return true;
  }

    </script>
</head>

<body class="corpo">

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaSoggettoIstanza">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActRicercaSoggettoIstanza">

    <input type="HIDDEN" name="<%=ICostantiJmsCode.CAMPO_DESCRIZIONE%>">
	<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Iscrizione Istanza - Ricerca Soggetto</font></td>
      </tr>
    </table>
<br>
<table width="100%" >
    <tr><td class="Titolo" align="center" colspan="2">Tipo Ricerca</td></tr>
    <tr>
       <td class="c" colspan="2">
           Nell'Ufficio &nbsp;<input type="radio" name="tipoRicerche" value="ufficio" checked>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nel distretto &nbsp;<input type="radio" name="tipoRicerche" value="distretto" >
       </td>
    </tr>
      <tr>
        <td class="l">Cognome</td>
        <td class="l"><input title="Cognome Soggetto" type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l">Nome</td>
        <td class="l"><input title="Nome Soggetto"  type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l">Data di nascita </td>
          <td class="l"><input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%> >
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%> >
          </td>
      </tr>
       <tr>
        <td class="l">Comune di nascita</td>
        <td class="l">
          <input title="Comune di Nascita" value="" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" maxlength="30" size="30" onChange="cancellaCodComuneReale();">
          <a href="Javascript:ListaComuniNascita('LoadRicercaSoggettoIstanza','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
          <img src="/images/filefolder.gif" border="0" />
          </a>
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
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_PATERNITA %>">Paternità </span></td>
          <td class="l"><input title="Paternita" type="text" name="<%= ICostantiSoggetto.CAMPO_PATERNITA%>" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>">Cognome Madre </span></td>
          <td class="l"><input title="Cognome Madre" type="text" name="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>">Nome Madre </span></td>
          <td class="l"><input title="Nome Madre" type="text" name="<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>" size="30" maxlength="30"></td>
      </tr>

      <tr>
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>">Codice CUI</span></td>
        <td class="L">
          <input title="Codice CUI" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>" maxlength="7" size="7">
        </td>
      </tr>
            <tr>
        <td colspan="2">
          &nbsp;
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <INPUT class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
  </table>
 </form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
         var frmvalidator  = new Validator("LoadRicercaSoggettoIstanza");

          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","0","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","0","alpha");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","0","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","0","alpha");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");
          <%-- Ticket#202506130166 - SIES: Anomalia inserimento provvedimento - schermata sede dell'autorità emittente--%>
          <%-- ELIMINATO CONTROLLO per consentire inserimento comuni tipo MERANO/MERAN) --%>
<%--           frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>","alpha"); --%>
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_PATERNITA%>","alphabetic");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>","alphabetic");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>","alphabetic");
          
          frmvalidator.setAddnlValidationFunction("Verify"); 

          </script>