<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="contenuto"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"   scope="request" class="java.util.Date"/>
<jsp:useBean id="tipoUfficioSIUS"  scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza di Sospensione Esecutiva Ordinanza TdS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
    var desktop;
    // 28/03/2007 Lista Uffici.
	  var desktop;
  	function ListaUffici(a_formname,a_fieldname,codTipoUfficio)
  	{
    	 desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}

    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaSospEsecOr.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      if (!VerifyCombo(lEsiti,"Esito") )
        return false;

      var ritorno = true;
      ritorno = ControlliDate();
      return ritorno;
   }

    function ControlliDate()
    {
     var ritorno = true;
     var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';
     var data_emissione_ordinanza = document.InserisciOrdinanzaSospEsecOr.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaSospEsecOr.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaSospEsecOr.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;

     // Controlli su Data Emissione Ordinanza
     if (data_emissione_ordinanza.length > 2 )
     {
       if (! ControllaData(data_emissione_ordinanza))
       {
        alert('Data Emissione Ordinanza non valida');
        ritorno =  false;
       }
       else if ( !CompareDate( data_emissione_ordinanza, data_emissione ) )
       {
        alert("Data Emissione Ordinanza deve precedere " + data_emissione + " !");
        ritorno =  false;
       }
     }
     return ritorno;
    }

    function Init()
    {
      document.InserisciOrdinanzaSospEsecOr.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
    }
 </script>

 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
 %>


  <body class="corpo" onLoad="Javascript:Init();">

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Sospensione Esecutiva di Ordinanza </font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaSospEsecOr">
    <table width=35%>
   <tr>
     <td class="l" width==30%> Data Emissione</td>
     <td class="l" width==70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
    </table>

    <tr> <td>&nbsp;</td> </tr>
     <table cellspacing="2" cellpadding="2" style="width: 90%;">
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
    <%}%>

  </table>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">

  <tr>
	  <td class="l">Ulteriore descrizione della decisione</td>
      <td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
    </tr>
    <tr> </tr>
  </table>
<br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">

  <tr>
    <td class="Titolo" colspan='4'> Estremi ordinanza sospesa: <td>
  </tr>
    <tr>
      <td class="l">Data Emissione <font class="ob">(*)</font> <br> (gg-mm-aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">
        <select title="Ufficio" name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>">
          <%=tipoUfficioSIUS%>
        </select>
				<font class="ob">(*)</font>
      </td>

      <td class="l">
         <input Title="Sede" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>" value="" type="text" maxlength="35" size="35">
         <a href="Javascript:ListaUffici('InserisciOrdinanzaSospEsecOr','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>', document.InserisciOrdinanzaSospEsecOr.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>[document.InserisciOrdinanzaSospEsecOr.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.selectedIndex].value);">
           <img src="/images/filefolder.gif" border=0>
         </a>
      </td>

    </tr>
  <tr> </tr>
  <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaSospEsecOr");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>","req","La data dell'ordinanza da sospendere è un dato obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>","req","La data dell'ordinanza da sospendere è un dato obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>","req","La data dell'ordinanza da sospendere è un dato obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>","req","La sede è un dato obbligatorio");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>


 </body>

</html>