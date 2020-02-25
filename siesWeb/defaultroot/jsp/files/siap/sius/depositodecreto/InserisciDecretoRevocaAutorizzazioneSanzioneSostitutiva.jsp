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
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione" 		scope="request" class="java.util.Date"/>
<jsp:useBean id="Action"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo_origine"	scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
	
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficio = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
	} else {
		labelUfficio = "Tribunale di Sorveglianza";
	}
%>
<%
// Flag che indica la presenza del fascicolo origine;
  boolean isFascicoloOrigine = (fascicolo_origine != null && fascicolo_origine.getFascicoloSiusModel() != null && fascicolo_origine.getFascicoloSiusModel().getIdFascicoloSius() != null) ? true : false;
  String  idFascicoloOrigine = (isFascicoloOrigine ? fascicolo_origine.getFascicoloSiusModel().getIdFascicoloSius().toString() : "");
  String lCodTipoProv;
   
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
<%
	// Imposta varibabili 
	String lAction = new String();
	String lFunctionName = new String();

  lAction = "siap.sius.depositodecreto.action.ActInserisciEmissioneDecretoDeposito";
  lFunctionName = "Emissione Decreto Revoca Autorizzazione Sanzione Sostitutiva";
%>

<html>
  <head>
    <title>[S.I.E.S.] - <%=lFunctionName%></title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
      var desktop;
      // Chiamata lista Procure
      function ListaProcure(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Tribunali di Sorveglianza", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

    <script language="JavaScript">

    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciRevoAutorizSS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");
      return ritorno;
    }
    </script>
  </head>

  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo"><%=lFunctionName%></font>&nbsp;
     
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciRevoAutorizSS">

    <table cellspacing="2" cellpadding="2" style="width: 90%;">
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
      <tr>
        <td class="l">Eventuale Motivazione</td>
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
    <%
    }  
    %>
     </table>
  
    <table cellspacing="2" cellpadding="2">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l"><%=labelUfficio%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficio%>" name="<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP%>" value="" size=35 >
        <a href="Javascript:ListaProcure('InserisciRevoAutorizSS','<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
 </table>

	<br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
        <td class="Titolo" colspan=6 > Autorizzazione da revocare </td>
    </tr>
<% if ( isFascicoloOrigine) { %>
	<tr>
		<td class="L">
        	<font class="label">Procedimento Collegato N.</font>
        		
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicolo_origine.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>">
          		<%= fascicolo_origine.getFascicoloSiusModel().getChiaveAnno()%>
          		/
          		<%= fascicolo_origine.getFascicoloSiusModel().getChiaveProgr()%>&nbsp;
             	</a>
         		&nbsp;<%=fascicolo_origine.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo_origine.getFascicoloSiusModel().getDescrComuneUfficio()%>&nbsp;&nbsp;
 				
 		</td>
    </tr>
    <tr>
    	<td class="L">
        	<font class="label">Provvedimento emesso in data </font>      		
        	
      		<%=DateUtils.getDateToString(fascicolo_origine.getFascicoloSiusModel().getDataIscrizione(),"dd-MM-yyyy")%>
      	
    	</td>
    </tr>
 </table>
	<br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
  	<tr><td>&nbsp;</td></tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
 <% } 
    else{%>
    <tr>
    	<td> <font class="crosso">Non esiste il procedimento collegato per risalire al provvedimento da revocare !</font></td>
 	</tr>
 </table>
<% 	} %>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciRevoAutorizSS");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>




  </script>

 </body>

</html>