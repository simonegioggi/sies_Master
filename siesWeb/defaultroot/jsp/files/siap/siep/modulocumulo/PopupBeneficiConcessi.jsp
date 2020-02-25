<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>

<jsp:useBean id="TitoliConBenefici" 	scope="request" class="java.util.Vector" />
<jsp:useBean id="tipoFormBeneficio" 	scope="request" class="java.lang.String"/>

<%
//==================================================================================
// Form di popup per l'elenco Titoli con benefici associati all'Istruttoria Corrente
// l'Utente seleziona poi il Beneficio che vuole REVOCARE
// (In particolare Revoca SOSPENSIONE CONDIZIONALE ALTRO PROVVEDIMENTO) 
//==================================================================================
%>

<!-- 		PopupBeneficiConcessi		 -->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <% if(tipoFormBeneficio.compareTo("01")==0 ){ %>
    <title>[S.I.E.S.] - Revoca Sospensione/Non Menzione - Elenco dei Titoli Associati </title>
    <% } else if(tipoFormBeneficio.compareTo("02")==0 ){ %>
    <title>[S.I.E.S.] - Revoca Indulto - Elenco dei Titoli Associati </title>
    <% } %>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      window.focus();
      
      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !TitoliConBenefici.isEmpty() )
          esistonoDati = true;
%>
		
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");
          window.parent.close();
        }
      }

      function insertIT( CodLuogoEmittente,
                         NumSezione,
                         CodTipoAutoritaEmittente,
      					 GiornoProvvedimento,
      					 MeseProvvedimento,
      					 AnnoProvvedimento,
      					 GiornoIrrevocab,
      					 MeseIrrevocab,
      					 AnnoIrrevocab,
      					 DescrTipoProvv,
      					 AnnoSentenza,
      					 NumeroSentenza,
      					 CodTipoBene,
      					 CodDpr,
      					 IdBeneficio,
      					 IdTitoloConcesso
      					 )
            						
      {

      	formname = '<%=request.getParameter("formname")%>';

        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_LUOGO_EMITTENTE%>.value=CodLuogoEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_NUM_SEZIONE_AUTORITA_EMITTENTE%>.value=NumSezione;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_AUTORITA_EMITTENTE%>.value=CodTipoAutoritaEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_PROVVEDIMENTO%>.value=GiornoProvvedimento;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_PROVVEDIMENTO%>.value=MeseProvvedimento;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_PROVVEDIMENTO%>.value=AnnoProvvedimento;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_GIORNO_IRREVOCABILITA%>.value=GiornoIrrevocab;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_MESE_IRREVOCABILITA%>.value=MeseIrrevocab;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_IRREVOCABILITA%>.value=AnnoIrrevocab;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_COD_TIPO_PROVVEDIMENTO%>.value=DescrTipoProvv;
        
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_ANNO_SENTENZA%>.value=AnnoSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_RIF_NUMERO_SENTENZA%>.value=NumeroSentenza;
        
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO%>.value=CodTipoBene;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_COD_DPR%>.value=CodDpr;

        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_DA_REVOCARE %>.value=IdBeneficio;
        
        if(CodTipoBene=='01')
        {
        	// Checkbox per selezione Sospensione Condizionale
        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.disabled = false;
        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked = true;
       		window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.disabled = true;
       		window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked = false;
        }
        
        if(CodTipoBene=='02')
        {
        	// Checkbox per selezioneNon Menzione
        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.disabled = false;
       		window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_NON_MENZIONE %>.checked = true;
       		window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.disabled = true;
       		window.parent.opener.document.<%=request.getParameter("formname")%>.<%= ICostantiBeneficiCumulo.CAMPO_FLAG_SOSP_COND %>.checked = false;
        }
        
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficiCumulo.CAMPO_TIT_ID_TITOLO_CUMULO_COLLEGATO%>.value=IdTitoloConcesso;
        window.parent.close();
    
      }     // chiude InsertIT                            
 
  	</script>
  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="ListaProcAssociati" action="<%=IWebConstants.PG_MAIN%>">
  	<table>
    	<tr>
      		<td class="LBG">
        	<a href="Javascript:window.print();">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        	</a>
      		</td>
      		
      		<td class="LBG">
        		<font class="label">Funzione :</font>
       <%		if(tipoFormBeneficio.compareTo("01")==0 )
      	 		{ 	%>
      	 		<font class="campo">ELENCO PROCEDIMENTI DI SOSPENSIONE CONDIZIONALE/NON MENZIONE</font>
      	<% 		}
       			else if(tipoFormBeneficio.compareTo("02")==0) 
       			{	%> 		
        		<font class="campo">ELENCO PROCEDIMENTI DI INDULTO </font>
        <%		} %>		
       		</td>   
		</tr>
	</table>
  	<br>	
      <jsp:include page="/jsp/files/siap/sico/soggetto/DettaglioSoggettoperRevoche.jsp"/>
   
  <br>
  <table cellspacing=2 cellpadding=2 width=100%>
  	<!--  tr><td class="titolo" colspan=3 width=100%>Elenco Procedimenti Associati Al Soggetto Del Distretto</td></tr -->
  	<tr><td class="titolo" colspan=3 width=100%>Elenco Titoli con Benefici Associati Al Soggetto in Istruttoria</td></tr>
  </table>
  
  <table>
  	<tr>
  		<td class="int" width=8% colspan=4>Titolo</td>
      	<td class="int" width=10% colspan=4>Data Titolo </td>
      	<td class="int" width=8% colspan=4>Numero sentenza</td>
      	<td class="int" width=9% colspan=4>Tipo Beneficio</td>
      	<td class="int" width=18% colspan=4>Autorità Titolo Esecutivo</td>
      	<td class="int" width=10% colspan=4>Data Irrevocabilita</td>
      	<td class="int" width=8% colspan=4>Numero SIEP</td>
      	<td class="int" width=25% colspan=4>Ufficio Esecuzione</td>
      	<td class="int" width=5% colspan=4>Azioni</td>
    </tr>
