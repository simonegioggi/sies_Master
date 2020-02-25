<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sius.remissionedebito.action.ICostantiSiusRemissioneDebito"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaRem" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaRemIst" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimento" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaProvvedimento" scope="request" class="java.lang.String" />
<jsp:useBean id="richiestaremissione" scope="request" class="siap.sius.remissionedebito.model.RichiestaRemissioneModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<%
Date dataIrrevocabilita = null;
if (! Utils.isNullObj(request.getAttribute("dataIrrevocabilita")))
	dataIrrevocabilita = (Date)request.getAttribute("dataIrrevocabilita");
%>

<html>
<head>
  <title> Modifica Richiesta Remissione Debito</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

  <script language="JavaScript" >
	  // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
		var desktop;
		function ListaComuni(a_formname,a_fieldname){
		  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
		 }
		  	 
	  // Inserire i controlli che non possono essere effettuati dal genvalidator 
	  function Verify() { 
			// ANNO/NUMERO PARTITA o ci sono entrambi o nessuno 
			if((document.LoadModificaRichiestaRemissioneDebito.<%= ICostantiSiusRemissioneDebito.CAMPO_ANNO_PARTITA %>.value==""
				&& !document.LoadModificaRichiestaRemissioneDebito.<%= ICostantiSiusRemissioneDebito.CAMPO_NUM_PARTITA %>.value=="")
				|| (!document.LoadModificaRichiestaRemissioneDebito.<%= ICostantiSiusRemissioneDebito.CAMPO_ANNO_PARTITA %>.value==""
				&& document.LoadModificaRichiestaRemissioneDebito.<%= ICostantiSiusRemissioneDebito.CAMPO_NUM_PARTITA %>.value=="")){
		        alert("anno e numero partita non corretti");
		        document.LoadModificaRichiestaRemissioneDebito.<%= ICostantiSiusRemissioneDebito.CAMPO_ANNO_PARTITA %>.focus(); 
		         return false;
			}
		  
 		// TIPO/SEDE AUTORITA' o ci sono entrambi o nessuno 
		if((document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=="-"
		&& !(document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_COD_LUOGO_EMITTENTE%>.value=="" ||
				 document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_COD_LUOGO_EMITTENTE%>.value=="-" ))
		||(!document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=="-"
		&& (document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_COD_LUOGO_EMITTENTE%>.value=="" 
		||  document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_COD_LUOGO_EMITTENTE%>.value=="-"))){
      	alert("Tipo e Sede autorità vanno valorizzati entrambi")
       	document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus(); 
       	return false;
		}
		  // controllo correttezza campo 'Data Emissione' 
		  var data_to_verify = document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+ 
		                       document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+ 
		                       document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_ANNO_DATA_EMISSIONE%>.value; 
		  if (!ControllaData(data_to_verify) && data_to_verify.length>2)
		  { 
		    alert('Data Emissione non corretta'); 
		    document.LoadModificaRichiestaRemissioneDebito.<%=ICostantiSiusRemissioneDebito.CAMPO_GIORNO_DATA_EMISSIONE%>.focus(); 
		    return false; 
		  } 
		
		return true; 
	} 
  </script>
</head>

<body class="corpo">
  <table>
     <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
<%			
				String lAzione = "siap.sius.remissionedebito.action.ActModificaRichiestaRemissioneDebito";  
