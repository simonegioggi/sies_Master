<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

    <%@page import="java.util.Iterator"%>
<%@page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@page import="f3b.util.DateUtils"%>
<table width="100%">
      <tr>
        <td class="l" width="25%">Posizione Giuridica <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="Posizione Giuridica" name="<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>">
                  <option value="-" />-
<%
            Iterator lIterS3 = posizioneGiuridicaIscrizione.iterator();
            while(lIterS3.hasNext()) {
              DecodificheModel lDecMod = (DecodificheModel)lIterS3.next();
              //if(lDecMod.getCode().equals("02") || lDecMod.getCode().equals("70") || lDecMod.getCode().equals("71") || lDecMod.getCode().equals("72")) {
              if(lDecMod.getCodiceAlt5().equals("EA")) {
            	  // Modifica del 11/04/2016
			      // Escluse le posizioni 41,42,43,44,45,51,52,67 (come richiesto da Michele)
			      // Aggiunta la posizione 27, per la quale è stato modificato il campo RV_ALT5_VALUE 
			      // della tabella CG_REF_CODES da 'L' a 'EA'
            	  if(lDecMod.getCode().equals("41") || lDecMod.getCode().equals("42") || lDecMod.getCode().equals("43")
   			    	 || lDecMod.getCode().equals("44") || lDecMod.getCode().equals("45") || lDecMod.getCode().equals("51") 
   			    	 || lDecMod.getCode().equals("52") || lDecMod.getCode().equals("67") ) 
            	  {
   	 					//eslusione della posizione giuridica
   				  } else {
%>
                  	<option value="<%=lDecMod.getCode()%>" <%=(lPosGiu.getDescrPosizioneGiuridica().equals(lDecMod.getDescription())) ? "selected" : ""%>/><%=lDecMod.getDescription()%>
<%
   				  }
   		      }
            } // end while
%>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Data di Decorrenza Pena</td>
        <td class="l"  colspan="3">
          <input Title="Giorno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPosizioneGiuridica.CAMPO_GIORNO_DATA_INIZIO%>" <%=jsNumFieldDM%>>
          -
          <input Title="Mese Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiPosizioneGiuridica.CAMPO_MESE_DATA_INIZIO%>" <%=jsNumFieldDM%>>
          -
          <input Title="Anno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiPosizioneGiuridica.CAMPO_ANNO_DATA_INIZIO%>" <%=jsNumFieldYear%>>
        </td>
       </tr>
    </table>

    <table width="100%">
      <tr>
        <td class="l">Luogo di Espiazione</td>
        <td class="L" colspan="3">
          <input title="Altro Luogo" value="<%=StringUtils.toStringJSP(lPosGiu.getLuogoEspiazione())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_LUOGO_ESPIAZIONE%>" size="60">
        </td>
      </tr>

      <tr>
        <td class="l">Autorità Competente per territorio</td>
        <td class="l" colspan="3">
          <select Title="Autorità" name="<%=ICostantiPosizioneGiuridica.CAMPO_AUTORITA_COMPETENTE%>">
          	<option value="-" />-
            <%=autoritaCompetente%>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede</td>
        <td class="l">
          <input type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_AUTORITA_COMPETENTE_SEDE%>" value="<%=StringUtils.toStringJSP(lPosGiu.getAutoritaCompetenteSedeDesc())%>" size="30">
          <a href="Javascript:ListaComuni('LoadInserisciPosizioneGiuridicaS3','<%=ICostantiPosizioneGiuridica.CAMPO_AUTORITA_COMPETENTE_SEDE%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>

        <td class="l">Indirizzo</td>
        <td class="l">
        	<textarea title='Indirizzo' name='<%=ICostantiPosizioneGiuridica.CAMPO_AUTORITA_COMPETENTE_INDIRIZZO%>'  cols='50' rows='3'><%=StringUtils.toStringJSP(lPosGiu.getAutoritaCompetenteIndirizzo())%></textarea>
        </td>
      </tr>
    </table>

<%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-SezFooter.jsp" %>