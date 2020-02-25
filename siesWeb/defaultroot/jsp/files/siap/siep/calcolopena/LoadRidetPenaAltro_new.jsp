<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%// Oggetti motivo computo%>
<jsp:useBean id="oggetto"             scope="request" class="java.lang.String" />
<jsp:useBean id="oggettoDufficio"     scope="request" class="java.lang.String" />
<jsp:useBean id="oggettoAltroUfficio" scope="request" class="java.lang.String" />

<%// Pena in decorrenza %>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
  
<%// Caricamento combo altra autorità %> 
<jsp:useBean id="emessoDa"              scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoprovvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettoProvvedimento"  scope="request" class="java.util.ArrayList"/>

<% // %>
<jsp:useBean id="autorita"             scope="request" class="java.util.ArrayList"/>

<%
//==============================================================================
// form per l'inserimento del Provvedimento di Rideterminazione Pena 'Altro'
// menu: 'Rideterminazione Pena - Provvedimenti del PM - Altro'
//
// - Posizione giuridica
// - Pena Residua In espiazione/Da espiare
// -
// -
//==============================================================================
int maxNumComputi = 4;

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel     lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel  lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel            lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

%>

<!-- LoadRidetPenaAltro_new.jsp -->
<html>

<head>
  <title> [S.I.E.S.] - Annotazioni Manuali - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    
    var lMaxNumComputi = <%=maxNumComputi%>;
    
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    function Verify()
    {
      // - Campo Oggetto obbligatori
      // - Quantum obbligatori (almeno uno): segno e quantità o importi
      // - Se provvedimento altra autorità obbligatori:
      //   * Tipo Provvedimento
      //   * Oggetto provvedimento
      //   * Autorità emittente tipo e sede
      // - Magistrato Firmatario (?)
      var obbligatori;

      // Controllo le combo Oggetto (n.b. sono due una visibile, l'altra no)
      if (   document.f.TipoOrd[0].checked
          && document.getElementById('selectProvvedimentoDufficio').selectedIndex==0
         )
      {
        alert("Selezionare Oggetto");
        document.getElementById('selectProvvedimentoDufficio').focus();
        return false;
      }
      
      //===========================================
      // Controllo sui campi altra autorità
      //===========================================
      if (document.f.TipoOrd[1].checked)
      {        
        
        if (   document.f.TIPO_AUTORITA_AA.selectedIndex==0  )
        {
          alert("Selezionare Tipo Autorità Emittente");
          document.f.TIPO_AUTORITA_AA.focus();
          return false;
        }        
        
        // Data Ricezione Provv AA
        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value;
        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value;

        var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di ricezione Provvedimento Altra Autorità non valida');
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.focus();
          return false;
        }
        
        // Data Emissione
        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value;
        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value;

        var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di emissione Provvedimento Altra Autorità non valida');
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.focus();
          return false;
        }
        
        if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.value=="")
        {
          alert('Anno Provvedimento Altra Autorità obbligatorio');
          document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.focus();
          return false;
        }
        else if (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.value<1950)
        {
          alert('Anno Provvedimento Altra Autorità non valido');
          document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.focus();
          return false;
        }
        
        if (document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA.value=="")
        {
          alert('Numero Provvedimento Altra Autorità obbligatorio');
          document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA.focus();
          return false;
        }

        // Tipo provvedimento
        if (document.f.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.selectedIndex==0)
        {        
          alert("Selezionare Tipo Provvedimento");
          document.f.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.focus();
          return false;
        }
        
        // Oggetto provvedimento
        if (document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.selectedIndex==0)
        {        
          alert("Selezionare Oggetto");
          document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.focus();
          return false;
        }
        
        // Sede 
        if (document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.value=="")
        {        
          alert("Selezionare Sede Autorità Emittente");
          document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.focus();
          return false;
        }
      }

  
      // Quantum di computo
      var numComputi = 0;
      
      // Per ogni rigo visibile verifico la coerenza dei dati. Se presente il segno
      // vanno specificati anche i quantum. Se presenti i quantum è obbligatorio
      // il segno. E' accettato il rigo vuoto anche se visibile.
      for (i=0; i<lMaxNumComputi; i++){
        idComputo = "annotazione_"+i;
        display = document.getElementById(idComputo).style.display;
        if (display=="block"){
          // Rigo visibile controllo la presenza e coerenza dei dati
          if (document.getElementById('PM_'+i).selectedIndex ==0)
          {
            // Segno assente verifico che NON ci siano i quantum
            if (   trimStringa(document.getElementById('ARec_'+i).value) != "" 
                || trimStringa(document.getElementById('MRec_'+i).value) != "" 
                || trimStringa(document.getElementById('GRec_'+i).value) != "" 
                || trimStringa(document.getElementById('Multa_'+i).value) != "" 
                || trimStringa(document.getElementById('Mul_dec_'+i).value) != "" 
                || trimStringa(document.getElementById('AArr_'+i).value) != ""
                || trimStringa(document.getElementById('MArr_'+i).value) != "" 
                || trimStringa(document.getElementById('GArr_'+i).value) != "" 
                || trimStringa(document.getElementById('Ammenda_'+i).value) != "" 
                || trimStringa(document.getElementById('Amm_dec_'+i).value) != "" 
                || trimStringa(document.getElementById('motivazioni_'+i).value) != ""
               )
            {
              alert ("Selezionare tra + e -");
              document.getElementById('PM_'+i).focus();
              return false;
            }
          }
          else {
            // Presente il segno verifico se presenti i quantum
            if (   trimStringa(document.getElementById('ARec_'+i).value) == "" 
                && trimStringa(document.getElementById('MRec_'+i).value) == "" 
                && trimStringa(document.getElementById('GRec_'+i).value) == "" 
                && trimStringa(document.getElementById('Multa_'+i).value) == "" 
                && trimStringa(document.getElementById('Mul_dec_'+i).value) == "" 
                && trimStringa(document.getElementById('AArr_'+i).value) == ""
                && trimStringa(document.getElementById('MArr_'+i).value) == "" 
                && trimStringa(document.getElementById('GArr_'+i).value) == "" 
                && trimStringa(document.getElementById('Ammenda_'+i).value) == "" 
                && trimStringa(document.getElementById('Amm_dec_'+i).value) == "" 
                //&& trimStringa(document.getElementById('motivazioni_'+i).value) == ""
               )
            {
              alert ("Indicare i quantum o deselezionare il segno + e -");
              document.getElementById('ARec_'+i).focus();
              return false;
            }
            else{
              numComputi=numComputi+1;
            }
          }
        }
      }
      
      if (numComputi==0){
        alert ("Selezionare la  durata del periodo per arresto o reclusione oppure la sanzione");
        //alert ("Indicare le quantità da computare. Quantum e/o importi. ");
        return false;
      }
      
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }     
      
      //=======================
      // Controllo Magistrato  
      //=======================
      if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
        alert("Selezionare Magistrato firmatario");
        return false;
      }       

      
      
      return true;
    }
    
    //==========================================================================
    // Visualizza nasconde le combo Oggetto. 
    // n.b. trattasi di due combo distinte una per i provvedimenti di 
    //      ufficio, una per i provvedimento altro ufficio
    //==========================================================================
    function radioBase()
    {
      if(document.f.TipoOrd[0].checked)
      {
        // Visualizzo la combo con i codici motivo previsti per i provvedimenti d'ufficio
        document.getElementById('OggettoProvvedimentoDufficio').style.display = "block";
        document.getElementById('SelectProvvedimentoDufficio').disabled = false;
        document.getElementById('SelectProvvedimentoDufficio').selectedIndex=0;
        
        // Nascondo la div con i dati del 'provvedimento altra autorità'
        // Combo oggetti compresa
        document.getElementById('DivAltraAutorita').style.display = "none";
      }
      else 
      {
        // Visualizzo la combo con i codici motivo previsti per i provvedimenti di altro ufficio
        //document.getElementById('OggettoProvvedimentoAltroUfficio').style.display = "block";
        //document.getElementById('SelectProvvedimentoAltroUfficio').disabled = false;
        //document.getElementById('SelectProvvedimentoAltroUfficio').selectedIndex=0;
        
        // Nascondo la Combo con gli oggetti "d'ufficio" e visualizzao la div con 
        // gli oggetti di altro ufficio
        document.getElementById('OggettoProvvedimentoDufficio').style.display = "none";
        document.getElementById('SelectProvvedimentoDufficio').disabled = true;
        
        // Visualizzo la div con i dati del 'provvedimento altra autorità'
        document.getElementById('DivAltraAutorita').style.display = "block";
      }
    }

    //==========================================================================    
    // ?????????? eliminare???
    //==========================================================================    
    function visualizza_dati_altra_autorita(isVisible)
    {
      var node1 = document.getElementById("DivAltraAutorita");

      if (isVisible=='visualizza') {
        node1.style.display = "block";
      }
      else {
        node1.style.display = "none";
      }
    }
    
      function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
      {
         var desktop;
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }
    

    //==========================================================================
    // Funzioni per la gestione del caricamento dinamico delle combo altra autorità
    //==========================================================================
    // ** GESTIONE COMBO BOX **
    // Matrice di tante righe quanti i componenti di "Provvedimento emesso da"
    // e tante colonne quante sono le combo da gestire. In questo caso 2 combo
    // La combo Autorità emittente e la combo Oggetto 
    // 
    //                            Combo Autorità emittente | Combo Oggetto
    // -                        :            []            |      []
    // Altra Autorità           :            []            |      []
    // Giudice Esecuzione       :            []            |      []
    // Giudice di Sorveglianza  :            []            |      []
    
    var lNumProvvedimento=<%=emessoDa.size()%>;
    var lNumCombo = 2; // Combo Autorità emittente e Combo Oggetto

    var lMatrice=new Array(lNumProvvedimento)
    for (i=0; i<lNumProvvedimento; i++)
      lMatrice[i]=new Array(lNumCombo);

