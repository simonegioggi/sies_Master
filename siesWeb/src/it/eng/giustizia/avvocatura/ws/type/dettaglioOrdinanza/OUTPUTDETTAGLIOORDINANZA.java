//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATI_RIEPILOGO_PROCEDIMENTO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATI_ORDINANZA"/>
 *         &lt;element name="descrFormaMisuraMA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComunitaMA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrFormaMisuraMS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComunitaMS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoMisureSicurezza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}MISURA_SICUREZZA_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="datiEsecuzioneMisureSicurezzaAttuali" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}MISURA_SICUREZZA_TYPE"/>
 *         &lt;element name="datiEsecuzioneMisureSicurezzaPrecedenti" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}MISURA_SICUREZZA_TYPE"/>
 *         &lt;element name="elencoMisureRideterminate" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}MISURA_SICUREZZA_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoPrescrizioni" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PRESCRIZIONE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="datiLibertaAnticipata">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiLicenza">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="numeroMesiLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="numeroGiorniLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="numeroOreLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataInizioLicenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="oraInizioLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataFineLicenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="oraFineLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiIndultino">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="flagEsistenzaReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="flagEspiazioneReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrUfficioMagistratoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrComuneCssaComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="autoritaVigilante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiMisuraAlternativa">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrComuneCssaComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="servizioTerapeuticoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="descrComuneUssmComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiRevocaMisuraAlternativa">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrizioneDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="DataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="descrUfficioMagistratoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataDecorrenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="durataArresto" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="descrDiagnosiPsichiatrica" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="datiEstinzionePena">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiEstinzionePenaLibCondizionale">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiConcessioneRinvio">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiProrogaDetenDomicSpeciale">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiProrogaDetenDomic">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiOrdinanzaSospesa">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiReclamoPermesso">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="giorniPermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="orePermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiReclamiCEDU">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiScomputo">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="numeroGiorni" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiOrdinanzaReclamata">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiTipoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiRevocaLibertaAnticipata">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiRevocaLiberazioneAnticipata">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiSopravvenienzaNuovoTitolo">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="codiTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="servizioTerapeuticoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiRicoveri">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiRevoca">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="tipoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="annoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="numeroOrdinanza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="dataDepositoCancelleria" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiSanzioneSostitutiva">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="desUfficioCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiRicoveriOssPsich">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiEstinzioneSanzSost">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiModificaPermanSanziSost">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiSospEsecSanzSost">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="codTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataSospensione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="durataSospensione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="dataScadenzaSospensioneSS" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="giorniRecuperoSS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="durataSanzSostEspiata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="durataSanzSostiResiduaEspiare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiConversioneSanzSost">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="durataSanzSostEspiata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="durataSanzSostiResiduaEspiare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="durataArrestoRev" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datiRinvioSanzSost">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="durataSospensione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="elencoConversioniPecuniarie" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}CONVERSIONE_PENE_PECUNIARIE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoDestinatariRimessionAtti" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DESTINATARIO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="datiEsecuzDomicilio">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="codTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="flagEsistenzaReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="flagEspiazioneReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrComuneCssaComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="autoritaVigilante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="descTipoControlloEsecuzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoDestinatari" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DESTINATARIO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}ERRORE"/>
 *         &lt;element name="datiFascicoloOrigine">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="annoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="numeroProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrComuneUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="oggettoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="nomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="cognomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataNascita" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiceProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiceStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataCameraConsiglio" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *                   &lt;element name="flagRinviata" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "datiriepilogoprocedimento",
    "datiordinanza",
    "descrFormaMisuraMA",
    "descrComunitaMA",
    "descrFormaMisuraMS",
    "descrComunitaMS",
    "elencoMisureSicurezza",
    "datiEsecuzioneMisureSicurezzaAttuali",
    "datiEsecuzioneMisureSicurezzaPrecedenti",
    "elencoMisureRideterminate",
    "elencoPrescrizioni",
    "datiLibertaAnticipata",
    "datiLicenza",
    "datiIndultino",
    "datiMisuraAlternativa",
    "datiRevocaMisuraAlternativa",
    "descrDiagnosiPsichiatrica",
    "datiEstinzionePena",
    "datiEstinzionePenaLibCondizionale",
    "datiConcessioneRinvio",
    "datiProrogaDetenDomicSpeciale",
    "datiProrogaDetenDomic",
    "datiOrdinanzaSospesa",
    "datiReclamoPermesso",
    "datiReclamiCEDU",
    "datiScomputo",
    "datiOrdinanzaReclamata",
    "datiRevocaLibertaAnticipata",
    "datiRevocaLiberazioneAnticipata",
    "datiSopravvenienzaNuovoTitolo",
    "datiRicoveri",
    "datiRevoca",
    "datiSanzioneSostitutiva",
    "datiRicoveriOssPsich",
    "datiEstinzioneSanzSost",
    "datiModificaPermanSanziSost",
    "datiSospEsecSanzSost",
    "datiConversioneSanzSost",
    "datiRinvioSanzSost",
    "elencoConversioniPecuniarie",
    "elencoDestinatariRimessionAtti",
    "datiEsecuzDomicilio",
    "descTipoControlloEsecuzione",
    "elencoDestinatari",
    "errore",
    "datiFascicoloOrigine"
})
@XmlRootElement(name = "OUTPUT_DETTAGLIO_ORDINANZA")
public class OUTPUTDETTAGLIOORDINANZA {

    @XmlElement(name = "DATI_RIEPILOGO_PROCEDIMENTO", required = true)
    protected DATIRIEPILOGOPROCEDIMENTO datiriepilogoprocedimento;
    @XmlElement(name = "DATI_ORDINANZA", required = true)
    protected DATIORDINANZA datiordinanza;
    @XmlElement(required = true, nillable = true)
    protected String descrFormaMisuraMA;
    @XmlElement(required = true, nillable = true)
    protected String descrComunitaMA;
    @XmlElement(required = true, nillable = true)
    protected String descrFormaMisuraMS;
    @XmlElement(required = true, nillable = true)
    protected String descrComunitaMS;
    @XmlElement(required = true)
    protected List<MISURASICUREZZATYPE> elencoMisureSicurezza;
    @XmlElement(required = true)
    protected MISURASICUREZZATYPE datiEsecuzioneMisureSicurezzaAttuali;
    @XmlElement(required = true)
    protected MISURASICUREZZATYPE datiEsecuzioneMisureSicurezzaPrecedenti;
    @XmlElement(required = true)
    protected List<MISURASICUREZZATYPE> elencoMisureRideterminate;
    @XmlElement(required = true)
    protected List<PRESCRIZIONETYPE> elencoPrescrizioni;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiLibertaAnticipata datiLibertaAnticipata;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiLicenza datiLicenza;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiIndultino datiIndultino;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiMisuraAlternativa datiMisuraAlternativa;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiRevocaMisuraAlternativa datiRevocaMisuraAlternativa;
    @XmlElement(required = true)
    protected String descrDiagnosiPsichiatrica;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePena datiEstinzionePena;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePenaLibCondizionale datiEstinzionePenaLibCondizionale;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiConcessioneRinvio datiConcessioneRinvio;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomicSpeciale datiProrogaDetenDomicSpeciale;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomic datiProrogaDetenDomic;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaSospesa datiOrdinanzaSospesa;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiReclamoPermesso datiReclamoPermesso;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiReclamiCEDU datiReclamiCEDU;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiScomputo datiScomputo;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaReclamata datiOrdinanzaReclamata;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiRevocaLibertaAnticipata datiRevocaLibertaAnticipata;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiRevocaLiberazioneAnticipata datiRevocaLiberazioneAnticipata;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiSopravvenienzaNuovoTitolo datiSopravvenienzaNuovoTitolo;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiRicoveri datiRicoveri;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiRevoca datiRevoca;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiSanzioneSostitutiva datiSanzioneSostitutiva;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiRicoveriOssPsich datiRicoveriOssPsich;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiEstinzioneSanzSost datiEstinzioneSanzSost;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiModificaPermanSanziSost datiModificaPermanSanziSost;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiSospEsecSanzSost datiSospEsecSanzSost;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiConversioneSanzSost datiConversioneSanzSost;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiRinvioSanzSost datiRinvioSanzSost;
    @XmlElement(required = true)
    protected List<CONVERSIONEPENEPECUNIARIETYPE> elencoConversioniPecuniarie;
    @XmlElement(required = true)
    protected List<DESTINATARIOTYPE> elencoDestinatariRimessionAtti;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiEsecuzDomicilio datiEsecuzDomicilio;
    @XmlElement(required = true)
    protected String descTipoControlloEsecuzione;
    @XmlElement(required = true)
    protected List<DESTINATARIOTYPE> elencoDestinatari;
    @XmlElement(name = "ERRORE", required = true)
    protected ERRORE errore;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIOORDINANZA.DatiFascicoloOrigine datiFascicoloOrigine;

    /**
     * Recupera il valore della proprietà datiriepilogoprocedimento.
     * 
     * @return
     *     possible object is
     *     {@link DATIRIEPILOGOPROCEDIMENTO }
     *     
     */
    public DATIRIEPILOGOPROCEDIMENTO getDATIRIEPILOGOPROCEDIMENTO() {
        return datiriepilogoprocedimento;
    }

    /**
     * Imposta il valore della proprietà datiriepilogoprocedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIRIEPILOGOPROCEDIMENTO }
     *     
     */
    public void setDATIRIEPILOGOPROCEDIMENTO(DATIRIEPILOGOPROCEDIMENTO value) {
        this.datiriepilogoprocedimento = value;
    }

