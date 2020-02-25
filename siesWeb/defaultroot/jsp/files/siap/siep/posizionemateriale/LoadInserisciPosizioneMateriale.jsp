<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.posizionemateriale.model.PosizioneMaterialeModel"%>
<%@ page import="siap.siep.posizionemateriale.action.ICostantiPosizioneMateriale"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="modalita"       			scope="request" class="java.lang.String"/>
<jsp:useBean id="posizionemateriale"  scope="request" class="siap.siep.posizionemateriale.model.PosizioneMaterialeModel"/>
<jsp:useBean id="uffici" 							scope="request" class="java.util.Vector"/>

<html>
<head>
<title>[S.I.A.P.] - GestionePosizioneMateriale </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%> ></script>
<script language="JavaScript">
	function Verify()
	{
<%
		if( modalita.equals("M"))
		{
%>
	   	if (document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value.length==1)
	       document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value="0"+document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value;
	    if (document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_MESE_DATA_FINE_VALIDITA %>.value.length==1)
	       document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_MESE_DATA_FINE_VALIDITA %>.value="0"+document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_MESE_DATA_FINE_VALIDITA %>.value;
	   	
	   	var data_to_verify = document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_GIORNO_DATA_FINE_VALIDITA %>.value+"/"+document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_MESE_DATA_FINE_VALIDITA %>.value+"/"+document.LoadInserisciPosizioneMateriale.<%= ICostantiPosizioneMateriale.CAMPO_ANNO_DATA_FINE_VALIDITA %>.value;
	
	   	if (!ControllaDataPassaVuota(data_to_verify))
	   	{
	    	alert ("Data Fine Validità Non Valida!");
	      
	      return false;
	   	}
<% 
		}
%>
	  return true;
	}
</script>

</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;

 <%
   String lReadOnly = "";
   PosizioneMaterialeModel lPosizioneMateriale = new PosizioneMaterialeModel();
   String lAzione = new String();
   if( modalita.equals("I") )
   {
     lAzione = "siap.siep.posizionemateriale.action.ActInserisciPosizioneMateriale";
%>
  	 <font class="campo">Inserimento Posizione Materiale</font>
<%
   }
   else if( modalita.equals("M") )
   {
     lReadOnly = "readonly";
     lAzione = "siap.siep.posizionemateriale.action.ActModificaPosizioneMateriale";
     lPosizioneMateriale = posizionemateriale;
%>
   	 <font class="campo">Modifica Posizione Materiale</font>
<%
	 }
   else if( modalita.equals("R") )
   {
     lAzione = "siap.siep.posizionemateriale.action.ActRicercaPosizioneMateriale";
     lPosizioneMateriale = posizionemateriale;
%>
     <font class="campo">Ricerca Posizione Materiale</font>
<%
	 }
%>
    </td>
    </tr>
  </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciPosizioneMateriale">
  	<input type="hidden" value="<%=lAzione%>" name="<%=IWebConstants.ACTION_FIELD%>">
  	<table cellspacing=4 cellpadding=4>
    	<tr>
      	<td class="int">Codice Posizione Materiale</td>
      	<td class="l"><input value="<%=lPosizioneMateriale.getCodPosizioneMateriale() %>" type="text" name="<%=ICostantiPosizioneMateriale.CAMPO_COD_POSIZIONE_MATERIALE %>" size= 3; maxlength=3;  <%=lReadOnly%> ></td>
      </tr>
 			<tr>
  			<td class="int">Descrizione Posizione Materiale</td>
  			<td class="l"><input value="<%=lPosizioneMateriale.getDescPosizioneMateriale() %>" type="text" name="<%= ICostantiPosizioneMateriale.CAMPO_DESC_POSIZIONE_MATERIALE %>"   maxlength=100; ></td>
 			</tr>
 			<tr>
 				<td class="int">Ufficio</td>
 				<td class="l">
  				<select name="<%=ICostantiPosizioneMateriale.CAMPO_COD_UFFICIO %>"  class="small">
