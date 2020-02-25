<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare" %>

<jsp:useBean id="elencoSezioni" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
    	function init()
    	{
    		document.f.<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>.focus();
    		onClickCheckBox();
    	}
			// Evento su onclick del check box della sezione.
    	function onClickCheckBox()
    	{
				if( document.f.flagTutti.checked == true )
					document.f.<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>.disabled=true;
				else
					document.f.<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>.disabled=false;
    	}

			// Controllo formale della data di inizio validità.
			function checkDataInizioValidita()
			{
				var ritorno = true;
	    	var dataInizioValidita = 
	    		document.f.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value +'/'+ 
	    		document.f.<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value +'/'+ 
	    		document.f.<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;

				if (dataInizioValidita.length > 2)
			  {    											
	    		if (!ControllaData(dataInizioValidita))
	      	{
	      		alert('Data inizio validità non corretta.');
	      		document.f.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
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

  <title>[S.I.E.S.] - Filtro Giudici Popolari</title>
  </head>

  <body class="corpo" onLoad="Javascript:init();">
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f" target=listaGiudiciPopolari>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" 
					 value="siap.sige.giudicepopolare.action.ActRicercaGiudicePopolareLista">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
		<input type="HIDDEN" name="idfieldnum" value="<%=request.getParameter("idfieldnum")%>">
   	<table>
    	<tr>
      	<td class=LBG>
      		<font class="campo">Filtra la lista  per : </font>
      	</td>
    	</tr>
    	<tr>
      	<td class="l">Cognome</td>
      	<td class="l">
      		<input value="" type="text" name="<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>" >
      	</td>
			</tr>
	<%// Presenta elenco sezioni se esse esistono. 
		if( elencoSezioni.length() != 0 ) { %>
			<tr>
      	<td class="l">Sezione</td>
      		<td class="l">
        		<select title="Sezione" name="<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>">
        			<%=elencoSezioni%>
        		</select>
      		</td>
					<!-- 20081030 - Commentato su cambio rquisiti funzionali. Eliminare quando confermato 
					&nbsp;
					<td class="l">oppure Tutti</td>
          <td class="l">
						<input type="checkbox" name="flagTutti" value=1 onClick="Javascript:onClickCheckBox();">
					</td> 
					-->
    	</tr>
	<%}%>

			<tr>
     		<td class="l">Dalla Data Inizio Validità</td>
       		<td class="l"> 
           	<input type="text" size="2" maxlength="2" 
								 name="<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>" 
								 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
								 onBlur="javascript:value=FillDM(value)">
           	/
           	<input type="text" size="2" maxlength="2" 
								 name="<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_INIZIO_VALIDITA%>" 
								 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
								 onBlur="javascript:value=FillDM(value)">
           	/
           	<input type="text" size="4" maxlength="4" 
								 name="<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>" 
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