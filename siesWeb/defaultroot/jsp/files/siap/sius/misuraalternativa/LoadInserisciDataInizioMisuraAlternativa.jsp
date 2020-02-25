<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.action.ICostantiEsecuzioneMA"%>
<%@ page import="siap.sius.scadenzario.action.ICostantiScadenzarioSius"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>


<jsp:useBean id="lEsecMisuraAlternativa" scope="request" class="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel"/>
<jsp:useBean id="tipoAutorita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutoritaArresto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoUfficioCSSA"     scope="request" class="java.lang.String"/>
<jsp:useBean id="idEvento"               scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficio"                scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Concessione misure alternative alla detenzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>

    <script language="JavaScript">
    function Verify()
    {
      // Controllo della data pervenimento del verbale.
      var data_verbale=document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>.value;
      if (! ControllaData(data_verbale))
      {
        alert('Data pervenimento del verbale non valida');
        return false;
      }

      // Controllo della data sottoscrizione degli obblighi.
      var data_sottoscrizione=document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_FINE_SCADENZA%>.value+'/'+document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_FINE_SCADENZA%>.value+'/'+document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_FINE_SCADENZA%>.value;
      if (! ControllaData(data_sottoscrizione))
      {
        alert('Data sottoscrizione obblighi non valida');
        return false;
      }

      var trovata = 0;
      // Controllo CAMPI INSERITI
      if (document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>.value.length == 0
          && document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>.value.length == 0
          && (document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>.value.length == 0
           || document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>.value.length <= 1 )
         )
      {
        alert('Selezionare una autorità competente che ha inviato il verbale!');
        return false;
      }else{
        if (document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>.value.length != 0)
         {trovata++;}
        if (document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>.value.length != 0 )
         {trovata++;}
        if (document.LoadInserisciDataInizioMisuraAlternativa.<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>.value.length != 0 )
         {trovata++;}
        if (trovata > 1)
         {
          alert('Selezionare una sola autorità competente che ha inviato il verbale!');
          return false;
         }
      }

      return true;
    }
    </script>

    <script language="JavaScript">
        var desktop;
        function ListaCSSA(a_formname,a_fieldname)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        }
        function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
        }
        function ListaComuni(a_formname,a_fieldname)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        }
    </script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :  </font>&nbsp;
<%
        FascicoloGPModel lFascicolo = new FascicoloGPModel();
        Date lDataIscrizione = new Date();
%>
        <font class="campo">Concessione misure alternative alla detenzione</font>
      </td>
    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>

  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciDataInizioMisuraAlternativa">
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="l" colspan=2>Concessione Misura Alternativa</td>
    </tr>

    <tr>
      <td class="l">Data pervenimento del verbale<font class="ob">(*)</font></td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>

    <tr>
      <td class="l">Data sottoscrizione obblighi<font class="ob">(*)</font></td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_FINE_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiScadenzarioSius.CAMPO_MESE_DATA_FINE_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiScadenzarioSius.CAMPO_ANNO_DATA_FINE_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
    <tr> <td>&nbsp;</td></tr>

    <tr>
      <td class="l" colspan=2>Autorità competente che ha inviato il verbale:</td>
    </tr>

    <tr><td colspan=2>&nbsp;</td></tr>

    <tr>
      <td class="l">Ufficio di Esecuzione Penale Esterna</td>
      <td class="l">
         <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
            value="" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaCSSA('LoadInserisciDataInizioMisuraAlternativa','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');">
            <img src="/images/filefolder.gif" border=0> </a>
      <input type="hidden" Title="Codice Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="" size="5">
      </td>
    </tr>

    <tr>
        <td class="l">Istituto Detenzione</td>
        <td class="l">
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciDataInizioMisuraAlternativa','<%= ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
        <img src="/images/filefolder.gif" border=0></a></td>
    </tr>

    <tr><td colspan=2>&nbsp;</td></tr>

    <tr style="width: 100%;">
      <td class=l colspan=2>oppure indicare Tipo e Sede</td>
    </tr>

    <tr>
      <td class="l">Tipo Autorità</td>
      <td class="l">
        <select title="Destinatario" name="<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>">
          <%= tipoAutorita %>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede Autorità</td>
      <td class="l">
         <input Title="Sede " name="<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>"
            value="" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciDataInizioMisuraAlternativa','<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>');">
            <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>

      <tr>
        <td class="l">Note</td>
        <td class="L" colspan=2>
          <TEXTAREA title="Note" name="<%= ICostantiVerbale.CAMPO_NOTE %>"  cols=40 rows=5 ></textarea>
       </td>
      </tr>

    </table>

    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

<% if(ufficio.equals("UDS") ){%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.misuraalternativa.action.ActInserisciDataInizioMisuraAlternativaUDS">
    <input type="HIDDEN" name="ufficio" value="UDS" >
<%   }else{%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.misuraalternativa.action.ActInserisciDataInizioMisuraAlternativa">
<%   }%>
    <input type="HIDDEN" name="idEvento" value="<%=idEvento%>" >

    </form>


  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciDataInizioMisuraAlternativa");

    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","req","Il campo Giorno della data pervenimento del verbale è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","numeric");

    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","req","Il campo Mese della data pervenimento del verbale è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","numeric");

    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","req","Il campo Anno della data pervenimento del verbale è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","minlen=4","La lunghezza del campo Anno della data pervenimento del verbale deve essere di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_FINE_SCADENZA%>","req","Il campo Giorno della data sottoscrizione obblighi è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_FINE_SCADENZA%>","numeric");

    frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_FINE_SCADENZA%>","req","Il campo Mese della data sottoscrizione obblighi è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_FINE_SCADENZA%>","numeric");

    frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_FINE_SCADENZA%>","req","Il campo Anno della data sottoscrizione obblighi è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_FINE_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_FINE_SCADENZA%>","minlen=4","La lunghezza del campo Anno della data sottoscrizione obblighi deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>