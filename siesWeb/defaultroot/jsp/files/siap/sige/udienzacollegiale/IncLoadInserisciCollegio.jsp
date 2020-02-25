<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.collegio.util.CollegioUtils"%>
<%@ page import="siap.sige.collegio.model.CollegioModel"%>
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>
<%@ page import="siap.sige.sezione.model.SezioneModel"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sige.udienzacollegiale.action.ICostantiUdienzaCollegiale"%>
<%@ page import="f3b.web.RedirectTo"%>

<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />
<jsp:useBean id="collegio" 		scope="request" class="siap.sige.collegio.model.CollegioModel" />

<% 
	
	String  lFormName = request.getParameter("form_name");
	String  lCodTipoUff = request.getParameter("tipo_ufficio");
	
	// Link all'Inserimento Collegio 
	RedirectTo lRedirColl = new RedirectTo();
	lRedirColl.setPage(IWebConstants.PG_MAIN);
	lRedirColl.setAction("siap.sige.collegio.action.ActLoadInserisciCollegio");
	lRedirColl.setParameter("TornaQui", TornaQui );
	// 20171124: aggiunto parametro in query string
	lRedirColl.setParameter("from", "jsp");
	String lLinkInsCollegio = lRedirColl.toString();
	
	String lCodCollegio ="";
	if (collegio != null && collegio.getCodCollegio()!=null)
		lCodCollegio = collegio.getCodCollegio(); 

	String lIdCollegio ="";
	if (collegio != null && collegio.getIdCollegio()!=null)
		lIdCollegio = collegio.getIdCollegio().toString(); 
	
%> 


<html>
<head>
  <title>[S.I.E.S.] Gestione Udienza Collegiale </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript">
 	// Chiama la pop-up per la selezione del collegio
    function ListaCollegi(a_formname)
    {
      var desktop;
      desktop = 
          window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.collegio.action.ActLoadRicercaCollegioLista&formname="+a_formname, "Ricerca_Collegio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=550");
    }
    // Visualizza o nasconde la DIV con i campi del Collegio in base alla selezione del tipo di rito
    function Visualizza ( a_tipo_rito)
    {
    	if (a_tipo_rito == "C")
    		VisualizzaCollegio();
    	else
    		NascondiCollegio();
    }
    
    // Visualizza  DIV con i campi per la selezione  del Collegio
   function VisualizzaCollegio()
  {
  	// alert("VisualizzaCollegio");
    node=document.getElementById("CollegioDiv");
    node.style.visibility='visible';
    node.disabled = false;   
   }
       
    // Nasconde DIV con i campi per la selezione  del Collegio
   function NascondiCollegio()
  {
 	// alert("NascondiCollegio");
   	node=document.getElementById("CollegioDiv");
    node.style.visibility='hidden';
    node.disabled = true;
   }
   </script>
  
</head>

  <%
  
  String lPage = "";
  
  if( lCodTipoUff.equalsIgnoreCase("CAP") )
      lPage = ICostantiUdienzaCollegiale.PG_INCLUDE_INS_COLLEGIO_CAP;
    else if( lCodTipoUff.equalsIgnoreCase("CAS") )
      lPage = ICostantiUdienzaCollegiale.PG_INCLUDE_INS_COLLEGIO_CAS;
    else if( lCodTipoUff.equalsIgnoreCase("CASAP") )
      lPage = ICostantiUdienzaCollegiale.PG_INCLUDE_INS_COLLEGIO_CASAP;
    else if( lCodTipoUff.equalsIgnoreCase("DIB") )
      lPage = ICostantiUdienzaCollegiale.PG_INCLUDE_INS_COLLEGIO_DIB;
    else if( lCodTipoUff.equalsIgnoreCase("DIBM") )
      lPage = ICostantiUdienzaCollegiale.PG_INCLUDE_INS_COLLEGIO_DIBM;  
    else if( lCodTipoUff.equalsIgnoreCase("CAPSM") )
      lPage = ICostantiUdienzaCollegiale.PG_INCLUDE_INS_COLLEGIO_CAPSM;
 
	String lSezione ="";
	if (collegio != null && collegio.getSezione()!=null)
		lSezione = collegio.getSezione().getDescrizione(); 

  %>
    <div id="CollegioDiv" style="position:relative;  top: 0; left: 0;   visibility:hidden;" >  
  	<table>
    <tr >
      <td class="l">Collegio <font class=ob>(*)</font>
			<font class="l">
        <input type="text" maxlength="10" size="10" value="<%=lCodCollegio%>" name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>" readonly>
        <input type="hidden" maxlength="10" size="10" value="<%=lIdCollegio%>" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" readonly>  
				 &nbsp;&nbsp; 
			</font>
      <font class="l">Sezione</font>
			<font class="l">
        <input type="text" maxlength="30" size="30" value="<%=lSezione%>" name="<%=ICostantiSezione.CAMPO_DESCRIZIONE%>" readonly>
      </font>&nbsp;&nbsp;&nbsp;&nbsp;
      	<a href="Javascript:ListaCollegi('<%=lFormName%>');">
        	Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
        </a>&nbsp;&nbsp;&nbsp;&nbsp;
        <a class="cliccabile" href="<%=lLinkInsCollegio%>">
					Inserimento Collegio
        </a>
      </td>
    </tr>
  	</table>
    <table>
  	<jsp:include page="<%=lPage%>"/>
     </table>
    </div>
</html>