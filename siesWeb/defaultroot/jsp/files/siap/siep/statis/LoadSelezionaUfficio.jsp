<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>
<%@ page import="siap.siep.statis.action.ICostantiStatis" %>

<jsp:useBean id="ListaUffAcc" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="ufficiomod" 	scope="request" class="siap.sico.ufficio.model.UfficioModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript" type="text/javascript">
    </script>
    
  </head>

  <BODY class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
	   <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">STATISTICHE UFFICIO - RIEPILOGO STATI PROCEDIMENTI -</font>
        </td>
      </tr>
    </table>
    <br>

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

<% // END NGG %>
	<table>
    <tr height=20> <td></td></tr>
    <tr>
      <td>
        <input type=submit value="Conferma" class=bottone name="btnconf">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.statis.action.ActLoadCreaRiepilogo">
      </td>
	</tr>
    <tr>
      <td>
      </td>
	</tr>
</table>
</FORM>
</body>
</html>