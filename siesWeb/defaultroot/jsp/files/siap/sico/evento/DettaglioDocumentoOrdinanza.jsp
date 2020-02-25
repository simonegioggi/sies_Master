<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.camponota.model.CampoNotaModel"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel"%>

<jsp:useBean id="eventonotifica"   scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"   scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="listaAutorita" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaIstituti" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaUffici" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaCssa" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaAvvSiep" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="listaAvvSius" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="misuraalternativa" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>
<jsp:useBean id="DepositoOrdinanzaPc" scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="decretoordinanza" scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="flagdecretoordinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnotazioneOrdinanza" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel" />
<jsp:useBean id="ListaAnnotazioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="lPageGE" scope="request" class="java.lang.String" />
<jsp:useBean id="archiviazione" scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel" />
<jsp:useBean id="tenori" scope="request" class="java.util.Vector" />



<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();

  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  String lCodicePosizione = lPosizione.getCodPosizioneGiuridica();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
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
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio <%=eventonotifica.getEvento().getDescrMotivo()%></font>
      </td>
    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table >
<%
    if(  lCodicePosizione != null
      && !lCodicePosizione.equals("")
      && !lCodicePosizione.equals("-"))
    {
%>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
<%
            if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
            {
%>
              DETENUTO PER ALTRA CAUSA
<%
            }
            else
            {
%>
             <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
           }
%>
          </font>
        </td>
      </tr>
<%
      if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
         if( lAltraCausa.getIstitutoDetenzione()!= null )
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
               //}
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
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             // if(lLuogoDetenzione.getDescrLuogo()!=null)
             // {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
             // }
%>
            </td>
          </tr>
<%
        }

        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
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
      }

      if (penaresidua.getDataInizio() != null)
      {
%>
        <tr>
          <td class="l">Data Decorrenza Pena</td>
          <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
        </tr>
<%
      }

      if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        && penaresidua.getDataFinePresunta() != null)
      {
%>
        <tr>
          <td class="l">Data Fine Pena Automatica</td>
          <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
        </tr>
<%
       }

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
      </tr>
      <tr>
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D") ))
            && penaresidua.getDataFine()!=null)
        {
%>
         <td class="l">Data Fine Pena Manuale</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%> &nbsp;</font></td>
<%
        }
%>
      </tr>
      <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
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
<%
          if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
%>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          }
        }
%>
     </tr>
     <tr>
<%
      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}else{
%>
        <td class="l">Arresto</td>
        <td class="l">
           <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
           <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
           <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
        </td>
<%
        if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
        {
%>
          <td class="l">Ammenda</td>
          <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
      }
    }
%>
    </tr>

<%
	// controllo se è Variazione Decorrenza Scadenza
	if (eventonotifica.getEvento().getCodMotivo().equals("0249"))
	{
	    // recupero eventuali motivazioni
	    CampoNotaModel[] lNote = eventonotifica.getCampoNote();
	    String motivazioni = "";
	    if (lNote != null && lNote.length > 0) {

	    	motivazioni = lNote[0].getDescr();
	    }
%>
       <tr>
          <td class="l">Data pervenimento richiesta variazione</td>
			<td class="L" colspan="1"><font class="campo"><%=DateUtils.getDateToString(eventonotifica.getEvento().getDataRicezioneAtti(), "dd-MM-yyyy")%></font></td>
       </tr>
       <tr>
          <td class="l">Motivazioni</td>
			<td class="L" colspan="1"><font class="campo"><%=motivazioni%></font></td>
       </tr>
<%
    }
%>
	<tr>
<%
      if(eventonotifica.getEvento().getDataEmissione()!= null)
      {
%>
          <td class="l">Data Emissione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>
          </td>
<%
        }

        // SE NON ESISTE NESSUNA NOTIFICA FALLISCE
        if(  eventonotifica.getNotifiche().length != 0
          && eventonotifica.getNotifiche()[0].getDataInvio()!= null)
        {
%>
          <td class="l">Data Trasmissione</td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%>
            </font>
          </td>
<%
        }
%>
    </tr>

