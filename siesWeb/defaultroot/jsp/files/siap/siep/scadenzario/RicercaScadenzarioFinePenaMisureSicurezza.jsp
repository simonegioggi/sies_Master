<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: modificata pagina RicercaScadenzarioFinePenaMisureSicurezza --%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>

<%@ page import="java.lang.String"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>
<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.util.ScadenzarioUtils"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>

<jsp:useBean id="tipo" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="giorniScadenza"	scope="request" class="java.lang.String"/>
<jsp:useBean id="mesiScadenza"		scope="request" class="java.lang.String"/>
<jsp:useBean id="anniScadenza"		scope="request" class="java.lang.String"/>
<jsp:useBean id="giorni"			scope="request" class="java.lang.String"/>
<jsp:useBean id="mesi"				scope="request" class="java.lang.String"/>
<jsp:useBean id="anni"				scope="request" class="java.lang.String"/>

<html>
  	<head>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<title>[S.I.E.S.] - Elenco - Consultazione Scadenzario - Data Scadenza Comunicazione Misura Sicurezza</title>
    	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	</head>

  	<BODY class="corpo">
  		<FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
   		<table>
     			<tr>
     				<td class="LBG">
     					<a href="Javascript:window.print();">
     						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
     					</a>
     				</td>
	       			<td class="LBG">
	       				<font class="label">Funzione: </font>
	       				<font class="campo">Consultazione Scadenzario - Data Scadenza Comunicazione Misura Sicurezza - <%=titolo%></font>
	       			</td>
	       			<td class="LBG">
	            		<a href="javascript:history.go(-1);">
	             			<img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	            		</a>
	          		</td>
	          		<td class="LBG">
	            		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActStampaScadenzarioComunicazioneScadenzaMS&tipo=<%=tipo%>&giorniScadenza=<%=giorniScadenza%>&mesiScadenza=<%=mesiScadenza%>&anniScadenza=<%=anniScadenza%>">
	             			<img align="middle" src="/images/xls.jpg" alt="scarica in excel" width="24" height="24" border="0">
	            		</a>
	          		</td>
     			</tr>
   		</table>
   		<br>
   		<table cellpadding="2" cellspacing="2" width="100%">
<%
String a = anni;
String m = mesi;
String g = giorni;
String periodo = "";
if (!"0".equals(a))
	periodo += a + " ANNI";
if (!"0".equals(m))
	if (periodo.length() > 1)
		periodo += " " + m + " MESI";
	else
		periodo += m + " MESI";
if (!"0".equals(g))
	if (periodo.length() > 1)
		periodo += " " + g + " GIORNI";
	else
		periodo += g + " GIORNI";
%>
   			<tr>
   				<td class="campo">
   					QUANTUM IMPOSTATO PER DATA SCADENZA COMUNICAZIONE MISURA DI SICUREZZA:&nbsp;
					<%=periodo%>
   				</td>
   			</tr>
   		</table>
   		<br>
   		<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
   		<br>
   		<table cellpadding="2" cellspacing="2" width="100%">
<%
List scadenzario = (List) request.getAttribute("scadenzario");
if ("Tutti".equals(tipo)) {
%>
       		<tr>
				<td class="campo" colspan="2"><img src="/images/QuadratinoVerde.gif">
					<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActRicercaScadenzarioFinePenaMisureSicurezza&tipo=sette&<%=ICostantiScadenzario.CAMPO_GIORNI_SCADENZA%>=<%=new BigDecimal(7)%>">In Scadenza</a>
		        </td>
		        <td class="campo" colspan="3"><img src="/images/QuadratinoRosso.gif">
		        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActRicercaScadenzarioFinePenaMisureSicurezza&tipo=oggi">In Scadenza Oggi</a>
		        </td>
		        <td class="campo" colspan="2"><img src="/images/QuadratinoGrigio.gif">
		        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActRicercaScadenzarioFinePenaMisureSicurezza&tipo=scaduto">Scaduto</a>
		        </td>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
		        <td>&nbsp;</td>
       		</tr>
<%
}
%>
   			<tr>
				<td class="int" width="10%">N° SIEP</td>
				<td class="int" width="10%">N° SIEP Collegato</td>
				<td class="int">Cognome</td>
				<td class="int">Nome</td>
				<td class="int" width="10%">Data Inizio Pena</td>
