<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiRif" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimenti" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiAltro" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaProvRif" scope="request" class="java.lang.String" />
<jsp:useBean id="flagSN" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito1" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito2" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String" />

<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel" />


<%
 SentenzaModel lSentenza = new SentenzaModel();
 if (modalita.equals("M")) {
			lSentenza = new SentenzaModel(sentenza);
}
%>
<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="l">Anno/Numero R.G.N.R. <font class="ob">(*)</font></td>
		<td class="L"><input Title="Anno R.G.N.R."
			value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRegePm()) %>"
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>"
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero R.G.N.R."
			value="<%=lSentenza.getNumeroRegePm() %>" type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>" maxlength="6"
			size="6"></td>
<%
String ARG = "";
String NRG = "";
String Tipo = "";
if (lSentenza.getAnnoRegeCap() != null) {
	ARG = lSentenza.getAnnoRegeCap() + "";
	NRG = lSentenza.getNumeroRegeCap() + "";
	Tipo = "cap";
}
if (lSentenza.getAnnoRegeCas() != null) {
	ARG = lSentenza.getAnnoRegeCas() + "";
	NRG = lSentenza.getNumeroRegeCas() + "";
	Tipo = "cas";
}
if (lSentenza.getAnnoRegeDib() != null) {
	ARG = lSentenza.getAnnoRegeDib() + "";
	NRG = lSentenza.getNumeroRegeDib() + "";
	Tipo = "dib";
}
if (lSentenza.getAnnoRegeGip() != null) {
	ARG = lSentenza.getAnnoRegeGip() + "";
	NRG = lSentenza.getNumeroRegeGip() + "";
	Tipo = "gip";
}
if (lSentenza.getAnnoRegeCasap() != null) {
	ARG = lSentenza.getAnnoRegeCasap() + "";
	NRG = lSentenza.getNumeroRegeCasap() + "";
	Tipo = "casap";
}
// MEV_66: aggiunte quattro nuove proprietà
if (lSentenza.getAnnoRegeGup() != null) {
	ARG = lSentenza.getAnnoRegeGup() + "";
	NRG = lSentenza.getNumeroRegeGup() + "";
	Tipo = "gup";
}
if (lSentenza.getAnnoRegeCapsm() != null) {
	ARG = lSentenza.getAnnoRegeCapsm() + "";
	NRG = lSentenza.getNumeroRegeCapsm() + "";
	Tipo = "capsm";
}
%>
		<td class="l">Anno/Numero Reg.Gen. <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Anno Reg.Gen." value="<%=ARG%>" type="text" name="ARG" maxlength="4" size="4" 
				onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input title="Numero Reg.Gen." value="<%=NRG%>" type="text" name="NRG" maxlength="6" size="6"> &nbsp; 
			<select name="TipoRG">
				<option value="-">-</option>
<%
String sel = "";
if (Tipo.equals("gip"))
	sel = " selected";
%>
				<option value="gip" <%=sel%>>GIP</option>
<%-- MERGE v10: aggiunta option GUP --%>
<%
sel = "";
if (Tipo.equals("gup"))
	sel = " selected";
%>
				<option value="gup" <%=sel%>>GUP</option>
<%
sel = "";
if (Tipo.equals("dib"))
	sel = " selected";
%>
				<option value="dib" <%=sel%>>DIB</option>
<%
sel = "";
if (Tipo.equals("cas"))
	sel = " selected";
%>
				<option value="cas" <%=sel%>>CAS</option>
<%
sel = "";
if (Tipo.equals("cap"))
	sel = " selected";
%>
				<option value="cap" <%=sel%>>CAP</option>
<%
sel = "";
if (Tipo.equals("casap"))
	sel = " selected";
%>
				<option value="casap" <%=sel%>>CASAP</option>
<%-- MEV_66: aggiunte quattro nuove proprietà --%>
<%
sel = "";
if (Tipo.equals("capsm"))
	sel = " selected";
