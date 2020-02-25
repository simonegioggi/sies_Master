<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="lPosGiuModificata" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="tipoMisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="notificaE"      scope="request" class="java.lang.String"/>
<jsp:useBean id="verbale"             scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="camponota" scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>
<jsp:useBean id="eventoannota"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<jsp:useBean id="listaAutorita" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaIstituti" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaUffici" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaCssa" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaAvvSiep" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaAvvSius" scope="request" class="java.util.ArrayList"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  //EventoModel evento1 = new EventoModel();
  //evento1 = eventoscr;
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

</head>
<body class="corpo">

  <table>
  <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
  <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;


    <font class="campo">Dettaglio Comunicazione Variazione Data Inizio Misura</font>



</td>

<% EventoModel lProvvedimento = new EventoModel(evento);

   MisuraAlternativaModel lModel = new MisuraAlternativaModel();
   String lAzione = new String();
   String flagMis = new String();

%>
<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 		if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) 
 		{%>

 <!-- BOTTONE DI STAMPA -->
			   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaVariazioneMADecSca&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura%>"/>
			   </jsp:include>
<%		}%>

<%		if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 		{%>

 <!-- BOTTONE DI STAMPA -->
			   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaVariazioneMADecSca&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura%>"/>
			   </jsp:include>
<%		}%>

	</tr>
</table>
 <br>
   		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
		<table style="width: 95%;">
<%		if(flagmisura.equals("N"))
		{%>

  				<tr><td><input type="HIDDEN" name="flagmisura" value="N"></td></tr>
 <%		}
		else
		{%>
   				<tr><td><input type="HIDDEN" name="flagmisura" value="S"></td></tr>
<%		}%>
 		<tr>
 			<td><input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>"></td>
 		</tr>

    	<tr>
      		<td class="l">Posizione Giuridica </td>
      		<td class="L" colspan=5>
      			<font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
     <%}
       else
       {
            if(lPosGiuModificata != null && lPosGiuModificata.getIdPosizioneGiuridica() != null)
            {
              %>
                   <%=lPosGiuModificata.getDescrPosizioneGiuridica()%>
            <%
             }
             else
             {
                %>
                     <%=lPosizione.getDescrPosizioneGiuridica()%>

           <%}
       }%>
         </font>
   </td>
</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if(lAltraCausa.getIstitutoDetenzione() != null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             //  if(lAltraCausa.getDescrLuogoIstituto()!=null)
              // {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
              // }
%>
            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         	if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          	{
%>
	            <tr>
	              <td class="l">Indirizzo</td>
	              <td class="L" colspan=5>
	                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
	              </td>
	            </tr>
<%
          	}
        }
%>
<tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0) )
        	{}
        	else
        	{
%>
		          	<td class="l">Reclusione</td>
		          	<td class="l" >
		            	<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
		            	<font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
		            	<font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		          	</td>
           <%		if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0)
           			{%>
				          <td class="l">Multa</td>
				          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%        			} 
           }
%>
		   </tr>
		   <tr>
<%
		    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
		        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
		        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
		    {}
		    else
		    {
%>
				    <td class="l" >Arresto</td>
				    <td class="l" >
				         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
				         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
				         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
				    </td>
           <%		if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0)
           			{%>
					      <td class="l">Ammenda</td>
					      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%          		} 
           }
    }
%>
</tr>
<% 
       if ( penaresidua.getFlagErgastolo() != null)
       {
	        if(penaresidua.getFlagErgastolo().equals("S"))
	        {
%>
		        <tr>
		          <td class="l">Pena Detentiva</td>
		          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
		        </tr>
<%
        	}
	        else
	        if(penaresidua.getFlagErgastolo().equals("D"))
	        {
%>
		        <tr>
		          <td class="l">Pena Detentiva</td>
		          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
		        </tr>
		<%
		        }
       }
%>
      
<!------------------------------ 	VARIAZIONE DATA INIZIO MISURA  	----------------------------------->  
 
     <tr>
        <td class="Titolo" colspan=10><font  class="label">Variazione Data Inizio Misura</font></td>
	   </tr>

      <tr>
        <td class="l" >Data Pervenimento Richiesta Variazione </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoannota.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
      </tr>

    <tr>
      <td class="l">Motivazioni</td>
      <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(camponota.getDescr())%> </font>
      </td>
    </tr> 
    
    <tr>
        <td class="l" >Nuova Data Inizio Misura </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%></font></td>
      
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
		    	if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
		    	{
%>
				         <td class="l">Data Fine Pena</td>
				         <td class="L" >
				           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
				           -
				           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
				           -
				           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
				         </td>
<%
        		}
		    	else
		    	{%>
				         <td class="l">Data Fine Pena</td>
				         <td class="lRosso" >
				           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
				           -
				           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
				           -
				           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
				         </td>
<%            	}
      }
%>
	</tr>

        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">

<!---------------------------------------TRASMISSIONE, EMISSIONE, MAGISTRATO------------------------------------------------------------->
      
	<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
         </td>
<%
	if(eventonotifica !=null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0)
	{
%>         
        <td class="l">Data Trasmissione</td>
        <td class="L" >
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>

         </td>
<%	}
%>         
      </tr>

<%
  	if(magistrato != null)
  	{%>
  		<tr>
   			<td class="l">Magistrato Firmatario
   			<td class="L">
		       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
		       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   			</td>
  		</tr>
<%	} %>
</table>

  <table style="width: 95%;">
