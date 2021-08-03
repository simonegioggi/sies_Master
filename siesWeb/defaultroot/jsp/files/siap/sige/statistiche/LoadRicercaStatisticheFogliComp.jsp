<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sige.statistiche.action.ICostantiStatistiche"%>

<html>
<head>
  <title>[S.I.E.S.] - Monitoraggio/Estrazione Dati</title>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"> </script>
  <script language="JavaScript" src="<%=ICostantiStatistiche.RICERCA_JS%>"></script>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
      function calendario(a_formname,a_field_year,a_field_month,a_field_day)    {
          desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }

      function Verify () {
    	  if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>.value != '' || 
    		  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_INIZIO%>.value != ''	||
    		  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>.value != '') {
    	      var data_to_verify=document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>.value+'/'+document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_INIZIO%>.value+'/'+document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>.value;
              if (! ControllaData(data_to_verify)) {
                  alert('Data Compilazione non valida');
                  return false;
              }
    	  }
 
    	  if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_FINE%>.value != '' || 
        	  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_FINE%>.value != ''	||
        	  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>.value != '') {

              var data_to_verify=document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_FINE%>.value+'/'+document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_FINE%>.value+'/'+document.RicercaStatisticheFC.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>.value;
              if (! ControllaData(data_to_verify)) {
                  alert('Data Finale non valida');
                  return false;
              }
    	  }

    	  if (!isReportSelected ()) {
    		  alert('Selezionare almeno una tipologia di Foglio Complementare.');
              return false;
    	  }
    	  
    	  return true;
      }

      function checkTutti() {
    	  var flagChecked=document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked;
    	  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_TRASMESSI%>.checked=flagChecked;
          document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_MANUALI%>.checked=flagChecked;
          document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_PROVV_PRIVI_FC%>.checked=flagChecked;
          //document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_NON_TRASMESSI%>.checked=flagChecked;
          //document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_ERRORE%>.checked=flagChecked;
          document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_ANNULLATI%>.checked=flagChecked;
      }
      
      function checkSelection () {
    	  
    	  if (!document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_MANUALI%>.checked) {
    		  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked=false;
    		  return;
    	  }  
    	  if (!document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_PROVV_PRIVI_FC%>.checked) {
    		  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked=false;
    		  return;
    	  }
    	  
    	  if (!document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_TRASMESSI%>.checked) {
    		  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked=false;
    		  return;
          }
    	  
    	  /* if (!document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_NON_TRASMESSI%>.checked) {
    		  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked=false;
    		  return;
    	  }	  
    	  
    	   if (!document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_ERRORE%>.checked) {
    	   	  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked=false;
    	      return;
    	  }
    	   */   

    	  if (!document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_ANNULLATI%>.checked) {
    		  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked=false;
    		  return;
    	  }
    	  
    	  document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>.checked=true;
      }
      
      function isReportSelected () {
    	  if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_TRASMESSI%>.checked) return true;
          if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_MANUALI%>.checked) return true;
    	  if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_PROVV_PRIVI_FC%>.checked) return true;
    	  //if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_NON_TRASMESSI%>.checked) return true;
    	  //if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_ERRORE%>.checked) return true;
    	  if (document.RicercaStatisticheFC.<%=ICostantiStatistiche.CHECK_FOGLI_ANNULLATI%>.checked) return true;
    	  return false;
      }
  </script>
