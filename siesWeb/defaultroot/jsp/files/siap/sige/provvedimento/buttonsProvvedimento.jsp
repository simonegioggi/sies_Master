<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>

<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile"  scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<table>
    <tr>

<%
	// Flag di abilitazione del bottone di modifica
	boolean abilitaModifica = true;

	String modifica = request.getParameter("Modifica");
  if( modifica!= null && modifica.compareTo("NO")==0 )
  	abilitaModifica = false;
    	
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

  //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();
        FunctionModel lFun = null;
        
        while(lIterBottoni.hasNext())
        {
          lFun = (FunctionModel)lIterBottoni.next();
          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
						if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO) )
            {   
              // Nota : 
              // Con l'introduzione del CodTipoProvvedimentoSige 
              // è stata migliorata la gestione della azioni di dettaglio da innescare.
              //
 							// I Parametri che pilotano la selezione sono: 
              // - CodTipoProvvedimentoSige 
              // - VisualizationOrder

              // Ordinanza Generica
							if ((request.getParameter("CodTipoProvvedimentoSige").compareTo("03")==0 )&&
            			lFun.getVisualizationOrder().intValue()==2 )
            	{%>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio ordinanza" border="0">
                	</a>
              	</td>
          	<%
	          		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	          		siesLogger.info( "####### Ordinanza Generica ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
          		}

              // Fissazione Udienza
            	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("01")==0 && 
            					 lFun.getVisualizationOrder().intValue()==1 )
            	{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  siesLogger.info( "####### Fissazione Udienza ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
            %>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%>&rinvio=<%=request.getParameter("rinvio")%>&Cancellabile=<%=request.getParameter("Cancellabile")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
                	</a>
              	</td>
          	<%
          		}
							// Ordinanza Rinvio Udienza
            	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("04")==0 &&
     					 					lFun.getVisualizationOrder().intValue()==3 )
            	{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  siesLogger.info( "####### Ordinanza Rinvio Udienza ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
            %>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
                	</a>
              	</td>
          	<%
          		}							
							// Decreto di Inammissibilità
            	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("05")==0 &&
      					 lFun.getVisualizationOrder().intValue()==2 )
            	{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  siesLogger.info( "####### Decreto di inammissibilità ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
            %>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
                	</a>
              	</td>
          	<%
          		}
							// Ordinanza NDP/NLP
            	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("06")==0 &&
      					 lFun.getVisualizationOrder().intValue()==2 )
            	{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  siesLogger.info( "####### Ordinanza NDP/NLP ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
            %>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
                	</a>
              	</td>
          	<%
          		}

							// Ordinanza Incompetenza
            	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA)==0 &&
      					 lFun.getVisualizationOrder().intValue()==2 )
            	{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  siesLogger.info( "####### Ordinanza Incompetenza ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
            %>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
                	</a>
              	</td>
          	<%
          		}
              
							// Ordinanza Sospensione
            	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE)==0 &&
      					 lFun.getVisualizationOrder().intValue()==2 )
            	{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  siesLogger.info( "####### Ordinanza Sospensione ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );

								// Forza l'avvio dell'azione di dettaglio relativa alla sospensione 
 						    lFun.setNameAction("siap.sige.provvedimento.action.ActDettaglioOrdinanzaSospensione");

            %>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
                	</a>
              	</td>
          	<%
          		}
              // Rinvio udienza da Verbale
            	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("50")==0 &&
      					 				lFun.getVisualizationOrder().intValue()==8 )
            	{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  siesLogger.info( "####### Rinvio Udienza da Verbale ######" );
						  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
								// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
            %>
              	<td>
                	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                  	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
                	</a>
              	</td>
          	<%
          		}	
              
           // Richiesta Rogatoria MdS
          	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("08")==0 &&
    					 lFun.getVisualizationOrder().intValue()==9 )
          	{
          	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          	  siesLogger.info( "####### Richiesta Rogatoria MdS ######" );
					  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
          %>
            	<td>
              	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
              	</a>
            	</td>
        	<%}
          // Decreto di Unificazione
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("55")==0 &&
  					 lFun.getVisualizationOrder().intValue()==12 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Decreto di Unificazione ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
        %>
       	<td>
         	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
           	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
         	</a>
       	</td>
      	<%
      		}	
          // Decreto di Latitanza
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("09")==0 &&
  					 lFun.getVisualizationOrder().intValue()==15 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Decreto di Latitanza ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
        %>
          	<td>
            	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
              	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
            	</a>
          	</td>
      	<%
      		}	
          // Ordinanza Conflitto di Competenza
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("18")==0 &&
  					 lFun.getVisualizationOrder().intValue()==18 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Ordinanza Conflitto di Competenza ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
        %>
          	<td>
            	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
              	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
            	</a>
          	</td>
      	<%
      		}	
					// Decreto di Irreperibilità
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("12")==0 &&
  					 lFun.getVisualizationOrder().intValue()==16 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Decreto di Irreperibilità ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
        %>
          	<td>
            	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
              	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
            	</a>
          	</td>
      	<%
      		}
					// Decreto di Nomina Periti
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("13")==0 &&
  					 lFun.getVisualizationOrder().intValue()==17 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Decreto di Nomina Periti ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
         %>
          	<td>
            	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
              	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
            	</a>
          	</td>
      	<%
      		}
				// Decreto di Citazione Testi
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("17")==0 &&
  					 lFun.getVisualizationOrder().intValue()==14 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Decreto di Citazione Testi ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
       %>
          	<td>
            	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
              	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
            	</a>
          	</td>
      	<%
      		}
		      // Ordine di Traduzione
          	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("53")==0 &&
    					 lFun.getVisualizationOrder().intValue()==19 )
          	{
          	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          	  siesLogger.info( "####### Ordine di Traduzione ######" );
					  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
							// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
          %>
            	<td>
              	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
                	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
              	</a>
            	</td>
        	<%}
              
          // Verbale di Unificazione
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("56")==0 &&
  					 lFun.getVisualizationOrder().intValue()==13 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Verbale di Unificazione ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
        %>
          	<td>
            	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
              	<img src="/images/dettagli.gif" width="12" height="12" alt="<%=lFun.getDescription()%>" border="0">
            	</a>
          	</td>
      	<%
      		}	

		    // Modifica del 02/03/2016 Nuova Infrastruttura - INIZIO ******  
            // Definizione Manuale
        	else if (request.getParameter("CodTipoProvvedimentoSige").compareTo("62")==0 &&
  					 lFun.getVisualizationOrder().intValue()==0 )
        	{
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.info( "####### Definizione Manuale ######" );
			  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			  siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
			  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			  siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
	    	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    	  siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
        %>
          	<td>
            	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEvento=<%=request.getParameter("IdEvento")%><%=retParam%>">
              	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Definizione Manuale" border="0">
            	</a>
          	</td>
      	<%
      		}
		    // Modifica del 02/03/2016 Nuova Infrastruttura - FINE ******
              
            // Allegati
			if (lFun.getVisualizationOrder().intValue()==5 && 
					   	(request.getParameter("Allegato").compareTo("SI")==0))
            {
            // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.info( "####### Allegati ######" );
				  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
						// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
				%>
            <td>
              <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimentoSige")%>&IdEventoGenerato=<%=request.getParameter("IdEvento")%><%=retParam%>">
                <img src="/images/attach.gif" width="12" height="12" alt="Allegati" border="0">
              </a>
            </td>
				<%          	
					}
				}
        else
        {
          // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.info( "####### DETTAGLIO NON TRATTATO ######" );
			  	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			  	siesLogger.info( ">>> CodTipoProvvedimentoSige = " + request.getParameter("CodTipoProvvedimentoSige") );
					// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.info( ">>> lFun.getVisualizationOrder() = " + lFun.getVisualizationOrder());
					// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.info( ">>> lFun.getNameAction() = " + lFun.getNameAction() );
        }

				if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && abilitaModifica )
        {%>
            <td>
              <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
              </a>
            </td>
			<%}
      	// ANNULLAMENTO
      	if ( lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)  && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null &&  request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null && 
      	     request.getParameter("Depositato") != null  && 
      	     request.getParameter("Depositato").equalsIgnoreCase("SI"))
 				{%>
   				<td>
     				<a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=request.getParameter("IdEvento")%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>');">
							<img src="/images/delete.gif" width="12" height="12" alt="Annulla" border="0">
     				</a>
   				</td>
			<%}
				// VISUALIZZA STAMPA
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) && 
        	    (request.getParameter("Stampa").compareTo("SI")==0))
        {%>
          	<td>
            	<a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=request.getParameter("IdEvento")%>')">
              	<img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
            	</a>
          	</td>
			<%}
      }  // IF FUNZIONE_BOTTONE
    }  // end WHILE
  }
%>
    </tr>
  </table>