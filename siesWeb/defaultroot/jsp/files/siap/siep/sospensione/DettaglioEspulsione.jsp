<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="java.util.Arrays"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>


<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="verbale"       scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="nuovapenaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione"        scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="NoteAutE"          scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"  scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="UffTDS"            scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"            scope="request" class="java.lang.String"/>
<jsp:useBean id="UffURC"            scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="NoteSA"          scope="request" class="java.lang.String"/>

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
    <font class="campo">DETTAGLIO AVVENUTA ESPULSIONE ART.16 COMMA 5 D.LO 286/1998 E SUCC.MOD.</font>
  </td>
<%
  if ((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
      (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null &&
      eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)) {%>
 <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaEspulsione&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%}
%>

</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<table>


    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
      <font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
       <%}
        else
        {%>
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
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>

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
           <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>

<%
        }  }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
           <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>

      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          } }
    }
%>
</tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
          <tr>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
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
      <tr>
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
        }else {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>
           -
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
         </td>


<%            }
      }
%>
  </tr>
       <tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
         </td>
        <td class="l">Data Trasmissione</td>
        <td class="L" >
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
         </td>
      </tr>

    <tr>
      <td class="l">Protocollo N.</td>
      <td class="l" colspan="3">
         <font class="campo"><%=StringUtils.toStringJSP(verbale.getNumeroProtocollo())%>   </font>
      </td>
   </tr>
   <tr>
      <td class="l">Data Emissione provvedimento</td>
      <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(), "dd-MM-yyyy") )%>   </font>
      </td>
      <td class="l">Data Ricezione Verbale</td>
      <td class="L">
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(), "dd-MM-yyyy") )%>   </font>
      </td>
  </tr>
  <tr>
        <td class="l">Autorità che ha provveduto all'espulsione</td>
        <td class="l" colspan='3'>
          <font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario())%></font>
          di <font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario())%></font>

        </td>
  </tr>
  <tr>
         <td class="l">Indirizzo</td>
         <td class="L"  colspan='3'>
          <font class="campo"><%=StringUtils.toStringJSP(verbale.getNote())%> </font>
         </td>
  </tr>
  <tr>
    <td class="l">Data Scarcerazione per espulsione</td>
     <td class="l" colspan="3">
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataInizio(), "dd-MM-yyyy") )%>   </font>
    </td>
  </tr>
  <tr>    
    <td class="l">Data Scadenza espulsione</td>
     <td class="l" colspan="3">
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataFine(), "dd-MM-yyyy") )%>   </font>
    </td>    
  </tr>
      <tr>
        <td class="l">
        	<font class="label">Pena Espiata</font>
        </td>
    		<td class="l" colspan="3">
    			<font class="label">Anni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
    			<font class="label">Mesi</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
    			<font class="label">Giorni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
    		</td>
      </tr>
      <tr>
        <td class="l">
        	<font class="label">Nuova Pena Residua</font>
        </td>
    		<td class="l">
    			<font class="label">Reclusione : Anni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniReclusione(), "0")%></font>
    			<font class="label">Mesi</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiReclusione(), "0")%></font>
    			<font class="label">Giorni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniReclusione(), "0")%></font>
            </td>
    		<td class="l" colspan=2>
    			<font class="label">Arresto : Anni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniArresto(), "0")%></font>
    			<font class="label">Mesi</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiArresto(), "0")%></font>
    			<font class="label">Giorni</font>
    			<font class="Campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniArresto(), "0")%></font>

    		</td>
      </tr>

<%
  if(magistrato != null && !"".equals(magistrato.getNome())){%>
  <tr>
   <td class="l">Magistrato Firmatario
   <td class="L" colspan="3">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<%}

       if(autoritaEsternaE != null && autoritaEsternaE.getIdAutoritaEsterna() != null)
       {
%>
        <tr>
         <td class="l">Autorità di polizia</td>
         <td class="l" colspan="3">
           <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrTipoAutorita())%></font> di &nbsp;
           <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede())%></font>
         </td>
        </tr>
<%
         if(NoteAutE != null && !"".equals(NoteAutE))
         {
%>
        <tr>
         <td class="l">Indirizzo</td>
         <td class="l" colspan="3">
           <font class="campo"><%=StringUtils.toStringJSP(NoteAutE)%></font>
         </td>
        </tr>
<%
         }
       }

       if(UffUDS != null && !"".equals(UffUDS))
       {
%>

        <tr>
         <td class="l">Magistrato di Sorveglianza</td>
         <td class="l" colspan="3">
            <font class="campo"><%=StringUtils.toStringJSP(UffUDS)%></font>
         </td>
        </tr>
<%
       }

       if(UffTDS != null && !"".equals(UffTDS))
       {
%>

        <tr>
         <td class="l">Tribunale di Sorveglianza</td>
         <td class="l" colspan="3">
            <font class="campo"><%=StringUtils.toStringJSP(UffTDS)%></font>
         </td>
        </tr>
<%
       }

       if(UffURC != null && !"".equals(UffURC.getDescrTipoUfficio()))
       {
%>
        <tr>
         <td class="l">Ufficio Recupero Crediti presso</td>
         <td class="l" colspan="3">
           <font class="campo"><%=StringUtils.toStringJSP(UffURC.getDescrTipoUfficio())%></font> di &nbsp;
           <font class="campo"><%=StringUtils.toStringJSP(UffURC.getDescrComune())%></font>
         </td>
        </tr>
<%
       }

       if(NoteSA != null && !"".equals(NoteSA))
       {
%>
        <tr>
         <td class="l">Numero Registro Sanzioni Alternative</td>
         <td class="l" colspan="3">
            <font class="campo"><%=StringUtils.toStringJSP(NoteSA)%></font>
         </td>
        </tr>
<%
       }
  if(eventonotifica != null && eventonotifica.getNotifiche() != null)
  {
    Iterator iter = (Arrays.asList(eventonotifica.getNotifiche())).iterator();
    while (iter.hasNext())
    {
     NotificaModel lNotMod = (NotificaModel)iter.next();
     if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
     {
      AvvocatoSiepModel lAvvMod = lNotMod.getAvvSiep();
      AutoritaEsternaModel lAuMod = lNotMod.getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per  Notifica</td>
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
	    <td class="l">Autorità Notifica</td>
            <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
      </td>
     </tr>
<%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")){%>
     <tr>
      <td class="l">Note</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
     </tr>
<%}
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
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActUploadEspulsione">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sospensione.action.ActLoadDettaglioEspulsione">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>