<%
      // Carico le combo Tipo Autorita Emittente per i 
      for(int i=0; i<emessoDa.size(); i++)
      {
        List lAut = (List)autorita.get(i);

        out.println("\n\n\tvar lAutorita"+i+"=new Array()\n");

        Iterator lIterAutorita = lAut.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterAutorita.hasNext())
        {
          lDec = (DecodificheModel) lIterAutorita.next();
          out.println("\tlAutorita"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCodiceAlternativo()+"\");");
          idx++;
        }
      }
      
      // Carico le Combo Oggetto 
      for(int i=0; i<emessoDa.size(); i++)
      {
        List lCont = (List)oggettoProvvedimento.get(i);

        out.println("\n\n\tvar lOggetto"+i+"=new Array()\n");

        Iterator lIterOggetto = lCont.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterOggetto.hasNext())
        {
          lDec = (DecodificheModel) lIterOggetto.next();
          out.println("\tlOggetto"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCode()+"\");");
          idx++;
        }
      }
      
%>
    
    // La matrice contiene l'insieme delle opzioni selezionabili
    // strutturare in questo modo:
    // ci sono tante righe quante sono le opzioni della combo 'Provvedimento'
    // e tante colonne quante sono le combo da relazionare
    //var lAutorita1=new Array()
    //var lOggetto1=new Array()
    
    //var lAutorita2=new Array()
    //var lOggetto2=new Array()

    lMatrice[0][0] = lAutorita0;  // Contiene Combo Tipo Autorità
    lMatrice[0][1] = lOggetto0;   // Contiene Combo Codici Motivo

    // Altra Autorità
    lMatrice[1][0] = lAutorita1;  // Contiene Combo Tipo Autorità
    lMatrice[1][1] = lOggetto1;   // Contiene Combo Codici Motivo
    
    // Giudice Esecuzione
    lMatrice[2][0] = lAutorita2;  // Contiene Combo Tipo Autorità
    lMatrice[2][1] = lOggetto2;   // Contiene Combo Codici Motivo
    
    // Giudice Sorveglianza
    lMatrice[3][0] = lAutorita3;  // Contiene Combo Tipo Autorità
    lMatrice[3][1] = lOggetto3;   // Contiene Combo Codici Motivo

    function initCombo()
    {
      //alert("initCombo");
      <% int lIndex = 0; %>
      document.f.TIPO_AUTORITA_AA.options[<%=lIndex%>].selected=true;
      caricamento();
    }
           
    function caricamento()
    {
      //alert("caricamento");TIPO_AUTORITA_AA
      var idxSel = document.f.TIPO_AUTORITA_AA.options.selectedIndex;

      loadCombo(document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA, idxSel, 0); // tipo autorità emittente
      loadCombo(document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA, idxSel, 1);            // CAMPO_COD_OGGETTO_DEFINIZIONE
    }

    //==========================================================================
    // Effettua il caricamento della combo 'campo' con i dati filtrati per
    // campo = oggetto combo da caricare
    // riga  = 
    //==========================================================================
    function loadCombo(campo, riga, colonna)
    {
      // ripulisce la combo
      for (m=campo.options.length-1;m>=0;m--)
        campo.options[m]=null;
      
      // Carica la combo
      for (i=0;i<lMatrice[riga][colonna].length;i++)
      {
        campo.options[i]=new Option(lMatrice[riga][colonna][i].text,lMatrice[riga][colonna][i].value)
      }

      campo.options[0].selected=true;

    }

    function initPage(){
      initCombo();
      radioBase();
    }
    
    //==========================================================
    // Visualizza una nuova riga per l'inserimento dei quantum
    //==========================================================
    function addQuantum(){
      for (i=0; i<lMaxNumComputi; i++){
        idComputo = "annotazione_"+i;
        display = document.getElementById(idComputo).style.display;
        if (display=="none"){
          Rigo = i;
          document.getElementById('PM_'+Rigo).disabled = false ;
          // Reclusione
          document.getElementById('ARec_'+Rigo).disabled = false ;
          document.getElementById('MRec_'+Rigo).disabled = false ;
          document.getElementById('GRec_'+Rigo).disabled = false ;
          document.getElementById('Multa_'+Rigo).disabled = false ;
          document.getElementById('Mul_dec_'+Rigo).disabled = false ;
    
          //Arresti
          document.getElementById('AArr_'+Rigo).disabled = false ;
          document.getElementById('MArr_'+Rigo).disabled = false ;
          document.getElementById('GArr_'+Rigo).disabled = false ;
          document.getElementById('Ammenda_'+Rigo).disabled = false ;
          document.getElementById('Amm_dec_'+Rigo).disabled = false ;
    
          // Motivazioni
          document.getElementById('motivazioni_'+Rigo).disabled = false ;
          
          strTdCancella = '<a href="Javascript:cancellaComputo(\''+Rigo+'\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';

          document.getElementById('tdCancella_'+Rigo).innerHTML = strTdCancella;
          document.getElementById('tdCancella_'+(Rigo-1)).innerHTML = '<br>';
          
          // Visualizzo
          document.getElementById(idComputo).style.display = "block";
          break;
        }
      }
    }
    
    function cancellaComputo(Rigo){
      
      //===========================================================
      // Cancello il contenuto delle celle
      //===========================================================
      document.getElementById('PM_'+Rigo).selectedIndex = 0 ;
      // Reclusione
      document.getElementById('ARec_'+Rigo).value = "" ;
      document.getElementById('MRec_'+Rigo).value = "" ;
      document.getElementById('GRec_'+Rigo).value = "" ;
      document.getElementById('Multa_'+Rigo).value = "" ;
      document.getElementById('Mul_dec_'+Rigo).value = "" ;

      //Arresti
      document.getElementById('AArr_'+Rigo).value = "" ;
      document.getElementById('MArr_'+Rigo).value = "" ;
      document.getElementById('GArr_'+Rigo).value = "" ;
      document.getElementById('Ammenda_'+Rigo).value = "" ;
      document.getElementById('Amm_dec_'+Rigo).value = "" ;
      
      // Motivazioni
      document.getElementById('motivazioni_'+Rigo).value = "" ;
      
      
      //===========================================================
      // Disabilito le celle in modo che non venga fatta la submit
      //===========================================================
      document.getElementById('PM_'+Rigo).disabled = true ;
      // Reclusione
      document.getElementById('ARec_'+Rigo).disabled = true ;
      document.getElementById('MRec_'+Rigo).disabled = true ;
      document.getElementById('GRec_'+Rigo).disabled = true ;
      document.getElementById('Multa_'+Rigo).disabled = true ;
      document.getElementById('Mul_dec_'+Rigo).disabled = true ;

      //Arresti
      document.getElementById('AArr_'+Rigo).disabled = true ;
      document.getElementById('MArr_'+Rigo).disabled = true ;
      document.getElementById('GArr_'+Rigo).disabled = true ;
      document.getElementById('Ammenda_'+Rigo).disabled = true ;
      document.getElementById('Amm_dec_'+Rigo).disabled = true ;

      // Motivazioni
      document.getElementById('motivazioni_'+Rigo).disabled = true ;

      //===================
      // Nascondo la riga
      //===================
      document.getElementById('annotazione_'+Rigo).style.display = "none";
      
      if (Rigo>1){
        strTdCancella = '<a href="Javascript:cancellaComputo(\''+(Rigo-1)+'\');"><img src="/images/delete.gif" border="0" title="Cancella computo"></a>';

        document.getElementById('tdCancella_'+(Rigo-1)).innerHTML = strTdCancella;
        document.getElementById('tdCancella_'+(Rigo)).innerHTML = '<br>';
      }
    }
  </script>
