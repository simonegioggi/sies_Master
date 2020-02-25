<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.depositosentenza.action.ICostantiDepositoSentenza"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
 
<jsp:useBean id="modalitaRicerca"  scope="request" class="java.lang.String"/>
  <% 
	// Il default è ricerca x Estremi Sentenza
	String testo1 = "";
	String lAzione = "";
 
    if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_ESTREMI_SENTENZA))
    {
      // Ricerca x Estremi Sentenza
      testo1 = "Anno/Numero Sentenza (*)";
      lAzione = "siap.sius.depositosentenza.action.ActRicercaDepositoSentenzaByAnnoNumUff";
    }
   
 %>
  
 
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaBase">
    <table  width=95%>
      <tr><td class="Titolo" colspan=4>Indicare</td></tr>
      <table  width=50%>
      <tr>
        <td class="c" >
          <font class="label"><%=testo1%></font>
        </td>
        <td class="l">
          <input type="text" title="Anno Ordinanza" name="<%=ICostantiDepositoSentenza.CAMPO_ANNO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >/
          <input type="text" title="Numero Ordinanza" name="<%=ICostantiDepositoSentenza.CAMPO_NUM%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        </tr>
        </table>
        <br>
        <tr>
         <td class="label">
           <input class="bottone" type="submit" name="RICERCA" value="Ricerca">
         </td>
        </tr>
    </table>
    <br>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  </FORM>
 
 
   <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("RicercaBase");

    frmvalidator.addValidation("<%=ICostantiDepositoSentenza.CAMPO_ANNO %>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoSentenza.CAMPO_ANNO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoSentenza.CAMPO_ANNO%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
   
     frmvalidator.addValidation("<%=ICostantiDepositoSentenza.CAMPO_NUM %>","req","Il campo Numero è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoSentenza.CAMPO_NUM%>","numeric");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("VerificaBase");

  </script>