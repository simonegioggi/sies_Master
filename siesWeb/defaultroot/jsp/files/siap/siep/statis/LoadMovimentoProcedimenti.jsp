<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di scelta stampe --%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.statis.model.StatoFascicoloResModel"%>
<%@ page import="siap.siep.statis.action.ICostantiStatis"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="statiFascicolo"		scope="request" class="java.util.Vector"/>
<jsp:useBean id="listaUfficiAccorpati"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficiomodel" 			scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioConnesso"		scope="request" class="java.lang.String"/>
<jsp:useBean id="dataIniziale"			scope="request" class="java.lang.String"/>
<jsp:useBean id="dataFinale"			scope="request" class="java.lang.String"/>

<html>
<head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" type="text/javascript">
    function Verify() {
    	if (document.LoadMovimentoProcedimenti.<%=ICostantiStatis.CAMPO_LISTA_STATI%>.selectedIndex == -1) {
    		alert ("Selezionare almeno un elemento dalla lista.");
    		return false;
    	}
    	return true;
    }

    function enableBtn() {
    	document.LoadMovimentoProcedimenti.btnconf.disabled = false;
    }

	function selAll() {
		var lista = LoadMovimentoProcedimenti.<%=ICostantiStatis.CAMPO_LISTA_STATI%>;
		var ind = lista.length;
		for (var i = 0; i < ind; i++) {
			lista.options[i].selected = true;
		}
		enableBtn();
	}
    </script>
</head>
<BODY class="corpo">
	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadMovimentoProcedimenti">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActCreaStatisticaRiepilogoMovimentoProcedimenti">
	<input type="HIDDEN" name="dataIniziale" value=<%=dataIniziale%>>
	<input type="HIDDEN" name="dataFinale" value=<%=dataFinale%>>
   	<table>
   		<tr>
   			<td class="LBG">
   				<a href="Javascript:window.print();">
   					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
   				</a>
   			</td>
     		<td class="LBG">
       			<font class="label">Funzione:&nbsp;</font>
       			<font class="campo">STATISTICHE - ESTRAZIONE DATI - RIEPILOGO MOVIMENTO PROCEDIMENTI</font>
     		</td>
     		<td class="LBG">
				<a href="javascript:history.go(-1);">
					<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
				</a>
			</td>
   		</tr>
   	</table>
   	<br>
   	<table>
   		<tr>
	        <td class="l" width="20%">Data Inizio Periodo</td>
	      	<td class="L" width="20%">
	      		<%=dataIniziale%>
		   	</td>
	 	    <td class="l" width="20%">Data Finale</td>
	      	<td class="L">
	      		<%=dataFinale%>
	      	</td>
	   	</tr>
    	<tr>
    		<td class="L" colspan="2">
         		<font class="label">Ufficio Accorpato Selezionato per la Statistica</font>
      		</td>
<%
if (listaUfficiAccorpati.size() == 0) {
%>     		    	
			<td colspan="2">
				<input type="text" size="10" maxlength="90" name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" title="Ufficio Accorpato" value="-" readonly>
			</td>
<%
} else {
	if (ufficioConnesso.equals("-")) {
%>	
			<td colspan="2">
				<input type="text" size="10" maxlength="90" name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" title="Ufficio Accorpato" value="-" readonly>
 			</td>
<%
	} else {
		Iterator itx = listaUfficiAccorpati.iterator();
		while (itx.hasNext()) {
			UfficioAccorpatoModel lUff = (UfficioAccorpatoModel) itx.next();
			if (lUff.getCodUfficio().equals(ufficioConnesso)) {
%>
			<td colspan="2">
				<input type="text" size="45" maxlength="90" name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" title="Anno" value="<%=lUff.getDescrizione() %>" readonly >
			</td>	
<%
			}
		}
	}	
}
%>					
		</tr>
	</table>
    <br>
    <table>
		<tr>
     		<td>
        		<input type=button value="Seleziona tutti" class=bottone name="btnsel" onClick="selAll();">
      		</td>
		</tr>
    	<tr><td class="Titolo">Selezione Stampa</td></tr>
      	<tr>    
        	<td class="LBG">
				<select name="<%=ICostantiStatis.CAMPO_LISTA_STATI%>" multiple="multiple" size="20" onChange="enableBtn();">
				<option value="0">RIEPILOGO GENERALE</option> 
<%
Iterator itx = statiFascicolo.iterator();
while (itx.hasNext()) {
	StatoFascicoloResModel sfrm = (StatoFascicoloResModel) itx.next();
%>
					<option value="<%=sfrm.getCodStatoFascicolo().toString()%>"><%=sfrm.getDescrizione().toUpperCase()%></option>
<%
}
%>
				</select>        
			</td>
		</tr>
	</table>
	<table>
    	<tr><td>&nbsp;</td></tr>
    	<tr>
      		<td>
        		<input type="submit" value="Conferma" class="bottone" name="btnconf">
      		</td>
		</tr>
    	<tr><td>&nbsp;</td></tr>
	</table>
	</FORM>
	<script language="JavaScript" type="text/javascript">
  		var frmvalidator  = new Validator("LoadMovimentoProcedimenti");
		frmvalidator.setAddnlValidationFunction("Verify");
 	</script>
</body>
</html>