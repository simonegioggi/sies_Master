<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>

<%@ page import=" siap.siep.statis.model.StatoFascicoloResModel" %>
<%@ page import=" siap.siep.statis.action.ICostantiStatis" %>
<%@ page import=" siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>

<jsp:useBean id="statifascicolo" scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaUffAcc" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficiomod" 	scope="request" class="siap.sico.ufficio.model.UfficioModel" />
<jsp:useBean id="UffScelto" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="sysdate" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="ggIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="mmIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="aaIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="ggFin" scope="request" class="java.lang.String"/>
<jsp:useBean id="mmFin" scope="request" class="java.lang.String"/>
<jsp:useBean id="aaFin" scope="request" class="java.lang.String"/>
<jsp:useBean id="soloaaIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="soloaaFin" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript" type="text/javascript">
    function Verify()
    {
    	
    	return true;
    	
    } // Chiude verify()
    
    function enableBtn() 
    {
    
    	document.CreaRiepCPP.btnconf.disabled = false;
    }
    
function selAll() 
{		
	
	var lista;
	var ind;
	var i;

	lista = CreaRiepCPP.<%=ICostantiStatis.CAMPO_LISTA_STATI%>;
	ind = lista.length;
	
	for (i=0; i < ind; i++) {
		
		lista.options[i].selected = true;
	}
	
	enableBtn();
}    
    </script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="CreaRiepCPP">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActCreaRiepilogo_CPP">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">STATISTICHE UFFICIO - RIEPILOGO PROCEDIMENTI PENDENTI </font>
        </td>
      </tr>
    </table>
    <br>
  
  <table width="80%">
  <% // 07-06-2016 - Riciclo dopo primo collaudo V.10 %>
	<tr><td class="Titolo" colspan="6" >Intervallo di Tempo da verificare (valido solo per Periodo di Definizione)</td></tr>
  <% // 07-06-2016 - END Riciclo %>	
	<tr>
<% if(!ggIni.equals(""))
   { %>	
        <td class="l" width="20%"> Data Iniziale </td>
      	<td class="L" width="20%">
        	<input type="text" name="<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>" value="<%=ggIni%>" maxlength="2" size="2" readonly 
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" name="<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>" value="<%=mmIni%>" maxlength="2" size="2" readonly 
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>" value="<%=aaIni%>" maxlength="4" size="4" readonly 
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;
	   	</td>
 	    <td class="l" ><center>Data Finale</center> </td>
      	<td class="L" >
        	<input type="text" name="<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>" value="<%=ggFin%>" maxlength="2" size="2" readonly 
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        	<input type="text" name="<%=ICostantiStatis.CAMPO_MESE_FINALE%>" value="<%=mmFin%>" maxlength="2" size="2" readonly 
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
        	<input type="text" name="<%=ICostantiStatis.CAMPO_ANNO_FINALE%>" value="<%=aaFin%>" maxlength="4" size="4" readonly
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">&nbsp;
      	</td>
<%	}
	else if(!soloaaIni.equals(""))
	{	%>      	
        <td class="l" width="20%"> Anno Iniziale </td>
      	<td class="L" >
        	<input type="text" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>" value="<%=soloaaIni%>" maxlength="4" size="4" readonly
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;
	   	</td>
 	    <td class="l" ><center>Anno Finale</center> </td>
      	<td class="L" >
        	<input type="text" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>" value="<%=soloaaFin%>" maxlength="4" size="4" readonly
        		onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">&nbsp;
      	</td>
<%	} %>	
	</tr>
</table>
     
    <table>
    	<tr><td></td></tr>
    	<tr>
    		<td class="L">
         		<font class="label">
          			Ufficio Accorpato Selezionato per la Statistica 
        		</font>
      		</td>
      		<td>&nbsp;</td>
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

<div id="divStati" style="position:relative; display:none;" >    
    <table>
    	<tr><td class="Titolo">Selezione Stampa</td></tr>
      <tr>    
        <td class="LBG">
			<select name="<%=ICostantiStatis.CAMPO_LISTA_STATI%>" MULTIPLE SIZE=20 onChange="enableBtn();">
			<option value="0">RIEPILOGO GENERALE</option>
			<%
			Iterator itx = statifascicolo.iterator();
			
			while ( itx.hasNext()) 
			{
				
				StatoFascicoloResModel lSfrMod = (StatoFascicoloResModel) itx.next();
				%>
				<option value="<%=lSfrMod.getCodStatoFascicolo().toString()%>"><%=lSfrMod.getDescrizione().toUpperCase()%></option><%
			}
			%>

			</select>        
          </td>
      </tr>
    </table>
</div>    
	<table>
    <tr><td>  </td></tr>
    <tr><td>  </td></tr>    
    <tr>
      <td>
        <input type=submit value="Conferma" class=bottone name="btnconf" onClick="selAll();">
      </td>
		</tr>
    <tr>
      <td>
      </td>
	</tr>
</table>
  </FORM>
<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("CreaRiepCPP");
	frmvalidator.setAddnlValidationFunction("Verify");
 </script>
</body>
</html>