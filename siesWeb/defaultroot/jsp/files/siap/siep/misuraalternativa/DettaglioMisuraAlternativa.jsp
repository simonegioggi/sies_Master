<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<jsp:useBean id="misuraalternativa" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<html>
<head>		
<title>[S.I.E.S.] - Dettaglio MisuraAlternativa </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


		<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio MisuraAlternativa</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=misuraalternativa.getIdMisuraAlternativa()%>" />
       </jsp:include>
     </td>
   </tr>
 </table>
</FORM>
		 <table cellspacing=4 cellpadding=4>
		<tr>
				<td class="l">IdMisuraAlternativa</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getIdMisuraAlternativa() %></font></td>
		</tr>
		<tr>
				<td class="l">CodTipoDecisione</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodTipoDecisione() %></font></td>
		</tr>
		<tr>
				<td class="l">CodNaturaDecisione</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodNaturaDecisione() %></font></td>
		</tr>
		<tr>
				<td class="l">CodTipoMisura</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodTipoMisura() %></font></td>
		</tr>
		<tr>
				<td class="l">DataDecisione</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">CodMagistrato</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodMagistrato() %></font></td>
		</tr>
		<tr>
				<td class="l">CodUfficioSorveglianza</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodUfficioSorveglianza() %></font></td>
		</tr>
		<tr>
				<td class="l">CssIdCssa</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCssIdCssa() %></font></td>
		</tr>
		<tr>
				<td class="l">DescrLuogoProva</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getDescrLuogoProva() %></font></td>
		</tr>
		<tr>
				<td class="l">NumAnniMisura</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getNumAnniMisura() %></font></td>
		</tr>
		<tr>
				<td class="l">NumMesiMisura</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getNumMesiMisura() %></font></td>
		</tr>
		<tr>
				<td class="l">NumGiorniMisura</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getNumGiorniMisura() %></font></td>
		</tr>
		<tr>
				<td class="l">DataInizioMisura</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">DataFineMisura</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">ChiaveAnnoFascicoloSius</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getChiaveAnnoFascicoloSius() %></font></td>
		</tr>
		<tr>
				<td class="l">ChiaveUfficioFascicoloSius</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getChiaveUfficioFascicoloSius() %></font></td>
		</tr>
		<tr>
				<td class="l">ChiaveProgrFascicoloSius</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getChiaveProgrFascicoloSius() %></font></td>
		</tr>
		<tr>
				<td class="l">AnnoRegistro</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getAnnoRegistro() %></font></td>
		</tr>
		<tr>
				<td class="l">NumeroRegistro</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getNumeroRegistro() %></font></td>
		</tr>
		<tr>
				<td class="l">CodOperatoreInserimento</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodOperatoreInserimento() %></font></td>
		</tr>
		<tr>
				<td class="l">DataInserimento</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(misuraalternativa.getDataInserimento(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">CodUfficioInserimento</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodUfficioInserimento() %></font></td>
		</tr>
		<tr>
				<td class="l">CodOperatoreAggiornamento</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodOperatoreAggiornamento() %></font></td>
		</tr>
		<tr>
				<td class="l">DataAggiornamento</td>
				<td class="l"><font class="campo"><%=DateUtils.getDateToString(misuraalternativa.getDataAggiornamento(),"dd-MM-yyyy")%> </font></td>
		</tr>
		<tr>
				<td class="l">CodUfficioAggiornamento</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getCodUfficioAggiornamento() %></font></td>
		</tr>
		<tr>
				<td class="l">FasSieIdFascicoloSiep</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getFasSieIdFascicoloSiep() %></font></td>
		</tr>
		<tr>
				<td class="l">EveIdEvento</td>
				<td class="l"><font class="campo"><%=misuraalternativa.getEveIdEvento() %></font></td>
		</tr>
		</table>	</body>
</html>