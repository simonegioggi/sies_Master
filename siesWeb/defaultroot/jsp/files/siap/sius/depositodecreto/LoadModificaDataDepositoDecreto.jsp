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
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="notifiche" scope="request" class="java.util.Vector"/>
<jsp:useBean id="lDepositoDecreto" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Deposito Decreto SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>listeDestSIUS.js" ></script>

    <script language="JavaScript">
    function Verify()
    {
      var ritorno = true;
      var data_deposito=document.LoadModificaDataDepositoDecreto.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO%>.value+'/'+document.LoadModificaDataDepositoDecreto.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO%>.value+'/'+document.LoadModificaDataDepositoDecreto.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO%>.value;
      var data_emissione='<%=DateUtils.getDateToString(lDepositoDecreto.getDataEmissione(), "dd/MM/yyyy")%>'
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

  	// MERGE v10: aggiunto controllo preventivo
 	if (document.LoadModificaDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%> 	&&
 			document.LoadModificaDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%> &&
 			document.LoadModificaDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>) {
 		var data_trasmissione=document.LoadModificaDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadModificaDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadModificaDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      	if (!ControllaData(data_trasmissione)) {
	        alert('Data Trasmissione atti non valida!');
	        return false;
      	} else if (!CompareDate(data_trasmissione, data_sistema)) {
	        alert('Data Trasmissione Atti maggiore della Data di sistema!');
	        return false;
      	} else if (!CompareDate(data_deposito,data_trasmissione)) {
	        alert('Data Trasmissione atti minore della Data Deposito!');
	        return false;
      	}
 	}

      return true;
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
        <font class="campo">Deposito Decreto &nbsp;del&nbsp;<%=DateUtils.getDateToString(lDepositoDecreto.getDataEmissione(), "dd/MM/yyyy") %></font>
      </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>

  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaDataDepositoDecreto">
<table cellspacing="2" cellpadding="2" width="100%">

 <tr>
   <td class="l">Data Emissione</td>
   <td class="L">
   <%= StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataEmissione(),"dd/MM/yyyy"))%>
 </tr>

 <tr>
   <td class="l">Data Deposito in Cancelleria <font class="ob">(*)</font></td>
   <td class="L">
<%
      if(lDepositoDecreto.getDataDeposito() != null)
      {
%>
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataDeposito(),"dd")) %>" type="text" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataDeposito(),"MM")) %>" type="text" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataDeposito(),"yyyy")) %>" type="text" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%    }else{%>
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%    }%>
   </td>
 </tr>
 <tr> <td>&nbsp;</td></tr>
</table>

<jsp:include page="<%=ICostantiDepositoDecreto.PG_MODIFICA_DESTINATARI%>">
 <jsp:param name="NomeForm" value="LoadModificaDataDepositoDecreto"/>
  </jsp:include>


    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.depositodecreto.action.ActModificaDataDepositoDecreto" >

    </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaDataDepositoDecreto");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>
