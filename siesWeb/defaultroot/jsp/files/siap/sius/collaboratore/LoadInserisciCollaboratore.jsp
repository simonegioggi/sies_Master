<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.web.Action"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.collaboratore.model.CollaboratoreModel"  %>
<%@ page import="siap.sius.collaboratore.action.ICostantiCollaboratore"  %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<jsp:useBean id="modalita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<jsp:useBean id="collaboratore" scope="request" class="siap.sius.collaboratore.model.CollaboratoreModel"/>


<html>
<head>
<title>[S.I.A.P.] - Inserimento/Modifica Collaboratore di Giustizia </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript">

function Verify()
{
	// Inizializzazione delle date di riferimento
   // Data della attivazione della legge sul Collaboratore di Giustizia
   var data_minima = "01/01/1991";
   // Blocco su inserimento date insensate
   var data_massima = "31/12/2050";

   var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
   // Letture delle date da controllare
   var data_inizio = document.LoadInserisciCollaboratore.<%=ICostantiCollaboratore.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciCollaboratore.<%=ICostantiCollaboratore.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciCollaboratore.<%=ICostantiCollaboratore.CAMPO_ANNO_DATA_INIZIO%>.value;
   var data_fine = document.LoadInserisciCollaboratore.<%=ICostantiCollaboratore.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciCollaboratore.<%=ICostantiCollaboratore.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciCollaboratore.<%=ICostantiCollaboratore.CAMPO_ANNO_DATA_FINE%>.value;
   
   if (!document.LoadInserisciCollaboratore.<%=ICostantiCollaboratore.CHK_DATA_INIZIO_NULLABLE%>.checked)
    {
	   if (data_inizio.length <= 2)
   	   {
   			alert('Inserire la Data Inizio oppure Spuntare Opzione per Inserimento in Assenza di Data Inizio!');
    		return false;
   		}
  		
   }
           
   // La data Inizio e' adesso opzionale (18/02/2009)
   if (data_inizio.length > 2)
   {
   		if (! ControllaData(data_inizio))
   		{
     		alert('Data Inizio non valida!');
     		return false;
   		}
   	   // data di sistema >= Data Inizio
   	   if ( !CompareDate( data_inizio, data_sistema ) )
   	   {
   	     alert('Data Inizio non può essere successiva alla data odierna');
   	     return false;
   	   }
   	   if (ControllaData(data_minima))
       {
       	// data Inizio >= Data Minima
       	if ( !CompareDate( data_minima, data_inizio ) )
       	{
         	alert("Data Inizio non può precedere la data " + data_minima);
         	return false;
       	}
   	   }
   	}
   
   // La data Fine e' opzionale
   if (data_fine.length > 2)
   {
   	  if (! ControllaData(data_fine))
   	  {
     	alert('Data Fine non valida!');
     	return false;
   	  }
   	  if (ControllaData(data_massima))
      {
       // data Data Massima >= Data fine
       if ( !CompareDate( data_fine, data_massima ) )
       {
         alert("Data Fine non può essere successiva a  " + data_massima);
         return false;
       }
      }
       // data Inizio < Data Fine
      if ( CompareDate( data_fine, data_inizio  ) )
      {
        alert("Data Fine deve essere successiva alla Data Inizio " + data_inizio);
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
      //PosizioneMaterialeFascModel lModel = new PosizioneMaterialeFascModel();
      String lAzione = new String();
      if( modalita.equals("I") )
      {
           lAzione = "siap.sius.collaboratore.action.ActInserisciCollaboratore";
    %>
       <font class="campo">Inserimento Collaboratore di Giustizia</font>
    <%
      }
      else if( modalita.equals("M") )
      {
       lAzione = "siap.sius.collaboratore.action.ActModificaCollaboratore";
    %>
       <font class="campo">Modifica Collaboratore di Giustizia</font>
     <%}%>
    </td>
   	<!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>   
   </tr>
 </table>
    <br>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciCollaboratore">
   <input type="hidden" value="<%=lAzione%>" name="<%=IWebConstants.ACTION_FIELD%>">
 <%if( modalita.equals("M")) { %>
	
	<input type="hidden" value="<%=collaboratore.getIdCollaboratore().toString()%>" name="<%=ICostantiCollaboratore.CAMPO_ID_COLLABORATORE%>">

<% } else  if( modalita.equals("I")) { %>
	<input type="hidden" value="<%=collaboratore.getIdFascicoloSius().toString()%>" name="<%=ICostantiCollaboratore.CAMPO_ID_FASCICOLO_SIUS%>">
	<input type="hidden" value="<%=collaboratore.getCodUfficio()%>" name="<%=ICostantiCollaboratore.CAMPO_COD_UFFICIO%>">
 <%} %>

 <table cellspacing=4 cellpadding=4>
    <tr>
      <td class="l">Data Inizio<font class="ob"></font></td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(collaboratore.getDataInizio(), "dd"), "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiCollaboratore.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(collaboratore.getDataInizio(), "MM"), "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiCollaboratore.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(collaboratore.getDataInizio(),"yyyy"), "")%>" type="text" size="4" maxlength="4" name="<%=ICostantiCollaboratore.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
 
    <tr>
      <td class="l">Data Fine<font class="ob"></font></td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(collaboratore.getDataFine(), "dd"), "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiCollaboratore.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(collaboratore.getDataFine(), "MM"), "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiCollaboratore.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(collaboratore.getDataFine(),"yyyy"), "")%>" type="text" size="4" maxlength="4" name="<%=ICostantiCollaboratore.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
 </table>
<BR>
 <table width="70%">
      <tr> 
      	<td class="l" >Spuntare questa opzione per inserire il collaboratore anche in assenza di Data Inizio<font class="ob"></font></td> 
      	<td class="l">
      	<input type="checkbox" name="<%=ICostantiCollaboratore.CHK_DATA_INIZIO_NULLABLE%>" value="S"> 
      </td>
      </tr>
 </table>
	
<BR>
<input type="submit" class=bottone name="go" value="Conferma">
</form>
 <script language="JavaScript" type="text/javascript"> var frmvalidator  = new Validator("LoadInserisciCollaboratore");
  frmvalidator.setAddnlValidationFunction("Verify");
    // frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_GIORNO_DATA_INIZIO%>","req","Il campo Giorno  della Data Inizio è obbligatorio"); 
    frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    // frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_MESE_DATA_INIZIO%>","req","Il campo Mese  della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_MESE_DATA_INIZIO%>","numeric");
    // frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_ANNO_DATA_INIZIO%>","req","Il campo Anno della Data Inizio è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_ANNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_GIORNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_MESE_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiCollaboratore.CAMPO_ANNO_DATA_FINE%>","numeric");
 </script>
 </body>
</html>