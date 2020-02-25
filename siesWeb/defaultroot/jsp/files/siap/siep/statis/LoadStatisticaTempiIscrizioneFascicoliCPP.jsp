<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.siep.statis.action.ICostantiStatis" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>

<jsp:useBean id="sysdate" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaUffAcc" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficiomod" 	scope="request" class="siap.sico.ufficio.model.UfficioModel" />

<!-- 		LoadStatisticaTempiIscrizioneFascicoliCPP		 -->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript" type="text/javascript">
    
    function Verify()
    {	
    	
	    if (document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value.length==1)
	       document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value='0'+document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value;
	    if (document.TempiCPP.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value.length==1)
	       document.TempiCPP.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value='0'+document.TempiCPP.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value;
	
	    if (document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value.length==1)
	       document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value='0'+document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value;
	    if (document.TempiCPP.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value.length==1)
	        document.TempiCPP.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value='0'+document.TempiCPP.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value;
	
	    var data_inizio=document.TempiCPP.<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value+'/'+document.TempiCPP.<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>.value+'/'+document.TempiCPP.<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>.value;
	    var data_fine=document.TempiCPP.<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>.value+'/'+document.TempiCPP.<%=ICostantiStatis.CAMPO_MESE_FINALE%>.value+'/'+document.TempiCPP.<%=ICostantiStatis.CAMPO_ANNO_FINALE%>.value;
		var data_sistema="<%=sysdate%>";
		
// Controllo Form Senza Nessun parametro di ricerca selezionato
		if(document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value.length==0 &&  
			document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value.length==0 &&
			document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.value.length==0 && 
			document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.value.length==0 && 
			!ControllaData(data_inizio) && 
			!ControllaData(data_fine)  )	
		{
			alert('Nessun Parametro di Ricerca è presente nella Form ');
			document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
	    	return false;
		}
	
		var tipodiStat=0;
		
// Controllo Parametri Data Iniziale/Data Finale Solo se NON sono VUOTE
		if(data_fine=='//' && data_inizio=='//')
		{
			// Date Vuote
		}	
		else
		{	
			if(!ControllaData(data_inizio))
	      	{
	        	alert('Data di inizio non valida - '+data_inizio);
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
	        	return false;
	      	}
			
	      	if(!ControllaData(data_fine))
	      	{
	        	alert('Data di fine non valida - '+data_fine);
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.focus();
	        	return false;
	      	}
	
	      	if(!CompareDate(data_inizio, data_sistema))
	      	{
	        	alert('La Data di inizio non può essere superiore alla Data odierna - '+data_inizio);
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
	        	return false;
	      	}
	
	      	if(!CompareDate(data_fine, data_sistema))
	      	{
	        	alert('La Data di fine non può essere superiore alla Data odierna');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.focus();
	        	return false;
	      	}
	      	  
	      	if(!CompareDate(data_inizio, data_fine))
	      	{
	        	alert('La Data di fine non può essere inferiore alla Data di inizio');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.focus();
	        	return false;
	      	}
	      	
	      	tipodiStat ++;
		}
	
// Controllo Parametri Solo Anno Iniziale/Solo Anno Finale Solo se NON sono VUOTI
		if(document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value.length==0 &&  
			document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value.length==0 )
		{
			// Anni Iniziale e Finale VUOTI
		}	
		else
		{
			if(document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value.length==0)
			{
				alert('Inserire Anno Iniziale');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.focus();
	        	return false;
			}
			
			if(document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value.length==0)
			{
				alert('Inserire Anno Finale');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.focus();
	        	return false;
			}
			
			if(document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>.value < document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.value )
			{
				alert('Anno Iniziale NON può essere Minore di Anno Finale');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>.focus();
	        	return false;
			}	
			
			tipodiStat ++;
		}
		
// Controllo parametri Semestre
		if(document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.value.length>0)
		{
			if(!document.TempiCPP.Semestre[0].checked && 
				!document.TempiCPP.Semestre[1].checked	)
			{
				alert('Selezionare Primo e/o Secondo Semestre');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>.focus();
	        	return false;				
			}	
			
			tipodiStat ++;
		}	

// Controllo parametri Trimestre
		if(document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.value.length>0)
		{
			// Selezionare almeno 1 trimestre
			if(!document.TempiCPP.Trimestre[0].checked && 
				!document.TempiCPP.Trimestre[1].checked &&
				!document.TempiCPP.Trimestre[2].checked &&
				!document.TempiCPP.Trimestre[3].checked)
			{
				alert('Selezionare il Trimestre \n (oppure i Trimestri purchè CONSECUTIVI)');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;				
			}
			
			// selezionare più trimestri, ma CONSECUTIVI
			if(document.TempiCPP.Trimestre[0].checked && 
				!document.TempiCPP.Trimestre[1].checked &&
				document.TempiCPP.Trimestre[2].checked )
			{
				alert('Selezionare SOLO Trimestri CONSECUTIVI)');
	        	document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;				
			}
			
			if(document.TempiCPP.Trimestre[0].checked && 
			   document.TempiCPP.Trimestre[3].checked && 
			   ( !document.TempiCPP.Trimestre[1].checked || 
				 !document.TempiCPP.Trimestre[2].checked ) )
			{
				alert('Selezionare SOLO Trimestri CONSECUTIVI)');
		       	document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;				
			}
			
			if(document.TempiCPP.Trimestre[1].checked && 
			   document.TempiCPP.Trimestre[3].checked && 
			  !document.TempiCPP.Trimestre[2].checked  )
			{
				alert('Selezionare SOLO Trimestri CONSECUTIVI)');
		       	document.TempiCPP.<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>.focus();
	        	return false;				
			}
		
			tipodiStat ++;
		}

//Controllo Form con + di un parametro di ricerca selezionato
		if(tipodiStat > 1)
		{
			alert('Scegliere solo un tipo di ricerca\n1) Data Inizio / Data Fine;\n2) Anno Inizio / Anno Fine;\n3) Anno Trimeste;\n4) Anno Semestre;' );
			document.TempiCPP.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.focus();
        	return false;		
		}	
		
// Scelta della classe Procedimento da elaborare
			if(!document.TempiCPP.tipo[0].checked && 
				!document.TempiCPP.tipo[1].checked &&
				!document.TempiCPP.tipo[2].checked &&
				!document.TempiCPP.tipo[3].checked &&
				!document.TempiCPP.tipo[4].checked && 
				!document.TempiCPP.tipo[5].checked &&
				!document.TempiCPP.tipo[6].checked &&
				!document.TempiCPP.tipo[7].checked )
			{
				alert('Inserire il check su Classe Procedimenti');
	        	document.TempiCPP.tipo[0].blur();
	        	return false;						
			}	
    	    	      
        return true;    	
    
    }	// CHIUDE Verify()

    function enableBtn()
    {
    	document.TempiCPP.btnconf.disabled = false;
    }
    
    function SelezionaClasse()
    {
    	if(document.TempiCPP.tipo[0].checked)			// Classe I - Pena Detentiva
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
    	}
    	else if(document.TempiCPP.tipo[1].checked)		// Classe II - Pena Pecuniaria
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
    	}
    	else if(document.TempiCPP.tipo[2].checked)		// Classe III  - Pena Sospesa
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
    	}
    	else if(document.TempiCPP.tipo[3].checked)		// Classe IV - Misure Sicurezza
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
    	}
    	else if(document.TempiCPP.tipo[4].checked)		// Classe V - Persona Giuridica
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
    	}
    	else if(document.TempiCPP.tipo[5].checked)		// Classe VI - Giudice di pace
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
    	}
    	else if(document.TempiCPP.tipo[6].checked)		// Classe VII - Conversione Pene Pecuniarie
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActCreaStatisticaTempiIscrizioneFascicoliCPP";
    	}
    	else if(document.TempiCPP.tipo[7].checked)		// Registro Istanze
    	{
    		document.TempiCPP.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
    	}
    		
    }
        
    </script>
  </head>

  <BODY class="corpo">
  <table>
  	<tr>
      	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>
          <font class="campo">STATISTICHE UFFICIO - TEMPI ISCRIZIONI PROCEDIMENTI</font>
        </td>
     </tr>
   </table>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="TempiCPP">
		<!--  input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActCreaTempiIscrizioni" -->
