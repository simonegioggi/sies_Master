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
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoFascicoloLicenzeModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="statoPermesso"   scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="decreti"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicolo" 			scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
	
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficioProc = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficioProc = "Sede Procura della Repubblica presso il Tribunale per Minorenni";
	} else {
		labelUfficioProc = "Sede Procura";
	}
%>

<%
// La stessa jsp viene usata per Decreto Revoca Permesso e per Decreto Esclusione Computo
String nomeFunzione = "Emissione Decreto Revoca Permesso";
String nomeCampoGiorni = "Periodo revocato";
String pgDecretoRif = ICostantiDepositoDecreto.PG_SINTESI_MULTI_DEC_PER_LIC_RIF;

if (tipo_decreto.compareTo(ICostantiDepositoDecreto.ESCLUSIONE_COMPUTO)== 0)
{
nomeFunzione = "Emissione Decreto Esclusione Computo Permesso";
nomeCampoGiorni = "Periodo scomputato";
}
else if (tipo_decreto.compareTo(ICostantiDepositoDecreto.ESCLUSIONE_COMPUTO_LICENZA)== 0)
{
  nomeFunzione = "Emissione Decreto Esclusione Computo Licenza";
  nomeCampoGiorni = "Periodo scomputato";
  pgDecretoRif = ICostantiDepositoDecreto.PG_SINTESI_MULTI_DEC_PER_LIC_RIF;

}
else if (tipo_decreto.compareTo(ICostantiDepositoDecreto.REVOCA_LICENZA)== 0)
{
  nomeFunzione = "Emissione Decreto Revoca Licenza";
  nomeCampoGiorni = "Periodo revocato";
}

// Serve per inizializzare la Procura di esecuzione
//Modifica del 13/09/2013 mev "Revisione Misure di Sicurezza SIUS"
//Eliminazione default sede Procura Competente
String comuneProcura = "";
//if (fascicolo.getIdFascicoloSiep() != null)
//{
//comuneProcura = fascicolo.getDescrComuneUfficio();
//}
// Confronto data emissione Eliminato ! Luigi 29-11-2005
boolean controlloData = true;
boolean presenzaDecreto = false;

%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Decreto Revoca Permesso</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">

    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      //alert ("div ->" + DivAttivo);
      //alert("indiceComboPermessi  -->  " + indiceComboPermessi);
      //alert("giorniRevoca  -->  " + ggRevoca[indiceComboPermessi]);
      //alert("oreRevoca  -->  " + hhRevoca[indiceComboPermessi]);
      var lEsiti=document.InserisciDecretoRevocaPermesso.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      if (!VerifyCombo(lEsiti,"Esito") )
        return false;
<%if(decreti.size()>0)
	{
 		if (!controlloData) { %>
       alert('La data di emissione del decreto di revoca non può precedere quella del decreto da revocare!' );
          return false;
	<%}%>
  	// Controllo valorizzazione giorni e ore di Scomputo
    var giorniScomputo = 0;
    giorniScomputo = document.InserisciDecretoRevocaPermesso.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI_SCOMPUTO%>.value;
    var oreScomputo = 0;
    oreScomputo = document.InserisciDecretoRevocaPermesso.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE_SCOMPUTO%>.value;

    if ( (giorniScomputo == '' || giorniScomputo == '0' || giorniScomputo == '00') &&
            (oreScomputo == '' ||    oreScomputo == '0' ||    oreScomputo == '00')   )
    {
    	alert('Valorizzare il periodo da revocare!' );
      return false;
    }

  	// Controllo dei giorni di Scomputo
    if ( giorniScomputo > ggRevoca[indiceComboPermessi])
    {
    	alert('I giorni di revoca o scomputo non possono superare quelli concessi!' );
      return false;
    }
    // 15/06/2007 Controllo delle ore di Scomputo
    if ( oreScomputo > hhRevoca[indiceComboPermessi])
    {
    	alert('Le ore di revoca o scomputo non possono superare quelle concesse!' );
      return false;
    }
<%} %>
	 return true;
  }

    </script>
    <script language="JavaScript">
      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
   </script>



  </head>

  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo"> <%=nomeFunzione%> </font>&nbsp;
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

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciDecretoRevocaPermesso">
  <table cellspacing="2" cellpadding="2" style="width: 90%;">
   <tr>
     <td class="l" width==30%> Data Emissione</td>
     <td class="l" width==70%><font class="campo"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></font></td>
   </tr>

    <tr> <td>&nbsp;</td> </tr>

<% if (decreti.size() > 0)
   {
%>
  <jsp:include page="<%=pgDecretoRif%>"/>
<% } else {%>
<tr>
 <td class="l" > <font color="red"> Decreto Permesso/Licenza di Riferimento non disponibile !</font> </td>
</tr>
<% }%>

    <tr> <td>&nbsp;</td> </tr>
  </table>

  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td class="label">Rilevato:</td>
      <td class="l" colspan=6 ><Textarea title="Note" name="<%=ICostantiDepositoDecreto.CAMPO_NOTE%>" cols=88 rows=3></Textarea></td>
    </tr>
    <tr> <td>&nbsp;</td> </tr>
  </table>
  <table cellspacing="2" cellpadding="2" style="width: 90%;">
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
      <td class="l"><%=nomeCampoGiorni%> <font class=ob>(*)</font> </td>
      <td class="l"> giorni &nbsp;
        <input Title="Giorni revoca" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI_SCOMPUTO %>" value="" size="2" maxlength="4" >
        &nbsp;&nbsp;&nbsp;e/o ore
        <input Title="Ore revoca" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE_SCOMPUTO %>" value="" size="2" maxlength="4" >
      </td>
    </tr>

      <tr>
        <td class="l"><%=labelUfficioProc%> </td>
        <td class="l">
           <input Title="<%=labelUfficioProc %>" name="<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_PROCURA_COMP%>"
              value="<%=comuneProcura%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('InserisciDecretoRevocaPermesso','<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_PROCURA_COMP%>');">
              <img src="/images/filefolder.gif" border=0> </a>
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
    var frmvalidator  = new Validator("InserisciDecretoRevocaPermesso");
    frmvalidator.addValidation("<%= ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI_SCOMPUTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE_SCOMPUTO%>","numeric");
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

 </body>

</html>