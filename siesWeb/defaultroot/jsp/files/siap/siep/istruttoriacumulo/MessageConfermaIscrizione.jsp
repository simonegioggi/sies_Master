<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="IdIstruttoriaCumulo" scope="request" class="java.lang.String"/>
<jsp:useBean id="idFasDaCumulare"     scope="request" class="java.lang.String"/>
<jsp:useBean id="messaggiodiarrivoatti"    scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="ProcedimentoCumulato"     scope="request" class="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"/>

<%
String lSeguito="";
if( messaggiodiarrivoatti!=null && messaggiodiarrivoatti.getCodTipoOperazione()!=null &&
    messaggiodiarrivoatti.getCodTipoOperazione().compareTo("00078")==0)
{	
	 lSeguito="SI";
}
%>

<html>
  <head>
    <title>[S.I.E.S.] - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>

<SCRIPT LANGUAGE="JavaScript">
  function eseguiSubmit(operazione){      

    $('input[type=button]').prop("disabled",true);

    if (operazione=='iscrivi') {
      document.FormWarning.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.siep.istruttoriacumulo.action.ActInserisciFascicoloInIstruttoria';
      document.FormWarning.submit();
    }
    else if  (operazione=='prosegui') {
      document.FormWarning.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti';
      document.FormWarning.submit();
    }
    else if  (operazione=='esecuzione') {
       document.FormWarning.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici';
       document.FormWarning.submit();
    }   
  
  }
</SCRIPT>





</head>

<body class="corpo">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="FormWarning">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"  value="">
    <input type="HIDDEN" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>"  value="<%=IdIstruttoriaCumulo%>">
    <input type="HIDDEN" name="idFasDaCumulare"  value="<%=idFasDaCumulare%>">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=messaggiodiarrivoatti.getIdMessaggio()%>">
    <input type="HIDDEN" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=ProcedimentoCumulato.getTitIdTitoloCumulato()%>">
  
    <br><br><br><br><br>
    
    <table width="300"  cellspacing="0" align="center" class="tab" >
      <tr>
        <td class="c">
          <table width="300"  cellspacing="0" align="center" class="tab" >
            <tr align="center" valign="middle">
            
         <%	if(lSeguito.equals("SI"))	// Messaggio 'seguito Atti' 
            {	
            	if(ProcedimentoCumulato!=null && ProcedimentoCumulato.getTitIdTitoloCumulato()!=null)
            	{	%>
	              <td align="center"  colspan="2"  class="tab">
	                <p>&nbsp;<p>
	                <!--B>Ricezione Seguito Atti Completata e Esito rispedito al Mittente. Il fascicolo è stato aggiornato! <br> Si vuole procedere alla modifica dello Stato Esecuzione?</B-->
	                <B>Ricezione Seguito Atti Completata e Esito rispedito al Mittente. Lo stato esecuzione del Titolo è stato aggiornato! <br> Si vuole visualizzare lo Stato Esecuzione Aggiornato?</B>
                </td>
	    <%		}
            	else
            	{	%>
            	  <td align="center"  colspan="2"  class="tab">
	                <p>&nbsp;<p>
	                <B>Ricezione Atto Completata e Esito rispedito al Mittente. Il fascicolo è stato preso in carico! <br> Clicca su ISCRIVI per iscrivere il Titolo in Istruttoria</B>
	              </td>	          
        <%		}
        	}
         	else						// Messaggio 'Atti per Competenza'
         	{	%>
			  <td align="center"  colspan="2"  class="tab">
                <p>&nbsp;<p>
                <B>Ricezione Atto Completata e Esito rispedito al Mittente. Il fascicolo è stato preso in carico! <br> Si vuole iscrivere subito il Titolo in Istruttoria?</B>
              </td>         	
        <%	} %>
         	      
            </tr>
            
            <tr><td><br></td></tr>

            <tr align="center" valign="middle">
              <td class="l" colspan="2" class="tab">
                <B></B>
              </td>
            </tr>
            
            <tr><td><br></td></tr>
            
            <tr align="center" valign="middle">
              <td align="center"  class="tab2" width="50%">
              
          <%Boolean lProsegui = true;
          	if(lSeguito.equals("SI"))	// Messaggio 'seguito Atti' 
          	{	
          		if(ProcedimentoCumulato!=null && ProcedimentoCumulato.getTitIdTitoloCumulato()!=null)
            	{	%>	     
                	<input type="button" class="bottone" name="I" value="Stato Esecuzione Fascicolo" onClick="eseguiSubmit('esecuzione')">
          <%	}
          		else
          		{	
          			lProsegui = false;	%> 
          			<input type="button" class="bottone" name="I" value="Iscrivi in Istruttoria" onClick="eseguiSubmit('iscrivi')">     	
          <%	}
          	}
          	else						// Messaggio 'Atti per Competenza'
          	{	%>
          		<input type="button" class="bottone" name="I" value="Iscrivi in Istruttoria" onClick="eseguiSubmit('iscrivi')">
          <%} %>
          		      
              </td>
          <%if(lProsegui)	{ %>    
              <td align="center"  class="tab2" width="50%">
                <input type="button" class="bottone" name="I" value="Prosegui" onClick="eseguiSubmit('prosegui')">
              </td>
          <%} %>    
            </tr>
          </table>
        </td>
      </tr>
    </table>
    

   </FORM>
 </body>
</html>