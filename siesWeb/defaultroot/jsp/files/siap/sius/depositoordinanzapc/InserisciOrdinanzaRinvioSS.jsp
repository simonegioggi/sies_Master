<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>


<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"     scope="request" class="java.util.Date"/>


<%
  TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
  String[] esiti  = (String[])request.getAttribute("esiti");
%>



<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Rinvio SS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaRinvioSS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");  
      if (ritorno) ritorno =  ControlliDate();
      return ritorno;
    }
    function ControlliDate()
    {
     var ritorno = true;
     var data_inizio_periodo = document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
     var data_termine_periodo = document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
     if (data_inizio_periodo.length > 2 )
     {
        ritorno =  ControllaData(data_inizio_periodo);
        if (!ritorno) 
        {
          alert( "data inizio periodo non valida");
          ritorno = false;
        }else{
            if (data_termine_periodo.length > 2 )
            {
            ritorno = ControllaData(data_termine_periodo);
              if (!ritorno)
              {
                alert( "data termine periodo non valida");
                ritorno = false;
              }else{
                 if ( CompareDate(data_termine_periodo,data_inizio_periodo ) )
                 {
                  alert( "La data inizio periodo deve precedere la data termine periodo");
                  ritorno =  false;
                }
              }
          }
        }
     }
     return ritorno;
   } 
  </script>
  
  <script language="JavaScript">
    //20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
    function updateCkCtrlE() {
      if ( document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked==true ) {
        document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked=false;
      } 
    }
    
    function updateCkCtrlT() {
      if ( document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked==true ) {
        document.InserisciOrdinanzaRinvioSS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked=false;
      } 
    }
  </script>

 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
 %>

  <body class="corpo" >

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Rinvio SS</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaRinvioSS">
    <table width=35%>
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
    </table>
 
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr> <td>&nbsp;</td> </tr>

    <tr>
        <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
        <td class="l" colspan=2 > Oggetto </td>
        <td class="l" colspan=2 > Esito </td>
    </tr>
    <%
   for (int i=0; i< tenori.length;i++)
    {
    %>
       <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2 >
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
    </table>
    <table cellspacing="2" cellpadding="2" width="90%">

    <tr>
      <td class="l">Ulteriore descrizione della decisione</td>
      <td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
    </tr>
 </table>
 
<br>

 <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="Titolo" colspan=6> In caso di Concessione indicare: <td>
    </tr>
    <tr>
      <td class="l">Data Inizio Periodo <br> (gg-mm-aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
      <td class="l">Data Termine Periodo <br> (gg-mm-aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
      
    <tr>
      <td class="Titolo" colspan=6> oppure: <td>
    </tr>
    <tr>
      <td class="l">Durata sospensione <br> (AA-MM-GG)</td>
      <td class="L">
        <input value="" title="Numero Anni sospensione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>"  > -
        <input value="" title="Numero Mesi sospensione" type="text" size="3" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS %>"  > -
        <input value="" title="Numero Giorni sospensione" type="text" size="4" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS %>"  >
      </td>
    </tr>
    
    <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td class="l" colspan="2" >
        Controllo tramite mezzi elettronici 
        <input value="E" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>"  
           onClick="javascript:updateCkCtrlE()"/> 
        &nbsp;&nbsp;&nbsp;
        Controllo tramite altri strumenti tecnici 
        <input value="T" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" 
           onClick="javascript:updateCkCtrlT()"/> 
      </td>
    </tr>
    
    
</table>
<br>
<table cellspacing="2" cellpadding="2" style="width: 90%;">
  <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td>
          <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
</table>
<br>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaRinvioSS");
    frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>","numeric");

  
  </script>

 </body>

</html>