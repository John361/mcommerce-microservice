package com.clientui.proxies;

import com.clientui.beans.ProductBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "zuul-server")
@RibbonClient(name = "microservice-produits")
public interface MicroserviceProduitProxy {

    @GetMapping(value = "/microservice-produits/Produits")
    List<ProductBean> listeDesProduits();

    @GetMapping(value = "/microservice-produits/Produits/{id}")
    ProductBean recupererUnProduit(@PathVariable("id") int id);
}
