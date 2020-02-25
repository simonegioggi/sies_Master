<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
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
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>

<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="decretoordinanza"    scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<%
//  < jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
%>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="sospensione"   scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>

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

  //Inizializzazione campi se SOSPENSIONE non trovata
  //(per evitare eventuale NullPointerException)
  if(sospensione.getIdSospensione() == null)
    sospensione.setQuantumZero();
%>

<html>
<head>
	<title>[S.I.E.S.] - Differimento / Rinvio dell'esecuzione </title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
	<script>
	 	function Verify() {
			return true;
		}
	</script>
</head>
<body class="corpo">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSosp">

  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActLoadInserisciNotificheDifferimento">
    <input type="HIDDEN" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP%>" value="<%=decretoordinanza.getIdDecretoOrdinanzaSiep()%>">
      <font class="campo">Dettaglio Differimento / Rinvio dell'esecuzione</font>
    </td>
<!--
     <td class="LBG">
-->
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
<!--
    </td>
-->
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


<%
	  if (  sospensione.getNumAnniPenaEspiata().intValue()!=0
	  	 || sospensione.getNumMesiPenaEspiata().intValue()!=0
	  	 || sospensione.getNumGiorniPenaEspiata().intValue()!=0 )
	  {
%>
       <tr>
				<td class="l">
					<font class="label">Pena Espiata</font>
				</td>
				<td class="l">
					<font class="label">Anni</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
					<font class="label">Mesi</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
					<font class="label">Giorni</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
				</td>
			</tr>
<%
	  }
	  if (  flagergastolo.equals("N")
       && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
	  	 || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
	  	 || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
       || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
	  	 || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
	  	 || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
      )
	  {
%>
      <tr>
				<td class="l">
					<font class="label">Pena Residua</font>
				</td>
				<td class="l">
<%
          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
	  	       || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0)
             )
          {
%>
            <font class="label">Reclusione : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
<%
            if(sospensione.getMultaResidua()!=null && sospensione.getMultaResidua().compareTo(new BigDecimal(0))!=0)
            {
%>
              <font class="label">Multa </font>
              <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%></font>&nbsp;€&nbsp;
<%
            }
          }
          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0) )
          {
%>
            <font class="label"> Arresto : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
<%
            if(sospensione.getAmmendaResidua()!=null && sospensione.getAmmendaResidua().compareTo(new BigDecimal(0))!=0)
            {
%>
              <font class="label">Ammenda </font>
              <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%></font>&nbsp;€&nbsp;
<%
            }
          }

          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
            )
          {
%>
              </td>
            </tr>
<%
          }
    }

    if(flagergastolo.equals("S"))
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO</font>
        </td>
      </tr>
<%
    }else if(flagergastolo.equals("D"))
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
        </td>
      </tr>
<%
    }
%>
    </table>
    <br>
<%
//==============================================================================
//           Dati del Differimento / Rinvio dell'Esecuzione
//==============================================================================
%>
    
    <table style="width: 95%;">
      <tr>
        <td colspan=6 class="titolo">Dati del Differimento / Rinvio dell'Esecuzione</td>
      </tr>

    <tr>
      <td class="l"  width="18%">
        Tipo Provvedimento
     </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrTipoProvvedimento())%></font>&nbsp;
    </td>
     </tr>
     <tr>
      <td class="l">
       Trasmessa da
       </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(decretoordinanza.getDescrTipoAutoritaEmittente())%>&nbsp;
        </font>

        di
        <font class="campo">
          <%=StringUtils.toStringJSP(decretoordinanza.getDescrLuogoEmittente())%>&nbsp;
        </font>

       in data

        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(),"dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
  </tr>

<%if(decretoordinanza.getAnnoRegistro() != null || decretoordinanza.getNumRegistro()!= null ){%>
 <tr>
      <td class="l">
        Anno/Numero SIUS:
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAnnoRegistro())%></font>&nbsp;
          /
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNumRegistro())%></font>&nbsp;
      </td>
 </tr>
<%}%>



<%if(decretoordinanza.getDataRicezioneProvvedimento() != null ){%>
<tr>
      <td class="l">
        Ricevuta il
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
      &nbsp;&nbsp;&nbsp;
     </td>
 </tr>
<%}%>
<%if(decretoordinanza.getAnnoProvvedimento() != null || decretoordinanza.getNumProvvedimento()!= null){%>
<tr>
    <td class="l">
        Anno/Numero Provvediento:
     </td>
     <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAnnoProvvedimento())%></font>&nbsp;
          /
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNumProvvedimento())%></font>&nbsp;
      </td>
 </tr>
<%}%>

</TABLE>
<table style="width: 95%;">
  <tr>
    <td class="l" width="18%">Contenuto Decisione</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrOggettoDecisione())%></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">Oggetto Decisione :</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrOggettoProcedimento())%></font>&nbsp;
    </td>
  </tr>

  <tr>
    <td class="l">Annotazioni</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getMotivazioni())%></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">Data Differimento</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataDifferimento(),"dd-MM-yyyy"))%></font>&nbsp;
      <%
          if( decretoordinanza.getFlagScarcerareScarcerato()!=null
             && decretoordinanza.getFlagScarcerareScarcerato().equals("S")
             )
          {
            out.print("       [Già scarcerato]");
          }
          else if(decretoordinanza.getFlagScarcerareScarcerato()!=null
                && decretoordinanza.getFlagScarcerareScarcerato().equals("D"))
          {
            out.print("       [Da scarcerare]");
          }
%>
    </td>
  </tr>

<%if(decretoordinanza.getDataRinvio() != null ||  decretoordinanza.getAltraAutorita() != null || (decretoordinanza.getNumAnniRinvio() != null || decretoordinanza.getNumMesiRinvio() != null || decretoordinanza.getNumGiorniRinvio() != null) ){%>
  <tr>
    <td colspan=5 class="titolo">Durata Differimento della Pena</td>
  </tr>
<%}%>

<%if(decretoordinanza.getDataRinvio() != null){%>
  <tr>
    <td class="l">Rinvio fino al :</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRinvio(),"dd-MM-yyyy"))%></font>&nbsp;
    </td>
  </tr>
<%}%>

<%if(decretoordinanza.getAltraAutorita() != null){%>
  <tr>
    <td class="L">Atti trasmessi al TDS di</td>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(decretoordinanza.getAltraAutorita())%>&nbsp;
      </font>
    </td>
  </tr>
<%}%>

<%if(decretoordinanza.getNumAnniRinvio() != null || decretoordinanza.getNumMesiRinvio() != null || decretoordinanza.getNumGiorniRinvio() != null){%>
  <tr>
    <td class="l">Rinvio della misura di :</td>
    <td class="L">
        Anni <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNumAnniRinvio())%></font>&nbsp;
        Mesi <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNumMesiRinvio())%></font>&nbsp;
        Giorni <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNumGiorniRinvio())%></font>&nbsp;
        Fino alla decisione del Tribunale di Sorveglianza
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
<%}%>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    </td>
  </tr>
</table>
</form>

<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciSosp");

    frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>