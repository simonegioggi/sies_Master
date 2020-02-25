<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"%>
<%@ page import="siap.sius.cancassfascsius.action.ICostantiCancAssFascSius"%>
<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria"%>


<jsp:useBean id="cancellerie" scope="request" class="java.util.Vector"/>
<jsp:useBean id="cod_ufficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="data_minima"     scope="request" class="java.util.Date"/>


<html>
<head>
<title>[S.I.A.P.] - GestioneCancelleriaAssegnatariaPerFascicoloSIUS </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript">
function Verify()
{
   var data_minima = "<%=DateUtils.getDateToString(data_minima,"dd/MM/yyyy")%>";
   var data_inizio=document.LoadInserisciCancAssFascSius.<%=ICostantiCancAssFascSius.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciCancAssFascSius.<%=ICostantiCancAssFascSius.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciCancAssFascSius.<%=ICostantiCancAssFascSius.CAMPO_ANNO_DATA_INIZIO%>.value;
   var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

   if (! ControllaData(data_inizio))
   {
     alert('Data Inizio non valida!');
     return false;
   }

   // Controllo data di sistema >= Data Inizio
   if ( !CompareDate( data_inizio, data_sistema ) )
   {
     alert('Data Inizio non può essere superiore alla data odierna');
     return false;
    }
   if (ControllaData(data_minima))
   {
      // Controllo data di Inizio >= Data Minima
      if ( !CompareDate( data_minima, data_inizio ) )
      {
        alert("Data Inizio non può precedere la data " + data_minima);
        return false;
      }
   }

   return true;
}
</script>

</head>


<body class="corpo">
  <table>
   <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <%
      String lAzione = new String();
      if( modalita.equals("I") )
      {
          lAzione = "siap.sius.cancassfascsius.action.ActInserisciCancAssFascSius";
    %>
       <font class="campo">Assegnazione del Procedimento ad una Cancelleria</font>
    <%
      }
      else if( modalita.equals("M") )
      {
     %>
       <font class="campo">Modifica</font>
     <%}%>
    </td>
   </tr>
 </table>
    <br>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciCancAssFascSius">
   <input type="hidden" value="<%=lAzione%>" name="<%=IWebConstants.ACTION_FIELD%>">
   <input type="hidden" value="<%=cod_ufficio%>"  name="<%= ICostantiCancAssFascSius.CAMPO_COD_UFFICIO%>"  >


 <table cellspacing=4 cellpadding=4>
    <tr>
      <td class="l">Data Inizio<font class="ob">(*)</font></td>
      <td class="l">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiCancAssFascSius.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiCancAssFascSius.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiCancAssFascSius.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <jsp:include page="<%=ICostantiCancelleriaAssegnataria.PG_CANCELLERIE_COMBO%>"/>
 </table>
<BR>
<input type="submit" class=bottone name="go" value="Conferma">
</form>
 <script language="JavaScript" type="text/javascript"> var frmvalidator  = new Validator("LoadInserisciCancAssFascSius");
  frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%=ICostantiCancAssFascSius.CAMPO_GIORNO_DATA_INIZIO%>","req","Il campo Giorno  della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiCancAssFascSius.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiCancAssFascSius.CAMPO_MESE_DATA_INIZIO%>","req","Il campo Mese  della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiCancAssFascSius.CAMPO_MESE_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiCancAssFascSius.CAMPO_ANNO_DATA_INIZIO%>","req","Il campo Anno della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiCancAssFascSius.CAMPO_ANNO_DATA_INIZIO%>","numeric");
 </script>
 </body>
</html>