<!-----------------NOTIFICHE-------------------------------------------->
<%   			// Autorita Esterne

	if(listaAutorita.size() > 0)
	{
%>		
		<tr>
	        <td class="Titolo" colspan=10><font  class="label">Destinatari per Notifiche</font></td>
	    </tr>
<% 	}		
  	for(int i=0; i<listaAutorita.size(); i++)
  	{
	    NotificaModel lAutorita = (NotificaModel)listaAutorita.get(i);
	    if(lAutorita.getAutoritaEsterna() == null)
    	{
      		lAutorita.setAutoritaEsterna( new AutoritaEsternaModel() );
    	}
    	if( lAutorita.getAvvIdAvvocatoFascicoloSiep() == null )
    	{
  %>
		    <tr>
		      <td class="l">Autorita Notifica</td>
		      <td class="L">
		        <font class="campo"><%=StringUtils.toStringJSP( lAutorita.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
		        di
		        <font class="campo"><%=StringUtils.toStringJSP( lAutorita.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
		      </td>
		    </tr>
  <%		if(lAutorita.getNote() != null)
    		{%>
				    <tr>
				      <td class="l">Note</td>
				      <td class="L">
				        <font class="campo"><%=StringUtils.toStringJSP(lAutorita.getNote())%></font>&nbsp;
				      </td>
				    </tr>
<%
    		}
   		}
 	}
%>

<% 		// - -- - - - - - - - UEPE	 - - - - - - - - 	%>
          
<%          
	if(listaCssa.size() > 0)
	{
%>		
		<tr>
	        <td class="Titolo" colspan=10><font  class="label">Notifica al Servizio Sociale</font></td>
	    </tr>        
<%	}	    
          
    for(int i=0; i<listaCssa.size(); i++)
    {
	      	NotificaModel lCssa = (NotificaModel)listaCssa.get(i);
	      	if(lCssa.getCSSA() == null)
	      	{
	        	lCssa.setCSSA( new CSSAModel() );
	      	}
  %>
	      	<tr>
	        	<td class="l"><%=StringUtils.toStringJSP(lCssa.getCSSA().getTipoDesc())%> Competente</td>
	        	<td class="l">
	          		<font class="campo"><%=StringUtils.toStringJSP(lCssa.getCSSA().getComune())%>-<%=StringUtils.toStringJSP(lCssa.getCSSA().getIndirizzo())%></font>
	        	</td>
	      	</tr>
    <%		if(lCssa.getNote() != null)
     		{%>
			      <tr>
			        <td class="l">Note</td>
			        <td  class="l">
			          <font class="campo"><%=StringUtils.toStringJSP(lCssa.getNote())%>&nbsp;</font>
			        </td>
			      </tr>
  <%
     		}
    }
 %>
 
 <% 		// - -- - - - - - - TDS	MDS	- - - - - - - 	%>

<%          
	if(listaUffici.size() > 0)
	{
%>		
		<tr>
	        <td class="Titolo" colspan=10><font  class="label">Notifica agli Uffici</font></td>
	    </tr>        
<%	}	    
 
  	for(int i=0; i<listaUffici.size(); i++)
  	{
	    	NotificaModel lUfficio = (NotificaModel)listaUffici.get(i);
	    	if(lUfficio.getUfficio() == null)
	    	{
	      		lUfficio.setUfficio( new UfficioModel() );
	    	}
%>
			    <tr>
			      <td class="l">Ufficio Preposto</td >
			      <td class="l">
			        <font class="campo"><%=StringUtils.toStringJSP(lUfficio.getUfficio().getDescrTipoUfficio())%></font>
			      </td>
			    </tr>
			    <tr>
			      <td class="l">Sede</td><td class="L">
			        <font class="campo"><%=StringUtils.toStringJSP(lUfficio.getUfficio().getDescrComune())%></font>
			      </td>
			    </tr>
	  <%		if(lUfficio.getNote() != null)
	   			{%>
				    <tr>
				      <td class="l">Note</td>
				      <td  class="l">
				        <font class="campo"><%=StringUtils.toStringJSP(lUfficio.getNote())%>&nbsp;</font>
				      </td>
				    </tr>
	<%
	  			}
	  	  
  	}
%>

 <% 		// - -- - - - - - AVVOCATI- - - - - 	%>
 
 </table>
 <br>
 <table style="width: 95%;">

<%int count=0;

if(listaAvvSiep.size() > 0)
{
	
  	for(int i=0; i<listaAvvSiep.size(); i++)
  	{
  		NotificaModel lAvv = new NotificaModel();
    	lAvv = (NotificaModel)listaAvvSiep.get(i);
  		
  			
		    AvvocatoSiepModel lAvvMod =  lAvv.getAvvSiep();      
		    AutoritaEsternaModel lAuMod = lAvv.getAutoritaEsterna();    
    %>
     		<tr>
       			<td class="l">Notifica per difensore</td>
       			<td class="L" colspan="2">
		        	<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
		        		&nbsp;Foro di&nbsp;
		        	<font class="campo">
		          		<%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
		        	</font>
        				&nbsp;Difensore di&nbsp;
        			<font class="campo">
          				<%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
        			</font>
       			</td>
      		</tr>
			  
			<tr>
	    		<td class="l">Autorita Notifica</td>
            	<td class="L" colspan=2>
        			<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
         			di
        			<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
      			</td>
     		</tr>
			<%if(lAvv.getNote()!= null && !lAvv.getNote().equals(""))
			{%>
				     <tr>
				      <td class="l">Note</td>
				      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getNote())%></font>&nbsp;</td>
				     </tr>
<% 
			}
	}
}
%>

</table>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadVariazioneMADecSca">
            <input type="HIDDEN" name="tipoMisura" value="<%=tipoMisura%>">
            <input type="HIDDEN" name="notificaE" value="<%=notificaE%>">
			<input type="HIDDEN" name="newidevento" value="">
			
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioVariazioneMADecSca">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>