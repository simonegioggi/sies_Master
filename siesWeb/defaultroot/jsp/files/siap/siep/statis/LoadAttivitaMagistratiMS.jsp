<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina per gestione attività magistrati --%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>
<%@ page import="siap.siep.statis.action.ICostantiStatis"%>

<jsp:useBean id="sysdate" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrati" 			scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficioConnesso"		scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioConnessoDesc"	scope="request" class="java.lang.String"/>
<jsp:useBean id="dataIniziale"			scope="request" class="java.lang.String"/>
<jsp:useBean id="dataFinale"			scope="request" class="java.lang.String"/>

<html>
  	<head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" type="text/javascript">
    function Verify() {
		if (document.c.<%=ICostantiStatis.CAMPO_LISTA_MAGISTRATI%> != null
				&& document.c.<%=ICostantiStatis.CAMPO_LISTA_MAGISTRATI%>.selectedIndex == -1) {
    		alert ("Selezionare un elemento dalla lista.");
    		return false;
    	}
		if (!document.c.pppm.checked && !document.c.am.checked) {
			alert ("Scegliere almeno un valore tra\n'Procedimenti Pendenti nel Periodo per Magistrati'\ned 'Attività Magistrati'!");
    		return false;
		}
        return true;
	}

    function enableBtn() {
		if (document.c.btnconf != null)
    		document.c.btnconf.disabled = false;
    }

    function cambiaVisibilita() {
	 	var m = document.getElementById('magis');
		m.style.display = 'none';
		var c = document.getElementById('conf');
		c.style.display = 'block';
		var lm = document.getElementById('listaMagis');
		lm.style.display = 'block';
    }
	</script> 
  	</head>
  	<BODY class="corpo">
  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
  	<input type="HIDDEN" name="dataIniziale" value="<%=dataIniziale%>">
	<input type="HIDDEN" name="dataFinale" value="<%=dataFinale%>">
	<input type="HIDDEN" name="ufficioConnesso" value="<%=ufficioConnesso%>">
	<input type="HIDDEN" name="ufficioConnessoDesc" value="<%=ufficioConnessoDesc%>">
	<table>
		<tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
				</a>
			</td>
        	<td class="LBG">
          		<font  class="label">Funzione:&nbsp;</font><font class="campo">STATISTICHE - ESTRAZIONE DATI - ATTIVITA' MAGISTRATI</font>
        	</td>
        	<td class="LBG">
				<a href="javascript:history.go(-1);">
					<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
				</a>
			</td>
      	</tr>
   	</table>
    <br>
	<table style="width: 95%;">
		<tr>
	        <td class="l" width="35%">Data Inizio Periodo</td>
	      	<td class="L">
	      		<%=dataIniziale%>
		   	</td>
		</tr>
		<tr>
	 	    <td class="l" width="35%">Data Finale</td>
	      	<td class="L">
	      		<%=dataFinale%>
	      	</td>
	   	</tr>
	</table> 
	<br>
	<table style="width: 95%;">
		<tr>
			<td class="L" width="35%">
         		<font class="label">Ufficio Accorpato Selezionato per la Statistica:</font>
      		</td>
      		<td class="L">
         		<%=ufficioConnessoDesc%>
      		</td>
		</tr>
    </table>
	<br>
	<table style="width: 95%;">
		<tr>
			<td class="l" width="35%">Procedimenti Pendenti nel Periodo per Magistrati</td>
			<td class="l"><input type="checkbox" name="pppm" value="pppm"/></td>
		</tr>
		<tr>
		   	<td class="l" width="35%">Attività Magistrati</td>
			<td class="l"><input type="checkbox" name="am" value="am"/></td>
		</tr>
	</table>
	<br>
	<div id="listaMagis" style="display: none;">
	    <table>
	    	<tr>
		 		<td class="L">
			        <font class="label">Magistrato</font>
		      	</td>
	        	<td class="LBG">
					<select name="<%=ICostantiStatis.CAMPO_LISTA_MAGISTRATI%>" size="5" onchange="enableBtn();">
						<option value="0" >Tutti</option>
<%
Iterator itx = magistrati.iterator();
while (itx.hasNext()) {
	MagistratoModel mm = (MagistratoModel) itx.next();
	if (!" MAGISTRATO NULLO".equals(mm.getCognome())) {
%>
						<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
						<option value="<%=mm.getCodMagistrato()%>">
							<%=StringUtils.toStringJSP(mm.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(mm.getNome())%>
						</option>
<%
	}
}
%>
					</select>
				</td>
			</tr>
		</table>
	</div>
	<div id="magis">
		<table style="width: 95%;">
	    	<tr><td>&nbsp;</td></tr>
	    	<tr>
	      		<td>
	       			<input type="button" value="Visualizza Magistrati" class="bottone" name="btnMag" onclick="cambiaVisibilita();">
	      		</td>
			</tr>
		</table>
	</div>
	<div id="conf" style="display: none;">
		<table style="width: 95%;">
	    	<tr><td>&nbsp;</td></tr>
	    	<tr>
	      		<td>
			        <input type="submit" value="Conferma" class="bottone" name="btnconf">
			        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActCreaStatisticaAttivitaMagistrati">
	      		</td>
			</tr>
		</table>
	</div>
	</FORM>
	<script language="JavaScript" type="text/javascript">
  		var frmvalidator = new Validator("c");
  		frmvalidator.setAddnlValidationFunction("Verify");
	</script>
	</body>
</html>