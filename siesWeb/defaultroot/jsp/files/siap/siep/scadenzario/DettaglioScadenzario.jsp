<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="decretoIrreperibilita" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="scadenzario" scope="request" class="siap.siep.scadenzario.model.ScadenzarioModel" />
<jsp:useBean id="rinnovo" scope="request" class="siap.siep.rinnovo.model.RinnovoModel" />

<html>
	<head>
		<title>[S.I.E.S.] - Dettaglio Stato Notifiche </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>
<body class="corpo">
	<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        	<font class="label">Funzione :</font>&nbsp;
        	<font class="campo">Dettaglio Stato Notifiche</font>
      	</td>
 		
<%		if (scadenzario != null && scadenzario.getIdScadenzario() != null)
      	 	{
%>
	 			<td class="LBG">
		          	<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
		          	<jsp:param name="CampoIdEntita" value="<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>" />
		          	<jsp:param name="ValoreIdEntita" value="<%=scadenzario.getIdScadenzario()%>" />
		       		</jsp:include>
	     		</td>
<%
        }
%>
        <td class="LBG">
          <a href="javascript:history.go(-1);">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
     	</tr>
 		</table>
	</FORM>
<%
	ScadenzarioModel lSca = scadenzario;
	
	FascicoloSiepModel lFas = lSca.getFascicoloModel();
	SoggettoModel lSog = lFas.getSoggetto();
	SentenzaModel lSentMod = lFas.getSentenza();
%>
	<table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
          <%=lFas.getChiaveAnno()%>
          /
          <%=lFas.getChiaveProgr()%>
        </a>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lSog.getIdSoggetto()%>" title="Soggetto">
          <%=lSog.getCognome()%>&nbsp;<%=lSog.getNome()%>
        </a>
      </font>&nbsp;