<table width="80%">
	<tr><td class="Titolo" colspan="6" >Intervallo di Tempo da verificare</td></tr>
	<tr>
        <td class="l" width="20%"> Data Iniziale </td>
      	<td class="L" width="20%">
        	<input type="text" name="<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" name="<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;
	   	</td>
 	    <td class="l" ><center>Data Finale</center> </td>
      	<td class="L" colspan=4>
        	<input type="text" name="<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" name="<%=ICostantiStatis.CAMPO_MESE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">&nbsp;
      	</td>
   	</tr>
</table>
<table width="80%">   	
   	<tr><td class="l" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; OPPURE  </td></tr>
	<tr>
        <td class="l" width="20%"> Anno Iniziale </td>
      	<td class="L" width="5%">
        	<input type="text" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;
	   	</td>
 	    <td class="l" > Anno Finale </td>
      	<td class="L" colspan=4>
        	<input type="text" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">&nbsp;
      	</td>
   </tr>
   <tr><td class="l"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; OPPURE  </td></tr>
   </tr>
   <tr>
        <td class="l" width="20%" >Per Semestre:   Anno </td>
      	<td class="L" width="5%">
        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_SEMESTRE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;
	   	</td>
	   	<td class="l" width="10%"><center>Primo</center> </td>
	   	<td class="c" > <input type="checkbox" name="Semestre" value="PRIMO" /> </td>
	   	<td>&nbsp;&nbsp;</td>
	   	<td class="l"><center>Secondo</center> </td>
	   	<td class="c" > <input type="checkbox" name="Semestre" value="SECONDO" /> </td>
   </tr>
   <tr><td class="l"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; OPPURE  </td></tr>
   <tr>
        <td class="l" width="20%" >Per Trimestre:   Anno </td>
      	<td class="L" width="5%">
        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;
	   	</td>
	   	<td class="l" width="10%"><center>Primo</center> </td>
	   	<td class="c" > <input type="checkbox" name="Trimestre" value="PRIMO" /> </td>
	   	<td>&nbsp;&nbsp;</td>
	   	<td class="l"><center>Secondo</center> </td>
	   	<td class="c" > <input type="checkbox" name="Trimestre" value="SECONDO" /> </td>
	   	<td>&nbsp;&nbsp;</td>
	   	<td class="l"><center>Terzo</center> </td>
	   	<td class="c" > <input type="checkbox" name="Trimestre" value="TERZO" /> </td>
	   	<td>&nbsp;&nbsp;</td>
	   	<td class="l"><center>Quarto</center> </td>
	   	<td class="c" > <input type="checkbox" name="Trimestre" value="QUARTO" /> </td>
   </tr>
