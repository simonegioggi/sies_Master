<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria" %>

<jsp:useBean id="ParentFormName"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ParentFormType"  scope="request" class="java.lang.String"/>

<jsp:useBean id="ListaTitoli"  scope="request" class="java.util.Vector"/>

<!-- 							LoadPopupElencoTitoliPerPA								 -->

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] -Popup di Elenco Titoli associati all'Istruttoria </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      window.focus();

      function insertIT(id_Titolo,
      					CodProvv,
      					DescrTipoProvv,
      					DataIrr,
      					DataProvv,
    		  			AnnoProvv, 
    		  			NumeroProvv,
						DescrTipoAutorEmittente,
						ComuneAutorEmittente,
						CodTipoAutorEmittente,
						CodLuogoAutorEmittente
					)
      {

      		formname = '<%=ParentFormName%>';
      		formtype = '<%=ParentFormType%>';
      		//alert('insertIT - formname = '+formname);
      		//alert('insertIT - formtype = '+formtype);

      		window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value=id_Titolo;
/*
   			window.parent.opener.document.< %=request.getParameter("ParentFormName")%>.< %=ICostantiRichiestePmInCumulo.CAMPO_DESCR_TIPO_PROVV%>.value=DescrTipoProvv;
   			window.parent.opener.document.< %=request.getParameter("ParentFormName")%>.< %=ICostantiRichiestePmInCumulo.CAMPO_DATA_IRR %>.value=DataIrr;
   			window.parent.opener.document.< %=request.getParameter("ParentFormName")%>.< %=ICostantiRichiestePmInCumulo.CAMPO_DATA_PROVV %>.value=DataProvv;
   			window.parent.opener.document.< %=request.getParameter("ParentFormName")%>.< %=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV %>.value=AnnoProvv;
   			window.parent.opener.document.< %=request.getParameter("ParentFormName")%>.< %=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV %>.value=NumeroProvv;
   			
   			window.parent.opener.document.< %=request.getParameter("ParentFormName")%>.< %=ICostantiRichiestePmInCumulo.CAMPO_DESCR_TIPO_AUTO_EMITTENTE %>.value = DescrTipoAutorEmittente
   			window.parent.opener.document.< %=request.getParameter("ParentFormName")%>.< %=ICostantiRichiestePmInCumulo.CAMPO_DESCR_LUOGO_AUTO_EMITTENTE %>.value = ComuneAutorEmittente;
*/
      		// Costruzione della stringa con le descrizione del Titolo
			var lEstremiSentenza='';
			lEstremiSentenza += DescrTipoProvv+" N. "+AnnoProvv+"/"+NumeroProvv+"  Emessa da "+DescrTipoAutorEmittente+" di "+ComuneAutorEmittente;
			lEstremiSentenza +="  il "+DataProvv+"  definitiva il "+DataIrr;
			window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiRichiestePmInCumulo.CAMPO_ESTREMI_TITOLO_CUMULATO%>.value = lEstremiSentenza;
			
          	window.parent.close();

      }

      function controlla()
      {
	        if(document.elenco.numeroTitoli.value==0)
	        {
		          alert("Nessun Titolo Presente");
		          window.parent.close();
	        }
      }

    </script>

  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  
     <input type="HIDDEN" name="ParentFormName" value="<%=ParentFormName%>">
     <input type="HIDDEN" name="ParentFormType" value="<%=ParentFormType%>">
     <input type="hidden" name="numeroTitoli" value="<%=ListaTitoli.size()%>">
  
  <table>
       <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>
          <font class=campo>Elenco Titoli associati all'Istruttoria </font>
        </td>
      </tr>
    </table>
    <br>
    
     <!--				Elenco Titoli				-->  

  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="titolo" colspan=3 width=100%>Elenco Titoli in Istruttoria</td>
    </tr>
  </table>  
  
  <table>
    <tr>
      <td class="int" width="8%"  >Titolo</td>
      <td class="int" width="10%" >Data Titolo </td>
      <td class="int" width="8%"  >Numero sentenza</td>
      <td class="int" width="18%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="8%"  >Numero SIEP</td>
      <td class="int" width="25%" >Ufficio Esecuzione</td>
      <td class="int" width="5%"  >Azioni</td>
    </tr>
<%
	String NumAutoritaSiep = "";
	Iterator itx = ListaTitoli.iterator();
	while ( itx.hasNext())
	{
	    TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
	    NumAutoritaSiep = "";
%>
    <tr>
      <td class="L">
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" nowrap>
   <% if (lTitoCum.getProcedimentoCumulato()!=null) 
      {   
      	if("S".equals(lTitoCum.getProcedimentoCumulato().getFlagAccorpato()) )
       	{
       		UfficioModel lUfficioOrigine = lTitoCum.getProcedimentoCumulato().getUfficioOrigine();
       		NumAutoritaSiep = lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato() +"/"+ lTitoCum.getProcedimentoCumulato().getChiaveProgrOrigine();
       		NumAutoritaSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>";   %>

			<%=NumAutoritaSiep%>
<%  	}
   		else
   		{	%>	
      		
   			<%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoCum.getProcedimentoCumulato().getChiaveProgrFasCumulato())%>

<% 		}
      }	
      else 
      {   %>
       	  &nbsp;
  <%  } 	%>
      </td>
      <td class="C" >
<% 	  if (lTitoCum.getProcedimentoCumulato()!=null) { %>
      	<%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato() )%>
<% 	  } else { %>
        &nbsp;
   <% } %>
      </td>

	    <td class="C" >      
          <a href="Javascript:insertIT( '<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())%>', 
        							  '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoProvvedimento())%>',
        							  '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrTipoProvvedimento() ),"-") %>',
        							  '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"),"")%>',
        							  '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"),"")%>',
        							  '<%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>',
        							  '<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza())%>',
        							
        							  '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrTipoAutoritaEmittente() ),"-") %>',
        							  '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrLuogoEmittente() ),"-") %>',
        							
        							  '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoAutoritaEmittente())%>',
        							  '<%=StringUtils.toStringJSP(lTitoCum.getCodLuogoEmittente())%>'
        							);">
            <img align="middle" src="/images/fileselected.gif" border=0>
          </a>
        </td>

    </tr>
<%	} %>				
    </table>
  </form>
  </body>
</html>