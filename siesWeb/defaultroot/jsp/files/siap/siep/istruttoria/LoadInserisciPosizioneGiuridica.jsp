<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="datairrevocabilita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="IdIstruttoriaCumulo" scope="request" class="java.lang.String"/>


<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Posizione Giuridica </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function Verify()
    {
      if (document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
              document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
              document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        return false;
      }
    if(!CompareDate("<%=datairrevocabilita%>",data_to_verify))
      {
        alert("La Data di Emissione del Documento non può essere Inferiore alla Data di Irrevocabilità della Sentenza");
        document.LoadInserisciPosizioneGiuridica.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
    }
  </script>
</head>

 <body class="corpo" >
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
       <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Inserisci Richiesta Posizione Giuridica</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciPosizioneGiuridica" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActInserisciPosizioneGiuridica">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>"	value="<%= IdIstruttoriaCumulo%>" >
    <table>
    <tr>
      <td class="Titolo" colspan=6>Oggetto </td>
    </tr>
    <tr>
      <td class="l">Autorità Destinatario <font class="ob">(*)</font></td >
      <td class="L">
        <select Title="Autorita " name="<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>" >
          <%=autorita%>
        </select>
      </td>
    </tr>
    <tr><td class="l">Sede <font class="ob">(*)</font></td><td class="L">
       <input title="Sede Autorita Emittente" value="" type="text" name="<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('LoadInserisciPosizioneGiuridica','<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>');">
       <img src="/images/filefolder.gif" border=0></a></td>
    </tr>
    <tr>
      <td class="l">Note <font class="ob">(*)</font></td>
      <td class="L" colspan=3>
        <TEXTAREA title="note" name="<%= ICostantiIstruttoria.CAMPO_NOTE %>"  cols=80 rows=5 ><%%></textarea>
      </td>
    </tr>
   <tr>
      <td class="l">Data Emissione </td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"  >
      </td>
  </tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
 </table>
 </form>
  <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("LoadInserisciPosizioneGiuridica");
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

      frmvalidator.addValidation("<%= ICostantiIstruttoria.CAMPO_NOTE%>","req","Il campo Note dell'Atto è obbligatorio");

      frmvalidator.addValidation("<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>","req","Il campo Sede Destinatario è obbligatorio");
      frmvalidator.addValidation("<%= ICostantiIstruttoria.AUTORITA_DESTINATARIO%>","req","Il campo Destinatario è obbligatorio");
      frmvalidator.addValidation("<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>","alpha");
  </script>
</body>
</html>