</head>

<body class="corpo" onLoad="initPage();">

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciRidetPenaAltro">
  <input type="hidden" name="maxNumComputi" value="<%=maxNumComputi%>" >
  
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Rideterminazione della Pena - Altro</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<%
//==============================================================================
// Sezione con:
// - la posizione giuridica
// - la pena residua (attuale)
//==============================================================================
%>
<table>
  <tr>
    <td class="l"> Posizione Giuridica :
      <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
        <% } else { %>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
      </font>
    </td>
  </tr>
</table>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: data inizio, MAI
// - Se Libero vengono visualizzati i Quantum
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>

<%
  // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
     && PenaComplessiva.getCodTipoPenaDetentiva() != ""
     && (   PenaComplessiva.getCodTipoPenaDetentiva().equals("03")
         || PenaComplessiva.getCodTipoPenaDetentiva().equals("04")
        )
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
      <% if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) {%>
      <td class=Titolonocap colspan=6 width=80%> Pena complessiva </td>
      <% } else  {%>
      <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
      <% } %>
    </tr>

    <% if (PenRes1.getErrorMsg().startsWith("Libero")) { %>
    <tr>
      <td class="l"> Reclusione :
        Anni   <font class=campo><%=PenRes1.getNumAnni()%></font>
        Mesi   <font class=campo><%=PenRes1.getNumMesi()%></font>
        Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>
        Multa  <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
        Anni    <font class=campo><%=PenRes2.getNumAnni()%></font>
        Mesi    <font class=campo><%=PenRes2.getNumMesi()%></font>
        Giorni  <font class=campo><%=PenRes2.getNumGiorni()%></font>
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
    {
      //========================================================================
      // Visualizzo la pena residua calcolata al volo tra la data odierna e la
      // data fine pena prevista
      //========================================================================
      CalendarUtil lCU = new CalendarUtil();
      PenRes2.setDataFine   (PenRes1.getDataFine());
      PenRes2.setDataInizio (DateUtils.getSysDate());
      PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2, true));
    %>
    <tr>
      <td class="l">
        Anni : <font class=campo><%=PenRes2.getNumAnni()%> </font>
        Mesi : <font class=campo><%=PenRes2.getNumMesi()%> </font>
        Giorni : <font class=campo><%=PenRes2.getNumGiorni()%></font>
      </td>
      <td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class=l>Data Inizio : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy")%> </font></td>
      <td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class=l>Data Fine : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy") %> </font></td>
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
    <% } %>
