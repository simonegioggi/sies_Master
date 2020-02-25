<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  
    <%@page import="f3b.util.StringUtils"%>
<%@page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Iterator"%>
<%@page import="siap.sico.decodifiche.model.DecodificheModel"%>
<table width="100%">
      <tr>
        <td class="l" width="25%">Posizione Giuridica <font class="ob">(*)</font></td>
        <td class="l" colspan="3">
          <select title="Posizione Giuridica" name="<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>" id="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>S1">
                  <option value="-" />-
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
			Set<String> elencoPosGiurAltraCausa = new HashSet<String>();
			elencoPosGiurAltraCausa.add("07");
			elencoPosGiurAltraCausa.add("74");
			elencoPosGiurAltraCausa.add("75");
			elencoPosGiurAltraCausa.add("76");
			elencoPosGiurAltraCausa.add("77");
			elencoPosGiurAltraCausa.add("78");
			elencoPosGiurAltraCausa.add("79");
			elencoPosGiurAltraCausa.add("80");
			elencoPosGiurAltraCausa.add("81");
			Iterator lIterS1 = posizioneGiuridicaIscrizione.iterator();
			String statoPosizione = "";
            while(lIterS1.hasNext()) {
              DecodificheModel lDecMod = (DecodificheModel)lIterS1.next();
              if(lDecMod.getCodiceAlt5().equals("L")) {
            	  String selected = "";
             	  if ( lDecMod.getDescription().equals(lPosGiu.getDescrPosizioneGiuridica()) && lDecMod.getCode().equals("07") ){
            		  //selezione di "Detenuto per altra causa" il codice posizione giuridica è uguale a "07"
            		  //quindi imposto il parametro passato per inserire la posizione giuridica uguale a statoPosizione="07"
            		  statoPosizione=lDecMod.getCode();
                	  selected = "selected";
            	  }
             	  if( elencoPosGiurAltraCausa.contains(lDecMod.getCode()) ) {
            		  //deselezione di "Detenuto per altra causa" il codice posizione giuridica si avrà tramite una lista
            		  //quindi il parametro passato per inserire la posizione giuridica sarà 
            		  //statoPosizione="posizione giuridica selezionatra"
            		  statoPosizione=lDecMod.getCode();
                	  selected = "selected";
            	  }
            	  
            	  if(lDecMod.getCode().equals("10")) {
%>
                  	<option style="color:red" value="<%=lDecMod.getCode()%>" <%=selected%>/><%=lDecMod.getDescription()%>
<%                
			      } if(lDecMod.getCode().equals("08") || lDecMod.getCode().equals("05") || lDecMod.getCode().equals("10")
			    	   // Modifica del 11/04/2016
			    	   // Escluse anche le posizioni 48-06-27 (come richiesto da Michele)
			    	   // Per la posizione giuridica 06 ho modificato il campo RV_ALT5_VALUE 
			    	   // della tabella CG_REF_CODES da 'L' a 'EI', per la posizione 
			    	   // giuridica 27 ho modificato il campo RV_ALT5_VALUE da 'L' a 'EA' 
			    	   // perchè è stato chiesto di includere la posizione 06 nell'elenco 
			    	   // delle posizioni 'Detentive' e la 27 in quello 'Altro Luogo'
			    	   //
			    	   || lDecMod.getCode().equals("48") || lDecMod.getCode().equals("06") || lDecMod.getCode().equals("27") ) 
			      {
	 					//eslusione della posizione giuridica latitante "08","05"
				  } else {
					  //escludo la posizione giuridica 07 perchè presa in considerazione in precedenza
%>
                    <option value="<%=lDecMod.getCode()%>" <%=selected%>/><%=lDecMod.getDescription()%>
<%            		  
            	  }
              }
            } // end while
%>
          </select>

                        
              <input type="hidden"  name="<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>" value="<%=statoPosizione%>"/>
           

        </td>
      </tr>

      <tr>
        <td class="l">Detenuto per altra causa</td>
        <td class="l" colspan="3">
          <input type='checkbox' name='<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>' id='<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>S1' value = 'S' onclick="checkSezioneAltraCausa();" 
          	<%=("S".equals(lFascicoloAssociato.getFlagAltraCausa())) ? "checked" : ""%>>
        </td>
      </tr>
    </table>

 
<div id="divSezioneAltraCausa" style="display: none;">
    <table width="100%">
      <tr>
        <td class="l">
        <input type='radio' name='tipoAltraCausa' id='tipoAltraCausaDD' value='DD' onClick='radioAltraCausa();' <%=checkL1%>>Definitivo - in Istituto di Detenzione
        </td>
        <td class='l'>
        <input type='radio' name='tipoAltraCausa' id='tipoAltraCausaMCD' value='MCD' onClick='radioAltraCausa();' <%=checkL2%>>Misure Cautelari - in Istituto di Detenzione
        </td>
        <td class='l'>
        <input type='radio' name='tipoAltraCausa' id='tipoAltraCausaMCA' value='MCA' onClick='radioAltraCausa();' <%=checkL3%>>Misure Cautelari - in Altro Luogo
        </td>
      </tr>
    </table>

<%
String subSez = "";
String xxx = "";
String yyy = "";
if(lAltraCausa.getIstitutoDetenzione() != null && !"".equals(lAltraCausa.getIstitutoDetenzione())) {
	xxx = StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()) + " di " + StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrizione()) + " - " + StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getIndirizzo());
	yyy = lAltraCausa.getIstDetIdIstitutoDetenzione();
}
%>

<%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-Sez1DD.jsp" %>
<%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-Sez1MCD.jsp" %>
<%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-Sez1MCA.jsp" %>

</div>

<%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-SezFooter.jsp" %>
