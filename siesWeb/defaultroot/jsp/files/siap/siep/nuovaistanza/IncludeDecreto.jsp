<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>

<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi2" scope="request" class="java.lang.String" />

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<table width="100%">
	<tr>
		<td class="l">Anno/Numero R.G.N.R.</td>
		<td class="L">
			<input Title="Anno Re.Ge. PM" value="" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM_D %>" maxlength="4" size="4"> /
			<input Title="Numero Re.Ge. PM" value="" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM_D %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr>
		<td class="l">Sede PM </td>
		<td class="L" colspan=3>
			<input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO_D%>" value="<%=UtenteConnesso.getUfficioUtente().getDescrComune()%>" type="text" maxlength="35" size="35"> 
  	    <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO_D%>','PM');">
				<img src="/images/filefolder.gif" border=0> 
			</a>
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Reg.Gen. GIP</td>
		<td class="L">
			<input Title="Anno Re.Ge. GIP" value="" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_GIP_D %>" maxlength="4" size="4"> /
			<input Title="Numero Re.Ge. GIP" value="" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_GIP_D  %>" maxlength="6" size="6">
		</td>
	<tr>
		<td class="Titolo" colspan=4>Dati Decreto Penale</td>
	</tr>
	<tr>
		<td class="l">Data Decreto <font class="ob">(*)</font></td>
		<td class="L" colspan=3>
			<input Title="Data Decreto" type="text" value="" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_D %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Decreto" type="text" value="" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_D %>" 
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Decreto" type="text" value="" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_D %>" 
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Decreto</td>
		<td class="L">
			<input Title="Anno Decreto" value="" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_D %>" maxlength="4" size="4"> /
			<input Title="Numero Decreto" value="" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_D %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
		<td class="L" colspan=3>
			<select Title="Autorità Emittente" name="<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D%>">
         <%= autoritaEmi2%>
			</select>
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L" colspan=3>
			<input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_D%>" value="" maxlength="35" size="35"> 
	      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_D%>',document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D %>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D%>.selectedIndex].value);">
				<img src="/images/filefolder.gif" border=0> 
			</a>
		</td>
	</tr>
	<tr>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L" colspan=3>
			<input Title="Sezione Autorità Emittente" value="" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_D %>" maxlength="30" size="30">
		</td>
	</tr>

	<tr>
		<td class="Titolo" colspan=4>Sentenza Cassazione</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Sentenza</td>
		<td class="L" colspan=3>
			<input Title="Anno Sentenza Cassazione" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE_D %>" value="" type="text" maxlength="4" size="4"> /
			<input Title="Numero Sentenza Cassazione" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE_D%>" value="" type="text" maxlength="6" size="6">
		</td>
	</tr>

	<tr>
		<td class="l">Anno/Numero Raccolta Generale</td>
		<td class="L" colspan=3>
			<input Title="Anno Raccolta Generale" value="" name="<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE_D %>" type="text" maxlength="4" size="4"> /
			<input Title="Numero Raccolta Generale" value="" name="<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE_D %>" type="text" maxlength="6" size="6">
		</td>
	</tr>

	<tr>
		<td class="l">Dispositivo Cassazione</td>
		<td class="L" colspan=3>
		<select Title="Dispositivo Cassazione" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D %>">
			<%=tipoDecisioneCassazione%>
		</select></td>
	</tr>

	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=3><textarea cols=80 rows=5
			Title="Note Aggiuntive" name="<%=ICostantiSentenza.CAMPO_NOTE_D%>"></textarea>
		</td>
	</tr>
</table>