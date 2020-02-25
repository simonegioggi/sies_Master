<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>


<html>
	<head>
  	<title> [S.I.E.S.] - Ricerca  Notifiche/Comunicazioni SIUS - </title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    	
    	function Verify()
    	{
    		var ritorno = true;
			var data_iniziale = document.LoadRicercaNotificaSius.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+document.LoadRicercaNotificaSius.<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+document.LoadRicercaNotificaSius.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO%>.value;
    		var data_finale = document.LoadRicercaNotificaSius.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO2%>.value+'/'+document.LoadRicercaNotificaSius.<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO2%>.value+'/'+document.LoadRicercaNotificaSius.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO2%>.value;
    		var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

     		if (! ControllaData(data_iniziale))
      		{
        		alert('Data iniziale non valida!');
        		ritorno =  false;
      		}
     		else if (! ControllaData(data_finale))
      		{
        		alert('Data finale non valida!');
        		ritorno =  false;
      		}
      		// Controllo data finale >= Data iniziale .
      		else if( !CompareDate( data_iniziale, data_finale ) )
      		{
        		alert('Data finale < Data Iniziale!');
        		ritorno =  false;
      		}
      		return ritorno;
 		}
 		
     	function Init()
    	{	
    		document.LoadRicercaNotificaSius.<%=ICostantiNotifica.CAMPO_COD_TIPO_NOTIFICA%>[0].checked = true;
    		VisualizzaNotifiche();
    	}
    	
  	</script>
  	
   <script language="JavaScript">  		

 	    function VisualizzaNotifiche()
    	{
    	   node=document.getElementById("ComunicazioneDiv");
           node.style.visibility='hidden';
           node.disabled = true;
           node=document.getElementById("NotificaDiv");
           node.style.visibility='visible';
           node.disabled = false;   
           document.LoadRicercaNotificaSius.<%=ICostantiNotifica.TIPO_DESTINATARIO%>[5].checked = true;
           document.LoadRicercaNotificaSius.<%=ICostantiNotifica.TIPO_PROVVEDIMENTO%>[0].checked = true;
        }
   	</script>
   	
   <script language="JavaScript">  	
      
  	    function VisualizzaComunicazioni()
    	{
           node=document.getElementById("NotificaDiv");
           node.style.visibility='hidden';
           node.disabled = true;
 	       node=document.getElementById("ComunicazioneDiv");
           node.style.visibility='visible'; 
           node.disabled = false;     
           document.LoadRicercaNotificaSius.<%=ICostantiNotifica.TIPO_DESTINATARIO%>[11].checked = true;
           document.LoadRicercaNotificaSius.<%=ICostantiNotifica.TIPO_PROVVEDIMENTO%>[3].checked = true;
        }
   	</script>
   	
   <script language="JavaScript">  		
          
        function CambiaAtto (tipoAtto)
        {
        	if( tipoAtto == "N")
        		VisualizzaNotifiche();
        	else
        		VisualizzaComunicazioni();   		
        }
   	</script>
	
	</head>

	<body class="corpo" onLoad="Javascript:return Init();" >
  	<FORM method="POST" name="LoadRicercaNotificaSius" action="<%=IWebConstants.PG_MAIN%>">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActRicercaNotificaSius">

  	<table>
    	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class="label">Funzione: </font>&nbsp;<font class="campo">Ricerca Notifiche/Comunicazioni</font></td>
    	</tr>
  	</table>

  	<br>
  	
  	 	<table width="96%">
      	<tr>
          <td class="l" width="37%" >
            Selezionare tipo atto (*)</td>          
          <td class="l" width="61%" >
            Notifiche <input type="radio" name="<%=ICostantiNotifica.CAMPO_COD_TIPO_NOTIFICA%>" value="N" onclick="Javascript:return VisualizzaNotifiche()"  >&nbsp;&nbsp;&nbsp;&nbsp; 
			Comunicazioni <input type="radio" name="<%=ICostantiNotifica.CAMPO_COD_TIPO_NOTIFICA%>" value="C"  onclick="Javascript:return VisualizzaComunicazioni();"   >&nbsp;</td>
          </tr>
    	</table>
<br>  	
  	
      <table width=96%>
        <tr>
		<td class="Titolo"  colspan ="4" >Intervallo Date di Inserimento Notifiche/Comunicazioni (*)</td>
        </tr>
        <tr>
          <td class="c" width="10%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="c" width="10%"  >
            <font class="label">Data Finale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO2%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO2%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO2%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
      </table>

	 <br>
  <div id="comune" style="position: relative; top: 0; left: 0;   visibility:hidden; " >     
  <div id="NotificaDiv" style="position:relative;  top: 0; left: 0;   visibility:visible; " >  
         <jsp:include page="<%=ICostantiNotifica.PG_INCLUDE_NOTIFICHE_SIUS%>">
          <jsp:param name="TipoAtto" value="Notifiche"/>
        </jsp:include>
</div>
    	
    <div id="ComunicazioneDiv" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
         <jsp:include page="<%=ICostantiNotifica.PG_INCLUDE_NOTIFICHE_SIUS%>">
          <jsp:param name="TipoAtto" value="Comunicazioni"/>
        </jsp:include>
</div>
 </div>

 <br>    	
   <div id="resto" style="position: relative; top: 0; left: 0;   visibility:visible; " >     
      <!--
      	Gestione radio buttons per tipo di ordinamento :
        	-> A-P ( Ordinamento per Numero Procedimento ) default
         	-> C-N ( Ordinamento per Cognome/Nome )
       -->
       
       
        <table >
      	<tr>
    		<td class="l">
      		 Ordina per Numero Procedimento &nbsp;
      		</td>
      		<td class="l">
          	<input type="radio" name="<%=ICostantiNotifica.TIPO_ORDINAMENTO%>" value="<%=ICostantiNotifica.PROCEDIMENTO%>" checked ></td>
      		</td>
			</tr>
			<tr>
    		<td class="l">
      		Ordina per Cognome Nome &nbsp;
      		</td>
      		<td class="l">
          	<input type="radio" name="<%=ICostantiNotifica.TIPO_ORDINAMENTO%>" value="<%=ICostantiNotifica.SOGGETTO%>"></td>
      		</tr>
			<tr>
    		<td class="l">
      		Ordina per orario inserimento &nbsp;
      		</td>
      		<td class="l">
          	<input type="radio" name="<%=ICostantiNotifica.TIPO_ORDINAMENTO%>" value="<%=ICostantiNotifica.DATA%>"></td>
      		</td>
      	</tr>
		</table>

<br> 
			<table>
    		<tr>
      		<td class="label">
        		<input class="bottone" type="submit" name="RICERCA" value="Ricerca">
        	</td>
      	</tr>
    	</table>
    	</div>
	</form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRicercaNotificaSius");

    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO2%>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO2%>","numeric");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INSERIMENTO2%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");

    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO2%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO2%>","numeric");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INSERIMENTO2%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");

    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO2%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO2%>","numeric");
    frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INSERIMENTO2%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>
</html>