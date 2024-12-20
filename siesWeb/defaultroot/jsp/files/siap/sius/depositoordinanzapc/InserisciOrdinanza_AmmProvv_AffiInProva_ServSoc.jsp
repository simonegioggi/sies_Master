<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>

<jsp:useBean id="fascicoloSiusGP"     	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"         	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"      	scope="request" class="java.util.Date"/>
<jsp:useBean id="statolibertatis"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="Action"        		scope="request" class="java.lang.String"/>
<jsp:useBean id="listaTenori"       	scope="request" class="java.util.Vector"/>
<jsp:useBean id="CodMagRel"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="inFormaDiPanelUDSM" 	scope="request" class="java.lang.String"/>  

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti = (String[]) request.getAttribute("esiti");
TenoreModel TMod = new TenoreModel();
TMod = (TenoreModel)listaTenori.get(0);
%>

<%
/* Estrazione della data udienza */
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
 	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd/MM/yyyy");
else
 	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(), "dd/MM/yyyy");
%>

<html>
	<head>
    <title>[S.I.E.S.] - Emissione Ordinanza di Misura Alternativa :Ammissione Provvisoria all'Affidamento in Prova </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  	<script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
        var lEsiti=document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
        if (!VerifyCombo(lEsiti,"Esito") )
          return false;

        // Se l'oggetto è tra 0001, 0002, 0003, 0005, 0010, 0012, 0013, 0195, 0361, 0362, 0610  e l'esito è di Concessione (0001), il
        // Luogo di Svolgimento della prova è obbligatorio
        // 01/2014 Decreto Legge 75gg - Aggiungo cod. 2008 tra Oggetti
        var lTenori=document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>;
        if (typeof (lTenori[0]) =="undefined" )
        {

        if ((lTenori.value in {'0001':1, '0002':1, '0003':1, '0005':1, '0010':1, '0012':1, '0013':1, '0195':1, '0362':1, '0610':1, '2008':1}) && lEsiti.value == '1010')
            { 
                  if (document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") 
                  {
                    alert ('ERRORE 1 - Luogo di svolgimento della prova obbligatorio!');
                    document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.focus();
                    return false;
                  }
             }
        }
      
        for (jTenori = 0; jTenori < lTenori.length; jTenori++) 
        {
        if (lTenori[jTenori].value in {'0001':1, '0002':1, '0003':1, '0005':1, '0010':1, '0012':1, 
                         '0013':1, '0195':1, '0362':1, '0610':1, '2008':1}) 
        {
                for (jEsiti = 0; jEsiti < lEsiti[jTenori].length ; jEsiti++ )
                  {
                    
                      if ( (lEsiti[jTenori][jEsiti].selected) && (lEsiti[jTenori][jEsiti].value == '1010' ) )
                      {
                          if (document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") 
                          {
                            alert ('ERRORE 2 - Luogo di svolgimento della prova obbligatorio!');
                            document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.focus();
                              return false;
                          }
                      }
                  }
                    
              }  
         }
        

        if(document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV %>.value == "-")
        {
          document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV %>.value = "00";
        }   
    } // Chiude verify()

    <%-- MEV63: cambiata gestione in caso di inserimento di più oggetti (ramo else) --%>
	function enableForma() {
		var lEsiti = document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
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

	var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Uffici di Sorveglianza", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Procura competente
      function ListaDistrettiProcure(a_formname,a_fieldname)
      {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Procure", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Distretti Procura o TDS
      function ListaProcure(a_formname,a_fieldname)
      {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Tribunali di Sorveglianza", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

	//20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
      function updateCkCtrlE() {
        if ( document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked==true ) {
          document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked=false;
        } 
      }
      
      function updateCkCtrlT() {
        if ( document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked==true ) {
          document.InserisciOrdinanzaAmmProvAffiPro.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked=false;
        } 
      }
	</script>
 	</head>

  	<body class="corpo" >
    <table>
		<tr>
	        <td class="LBG">
	        	<a href="Javascript:window.print();">
	        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
	        	</a>
	        </td>
	        <td class=LBG>
	        	<font class="label">Funzione:</font>&nbsp;
	        	<font class="campo">Emissione Ordinanza di Applicazione Provvisoria di Misura Alternativa</font>
      		</td>
      	</tr>
      	<tr>
        	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
      	</tr>
    </table>

  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaAmmProvAffiPro">
    <table width=35%>
      	<tr>
	        <td class="l" width="30%"> Data Emissione</td>
	        <td class="l" width="70%"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
      	</tr>
    </table>
    
	<table cellspacing="2" cellpadding="2" width="90%">
    	<tr>
			<td class="l"> Rilevato </td>
			<td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
    	</tr>
	</table>
	<br>
	<table cellspacing="2" cellpadding="2" width="90%">
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

    <input type="HIDDEN" name="<%=ICostantiTenore.CAMPO_ID_TENORE %>" value="<%=StringUtils.toStringJSP(tenori[i].getIdTenore(), "0") %>">

       <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=80%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2 >
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onChange="enableForma();">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
    </table>
<br>
 <table cellspacing="2" cellpadding="2"width="90%">

    <tr>
      <td class="l">Tribunale di Sorveglianza Competente </td>
      <td class="l">
        <input Title="TDS Competente" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_TDS_COMP%>" value="" size=35 >
        <a href="Javascript:ListaProcure('InserisciOrdinanzaAmmProvAffiPro','<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_TDS_COMP%>');">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Magistrato di sorveglianza Competente </td>
      <td class="l">
        <input Title="Magistrato" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaAmmProvAffiPro','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo svolgimento della prova </td>
      <td class="l">
        <input Title="Luogo svolgimento della prova" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA %>" value="" size=35 >
      </td>
    </tr>

    <tr>
      <td class="l">Procura competente </td>
      <td class="l">
        <input Title="procura competente " name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_AUTORITA_VIGILANTE %>" value="" size=35 >
          <a href="Javascript:ListaDistrettiProcure('InserisciOrdinanzaAmmProvAffiPro','<%=ICostantiDepositoOrdinanzaPc.CAMPO_AUTORITA_VIGILANTE%>');">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Stato libertà personale  raggiungimento detenzione domiciliare </td>
         <td class="l"colspan=2>
            <select Title="StatusPersona" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV%>">
            <%=statolibertatis%>
            </select>      </td>
    </tr>
    
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
     
    <tr>
      <td class="l" colspan="2">Inserimento Prescrizioni 
        <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"> 
      </td>
    </tr>
    
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
        		<input class="bottone" type="submit" value="Conferma" onClick="javascript:return Verify();" >
      		</td>
    	</tr>
 	</table>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=Action%>" --%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS">

    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE %>" value="2008" >
    <input type="HIDDEN" name="<%=ICostantiMagistratoRelatore.CAMPO_MAG_COD_MAGISTRATO %>" value="<%=StringUtils.toStringJSP(CodMagRel)%>" >

  </form>
 </body>
</html>