</table>
<%
  }  // Fine Pena Residua
%>

<%
//==============================================================================
// Sezione con i dati del provvedimento
//==============================================================================
%>

<table style="width: 95%;">
  <tr>
    <td class="Titolo" colspan="100%" >Rideterminazione della pena</td>
  </tr>
  <tr>
    <td class="l" colspan="100%">
      <input type="radio" name="TipoOrd" value="dufficio" checked onClick="javascript:radioBase();">D'ufficio &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="altroUfficio" onClick="javascript:radioBase();">In esecuzione di provvedimento altro ufficio &nbsp;&nbsp;
    </td>
  </tr>
  <tr id="OggettoProvvedimentoDufficio" style="display:block">
    <td class="l" width="15%">Oggetto <font class="ob">(*)</font>:</td>
    <td class="l" >
      <select name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" id="selectProvvedimentoDufficio">
        <%=oggettoDufficio%>
      </select>
    </td>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--td class="l" id="OggettoProvvedimentoAltroUfficio" style="display:none">
      <select name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" id="selectProvvedimentoAltroUfficio">
        <%=oggettoAltroUfficio%>
      </select>
    </td--%>
  </tr>
  <tr>
    <td class="l">Note :&nbsp;</td>
    <td class="l" colspan="4"><textarea cols="100" rows="2" name="noteComputo"></textarea></td>
  </tr>
