<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@page import="f3b.util.DateUtils"%>
<%
String alfa = "";
String beta = "";

if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null && !"".equals(lLuogoDetenzione.getIstDetIdIstitutoDetenzione()) && !"-".equals(lLuogoDetenzione.getIstDetIdIstitutoDetenzione())) {
	alfa = StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()) + " di " + StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrizione()) + " - " + StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo());
	beta = lLuogoDetenzione.getIstDetIdIstitutoDetenzione();
}
%>

    <table width="100%">
      <tr>
        <td class="l" width="25%">Posizione Giuridica <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="Posizione Giuridica" name="<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>">
                  <option value="-" />-
<%
            Iterator lIterS2 = posizioneGiuridicaIscrizione.iterator();
            while(lIterS2.hasNext()) {
              DecodificheModel lDecMod = (DecodificheModel)lIterS2.next();
              //if(lDecMod.getCode().equals("01") || lDecMod.getCode().equals("73")) {
              if(lDecMod.getCodiceAlt5().equals("EI")) {
			      // Modifica del 11/04/2016
			      // Escluse le posizioni 09-22-24-30-36-37-38-39-40-49 (come richiesto da Michele)
			      // Aggiunta la posizione 06, per la quale è stato modificato il campo RV_ALT5_VALUE 
			      // della tabella CG_REF_CODES da 'L' a 'EI'
            	  if(lDecMod.getCode().equals("09") || lDecMod.getCode().equals("22") || lDecMod.getCode().equals("24")
   			    	 || lDecMod.getCode().equals("30") || lDecMod.getCode().equals("36") || lDecMod.getCode().equals("37") 
   			    	 || lDecMod.getCode().equals("38") || lDecMod.getCode().equals("39") || lDecMod.getCode().equals("40")
   			    	 || lDecMod.getCode().equals("49") ) 
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
        <td class="l">Istituto</td>
	    <td class="l" colspan="3">
	      <input title="Istituto" name="Comune_EI" id="Comune_EI" value="<%=alfa%>" size=100 readonly>
	      <input type="hidden"  name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_EI" id="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_EI" value="<%=beta%>">
	      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciPosizioneGiuridicaS2','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>_EI','Comune_EI');">
	        <img src="/images/filefolder.gif" border=0></a>
	      <a href="Javascript:pulisciIstitutoId('Comune_EI','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_EI');"><img src="/images/delete.gif" border=0></a>
	    </td>
     </tr>
    </table>

<%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-SezFooter.jsp" %>