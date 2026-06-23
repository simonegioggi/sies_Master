<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.StringUtils"%>
<%@page import="f3b.util.DateUtils"%>
<%@page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>

<div id="altraCausaDD" style="display: none;">
    <table width="100%">

      <tr>
        <td class="l">Tipo Misura <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="Tipo Misura" name="<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>">
            <%=tipoMisuraAltraCausa%>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Istituto</td>
	    <td class="l"  colspan="3">
	      <input  title="Istituto" name="Istituto_L1" id="Istituto_L1" value="<%=xxx%>" size="100" readonly>
	      <input type="hidden"  name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_L1" id="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_L1" value="<%=yyy%>">
	      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciPosizioneGiuridicaS1','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>_L1','Istituto_L1');">
	        <img src="/images/filefolder.gif" border=0></a>
	      <a href="Javascript:pulisciIstitutoId('Istituto_L1','<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_L1');"><img src="/images/delete.gif" border=0></a>
	    </td>
      </tr>

      <tr>
        <td class="l">Anno/Numero SIEP</td>
        <td class="L" colspan="3">
           <input Title="Anno SIEP" value="<%=StringUtils.toStringJSP(lAltraCausa.getAnno())%>" type="text" name="<%=ICostantiAltraCausa.CAMPO_ANNO%>" maxlength="4" size="4" <%=jsNumField%>>
           /
           <input Title="Numero SIEP" value="<%=StringUtils.toStringJSP(lAltraCausa.getNumero())%>" type="text" name="<%=ICostantiAltraCausa.CAMPO_NUMERO%>" maxlength="6" size="6" <%=jsNumField%>>
        </td>
      </tr>

      <tr>
        <td class="l">Autorità</td>
        <td class="l" colspan="3">
          <select Title="Autorità" name="<%=ICostantiAltraCausa.CAMPO_COD_AUTORITA%>">
            <%=autoritaSez1DD%>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Luogo</td>
        <td class="l" colspan="3">
          <input value="<%=StringUtils.toStringJSP(lAltraCausa.getDescrLuogo())%>" type="text" name="<%=ICostantiAltraCausa.CAMPO_COD_LUOGO%>">
          <a href="Javascript:ListaUfficiPerTipo('LoadInserisciPosizioneGiuridicaS1','<%=ICostantiAltraCausa.CAMPO_COD_LUOGO%>',document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_COD_AUTORITA%>[document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_COD_AUTORITA%>.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>

      <tr>
        <td class="l">Data Scadenza Altra Pena</td>
        <td class="L" colspan="3">
          <input Title="Giorno Data di scadenza altra pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "dd") )%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>" <%=jsNumFieldDM%>>
          -
          <input Title="Mese Data di scadenza altra pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>" <%=jsNumFieldDM%>>
          -
          <input Title="Anno Data di scadenza altra pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getDataScadenza(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>" <%=jsNumFieldYear%>>
        </td>
      </tr>

    </table>
</div>