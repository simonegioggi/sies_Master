<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.util.SiapStringUtil"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.penapresunta.model.PenaPresuntaModel" %>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.camponota.model.CampoNotaModel"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel" %>
<%@ page import="siap.siep.penacumulo.model.PenaCumuloModel" %>

<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria" %>
<%@ page import="siap.sico.evento.action.ActGestisciButtonsProvvedimento" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel" %>
<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo"          scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="dettagliofascicolo" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />
<jsp:useBean id="fascicoloconversione" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="fascicoloCollMod" 	scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="richiestaconversione" scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel" />
<jsp:useBean id="vectfasc" 			scope="request" class="java.util.Vector" />

<jsp:useBean id="IstruttoriaCumuloAperta"	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel" />

<%
  SoggettoModel soggetto = fascicolo.getSoggetto();
  SentenzaModel sentenza = fascicolo.getSentenza();
%>
<%
//==============================================================================
// I dati visualizzati nella form sono i seguenti
// - PROCEDIMENTO
// - SOGGETTO
// - SENTENZA
// - DATA IRREVOCABILITÀ/NOTE
// - stato del procedimneto ???
// - CUMULO
// - POSIZIONE GIURIDICA/istituto
// - LISTA AVVOCATI
// - PENA IRROGATA IN SENTENZA (solo se non cumulante)
// - PENA DA ESPIARE (quantum ultima pena validata)
// - DECORRENZA/SCADENZA (solo data inizio e data fine (no date intermedie))
// - PENA RESIDUA (calcolata al volo tra la data di systema e il fine pena previsto) solo se pena effettivamente in decorrenza
// - LIBERAZIONE ANTICIPATA
// - Stato differimento (pena differita)
// - MISURA ALTERNATIVA
// - POSIZIONE MATERIALE
// - ULTIMI EVENTI
//==============================================================================
%>

<script language="JavaScript">
    	 function ListaSanzioni()
    	{
    	 desktop = window.open("/jsp/Main.jsp?Action=siap.sius.sanzionesostitutiva.action.ActLoadListaSanzioniSostitutiveUDS" , "Lista_Date", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=600, height=500");
    	}																																												  
    </script>

<table cellspacing=0 cellpadding=0 width=95%>
<%
//==============================================================================
//                                PROCEDIMENTO
//==============================================================================
%>
    <tr>
      <td class="L">
        <font class="label">Procedimento (N.SIEP) Registro Istanza </font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
          <%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>
          /
          <%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>
        </a>&nbsp;
<%
        if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio())))
        {
%>
          <font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio() %></font>
          <br>
<%
        }

        if(fascicolo.getFlagCumulante() != null && fascicolo.getFlagCumulante().equals("S"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;C&nbsp; </font>&nbsp;
<%
        }
%>
<%
        if(fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font>&nbsp;
<%
        }
%>
<%
        if(   fascicolo.getCodStatoFascicolo() != null
           && (fascicolo.getCodStatoFascicolo().equals("01"))
           )
        {
%>
          <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font>&nbsp;
<%
        }

        boolean isPenaSospesa = false;
        if(   ( dettagliofascicolo.getPenaResidua()!= null
             && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa()!= null
             && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("S") )
             || (      fascicolo!= null && fascicolo.getChiaveProgr() != null
                  && ( fascicolo.getChiaveProgr().intValue() >= 30000
                  &&   fascicolo.getChiaveProgr().intValue() < 40000) ) )
        {
          if(    fascicolo!= null && fascicolo.getChiaveProgr() != null
            && ( fascicolo.getChiaveProgr().intValue() >= 30000
            &&   fascicolo.getChiaveProgr().intValue() < 40000) )
          {
            isPenaSospesa = true;
%>
            <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
<%
          }
          else
          {
            isPenaSospesa = true;
%>
            <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
<%
          }
        }

        if(   dettagliofascicolo.getPenaResidua()!= null
           && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa()!= null
           && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("I") )
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
<%
        }
        if(   dettagliofascicolo.getPenaResidua()!= null
           && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa()!= null
           && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("D"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
<%
        }
%>
        <font class="label">Data Iscrizione :</font>
<%
        if(fascicolo.getDataIscrizione()!= null)
        {
%>
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>
          </font>
<%
        }
        else
        {
%>
          -
<%
        }
%>
      <% if (IstruttoriaCumuloAperta.getIdIstruttoriaCumulo()!=null) {%>
        <font class="label" style="color:red">Aperta Istruttoria Cumulo N. :</font>
        <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumuloAperta.getIdIstruttoriaCumulo()%>" title="Istruttoria">
          <%=IstruttoriaCumuloAperta.getAnnoProtocollo()%>
          /
          <%=IstruttoriaCumuloAperta.getNumProtocollo()%>
        </a>&nbsp;
      
      <% } %>
      </td>
    </tr>

<%
//==============================================================================
//                                 SOGGETTO
//==============================================================================
%>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto  :</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=StringUtils.toStringJSP(soggetto.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(soggetto.getNome())%>
        </a>
      </font>&nbsp;
<%
        if (soggetto.getSesso().compareTo("F")==0)
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

if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%}else
   {%>
      <font class="campo">***</font>&nbsp;
<%}}else{%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%}%>
      <font class="label">in : </font>
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
       <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
<%
      }
%>


      </font>
      <font class="label">Codice CUI : </font>
      <font class="campo"> <%=StringUtils.toStringJSP(soggetto.getCodAfis())%></font>
    </td>
  </tr>

<%
//==============================================================================
//                                SENTENZA
//==============================================================================
%>
    <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%> </a>&nbsp;   
          <font class="label">del</font>&nbsp;

            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>

        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
 		}else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null) {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
<%
        if (sentenza.getAnnoRegeGip() != null) {
%>
          &nbsp;<font class="label"> (N.Reg.Gen. </font>
          <font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%></font>
          <font class="label"> GIP) </font>
<%
        }
        else if (sentenza.getAnnoRegeDib() != null) {
%>
          &nbsp;<font class="label"> (N.Reg.Gen. </font>
          <font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeDib())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeDib())%></font>
          <font class="label"> DIB) </font>
