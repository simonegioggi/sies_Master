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
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="statolibertatis" scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoLibertatis" scope="request" class="java.util.Vector"/>
<jsp:useBean id="data_emissione" 	scope="request" class="java.util.Date"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
	
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	} else {
		labelUfficio = "Ufficio di Sorveglianza";
	}
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Decreto Modifica Attività Lavorativa / Luogo Detenzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciDecretoModificaAttLuogoDet.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");
      return ritorno;
    }
    </script>

    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname,a_typename)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename+"&NomeLista="+"Magistrati presso Uffici di Sorveglianza di:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>
  </head>

  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Emissione Decreto Modifica Attività Lavorativa / Luogo Detenzione</font>&nbsp;
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

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciDecretoModificaAttLuogoDet">

    <table cellspacing="2" cellpadding="2" style="width: 90%;">
      <tr>
        <td class="l" width="30%"> Data Emissione</td>
        <td class="l" width="70%"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
      </tr>
      <tr>
        <td class="l">Rilevato</td>
        <td class="l"><Textarea title="Note" name="<%=ICostantiDepositoDecreto.CAMPO_NOTE%>" cols=88 rows=3></Textarea></td>
      </tr>
    </table>

    <tr> <td>&nbsp;</td> </tr>
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
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=70%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2>
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
     </table>
    <table cellspacing="2" cellpadding="2">
    <tr><td>&nbsp;</td></tr>

 </table>



 <table cellspacing="2" cellpadding="2" style="width: 90%;">
 <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td class="l">Attività lavorativa / Luogo esecuzione </td>
      <td class="l">
        <input Title="Luogo" name="<%= ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA %>" value="" size=50 >
      </td>
    </tr>
    <tr>
      <td class="l"><%=labelUfficio%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficio%>" name="<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciDecretoModificaAttLuogoDet','<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>

    <tr>
      <td class="l">Stato Libertà Personale raggiungimento detenzione domiciliare </td>
          <td class="l"colspan=2>
            <select Title="StatusPersona" name="<%=ICostantiDepositoDecreto.CAMPO_STATUS_PERSONA%>">
            <%=statolibertatis%>
            </select>
        </td>
   </tr>

    <tr>
      <td class="l">Numero massimo ore per raggiungimento luogo detenzione</td>
      <td class="l">
        <input Title="Luogo" name="<%= ICostantiDepositoDecreto.CAMPO_TOT_ORE_RAGGIUNGIMENTO%>" value="" maxlength="3" size=3 >
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
    var frmvalidator = new Validator("InserisciDecretoModificaAttLuogoDet");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

 </body>

</html>