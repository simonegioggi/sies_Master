<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="f3b.security.model.ProfileModel" %>

<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="nazioni"     scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoCittadinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="sesso"       scope="request" class="java.lang.String"/>
<jsp:useBean id="dataNascitaPresunta" scope="request" class="java.lang.String"/>
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="tipoinserimento" scope="request" class="java.lang.String" />
<%
	if("fascicolo".equals(tipoinserimento)	||
		("sentenza".equals(tipoinserimento) ) ) 
		{
%>
    <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="titolo" colspan="4">Integrazione dati del Procedimento</td>                         
    </tr>     
    <tr>
      <td class="l" >Data Irrevocabilità </td>
      <td class="l" colspan="3"> 
        <input type="text" size="2" maxlength="2"  
               name="<%= ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               name="<%= ICostantiNuovaIstanza.CAMPO_MESE_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               name="<%= ICostantiNuovaIstanza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
	</table>
<%} %>
    <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="titolo" colspan="4">Soggetto In fase di Iscrizione</td>                         
    </tr>     
		<tr>
				<td class="l">Cognome <font class=ob>(*)</font></td>
				<td class="L">
					<input title="Cognome" type="text" name="<%= ICostantiSoggetto.CAMPO_COGNOME %>"  maxlength="35" size="35"
					value="<%=soggetto.getCognome()%>" >
				</td>
				<td class="l">Nome <font class=ob>(*)</font></td>
				<td class="L">
					<input title="Nome" type="text" name="<%= ICostantiSoggetto.CAMPO_NOME %>"  maxlength="35" size="35"
					value="<%=soggetto.getNome()%>" >
				</td>
		</tr>
		<tr>
				<td class="l">Sesso <font class=ob>(*)</font></td>
				<td class="L">
          <select title="Sesso" name="<%=ICostantiSoggetto.CAMPO_SESSO%>">
           <%= sesso %>
          </select>
        </td>

		 <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="L">
        		<input type="text" title="Giorno Data di nascita" name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" 
        			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(soggetto.getDataNascita(),"dd"))%>" >
            /
            <input type="text" title="Mese Data di nascita" name="<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" 
            	maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(soggetto.getDataNascita(),"MM"))%>" >
            /
            <input type="text" title="Anno Data di nascita" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(soggetto.getDataNascita(),"yyyy"))%>" >

          Data Presunta
     
          <select title="Data presunta" name="<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>">
            <%= dataNascitaPresunta %>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Comune Nascita <font class=ob>(*)</font></td>
        <td class="L">
          <input title="Comune di Nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>"  maxlength="35" size="35" onChange="cancellaCodComuneReale();"
						value="<%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita())%>" >
          <a href="Javascript:ListaComuni('LoadInserisciSentenza','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
				<td class="l">Stato Cittadinanza</td>
				<td class="L">
          	<select title="Stato Cittadinanza" name="<%=ICostantiSoggetto.CAMPO_NAZIONALITA%>" > 
          			<%= StatoCittadinanza %>
         		</select>
				</td>
        
 		</tr>
		<tr>        
		 <td class="l">Stato di Nascita</td>
		 <td class="L">
          <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
           	<%= nazioni %>
          </select>
         </td>
				<td class="l">Comune Nascita Estero</td>
				<td class="L"><input title="Comune di Nascita Estero" type="text" name="<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>"  
					value="<%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()) %>">
				</td>
		</tr>

		<tr>
				<td class="l">Paternità</td>
				<td class="L" colspan="3"><input title="Paternità" type="text" name="<%= ICostantiSoggetto.CAMPO_PATERNITA %>"  maxlength="35" size="35"
					value="<%=StringUtils.toStringJSP(soggetto.getPaternita() )%>">
				</td>
		</tr>
		<tr>
				<td class="l">Cognome Madre</td>
				<td class="L"><input title="Cognome della madre"  type="text" name="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>"  maxlength="35" size="35"
					value="<%=StringUtils.toStringJSP(soggetto.getCognomeMadre() )%>">
				</td>

				<td class="l">Nome Madre</td>
				<td class="L"><input title="Nome della madre" type="text" name="<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>"  maxlength="35" size="35"
					value="<%=StringUtils.toStringJSP(soggetto.getNomeMadre() )%>">
				</td>
		</tr>

    <tr>
        <td class="l">Codice Fiscale</td>
        <td class="L"><input  title="Codice Fiscale" id=<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>
        	type="text" name="<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>"  maxlength="16" size="18"
        	value="<%=StringUtils.toStringJSP(soggetto.getCodFiscale() )%>">
        </td>

				<td class="l">Atto Nascita</td>
				<td class="L"><input title="Atto di nascita" 
					type="text" name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>"  maxlength="10" size="10"
        	value="<%=StringUtils.toStringJSP(soggetto.getAttoNascita() )%>">
        </td>
		</tr>

    <tr>
      <td class="l">Codice CUI</td>
			<td class="L"><input title="Codice CUI" 
   			type="text" name="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>"  maxlength="7" size="7"
       	value="<%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>">
      </td>

      <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="note" name="<%= ICostantiSoggetto.CAMPO_NOTE %>" cols=50 rows=3>
        </textarea>
      </td>
		</tr>

    <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">

  </table>