%>
				<option value="capsm" <%=sel%>>CAPSM</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="l">Sede PM <font class=ob>(*)</font></td>		
		<%
			String sedePM =( modalita.equals("M")? lSentenza.getDescrSedeNotiziaReato() : UtenteConnesso.getUfficioUtente().getDescrComune());

		  if (UtenteConnesso.getUfficioUtente().getDescrTipoUfficio().contains("Procura Generale") || modalita.equals("M")) {
		%>
		<td class="L">
			<input Title="Sede PM" name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>" value="<%=sedePM%>" type="text" maxlength="35" size="35">
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>','PM');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
   <%}else{%>
		<td class="L">
			<input Title="Sede PM"
			type="text"
			name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>"
			value="<%=UtenteConnesso.getUfficioUtente().getDescrComune()%>" maxlength="35" size="35" readonly>
		</td>			
            <% }%>
			
	</tr>
	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Sentenza da Eseguire</td>
	</tr>
	<tr>
		<td class="l">Data Sentenza <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" 
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>"
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"
				maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Anno Sentenza" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza()) %>" 
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>" maxlength="4" size="4" 
			onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Sentenza" value="<%=lSentenza.getNumeroSentenza() %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
		<td class="L">
		<select Title="Autorità Emittente" onChange="ctrl_autorita('<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>', '<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>', 'D1', '<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO %>');"
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>"> 
			<%=autoritaEmi%>
		</select></td>
		<td colspan=2>
		<%
			String visib1 = new String("hidden");
			if (modalita.equals("M") && (lSentenza.getCodTipoAutoritaEmittente().equals("DIB") || 
					lSentenza.getCodTipoAutoritaEmittente().equals("TRIBSD"))) 
			{
				visib1 = "visible";
			}
		%>
		<div id=D1 STYLE="visibility: <%=visib1%>">
		<table style="width: 100%;">
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L"><select Title="Tipo Rito"
					name="<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO %>">
					<%=tipoRito1%>
				</select></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L">
			<input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>" 
				value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
					<img src="/images/filefolder.gif" border=0> </a></td>
		<td class="L">Sezione Autorità Emittente</td>
		<td class="L"><input Title="Sezione Autorità Emittente" value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>"
			type="text"
			name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>"
			maxlength="35" size="35"></td>
	</tr>	
	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Altro Grado di Giudizio</td>
	</tr>
	<tr>
		<td class="l">Tipo Provvedimento</td>
		<td class="L">
			<%-- MERGE v10: modificato commento nel value --%> <%--//=lSentenza.getCodTipoProvvedimentoRif()--%>
			<select Title="Tipo Prevvedimento Riferimento"
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>"
				OnChange="viewDiv('cassazione', 'LoadInserisciSentenza', '<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>', '53')">
				<%=tipoProvvedimenti%>
			</select>
		</td>
	</tr>
	<tr id="tipoSentenza" style="display:block">
		<td class="l">Tipo Sentenza</td>
		<td class="L"><select Title="Tipo Sentenza Riferimento"
			name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>">
			<%=tipoProvvedimentiRif%>
		</select></td>
	</tr>
	<tr>
		<td class="l">Data <div id="labelSentenza">Sentenza</div><div id="labelOrdinanza" style="display:none">Ordinanza</div></td>
		<td class="L">
			<input Title="Giorno Data Sentenza di Riferimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"dd")) %>" 
				name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Mese Data Sentenza di Riferimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"MM")) %>"
				name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Anno Data Sentenza di Riferimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvRif(),"yyyy")) %>" 
				name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Anno/Numero 
			<div id="anSentenza">Sentenza</div>
			<div id="anOrdinanza" style="display:none">Ordinanza</div>
		</td>
		<td class="L">
			<input Title="Anno Sentenza Riferimento" value="<%=StringUtils.toStringJSP(lSentenza.getAnnoProvvRif())%>" 
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_PROVV_RIF %>" maxlength="4" size="4" 
			onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Sentenza Riferimento" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroProvvRif() )%>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L">
			<select Title="Autorità Sentenza Riferimento" onChange="ctrl_autorita('<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>', '<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>', 'D2', '<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF %>');"
				name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>">
				<%=autoritaProvRif%>
			</select>
		</td>
		<td colspan=2>
		<%
			String visib2 = new String("hidden");
			if (modalita.equals("M") && (lSentenza.getCodTipoAutoritaProvvRif().equals("DIB") || 
				  lSentenza.getCodTipoAutoritaProvvRif().equals("TRIBSD"))) 
			{
				visib2 = "visible";
			}
		%>
		<div id=D2 STYLE="visibility: <%=visib2%>">
		<table style="width: 100%;">
			<tr>
				<td class="l">Tipo Rito</td>
				<td class="L">
					<select Title="Tipo Rito Riferimento" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_RITO_RIF %>">
						<%=tipoRito2%>
					</select>
				</td>
			</tr>
		</table>		
		</div>
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L">
		<input Title="Luogo Sentenza Riferimento" value="<%=lSentenza.getDescrLuogoProvvRif() %>" type="text" name="<%= ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>" maxlength="35" size="35">
      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF%>',document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF%>.selectedIndex].value);">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L">
			<input Title="Sezione Autorità Riferimento" value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaProvvRif()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF %>" maxlength="100" size="35">
		</td>
	</tr>
	</table>
	<!---------------------------DECISIONE CASSAZIONE------------------------------->
	<div id="cassazione" style="display:block">
	<table cellspacing=2 cellpadding=2 style="width: 100%;">
	<tr>
		<td class="Titolo" colspan=4>Decisione Cassazione</td>
	</tr>
	<tr>
		<td class="l">Tipo Provvedimento</td>
		<td class="L">
			<%-- MERGE v10: modificato commento nel value --%> <%--//=lSentenza.getCodTipoProvvedimentoAltro()--%>
			<select Title="Tipo Prevvedimento Cassazione" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>"
				onChange="viewDiv('0', 'LoadInserisciSentenza', '<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>', '53')">
				<%=tipoProvvedimentiAltro%>
			</select>
		</td>
	</tr>
	<tr id="anRegGen">
		<td class="l">Anno/Numero Reg.Gen.</td>
		<td class="L">
		<input Title="Anno Re.Ge. CASSAZIONE" value="<%=StringUtils.toStringJSP( lSentenza.getNote1DecisioneCassazione())%>" 
		type="text" name="ANNOREGECAS" maxlength="4" size="4"
		onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / 
		<input Title="Numero Re.Ge. CASSAZIONE" value="<%=StringUtils.toStringJSP( lSentenza.getNote2DecisioneCassazione())%>" type="text" name="NUMREGECAS" maxlength="6" size="6">
	</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero
		<div id="lblSentenza">Sentenza</div><div id="lblOrdinanza" style="display:none">Ordinanza</div>
		</td>	
		<td class="L">
			<input Title="Anno Sentenza Cassazione" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenzaCassazione())%>" 
				type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>" 
				maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Sentenza Cassazione" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione()) %>"
				type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>" maxlength="6" size="6">
		</td>
	</tr>
	<tr id="anRacGen"> 
		<td class="l">Anno/Numero Raccolta Generale</td>
		<td class="L">
			<input Title="Anno Raccolta Generale" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRaccoltaGenerale())%>"
				type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>" 
				maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / 
			<input Title="Numero Raccolta Generale" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%>" 
				type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr id="disp">
		<td class="l">Dispositivo</td>
		<td class="L" colspan=3>
			<select Title="Dispositivo Cassazione" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>">
				<%=tipoDecisioneCassazione%>
			</select>
		</td>
	</tr>
	</table>
	</div>
	<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=3><textarea cols=80 rows=5
			Title="Note Aggiuntive" name="<%=ICostantiSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
		</td>
	</tr>
	</table>
	