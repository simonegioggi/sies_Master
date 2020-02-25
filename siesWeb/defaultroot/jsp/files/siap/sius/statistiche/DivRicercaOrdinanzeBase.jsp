<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
 
<jsp:useBean id="modalitaRicerca"  scope="request" class="java.lang.String"/>
  <% 
 // Il default è ricerca x Estremi Ordinanza
 String testo1 = "Anno/Numero Ordinanza (*)";
 String lAzione = "siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff";

 
    if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_DECRETO))
    {
      // Ricerca x Estremi Decreto
      testo1 = "Anno/Numero Decreto (*)";
      lAzione = "siap.sius.depositodecreto.action.ActRicercaDepositoDecretoByAnnoNumUff";
    }
    else if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_IMPUGNAZIONE))
    {
      // Ricerca x Estremi Impugnazione
      testo1 = "Anno/Numero (*)";
      lAzione = "siap.sius.statistiche.action.ActRicercaImpugnazioneByAnnoNumUff";
    }
  else if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_FOGLIO_COMPLEMENTARE))
    {
      // Ricerca x Estremi Impugnazione
      testo1 = "Anno/Numero (*)";
      lAzione = "siap.sius.documentoallegato.action.ActRicercaFoglioComplementareByAnnoNumUff";
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
          <input type="text" title="Anno Ordinanza" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >/
          <input type="text" title="Numero Ordinanza" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
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

    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3 %>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
   
     frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3 %>","req","Il campo Numero è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3%>","numeric");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("VerificaBase");

  </script>