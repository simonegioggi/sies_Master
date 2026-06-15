<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@page import="f3b.web.IWebConstants"%>
<%@page import="f3b.util.DateUtils"%>
<%@page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare"%>
<%@page import="f3b.util.StringUtils"%>
<%
subSez = "_L3";
%>

<div id="altraCausaMCA" style="display: none;">
    <table width="100%">

      <tr>
        <td class="l">Anno/Numero RG.N.R.</td>
        <td class="l">
           <input Title="Anno RG.N.R." value="<%=StringUtils.toStringJSP(lMisuraCautelare.getAnnoRgnr())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_ANNO_RGNR%><%=subSez%>" maxlength="4" size="4" <%=jsNumField%>>
           /
           <input Title="Numero RG.N.R." value="<%=StringUtils.toStringJSP(lMisuraCautelare.getNumeroRgnr())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_NUMERO_RGNR%><%=subSez%>" maxlength="6" size="6" <%=jsNumField%>>
        </td>  		
        <td class="l">Tipo Ufficio PM <font class="ob">(*)</font></td>
        <td class="l">
          <select title="Tipo Ufficio PM" name="<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo<%=subSez%>" id="<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo<%=subSez%>">
            <!-- <//%=tipoUfficioPM%>  -->
            <%=tipUffPM%>
          </select>
        </td>
        <td class="l">Sede(*) </td>
        <td class="l">
          	<input value="<%=defaultSedeTipoUfficioPM%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%><%=subSez%>">          	
          	<input type="HIDDEN" name="<%=ICostantiMisuraCautelare.COMUNE_COD_SEDE_UFFICIO_PM%>" value="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" >           
            <a href="Javascript:ListaUfficiComuni('LoadInserisciPosizioneGiuridicaS1','<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%><%=subSez%>',document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo<%=subSez%>[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo<%=subSez%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0></a>
        </td>	         
      </tr>
      <tr>
	  	<td class="Titolo" colspan="6" height="20px" >   </td>
	  </tr>
    </table>

    <table width="100%">
      <tr>
		<td class="l">Anno/Numero B.D.M.C. </td>
        <td class="l">
           <input Title="Anno B.D.M.C." value="<%=StringUtils.toStringJSP(lMisuraCautelare.getAnnoFascBdmc())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_ANNO_FASC_BDMC%><%=subSez%>" maxlength="4" size="4" <%=jsNumField%>>
           /
           <input Title="Numero B.D.M.C." value="<%=StringUtils.toStringJSP(lMisuraCautelare.getNumeFascBdmc())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_NUME_FASC_BDMC%><%=subSez%>" maxlength="6" size="6" <%=jsNumField%>>
        </td>  		
        <td class="l">Anno/Numero Reg.Gen.</td>
        <td class="l">
           <input Title="Anno Reg.Gen." value="<%=StringUtils.toStringJSP(lMisuraCautelare.getAnnoRegGen())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_ANNO_REG_GEN%><%=subSez%>" maxlength="4" size="4" <%=jsNumField%>>
           /
           <input Title="Numero Reg.Gen." value="<%=StringUtils.toStringJSP(lMisuraCautelare.getNumeroRegGen())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_NUMERO_REG_GEN%><%=subSez%>" maxlength="6" size="6" <%=jsNumField%>>
        </td>

        <td class="l">Tipo Ufficio Reg. Gen.</td>
        <td class="l">
          <select title="Tipo Ufficio Reg. Gen." name="<%=ICostantiMisuraCautelare.CAMPO_TIPO_UFFICIO_REG_GEN%><%=subSez%>">
            <%=tipoUfficioRegGen%>
          </select>
        </td>
      </tr>
      <tr>
	  	<td class="Titolo" colspan="6" height="20px" >   </td>
	  </tr>
    </table>

    <table width="100%">
      <tr>
        <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select Title="Autorità" name="<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%><%=subSez%>">
            <%=autoritaEmittenteCautelare%>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Luogo Emittente</td>
        <td class="l" colspan="3">
          <input value="<%=StringUtils.toStringJSP(autoritaEmittenteCautelareLuogo)%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE_LUOGO%><%=subSez%>">
          <a href="Javascript:ListaUfficiPerTipo('LoadInserisciPosizioneGiuridicaS1','<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE_LUOGO%><%=subSez%>',document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%><%=subSez%>[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%><%=subSez%>.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>

	  <tr>    	
        <td class="l">Data emissione Ordinanza</td>
        <td class="L"> 																				
           <input type="text" title="Giorno Data inizio emissione Ordinanza" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelare.getDataEmissioneOrdinanza(), "dd")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %><%=subSez%>" <%=IWebConstants.UTIL_DATA%>>
           -
           <input type="text" title="Mese Data inizio emissione Ordinanza" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelare.getDataEmissioneOrdinanza(), "MM")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %><%=subSez%>" <%=IWebConstants.UTIL_DATA%>>
           -
           <input type="text" title="Anno Data inizio emissione Ordinanza" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraCautelare.getDataEmissioneOrdinanza(), "yyyy")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA %><%=subSez%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>         
      </tr>
      	
      <tr>
        <td class="l">Tipo Misura<font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="Tipo Misura" name="<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%><%=subSez%>">
            <%=tipoMisuraCautelareL3%>
          </select>
        </td>
      </tr>
    </table>

    <table width="100%">
      <tr>
        <td class="l">Luogo di Espiazione</td>
        <td class="l" colspan="3">
          <input title="Altro Luogo" value="<%=StringUtils.toStringJSP(lMisuraCautelare.getAltroLuogoDetenzione())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE%><%=subSez%>" size="60">
        </td>
      </tr>

      <tr>
        <td class="l">Autorità Competente per territorio</td>
        <td class="l" colspan="3">
          <select Title="Autorità" name="<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_COMPETENTE%><%=subSez%>">
          	<option value="-" />-
            <%=autoritaCompetenteCautelare%>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede</td>
        <td class="l">
          <input type="text" name="<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_COMPETENTE_SEDE%><%=subSez%>" value="<%=StringUtils.toStringJSP(lMisuraCautelare.getAutoritaCompetenteSedeDesc())%>" size="30">
          <a href="Javascript:ListaComuni('LoadInserisciPosizioneGiuridicaS1','<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_COMPETENTE_SEDE%><%=subSez%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>

        <td class="l">Indirizzo</td>
        <td class="l">
        	<textarea title='Indirizzo' name='<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_COMPETENTE_INDIRIZZO%><%=subSez%>'  cols='50' rows='3'><%=StringUtils.toStringJSP(lMisuraCautelare.getAutoritaCompetenteIndirizzo())%></textarea>
        </td>
      </tr>
    </table>
</div>



</script>