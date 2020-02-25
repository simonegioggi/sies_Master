<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Decreti in corso di Definizione</title>
    
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript">
    
    function selezionaTipologiaProvvedimentoNoTutti()
	{
   		//alert("selezionaTipologiaProvvedimentoNoTutti");	        
        if(document.f.tipoNotifica[1].checked || document.f.tipoNotifica[2].checked || 
           document.f.tipoNotifica[3].checked || document.f.tipoNotifica[4].checked || 
           document.f.tipoNotifica[5].checked || document.f.tipoNotifica[6].checked)
		{	      
        	document.f.tipoNotifica[0].checked=false;							        	      
		} 	        
	   	return true;
	}  
    
    function selezionaTipologiaProvvedimentoTutti()
	{
    	//alert("selezionaTipologiaProvvedimentoTutti");
        if(document.f.tipoNotifica[0].checked)
		{	      
        	document.f.tipoNotifica[1].checked=false;
			document.f.tipoNotifica[2].checked=false;
			document.f.tipoNotifica[3].checked=false;
			document.f.tipoNotifica[4].checked=false;
		    document.f.tipoNotifica[5].checked=false;
			document.f.tipoNotifica[6].checked=false;					        	      
		} 	
	   	return true;
	}  


	 function Verify()
	 {

	    	  if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
		           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
		      {
			        alert("Valorizzare Anno inizio ricerca");
			        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
			        return false;
		      }
		      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length == 0)
		           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
		      {
			        alert("Valorizzare Numero inizio ricerca");
			        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.focus();
			        return false;
		      }
		      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
		           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
		      {
			        alert("Valorizzare Anno di fine ricerca");
			        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
			        return false;
		      }
		      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length == 0)
		           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
		      {
			        alert("Valorizzare Numero di fine ricerca");
			        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.focus();
			        return false;
		      }
		      
		     
		      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
			           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
			           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
			           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
			      {
				        if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
				        {
					          alert("Anno inizio maggiore Anno fine");
					          document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
					          return false;
				        }
				        else if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
				        {
				
					          if(parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value) < parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value))
					          {
						            alert("Numero inizio maggiore Numero fine");
						            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.focus();
						            return false;
					          }
				        }
			      }
			      
		      	if (document.f.GiornoEmissioneIniziale.value.length==1)
	       			document.f.GiornoEmissioneIniziale.value='0'+document.f.GiornoEmissioneIniziale.value;
	    		if (document.f.MeseEmissioneIniziale.value.length==1)
	       			document.f.MeseEmissioneIniziale.value='0'+document.f.MeseEmissioneIniziale.value;

	    		if (document.f.GiornoEmissioneFinale.value.length==1)
	       			document.f.GiornoEmissioneFinale.value='0'+document.f.GiornoEmissioneFinale.value;
	    		if (document.f.MeseEmissioneFinale.value.length==1)
	        		document.f.MeseEmissioneFinale.value='0'+document.f.MeseEmissioneFinale.value;

	    		var data_inizio=document.f.GiornoEmissioneIniziale.value+'/'+document.f.MeseEmissioneIniziale.value+'/'+document.f.AnnoEmissioneIniziale.value;
	    		var data_fine=document.f.GiornoEmissioneFinale.value+'/'+document.f.MeseEmissioneFinale.value+'/'+document.f.AnnoEmissioneFinale.value;

		      if(!ControllaDataPassaVuota(data_inizio))
		      {
		        alert('Data Emissione iniziale non valida');
		        document.f.GiornoEmissioneIniziale.focus();
		        return false;
		      }
		      if(!ControllaDataPassaVuota(data_fine))
		      {
		        alert('Data Emissione finale non valida');
		        document.f.GiornoEmissioneFinale.focus();
		        return false;
		      }


	        if ( (  document.f.GiornoEmissioneIniziale.value.length!=0 
	                && document.f.MeseEmissioneIniziale.value.length!=0
	                && document.f.AnnoEmissioneIniziale.value.length!=0 ) &&
				 (  document.f.GiornoEmissioneFinale.value.length!=0 
	                && document.f.MeseEmissioneFinale.value.length!=0
	                && document.f.AnnoEmissioneFinale.value.length!=0 ) )
	        {
			      if(!CompareDate(data_inizio,data_fine))
			      {
				        alert('La Data Emissione finale non può essere inferiore alla data Emissione iniziale');
				        document.f.GiornoEmissioneIniziale.focus();
				        return false;
			      }
		   
	        } 
	        
	        
		      if(   !document.f.tipoNotifica[0].checked 
			     && !document.f.tipoNotifica[1].checked
			     && !document.f.tipoNotifica[2].checked
			     && !document.f.tipoNotifica[3].checked
			     && !document.f.tipoNotifica[4].checked
			     && !document.f.tipoNotifica[5].checked
			     && !document.f.tipoNotifica[6].checked)
			  {	      	
					      	alert('Selezionare almeno una Tipologia Stato');
					      	document.f.tipoNotifica[0].focus();
					      	return false;
			  }
		      
				      
			  if( document.f.tipoNotifica[0].checked && 
			    (document.f.tipoNotifica[1].checked ||
			     document.f.tipoNotifica[2].checked ||
			     document.f.tipoNotifica[3].checked ||
			     document.f.tipoNotifica[4].checked ||
			     document.f.tipoNotifica[5].checked ||
			     document.f.tipoNotifica[6].checked) )
			  {	      	
			 		      	alert('Scegliere o TUTTI o uno o più stati');
			 		      	document.f.tipoNotifica[1].focus();
			 		      	return false;
			  }

			  
    	checkNewProg();

    	mostraAttesa('Attendere: elaborazione in corso');

    	return true;
	 }  <%// Chiude Verify  %>

	    function checkNewProg(){
	        var ufficioAccorpato = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
	        var parts=ufficioAccorpato.split("-"); 
	        var offSetInt = parseInt(parts[0]);

            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = "";
	        var numProgIni = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value;
	        if (numProgIni){
		        var numProgIniInt = parseInt(numProgIni);
		        var newProgIniInt = numProgIniInt + offSetInt;
	            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = newProgIniInt;
	            }

            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value = "";
	        var numProgFin = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value;
	        if (numProgFin){
		        var numProgFinInt = parseInt(numProgFin);
		        var newProgFinInt = numProgFinInt + offSetInt;
	            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value = newProgFinInt;
	            }

	        return true;
	    }

		<%	// accetta una stringa di testo non html da mostrare %>
	function mostraAttesa(testo) 
	{
	    var puntini = 0;
	    testoIntrattenimento = prendiElementoDaId("testo-temporaneo");
		
	    animaTesto = function() 
	    {
		      var testoAggiunto = "";
		
		      for(var a = 0; a < puntini; a++)
		        testoAggiunto += ".";
		
		      testoIntrattenimento.nodeValue = testo + testoAggiunto;
		
		      if(puntini < 4)
		        puntini++;
		      else
		        puntini = 0;
		
		      setTimeout(animaTesto, 300);
		}
		
		if(testoIntrattenimento.firstChild) 
		{
			    animaTesto = function(){};
			    testoIntrattenimento.removeChild(testoIntrattenimento.firstChild);
		}
		else 
		{
			    testoIntrattenimento = document.createTextNode(testo);
			
			    prendiElementoDaId("testo-temporaneo").appendChild(testoIntrattenimento);
			
			    animaTesto();
		}
		
	}   <% // Chiude MostraAttesa   %>
		
	function prendiElementoDaId(id_elemento) 
	{
		var elemento;
		if(document.getElementById)
			elemento = document.getElementById(id_elemento);
		else
			elemento = document.all[id_elemento];
	
		return elemento;
	
	}	
		</script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaProcTrasmessiL78del2013">
	  <table>
	    <tr>
	    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimenti Trasmessi - Gestione Decreto Legge 78/2013</font></td>
	    </tr>
	  </table>
	  <br>
	  <table width="80%" cellpadding=2 cellspacing=2>
	    <tr><td class="Titolo" colspan="4">Intervallo Procedimenti</td>
	    <tr>
	      <td class="L" >
	        <font class="label">
	          Anno/Numero Iniziale
	        </font>
	      </td>
	      <td class="l">
	        <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
	        /
	        <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>" maxlength="14" size="14">
	        <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" value="">
	      </td>
	      <td class="L">
	        <font class="label">
	          Anno/Numero Finale
	        </font>
	      </td>
	      <td class="l">
	        <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
	        /
	        <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>" maxlength="14" size="14">
	        <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" value="">
	      </td>
		   </tr>

	    <tr>
	      <td class="L" >
	        <font class="label">
	          Ufficio Accorpato
	        </font>
	      </td>
	      <td class="l">
         	<select name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>">
         	<option value="0" >-</option>
  <%
  Iterator it = elencoUfficiAccorpati.iterator();
  int indice = 0;
  while (it.hasNext())
  {
  	UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
  %>
         	<option value="<%=ua.getIncrProgressivo()%>-<%=ua.getCodUfficio()%>" ><%=ua.getDescrizione()%></option>
  <%
	indice ++;
  }
  %>
         	</select>
	      </td>
	      <td class="L">
	      </td>
	      <td class="l">
	      </td>
		   </tr>

		   <tr><td>&nbsp;</td></tr>
	     <tr><td class="Titolo" colspan="4">Intervallo Date Emissione Decreto</td>
	     <tr>
	       <td class="l">
	        <font class="label">
	          Data Emissione Iniziale
	        </font>
	      </td>
	      <td class="l" >
	        <input type="text" title="Giorno Emissione Iniziale" name="GiornoEmissioneIniziale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        -
	        <input type="text" title="Mese Emissione Iniziale" name="MeseEmissioneIniziale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        -
	        <input type="text" title="Anno Emissione Iniziale" name="AnnoEmissioneIniziale" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
				</td>
	 			<td class="l">
	        <font class="label">
	          Data Emissione Finale
	        </font>
	      </td>
	      <td class="l">
	        <input type="text" title="Giorno Emissione Finale" name="GiornoEmissioneFinale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        -
	        <input type="text" title="Mese Emissione Finale" name="MeseEmissioneFinale" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        -
	        <input type="text" title="Anno Emissione Finale" name="AnnoEmissioneFinale" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
	      </td>     
	    </tr>
		</table>
		<br>
  	<table width="80%" cellspacing=2 cellpadding=2>
    <tr>
    	<td class="Titolo" colspan="2">Tipologia Provvedimento</td>
    </tr>
    	
	<tr>
	  <td width="15%" class="l">
	     Tutti  <input type="checkbox" checked name="tipoNotifica" onClick="selezionaTipologiaProvvedimentoTutti();" value="Tutti">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  </td>
	  <td width="50%" class="l">    
	      Comunicazione - ex art.656 co.4 Bis c.p.p. (D.L. 78/2013) -----------------------------------------------------> <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaProvvedimentoNoTutti();" value="ComunicaBis">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  </td>
	</tr>
	
	<tr>
		<td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="50%" class="l">
		     Comunicazione - ex art.656 co.4 Bis c.p.p. Arresti Domiciliari (D.L. 78/2013) -------------------------------> <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaProvvedimentoNoTutti();" value="ComunicaQua">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	<tr>	
		<td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="50%" class="l">     
		     Ordine Esecuzione - Per la Carcerazione Detenuto per questa causa (D.L. 78/2013) -----------------------> <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaProvvedimentoNoTutti();" value="OrdineEsec">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>	
	<tr>	
		<td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="50%" class="l">     
		     Comunicazione - ex art.656 co.4 Bis c.p.p. Arresti Domiciliari ex art. 89 dpr 309/90  (D.L. 78/2013) ---> <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaProvvedimentoNoTutti();" value="ComunicazioneArresti">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>	
	<tr>	
		<td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="50%" class="l">     
		     Comunicazione - ex art.656 co.4 Bis c.p.p. Permanenza in casa (D.L. 78/2013) ----------------------------> <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaProvvedimentoNoTutti();" value="ComunicazionePermanenza">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>	
	<tr>	
		<td width="15%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="50%" class="l">     
		     Comunicazione - ex art.656 co.4 Bis c.p.p. Collocamento in comunità (D.L. 78/2013) --------------------> <input type="checkbox" name="tipoNotifica" onClick="selezionaTipologiaProvvedimentoNoTutti();" value="ComunicazioneCollocamento">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
	
	<tr>
		<td width="15%" class="l">
	  		Solo Attivi  <input type="checkbox" name="AttivoSiNo" value="Attivi">
	  	</td>

	</tr> 