</table>

<%
//==============================================================================
// Div per la visualizzazione della sezione con gli estremi del Provvedimento
// Altra Autorità
// n.b. se presente verrà inserito un evento apposito
//  !!!!!! Attenzione!!!! Per ora non vengono acquisiti. Da rinominare le costanti
//==============================================================================
%>
<div id="DivAltraAutorita" style="display:none">
<table style="width: 100%;">
   	<tr>
   		<td colspan=4 class="titolo">Dati Provvedimento Altra Autorità</td>
   	</tr>
   	<!--tr>
   	<td class="l" colspan="4">
    <a href="Javascript:ListaDocumenti('f');">
	Seleziona provvedimenti dalla lista <img src="/images/filefolder.gif" border="0">
	</a>
	</td>
   	</tr-->
   	<tr>
   		<%-- MEV_66: aggiunto spazio prima asterisco --%>
   		<td class="l" width="20%">Provvedimento emesso da <font class=ob>(*)</font></td>
   		<td class="l" colspan="3">
   			<select Title="Provvedimento" name="TIPO_AUTORITA_AA" onChange="caricamento();">
<%
Iterator lIter = emessoDa.iterator();
while (lIter.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
       			<option value="<%=lDecMod.getCode()%>"/><%=lDecMod.getDescription()%>
<%
}
%>
   			</select>
		</td>
   	</tr>
   	<tr>
   		<td class="l">Data ricezione provvedimento</td>
   		<td class="l" colspan="3">
       		<input type="text" Title="Giorno Ricezione" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       		/
       		<input type="text" Title="Mese Ricezione" value="<%=DateUtils.getSysDate("MM")%>"   name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       		/
       		<input type="text" Title="Anno Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
   		</td>
   	</tr>
    <tr>
      	<td class="l">Data emissione provvedimento</td>
      	<td class="l">
	        <input type="text" Title="Giorno Emissione provvedimento" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Mese Emissione provvedimento" value="<%=DateUtils.getSysDate("MM")%>"   name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA"   maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Anno Emissione provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	</td>
      	<td class="l">Anno / Numero Provvedimento</td>
      	<td class="l">
			<input Title="Anno Provvedimento"   value="" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         	<input Title="Numero Provvedimento" value="" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
     	 </td>
	</tr>

    <tr>
      	<td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      	<td class="l" colspan=3>
        	<select Title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA">
          		<%=tipoprovvedimento%>
        	</select>
      	</td>
	</tr>

    <tr>
      	<td class="l">Oggetto Provvedimento <font class=ob>(*)</font></td>
      	<td class="l" colspan=3>
        	<select Title="Oggetto Definzione" class="small" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA"></select>
      	</td>
	</tr>

    <tr>
      	<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      	<td class="l">
        	<select Title="Autorità Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA"></select>
      	</td>
      	<td class="l" colspan="2">Sede  &nbsp;
        	<input title="Sede Autorita"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA"  maxlength="35" size="35">
        	<a href="Javascript:ListaComuni('f','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA',document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA[document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.selectedIndex].value);">
          		<img src="/images/filefolder.gif" border="0">
        	</a>
      	</td>
	</tr>