<%
		// MEV_66: aggiunte quattro nuove proprietà
		} else if (sentenza.getAnnoRegeGup() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGup())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGup())%></font>
			<font class="label"> GUP) </font>
<%
		} else if (sentenza.getAnnoRegeCapsm() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCapsm())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm())%></font>
			<font class="label"> CAPSM) </font>
<%
		// MEV_66: aggiunti anche CAS, CAP e CASAP
		} else if (sentenza.getAnnoRegeCap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCap())%></font>
			<font class="label"> CAP) </font>
<%
		} else if (sentenza.getAnnoRegeCas() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCas())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCas())%></font>
			<font class="label"> CAS) </font>
<%
		} else if (sentenza.getAnnoRegeCasap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCasap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCasap())%></font>
			<font class="label"> CASAP) </font>
<%
		}
%>
		</td>
	</tr>

<%
//==============================================================================
//                               DATA IRREVOCABILITÀ/NOTE
//==============================================================================
%>
<%if(fascicolo.getDataIrrevocabilita()!=null){%>
    <tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy"))%></font>
      </td>
    </tr>
<%}%>

</table>

<table width=95%>

<%  // Ambros 25/03/2009  Collegamento Classi I e VII	
//	==============================================================================
//			 Richieste Conversione e Procedimenti di Classe VII
//  ==============================================================================
%>
<tr>


<%  int lFascProg = fascicolo.getChiaveProgr().intValue();
	
	if((lFascProg > 70000 && lFascProg < 80000) &&
		(fascicoloCollMod.getChiaveProgr()!=null))	
	{ %>
			
    			<td class="L" colspan=1>
      				<font class="label">Collegato al Procedimento: N.</font>
      				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloCollMod.getIdFascicoloSiep()%>" title="Procedimento">
      					<%=StringUtils.toStringJSP(fascicoloCollMod.getChiaveAnno())%>/
      					<%=StringUtils.toStringJSP(fascicoloCollMod.getChiaveProgr())%>
      				</a>
    			</td>
  			
<%	}	 
	if((lFascProg > 70000 && lFascProg < 80000) &&
		(richiestaconversione.getIdRichiestaConversione() !=null))	
	{ 
     		String lDescrLink = "";
     		lDescrLink = "Dettaglio Conversione Pena Pecuniaria"; 
%>     				

    			<td align="right">
     				<font class="cRosso">
     					<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActLoadDettaglioRichiestaConversione&<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>=<%=richiestaconversione.getIdRichiestaConversione()%>">
           					<%=lDescrLink%>
           			
         				</a>
       				</font>
     			</td>     	
<%
	}
%>


</tr>
</table>

<table cellspacing=0 cellpadding=0 width=95%>
<%
	int Conta = 0;
	if(lFascProg < 20000)
	{		
		FascicoloSiepModel lFasciMod = new FascicoloSiepModel();
		Iterator itx = vectfasc.iterator();
		while ( itx.hasNext())
		{
			lFasciMod = (FascicoloSiepModel)itx.next();
	    	if(lFasciMod.getChiaveProgr().intValue() > 70000 && lFasciMod.getChiaveProgr().intValue() < 80000  )
	    	{
	    		Conta = Conta + 1;
	    	}
		}
	}
	
	if (Conta > 0)
	{
%>
		<tr>
			<td class="L">
  				<font class="label">Collegato al Procedimento: N.</font>	
<%  					
		FascicoloSiepModel lFasciMod = new FascicoloSiepModel();
		Iterator itx = vectfasc.iterator();
		while ( itx.hasNext())
		{
			lFasciMod = (FascicoloSiepModel)itx.next();
%>
      				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFasciMod.getIdFascicoloSiep()%>" title="Procedimento">
      					<%=StringUtils.toStringJSP(lFasciMod.getChiaveAnno())%>/
      					<%=StringUtils.toStringJSP(lFasciMod.getChiaveProgr())%>&nbsp;
      				</a>
    			</td>
<%   	} %> 	
		</tr>			
<% 		
	}	
		
//	==============================================================================
//	          STATO PROCEDIMENTO E STATO FASCICOLO                     
//	==============================================================================
	
	
  List lListStatProc = dettagliofascicolo.getStatoProcedimento();
  if(lListStatProc != null && lListStatProc.size() != 0)
  {
%>
    <tr>
      <td class="L" colspan="2">
            <jsp:include page="/jsp/files/siap/siep/statoprocedimento/IncludeStatoProcedimento.jsp"/>
      </td>
    </tr>
<%
  }
  else
  {
    if(fascicolo.getDescrStatoFascicolo() !=  null)
    {
%>
      <tr>
        <td class="L" colspan="2">
          <font class="label">Stato Procedimento : </font>          
          <font color=red><%=StringUtils.toStringJSP(fascicolo.getDescrStatoFascicolo())%></font>
        </td>
      </tr>
<%
    }
  }
  if (fascicoloconversione != null && fascicoloconversione.getChiaveAnno() != null && fascicoloconversione.getChiaveProgr() != null) { 
	  %>
	<td class="L" colspan=1>
      <font class="label">Iscritto Procedimento: N.</font>
      <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloconversione.getIdFascicoloSiep()%>" title="Procedimento">
      	<%=StringUtils.toStringJSP(fascicoloconversione.getChiaveAnno())%>/
      	<%=StringUtils.toStringJSP(fascicoloconversione.getChiaveProgr())%>
      </a>
    </td>
<% }

//==============================================================================
// CUMULO
//==============================================================================
if(fascicolo!= null && fascicolo.getCodStatoFascicolo() != null && fascicolo.getCodStatoFascicolo().equals("01")
  && fascicolo.getCodMotivoArchiviazione() != null && fascicolo.getCodMotivoArchiviazione().equals("01"))
{
  if (!UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getCodUfficioUnione()))
  {
%>
      <tr>
        <td class="L">
          <font class="label">Data Cumulo : </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataUnione(),"dd-MM-yyyy"))%></font>

          <font class="label">Ufficio : </font>
          <font class="campo"><%=StringUtils.toStringJSP(fascicolo.getDescrTipoUfficioUnione())%> di <%=StringUtils.toStringJSP(fascicolo.getDescrComuneUfficioUnione())%></font>

          <font class="label">N.Siep : </font>
          <font class="campo"><%=StringUtils.toStringJSP(fascicolo.getAnnoFascicoloUnione())%>/<%=StringUtils.toStringJSP(fascicolo.getNumFascicoloUnione())%></font>
        </td>
      </tr>