<%
    if(archiviazione!= null && archiviazione.getIdArchiviazione() != null)
     {

      if(archiviazione.getCodTipoProvvedimento() != null && archiviazione.getCodTipoProvvedimento().equals("23"))
      {
       if(archiviazione.getCodProvvedimento()!= null)
        {
%>
         <tr>
          <td class="l">Provvedimento</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrProvvedimento())%></font>
          </td>
         </tr>
<%
        }


      if(archiviazione.getNumProvvedimento()!= null)
      {
%>
       <tr>
        <td class="l">Anno /Numero Provvedimento</td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(archiviazione.getAnnoProvvedimento())%> /</font>
        <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getNumProvvedimento())%></font>
        </td>
       </tr>
<%
      }

       if(archiviazione.getCodTipoProvvedimentoArc() != null)
        {
%>
         <tr>
           <td class="l">Tipo Provvedimento</td>
           <td class="L">
             <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrTipoProvvedimentoArc())%></font>
           </td>
         </tr>
<%
       }

       if(archiviazione.getCodTipoAutoritaEmittente() != null && !archiviazione.getCodTipoAutoritaEmittente().equals("-")
          && archiviazione.getCodLuogoEmittente() != null && !archiviazione.getCodLuogoEmittente().equals("-"))
        {
%>
        <tr>
         <td class="l">Autorità emittente</td>
         <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrTipoAutoritaEmittente())%></font>
          di <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrLuogoEmittente()) %></font>
         </td>
       </tr>

<%
       }
      }

      if(archiviazione.getCodTipoProvvedimento() != null && archiviazione.getCodTipoProvvedimento().equals("21"))
      {
       if(archiviazione.getAnnoNota()!= null)
      {
%>
       <tr>
        <td class="l">Anno /Numero Nota</td>
        <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(archiviazione.getAnnoNota())%> /</font>
        <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getNumNota())%></font>
        </td>
       </tr>
<%
      }

        if(archiviazione.getDataRicezione()!= null)
        {
%>
         <tr>
          <td class="l">Data Ricezione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataRicezione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
        }

       if(archiviazione.getIstitutoDetenzione() != null)
        {
%>
        <tr>
         <td class="l">Istituto Detenzione che ha inviato la nota</td>
         <td class="l">
          <font class="campo"> <%=StringUtils.toStringJSP(archiviazione.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
          <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getIstitutoDetenzione().getDescrComune())%></font>
        </td>
       </tr>
<%
        }else
        if(archiviazione.getCssIdCssa() != null)
        {
%>
        <tr>
         <td class="l">UEPE che ha inviato la nota</td>
         <td class="l">
          <font class="campo"> <%=StringUtils.toStringJSP(archiviazione.getCssa().getComune())%></font>&nbsp;-
          <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getCssa().getIndirizzo())%></font>
        </td>
       </tr>
<%
       }else
       if(archiviazione.getCodTipoAutoritaEmittente() != null && !archiviazione.getCodTipoAutoritaEmittente().equals("-")
          && archiviazione.getCodLuogoEmittente() != null && !archiviazione.getCodLuogoEmittente().equals("-"))
        {
%>
        <tr>
         <td class="l">Autorità che ha inviato la nota</td>
         <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrTipoAutoritaEmittente())%></font>
          di <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getDescrLuogoEmittente()) %></font>
         </td>
       </tr>

