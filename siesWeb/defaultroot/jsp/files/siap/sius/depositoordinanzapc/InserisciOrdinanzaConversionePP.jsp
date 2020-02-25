<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>

<%@ page import="java.util.AbstractList"%>

<jsp:useBean id="fascicoloSiusGP" 			scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"     		scope="request" class="java.util.Date"/>
<jsp:useBean id="richiesteconversioni" 	scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     					scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
	String[] esiti = (String[]) request.getAttribute("esiti");

  String lCodTipoProv = ICostantiProvvedimento.COD_ORDINANZA;
   // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
 	String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>
<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Conversione PP</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  <script language="JavaScript">
    // Controllo obbligatorietà esiti.
    function Verify()
    {
		var lEsiti=document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
		var ritorno = VerifyCombo(lEsiti,"Esito");

    	// Verifica congruenza Oggetto-Esito.
		if ((document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value in { '1770':1, '1771':1 }  &&
			 document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value == 2471 ) || 		
	     	(document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1773'  &&
	     	 document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value == 2470 ) )
		{
		    alert("Esito selezionato incompatibile con l'oggetto Procedimento");
		    return false;
		}
		return ritorno;
    }
    
    function caricamento()
    {
        node=document.getElementById("divConversione");
        node.style.display='none';
        node=document.getElementById("divRateizzazione");
        node.style.display='none';
        node=document.getElementById("divFinale");
        node.style.display='none';
	}

    function selectEsito()
    {
		var sizeTenori = <%=tenori.length%>

		// Esito 'Dispone conversione...'
		if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value in { '1770':1, '1771':1 }  ) 		
        {
	        node=document.getElementById("divConversione");
	        node.style.display='block';
	        node=document.getElementById("divRateizzazione");
	        node.style.display='none';
	        node=document.getElementById("divFinale");
	        node.style.display='block';
        }
		// Esito 'Rateizza...'
        else if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1773' )
        {
	        node=document.getElementById("divConversione");
	        node.style.display='none';
	        node=document.getElementById("divRateizzazione");
	        node.style.display='block';
	        node=document.getElementById("divFinale");
	        node.style.display='block';
        }
		// Tutti gli altri Esiti.
		else {
			//alert ('Tutti gli altri esiti');
	        node=document.getElementById("divConversione");
	        node.style.display='none';
	        node=document.getElementById("divRateizzazione");
	        node.style.display='none';
	        node=document.getElementById("divFinale");
	        node.style.display='block';
        }

<%		for (int i=0; i< richiesteconversioni.size(); i++) {  %> 			
	        if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1770' )
			{
	        	document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[0].checked=true;
	        	document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[0].disabled=false;
	        	document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[1].disabled=true;
			}
	        if (document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.selectedIndex].value == '1771' )
			{
	        	document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[1].checked=true;
	        	document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[1].disabled=false;
	        	document.InserisciOrdinanzaConversionePP.<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=i%>[0].disabled=true;
			}
   	<%	} %>
        	
		if (sizeTenori > 1) {
			document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>[0].focus();
		} else {
			document.InserisciOrdinanzaConversionePP.<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>.focus();
		}
	}
    
  </script >

 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaConversioneRateizzazionePP";
 %>

  <body class="corpo" onLoad="caricamento();">

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Conversione Pene Pecuniarie</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaConversionePP">
		<table width=35%>
			<tr>
				<td class="l" width==30%> Data Emissione</td>
     		<td class="l" width==70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
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
    	{%>
      <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2 >
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onChange="selectEsito();">
             <%=esiti[i]%>
           </select>
        </td>
      </tr>
  	<%}
    %>
	</table>
	<table cellspacing="2" cellpadding="2" style="width: 90%;">
 		<tr>
			<td class="l">Motivazione</td>
    	<td class="l"><TEXTAREA title="Motivazione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
		</tr>
 		<tr>
			<td class="l">Ulteriore descrizione della decisione</td>
    	<td class="l"><TEXTAREA title="Ulteriore Descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
		</tr>
  </table>

<%
  int indice = -1;
  Iterator itx = richiesteconversioni.iterator();  String lTitoloRichiestaCPP = "";
%> 
  <div id="divConversione" style="position: relative; top: 0; left: 0; " >
  
	<table cellspacing="2" cellpadding="2" width="90%">
<%
      while (itx.hasNext())
      {
		indice++;
    	RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel)itx.next();
	  	if (lRicConEstesa.getRichiestaConversione().getFasSieIdFascicoloSiep() != null &&
			lRicConEstesa.getRichiestaConversione().getFasSiuIdFascicoloSius() == null) 
			lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP "+lRicConEstesa.getFasSiep().getChiaveAnno()+" / "+lRicConEstesa.getFasSiep().getChiaveProgr();
		else
			lTitoloRichiestaCPP = "Pena Pecuniaria inserita dall' UDS";
%>
        <input type="hidden" name="<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" value="<%=lRicConEstesa.getRichiestaConversione().getIdRichiestaConversione()%>" >

		<tr>
       		<td class="Titolo" colspan=6 > <%=lTitoloRichiestaCPP%></td>
		</tr>

  		<tr>
			<td colspan="3" width="20%" class="l"> 
				<font class="label"> Multa</font>&nbsp;
<%					if (lRicConEstesa.getRichiestaConversione().getImportoMulta()!= null) { %>
			    		<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta()) %></font><br>
				  <%} else {%>-<%}%><br>
			    		<font class="label"> Ammenda</font>&nbsp;
