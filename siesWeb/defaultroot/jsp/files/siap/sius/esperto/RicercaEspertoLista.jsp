<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>
<%@ page import="siap.sius.esperto.model.EspertoModel"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="esperti" 		scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" 		scope="request" class="java.lang.String" />
<jsp:useBean id="formname" 		scope="request" class="java.lang.String" />
<jsp:useBean id="idfieldnum" 	scope="request" class="java.lang.String" />


<html>
  <head>
    <title>[S.I.E.S.] - Lista Esperti</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
        function insertIT(id,codfis,cognome,nome)
        {
<%
        	if( !idfieldnum.equals("null") )
        	{
%>
						var len =
							window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>.length;
			
						// Ciclo for
						for( i=0; i<len; i++ ){
							var lId = 
								window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>[i].value;
							// Verifica se l'esperto selezionato sia stato già selezionato.	
							if( lId == id ){
								alert('Esperto già selezionato.');
								return;
							}	// end if									
						} // end for

	        	window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>[<%=idfieldnum%>].value=id;
	          window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_COGNOME%>[<%=idfieldnum%>].value=cognome;
	          window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_NOME%>[<%=idfieldnum%>].value=nome;
<%
	    		}
	    		else
	    		{
%>	    					
          	window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>.value=id;
          	window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_COGNOME%>.value=cognome;
          	window.parent.opener.document.<%=formname%>.<%=ICostantiEsperto.CAMPO_NOME%>.value=nome;
<%
					}
%>
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
      	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td --%>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Esperti Ufficio</font></td>
      </tr>
    </table>

    <Table width="100%">
    <tr>
    <td class=int>Cod. Fiscale</td>
    <td class=int>Nominativo</td>
    <% if (! modalita.equals("NoPop"))
    { 
    %>
      <td class=int>Seleziona</td>
    <%
    } 
    %>
    </tr>
    <%
    Iterator itx = esperti.iterator();

    while ( itx.hasNext() )
    {
      EspertoModel lEsp = (EspertoModel)itx.next();
    %>
      <tr>
        <td class=l>
        	<%=StringUtils.toStringJSP(lEsp.getCodiceFiscale(),"-")%>
				</td>
        <td class=l>
        	<%=StringUtils.toStringJSP(lEsp.getCognome(),"-") + " " + StringUtils.toStringJSP(lEsp.getNome(),"-")%>
				</td>
    <%
      if (! modalita.equals("NoPop"))
      { 
    %>
        <td class=c>
					<a href="Javascript:insertIT('<%=lEsp.getIdEsperto()%>','<%=lEsp.getCodiceFiscale()%>','<%=StringUtils.cStrForJS(lEsp.getCognome())%>','<%=StringUtils.cStrForJS(lEsp.getNome())%>');">
						<img align="middle" src="/images/fileselected.gif" border=0>
					</a>
				</td>
    <%
      }
    %>
      </tr>
    <%
    }
    %>
    </table>
  </body>
</html>