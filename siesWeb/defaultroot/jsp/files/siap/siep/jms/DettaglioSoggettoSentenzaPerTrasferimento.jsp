<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.penapresunta.model.PenaPresuntaModel" %>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import=" siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import=" siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel" %>

<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel" %>
<%@ page import="siap.siep.statoprocedimento.model.StatoProcedimentoModel" %>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="dettagliofascicolo" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />

<%
  FascicoloSiepModel fascicolo = dettagliofascicolo.getFascicoloSiep();
  SoggettoModel soggetto =  dettagliofascicolo.getFascicoloSiep().getSoggetto();
  SentenzaModel sentenza =  dettagliofascicolo.getFascicoloSiep().getSentenza();
%>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <table cellspacing=0 cellpadding=0 width=95%>
      <tr>
        <td class="Titolo" colspan=4>Dati Procedimento&nbsp;&nbsp;&nbsp;&nbsp;</td>
      </tr>
    <tr>
      <td>
        <font class="label">Procedimento N.</font>
        <font class="campo">
          <%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>
          /
          <%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>
        </font>&nbsp;

       <font class="label">Data Iscrizione :</font>
        <font class="campo">
         <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataInserimento(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
	<font class="campo">
	  <%=StringUtils.toStringJSP(soggetto.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(soggetto.getNome())%>
	</font>&nbsp;
<%
	  if (soggetto.getSesso().compareTo("F")==0)
	  {%>
	    <font class="label">nata il :</font>&nbsp;
	<%}else{%>
	    <font class="label">nato il :</font>&nbsp;
	<%}

	if(soggetto.getDataNascita() == null)
	{
	  if(soggetto.getDataNascitaPresunta().equals("S"))
	  {%>
	    <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
	<%}else{%>
	    <font class="campo">***</font>&nbsp;
	<%}
	}else{%>
	  <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
      <%}%>
	<font class="label">in : </font>
	<font class="campo">
<%
	if (soggetto.getDescrComuneNascita().compareTo("-")==0)
	{%>
	  <%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>
      <%}else{%>
	  <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita())+ "  ("+StringUtils.toStringJSP(soggetto.getCodProvinciaNascita())+")" %>
      <%}%>
	</font>
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoProvvedimento())%></font>&nbsp;<font class="label">N.</font>
        <font class="campo">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%>&nbsp;
          <font class="label">del</font>&nbsp;
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy"))%>
	  </font>
	    <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
 		}else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
	  <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
<%
	  if (sentenza.getNumSezioneAutoritaEmittente() != null)
	  {%>
	    <font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
	<%}%>
	  <font class="label"> di </font>
	  <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
	</td>
      </tr>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%-- 
      ////  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
      %if(sentenza.getDataIrrevocabilita()!=null)
		{%>
	  <tr>
	    <td class="L">
	      <font class="label">Data irrevocabilità : </font>
	      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataIrrevocabilita(), "dd-MM-yyyy"))%></font>
	    </td>
	  </tr>
      < %}%
      --%>
	<%if(fascicolo.getNote()!=null)
	  {%>
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
        <font class="label">Stato Procedimento : </font>
<%
          Iterator lIterProc = lListStatProc.iterator();
          for(int i=0;i<lListStatProc.size();i++)
          {
            StatoProcedimentoModel lStatoProcMod = (StatoProcedimentoModel)lIterProc.next();
%>
            <font class="campo"><%=StringUtils.toStringJSP(lStatoProcMod.getDescrStatoProcedimento())%></font>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoProcMod.getData(),"dd-MM-yyyy"))%></font>&nbsp;&nbsp;
<%
        	}
%>
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
          <font class="campo"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoFascicolo())%></font>
        </td>
      </tr>
