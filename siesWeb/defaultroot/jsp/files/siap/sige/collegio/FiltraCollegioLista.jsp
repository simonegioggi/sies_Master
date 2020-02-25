<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants"%>

<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>

<jsp:useBean id="elencoSezioni"	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoCodiciCollegi"	scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript">
  	function init()
		{
			//onClickCheckBox();
		}
		// Controllo formale della data di inizio validità.
		function checkDataInizioValidita()
		{
			var ritorno = true;
    	var dataInizioValidita = 
    		document.f.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value +'/'+ 
    		document.f.<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value +'/'+ 
    		document.f.<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;

			if (dataInizioValidita.length > 2)
		  {    											
    		if (!ControllaData(dataInizioValidita))
      	{
      		alert('Data inizio validità non corretta.');
      		document.f.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
        	ritorno = false;
      	}
		  }
			return ritorno;
		}

		function  Verify()
		{
			// Controllo formale data inizio validità.
			if( !checkDataInizioValidita() )
    	return false;	

	    return true;
		}  
    </script>

    <title>[S.I.E.S.] - Filtro Collegi</title>
  </head>

  <body class="corpo">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listaCollegi>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" 
					 value="siap.sige.collegio.action.ActRicercaCollegioLista">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
   <table>
    <tr>
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%--td class="LBG">
      	<a href="Javascript:window.print();">
      	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td --%>
      <td class=LBG>
      	<font class="campo">Filtra la lista per : </font>
      </td>
    </tr>
    <tr>
      <td class="l">Collegio</td>
   		<td class="l">
   			<select title="Numero Collegi" name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>">
     			<%=elencoCodiciCollegi%>
     		</select>
   		</td>
		<%// Presenta elenco sezioni se esse esistono. 
		if( elencoSezioni.length() != 0 ) { %>
 			<td class="l">Sezione</td>
   		<td class="l">
   			<select title="Sezioni" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>">
     			<%=elencoSezioni%>
     		</select>
   		</td>
	<% } %>
		</tr>	
			<tr>
     		<td class="l">da Data Inizio Validità</td>
       		<td class="l"> 
           	<input type="text" size="2" maxlength="2" 
								 name="<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>" 
								 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
								 onBlur="javascript:value=FillDM(value)">/
           	<input type="text" size="2" maxlength="2" 
								 name="<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>" 
								 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
								 onBlur="javascript:value=FillDM(value)">/
           	<input type="text" size="4" maxlength="4" 
								 name="<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>" 
								 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
								 onBlur="javascript:value=FillYear(value)">
         	</td>
       </tr>				
			<tr>
				<td class="l">Data fine validità non valorizzata</td>
         <td class="l">
					<input type=checkbox name="flagDataFine@Null" value=1>
				</td>
			</tr>
		<tr>	
      <td>
      	<input onclick="Javascript:return Verify();" type="submit" name="go" value="Filtra >>">
      </td>
    </tr>

  </table>
</form>
  </body>
</html>