<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.sospensione.model.PeriodoInterruzioneModel"%>

<jsp:useBean id="decretoordinanza"    scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="listainterruzioni"   scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="totaleinterruzione"  scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
String lcodicePosizione = "";
if (lPosizione != null)
  	lcodicePosizione = lPosizione.getCodPosizioneGiuridica();
else
   	lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

/*
  //Inizializzazione campi se SOSPENSIONE non trovata
  //(per evitare eventuale NullPointerException)
  if(sospensione.getIdSospensione() == null)
    sospensione.setQuantumZero();
*/
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Ripristino </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript">
    function Verify()
    {
      return true;
    }
  </script>
</head>
<body class="corpo">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <INPUT type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ripristino.action.ActLoadInserisciProvvedimentoRipristino">
      <font class="campo">Dettaglio Sospensione</font>
    </td>
     <td class="LBG">
      <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
     </td>
<%
/*
    if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null)
      if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
      {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
 <td class="LBG">
   <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActStampaOrdineScarcerazionePerNuovaScadenzaPena&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
     <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
   </a>
 </td>
--%>
<%
/*
      }

      if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
      {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="LBG">
  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActStampaOrdineScarcerazionePerNuovaScadenzaPena&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
    <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
  </a>
</td>
--%>
<%
/*
      }
*/
%>
    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=7>
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
%>
    <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
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
          <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
        </tr>
<%
      }
    }
%>
  </table>
  <br>
  <table width="50%">
    <tr>
      <td colspan=4 class="titolo">Ripristino Esecuzione</td>
    </tr>
    <tr>
      <td class="l">
        Data comunicazione evento
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(), "dd-MM-yyyy"))%>
        </font>&nbsp;
      </td>
    </tr>
    <tr>
      <td  class="l">
        Data ricezione evento
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(), "dd-MM-yyyy"))%>
        </font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Protocollo
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(decretoordinanza.getProtocollo())%>
        </font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Autorità
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(decretoordinanza.getAltraAutorita())%>
        </font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Luogo
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(decretoordinanza.getAltroLuogo())%>
        </font>&nbsp;
      </td>
    </tr>
      <tr>
      <td class="l">
        Motivo
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(decretoordinanza.getNote())%>
        </font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Data ripristino esecuzione
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataFineInterruzione(), "dd-MM-yyyy"))%>
        </font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Annotazioni
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(decretoordinanza.getMotivazioni())%>
        </font>&nbsp;
      </td>
    </tr>
  </table>
  <br>
  <table>
<%
    if(!listainterruzioni.isEmpty())
    {
%>
      <tr>
        <td colspan=4 class="titolo">Riepilogo Periodi di Interruzione</td>
      </tr>
<%

      Iterator iter = listainterruzioni.iterator();
      boolean lFlagPosizione = false;
      while (iter.hasNext())
      {
        PeriodoInterruzioneModel lPerInt = (PeriodoInterruzioneModel)iter.next();
%>
        <tr>
          <td class="l">
            Dal :
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerInt.getQuantum().getDataInizio(), "dd-MM-yyyy"))%>
            </font>&nbsp;
            Al :
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerInt.getQuantum().getDataFine(), "dd-MM-yyyy"))%>
            </font>&nbsp;
            Posizione Giuridica :
            <font class="campo">
<%
              if(    lPerInt.getPosizioneGiuridica() != null
                 && !lPerInt.getPosizioneGiuridica().equals(""))
              {
%>
                <%=StringUtils.toStringJSP(lPerInt.getPosizioneGiuridica())%>
<%
              }
              else
              {
                lFlagPosizione = true;
%>
                (*)
<%
              }
%>
            </font>&nbsp;
            Durata : Anni&nbsp;
            <font class="campo">
              <%=lPerInt.getQuantum().getNumAnni()%>
            </font>&nbsp;
            Mesi&nbsp;
            <font class="campo">
              <%=lPerInt.getQuantum().getNumMesi()%>
            </font>&nbsp;
            Giorni&nbsp;
            <font class="campo">
              <%=lPerInt.getQuantum().getNumGiorni()%>
            </font>&nbsp;
          </td>
        </tr>
<%
      }
%>
        <tr>
          <td class="lNoBord" colspan="4">
<%
            if(lFlagPosizione)
            {
%>
            <font class="descr">
              (*) Impossibile visualizzare: la data interruzione risulta diversa dalla data di
              decorrenza della posizione giuridica
            </font>&nbsp;
<%
            }
%>
          </td>
        </tr>
<%
    }
%>
  </table>
  <br>
  <table width="60%">
    <tr>
      <td colspan=5 class="titolo">Pena Residua</td>
    </tr>
<%
    if( flagergastolo.equals("N") )
    {
      if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
          <tr>
            <td class="l">Reclusione</td>
            <td class="l" colspan=3>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">Multa</td>
<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
--%>
          </tr>
<%
      }

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=3>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">Ammenda</td>
<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
--%>
        </tr>
<%
      }
    }

    if( lcodicePosizione != null
      && (!lcodicePosizione.equals("07")
      && !lcodicePosizione.equals("10")) )
    {
%>
        <tr>
<%
          if(   flagergastolo.equals("N")
             && penaresidua.getDataInizio() != null )
          {
%>
            <td class="l">Ultima Decorrenza Pena</td>
            <td class="L">
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
              </font>
            </td>
<%
          }

          if(   flagergastolo.equals("N")
             && penaresidua.getDataFine() != null )
          {
%>
            <td class="l">Data fine Pena</td>
            <td class="L">
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;
              </font>
            </td>
<%
          }

/*
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S"))) && penaresidua.getDataFinePresunta() != null)
        {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">Data Fine Pena Automatica</td>
<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
--%>
<%
/*
       }
*/
/*
         if (  flagergastolo.equals("N")
            && penaresidua.getDataFine()!=null
            )
         {
           String lClassTd="l";
           String lClassFont="campo";
           if(penaresidua.getDataFine() != null
             && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
           {
             lClassTd="lRosso";
             lClassFont="lRosso";
           }
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">Data Fine Pena</td>
<td class="< %=lClassTd%>">
  <font class="< %=lClassFont%>">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%>&nbsp;
  </font>
</td>
--%>
<%
/*
         }
*/
    }
%>
    </tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><input class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>