<%
    }
  }

  if(dettagliofascicolo.getPosizioneGiuridica() != null)
  {
%>
      <tr>
        <td class="L">
          <font class="label">Posizione Giuridica : </font>&nbsp;
          <!-----------Posizione giuridica--------------->
<%
          if( fascicolo.getFlagAltraCausa() != null && fascicolo.getFlagAltraCausa().equals("S"))
          {
%>
            <font class="campo">DETENUTO PER ALTRA CAUSA</font>
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
<%   //modifica relativa al tipo istituto
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
              }else{
 //modifica relativa al tipo istituto
 if(dettagliofascicolo.getAltraCausa().getAltroLuogo() != null && !dettagliofascicolo.getAltraCausa().getAltroLuogo().equals(""))
               {%>
        <tr>
        <td class="L">
                <font class="label">Indirizzo </font>
                <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getAltraCausa().getAltroLuogo())%></font>
        </td>
         </tr>

            <%   }
              }
 //fine modifica relativa al tipo istituto
            }
          }
          else
          {
%>
            <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica())%></font>
<%
            Date lDataInizio=dettagliofascicolo.getPosizioneGiuridica().getDataInizio();
            if(lDataInizio!=null)
            {
%>
              <font class="label">dal : </font>
<%
            }

            Date lDataFine=dettagliofascicolo.getPosizioneGiuridica().getDataFine();
            if(lDataFine==null)
            {
%>
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataInizio,"dd-MM-yyyy"))%>
              </font>
<%
            }
%>
        </td>
      </tr>

<%
      if(dettagliofascicolo.getLuogoDetenzione() != null )
      {
 //modifica relativa al tipo istituto
        //if(!dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals("-") ||!dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals("") )
   if(dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null && !(dettagliofascicolo.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals(""))
   && (dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione()!= null))

        {
%>
<tr>
        <td class="L">
              <font class="label">Tipo Istituto : </font>
              <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%></font>
<% //modifica relativa al tipo istituto
            //  if( !dettagliofascicolo.getLuogoDetenzione().getCodLuogo().equals("-") )
            //  {
%>
                <font class="label">Luogo Detenzione</font>
                <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%></font>
                <font class="label">Indirizzo </font>
                <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo())%></font>
 </td>
          </tr>

<% }else
       {

          if(dettagliofascicolo.getLuogoDetenzione().getAltroLuogo() != null && !dettagliofascicolo.getLuogoDetenzione().getAltroLuogo().equals(""))
               {%>
<tr>
        <td class="L">
                <font class="label">Indirizzo </font>
                <font class="campo"><%=StringUtils.toStringJSP(dettagliofascicolo.getLuogoDetenzione().getAltroLuogo())%></font>
 </td>
          </tr>
               <%}
        }
            //  }
%>

<%
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

  List lListaAvvocati = new Vector();
lListaAvvocati=dettagliofascicolo.getAvvocati();
if(lListaAvvocati.size()>0)
{
   for(int i=0; i<lListaAvvocati.size();i++){
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

       <%if(lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04")){%>
                <font class="campo">Ergastolo</font>
       <%}%>




      </td>
    </tr>
<%
  }
}
}
%>
<%
 PenaResiduaModel lPenResMod=dettagliofascicolo.getPenaResidua();