<%
	TitoloCumulatoModel lTitoCum = new TitoloCumulatoModel();

	Iterator itx = TitoliConBenefici.iterator();

	while ( itx.hasNext())
    {
		lTitoCum = (TitoloCumulatoModel)itx.next();
		String lTipoBene="";
     	if(lTitoCum.getBeneficioCumulato()!=null && lTitoCum.getBeneficioCumulato().getCodTipoBeneficio()!=null)
     	{
     		if(lTitoCum.getBeneficioCumulato().getCodTipoBeneficio().compareTo("01")==0)
     			lTipoBene = "Sospensione";
     		else if(lTitoCum.getBeneficioCumulato().getCodTipoBeneficio().compareTo("02")==0)
     			lTipoBene = "Non Menzione";
     		else if(lTitoCum.getBeneficioCumulato().getCodTipoBeneficio().compareTo("03")==0)
     			lTipoBene = "Indulto";
     	}	
%>        
      		<tr>
      			<td class=L colspan=4>
     			<%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
   				</td>		
   				<td class=C colspan=4>
     			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
   				</td>
   				<td class=C colspan=4>
     			<%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
   				</td>
   				<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(lTipoBene)%> 
        		</td>
   				<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
        		</td>
        		<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
        		</td>
        		<td class=C colspan=4>
              <% if (lTitoCum.getProcedimentoCumulato()!=null && lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato()!=null) { %>
              <%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoCum.getProcedimentoCumulato().getChiaveProgrFasCumulato())%>
              <% } else { %>
              &nbsp;n.d.
              <% }%>
        </td>
        		<td class=C colspan=4>
              <% if (lTitoCum.getProcedimentoCumulato()!=null && lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()!=null) { %>
          		<%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato() )%>
              <% } else { %>
              &nbsp;n.d.
              <% }%>
				</td>		
<%			if(lTitoCum.getBeneficioCumulato().getTitIdTitoloCumulatoCollegato()!=null)
			{ 	%>
				<td class="c">
					<font class="cRosso"> Revocato </font>
				</td>	 
<%			}
			else
			{	%>
        		<td class="C">
            		<a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lTitoCum.getDescrLuogoEmittente())%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getNumSezioneAutoritaEmittente())%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoAutoritaEmittente())%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd"))%>',
        		    							 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"MM"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"yyyy"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"MM"))%>',
      		      								 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"yyyy"))%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoProvvedimento())%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"")%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getBeneficioCumulato().getCodTipoBeneficio(),"")%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getBeneficioCumulato().getCodDpr(),"" )%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getBeneficioCumulato().getIdBeneficioCumulo(),"")%>',
            									 '<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())%>'
            									 );">
              		<img align="middle" src="/images/fileselected.gif" border=0>
            		</a>
        		</td>
<%			} %>        		                                        
      	</tr>
<%  
    } // chiude while
%>
    </table>
  </form>
  
</body>
</html>