<%
if (!"oggi".equals(tipo)) {
%>
       			<td class="int" width="10%">Data Fine Pena</td>
       			<td class="int" width="10%">Data Scadenza Comunicazione</td>
       			<td class="int" width="15%">Tipologia Misura Sicurezza</td>
<%
	if (!"scaduto".equals(tipo)) {
%>
           		<td class="int" width="10%">N° Giorni Residui</td>
<%
	}
}
%>
				<td class="int">Visto</td>
				<td class="int">Azioni</td>
   			</tr>
<%
Iterator itx = scadenzario.iterator();
while (itx.hasNext()) {
	ScadenzarioModel lSca = (ScadenzarioModel) itx.next();
	FascicoloSiepModel lFas = lSca.getFascicoloModel();
	SoggettoModel lSog = lFas.getSoggetto();
%>
   			<tr>
<%
	if ("Tutti".equals(tipo)) {
      	if (lSca != null
      			&& lSca.getGiorniResidui() != null
      			&& lSca.getGiorniResidui().intValue() == 0) {
%>
				<td class="crosso">       				
         				<%=lFas.getChiaveAnno()%>
         				/
         				<%=lFas.getChiaveProgr()%>       				
       			</td>
<%
      		if (lSca.getFascMsToFascSiep() != null && lSca.getFascMsToFascSiep().getFasSieIdFascicoloCollegato() != null) {
%> 
      	  		<td class="crosso">               		
              			<%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
              			/
              			<%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>             		
             	</td>
<%
			} else if (lSca.getFascMsToFascSiep() != null  && lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato() !=null) {
%>
      			<td class="crosso"> 
      				<%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
              		/
              		<%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>
              	</td>
<%
			} else {
%>       
      	  		<td class="C">&nbsp;</td>  
<%
			}
%>
				<td class="crosso"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
				<td class="crosso"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
				<td class="crosso"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
				<td class="crosso"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
<%-- MEV_39: modificato campo in estrazione
if (lSca != null && lSca.getDataFineScadenza() != null)
	Date Data_Inizio_Mis = DateUtils.moveDateTo(lSca.getDataFineScadenza(), Calendar.MONTH, -6);
--%>
<%
			if (lSca != null && lSca.getDataScadenzaComunicazione() != null) {
%>
				<td class="crosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataScadenzaComunicazione(),"dd/MM/yyyy"))%></td>
<%-- 				<td class="crosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Data_Inizio_Mis,"dd/MM/yyyy"))%></td> --%>
<%
			} else {
%>
				<td class="C">&nbsp;</td>
<%
			}
%>
				<td class="crosso"><%=StringUtils.toStringJSP(lSca.getDescrTipoMS())%></td>
<%
			if (lSca.getDataFineScadenza() != null) {
%> 
          		<td class="crosso" nowrap><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
			} else {
%>
				<td class="C">&nbsp;</td>
<%
			}
   		} else {
   			if (lSca != null
   					&& lSca.getGiorniResidui() != null
   					&& lSca.getGiorniResidui().intValue() <= 7
   					&& lSca.getGiorniResidui().intValue() > 0) {
%>
				<td class="cverde">
              		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
		                <%=lFas.getChiaveAnno()%>
		                /
		                <%=lFas.getChiaveProgr()%>
              		</a>
            	</td>
<%
   				if (lSca.getFascMsToFascSiep() != null && lSca.getFascMsToFascSiep().getFasSieIdFascicoloCollegato() != null) {
%>
	    	  	<td class="cverde"> 	           		
	            		<%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
	           			/
	            		<%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>	           		
	           	</td>
<%
				} else if (lSca.getFascMsToFascSiep() != null  && lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato() !=null) {
%>
	    		<td class="cverde"> 
	    			<%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
	            	/
	            	<%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>
				</td>
<%
				} else {
%>       
	    	  	<td class="C">&nbsp;</td>  
<%
				}
%>
				<td class="cverde"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
				<td class="cverde"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
				<td class="cverde"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
				<td class="cverde"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
<%-- MEV_39: modificato campo in estrazione
if (lSca != null && lSca.getDataFineScadenza() != null)
	Date Data_Inizio_Mis = DateUtils.moveDateTo(lSca.getDataFineScadenza(), Calendar.MONTH, -6);
--%>
<%
				if (lSca != null && lSca.getDataScadenzaComunicazione() != null) {
%>
				<td class="cverde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataScadenzaComunicazione(),"dd/MM/yyyy"))%></td>
<%--   				<td class="cverde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Data_Inizio_Mis,"dd/MM/yyyy"))%></td> --%>
<%
				} else {
%>
				<td class="C">&nbsp;</td>
<%
				}
%>
				<td class="cverde"><%=StringUtils.toStringJSP(lSca.getDescrTipoMS())%></td>
<%
				if (lSca.getDataFineScadenza() != null) {
%>
				<td class="cverde" nowrap><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
				} else {
%>
				<td class="C">&nbsp;</td>
<%
				}
       		} else {
       			if (lSca != null && lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() < 0) {
%>
				<td class="cgrigio">
	              	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
		            	<%=lFas.getChiaveAnno()%>
		                /
		                <%=lFas.getChiaveProgr()%>
	              	</a>
				</td>
<%
       				if (lSca.getFascMsToFascSiep() != null && lSca.getFascMsToFascSiep().getFasSieIdFascicoloCollegato() != null) {
%>
				<td class="cgrigio">     					
			            <%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
			            /
			            <%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>		           	
           		</td>
<%
					} else if (lSca.getFascMsToFascSiep() != null && lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato() !=null) {
%>
				<td class="cgrigio"> 
		    		<%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
		            /
		            <%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>
	            </td>
<%
					} else {
%>       
				<td class="C">&nbsp;</td>  
<%
					}
%>
            	<td class="cgrigio"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
            	<td class="cgrigio"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
            	<td class="cgrigio"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
            	<td class="cgrigio"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
<%-- MEV_39: modificato campo in estrazione
if (lSca != null && lSca.getDataFineScadenza() != null)
	Date Data_Inizio_Mis = DateUtils.moveDateTo(lSca.getDataFineScadenza(), Calendar.MONTH, -6);
--%>
<%
					if (lSca != null && lSca.getDataScadenzaComunicazione() != null) {
%>
				<td class="cgrigio"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataScadenzaComunicazione(),"dd/MM/yyyy"))%></td>
<%--   				<td class="cgrigio"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Data_Inizio_Mis,"dd/MM/yyyy"))%></td> --%>
<%
					} else {
%>
				<td class="C">&nbsp;</td>
<%
					}
%>
				<td class="cgrigio"><%=StringUtils.toStringJSP(lSca.getDescrTipoMS())%></td>
<%
					if (lSca.getDataFineScadenza() != null) {
%>
				<td class="cgrigio" nowrap><%=ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(), lSca.getDataFineScadenza())%></td>
<%
					} else {
%>
				<td class="C">&nbsp;</td>
<%
					}
       			} else {
%>
				<td class="C">
	              	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
		                <%=lFas.getChiaveAnno()%>
		                /
		                <%=lFas.getChiaveProgr()%>
	              	</a>
	            </td>
<%
       				if (lSca.getFascMsToFascSiep() != null && lSca.getFascMsToFascSiep().getFasSieIdFascicoloCollegato() != null) {
%> 
				<td class="C">    		           	
			            <%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
			            /
			            <%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>		           	
				</td>
<%	
					} else if (lSca.getFascMsToFascSiep() != null  && lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato() !=null) {
%>
				<td class="C"> 
		    		<%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
		            /
		            <%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>
	            </td>
<%
					}  else {
%>       
				<td class="C">&nbsp;</td>  
<%
					}
%>
	            <td class="C"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
    	        <td class="C"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
            	<td class="C"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
            	<td class="C"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
<%-- MEV_39: modificato campo in estrazione
if (lSca != null && lSca.getDataFineScadenza() != null)
	Date Data_Inizio_Mis = DateUtils.moveDateTo(lSca.getDataFineScadenza(), Calendar.MONTH, -6);
--%>
<%
					if (lSca != null && lSca.getDataScadenzaComunicazione() != null) {
%>
				<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataScadenzaComunicazione(),"dd/MM/yyyy"))%></td>
