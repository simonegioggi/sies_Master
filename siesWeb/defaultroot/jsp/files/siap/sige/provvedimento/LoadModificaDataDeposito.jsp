<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="notifiche" scope="request" class="java.util.Vector"/>
<jsp:useBean id="lProvvedimento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />

<html>
  <head>
    <title>[S.I.E.S.] - Deposito Provvedimento SIGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>listeDestSIUS.js" ></script>

    <script language="JavaScript">
    function Verify()
    {
      var ritorno = true;
      var data_deposito=document.LoadModificaDataDeposito.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>.value+'/'+document.LoadModificaDataDeposito.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>.value+'/'+document.LoadModificaDataDeposito.<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>.value;
      var data_emissione='<%=DateUtils.getDateToString(lProvvedimento.getEventoNotifica().getEvento().getDataEmissione(), "dd/MM/yyyy")%>'
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

     if (! ControllaData(data_deposito))
      {
        alert('Data deposito non valida!');
        return false;
      }
      // Controllo data di sistema >= Data Emissione .
      else if( !CompareDate( data_deposito, data_sistema) )
      {
        alert('Data Deposito maggiore della Data di sistema!');
        return false;
      }
      // Controllo della data deposito <= data emissione
      else if ( !CompareDate( data_emissione, data_deposito) )
      {
        alert('Data Deposito minore della Data di Emissione!');
        return false;
      }

      var data_trasmissione=document.LoadModificaDataDeposito.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadModificaDataDeposito.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadModificaDataDeposito.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (! ControllaData(data_trasmissione))
      {
        alert('Data Trasmissione atti non valida!');
        return false;
      }
      else if( !CompareDate( data_trasmissione, data_sistema) )
      {
        alert('Data Trasmissione Atti maggiore della Data di sistema!');
        return false;
      }
      else if( !CompareDate( data_deposito,data_trasmissione ) )
      {
        alert('Data Trasmissione atti minore della Data Deposito!');
        return false;
      }

      return true;
    }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
    </script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :  </font>&nbsp;
        <%-- MAC20200107012-MG-09/01/2020: correzione nome funzione --%>
        <font class="campo">Deposito Ordinanza &nbsp;del&nbsp;<%=DateUtils.getDateToString(lProvvedimento.getEventoNotifica().getEvento().getDataEmissione(), "dd/MM/yyyy") %></font>
        <%-- FINE MAC20200107012-MG-09/01/2020: correzione nome funzione --%>
      </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>

  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaDataDeposito">
<table cellspacing="2" cellpadding="2" width="100%">

 <tr>
   <td class="l">Data Emissione</td>
   <td class="L">
   <%= StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getEventoNotifica().getEvento().getDataEmissione(),"dd/MM/yyyy"))%>
 </tr>

 <tr>
   <td class="l">Data Deposito in Cancelleria <font class="ob">(*)</font></td>
   <td class="L">
<%
      if(lProvvedimento.getProvvedimento().getDataDeposito() != null)
      {
%>
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataDeposito(),"dd")) %>" type="text" name="<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataDeposito(),"MM")) %>" type="text" name="<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lProvvedimento.getProvvedimento().getDataDeposito(),"yyyy")) %>" type="text" name="<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%    }else{%>
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%    }%>

		<!-- MEV 15 - Revisione SIGE -->
		<a href="javascript:calendario('LoadModificaDataDeposito','<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_DEPOSITO%>','<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_DEPOSITO%>','<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_DEPOSITO%>');">
      	    <img src="/images/calendario.gif" border=0>
       	</a>
   </td>
 </tr>
 <tr> <td>&nbsp;</td></tr>
</table>

<jsp:include page="<%=ICostantiProvvedimentoSige.PG_MODIFICA_DESTINATARI%>">
 <jsp:param name="NomeForm" value="LoadModificaDataDeposito"/>
  </jsp:include>


    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.provvedimento.action.ActModificaDataDeposito" >

    </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaDataDeposito");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>
