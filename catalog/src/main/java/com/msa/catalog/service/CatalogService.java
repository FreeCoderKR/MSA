package com.msa.catalog.service;

import com.msa.catalog.jpa.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
