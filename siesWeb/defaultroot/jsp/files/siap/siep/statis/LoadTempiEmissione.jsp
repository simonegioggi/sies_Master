<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>
<%@ page import="siap.siep.statis.action.ICostantiStatis" %>

<jsp:useBean id="sysdate" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrati" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="SecondoGiro" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoI" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="MeseI" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="GiornoI" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoF" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="MeseF" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="GiornoF" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaUffAcc" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficiomod" 	scope="request" class="siap.sico.ufficio.model.UfficioModel" />
<jsp:useBean id="UffScelto"		scope="request" class="java.lang.String"/>

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

		<%if(SecondoGiro !=null && SecondoGiro.equals("SI")) {%>
		
		
	    	if (document.c.<%=ICostantiStatis.CAMPO_LISTA_INTERVALLI%>.selectedIndex == -1) {
	    	
	    		alert ("Selezionare un intervallo di tempo.");
	    		return false;
	    	}
	    			
	    	if (document.c.<%=ICostantiStatis.CAMPO_LISTA_MAGISTRATI%>.selectedIndex == -1) {
	
	    		alert ("Selezionare un magistrato.");
	    		return false;
	    	}
		<%}%>
	        
        <%if(SecondoGiro ==null || !SecondoGiro.equals("SI")) {%>
    		ciao();
     	<%}%>
		
        return true;
    }

    function enableBtn() {
		
		<%if(SecondoGiro !=null && SecondoGiro.equals("SI")) {%>
    		document.c.btnconf.disabled = false;
   	
    	<%}%>
    }

    </script>
    
    <script language="JavaScript">
    // Visualizzazione rotelle
    function ciao()
    {
      var node=document.getElementById('ciao');
      node.style.visibility='visible';
    }
     </script> 
    
  </head>

  <BODY class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
	   <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">STATISTICHE UFFICIO - TEMPI EMISSIONE PROVVEDIMENTI</font>
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
        <input type="text" value="<%=GiornoI%>" title="Giorno Iscrizione Iniziale" name="<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" value="<%=MeseI%>" title="Mese Iscrizione Iniziale" name="<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" value="<%=AnnoI%>"  title="Anno Iscrizione Iniziale" name="<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>"
        maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

</td>
 <td class="L" >
        <font class="label">
          Data Finale
        </font>
      </td>
      <td class="l">
        <input type="text" value="<%=GiornoF%>"  title="Giorno Iscrizione Finale" name="<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" value="<%=MeseF%>"  title="Mese Iscrizione Finale" name="<%=ICostantiStatis.CAMPO_MESE_FINALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" value="<%=AnnoF%>"  title="Anno Iscrizione Finale" name="<%=ICostantiStatis.CAMPO_ANNO_FINALE%>"
        maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
   </tr>
   </table>
   
   <% // NGG - Statistiche SIEP %>   
		 <br>
<table width="100%">
     <tr>
     	<td class="L">
     		<font class="label" style="color:green; font-size: 12pt"> N.B.    
     		</font>
     	</td>
     </tr>
     <tr>
     	<td class="L">
     		<font class="label" style="color:green; font-size: 11pt">Se non si seleziona un Ufficio Accorpato, la funzione effettua l'elaborazione per Ufficio Accorpante, inclusi i dati di tutti gli Uffici Accorpati    
     		</font>
     	</td>
     </tr>
     <tr>	
     	<td class="L">
     	<font class="label" style="color:green; font-size: 11pt">Se si desidera l'elaborazione relativa al solo Ufficio Accorpato al momento della chiusura, selezionarlo nella successiva combo
     	</font>
     	</td>
     </tr>
</table> 
	<br>

<% // NGG Step2 - %>
<%	if(!SecondoGiro.equals("SI"))
	{ %>
	
	<table>
	<tr>
		<td class="L" >
        	<font class="label">
          		Ufficio Accorpato
        	</font>
     	 </td>
     	 
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
				<%
		     		Iterator itx = ListaUffAcc.iterator();
	  				while ( itx.hasNext())
	  				{	
	  						UfficioAccorpatoModel lUff = (UfficioAccorpatoModel)itx.next(); %>
	   						<option value="<%=lUff.getCodUfficio()%>"><%=lUff.getDescrizione()%></option>    					
	 	   	<%		}	%>
		  				</select>
		  			</td>	
	  	<%	} %>		
	 </tr>
	</table>	  		
