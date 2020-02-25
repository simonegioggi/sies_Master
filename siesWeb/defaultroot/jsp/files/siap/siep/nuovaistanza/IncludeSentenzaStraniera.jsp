<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>

<jsp:useBean id="autoritaEmi2" scope="request" class="java.lang.String"/>
<table width="100%">
    <tr>
      <td class="l">Data Sentenza <font class="ob">(*)</font></td>
         <td class="L" colspan=3>
            <input Title="Data Sentenza" type="text" value="" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO_SS %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO_SS %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
     </tr>

     <tr>
      <td class="l">Anno/Numero Sentenza </td>
      <td class="L">
          <input Title="Anno Sentenza" value="" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_SS %>" maxlength="4" size="4" >
         /<input Title="Numero Sentenza" value="" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_SS %>" maxlength="6" size="6">
      </td>
		</tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L" colspan=3>
          <select Title="Autorità Emittente"  name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS %>" >
          <%= autoritaEmi2%>
          </select>
      </td>
		</tr>
   	<tr>
				<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      <td class="L"  colspan=3>
         <input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_SS%>" value="" type="text" maxlength="35" size="35">
		       <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE_SS%>',document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS %>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
		</tr>
    <tr>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="L" colspan=3>
          <input Title="Sezione Autorità Emittente" value="" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_SS %>" maxlength="30" size="30" >
      </td>
		</tr>
    <tr><td class="Titolo" colspan=4>Sentenza di riferimento</td></tr>
		<tr>
		  <td class="l">Estremi Sentenza Straniera</td>
      <td class="L" colspan=3>
        <textarea cols=80 rows=5 Title="Note" name="<%=ICostantiSentenza.CAMPO_NOTE_SS%>"></textarea>
      </td>
		</tr>
</table>


<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"dd"))%>"> 
<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"MM"))%>"> 
<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"yyyy"))%>"> 

	