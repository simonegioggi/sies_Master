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
		  function Verify()
		  {
	      // Non è possibile specificare solo il numero o solo l'anno
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
	      {
	        alert("Valorizzare Anno inizio ricerca");
	        return false;
	      }
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length == 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
	      {
	        alert("Valorizzare Numero inizio ricerca");
	        return false;
	      }
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
	      {
	        alert("Valorizzare Anno di fine ricerca");
	        return false;
	      }
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length == 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
	      {
	        alert("Valorizzare Numero di fine ricerca");
	        return false;
	      }
	      
	      // Non è possibile cercare per numero/anno fine minore di numero/anno inizio
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
	      {
	        if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
	        {
	          alert("Anno inizio maggiore Anno fine");
	          return false;
	        }
	        else if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
	        {
	
	          if(parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value) < parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value))
	          {
	            alert("Numero inizio maggiore Numero fine");
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
	        return false;
	      }
	      if(!ControllaDataPassaVuota(data_fine))
	      {
	        alert('Data Emissione finale non valida');
	        return false;
	      }

        //----------------------------------------------------------------------
        // Data Fine > data inizio
        //----------------------------------------------------------------------
        if (   (   document.f.GiornoEmissioneIniziale.value.length!=0 
                && document.f.MeseEmissioneIniziale.value.length!=0
                && document.f.AnnoEmissioneIniziale.value.length!=0 
               )
            && (   document.f.GiornoEmissioneFinale.value.length!=0 
                && document.f.MeseEmissioneFinale.value.length!=0
                && document.f.AnnoEmissioneFinale.value.length!=0
               )
           )
        {
		      if(!CompareDate(data_inizio,data_fine))
		      {
		        alert('La Data Emissione finale non può essere inferiore alla data Emissione iniziale');
		        return false;
		      }
	      }
	      
	      // Almeno una Tipologia Stato deve essere selezionata
	      if(   !document.f.tipoNotifica[0].checked 
	         && !document.f.tipoNotifica[1].checked
	         && !document.f.tipoNotifica[2].checked
	         && !document.f.tipoNotifica[3].checked
	         && !document.f.tipoNotifica[4].checked
	         && !document.f.tipoNotifica[5].checked
	         && !document.f.tipoNotifica[6].checked
	        )
	      {	      	
	      	alert('Selezionare almeno una Tipologia Stato');
	      	
	      	return false;
	      }
	            
    		mostraAttesa('Attendere: elaborazione in corso');
    	
      	return true;
			}
			
			// accetta una stringa di testo non html da mostrare
			function mostraAttesa(testo) 
			{
		    var puntini = 0,
		    testoIntrattenimento = prendiElementoDaId("testo-temporaneo"),
		
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
			}  
		
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
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaDecretiSospInDefinizione">
	  <table>
	    <tr>
	    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Ricerca Decreti in corso di Definizione</font></td>
	    </tr>
	  </table>
	  <br>
	  <table cellpadding=2 cellspacing=2>
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
	      <td></td>
	      <td></td>
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
  	<table cellspacing=2 cellpadding=2>
    <tr><td class="Titolo" colspan="2">Tipologia Stato</td>
	 	 <tr>
	     <td class="l">
		     Tutti<input type="checkbox" checked name="tipoNotifica" value="Tutti">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		     Attesa Notifica<input type="checkbox" name="tipoNotifica" value="Condannato">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		     Attesa Notifica Avvocati<input type="checkbox" name="tipoNotifica" value="Avvocato">&nbsp;&nbsp;&nbsp;
		     Mancata Notifica<input type="checkbox" name="tipoNotifica" value="Mancata">&nbsp;&nbsp;&nbsp;&nbsp;
		     Sollecito<input type="checkbox" name="tipoNotifica" value="Sollecito">
	     </td>
	   </tr>
	 	 <tr>
	     <td class="l">
		     Richiesta Informazioni 8 bis<input type="checkbox" name="tipoNotifica" value="Richiesta">&nbsp;&nbsp;&nbsp;
		     Rinnovazione Notifica 8 bis<input type="checkbox" name="tipoNotifica" value="Rinnovazione">
	     </td>
	   </tr>
     <tr><td>&nbsp;</td></tr>
      <tr>
        <td colspan="2">
          <INPUT class="bottone" type="submit"  name="RICERCA" value="Ricerca" onClick="javascript:return checkNewProg();">
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

    frmvalidator.setAddnlValidationFunction("Verify");

    function checkNewProg(){
        var ufficioAccorpato = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
        var parts=ufficioAccorpato.split("-"); 
        var offSetInt = parseInt(parts[0]);

        var numProgIni = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value;
        var numProgIniInt = 0;
        if (numProgIni){
        	numProgIniInt = parseInt(numProgIni);
            }
        var newProgIniInt = numProgIniInt + offSetInt;
        if (newProgIniInt>0){
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = newProgIniInt;
            }

        var numProgFin = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value;
        var numProgFinInt = 0;
        if (numProgFin){
        	numProgFinInt = parseInt(numProgFin);
            }
        var newProgFinInt = numProgFinInt + offSetInt;
        if (newProgFinInt>0){
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value = newProgFinInt;
            }

        return true;
    }
  </script>
  </body>
</html>