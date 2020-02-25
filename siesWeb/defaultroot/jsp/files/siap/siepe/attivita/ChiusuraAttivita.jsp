<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.siepe.attivita.model.AttivitaModel"%>

<jsp:useBean id="attivita" scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>
<jsp:useBean id="esiti" scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<script language="JavaScript">
	
	// Controllo di verifica.
  function Verify()
  {
     var ritorno = false;
     ritorno = controlloDate();
     // Se i controlli formali sono andati a buon fine presenta la message di conferma.
     if( ritorno )
     		ritorno = window.confirm('Confermi la chiusura attività ?');
     
     return ritorno;
   }

   // Controllo sulla Data di Chiusura.
   function controlloDate()
   {
   var ret = true;

   var dataIni = "<%=DateUtils.getDateToString(attivita.getDataInizio(),"dd/MM/yyyy")%>";

   var gg1 = FillDM(document.ChiusuraAttivita.<%=ICostantiAttivita.CAMPO_GIORNO_DATA_CHIUSURA%>.value);
   var mm1 = FillDM(document.ChiusuraAttivita.<%=ICostantiAttivita.CAMPO_MESE_DATA_CHIUSURA%>.value);
   var aa1 = document.ChiusuraAttivita.<%=ICostantiAttivita.CAMPO_ANNO_DATA_CHIUSURA%>.value;

   var dataFine = gg1 + "/" + mm1 + "/" + aa1;


     	if (dataFine.length < 10 )
      {
      		ret = false;
          alert ("Data di chiusura mancante");
      	}
      	else if (ControllaData (dataFine) == false)
      	{
      		ret = false;
         	alert ("Errore nella data : " + dataFine);
      	}
      	else if (CompareDate(dataIni,dataFine)== false)
      	{
      		ret = false;
          alert ("Data di Chiusura minore di Data inizio");
      	}

            return ret;
   		}

</script>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ChiusuraAttivita">

 <table cellspacing="2" cellpadding="2" style="width: 90%;">
    </tr>
      <td class="l">Data chiusura <font class="ob">(*)</font></td>
      <td class="L">(gg/mm/aa)
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataChiusura(),"dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAttivita.CAMPO_GIORNO_DATA_CHIUSURA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataChiusura(),"MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAttivita.CAMPO_MESE_DATA_CHIUSURA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataChiusura(),"yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiAttivita.CAMPO_ANNO_DATA_CHIUSURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  <tr>
    <td class="l" >Esito Attività <font class="ob">(*)</font></td>
    <td class="L">&nbsp;
      <select title="esiti"   name="<%=ICostantiAttivita.CAMPO_COD_ESITO%>" >
        <%=esiti%>
      </select>
  </tr>
  <tr>
    <td class="l">Nota di Chiusura</td>
    <td class="l">
      <Textarea Title="NotaChiusura"  name="<%=ICostantiAttivita.CAMPO_NOTA_CHIUSURA%>" cols=88 rows=5></textarea>
    </td>
  </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.attivita.action.ActChiusuraAttivita" >
  <input type="HIDDEN" name="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" value="<%=attivita.getIdAttivita()%>" >
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("ChiusuraAttivita");
    frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%=ICostantiAttivita.CAMPO_GIORNO_DATA_CHIUSURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiAttivita.CAMPO_MESE_DATA_CHIUSURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiAttivita.CAMPO_ANNO_DATA_CHIUSURA%>","numeric");
  </script>