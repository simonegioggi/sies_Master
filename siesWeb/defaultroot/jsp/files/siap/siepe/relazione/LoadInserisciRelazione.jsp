<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siepe.relazione.action.ICostantiRelazione"%>


<%
//	if( insRel )
//	{
%>
<html>
	
	<head>
    <title>[S.I.E.S.] - Inserimento Relazione</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript">
			// Controllo di verifica.
			function Verify()
    	{
      	var ritorno = false;
      	ritorno = controlloDate();
      	return ritorno;
    	}
    	
    	// Controllo delle date.
      function controlloDate()
      {
         var ret = true;
         var gg = FillDM(document.InserimentoRelazione.<%=ICostantiRelazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value);
         var mm = FillDM(document.InserimentoRelazione.<%=ICostantiRelazione.CAMPO_MESE_DATA_EMISSIONE%>.value);
         var aa = document.InserimentoRelazione.<%=ICostantiRelazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	
         var dataEmissione = gg + "/" + mm + "/" + aa;
         var dataSistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			 var dataRif = '<%=request.getParameter("dateRef")%>';	       

         if (dataEmissione.length < 10 )
         {
            ret = false;
            alert ('Data di emissione mancante');
         }
         else if (!ControllaData (dataEmissione))
         {
            ret = false;
            alert ('Data emissione non valida : ' + dataEmissione);
         }
				 else if ( ! CompareDate( dataEmissione,dataSistema) )
      	 {
        		ret = false;
        		alert('Data emissione maggiore della data attuale!');
      	 }
         else if ( ! CompareDate( dataRif, dataEmissione ) )
         {
         		ret = false;
         		alert ( "Data emissione minore della data attività o richiesta." );
         }
				
				 if( !ret )
				 	document.InserimentoRelazione.<%=ICostantiRelazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();	
         
         return ret;
      }
     </script>
    	
  </head>
  
	<body class="corpo" onLoad="javascript:document.InserimentoRelazione.<%=ICostantiRelazione.CAMPO_NOTE%>.focus()">
		<FORM name="InserimentoRelazione" enctype="multipart/form-data" method="post">
  		<table>
				
				<tr>
    			<td class="l">Titolo relazione <font class="ob">(*)</font></td>
      		<td class="L">
        		<font class="campo">
        			<input value="" type="text" size="20" maxlength="30" name="<%=ICostantiRelazione.CAMPO_NOTE%>" >
     				</font>
      		</td>
    		</tr>

				<tr>
      		<td class="l">Data emissione <font class="ob">(*)</font> (gg/mm/aa) </td>
      			<td class="L">
        			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiRelazione.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiRelazione.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        			<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiRelazione.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        		</td>
    		</tr>

				<tr>
      		<td class="l" rowspan="2">Indica il percorso locale del documento da salvare </td>
      		<td class="L">
						<font class="campo">
        			<input type="file" size="35" name="<%=ICostantiRelazione.CAMPO_DOC_BLOB%>">
						</font>
    			</td>
      	</tr>

				<tr>
      		<td class="L">
      			<input class="bottone"  type="submit" value="Conferma">
       			<input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.siepe.relazione.action.ActInserisciRelazione">
       			<input type="HIDDEN" name="<%=request.getParameter("keyField")%>"  value="<%=request.getParameter("id")%>">
      		</td>
    		</tr>
			</table>
		</FORM>
		<script language="JavaScript" type="text/javascript">
    	var frmvalidator = new Validator("InserimentoRelazione");
    	frmvalidator.setAddnlValidationFunction("Verify");
    	// Controllo campo titolo relazione.
    	frmvalidator.addValidation("<%= ICostantiRelazione.CAMPO_NOTE%>","req", "Il campo titolo relazione è obbligatorio");
    	
  	</script>
		
	</body>
</html>
<%
	//}
%>