    /**
     * Recupera il valore della proprietà datiordinanza.
     * 
     * @return
     *     possible object is
     *     {@link DATIORDINANZA }
     *     
     */
    public DATIORDINANZA getDATIORDINANZA() {
        return datiordinanza;
    }

    /**
     * Imposta il valore della proprietà datiordinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIORDINANZA }
     *     
     */
    public void setDATIORDINANZA(DATIORDINANZA value) {
        this.datiordinanza = value;
    }

    /**
     * Recupera il valore della proprietà descrFormaMisuraMA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrFormaMisuraMA() {
        return descrFormaMisuraMA;
    }

    /**
     * Imposta il valore della proprietà descrFormaMisuraMA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrFormaMisuraMA(String value) {
        this.descrFormaMisuraMA = value;
    }

    /**
     * Recupera il valore della proprietà descrComunitaMA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComunitaMA() {
        return descrComunitaMA;
    }

    /**
     * Imposta il valore della proprietà descrComunitaMA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComunitaMA(String value) {
        this.descrComunitaMA = value;
    }

    /**
     * Recupera il valore della proprietà descrFormaMisuraMS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrFormaMisuraMS() {
        return descrFormaMisuraMS;
    }

    /**
     * Imposta il valore della proprietà descrFormaMisuraMS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrFormaMisuraMS(String value) {
        this.descrFormaMisuraMS = value;
    }

    /**
     * Recupera il valore della proprietà descrComunitaMS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComunitaMS() {
        return descrComunitaMS;
    }

    /**
     * Imposta il valore della proprietà descrComunitaMS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComunitaMS(String value) {
        this.descrComunitaMS = value;
    }

    /**
     * Gets the value of the elencoMisureSicurezza property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoMisureSicurezza property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoMisureSicurezza().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MISURASICUREZZATYPE }
     * 
     * 
     */
    public List<MISURASICUREZZATYPE> getElencoMisureSicurezza() {
        if (elencoMisureSicurezza == null) {
            elencoMisureSicurezza = new ArrayList<MISURASICUREZZATYPE>();
        }
        return this.elencoMisureSicurezza;
    }

    /**
     * Recupera il valore della proprietà datiEsecuzioneMisureSicurezzaAttuali.
     * 
     * @return
     *     possible object is
     *     {@link MISURASICUREZZATYPE }
     *     
     */
    public MISURASICUREZZATYPE getDatiEsecuzioneMisureSicurezzaAttuali() {
        return datiEsecuzioneMisureSicurezzaAttuali;
    }

    /**
     * Imposta il valore della proprietà datiEsecuzioneMisureSicurezzaAttuali.
     * 
     * @param value
     *     allowed object is
     *     {@link MISURASICUREZZATYPE }
     *     
     */
    public void setDatiEsecuzioneMisureSicurezzaAttuali(MISURASICUREZZATYPE value) {
        this.datiEsecuzioneMisureSicurezzaAttuali = value;
    }

    /**
     * Recupera il valore della proprietà datiEsecuzioneMisureSicurezzaPrecedenti.
     * 
     * @return
     *     possible object is
     *     {@link MISURASICUREZZATYPE }
     *     
     */
    public MISURASICUREZZATYPE getDatiEsecuzioneMisureSicurezzaPrecedenti() {
        return datiEsecuzioneMisureSicurezzaPrecedenti;
    }

    /**
     * Imposta il valore della proprietà datiEsecuzioneMisureSicurezzaPrecedenti.
     * 
     * @param value
     *     allowed object is
     *     {@link MISURASICUREZZATYPE }
     *     
     */
    public void setDatiEsecuzioneMisureSicurezzaPrecedenti(MISURASICUREZZATYPE value) {
        this.datiEsecuzioneMisureSicurezzaPrecedenti = value;
    }

    /**
     * Gets the value of the elencoMisureRideterminate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoMisureRideterminate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoMisureRideterminate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MISURASICUREZZATYPE }
     * 
     * 
     */
    public List<MISURASICUREZZATYPE> getElencoMisureRideterminate() {
        if (elencoMisureRideterminate == null) {
            elencoMisureRideterminate = new ArrayList<MISURASICUREZZATYPE>();
        }
        return this.elencoMisureRideterminate;
    }

    /**
     * Gets the value of the elencoPrescrizioni property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoPrescrizioni property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoPrescrizioni().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRESCRIZIONETYPE }
     * 
     * 
     */
    public List<PRESCRIZIONETYPE> getElencoPrescrizioni() {
        if (elencoPrescrizioni == null) {
            elencoPrescrizioni = new ArrayList<PRESCRIZIONETYPE>();
        }
        return this.elencoPrescrizioni;
    }

    /**
     * Recupera il valore della proprietà datiLibertaAnticipata.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiLibertaAnticipata }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiLibertaAnticipata getDatiLibertaAnticipata() {
        return datiLibertaAnticipata;
    }

    /**
     * Imposta il valore della proprietà datiLibertaAnticipata.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiLibertaAnticipata }
     *     
     */
    public void setDatiLibertaAnticipata(OUTPUTDETTAGLIOORDINANZA.DatiLibertaAnticipata value) {
        this.datiLibertaAnticipata = value;
    }

    /**
     * Recupera il valore della proprietà datiLicenza.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiLicenza }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiLicenza getDatiLicenza() {
        return datiLicenza;
    }

    /**
     * Imposta il valore della proprietà datiLicenza.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiLicenza }
     *     
     */
    public void setDatiLicenza(OUTPUTDETTAGLIOORDINANZA.DatiLicenza value) {
        this.datiLicenza = value;
    }

    /**
     * Recupera il valore della proprietà datiIndultino.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiIndultino }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiIndultino getDatiIndultino() {
        return datiIndultino;
    }

    /**
     * Imposta il valore della proprietà datiIndultino.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiIndultino }
     *     
     */
    public void setDatiIndultino(OUTPUTDETTAGLIOORDINANZA.DatiIndultino value) {
        this.datiIndultino = value;
    }

    /**
     * Recupera il valore della proprietà datiMisuraAlternativa.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiMisuraAlternativa }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiMisuraAlternativa getDatiMisuraAlternativa() {
        return datiMisuraAlternativa;
    }

    /**
     * Imposta il valore della proprietà datiMisuraAlternativa.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiMisuraAlternativa }
     *     
     */
    public void setDatiMisuraAlternativa(OUTPUTDETTAGLIOORDINANZA.DatiMisuraAlternativa value) {
        this.datiMisuraAlternativa = value;
    }

    /**
     * Recupera il valore della proprietà datiRevocaMisuraAlternativa.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevocaMisuraAlternativa }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiRevocaMisuraAlternativa getDatiRevocaMisuraAlternativa() {
        return datiRevocaMisuraAlternativa;
    }

    /**
     * Imposta il valore della proprietà datiRevocaMisuraAlternativa.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevocaMisuraAlternativa }
     *     
     */
    public void setDatiRevocaMisuraAlternativa(OUTPUTDETTAGLIOORDINANZA.DatiRevocaMisuraAlternativa value) {
        this.datiRevocaMisuraAlternativa = value;
    }

    /**
     * Recupera il valore della proprietà descrDiagnosiPsichiatrica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrDiagnosiPsichiatrica() {
        return descrDiagnosiPsichiatrica;
    }

    /**
     * Imposta il valore della proprietà descrDiagnosiPsichiatrica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrDiagnosiPsichiatrica(String value) {
        this.descrDiagnosiPsichiatrica = value;
    }

    /**
     * Recupera il valore della proprietà datiEstinzionePena.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePena }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePena getDatiEstinzionePena() {
        return datiEstinzionePena;
    }

    /**
     * Imposta il valore della proprietà datiEstinzionePena.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePena }
     *     
     */
    public void setDatiEstinzionePena(OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePena value) {
        this.datiEstinzionePena = value;
    }

    /**
     * Recupera il valore della proprietà datiEstinzionePenaLibCondizionale.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePenaLibCondizionale }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePenaLibCondizionale getDatiEstinzionePenaLibCondizionale() {
        return datiEstinzionePenaLibCondizionale;
    }

    /**
     * Imposta il valore della proprietà datiEstinzionePenaLibCondizionale.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePenaLibCondizionale }
     *     
     */
    public void setDatiEstinzionePenaLibCondizionale(OUTPUTDETTAGLIOORDINANZA.DatiEstinzionePenaLibCondizionale value) {
        this.datiEstinzionePenaLibCondizionale = value;
    }

    /**
     * Recupera il valore della proprietà datiConcessioneRinvio.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiConcessioneRinvio }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiConcessioneRinvio getDatiConcessioneRinvio() {
        return datiConcessioneRinvio;
    }

    /**
     * Imposta il valore della proprietà datiConcessioneRinvio.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiConcessioneRinvio }
     *     
     */
    public void setDatiConcessioneRinvio(OUTPUTDETTAGLIOORDINANZA.DatiConcessioneRinvio value) {
        this.datiConcessioneRinvio = value;
    }

    /**
     * Recupera il valore della proprietà datiProrogaDetenDomicSpeciale.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomicSpeciale }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomicSpeciale getDatiProrogaDetenDomicSpeciale() {
        return datiProrogaDetenDomicSpeciale;
    }

    /**
     * Imposta il valore della proprietà datiProrogaDetenDomicSpeciale.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomicSpeciale }
     *     
     */
    public void setDatiProrogaDetenDomicSpeciale(OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomicSpeciale value) {
        this.datiProrogaDetenDomicSpeciale = value;
    }

