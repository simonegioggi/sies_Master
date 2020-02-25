<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.html.*"%>

<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>
<%@ page import="siap.sige.collegio.model.CollegioModel"%>
<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>

<jsp:useBean id="collegi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="formname" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Collegio Lista</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    	 {
    %>
    	<script language="JavaScript">
        function insertIT(index)
        {
          window.parent.opener.document.<%=formname%>.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value=
              index >-1 ? document.f.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>[index].value : 
												  document.f.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value;

          window.parent.opener.document.<%=formname%>.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.value=
        	  	index >-1 ? document.f.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>[index].value :
        	  						  document.f.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.value;
        	  						 	
          window.parent.opener.document.<%=formname%>.<%=ICostantiSezione.CAMPO_DESCRIZIONE%>.value=
      	  		index >-1 ? document.f.<%=ICostantiSezione.CAMPO_DESCRIZIONE%>[index].value :
      	  							  document.f.<%=ICostantiSezione.CAMPO_DESCRIZIONE%>.value;	

					var arrayMag = index >-1 ? document.f.<%="Magistrati"%>[index].value.split(';') :
																		 document.f.<%="Magistrati"%>.value.split(';') 
					
					// Sezione Magistrato.
					if( window.parent.opener.document.<%=formname%>.<%="Magistrato"%> != undefined )          
          	for( var i=0; i<arrayMag.length-1; i++ )
        	  	window.parent.opener.document.<%=formname%>.<%="Magistrato"%>[i].value = arrayMag[i];  

        	// Sezione Esperti.
        	if( window.parent.opener.document.<%=formname%>.<%="Esperti"%> != undefined ) {  	
	        	if( index > -1 ){  
	        		if( document.f.<%="Esperti"%>[index].value.length > 0 )
	        			window.parent.opener.document.<%=formname%>.<%="Esperti"%>.value = 
	            			document.f.<%="Esperti"%>[index].value;
	        	} else {
	      			if( document.f.<%="Esperti"%>.value.length > 0 )
	    					window.parent.opener.document.<%=formname%>.<%="Esperti"%>.value = 
	        					document.f.<%="Esperti"%>.value;
	          }    
        	}
        	
					// Sezione Giudici Popolari. 
        	if( window.parent.opener.document.<%=formname%>.<%="GiudiciPopolari"%> != undefined ) {  		
	      		if( index > -1 ){  		
	          	if( document.f.<%="GiudiciPopolari"%>[index].value.length > 0 )
	            	window.parent.opener.document.<%=formname%>.<%="GiudiciPopolari"%>.value = 
	                	document.f.<%="GiudiciPopolari"%>[index].value;
	       		} else {
	            if( document.f.<%="GiudiciPopolari"%>.value.length > 0 )
	            	window.parent.opener.document.<%=formname%>.<%="GiudiciPopolari"%>.value = 
	                  document.f.<%="GiudiciPopolari"%>.value;
	       		}
        	}
        	
          window.parent.close();
          return;
        }
    	</script>
    <%
    }
    %>
  </head>

  <body class=corpo>
    <table>
      <tr>
        <td class="LBG">
					<font class=label>Funzione :</font>&nbsp;
					<font class="campo">Elenco Collegi</font></td>
      </tr>
    </table>

		<FORM method="get" name="f">
    	<table width="100%">
    	<tr>
    		<td class=int>Collegio</td>
    		<td class=int>Sezione</td>
				<td class=int>Componenti</td>
    		<% 
    		if (! modalita.equals("NoPop"))
    		{ 
    		%>
      		<td class=int>Seleziona</td>
    		<%
    		} 
    		%>
    	</tr>
    <%
    int lSize = collegi.size(); int x = 0;
    Iterator itx = collegi.iterator();
    while ( itx.hasNext() ){
      CollegioModel lCollegio = (CollegioModel)itx.next();
    %>
      <tr>
        <td class=l>
        	<%=StringUtils.toStringJSP(lCollegio.getCodCollegio())%>
				</td>
        <td class=l>
        	<%=StringUtils.toStringJSP(lCollegio.getSezione().getDescrizione())%>
				</td>
				<td class=l>
<% 
				String lNamesMag = new String();
				for( int i=0; lCollegio.getCollegioMagistrati()!=null && 
											i<lCollegio.getCollegioMagistrati().length; i++ ){
				  
					lNamesMag += lCollegio.getCollegioMagistrati()[i].getMagistrato().getCognome() + " " +
											 lCollegio.getCollegioMagistrati()[i].getMagistrato().getNome() + ";";
%>
					<%=StringUtils.toStringJSP(lCollegio.getCollegioMagistrati()[i].getMagistrato().getCognome()) + " " + 
						 StringUtils.toStringJSP(lCollegio.getCollegioMagistrati()[i].getMagistrato().getNome())%> <br>
<% 											 
				}
%>

    <%
      if (!modalita.equals("NoPop"))
     	{ 
    %>  <td class=c>
        	<input type="HIDDEN" 
							 	 name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" 
							 	 value="<%=lCollegio.getIdCollegio()%>">

        	<input type="HIDDEN" 
							 	 name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>" 
							 	 value="<%=lCollegio.getCodCollegio()%>">

        	<input type="HIDDEN" 
							 	 name="<%=ICostantiSezione.CAMPO_DESCRIZIONE%>" 
							 	 value="<%=StringUtils.toStringJSP(lCollegio.getSezione().getDescrizione())%>">
<%
				String lNames = new String();
				for( int i=0; lCollegio.getCollegioGiudiciPopolari() != null && 
											i<lCollegio.getCollegioGiudiciPopolari().length; i++ ){
				  
					lNames += lCollegio.getCollegioGiudiciPopolari()[i].getGiudicePopolare().getCognome() + " " +
										lCollegio.getCollegioGiudiciPopolari()[i].getGiudicePopolare().getNome() + ";";
				}
%>
				<input type="hidden" 
	 						 name="<%="GiudiciPopolari"%>"
	 						 value="<%=lNames%>">
<% 			
				lNames = new String();
				for( int i=0; lCollegio.getCollegioEsperti()!=null && 
											i<lCollegio.getCollegioEsperti().length; i++ ){
				  
					 lNames += lCollegio.getCollegioEsperti()[i].getEsperto().getCognome() + " " +
										 lCollegio.getCollegioEsperti()[i].getEsperto().getNome() + ";";
				}
%>
        	<input type="hidden" 
								 name="<%="Esperti"%>" 
								 value="<%=lNames%>">

        	<input type="hidden" 
								 name="<%="Magistrati"%>" 
								 value="<%=lNamesMag%>">

					<a href="Javascript:insertIT(<%=lSize==1 ? -1 : x%>);"> 
						<img align="middle" src="/images/fileselected.gif" border=0>
					</a>
				</td>
<% 
   		}
%>
      </tr>
<%
   		x++;
    }
%>
    </table>
		</FORM>
  </body>
</html>