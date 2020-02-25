<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="listaStorico" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<html>
<head>
  <title>[S.I.E.S.] - Elenco Storico Difensori</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript">
  	var str;
	function confermaeve(a_action, a_entityname, a_entityvalue, a_evento)
	{
		if (a_evento != "")
		{
			alert ('Impossibile cancellare. Esiste un provvedimento collegato');
		}else{
		  	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue;
		
		    if (window.confirm('Confermi la cancellazione ?'))
		    {
		    	window.location.href=str;
		    }
		 }  
	}
  </script>
</head>

<body class=corpo >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Storico Difensori</font></td>
    </tr>
  </table>

 <BR>
 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <BR>

 <table style="width: 95%;">
  <tr>
    <td class=int >Cognome e Nome</td>
    <td class=int >Foro</td>
    <td class=int >Tipo Difensore</td>
    <td class=int >Data Inizio Validità</td>
    <td class=int >Data Fine Validità</td>
    <td class=int >Motivo</td>
    <td class="int" width=5%>Azioni</td>
  </tr>

<%
    Iterator itx = listaStorico.iterator();
    while ( itx.hasNext()) 
    {
    	AvvocatoSiepModel lAvv = (AvvocatoSiepModel)itx.next();
%>
		  <tr>
		    <td class=c><%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%></td>
		    <td class=c><%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></td>
		    <td class=c><%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%></td>
		    <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getAvvocatoFascicoloSiepModel().getDataInizioValidita(),"dd-MM-yyyy"))%></td>
		 	<td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getAvvocatoFascicoloSiepModel().getDataFineValidita(),"dd-MM-yyyy"))%></td>
	  		<td class=c><%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getMotivo())%></td>
			<td class=c>  
			<%
		    String modificabile = "";
		    if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lAvv.getAvvocatoFascicoloSiepModel().getCodUfficioInserimento()))
		    	{modificabile = "SI";}
		    else
		    	{modificabile = "NO";}
		    %>
		    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
				<%--jsp:include page="<%=IWebConstants.PG_BUTTONS%>"--%>
				<jsp:include page="<%=ICostantiAvvocato.PG_BUTTONS%>">
		           	<jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
		           	<jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
		           	<jsp:param name="CampoIdEntita" value="idAvvFascicoloSiep" />
		           	<jsp:param name="ValoreIdEntita" value="<%=lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep() %>" />      
		           	<jsp:param name="CampoIdEvento" value="IdEvento" />
		           	<jsp:param name="ValoreIdEvento" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getEveIdEvento(),"") %>" />
	            	<jsp:param name="Modificabile" value="<%=modificabile%>" />
 				</jsp:include>
			</td>
		  </tr>
<%
		}
%>
</table>
</body>
</html>