<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesoggetto.action.ICostantiRegeSoggetto"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>
<%@ page import="siap.regesies.regesoggetto.model.RegeSoggettoModel" %>
<%@ page import="java.util.Iterator" %>

<jsp:useBean id="regesoggetto"    scope="request" class="siap.regesies.regesoggetto.model.RegeSoggettoModel"/>
<jsp:useBean id="nazioni"     scope="request" class="java.lang.String"/>
<jsp:useBean id="nazionalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sesso"       scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Gestione Soggetto - </title>
    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    </script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript">
      function Verify()
      {
        var ritorno = true;
        if (document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_COD_STATO_NASCITA%>[document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039')
        {
          document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>.value="";
          if (document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value.length==0)
          {
            alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
            return false;
          }
        }
        else
          document.ModificaRegeSoggetto.<%= ICostantiRegeSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value='';

        if (document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
          document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
        if (document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
          document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;

        ritorno =  controllaEtaSoggetto();
        return ritorno;
      }
    </script>

    <script language="JavaScript">
      function controllaEtaSoggetto()
      {
        var ritorno = true;
        var oggi = new Date();
        var anno = Math.abs(document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value);
        var mese = 1;
        var giorno = 1;
        if (document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length > 1 )
            mese = document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
        if (document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length > 1)
            giorno = document.ModificaRegeSoggetto.<%=ICostantiRegeSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
        var anno14 = anno + 14;
        var anno18 = anno + 18;
        var data_compleanno14 = new Date( anno14, mese -1, giorno);
        var data_compleanno18 = new Date( anno18, mese -1, giorno);
           //alert ("14esimo compleanno ->" + data_compleanno14.toString());
           //alert ("18esimo compleanno ->" + data_compleanno18.toString());
           //alert("Tipo Ufficio " + tipoUff);

           if (oggi < data_compleanno18)
              ritorno = window.confirm('Il regesoggetto non ha compiuto i 18 anni. Confermi il suo inserimento?');

        return ritorno;
      }
    </script>

  </head>
  <BODY class="corpo">
  <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione :</font>&nbsp;
      <%  String  lAction = "siap.regesies.regesoggetto.action.ActModificaRegeSoggetto";
          RegeSoggettoModel lSoggetto = regesoggetto;%>
            <font class="campo">Modifica Rege Soggetto</font>
      </td>
  </tr>
  </table>
  <br>
   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_SOLO_INCLUDE%>"/>
<br>

  <form  method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModificaRegeSoggetto" id="ModificaRegeSoggetto">
     <table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l">Cognome <font class=ob>(*)</font></td>
				<td class="L"><input title="Cognome" value="<%=lSoggetto.getCognome() %>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_COGNOME %>"  maxlength="35" size="35"></td>
				  <td class="l">Nome <font class=ob>(*)</font></td>
				<td class="L"><input title="Nome" value="<%=lSoggetto.getNome() %>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_NOME %>"  maxlength="35" size="35"></td>
		</tr>
		<tr>
				<td class="l">Sesso <font class=ob>(*)</font></td>
				<td class="L">
          <select title="Sesso" name="<%=ICostantiRegeSoggetto.CAMPO_SESSO%>">
           <%= sesso %>
          </select>
        </td>
		</tr>
		<tr>
		    <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="L">
              <input title="Giorno Data di nascita" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"dd")) %>" type="text" name="<%=ICostantiRegeSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              /
              <input title="Mese Data di nascita" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"MM")) %>" type="text" name="<%= ICostantiRegeSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              /
              <input title="Anno Data di nascita" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"yyyy")) %>" type="text" name="<%= ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>

      </tr>
      <tr>
        <td class="l">Comune Nascita <font class=ob>(*)</font></td>
        <td class="L">
          <input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getDescrComuneNascita()) %>" type="text" name="<%= ICostantiRegeSoggetto.CAMPO_COD_COMUNE_NASCITA %>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('ModificaRegeSoggetto','<%= ICostantiRegeSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>

		<tr>
				<td class="l">Nazionalità</td>
				<td class="L">

          <select title="Nazionalità" name="<%=ICostantiRegeSoggetto.CAMPO_NAZIONALITA%>">
          <%= nazionalita %>
         </select>

				<td class="l">Stato di Nascita</td>
				<td class="L">

        <select  title="Stato di Nascita" name="<%=ICostantiRegeSoggetto.CAMPO_COD_STATO_NASCITA%>">
         <%= nazioni %>
         </select>
         </td>
 		</tr>

		<tr>
				<td class="l">Comune Nascita Estero</td>
				<td class="L"><input title="Comune di Nascita Estero" value="<%=StringUtils.toStringJSP(lSoggetto.getDescComuneNascitaEstero()) %>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>" maxlength="35" size="35" ></td>
		</tr>

		<tr>
				<td class="l">Paternità</td>
				<td class="L"><input title="Paternità" value="<%=StringUtils.toStringJSP(lSoggetto.getPaternita() )%>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_PATERNITA %>"  maxlength="35" size="35"></td>
		</tr>
		<tr>
				<td class="l">Cognome Madre</td>
				<td class="L"><input title="Cognome della madre" value="<%=StringUtils.toStringJSP(lSoggetto.getCognomeMadre()) %>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_COGNOME_MADRE %>"  maxlength="35" size="35"></td>

				<td class="l">Nome Madre</td>
				<td class="L"><input title="Nome della madre" value="<%=StringUtils.toStringJSP(lSoggetto.getNomeMadre() )%>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_NOME_MADRE %>"  maxlength="35" size="35"></td>
		</tr>
		<tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
        <td class="l">Codice Fiscale</td>
        <td class="L"><input  title="Codice Fiscale" id=<%= ICostantiRegeSoggetto.CAMPO_COD_FISCALE %>
        value="<%=StringUtils.toStringJSP(lSoggetto.getCodFiscale()) %>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_COD_FISCALE %>"  maxlength="16" size="18"></td>

				<td class="l">Atto Nascita</td>
				<td class="L"><input title="Atto di nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getAttoNascita() )%>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_ATTO_NASCITA %>"  maxlength="10" size="10"></td>
		</tr>

    <tr>
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
		<%--<td class="l">Codice Fascicolo Rosso</td>
		<td class="L"><input title="Codice Fascicolo Rosso" value="< %=StringUtils.toStringJSP(lSoggetto.getCodCs()) %>" type="text"
      		name="< %= ICostantiSoggetto.CAMPO_COD_CS %>"  maxlength="7" size="7"></td>
		--%>
       <td class="l">Codice CUI</td>
				<td class="L"><input title="Codice CUI" value="<%=StringUtils.toStringJSP(lSoggetto.getCodAfis()) %>" type="text"
        name="<%= ICostantiRegeSoggetto.CAMPO_COD_AFIS %>"  maxlength="7" size="7"></td>
		  <td >&nbsp;</td><td >&nbsp;</td>

        </tr>

		<tr>
      <td class="l">Note</td>
      <td class="L" colspan=3>
        <TEXTAREA title="note" name="<%= ICostantiRegeSoggetto.CAMPO_NOTE %>"  cols=80 rows=5 ><%=StringUtils.toStringJSP(lSoggetto.getNote() )%></textarea>
      </td>
		</tr>
    <tr>
      <td colspan=2>
        <input onclick="Javascript:return Verify();"  class=bottone  type="submit" value="Conferma">
      </td>
    </tr>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
    <input type="HIDDEN" name="<%=ICostantiRegeSoggetto.CAMPO_ID_FILE%>" value="<%=lSoggetto.getIdFile()%>">

  </table>

  </form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("ModificaRegeSoggetto");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_NOME %>","req","Il campo Nome Soggetto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_NOME %>","alpha");

  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COGNOME %>","req","Il campo Cognome Soggetto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COGNOME %>","alpha");


  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA %>","req","Il campo Anno di Nascita è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");


  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COD_FISCALE %>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COD_AFIS %>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_NAZIONALITA%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COD_STATO_NASCITA%>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_PATERNITA%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COGNOME_MADRE%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_NOME_MADRE%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric");
  <%-- Ticket#202506130166 - SIES: Anomalia inserimento provvedimento - schermata sede dell'autorità emittente--%>
  <%-- ELIMINATO CONTROLLO per consentire inserimento comuni tipo MERANO/MERAN) --%>
<%--   frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_COD_COMUNE_NASCITA %>","alpha"); --%>
<%--   frmvalidator.addValidation("<%= ICostantiRegeSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>","alpha"); --%>

</script>
</body>
</html>