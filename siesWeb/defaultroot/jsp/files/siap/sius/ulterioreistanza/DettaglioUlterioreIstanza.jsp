<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>

<%@ page import="siap.sius.ulterioreistanzatenore.model.UlterioreIstanzaTenoreModel"%>
<%@ page import="siap.sius.ulterioreistanza.action.ICostantiUlterioreIstanza"%>

<jsp:useBean id="ulterioreistanza" scope="request" class="siap.sius.ulterioreistanza.model.UlterioreIstanzaModel"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />


<html>
	<head>
	<title>[S.I.E.S.] - Dettaglio Ulteriore Istanza </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
 		<script language="JavaScript">
    	function lookUpload()
    	{
      	var node;
      	node=document.getElementById('upld');
      	node.style.visibility='visible';
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
        <td class="LBG"><font class="label">Funzione : </font>
          <font class="campo">Dettaglio Ulteriore Istanza</font>&nbsp;
        </td>
			
			<!-- BOTTONE DI MODIFICA -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.ulterioreistanza.action.ActLoadModificaUlterioreIstanza&IdUlterioreIstanza=<%=ulterioreistanza.getIdUlterioreIstanza()%>" >
            <img  align="middle" src="/images/modifica24.gif" alt="Modifica Procedimento" width="24" height="24" border="0">
          </a>
        </td>
    	<!-- BOTTONE DI CANCELLAZIONE -->
    	<td class="LBG">
      		<a href="Javascript:conferma('siap.sius.ulterioreistanza.action.ActCancellaUlterioreIstanza','<%=ICostantiUlterioreIstanza.CAMPO_ID_ULTERIORE_ISTANZA%>','<%=ulterioreistanza.getIdUlterioreIstanza()%>','<%=ICostantiDepositoDecreto.ACTION_DOPO_CANCELLAZIONE%>','siap.sius.depositodecreto.action.ActLoadFSPInserisciDecretoIrreperibilità');">
        		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      		</a>
    	</td>
    
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr> 
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>    
    <tr> 
      <td>&nbsp;</td>
    </tr>
  </table>

  <table cellspacing="4" cellpadding="4" width="95%">  	
  	<tr>
			<td class="l">Data Richiesta</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreistanza.getDataRichiesta(),"dd-MM-yyyy"), "-" )%> </font></td>
		</tr>
		<tr>
			<td class="l">Tipo Atto</td>
			<td class="l"><font class="campo"><%=ulterioreistanza.getDescrTipoAtto() %></font></td>
		</tr>	
		<tr>
			<td class="l">Tipo Mittente Atto</td>
			<td class="l"><font class="campo"><%=ulterioreistanza.getDescrTipoMittenteAtto() %></font></td>
		</tr>
		<tr>
			<td class="l">Sede Mittente</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP( ulterioreistanza.getSedeMittente(), "-" ) %></font></td>
		</tr>
		<tr>
			<td class="l">Descr Mittente</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP( ulterioreistanza.getDescrMittente(), "-" ) %></font></td>
		</tr>
		<tr>
			<td class="l">Data Arrivo in Cancelleria</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP( DateUtils.getDateToString(ulterioreistanza.getDataArrivoCancelleria(),"dd-MM-yyyy"), "-" )%> </font></td>
		</tr>
		<tr>
			<td class="l">Note</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP( ulterioreistanza.getNote(), "-" ) %></font></td>
		</tr>
  	<tr>
    	<td colspan=2>&nbsp;</td>
  	</tr>
 		<tr>
    	<td class="Titolo" colspan=2>Contenuto</td>
  	</tr>
 		
 		<tr>
 			<td class="l"><font class="campo"><%=ulterioreistanza.getDescrOggettoProcedimento()%></font></td>
 		</tr>
 		
  	<tr>
    	<td class="Titolo" colspan=2> Oggetti</td>
  	</tr>

	  <%
		List tenori = ulterioreistanza.getListUltIstTenori();
  	Iterator lInd = tenori.iterator();
  	while (lInd.hasNext())
  	{
		%>
   		<tr>
      	<%UlterioreIstanzaTenoreModel lUltIstTen = (UlterioreIstanzaTenoreModel) lInd.next();%>
      	<td class="l"><%=lUltIstTen.getDescrOggettoTenore()%></td>
   		</tr>
		<%
  	}
		%>
  	<tr>
    	<td colspan="2"> &nbsp;</td>
  	</tr>
  
    <tr> 
      <td>N.B.: In caso di Modifica o Cancellazione verificare gli oggetti del procedimento</td>
    </tr>
  
  
  </table>
  
</body>
</html>