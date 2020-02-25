<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.util.List"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>

<!--  jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" /-->
<jsp:useBean id="Modificabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="prescrizioni"               scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%
if (Modificabile == null || Modificabile.trim().length() < 1)
    Modificabile = "SI";

// Flag che indica l'utilizzo del Nuovo Tipo di Prescrizioni
boolean isPrescrizioneNew = false;
if (request.getParameter("PrescrizioniSSePP") != null)
{
	isPrescrizioneNew = true;
}
if (fascicoloSiusGP != null && fascicoloSiusGP.getGeneraleProcedimentoModel() != null && 
		fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro() != null && 
			fascicoloSiusGP.getGeneraleProcedimentoModel().getCodTipoRegistro().equals("S09") )
{
	isPrescrizioneNew = true;
}

%>
    <table cellspacing=4 cellpadding=4 width=95%>
<%
      // Elenco Prescrizioni
      if (prescrizioni != null && (prescrizioni.size()> 0))
      {
%>
        <tr>
          <td class="Titolo" colspan=2> Prescrizioni </td>
        </tr>
<%
        // Controllo Sanzioni Sostitutive
		//if(fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U059")  || fascicoloSiusGP.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U017")   )
		if (isPrescrizioneNew)
		{
			// Chiama la funzione di decodifica
			List parsePrescrizioni = DecodificheUtils.parsePrescrizioni(null,prescrizioni);
          	
          	Iterator itx = parsePrescrizioni.iterator();
    		while ( itx.hasNext())
    		{
    			PrescrizioneModel lPrescrizioneMod = (PrescrizioneModel)itx.next();
    			String descrPrescrizione = "-";
    			if (lPrescrizioneMod.getDescrAltraPrescrizione() != null)
    				descrPrescrizione = lPrescrizioneMod.getDescrAltraPrescrizione();
    			else if (lPrescrizioneMod.getDescrTipoPrescrizione() != null)    				
    					descrPrescrizione = lPrescrizioneMod.getDescrTipoPrescrizione();
%>					
					<tr>
 						<td class="l" colspan="2"><%=descrPrescrizione%></td>  	
 					</tr>
 <% 	 			
    		}
		}
		else
		{
			Iterator lIndPre = prescrizioni.iterator();
        	while (lIndPre.hasNext())
        	{
          		PrescrizioneModel lPrescrizione = (PrescrizioneModel) lIndPre.next();
%>
          		<tr>
            		<td class=l colspan=2><%=lPrescrizione.getDescrTipoPrescrizione().compareTo("-")==0 ? lPrescrizione.getDescrAltraPrescrizione() : lPrescrizione.getDescrTipoPrescrizione()%></td>
          		</tr>
<%
        	}
		}
      }
      else if (Modificabile.compareTo("SI") == 0)
      {
      	String linkInserimentoPrescrizione = "";
    	  
   	// Sanzioni Sostitutive o Conv. Pene Pecuniarie
		if (isPrescrizioneNew)
		{
			linkInserimentoPrescrizione ="siap.sius.prescrizione.action.ActLoadInserisciPrescrizioneNew&nextaction="+request.getParameter(ICostantiPrescrizione.NEXT_ACTION)+"&IdEvento="+request.getParameter(ICostantiPrescrizione.CAMPO_EVE_ID_EVENTO);
	
		}
		else
		{
			linkInserimentoPrescrizione = "siap.sius.prescrizione.action.ActLoadInserisciPrescrizione&nextaction="+request.getParameter(ICostantiPrescrizione.NEXT_ACTION)+"&IDEvento="+request.getParameter(ICostantiPrescrizione.CAMPO_EVE_ID_EVENTO);
		}
  
%>
        <tr>
          <td class="L">
            <font class="label">Prescrizioni assenti</font>
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=linkInserimentoPrescrizione %>">
            <!--a class="cliccabile" href="Javascript:InserisciPrescrizioni()"-->
             Inserimento
            </a>
          </td>
         </tr>
<%
      }
%>
      <tr>  <td> </td> </tr>
      <tr>  <td> </td> </tr>
    </table>