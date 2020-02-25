<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
 
<jsp:useBean id="modalitaRicerca"  scope="request" class="java.lang.String"/>
  <% 
 // Il default è ricerca x Anno/Num
 String testo1 = " Anno/Numero (*)";
 String lAzione = "";
 %>
    <table  width=65%>
      <tr><td class="Titolo" colspan=4>Selezionare Procedimento:</td></tr>
      <table width="65%">
      <tr>
        <td class="l" width=30%>
          <font class="label"><%=testo1%></font>
        </td>
        <td class="l">
          <input type="text" title="Anno" name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          <input type="text" title="Numero" name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          <input type="hidden" name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>" value="">
        </td>
        </tr>
        </table>
        <br>
        <tr>
         <td class="label">
           <input class="bottone" type="submit" name="RICERCA" value="Ricerca"   onClick="ValidatorBase();">
         </td>
        </tr>
    </table>
    <br>
 
 
 