%>
        <font class="campo">Modifica Richiesta Remissione Debito</font>
      </td>
			<!-- BOTTONE DI RITORNO -->
				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN %>" name="LoadModificaRichiestaRemissioneDebito">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="HIDDEN" name="<%=ICostantiSiusRemissioneDebito.CAMPO_ID_RICHIESTA_REMISSIONE%>" value="<%=richiestaremissione.getIdRichiestaRemissione()%>">

  <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>

	<table>
	<tr><td>&nbsp;</td></tr>
    <tr><td class="Titolo" colspan=6>Dati Richiesta Remissione</td></tr>
    <tr>
      <td class="l" colspan="2">Anno/Numero Partita</td>
      <td class="l" colspan="4"> 
        <input type="text" maxlength="4" size="4" ONKEYPRESS="return TicTabNumField(this,event)" value="<%=StringUtils.toStringJSP(richiestaremissione.getAnnoPartita()) %>" name="<%= ICostantiSiusRemissioneDebito.CAMPO_ANNO_PARTITA %>"> /  
        <input type="text" maxlength="9" size="11" ONKEYPRESS="return TicTabNumField(this,event)" value="<%=StringUtils.toStringJSP(richiestaremissione.getNumPartita()) %>" name="<%= ICostantiSiusRemissioneDebito.CAMPO_NUM_PARTITA %>"> 
      </td> 
    </tr>
    <tr>
      <td class="l" colspan="2">Numero Ex Campione</td>
      <td class="l" colspan="4"> 
        <input type="text" maxlength="20" size="20" value="<%=StringUtils.toStringJSP(richiestaremissione.getNumExCampione()) %>" name="<%= ICostantiSiusRemissioneDebito.CAMPO_NUM_EX_CAMPIONE %>" > 
      </td> 
    </tr>
    <tr>
      <td class="l" colspan="2">Prot. Circosrizione Doganale</td>
      <td class="l" colspan="4"> 
        <input type="text" maxlength="20" size="20" value="<%=StringUtils.toStringJSP(richiestaremissione.getProtCircosrizioneDoganale()) %>" name="<%= ICostantiSiusRemissioneDebito.CAMPO_PROT_CIRCOSRIZIONE_DOGANALE %>" > 
      </td> 
  	</tr>
    <tr>
		<td class="l" colspan="2">Autorità</td>
		<td class="L" colspan=4>
			<select Title="Autorità" name="<%= ICostantiSiusRemissioneDebito.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
				<%=autoritaRem%>
				<%=autoritaRemIst%>
			</select></td>
		</tr>
    <tr>
			<td class="l" colspan="2" >Sede</font></td>
			<td class="L"colspan="4" >
				<input Title="Sede" name="<%=ICostantiSiusRemissioneDebito.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=richiestaremissione.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35"> 
					<a href="Javascript:ListaComuni('LoadModificaRichiestaRemissioneDebito','<%= ICostantiSiusRemissioneDebito.CAMPO_COD_LUOGO_EMITTENTE %>');"> <img src="/images/filefolder.gif" border=0> </a>
			</td>
	</tr>

	<tr>
		<td class="Titolo" colspan=6>Estremi provvedimento di riferimento</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Tipo Provvedimento</td>
		<td class="L" colspan="4"><select Title="Tipo Provvedimento" name="<%= ICostantiSiusRemissioneDebito.COD_TIPO_PROVVEDIMENTO %>">
			<%=tipoProvvedimento%>
		</select></td>
	</tr>
	<tr>
		<td class="l" colspan="2">Data Emissione</td>
		<td class="l" colspan="4">
			<input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaremissione.getDataEmissione(),"dd")) %>" name="<%=ICostantiSiusRemissioneDebito.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; 
			<input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaremissione.getDataEmissione(),"MM")) %>" name="<%=ICostantiSiusRemissioneDebito.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; 
			<input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaremissione.getDataEmissione(),"yyyy")) %>" name="<%=ICostantiSiusRemissioneDebito.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Autorità emittente</td>
		<td class="L" colspan="4"><select Title="Autorità emittente" name="<%= ICostantiSiusRemissioneDebito.COD_AUTORITA_EMITTENTE_PROVV %>">
			<%=autoritaProvvedimento%>
		</select></td>
	</tr>
	<tr>
		<td class="l" colspan="2">Sede</td>
		<td class="L" colspan="4"><input Title="Sede"
			name="<%=ICostantiSiusRemissioneDebito.COD_LUOGO_EMITTENTE_PROVV%>"
			value="<%=richiestaremissione.getDescrLuogoEmittenteProvv()%>"
			type="text" maxlength="35" size="35"> <a
			href="Javascript:ListaComuni('LoadModificaRichiestaRemissioneDebito','<%=ICostantiSiusRemissioneDebito.COD_LUOGO_EMITTENTE_PROVV %>');">
		<img src="/images/filefolder.gif" border=0> </a></td>
	</tr>

	<tr>
		<td class="Titolo" colspan=6>Spese cui si riferisce la remissione</td>
	</tr>
	
	<tr>
		<td class="l">Spese di mantenimento in carcere</td>
		<%String flagSpeseCarcere = ""; 
		if (richiestaremissione.getFlagSpeseCarcere().compareTo("S")==0)
			flagSpeseCarcere = "checked"; 
		%>
		<td class="l"><input type="checkbox" name="<%=ICostantiSiusRemissioneDebito.FLAG_SPESE_CARCERE%>" <%= flagSpeseCarcere%> ></td>
		<td class="l">importo</td>
		<td class="l">
			<input type="text" maxlength="14" size="16" ONKEYPRESS="return TicTabNumField(this,event)" style="text-align:right" name="<%=ICostantiSiusRemissioneDebito.IMPORTO_SPESE_CARCERE%>INT" value="<%=StringUtils.toStringJSP(StringUtils.getParteIntera(richiestaremissione.getImportoSpeseCarcere())) %>"><strong>&nbsp;,&nbsp;</strong>
			<input type="text" maxlength="2" size="2" ONKEYPRESS="return TicTabNumField(this,event)" name="<%=ICostantiSiusRemissioneDebito.IMPORTO_SPESE_CARCERE %>DEC" value="<%=StringUtils.toStringJSP(StringUtils.getParteDecimale(richiestaremissione.getImportoSpeseCarcere())) %>"> 
		</td>
	</tr>
	<tr>
		<td class="l">Spese di procedimento</td>
		<%String flagSpeseProcedimento = ""; 
		if (richiestaremissione.getFlagSpeseProcedimento().compareTo("S")==0)
			flagSpeseProcedimento = "checked"; %>
		<td class="l"><input type="checkbox" name="<%= ICostantiSiusRemissioneDebito.FLAG_SPESE_PROCEDIMENTO%>" <%= flagSpeseProcedimento%> > </td>
		<td class="l">importo</td>
		<td class="l">
			<input type="text" maxlength="14" size="16" ONKEYPRESS="return TicTabNumField(this,event)" style="text-align:right" name="<%=ICostantiSiusRemissioneDebito.IMPORTO_SPESE_PROCEDIMENTO%>INT" value="<%=StringUtils.toStringJSP(StringUtils.getParteIntera(richiestaremissione.getImportoSpeseProcedimento())) %>"><strong>&nbsp;,&nbsp;</strong>
			<input type="text" maxlength="2" size="2" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiSiusRemissioneDebito.IMPORTO_SPESE_PROCEDIMENTO%>DEC" value="<%=StringUtils.toStringJSP(StringUtils.getParteDecimale(richiestaremissione.getImportoSpeseProcedimento())) %>"> 
		</td>
	</tr>
	
	<tr>
	    <td class="l">Note</td>
	    <td class="l" colspan="5">
	      <Textarea Title="Note" name="<%=ICostantiSiusRemissioneDebito.CAMPO_NOTE %>"  cols=90 rows=5><%=StringUtils.toStringJSP(richiestaremissione.getNote())%></textarea>
	    </td>
	</tr>

    <tr><td>&nbsp;</td></tr>
   <tr>
    	<td class="lNoBord" colspan="2">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>


<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaRichiestaRemissioneDebito");

  //frmvalidator.addValidation("<%= ICostantiSiusRemissioneDebito.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità è obbligatorio");
  //frmvalidator.addValidation("<%= ICostantiSiusRemissioneDebito.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>
</html>