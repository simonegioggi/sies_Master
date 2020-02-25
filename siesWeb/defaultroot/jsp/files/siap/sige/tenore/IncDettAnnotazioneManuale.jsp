<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.html.Option"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>

<%// Pena residua corrente%>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<%// Reati%>
<jsp:useBean id="reati"         scope="request" class="java.util.Vector" /> <% // Reati sul fascicolo %>
<jsp:useBean id="ReaAntEffetti" scope="request" class="java.util.Vector" /> <% // Richieste con anticipazione %>
<jsp:useBean id="ReaRichiesti"  scope="request" class="java.util.Vector" /> <% // Richieste senza anticipazione %>
<jsp:useBean id="RichiesteAlGE" scope="request" class="java.util.Vector" />

<%// bean valorizzati se provengo da 'aggiungi' ed è presente già a sistema una annotazione %>
<jsp:useBean id="OrdinanzaGEAnn"   scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="FlagIndulto" scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="AnnotazioneManuale" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>


<%// bean utilizzati nelle sezioni commentate 

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

%>

<%
//==============================================================================
//          Form di inserimento dei dati della decisione del GE
// - include DettaglioSoggettoSentenza.jsp
// - Posizione Giuridica
// - Sezione con le annotazioni manuali relative a Richieste con Anticipazione degli effetti (non validate)
// - Sezione con le annotazioni manuali relative a Richieste senza Anticipazione (non validate)
// - Sezione contenente tutti i reati collegati al fascicolo (titoli di reato)
// - Sezione con la pena complessiva (se ergastolo) o PENA RESIDUA
// - SEZIONE CON I CAMPI DI IMPUT
//
// n.b. la pena residua visualizzata
//==============================================================================
	
	
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)FascicoloSigeEsteso.getFascicoloSiep();
  
  // Valore di default del Flag Conforme (-, C, D)
  String lFlagConf = "-";
  if( AnnotazioneManuale != null && AnnotazioneManuale.getFlagConforme().length() > 0)
	  lFlagConf = AnnotazioneManuale.getFlagConforme();

  // Valore di default del Flag + o - 
  String lFlagPiuMeno = "";
  if( AnnotazioneManuale != null && AnnotazioneManuale.getFlagPiuMeno() != null && AnnotazioneManuale.getFlagPiuMeno().length() > 0)
	  lFlagPiuMeno = AnnotazioneManuale.getFlagPiuMeno();
  
%>