<%
       if(archiviazione.getIndirizzoEmittente() != null)
      {
%>
       <tr>
        <td class="l">Indirizzo</td>
        <td class="l">
         <font class="campo"><%=archiviazione.getIndirizzoEmittente()%>&nbsp;</font>
        <td>
       </tr>
<%
       }
      }

      if(archiviazione.getAltraAutorita() != null)
        {
%>
        <tr>
         <td class="l">Altra Autorità che ha inviato la nota</td>
         <td class="l">
          <font class="campo"> <%=StringUtils.toStringJSP(archiviazione.getAltraAutorita())%></font>
        </td>
       </tr>
<%
        }
     }

      if(archiviazione.getCodOggettoDefinizione()!= null)
      {
%>
       <tr>
          <td class="l">Oggetto Definizione</td>
        <td class="l">
          <font class="campo"> <%=StringUtils.toStringJSP(archiviazione.getDescrOggettoDefinizione())%></font>
        </td>
       </tr>
<%
       }
      if(archiviazione.getCodTipoProvvedimento() != null && archiviazione.getCodTipoProvvedimento().equals("22"))
      {
       if(lFascicoloAssociato.getDataUnione()!= null)
        {
%>
         <tr>
          <td class="l">Data Provvedimento di cumulo</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloAssociato.getDataUnione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
        }

        if(lFascicoloAssociato.getCodUfficioUnione()!= null && lFascicoloAssociato.getDescrComuneUfficioUnione() != null)
        {
%>
         <tr>
          <td class="l">Ufficio che ha emesso il cumulo</td>
          <td class="L">
            <font class="campo"> <%=StringUtils.toStringJSP(lFascicoloAssociato.getDescrTipoUfficioUnione())%></font>&nbsp;di
            <font class="campo"><%=StringUtils.toStringJSP(lFascicoloAssociato.getDescrComuneUfficioUnione())%></font>
          </td>
         </tr>
<%
        }


       if(lFascicoloAssociato.getNumFascicoloUnione() != null && lFascicoloAssociato.getAnnoFascicoloUnione() != null)
        {
%>
        <tr>
         <td class="l">Numero Procedimento SIEP</td>
         <td class="l">
          <font class="campo"> <%=StringUtils.toStringJSP(lFascicoloAssociato.getAnnoFascicoloUnione())%></font>/
          <font class="campo"><%=StringUtils.toStringJSP(lFascicoloAssociato.getNumFascicoloUnione())%></font>
        </td>
       </tr>
<%
        }
      }
       if(archiviazione.getDataDefinizione()!= null)
        {
%>
         <tr>
          <td class="l">Data Definizione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataDefinizione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
        }

       if(archiviazione.getNote()!= null)
        {
%>
         <tr>
          <td class="l">Note</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(archiviazione.getNote())%></font>
          </td>
         </tr>
<%
        }
     }
%>


<%if(misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa()!= null){%>
<%if(misuraalternativa.getChiaveAnnoFascicoloSius()!= null){%>

      <td class="l">Anno /Numero SIUS</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
     </td>
<%}%>
<%if(misuraalternativa.getAnnoRegistro()!= null){%>

     <td class="l"> Anno / Numero Ordinanza </td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
     <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
<%}%>
  </tr>
<%if(!sedeUfficioEmittente.getDescrTipoUfficio().equals("")){%>

 <tr>
    <td class="l">Ufficio Emittente </td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio())%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
 </tr>
<%}%>

 <tr>

<%if(misuraalternativa.getDescrTipoMisura()!= null && !misuraalternativa.getDescrTipoMisura().equals(""))
 {
  if(DepositoOrdinanzaPc != null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc() != null)
  {
%>
   <td class="l">Oggetto Ordinanza </td>
<%
  }else{
%>
   <td class="l">Oggetto Decreto </td>
<%
  }
%>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
<%}%>
<%if(misuraalternativa.getDataDecisione()!= null){
  if(DepositoOrdinanzaPc != null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc() != null)
  {
%>
   <td class="l">Data Emissione Ordinanza </td>
<%
  }else{
%>
   <td class="l">Data Emissione Decreto </td>
<%
  }
%>


   <td class="l"><font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
    </font>
   </td>
<%}%>
  </tr>
<%if(misuraalternativa.getDescrLuogoProva()!= null && !misuraalternativa.getDescrLuogoProva().equals("")){%>
  <tr>
      <td class="l">Luogo della Prova </td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>&nbsp;</font>
      </td>
   </tr>
<%}%>

<%if(misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione()!= null
    && !misuraalternativa.getCodTipoUfficioScarcerazione().equals("-") && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")){%>
<tr>
    <td class="l">Scarcerato in Data</td >
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd-MM-yyyy"))%></FONT></td >
</tr>
<%}else if(misuraalternativa != null && misuraalternativa.getDataScarcerazione() != null){%>
<tr>
    <td class="l">Da Scarcerare in Data</td >
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd-MM-yyyy"))%></FONT></td >
</tr>
<%}%>

<%}%>

