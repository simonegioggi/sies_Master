<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  		scope="request" class="java.util.Date"/>
<jsp:useBean id="inFormaDiPanelTDSM" 	scope="request" class="java.lang.String"/>

<%
TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
String[] esiti  = (String[])request.getAttribute("esiti");
%>

<html>
  	<head>
    <title>[S.I.E.S.] - Emissione Ordinanza Generica</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaGenerica.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");
      return ritorno;
    }
    
    //20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
    function updateCkCtrlE() {
      if ( document.InserisciOrdinanzaGenerica.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked==true ) {
        document.InserisciOrdinanzaGenerica.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked=false;
      } 
    }
    
    function updateCkCtrlT() {
      if ( document.InserisciOrdinanzaGenerica.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked==true ) {
        document.InserisciOrdinanzaGenerica.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked=false;
      } 
    }

    <%-- MEV63: cambiata gestione in caso di inserimento di più oggetti (ramo else) --%>
    function enableForma() {
        var lEsiti = document.InserisciOrdinanzaGenerica.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
        var node = document.getElementById('tableInForma');
        node.style.display = 'none';
        var len = <%=tenori.length%>;
    	if (len == 1) {
    		var selectedValue = lEsiti.options[lEsiti.selectedIndex].value;
        	if (selectedValue in { '0029':1, '1470':1,  '1471':1 } && '<%=inFormaDiPanelTDSM%>' == 'visible') {	  
  			    node.style.display = 'block';
  			    return;
            }
		} else {
			for (var x = 0; x < len; x++) {
		        for (var jEsiti = 0; jEsiti < lEsiti.length ; jEsiti++) {
		        	var item = lEsiti[x];
		        	var selectedValue = item.options[item.selectedIndex].value;
		        	if (selectedValue in { '0029':1, '1470':1,  '1471':1 } && '<%=inFormaDiPanelTDSM%>' == 'visible') {	  
		  			    node.style.display = 'block';
		  			    return;
		            }
		        }
			}
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
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Generica</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaGenerica">
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

 <table cellspacing="2" cellpadding="2" width="90%">

    <tr>
      <td class="l">Ulteriore descrizione della decisione</td>
      <td class="l"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
    </tr>

    <tr>
      <td class="l">Dispositivo </td>
      <td class="l"> <Textarea Title="Dispositivo" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO %>" cols="70" rows="4"></Textarea></td>
    </tr>
    
    
    <% 
    if (   contenuto.equals("U080")    //Rinvio esecuzione misura alternativa ex art. 684 cpp c . 2
        || contenuto.equals("U081")    // Rinvio esecuzione sanzione sostitutiva  ex art. 684 cpp c . 2
       ) 
    { %>
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
    <% } %>
    

    <tr> <td>&nbsp;</td> </tr>
    <tr>
      <td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
    </tr>
    
    <%-- MEV10-s3: refactoring del layout della pagina --%>
	<tr><td>&nbsp;</td></tr>
   	<table title="tableInForma" id="tableInForma" style="display: none;">
   		<tr>
   			<td class="l" colspan="3">Indicare se la misura deve essere eseguita nelle forme della</td>
   		</tr>
   		<tr>
   			<td class="l">
   				<input value="1" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">
   				Permanenza in casa
   			</td>
   			<td class="l">
   				<input value="2" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">
   				Collocamento in Comunità
   			</td>
   			<td class="l">
   				<input value="" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>" size="50">
   			</td>
   		</tr>
   	</table>

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
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaGenerica");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

 </body>

</html>
