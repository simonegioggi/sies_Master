<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto" scope="request" class="java.lang.String"/>
<jsp:useBean id="lGPTenModel" scope="request" class="siap.sius.generaleprocedimento.model.GPTenoreModel"/>
<jsp:useBean id="data_emissione"     scope="request" class="java.util.Date"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Ordinanza SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
          <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Chiamata all'elenco dei UEPE
      function ListaCSSA(a_formname,a_fieldname, a_fieldcode)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldcode="+a_fieldcode, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

    <script language="JavaScript">
    function Verify()
    {
/*
      // Controllo della data deposito.
      var data_tenore=document.InserisciEmissioneOrdinanza.<%=ICostantiTenore.CAMPO_GIORNO_DATA%>.value+'/'+document.InserisciEmissioneOrdinanza.<%=ICostantiTenore.CAMPO_MESE_DATA%>.value+'/'+document.InserisciEmissioneOrdinanza.<%=ICostantiTenore.CAMPO_ANNO_DATA%>.value;
      if (! ControllaData(data_tenore))
      {
        alert('Data emissione non valida!');
        return false;
      }

      // Controllo data di sistema >= Data Emissione .
      var data_camera='<%=DateUtils.getDateToString(lGPTenModel.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd/MM/yyyy")%>';
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if ( !CompareDate( data_sistema, data_tenore) )
      {
        alert('Data Emissione < della Data di sistema!');
        return false;
      }

      // Controllo della data deposito <= data camera di consiglio
      if ( !CompareDate( data_camera, data_tenore) )
      {
        alert('Data Emissione minore della Data di Camera di Consiglio!');
        return false;
      }
*/
     return true;
    }
    </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Inserimento Emissione Ordinanza</font>&nbsp;
      <%
        String lAction = new String();
        lAction = "siap.sius.depositoordinanzapc.action.ActInserisciDepositoOrdinanzaPc";
      %>
      </td>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>

  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciEmissioneOrdinanza">
    <table cellspacing="2" cellpadding="2">
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
      <tr> <td>&nbsp;</td> </tr>
      <tr>
        <td class="Titolo" colspan=6>Specificare esito per ciascun oggetto: </td>
      </tr>

      <tr>
        <td class="l" colspan=2>Oggetto </td>
        <td class="l" colspan=2>Esito </td>
      </tr>

    <%
    for (int i=0; i< lGPTenModel.getTenori().length;i++)
    {
    %>
       <tr>
        <td class="l"colspan=2>
          <input Title="Oggetto" name="<%= ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=lGPTenModel.getTenori()[i].getDescrOggettoTenore()%>" size=65 readonly>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=lGPTenModel.getTenori()[i].getCodOggettoTenore()%>" >
          <input Title="Id Tenore" type="hidden" name="<%= ICostantiTenore.CAMPO_ID_TENORE %>" value="<%=lGPTenModel.getTenori()[i].getIdTenore()%>" >

        </td>
          <td class="l"colspan=2>
           <select Title="Esito" name="<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
             <%=lGPTenModel.getEsito()[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">UEPE Competente </td>
      <td class="l">
        <input Title="UEPE Competente" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COMUNE_CSSA_COMP%>" value="" size=35 >
        <a href="Javascript:ListaCSSA('InserisciEmissioneOrdinanza','<%= ICostantiDepositoOrdinanzaPc.CAMPO_COMUNE_CSSA_COMP %>','<%= ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP %>');">
        <img src="/images/filefolder.gif" border=0></a></td>
    </tr>

    <tr>
      <td class="l">Magistrato di sorveglianza Competente </td>
      <td class="l">
        <input Title="Magistrato" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciEmissioneOrdinanza','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
    </tr>

    <tr>
      <td class="l">Luogo svolgimento della prova </td>
      <td class="l">
        <input Title="Luogo svolgimento della prova" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA %>" value="" size=35 >
      </td>
    </tr>

    <tr>
      <td class="l">Servizio terapeutico competente </td>
      <td class="l">
        <input Title="Servizio terapeutico competente " name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_SERVIZIO_TERAPEUTICO_COMP %>" value="" size=35 >
      </td>
    </tr>

    <tr>
      <td class="l">Durata Detenzione Domiciliare</td>
      <td class="L">
        <input value="" title="Numero Anni Detenzione" type="text" size="3" maxlength="3" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM %>"  > -
        <input value="" title="Numero Mesi Detenzione" type="text" size="3" maxlength="3" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM %>"  > -
        <input value="" title="Numero Giorni Detenzione" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM %>"  >
      </td>
    </tr>

    <tr>
      <td class="l">Giorni permesso premiale concessi </td>
      <td class="l">
        <input Title="Giorni permesso premiale concessi" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI %>" value="" size=4 >
      </td>
    </tr>

    <!--tr>
      <td class="l">Giorni riduzione Pena </td>
      <td class="l">
        <input Title="Giorni riduzione Pena" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_RIDUZIONE_PENA %>" value="" size=4 >
      </td>
    </tr-->

    <!--tr>
      <td class="l">Giorni riduzione Pena già usufruiti</td>
      <td class="L">
        <input Title="Giorni riduzione Pena già usufruiti" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_RIDUZIONE_USUFRUITI%>" value="" size=4 >
        concessi dal Tribunale Sorveglianza di </td><td class="L">
        <input Title="Tribunale Sorveglianza di" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>" value="" size=35 >
      </td>
    </tr-->

    <td class="l">Tipo Ordinanza da produrre</td>
    <td class="l">
       <select Title="Tipo Ordinanza da produrre" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE%>">
         <option value="01"/>Generata da sistema
         <option value="02"/>Accoglimento generica
         <option value="03"/>Rigetto generica
         <option value="04"/>N.L.P. generica
      </select>
  </td>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_SEDE_MITTENTE%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>">
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >

  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("InserisciEmissioneOrdinanza");

   // frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_GIORNO_DATA%>","req","Il campo Giorno  della Data Emissione è obbligatorio");
  //  frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_GIORNO_DATA%>","numeric");
    //frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_GIORNO_DATA%>","gt=1");
    //frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_GIORNO_DATA%>","lt=31");

  //  frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_MESE_DATA%>","req","Il campo Mese  della Data Emissione è obbligatorio");
   // frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_MESE_DATA%>","numeric");
    //frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_MESE_DATA%>","gt=1");
    //frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_MESE_DATA%>","lt=13");

  //  frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_ANNO_DATA%>","req","Il campo Anno della Data Emissione è obbligatorio");
 //   frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_ANNO_DATA%>","numeric");
    //frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_ANNO_DATA%>","gt=1900");
    //frmvalidator.addValidation("<%= ICostantiTenore.CAMPO_ANNO_DATA%>","lt=2050");

    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>","numeric");

    //frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>

</html>