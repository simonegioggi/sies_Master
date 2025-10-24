<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.generaleprocedimento.action.ICostantiGeneraleProcedimento"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento"%>

<jsp:useBean id="udienza"         scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP"    scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
    String modalita = "";
    UdienzaModel lUdienza = udienza;
    if (lUdienza != null && lUdienza.getIdUdienza() != null)
        modalita = "readonly";
    String oldIdUdienza =  (fascicoloSiusGP.getGeneraleProcedimentoModel() != null &&  fascicoloSiusGP.getGeneraleProcedimentoModel().getUdiIdUdienza() != null) ? fascicoloSiusGP.getGeneraleProcedimentoModel().getUdiIdUdienza().toString() : "";

%>
<html>
<head>
  <title>[S.I.E.S.] - Prefissazione Udienza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/conferma.js"></script>



  <script language="JavaScript">
  function Verify()
  {

    if (document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value.length==1)
        document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value='0'+document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value;

    if (document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value.length==1)
        document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value='0'+document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value;

    // Controllo validità data Udienza.
    var dataUdienza= document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
                     document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+
                     document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;

    if (! ControllaData(dataUdienza))
    {
      alert('Data Udienza non valida!');
      return false;
    }
    var IdUdienza= document.LoadInserisciPreFissazioneUdienza.<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>.value;
    if (IdUdienza.length < 1)
    {
      alert('Selezionare una udienza !');
      return false;
    }
    // Controllo cambiamento Udienza
    var IdUdienzaOld = "<%=oldIdUdienza%>";
    if (IdUdienzaOld == IdUdienza)
    {
      alert('Non è possibile rifissare la stessa udienza!');
      return false;
    }

    return true;
  }

  </script>

  <script language="JavaScript">
    var desktop;

   // Chiamata funzione elenco Udienze.
    function ListaUdienze( aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo,  aNomeCampoIdUdienza, aNomeCampoCollegio)
    {

      // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
      var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActLoadRicercaUdienzaXProcedimenti";
          lLink += "&formname="+ aNomeForm;
          lLink += "&campoGG=" + aNomeCampoGG;
          lLink += "&campoMM=" + aNomeCampoMM;
          lLink += "&campoAA=" + aNomeCampoAA;
          lLink += "&campoLuogo=" + aNomeCampoLuogo;
          lLink += "&campoID=" + aNomeCampoIdUdienza;
          lLink += "&campoColl=" + aNomeCampoCollegio;

      desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=550" );
    }

  </script>

</head>

<%
    String lAzione = new String();
    lAzione = "siap.sius.udienzaprocedimento.action.ActInserisciPreFissazioneUdienza";
%>

<body class="corpo" >

  <table >
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Prefissazione Udienza</font>
      </td>
<%
if (modalita.compareToIgnoreCase("readonly") == 0 && request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO) != null)
{
%>
  <!-- BOTTONI DI MODIFICA -->

          <jsp:include page="<%=ICostantiUdienza.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)%>" />
          </jsp:include>
<%
}
%>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPreFissazioneUdienza">


    <table cellspacing=2 cellpadding=2 width="95%">

    <tr>
      <td class="Titolo" colspan=6>Udienza</td>
    </tr>
    <tr>
      <td class="l">Data <font class="ob">(*)</font></td>
      <td class="l">
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"dd")) %>" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>" maxlength="2" size="2" readonly >
        /
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"MM")) %>" type="text" name="<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>" maxlength="2" size="2" readonly >
        /
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"yyyy")) %>" type="text" name="<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4" readonly >
        &nbsp; Num. Coll. <font class="ob">(*)</font>
        <input value="<%=( (lUdienza.getNumCollegio() != null) ? StringUtils.toStringJSP( lUdienza.getNumCollegio()) : "") %>" name="Collegio" maxlength="2" size="2" readonly >
        &nbsp;
<% if (modalita.compareToIgnoreCase("readonly") != 0)
{
%>
        <a  href="Javascript:ListaUdienze('LoadInserisciPreFissazioneUdienza',
                                          '<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>',
                                          '<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>',
                                          '<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA %>',
                                          'luogo',
                                           '<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>',
                                            'Collegio');">
                                          Lista udienze
        <img src="/images/filefolder.gif" border=0>
        </a>
<% } %>
    <tr><td>&nbsp;</td></tr>
<% if (modalita.compareToIgnoreCase("readonly") != 0)
{
%>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
<% } %>
    </table>
    <input type="HIDDEN" name="luogo">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" value="" name="<%= ICostantiUdienza.CAMPO_ID_UDIENZA %>"  >
<%
    // Passaggio dell'eventuale ID UDIENZA_PROCEDIMENTO
    if (request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO) != null)
   {
%>
    <input type="HIDDEN" value="<%=StringUtils.toStringJSP( request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO), "0" )%>" name="<%=ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO%>"  >
<% } %>


  </form>

  <script language="JavaScript" type="text/javascript">

    var frmvalidator = new Validator("LoadInserisciPreFissazioneUdienza");

    // Controllo data udienza.
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","req", "Il campo Giorno Data Udienza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","req", "Il campo Mese Data Udienza é obbligatorio");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","req", "Il campo Anno Data Udienza é obbligatorio");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>