<%
//==============================================================================
// Tabella con la lista delle richieste con e senza anticipazione già validate
// n.b. nasce con i record nascosti che vengono abilitati da Seleziona Richieste
//      simulando il caricamento dinamico in maschera
//==============================================================================
if( lFascicoloAssociato != null && RichiesteAlGE != null && RichiesteAlGE.size() > 0) {
%>
<input type="hidden" name="NumTotRichieste" value="<%=RichiesteAlGE.size()%>">
<table style="width: 95%;" id="tabella_richieste">
  <tr><td colspan="5" class="Titolonocap">Richieste del PM al Giudice dell'Esecuzione </td></tr>

<%
int id_record = 0;
%>  
  <tr style="display:none;" id="titolo_richieste">
    <td class="c">Tipo</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
    <td class="c">Anticipazione</td>
    <td class="c">&nbsp;</td>
  </tr>
<%
  Iterator itx = RichiesteAlGE.iterator();
  for (int i = 0; itx.hasNext(); i++)
  {
    id_record++;
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)itx.next();
    String tipoRichiesta = "";
    if (   lAnnMod.getFlagAppProvvisoria()!=null 
           && lAnnMod.getFlagAppProvvisoria().equals("A")
       )
    {
      tipoRichiesta = "A";
    }
    else
    {
      tipoRichiesta = "R";
    }
%>
  <tr style="display:none;" id="record_<%=id_record%>"
<%--   	   tipoRich="<%=tipoRichiesta%>" --%>
<%--       segno="<%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno(),"")%>" --%>
<%--       aaRec="<%=StringUtils.toStringJSP (lAnnMod.getNumAnniReclusione(),"0")%>" --%>
<%--       mmRec="<%=StringUtils.toStringJSP (lAnnMod.getNumMesiReclusione(),"0")%>" --%>
<%--       ggRec="<%=StringUtils.toStringJSP (lAnnMod.getNumGiorniReclusione(),"0")%>" --%>
<%--       multa ="<%=StringUtils.toStringJSP(lAnnMod.getImportoMulta(),"0.00")%>"  --%>
<%--       aaArr="<%=StringUtils.toStringJSP (lAnnMod.getNumAnniArresto(),"0")%>" --%>
<%--       mmArr="<%=StringUtils.toStringJSP (lAnnMod.getNumMesiArresto(),"0")%>" --%>
<%--       ggArr="<%=StringUtils.toStringJSP (lAnnMod.getNumGiorniArresto(),"0")%>" --%>
<%--       ammenda ="<%=StringUtils.toStringJSP(lAnnMod.getImportoAmmenda(),"0.00")%>"  --%>
      >
      
    <td class="l" style="text-align:center">
      <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno(),"")%></font>
    </td>
    <td class="l">
       Anni   <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumAnniReclusione(),"0")%></font>
       Mesi   <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumMesiReclusione(),"0")%></font>
       Giorni <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumGiorniReclusione(),"0")%></font>
       Multa  <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoMulta())%></font>
    </td>
    <td class="l">
       Anni    <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumAnniArresto(),"0")%></font>
       Mesi    <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumMesiArresto(),"0")%></font>
       Giorni  <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumGiorniArresto(),"0")%></font>
       Ammenda <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda())%></font>
    </td>
    <% if ( tipoRichiesta.equals("A")) { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else {%>
    <td class="l">&nbsp;</td>
    <% }%>
    <td class="l">
      <a href="Javascript:rimuovi('record_<%=id_record%>');">
        <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0" title="Elimina dalla lista">
      </a>
    </td>
    <td class="l" style="display:none;">
      <input type="checkbox" name="cb_record_<%=id_record%>" id="cb_record_<%=id_record%>" value="<%=lAnnMod.getIdAnnotazioneManuale()%>" >
    </td>
  </tr>
<% } %>
</table>
<%
} // endif( lFascicoloAssociato != null 

%>

<%
//==============================================================================
// Sezione contenente l'elenco delle richieste con anticipazione degli effetti.
// Quali, quelle non ancora validate
// Per ogni annotazione vengono visualizzati i seguenti dati:
// - reato o Pena Complessiva
// - quantum reclusione richiesto
// - quantum arresto    richiesto
//==============================================================================
if (ReaAntEffetti.size()!=0)
{
%>
<table style="width: 95%;">
  <tr><td colspan=3 class="Titolo">Richiesta Anticipazioni degli effetti</td></tr>
  <tr>
    <td class="c">Applicata a</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
  </tr>
<%
  AnnotazioneManualeModel tmp = null;
  ReatoModel rtmp = new ReatoModel();
  BigDecimal idRea;

  for (int i=0; i<ReaAntEffetti.size(); i++)
  {
      out.println("<tr>");

      tmp=(AnnotazioneManualeModel)ReaAntEffetti.get(i);

      idRea=tmp.getReaIdReato();
      boolean found=false;
      for (int itR=0;itR<reati.size();itR++)
      {
        rtmp=(ReatoModel)reati.get(itR);
        if (rtmp.getIdReato().equals(idRea))
        {
          found=true;
          itR=reati.size()+1;
        }
      }

      out.println("<td class=\"l\">");

      if (found) // esiste reato associato all'annotazione
      {
        boolean lFlagAnnoNumero;
        lFlagAnnoNumero = false;
        if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
          lFlagAnnoNumero = true;
%>
        <font class="label">
<%
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
%>
        </font>
        <font class="campo">
<%
        if(lFlagAnnoNumero)
        {
          if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
            out.println(rtmp.getDescrFonte()+" ");
          if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
            out.println(rtmp.getAnnoFonte());
          if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
            out.println("/"+rtmp.getNumeroFonte());
        }

        if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
          out.println("art."+rtmp.getArticolo());
        if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
          out.println(" "+rtmp.getDescrSottonumerazione());

        if(!lFlagAnnoNumero)
        {
          if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
            out.println(rtmp.getDescrFonte());
        }

        if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
          out.println(" c. "+rtmp.getComma());
        if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
          out.println(" l. "+rtmp.getLettera());
        if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
          out.println(" n. "+rtmp.getNumero());%></font>
<%
      }
      else
      { // non è stato associato un reato un fase di richiesta
        out.println("<font class=\"campo\">Pena Complessiva</font>");
      }
      out.println("</td>");
%>
      <td class="l">
         Anni   <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
         Mesi   <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
         Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
         Multa  <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
      </td>
      <td class="l">
         Anni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
         Mesi <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
         Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
         Ammenda <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
      </td>
<%
    out.println("</tr>");
   }
%>
   </table>
<%
  }  // fine elenco richieste con anticipazione


