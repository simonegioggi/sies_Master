<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Date"%>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="aCampoNota"          scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>

<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="penaresiduavalidata" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="annotazioni"         scope="request" class="java.util.Vector"/>

<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>


<jsp:useBean id="TIPO_COMPUTO_LA" scope="request" class="java.lang.String" />
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
  // Test su ergastolo
  boolean isErgastolo = penaresiduavalidata.isErgastolo();

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel       lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel    lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel              lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

// 20/05/2014 - Nuova L.A.  - DL 146/2013 - Gestione L.A. SPECIALE e L.A. INTEGRAZIONE

LicenzaPeriodiLibAnticipataModel lLicMod = new LicenzaPeriodiLibAnticipataModel();
//PeriodoLibAnticipataModel[] lperiodiLA ;

// ===================================================================
// Preparo i totali gg concessi delle varie tipologie di L.A.

Iterator IteLic = LicenzePeriodi.iterator();

int totggLA = 0;
int totperLA = 0;
int totggLS = 0;
int totperLS = 0;
int totggLI = 0;
int totperLI = 0;
boolean NuovaLA = false;

while(IteLic.hasNext())
{
    lLicMod = (LicenzaPeriodiLibAnticipataModel)IteLic.next();
      if(lLicMod.getLicenza() != null )
      {
        if(lLicMod.getLicenza().getDescrStatoPermesso() != null)
        {
          if(lLicMod.getLicenza().getDescrStatoPermesso().compareTo("LA") == 0)
          {
            NuovaLA = true;
            totggLA = lLicMod.getLicenza().getNumeroGiorni().intValue();
            if(lLicMod.getPeriodi()[0] != null && lLicMod.getPeriodi()[0].getIdPeriodoLibanticipata()!= null)
              totperLA = lLicMod.getPeriodi().length;
          }
          
          if(lLicMod.getLicenza().getDescrStatoPermesso().compareTo("LS") == 0)
            {
              NuovaLA = true;
              totggLS = lLicMod.getLicenza().getNumeroGiorni().intValue();
              if(lLicMod.getPeriodi()[0] != null && lLicMod.getPeriodi()[0].getIdPeriodoLibanticipata()!= null )
              totperLS = lLicMod.getPeriodi().length;
            }
          
          if(lLicMod.getLicenza().getDescrStatoPermesso().compareTo("LI") == 0)
          {
            NuovaLA = true;
              totggLI = lLicMod.getLicenza().getNumeroGiorni().intValue();
              if(lLicMod.getPeriodi()[0] != null && lLicMod.getPeriodi()[0].getIdPeriodoLibanticipata()!= null)
              totperLI = lLicMod.getPeriodi().length;
          }
        
        }
        else
        { 
          totggLA = lLicMod.getLicenza().getNumeroGiorni().intValue();
          if(lLicMod.getPeriodi()[0] != null && lLicMod.getPeriodi()[0].getIdPeriodoLibanticipata()!= null)
            totperLA = lLicMod.getPeriodi().length;
        } 
      }
      
}   // Chiude primo ciclo For

//===============================================================================
//Preparo gli array con i periodi VALIDI concessi delle varie tipologie di L.A. 

  PeriodoLibAnticipataModel[] lperiodiLA = new PeriodoLibAnticipataModel[totperLA]; 
  PeriodoLibAnticipataModel[] lperiodiLS = new PeriodoLibAnticipataModel[totperLS]; 
  PeriodoLibAnticipataModel[] lperiodiLI = new PeriodoLibAnticipataModel[totperLI]; 
  
  Iterator ItePer = LicenzePeriodi.iterator();
  while(ItePer.hasNext())
  {
    lLicMod = (LicenzaPeriodiLibAnticipataModel)ItePer.next();
      if(lLicMod.getLicenza() != null )
      {
        if(lLicMod.getLicenza().getDescrStatoPermesso() != null)
        {
          if(lLicMod.getLicenza().getDescrStatoPermesso().compareTo("LA") == 0)
          {
            if(totperLA > 0)
              lperiodiLA = lLicMod.getPeriodi();
          }
          
          if(lLicMod.getLicenza().getDescrStatoPermesso().compareTo("LS") == 0)
          {
            if(totperLS > 0)
              lperiodiLS = lLicMod.getPeriodi();
          }
          
          if(lLicMod.getLicenza().getDescrStatoPermesso().compareTo("LI") == 0)
          {
            if(totperLI > 0)
              lperiodiLI = lLicMod.getPeriodi();
          }
          
        }
        else
        {
          if(totperLA > 0)
            lperiodiLA = lLicMod.getPeriodi();
        }
      } 
  }