<%if(DepositoOrdinanzaPc!= null  && DepositoOrdinanzaPc.getDataCameraConsiglio()!= null)
{%>
 <tr>
      <td class="L">
        Data emissione
      </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DepositoOrdinanzaPc.getDataCameraConsiglio(),"dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
    </tr>
<%}%>
<%if(DepositoOrdinanzaPc!= null  && DepositoOrdinanzaPc.getNumGiorniLibanticipata()!= null)
{%>
    <tr>
      <td class="L">
        Totale giorni concessi
      </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(DepositoOrdinanzaPc.getNumGiorniLibanticipata())%>&nbsp;
        </font>
      </td>
    </tr>
<%}%>
<%
    if(!LicenzePeriodi.isEmpty())
    {
      LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel)LicenzePeriodi.firstElement();
%>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="L">
          Anno / Numero SIUS
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getAnnoSius())%>&nbsp;
          </font>
          /
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getNumeroSius())%>&nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="L">
          Anno / Numero Ordinanza
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getAnnoOrdinanza())%>&nbsp;
          </font>
          /
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getNumeroOrdinanza())%>&nbsp;
          </font>
        </td>
      </tr>

      <tr>
      <td class="l">
       Autorità emittente
       </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getDescrUfficioEmittente())%>&nbsp;
          </font>
          di
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getDescrLuogoEmittente())%>&nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="L">
          Data Emissione
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lLicenzaPeriodiModel.getLicenza().getDataEmissioneOrdinanza(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
<%
    }
%>
<%if(flagdecretoordinanza.equals("S")){%>
<tr>
      <td class="l" width="30%">
        Data sospensione esecuzione
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataSospensioneEsecuzione(),"dd-MM-yyyy"))%>&nbsp;
          &nbsp;
<%
          if( decretoordinanza.getFlagScarcerareScarcerato()!=null
             && decretoordinanza.getFlagScarcerareScarcerato().equals("S")
             )
          {
            out.print("[Già scarcerato]");
          }
          else if(decretoordinanza.getFlagScarcerareScarcerato()!=null
                && decretoordinanza.getFlagScarcerareScarcerato().equals("D"))
          {
            out.print("[Da scarcerare]");
          }
%>
        </font>
      </td>
    </tr>

    <tr>
      <td class="l" >
        Autorità emittente
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Contenuto provvedimento
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrOggettoDecisione())%></font>&nbsp;
      </td>
    </tr>

  <tr>
    <td class="l">
      Motivazioni
    </td>
    <td class="l">
     <font class="campo"><%=StringUtils.toStringJSP( decretoordinanza.getMotivazioni() )%>&nbsp;</FONT>
    </td>
  </tr>

<%}%>



<%
  if(AnnotazioneOrdinanza.getAnnotazioneManuale() != null && AnnotazioneOrdinanza.getEvento() != null)
  {
    AnnotazioneManualeModel OrdinanzaGEAnn = AnnotazioneOrdinanza.getAnnotazioneManuale();
    EventoModel OrdinanzaGEEve = AnnotazioneOrdinanza.getEvento();
%>
      <tr>
        <td class="l">Declaratoria :</td>
        <td class="l">
          Anno/Numero
          <font class="campo">
            <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getAnnoGe() )%>/<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>
          </font>
        </td>
        <td class="l">
          <font class="label">in data </font>
          &nbsp;&nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio :</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrUfficioEmittente())%>&nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="l">Sede :</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrLuogoEmittente())%>&nbsp;
          </font>
        </td>
      </tr>