if(dettagliofascicolo.getPenaResidua() != null){
%>
<tr>
      <td class="L">
         <font class="label">Pena da espiare : </font>
 <%if((lPenResMod.getNumAnniReclusione()!=null && lPenResMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lPenResMod.getNumMesiReclusione()!=null && lPenResMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lPenResMod.getNumGiorniReclusione()!=null && lPenResMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
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


      <%if(lPenResMod.getImportoMulta()!=null && lPenResMod.getImportoMulta().compareTo(new BigDecimal(0))!=0)
                {%>
                    <font class="label">Multa </font>
                    <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoMulta())%></font>&nbsp;€&nbsp;
                 <%}%>

  <%if((lPenResMod.getNumAnniArresto()!=null && lPenResMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lPenResMod.getNumMesiArresto()!=null && lPenResMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lPenResMod.getNumGiorniArresto()!=null && lPenResMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
        {%>


             <font class="campo">Arresto</font>


                   <font class="label">Anni</font>
                   <font class="campo"><%=StringUtils.toStringJSP(lPenResMod.getNumAnniArresto(),"0")%></font>
                   <font class="label">Mesi</font>
                   <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumMesiArresto(),"0")%></font>
                   <font class="label">Giorni</font>
                   <font class="campo"> <%=StringUtils.toStringJSP(lPenResMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
<%}%>

          <%if(lPenResMod.getImportoAmmenda()!=null && lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
                   {%>
                        <font class="label">Ammenda </font>
                        <font class="campo"><%=StringUtils.toEuroFormat(lPenResMod.getImportoAmmenda())%></font>&nbsp;€&nbsp;
                     <%}%>

            </td>

    </tr>
<tr>
    <td class="L">
                <font class="label">Inizio Pena : </font>
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
                <font class="label">Fine Pena : </font>
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenResMod.getDataFine(),"dd-MM-yyyy"))%></font>
            </td>
</tr>
   <%CalendarModel lCalMod = new CalendarModel();
 if(lPenResMod.getDataFine()!=null){
    lCalMod.setDataInizio(lPenResMod.getDataFine());
    lCalMod.setDataFine(DateUtils.getSysDate());

    CalendarUtil lCalUtil=new CalendarUtil();
    lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);

if((lCalMod.getNumAnni()>0) && (lCalMod.getNumMesi()>0) && (lCalMod.getNumGiorni()>0)){%>
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
    <%}%>
<%}%>
<%}else if(dettagliofascicolo.getPenaPresunta()!=null){
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
   <%CalendarModel lCalMod = new CalendarModel();
    if(lPenPresMod.getDataFine()!=null){
    lCalMod.setDataInizio(lPenPresMod.getDataFine());
    lCalMod.setDataFine(DateUtils.getSysDate());

    CalendarUtil lCalUtil=new CalendarUtil();
    lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);

if((lCalMod.getNumAnni()>0) && (lCalMod.getNumMesi()>0) && (lCalMod.getNumGiorni()>0)){%>
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
<%}}%>
<%}%>
<!-- pena presunta--------------------------------------------------------------------------------->
     <%MisuraAlternativaModel lMisMod =dettagliofascicolo.getMisuraAlternativa();%>

   <%if(lMisMod != null && lMisMod.getIdMisuraAlternativa()!= null){%>
    <tr>
      <td class="L">
             <font class="label">Tipo misura alternativa : </font>
             <font class="campo"><%=lMisMod.getDescrTipoMisura()%></font>

      </td>
  </tr>
<%}%>
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
<%
  List lListEve = dettagliofascicolo.getEventi();
  if(lListEve != null && lListEve.size() != 0)
  {
%>
    <tr><td class="Titolo" colspan=3>Ultimi Eventi</td></tr>
<%
    Iterator lIterEventi = lListEve.iterator();

    for(int i=0; i< Math.min(lListEve.size(), 2); i++)
    {
      if (lIterEventi.hasNext())
      {
      EventoNotificaModel lEveMod = (EventoNotificaModel)lIterEventi.next();
%>
      <tr>
        <td class="L">
          <font class="label">Data Emissione : </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveMod.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
          <font class="label"><%=StringUtils.toStringJSP(lEveMod.getEvento().getDescrTipoProvvedimento())%>: </font>
          <font class="campo"><%=StringUtils.toStringJSP(lEveMod.getEvento().getDescrMotivo())%></font>&nbsp;
          <font class="label">Ufficio : </font>
          <font class="campo"><%=StringUtils.toStringJSP(lEveMod.getEvento().getDescrUfficioEmittente())%></font>
          <font class="label"> di </font>
          <font class="campo"><%=StringUtils.toStringJSP(lEveMod.getEvento().getDescrLuogoEmittente())%></font>
<%
          if( (lEveMod.getEvento().getCodTipoEvento() != null && !lEveMod.getEvento().getCodTipoEvento().equals("03"))
              && (lEveMod.getEvento().getFlagDocumentoRegistrato() == null || lEveMod.getEvento().getFlagDocumentoRegistrato().equals("N")) )
          {
%>
            <font color="red"> - NON VALIDATO</font>
<%
          }
%>
        </td>
      </tr>
<%}
    }
  }
%>
  </table>