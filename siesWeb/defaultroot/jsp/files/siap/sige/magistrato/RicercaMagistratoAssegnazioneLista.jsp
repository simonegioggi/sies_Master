<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato" %>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>
<%@ page import="siap.sico.w_magistrato.model.WMagistratoModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="magistrati" 		scope="request" class="java.util.Vector" />
<jsp:useBean id="elencoSezioni" scope="request" class="java.util.Vector" />

<jsp:useBean id="modalita"   		scope="request" class="java.lang.String" />
<jsp:useBean id="formname"   		scope="request" class="java.lang.String" />
<jsp:useBean id="idfieldnum" 		scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Magistrati (RicercaMagistratoAssegnazioneLista.jsp)</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
<%
    if (! modalita.equals("NoPop"))
    {
%>
      <script language="JavaScript">
        function insertIT(cod,cognome,nome)
        {
<%
					if( !idfieldnum.equals("null") )
					{
%>
						var codMagDaModif = window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>[<%=idfieldnum%>].value;
						
						var len =
							window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.length;
							
						// Ciclo for
						for( i=0; i<len; i++ ){
							var lCod = 
								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>[i].value;
							// Verifica se il magistrato selezionato sia stato già selezionato.	
							if( lCod == cod ){
								alert('Magistrato già selezionato.');
								return;
							}	// end if									
						} // end for
						 
        		window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>[<%=idfieldnum%>].value=cod;
          	window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME%>[<%=idfieldnum%>].value=cognome;
          	window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME%>[<%=idfieldnum%>].value=nome;
          		
          		//intervento per 11.2.1     		          		
          		if(typeof(window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>) != "undefined"){
          		         			
		          	  var lenMAgAssegnatari =
									window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>.length;

						if(typeof(lenMAgAssegnatari) != "undefined"){					
	          		        // Ciclo for sugli assegnatari
	   						for( j=0; j<lenMAgAssegnatari; j++ ){
	   							var lCodAssegnatario = 
	   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>[j].value;
	   							
	   								if( lCodAssegnatario == codMagDaModif ){
	   									
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>[j].value=cod;    								
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME_ASS%>[j].value=cognome;
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME_ASS%>[j].value=nome;        								
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_CHECK_MAGISTRATO_ASS%>[j].checked = true;
	       								
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>[j].disabled="";
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_CHECK_MAGISTRATO_ASS%>[j].disabled="";    
	       								
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME_ASS%>[j].disabled="";    								
	       								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME_ASS%>[j].disabled="";
	       				          		
	       							}	// end if		
	   														
	   						} // end for
						} // end if
						else{
							var lCodAssegnatario = 
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>.value;
   								
							if( lCodAssegnatario == codMagDaModif ){
									
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>.value=cod;    								
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME_ASS%>.value=cognome;
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME_ASS%>.value=nome;        								
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_CHECK_MAGISTRATO_ASS%>.checked = true;
   								
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS%>.disabled="";
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_CHECK_MAGISTRATO_ASS%>.disabled="";    
   								
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME_ASS%>.disabled="";    								
   								window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME_ASS%>.disabled="";
   				          		
   							}	// end if		
							
						}
					}
<%
					}
					else
					{
%>
						window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value=cod;
						window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value=cognome;
						window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME%>.value=nome;
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
				<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
					</a>
				</td>
        <td class="LBG">
					<font class=label>Funzione :</font>&nbsp;
					<font class="campo">Elenco Magistrati Ufficio</font>
				</td>
      </tr>
    </table>

    <table width="100%">
    <tr>
    	<td class=int>Cod</td>
    	<td class=int>Nome</td>
    	<td class=int>Data Nascita</td>
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
    Iterator itx = magistrati.iterator();
    while ( itx.hasNext() )
    {
      MagistratoModel lMag = (MagistratoModel)itx.next();
    %>
      <tr>
        <td class=l>
        	<%=StringUtils.toStringJSP(lMag.getCodMagistrato(),"-")%>
				</td>
        <td class=l>
        	<%=StringUtils.toStringJSP(lMag.getCognome(),"-") + " " + StringUtils.toStringJSP(lMag.getNome(),"-")%>
				</td>
        <td class=l>
        	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMag.getDataNascita(),"dd-MM-yyyy"))%>
				</td>
      <%
      if (! modalita.equals("NoPop"))
      { 
      %>
        <td class=c>
					<a href="Javascript:insertIT('<%=lMag.getCodMagistrato()%>','<%=StringUtils.cStrForJS(lMag.getCognome())%>','<%=StringUtils.cStrForJS(lMag.getNome())%>');"> 
						<img align="middle" src="/images/fileselected.gif" border=0>
					</a>
				</td>
   <% } %>
      </tr>
    <%
    }
    %>
    </table>
  </body>
</html>