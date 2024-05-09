<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>


<%@ page import="siap.siep.pagoPaBatch.model.BatchPagopaModel" %>
<%@ page import="siap.siep.pagoPaBatch.action.ICostantiBatchPagoPa" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="ConsultaPagamentiJob" scope="request" class="siap.siep.pagoPaBatch.model.QuartzJobModel" />

<jsp:useBean id="PagoPaCronDebugEnabled" scope="request" class="java.lang.String" />

<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title> [S.I.E.S.] - Batch PagoPa - </title>

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript">
    function Verify()
    {
      
      return true;
    }
    
    function checkGGsett (){
      if (document.SchedulazioneBatchPagoPa.<%=ICostantiBatchPagoPa.CAMPO_CHECK_GG_SET%>.checked){
        $('input[name=<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>]').attr("disabled",false);
    	}
    	else {
        $('input[name=<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>]').attr("disabled",true);
    	}  
    }
    
  </script>
  <style>
    td.int,td.c, td.l {
      padding-left: 10px;
      padding-right: 10px;
    }
  </style>
</head>

<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="SchedulazioneBatchPagoPa">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPaBatch.action.ActConfiguraDemoneConsultazionePagoPa">

    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Configurazione Schedulazione Batch PagoPa</font></td>
      </tr>
    </table>

    <br><br>
    <%
    // Visualizzo lo stato attuale del batch
    %>

    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="int">Descrizione</td>
        <td class="int">Programmazione Esecuzione</td>
        <td class="int">Ultima Esecuzione</td>
        <td class="int">Prossima Esecuzione</td>
        <td class="int">Stato</td>
      </tr>
      <tr>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getDescrizione(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getCronExpression(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getLastExec(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getNextSched(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getStatus(), "-")%></td>
      </tr>
    </table>    
    
    <br><br>
    
<%
// if ("true".equals(PagoPaCronDebugEnabled)) {
%>
<table>
	<tr>
		<td class="Titolo" colspan="3"><font style="color:red;">Modalita' debug attiva</font></td>
	</tr>
	<tr>
		<td class="L">
			<input type="checkbox" name="<%=ICostantiBatchPagoPa.CAMPO_CHECK_CRON_EXPR%>" onClick="">QuartzCronExpression
		</td>
		<td class="L">
			<input type="text" Title="" size="20" name="<%=ICostantiBatchPagoPa.CAMPO_CRON_EXPR%>" value="<%=StringUtils.toStringJSP(ConsultaPagamentiJob.getCronExpression(), "-")%>">
		</td>
	</tr>
</table>
<%
// }
%>
    
    <table>
      <tr>
        <td class="Titolo" colspan="3">Schedulazione</td>
      </tr>
      <tr>
        <td class="L" nowrap>Minuti di Attivazione (0-59)</td>
        <td class="L">
       		<input type="text" Title="Minuti" size="4" 
                 name="<%=ICostantiBatchPagoPa.CAMPO_MINUTI%>"  
                 value="<%=ConsultaPagamentiJob.getMinuti()%>" 
                 <% if (!"true".equals(PagoPaCronDebugEnabled)) {%>
                 maxlength="2"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
                 <% } %>
                 >
        </td>
        <td class="L">descrizione</td>
      </tr>
      <tr>
        <td class="L" nowrap>Orario di Attivazione (0-23)</td>
        <td class="L">       		
       		<input type="text" Title="Ora" size="4"
                 name="<%=ICostantiBatchPagoPa.CAMPO_ORA%>"   
                 value="<%=ConsultaPagamentiJob.getOre()%>"
                 <% if (!"true".equals(PagoPaCronDebugEnabled)) {%>
                 maxlength="2"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
                 <% } %>
                 >                 
        </td>
        <td class="L">
          Indica l'orario in cui attivare il batch. Valori possibili:
          <br>* = ogni ora
          <br>Una  cifra da 0 a 23 indica l'ora in cui si vuole attivare es: 15 = si attiva alle 15
          <br>Se si vogliono specificare piu' orari separarli da virgola es 8,12,15 si attiva alle 8, alle 12 e alle 15
          <br>Se si vuole specificare un intervallo utilizzare lo / es: 0/3 ogni 3 ore a partire dalla mezzanotte [0,3,6...], 2/3 ogni 3 ore a partire dalle 2 [2,5,8...]
        </td>
      </tr>
      <tr>
        <td class="L" nowrap>Giorno del Mese (1-31)</td>
        <td class="L">       		
       		<input type="text" Title="Giorno" size="4"
                 name="<%=ICostantiBatchPagoPa.CAMPO_GIORNO%>"   
                 value="<%=ConsultaPagamentiJob.getGiornoDelMese()%>"   
                 <% if (!"true".equals(PagoPaCronDebugEnabled)) {%>
                 maxlength="2"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
                 <% } %>>
        </td>
        <td class="L">
          Indica il giorno o i giorni del mese in cui attivare il batch. Valori possibili:
          <br>* = tutti i giorni
          <br>Una  cifra da 1 a 31 indica il giorno in cui si vuole attivare es: 25 = si attiva il 25 del mese
          <br>Se si vogliono specificare piu' giorni separarli da virgola es 1,5,7 si attiva i giorni 1, 5 e 7
          <br>Se si vuole specificare un intervallo utilizzare lo / es: 0/7 ogni 7 giorni [7,14,21...], 2/3 ogni 3gg a partire dal 2 [2,5,8...]
          <br>? = il valore viene ignorato. Viene utilizzato il valore specificato in "Giorno della settimana". I 2 campi sono mutuamente esclusivi.
        </td>
      </tr>
      <tr>
        <td class="L" style="vertical-align:top;" nowrap>
          <input type="checkbox" name="<%=ICostantiBatchPagoPa.CAMPO_CHECK_GG_SET%>"
                 onClick="checkGGsett();">Giorno della settimana
        </td>
        <td class="L" nowrap>
          <input type="radio" name="<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>" value="1"/>1 - Domenica<br>
          <input type="radio" name="<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>" value="2"/>2 - Lunedi<br>
          <input type="radio" name="<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>" value="3"/>3 - Martedi<br>
          <input type="radio" name="<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>" value="4"/>4 - Mercoledi<br>
          <input type="radio" name="<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>" value="5"/>5 - Giovedi<br>
          <input type="radio" name="<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>" value="6"/>6 - Venerdi<br>
          <input type="radio" name="<%=ICostantiBatchPagoPa.CAMPO_GG_SETT%>" value="7"/>7 - Sabato
        </td>
        <td class="L">descrizione</td>
      <tr>      
    </table>
    
    <table>
      <tr>
        <td class="lNoBord" colspan="2">
         <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
        </td>
      </tr>
    </table>
    
  </form>

 <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("SchedulazioneBatchPagoPa");
  
  frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>

</html>    
    
    