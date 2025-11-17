<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.util.SIEPLookupRemote"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>
<%@ page import="java.util.Date"%>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel"%>
<%@ page import="siap.siep.fascicolo.controller.IFascicoloSiep"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Vector"%>

<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import=" siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>

<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.penapresunta.model.PenaPresuntaModel"%>

<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="fascicolo"          scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="dettagliofascicolo" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel"/>

<%
SoggettoModel soggetto = fascicolo.getSoggetto();
SentenzaModel sentenza = fascicolo.getSentenza();

// MEV_2025-48: dicitura CARTABIA se almeno uno dei reati collegati al procedimento ha una data inizio > 30/12/2022
IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
DettaglioFascicoloModel dfm = ifs.ExDettaglioFascicoloSiepNew(fascicolo.getIdFascicoloSiep());
Collection reatiCircostanzeColl = dfm.getReatiCircostanze();
Vector reatiCircostanzeVect = new Vector(reatiCircostanzeColl);
boolean isCartabia = false;
final Date dataCartabia = DateUtils.getDate("30/12/2022", "dd/MM/yyyy");
Date dataInizioReato = null;
Iterator itx = reatiCircostanzeVect.iterator();
while (itx.hasNext()) {
	ReatoModel rm = null;
	Object obj = itx.next();
	if (obj instanceof ReatoModel) {
		rm = (ReatoModel) obj;
	} else if (obj instanceof ReatoCircostanzaModel) {
		ReatoCircostanzaModel rcm = (ReatoCircostanzaModel) obj;
		rm = rcm.getReato();
	}
	Date dataReato = null;
	if (rm.getDataInizio() != null) {
		dataReato = rm.getDataInizio();
	} else if (rm.getMeseInizio() != null && rm.getAnnoInizio() != null) {
		dataReato = DateUtils.getDate(rm.getAnnoInizio().intValue(), rm.getMeseInizio().intValue(), 1);
	} else if (rm.getAnnoInizio() != null) {
		dataReato = DateUtils.getDate(rm.getAnnoInizio().intValue(), 1, 1);
	}
	if (dataReato != null) {
		if (dataInizioReato == null)
			dataInizioReato = dataReato;
		else if (DateUtils.isLower(dataInizioReato, dataReato))
			dataInizioReato = dataReato;
		if (DateUtils.isGreater(dataInizioReato, dataCartabia))
			isCartabia = true;
	}
}
// FINE MEV_2025-48
%>

<%
//==============================================================================
// I dati visualizzati nella form sono i seguenti
// - PROCEDIMENTO
// - SOGGETTO
// - SENTENZA
// - DATA IRREVOCABILITÀ/NOTE
// - stato del procedimneto ???
// - POSIZIONE GIURIDICA/istituto
// - LISTA AVVOCATI
// - PENA IRROGATA IN SENTENZA (solo se non cumulante)
// - PENA DA ESPIARE (quantum ultima pena validata)
// - DECORRENZA/SCADENZA (solo data inizio e data fine (no date intermedie))
// - PENA RESIDUA (calcolata al volo tra la data di systema e il fine pena previsto) solo se pena effettivamente in decorrenza
// - MISURA ALTERNATIVA
//==============================================================================
%>

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento : N.</font>
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

        if(fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-"))
        {
%>
          	<font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font>&nbsp;
<%
        }
// MEV_2025-48: aggiunta sezione
if (isCartabia) {
%>
			<font class="cRossoCumulo"> &nbsp;Cartabia&nbsp; </font>&nbsp;
<%
}
        if(   fascicolo.getCodStatoFascicolo() != null
           && (fascicolo.getCodStatoFascicolo().equals("01"))
           )
        {
%>
          	<font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font>&nbsp;
<%
        }

        if(   ( dettagliofascicolo.getPenaResidua()!= null
             && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa()!= null
             && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("S") )
             || (     fascicolo!= null && fascicolo.getChiaveProgr() != null
                  && ( fascicolo.getChiaveProgr().intValue() >= 30000
                  &&   fascicolo.getChiaveProgr().intValue() < 40000) ) )
        {
          if( fascicolo!= null && fascicolo.getChiaveProgr() != null
            && ( fascicolo.getChiaveProgr().intValue() >= 30000
            &&   fascicolo.getChiaveProgr().intValue() < 40000) )
          {
%>
            <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
<%
          }
          else
          {
%>
            <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
<%
          }
        }

        if(dettagliofascicolo.getPenaResidua()!= null && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa()!= null &&
            dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("I"))
        {
%>

          <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
<%
        }

        if(dettagliofascicolo.getPenaResidua()!= null && dettagliofascicolo.getPenaResidua().getFlagPenaSospesa()!= null &&
            dettagliofascicolo.getPenaResidua().getFlagPenaSospesa().equals("D"))
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
if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {
    	if (soggetto.getSesso().compareTo("F")==0)
    	{
%>
      		<font class="label">nata il :</font>&nbsp;
<%
    	} else {
%>
      		<font class="label">nato il :</font>&nbsp;
<%
    	}
    	
    	if(soggetto.getAnnoNascita() != null){
%>
        	<font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%
    	} else {
%>
			<font class="campo">**-**-****</font>&nbsp;
<%    	
   		}	
    	
   }else if (soggetto.getEtaPresuntaAnni() != null || soggetto.getEtaPresuntaMesi()!=null ){ %>
      	<font class="label">Età Presunta: </font>
<%
		if (soggetto.getEtaPresuntaAnni() != null){
%>
			anni <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font>
<%
		}
		if (soggetto.getEtaPresuntaMesi() != null){
%>
			mesi <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font>
<%			
		}
%>

<% }else {%>
      <font class="campo">**-**-****</font>&nbsp;
<% }

}else {
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
%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%
} // chiude else presenza data nascita
%>

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

      </font> <font class="label"> Codice CUI: </font>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis())%> </font>
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
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
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
<% // paolo cherubini 05/01/2011 
        if(!sentenza.getCodTipoProvvedimento().equals("02"))
        {
%>
		&nbsp;<font class="label">Data irrevocabilità : </font>
<% 
        }
		else
		{
%>
		&nbsp;<font class="label">Esecutivo il : </font>
<%		
		} 