//==============================================================================
// Sezione con le annotazioni manuali relative a Richieste senza Anticipazione
// Per ogni annotazione vengono visualizzati i seguenti dati:
// - reato o Pena Complessiva
// - quantum reclusione richiesto
// - quantum arresto    richiesto
//==============================================================================
if (ReaRichiesti.size()!=0)
{
%>
<table style="width: 95%;">
  <tr><td colspan=3 class="Titolo">Richiesta</td></tr>
  <tr>
    <td class="c">Applicata a</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
  </tr>
<%
  AnnotazioneManualeModel tmp = null;
  ReatoModel rtmp = new ReatoModel();
  BigDecimal idRea;

  for (int i=0; i<ReaRichiesti.size(); i++)
  {
    out.println("<tr>");
    tmp=(AnnotazioneManualeModel)ReaRichiesti.get(i);
    idRea=tmp.getReaIdReato();
    boolean found=false;
    for (int itR=0;itR<reati.size();itR++)
    {
      rtmp=(ReatoModel)reati.get(itR);
      if (rtmp.getIdReato().equals(idRea))
      {
       found=true;
       itR=reati.size()+1;
      }
    }

    out.println("<td class=\"l\">");

    if (found)
    {
      boolean lFlagAnnoNumero;
      lFlagAnnoNumero = false;
      if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
        lFlagAnnoNumero = true;
%>
        <font class="label">
<%
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
%>
      </font>
      <font class="campo">
<%
          if(lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte()+" ");
            if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
              out.println(rtmp.getAnnoFonte());
            if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
              out.println("/"+rtmp.getNumeroFonte());
          }

          if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
            out.println("art."+rtmp.getArticolo());
          if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
            out.println(" "+rtmp.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte());
          }

          if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
            out.println(" c. "+rtmp.getComma());
          if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
            out.println(" l. "+rtmp.getLettera());
          if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
            out.println(" n. "+rtmp.getNumero());%></font>
<%
      }
      else
      {
        out.println("<font class=\"campo\">Pena Complessiva</font>");
      }

      out.println("</td>");
%>
        <td class="l">
           Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
           Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
           Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
           Multa <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
        </td>
        <td class="l">
           Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
           Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
           Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
           Ammenda <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
        </td>
<%
    out.println("</tr>");
   }
%>
   </table>
<%
}

