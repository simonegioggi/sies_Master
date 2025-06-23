<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="dataInsFS"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo"     scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="sentenza"      scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="ufficioSiep"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.sius.richiestaatti.action.ActInserisciParerePMRidetermPena";
  String lLuogoUfficio = "";
  String lEstremiSentenza = "";
  String lRegEsecuzione = "";

  if (! Utils.isNullObj(ufficioSiep))
    {lLuogoUfficio = ufficioSiep.getDescrComune();}
  if ( fascicolo.getChiaveAnno() != null)
    {
      lRegEsecuzione = StringUtils.toStringJSP(fascicolo.getChiaveAnno()) +"/"+StringUtils.toStringJSP(fascicolo.getChiaveProgr()) ;
      if (sentenza.getNumeroSentenza() != null)
         {lEstremiSentenza = StringUtils.toStringJSP(sentenza.getNumeroSentenza())+"/"+ StringUtils.toStringJSP(sentenza.getAnnoSentenza()) +" - "+StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente()) +" "+StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente()) +" - "+StringUtils.toStringJSP((DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy"))) ;}
    }
%>

<script language="JavaScript">
    var desktop;
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
</script>
<script language="JavaScript">
    function Verify()
    {
      if (document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>.value != '-'
          && document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>.value == '')
          {
              alert('La Sede del destinatario è obbligatoria');
              return false;
          }

      if (document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      if (document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_ULTERIORE %>.value.length==1)
          document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_ULTERIORE%>.value='0'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_ULTERIORE%>.value;

      if (document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_ULTERIORE %>.value.length==1)
          document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_ULTERIORE %>.value='0'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_ULTERIORE %>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
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

      // Controllo validita' della data ulteriore
      var data_ulteriore=document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_ULTERIORE%>.value+'/'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_MESE_ULTERIORE%>.value+'/'+document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_ANNO_ULTERIORE%>.value;
      if (data_ulteriore != "//"){
      if (! ControllaData(data_ulteriore) )
       {
        alert('Data provvedimento sospensione non valida');
        return false;
       }
      else
       {
         document.LoadInserisciRidetermPena.<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>[1].value = data_ulteriore;
       }
      }

      return true;
    }
</script>


<html>
<head>
    <title>[S.I.E.S.] - Richiesta Parere PM Rideterminazione Pena</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
</head>

<body onLoad="document.forms[0].elements[0].focus()" class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Richiesta Parere PM Rideterminazione Pena</font>
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

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRidetermPena">
    <table cellspacing=2 cellpadding=2>

        <tr>
          <td class="l">Data Emissione <font class=ob>(*)</font></td>
          <td class="L">
            <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
            <input Title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
            <input Title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>

        <!-- Primo destinatario + luogo -->
        <tr>
          <td class="l">Destinatario</td>
          <td class="L">
          <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= TipiIstituti1 %>
          </select>
        </tr>
        <tr>
          <td class="l">Sede <font class=ob>(*)</font></td>
          <td class="l">
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="<%=lLuogoUfficio%>" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaUffici('LoadInserisciRidetermPena','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');">
                <img src="/images/filefolder.gif" border=0> </a>
          </td>
        </tr>

        <tr>
          <td class="l">N° Reg. Esecuzione </td>
          <td class="L">
          <input title="N° proc. esecuzione" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>"
                value="<%=lRegEsecuzione%>" type="text" maxlength="35" size="35">
          </td>
        </tr>
        <tr>
          <td class="l">Data Prov. Sospensione </td>
          <td class="L">
            <input Title="Giorno" value="" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_ULTERIORE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
            <input Title="Mese" value="" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_ULTERIORE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
            <input Title="Anno" value="" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_ULTERIORE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
            <input type="HIDDEN" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>" value="" >
          </td>
        </tr>
        <tr>
          <td class="l">Estremi sentenza </td>
          <td class="L">
          <input title="Estremi sentenza" name="<%=ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO%>"
                value="<%=lEstremiSentenza%>" type="text" maxlength="100" size="100">
          </td>
        </tr>

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=40 rows=4 ></textarea>
            </td>
            <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >
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
      var frmvalidator = new Validator("LoadInserisciRidetermPena");
        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

        frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","maxlen=35","La lunghezza massima per la Sede è di 35 caratteri");
<%--         frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","alpha"); --%>

        //Chiama la funzione di Verify().
        frmvalidator.setAddnlValidationFunction("Verify");

    </script>

  </body>
</html>