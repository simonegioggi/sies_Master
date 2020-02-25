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

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    
    <script language="JavaScript" type="text/javascript">
    function Verify() {	
    
    	if (document.FLoadCreaRiepilogo.<%=ICostantiStatis.CAMPO_LISTA_STATI%>.selectedIndex == -1) {
    	
    		alert ("Selezionare almeno un elemento dalla lista.");
    		return false;
    	}
    	
    	return true;
    }
    
    function enableBtn() {
    
    	document.FLoadCreaRiepilogo.btnconf.disabled = false;
    }
    
function selAll() {		
	
	var lista;
	var ind;
	var i;

	lista = FLoadCreaRiepilogo.<%=ICostantiStatis.CAMPO_LISTA_STATI%>;
	ind = lista.length;
	
	for (i=0; i < ind; i++) {
		
		lista.options[i].selected = true;
	}
	
	enableBtn();
}    
    </script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="FLoadCreaRiepilogo">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActCreaRiepilogo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">STATISTICHE UFFICIO - RIEPILOGO STATI PROCEDIMENTI </font>
        </td>
      </tr>
    </table>
    <br>
    
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
    
    <table>
       <tr>
      <td>
        <input type=button value="Seleziona tutti" class=bottone name="btnsel" onClick="selAll();">
      </td>
		</tr>
    	<tr><td class="Titolo">Selezione Stampa</td></tr>
      <tr>    
        <td class="LBG">
			<select name="<%=ICostantiStatis.CAMPO_LISTA_STATI%>" MULTIPLE SIZE=20 onChange="enableBtn();">
			<option value="0">RIEPILOGO GENERALE</option>
			<%
			Iterator itx = statifascicolo.iterator();
			
			while ( itx.hasNext()) {
				
				StatoFascicoloResModel lSfrMod = (StatoFascicoloResModel) itx.next();
				%>
				<option value="<%=lSfrMod.getCodStatoFascicolo().toString()%>"><%=lSfrMod.getDescrizione().toUpperCase()%></option><%
			}
			%>

			</select>        
          </td>
      </tr>
    </table>
	<table>
    <tr><td>  </td></tr>
    <tr><td>  </td></tr>    
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
  	var frmvalidator  = new Validator("FLoadCreaRiepilogo");
	frmvalidator.setAddnlValidationFunction("Verify");
 </script>
</body>
</html>