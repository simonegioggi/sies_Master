<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>

<jsp:useBean id="contenuto"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="statoPermesso"     scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"    scope="request" class="java.util.Date"/>
<jsp:useBean id="fascicoloSiusGP" 	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Decreto Permesso</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciDecretoPermesso.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      if (!VerifyCombo(lEsiti,"Esito") )
        return false;

        var tipo_permesso=document.InserisciDecretoPermesso.<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>.value;
        if ( (tipo_permesso == 'PERMESSO PREMIO') && (giorni > 45 ))
        {
          alert('Il permesso premio non può superare i 45 gg' );
          return false;
        }
      	
      	return true;
    }
    </script>
  </head>

  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Emissione Decreto Permesso</font>&nbsp;
      <%
        String lAction = new String();
        lAction = "siap.sius.depositodecreto.action.ActInserisciEmissioneDecretoDeposito";
      %>
      </td>
    </tr>
    
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciDecretoPermesso">

  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"><font class="campo"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></font></td>
   </tr>

   </tr>

    <tr> <td>&nbsp;</td> </tr>
    <tr>
        <td class="Titolo" colspan=6> Specificare esito per ciascun oggetto: </td>
    </tr>

    <tr>
      <td class="l" colspan=2 >Oggetto </td>
      <td class="l" colspan=2 >Esito </td>
    </tr>
    <%
    for (int i=0; i< tenori.length;i++)
    {
    %>
       <tr>
        <td class="l"colspan=2>
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
        <td class="l"colspan=2>
         <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
           <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%}%>
  </table>

  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="label">Motivazione permesso:</td>
    </tr>
    <tr>
      <td class="l" colspan=6 ><Textarea title="Note" name="<%=ICostantiDepositoDecreto.CAMPO_NOTE%>" cols=117 rows=3></Textarea></td>
    </tr>
  </table>

  <table cellspacing="2" cellpadding="2" width=55%>
    <tr>
      <td class="label" >Con scorta</td>
      <td>
        <input type=radio name="<%=ICostantiLicenzaLibanticipata.CAMPO_FLAG_SCORTA %>" value='S' >
      </td>
      <td class="label" >Senza scorta</td>
      <td>
        <input type=radio name="<%=ICostantiLicenzaLibanticipata.CAMPO_FLAG_SCORTA %>" value='N' CHECKED >
      </td>
    </tr>
  </table>
  <table cellspacing="2" cellpadding="2" style="width: 90%;">

    <tr>
      <td class="l">Luogo fruizione / Oggetto permesso</td>
    </tr>
    <tr>
      <td class="l">
        <input Title="Luogo" name="<%= ICostantiLicenzaLibanticipata.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" value="" size=75 >
      </td>
    </tr>
</table>
  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="l">Durata permesso &nbsp;</td>
      <td class="l"> giorni &nbsp;
        <input Title="Giorni Permesso" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI %>" value="" size="2" maxlength="4" >
        &nbsp;&nbsp;&nbsp;e/o ore
        <input Title="Ore Permesso" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE %>" value="" size="2" maxlength="4" >
      </td>
    </tr>

    <tr>
      <td class="l">Stato Libertà personale</td>
      <td class="l"colspan=2>
        <select Title="Stato Permesso" name="<%=ICostantiLicenzaLibanticipata.CAMPO_COD_STATO_PERMESSO%>">
          <%=statoPermesso%>
        </select>&nbsp;&nbsp;&nbsp;&nbsp;
        <input Title="Descrizione Stato Libertà" name="<%=ICostantiLicenzaLibanticipata.CAMPO_DESCR_STATO_PERMESSO %>" value="" >
      </td>
    </tr>
  </table>

  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="l">Inserimento Prescrizioni </td>
      <td class="l">
        <input type=checkbox name="<%=ICostantiLicenzaLibanticipata.CAMPO_CK_PRESCRIZIONI%>" value=1>
      </td>
    </tr>

 </table>

 <table cellspacing="2" cellpadding="2" style="width: 90%;">
  <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("InserisciDecretoPermesso");

    frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI %>","numeric");
    frmvalidator.addValidation("<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE %>","numeric");

  </script>

 </body>

</html>