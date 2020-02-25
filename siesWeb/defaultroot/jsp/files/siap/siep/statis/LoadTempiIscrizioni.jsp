<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.siep.statis.action.ICostantiStatis" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>

<jsp:useBean id="sysdate" scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaUffAcc" scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficiomod" scope="request" class="siap.sico.ufficio.model.UfficioModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript" type="text/javascript">
    function Verify() {	
    	
    if (document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value='0'+document.c.<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value;
    if (document.c.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value='0'+document.c.<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>.value;

    if (document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value.length==1)
       document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value='0'+document.c.<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>.value;
    if (document.c.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value.length==1)
        document.c.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value='0'+document.c.<%= ICostantiStatis.CAMPO_MESE_FINALE%>.value;

    var data_inizio=document.c.<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>.value+'/'+document.c.<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>.value+'/'+document.c.<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>.value;
    var data_fine=document.c.<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>.value+'/'+document.c.<%=ICostantiStatis.CAMPO_MESE_FINALE%>.value+'/'+document.c.<%=ICostantiStatis.CAMPO_ANNO_FINALE%>.value;
	var data_sistema="<%=sysdate%>";
	
      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data di inizio non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data di fine non valida');
        return false;
      }

      if(data_inizio.length==2) {
      
       alert('Inserire la Data di inizio');
       return false;
	  }	
	  
      if(data_fine.length==2) {
      
       alert('Inserire la Data di fine');
       return false;
	  }	

      if(!CompareDate(data_inizio, data_sistema))
      {
        alert('La Data di inizio non può essere superiore alla Data odierna');
        return false;
      }

      if(!CompareDate(data_fine, data_sistema))
      {
        alert('La Data di fine non può essere superiore alla Data odierna');
        return false;
      }
      	  
      if(!CompareDate(data_inizio, data_fine))
      {
        alert('La Data di fine non può essere inferiore alla Data di inizio');
        return false;
      }
      
    	if (document.c.<%=ICostantiStatis.CAMPO_LISTA_DISTINTE%>.selectedIndex == -1) {
    	
    		alert ("Selezionare un tipo di distinta.");
    		return false;
    	}

    	if (document.c.<%=ICostantiStatis.CAMPO_LISTA_INTERVALLI%>.selectedIndex == -1) {
    	
    		alert ("Selezionare un intervallo di tempo.");
    		return false;
    	}
    	    	      
        return true;    	
    }

    function enableBtn() {
    
    	document.c.btnconf.disabled = false;
    }
        
    </script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActCreaTempiIscrizioni">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">STATISTICHE UFFICIO - TEMPI ISCRIZIONI PROCEDIMENTI</font>
        </td>
      </tr>
    </table>
    <br>
    
<table >
        <tr> <td class="Titolo"  colspan ="4" >Intervallo di Tempo da verificare</td></tr>
<tr>
       <td class="L" width="20%" >
        <font class="label">
          Data Iniziale
        </font>
      </td>
      <td class="l" >
        <input type="text" title="Giorno Iscrizione Iniziale" name="<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Iscrizione Iniziale" name="<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Iscrizione Iniziale" name="<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

</td>
 <td class="L" >
        <font class="label">
          Data Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Giorno Iscrizione Finale" name="<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Iscrizione Finale" name="<%=ICostantiStatis.CAMPO_MESE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Iscrizione Finale" name="<%=ICostantiStatis.CAMPO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
   </tr>
   </table>
 <br>
<table width="100%">
     <tr>
     	<td class="L">
     		<font class="label" style="color:green; font-size: 11pt"> N.B. Se non si seleziona un Ufficio Accorpato, la funzione effettua l'elaborazione per Ufficio Accorpante    
     		</font>
     	</td>
     </tr>
     <tr>	
     	<td class="L">
     	<font class="label" style="color:green; font-size: 11pt">&nbsp;&nbsp;&nbsp;&nbsp;Se si desidera l'elaborazione relativa al solo Ufficio Accorpato al momento della chiusura, selezionarlo nella successiva combo
     	</font>
     	</td>
     </tr>
</table> 
<br>
<table>
	<tr>
		<td class="L" >
        	<font class="label">
          		Ufficio Accorpato
        	</font>
     	 </td>
     	 
     	 <% int Nuff = 0;
     	 	Nuff = ListaUffAcc.size();
			
     	 	if(ListaUffAcc.size()==0)
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
<table width="100%">
     <tr>
     	<td class="L">
     		<font class="label" style="color:green; font-size: 11pt"> N.B. La selezione del tipo di distinta e Intervallo di tempo è utilizzata solo per la produzione del foglio di dettaglio    
     		</font>
     	</td>
     </tr>
     <tr>	
     	<td class="L">
     	<font class="label" style="color:green; font-size: 11pt">&nbsp;&nbsp;&nbsp;&nbsp;il foglio di Riepilogo riporta comunque i dati relativi a tutti i tipi di distinta e Intervalli di tempo 
     	</font>
     	</td>
     </tr>
</table>

    <table>    	
    <tr height=20> <td></td></tr>
      <tr>    
 	<td class="L" >
        <font class="label">
          Tipo di distinta
        </font>
      </td>      
        <td class="LBG">
			<select style="width: 200;" name="<%=ICostantiStatis.CAMPO_LISTA_DISTINTE%>" SIZE=3 onChange="enableBtn();" >
			<option value="1">Da RICEZIONE a ISCRIZIONE</option>
			<option value="2">Da GIUDICATO a RICEZIONE</option>
			<option value="3">Da GIUDICATO a ISCRIZIONE</option>
			</select>        
          </td>
      </tr>
      <tr height=10> <td></td></tr>
      <tr>    
 	<td class="L" >
        <font class="label">
          Intervallo di tempo
        </font>
      </td>      
        <td class="LBG">
			<select style="width: 200;" name="<%=ICostantiStatis.CAMPO_LISTA_INTERVALLI%>" SIZE=6 onChange="enableBtn();">
			<option value="5">Entro 5 giorni</option>
			<option value="20">Entro 20 giorni</option>
			<option value="30">Entro 30 giorni</option>
			<option value="60">Entro 60 giorni</option>
			<option value="90">Entro 90 giorni</option>
			<option value="0">Oltre 90 giorni</option>
			</select>        
          </td>
      </tr>      
    </table>
	<table>
    <tr height=20> <td></td></tr>
    <tr>
      <td>
        <input type=submit value="Conferma" class=bottone name="btnconf">
      </td>
		</tr>
    <tr>
      <td>
      </td>
	</tr>
</table>
  </FORM>
<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("c");
  	
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","gt=1", "Giorno iniziale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_INIZIALE%>","lt=31", "Giorno iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","gt=1", "Giorno finale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_GIORNO_FINALE%>","lt=31", "Giorno finale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>","gt=1", "Mese iniziale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_INIZIALE%>","lt=12", "Mese iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_MESE_FINALE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_FINALE%>","gt=1", "Mese finale non valido");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_MESE_FINALE%>","lt=12", "Mese finale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_INIZIALE%>","gt=1900", "Anno iniziale non valido");
//  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_INIZIALE%>","lt=3000", "Anno iniziale non valido");

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","gt=1900", "Anno finale non valido");
//  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","lt=3000", "Anno finale non valido");
  	
  frmvalidator.setAddnlValidationFunction("Verify");
  
 </script>
</body>
</html>