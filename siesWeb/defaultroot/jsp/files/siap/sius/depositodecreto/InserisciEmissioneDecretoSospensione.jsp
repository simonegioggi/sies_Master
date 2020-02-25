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
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>


<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="statolibertatis" scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>
<jsp:useBean id="inFormaDiPanelUDSM" 	scope="request" class="java.lang.String"/>  


<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
	
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficioTrib = "";
	String labelUfficioSorv = "";
	String labelUfficioProc = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficioTrib = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
		labelUfficioSorv = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
		labelUfficioProc = "Procura della Repubblica presso il Tribunale per Minorenni";
	} else {
		labelUfficioTrib = "Tribunale di Sorveglianza";
		labelUfficioSorv = "Ufficio di Sorveglianza";
		labelUfficioProc = "Procura";
	}
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Decreto Applicazione Provvisoria di Misura Alternativa</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname,a_typename)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename+"&NomeLista="+"Magistrati presso Uffici di Sorveglianza di:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Chiamata lista Procure
      function ListaProcure(a_formname,a_fieldname)
      {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Tribunali di Sorveglianza", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Lista Procure per Distretti ovvero Lista TDS
      function ListaDistrettiProcure(a_formname,a_fieldname)
      {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Lista Procure Sedi di revoca (TDS o UDS)
      function ListaSediRevoca(a_formname,a_fieldname)
      {
        var valore = document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoDecreto.CAMPO_TIPO_SEDE_REVOCA%>.value;
        var i = document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoDecreto.CAMPO_TIPO_SEDE_REVOCA%>.selectedIndex;
        if ( i == 0)
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        else
          ListaProcure(a_formname,a_fieldname);
      }
      
      <%-- MEV63: cambiata gestione in caso di inserimento di più oggetti (ramo else) --%>
  	function enableForma() {
  		var lEsiti = document.InserisciEmissioneDecretoSosp.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      	var nodeInForma = document.getElementById("tableInForma");
      	nodeInForma.style.display = 'none';
      	var len = <%=tenori.length%>;
      	if (len == 1) {
  	   		if ((lEsiti.options[lEsiti.selectedIndex].value == '1010'
  	   				|| lEsiti.options[lEsiti.selectedIndex].value == '1012')
  	   				&& '<%=inFormaDiPanelUDSM%>' == 'visible') {
  	   			nodeInForma.style.display = 'block';
  	   			return;
          	}
      	} else {
      		for (var x = 0; x < len; x++) {
  		    	for (var jEsiti = 0; jEsiti < lEsiti[x].length; jEsiti++) {
  		    		var item = lEsiti[x];
  		    		if ((item.options[item.selectedIndex].value == '1010'
  		    				|| item.options[item.selectedIndex].value == '1012')
  		    				&& '<%=inFormaDiPanelUDSM%>' == 'visible') {
  		    			nodeInForma.style.display = 'block';
  		    			return;
  		    		}
  		        }
      		}
      	}
      }
      
    </script>

    <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciEmissioneDecretoSosp.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");
      return ritorno;
    }
    </script>
    
    <script language="JavaScript">
    	//20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
      function updateCkCtrlE() {
      	if ( document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoDecreto.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked==true ) {
       		document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoDecreto.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked=false;
       	} 
      }
      
      function updateCkCtrlT() {
     		if ( document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoDecreto.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked==true ) {
       		document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoDecreto.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked=false;
       	} 
     	}
    </script>
  </head>

  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Emissione Decreto Applicazione Provvisoria di Misura Alternativa</font>&nbsp;
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

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciEmissioneDecretoSosp">

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
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2>
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onChange="enableForma();">
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
      <td class="l"><%=labelUfficioTrib%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficioTrib%>" name="<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP%>" value="" size=35 >
        <a href="Javascript:ListaProcure('InserisciEmissioneDecretoSosp','<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP%>');">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l"><%=labelUfficioSorv%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficioSorv%>" name="<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciEmissioneDecretoSosp','<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo svolgimento della prova </td>
      <td class="l">
        <input Title="Luogo" name="<%= ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA %>" value="" size=35 >
      </td>
    </tr>

    <tr>
       <td class="l"><%=labelUfficioProc%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficioProc%>" name="<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_PROCURA_COMP%>" value="" size=35 >
            <a href="Javascript:ListaDistrettiProcure('InserisciEmissioneDecretoSosp','<%=ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_PROCURA_COMP%>');">
            <img src="/images/filefolder.gif" border=0> </a>
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
			<td class="l" colspan="2" >
				Controllo tramite mezzi elettronici 
				<input value="E" type="checkbox" name="<%=ICostantiDepositoDecreto.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>"  
							 onClick="javascript:updateCkCtrlE()"/> 
				&nbsp;&nbsp;&nbsp;
				Controllo tramite altri strumenti tecnici 
				<input value="T" type="checkbox" name="<%=ICostantiDepositoDecreto.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" 
							 onClick="javascript:updateCkCtrlT()"/> 
			</td>
		</tr>
   
 </table>

 <table cellspacing="2" cellpadding="2" style="width: 90%;">
  <tr> <td>&nbsp;</td> </tr>
      <tr>
        <td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoDecreto.CAMPO_CK_PRESCRIZIONI%>"></td>
      </tr>
      
   <!--  mev63 -->
  		<tr>
       	<td>
       		<table title="tableInForma" id="tableInForma" style="display: none;">
       			<tr>
			        <td class="l" width="50%">Indicare se la misura deve essere eseguita nelle forme della </td>
			        <td class="l" width="40%"><input value="1" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>"> Permanenza in casa </td>
			        <td width="10%">&nbsp;</td>
		        </tr>
		        <tr>
			        <td width="50%">&nbsp;</td>
			        <td width="40%" class="l"><input value="2" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">Collocamento in Comunità</td>
			        <td width="10%" class="l"><input value="" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>"></td>
		        </tr>
       		</table> 
       	</td>
   	</tr>
 
  		
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
    var frmvalidator = new Validator("InserisciEmissioneDecretoSosp");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

 </body>

</html>