//==============================================================================
//       Sezione contenente tutti i reati collegati al fascicolo
//==============================================================================
if (!reati.isEmpty())
{
%>
<table style="width: 95%;">
  <tr><td colspan=7 class="Titolonocap">Titoli di reato</td></tr>
  <tr>
    <td class="c">Reato</td>
    <td class="c">Durata</td>
    <td class="c">Sanzione</td>
    <td class="c">Sel.</td>
    <td class="c">Ann.Inserita</td>
  </tr>
<%
  ReatoModel lReato;
  boolean lFlagAnnoNumero;
  for (int i=0;i<reati.size();i++)
  {
    lReato=(ReatoModel)reati.get(i);
    lFlagAnnoNumero = false;
    
    if(  lReato.getAnnoFonte() != null
       && !lReato.getAnnoFonte().equals("")
       && lReato.getNumeroFonte() != null
       && !lReato.getNumeroFonte().equals("") )
    {
      lFlagAnnoNumero = true;
    }
%>
  <tr>
    <td class="l">
      <font class="label">
      <% if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals("")) { %>
        n.<%=lReato.getProgrNumeroManuale()%>: 
      <% } else { %>
        n.<%=lReato.getProgrReato()%>:
      <% } %>
      </font>
      <font class="L">
<%
        if(lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte()+" ");
          if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
            out.println(lReato.getAnnoFonte());
          if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
            out.println("/"+lReato.getNumeroFonte());
        }

        if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
          out.println("art."+lReato.getArticolo());
        if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
          out.println(" "+lReato.getDescrSottonumerazione());

        if(!lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte());
        }

        if(lReato.getComma() != null && !lReato.getComma().equals(""))
          out.println(" c. "+lReato.getComma());
        if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
          out.println(" l. "+lReato.getLettera());
        if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
          out.println(" n. "+lReato.getNumero());%>
      </font>
      
      <% if(lReato.getStringaConsumazione()!= null) { %>
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
      <% } %>
    
      <%if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
      <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
      <% } %>

      <% if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
      <font class="label">Luogo</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
      <% } %>
    </td>

    <td class="l">
      <table>
        <tr>
          <td class="lnobord"><font class="label">AA</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumAnni(),"0")%></font></td>
          <td class="lnobord"><font class="label">MM</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumMesi(),"0")%></font></td>
          <td class="lnobord"><font class="label">GG</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumGiorni(),"0")%></font></td>
        </tr>
      </table>
    </td>
    <td class="r">
      <font class="campo">
        <%= StringUtils.toEuroFormat(lReato.getSanzionePecuniaria())%>
      </font>
      €
      <% if (lReato.getSanzionePecuniaria() != null && lReato.getSanzionePecuniaria().compareTo(new BigDecimal(0)) != 0) { %>
        di
        <font class="campo">
          <%= StringUtils.toStringJSP(lReato.getDescrTipoSanzione())%>
        </font>&nbsp;
      <% } %>
    </td>
    <td class="c"><input type="radio" name="IdReato" value="<%= lReato.getIdReato() %>"></td>
    <% if(lReato.getFlagVisto()!= null && lReato.getFlagVisto().equals("S")) { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else { %>
    <td class="C"> &nbsp;</td>
    <% } %>
    </tr>
<%
		}
%>
    </table>
<%
  }
%>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: solo data inizio e data fine MAI
// - Se Libero vengono visualizzati i Quantum 
//   - PenRes1 = Reclusione
//   - PenRes2 = Arresti
// - Se detenuto viene visualizzato il quantum residuo calcolato al volo tra la 
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
// se la Pena Complessiva è un ergastolo o ergastolo con isolamento

boolean flagPenacomplessiva = false;

if(flagPenacomplessiva)
{
if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
   && PenaComplessiva.getCodTipoPenaDetentiva() != ""
   && ( PenaComplessiva.getCodTipoPenaDetentiva().equals("03") || PenaComplessiva.getCodTipoPenaDetentiva().equals("04") )
    )
{
%>
<table style="width: 95%;">
  <tr>
    <td colspan=3 class="Titolonocap">Pena complessiva</td>
  </tr>
  <tr>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
      </font>
    </td>
    <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
    <td class="l">Data Fine : <font class="campo">MAI</font></td>
  </tr>
</table>
<%
}
else if (!PenRes1.getErrorMsg().equals("-"))
{
%>
<table style="width: 95%;">
  <tr>
  <% if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) { %>
    <td class=Titolonocap colspan=6 width=80%> Pena complessiva </td>
  <% } else { %>
    <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
  <% } %>
  </tr>
  
  <%
  if (PenRes1.getErrorMsg().startsWith("Libero")) //Patch per gestire il titolo
  {
%>
  <tr>
    <td class="l"> Reclusione :
      Anni <font class=campo><%=PenRes1.getNumAnni()%></font>
      Mesi <font class=campo><%=PenRes1.getNumMesi()%></font>
      Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>
      Multa <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
    </td>
    <td class="l"> Arresto :
      Anni <font class=campo><%=PenRes2.getNumAnni()%></font>
      Mesi <font class=campo><%=PenRes2.getNumMesi()%></font>
      Giorni <font class=campo><%=PenRes2.getNumGiorni()%></font>
      Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
    </td>
  </tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
  }
  else //non libero
  { // pena in espiazione ??? calcolo al volo la pena residua come intervallo
    // tra date
    //==========================================================================
    // Solo se la pena è in espiazione calcolo il quantum residuo, altrimenti
    // visualizzo solo decorrenza-scadenza.
    // n.b. la pena non viene considerata in espiazione se:
    //      1 - data inizio >= sysdate (pene con decorrenza futura)
    //      2 - data fine < sysdate (in questo caso il condannato viene considerato scarcerato)
    //     data inizio <= sysdate <data fine
    %>
    <tr>
      <td class="l"> Reclusione :
         Anni <font class="campo"><%=PenRes1.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes1.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
         Multa <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
         Anni <font class="campo"><%=PenRes2.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes2.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes2.getNumGiorni()%></font>
         Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(), "dd/MM/yyyy"))%></font></td>
      <td class="l">Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(), "dd/MM/yyyy"))%></font></td>
    </tr>
  
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
  }