<%--   				<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Data_Inizio_Mis,"dd/MM/yyyy"))%></td> --%>
  				<td class="C"><%=StringUtils.toStringJSP(lSca.getDescrTipoMS())%></td>
<%
						if (lSca.getDataFineScadenza() != null) {
%>
	            <td class="C"><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
						} else {
%>
				<td class="C">&nbsp;</td>
<%
						}
            		} else {
%>
				<td class="C">&nbsp;</td>
				<td class="C"><%=StringUtils.toStringJSP(lSca.getDescrTipoMS())%></td>
				<td class="C">&nbsp;</td>
<%
            		}
       			}
			}
		}
   	} // FINE TUTTI
   	else {
%>
				<td class="C">
          			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
			            <%=lFas.getChiaveAnno()%>
			            /
			            <%=lFas.getChiaveProgr()%>
          			</a>
        		</td>
<%
		if (lSca.getFascMsToFascSiep() != null && lSca.getFascMsToFascSiep().getFasSieIdFascicoloCollegato() != null) {
%> 
    	  		<td class="C">	               			
			            <%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
			            /
			            <%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>       
           		</td>
<%
		} else if (lSca.getFascMsToFascSiep() != null && lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato() !=null) {
%>
    			<td class="C"> 
   					<%=lSca.getFascMsToFascSiep().getChiaveAnnoSiepCollegato()%>
            		/
            		<%=lSca.getFascMsToFascSiep().getChiaveProgrSiepCollegato()%>
            	</td>
<%
		} else {
%>       
    	  		<td class="C">&nbsp;</td>  
<%
		}
%> 
		        <td class="C"><%=StringUtils.toStringJSP(lSog.getCognome())%></td>
		        <td class="C"><%=StringUtils.toStringJSP(lSog.getNome())%></td>
		        <td class="C"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy"))%></td>
<%
		if (!"oggi".equals(tipo)) {
%>
          		<td class="C"><%=StringUtils.cStrForJS(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy"))%></td>
<%-- MEV_39: modificato campo in estrazione
if (lSca != null && lSca.getDataFineScadenza() != null)
	Date Data_Inizio_Mis = DateUtils.moveDateTo(lSca.getDataFineScadenza(), Calendar.MONTH, -6);
--%>
<%
			if (lSca != null && lSca.getDataScadenzaComunicazione() != null) {
%>
				<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataScadenzaComunicazione(),"dd/MM/yyyy"))%></td>
<%--  				<td class="C"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Data_Inizio_Mis,"dd/MM/yyyy"))%></td> --%>
<%
			} else {
%> 
		     	<td class="C">&nbsp;</td>      
<%
			}