<%
  }  if(!ListaAnnotazioni.isEmpty())
  {
%>
    <br>
      <tr><td colspan=9 class="Titolonocap">Periodi</td></tr>
<%
    for (Iterator lIter = ListaAnnotazioni.iterator(); lIter.hasNext(); )
    {
      AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel)lIter.next();

      if(lPageGE.equals("AMNI"))
      {
%>
        <tr>
          <td class="l">Computo beneficio :</td>
          <td class="l" >
            <font class="campo">
              <%=StringUtils.toStringJSP( lAnnMan.getDescrTipoAnnotazione(),"-")%>
            </font>
          </td>
          <td class="l">DPR :</td>
          <td class="l" >
            <font class="campo">
              <%=StringUtils.toStringJSP( lAnnMan.getDescrDpr(),"-")%>
            </font>
          </td>
        </tr>
<%
    }

    if(lPageGE.equals("INCOST"))
    {
%>
      <tr>
        <td class="l" width="45%">Sentenza Corte Costituzionale</td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
<%
      if(lAnnMan.getAnnoCc() == null || lAnnMan.getAnnoCc().compareTo(new BigDecimal(0))==0)
      {
%>
        -
<%
      }
      else
      {
%>
        <%=StringUtils.toStringJSP(lAnnMan.getAnnoCc() )%>
<%
      }
%>
        / <%=StringUtils.toStringJSP(lAnnMan.getNumeroCc(), "-")%>
        </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataCC(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
<%
    }

    if(lPageGE.equals("DEPEN"))
    {
%>
      <tr>
        <td class="l" width="20%">Fonte </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getDescrFonte(),"-")%>&nbsp;
          </font>
        </td>
        <td class="l">
        Anno
     <%if(lAnnMan.getAnnoFonte() == null || lAnnMan.getAnnoFonte().compareTo(new BigDecimal(0))==0){%>
            -
     <%}else{%>
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getAnnoFonte())%></font>
     <%}%>


          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumeroFonte(),"-")%></font>
          Art.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getArticolo(),"-")%></font>
        </td>
      </tr>
  <tr>
        <td class="l" width="20%">Art. Qualificante </td>
         <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getDescrSottonumerazione(),"-")%>&nbsp;</font>
        </td>
        <td class="l">
          Comma
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getComma(),"-")%></font>
          Let.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getLettera(),"-")%></font>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumero(),"-")%></font>
        </td>
      </tr>
<%}%>
      <tr>
<%if(lPageGE.equals("DEPEN")){%>

        <td class="l" colspan="2">Reclusione :
<%}else{%>
        <td class="l">Reclusione :
<%}%>

          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniReclusione(),"0")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiReclusione(),"0")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniReclusione(),"0")%></font>
        </td>
        <td class="l">Multa :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoMulta(),"0")%>&nbsp;
          </font>
        </td>
    <%if(lAnnMan != null && lAnnMan.getDataReclusioneDa() != null && lAnnMan.getDataReclusioneA() != null)
      {%>
      </tr>
      <tr>
        <td class="l"><font  class="label">Data Reclusione Da: </font></td>
        <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneDa(),"dd-MM-yyyy"))%>
         </font></td>
         <td class="l"><font  class="label">Data Reclusione A: </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneA(),"dd-MM-yyyy"))%>
          </font></td>
     <%}%>
      </tr>
      <tr>
<%if(lPageGE.equals("DEPEN")){%>

        <td class="l" colspan="2">Arresto :
<%}else{%>
        <td class="l">Arresto :
<%}%>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniArresto(),"0")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiArresto(),"0")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniArresto(),"0")%></font>
        </td>
        <td class="l">Ammenda :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoAmmenda(),"0")%>&nbsp;
          </font>
        </td>

    <%if(lAnnMan != null && lAnnMan.getDataArrestoDa() != null && lAnnMan.getDataArrestoA() != null)
      {%>
       </tr>
       <tr>
        <td class="l"><font  class="label">Data Arresto Da: </font></td>
        <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataArrestoDa(),"dd-MM-yyyy"))%>
         </font></td>
         <td class="l"><font  class="label">Data Arresto A: </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataArrestoA(),"dd-MM-yyyy"))%>
          </font></td>
    <%}%>
      </tr>

<%
    }
%>

<%
  }
%>