%>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy"))%></font>
      </td>
    </tr>
<%}%>
<%if(fascicolo.getNote()!=null){%>
     <tr>
      <td class="L">
        <font class="label">Note : </font>
        <font class="campo"><%=StringUtils.toStringJSP(fascicolo.getNote())%></font>
      </td>
    </tr>
<%}%>
<!------------------->
<%
  List lListStatProc = dettagliofascicolo.getStatoProcedimento();
  if(lListStatProc != null && lListStatProc.size() != 0)
  {
%>
    <tr>
      <td class="L">
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
        <td class="L">
          <font class="label">Stato Procedimento : </font>
          <font color=red><%=StringUtils.toStringJSP(fascicolo.getDescrStatoFascicolo())%></font>
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

          <!----------- POSIZIONE GIURIDICA --------------->
<%
          if( fascicolo.getFlagAltraCausa() != null && fascicolo.getFlagAltraCausa().equals("S") &&
             (dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") || dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") ))
{%>
            <font color=red>DETENUTO PER ALTRA CAUSA</font>
<%
            if(dettagliofascicolo.getAltraCausa() != null)
            {
              Date lDataDecorrenza = dettagliofascicolo.getAltraCausa().getDataDecorrenza();

              if(lDataDecorrenza != null)
              {
%>
                <font class="label">dal : </font>
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "dd-MM-yyyy"))%></font>
<%
              }

              Date lDataScadenza = dettagliofascicolo.getAltraCausa().getDataScadenza();
              if(lDataScadenza != null)
              {
%>
                <font class="label">al : </font>
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataScadenza, "dd-MM-yyyy"))%></font>
<%
              }

              //modifica relativa al tipo istituto
              //  if(!dettagliofascicolo.getAltraCausa().getCodTipoIstituto().equals("-"))
              if(dettagliofascicolo.getAltraCausa().getIstDetIdIstitutoDetenzione() != null)
              {
%>
                <tr>
                  <td class="L">
                    <font class="label">Tipo Istituto : </font>
                    <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
<%
                   // modifica relativa al tipo istituto
                   //   if( !dettagliofascicolo.getAltraCausa().getDescrLuogoIstituto().equals("-") )
                   //   {
%>
                        <font class="label">Luogo Detenzione</font>
                        <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getDescrComune())%></font>
                        <font class="label">Indirizzo </font>
                        <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getIstitutoDetenzione().getIndirizzo())%></font>

<%
                 //   }
%>
                  </td>
                </tr>
<%
              }
              else
              {
                //modifica relativa al tipo istituto
                if(dettagliofascicolo.getAltraCausa().getAltroLuogo() != null && !dettagliofascicolo.getAltraCausa().getAltroLuogo().equals(""))
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
              //fine modifica relativa al tipo istituto
            }
          }
          else
          {
%>
            <font color=red><%=StringUtils.toStringJSP(dettagliofascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica())%></font>
<%
            Date lDataInizio = dettagliofascicolo.getPosizioneGiuridica().getDataInizio();
            // Nel caso di LIBERO (07, 10) non viene scritto 'dal..'
            String lCodPosizione = dettagliofascicolo.getPosizioneGiuridica().getCodPosizioneGiuridica();

            if(  lCodPosizione != null && !lCodPosizione.equals("")
              && !lCodPosizione.equals("07") && !lCodPosizione.equals("10")
              && lDataInizio != null )
            {
%>
              <font class="label">dal : </font>
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataInizio, "dd-MM-yyyy"))%>
              </font>