</table>    
<br>
<table style="width: 95%;" >
	<tr><td class="Titolo" colspan="15" > selezionare la Classe Procedimenti da Elaborare</td></tr>
     <tr>
       <td class="l">Classe I - Pena Detentiva</td>
       <td class="l"><input type="radio" name="tipo" value="1" onClick=""></td>
       <td>&nbsp;</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
       <td class="l">Classe II - Pena Pecuniaria</td>
       <td class="l"><input type="radio" name="tipo" value="2" onClick=""></td>
       <td>&nbsp;</td><td>&nbsp;</td>
       <td class="l">Classe III - Pena Sospesa</td>
       <td class="l"><input type="radio" name="tipo" value="3" onClick=""></td>
       <td>&nbsp;</td><td>&nbsp;</td>
       <td class="l">Classe IV - Misure Sicurezza</td>
       <td class="l"><input type="radio" name="tipo" value="4" onClick=""></td>
     </tr>
     <tr>
       <td class="l">Classe V - Persona Giuridica</td>
       <td class="l"><input type="radio" name="tipo" value="5" onClick=""></td>
       <td>&nbsp;</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
       <td class="l">Classe VI - Giudice di Pace</td>
       <td class="l"><input type="radio" name="tipo" value="6" onClick=""></td>
	   <td>&nbsp;</td><td>&nbsp;</td>
       <td class="l">Classe VII - Conversione Pena Pecuniaria</td>
       <td class="l"><input type="radio" name="tipo" value="7" onClick="" CHECKED></td>
       <td>&nbsp;</td><td>&nbsp;</td>
       <td class="l">Registro Istanze</td>
       <td class="l"><input type="radio" name="tipo" value="8" onClick=""></td>
     </tr>
  </table>
 <br>