</table>
</div>

<%
//==============================================================================
//        Sezione per specificare i quantum di rideterminazione
// n.b. è possibile inserire più quantum
//==============================================================================
%>
<table style="width: 95%; border: 0;">
  <% for (int i=0;i<maxNumComputi;i++ ) { %>
    
  <% if (i==0) { %>
  <tr style="display:block" id="annotazione_<%=i%>">
  <% } else{ %>
  <tr style="display:none" id="annotazione_<%=i%>">
  <% } %>
    <td>
      <table style="width: 100%;">
        <tr><td colspan="100%"><hr style="width: 100%;"></td></tr>
        <tr>
          <td valign="middle" class="c" rowspan="3">+/- <font class="ob">(*)</font><br>
            <select name="PM_<%=i%>" id="PM_<%=i%>">
              <option value=""></option>
              <option value="+">+</option>
              <option value="-">-</option>
            </select>
          </td>
          <td class="titolo" colspan="2">Reclusione</td>
          <td width="25">&nbsp;</td>
          <td class="titolo" colspan="2">Arresto</td>
        <% if (i==0) { %>
          <td valign="middle" class="c" rowspan="3" id="tdCancella_<%=i%>" width="15px">&nbsp;</td>
        <% } else{ %>
          <td valign="middle" class="c" rowspan="3" id="tdCancella_<%=i%>" width="15px">&nbsp;</td>
          <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          <%--td valign="middle" class="c" rowspan="3" id="tdCancella_<%=i%>">
            <a href="Javascript:cancellaComputo('<%=i%>');">
              <img src="/images/delete.gif" border="0" title="Cancella computo">
            </a>
          </td--%>
        <% } %>
        </tr>
        <tr>
          <td class="c">
            <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Giorni</font><br>
            <input type="text" name="ARec_<%=i%>" id="ARec_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="MRec_<%=i%>" id="MRec_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="GRec_<%=i%>" id="GRec_<%=i%>" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
          <td class="c">
            <font class="label">Multa</font><br>
            <input style="align:right" type="text" name="Multa_<%=i%>"   id="Multa_<%=i%>" maxlength="8" size="6" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            ,
            <input style="align:right" type="text" name="Mul_dec_<%=i%>" id="Mul_dec_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
          <td width="25">&nbsp;</td>
          <td class="c">
            <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Giorni</font><br>
            <input type="text" name="AArr_<%=i%>" id="AArr_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="MArr_<%=i%>" id="MArr_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="GArr_<%=i%>" id="GArr_<%=i%>" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
          <td class="c">
            <font class="label">Ammenda</font><br>
            <input style="align:right" type="text" name="Ammenda_<%=i%>" id="Ammenda_<%=i%>" maxlength="8" size="6" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            ,
            <input style="align:right" type="text" name="Amm_dec_<%=i%>" id="Amm_dec_<%=i%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
        </tr>
        <tr>
          <td class="c" colspan="1"><font class="label">Motivazioni:</font></td>
          <td class="c" colspan="4"><textarea cols="60" rows="2" name="motivazioni_<%=i%>"></textarea></td>
        </tr>
      </table>
    </td>
  </tr>
  <% } %>
  
  <tr>
    <td class="l" colspan="100%">
      <a href="Javascript:addQuantum('f');">
        Aggiungi ulteriore computo
      </a>
    </td>
  </tr>
</table>



<!-- 
================================================================================
      Sezione con data Emissione, data trasmissione e magistrato
================================================================================
-->

<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Magistrato Firmatario </td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <tr>
    <td class="l">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>

<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>