%>
</table>
<%
  }
}
%>

<%
//====================================================================================
// SEZIONE CON I CAMPI DI IMPUT DEI DATI DELLA DECISIONE DEL GIUDICE DELL' ESECUZIONE
// se provengo da 'aggiungi' i campi vengono precaricati con i dati precedentemente
// inseriti non modificabili
//====================================================================================
%>
<table style="width: 95%;">
	<tr><td colspan=8 class="Titolonocap"> Quantum </td></tr>
	<tr>
    <td class="l" nowrap>Tipo Beneficio: </td>
		<td class="l"></td>    
		<td class="L">
      <font class="campo">
        <%=AnnotazioneManuale.getDescrTipoAnnotazione()%>
      </font>
		</td>
		<td class="l" nowrap>DPR: <td class="L">
      <font class="campo">
        <%=AnnotazioneManuale.getDescrDpr()%>
      </font>
		</td>
	</tr>
	</table>
	<table>
  <tr>
    <td class="l">      <font class="campo">
        <% if (lFlagConf.equalsIgnoreCase("-")){ %>
        	senza richiesta
       <%}else if (lFlagConf.equalsIgnoreCase("C")){ %>
       in conformita' alla richiesta del PM
        <%}else if (lFlagConf.equalsIgnoreCase("D")){ %>
       in difformita' alla richiesta del PM
       <%} %>
       </font>
    </td>
  </tr>
 </table>

<%
//==============================================================================
//                    Sezione con i quantum da imputare
//==============================================================================
%>
<table style="width: 95%;">
  <tr><td colspan=6><hr width="100%"></td></tr>
  <tr>
    <td valign="middle" class=c rowspan=3>+/-<br>
     <font class="campo">
     <%=lFlagPiuMeno%>
     </font>
  </td>
  <td class=titolo colspan=2>Reclusione</td>
  <td width=25>&nbsp;</td>
  <td class="titolo" colspan=2>Arresto</td>
  </tr>
  <tr>
			<td class="c">
			<table>
			<tr>
				<td><font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><font class="label">Giorni</font></td>
			</tr>
			<tr>
				<td><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManuale.getNumAnniReclusione())%></font></td>
				<td><font class="campo">	<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumMesiReclusione())%></font></td>
				<td><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManuale.getNumGiorniReclusione())%></font></td>
			</tr>
			</table>
			</td>
			<td class="c">
				<font class="label">Multa</font><br>
				<font class="campo">
				<%=StringUtils.toStringJSP(AnnotazioneManuale.getImportoMulta())%>
				</font>
			</td>
			<td width=25>&nbsp;</td>
			<td class="c">
			<table>
			<tr>
				<td><font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><font class="label">Giorni</font></td>
			</tr>
			<tr>
				<td><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManuale.getNumAnniArresto())%></font></td>
				<td><font class="campo">	<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumMesiArresto())%></font></td>
				<td><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManuale.getNumGiorniArresto())%></font></td>
			</tr>
			</table>			
			</td>
      <td class="c">
				<font class="label">Ammenda</font><br>
				<font class="campo">
				<%=StringUtils.toStringJSP(AnnotazioneManuale.getImportoAmmenda())%>
				</font>
			</td>
	</tr>
 	</table>
 	
</html>