</table>
<table width="80%">
     <tr>
     	<td width="30%">
     		<font class="label" style="color:green">&nbsp;(N.B.) Selezionando 'Solo Attivi' il sistema estrae i procedimenti
     														con STATO corrispondente al tipo di provvedimento scelto&nbsp;
     		</font>
     	</td>
     </tr>
</table>     	
<table width="80%" cellpadding=2 cellspacing=2>
	<tr>
		<td width="15%" class="l">
	  		NON Attivi  <input type="checkbox" name="NonAttivi" value="NoAttivi">
	  	</td>
	  	<td width="50%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr> 			
</table>
<table width="80%">
  
     <tr><td>&nbsp;</td></tr>
      <tr>
        <td colspan="2">
          <INPUT class="bottone" type="submit"  name="RICERCA" value="Ricerca" onClick="javascript:return Verify();">
        </td>
      </tr>
  </table>
  </FORM>
  <div> 
	  <table width="100%" >
	    <tr>
	      <td width="35%">
	        &nbsp;    
	      </td>   
	      <td width="30%" class="lrosso">
	        <p id="testo-temporaneo"></p>     
	      </td> 
	      <td width="35%">
	        &nbsp;
	      </td>     
	    </tr>
	  </table>
	</div>
	<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("f");
  <%  //   frmvalidator.setAddnlValidationFunction("Verify");  %>
  </script>

  </body>
</html>