    /**
     * Recupera il valore della proprietà datiProrogaDetenDomic.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomic }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomic getDatiProrogaDetenDomic() {
        return datiProrogaDetenDomic;
    }

    /**
     * Imposta il valore della proprietà datiProrogaDetenDomic.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomic }
     *     
     */
    public void setDatiProrogaDetenDomic(OUTPUTDETTAGLIOORDINANZA.DatiProrogaDetenDomic value) {
        this.datiProrogaDetenDomic = value;
    }

    /**
     * Recupera il valore della proprietà datiOrdinanzaSospesa.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaSospesa }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaSospesa getDatiOrdinanzaSospesa() {
        return datiOrdinanzaSospesa;
    }

    /**
     * Imposta il valore della proprietà datiOrdinanzaSospesa.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaSospesa }
     *     
     */
    public void setDatiOrdinanzaSospesa(OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaSospesa value) {
        this.datiOrdinanzaSospesa = value;
    }

    /**
     * Recupera il valore della proprietà datiReclamoPermesso.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiReclamoPermesso }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiReclamoPermesso getDatiReclamoPermesso() {
        return datiReclamoPermesso;
    }

    /**
     * Imposta il valore della proprietà datiReclamoPermesso.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiReclamoPermesso }
     *     
     */
    public void setDatiReclamoPermesso(OUTPUTDETTAGLIOORDINANZA.DatiReclamoPermesso value) {
        this.datiReclamoPermesso = value;
    }

    /**
     * Recupera il valore della proprietà datiReclamiCEDU.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiReclamiCEDU }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiReclamiCEDU getDatiReclamiCEDU() {
        return datiReclamiCEDU;
    }

    /**
     * Imposta il valore della proprietà datiReclamiCEDU.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiReclamiCEDU }
     *     
     */
    public void setDatiReclamiCEDU(OUTPUTDETTAGLIOORDINANZA.DatiReclamiCEDU value) {
        this.datiReclamiCEDU = value;
    }

    /**
     * Recupera il valore della proprietà datiScomputo.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiScomputo }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiScomputo getDatiScomputo() {
        return datiScomputo;
    }

    /**
     * Imposta il valore della proprietà datiScomputo.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiScomputo }
     *     
     */
    public void setDatiScomputo(OUTPUTDETTAGLIOORDINANZA.DatiScomputo value) {
        this.datiScomputo = value;
    }

    /**
     * Recupera il valore della proprietà datiOrdinanzaReclamata.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaReclamata }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaReclamata getDatiOrdinanzaReclamata() {
        return datiOrdinanzaReclamata;
    }

    /**
     * Imposta il valore della proprietà datiOrdinanzaReclamata.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaReclamata }
     *     
     */
    public void setDatiOrdinanzaReclamata(OUTPUTDETTAGLIOORDINANZA.DatiOrdinanzaReclamata value) {
        this.datiOrdinanzaReclamata = value;
    }

    /**
     * Recupera il valore della proprietà datiRevocaLibertaAnticipata.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevocaLibertaAnticipata }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiRevocaLibertaAnticipata getDatiRevocaLibertaAnticipata() {
        return datiRevocaLibertaAnticipata;
    }

    /**
     * Imposta il valore della proprietà datiRevocaLibertaAnticipata.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevocaLibertaAnticipata }
     *     
     */
    public void setDatiRevocaLibertaAnticipata(OUTPUTDETTAGLIOORDINANZA.DatiRevocaLibertaAnticipata value) {
        this.datiRevocaLibertaAnticipata = value;
    }

    /**
     * Recupera il valore della proprietà datiRevocaLiberazioneAnticipata.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevocaLiberazioneAnticipata }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiRevocaLiberazioneAnticipata getDatiRevocaLiberazioneAnticipata() {
        return datiRevocaLiberazioneAnticipata;
    }

    /**
     * Imposta il valore della proprietà datiRevocaLiberazioneAnticipata.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevocaLiberazioneAnticipata }
     *     
     */
    public void setDatiRevocaLiberazioneAnticipata(OUTPUTDETTAGLIOORDINANZA.DatiRevocaLiberazioneAnticipata value) {
        this.datiRevocaLiberazioneAnticipata = value;
    }

    /**
     * Recupera il valore della proprietà datiSopravvenienzaNuovoTitolo.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiSopravvenienzaNuovoTitolo }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiSopravvenienzaNuovoTitolo getDatiSopravvenienzaNuovoTitolo() {
        return datiSopravvenienzaNuovoTitolo;
    }

    /**
     * Imposta il valore della proprietà datiSopravvenienzaNuovoTitolo.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiSopravvenienzaNuovoTitolo }
     *     
     */
    public void setDatiSopravvenienzaNuovoTitolo(OUTPUTDETTAGLIOORDINANZA.DatiSopravvenienzaNuovoTitolo value) {
        this.datiSopravvenienzaNuovoTitolo = value;
    }

    /**
     * Recupera il valore della proprietà datiRicoveri.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRicoveri }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiRicoveri getDatiRicoveri() {
        return datiRicoveri;
    }

    /**
     * Imposta il valore della proprietà datiRicoveri.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRicoveri }
     *     
     */
    public void setDatiRicoveri(OUTPUTDETTAGLIOORDINANZA.DatiRicoveri value) {
        this.datiRicoveri = value;
    }

    /**
     * Recupera il valore della proprietà datiRevoca.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevoca }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiRevoca getDatiRevoca() {
        return datiRevoca;
    }

    /**
     * Imposta il valore della proprietà datiRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRevoca }
     *     
     */
    public void setDatiRevoca(OUTPUTDETTAGLIOORDINANZA.DatiRevoca value) {
        this.datiRevoca = value;
    }

    /**
     * Recupera il valore della proprietà datiSanzioneSostitutiva.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiSanzioneSostitutiva }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiSanzioneSostitutiva getDatiSanzioneSostitutiva() {
        return datiSanzioneSostitutiva;
    }

    /**
     * Imposta il valore della proprietà datiSanzioneSostitutiva.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiSanzioneSostitutiva }
     *     
     */
    public void setDatiSanzioneSostitutiva(OUTPUTDETTAGLIOORDINANZA.DatiSanzioneSostitutiva value) {
        this.datiSanzioneSostitutiva = value;
    }

    /**
     * Recupera il valore della proprietà datiRicoveriOssPsich.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRicoveriOssPsich }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiRicoveriOssPsich getDatiRicoveriOssPsich() {
        return datiRicoveriOssPsich;
    }

    /**
     * Imposta il valore della proprietà datiRicoveriOssPsich.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRicoveriOssPsich }
     *     
     */
    public void setDatiRicoveriOssPsich(OUTPUTDETTAGLIOORDINANZA.DatiRicoveriOssPsich value) {
        this.datiRicoveriOssPsich = value;
    }

    /**
     * Recupera il valore della proprietà datiEstinzioneSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEstinzioneSanzSost }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiEstinzioneSanzSost getDatiEstinzioneSanzSost() {
        return datiEstinzioneSanzSost;
    }

    /**
     * Imposta il valore della proprietà datiEstinzioneSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEstinzioneSanzSost }
     *     
     */
    public void setDatiEstinzioneSanzSost(OUTPUTDETTAGLIOORDINANZA.DatiEstinzioneSanzSost value) {
        this.datiEstinzioneSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà datiModificaPermanSanziSost.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiModificaPermanSanziSost }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiModificaPermanSanziSost getDatiModificaPermanSanziSost() {
        return datiModificaPermanSanziSost;
    }

    /**
     * Imposta il valore della proprietà datiModificaPermanSanziSost.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiModificaPermanSanziSost }
     *     
     */
    public void setDatiModificaPermanSanziSost(OUTPUTDETTAGLIOORDINANZA.DatiModificaPermanSanziSost value) {
        this.datiModificaPermanSanziSost = value;
    }

    /**
     * Recupera il valore della proprietà datiSospEsecSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiSospEsecSanzSost }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiSospEsecSanzSost getDatiSospEsecSanzSost() {
        return datiSospEsecSanzSost;
    }

    /**
     * Imposta il valore della proprietà datiSospEsecSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiSospEsecSanzSost }
     *     
     */
    public void setDatiSospEsecSanzSost(OUTPUTDETTAGLIOORDINANZA.DatiSospEsecSanzSost value) {
        this.datiSospEsecSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà datiConversioneSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiConversioneSanzSost }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiConversioneSanzSost getDatiConversioneSanzSost() {
        return datiConversioneSanzSost;
    }

    /**
     * Imposta il valore della proprietà datiConversioneSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiConversioneSanzSost }
     *     
     */
    public void setDatiConversioneSanzSost(OUTPUTDETTAGLIOORDINANZA.DatiConversioneSanzSost value) {
        this.datiConversioneSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà datiRinvioSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRinvioSanzSost }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiRinvioSanzSost getDatiRinvioSanzSost() {
        return datiRinvioSanzSost;
    }

    /**
     * Imposta il valore della proprietà datiRinvioSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiRinvioSanzSost }
     *     
     */
    public void setDatiRinvioSanzSost(OUTPUTDETTAGLIOORDINANZA.DatiRinvioSanzSost value) {
        this.datiRinvioSanzSost = value;
    }

    /**
     * Gets the value of the elencoConversioniPecuniarie property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoConversioniPecuniarie property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoConversioniPecuniarie().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CONVERSIONEPENEPECUNIARIETYPE }
     * 
     * 
     */
    public List<CONVERSIONEPENEPECUNIARIETYPE> getElencoConversioniPecuniarie() {
        if (elencoConversioniPecuniarie == null) {
            elencoConversioniPecuniarie = new ArrayList<CONVERSIONEPENEPECUNIARIETYPE>();
        }
        return this.elencoConversioniPecuniarie;
    }

