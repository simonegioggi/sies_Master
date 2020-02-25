<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="uffici" scope="request" class="java.util.Vector"/>
<jsp:useBean id="parametro" scope="request" class="siap.siep.parametro.model.ParametroModel"/>

<html>
<head>
<%  
	if("I".equals(modalita))
	{
%>
		<title>[S.I.E.S.] - Inserimento Periodo Feriale </title>
<%
	}
	else
	{
%>
		<title>[S.I.E.S.] - Modifica Periodo Feriale </title>
<%
	}
%>
	
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	
	<script language="JavaScript" src="/html/conferma.js"></script>
	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
	<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%> ></script>
	<script language="JavaScript">
	
		function Verify()
		{
	  	//Controllo Data Inizio Periodo Feriale
	  	if (document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE %>.value.length==1)
	    	document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE %>.value="0"+document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE %>.value;
	   	if (document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE %>.value.length==1)
	    	document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE %>.value="0"+document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE %>.value;
	   	
	   	var data_inizio_periodo_feriale = document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE %>.value+"/"+document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE %>.value+"/"+document.f.<%= ICostantiUfficio.CAMPO_ANNO_DATA_INIZIO_PERIODO_FERIALE %>.value;
	
	   	if ( !ControllaData(data_inizio_periodo_feriale) )
	   	{
	    	alert ("Data Inizio Periodo Feriale Non Valido!");
	    	return false;
	   	} 

	  	//Controllo Data Fine Periodo Feriale
	  	if (document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE %>.value.length==1)
	    	document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE %>.value="0"+document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE %>.value;
	   	if (document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_FINE_PERIODO_FERIALE %>.value.length==1)
	    	document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_FINE_PERIODO_FERIALE %>.value="0"+document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_FINE_PERIODO_FERIALE %>.value;
	   	
	   	var data_fine_periodo_feriale = document.f.<%= ICostantiUfficio.CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE %>.value+"/"+document.f.<%= ICostantiUfficio.CAMPO_MESE_DATA_FINE_PERIODO_FERIALE %>.value+"/"+document.f.<%= ICostantiUfficio.CAMPO_ANNO_DATA_FINE_PERIODO_FERIALE %>.value;
	
	   	if ( !ControllaData(data_fine_periodo_feriale) )
	   	{
	    	alert ("Data Fine Periodo Feriale Non Valido!");
	    	return false;
	   	} 

  		return true;
		}
	</script>
	</head>
	
	<body class="corpo">
		<table>
	  	<tr>
	  		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	      <td class="LBG">
	      	<font class="label">Funzione :</font>&nbsp;
<%  
						if("I".equals(modalita))
						{
%>
	        		<font class="campo">Inserimento Periodo Feriale</font>
<%
						}
						else
						{
%>
	        		<font class="campo">Modifica Periodo Feriale</font>
<%
						}
%>
	      </td>
	    </tr>
	 	</table>
	
		<FORM method=post action="<%= IWebConstants.PG_MAIN %>" name=f>
<%  
			if("I".equals(modalita))
			{
%>
				<input type="HIDDEN" value="siap.sico.ufficio.action.ActInserisciPeriodoFeriale" name="<%=IWebConstants.ACTION_FIELD%>">
<%
			}
			else
			{
%>
				<input type="HIDDEN" value="siap.sico.ufficio.action.ActModificaPeriodoFeriale" name="<%=IWebConstants.ACTION_FIELD%>">
<%
			}
%>			
			<input type="HIDDEN" value="<%= DateUtils.getSysDate("yyyy") %>" name="<%= ICostantiUfficio.CAMPO_ANNO_CORRENTE %>"> 
	
			<table cellspacing=2 cellpadding=2>
				<tr>
	  	   	<td class="int">Ufficio</td>
	        <td class="l">
		        <select name="<%= ICostantiUfficio.CAMPO_COD_UFFICIO %>" class=small>
<%
						String codiceUfficio;
						String descrUfficio;
						for (int i = 0; i<uffici.size(); i++)
						{
						  codiceUfficio = ((UfficioModel)uffici.get(i)).getCodUfficio();
						  descrUfficio = ((UfficioModel)uffici.get(i)).getDescrTipoUfficio()+ " di " + ((UfficioModel)uffici.get(i)).getDescrComune() + " ("+((UfficioModel)uffici.get(i)).getCodProvincia() + ") - " + StringUtils.toStringJSP(((UfficioModel)uffici.get(i)).getIndirizzo());
%>
							<option value="<%= codiceUfficio %>"><%= descrUfficio %></option>
<%
						}
%>
		        </select> 
		      </td>
				</tr>
				<tr>
	      	<td class="int">Data Inizio Periodo</td>
				  <td class="l">
					  <input size=2 maxlength=2 type="text" name="<%=ICostantiUfficio.CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE%>" value="<%= StringUtils.toStringJSP( DateUtils.getDateToString(parametro.getDataInizioValidita(), "dd")) %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
					  /
		        <input size=2 maxlength=2 type="text" name="<%=ICostantiUfficio.CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE%>" value="<%= StringUtils.toStringJSP( DateUtils.getDateToString(parametro.getDataInizioValidita(), "MM")) %>" onFocus="javascript:textboxSelect(this)" onBlur="javascript:value=FillDM(value)">
		        /
		        <input disabled size=4 maxlength=4 type="text" name="<%=ICostantiUfficio.CAMPO_ANNO_DATA_INIZIO_PERIODO_FERIALE%>" value="<%= DateUtils.getSysDate("yyyy") %>">
					</td>
			  </tr>
				<tr>
	      	<td class="int">Data Fine Periodo</td>
				  <td class="l">
					  <input size=2 maxlength=2 type="text" name="<%=ICostantiUfficio.CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE%>" value="<%= StringUtils.toStringJSP( DateUtils.getDateToString(parametro.getDataFineValidita(), "dd")) %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
					  /
		        <input size=2 maxlength=2 type="text" name="<%=ICostantiUfficio.CAMPO_MESE_DATA_FINE_PERIODO_FERIALE%>" value="<%= StringUtils.toStringJSP( DateUtils.getDateToString(parametro.getDataFineValidita(), "MM")) %>" onFocus="javascript:textboxSelect(this)" onBlur="javascript:value=FillDM(value)">
		        /
		        <input disabled size=4 maxlength=4 type="text" name="<%=ICostantiUfficio.CAMPO_ANNO_DATA_FINE_PERIODO_FERIALE%>" value="<%= DateUtils.getSysDate("yyyy") %>">
					</td>
			  </tr>
				<tr>
	      	<td class="l" colspan=2><input type="submit" class=bottone name="go" value="Conferma"></td>
				</tr>
			</table>
	  </FORM>
  	<script language="JavaScript" type="text/javascript">
			var frmvalidator  = new Validator("f");
			
    	frmvalidator.setAddnlValidationFunction("Verify");
  	</script>
	</body>
</html>