<%
  // MAGISTRATO
  if(magistrato.getCodMagistrato() != null
    && !magistrato.getCodMagistrato().equals("")
    && !magistrato.getCodMagistrato().equals("-"))
  {
%>
    <tr>
      <td class="l">Magistrato Assegnatario
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
        <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
      </td>
    </tr>
<%
  }

  // Autorita Esterne
  for(int i=0; i<listaAutorita.size(); i++)
  {
    NotificaModel lAutorita = (NotificaModel)listaAutorita.get(i);
    if(lAutorita.getAutoritaEsterna() == null)
    {
      lAutorita.setAutoritaEsterna( new AutoritaEsternaModel() );
    }
%>
    <tr>
      <td class="l">Autorita Notifica</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP( lAutorita.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lAutorita.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lAutorita.getNote())%></font>&nbsp;
      </td>
    </tr>
<%
  }

  // UEPE
  for(int i=0; i<listaCssa.size(); i++)
  {
    NotificaModel lCssa = (NotificaModel)listaCssa.get(i);
    if(lCssa.getCSSA() == null)
    {
      lCssa.setCSSA( new CSSAModel() );
    }
%>
    <tr>
      <td class="l">UEPE Competente</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lCssa.getCSSA().getComune())%>-<%=StringUtils.toStringJSP(lCssa.getCSSA().getIndirizzo())%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td  class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lCssa.getNote())%>&nbsp;</font>
      </td>
    </tr>
<%
  }

  // Uffici
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
        <font class="campo"><%=StringUtils.toStringJSP( lUfficio.getUfficio().getDescrTipoUfficio())%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td><td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lUfficio.getUfficio().getDescrComune())%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Note</td>
      <td  class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lUfficio.getNote())%>&nbsp;</font>
      </td>
    </tr>
<%
  }

  // Istituto
  for(int i=0; i<listaIstituti.size(); i++)
  {
    NotificaModel lIstituto = (NotificaModel)listaIstituti.get(i);
    if(lIstituto.getIstitutoDetenzione() == null)
    {
      lIstituto.setIstitutoDetenzione( new IstitutoDetenzioneModel() );
    }
%>
    <tr>
      <td class="l">Destinatario Istituto</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lIstituto.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
        <font class="campo"><%=StringUtils.toStringJSP(lIstituto.getIstitutoDetenzione().getDescrComune())%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lIstituto.getNote())%>&nbsp;</font></td>
    </tr>
<%
  }

  // Avvocati Siep
  for(int i=0; i<listaAvvSiep.size(); i++)
  {
    NotificaModel lAvvocatoSiep = (NotificaModel)listaAvvSiep.get(i);
    if(lAvvocatoSiep.getAvvSiep() == null)
    {
      lAvvocatoSiep.setAvvSiep( new AvvocatoSiepModel() );
    }
%>
    <tr>
      <td class="l">Avvocato per Notifica</td>
      <td class="L" colspan="2">
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getNome())%></font>&nbsp;
          &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getDescrTipo())%>
        </font>
      </td>
    </tr>
<%
  }

  // Avvocati Sius
if(listaAvvSius.size()>0)
{
  for(int i=0; i<listaAvvSius.size(); i++)
  {
    NotificaModel lAvvocatoSius = (NotificaModel)listaAvvSius.get(i);
    if(lAvvocatoSius.getAvvSius() == null)
    {
      lAvvocatoSius.setAvvSius( new AvvocatoSiusModel() );
    }
%>
    <tr>
      <td class="l">Avvocato per  Notifica</td>
      <td class="L" colspan="2">
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSius.getAvvSius().getAvvocato().getCognome()) +" "+ StringUtils.toStringJSP(lAvvocatoSius.getAvvSius().getAvvocato().getNome())%></font>&nbsp;
          &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSius.getAvvSius().getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvocatoSius.getAvvSius().getAvvocato().getDescrTipo())%>
        </font>
      </td>
    </tr>
<%
  }}
%>
</table>
<%if(tenori != null && tenori.size()>0){%>
<table>
    <tr>
      <td colspan=2>&nbsp;</td>
    </tr>

    <tr>
      <td class="Titolo" colspan=2> Esiti</td>
    </tr>

<%
  //Elenco Tenori.
  Iterator iter = tenori.iterator();
  while (iter.hasNext())
  {
    TenoreModel lTenMod = (TenoreModel)iter.next();
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(lTenMod.getDescrOggettoTenore())%></td>
      <td class="l"><%=StringUtils.toStringJSP(lTenMod.getDescrEsitoTenore())%></td>
    </tr>
<%
  }
%>
</table>
<%}%>
</body>
</html>