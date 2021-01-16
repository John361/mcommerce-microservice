package com.clientui.controller;

import com.clientui.beans.CommandeBean;
import com.clientui.beans.PaiementBean;
import com.clientui.beans.ProductBean;
import com.clientui.proxies.MicroserviceCommandeProxy;
import com.clientui.proxies.MicroservicePaiementProxy;
import com.clientui.proxies.MicroserviceProduitProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceProduitProxy produitProxy;

    @Autowired
    private MicroservicePaiementProxy paiementProxy;

    @Autowired
    private MicroserviceCommandeProxy commandeProxy;

    @RequestMapping("/")
    public String accueil(Model model) {
        List<ProductBean> produits = this.produitProxy.listeDesProduits();
        model.addAttribute("produits", produits);

        return "accueil";
    }

    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable("id") int id, Model model) {
        ProductBean produit = this.produitProxy.recupererUnProduit(id);
        model.addAttribute("produit", produit);

        return "fiche-produit";
    }

    @RequestMapping("/commander-produit/{idProduit}/{montant}")
    public String passerCommande(@PathVariable("idProduit") int idProduit, @PathVariable("montant") Double montant, Model model) {
        CommandeBean commande = new CommandeBean();
        commande.setProductId(idProduit);
        commande.setQuantite(1);
        commande.setDateCommande(new Date());

        CommandeBean commandeAjoutee = this.commandeProxy.ajouterCommande(commande);

        model.addAttribute("commande", commandeAjoutee);
        model.addAttribute("montant", montant);

        return "paiement";
    }

    @RequestMapping("/payer-commande/{idCommande}/{montantCommande}")
    public String payerCommande(@PathVariable int idCommande, @PathVariable Double montantCommande, Model model){
        PaiementBean paiementAExcecuter = new PaiementBean();
        paiementAExcecuter.setIdCommande(idCommande);
        paiementAExcecuter.setMontant(montantCommande);
        paiementAExcecuter.setNumeroCarte(this.numcarte());

        ResponseEntity<PaiementBean> paiement = paiementProxy.payerUneCommande(paiementAExcecuter);
        boolean paiementAccepte = false;

        if (paiement.getStatusCode() == HttpStatus.CREATED)
            paiementAccepte = true;

        model.addAttribute("paiementOk", paiementAccepte);

        return "confirmation";
    }

    private Long numcarte() {
        return ThreadLocalRandom.current().nextLong(1000000000000000L,9000000000000000L );
    }
}