%>
				<td class="C"><%=StringUtils.toStringJSP(lSca.getDescrTipoMS())%></td>
<%
          	if (!"scaduto".equals(tipo)) {
				if (lSca.getDataFineScadenza() != null) {
%>
        		<td class="C"><%=ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate())%></td>
<%
				} else {
%>
				<td class="C">&nbsp;</td>
<%
				}
          	}
		}
	}
	if (lSca != null && lSca.getFlagVisto() != null && "S".equals(lSca.getFlagVisto())) {
%>
        		<td class="C"><img src="/images/TickRed.gif"></td>
<%
	} else {
%>
        		<td class="C">&nbsp;</td>
<%
	}
	if (lSca != null && lSca.getFasSieIdFascicoloSiep() != null) {
%>
      			<td class="C">
         			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActLoadDettaglioScadenzarioFinePenaMisureSicurezza&<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>=<%=lSca.getIdScadenzario()%>&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lSca.getFasSieIdFascicoloSiep()%>">
           				<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
         			</a>
      			</td>
<%	
	} else {
%>   
	  			<td class="C">
         			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActLoadDettaglioScadenzarioFinePenaMisureSicurezza&<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>=<%=lSca.getIdScadenzario()%>&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>">
           				<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
         			</a>
      			</td>
<%	
	}
%>
    		</tr>
<%
} // end while
%>
   		</table>
  		</FORM>
	</body>
</html>