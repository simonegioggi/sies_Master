<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="java.util.Date"%>

<jsp:useBean id="sentenza"         scope="session"	class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="fascicolo"        scope="session"	class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="titoloCompetenza" scope="request" class="java.lang.String" />
<jsp:useBean id="modalita"         scope="request" class="java.lang.String" />
<jsp:useBean id="titolo"         scope="request" class="java.lang.String" />
<jsp:useBean id="isSentenza"     scope="session" class="java.lang.String" />


<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<%
boolean titCompetenza = false;
if (titoloCompetenza != null && titoloCompetenza.equalsIgnoreCase("S"))
		titCompetenza = true;

Date dataIrrevocabilita = null;
if (titolo.equals(""))
    titolo = "-";

String lAction = "siap.sige.sentenza.action.ActAssegnazione";
String lchecked = "";

if (modalita != null && modalita.equalsIgnoreCase("M") ) 
{
	dataIrrevocabilita = ((SentenzaSigeModel) sentenza).getDataIrrevocabilita();
	titolo = "Modifica Assegnazione Titolo Esecutivo";
	lAction = "siap.sige.sentenza.action.ActModificaAssegnazione";
	if ( ((SentenzaSigeModel)sentenza).getFlagCompetenza() != null && ((SentenzaSigeModel)sentenza).getFlagCompetenza().equalsIgnoreCase("S"))
		lchecked = "checked";
}
else 
	if (fascicolo != null && fascicolo.getDataIrrevocabilita() != null)
{
	dataIrrevocabilita = fascicolo.getDataIrrevocabilita();
	titolo = "Assegnazione Sentenza del Procedimento SIEP a Procedimento SIGE";
}
//  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
//else if (sentenza != null )
//{
//	dataIrrevocabilita = sentenza.getDataIrrevocabilita();
//	titolo = "Assegnazione Sentenza a Procedimento SIGE";
//}


String labelEsecutivoIrrevocabilita="Eescutivo il";
if (isSentenza.equalsIgnoreCase("true"))
	labelEsecutivoIrrevocabilita="Data Irrevocabilità";
%>
<html>
<head>
<title>[S.I.A.P.] - Assegnazione Sentenza Sige </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<script language="JavaScript">
  function Verify()
  {
  	var ritorno = true;
    //Data Irrevocabilità
    var d4=document.AssegnaSentenza.<%=ICostantiFasSigeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.AssegnaSentenza.<%=ICostantiFasSigeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.AssegnaSentenza.<%=ICostantiFasSigeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
    //if (! ControllaData(d4))
    //{
    //  alert('Data irrevocabità non valida');
    //  return false;
    //}
    var d2 = "<%=StringUtils.toStringJSP( DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd/MM/yyyy"))%>";
  	 
  	if (ControllaData(d2))
    {
    	if (ControllaData(d4))
			{
      	if (! CompareDate(d2,d4))
      	{
       		alert('La Data <%=labelEsecutivoIrrevocabilita%> deve essere successiva alla data della sentenza');
       		return false;
      	}
      }
    }
  	ritorno = controlloCompetenza();
 	return ritorno;
  }
  
  function controlloCompetenza()
  {
  <% if (titCompetenza) { %>
    	if (document.AssegnaSentenza.<%=ICostantiFasSigeSentenza.CAMPO_FLAG_COMPETENZA%>.checked )
    	{
    	if (!window.confirm('Confermi la sostituzione del Titolo di Competenza ?'))
			return false;
    	}
    <% }%>
	return true;
  }
  
    function VisualizzaWarning()
  	{
   	node=document.getElementById("Warning");
    node.style.visibility='visible';
  	}
  	
    function NascondiWarning()
  	{
   	node=document.getElementById("Warning");
    node.style.visibility='hidden';
  	}
  	
  function checkCompetenza()
  {
    <% if (titCompetenza) { %>
  
  	// alert("click competenza");
    	if (document.AssegnaSentenza.<%=ICostantiFasSigeSentenza.CAMPO_FLAG_COMPETENZA%>.checked )
  			VisualizzaWarning();
    	else
    		NascondiWarning();
      <% }%>
    		
	return true;
  }
  	
  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
  {
    desktop = 
        window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
  }
  
 </script>
</head>

<body class="corpo" onLoad="Javascript:checkCompetenza();" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=titolo%></font>
 	 </td>     
   		<td class="LBG">
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   </tr>
 </table>

 <br>
	 <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
	 <jsp:include page="<%=ICostantiFasSigeSentenza.PG_INCLUDE_SENTENZA%>">
	       <jsp:param name="isSentenzaStranieraDelibata" value="<%=isSentenza %>"/>
	 </jsp:include>
 <br>

 <FORM name="AssegnaSentenza" >         
 
 <table  width="85%">
 
 <tr> 
 <table width="85%">