<%	} %>

   <%if(SecondoGiro !=null && SecondoGiro.equals("SI")) 
     { %>

		<table>
    		<tr>
    		<td class="L">
         		<font class="label">
          			Ufficio Accorpato Selezionato per la Statistica 
        		</font>
      		</td>
 		<% 	if(ListaUffAcc.size()==0)
	     	{ %>     		    	
    			<td>
    				<input type="text" size="10" maxlength="90" name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" title="Anno" value="-" readonly >
    			</td>
    	<%	}
 			else
 			{	
				if(UffScelto.equals("-"))
	     	  	{	%>	
    				<td>
    					<input type="text" size="10" maxlength="90" name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" title="Anno" value="-" readonly >
    				</td>
		    <%	}
	     		else 
	     		{  
	    			Iterator itx = ListaUffAcc.iterator();
					while ( itx.hasNext())
					{
						UfficioAccorpatoModel lUff = (UfficioAccorpatoModel)itx.next(); 
						if(lUff.getCodUfficio().equals(UffScelto) )
						{		%>
							<td>
								<input type="text" size="45" maxlength="90" name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" title="Anno" value="<%=lUff.getDescrizione() %>" readonly >
							</td>	
		  			<%	}
		   					   					
		  			}
	     		}	
			} %>					
    	</tr>
    </table>
	<br>
<% // END NGG %>

    <table>
    <tr height=20> <td></td></tr>
     <tr>    
 		<td class="L" >
        	<font class="label">
          	Intervallo di tempo
        	</font>
      	</td>      
        <td class="LBG">
			<select name="<%=ICostantiStatis.CAMPO_LISTA_INTERVALLI%>" SIZE=6 onChange="enableBtn();">
			<option value="5">Entro 5 giorni</option>
			<option value="20">Entro 20 giorni</option>
			<option value="30">Entro 30 giorni</option>
			<option value="60">Entro 60 giorni</option>
			<option value="90">Entro 90 giorni</option>
			<option value="0">Oltre 90 giorni</option>
			</select>        
          </td>
      </tr>    
            <tr height=10> <td></td></tr>      
      
      </table>
      <table>
      <tr>
 		<td class="L" >
        <font class="label">
          Magistrato
        </font>
      </td>
        <td class="LBG">
			<select name="<%=ICostantiStatis.CAMPO_LISTA_MAGISTRATI%>" SIZE=5 onChange="enableBtn();">
			<option value="0">Tutti</option>
			<%
			Iterator itx = magistrati.iterator();
			while ( itx.hasNext()) 
			{
				MagistratoModel lMagMod = (MagistratoModel) itx.next();
				if(lMagMod.getCognome().equals(" MAGISTRATO NULLO"))
				{
					
				}
				else
				{	
				%>
					<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
					<option value="<%=lMagMod.getCodMagistrato()%>"><%=lMagMod.getCognome()%>&nbsp;<%=StringUtils.toStringJSP(lMagMod.getNome())%></option>
	<%			}
			}
			%>

			</select>
          </td>
      </tr>
    </table>

	<table>
    <tr height=20> <td></td></tr>
    <tr>
      <td>
        <input type=submit value="Conferma" class=bottone name="btnconf">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActCreaTempiEmissione">
      </td>
		</tr>
    <tr>
      <td>
      </td>
	</tr>
</table>
<%}else{%>
<table>
    <tr height=20> <td></td></tr>
    <tr>
      <td>
        <input type=submit value="Visualizza Intervalli e Magistrati" class=bottone name="btnMag">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActLoadTempiEmissione">
      </td>
		</tr>
    <tr>
      <td>
      </td>
	</tr>
</table>
    <div align=center id="ciao" style="visibility:hidden;position:relative;">
      <table bgcolor="#EEEEEE">
        <tr>
          <td>
            <img src="/images/rotelle3.gif">
          </td>
          <td>
            <font size=+1 color=navy>
              Attendere... Caricamento in corso.
            </font>
          </td>
        </tr>
      </table>
    </div>
<%}%>
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

  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiStatis.CAMPO_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiStatis.CAMPO_ANNO_FINALE%>","gt=1900", "Anno finale non valido");

  frmvalidator.setAddnlValidationFunction("Verify");

 </script>
</body>
</html>