<%
  }
}


//==============================================================================
// POSIZIONE GIURIDICA
//==============================================================================
if(dettagliofascicolo.getPosizioneGiuridica() != null)
{
%>
  <tr>
    <td class="L">
      <font class="label">Posizione Giuridica : </font>&nbsp;
      <%
      // Se detenuto altra causa, visualizzo Istituito di detenzione o Indirizzo
      if (   fascicolo.getFlagAltraCausa() != null && fascicolo.getFlagAltraCausa().equals("S")
          && (   dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // libero
              || dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") // libero
             )
         )
      {
      %>
        <font color=red>DETENUTO PER ALTRA CAUSA</font>
        <% 
        if(dettagliofascicolo.getAltraCausa() != null) 
        {
          if(dettagliofascicolo.getAltraCausa().getIstDetIdIstitutoDetenzione() != null)
          {%>
            <tr>
              <td class="L">
                <font class="label">Tipo Istituto : </font>
                <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
                <font class="label">Luogo Detenzione</font>
                <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
                <%--font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrComune())%></font--%>
                <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrizione())%></font>
                <font class="label">Indirizzo </font>
                <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getIndirizzo())%></font>
              </td>
            </tr>
          <%
          }
          else if(dettagliofascicolo.getAltraCausa().getAltroLuogo() != null && !dettagliofascicolo.getAltraCausa().getAltroLuogo().equals(""))
          {
          %>
              <tr>
                <td class="L">
                  <font class="label">Indirizzo :</font>
                  <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getAltroLuogo())%></font>
                </td>
              </tr>
          <%
          }
        }
      }
      else
      { 
        //===================================
        // Non detenuto altra causa
        //===================================
        %>
        <font color=red><%=StringUtils.toStringJSP(dettagliofascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica())%></font>
        <%
        if(dettagliofascicolo.getLuogoDetenzione() != null )
        {
          if(dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null && !dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals(""))
          {
          %>
          <tr>
            <td class="L">
              <font class="label">Tipo Istituto : </font>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
              <font class="label">Luogo Detenzione</font>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%></font--%>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione())%></font>
              <font class="label">Indirizzo :</font>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo())%></font>
            </td>
          </tr>
          <%
          }
          else if(dettagliofascicolo.getLuogoDetenzione().getAltroLuogo() != null && !dettagliofascicolo.getLuogoDetenzione().getAltroLuogo().equals(""))
          {
          %>
          <tr>
            <td class="L">
              <font class="label">Indirizzo :</font>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getAltroLuogo())%></font>
            </td>
          </tr>
          <%
          }
        }
      }
}
// end if(dettagliofascicolo.getPosizioneGiuridica() != null)
    
 

  if(fascicolo.getCodTipoPosLibero().equals("I"))
  {
%>
    <tr>
      <td class="L">
        <font class="campo">Irreperibile</font>
      </td>
    </tr>
<%
  }

//==============================================================================
//  LISTA AVVOCATI
//==============================================================================
List lListaAvvocati = new Vector();
lListaAvvocati=dettagliofascicolo.getAvvocati();
if(lListaAvvocati.size()>0)
{
  for(int i=0; i<lListaAvvocati.size();i++)
  {
    AvvocatoModel lAvvocatoModel=(AvvocatoModel)lListaAvvocati.get(i);
%>
 <tr>
      <td class="L">
        <font class="label">Avvocato :</font>
        <font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getCognome())%></font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getNome())%></font>
        <font class="label">Foro :</font>
        <font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getForo())%></font>&nbsp;
        <font class="label">Indirizzo :</font>
        <font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getIndirizzo())%></font>&nbsp;
        <font class="label">Luogo Studio :</font>
        <font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getDescComuneResidenza())%></font>&nbsp;
        <font class="label">Tipo Avvocato :</font>
        <font class="campo"><%=StringUtils.toStringJSP(lAvvocatoModel.getDescrTipo())%></font>&nbsp;
      </td>
    </tr>
<%
  }
}
%>


