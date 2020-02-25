<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.giudicepopolare.model.GiudicePopolareModel"%>
<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>

<jsp:useBean id="elencoSezioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="codFunzione"   scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Giudice Popolare </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
   	<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript">
      function init()
      {
      	document.LoadRicercaGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>.focus();
      	//onClickCheckBox();
      }

			// Evento su onclick del check box della sezione.
      /*
      function onClickCheckBox()
      {
				if( document.LoadRicercaGiudicePopolare.flagTutti.checked == true )
					document.LoadRicercaGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>.disabled=true;
				else
					document.LoadRicercaGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>.disabled=false;
      }
			*/
			// Controllo formale della data di inizio validità.
			function checkDataInizioValidita()
			{
				var ritorno = true;
	    	var dataInizioValidita = 
	    		document.LoadRicercaGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value +'/'+ 
	    		document.LoadRicercaGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value +'/'+ 
	    		document.LoadRicercaGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;

				if (dataInizioValidita.length > 2)
			  {    											
	    		if (!ControllaData(dataInizioValidita))
	      	{
	      		alert('Data inizio validità non corretta.');
	      		document.LoadRicercaGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
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

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>
  </head>
  <body class="corpo" onload="Javascript:init();">
    <table>
    	<tr>
				<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
					</a>
				</td>
    		<td class="LBG">
					<font class="label">Funzione :</font>&nbsp;&nbsp;
    			<font class="campo">Ricerca Giudice Popolare</font>
    		</td>
    	</tr>
    </table>

    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaGiudicePopolare">
      <table cellspacing="4" cellpadding="4">
        <tr>
          <td class="l">Cognome</td>
          <td class="l">
						<input type="text" name="<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>">
					</td>
        </tr>
        
				<tr>
          <td class="l">Nome</td>
          <td class="l">
						<input type="text" name="<%=ICostantiGiudicePopolare.CAMPO_NOME%>">
					</td>
        </tr>
         
				<tr>
      		<td class="l">Sezione</td>
      		<td class="l">
        		<select title="Sezione" name="<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>">
        			<%=elencoSezioni%>
        		</select>
      		</td>
					<!--  &nbsp;
					<td class="l">oppure Tutti</td>
          <td class="l">
						<input type="checkbox" name="flagTutti" value=1 onClick="Javascript:onClickCheckBox();">
					</td>
					-->
    		</tr>
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
				<!-- MEV 15 -->
				<a href="javascript:calendario('LoadRicercaGiudicePopolare','<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>','<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_INIZIO_VALIDITA%>','<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>');">
	       			<img src="/images/calendario.gif" border=0>
	        	</a>
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
            <input class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>

        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" 
							 value="siap.sige.giudicepopolare.action.ActRicercaGiudicePopolare">

     </form>
     <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadRicercaGiudicePopolare");
        frmvalidator.setAddnlValidationFunction("Verify");
     </script>
   </body>
</html>