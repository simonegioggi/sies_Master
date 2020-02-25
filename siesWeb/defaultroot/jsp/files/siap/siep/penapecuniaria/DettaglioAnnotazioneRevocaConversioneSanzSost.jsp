<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="scambiosanzione" 	  scope="request" class="siap.siep.scambiosanzione.model.ScambioSanzioneModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="annotazione"         scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="evento"         	  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="fascicoloClasseI"	  scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="motivo"              scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoRidetPena"     scope="request" class="java.lang.String"/>
<jsp:useBean id="descMotivoRidetPena" scope="request" class="java.lang.String"/>
<% //01-06-2016 - Riciclo dopo Primo Collaudo per V.10 %>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<% //01-06-2016 - END Riciclo %>

<%
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
	PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
	LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
	AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>"> 

    <script language="javascript">
  		var desktop;
		function conferma()
		{
			var risposta=confirm('Viene ora creato un nuovo procedimento in classe I mentre\nviene archiviato il relativo procedimento di classe VII!\n Vuoi proseguire?');
			return risposta;
		}

		function confermaRidetPena()
		{
			var risposta=confirm("Attenzione! Si procede all'archiviazione del procedimento di classe VII\n Vuoi proseguire?");
			return risposta;
		}
		function confermaPenaScaduta()
		{
			var risposta=confirm("Attenzione! Fascicolo "+<%=fascicoloClasseI.getChiaveAnno()%>+"/"+<%=fascicoloClasseI.getChiaveProgr()%> +" di classe I con fine pena scaduto. \n\n Selezionando il tasto OK il sistema inserirà una pena residua manuale con i quantum a 0. \n\n Selezionando il tasto Annulla l'utente deve prima procedere a inserire un provvedimento di archiviazione per pena espiata, e poi rieseguire l'operazione corrente.");
			return risposta;
		}
	</script>
	
</head>

<body class="corpo">
	<table>
	    <tr>
		    <td class="LBG">
		    	<a href="Javascript:window.print();">
		    		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    	</a>
		    </td>
		    <td class="LBG">
		    	<font class="label">Funzione :</font>&nbsp;&nbsp;
		    	<font class="campo">Dettaglio Annotazione Revoca/Conversione Sanzione Sostitutiva in pena detentiva (Art. 66 L. 689/81 per Pene Pecuniarie)</font>
			</td>
		</tr>
	</table>
	<br>
		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>    

<% //======================= BLOCCO POSIZIONE GIURIDICA ========================= %>
	<table>
		<tr>
			<td class="l">Posizione Giuridica </td>
			<td class="L" colspan=5>
				<font class="campo">
<%     			if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       			{%>
              		DETENUTO PER ALTRA CAUSA <%=posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica()%>
<%    			}
				else
			    {%>
					<%=lPosizione.getDescrPosizioneGiuridica()%>
			<%  }%>
				</font>
			</td>
		</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
			if( lAltraCausa.getIstitutoDetenzione()!= null)
			{
%>
	           <tr>
	             <td class="l">Detenuto presso </td>
	             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
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
	        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null && !"-".equals(lLuogoDetenzione.getIstitutoDetenzione().getCodTipoIstituto()))
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
	        }
        }%>
   <!--  input type="HIDDEN" title="Codice Posizione" 
   		value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" 
   		type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  
   		maxlength="6" size="6" -->

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
             </tr>