<%
//==============================================================================
// PENA IRROGATA IN SENTENZA (solo se non cumulante)
//==============================================================================
if (!"S".equals(fascicolo.getFlagCumulante()))
{
  if(dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva()!=null)
  {
    PenaComplessivaSanzioneSostitutivaModel lPenaSostMod=dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();

    if(lPenaSostMod!=null)
    {
      PenaComplessivaModel lPenCompMod=lPenaSostMod.getPenaComplessiva();
      if(lPenCompMod!=null)
      {
      %>
		  <tr>
		    <td class="L">
          <font class="label">Pena irrogata in sentenza : </font>
          <%
          if (   (lPenCompMod.getNumAnniReclusione()!=null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenCompMod.getNumMesiReclusione()!=null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenCompMod.getNumGiorniReclusione()!=null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
        		 )
          {%>
          <font class="campo">Reclusione</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenCompMod.getImportoMulta()!=null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0))!=0){%>
          <font class="label">Multa </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <%
          if (   (lPenCompMod.getNumAnniArresto()!=null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenCompMod.getNumMesiArresto()!=null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenCompMod.getNumGiorniArresto()!=null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Arresto</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenCompMod.getImportoAmmenda()!=null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
          <font class="label">Ammenda </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <% if (lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04")) {%>
          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
            <%if(lPenCompMod.getCodTipoPenaDetentiva().equals("04")){%>
              <%if(lPenCompMod.getNumAnniIsolamentoDiurno()!=null){%>
              <font class="label">Anni</font>
              <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenCompMod.getNumMesiIsolamentoDiurno()!=null){%>
              <font class="label">Mesi</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenCompMod.getNumGiorniIsolamentoDiurno()!=null){%>
              <font class="label">Giorni</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
              <%}%>
            <%}%>
         <%}%>
      </td>
    </tr>
<%
  }
}
}
}  //
else {
  //============================================================================
  // Aggiunta nuova versione calcolo pena: viene visualizzata la pena in Cumulo
  //============================================================================
  if(dettagliofascicolo.getPenaCumulo()!=null)
  {
    PenaCumuloModel lPenaCumulo = dettagliofascicolo.getPenaCumulo();
      %>
		  <tr>
		    <td class="L">
          <font class="label">Pena Irrogata in Cumulo : </font>
          <%
          if (   (lPenaCumulo.getNumAnniReclusione()!=null && lPenaCumulo.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenaCumulo.getNumMesiReclusione()!=null && lPenaCumulo.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
        		  || (lPenaCumulo.getNumGiorniReclusione()!=null && lPenaCumulo.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
        		 )
          {%>
          <font class="campo">Reclusione</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniReclusione(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiReclusione(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenaCumulo.getImportoMulta()!=null && lPenaCumulo.getImportoMulta().compareTo(new BigDecimal(0))!=0){%>
          <font class="label">Multa </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <%
          if (   (lPenaCumulo.getNumAnniArresto()!=null && lPenaCumulo.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumMesiArresto()!=null && lPenaCumulo.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumGiorniArresto()!=null && lPenaCumulo.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Arresto</font>
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniArresto(),"0")%></font>
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiArresto(),"0")%></font>
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenaCumulo.getImportoAmmenda()!=null && lPenaCumulo.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
          <font class="label">Ammenda </font>
          <font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <% if (lPenaCumulo.getCodTipoPenaDetentiva().equals("E") || lPenaCumulo.getCodTipoPenaDetentiva().equals("I")) 
          {
          %>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getDescrTipoPenaDetentiva())%></font>
            <%if(lPenaCumulo.getCodTipoPenaDetentiva().equals("I")){%>
              <%if(lPenaCumulo.getNumAnniIsolamentoDiurno()!=null){%>
              <font class="label">Anni</font>
              <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenaCumulo.getNumMesiIsolamentoDiurno()!=null){%>
              <font class="label">Mesi</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiIsolamentoDiurno(),"0")%></font>
              <%}%>

              <%if(lPenaCumulo.getNumGiorniIsolamentoDiurno()!=null){%>
              <font class="label">Giorni</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniIsolamentoDiurno(),"0")%></font>
              <%}%>
            <%}%>
         <%}%>
      </td>
    </tr>
<%
  }
}

//==============================================================================      
//SANZIONE SOSTITUTIVA
//==============================================================================      
PenaComplessivaSanzioneSostitutivaModel lPenComSanSost = dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
boolean lEsisteSanSost = false;
if(lPenComSanSost!=null)
{
SanzioneSostitutivaModel lSanSos = lPenComSanSost.getSanzioneSostitutiva();
if(lSanSos != null && lSanSos.getIdSanzioneSostitutiva() != null)
{
%>

<tr>
<td class="L"><font class="label">Sanzione Sostitutiva applicata: </font>

<%
if((lSanSos.getNumAnni()!=null && lSanSos.getNumAnni().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumMesi()!=null && lSanSos.getNumMesi().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumGiorni()!=null && lSanSos.getNumGiorni().compareTo(new BigDecimal(0))!=0))
{
lEsisteSanSost=true;
%>

<font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;</font>
<%if("E".equals(lSanSos.getCodTipoSanzione())){ %>
per un periodo di
<%} %>
<font class="label">&nbsp;Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
<font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
<font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>

<%
}

if(lSanSos.getSanzionePecuniariaMulta() != null && lSanSos.getSanzionePecuniariaMulta().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Multa&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%>&nbsp;</font>&euro;
<%
}
if(lSanSos.getSanzionePecuniariaAmmenda() != null && lSanSos.getSanzionePecuniariaAmmenda().intValue() != 0)
{
%>
<font class="label"> Sanz.Pec. Ammenda&nbsp;</font><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%>&nbsp;</font>&euro;
<%
}
%>

</td>
</tr>
<%
}
}  
//==============================================================================      
//                          SANZIONE SOSTITUTIVA
//==============================================================================     
	
   PenaResiduaModel lPenResSanzSostMod=dettagliofascicolo.getPenaResidua();
 
  if(lPenResSanzSostMod != null && lPenResSanzSostMod.getFlagSanzioneSostitutiva()!= null && "S".equals(lPenResSanzSostMod.getFlagSanzioneSostitutiva()))
  {
%>
     <tr>
      <td class="l">Sanzione Sostitutiva da espiare :
           <font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(lPenResSanzSostMod.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (lPenResSanzSostMod.getImportoMultaSS() != null && lPenResSanzSostMod.getImportoMultaSS().intValue() != 0)
            || (lPenResSanzSostMod.getImportoAmmendaSS() != null && lPenResSanzSostMod.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (lPenResSanzSostMod.getImportoMultaSS() != null && lPenResSanzSostMod.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(lPenResSanzSostMod.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (lPenResSanzSostMod.getImportoAmmendaSS() != null && lPenResSanzSostMod.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(lPenResSanzSostMod.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%> 
      </td>
    </tr>

<%} 
%>	
 	 
<%

//==============================================================================
//                                 PENA DA ESPIARE
// - Pena residua (ultima pena validata)
//   - <tr>pena da espiare</tr>
//   - <tr>decorrenza</tr>
//   - <tr>pena residua</tr>
// o
// - Pena presunta
//   - <tr>pena da espiare</tr>
//   - <tr>decorrenza</tr>
//   - <tr>pena residua</tr>
//==============================================================================
	PenaResiduaModel lPenResMod=dettagliofascicolo.getPenaResidua();
	if(dettagliofascicolo.getPenaResidua() != null)
	{
	  
	  // ultima pena validata
%>
<tr>
  <td class="L">
<%if(!"S".equals(lPenResMod.getFlagSanzioneSostitutiva()))
{ %>  
   
    <font class="label">Pena da espiare : </font>
    <!--ERGASTOLO--->
<%
    if( "S".equals(lPenResMod.getFlagErgastolo()) ) 
    {
%>
	    <font class="campo">ERGASTOLO</font>
<%
    }
    else if( "D".equals(lPenResMod.getFlagErgastolo()) )
    {
%>
      <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
<%
			if(lPenResMod.getNumAnniIsolamentoDiurno()!=null && lPenResMod.getNumAnniIsolamentoDiurno().intValue()!=0 )
			{
%>
      	<font class="label">Anni</font>
	      <font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniIsolamentoDiurno(),"0")%></font>
<%
			}

			if(lPenResMod.getNumMesiIsolamentoDiurno()!=null && lPenResMod.getNumMesiIsolamentoDiurno().intValue()!=0)
			{
%>
	      <font class="label">Mesi</font>
	      <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiIsolamentoDiurno(),"0")%></font>
<%
			}

      if(lPenResMod.getNumGiorniIsolamentoDiurno()!=null && lPenResMod.getNumGiorniIsolamentoDiurno().intValue()!=0)
      {
%>
	      <font class="label">Giorni</font>
	      <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
<%
			}
    }

		if(   (lPenResMod.getNumAnniReclusione()!=null && lPenResMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
       || (lPenResMod.getNumMesiReclusione()!=null && lPenResMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
       || (lPenResMod.getNumGiorniReclusione()!=null && lPenResMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
      )
    { 
%>
        <font class="campo">Reclusione</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniReclusione(),"0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiReclusione(),"0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
<%
		}

		if ( lPenResMod.getImportoMulta()!=null && lPenResMod.getImportoMulta().compareTo(new BigDecimal(0))!=0) 
		{
%>
	    <font class="label">Multa </font>
	    <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
<%
		}

		if (   (lPenResMod.getNumAnniArresto()!=null && lPenResMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
        || (lPenResMod.getNumMesiArresto()!=null && lPenResMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
        || (lPenResMod.getNumGiorniArresto()!=null && lPenResMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
       )
    {
%>
        <font class="campo">Arresto</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniArresto(),"0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiArresto(),"0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
    <%}%>

    <% if(lPenResMod.getImportoAmmenda()!=null && lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
        <font class="label">Ammenda </font>
        <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
    <%}%>
  </td>
</tr>
<%}
//==============================================================================
//                            DECORRENZA PENA
// solo data inizio e data fine (no date intermedie)
//==============================================================================
%>
<tr>
  <td class="L">
    <%if(lPenResMod.getDataInizio()!= null){%>
      <font class="label">Inizio Pena : </font>
      <font class="cVerde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
    <%}%>

<%//---gdv 24/01/2006

//if(lPenResMod.getDataFine()!= null && lPenResMod.getDataFine().compareTo(DateUtils.getDate("21-12-9999","dd-MM-yyyy"))==0)
//{
%>
 <!--<font  color=red>ERGASTOLO </font>-->
<%
//}else{
%>
<%
      PenaComplessivaSanzioneSostitutivaModel lPenaSostMod = null;
      PenaComplessivaModel lPenCompMod= null;
      if(dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva()!=null) {
        lPenaSostMod=dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
        if(lPenaSostMod!=null){
          lPenCompMod=lPenaSostMod.getPenaComplessiva();
        }
      }

      if(   lPenCompMod != null
         && (    lPenCompMod.getCodTipoPenaDetentiva() != null
             && (   lPenCompMod.getCodTipoPenaDetentiva().equals("03")
                 || lPenCompMod.getCodTipoPenaDetentiva().equals("04")
                )
             )
        )
      { // ERGASTOLO
      %>
        <font class="label">Fine Pena : </font> <font color=red>MAI</font>
      <%
      }
      else if(   lPenResMod.getDataFine() != null
              && lPenResMod.getDataFinePresunta() != null
              && (!lPenResMod.getDataFine().equals(lPenResMod.getDataFinePresunta()))
             )
      {
      %>
        <font class="label">Fine Pena : </font>   <font  color=red><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
      <%
      }
      else
      {
        if(lPenResMod.getDataFine()!= null){ %>
           <font class="label">Fine Pena : </font><font  class="cVerde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
<%      }
//}
      }
%>
    </td>
  </tr>


<%
  //==============================================================================
  // PENA RESIDUA calcolata al volo tra la data di systema e il fine pena previsto
  // Se data inizio > sysdate non viene visualizzato (pena a decorrenza futura - detenuto altra causa)
  // Se data fine < sysdate non viene visualizzato (detenuto scarcerato o comunque fungibile)
  //==============================================================================
  CalendarModel lCalMod = new CalendarModel();
  if(   lPenCompMod != null
     && (    lPenCompMod.getCodTipoPenaDetentiva() != null
         && (   lPenCompMod.getCodTipoPenaDetentiva().equals("03")
             || lPenCompMod.getCodTipoPenaDetentiva().equals("04")
            )
         )
    )
  {
    // ERGASTOLO
    // Nel caso id ergastolo non visualizzo la pena da espiare
  }
  else
  {
    if(lPenResMod.getDataInizio()!=null  && lPenResMod.getDataFine()!=null)
    {
      if (   !DateUtils.isGreater(lPenResMod.getDataInizio(),DateUtils.getSysDate())
//          && !DateUtils.isLower(lPenResMod.getDataFine(),DateUtils.getSysDate())
          && (   DateUtils.isGreater(lPenResMod.getDataFine(), DateUtils.getSysDate())
              || DateUtils.isEquals (lPenResMod.getDataFine(), DateUtils.getSysDate())
             )
         )
      {

        lCalMod.setDataInizio (DateUtils.getSysDate());
        lCalMod.setDataFine   (lPenResMod.getDataFine());

        CalendarUtil lCalUtil=new CalendarUtil();
        lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod, true);

        // Se il quantum è >0
        // 25-01-2007 ennesimo rework, questa volta la pena residua non viene 
        // visualizzata, ma calcolata su richiesta non più tra data systema e fine pena
        // ma con la procedura della calcolatrice: quantum residuo=quantum che bisognerebbe
        // sottrarre per ottenere come data di scarcerazione la data odierna
        if( lCalUtil.getTotGiorni(lCalMod)>=0 ) {
        %>
          <tr>
            <td class="L">
              <font class="label">Pena Residua :</font>
              <a href="Javascript:CalcoloResiduoPena();">Calcolo Pena Residua da Espiare ad Oggi</a>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--
              <font class="label">Anni</font>
              <font class="campo"><%=lCalMod.getNumAnni()%></font>
              <font class="label">Mesi</font>
              <font class="campo"> <%=lCalMod.getNumMesi()%></font>
              <font class="label">Giorni</font>
              <font class="campo"> <%=lCalMod.getNumGiorni()%></font>&nbsp;
              --%>
            </td>
          </tr>
        <%
        }
      }
    }
  }

} // end pena Residua
else if(dettagliofascicolo.getPenaPresunta()!=null)
{
//==============================================================================
//                                  PENA PRESUNTA
// - <tr>pena da espiare</tr>
// - <tr>decorrenza</tr>
// - <tr>pena residua</tr>
//==============================================================================
  PenaPresuntaModel lPenPresMod=dettagliofascicolo.getPenaPresunta();
  
  %>
  <tr>
    <td class="L">
      <font class="label">Pena da espiare : </font>
      <%
      if (   (lPenPresMod.getNumAnniReclusione()!=null && lPenPresMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
          || (lPenPresMod.getNumMesiReclusione()!=null && lPenPresMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
          || (lPenPresMod.getNumGiorniReclusione()!=null && lPenPresMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
         )
      {
      %>
        <font class="campo">Reclusione</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lPenPresMod.getNumAnniReclusione(),"0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumMesiReclusione(),"0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
      <%}%>


      <%if(lPenPresMod.getImportoMulta()!=null && lPenPresMod.getImportoMulta().compareTo(new BigDecimal(0))!=0) {%>
      <font class="label">Multa </font>
      <font class="campo"><%=StringUtils.toEuroFormat(lPenPresMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
      <%}%>

      <%
      if (   (lPenPresMod.getNumAnniArresto()!=null && lPenPresMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
          || (lPenPresMod.getNumMesiArresto()!=null && lPenPresMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
          || (lPenPresMod.getNumGiorniArresto()!=null && lPenPresMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
         )
      {%>
        <font class="campo">Arresto</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lPenPresMod.getNumAnniArresto(),"0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumMesiArresto(),"0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
      <%}%>

      <%if(lPenPresMod.getImportoAmmenda()!=null && lPenPresMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
      <font class="label">Ammenda </font>
      <font class="campo"><%=StringUtils.toEuroFormat(lPenPresMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
      <%}%>
    </td>
  </tr>

  <%
  //==============================================================================
  // Decorrenza
  //==============================================================================
  %>
  <tr>
    <td class="L">
      <%if(lPenPresMod.getDataInizio()!= null ){%>
      <font class="label">Inizio Pena</font>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenPresMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
      <%}%>

      <%if(lPenPresMod.getDataFine()!= null &&  !lPenPresMod.getDataFine().equals("")){%>
      <font class="label">Fine Pena</font>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenPresMod.getDataFine(),"dd-MM-yyyy"))%></font>
      <%}%>
    </td>
  </tr>
<%
    //==========================================================================
    // PENA RESIDUA calcolata al volo tra la data di systema e il fine pena
    // previsto.
    // Se data inizio > sysdate non viene visualizzato (pena a decorrenza futura - detenuto altra causa)
    // Se data fine < sysdate non viene visualizzato (detenuto scarcerato o comunque fungibile)
    //==========================================================================
    CalendarModel lCalMod = new CalendarModel();
    if( lPenPresMod.getDataFine() != null )
    {
      if (   !DateUtils.isGreater(lPenPresMod.getDataInizio(),DateUtils.getSysDate())
          && DateUtils.isLower(lPenPresMod.getDataFine(),DateUtils.getSysDate())
         )
      {
        // data inizio<=sysdate (pena in decorrenza)
        lCalMod.setDataInizio(DateUtils.getSysDate());
        lCalMod.setDataFine(lPenResMod.getDataFine());

        CalendarUtil lCalUtil=new CalendarUtil();
        lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);


        // Se il quantum è positivo e non è 0
        if( lCalUtil.getTotGiorni(lCalMod)>0 ) { %>
          <tr>
            <td class="L">
              <font class="label">Pena Residua :</font>
              <font class="label">Anni</font>
              <font class="campo"><%=lCalMod.getNumAnni()%></font>
              <font class="label">Mesi</font>
              <font class="campo"> <%=lCalMod.getNumMesi()%></font>
              <font class="label">Giorni</font>
              <font class="campo"> <%=lCalMod.getNumGiorni()%></font>&nbsp;
            </td>
          </tr>
        <%
        }
      }  // end if pena in decorrenza
    } //
  }
%>
<!------- Fine PENA PRESUNTA -------------------------------------------------->


<%
//==============================================================================
// Liberazione Anticipata
//==============================================================================

if(dettagliofascicolo.getGiorniLibConcessa()!= null && dettagliofascicolo.getGiorniLibConcessa().compareTo(new BigDecimal(0))!=0)
{
%>
    <tr>
      <td class="L">
        <font class="label">Liberazione Anticipata Concessa già detratta in giorni:</font>&nbsp;
        <font class="campo"><%=dettagliofascicolo.getGiorniLibConcessa()%></font>
      </td>
    </tr>
<%
  }
%>

<%
if(dettagliofascicolo.getGiorniLibNonConcessa()!= null && dettagliofascicolo.getGiorniLibNonConcessa().compareTo(new BigDecimal(0))!=0)
{
%>
    <tr>
      <td class="L">
        <font class="label">Liberazione Anticipata Concessa da detrarre in giorni:</font>&nbsp;
        <font class="cVerde"><%=dettagliofascicolo.getGiorniLibNonConcessa()%></font>
      </td>
    </tr>
<%
}
%>

<!------------------differimento------------------->
<%
if(dettagliofascicolo.getDecretoOrdinanzaSiep()!= null )
{
%>
  <tr>
    <td class="L">
      <font class="label">Pena Differita</font>
      <%
      if(dettagliofascicolo.getDecretoOrdinanzaSiep().getFlagDecisioneTribunale().equals("S"))
      { %>
      <font class="label">:</font><font class="campo"> fino alla decisione Del Tribunale di Sorveglianza</font>&nbsp;
      <%
      }
      else
      {
        if(dettagliofascicolo.getDecretoOrdinanzaSiep().getDataDifferimento()!= null ) { %>
        <font class="label">il :</font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettagliofascicolo.getDecretoOrdinanzaSiep().getDataDifferimento(),"dd-MM-yyyy"))%></font>
        <% } %>

        <%
        if(dettagliofascicolo.getDecretoOrdinanzaSiep().getDataRinvio()!= null ) { %>
        <font class="label">fino al :</font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettagliofascicolo.getDecretoOrdinanzaSiep().getDataRinvio(),"dd-MM-yyyy"))%></font>
        <%}%>
      <%}
}%>
</td>
</tr>


<%
//==============================================================================
//    MISURA ALTERNATIVA
//==============================================================================
MisuraAlternativaModel lMisMod = dettagliofascicolo.getMisuraAlternativa();
// Se esiste una Ma e il fascicolo non è la pena non è sospesa
// 10/04/06 Daniele-Viviana
//se Flag Situazione è uguale a N vuole dire che non devo visualizzare la situazione
//misura alternativa,viene impostata ad N nel caso di ripristino detenzione in carcere
//e ordine esecuzione proveniente dalla detenzione domiciliare a termine
//2010-2011-0030-0031-0032-0033-0201-0202-0203-0204-0205-0206-0207-0208--MOTIVI DEL DIFFERIMENTO
if(lMisMod != null && lMisMod.getIdMisuraAlternativa()!= null && !"N".equals(lMisMod.getFlagSituazione())
     && !isPenaSospesa && !"2010".equals(lMisMod.getCodTipoMisura()) && !"2011".equals(lMisMod.getCodTipoMisura())
     && !"0030".equals(lMisMod.getCodTipoMisura())&& !"0031".equals(lMisMod.getCodTipoMisura())&& !"0032".equals(lMisMod.getCodTipoMisura())
     && !"0033".equals(lMisMod.getCodTipoMisura())&& !"0201".equals(lMisMod.getCodTipoMisura())&& !"0202".equals(lMisMod.getCodTipoMisura())
     && !"0203".equals(lMisMod.getCodTipoMisura())&& !"0204".equals(lMisMod.getCodTipoMisura())&& !"0205".equals(lMisMod.getCodTipoMisura())
     && !"0206".equals(lMisMod.getCodTipoMisura())&& !"0207".equals(lMisMod.getCodTipoMisura())&& !"0208".equals(lMisMod.getCodTipoMisura()))
  {
%>
  <tr>
    <td class="L">
      <font class="label">Situazione misura alternativa : </font>
      <font class="campo">
<%
        //if(lMisMod.getCodNaturaDecisione().equals("RG")){
        if(   lMisMod.getCodNaturaDecisione() != null && lMisMod.getCodTipoMisura() != null && !lMisMod.getCodTipoMisura().equals("9000")
           && !lMisMod.getCodTipoMisura().equals("9001") && !lMisMod.getCodNaturaDecisione().equals("DD")
           && !lMisMod.getCodNaturaDecisione().equals("PE") && !lMisMod.getCodNaturaDecisione().equals("RE")
           && !lMisMod.getCodNaturaDecisione().equals("AP")
           && !lMisMod.getCodTipoMisura().equals("2145") && !lMisMod.getCodTipoMisura().equals("2146")
           && !lMisMod.getCodTipoMisura().equals("2147") && !lMisMod.getCodTipoMisura().equals("2148")
           && !lMisMod.getCodTipoMisura().equals("2149") && !lMisMod.getCodTipoMisura().equals("2150")
           && !lMisMod.getCodTipoMisura().equals("2151") && !lMisMod.getCodTipoMisura().equals("2153")
           && !lMisMod.getCodTipoMisura().equals("2005") && !lMisMod.getCodTipoMisura().equals("2006"))
        {
%>
          <%=StringUtils.toStringJSP(lMisMod.getDescrNaturaDecisione())%>
<%
        }
%>
        &nbsp;<%=lMisMod.getDescrTipoMisura()%>
      </font>
    </td>
  </tr>

  <tr>
<%
    if(!lMisMod.getCodNaturaDecisione().equals("RE"))
    {
%>
      <td class="L">
<%
      if(lMisMod.getDataInizioMisura()!= null)
      {
        if(lMisMod.getCodNaturaDecisione() != null && lMisMod.getCodNaturaDecisione().equals("SP"))
        {
%>
          <font class="label">Data decorrenza sospensione misura :</font>
<%
        }
        else
        {
%>
          <font class="label">Data decorrenza misura : </font>
<%
        }
%>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisMod.getDataInizioMisura(),"dd-MM-yyyy"))%></font>
<%
       }

    if(lMisMod.getDataFineMisura()!= null)
    {
%>
      <font class="label">Data scadenza misura : </font>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisMod.getDataFineMisura(),"dd-MM-yyyy"))%></font>
<%
    }
    else if(lMisMod.getDataScadenzaProroga() != null && "2340".equals(lMisMod.getCodTipoMisura()))
    {
%>
      <font class="label">Fino alla data</font>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisMod.getDataScadenzaProroga(),"dd-MM-yyyy"))%></font>
<%
    }
    else if(lMisMod.getFlagDecisioneTribunale() != null && "2340".equals(lMisMod.getCodTipoMisura()))
    {
%>
      <font class="label">Fino alla decisione del TDS</font>
<%
    }
%>
    </td>
  </tr>
<%
  }
}




//==============================================================================
//  SITUAZIONE SANZIONE SOSTITUTIVA
//==============================================================================
ScambioSanzioneModel lScambioMod = dettagliofascicolo.getScambioSanzione();
if(lScambioMod != null && lScambioMod.getIdScambioSanzione()!= null )
{
	
	if((lScambioMod.getCodTipoSanzione().compareTo("2470") != 0)
		&& (lScambioMod.getCodTipoSanzione().compareTo("2471") != 0))	
  	{

%>
  	<tr>
    	<td class="L">
      		<font class="label">Situazione sanzione sostitutiva : </font>
      		<font class="campo">
          	<%=StringUtils.toStringJSP(lScambioMod.getDescrNaturaSanzione())%>
      		</font>

      		<font class="label">Procedimento SIUS : </font>
      		<font class="campo">
          	<%=StringUtils.toStringJSP(lScambioMod.getChiaveAnnoFascicoloSius())%>
          	/
          	<%=StringUtils.toStringJSP(lScambioMod.getChiaveProgrFascicoloSius())%>
      		</font>
        	<font class="campo">
          	<%=StringUtils.toStringJSP(lScambioMod.getDescrUfficioEmittente())%>
          	di 
          	<%=StringUtils.toStringJSP(lScambioMod.getComuneUfficioEmittente())%>
      		</font>
    	</td>
  	</tr>

  	<tr>
		<td class="l">
			<a href="Javascript:ListaSanzioni();">
		   	Periodi Sanzione Sostitutiva
			</a>
		</td>
	</tr>  
<%	}
 
 } // Chiude if lScambiomod	

//==============================================================================
//  POSIZIONE MATERIALE
//==============================================================================
if (dettagliofascicolo.getPosizioneMateriale()!= null) { %>
<tr>
  <td class="L">
    <font class="label">Posizione Materiale :  </font>
    <font class="campo"><%=dettagliofascicolo.getPosizioneMateriale().getDescrPosizioneMateriale() %> </font>
  </td>
</tr>
<%}
 if(fascicolo.getNote()!=null){%>
     <tr>
      <td class="L">
        <font class="label">Note : </font>
        <font class="cVerde"><%=StringUtils.toStringJSP(fascicolo.getNote())%></font>
      </td>
    </tr>
	<%
	}    
	%>

<!-- CAMPI PER IL MOMENTO NON GESTITI --
  <tr>
    <td class="L">
      <font class="label">Inizio differimento : </font>
    </td>
  </tr>
  <tr>
    <td class="L">
      <font class="label">Fissazione Udienza : </font>
    </td>
  </tr>
-->

<%
//==============================================================================
//  ULTIMI EVENTI
//==============================================================================
  List lListEve = dettagliofascicolo.getEventi();

  if(lListEve != null && lListEve.size() != 0)
  {
%>
    <tr>
      <td class="Titolo" colspan=3>Ultimi Eventi</td>
    </tr>
<%
    Iterator lIterEventi = lListEve.iterator();

    for(int i=0; i< Math.min(lListEve.size(), 2); i++)
    {
      EventoNotificaModel lEveNotificaMod = (EventoNotificaModel)lIterEventi.next();
	    EventoModel lEveMod = lEveNotificaMod.getEvento();
      CampoNotaModel[] lListCampoNota = null;
	    lListCampoNota = lEveNotificaMod.getCampoNote();
      CampoNotaModel lCampoMod = null;
      
      // Vengono filtrati provvedimenti non validati. Luigi 14-04-2011
      if(UtenteConnesso.getUfficioUtente().getCodUfficio().equalsIgnoreCase(fascicolo.getChiaveUfficio()) || (lEveMod.getFlagDocumentoRegistrato() != null && lEveMod.getFlagDocumentoRegistrato().equalsIgnoreCase("S")))
      {
      
%>
      <tr>
        <td class="L">
          <font class="label">Data Emissione : </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveMod.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
           <font class="label"><%=StringUtils.toStringJSP(lEveMod.getDescrTipoProvvedimento())%>: </font>
 

<%
           
/*
          if(lEveMod.getCodEsito().equals("0112"))
          {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrEsito())%></font>&nbsp;
--%>
<%
/*
          }
*/

if(lEveMod.getCodTipoEvento() != null && lEveMod.getCodTipoEvento().equals("03")
   && ("S").equals(lEveMod.getFlagDocumentoRegistrato()))
   {
  	  String lActDettaglio = null;
	  
       ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();

       lActDettaglio = lAction.getActionDettaglioProvvedimento(lEveMod.getCodMotivo(),
       lEveMod.getCodTipoEvento(),
       lEveMod.getCodTipoProvvedimento(),
       lEveMod.getTemIdTemplate(),
       lEveMod.getFlagDocumentoRegistrato());       	  
     

%>
         <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=lEveMod.getIdEvento()%>">
             <%=StringUtils.toStringJSP(lEveMod.getDescrMotivo())%>
         </a>
<% }
   else
   {
%>
         <font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrMotivo())%></font>&nbsp;<br>


<%
   }
          if(lEveMod.getCodTipoEvento().equals("01") && lEveMod.getCodTipoProvvedimento().equals("04") && lEveMod.getCodMotivo().equals("7777"))
          {
            if((lListCampoNota != null)  && !(lListCampoNota.length==0))
            {
              lCampoMod = (CampoNotaModel)lListCampoNota[0]; // è previsto un solo campo nota
%>
    		      <font class="label">Nota : </font>
              <font class="campoNoCap"><%=SiapStringUtil.formattaCampoNote(lCampoMod.getDescr(), "<br>")%><br></font>
<%
            }
%>
<%
          }
%>
          <font class="label">Ufficio : </font>
          <font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrUfficioEmittente())%></font>
          <font class="label"> di </font>
          <font class="campo"><%=StringUtils.toStringJSP(lEveMod.getDescrLuogoEmittente())%></font>
<%
          if( (lEveMod.getCodTipoEvento() != null && !lEveMod.getCodTipoEvento().equals("03"))
              && (lEveMod.getFlagDocumentoRegistrato() == null || lEveMod.getFlagDocumentoRegistrato().equals("N")) )
          {

//Modifica 06-03-06 -- Dario -- Viviana
//modifica per permettere il link dal provvedimento non validato direttamente sul proprio dettaglio
           String lActDettaglio = null;

	          ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
            /*
             String lMotivoProvvedimento,
    String lTipoEvento,
    String lTipoProvvedimento,
    String lTemIdTemplate,
    String lValidato)
    */
	          lActDettaglio = lAction.getActionDettaglioProvvedimento(lEveMod.getCodMotivo(),
            lEveMod.getCodTipoEvento(),
            lEveMod.getCodTipoProvvedimento(),
            lEveMod.getTemIdTemplate(),
            lEveMod.getFlagDocumentoRegistrato());

            if(lActDettaglio == null || lActDettaglio.equals(""))
             {
               lActDettaglio = "siap.siep.ordineesecuzione.action.ActRicercaProvvedimenti";
             }


%>

        - <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=lEveMod.getIdEvento()%>">
            <font color="red"> NON VALIDATO</font>
        </a>&nbsp;


<%
          }
%>
        </td>
      </tr>            
<%
      } // endif
    } // endfor
  }

%>
 </table>