<%
        if (lSog.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=DateUtils.getDateToString(lSog.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (lSog.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=lSog.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=lSog.getDescrComuneNascita()+ "  ("+lSog.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="campo"><%=lSentMod.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N.</font>
        <font class="campo">
          <%=lSentMod.getAnnoSentenza()%> / <%=lSentMod.getNumeroSentenza()%>&nbsp;
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=lSentMod.getIdSentenza()%>" title="Sentenza">
            <%=DateUtils.getDateToString(lSentMod.getDataProvvedimento(), "dd-MM-yyyy")%>
          </a>
        </font>
        <%if(!lSentMod.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
 		}else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=lSentMod.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
        if (lSentMod.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=lSentMod.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=lSentMod.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(lFas.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr>
  </table>
  <br>
<%
	if( lSca.getDataFineScadenza() != null )
	{
%>
		<table cellspacing=2 cellpadding=2>
			<tr>
				<td class="l">Data Ultima Scadenza</td>
				<td class="l"><font color="red"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %>&nbsp;</font></td>
			</tr>
		</table>
		<br>  
<%
	}

	if( eventonotifica.getEvento() != null && eventonotifica.getEvento().getIdEvento() != null )
	{
%>
	  <table cellspacing=2 cellpadding=2>
	    <tr>
	    <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
	      <td class="L">
	        Notifiche relative a: 
	        <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%> emesso in data <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
	      </td>
	    </tr>
	  </table>
	  <br>  
<%
	}

	NotificaModel[] lArrNot = eventonotifica.getNotifiche();
%>
	<table cellspacing=2 cellpadding=2>
<%
       for(int i = 0; i < lArrNot.length; i ++)
       {
         NotificaModel lNotMod = lArrNot[i];
				
         if( lNotMod != null && "E".equals(lNotMod.getCodTipoNotifica()) )
         {
	     		if( 	 decretoIrreperibilita.getEvento() == null 
	     		    || ( decretoIrreperibilita.getEvento() != null && lNotMod.getDataAvvenutaNotifica() != null ) )
	     	  {
%>
		        <tr><td class="titolo" colspan="2">Condannato</td></tr>
		        <tr>
		        	<td class="l">Destinatario</td>
<%
			          if(lNotMod.getAutoritaEsterna()!= null)
			          {
%>
				          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font></td>
<%
			          }
			          else if(lNotMod.getIstitutoDetenzione()!= null)
			          {
%>
			            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrComune())%></font></td>
<%
			          }              
%>
		          <tr>
		            <td class="l">Data Notifica</td>
<%
			            if(lNotMod.getDataAvvenutaNotifica() != null)
			            {            
%>
				            <td class="l"><font class="campo"><%=DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(),"dd/MM/yyyy")%></font></td>
<%
			            }
			            else
			            {
%>
				            <td class="l"><font color="red">Non Notificato</font></td>
<%
			            }
%>
		          </tr>
<%          
							if(lNotMod.getAutoritaEsternaDelegata() != null)
		          {
%>
								<tr>
									<td class="l">Autorità delegata per la notifica</td>
				       		<td class="l">
			          		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrSede())%></font>&nbsp;
					       	</td>
		            </tr>							
<%                
		          }
          	}
          }
          else if( lNotMod != null && "N".equals(lNotMod.getCodTipoNotifica()) )
          {
%>              
  	       <% // Paolo Cherubini 23/06/2011 commento e sposto sotto <tr><td class="titolo" colspan="2">Difensore</td></tr>
			  // <tr>
			  // <td class="l">Autorità delegata alla notifica</td>	%>			
<%
			if(lNotMod.getSogIdSoggetto() !=null)
			{
				if(lFas.getSoggetto() != null)
			    {
%>
					<td class="l"><font class="campo"><%=lFas.getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=lFas.getSoggetto().getNome()%></font></td>
					</tr>
<%
			     }
			 }else if(lNotMod.getUfficio()!= null)
			{
			
%>
 				<% // Paolo Cherubini 23/06/2011 Notifica Ente di Sorveglianza non deve essere visibile
 				   // <td class="l"><font class="campo"><%--=lNotMod.getUfficio().getDescrTipoUfficio()%-->&nbsp;di&nbsp; 
 				   // <--%=lNotMod.getUfficio().getDescrComune()%--></font></td>
	 			   // </tr> %>
<%    
			 }else if(lNotMod.getAutoritaEsterna()!= null)
			{
				 
%>
				 <tr><td class="titolo" colspan="2">Difensore</td></tr>
				 <tr>
				 <td class="l">Autorità delegata alla notifica</td>
				 
			     <td class="l"><font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrSede()%></font></td>
			     </tr>
<%
			}
           
            if(lNotMod.getAvvIdAvvocatoFascicoloSiep() != null)
            {
              if(lNotMod.getAvvSiep()!= null)
              {
%>
								<tr>
								  <td class="l">Notifica al difensore</td>
  	  						<td class="l">
  	  							<font class="campo">
  	  								<%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getCognome() +" "+lNotMod.getAvvSiep().getAvvocato().getNome())%>
  	  							</font>&nbsp;Foro di&nbsp;
  	  							<font class="campo">
  	  								<%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getForo())%>
  	  							</font>  	  						
  	  						</td>
								</tr>
								<tr>
                  <td class="l">Tipo Difensore</td>
                  <td class="l">
                  	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getDescrTipo())%></font>&nbsp;
                  </td>
                </tr>							
<%
            	}          
%>
              <tr>
	              <td class="l">Data Notifica</td>
<%
              if(lNotMod.getDataAvvenutaNotifica() != null)
              {           
%>
	  	          <td class="l"><font class="campo"><%=DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(),"dd/MM/yyyy")%></font></td>
<%
	            }
	            else
	            {
%>
		            <td class="l"><font color="red">Non Notificato</font></td>
<%
	            }
%>
	          	</tr>
<%
            
							if(lNotMod.getAutoritaEsternaDelegata() != null)
              {
%>
								<tr>
 		             <td class="l">Autorità che ha effettuato la notifica</td>
					       <td class="l">
                 	 <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrSede())%></font>&nbsp;
					       </td>
                </tr>							
<%                
              }

							if( 	 lNotMod.getAutoritaEsternaDelegata() != null
							    && lNotMod.getAutoritaEsternaDelegata().getDescrizione() != null
							    && !"".equals( lNotMod.getAutoritaEsternaDelegata().getDescrizione().trim()) )
							{
%>
								<tr>
									<td class="l">Indirizzo</td>
							 		<td class="l">
							  		<font class="campo"><%=StringUtils.toStringJSP(StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrizione()))%></font>
							   	</td>
							  </tr>							
