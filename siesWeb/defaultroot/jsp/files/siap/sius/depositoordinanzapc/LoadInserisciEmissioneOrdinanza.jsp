<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="modalita"      scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"    scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratorelatore"   scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>

<%-- STUB 21/04/2004 Aggiunti i Codici Dettaglio Oggetti. --%>
<jsp:useBean id="codDettagli"   scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<%
/* Estrazione della data udienza */
 String data1;
 if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Ordinanza SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
        var desktop;

        // Chiamata funzione lista Oggetti
        function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
        {
          // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
          var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
              aLink += "&formname="+a_formname;
              aLink += "&field_contenuto="+a_field_contenuto;
              aLink += "&fieldname="+a_fieldname;
              aLink += "&fieldcodes="+a_fieldcodes;
              aLink += "&fieldcodesdet="+a_fieldcodesdet;
              aLink += "&ifieldcodes="+i_fieldcodes;
              aLink += "&ifieldcodesdet="+i_fieldcodesdet;
          desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
        }

        function  Verifica()
        {
          // Controllo obbligatorietà contenuto.
          var contenuto=document.LoadInserisciEmissioneOrdinanza.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciEmissioneOrdinanza.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;
          if(contenuto =="-")
          {
            alert("Il Campo Contenuto è obbligatorio");
            return false;
          }
          var ritorno = true;

          <% if (magistratorelatore.getEsperto() == null && magistratorelatore.getMagistrato() == null)	{ %>
              ritorno = false;
          <% } %>

          if (! ritorno)
          alert (" Magistrato Relatore non assegnato");

          return ritorno;
        }
    </script>

    <script language="JavaScript">
    function Verify()
    {
        var ritorno = true;
        //alert("Verify");
        var data_camera = '<%=data1%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

      // Controllo della data emissione.
      var data_emissione = document.LoadInserisciEmissioneOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciEmissioneOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciEmissioneOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      //alert("data emissione ->" + data_emissione);

     if (! ControllaData(data_emissione))
      {
        alert('Data emissione non valida!');
        ritorno =  false;
      }
      // Controllo data di sistema >= Data Emissione .
      else if( !CompareDate( data_emissione, data_sistema) )
      {
        alert('Data Emissione maggiore della Data di sistema!');
        ritorno =  false;
      }
      // Controllo della data deposito <= data camera di consiglio
     //alert("data_camera ->" + data_camera);
      else if ( !CompareDate( data_camera, data_emissione) )
      {
        alert('Data Emissione minore della Data di Camera di Consiglio!');
        ritorno =  false;
      }
     return ritorno;
    }
    </script>



  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
        <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :  </font>&nbsp;
<%
        String lAction = new String();

        if( modalita.equals("IF") )
        {
          lAction = "siap.sius.depositoordinanzapc.action.ActInserisciEmissioneOrdinanza";
%>
          <font class="campo">Emissione Ordinanza</font>
<%
        }
        else if( modalita.equals("IS") )
        {
          lAction = "siap.sius.depositoordinanzapc.action.ActInserisciEmissioneOrdinanza";
%>
          <font class="campo">Iscrizione Procedimento da Soggetto</font>
<%
        }
        else if( modalita.equals("M") )
        {
          lAction = "siap.sius.depositoordinanzapc.action.ActInserisciEmissioneOrdinanza";
%>
          <font class="campo">Modifica Procedimento</font>
<%
        }
%>
      </td>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>">
          <jsp:param name="MagRelRitorno" value="siap.sius.depositoordinanzapc.action.ActLoadEmissioneOrdinanza"/>
    </jsp:include>

    </tr>

  </table>


  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciEmissioneOrdinanza">
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  <tr>
    <td class="l">Contenuto <font class="ob">(*)</font></td>
    <td class="L">
      <select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>">
        <%= contenuto %>
      </select>
  </tr>

  <tr>
    <td class="l">Oggetto </td>
    <td class="l">
      <Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly><%=descOggetti%></Textarea>
      <a href="Javascript:ListaOggetti('LoadInserisciEmissioneOrdinanza',document.LoadInserisciEmissioneOrdinanza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciEmissioneOrdinanza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciEmissioneOrdinanza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciEmissioneOrdinanza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
      &nbsp;
      <a href="Javascript:ListaOggetti('LoadInserisciEmissioneOrdinanza','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciEmissioneOrdinanza.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciEmissioneOrdinanza.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
      <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
    </td>
  </tr>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verifica();">
    </td>
  </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">

  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciEmissioneOrdinanza");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno  della Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese  della Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno della Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

  </body>
</html>