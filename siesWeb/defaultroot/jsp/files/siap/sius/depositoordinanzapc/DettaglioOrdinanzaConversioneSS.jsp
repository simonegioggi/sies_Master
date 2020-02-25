<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="lPerMod" scope="request" class="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel" />
<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<%@ page import="f3b.util.StringUtils"%>
 <table cellspacing="2" cellpadding="2" width="90%">
       <tr>
		<td class="l">Sanzione Sostitutiva Espiata</td>
		<td class="c">
	              <font class="label">Anni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getEspiataAA(),"0")%></font>
			      <font class="label">Mesi</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getEspiataMM(),"0")%></font>
	              <font class="label">Giorni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getEspiataGG(),"0")%></font>&nbsp;
	          </td>
	</tr>
	<tr>
		<td class="l">Sanzione Sostitutiva residua da Espiare</td>
		<td class="c">
	              <font class="label">Anni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getResiduaAA(),"0")%></font>
	              <font class="label">Mesi</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getResiduaMM(),"0")%></font>
	              <font class="label">Giorni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(lPerMod.getResiduaGG(),"0")%></font>&nbsp; 
	          </td>
	</tr>
	


       <tr>
		<td class="l">Pena detentiva da espiare: Reclusione </td>
		<td class="c">
	              <font class="label">Anni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumAnniDetenzioneDom(),"0")%></font>
	              <font class="label">Mesi</font>
	              <font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumMesiDetenzioneDom(),"0")%></font>
	              <font class="label">Giorni</font>
	              <font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumGiorniDetenzioneDom(),"0")%></font>&nbsp;
	          </td>
	</tr>
	<tr>
		<td class="l">Pena detentiva da espiare: Arresto </td>
		<td class="c">
	              <font class="label">Anni</font>
	              <font class="campo"><%=StringUtils.toStringJSP (datiOrdinanza.getOrdinanza().getNumAnniArrestoRev(),"0")%></font>
	              <font class="label">Mesi</font>
	              <font class="campo"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumMesiArrestoRev(),"0")%></font>
	              <font class="label">Giorni</font>
	              <font class="campo"><%=StringUtils.toStringJSP (datiOrdinanza.getOrdinanza().getNumGiorniArrestoRev(),"0")%></font>&nbsp; 
	          </td>
	</tr>
	
</table>
<br>