<!-- Modifica del 24/11/2016 MEV_15_S4 
	 Nel caso di "Cumulo/Ordinanza/Decreto Archiviazione" la Data Irrevocabilità non viene visualizzata (Richiesta di Michele)
 -->
<% if (sentenza != null && sentenza.getCodTipoProvvedimento() != null && (sentenza.getCodTipoProvvedimento().equals("13")
	   || sentenza.getCodTipoProvvedimento().equals("03") || sentenza.getCodTipoProvvedimento().equals("63"))	
	  ){ %>
		<input Title="Giorno <%=labelEsecutivoIrrevocabilita%>" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd")) %>" name="<%= ICostantiFasSigeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>"	maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> 
		<input Title="Mese <%=labelEsecutivoIrrevocabilita%>" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sentenza.getDataProvvedimento(),"MM")) %>" name="<%= ICostantiFasSigeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> 
		<input Title="Anno <%=labelEsecutivoIrrevocabilita%>" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sentenza.getDataProvvedimento(),"yyyy")) %>" name="<%= ICostantiFasSigeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

<% } else { %>
  	<tr>
		<td class="l"><%=labelEsecutivoIrrevocabilita%></td>
		<td class="L" colspan=3>
			<input Title="Giorno <%=labelEsecutivoIrrevocabilita%>" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataIrrevocabilita,"dd")) %>" name="<%= ICostantiFasSigeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>"	maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Mese <%=labelEsecutivoIrrevocabilita%>" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataIrrevocabilita,"MM")) %>" name="<%= ICostantiFasSigeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Anno <%=labelEsecutivoIrrevocabilita%>" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataIrrevocabilita,"yyyy")) %>" name="<%= ICostantiFasSigeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('AssegnaSentenza','<%=ICostantiFasSigeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>','<%=ICostantiFasSigeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>','<%=ICostantiFasSigeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>

		</td>
	</tr>
<% } %>
  <tr> 
  	<td class="l" >Titolo che definisce la competenza del Procedimento </td>
  	<td class="L"> 
  		<input type="checkbox" name="<%=ICostantiFasSigeSentenza.CAMPO_FLAG_COMPETENZA%>"  value="" onClick="Javascript:checkCompetenza();" <%=lchecked%> > 
  	</td>
  </tr>
 
 </table>
 
  <div id="Warning" style="position:relative;  top: 0; left: 0;   visibility:hidden; ">  
  <table width="85%">
     <tr>
     	<td>       
         <font class="cRosso"> Al Procedimento già risulta assegnato il Titolo di Competenza, vistare questa opzione comporterà il cambiamento del Titolo di Competenza.</font>
     	</td>
     </tr>
     </table> 
    </div>
  
  <tr>
     <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>
 </table>

 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >

 <%
 if (fascicolo != null && fascicolo.getIdFascicoloSiep() != null)
{
 %>
     <input type="HIDDEN" name="<%=ICostantiFasSigeSentenza.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=fascicolo.getIdFascicoloSiep().toString()%>" >
 <%} 
 if (sentenza != null && sentenza.getIdSentenza() != null)
 {
%>
 	<input type="HIDDEN" name="<%=ICostantiFasSigeSentenza.CAMPO_SEN_ID_SENTENZA%>" value="<%=sentenza.getIdSentenza().toString()%>" >
<%	 
 }
 if (sentenza != null && sentenza instanceof SentenzaSigeModel && ((SentenzaSigeModel)sentenza).getIdFasSigeSentenza() != null) 
 {
%>
 	<input type="HIDDEN" name="<%=ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA%>" value="<%=((SentenzaSigeModel)sentenza).getIdFasSigeSentenza().toString()%>" >
<%	 
 }
 %>
 
  </FORM>
  
   <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("AssegnaSentenza");
    // frmvalidator.addValidation("<%= ICostantiFasSigeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","req", "Il campo Giorno Data Esecutivo il è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFasSigeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","numeric");
    // frmvalidator.addValidation("<%= ICostantiFasSigeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","req", "Il campo Mese Data Esecutivo il è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFasSigeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","numeric");
    // frmvalidator.addValidation("<%= ICostantiFasSigeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","req", "Il campo Anno Data Esecutivo il è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFasSigeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFasSigeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","minlen=4","La lunghezza del campo Anno Data Esecutivo il deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

 </body>
</html>