    /**
     * Gets the value of the elencoDestinatariRimessionAtti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoDestinatariRimessionAtti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoDestinatariRimessionAtti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DESTINATARIOTYPE }
     * 
     * 
     */
    public List<DESTINATARIOTYPE> getElencoDestinatariRimessionAtti() {
        if (elencoDestinatariRimessionAtti == null) {
            elencoDestinatariRimessionAtti = new ArrayList<DESTINATARIOTYPE>();
        }
        return this.elencoDestinatariRimessionAtti;
    }

    /**
     * Recupera il valore della proprietà datiEsecuzDomicilio.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEsecuzDomicilio }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiEsecuzDomicilio getDatiEsecuzDomicilio() {
        return datiEsecuzDomicilio;
    }

    /**
     * Imposta il valore della proprietà datiEsecuzDomicilio.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiEsecuzDomicilio }
     *     
     */
    public void setDatiEsecuzDomicilio(OUTPUTDETTAGLIOORDINANZA.DatiEsecuzDomicilio value) {
        this.datiEsecuzDomicilio = value;
    }

    /**
     * Recupera il valore della proprietà descTipoControlloEsecuzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescTipoControlloEsecuzione() {
        return descTipoControlloEsecuzione;
    }

    /**
     * Imposta il valore della proprietà descTipoControlloEsecuzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescTipoControlloEsecuzione(String value) {
        this.descTipoControlloEsecuzione = value;
    }

    /**
     * Gets the value of the elencoDestinatari property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoDestinatari property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoDestinatari().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DESTINATARIOTYPE }
     * 
     * 
     */
    public List<DESTINATARIOTYPE> getElencoDestinatari() {
        if (elencoDestinatari == null) {
            elencoDestinatari = new ArrayList<DESTINATARIOTYPE>();
        }
        return this.elencoDestinatari;
    }

    /**
     * Recupera il valore della proprietà errore.
     * 
     * @return
     *     possible object is
     *     {@link ERRORE }
     *     
     */
    public ERRORE getERRORE() {
        return errore;
    }

    /**
     * Imposta il valore della proprietà errore.
     * 
     * @param value
     *     allowed object is
     *     {@link ERRORE }
     *     
     */
    public void setERRORE(ERRORE value) {
        this.errore = value;
    }