%>
<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Rideterminazione Pena Ridimensionamento LA - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script>
    function CalcoloPena(idEvento){
      
      document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActCalcoloPenaScompRidimLA";

      if (typeof (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>)!="undefined")  
      {
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length==1)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value='0'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length==1)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value='0'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;

        var data_scarcerazione = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value
                            +'/'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value
                            +'/'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;

        if (!ControllaDataPassaVuota(data_scarcerazione) )
        {
          alert('Data scarcerazione non valida');
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
          return false;
        }       
      }   

      try{
        document.f.CALCOLO.disabled=true;
      }
      catch(err) {
        //alert('Tasto CALCOLO Assente nella form');
      }
      
      try{
        document.f.STAMPA.disabled=true;
      }
      catch(err) {
        //alert('Tasto STAMPA  Assente nella form');
      }

      document.f.submit();

    }
    
    function caricaStampe(idEvento) {
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadGrigliaRidetPenaRidimLA&<%=ICostantiEvento.CAMPO_ID_EVENTO%>="+idEvento;
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
        <% if (TIPO_COMPUTO_LA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)) {%>
        <font class="campo">Dettaglio Rideterminazione Pena Revoca LA </font>
        <% } else { %>
        <font class="campo">Dettaglio Rideterminazione Pena Ridimensionamento LA </font>
        <% } %>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<%
//==============================================================================
// Sezione Contenente
// - Posizione giuridica
// - Luogo di detenzione
//==============================================================================
%>
  <table style="width: 95%;">
    <tr>
      <td class="l">Posizione Giuridica:
        <font class="campo">
        <% 
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){%>
        DETENUTO PER ALTRA CAUSA
        <%} else {%>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
        <%}%>
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
        <td class="l">Detenuto presso <font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
           di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
      </tr>
        <%
        if (lAltraCausa.getAltroLuogo()!=null) { %>
          <tr>
            <td class="l">Altro Luogo </td>
            <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
            </td>
          </tr>
        <% }
      }  //
    }
    else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
    { // non detenuto altra causa
    %>
    <tr>
      <td class="l">Detenuto presso
        <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
      </td>
    </tr>
    <% } %>

    <% 
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    if(    lPosizione.getCodPosizioneGiuridica() != null
       && (   lPosizione.getCodPosizioneGiuridica().equals("02")
           || lPosizione.getCodPosizioneGiuridica().equals("04")
          )
      )
    {
      if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
      <tr>
        <td class="l">Indirizzo
          <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
        </td>
       </tr>
       <% }
    }
    %>
  </table>

<%
//==============================================================================
//                Sezione contenente le Annotazioni inserite
//==============================================================================
%>
<table>
<%
  Iterator iter = annotazioni.iterator();
  while (iter.hasNext())
  {
   
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)iter.next();
  %>
    <tr>
      <% if(lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("-")) {%>
      <td class="Titolo"  colspan=10> Detratta Pena </td>
      <%}else if(lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+")) {%>
      <td class="Titolo"  colspan=10> Aggiunta Pena </td>
      <%}%>
    </tr>

    <tr>
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoMulta()))%></font>&nbsp;</td>
    </tr>

    <tr>
      <td class="l"><font class="label">Arresto / Ammenda : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda()))%></font>&nbsp;</td>
    </tr>
  </table>
  <table>
    <tr>
      <td class="l">Motivazioni :</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getMotivazioni())%></font>&nbsp;</td>
    </tr>
<%
 } // end while
%>
</table>

<%
//==============================================================================
//                Sezione contenente la Pena Ricalcolata
//==============================================================================
%>

<%
//==============================================================================
//         Sezione contenente il Riepilogo dei dati del Provvedimento
// - foglio complementare
// - data trasmissione
// - magistrato competente
// - notifiche
//==============================================================================
%>
<table width=95%>
  <tr>
    <td class="Titolo" colspan=10> Provvedimento</td>
  </tr>
</table>

<table width=95%>
  <tr>
    <td class="l">Oggetto :&nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(evento.getDescrMotivo())%></font>
    </td>
  </tr>
  <tr>
    <td class="l">Nota :&nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(aCampoNota.getDescr())%></font>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td class="l">Data Emissione :&nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(), "dd-MM-yyyy") )%>   </font>
    </td>

  <%  
  if(magistrato != null){%>
   <td class="l">Magistrato Firmatario :&nbsp;</td>
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
  <%}%>
</table>
  <br>
 
 <!--     Nuova L.A.  - DL 146/2013 --> 
<table width=80%>
  <tr>
    <% if (TIPO_COMPUTO_LA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)) {%>
    <td class="Titolo" colspan="2"> Totale Giorni Revocati </td>
    <% } else { %>
    <td class="Titolo" colspan="2"> Totale Giorni Scomputati </td>
    <% } %>
    <td class="Titolo"  colspan="4"> Relativamente ai Periodi </td>
  </tr>
  
  <!--    GESTIONE LIBERAZIONE ANTICIPATA  -->