<%
            }

            //Date lDataFine=dettagliofascicolo.getPosizioneGiuridica().getDataFine();

            //if( lDataFine == null)
            //{
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <font class="campo">
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataInizio, "dd-MM-yyyy"))%>
    </font>
--%>
<%
            //}
%>
        </td>
      </tr>
<%
      if(dettagliofascicolo.getLuogoDetenzione() != null )
      {
        //modifica relativa al tipo istituto
        //if(!dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals("-") ||!dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals("") )
        if(dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null && !dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals(""))
        {
%>
          <tr>
            <td class="L">
              <font class="label">Tipo Istituto : </font>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
<%
              //modifica relativa al tipo istituto
              //  if( !dettagliofascicolo.getLuogoDetenzione().getCodLuogo().equals("-") )
              //  {
%>
              <font class="label">Luogo Detenzione</font>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%></font>
              <font class="label">Indirizzo :</font>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo())%></font>
            </td>
          </tr>
<%
      }
      else
      {
        if(dettagliofascicolo.getLuogoDetenzione().getAltroLuogo() != null && !dettagliofascicolo.getLuogoDetenzione().getAltroLuogo().equals(""))
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
            //  }
        //}
      }
    }
  }

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
      </td>
    </tr>
<%}}%>


<%
//==============================================================================
//
//==============================================================================
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
 <%if((lPenCompMod.getNumAnniReclusione()!=null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lPenCompMod.getNumMesiReclusione()!=null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lPenCompMod.getNumGiorniReclusione()!=null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
          {%>
          <font class="campo">Reclusione</font>

               <font class="label">Anni</font>
                <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>
                <font class="label">Mesi</font>
                <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>
                <font class="label">Giorni</font>
                <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
             <%}%>
             <%if(lPenCompMod.getImportoMulta()!=null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0))!=0)
                {%>
                    <font class="label">Multa </font>
                    <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;€&nbsp;
                 <%}%>



  <%if((lPenCompMod.getNumAnniArresto()!=null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lPenCompMod.getNumMesiArresto()!=null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lPenCompMod.getNumGiorniArresto()!=null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
          {%>


                  <font class="campo">Arresto</font>


                   <font class="label">Anni</font>
                   <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>
                   <font class="label">Mesi</font>
                   <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>
                   <font class="label">Giorni</font>
                   <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;

        <%}%>
         <%if(lPenCompMod.getImportoAmmenda()!=null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
                   {%>
                        <font class="label">Ammenda </font>
                        <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;€&nbsp;
                     <%}%>

       <%if(lPenCompMod.getCodTipoPenaDetentiva()!= null)
        {
          if(lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04"))
          {%>
                <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
         <%}%>

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
//============================================================================
//                                PENA RESIDUA
//============================================================================
%>
  <tr>
    <td class="L">
      <font class="label">Pena da espiare : </font>
      <%
      //===============================
      // Reclusione
      //===============================
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
      <%}%>


      <%
      if(lPenResMod.getImportoMulta()!=null && lPenResMod.getImportoMulta().compareTo(new BigDecimal(0))!=0)
      {
      %>
      <font class="label">Multa </font>
      <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoMulta())%></font>&nbsp;€&nbsp;
      <%}%>

      <%
      //===============================
      // Arresti
      //===============================
      if(   (lPenResMod.getNumAnniArresto()!=null && lPenResMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) 
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

      <%
      if(lPenResMod.getImportoAmmenda()!=null && lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
      %>
      <font class="label">Ammenda </font>
      <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoAmmenda())%></font>&nbsp;€&nbsp;
      <%}%>
    </td>
  </tr>

<%
//==============================================================================
// Decorrenza scadenza della pena
//==============================================================================
%>  
  <tr>
    <td class="L">
        <font class="label">Inizio Pena : </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
        <font class="label">Fine Pena : </font>
<%
        PenaComplessivaSanzioneSostitutivaModel lPenaSostMod = null;
        PenaComplessivaModel lPenCompMod= null;
        if(dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva()!=null)
        {
           lPenaSostMod=dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();

           if(lPenaSostMod!=null)
           {
             lPenCompMod=lPenaSostMod.getPenaComplessiva();
           }
        }
        
        if(lPenCompMod != null && ( lPenCompMod.getCodTipoPenaDetentiva() != null && (lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04"))) )
        {
%>
          <font color=red>MAI</font>
<%
        }
        else
        {
%>
          <font  color=red><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
<%
        }
%>
    </td>
  </tr>
  
  
<%
  //============================================================================
  // Visualizzazine della pena residua ricalcolata al volo tra la data di 
  // systema e il fine pena previsto
  // Se data inizio > sysdate non viene visualizzato (pena a decorrenza futura - detenuto altra causa)
  // Se data fine < sysdate non viene visualizzato (detenuto scarcerato o comunque fungibile)
  //============================================================================
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
    if(lPenResMod.getDataFine()!=null)
    {
      if (   !DateUtils.isGreater(lPenResMod.getDataInizio(),DateUtils.getSysDate())
//          && !DateUtils.isLower(lPenResMod.getDataFine(),DateUtils.getSysDate())
          && (   DateUtils.isGreater(lPenResMod.getDataFine(), DateUtils.getSysDate())
              || DateUtils.isEquals (lPenResMod.getDataFine(), DateUtils.getSysDate())
             )
         )
      {
        // Se la Data Fine è precedente alla data fine, il risultato
        // viene negativo e non viene visualizzato
        lCalMod.setDataInizio(DateUtils.getSysDate());
        lCalMod.setDataFine(lPenResMod.getDataFine());

        CalendarUtil lCalUtil=new CalendarUtil();
        lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod,true);

      // Se il quantum è positivo e non è 0
        if( lCalUtil.getTotGiorni(lCalMod)>=0 ) 
        {
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
            <font class="campo"><%=lCalMod.getNumGiorni()%></font>&nbsp;
            --%>
          </td>
        </tr>
<%
        }
      }
    }
  }  
}
else if(dettagliofascicolo.getPenaPresunta()!=null)
{
  //============================================================================
  //                                PENA PRESUNTA
  //============================================================================
  PenaPresuntaModel lPenPresMod=dettagliofascicolo.getPenaPresunta();%>
       <tr>
      <td class="L">
         <font class="label">Pena da espiare : </font>
  <%if((lPenPresMod.getNumAnniReclusione()!=null && lPenPresMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lPenPresMod.getNumMesiReclusione()!=null && lPenPresMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lPenPresMod.getNumGiorniReclusione()!=null && lPenPresMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
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
         <%if(lPenPresMod.getImportoMulta()!=null && lPenPresMod.getImportoMulta().compareTo(new BigDecimal(0))!=0)
                {%>
                    <font class="label">Multa </font>
                    <font class="campo"><%=StringUtils.toEuroFormat(lPenPresMod.getImportoMulta())%></font>&nbsp;€&nbsp;
                 <%}%>
       <%if((lPenPresMod.getNumAnniArresto()!=null && lPenPresMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lPenPresMod.getNumMesiArresto()!=null && lPenPresMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lPenPresMod.getNumGiorniArresto()!=null && lPenPresMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
        {%>

             <font class="campo">Arresto</font>


                   <font class="label">Anni</font>
                   <font class="campo"><%=StringUtils.toStringJSP(lPenPresMod.getNumAnniArresto(),"0")%></font>
                   <font class="label">Mesi</font>
                   <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumMesiArresto(),"0")%></font>
                   <font class="label">Giorni</font>
                   <font class="campo"> <%=StringUtils.toStringJSP(lPenPresMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
                <%}%>
                   <%if(lPenPresMod.getImportoAmmenda()!=null && lPenPresMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
                   {%>
                        <font class="label">Ammenda </font>
                        <font class="campo"><%=StringUtils.toEuroFormat(lPenPresMod.getImportoAmmenda())%></font>&nbsp;€&nbsp;
                     <%}%>

                 </td>

    </tr>
<tr>
    <td class="L">
                <font class="label">Inizio Pena</font>
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenPresMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
                <font class="label">Fine Pena</font>
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenPresMod.getDataFine(),"dd-MM-yyyy"))%></font>

      </td>
    </tr>
<%
    CalendarModel lCalMod = new CalendarModel();
    if(lPenPresMod.getDataFine()!=null)
    {
      // Se la Data Fine è precedente alla data fine, il risultato
      // viene negativo e non viene visualizzato
      lCalMod.setDataInizio(DateUtils.getSysDate());
      lCalMod.setDataFine(lPenResMod.getDataFine());

      CalendarUtil lCalUtil=new CalendarUtil();
      lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);

      // Se il quantum è positivo e non è 0
      if(     lCalUtil.isPositiveTime(lCalMod)
          && ((lCalMod.getNumAnni()>0) || (lCalMod.getNumMesi()>0) || (lCalMod.getNumGiorni()>0)))
      {
%>
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
    }
  }
%>
<%
//==============================================================================
// Misure Alternativa
//==============================================================================
  MisuraAlternativaModel lMisMod = dettagliofascicolo.getMisuraAlternativa();

  if(lMisMod != null && lMisMod.getIdMisuraAlternativa()!= null)
  {
%>
    <tr>
    <td class="L">
      <font class="label">Tipo misura alternativa : </font>
      <font class="campo"><%=lMisMod.getDescrTipoMisura()%></font>
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
  </table>