    /**
     * Recupera il valore della proprietà datiFascicoloOrigine.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiFascicoloOrigine }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA.DatiFascicoloOrigine getDatiFascicoloOrigine() {
        return datiFascicoloOrigine;
    }

    /**
     * Imposta il valore della proprietà datiFascicoloOrigine.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA.DatiFascicoloOrigine }
     *     
     */
    public void setDatiFascicoloOrigine(OUTPUTDETTAGLIOORDINANZA.DatiFascicoloOrigine value) {
        this.datiFascicoloOrigine = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrTipoUfficio",
        "descrDecisione",
        "dataTrasmissione",
        "descrUfficioMagistratoCompetente",
        "dataInizioPeriodo",
        "dataFineMisura",
        "durataDetenzioneDomiciliare"
    })
    public static class DatiConcessioneRinvio {

        @XmlElement(required = true)
        protected String descrTipoUfficio;
        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataTrasmissione;
        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected DATATYPE dataInizioPeriodo;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;

        /**
         * Recupera il valore della proprietà descrTipoUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrTipoUfficio() {
            return descrTipoUfficio;
        }

        /**
         * Imposta il valore della proprietà descrTipoUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrTipoUfficio(String value) {
            this.descrTipoUfficio = value;
        }

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataTrasmissione.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataTrasmissione() {
            return dataTrasmissione;
        }

        /**
         * Imposta il valore della proprietà dataTrasmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataTrasmissione(DATATYPE value) {
            this.dataTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà dataInizioPeriodo.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataInizioPeriodo() {
            return dataInizioPeriodo;
        }

        /**
         * Imposta il valore della proprietà dataInizioPeriodo.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataInizioPeriodo(DATATYPE value) {
            this.dataInizioPeriodo = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="durataSanzSostEspiata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="durataSanzSostiResiduaEspiare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="durataArrestoRev" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "durataSanzSostEspiata",
        "durataSanzSostiResiduaEspiare",
        "durataDetenzioneDomiciliare",
        "durataArrestoRev"
    })
    public static class DatiConversioneSanzSost {

        @XmlElement(required = true)
        protected DURATATYPE durataSanzSostEspiata;
        @XmlElement(required = true)
        protected DURATATYPE durataSanzSostiResiduaEspiare;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;
        @XmlElement(required = true)
        protected DURATATYPE durataArrestoRev;

        /**
         * Recupera il valore della proprietà durataSanzSostEspiata.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataSanzSostEspiata() {
            return durataSanzSostEspiata;
        }

        /**
         * Imposta il valore della proprietà durataSanzSostEspiata.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataSanzSostEspiata(DURATATYPE value) {
            this.durataSanzSostEspiata = value;
        }

        /**
         * Recupera il valore della proprietà durataSanzSostiResiduaEspiare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataSanzSostiResiduaEspiare() {
            return durataSanzSostiResiduaEspiare;
        }

        /**
         * Imposta il valore della proprietà durataSanzSostiResiduaEspiare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataSanzSostiResiduaEspiare(DURATATYPE value) {
            this.durataSanzSostiResiduaEspiare = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

        /**
         * Recupera il valore della proprietà durataArrestoRev.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataArrestoRev() {
            return durataArrestoRev;
        }

        /**
         * Imposta il valore della proprietà durataArrestoRev.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataArrestoRev(DURATATYPE value) {
            this.durataArrestoRev = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="codTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="flagEsistenzaReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="flagEspiazioneReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrComuneCssaComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="autoritaVigilante" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codTipoUfficio",
        "flagEsistenzaReatoOstativo",
        "flagEspiazioneReatoOstativo",
        "dataFineMisura",
        "luogoSvolgimentoProva",
        "descrUfficioMagistratoCompetente",
        "descrComuneCssaComp",
        "autoritaVigilante"
    })
    public static class DatiEsecuzDomicilio {

        @XmlElement(required = true)
        protected String codTipoUfficio;
        @XmlElement(required = true)
        protected String flagEsistenzaReatoOstativo;
        @XmlElement(required = true)
        protected String flagEspiazioneReatoOstativo;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;
        @XmlElement(required = true)
        protected String luogoSvolgimentoProva;
        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String descrComuneCssaComp;
        @XmlElement(required = true)
        protected String autoritaVigilante;

        /**
         * Recupera il valore della proprietà codTipoUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodTipoUfficio() {
            return codTipoUfficio;
        }

        /**
         * Imposta il valore della proprietà codTipoUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodTipoUfficio(String value) {
            this.codTipoUfficio = value;
        }

        /**
         * Recupera il valore della proprietà flagEsistenzaReatoOstativo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFlagEsistenzaReatoOstativo() {
            return flagEsistenzaReatoOstativo;
        }

        /**
         * Imposta il valore della proprietà flagEsistenzaReatoOstativo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFlagEsistenzaReatoOstativo(String value) {
            this.flagEsistenzaReatoOstativo = value;
        }

        /**
         * Recupera il valore della proprietà flagEspiazioneReatoOstativo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFlagEspiazioneReatoOstativo() {
            return flagEspiazioneReatoOstativo;
        }

        /**
         * Imposta il valore della proprietà flagEspiazioneReatoOstativo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFlagEspiazioneReatoOstativo(String value) {
            this.flagEspiazioneReatoOstativo = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

        /**
         * Recupera il valore della proprietà luogoSvolgimentoProva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoSvolgimentoProva() {
            return luogoSvolgimentoProva;
        }

        /**
         * Imposta il valore della proprietà luogoSvolgimentoProva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoSvolgimentoProva(String value) {
            this.luogoSvolgimentoProva = value;
        }

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà descrComuneCssaComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrComuneCssaComp() {
            return descrComuneCssaComp;
        }

        /**
         * Imposta il valore della proprietà descrComuneCssaComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrComuneCssaComp(String value) {
            this.descrComuneCssaComp = value;
        }

        /**
         * Recupera il valore della proprietà autoritaVigilante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAutoritaVigilante() {
            return autoritaVigilante;
        }

        /**
         * Imposta il valore della proprietà autoritaVigilante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAutoritaVigilante(String value) {
            this.autoritaVigilante = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "dataInizioPeriodo",
        "dataFineMisura",
        "durataDetenzioneDomiciliare"
    })
    public static class DatiEstinzionePena {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataInizioPeriodo;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataInizioPeriodo.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataInizioPeriodo() {
            return dataInizioPeriodo;
        }

        /**
         * Imposta il valore della proprietà dataInizioPeriodo.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataInizioPeriodo(DATATYPE value) {
            this.dataInizioPeriodo = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "dataInizioPeriodo"
    })
    public static class DatiEstinzionePenaLibCondizionale {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataInizioPeriodo;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataInizioPeriodo.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataInizioPeriodo() {
            return dataInizioPeriodo;
        }

        /**
         * Imposta il valore della proprietà dataInizioPeriodo.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataInizioPeriodo(DATATYPE value) {
            this.dataInizioPeriodo = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrUffTdsConcessoRiduzione",
        "codiNaturaProvvedimento"
    })
    public static class DatiEstinzioneSanzSost {

        @XmlElement(required = true)
        protected String descrUffTdsConcessoRiduzione;
        @XmlElement(required = true)
        protected String codiNaturaProvvedimento;

        /**
         * Recupera il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUffTdsConcessoRiduzione() {
            return descrUffTdsConcessoRiduzione;
        }

        /**
         * Imposta il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUffTdsConcessoRiduzione(String value) {
            this.descrUffTdsConcessoRiduzione = value;
        }

        /**
         * Recupera il valore della proprietà codiNaturaProvvedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiNaturaProvvedimento() {
            return codiNaturaProvvedimento;
        }

        /**
         * Imposta il valore della proprietà codiNaturaProvvedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiNaturaProvvedimento(String value) {
            this.codiNaturaProvvedimento = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="annoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="numeroProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrComuneUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="oggettoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="nomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="cognomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataNascita" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiceProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiceStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataCameraConsiglio" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="flagRinviata" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "annoProcedimentoSIUS",
        "numeroProcedimentoSIUS",
        "descrTipoUfficio",
        "descrComuneUfficio",
        "oggettoProcedimentoSIUS",
        "nomeSoggetto",
        "cognomeSoggetto",
        "sesso",
        "dataNascita",
        "descrComuneNascita",
        "codiceProvincia",
        "descrStatoNascita",
        "codiceStatoFascicolo",
        "descrStatoFascicolo",
        "dataCameraConsiglio",
        "flagRinviata"
    })
    public static class DatiFascicoloOrigine {

        @XmlElement(required = true)
        protected BigInteger annoProcedimentoSIUS;
        @XmlElement(required = true)
        protected BigInteger numeroProcedimentoSIUS;
        @XmlElement(required = true)
        protected String descrTipoUfficio;
        @XmlElement(required = true)
        protected String descrComuneUfficio;
        @XmlElement(required = true)
        protected String oggettoProcedimentoSIUS;
        @XmlElement(required = true)
        protected String nomeSoggetto;
        @XmlElement(required = true)
        protected String cognomeSoggetto;
        @XmlElement(required = true)
        protected String sesso;
        @XmlElement(required = true)
        protected DATATYPE dataNascita;
        @XmlElement(required = true)
        protected String descrComuneNascita;
        @XmlElement(required = true)
        protected String codiceProvincia;
        @XmlElement(required = true)
        protected String descrStatoNascita;
        @XmlElement(required = true)
        protected String codiceStatoFascicolo;
        @XmlElement(required = true)
        protected String descrStatoFascicolo;
        @XmlElement(required = true)
        protected DATATYPE dataCameraConsiglio;
        @XmlElement(required = true)
        protected String flagRinviata;

        /**
         * Recupera il valore della proprietà annoProcedimentoSIUS.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getAnnoProcedimentoSIUS() {
            return annoProcedimentoSIUS;
        }

        /**
         * Imposta il valore della proprietà annoProcedimentoSIUS.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setAnnoProcedimentoSIUS(BigInteger value) {
            this.annoProcedimentoSIUS = value;
        }

        /**
         * Recupera il valore della proprietà numeroProcedimentoSIUS.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getNumeroProcedimentoSIUS() {
            return numeroProcedimentoSIUS;
        }

        /**
         * Imposta il valore della proprietà numeroProcedimentoSIUS.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setNumeroProcedimentoSIUS(BigInteger value) {
            this.numeroProcedimentoSIUS = value;
        }

        /**
         * Recupera il valore della proprietà descrTipoUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrTipoUfficio() {
            return descrTipoUfficio;
        }

        /**
         * Imposta il valore della proprietà descrTipoUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrTipoUfficio(String value) {
            this.descrTipoUfficio = value;
        }

        /**
         * Recupera il valore della proprietà descrComuneUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrComuneUfficio() {
            return descrComuneUfficio;
        }

        /**
         * Imposta il valore della proprietà descrComuneUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrComuneUfficio(String value) {
            this.descrComuneUfficio = value;
        }

        /**
         * Recupera il valore della proprietà oggettoProcedimentoSIUS.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOggettoProcedimentoSIUS() {
            return oggettoProcedimentoSIUS;
        }

        /**
         * Imposta il valore della proprietà oggettoProcedimentoSIUS.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOggettoProcedimentoSIUS(String value) {
            this.oggettoProcedimentoSIUS = value;
        }

        /**
         * Recupera il valore della proprietà nomeSoggetto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNomeSoggetto() {
            return nomeSoggetto;
        }

        /**
         * Imposta il valore della proprietà nomeSoggetto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNomeSoggetto(String value) {
            this.nomeSoggetto = value;
        }

        /**
         * Recupera il valore della proprietà cognomeSoggetto.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCognomeSoggetto() {
            return cognomeSoggetto;
        }

        /**
         * Imposta il valore della proprietà cognomeSoggetto.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCognomeSoggetto(String value) {
            this.cognomeSoggetto = value;
        }

        /**
         * Recupera il valore della proprietà sesso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSesso() {
            return sesso;
        }

        /**
         * Imposta il valore della proprietà sesso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSesso(String value) {
            this.sesso = value;
        }

        /**
         * Recupera il valore della proprietà dataNascita.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataNascita() {
            return dataNascita;
        }

        /**
         * Imposta il valore della proprietà dataNascita.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataNascita(DATATYPE value) {
            this.dataNascita = value;
        }

        /**
         * Recupera il valore della proprietà descrComuneNascita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrComuneNascita() {
            return descrComuneNascita;
        }

        /**
         * Imposta il valore della proprietà descrComuneNascita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrComuneNascita(String value) {
            this.descrComuneNascita = value;
        }

        /**
         * Recupera il valore della proprietà codiceProvincia.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiceProvincia() {
            return codiceProvincia;
        }

        /**
         * Imposta il valore della proprietà codiceProvincia.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiceProvincia(String value) {
            this.codiceProvincia = value;
        }

        /**
         * Recupera il valore della proprietà descrStatoNascita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrStatoNascita() {
            return descrStatoNascita;
        }

        /**
         * Imposta il valore della proprietà descrStatoNascita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrStatoNascita(String value) {
            this.descrStatoNascita = value;
        }

        /**
         * Recupera il valore della proprietà codiceStatoFascicolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiceStatoFascicolo() {
            return codiceStatoFascicolo;
        }

        /**
         * Imposta il valore della proprietà codiceStatoFascicolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiceStatoFascicolo(String value) {
            this.codiceStatoFascicolo = value;
        }

        /**
         * Recupera il valore della proprietà descrStatoFascicolo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrStatoFascicolo() {
            return descrStatoFascicolo;
        }

        /**
         * Imposta il valore della proprietà descrStatoFascicolo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrStatoFascicolo(String value) {
            this.descrStatoFascicolo = value;
        }

        /**
         * Recupera il valore della proprietà dataCameraConsiglio.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataCameraConsiglio() {
            return dataCameraConsiglio;
        }

        /**
         * Imposta il valore della proprietà dataCameraConsiglio.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataCameraConsiglio(DATATYPE value) {
            this.dataCameraConsiglio = value;
        }

        /**
         * Recupera il valore della proprietà flagRinviata.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFlagRinviata() {
            return flagRinviata;
        }

        /**
         * Imposta il valore della proprietà flagRinviata.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFlagRinviata(String value) {
            this.flagRinviata = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="flagEsistenzaReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="flagEspiazioneReatoOstativo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrUfficioMagistratoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrComuneCssaComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="autoritaVigilante" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "flagEsistenzaReatoOstativo",
        "flagEspiazioneReatoOstativo",
        "dataFineMisura",
        "luogoSvolgimentoProva",
        "descrUfficioMagistratoComp",
        "descrComuneCssaComp",
        "autoritaVigilante"
    })
    public static class DatiIndultino {

        @XmlElement(required = true)
        protected String flagEsistenzaReatoOstativo;
        @XmlElement(required = true)
        protected String flagEspiazioneReatoOstativo;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;
        @XmlElement(required = true)
        protected String luogoSvolgimentoProva;
        @XmlElement(required = true)
        protected String descrUfficioMagistratoComp;
        @XmlElement(required = true)
        protected String descrComuneCssaComp;
        @XmlElement(required = true)
        protected String autoritaVigilante;

        /**
         * Recupera il valore della proprietà flagEsistenzaReatoOstativo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFlagEsistenzaReatoOstativo() {
            return flagEsistenzaReatoOstativo;
        }

        /**
         * Imposta il valore della proprietà flagEsistenzaReatoOstativo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFlagEsistenzaReatoOstativo(String value) {
            this.flagEsistenzaReatoOstativo = value;
        }

        /**
         * Recupera il valore della proprietà flagEspiazioneReatoOstativo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFlagEspiazioneReatoOstativo() {
            return flagEspiazioneReatoOstativo;
        }

        /**
         * Imposta il valore della proprietà flagEspiazioneReatoOstativo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFlagEspiazioneReatoOstativo(String value) {
            this.flagEspiazioneReatoOstativo = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

        /**
         * Recupera il valore della proprietà luogoSvolgimentoProva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoSvolgimentoProva() {
            return luogoSvolgimentoProva;
        }

        /**
         * Imposta il valore della proprietà luogoSvolgimentoProva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoSvolgimentoProva(String value) {
            this.luogoSvolgimentoProva = value;
        }

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoComp() {
            return descrUfficioMagistratoComp;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoComp(String value) {
            this.descrUfficioMagistratoComp = value;
        }

        /**
         * Recupera il valore della proprietà descrComuneCssaComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrComuneCssaComp() {
            return descrComuneCssaComp;
        }

        /**
         * Imposta il valore della proprietà descrComuneCssaComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrComuneCssaComp(String value) {
            this.descrComuneCssaComp = value;
        }

        /**
         * Recupera il valore della proprietà autoritaVigilante.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAutoritaVigilante() {
            return autoritaVigilante;
        }

        /**
         * Imposta il valore della proprietà autoritaVigilante.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAutoritaVigilante(String value) {
            this.autoritaVigilante = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrUfficioMagistratoCompetente",
        "descrDecisione",
        "licenzaPeriodiLibertaAnticipata"
    })
    public static class DatiLibertaAnticipata {

        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected List<PERIODILIBERTAANTICIPATATYPE> licenzaPeriodiLibertaAnticipata;

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Gets the value of the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLicenzaPeriodiLibertaAnticipata().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PERIODILIBERTAANTICIPATATYPE }
         * 
         * 
         */
        public List<PERIODILIBERTAANTICIPATATYPE> getLicenzaPeriodiLibertaAnticipata() {
            if (licenzaPeriodiLibertaAnticipata == null) {
                licenzaPeriodiLibertaAnticipata = new ArrayList<PERIODILIBERTAANTICIPATATYPE>();
            }
            return this.licenzaPeriodiLibertaAnticipata;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="numeroMesiLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="numeroGiorniLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="numeroOreLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataInizioLicenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="oraInizioLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataFineLicenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="oraFineLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "numeroMesiLicenza",
        "numeroGiorniLicenza",
        "numeroOreLicenza",
        "dataInizioLicenza",
        "oraInizioLicenza",
        "dataFineLicenza",
        "oraFineLicenza",
        "luogoSvolgimentoProva"
    })
    public static class DatiLicenza {

        @XmlElement(required = true)
        protected String numeroMesiLicenza;
        @XmlElement(required = true)
        protected String numeroGiorniLicenza;
        @XmlElement(required = true)
        protected String numeroOreLicenza;
        @XmlElement(required = true)
        protected DATATYPE dataInizioLicenza;
        @XmlElement(required = true)
        protected String oraInizioLicenza;
        @XmlElement(required = true)
        protected DATATYPE dataFineLicenza;
        @XmlElement(required = true)
        protected String oraFineLicenza;
        @XmlElement(required = true)
        protected String luogoSvolgimentoProva;

        /**
         * Recupera il valore della proprietà numeroMesiLicenza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroMesiLicenza() {
            return numeroMesiLicenza;
        }

        /**
         * Imposta il valore della proprietà numeroMesiLicenza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroMesiLicenza(String value) {
            this.numeroMesiLicenza = value;
        }

        /**
         * Recupera il valore della proprietà numeroGiorniLicenza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroGiorniLicenza() {
            return numeroGiorniLicenza;
        }

        /**
         * Imposta il valore della proprietà numeroGiorniLicenza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroGiorniLicenza(String value) {
            this.numeroGiorniLicenza = value;
        }

        /**
         * Recupera il valore della proprietà numeroOreLicenza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNumeroOreLicenza() {
            return numeroOreLicenza;
        }

        /**
         * Imposta il valore della proprietà numeroOreLicenza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNumeroOreLicenza(String value) {
            this.numeroOreLicenza = value;
        }

        /**
         * Recupera il valore della proprietà dataInizioLicenza.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataInizioLicenza() {
            return dataInizioLicenza;
        }

        /**
         * Imposta il valore della proprietà dataInizioLicenza.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataInizioLicenza(DATATYPE value) {
            this.dataInizioLicenza = value;
        }

        /**
         * Recupera il valore della proprietà oraInizioLicenza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraInizioLicenza() {
            return oraInizioLicenza;
        }

        /**
         * Imposta il valore della proprietà oraInizioLicenza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraInizioLicenza(String value) {
            this.oraInizioLicenza = value;
        }

        /**
         * Recupera il valore della proprietà dataFineLicenza.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineLicenza() {
            return dataFineLicenza;
        }

        /**
         * Imposta il valore della proprietà dataFineLicenza.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineLicenza(DATATYPE value) {
            this.dataFineLicenza = value;
        }

        /**
         * Recupera il valore della proprietà oraFineLicenza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOraFineLicenza() {
            return oraFineLicenza;
        }

        /**
         * Imposta il valore della proprietà oraFineLicenza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOraFineLicenza(String value) {
            this.oraFineLicenza = value;
        }

        /**
         * Recupera il valore della proprietà luogoSvolgimentoProva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoSvolgimentoProva() {
            return luogoSvolgimentoProva;
        }

        /**
         * Imposta il valore della proprietà luogoSvolgimentoProva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoSvolgimentoProva(String value) {
            this.luogoSvolgimentoProva = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrComuneCssaComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="servizioTerapeuticoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="descrComuneUssmComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrUfficioMagistratoCompetente",
        "descrDecisione",
        "luogoSvolgimentoProva",
        "descrComuneCssaComp",
        "servizioTerapeuticoComp",
        "dataFineMisura",
        "durataDetenzioneDomiciliare",
        "descrComuneUssmComp"
    })
    public static class DatiMisuraAlternativa {

        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected String luogoSvolgimentoProva;
        @XmlElement(required = true)
        protected String descrComuneCssaComp;
        @XmlElement(required = true)
        protected String servizioTerapeuticoComp;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;
        @XmlElement(required = true, nillable = true)
        protected String descrComuneUssmComp;

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà luogoSvolgimentoProva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoSvolgimentoProva() {
            return luogoSvolgimentoProva;
        }

        /**
         * Imposta il valore della proprietà luogoSvolgimentoProva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoSvolgimentoProva(String value) {
            this.luogoSvolgimentoProva = value;
        }

        /**
         * Recupera il valore della proprietà descrComuneCssaComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrComuneCssaComp() {
            return descrComuneCssaComp;
        }

        /**
         * Imposta il valore della proprietà descrComuneCssaComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrComuneCssaComp(String value) {
            this.descrComuneCssaComp = value;
        }

        /**
         * Recupera il valore della proprietà servizioTerapeuticoComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getServizioTerapeuticoComp() {
            return servizioTerapeuticoComp;
        }

        /**
         * Imposta il valore della proprietà servizioTerapeuticoComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setServizioTerapeuticoComp(String value) {
            this.servizioTerapeuticoComp = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

        /**
         * Recupera il valore della proprietà descrComuneUssmComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrComuneUssmComp() {
            return descrComuneUssmComp;
        }

        /**
         * Imposta il valore della proprietà descrComuneUssmComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrComuneUssmComp(String value) {
            this.descrComuneUssmComp = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "descrUfficioMagistratoCompetente",
        "codiNaturaProvvedimento"
    })
    public static class DatiModificaPermanSanziSost {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String codiNaturaProvvedimento;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà codiNaturaProvvedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiNaturaProvvedimento() {
            return codiNaturaProvvedimento;
        }

        /**
         * Imposta il valore della proprietà codiNaturaProvvedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiNaturaProvvedimento(String value) {
            this.codiNaturaProvvedimento = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiTipoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "codiTipoOrdinanza",
        "dataTrasmissione",
        "descrUfficioMagistratoCompetente"
    })
    public static class DatiOrdinanzaReclamata {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected String codiTipoOrdinanza;
        @XmlElement(required = true)
        protected DATATYPE dataTrasmissione;
        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà codiTipoOrdinanza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiTipoOrdinanza() {
            return codiTipoOrdinanza;
        }

        /**
         * Imposta il valore della proprietà codiTipoOrdinanza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiTipoOrdinanza(String value) {
            this.codiTipoOrdinanza = value;
        }

        /**
         * Recupera il valore della proprietà dataTrasmissione.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataTrasmissione() {
            return dataTrasmissione;
        }

        /**
         * Imposta il valore della proprietà dataTrasmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataTrasmissione(DATATYPE value) {
            this.dataTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "dataInizioPeriodo",
        "descrUffTdsConcessoRiduzione",
        "codiTipoUfficio"
    })
    public static class DatiOrdinanzaSospesa {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataInizioPeriodo;
        @XmlElement(required = true)
        protected String descrUffTdsConcessoRiduzione;
        @XmlElement(required = true)
        protected String codiTipoUfficio;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataInizioPeriodo.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataInizioPeriodo() {
            return dataInizioPeriodo;
        }

        /**
         * Imposta il valore della proprietà dataInizioPeriodo.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataInizioPeriodo(DATATYPE value) {
            this.dataInizioPeriodo = value;
        }

        /**
         * Recupera il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUffTdsConcessoRiduzione() {
            return descrUffTdsConcessoRiduzione;
        }

        /**
         * Imposta il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUffTdsConcessoRiduzione(String value) {
            this.descrUffTdsConcessoRiduzione = value;
        }

        /**
         * Recupera il valore della proprietà codiTipoUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiTipoUfficio() {
            return codiTipoUfficio;
        }

        /**
         * Imposta il valore della proprietà codiTipoUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiTipoUfficio(String value) {
            this.codiTipoUfficio = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "dataFineMisura",
        "durataDetenzioneDomiciliare"
    })
    public static class DatiProrogaDetenDomic {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "dataFineMisura"
    })
    public static class DatiProrogaDetenDomicSpeciale {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "licenzaPeriodiLibertaAnticipata"
    })
    public static class DatiReclamiCEDU {

        @XmlElement(required = true)
        protected List<PERIODILIBERTAANTICIPATATYPE> licenzaPeriodiLibertaAnticipata;

        /**
         * Gets the value of the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLicenzaPeriodiLibertaAnticipata().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PERIODILIBERTAANTICIPATATYPE }
         * 
         * 
         */
        public List<PERIODILIBERTAANTICIPATATYPE> getLicenzaPeriodiLibertaAnticipata() {
            if (licenzaPeriodiLibertaAnticipata == null) {
                licenzaPeriodiLibertaAnticipata = new ArrayList<PERIODILIBERTAANTICIPATATYPE>();
            }
            return this.licenzaPeriodiLibertaAnticipata;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="giorniPermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="orePermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "dataTrasmissione",
        "descrUfficioMagistratoCompetente",
        "giorniPermesso",
        "orePermesso"
    })
    public static class DatiReclamoPermesso {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataTrasmissione;
        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String giorniPermesso;
        @XmlElement(required = true)
        protected String orePermesso;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataTrasmissione.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataTrasmissione() {
            return dataTrasmissione;
        }

        /**
         * Imposta il valore della proprietà dataTrasmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataTrasmissione(DATATYPE value) {
            this.dataTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà giorniPermesso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGiorniPermesso() {
            return giorniPermesso;
        }

        /**
         * Imposta il valore della proprietà giorniPermesso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGiorniPermesso(String value) {
            this.giorniPermesso = value;
        }

        /**
         * Recupera il valore della proprietà orePermesso.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOrePermesso() {
            return orePermesso;
        }

        /**
         * Imposta il valore della proprietà orePermesso.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOrePermesso(String value) {
            this.orePermesso = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="tipoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="annoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="numeroOrdinanza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="dataDepositoCancelleria" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tipoOrdinanza",
        "dataEmissione",
        "annoOrdinanza",
        "numeroOrdinanza",
        "dataDepositoCancelleria"
    })
    public static class DatiRevoca {

        @XmlElement(required = true)
        protected String tipoOrdinanza;
        @XmlElement(required = true)
        protected DATATYPE dataEmissione;
        @XmlElement(required = true)
        protected BigInteger annoOrdinanza;
        @XmlElement(required = true)
        protected BigInteger numeroOrdinanza;
        @XmlElement(required = true)
        protected DATATYPE dataDepositoCancelleria;

        /**
         * Recupera il valore della proprietà tipoOrdinanza.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoOrdinanza() {
            return tipoOrdinanza;
        }

        /**
         * Imposta il valore della proprietà tipoOrdinanza.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoOrdinanza(String value) {
            this.tipoOrdinanza = value;
        }

        /**
         * Recupera il valore della proprietà dataEmissione.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataEmissione() {
            return dataEmissione;
        }

        /**
         * Imposta il valore della proprietà dataEmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataEmissione(DATATYPE value) {
            this.dataEmissione = value;
        }

        /**
         * Recupera il valore della proprietà annoOrdinanza.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getAnnoOrdinanza() {
            return annoOrdinanza;
        }

        /**
         * Imposta il valore della proprietà annoOrdinanza.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setAnnoOrdinanza(BigInteger value) {
            this.annoOrdinanza = value;
        }

        /**
         * Recupera il valore della proprietà numeroOrdinanza.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getNumeroOrdinanza() {
            return numeroOrdinanza;
        }

        /**
         * Imposta il valore della proprietà numeroOrdinanza.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setNumeroOrdinanza(BigInteger value) {
            this.numeroOrdinanza = value;
        }

        /**
         * Recupera il valore della proprietà dataDepositoCancelleria.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataDepositoCancelleria() {
            return dataDepositoCancelleria;
        }

        /**
         * Imposta il valore della proprietà dataDepositoCancelleria.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataDepositoCancelleria(DATATYPE value) {
            this.dataDepositoCancelleria = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrDecisione",
        "dataInizioPeriodo",
        "durataDetenzioneDomiciliare"
    })
    public static class DatiRevocaLiberazioneAnticipata {

        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataInizioPeriodo;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataInizioPeriodo.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataInizioPeriodo() {
            return dataInizioPeriodo;
        }

        /**
         * Imposta il valore della proprietà dataInizioPeriodo.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataInizioPeriodo(DATATYPE value) {
            this.dataInizioPeriodo = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrUfficioMagistratoCompetente",
        "descrDecisione",
        "licenzaPeriodiLibertaAnticipata"
    })
    public static class DatiRevocaLibertaAnticipata {

        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected List<PERIODILIBERTAANTICIPATATYPE> licenzaPeriodiLibertaAnticipata;

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Gets the value of the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLicenzaPeriodiLibertaAnticipata().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PERIODILIBERTAANTICIPATATYPE }
         * 
         * 
         */
        public List<PERIODILIBERTAANTICIPATATYPE> getLicenzaPeriodiLibertaAnticipata() {
            if (licenzaPeriodiLibertaAnticipata == null) {
                licenzaPeriodiLibertaAnticipata = new ArrayList<PERIODILIBERTAANTICIPATATYPE>();
            }
            return this.licenzaPeriodiLibertaAnticipata;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrizioneDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="DataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataTrasmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="descrUfficioMagistratoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataDecorrenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="durataArresto" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrizioneDecisione",
        "dataEmissione",
        "descrTipoUfficio",
        "descrUffTdsConcessoRiduzione",
        "dataTrasmissione",
        "descrUfficioMagistratoComp",
        "dataDecorrenza",
        "durataDetenzioneDomiciliare",
        "durataArresto"
    })
    public static class DatiRevocaMisuraAlternativa {

        @XmlElement(required = true)
        protected String descrizioneDecisione;
        @XmlElement(name = "DataEmissione", required = true)
        protected DATATYPE dataEmissione;
        @XmlElement(required = true)
        protected String descrTipoUfficio;
        @XmlElement(required = true)
        protected String descrUffTdsConcessoRiduzione;
        @XmlElement(required = true)
        protected DATATYPE dataTrasmissione;
        @XmlElement(required = true)
        protected String descrUfficioMagistratoComp;
        @XmlElement(required = true)
        protected DATATYPE dataDecorrenza;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;
        @XmlElement(required = true)
        protected DURATATYPE durataArresto;

        /**
         * Recupera il valore della proprietà descrizioneDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrizioneDecisione() {
            return descrizioneDecisione;
        }

        /**
         * Imposta il valore della proprietà descrizioneDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrizioneDecisione(String value) {
            this.descrizioneDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataEmissione.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataEmissione() {
            return dataEmissione;
        }

        /**
         * Imposta il valore della proprietà dataEmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataEmissione(DATATYPE value) {
            this.dataEmissione = value;
        }

        /**
         * Recupera il valore della proprietà descrTipoUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrTipoUfficio() {
            return descrTipoUfficio;
        }

        /**
         * Imposta il valore della proprietà descrTipoUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrTipoUfficio(String value) {
            this.descrTipoUfficio = value;
        }

        /**
         * Recupera il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUffTdsConcessoRiduzione() {
            return descrUffTdsConcessoRiduzione;
        }

        /**
         * Imposta il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUffTdsConcessoRiduzione(String value) {
            this.descrUffTdsConcessoRiduzione = value;
        }

        /**
         * Recupera il valore della proprietà dataTrasmissione.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataTrasmissione() {
            return dataTrasmissione;
        }

        /**
         * Imposta il valore della proprietà dataTrasmissione.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataTrasmissione(DATATYPE value) {
            this.dataTrasmissione = value;
        }

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoComp() {
            return descrUfficioMagistratoComp;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoComp(String value) {
            this.descrUfficioMagistratoComp = value;
        }

        /**
         * Recupera il valore della proprietà dataDecorrenza.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataDecorrenza() {
            return dataDecorrenza;
        }

        /**
         * Imposta il valore della proprietà dataDecorrenza.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataDecorrenza(DATATYPE value) {
            this.dataDecorrenza = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

        /**
         * Recupera il valore della proprietà durataArresto.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataArresto() {
            return durataArresto;
        }

        /**
         * Imposta il valore della proprietà durataArresto.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataArresto(DURATATYPE value) {
            this.durataArresto = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codiNaturaProvvedimento",
        "luogoSvolgimentoProva"
    })
    public static class DatiRicoveri {

        @XmlElement(required = true)
        protected String codiNaturaProvvedimento;
        @XmlElement(required = true)
        protected String luogoSvolgimentoProva;

        /**
         * Recupera il valore della proprietà codiNaturaProvvedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiNaturaProvvedimento() {
            return codiNaturaProvvedimento;
        }

        /**
         * Imposta il valore della proprietà codiNaturaProvvedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiNaturaProvvedimento(String value) {
            this.codiNaturaProvvedimento = value;
        }

        /**
         * Recupera il valore della proprietà luogoSvolgimentoProva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoSvolgimentoProva() {
            return luogoSvolgimentoProva;
        }

        /**
         * Imposta il valore della proprietà luogoSvolgimentoProva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoSvolgimentoProva(String value) {
            this.luogoSvolgimentoProva = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codiNaturaProvvedimento",
        "luogoSvolgimentoProva"
    })
    public static class DatiRicoveriOssPsich {

        @XmlElement(required = true)
        protected String codiNaturaProvvedimento;
        @XmlElement(required = true)
        protected String luogoSvolgimentoProva;

        /**
         * Recupera il valore della proprietà codiNaturaProvvedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiNaturaProvvedimento() {
            return codiNaturaProvvedimento;
        }

        /**
         * Imposta il valore della proprietà codiNaturaProvvedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiNaturaProvvedimento(String value) {
            this.codiNaturaProvvedimento = value;
        }

        /**
         * Recupera il valore della proprietà luogoSvolgimentoProva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoSvolgimentoProva() {
            return luogoSvolgimentoProva;
        }

        /**
         * Imposta il valore della proprietà luogoSvolgimentoProva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoSvolgimentoProva(String value) {
            this.luogoSvolgimentoProva = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="durataSospensione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataInizioPeriodo" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="dataFineMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "durataSospensione",
        "descrDecisione",
        "dataInizioPeriodo",
        "dataFineMisura"
    })
    public static class DatiRinvioSanzSost {

        @XmlElement(required = true)
        protected DURATATYPE durataSospensione;
        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected DATATYPE dataInizioPeriodo;
        @XmlElement(required = true)
        protected DATATYPE dataFineMisura;

        /**
         * Recupera il valore della proprietà durataSospensione.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataSospensione() {
            return durataSospensione;
        }

        /**
         * Imposta il valore della proprietà durataSospensione.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataSospensione(DURATATYPE value) {
            this.durataSospensione = value;
        }

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà dataInizioPeriodo.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataInizioPeriodo() {
            return dataInizioPeriodo;
        }

        /**
         * Imposta il valore della proprietà dataInizioPeriodo.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataInizioPeriodo(DATATYPE value) {
            this.dataInizioPeriodo = value;
        }

        /**
         * Recupera il valore della proprietà dataFineMisura.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataFineMisura() {
            return dataFineMisura;
        }

        /**
         * Imposta il valore della proprietà dataFineMisura.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataFineMisura(DATATYPE value) {
            this.dataFineMisura = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="durataDetenzioneDomiciliare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="desUfficioCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descrUfficioMagistratoCompetente",
        "descrDecisione",
        "codiNaturaProvvedimento",
        "durataDetenzioneDomiciliare",
        "desUfficioCompetente"
    })
    public static class DatiSanzioneSostitutiva {

        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected String codiNaturaProvvedimento;
        @XmlElement(required = true)
        protected DURATATYPE durataDetenzioneDomiciliare;
        @XmlElement(required = true)
        protected String desUfficioCompetente;

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Recupera il valore della proprietà codiNaturaProvvedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiNaturaProvvedimento() {
            return codiNaturaProvvedimento;
        }

        /**
         * Imposta il valore della proprietà codiNaturaProvvedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiNaturaProvvedimento(String value) {
            this.codiNaturaProvvedimento = value;
        }

        /**
         * Recupera il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataDetenzioneDomiciliare() {
            return durataDetenzioneDomiciliare;
        }

        /**
         * Imposta il valore della proprietà durataDetenzioneDomiciliare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataDetenzioneDomiciliare(DURATATYPE value) {
            this.durataDetenzioneDomiciliare = value;
        }

        /**
         * Recupera il valore della proprietà desUfficioCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDesUfficioCompetente() {
            return desUfficioCompetente;
        }

        /**
         * Imposta il valore della proprietà desUfficioCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDesUfficioCompetente(String value) {
            this.desUfficioCompetente = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="numeroGiorni" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "numeroGiorni"
    })
    public static class DatiScomputo {

        @XmlElement(required = true)
        protected BigInteger numeroGiorni;

        /**
         * Recupera il valore della proprietà numeroGiorni.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getNumeroGiorni() {
            return numeroGiorni;
        }

        /**
         * Imposta il valore della proprietà numeroGiorni.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setNumeroGiorni(BigInteger value) {
            this.numeroGiorni = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="codiTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="servizioTerapeuticoComp" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codiTipoUfficio",
        "codiNaturaProvvedimento",
        "descrUffTdsConcessoRiduzione",
        "luogoSvolgimentoProva",
        "servizioTerapeuticoComp"
    })
    public static class DatiSopravvenienzaNuovoTitolo {

        @XmlElement(required = true)
        protected String codiTipoUfficio;
        @XmlElement(required = true)
        protected String codiNaturaProvvedimento;
        @XmlElement(required = true)
        protected String descrUffTdsConcessoRiduzione;
        @XmlElement(required = true)
        protected String luogoSvolgimentoProva;
        @XmlElement(required = true)
        protected String servizioTerapeuticoComp;

        /**
         * Recupera il valore della proprietà codiTipoUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiTipoUfficio() {
            return codiTipoUfficio;
        }

        /**
         * Imposta il valore della proprietà codiTipoUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiTipoUfficio(String value) {
            this.codiTipoUfficio = value;
        }

        /**
         * Recupera il valore della proprietà codiNaturaProvvedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiNaturaProvvedimento() {
            return codiNaturaProvvedimento;
        }

        /**
         * Imposta il valore della proprietà codiNaturaProvvedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiNaturaProvvedimento(String value) {
            this.codiNaturaProvvedimento = value;
        }

        /**
         * Recupera il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUffTdsConcessoRiduzione() {
            return descrUffTdsConcessoRiduzione;
        }

        /**
         * Imposta il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUffTdsConcessoRiduzione(String value) {
            this.descrUffTdsConcessoRiduzione = value;
        }

        /**
         * Recupera il valore della proprietà luogoSvolgimentoProva.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLuogoSvolgimentoProva() {
            return luogoSvolgimentoProva;
        }

        /**
         * Imposta il valore della proprietà luogoSvolgimentoProva.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLuogoSvolgimentoProva(String value) {
            this.luogoSvolgimentoProva = value;
        }

        /**
         * Recupera il valore della proprietà servizioTerapeuticoComp.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getServizioTerapeuticoComp() {
            return servizioTerapeuticoComp;
        }

        /**
         * Imposta il valore della proprietà servizioTerapeuticoComp.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setServizioTerapeuticoComp(String value) {
            this.servizioTerapeuticoComp = value;
        }

    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="codTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiNaturaProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrUffTdsConcessoRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataSospensione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="durataSospensione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="dataScadenzaSospensioneSS" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
     *         &lt;element name="giorniRecuperoSS" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="durataSanzSostEspiata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *         &lt;element name="durataSanzSostiResiduaEspiare" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codTipoUfficio",
        "codiNaturaProvvedimento",
        "descrUffTdsConcessoRiduzione",
        "dataSospensione",
        "durataSospensione",
        "dataScadenzaSospensioneSS",
        "giorniRecuperoSS",
        "durataSanzSostEspiata",
        "durataSanzSostiResiduaEspiare"
    })
    public static class DatiSospEsecSanzSost {

        @XmlElement(required = true)
        protected String codTipoUfficio;
        @XmlElement(required = true)
        protected String codiNaturaProvvedimento;
        @XmlElement(required = true)
        protected String descrUffTdsConcessoRiduzione;
        @XmlElement(required = true)
        protected DATATYPE dataSospensione;
        @XmlElement(required = true)
        protected DURATATYPE durataSospensione;
        @XmlElement(required = true)
        protected DATATYPE dataScadenzaSospensioneSS;
        @XmlElement(required = true)
        protected String giorniRecuperoSS;
        @XmlElement(required = true)
        protected DURATATYPE durataSanzSostEspiata;
        @XmlElement(required = true)
        protected DURATATYPE durataSanzSostiResiduaEspiare;

        /**
         * Recupera il valore della proprietà codTipoUfficio.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodTipoUfficio() {
            return codTipoUfficio;
        }

        /**
         * Imposta il valore della proprietà codTipoUfficio.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodTipoUfficio(String value) {
            this.codTipoUfficio = value;
        }

        /**
         * Recupera il valore della proprietà codiNaturaProvvedimento.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodiNaturaProvvedimento() {
            return codiNaturaProvvedimento;
        }

        /**
         * Imposta il valore della proprietà codiNaturaProvvedimento.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodiNaturaProvvedimento(String value) {
            this.codiNaturaProvvedimento = value;
        }

        /**
         * Recupera il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUffTdsConcessoRiduzione() {
            return descrUffTdsConcessoRiduzione;
        }

        /**
         * Imposta il valore della proprietà descrUffTdsConcessoRiduzione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUffTdsConcessoRiduzione(String value) {
            this.descrUffTdsConcessoRiduzione = value;
        }

        /**
         * Recupera il valore della proprietà dataSospensione.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataSospensione() {
            return dataSospensione;
        }

        /**
         * Imposta il valore della proprietà dataSospensione.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataSospensione(DATATYPE value) {
            this.dataSospensione = value;
        }

        /**
         * Recupera il valore della proprietà durataSospensione.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataSospensione() {
            return durataSospensione;
        }

        /**
         * Imposta il valore della proprietà durataSospensione.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataSospensione(DURATATYPE value) {
            this.durataSospensione = value;
        }

        /**
         * Recupera il valore della proprietà dataScadenzaSospensioneSS.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataScadenzaSospensioneSS() {
            return dataScadenzaSospensioneSS;
        }

        /**
         * Imposta il valore della proprietà dataScadenzaSospensioneSS.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataScadenzaSospensioneSS(DATATYPE value) {
            this.dataScadenzaSospensioneSS = value;
        }

        /**
         * Recupera il valore della proprietà giorniRecuperoSS.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGiorniRecuperoSS() {
            return giorniRecuperoSS;
        }

        /**
         * Imposta il valore della proprietà giorniRecuperoSS.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGiorniRecuperoSS(String value) {
            this.giorniRecuperoSS = value;
        }

        /**
         * Recupera il valore della proprietà durataSanzSostEspiata.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataSanzSostEspiata() {
            return durataSanzSostEspiata;
        }

        /**
         * Imposta il valore della proprietà durataSanzSostEspiata.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataSanzSostEspiata(DURATATYPE value) {
            this.durataSanzSostEspiata = value;
        }

        /**
         * Recupera il valore della proprietà durataSanzSostiResiduaEspiare.
         * 
         * @return
         *     possible object is
         *     {@link DURATATYPE }
         *     
         */
        public DURATATYPE getDurataSanzSostiResiduaEspiare() {
            return durataSanzSostiResiduaEspiare;
        }

        /**
         * Imposta il valore della proprietà durataSanzSostiResiduaEspiare.
         * 
         * @param value
         *     allowed object is
         *     {@link DURATATYPE }
         *     
         */
        public void setDurataSanzSostiResiduaEspiare(DURATATYPE value) {
            this.durataSanzSostiResiduaEspiare = value;
        }

    }

}
