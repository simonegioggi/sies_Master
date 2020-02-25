<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel"%>

<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>

<jsp:useBean id="richiesteconversioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
	TenoreModel[] tenori = datiOrdinanza.getTenori(); 

  String lCodTipoProv = ICostantiProvvedimento.COD_ORDINANZA;
%>

		<table cellspacing="2" cellpadding="2" width="90%">
<%
    	Iterator itx = richiesteconversioni.iterator();
    	while (itx.hasNext())
    	{
    		RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel)itx.next();
				String lTitoloRichiestaCPP = ""; 
				if (lRicConEstesa.getRichiestaConversione().getFasSieIdFascicoloSiep() != null &&
						lRicConEstesa.getRichiestaConversione().getFasSiuIdFascicoloSius() == null) 
					lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP "+lRicConEstesa.getFasSiep().getChiaveAnno()+" / "+lRicConEstesa.getFasSiep().getChiaveProgr();
				else
					lTitoloRichiestaCPP = "Pena Pecuniaria inserita dall' UDS";

				// Distinzione tra "Conversione e Rateizzazione"
				if (tenori[0].getCodOggettoTenore().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_CONVERSIONE)==0)
				{%>
				<table cellspacing="2" cellpadding="2" width="90%">
				<tr>
        		<td class="Titolo" width="100%" colspan="6" > <%=lTitoloRichiestaCPP%></td>
    			</tr>

  				<tr>
			    	<td rowspan="3" width="20%" class="l"> 
			    		<font class="label"> Multa</font>&nbsp;
<%						if (lRicConEstesa.getRichiestaConversione().getImportoMulta()!= null) { %>
			    			<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta()) %></font><br>
							<%}%><br>
			    		<font class="label"> Ammenda</font>&nbsp;
<%						if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda()!= null) { %>
				    		<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda()) %></font>&nbsp;
						<%}%>
					</td>
			    	<td rowspan="3" width="80%" class="l"> 
				    	<font class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; C&nbsp;&nbsp;  o&nbsp;&nbsp;  n&nbsp;&nbsp;  v&nbsp;&nbsp;  e&nbsp;&nbsp;  r&nbsp;&nbsp;  t&nbsp;&nbsp;  i&nbsp;&nbsp;  t&nbsp;&nbsp;  a&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       i&nbsp;&nbsp;  n &nbsp;<br><br></font>
	            		<font class="label">&nbsp;Anni: </font>
	            		<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoAnni()) %> </font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            		<font class="label">&nbsp;Mesi: </font>
	            		<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoMesi()) %> </font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            		<font class="label">&nbsp;Giorni: </font>
	            		<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoGiorni()) %> </font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	            		<font class="label"> di&nbsp;&nbsp; </font>
	<%
								if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("01")==0) {%>
	            		<font class="crosso">Libertà controllata </font>
							<%} else if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("02")==0)  {%>
	            		<font class="crosso"> Lavoro sostitutivo </font>
							<%} else if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("03")==0) {%>
	            		<font class="crosso"> Rateizzazione Pena Pecuniaria </font>
            <%}%>
        		</td>
					</tr>
					<br>				
			<%} else
				if (tenori[0].getCodOggettoTenore().trim().compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_RATEIZZAZIONE)==0) 
				{ %>
					<table cellspacing="2" cellpadding="2" width="90%">
						<tr>
        			<td class="Titolo" colspan=6 > <%=lTitoloRichiestaCPP%></td>
    				</tr>

  					<tr>
			    		<td rowspan="3" width="20%" class="l"> 
			    			<font class="label"> Multa</font>&nbsp;
<%							if (lRicConEstesa.getRichiestaConversione().getImportoMulta()!= null) { %>
			    				<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta()) %></font>&nbsp;&nbsp;&nbsp;
								<%}%><br>
			    			<font class="label"> Ammenda </font>&nbsp;
<%							if (lRicConEstesa.getRichiestaConversione().getImportoAmmenda()!= null) { %>
				    			<font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoAmmenda()) %></font>&nbsp;
							<%}%>
							</td>
			    		<td rowspan="3" class="l"> 
            		<font class="label">Rateizzati in  </font>
            		<font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getNumeroRate()) %> </font>
            		<font class="label">&nbsp;rate da &nbsp;&nbsp;</font>
            		<font class="cRosso">&nbsp;&euro;<%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getValoreRata())%> </font>&nbsp;
            		<font class="label">&nbsp;&nbsp;e&nbsp; </font> <font class="cRosso">1</font>&nbsp; <font class="label">rata finale da&nbsp; </font> 
            		<font class="crosso">&euro; &nbsp; <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getValoreUltimaRata()) %> </font>
        			</td>
						</tr>
			
				</table>
			<%}
			}

   		if (tenori[0].getDescrOggettoTenore().trim().compareTo("Rateizzazione pena pecuniaria")==0) 
			{ 
   			RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel) richiesteconversioni.get(0);			
			%>
				<table cellspacing="2" cellpadding="2" width="90%">
 					<tr>
		    		<td class="l"> 
		    			<font class="label"> Termine pagamento 1° Rata : </font>&nbsp;
						</td>
			    	<td class="l"> 
  	         	<font class="label">entro il </font>
            	<font class="crosso">
            	<% if (lRicConEstesa.getRichiestaConversione().getDataInizioPagamento()!=null) {%>
            	 	<%=DateUtils.getDateToString(lRicConEstesa.getRichiestaConversione().getDataInizioPagamento(), "dd-MM-yyyy") %> 
							<%} else { %> - <%}%>
							</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;            	 	
            	<% if (lRicConEstesa.getRichiestaConversione().getNumeroGiorniInizioPagamento()!=null  &&
            				 lRicConEstesa.getRichiestaConversione().getNumeroGiorniInizioPagamento().intValue()>0 )	{%>
	           		<font class="label"> oppure entro &nbsp; </font>
            		<font class="crosso">
            	 		<%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getNumeroGiorniInizioPagamento()) %> 
           	 		</font>&nbsp;&nbsp;
    	       		<font class="label"> giorni dalla data di notifica </font>
							<%}%>            	 	
      	    </td>
	 					<tr>
				</table>
		<%}%>
		</table>		
	</table>		

<br>