<table style="width: 95%;">
     <tr>
     	<td class="L">
     		<font class="label" style="color:green; font-size: 10pt" > N.B. Se non si seleziona un Ufficio Accorpato, la funzione effettua l'elaborazione per Ufficio Accorpante    
     		</font>
     	</td>
     </tr>
     <tr>	
     	<td class="L">
     	<font class="label" style="color:green; font-size: 10pt" >&nbsp;&nbsp;&nbsp;&nbsp;Se si desidera l'elaborazione relativa al solo Ufficio Accorpato al momento della chiusura, selezionarlo nella successiva combo
     	</font>
     	</td>
     </tr>
</table> 
<br>
<table>
	<tr>
		<td class="l"> Ufficio Accorpato </td>
     	 <% if(ListaUffAcc.size()==0)
     	 	{ %>
     	 	  	 <td>
	     			<select name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" >
 	   					<option value="-" selected="selected">-  </option>
 	   				</select>
 	   			</td>		
 	   	<%	} 
 	   		else
 	   		{ %>
 	   			<td>
	     			<select name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" >
	     				<option value="-" selected="selected">-  </option>
	     	<%	Iterator itx = ListaUffAcc.iterator();
  				while ( itx.hasNext())
  				{
  					UfficioAccorpatoModel lUff = (UfficioAccorpatoModel)itx.next();%>
  						
   						<option value="<%=lUff.getCodUfficio()%>"><%=lUff.getDescrizione()%></option>    					
  					
 	   	<%		} %>
	  				</select>
	  			</td>
	  	<%	} %>		
	 </tr>
</table>	  		
<br>

<table>
    <tr>
      <td>
      	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="" >
        <input type=submit value="Conferma" class=bottone name="btnconf" onclick="javascript:SelezionaClasse();">
      </td>
	</tr>
    <tr><td>&nbsp;</td></tr>
</table>
</FORM>
<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("TempiCPP");
  	
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","numeric","Il campo Giorno Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","maxlen=2","La lunghezza massima per il giorno Data Iniziale è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","gt=1", "Giorno Data iniziale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","lt=31", "Giorno Data iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","numeric","Il campo Giorno Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","maxlen=2","La lunghezza massima per il giorno Data Finale è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","gt=1", "Giorno Data finale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","lt=31", "Giorno Data finale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>","numeric","Il campo Mese Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>","maxlen=2","La lunghezza massima per il mese Data Iniziale è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>","gt=1", "Mese Data iniziale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>","lt=12", "Mese Data iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_FINALE%>","numeric","Il campo Mese Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_FINALE%>","maxlen=2","La lunghezza massima per il mese Data Finale è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_FINALE%>","gt=1", "Mese Data finale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_FINALE%>","lt=12", "Mese Data finale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_INIZIALE%>","numeric","Il campo Anno Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per anno Data Iniziale è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per anno Data Iniziale è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_INIZIALE%>","gt=1900", "Anno Data iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","numeric","Il campo Anno Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","maxlen=4","La lunghezza massima per anno Data Finale è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza minima per anno Data Finale è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","gt=1900", "Anno Data finale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","lt=3000", "Anno Data finale non valido");

frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>","numeric","Il campo solo Anno Iniziale può avere solo caratteri numerici");
frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>","numeric","Il campo solo Anno Finale può avere solo caratteri numerici");
frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_SEMESTRE%>","numeric","Il campo Anno Semestre può avere solo caratteri numerici");
frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_TRIMESTRE%>","numeric","Il campo Anno Trimestre può avere solo caratteri numerici");
  	
  frmvalidator.setAddnlValidationFunction("Verify");
  
 </script>
</body>
</html>