<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.sentenza.model.SentenzaFascicoloModel" %>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>

<jsp:useBean id="fascicolo" 			scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="lSentenzaFascicolo" 	scope="request" class="java.util.Vector" />

<%
//==============================================================================
// Form di popup per l'elenco dei procedimenti associati
// (In particolare Revoca SOSPENSIONE INDULTO ALTRO PROVVEDIMENTO) 
//==============================================================================
//

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Revoca Indulto - Elenco dei Procedimenti Associati </title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !lSentenzaFascicolo.isEmpty() )
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
      					 CodDpr,
      					 IdProvvRif
      					 )
            						
      {
      
      	formname = '<%=request.getParameter("formname")%>';
    	    	
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_COD_LUOGO_EMITTENTE%>.value=CodLuogoEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE%>.value=NumSezione;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=CodTipoAutoritaEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_GIORNO_PROVVEDIMENTO%>.value=GiornoProvvedimento;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_MESE_PROVVEDIMENTO%>.value=MeseProvvedimento;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_ANNO_PROVVEDIMENTO%>.value=AnnoProvvedimento;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_GIORNO_IRREVOCABILITA%>.value=GiornoIrrevocab;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_MESE_IRREVOCABILITA%>.value=MeseIrrevocab;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_ANNO_IRREVOCABILITA%>.value=AnnoIrrevocab;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=DescrTipoProvv;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_COD_DPR%>.value=CodDpr;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiBeneficio.CAMPO_SEN_ID_SENTENZA%>.value=IdProvvRif;
        window.parent.close();
    
      }     // chiude InsertIT                            
 
  	</script>
  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="ListaProcAssociatiIndu" action="<%=IWebConstants.PG_MAIN%>">
  	<table>
    	<tr>
      		<td class="LBG">
        	<a href="Javascript:window.print();">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        	</a>
      		</td>
      		
      		<td class="LBG">
        		<font class="label">Funzione :</font>
        		<font class="campo">
          			ELENCO DEI PROCEDIMENTI CON INDULTO  
        		</font>
       		</td>   
		</tr>
	</table>
  
      <jsp:include page="/jsp/files/siap/sico/soggetto/DettaglioSoggettoperRevoche.jsp"/>
   
  <br>
  <table cellspacing=2 cellpadding=2 width=100%>
  	<tr><td class="titolo" colspan=3 width=100%>Elenco Procedimenti Associati Al Soggetto Del Distretto</td></tr>
  </table>
  
  <table>
  	<tr>
      	<td class="int" width=10% colspan=4>Data Titolo Esecutivo</td>
      	<td class="int" width=10% colspan=4>Autorità Titolo Esecutivo</td>
      	<td class="int" width=10% colspan=4>Data Irrevocabilita</td>
      	<td class="int" width=10% colspan=4>Numero SIEP</td>
      	<td class="int" width=20% colspan=4>Ufficio Esecuzione</td>
      	<td class="int" width=10% colspan=4>Data di Iscrizione</td>
      	<td class="int" width=10% colspan=4>Stato del Procedimento</td>
      	<td class="int" width=10% colspan=4>Azioni</td>
    </tr>
<%
    SentenzaFascicoloModel lSenFasMod = new SentenzaFascicoloModel();

	Iterator itx = lSentenzaFascicolo.iterator();

	while ( itx.hasNext())
    {
    	lSenFasMod = (SentenzaFascicoloModel)itx.next();
    	
       	if(lSenFasMod.getIdFascicoloSiep().intValue()!= fascicolo.getIdFascicoloSiep().intValue())
       	{	
%>        
      		<tr>		
   				<td class=L colspan=4>
     			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataProvvedimento(),"dd-MM-yyyy"))%>
   				</td>
   				<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(lSenFasMod.getDescrTipoAutoritaEmittente()+" "+lSenFasMod.getDescrLuogoEmittente())%> 
        		</td>
        		<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
        		</td>
        		<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(lSenFasMod.getChiaveAnnoFascicolo()+"/"+lSenFasMod.getChiaveNumeroFascicolo())%>
				</td>
        		<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(lSenFasMod.getDescrChiaveUfficio()+" "+lSenFasMod.getDescrComune())%>
				</td>		
				<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataIscrizione(),"dd-MM-yyyy"))%> 
        		</td>
				<td class=C colspan=4>
          		<%=StringUtils.toStringJSP(lSenFasMod.getDescrStatoFasc())%> 
        		</td>
        		<td class="C">
            		<a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lSenFasMod.getDescrLuogoEmittente())%>',
            									 '<%=StringUtils.toStringJSP(lSenFasMod.getNumSezioneAutoritaEmittente())%>',
            									 '<%=StringUtils.toStringJSP(lSenFasMod.getCodTipoAutoritaEmittente())%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataProvvedimento(),"dd"))%>',
        		    							 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataProvvedimento(),"MM"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataProvvedimento(),"yyyy"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataIrrevocabilita(),"dd"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataIrrevocabilita(),"MM"))%>',
      		      								 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSenFasMod.getDataIrrevocabilita(),"yyyy"))%>',
            									 '<%=StringUtils.toStringJSP(lSenFasMod.getCodTipoProvvedimento())%>',
            									 '<%=StringUtils.toStringJSP(lSenFasMod.getCodDpr())%>',
            									 '<%=StringUtils.toStringJSP(lSenFasMod.getIdSentenza())%>'
            									 );">
              		<img align="middle" src="/images/fileselected.gif" border=0>
            		</a>
        		</td>                                        
      	</tr>
<%  
    	} // Chiude If
     
    } // chiude while
%>
    </table>
  </form>
  
</body>
</html>