<%
						String cod;
    				String desc;
   					if( modalita.equals("M"))
   					{
       				cod=lPosizioneMateriale.getCodUfficio();
       				desc=lPosizioneMateriale.getDescrUfficio();
%>
							<option value="<%=cod%>"><%=desc%></option>
<%
   					}
   					else
   					{
     					for (int i=0;i<uffici.size();i++)
     					{
       					cod=((UfficioModel)uffici.get(i)).getCodUfficio();
       					desc=((UfficioModel)uffici.get(i)).getDescrTipoUfficio()+ " di " +((UfficioModel)uffici.get(i)).getDescrComune()+ " ("+((UfficioModel)uffici.get(i)).getCodProvincia() + ")";
%>
								<option value="<%=cod%>"><%=desc%></option>
<%  
							} // end For
					/*
				      if( modalita.equals("R") )
     					{
     			*/
%>
      					<!--<option value="" selected> - </option>-->
<%  
 					//}
   					}
%>
  				</select>
 				</td>
 			</tr>
<%
		if( modalita.equals("M"))
		{
%>
			<tr>
      	<td class="int">Data Fine Validità</td>
			  <td class="l">
			  	<input size=2 maxlength=2 type="text" name="<%= ICostantiPosizioneMateriale.CAMPO_GIORNO_DATA_FINE_VALIDITA %>" value="<%= StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneMateriale.getDataFineValidita(), "dd") ) %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
          <input size=2 maxlength=2 type="text" name="<%= ICostantiPosizioneMateriale.CAMPO_MESE_DATA_FINE_VALIDITA %>" value="<%= StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneMateriale.getDataFineValidita(), "MM") ) %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
          <input size=4 maxlength=4 type="text" name="<%= ICostantiPosizioneMateriale.CAMPO_ANNO_DATA_FINE_VALIDITA %>" value="<%= StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneMateriale.getDataFineValidita(), "yyyy") ) %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			  </td>
			</tr>
<%
		}
%>
		</table>
		<BR>
<%
		if( modalita.equals("R"))
		{
%>
	 		<table cellspacing=2 cellpadding=2>
				<tr>
		  		<td class="l">
	        <table cellspacing=2 cellpadding=2>
		      	<tr>
							<td class="label" >
			  				Visualizza Solo Posizioni Materiali Valide
							</td>
							<td class="label">
			  				<input type=radio name="<%= ICostantiPosizioneMateriale.CAMPO_FILTRO_DATA %>" value=0 CHECKED>
							</td>
		      	</tr>
		      	<tr>
							<td class="label">
			  				Visualizza Solo Posizioni Materiali Non Valide
							</td>
							<td class="label">
			  				<input type=radio name="<%= ICostantiPosizioneMateriale.CAMPO_FILTRO_DATA %>" value=1>
							</td>
		      	</tr>
		      	<tr>
							<td class="label">
			  				Visualizza Tutte le Posizioni Materiali
							</td>
							<td class="label">
			  				<input type=radio name="<%= ICostantiPosizioneMateriale.CAMPO_FILTRO_DATA %>" value=2>
							</td>
		      	</tr>
		    	</table>
		  	</td>
			</tr>
	  </table>
	  <BR>
<%
		}
%>
	<input type="submit" class=bottone name="go" value="Conferma">
	</form>

		<script language="JavaScript" type="text/javascript" >
			var frmvalidator  = new Validator("LoadInserisciPosizioneMateriale");
  		frmvalidator.setAddnlValidationFunction("Verify");
<%
   		if( modalita.equals("I") || modalita.equals("M"))
   		{
%>
  			frmvalidator.addValidation("<%=ICostantiPosizioneMateriale.CAMPO_COD_POSIZIONE_MATERIALE%>","req","Il campo Codice è obbligatorio");
  			frmvalidator.addValidation("<%=ICostantiPosizioneMateriale.CAMPO_DESC_POSIZIONE_MATERIALE%>","req","Il campo Descrizione è obbligatorio");
<% 
			} 
%>
		</script>
	</body>
</html>