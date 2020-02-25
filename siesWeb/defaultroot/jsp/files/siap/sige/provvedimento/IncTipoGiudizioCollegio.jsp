<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienzacollegiale.action.ICostantiUdienzaCollegiale"%>

<jsp:useBean id="tipoGiudizio"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioUtente"  scope="request" class="java.lang.String"/>

  <table cellspacing=2 cellpadding=2 width="95%">
   <tr>
      <td class="Titolo" colspan=6 > Definizione Tipo rito del Procedimento </td>
 </tr>
  
   <tr>
    <td class="label">Tipo Rito <font class="ob">(*)</font> &nbsp;&nbsp;&nbsp;&nbsp;
      <select title="Tipo Rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>" onchange="Visualizza(this.value)">
        <%=tipoGiudizio%>
      </select>
    </td>
   </tr>
</table>
  	<jsp:include page="<%=ICostantiUdienzaCollegiale.PG_INCLUDE_INSERISCI_COLLEGIO%>">
          <jsp:param name="form_name" value="LoadEmissioneOrdinanza"/>
          <jsp:param name="tipo_ufficio" value="<%=tipoUfficioUtente%>"/>
     </jsp:include>