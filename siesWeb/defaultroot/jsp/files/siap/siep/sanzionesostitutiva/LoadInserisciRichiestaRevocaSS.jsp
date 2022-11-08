<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="magistrato"    scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="motivoProvv"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaGe"     scope="request" class="java.lang.String"/>

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
<title>[S.I.E.S.]</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    

    function Verify()
    {
       if(document.LoadInserisciRichiestaRevoca.<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT%>.value=="")
      {
        alert("La Sede dell'Ufficio Emittente è obbligatoria");
        return false;
      }	

    }
     

   function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
     {
  
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
     } 
 
  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
     
  </script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Richiesta Revoca Sanzione Sostitutiva</font>
      </td>
    </tr>
</table>


  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaRevoca">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciRichiestaRevSS">
 


  <table>
      <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
          <font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
<%    }
      else
     {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%   }%>
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
               if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null)
               {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               }
%>
            </td>
           </tr>
<%
               if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td>
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
<%
              if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null)
              {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
              }
%>
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
<%
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

<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
  </table>
  <table width='100%'>
    <tr>
      <td class="Titolo" colspan="6"> Richiesta</td>
    </tr>
     <tr>
      <td class="l">Oggetto</td>
      <td class="L" colspan="3">
        <select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
          <%=motivoProvv%>
        </select>
      </td>
    </tr>
   <tr>
     <td class="Titolo" colspan=6> Magistrato Firmatario</td>
   </tr>
  <tr>
   <td class="l">Magistrato Firmatario
   <td class="L" colspan="3">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciRichiestaRevoca','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
      </td>
  </tr> 
   <tr>
     <td class="Titolo" colspan=6>Destinatari</td>
   </tr>
    <tr>
      <td class="l" >Ufficio Giudice dell'Esecuzione <font class=ob>(*)</font></td>
        <td class="l" colspan="3">
          <select Title="Autorità Emittente" name="<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>" >
            <%=autoritaGe%>
           </select>
      </td>
    </tr> 
    <tr>
        <td class="l">Sede Ufficio Emittente &nbsp;</td>
          <td class="l" colspan="3">
          <input title="Sede Ufficio Emittente"  type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT%>"  maxlength="35" size="35" >
          <a href="Javascript:ListaComuni('LoadInserisciRichiestaRevoca','<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT%>',document.LoadInserisciRichiestaRevoca.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>[document.LoadInserisciRichiestaRevoca.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
       </td>
    </tr> 
    <tr>
      <td  class="l">Motivazioni</td>
      <td  class="L" colspan="3">
        <TEXTAREA title="Note" name="<%= ICostantiSanzioneSostitutiva.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      </td>
    </tr>
 
    <tr>      
     <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
     </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaRevoca");
  
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");  
 
//Controlli Data Trasmissione
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Giorno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Giorno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099"); 
  
</script>
</body>
</html>