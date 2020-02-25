<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<jsp:useBean id="notificheSog"     scope="request" class="java.lang.String"/>
<jsp:useBean id="lDepositoDecreto" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="avvocato"         scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="luogodet"         scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="TipiIstitutiSog"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutorita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"    scope="request" class="java.lang.String"/>
<jsp:useBean id="Aggiungi"         scope="request" class="java.lang.String"/>


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
      var data_deposito=document.LoadInserisciDataDepositoDecreto.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO%>.value+'/'+document.LoadInserisciDataDepositoDecreto.<%=ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO%>.value+'/'+document.LoadInserisciDataDepositoDecreto.<%=ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO%>.value;
      var data_emissione='<%=DateUtils.getDateToString(lDepositoDecreto.getDataEmissione(), "dd/MM/yyyy")%>'
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

     if (! ControllaData(data_deposito))
      {
        alert('Data deposito non valida!');
        ritorno =  false;
      }
      // Controllo data di sistema >= Data Emissione .
      else if( !CompareDate( data_deposito, data_sistema) )
      {
        alert('Data Deposito maggiore della Data di sistema!');
        ritorno =  false;
      }
      // Controllo della data deposito <= data emissione
      else if ( !CompareDate( data_emissione, data_deposito) )
      {
        alert('Data Deposito minore della Data di Emissione!');
        ritorno =  false;
      }

     // if (document.LoadInserisciDataDepositoDecreto.tipo != undefined && document.LoadInserisciDataDepositoDecreto.tipo[1].checked){
       // Controllo data di trasmissione
       if (document.LoadInserisciDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%> != undefined ){

       var data_trasmissione=document.LoadInserisciDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciDataDepositoDecreto.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (! ControllaData(data_trasmissione))
       {
         alert('Data Trasmissione atti non valida!');
         ritorno =  false;
       }
       else if( !CompareDate( data_trasmissione, data_sistema) )
       {
        alert('Data Trasmissione Atti maggiore della Data di sistema!');
        ritorno =  false;
       }
       else if( !CompareDate( data_deposito,data_trasmissione ) )
       {
         alert('Data Trasmissione atti minore della Data Deposito!');
         ritorno =  false;
       }
      }

     return ritorno;
    }
    </script>

    <script language="JavaScript">
     function CambiaTipo()
     {
       var node;
       node=document.getElementById('elenco');
       if (document.LoadInserisciDataDepositoDecreto.tipo[1].checked)
        {
          node.style.visibility='visible';
          node.style.display = ''
        }
       if (document.LoadInserisciDataDepositoDecreto.tipo[0].checked)
        {
          node.style.visibility='hidden';
          node.style.display = 'none'
        }
     }
    </script>




  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :  </font>&nbsp;
        <font class="campo">Deposito Decreto &nbsp;del&nbsp; <%=DateUtils.getDateToString(lDepositoDecreto.getDataEmissione(), "dd/MM/yyyy") %> </font>
      </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciDataDepositoDecreto">
  <table cellspacing="2" cellpadding="2" width="95%">
<%
      if(lDepositoDecreto.getDataDeposito() != null)
      {
%>     <tr>
         <td class="l"> Anno / Numero del Decreto</td>
         <td class="l"> <%=StringUtils.toStringJSP(lDepositoDecreto.getAnnoS72())%> / <%=StringUtils.toStringJSP(lDepositoDecreto.getNumS72())%></td>
      </tr>
<%    }%>

    <tr>
      <td class="l">Data Emissione</td>
      <td class="L">
      <%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataEmissione(),"dd/MM/yyyy"))%>
      </td>
    </tr>

    <tr>
      <td class="l">Data Deposito in Cancelleria
<%
      if(lDepositoDecreto.getDataDeposito() != null)
      {
%>      </td>
        <td class="L">
        <%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataDeposito(),"dd/MM/yyyy"))%>
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataDeposito(),"dd"))%>" type="hidden" size="2" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO%>"  >
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataDeposito(),"MM"))%>" type="hidden" size="2" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO%>"  >
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDepositoDecreto.getDataDeposito(),"yyyy"))%>" type="hidden" size="4" maxlength="4" name="<%= ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO%>"  >
<%    }else{%>
        <font class="ob">(*)</font></td>
        <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_MESE_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoDecreto.CAMPO_ANNO_DATA_DEPOSITO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%    }%>
      </td>
    </tr>
  </table>
<%
if (Aggiungi.equals("no") ){
%>
  <table cellspacing="0" cellpadding="0" width="95%">
   <tr>
     <td class="l"><input type="radio" name="tipo" value="Dep" CHECKED onClick="javascript:CambiaTipo();">Deposito Decreto</td>
     <td class="l"><input type="radio" name="tipo" value="DepTrasm" onClick="javascript:CambiaTipo();">Deposito Decreto e Trasmissione</td>
   </tr>
  </table>
  <div id="elenco" style="visibility:hidden; display:none; width:100%;">
<%
}else{
%>
  <div id="elenco" style="visibility:visible; width:100%;">
<%
}
%>

 <jsp:include page="<%=ICostantiUdienza.PG_LOAD_DESTINATARI%>"/>

 <jsp:include page="<%=ICostantiDepositoDecreto.PG_INSERIMENTO_DESTINATARI%>">
 <jsp:param name="NomeForm" value="LoadInserisciDataDepositoDecreto"/>
  </jsp:include>

<jsp:include page="<%=ICostantiDepositoDecreto.PG_INSERIMENTO_NOTIFICA_SOGGETTO%>">
 <jsp:param name="NomeForm" value="LoadInserisciDataDepositoDecreto"/>
  </jsp:include>

<jsp:include page="<%=ICostantiDepositoDecreto.PG_INSERIMENTO_NOTIFICA_AVVOCATI_ALTRO%>">
 <jsp:param name="NomeForm" value="LoadInserisciDataDepositoDecreto"/>
  </jsp:include>
</div>
    <br><br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>


    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.depositodecreto.action.ActInserisciDataDepositoDecreto" >
    </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciDataDepositoDecreto");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>