<%
          }
        }

  if(penaresidua != null && penaresidua.getFlagSanzioneSostitutiva()!=null)
  {
%>
     <tr>
      <td class="l">Sanzione sostitutiva da espiare</td>  
      <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
            || (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%> 
      </td>
    </tr>

<%} %>
 
<% //======================= FINE BLOCCO POSIZIONE GIURIDICA ========================= %>	   
 
<%// 01-06-2016 - Riciclo dopo Primo Collaudo per V.10 %>
<% 	if (magistrato!= null && magistrato.getCognome()!=null && magistrato.getNome()!=null)
	{%>
	<tr>
	  <td class="l">Magistrato</td>
	  <td class="L" colspan="4"><font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%>&nbsp;</font>
			        <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
	  </td>
	</tr>
	<%} %>
<%// 01-06-2016 - END Riciclo %>
	
     <tr>
      <td class="l">Data Ricezione Provvedimento</td>
      <td class="L" colspan="4">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataRicezioneAtti(),"dd/MM/yyyy") )%></font>
      </td>
    </tr>
     <tr>
      <td class="l">Data emissione Provvedimento</td>
		<td class="l" colspan="4">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataEmissione(),"dd-MM-yyyy"))%>
			&nbsp;</font>
		</td>
    </tr>    
    <tr>
      <td class="l"> Anno/Numero Provvedimento</td>
      <td class="l" colspan="5">
			<font class="campo">
        		<%=StringUtils.toStringJSP(scambiosanzione.getAnnoRegistro())%>
        		/
        		<%=StringUtils.toStringJSP(scambiosanzione.getNumeroRegistro())%>
        	&nbsp;</font>
      </td>

    </tr>
    <tr>
        <td class="l">Tipo Provvedimento </td>
		<td class="l" colspan="4">
			<font class="campo">
				<%=StringUtils.toStringJSP(scambiosanzione.getDescrTipoDecisione())%>
			&nbsp;</font>
		</td>
    </tr>


    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="l" colspan="5">
			<font class="campo">
	          <%=StringUtils.toStringJSP(scambiosanzione.getDescrUfficioEmittente())%>
				&nbsp;di&nbsp;
	          <%=StringUtils.toStringJSP(scambiosanzione.getComuneUfficioEmittente())%>
	        &nbsp;</font>
      </td>
    </tr> 

    <tr>
        <td class="l">Motivo Provvedimento </td>
		<td class="l" colspan="4">
			<font class="campo">
				<%=StringUtils.toStringJSP(scambiosanzione.getDescrTipoSanzione())%>
			&nbsp;</font>
		</td>
    </tr>

    <tr>
      <td class="l">Tipo Pena da Convertire</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%></font></td>
     
      <td class="l" colspan="3">Quantum di Pena
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>

        </td>
    </tr>
    <tr>
      <td class="l" colspan="4">Pena Convertita:</td>
    </tr>  
    <tr>
      <td class="l" colspan="4">   RECLUSIONE
          <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumAnniReclusione(), "0")%>&nbsp;</font>
          <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumMesiReclusione(), "0")%>&nbsp;</font>
          <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumGiorniReclusione(), "0")%></font>
        ARRESTO
          <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumAnniArresto(), "0")%>&nbsp;</font>
          <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumMesiArresto(), "0")%>&nbsp;</font>
          <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumGiorniArresto(), "0")%></font>

      </td>
    </tr>

<% 	if(lFascicoloAssociato.getChiaveProgr().intValue() > 70000 && 
	   lFascicoloAssociato.getChiaveProgr().intValue() < 80000 &&
	   evento.getEvento().getFlagDocumentoRegistrato().compareTo("A")!=0) { %>
	<tr>
	<% 	if (Utils.isNullObj(lFascicoloAssociato.getFasSieIdFascicoloSiep() ) ) {%>
	<td class="lNoBord">
		<FORM  method="POST" name="IscrizioneClasseI" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActInserisciFascicoloDaClasseVII&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getEvento().getIdEvento()%>">
    	  <br><INPUT class="bottone" type="submit" name="OE" value="Iscrizione Procedimento in classe I" onclick="javascript:return conferma()">
			  <input type="HIDDEN" name="Motivo" value="<%=motivo%>" >
      
		</FORM>
	</td>
	<% } else if (Utils.isNullObj(fascicoloClasseI.getIdFascicoloSiep())) { %>
	<td class="lNoBord">
	<FORM  method="POST" name="RPena" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActLoadRideterminazionePenaRevocaSSPP&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getEvento().getIdEvento()%>&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getFasSieIdFascicoloSiep()%>&IdFascClasseVII=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&flagPenaScaduta=N">
    	  <br><INPUT class="bottone" type="submit" name="RP" value="Rideterminazione Pena Procedimento in classe I" onclick="javascript:return confermaRidetPena()">
  			  <input type="HIDDEN" name="ChiaveFascicolo" value="<%=lFascicoloAssociato.getFasSieIdFascicoloSiep()%>" >
			  <input type="HIDDEN" name="Motivo" value="<%=motivo%>" >
			  <input type="HIDDEN" name="MotivoRidetPena" value="<%=motivoRidetPena%>" >
			  <input type="HIDDEN" name="DescMotivoRidetPena" value="<%=descMotivoRidetPena%>" >
 	</FORM>
	</td>
	<% } else {%>
	<td class="lNoBord">
	<FORM  method="POST" name="RPena" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActLoadRideterminazionePenaRevocaSSPP&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getEvento().getIdEvento()%>&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getFasSieIdFascicoloSiep()%>&IdFascClasseVII=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&flagPenaScaduta=S">
    	  <br><INPUT class="bottone" type="submit" name="RP" value="Rideterminazione Pena Procedimento in classe I" onclick="javascript:return confermaPenaScaduta()">
  			  <input type="HIDDEN" name="ChiaveFascicolo" value="<%=lFascicoloAssociato.getFasSieIdFascicoloSiep()%>" >
			  <input type="HIDDEN" name="Motivo" value="<%=motivo%>" >
			  <input type="HIDDEN" name="MotivoRidetPena" value="<%=motivoRidetPena%>" >
			  <input type="HIDDEN" name="DescMotivoRidetPena" value="<%=descMotivoRidetPena%>" >
 	</FORM>
	</td>
	<% }%>

	</tr> 
<%} %>
  
    
</table>

</body>
</html>