<%  if(totggLA > 0) 
  { %>  
    <tr>
      <td class="l">Giorni di Liberazione Anticipata </td>
      <td class="L">
          <font class="campo"><%=totggLA %></font>
    </td>     
<%
    if(totperLA > 0)
    {  
        for (int k=0;k<lperiodiLA.length;k++)
        {
          if (k>0)
          {   %>
          <td></td>     
            <td></td>  
<%        } %>
        <td class="l" width ="10%"><font class="label"><%=k+1%>)</font></td>
          <td class="L"><font class="campo">
             <%=StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLA[k].getDataInizio(),"dd-MM-yyyy")).length()>0 ?
             StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLA[k].getDataInizio(), "dd-MM-yyyy")): "-"%></font>&nbsp;
             
          <td class="l"><font class="label">&nbsp;- </font></td>
          
          <td class="L"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLA[k].getDataFine(),"dd-MM-yyyy")).length()>0 ?
            StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLA[k].getDataFine(), "dd-MM-yyyy")): "-"%></font></td>
          </tr> 
<%
      } // end for  
    } // chiude if(totperLA > 0)
    else
    { %>
      <td class="l" ><font class="label">NO periodi</font></td>
<%    } %>
    <br>    
<%  } // chiude if(totggLA > 0) %>

<!--    GESTIONE LIBERAZIONE ANTICIPATA  SPECIALE -->
<%  if(totggLS > 0) 
  { %>  
    <tr>
      <td class="l">Giorni di Liberazione Anticipata Speciale</td>
      <td class="L">
          <font class="campo"><%=totggLS %></font>
    </td>     
<%
    if(totperLS > 0)
    {  
        for (int k=0;k<lperiodiLS.length;k++)
        {
          if (k>0)
          {   %>
          <td></td>     
            <td></td>  
<%        } %>
        <td class="l" width ="10%"><font class="label"><%=k+1%>)</font></td>
          <td class="L"><font class="campo">
             <%=StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLS[k].getDataInizio(),"dd-MM-yyyy")).length()>0 ?
             StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLS[k].getDataInizio(), "dd-MM-yyyy")): "-"%></font>&nbsp;
          <td class="l"><font class="label">&nbsp;-</font></td>
          <td class="L"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLS[k].getDataFine(),"dd-MM-yyyy")).length()>0 ?
            StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLS[k].getDataFine(), "dd-MM-yyyy")): "-"%></font></td>
         </tr>  
<%      } // end for  
    } // chiude if(totperLS > 0)  
    else
    { %>
      <td class="l" ><font class="label">NO Periodi</font></td>
<%    } %>
    <br>
<%  } // chiude if(totggLS > 0) %>

<!--    GESTIONE  INTEGRAZIONE LIBERAZIONE ANTICIPATA   -->
<%  if(totggLI > 0) 
  { %>  
    <tr>
      <td class="l">Giorni di Integrazione Liberazione Anticipata </td>
      <td class="L">
          <font class="campo"><%=totggLI %></font>
    </td>     
<%
    if(totperLI > 0)
    {  
        for (int k=0;k<lperiodiLI.length;k++)
        {
          if (k>0)
          {   %>
          <td></td>     
            <td></td>  
<%        } %>
        <td class="l" width ="10%"><font class="label"><%=k+1%>)</font></td>
          <td class="L"><font class="campo">
             <%=StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLI[k].getDataInizio(),"dd-MM-yyyy")).length()>0 ?
             StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLI[k].getDataInizio(), "dd-MM-yyyy")): "-"%></font>&nbsp;
          <td class="l"><font class="label">&nbsp;-</font></td>
          <td class="L"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLI[k].getDataFine(),"dd-MM-yyyy")).length()>0 ?
            StringUtils.toStringJSP(DateUtils.getDateToString(lperiodiLI[k].getDataFine(), "dd-MM-yyyy")): "-"%></font></td>
        </tr>       
<%      } // end for
    } // chiude if(totperLI > 0)
    else
    { %>
      <td class="l" ><font class="label">NO Periodi</font></td> 
<%    }
  } // chiude if(totggLI > 0) 
%>

</table>

