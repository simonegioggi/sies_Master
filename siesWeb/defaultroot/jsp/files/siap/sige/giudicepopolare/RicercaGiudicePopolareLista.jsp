<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>
<%@ page import="siap.sige.giudicepopolare.model.GiudicePopolareModel"%>

<jsp:useBean id="giudicipopolari" scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="formname" scope="request" class="java.lang.String"/>
<jsp:useBean id="idfieldnum" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Lista Giudici Popolari</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% 
    if (!modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
    	function insertIT(id,cognome,nome)
      {
		<%
				if( !idfieldnum.equals("null") )
				{ 				  
		%>
					var len = 
						window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.length;

					for( i=0; i<len; i++ ) {
						if( window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>[i].value==id )
						{
							alert('Il giudice popolare richiesto è già stato selezionato.' );
							return; 
						}
					}
					
          window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>[<%=idfieldnum%>].value=id;
          window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_COGNOME_GIU_POP%>[<%=idfieldnum%>].value=cognome;
          window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_NOME_GIU_POP%>[<%=idfieldnum%>].value=nome;
    <%
				}
				else
				{
		%>
        	window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.value=id;
        	window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_COGNOME_GIU_POP%>.value=cognome;
        	window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_NOME_GIU_POP%>.value=nome;		
		<%
				}
		%>          
        window.parent.close();
        return;
			}

			// 
			// Esegue l'inserimento one shot. 
			//
    	function insertALL()
      {
 				var len =
 					window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.length;
 				var lenItem =
 					document.f.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>.length;
 					
 					// Ciclo for
 					for( i=0; i<lenItem && i<len; i++ ){
 				  	window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>[i].value=
 				  		document.f.<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>[i].value;
 				    window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_COGNOME_GIU_POP%>[i].value=
 				    	document.f.<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>[i].value;
 				    window.parent.opener.document.<%=formname%>.<%=ICostantiGiudicePopolare.CAMPO_NOME_GIU_POP%>[i].value=
 				    	document.f.<%=ICostantiGiudicePopolare.CAMPO_NOME%>[i].value;						
 					} // end for

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
					<font class="campo">Elenco Giudici Popolari Ufficio</font>
				</td>
      </tr>
    </table>

		<form name="f">
    	<table width="100%">
    		<tr>
    			<td class=int>Nominativo</td>
    			<td class=int>Data di nascita</td>
					<td class=int>Ruolo</td>
    	<% 
    			if (! modalita.equals("NoPop"))
    			{
    	%>
      			<td class=int>Seleziona -  
							[ Tutti <a href="Javascript:insertALL();"> 
								<img align="middle" src="/images/fileselected.gif" border=0>
							</a> ]

						</td>						
    	<%
    			} 
    	%>
    	</tr>
    <%
    Iterator itx = giudicipopolari.iterator();
    int i = 0;
    while ( itx.hasNext() )
    {
      GiudicePopolareModel lGiudicePopolare = (GiudicePopolareModel)itx.next();
      String lClass = 
        lGiudicePopolare.getCodRuolo().equalsIgnoreCase("S") ? "lVerde" : "l";  
    %>
      <tr>
        <td class=<%=lClass%>>
        	<%=StringUtils.toStringJSP(lGiudicePopolare.getCognome(),"-") + " " + 
        		 StringUtils.toStringJSP(lGiudicePopolare.getNome(),"-")%>
        	<input type="HIDDEN" name="<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>" 
								 value="<%=lGiudicePopolare.getCognome()%>">
        	<input type="HIDDEN" name="<%=ICostantiGiudicePopolare.CAMPO_NOME%>" 
								 value="<%=lGiudicePopolare.getNome()%>">
        	<input type="HIDDEN" name="<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>" 
								 value="<%=lGiudicePopolare.getIdGiudicePopolare()%>">
				</td>
				<td class=<%=lClass%>>
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(lGiudicePopolare.getDataNascita(),"dd-MM-yyyy"))%>
				</td>
				<td class=<%=lClass%>>
					<%=StringUtils.toStringJSP(lGiudicePopolare.getDescrRuolo(),"-")%>
				</td>
   <%
      if (!modalita.equals("NoPop"))
     	{ 
   %>
   			<td class=c>
					<a href="Javascript:insertIT('<%=lGiudicePopolare.getIdGiudicePopolare()%>','<%=StringUtils.cStrForJS(lGiudicePopolare.getCognome())%>','<%=StringUtils.cStrForJS(lGiudicePopolare.getNome())%>');"> 
						<img align="middle" src="/images/fileselected.gif" border=0>
					</a>
				</td>
   <% 
   		} 
   %>
     </tr>
   <%
   		i++;
    }
   %>
    </table>
	</form>
  </body>
</html>