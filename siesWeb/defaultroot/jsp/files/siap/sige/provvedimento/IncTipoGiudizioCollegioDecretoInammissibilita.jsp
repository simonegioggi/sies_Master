<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.udienzacollegiale.action.ICostantiUdienzaCollegiale"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="tipoGiudizio"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizioVal"   scope="request" class="java.lang.String"/>
<jsp:useBean id="UdienzaSige"	    scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>

<%
boolean udienzaSigeDefinita = false;
UdienzaSigeModel lUdienzaSige = new UdienzaSigeModel();
if( UdienzaSige!=null && UdienzaSige.getIdUdienzaSige() != null  ) {
	lUdienzaSige = UdienzaSige;
	udienzaSigeDefinita = true;
}
%>

<table cellspacing=2 cellpadding=2 width="95%">
   <tr>
      <td class="Titolo" colspan=6 > Definizione Tipo Giudizio del Procedimento </td>
 </tr>
  
   <tr>
    <td class="l" colspan="2">Tipo Rito <font class="ob">(*)</font> &nbsp;&nbsp;&nbsp;&nbsp;
<%
if (udienzaSigeDefinita) {
%>
      <select title="Tipo Rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>_CBX" disabled="disabled">
        <%=tipoGiudizio%>
      </select>
      <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>" value="<%=tipoGiudizioVal%>">
<%
} else {
%>
      <select title="Tipo Rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>">
        <%=tipoGiudizio%>
      </select>
<%
}
%>
    </td>
   </tr>

    <tr>
      <td class="l">Data Udienza</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"dd"))%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" maxlength="2" size="2" readonly>
        /
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"MM"))%>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" maxlength="2" size="2" readonly>
        /
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienzaSige.getDataUdienza(),"yyyy")) %>" type="text" name="<%= ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4" readonly>
        &nbsp;
        <a class="cliccabile" href="Javascript:InserisciUdienza(document.LoadEmissioneDecretoInammissibilita.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value);">Inserimento Udienza - Visualizza Collegio </a>
        <input type="hidden" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" value="<%=StringUtils.toStringJSP( lUdienzaSige.getColIdCollegio() )%>">
        <input type="hidden" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" value="<%=StringUtils.toStringJSP( lUdienzaSige.getIdUdienzaSige() ) %>" /> 
      </td>	
    </tr>

</table>
    