<%
//==============================================================================
// Sezione contenente la Pena Ricalcolata (se presente e associata al provvedimento)
// e l'eventuale fungibilità
//==============================================================================
%>
<% 
if (penaresidua != null && penaresidua.getIdPenaResidua() != null ) { %>    
    <table>
      <tr>
        <td class="Titolo"  colspan=10> Pena Rideterminata </td>
      </tr>
      
      <tr>
        <td class="l"><font class="label">Reclusione / Multa : </font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione())%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione())%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione())%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoMulta()))%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto())%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto())%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto())%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoAmmenda()))%></font></td>
      </tr>
    </table>
  
    <table>
      <% if(penaresidua.getDataInizio() != null) {%>
      <tr>
        <td class="l"><font class="label">Data Decorrenza Pena : </font></td>
        <td class="l">
          <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
          </font>
        </td>
      <% 
      }
    
      if(penaresidua.getDataFineReclusione() != null) {%>
        <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
        <td class="l">
          <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
      <%
      }
   
      if(penaresidua.getDataInizioArresto() != null) {%>
      <tr>
        <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
        <td class="l">
          <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
          </font>
        </td>
      <%
      }
    
      if(penaresidua.getDataFine() != null || penaresidua.getDataFinePresunta()!= null) {%>
        <td class="l"><font  class="label">Data Fine Pena : </font></td>
        <td class="l">
          <font class="campo">
          <%if(penaresidua.getDataFine() != null){%>
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"),"-")%>
          <%} else if(penaresidua.getDataFinePresunta()!= null){%>
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"),"-")%>
          <%}%>
          </font>
        </td>
      </tr>
      <% } %>
    </table>
    
    <%
      // 01/06/2016 aggiunta visualizzazione delle Fungibilità ricalcolata
      if (fungibilita!=null && fungibilita.getIdFungibilita()!=null) {
    %>
    <table>
      <tr>
        <td class="l" width="200px">Pena espiata in eccesso</td>
        <td class="l"><font class="label">Anni   </font><font color="red"> <%=StringUtils.toStringJSP(fungibilita.getNumAnni(),"0")%></font></td>
        <td class="l"><font class="label">Mesi   </font><font color="red"> <%=StringUtils.toStringJSP(fungibilita.getNumMesi(),"0")%></font></td>
        <td class="l"><font class="label">Giorni </font><font color="red"> <%=StringUtils.toStringJSP(fungibilita.getNumGiorni(),"0")%></font></td>
      </tr> 
    </table>  
    <% } %> 
<% }  // end if penaresidua != null  %>




<br>
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="EveIdEvento" value="<%=evento.getIdEvento()%>">
  
  <table width="800px">
      <% 
      if (   (penaresidua==null || penaresidua.getIdPenaResidua() == null)                // non ho ancora effettuato il calcolo pena 
          && (penaresiduavalidata != null && penaresiduavalidata.getDataInizio() != null) // ho la pena in decorrenza
          && !isErgastolo                                                                 // non sono in ergastolo
         )
      { 
        Date oggi = DateUtils.getSysDateAsDate("dd/MM/yyyy");
        Date dataFinePena = penaresiduavalidata.getDataFine();
      %>

      <% if (dataFinePena!=null && !DateUtils.isGreater(dataFinePena, oggi) ){%>
      <tr>
        <td class="L" colspan="2">
          <font color="red">
            Attenzione. La pena a sistema risulta interamente espiata al <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataFinePena,"dd-MM-yyyy"))%>. I calcoli verranno effettuati considerando tale data come data di effettivo fine pena se non diversamente indicato.
           </font>
        </td>
      </tr>
      <% } %>

      <tr>
        <td class="L" width="150px">
          <INPUT class="bottone" type="button" name="CALCOLO" value="Calcolo Pena" onClick="javascript:CalcoloPena();">
        </td>
        <% if (dataFinePena!=null && !DateUtils.isGreater(dataFinePena, oggi) ) { %>
          <td class="L">
            Data eventuale Scarcerazione
            <input type="text" maxlength="2" size="2" 
                   name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" 
                   value="<%=DateUtils.getDateToString(dataFinePena,"dd")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
                   >
            <input type="text" maxlength="2" size="2" 
                   name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>" 
                   value="<%=DateUtils.getDateToString(dataFinePena,"MM")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
                   >
            <input type="text" maxlength="4" size="4" 
                   name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>" 
                   value="<%=DateUtils.getDateToString(dataFinePena,"yyyy")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" 
                   >
          </td>
        <% } %>
      </tr>
      <% } %>


      <% // E' possibile procedere alla produzione del provvedimento correlato 
         // o alla validazione solo dopo aver effettuato il calcolo della pena
      if (  penaresidua!=null  && penaresidua.getIdPenaResidua() != null 
            && !penaresiduavalidata.isErgastolo()
         )  
      { 
      %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="STAMPA" value="Stampe" onClick="javascript:caricaStampe('<%=evento.getIdEvento()%>');">
      </td>
    </tr>
      <% } %>
  </table>
</form>
<br>

</body>
</html>