<%                
							}
						}            
          }
       }
%>
  	 <tr><td>&nbsp;</td></tr>
	</table>
	
<%
		if( decretoIrreperibilita.getEvento() != null && decretoIrreperibilita.getEvento().getIdEvento() != null )
	  {
%>
	  <table cellspacing=2 cellpadding=2>
	    <tr>
	    <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
	      <td class="L">
	        Notifiche relative a: 
	        <font class="campo"><%=StringUtils.toStringJSP(decretoIrreperibilita.getEvento().getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(decretoIrreperibilita.getEvento().getDescrMotivo())%> emesso in data <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoIrreperibilita.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
	      </td>
	    </tr>
	  </table>
		<br>
		<table cellspacing=2 cellpadding=2>
<%
			 NotificaModel[] lArrNotDecrIrr = decretoIrreperibilita.getNotifiche();
       
			 for(int i = 0; i < lArrNotDecrIrr.length; i ++)
       {
         NotificaModel lNotMod = lArrNotDecrIrr[i];
				
         if( lNotMod != null && "N".equals(lNotMod.getCodTipoNotifica()) )
         {
%>              
  	      <tr><td class="titolo" colspan="2">Difensore</td></tr>
					<tr>
					<td class="l">Autorità delegata alla notifica</td>				
<%
						if(lNotMod.getSogIdSoggetto() !=null)
						{
			        if(lFas.getSoggetto() != null)
			        {
%>
								<td class="l"><font class="campo"><%=lFas.getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=lFas.getSoggetto().getNome()%></font></td>
								</tr>
<%
			        }
			     }
				   else if(lNotMod.getUfficio()!= null)
				   {
%>
 						 <td class="l"><font class="campo"><%=lNotMod.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=lNotMod.getUfficio().getDescrComune()%></font></td>
	 					 </tr>
<%    
					 }
				   else if(lNotMod.getAutoritaEsterna()!= null)
				   {
%>
			       <td class="l"><font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrSede()%></font></td>
			       </tr>
<%
				   }
               
            if(lNotMod.getAvvIdAvvocatoFascicoloSiep() != null)
            {
              if(lNotMod.getAvvSiep()!= null)
              {
%>
								<tr>
								  <td class="l">Notifica al difensore</td>
  	  						<td class="l">
  	  							<font class="campo">
  	  								<%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getCognome() +" "+lNotMod.getAvvSiep().getAvvocato().getNome())%>
  	  							</font>&nbsp;Foro di&nbsp;
  	  							<font class="campo">
  	  								<%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getForo())%>
  	  							</font>  	  						
  	  						</td>
								</tr>
								<tr>
                  <td class="l">Tipo Difensore</td>
                  <td class="l">
                  	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAvvSiep().getAvvocato().getDescrTipo())%></font>&nbsp;
                  </td>
                </tr>							
<%
            	}          
%>
              <tr>
	              <td class="l">Data Notifica</td>
<%
              if(lNotMod.getDataAvvenutaNotifica() != null)
              {            
%>
	  	          <td class="l"><font class="campo"><%=DateUtils.getDateToString(lNotMod.getDataAvvenutaNotifica(),"dd/MM/yyyy")%></font></td>
<%
	            }
	            else
	            {
%>
		            <td class="l"><font color="red">Non Notificato</font></td>
<%
	            }
%>
	          	</tr>
<%
            
							if(lNotMod.getAutoritaEsternaDelegata() != null)
              {
%>
								<tr>
 		             <td class="l">Autorità che ha effettuato la notifica</td>
					       <td class="l">
                 	 <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrSede())%></font>&nbsp;
					       </td>
                </tr>							
<%                
              }

							if( 	 lNotMod.getAutoritaEsternaDelegata() != null
							    && lNotMod.getAutoritaEsternaDelegata().getDescrizione() != null
							    && !"".equals( lNotMod.getAutoritaEsternaDelegata().getDescrizione().trim()) )
							{
%>
								<tr>
									<td class="l">Indirizzo</td>
							 		<td class="l">
							  		<font class="campo"><%=StringUtils.toStringJSP(StringUtils.toStringJSP(lNotMod.getAutoritaEsternaDelegata().getDescrizione()))%></font>
							   	</td>
							  </tr>							
<%                
							}
						}            
          }
       }