</head>
  <body class="corpo">
  
  <form name="f">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Statistiche Fogli Complementari</font>

      </td>
    </tr>
  </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  </form>

  <div id="comune" style="position: relative; top: 0; left: 0; visibility:visible; " >     
     <div id="RicercaAvanzataDiv" style="position: absolute; top: 0; left: 0; visibility:visible; ">      
    <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaStatisticheFC" >
    <table  width=96%>
      <tr> <td class="Titolo" colspan=4>Compilazione Foglio Complementare: Intervallo Anno</td></tr>
    </table>
   
     <table width=96% >
     
      <tr>
	      <td class="c" width="50%" >
	            Anno:
				<input type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>" onkeypress="return TicTabNumField(this,event)" value=""  size="4" maxlength="4" />
		 </td>		 
		 <td class="c" width="50%" >
	            Anno Finale:
				<input type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>" onkeypress="return TicTabNumField(this,event)" value=""  size="4" maxlength="4" />
		 </td>
      </tr>
      <tr>
     </tr>
 
      </table>
    <div id="contenitore" style="position: relative; top: 0; left: 0;  " >         
    <div id="EstremiOrdinanza" style="position:relative;  top: 0; left: 0; " >  
      <table  width=95%>
       <tr> <td class="Titolo" colspan=4>Compilazione Foglio Complementare: Intervallo Date </td></tr>
      <tr>
        <td class="c" >
          <font class="label">Data Compilazione </font>
        </td>
        <td class="l">
          <input type="text" 
        	name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>"
        	value=""
        	size="2" maxlength="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" 
        	name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_INIZIO%>" 
        	value="" 
        	size="2" maxlength="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" 
        	name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>" 
        	value="" 
        	size="4" maxlength="4" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
			<a href="javascript:calendario('RicercaStatisticheFC','<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>','<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_INIZIO%>','<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
        </td>
        <td class="c" >
          <font class="label">Data Finale </font>
        </td>
        <td class="l">
          <input type="text" 
        	name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_FINE%>"
        	value=""
        	size="2" maxlength="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" 
        	name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_FINE%>" 
        	value="" 
        	size="2" maxlength="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" 
        	name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>" 
        	value="" 
        	size="4" maxlength="4" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
			
			<a href="javascript:calendario('RicercaStatisticheFC','<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>','<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_FINE%>','<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_FINE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
        </td>
        </tr>      
       </table>
       <table  width=96%>
      <tr> <td class="Titolo" colspan=4>Tipologia foglio Complementare</td></tr>
      <tr>
        
        <td class="l" width="90%">
          &nbsp;
        </td>
        
        <td class="l">
          <font class="label">Tutti</font>
        </td>
        
        <td class="l">
          <input type="checkbox" name="<%=ICostantiStatistiche.CHECK_ALL_STATISTICS%>" value="TUTTI" onclick="checkTutti()" />
        </td>
        </tr>
        <tr>
        
        <td class="l" colspan="2">
          <font class="label">Provvedimenti con Fogli Complementari</font>
        </td>
        <td class="l">
          <input type="checkbox" name="<%=ICostantiStatistiche.CHECK_FOGLI_TRASMESSI%>" value="true" onclick="checkSelection()" />
        </td>
        </tr>
        <tr>
        <td class="l" colspan="2">
          <font class="label">Fogli Complementari Iscritti Manulamente o con altre opzioni</font>
        </td>
        <td class="l">
          <input type="checkbox" name="<%=ICostantiStatistiche.CHECK_FOGLI_MANUALI%>" value="true" onclick="checkSelection()" />
        </td>
        </tr>
        
        <tr>
        <td class="l" colspan="2">
          <font class="label">Provvedimenti Privi di Fogli Complementari</font>
        </td>
        <td class="l">
          <input type="checkbox" name="<%=ICostantiStatistiche.CHECK_PROVV_PRIVI_FC%>" value="true" onclick="checkSelection()" />
        </td>
        </tr>
        
         <!--   tr>
        <td class="l" colspan="2">
          <font class="label">Provvedimenti con Fogli Complementari compilati ma non trasmessi</font>
        </td>
        <td class="l">
          <input type="checkbox" name="<%=ICostantiStatistiche.CHECK_FOGLI_NON_TRASMESSI%>" value="true" onclick="checkSelection()" />
        </td>
        </tr>
        
         <tr>
        <td class="l" colspan="2">
          <font class="label">Fogli complementari trasmessi con errore</font>
        </td>
        <td class="l">
          <input type="checkbox" name="<%=ICostantiStatistiche.CHECK_FOGLI_ERRORE%>" value="true" onclick="checkSelection()" />
        </td>
        </tr -->
        
        <tr>
        <td class="l" colspan="2">
          <font class="label">Fogli complementari annullati</font>
        </td>
        <td class="l">
          <input type="checkbox" name="<%=ICostantiStatistiche.CHECK_FOGLI_ANNULLATI%>" value="true" onclick="checkSelection()" />
        </td>
        </tr>
    </table>
   </div>     
      
   </div>
        <br />
        <table>
        <tr>
         <td class="label">
           <input class="bottone" type="submit" name="RICERCA" value="Ricerca"> <%-- onClick="Validazione(); --%>
         </td>
        </tr>
    </table>
    <br>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.statistiche.action.ActRicercaFogliComplementari">
    <input type="HIDDEN" name="<%=ICostantiStatistiche.CAMPO_MODALITA_RICERCA%>" value="">
  </FORM>
     </div>
 </div>


  </body>
  <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("RicercaStatisticheFC");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
  
  
</html>