<%					if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda()!= null) { %>
				    	<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda()) %></font>&nbsp;
				  <%} else {%>-<%}%>
			</td>
			<td colspan="3" class="l"> 
				<font class="label"> Q u a n t u m  &nbsp;&nbsp;  S a n z i o n e &nbsp;&nbsp;  S o s t i t  u t i v a  <br></font>
            	<font class="label">Anni</font>
            	<font class="campo"> <input title="Anni" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS%>" ></font>
            	<font class="label">Mesi</font>
            	<font class="campo"> <input title="Mesi" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS%>" > </font>
            	<font class="label">Giorni</font>
            	<font class="campo"> <input title="Giorni" type="text" size="4" maxlength="4" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS%>" > </font>
            	<font class="label">Libertà controllata </font>
         			<input value="01" type="radio" checked name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=indice%>" >&nbsp; &nbsp;
            	<font class="label"> o Lavoro sostitutivo </font>
         			<input value="02" type="radio" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%><%=indice%>" >&nbsp; &nbsp;
        	</td>
		</tr>
		<br>
	  <%}%>				
	</table>
  </div>
<%
  ///indice = -1;
  itx = richiesteconversioni.iterator();
%> 
  <div id="divRateizzazione" style="position: relative; top: 0; left: 0; " >
	<table cellspacing="2" cellpadding="2" width="90%">
<%
      while (itx.hasNext())
      {
		///indice++;
    	RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel)itx.next();
	  	if (lRicConEstesa.getRichiestaConversione().getFasSieIdFascicoloSiep() != null &&
			lRicConEstesa.getRichiestaConversione().getFasSiuIdFascicoloSius() == null) 
			lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP "+lRicConEstesa.getFasSiep().getChiaveAnno()+" / "+lRicConEstesa.getFasSiep().getChiaveProgr();
		else
			lTitoloRichiestaCPP = "Pena Pecuniaria inserita dall' UDS";
%>
        <%-- input type="hidden" name="<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" value="<%=lRicConEstesa.getRichiestaConversione().getIdRichiestaConversione()%>" --%>

		<tr>
       		<td class="Titolo" colspan='6' > <%=lTitoloRichiestaCPP%></td>
		</tr>

		<tr>
    		<td colspan="2" width="20%" class="l"> 
    			<font class="label"> Multa</font>&nbsp;
<%				if (lRicConEstesa.getRichiestaConversione().getImportoMulta()!= null) { %>
					<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta()) %></font>&nbsp;&nbsp;&nbsp;
		  		<%} else {%>-<%}%><br>
				<font class="label"> Ammenda </font>&nbsp;
<%				if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda()!= null) { %>
					<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda()) %></font>&nbsp;
				<%} else {%>-<%}%>
			</td>
			<td colspan="4" class="l"> 
				<font class="label">N.ro rate</font>
				<font class="campo"> <input title="Rate" type="text" size="2" maxlength="2" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUMERO_RATE%>" > </font>
				<font class="label">&nbsp;da &euro; &nbsp;</font>
				<font class="campo"> 
				<input Title="Multa" size=7 maxlength=16 type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)">,
				<input Title="Multa" size=2 maxlength=2 type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)"> </font>
				<font class="label">&nbsp;&nbsp;+ 1 rata da &euro; &nbsp;</font>
				<font class="campo"> 
				<input Title="Multa" size=7 maxlength=16 type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_INTERO_IMPORTOFINALE_MULTA%>" onkeypress="return TicTabNumField(this,event)">,
				<input Title="Multa" size=2 maxlength=2 type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_DECIMALE_IMPORTOFINALE_MULTA%>" onkeypress="return TicTabNumField(this,event)"> </font>
			</td>
		</tr>
	  <% } %>
	  <tr>
		<td colspan="2" width="20%" class="l"> 
			<font class="label"> Termine pagamento 1° Rata : </font>&nbsp;
		</td>
		<td colspan="3" class="l"> 
			<font class="label">entro il </font>
			<font class="campo"> 
				<input Title="Data Termine Pagamento" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_GIORNO_DATA_TERMINE_PAG%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				/
				<input Title="Data Termine Pagamento" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_MESE_DATA_TERMINE_PAG%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				/
				<input Title="Data Termine Pagamento" type="text" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_ANNO_DATA_TERMINE_PAG%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			</font>
           	<font class="label">oppure entro </font>
			<font class="campo"><input type="text" size="4" maxlength="4" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_PER_PAGAMENTO%>" ></font>           		
   	       	<font class="label">giorni dalla data di notifica</font>
  			<input value="03" type="hidden" name="<%=ICostantiSiusPenaPecuniaria.CAMPO_FLAG_TIPO_SANZIONE%>" >&nbsp; &nbsp;
     	</td>
	  </tr>
		
	  <br>				
	</table>
  </div>



		<div id="divFinale" style="position: relative; top: 0; left: 0; " >
			<table cellspacing="2" cellpadding="2" width="90%">
				<tr>
	      			<td class="label">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
	    		</tr>
			</table>

<br>
			<table>
		    <tr>
		    	<td>
	        	<input class="bottone" type="submit" value="Conferma" onclick="javascript:return Verify();" >
		      </td>
		    </tr>
			</table>
		</div>
<br>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>" >
    <input type="HIDDEN" name="numeroRichiesteCPP" value="<%=richiesteconversioni.size()%>" >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaConversionePP");
    frmvalidator.setAddnlValidationFunction("Verify");
    //frmvalidator.addValidation("<%= ICostantiSiusPenaPecuniaria.CAMPO_NUM_ANNI_SS%>","numeric");
    //frmvalidator.addValidation("<%= ICostantiSiusPenaPecuniaria.CAMPO_NUM_MESI_SS%>","numeric");
    //frmvalidator.addValidation("<%= ICostantiSiusPenaPecuniaria.CAMPO_NUM_GIORNI_SS%>","numeric");

  </script>

 </body>

</html>