%>
 		<tr><td>&nbsp;</td></tr>
	</table>
		
<%
	}

	if( rinnovo.getIdRinnovo() != null )
  {
	  String lTitolo = "";
	  
    if(    "R".equals( rinnovo.getCodTipoRinnovo() )
        || "N".equals( rinnovo.getCodTipoRinnovo() )
        || "A".equals( rinnovo.getCodTipoRinnovo() )
       )
    {
      lTitolo = "Rinnovo ricerche per omesse notifiche";
    }
    else if(   "D".equals( rinnovo.getCodTipoRinnovo() )
        		|| "I".equals( rinnovo.getCodTipoRinnovo() )
           )
    {
      lTitolo = "Richiesta informazioni comma 8 bis";
    }
    else if(   "U".equals( rinnovo.getCodTipoRinnovo() )
    				|| "P".equals( rinnovo.getCodTipoRinnovo() )
       )
		{
		  lTitolo = "Rinnovazione notifica comma 8 bis";
		}
    else if( "S".equals( rinnovo.getCodTipoRinnovo() ) )
    {
      lTitolo = "Solleciti";
    }
	    
%>
		<table cellspacing=2 cellpadding="2">
  		<tr><td class="titolo" colspan="3"><%= lTitolo %></td></tr>
			<tr>
				<td class="l">Autorità delegata</td>
		 		<td class="l">
		  		<font class="campo">
		  			<%=StringUtils.toStringJSP( rinnovo.getDescrTipoAutoritaRinnovo() )%>
		  			di
		  			<%=StringUtils.toStringJSP( rinnovo.getDescrLuogoRinnovo() )%>
		  			in data
		  			<%=StringUtils.toStringJSP( DateUtils.getDateToString(rinnovo.getDataRinnovo(), "dd/MM/yyyy") )%>
		  		</font>
		   	</td>
				<td class="l">
					<table>
						<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_RICERCHE_STAMPA_SIEP%>">
							<jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.rinnovo.action.ActLoadDocumentoRinnovo&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+rinnovo.getIdRinnovo()%>"/>			
						</jsp:include>	  	

						<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_CANCELLA_SIEP%>">
							<jsp:param name="LinkAction" value="<%= "/jsp/Main.jsp?Action=siap.siep.rinnovo.action.ActCancellaRinnovo&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+rinnovo.getIdRinnovo()+"&"+ICostantiScadenzario.CAMPO_ID_SCADENZARIO+"="+lSca.getIdScadenzario()+"&LinkActionRitorno=siap.siep.scadenzario.action.ActLoadDettaglioScadenzario"%>"/>				
						</jsp:include>	  	
					</table>				
			   	</td>
		  </tr>							
 			<tr><td>&nbsp;</td></tr>
		</table>	
<%
	}
%>

</body>
</html>